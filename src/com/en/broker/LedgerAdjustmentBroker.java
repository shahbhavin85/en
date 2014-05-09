package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.Utils;

public class LedgerAdjustmentBroker extends BaseBroker {

	public LedgerAdjustmentBroker(Connection conn) {
		super(conn);
	}

	public EntryModel[] getOrderAdvanceModel(String custId) {
		ArrayList<EntryModel> entries = new ArrayList<EntryModel>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EntryModel model = null;
		
		// query to get the order advance 
		String getPendingAdvance = "SELECT A.BILLNO, A.BRANCH, D.BILL_PREFIX, B.CUSTOMERNAME, B.AREA, B.CITY, SUM(A.AMOUNT) AS AMT, COALESCE(D.PAMT,0) AS PAYAMT "
				+ " FROM CUSTOMER B, ACCESS_POINT D, BRANCH_DAY_BOOK_ENTRIES A "
				+ " LEFT OUTER JOIN (SELECT C.FROMNO, C.FROMBRANCH, SUM(C.AMOUNT) AS PAMT FROM BILL_TO_BILL_ADJUST C WHERE C.TYPE = 1 GROUP BY C.FROMBRANCH, C.FROMNO) D "
				+ " ON A.BILLNO = D.FROMNO AND A.BRANCH = D.FROMBRANCH "
				+ " WHERE A.CUSTOMER = B.CUSTOMERID "
				+ " AND A.TYPE IN (1,2,3,4) "
				+ " AND A.BRANCH = D.ACCESSID "
				+ " AND A.BILLNO > 0 "
				+ " AND B.CUSTOMERID = ? "
				+ " GROUP BY A.BILLNO, A.BRANCH "
				+ " ORDER BY A.BILLNO, A.BRANCH";
		
		try{
			
			stmt = getConnection().prepareStatement(getPendingAdvance);
			stmt.setString(1, custId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(rs.getDouble("AMT") - rs.getDouble("PAYAMT") > 0){
					model = new EntryModel();
					model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
					model.getCustomer().setArea(rs.getString("AREA"));
					model.getCustomer().setCity(rs.getString("CITY"));
					model.setBillNo(rs.getInt("BILLNO"));
					model.getBillBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
					model.getBillBranch().setAccessId(rs.getInt("BRANCH"));
					model.setAmount(rs.getDouble("AMT")-rs.getDouble("PAYAMT"));
					entries.add(model);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				getConnection().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return (EntryModel[]) entries.toArray(new EntryModel[0]);
	}

	public double getOrderAdvance(String billNo) {
		double advance = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the order advance 
		String getPendingAdvance = "SELECT A.BILLNO, A.BRANCH, B.CUSTOMERNAME, B.AREA, B.CITY, SUM(A.AMOUNT) AS AMT, COALESCE(D.PAMT,0) AS PAYAMT "
				+ " FROM CUSTOMER B, BRANCH_DAY_BOOK_ENTRIES A "
				+ " LEFT OUTER JOIN (SELECT C.FROMNO, C.FROMBRANCH, SUM(C.AMOUNT) AS PAMT FROM BILL_TO_BILL_ADJUST C WHERE C.TYPE = 1 GROUP BY C.FROMBRANCH, C.FROMNO) D "
				+ " ON A.BILLNO = D.FROMNO AND A.BRANCH = D.FROMBRANCH "
				+ " WHERE A.CUSTOMER = B.CUSTOMERID "
				+ " AND A.TYPE IN (1,2,3,4) "
				+ " AND A.BRANCH = (SELECT K.ACCESSID FROM ACCESS_POINT K WHERE K.BILL_PREFIX= ?) "
				+ " AND A.BILLNO = ? "
				+ " GROUP BY A.BILLNO, A.BRANCH "
				+ " ORDER BY A.BILLNO, A.BRANCH";
		
		try{
			
			stmt = getConnection().prepareStatement(getPendingAdvance);
			stmt.setString(1, billNo.substring(0,3));
			stmt.setString(2, billNo.substring(4));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(rs.getDouble("AMT") - rs.getDouble("PAYAMT") > 0){
					advance = (rs.getDouble("AMT")-rs.getDouble("PAYAMT"));
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				getConnection().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return advance;
	}

	public MessageModel adjustOrderAdvanceBillPending(String orderNo, EntryModel[] billEntry, String custId) {
		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		int idx = 1;
		int branchId = 0;
		
		// query to get the branchid 
		String getBranchId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?";
		
		// query to get the max idx
		String getMaxIdx = "SELECT MAX(IDX) FROM BILL_TO_BILL_ADJUST";
		
		// query to insert the entry
		String insertDetails = "INSERT INTO BILL_TO_BILL_ADJUST (TYPE, FROMNO, FROMBRANCH, TONO, TOBRANCH, AMOUNT, ADJAMT, IDX, ADJUSTDATE, CUSTID) VALUES (?,?,?,?,?,?,?,?,CURRENT_DATE,?)";
		
		// query to update sales table
		String updateSales = "UPDATE SALES SET PAYAMT = PAYAMT + ? , PAYDATE = CURRENT_DATE, ADJAMT = ADJAMT + ? WHERE SALESID = ? AND BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getBranchId);
			stmt.setString(1, orderNo.substring(0,3));
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				branchId = rs.getInt("ACCESSID");
			}
			
			stmt = getConnection().prepareStatement(getMaxIdx);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				idx = rs.getInt(1)+1;
			}
			
			stmt = getConnection().prepareStatement(insertDetails);
			stmt1 = getConnection().prepareStatement(updateSales);
			
			for(int i=0; i<billEntry.length; i++){
				stmt.setInt(1, 1);
				stmt.setString(2, orderNo.substring(5));
				stmt.setInt(3, branchId);
				stmt.setInt(4, billEntry[i].getBillNo());
				stmt.setInt(5, billEntry[i].getBillBranch().getAccessId());
				stmt.setDouble(6, billEntry[i].getAmount());
				stmt.setDouble(7, billEntry[i].getAdjAmt());
				stmt.setInt(8, idx);
				stmt.setString(9, custId);
				stmt.addBatch();
				
				stmt1.setDouble(1, billEntry[i].getAmount());				
				stmt1.setDouble(2, billEntry[i].getAdjAmt());
				stmt1.setInt(3, billEntry[i].getBillNo());
				stmt1.setInt(4, billEntry[i].getBillBranch().getAccessId());
				stmt1.addBatch();
				
				idx++;
				msg.addNewMessage(new Message(ALERT, "Adjusted Bill No:" +billEntry[i].getBillBranch().getBillPrefix()+Utils.padBillNo(billEntry[i].getBillNo())+" with Amount Rs. "+billEntry[i].getAmount()));
			}
			
			stmt.executeBatch();
			stmt1.executeBatch();
			
		} catch (Exception e) {
			msg.addNewMessage(new Message(ERROR, e.getMessage()));
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
		} finally {
			try{
				getConnection().commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return msg;
	}

	public EntryModel[] getAdjustmentEntries(String custId) {
		ArrayList<EntryModel> entries = new ArrayList<EntryModel>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EntryModel model = null;
		HashMap<Integer, String> billPrefixes = new HashMap<Integer, String>(0);
		
		// query to get the order advance 
		String getBillPrefix = "SELECT BILL_PREFIX, ACCESSID " +
				"FROM ACCESS_POINT WHERE ACCESSID > 10";
		
		// query to get the order advance 
		String getPendingAdvance = "SELECT TYPE, FROMNO, FROMBRANCH, TONO, TOBRANCH, AMOUNT, ADJAMT, IDX, ADJUSTDATE " +
				"FROM BILL_TO_BILL_ADJUST WHERE CUSTID = ? ORDER BY ADJUSTDATE";
		
		try{
			
			stmt = getConnection().prepareStatement(getBillPrefix);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				billPrefixes.put(rs.getInt("ACCESSID"), rs.getString("BILL_PREFIX"));
			}
			
			stmt = getConnection().prepareStatement(getPendingAdvance);
			stmt.setString(1, custId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new EntryModel();
				model.setRemark("Adjusted From "+billPrefixes.get(rs.getInt("FROMBRANCH"))+"O"+Utils.padBillNo(rs.getInt("FROMNO"))+" To "+billPrefixes.get(rs.getInt("TOBRANCH"))+Utils.padBillNo(rs.getInt("TONO")));
				model.setAmount(rs.getDouble("AMOUNT"));
				model.setAdjAmt(rs.getDouble("ADJAMT"));
				model.setEntryType(rs.getInt("TYPE"));
				model.setEntryDate(rs.getString("ADJUSTDATE"));
				model.setEntryId(rs.getInt("IDX"));
				entries.add(model);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				getConnection().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return (EntryModel[]) entries.toArray(new EntryModel[0]);
	}

	public MessageModel deleteAdjustment(String idx) {
		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int branchId = 0;
		int billNo = 0;
		double amt = 0;
		double adj = 0;
		String payDate = "0000-00-00";
		
		// query to get the branchid 
		String getEntryDetails = "SELECT TONO, TOBRANCH, AMOUNT, ADJAMT FROM BILL_TO_BILL_ADJUST  WHERE IDX = ?";
		
		// query to get the max idx
		String deleteEntry = "DELETE FROM BILL_TO_BILL_ADJUST WHERE IDX = ?";
		
		// query to update sales table
		String updateSales = "UPDATE SALES SET PAYAMT = PAYAMT - ?, PAYDATE = ?, ADJAMT = ADJAMT - ? WHERE SALESID = ? AND BRANCHID = ?";
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE, SUM(B.AMOUNT) AS PAYAMT FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (41,42,43,44)";
		
		// 	query to get the max ajdust date
		String getAdjustDate = "SELECT MAX(ADJUSTDATE) AS PAYDATE FROM BILL_TO_BILL_ADJUST WHERE TONO = ? AND TOBRANCH = ? AND TYPE = 1";
		
		try{
			
			stmt = getConnection().prepareStatement(getEntryDetails);
			stmt.setString(1, idx);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				branchId = rs.getInt("TOBRANCH");
				billNo = rs.getInt("TONO");
				amt = rs.getDouble("AMOUNT");
				adj = rs.getDouble("ADJAMT");
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, idx);
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(getPaymentDtls);
			stmt.setInt(1, branchId);
			stmt.setInt(2, billNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				if(rs.getString("PAYDATE") != null){
					payDate = rs.getString("PAYDATE");
	//				payAmt = rs.getDouble("PAYAMT");
				}
			}
				
			stmt = getConnection().prepareStatement(getAdjustDate);
			stmt.setInt(1, branchId);
			stmt.setInt(2, billNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				if(rs.getString("PAYDATE") != null){
					payDate = rs.getString("PAYDATE");
	//				payAmt = rs.getDouble("PAYAMT");
				}
			}
			
			stmt = getConnection().prepareStatement(updateSales);
				
			stmt.setDouble(1, amt);	
			stmt.setString(2, payDate);
			stmt.setDouble(3, adj);
			stmt.setInt(4, billNo);
			stmt.setInt(5, branchId);
			stmt.execute();
						
		} catch (Exception e) {
			msg.addNewMessage(new Message(ERROR, e.getMessage()));
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
		} finally {
			try{
				getConnection().commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return msg;
	}

}
