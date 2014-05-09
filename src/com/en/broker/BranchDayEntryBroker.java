package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.en.model.AccessPointModel;
import com.en.model.DayBookEntryModel;
import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.PurchaseModel;
import com.en.model.UserModel;
import com.en.util.OrderAlertDateComparator;
import com.en.util.PurchaseRecdDateComparator;
import com.en.util.Utils;

public class BranchDayEntryBroker extends BaseBroker {

	public BranchDayEntryBroker(Connection conn) {
		super(conn);
	}

	public DayBookEntryModel getBranchDayEntries(String accessId, String date) {
		DayBookEntryModel model = new DayBookEntryModel();
		model.setEntryDate(date);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		long rptId = 0;		
		boolean isPresent = false;
		
		// query to get the master details
		String getMasterDetails = "SELECT ID, BRANCH, DAY, ADDDATE(DAY, 1) AS NEXTDAY, SUBDATE(DAY,1) AS PREVDAY, ISAPPROVED, APPROVEDBY FROM BRANCH_DAY_BOOK_MASTER " +
				"WHERE BRANCH = ? AND DAY = ?";
		
		// query to get the details
		String getItemList = "SELECT ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, REMARK, STAFF, EDITABLE, BILLNO, BRANCH, PAYMODE, USER FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? ORDER BY ENTRYID";
		
		// query to get customer name
		String getCustomerDtls = "SELECT CUSTOMERNAME, AREA, CITY FROM CUSTOMER WHERE CUSTOMERID = ?";
		
		// query to get the cr balance
		String getCreditCashBalanace = "SELECT SUM(AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES WHERE CRDR = 'C' AND ISCASH = 1 AND ID IN (SELECT A.ID FROM BRANCH_DAY_BOOK_MASTER A WHERE A.BRANCH = ? AND A.DAY < ?)";
		
		// query to get the dr balance
		String getDebitCashBalanace = "SELECT SUM(AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES WHERE CRDR = 'D' AND ISCASH = 1 AND ID IN (SELECT A.ID FROM BRANCH_DAY_BOOK_MASTER A WHERE A.BRANCH = ? AND A.DAY < ?)";
		
		try{
			
			stmt = getConnection().prepareStatement(getMasterDetails);
			stmt.setString(1, accessId);
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				isPresent = true;
				rptId = rs.getLong("ID");
				model.setId(rs.getLong("ID"));	
				model.setNextDate(rs.getString("NEXTDAY"));
				model.setPreviousDate(rs.getString("PREVDAY"));
				model.setStatus(rs.getString("ISAPPROVED"));
				model.getApprover().setUserName(rs.getString("APPROVEDBY"));
				model.getBranch().setAccessId(rs.getInt("BRANCH"));
			} else {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date ndate = formatter.parse(Utils.convertToSQLDate(date));
				Date pDate = formatter.parse(Utils.convertToSQLDate(date));
				ndate.setTime( ndate.getTime() + 1*1000*60*60*24 );
				pDate.setTime( pDate.getTime() - 1*1000*60*60*24 );
				model.setNextDate(formatter.format(ndate));
				model.setPreviousDate(formatter.format(pDate));
			}
			
			if(isPresent){
				stmt = getConnection().prepareStatement(getItemList);
				stmt.setLong(1, rptId);
				stmt1 = getConnection().prepareStatement(getCustomerDtls);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					entry = new EntryModel();
					entry.setEntryId(rs.getInt("ENTRYID"));
					entry.setEntryType(rs.getInt("TYPE"));
					entry.setCash((rs.getInt("ISCASH") == 1) ? true : false);
					entry.setCrdr(rs.getString("CRDR"));
					entry.setAmount(rs.getDouble("AMOUNT"));
					entry.setBankName(rs.getString("BANKNAME"));
					entry.setRemark(rs.getString("REMARK"));
					entry.setUser(rs.getString("USER"));
					entry.setPayMode(rs.getInt("PAYMODE"));
					if(rs.getString("EDITABLE").equals("N")){
						entry.setEditable();
					}
					if(rs.getString("TYPE").equals("1") || rs.getString("TYPE").equals("2") || rs.getString("TYPE").equals("3") || rs.getString("TYPE").equals("4")|| rs.getString("TYPE").equals("31") || rs.getString("TYPE").equals("32") || 
							rs.getString("TYPE").equals("21") || rs.getString("TYPE").equals("22") || rs.getString("TYPE").equals("23") || rs.getString("TYPE").equals("24")||
							rs.getString("TYPE").equals("41") || rs.getString("TYPE").equals("42") || rs.getString("TYPE").equals("43") || rs.getString("TYPE").equals("44")|| rs.getString("TYPE").equals("55") || 
							rs.getString("TYPE").equals("46") || rs.getString("TYPE").equals("47") || rs.getString("TYPE").equals("48") || rs.getString("TYPE").equals("49")||
							rs.getString("TYPE").equals("71") || rs.getString("TYPE").equals("72") || rs.getString("TYPE").equals("73") || 
							rs.getString("TYPE").equals("81") || rs.getString("TYPE").equals("11") || (rs.getInt("TYPE") > 50 && rs.getInt("CUSTOMER") !=0)){
						stmt1.setString(1, rs.getString("CUSTOMER"));
						
						rs1 = stmt1.executeQuery();
						
						if(rs1.next()){
							entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
							entry.getCustomer().setCustomerName(rs1.getString("CUSTOMERNAME"));
							entry.getCustomer().setArea(rs1.getString("AREA"));
							entry.getCustomer().setCity(rs1.getString("CITY"));
						}
						entry.setChqNo(rs.getString("CHQNO"));
						entry.setChqDt(rs.getString("CHQDT"));
						entry.setCustBankName(rs.getString("CUSTBANK"));
						
						if(rs.getString("TYPE").equals("41") || rs.getString("TYPE").equals("42") || rs.getString("TYPE").equals("43") || rs.getString("TYPE").equals("44")
								|| rs.getString("TYPE").equals("46") || rs.getString("TYPE").equals("47") || rs.getString("TYPE").equals("48") || rs.getString("TYPE").equals("49")
								|| rs.getString("TYPE").equals("1") || rs.getString("TYPE").equals("2") || rs.getString("TYPE").equals("3") || rs.getString("TYPE").equals("4")){
							entry.setBillNo(rs.getInt("BILLNo"));
							entry.setBillBranch((new AccessPointBroker(getConnection()).getAccessPointDtls(rs.getInt("BRANCH"))));
						}
						
						if(rs.getString("TYPE").equals("71") || rs.getString("TYPE").equals("72") || rs.getString("TYPE").equals("73")){
							entry.setBillNo(rs.getInt("BILLNo"));
							entry.setBillBranch((new AccessPointBroker(getConnection()).getAccessPointDtls(rs.getInt("BRANCH"))));
						}
					}
					if(rs.getString("TYPE").equals("5") || rs.getString("TYPE").equals("60")){
						if(rs.getInt("BRANCH") == 2){
							entry.getBillBranch().setAccessId(2);
						} else if(rs.getInt("BRANCH") == 3){
							entry.getBillBranch().setAccessId(3);
						} else { 
							entry.setBillBranch((new AccessPointBroker(getConnection()).getAccessPointDtls(rs.getInt("BRANCH"))));
						}	
					}
					if(rs.getString("TYPE").equals("10") || rs.getString("TYPE").equals("58") || rs.getString("TYPE").equals("59")){
						entry.setStaff((new UserBroker(getConnection()).getUserDtls(rs.getString("STAFF"))));
					}
					if(rs.getInt("PAYMODE") == 1){
						entry.setChqNo(rs.getString("CHQNO"));
						entry.setChqDt(rs.getString("CHQDT"));
					}
					model.addEntry(entry);
				}
			}
			
