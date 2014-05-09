package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.en.model.EntryModel;
import com.en.model.LabourBillModel;
import com.en.model.SalesModel;
import com.en.util.SalesDueDateComparator;
import com.en.util.Utils;

public class CollectionBroker extends BaseBroker {

	public CollectionBroker(Connection conn) {
		super(conn);
	}

	public SalesModel[] getOutstandingRpt(String customer,String[] branch, String userId) {
		ArrayList<SalesModel> outstandingBills = new ArrayList<SalesModel>(0);
		SalesModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, A.TAXTYPE, E.BILL_PREFIX, E.NAME, E.CITY AS BRANCHCITY, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, B.CONTACTPERSON, B.STDCODE, " +
				"B.PHONE1, B.PHONE2, B.MOBILE1, B.MOBILE2, A.PAYMODE, DATE_ADD(A.SALESDATE,INTERVAL A.CREDITDAYS DAY) AS DUEDATE, A.CREDITDAYS, B.COLLECTIONPERSON, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, D.USERNAME, A.PAYDATE, A.PAYAMT, A.ADJAMT, Z.ALERTDATE, Z.ALERTREMARK, A.CNT, Y.USERNAME AS FOLLOWUPUSER  " +
				"FROM SALES_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E, SALES A " +
				"LEFT OUTER JOIN SALES_ALERT Z ON A.BRANCHID = Z.BRANCHID AND A.SALESID = Z.SALESID AND A.CNT = Z.SNO  LEFT OUTER JOIN USER Y ON Z.USER = Y.USERID " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.PAYMODE = 3 ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.CUSTID = ? ";
		}
		if(branch!= null && branch.length!=0 && !branch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<branch.length; k++){
				outstandingBill	= outstandingBill +	"A.BRANCHID = ? ";
				if(k !=branch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(!userId.equals("--")){
			outstandingBill = outstandingBill + "AND A.SALESMANID = ? ";
		}

		outstandingBill	= outstandingBill +	"GROUP BY A.SALESID, A.BRANCHID " +
				 "HAVING (AMT + A.PACKING + A.FORWARD + A.INSTALLATION - A.LESS - A.PAYAMT) >= 1 ";
		outstandingBill	= outstandingBill +	"ORDER BY Z.ALERTDATE, DUEDATE, A.SALESDATE, A.BRANCHID, A.SALESID ";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(branch!= null && branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
					i++;
				}
			}
			if(!userId.equals("--")){
				stmt.setString(i, userId);
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new SalesModel();
				model.setSaleid(rs.getInt("SALESID"));
				model.setSalesdate(rs.getString("SALESDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setCity(rs.getString("BRANCHCITY"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getCustomer().setStdcode(rs.getString("STDCODE"));
				model.getCustomer().setPhone1(rs.getString("PHONE1"));
				model.getCustomer().setPhone2(rs.getString("PHONE2"));
				model.getCustomer().setMobile1(rs.getString("MOBILE1"));
				model.getCustomer().setMobile2(rs.getString("MOBILE2"));
				if(!rs.getString("COLLECTIONPERSON").equals("")){
					model.getCustomer().setCollectionPerson((new UserBroker(getConnection())).getUserDtls(rs.getString("COLLECTIONPERSON")));
				}
				model.setPaymode(rs.getInt("PAYMODE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setCreditDays(rs.getInt("CREDITDAYS"));
				model.setDueDate((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") : rs.getString("DUEDATE"));
				model.setPayDate(rs.getString("PAYDATE"));
				model.setPayAmt(rs.getDouble("PAYAMT"));
				model.getSalesman().setUserName(rs.getString("USERNAME"));
				model.setFollowupCnt(rs.getInt("CNT"));
				model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") :""); 
				model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
				model.setFollowupUser((rs.getString("FOLLOWUPUSER") != null) ? rs.getString("FOLLOWUPUSER") :""); 
				outstandingBills.add(model);
			}
			
			Collections.sort(outstandingBills, new SalesDueDateComparator());
			
			getConnection().commit();
			
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (SalesModel[]) outstandingBills.toArray(new SalesModel[0]);
	}

	public EntryModel[] getPaymentDetails(String billNo) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT A.BRANCH, A.DAY, B.TYPE, B.AMOUNT, B.CHQNO, B.BANKNAME, B.CUSTBANK, B.BRANCH AS BILLBRANCH, B.BILLNO " +
				"FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B " +
				"WHERE A.ID = B.ID " +
				"AND B.BRANCH = (SELECT C.ACCESSID FROM ACCESS_POINT C WHERE C.BILL_PREFIX = ?) " +
				"AND B.TYPE IN (31,32,41,42,43,44) " +
				"AND B.BILLNO = ? " +
				"ORDER BY A.DAY ";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setString(1, billNo.substring(0,3));
			stmt.setInt(2, Integer.parseInt(billNo.substring(3)));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("DAY"));
				entry.setBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
				entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BILLBRANCH")));
				entry.setBillNo(rs.getInt("BILLNO"));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setBankName(rs.getString("BANKNAME"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

	public void addFollowupRemarks(String[] billNos, String followupDt,
			String followupRemark, String userId) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		
		int idx = 1;
		int branchId = 0;
		
		// query to get the details
		String getMaxCount = "SELECT BRANCHID, CNT FROM SALES WHERE SALESID = ? AND BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) ";
		
		// query to update the count in sales
		String updateSalesCount = "UPDATE SALES SET CNT = CNT + 1 WHERE SALESID = ? AND BRANCHID = ? ";
		
		// query to insert the alert record 
		String insertFollowupRecord = "INSERT INTO SALES_ALERT (SALESID, BRANCHID, ALERTDATE, ALERTREMARK, SNO, USER) VALUES (?,?,?,?,?,?)";
		
		try{
		
			stmt = getConnection().prepareStatement(getMaxCount);
			stmt1 = getConnection().prepareStatement(updateSalesCount);
			stmt2 = getConnection().prepareStatement(insertFollowupRecord);
			
			for(int i=0; i<billNos.length; i++){
			
				stmt.setInt(1, Integer.parseInt(billNos[i].substring(3)));
				stmt.setString(2, billNos[i].substring(0,3));
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					idx = rs.getInt("CNT") + 1;
					branchId = rs.getInt("BRANCHID");
				}

				stmt1.setInt(1, Integer.parseInt(billNos[i].substring(3)));
				stmt1.setInt(2, branchId);
				
				stmt1.execute();
				
				stmt2.setInt(1, Integer.parseInt(billNos[i].substring(3)));
				stmt2.setInt(2, branchId);
				stmt2.setString(3, Utils.convertToSQLDate(followupDt));
				stmt2.setString(4, followupRemark);
				stmt2.setInt(5, idx);
				stmt2.setString(6, userId);
				
				stmt2.execute();
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
		}
		
		return;
	}

	public SalesModel[] getFollowupDetails(String billNo) {
		ArrayList<SalesModel> followupLst = new ArrayList<SalesModel>(0);
		SalesModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String followupDtls = "SELECT A.SNO, B.USERNAME, A.SALESID, C.BILL_PREFIX, A.ALERTDATE, A.ALERTREMARK FROM SALES_ALERT A, USER B, ACCESS_POINT C " +
				"WHERE A.USER = B.USERID " +
				"AND A.BRANCHID = C.ACCESSID " +
				"AND A.SALESID = ? " +
				"AND A.BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) " +
				"ORDER BY A.SNO";
		
		try{
			
			stmt = getConnection().prepareStatement(followupDtls);
			stmt.setString(1, billNo.substring(3));
			stmt.setString(2, billNo.substring(0,3));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
					model = new SalesModel();
					model.setSaleid(rs.getInt("SALESID"));
					model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
					model.setFollowupCnt(rs.getInt("SNO"));
					model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") :""); 
					model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
					model.setFollowupUser((rs.getString("USERNAME") != null) ? rs.getString("USERNAME") :""); 
					followupLst.add(model);
			}
			
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return (SalesModel[]) followupLst.toArray(new SalesModel[0]);
	}

	public LabourBillModel[] getOutstandingLabourBillRpt(String customer,String[] branch) {
		ArrayList<LabourBillModel> outstandingBills = new ArrayList<LabourBillModel>(0);
		LabourBillModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.BILLNO, A.BILLDATE, A.BRANCHID, E.BILL_PREFIX, E.NAME, E.CITY AS BRANCHCITY, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, B.CONTACTPERSON, B.STDCODE, " +
				"B.PHONE1, B.PHONE2, B.MOBILE1, B.MOBILE2, B.COLLECTIONPERSON, " +
				"A.AMOUNT AS AMT, D.USERNAME, A.PAYDATE, A.PAYAMT, A.ADJAMT " +
				"FROM  CUSTOMER B, USER D, ACCESS_POINT E, LABOUR_BILL A " +
				"WHERE A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.CUSTID = ? ";
		}
		if(branch!= null && branch.length!=0 && !branch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<branch.length; k++){
				outstandingBill	= outstandingBill +	"A.BRANCHID = ? ";
				if(k !=branch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}		

		outstandingBill	= outstandingBill +	"HAVING (AMT - A.PAYAMT + A.ADJAMT) >= 1 ";
		outstandingBill	= outstandingBill +	"ORDER BY A.BILLDATE, A.BRANCHID, A.BILLNO ";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(branch!= null && branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
					i++;
				}
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new LabourBillModel();
				model.setSaleid(rs.getInt("BILLNO"));
				model.setSalesdate(rs.getString("BILLDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setCity(rs.getString("BRANCHCITY"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getCustomer().setStdcode(rs.getString("STDCODE"));
				model.getCustomer().setPhone1(rs.getString("PHONE1"));
				model.getCustomer().setPhone2(rs.getString("PHONE2"));
				model.getCustomer().setMobile1(rs.getString("MOBILE1"));
				model.getCustomer().setMobile2(rs.getString("MOBILE2"));
				if(!rs.getString("COLLECTIONPERSON").equals("")){
					model.getCustomer().setCollectionPerson((new UserBroker(getConnection())).getUserDtls(rs.getString("COLLECTIONPERSON")));
				}
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPayDate(rs.getString("PAYDATE"));
				model.setPayAmt(rs.getDouble("PAYAMT"));
				model.getSalesman().setUserName(rs.getString("USERNAME"));
				outstandingBills.add(model);
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (LabourBillModel[]) outstandingBills.toArray(new LabourBillModel[0]);
	}

	public EntryModel[] getAdjustDetails(String billNo) {
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
				"FROM BILL_TO_BILL_ADJUST WHERE TONO = ? AND TOBRANCH = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) ORDER BY ADJUSTDATE";
		
		try{
			
			stmt = getConnection().prepareStatement(getBillPrefix);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				billPrefixes.put(rs.getInt("ACCESSID"), rs.getString("BILL_PREFIX"));
			}
			
			stmt = getConnection().prepareStatement(getPendingAdvance);
			stmt.setString(1, billNo.substring(3));
			stmt.setString(2, billNo.substring(0,3));
			
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

}