			stmt = getConnection().prepareStatement(getCreditCashBalanace);
			stmt.setString(1, accessId);
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setOpeningBal(rs.getDouble(1));
			}
			
			stmt = getConnection().prepareStatement(getDebitCashBalanace);
			stmt.setString(1, accessId);
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setOpeningBal(model.getOpeningBal() - rs.getDouble(1));
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return model;
	}

	public DayBookEntryModel getBranchDayEntries(long rptId) {
		DayBookEntryModel model = null;
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		boolean isPresent = false;
		
		// query to get the master details
		String getMasterDetails = "SELECT A.ID, A.BRANCH, A.DAY, A.ISAPPROVED, A.APPROVEDBY, B.NAME, B.CITY FROM BRANCH_DAY_BOOK_MASTER A, ACCESS_POINT B " +
				"WHERE A.ID = ? AND A.BRANCH = B.ACCESSID";

		// query to get the cr balance
		String getCreditCashBalanace = "SELECT SUM(AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES WHERE CRDR = 'C' AND ISCASH = 1 AND ID IN (SELECT A.ID FROM BRANCH_DAY_BOOK_MASTER A WHERE A.BRANCH = ? AND A.DAY < ?)";
		
		// query to get the dr balance
		String getDebitCashBalanace = "SELECT SUM(AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES WHERE CRDR = 'D' AND ISCASH = 1 AND ID IN (SELECT A.ID FROM BRANCH_DAY_BOOK_MASTER A WHERE A.BRANCH = ? AND A.DAY < ?)";
		
		// query to get the details
		String getItemList = "SELECT ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, STAFF, REMARK, EDITABLE, BILLNO, BRANCH, PAYMODE, USER FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? ORDER BY ENTRYID";
		
		// query to get customer name
		String getCustomerDtls = "SELECT CUSTOMERNAME, AREA, CITY FROM CUSTOMER WHERE CUSTOMERID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getMasterDetails);
			stmt.setLong(1, rptId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model = new DayBookEntryModel();
				isPresent = true;
				rptId = rs.getLong("ID");
				model.setId(rs.getLong("ID"));	
				model.getBranch().setAccessId(rs.getInt("BRANCH"));
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setCity(rs.getString("CITY"));
				model.setEntryDate(rs.getString("DAY"));
				model.setStatus(rs.getString("ISAPPROVED"));
				model.getApprover().setUserName(rs.getString("APPROVEDBY"));
			}
			
			if(isPresent){
				stmt = getConnection().prepareStatement(getItemList);
				stmt.setLong(1, rptId);
				stmt1 = getConnection().prepareStatement(getCustomerDtls);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					entry = new EntryModel();
					entry.setEntryId(rs.getInt("ENTRYID"));
					entry.setEntryType(rs.getInt("TYPE"));
					entry.setCash((rs.getInt("ISCASH") == 1) ? true : false);
					entry.setCrdr(rs.getString("CRDR"));
					entry.setAmount(rs.getDouble("AMOUNT"));
					entry.setBankName(rs.getString("BANKNAME"));
					entry.setRemark(rs.getString("REMARK"));
					entry.setPayMode(rs.getInt("PAYMODE"));
					entry.setUser(rs.getString("USER"));
					if(rs.getString("EDITABLE").equals("N")){
						entry.setEditable();
					}
					if(rs.getString("TYPE").equals("1") || rs.getString("TYPE").equals("2") || rs.getString("TYPE").equals("3") || rs.getString("TYPE").equals("4")|| rs.getString("TYPE").equals("31") || rs.getString("TYPE").equals("32") || 
							rs.getString("TYPE").equals("21") || rs.getString("TYPE").equals("22") || rs.getString("TYPE").equals("23") || rs.getString("TYPE").equals("24")||
							rs.getString("TYPE").equals("41") || rs.getString("TYPE").equals("42") || rs.getString("TYPE").equals("43") || rs.getString("TYPE").equals("44")|| rs.getString("TYPE").equals("55") || 
							rs.getString("TYPE").equals("46") || rs.getString("TYPE").equals("47") || rs.getString("TYPE").equals("48") || rs.getString("TYPE").equals("49")||
							rs.getString("TYPE").equals("71") || rs.getString("TYPE").equals("72") || rs.getString("TYPE").equals("73") || 
							rs.getString("TYPE").equals("81") || rs.getString("TYPE").equals("11") || (rs.getInt("TYPE") > 50 && rs.getInt("CUSTOMER") !=0)){
						stmt1.setString(1, rs.getString("CUSTOMER"));
						
						rs1 = stmt1.executeQuery();
						
						if(rs1.next()){
							entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
							entry.getCustomer().setCustomerName(rs1.getString("CUSTOMERNAME"));
							entry.getCustomer().setArea(rs1.getString("AREA"));
							entry.getCustomer().setCity(rs1.getString("CITY"));
						}
						entry.setChqNo(rs.getString("CHQNO"));
						entry.setChqDt(rs.getString("CHQDT"));
						entry.setCustBankName(rs.getString("CUSTBANK"));
						
						if(rs.getString("TYPE").equals("41") || rs.getString("TYPE").equals("42") || rs.getString("TYPE").equals("43") || rs.getString("TYPE").equals("44")
								|| rs.getString("TYPE").equals("46") || rs.getString("TYPE").equals("47") || rs.getString("TYPE").equals("48") || rs.getString("TYPE").equals("49")
								|| rs.getString("TYPE").equals("1") || rs.getString("TYPE").equals("2") || rs.getString("TYPE").equals("3") || rs.getString("TYPE").equals("4")){
							entry.setBillNo(rs.getInt("BILLNo"));
							entry.setBillBranch((new AccessPointBroker(getConnection()).getAccessPointDtls(rs.getInt("BRANCH"))));
						}
						
						if(rs.getString("TYPE").equals("71") || rs.getString("TYPE").equals("72") || rs.getString("TYPE").equals("73")){
							entry.setBillNo(rs.getInt("BILLNo"));
							entry.setBillBranch((new AccessPointBroker(getConnection()).getAccessPointDtls(rs.getInt("BRANCH"))));
						}
					}
					if(rs.getString("TYPE").equals("5") || rs.getString("TYPE").equals("60")){
						if(rs.getInt("BRANCH") == 2){
							entry.getBillBranch().setAccessId(2);
						} else if(rs.getInt("BRANCH") == 3){
							entry.getBillBranch().setAccessId(3);
						} else { 
							entry.setBillBranch((new AccessPointBroker(getConnection()).getAccessPointDtls(rs.getInt("BRANCH"))));
						}	
					}
					if(rs.getString("TYPE").equals("10") || rs.getString("TYPE").equals("58") || rs.getString("TYPE").equals("59")){
						entry.setStaff((new UserBroker(getConnection()).getUserDtls(rs.getString("STAFF"))));
					}
					if(rs.getInt("PAYMODE") == 1){
						entry.setChqNo(rs.getString("CHQNO"));
						entry.setChqDt(rs.getString("CHQDT"));
					}
					model.addEntry(entry);
				}
				
				stmt = getConnection().prepareStatement(getCreditCashBalanace);
				stmt.setInt(1, model.getBranch().getAccessId());
				stmt.setString(2, model.getEntryDate());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					model.setOpeningBal(rs.getDouble(1));
				}
				
				stmt = getConnection().prepareStatement(getDebitCashBalanace);
				stmt.setInt(1, model.getBranch().getAccessId());
				stmt.setString(2, model.getEntryDate());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					model.setOpeningBal(model.getOpeningBal() - rs.getDouble(1));
				}
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return model;
	}

	@SuppressWarnings("resource")
	public MessageModel addEntry(EntryModel entry) {
		MessageModel model = new MessageModel();
//		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long id = 1;
		long refNo = 1;
		boolean isMasterPresent = false;
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";
		
		// query to get max id
		String getRefNo = "SELECT MAX(REFNO) FROM BRANCH_DAY_BOOK_ENTRIES";

		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, REMARK, BRANCH, STAFF, REFNO, PAYMODE, USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, entry.getBranchId());
			stmt.setString(2, entry.getEntryDate());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			stmt = getConnection().prepareStatement(getRefNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1)>0){
				refNo = rs.getLong(1) + 1;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, entry.getBranchId());
				stmt.setString(3, entry.getEntryDate());
				
				stmt.executeUpdate();
				
				stmt = getConnection().prepareStatement(insertEntryDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, 1);
				stmt.setInt(3, entry.getEntryType());
				stmt.setString(4, entry.isCash() ? "1" : "0");
				stmt.setString(5, entry.getCrdr());
				stmt.setDouble(6, entry.getAmount());
				stmt.setString(7, entry.getChqNo());
				stmt.setString(8, entry.getChqDt());
				stmt.setString(9, entry.getBankName());
				stmt.setInt(10, entry.getCustomer().getCustomerId());
				stmt.setString(11, entry.getCustBankName());
				stmt.setString(12, entry.getRemark());
				stmt.setInt(13, entry.getBranch().getAccessId());
				stmt.setString(14, entry.getStaff().getUserId());
				stmt.setLong(15, refNo);
				stmt.setInt(16, entry.getPayMode());
				stmt.setString(17, entry.getUser());
				
				stmt.executeUpdate();
				
				if(entry.getEntryType() == 60){
					addReverseEntry(entry, refNo);
				}
				
			} else {
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					
					stmt = getConnection().prepareStatement(insertEntryDtls);
					stmt.setLong(1, id);
					stmt.setInt(2, idx);
					stmt.setInt(3, entry.getEntryType());
					stmt.setString(4, entry.isCash() ? "1" : "0");
					stmt.setString(5, entry.getCrdr());
					stmt.setDouble(6, entry.getAmount());
					stmt.setString(7, entry.getChqNo());
					stmt.setString(8, entry.getChqDt());
					stmt.setString(9, entry.getBankName());
					stmt.setInt(10, entry.getCustomer().getCustomerId());
					stmt.setString(11, entry.getCustBankName());
					stmt.setString(12, entry.getRemark());
					stmt.setInt(13, entry.getBranch().getAccessId());
					stmt.setString(14, entry.getStaff().getUserId());
					stmt.setLong(15, refNo);
					stmt.setInt(16, entry.getPayMode());
					stmt.setString(17, entry.getUser());
					
					stmt.executeUpdate();
					
					if(entry.getEntryType() == 60){
						addReverseEntry(entry, refNo);
					}
					
					
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	@SuppressWarnings("resource")
	private void addReverseEntry(EntryModel entry, long refNo) {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long id = 1;
		boolean isMasterPresent = false;
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";
		
		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, REMARK, BRANCH, STAFF, REFNO, EDITABLE, USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'N',?)";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, entry.getBranch().getAccessId());
			stmt.setString(2, entry.getEntryDate());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, entry.getBranch().getAccessId());
				stmt.setString(3, entry.getEntryDate());
				
				stmt.executeUpdate();
				
				stmt = getConnection().prepareStatement(insertEntryDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, 1);
				stmt.setInt(3, 5);
				stmt.setString(4, entry.isCash() ? "1" : "0");
				stmt.setString(5, "C");
				stmt.setDouble(6, entry.getAmount());
				stmt.setString(7, entry.getChqNo());
				stmt.setString(8, entry.getChqDt());
				stmt.setString(9, entry.getBankName());
				stmt.setInt(10, entry.getCustomer().getCustomerId());
				stmt.setString(11, entry.getCustBankName());
				stmt.setString(12, entry.getRemark());
				stmt.setInt(13, entry.getBranchId());
				stmt.setString(14, entry.getStaff().getUserId());
				stmt.setLong(15, refNo);
				stmt.setString(16, entry.getUser());
				
				stmt.executeUpdate();
				
			/*	msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);*/
				
			} else {
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					
					stmt = getConnection().prepareStatement(insertEntryDtls);
					stmt.setLong(1, id);
					stmt.setInt(2, idx);
					stmt.setInt(3, 5);
					stmt.setString(4, entry.isCash() ? "1" : "0");
					stmt.setString(5, "C");
					stmt.setDouble(6, entry.getAmount());
					stmt.setString(7, entry.getChqNo());
					stmt.setString(8, entry.getChqDt());
					stmt.setString(9, entry.getBankName());
					stmt.setInt(10, entry.getCustomer().getCustomerId());
					stmt.setString(11, entry.getCustBankName());
					stmt.setString(12, entry.getRemark());
					stmt.setInt(13, entry.getBranchId());
					stmt.setString(14, entry.getStaff().getUserId());
					stmt.setLong(15, refNo);
					stmt.setString(16, entry.getUser());
					
					stmt.executeUpdate();
					
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public MessageModel delEntry(String id, String entryId) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		int type = 0;
		int refNo = 0;
		
		// query to get rpt id
		String deleteEntry = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? AND ENTRYID = ?";
		
		// query to get rpt id
		String getTypeRefNo = "SELECT REFNO, TYPE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? AND ENTRYID = ?";
		
		// query to get rpt id
		String deleteReverseEntry = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE TYPE = 5 AND REFNO = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getTypeRefNo);
			stmt.setString(1, id);
			stmt.setString(2, entryId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				type = rs.getInt("TYPE");
				refNo = rs.getInt("REFNO");
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			stmt.setString(2, entryId);
			
			if(stmt.executeUpdate()>0){
				
				if(type == 60){
					stmt = getConnection().prepareStatement(deleteReverseEntry);
					stmt.setInt(1, refNo);
					
					stmt.execute();
				}
				
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel delBillPaymentEntry(String id, String entryId) {
		MessageModel model = new MessageModel();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		String payDate = "0000-00-00";
//		double payAmt = 0;
		String billno = "";
		String branch = "";
		double adjust = 0;
		double amount = 0;
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE, SUM(B.AMOUNT) AS PAYAMT FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (41,42,43,44)";
		
		// 	query to get the max ajdust date
		String getAdjustDate = "SELECT MAX(ADJUSTDATE) AS PAYDATE FROM BILL_TO_BILL_ADJUST WHERE TONO = ? AND TOBRANCH = ? AND TYPE = 1";

		// query to change the sales table 
		String updateSalesDtls = "UPDATE SALES SET PAYAMT = PAYAMT - ?, PAYDATE = ?, ADJAMT = ADJAMT - ? WHERE SALESID = ? AND BRANCHID = ?";
		
		// query to get the billno and branch
		String getBillDtls = "SELECT A.BILLNO, A.BRANCH, A.ADJUST, A.AMOUNT FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER B WHERE A.ID=B.ID AND A.ENTRYID = ? AND A.ID = ?";
		
		// query to get rpt id
		String deleteEntry = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? AND ENTRYID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getBillDtls);
			stmt.setString(1, entryId);
			stmt.setString(2, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				billno = rs.getString("BILLNO");
				branch = rs.getString("BRANCH");
				adjust = rs.getDouble("ADJUST");
				amount = rs.getDouble("AMOUNT");
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			stmt.setString(2, entryId);
			
			stmt.executeUpdate();
			
			getConnection().commit();
				
			stmt = getConnection().prepareStatement(getPaymentDtls);
			stmt.setString(1, branch);
			stmt.setString(2, billno);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				if(rs.getString("PAYDATE") != null){
					payDate = rs.getString("PAYDATE");
//					payAmt = rs.getDouble("PAYAMT");
				}
			}
				
			stmt = getConnection().prepareStatement(getAdjustDate);
			stmt.setString(1, branch);
			stmt.setString(2, billno);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				if(rs.getString("PAYDATE") != null){
					payDate = rs.getString("PAYDATE");
	//				payAmt = rs.getDouble("PAYAMT");
				}
			}
			
			stmt = getConnection().prepareStatement(updateSalesDtls);
			stmt.setDouble(1, amount);
			stmt.setString(2, payDate);
			stmt.setDouble(3, adjust);
			stmt.setString(4, billno);
			stmt.setString(5, branch);
			
			stmt.execute();
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	@SuppressWarnings("resource")
	public MessageModel syncSalesData(String date, AccessPointModel accessPoint) {
		MessageModel model = new MessageModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		long id = 1;
		boolean isMasterPresent = false;
		int idx = 1;
		int accessid = accessPoint.getAccessId();
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";

		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, BANKNAME, CUSTOMER, CUSTBANK, REMARK, EDITABLE) VALUES (?,?,?,?,'C',?,'','',?,'',?,'N')";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		// query to delete the old data
		String delSyncData = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = (SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?) AND EDITABLE = 'N' AND (TYPE = 31 OR TYPE = 32)";
		
		//query to add new data
		String SyncSalesDtls = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, A.CUSTID, SUM(((100+B.TAX)*(((100-B.DISRATE)*B.QTY*B.RATE)/100))/100) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.PAYMODE " +
				"FROM SALES A, SALES_ITEM B " +
				"WHERE A.SALESID = B.SALESID " +
				"AND A.BRANCHID = B.BRANCHID " +
				"AND A.SALESDATE = ? " +
				"AND A.BRANCHID = ? " +
				"AND (A.PAYMODE = 1 " +
				"OR A.PAYMODE = 2) " +
				"GROUP BY A.SALESID, A.BRANCHID " +
				"ORDER BY A.SALESID, A.BRANCHID";
		try{
			
			stmt1 = getConnection().prepareStatement(delSyncData);
			stmt1.setInt(1, accessid);
			stmt1.setString(2, date);
			
			stmt1.execute();
			
			stmt1 = getConnection().prepareStatement(SyncSalesDtls);
			stmt1.setString(1, date);
			stmt1.setInt(2, accessid);
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, accessid);
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, accessid);
				stmt.setString(3, date);
				
				stmt.executeUpdate();
				
			/*	msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);*/
				
			} else {
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}					
			}
			
			rs1 = stmt1.executeQuery();
			stmt = getConnection().prepareStatement(insertEntryDtls);
			double total = 0;
			double roundOff = 0;
			
			while(rs1.next()){
				total = rs1.getDouble("AMT")+rs1.getDouble("PACKING")+rs1.getDouble("FORWARD")+rs1.getDouble("INSTALLATION")-rs1.getDouble("LESS");
				roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
				stmt.setLong(1, id);
				stmt.setInt(2, idx);
				stmt.setInt(3, (rs1.getInt("PAYMODE") == 1) ? 31 : 32);
				stmt.setString(4, (rs1.getInt("PAYMODE") == 1) ? "1" : "0");
				stmt.setDouble(5, (new Double(Utils.get2DecimalDouble(total+roundOff))));
				stmt.setInt(6, rs1.getInt("CUSTID"));
				stmt.setString(7, "Bill No : "+accessPoint.getBillPrefix()+Utils.padBillNo(rs1.getInt("SALESID")));
				stmt.addBatch();
				idx++;
				
			}
			
			stmt.executeBatch();
			
			getConnection().commit();
			
			model.addNewMessage(new Message(SUCCESS, "Sales Data is synced"));
			
		} catch (Exception e){
			e.printStackTrace();
			model.addNewMessage(new Message(ERROR, "Error occured while syncing Sales Data"));
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel sendForApproval(String id, String date, AccessPointModel access) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get rpt id
		String deleteEntry = "UPDATE BRANCH_DAY_BOOK_MASTER SET ISAPPROVED = 'S' WHERE ID = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";
		
		try{
			
			if(id.equals("")){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					id = rs.getString(1);
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setString(1, id);
				stmt.setInt(2, access.getAccessId());
				stmt.setString(3, date);
				
				stmt.execute();
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Sent for Approval successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel rejectApproval(String id) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String deleteEntry = "UPDATE BRANCH_DAY_BOOK_MASTER SET ISAPPROVED = 'R' WHERE ID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Rejected Approval successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel approveBook(String id, UserModel user) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String deleteEntry = "UPDATE BRANCH_DAY_BOOK_MASTER SET ISAPPROVED = 'Y' , APPROVEDBY = ? WHERE ID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, id);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Approved successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public HashMap<String, DayBookEntryModel[]> getAwaitingList() {
		HashMap<String, DayBookEntryModel[]> list = new HashMap<String, DayBookEntryModel[]>(0);
		ArrayList<DayBookEntryModel> tempList = null;
		DayBookEntryModel temp = null;
		int accessId = 0;
		String branchName = "";

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get rpt id
		String getRptId = "SELECT A.ID, A.BRANCH, A.DAY, B.NAME, B.CITY, A.ISAPPROVED FROM BRANCH_DAY_BOOK_MASTER A, ACCESS_POINT B WHERE A.ISAPPROVED = 'S' AND A.BRANCH = B.ACCESSID ORDER BY A.BRANCH, A.DAY";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			
			rs = stmt.executeQuery();

			while(rs.next()){
				if(accessId != rs.getInt("BRANCH")){
					if(accessId !=0){
						list.put(branchName, (DayBookEntryModel[])tempList.toArray(new DayBookEntryModel[0]));
					}
					tempList = new ArrayList<DayBookEntryModel>(0);
					accessId = rs.getInt("BRANCH");
					branchName = rs.getString("NAME");
				}
				temp = new DayBookEntryModel();
				temp.setEntryDate(rs.getString("DAY"));
				temp.setId(rs.getLong("ID"));
				temp.setStatus(rs.getString("ISAPPROVED"));
				temp.getBranch().setAccessId(rs.getInt("BRANCH"));
				temp.getBranch().setAccessName(rs.getString("NAME"));
				temp.getBranch().setCity(rs.getString("CITY"));
				tempList.add(temp);
			}
			
			list.put(branchName, (DayBookEntryModel[])tempList.toArray(new DayBookEntryModel[0]));
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return list;
	}

	public EntryModel[] getPDChequesEntries(int accessId) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CHQNO, A.CHQDT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C  WHERE A.ID = C.ID AND (A.TYPE = 1 OR A.TYPE = 43) AND C.BRANCH = ?  AND A.CUSTOMER = B.CUSTOMERID AND C.DAY > CURRENT_DATE ORDER BY C.DAY";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(1);
				entry.setRemark(rs.getString("REMARK"));
				entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
				entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				entry.getCustomer().setArea(rs.getString("AREA"));
				entry.getCustomer().setCity(rs.getString("CITY"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setChqDt(rs.getString("CHQDT"));
				if(rs.getInt("BRANCH") != -1){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
					entry.setBillNo(rs.getInt("BILLNO"));
				}
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);}

	public EntryModel[] getExportData(String fromDate, String toDate) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT A.BRANCH, A.DAY, B.ENTRYID, B.TYPE, B.AMOUNT, B.CHQNO, B.CHQDT, B.BANKNAME, B.CUSTOMER, B.CUSTBANK, B.REMARK, B.BRANCH AS BRNH " +
				"FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B " +
				"WHERE A.ID = B.ID " +
				"AND B.TYPE <> 5 "+
				"AND A.DAY >= ? " +
				"AND A.DAY <= ? " +
				"ORDER BY A.BRANCH, A.DAY ";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setEntryId(rs.getInt("ENTRYID"));
				entry.setEntryDate(rs.getString("DAY"));
				entry.setBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setBankName(rs.getString("BANKNAME"));
				entry.setRemark(rs.getString("REMARK"));
				if(rs.getString("CUSTOMER")!= null){
					entry.setCustomer((new CustomerBroker(getConnection())).getCustomerDtls(rs.getInt("CUSTOMER")));
				}
				if(rs.getString("CHQNO")!= null){
					entry.setChqNo(rs.getString("CHQNO"));
				}
				if(rs.getString("CHQDT")!= null){
					entry.setChqDt(rs.getString("CHQDT"));
				}
				if(rs.getString("CUSTBANK")!= null){
					entry.setCustBankName(rs.getString("CUSTBANK"));
				}
				if(rs.getString("BRNH") != null && rs.getInt("BRNH") != -1 && rs.getInt("TYPE") == 60){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRNH")));
				}
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

	@SuppressWarnings("resource")
	public MessageModel addBillPaymentEntry(EntryModel[] billEntry) {
		MessageModel model = new MessageModel();
//		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		long refno = 1;
		long id = 1;
		boolean isMasterPresent = false;
		double total = 0;
		String payDate = "0000-00-00";
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to get max id
		String getRefNo = "SELECT MAX(REFNO) FROM BRANCH_DAY_BOOK_ENTRIES";
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (41,42,43,44)";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";

		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, BRANCH, BILLNO, EDITABLE, REFNO, REMARK, ADJUST, USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?,?)";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		// query to update sales table
		String updateSales = "UPDATE SALES SET PAYAMT = PAYAMT + ? , PAYDATE = ?, ADJAMT = ? WHERE SALESID = ? AND BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, billEntry[0].getBranchId());
			stmt.setString(2, billEntry[0].getEntryDate());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			stmt = getConnection().prepareStatement(getRefNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1)>0){
				refno = rs.getLong(1) + 1;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, billEntry[0].getBranchId());
				stmt.setString(3, billEntry[0].getEntryDate());
				
				stmt.executeUpdate();
				int idx = 1;
				stmt = getConnection().prepareStatement(insertEntryDtls);
				stmt1 = getConnection().prepareStatement(updateSales);
				stmt2 = getConnection().prepareStatement(getPaymentDtls);
				
				for(int i=0; i<billEntry.length; i++){
					stmt.setLong(1, id);
					stmt.setInt(2, idx);
					stmt.setInt(3, billEntry[i].getEntryType());
					stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
					stmt.setString(5, billEntry[i].getCrdr());
					stmt.setDouble(6, billEntry[i].getAmount()+billEntry[i].getAdjAmt());
					stmt.setString(7, billEntry[i].getChqNo());
					stmt.setString(8, billEntry[i].getChqDt());
					stmt.setString(9, billEntry[i].getBankName());
					stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
					stmt.setString(11, billEntry[i].getCustBankName());
					stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
					stmt.setInt(13, billEntry[i].getBillNo());
					stmt.setLong(14, refno);
					stmt.setString(15, billEntry[i].getRemark());
					stmt.setDouble(16, billEntry[i].getAdjAmt());
					stmt.setString(17, billEntry[i].getUser());
					
					stmt.executeUpdate();
					
					stmt2.setInt(1, billEntry[i].getBillBranch().getAccessId());
					stmt2.setInt(2, billEntry[i].getBillNo());
					
					rs1 = stmt2.executeQuery();
					
					if(rs1.next() && rs1.getString(1) != null){
						payDate = rs1.getString(1);
					} else {
						payDate = billEntry[i].getEntryDate();
					}
					
					stmt1.setDouble(1, billEntry[i].getAmount());
					stmt1.setString(2, payDate);
					stmt1.setDouble(3, billEntry[i].getAdjAmt());
					stmt1.setInt(4, billEntry[i].getBillNo());
					stmt1.setInt(5, billEntry[i].getBillBranch().getAccessId());
					
					stmt1.executeUpdate();
					
					total = total + billEntry[i].getAmount();
					
					idx++;
				}
				
			/*	msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);*/
				
			} else {
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					stmt = getConnection().prepareStatement(insertEntryDtls);
					stmt1 = getConnection().prepareStatement(updateSales);
					stmt2 = getConnection().prepareStatement(getPaymentDtls);
					
					for(int i=0; i<billEntry.length; i++){
						stmt.setLong(1, id);
						stmt.setInt(2, idx);
						stmt.setInt(3, billEntry[i].getEntryType());
						stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
						stmt.setString(5, billEntry[i].getCrdr());
						stmt.setDouble(6, billEntry[i].getAmount()+billEntry[i].getAdjAmt());
						stmt.setString(7, billEntry[i].getChqNo());
						stmt.setString(8, billEntry[i].getChqDt());
						stmt.setString(9, billEntry[i].getBankName());
						stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
						stmt.setString(11, billEntry[i].getCustBankName());
						stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
						stmt.setInt(13, billEntry[i].getBillNo());
						stmt.setLong(14, refno);
						stmt.setString(15, billEntry[i].getRemark());
						stmt.setDouble(16, billEntry[i].getAdjAmt());
						stmt.setString(17, billEntry[i].getUser());
						
						stmt.executeUpdate();
						
						stmt2.setInt(1, billEntry[i].getBillBranch().getAccessId());
						stmt2.setInt(2, billEntry[i].getBillNo());
						
						rs1 = stmt2.executeQuery();
						
						if(rs1.next() && rs1.getString(1) != null){
							payDate = rs1.getString(1);
						} else {
							payDate = billEntry[i].getEntryDate();
						}
						
						stmt1.setDouble(1, billEntry[i].getAmount());
						stmt1.setString(2, payDate);
						stmt1.setDouble(3, billEntry[i].getAdjAmt());
						stmt1.setInt(4, billEntry[i].getBillNo());
						stmt1.setInt(5, billEntry[i].getBillBranch().getAccessId());
						
						stmt1.executeUpdate();

						total = total + billEntry[i].getAmount();
						
						idx++;
					}
					
			}
			
			getConnection().commit();
			
			model.addNewMessage(new Message(SUCCESS, Utils.getEntryType(billEntry[0].getEntryType()) +" of Rs.  "+Utils.get2Decimal(total)+" added successfully to "+Utils.convertToAppDateDDMMYY(billEntry[0].getEntryDate())+" day book."));
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public EntryModel[] getBankDepositEntries(int accessId) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C  WHERE A.ID = C.ID AND (A.TYPE = 4 OR A.TYPE = 44) AND C.BRANCH = ?  AND A.CUSTOMER = B.CUSTOMERID  ORDER BY C.DAY";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(1);
				entry.setRemark(rs.getString("REMARK"));
				entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
				entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				entry.getCustomer().setArea(rs.getString("AREA"));
				entry.getCustomer().setCity(rs.getString("CITY"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setChqDt(rs.getString("CHQDT"));
				if(rs.getInt("BRANCH") != -1){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
					entry.setBillNo(rs.getInt("BILLNO"));
				}
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);}
	
	public EntryModel[] getLastTransactions(int custId) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CHQNO, A.CHQDT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C  WHERE A.ID = C.ID AND A.TYPE IN (1,2,3,4,41,42,43,44) AND A.CUSTOMER = ?  AND A.CUSTOMER = B.CUSTOMERID ORDER BY C.DAY DESC LIMIT 10";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setInt(1, custId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("DAY"));
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setRemark(rs.getString("REMARK"));
				entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
				entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				entry.getCustomer().setArea(rs.getString("AREA"));
				entry.getCustomer().setCity(rs.getString("CITY"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setChqDt(rs.getString("CHQDT"));
				if(rs.getInt("BRANCH") != -1){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
					entry.setBillNo(rs.getInt("BILLNO"));
				}
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

	public EntryModel[] getUndepositedChqLst(int accessId) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT A.ID, A.ENTRYID, C.DAY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CHQNO, A.CHQDT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO, D.NAME AS BRANCHNAME, D.CITY AS BRANCHCITY FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C, ACCESS_POINT D WHERE A.ID = C.ID AND C.BRANCH = D.ACCESSID AND (A.TYPE = 1 OR A.TYPE = 43) AND C.BRANCH = ? AND DEPOSITDATE = '0000-00-00' AND A.CUSTOMER = B.CUSTOMERID AND C.DAY > CURRENT_DATE ORDER BY C.DAY";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setId(rs.getInt("ID"));
				entry.getBranch().setAccessName(rs.getString("BRANCHNAME"));
				entry.getBranch().setCity(rs.getString("BRANCHCITY"));
				entry.setEntryId(rs.getInt("ENTRYID"));
				entry.setEntryDate(rs.getString("DAY"));
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setRemark(rs.getString("REMARK"));
				entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
				entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				entry.getCustomer().setArea(rs.getString("AREA"));
				entry.getCustomer().setCity(rs.getString("CITY"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setChqDt(rs.getString("CHQDT"));
				if(rs.getInt("BRANCH") != -1){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
					entry.setBillNo(rs.getInt("BILLNO"));
				}
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);}

	public MessageModel depositChq(String id, String entryid) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String deleteEntry = "UPDATE BRANCH_DAY_BOOK_ENTRIES SET DEPOSITDATE = CURRENT_DATE WHERE ID = ? AND ENTRYID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			stmt.setString(2, entryid);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Deposited successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while depositing cheque entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public double getBranchCashBalance(int accessId) {
		double openBal = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the cr balance
		String getCreditCashBalanace = "SELECT SUM(AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES WHERE CRDR = 'C' AND ISCASH = 1 AND ID IN (SELECT A.ID FROM BRANCH_DAY_BOOK_MASTER A WHERE A.BRANCH = ?)";
		
		// query to get the dr balance
		String getDebitCashBalanace = "SELECT SUM(AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES WHERE CRDR = 'D' AND ISCASH = 1 AND ID IN (SELECT A.ID FROM BRANCH_DAY_BOOK_MASTER A WHERE A.BRANCH = ?)";
		
		try{
			
			stmt = getConnection().prepareStatement(getCreditCashBalanace);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				openBal = (rs.getDouble(1));
			}
			
			stmt = getConnection().prepareStatement(getDebitCashBalanace);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				openBal = openBal - rs.getDouble(1);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return openBal;
	}

	public PurchaseModel[] getPurchaseDtls(String customer, String[] branch) {
		ArrayList<PurchaseModel> outstandingBills = new ArrayList<PurchaseModel>(0);
		PurchaseModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.PURCHASEID, A.RECDDT, A.INVDT, A.INVNO, A.BRANCHID, A.TAXTYPE, E.BILL_PREFIX, E.NAME, E.CITY AS BRANCHCITY, A.SUPPID, B.CUSTOMERNAME, B.AREA, B.CITY, B.CONTACTPERSON, B.STDCODE, " +
				"B.PHONE1, B.PHONE2, B.MOBILE1, B.MOBILE2, SUM(C.QTY*((100+C.TAX)*(C.RATE)/100)) AS AMT, A.EXTRA, A.DISCOUNT, A.PAYDATE, A.PAYAMT  " +
				"FROM PURCHASE_ITEM C, CUSTOMER B, ACCESS_POINT E, PURCHASE A " +
				"WHERE A.PURCHASEID = C.PURCHASEID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SUPPID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SUPPID = ? ";
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

		outstandingBill	= outstandingBill +	"GROUP BY A.PURCHASEID, A.BRANCHID " +
				 "HAVING (AMT + A.EXTRA - A.DISCOUNT - A.PAYAMT) >= 1 ";
		outstandingBill	= outstandingBill +	"ORDER BY A.RECDDT, A.BRANCHID, A.PURCHASEID ";
		
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
				model = new PurchaseModel();
				model.setPurchaseId(rs.getInt("PURCHASEID"));
				model.setRecdDt(rs.getString("RECDDT"));
				model.setInvDt(rs.getString("INVDT"));
				model.setInvNo(rs.getString("INVNO"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setCity(rs.getString("BRANCHCITY"));
				model.getSupplier().setCustomerId(rs.getInt("SUPPID"));
				model.getSupplier().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getSupplier().setArea(rs.getString("AREA"));
				model.getSupplier().setCity(rs.getString("CITY"));
				model.getSupplier().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getSupplier().setStdcode(rs.getString("STDCODE"));
				model.getSupplier().setPhone1(rs.getString("PHONE1"));
				model.getSupplier().setPhone2(rs.getString("PHONE2"));
				model.getSupplier().setMobile1(rs.getString("MOBILE1"));
				model.getSupplier().setMobile2(rs.getString("MOBILE2"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setExtra(rs.getDouble("EXTRA"));
				model.setDiscount(rs.getDouble("DISCOUNT"));
				model.setPayDate(rs.getString("PAYDATE"));
				model.setPayAmt(rs.getDouble("PAYAMT"));
				outstandingBills.add(model);
			}
			
			Collections.sort(outstandingBills, new PurchaseRecdDateComparator());
			
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
		
		return (PurchaseModel[]) outstandingBills.toArray(new PurchaseModel[0]);
	}

	@SuppressWarnings("resource")
	public MessageModel addPurchasePaymentEntry(EntryModel[] billEntry) {
		MessageModel model = new MessageModel();
//		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		long refno = 1;
		long id = 1;
		boolean isMasterPresent = false;
		double total = 0;
		String payDate = "0000-00-00";
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to get max id
		String getRefNo = "SELECT MAX(REFNO) FROM BRANCH_DAY_BOOK_ENTRIES";
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (71,72,73)";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";

		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, BRANCH, BILLNO, EDITABLE, REFNO, REMARK, ADJUST, USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?,?)";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		// query to update sales table
		String updateSales = "UPDATE PURCHASE SET PAYAMT = PAYAMT + ? , PAYDATE = ?, ADJAMT = ADJAMT + ? WHERE PURCHASEID = ? AND BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, billEntry[0].getBranchId());
			stmt.setString(2, billEntry[0].getEntryDate());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			stmt = getConnection().prepareStatement(getRefNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1)>0){
				refno = rs.getLong(1) + 1;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, billEntry[0].getBranchId());
				stmt.setString(3, billEntry[0].getEntryDate());
				
				stmt.executeUpdate();
				int idx = 1;
				stmt = getConnection().prepareStatement(insertEntryDtls);
				stmt1 = getConnection().prepareStatement(updateSales);
				stmt2 = getConnection().prepareStatement(getPaymentDtls);
				
				for(int i=0; i<billEntry.length; i++){
					stmt.setLong(1, id);
					stmt.setInt(2, idx);
					stmt.setInt(3, billEntry[i].getEntryType());
					stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
					stmt.setString(5, billEntry[i].getCrdr());
					stmt.setDouble(6, billEntry[i].getAmount()+billEntry[i].getAdjAmt());
					stmt.setString(7, billEntry[i].getChqNo());
					stmt.setString(8, billEntry[i].getChqDt());
					stmt.setString(9, billEntry[i].getBankName());
					stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
					stmt.setString(11, billEntry[i].getCustBankName());
					stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
					stmt.setInt(13, billEntry[i].getBillNo());
					stmt.setLong(14, refno);
					stmt.setString(15, billEntry[i].getRemark());
					stmt.setDouble(16, billEntry[i].getAdjAmt());
					stmt.setString(17, billEntry[i].getUser());
					
					stmt.executeUpdate();
					
					stmt2.setInt(1, billEntry[i].getBillBranch().getAccessId());
					stmt2.setInt(2, billEntry[i].getBillNo());
					
					rs1 = stmt2.executeQuery();
					
					if(rs1.next() && rs1.getString(1) != null){
						payDate = rs1.getString(1);
					} else {
						payDate = billEntry[i].getEntryDate();
					}
					
					stmt1.setDouble(1, billEntry[i].getAmount());
					stmt1.setString(2, payDate);
					stmt1.setDouble(3, billEntry[i].getAdjAmt());
					stmt1.setInt(4, billEntry[i].getBillNo());
					stmt1.setInt(5, billEntry[i].getBillBranch().getAccessId());
					
					stmt1.executeUpdate();
					
					total = total + billEntry[i].getAmount();
					
					idx++;
				}
				
			/*	msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);*/
				
			} else {
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					stmt = getConnection().prepareStatement(insertEntryDtls);
					stmt1 = getConnection().prepareStatement(updateSales);
					stmt2 = getConnection().prepareStatement(getPaymentDtls);
					
					for(int i=0; i<billEntry.length; i++){
						stmt.setLong(1, id);
						stmt.setInt(2, idx);
						stmt.setInt(3, billEntry[i].getEntryType());
						stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
						stmt.setString(5, billEntry[i].getCrdr());
						stmt.setDouble(6, billEntry[i].getAmount()+billEntry[i].getAdjAmt());
						stmt.setString(7, billEntry[i].getChqNo());
						stmt.setString(8, billEntry[i].getChqDt());
						stmt.setString(9, billEntry[i].getBankName());
						stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
						stmt.setString(11, billEntry[i].getCustBankName());
						stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
						stmt.setInt(13, billEntry[i].getBillNo());
						stmt.setLong(14, refno);
						stmt.setString(15, billEntry[i].getRemark());
						stmt.setDouble(16, billEntry[i].getAdjAmt());
						stmt.setString(17, billEntry[i].getUser());
						
						stmt.executeUpdate();
						
						stmt2.setInt(1, billEntry[i].getBillBranch().getAccessId());
						stmt2.setInt(2, billEntry[i].getBillNo());
						
						rs1 = stmt2.executeQuery();
						
						if(rs1.next() && rs1.getString(1) != null){
							payDate = rs1.getString(1);
						} else {
							payDate = billEntry[i].getEntryDate();
						}
						
						stmt1.setDouble(1, billEntry[i].getAmount());
						stmt1.setString(2, payDate);
						stmt1.setDouble(3, billEntry[i].getAdjAmt());
						stmt1.setInt(4, billEntry[i].getBillNo());
						stmt1.setInt(5, billEntry[i].getBillBranch().getAccessId());
						
						stmt1.executeUpdate();

						total = total + billEntry[i].getAmount();
						
						idx++;
					}
					
			}
			
			getConnection().commit();
			
			model.addNewMessage(new Message(SUCCESS, Utils.getEntryType(billEntry[0].getEntryType()) +" of Rs.  "+Utils.get2Decimal(total)+" added successfully to "+Utils.convertToAppDateDDMMYY(billEntry[0].getEntryDate())+" day book."));
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel delPurchaseEntry(String id, String entryId) {
		MessageModel model = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String payDate = "0000-00-00";
		double payAmt = 0;
		String billno = "";
		String branch = "";
		double adjust = 0;
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE, SUM(B.AMOUNT) AS PAYAMT FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (71,72,73)";
		
		// query to change the sales table 
		String updateSalesDtls = "UPDATE PURCHASE SET PAYAMT = ?, PAYDATE = ?, ADJAMT = ADJAMT - ? WHERE PURCHASEID = ? AND BRANCHID = ?";
		
		// query to get the billno and branch
		String getBillDtls = "SELECT A.BILLNO, A.BRANCH, A.ADJUST FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER B WHERE A.ID=B.ID AND A.ENTRYID = ? AND A.ID = ?";
		
		// query to get rpt id
		String deleteEntry = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? AND ENTRYID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getBillDtls);
			stmt.setString(1, entryId);
			stmt.setString(2, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				billno = rs.getString("BILLNO");
				branch = rs.getString("BRANCH");
				adjust = rs.getDouble("ADJUST");
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			stmt.setString(2, entryId);
			
			if(stmt.executeUpdate()>0){
				
				stmt = getConnection().prepareStatement(getPaymentDtls);
				stmt.setString(1, branch);
				stmt.setString(2, billno);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					if(rs.getString("PAYDATE") != null){
						payDate = rs.getString("PAYDATE");
						payAmt = rs.getDouble("PAYAMT");
					}
				}
				
				stmt = getConnection().prepareStatement(updateSalesDtls);
				stmt.setDouble(1, payAmt);
				stmt.setString(2, payDate);
				stmt.setDouble(3, adjust);
				stmt.setString(4, billno);
				stmt.setString(5, branch);
				
				stmt.execute();
				
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public OrderModel[] getOrderDtls(String customer, String[] branch) {
		ArrayList<OrderModel> outstandingBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, A.TAXTYPE, E.BILL_PREFIX, E.NAME, E.CITY AS BRANCHCITY, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, B.CONTACTPERSON, B.STDCODE, " +
				"B.PHONE1, B.PHONE2, B.MOBILE1, B.MOBILE2, SUM(C.QTY*((100+C.TAX)*(1-(C.DISRATE/100))*(C.RATE)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE " +
				"FROM ORDER_ITEM C, CUSTOMER B, ACCESS_POINT E, ORDER_MASTER A " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
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

		outstandingBill	= outstandingBill +	"GROUP BY A.ORDERID, A.BRANCHID " +
				 "HAVING (AMT + A.PACKING + A.FORWARD + A.INSTALLATION - A.LESS - A.ADVANCE) >= 1 ";
		outstandingBill	= outstandingBill +	"ORDER BY A.ORDERDATE, A.BRANCHID, A.ORDERID ";
		
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
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
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
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				outstandingBills.add(model);
			}
			
			Collections.sort(outstandingBills, new OrderAlertDateComparator());
			
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
		
		return (OrderModel[]) outstandingBills.toArray(new OrderModel[0]);
	}

	@SuppressWarnings("resource")
	public MessageModel addOrderAdvanceEntry(EntryModel[] billEntry) {
		MessageModel model = new MessageModel();
//		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		long refno = 1;
		long id = 1;
		boolean isMasterPresent = false;
		double total = 0;
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to get max id
		String getRefNo = "SELECT MAX(REFNO) FROM BRANCH_DAY_BOOK_ENTRIES";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";

		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, BRANCH, BILLNO, EDITABLE, REFNO, REMARK, USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?)";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		// query to update sales table
		String updateSales = "UPDATE ORDER_MASTER SET ADVANCE = ADVANCE + ? WHERE ORDERID = ? AND BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, billEntry[0].getBranchId());
			stmt.setString(2, billEntry[0].getEntryDate());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			stmt = getConnection().prepareStatement(getRefNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1)>0){
				refno = rs.getLong(1) + 1;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, billEntry[0].getBranchId());
				stmt.setString(3, billEntry[0].getEntryDate());
				
				stmt.executeUpdate();
				int idx = 1;
				stmt = getConnection().prepareStatement(insertEntryDtls);
				stmt1 = getConnection().prepareStatement(updateSales);
				
				for(int i=0; i<billEntry.length; i++){
					stmt.setLong(1, id);
					stmt.setInt(2, idx);
					stmt.setInt(3, billEntry[i].getEntryType());
					stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
					stmt.setString(5, billEntry[i].getCrdr());
					stmt.setDouble(6, billEntry[i].getAmount());
					stmt.setString(7, billEntry[i].getChqNo());
					stmt.setString(8, billEntry[i].getChqDt());
					stmt.setString(9, billEntry[i].getBankName());
					stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
					stmt.setString(11, billEntry[i].getCustBankName());
					stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
					stmt.setInt(13, billEntry[i].getBillNo());
					stmt.setLong(14, refno);
					stmt.setString(15, billEntry[i].getRemark());
					stmt.setString(16, billEntry[i].getUser());
					
					stmt.executeUpdate();
					
					stmt1.setDouble(1, billEntry[i].getAmount());
					stmt1.setInt(2, billEntry[i].getBillNo());
					stmt1.setInt(3, billEntry[i].getBillBranch().getAccessId());
					
					stmt1.executeUpdate();
					
					total = total + billEntry[i].getAmount();
					
					idx++;
				}
				
			/*	msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);*/
				
			} else {
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					stmt = getConnection().prepareStatement(insertEntryDtls);
					stmt1 = getConnection().prepareStatement(updateSales);
					
					for(int i=0; i<billEntry.length; i++){
						stmt.setLong(1, id);
						stmt.setInt(2, idx);
						stmt.setInt(3, billEntry[i].getEntryType());
						stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
						stmt.setString(5, billEntry[i].getCrdr());
						stmt.setDouble(6, billEntry[i].getAmount());
						stmt.setString(7, billEntry[i].getChqNo());
						stmt.setString(8, billEntry[i].getChqDt());
						stmt.setString(9, billEntry[i].getBankName());
						stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
						stmt.setString(11, billEntry[i].getCustBankName());
						stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
						stmt.setInt(13, billEntry[i].getBillNo());
						stmt.setLong(14, refno);
						stmt.setString(15, billEntry[i].getRemark());
						stmt.setString(16, billEntry[i].getUser());
						
						stmt.executeUpdate();
						
						stmt1.setDouble(1, billEntry[i].getAmount());
						stmt1.setInt(2, billEntry[i].getBillNo());
						stmt1.setInt(3, billEntry[i].getBillBranch().getAccessId());
						
						stmt1.executeUpdate();

						total = total + billEntry[i].getAmount();
						
						idx++;
					}
					
			}
			
			getConnection().commit();
			
			model.addNewMessage(new Message(SUCCESS, Utils.getEntryType(billEntry[0].getEntryType()) +" of Rs.  "+Utils.get2Decimal(total)+" added successfully to "+Utils.convertToAppDateDDMMYY(billEntry[0].getEntryDate())+" day book."));
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel delOrderEntry(String id, String entryId) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		double advcance = 0;
		String billno = "";
		String branch = "";
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT SUM(B.AMOUNT) AS PAYAMT FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (1,2,3,4)";
		
		// query to change the sales table 
		String updateSalesDtls = "UPDATE ORDER_MASTER SET ADVANCE = ? WHERE ORDERID = ? AND BRANCHID = ?";
		
		// query to get the billno and branch
		String getBillDtls = "SELECT A.BILLNO, A.BRANCH FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER B WHERE A.ID=B.ID AND A.ENTRYID = ? AND A.ID = ?";
		
		// query to get rpt id
		String deleteEntry = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? AND ENTRYID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getBillDtls);
			stmt.setString(1, entryId);
			stmt.setString(2, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				billno = rs.getString("BILLNO");
				branch = rs.getString("BRANCH");
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			stmt.setString(2, entryId);
			
			if(stmt.executeUpdate()>0){
				
				stmt = getConnection().prepareStatement(getPaymentDtls);
				stmt.setString(1, branch);
				stmt.setString(2, billno);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					if(rs.getString("PAYAMT") != null){
						advcance = rs.getDouble("PAYAMT");
					}
				}
				
				stmt = getConnection().prepareStatement(updateSalesDtls);
				stmt.setDouble(1, advcance);
				stmt.setString(2, billno);
				stmt.setString(3, branch);
				
				stmt.execute();
				
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	@SuppressWarnings("resource")
	public MessageModel addLabourBillPaymentEntry(EntryModel[] billEntry) {
		MessageModel model = new MessageModel();
//		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		long refno = 1;
		long id = 1;
		boolean isMasterPresent = false;
		double total = 0;
		String payDate = "0000-00-00";
		
		// query to get id
		String getId = "SELECT ID FROM BRANCH_DAY_BOOK_MASTER WHERE BRANCH = ? AND DAY = ?";
		
		// query to get max id
		String getMaxId = "SELECT MAX(ID) FROM BRANCH_DAY_BOOK_MASTER";
		
		// query to get max id
		String getRefNo = "SELECT MAX(REFNO) FROM BRANCH_DAY_BOOK_ENTRIES";
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (41,42,43,44)";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO BRANCH_DAY_BOOK_MASTER(ID, BRANCH, DAY) VALUE (?,?,?)";

		// query to insert appointment type details 
		String insertEntryDtls = "INSERT INTO BRANCH_DAY_BOOK_ENTRIES(ID, ENTRYID, TYPE, ISCASH, CRDR, AMOUNT, CHQNO, CHQDT, BANKNAME, CUSTOMER, CUSTBANK, BRANCH, BILLNO, EDITABLE, REFNO, REMARK, ADJUST, USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,'Y',?,?,?,?)";

		// query to get max idx
		String getMaxIdx = "SELECT MAX(ENTRYID) FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ?";
		
		// query to update sales table
		String updateSales = "UPDATE LABOUR_BILL SET PAYAMT = PAYAMT + ? , PAYDATE = ?, ADJAMT = ADJAMT + ? WHERE BILLNO = ? AND BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getId);
			stmt.setInt(1, billEntry[0].getBranchId());
			stmt.setString(2, billEntry[0].getEntryDate());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				id = rs.getLong("ID");
				isMasterPresent = true;
			}
			
			stmt = getConnection().prepareStatement(getRefNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1)>0){
				refno = rs.getLong(1) + 1;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					id = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, id);
				stmt.setInt(2, billEntry[0].getBranchId());
				stmt.setString(3, billEntry[0].getEntryDate());
				
				stmt.executeUpdate();
				int idx = 1;
				stmt = getConnection().prepareStatement(insertEntryDtls);
				stmt1 = getConnection().prepareStatement(updateSales);
				stmt2 = getConnection().prepareStatement(getPaymentDtls);
				
				for(int i=0; i<billEntry.length; i++){
					stmt.setLong(1, id);
					stmt.setInt(2, idx);
					stmt.setInt(3, billEntry[i].getEntryType());
					stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
					stmt.setString(5, billEntry[i].getCrdr());
					stmt.setDouble(6, billEntry[i].getAmount()+billEntry[i].getAdjAmt());
					stmt.setString(7, billEntry[i].getChqNo());
					stmt.setString(8, billEntry[i].getChqDt());
					stmt.setString(9, billEntry[i].getBankName());
					stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
					stmt.setString(11, billEntry[i].getCustBankName());
					stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
					stmt.setInt(13, billEntry[i].getBillNo());
					stmt.setLong(14, refno);
					stmt.setString(15, billEntry[i].getRemark());
					stmt.setDouble(16, billEntry[i].getAdjAmt());
					stmt.setString(17, billEntry[i].getUser());
					
					stmt.executeUpdate();
					
					stmt2.setInt(1, billEntry[i].getBillBranch().getAccessId());
					stmt2.setInt(2, billEntry[i].getBillNo());
					
					rs1 = stmt2.executeQuery();
					
					if(rs1.next() && rs1.getString(1) != null){
						payDate = rs1.getString(1);
					} else {
						payDate = billEntry[i].getEntryDate();
					}
					
					stmt1.setDouble(1, billEntry[i].getAmount());
					stmt1.setString(2, payDate);
					stmt1.setDouble(3, billEntry[i].getAdjAmt());
					stmt1.setInt(4, billEntry[i].getBillNo());
					stmt1.setInt(5, billEntry[i].getBillBranch().getAccessId());
					
					stmt1.executeUpdate();
					
					total = total + billEntry[i].getAmount();
					
					idx++;
				}
				
			/*	msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);*/
				
			} else {
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, id);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					stmt = getConnection().prepareStatement(insertEntryDtls);
					stmt1 = getConnection().prepareStatement(updateSales);
					stmt2 = getConnection().prepareStatement(getPaymentDtls);
					
					for(int i=0; i<billEntry.length; i++){
						stmt.setLong(1, id);
						stmt.setInt(2, idx);
						stmt.setInt(3, billEntry[i].getEntryType());
						stmt.setString(4, billEntry[i].isCash() ? "1" : "0");
						stmt.setString(5, billEntry[i].getCrdr());
						stmt.setDouble(6, billEntry[i].getAmount()+billEntry[i].getAdjAmt());
						stmt.setString(7, billEntry[i].getChqNo());
						stmt.setString(8, billEntry[i].getChqDt());
						stmt.setString(9, billEntry[i].getBankName());
						stmt.setInt(10, billEntry[i].getCustomer().getCustomerId());
						stmt.setString(11, billEntry[i].getCustBankName());
						stmt.setInt(12, billEntry[i].getBillBranch().getAccessId());
						stmt.setInt(13, billEntry[i].getBillNo());
						stmt.setLong(14, refno);
						stmt.setString(15, billEntry[i].getRemark());
						stmt.setDouble(16, billEntry[i].getAdjAmt());
						stmt.setString(17, billEntry[i].getUser());
						
						stmt.executeUpdate();
						
						stmt2.setInt(1, billEntry[i].getBillBranch().getAccessId());
						stmt2.setInt(2, billEntry[i].getBillNo());
						
						rs1 = stmt2.executeQuery();
						
						if(rs1.next() && rs1.getString(1) != null){
							payDate = rs1.getString(1);
						} else {
							payDate = billEntry[i].getEntryDate();
						}
						
						stmt1.setDouble(1, billEntry[i].getAmount());
						stmt1.setString(2, payDate);
						stmt1.setDouble(3, billEntry[i].getAdjAmt());
						stmt1.setInt(4, billEntry[i].getBillNo());
						stmt1.setInt(5, billEntry[i].getBillBranch().getAccessId());
						
						stmt1.executeUpdate();

						total = total + billEntry[i].getAmount();
						
						idx++;
					}
					
			}
			
			getConnection().commit();
			
			model.addNewMessage(new Message(SUCCESS, Utils.getEntryType(billEntry[0].getEntryType()) +" of Rs.  "+Utils.get2Decimal(total)+" added successfully to "+Utils.convertToAppDateDDMMYY(billEntry[0].getEntryDate())+" day book."));
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel delLabourBillPaymentEntry(String id, String entryId) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		String payDate = "0000-00-00";
		double payAmt = 0;
		String billno = "";
		String branch = "";
		double adjust = 0;
		
		// query to get the details of payment
		String getPaymentDtls = "SELECT MAX(A.DAY) AS PAYDATE, SUM(B.AMOUNT) AS PAYAMT FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B WHERE A.ID = B.ID AND B.BRANCH = ? AND B.BILLNO = ? AND B.TYPE IN (46,47,48,49)";
		
		// query to change the sales table 
		String updateSalesDtls = "UPDATE LABOUR_BILL SET PAYAMT = ?, PAYDATE = ?, ADJAMT = ADJAMT - ? WHERE BILLNO = ? AND BRANCHID = ?";
		
		// query to get the billno and branch
		String getBillDtls = "SELECT A.BILLNO, A.BRANCH, A.ADJUST FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER B WHERE A.ID=B.ID AND A.ENTRYID = ? AND A.ID = ?";
		
		// query to get rpt id
		String deleteEntry = "DELETE FROM BRANCH_DAY_BOOK_ENTRIES WHERE ID = ? AND ENTRYID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getBillDtls);
			stmt.setString(1, entryId);
			stmt.setString(2, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				billno = rs.getString("BILLNO");
				branch = rs.getString("BRANCH");
				adjust = rs.getDouble("ADJUST"); 
			}
			
			stmt = getConnection().prepareStatement(deleteEntry);
			stmt.setString(1, id);
			stmt.setString(2, entryId);
			
			if(stmt.executeUpdate()>0){
				
				stmt = getConnection().prepareStatement(getPaymentDtls);
				stmt.setString(1, branch);
				stmt.setString(2, billno);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					if(rs.getString("PAYDATE") != null){
						payDate = rs.getString("PAYDATE");
						payAmt = rs.getDouble("PAYAMT");
					}
				}
				
				stmt = getConnection().prepareStatement(updateSalesDtls);
				stmt.setDouble(1, payAmt);
				stmt.setString(2, payDate);
				stmt.setDouble(3, adjust);
				stmt.setString(4, billno);
				stmt.setString(5, branch);
				
				stmt.execute();
				
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}
	

}
