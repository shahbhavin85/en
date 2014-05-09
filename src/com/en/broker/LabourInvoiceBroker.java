package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.AccessPointModel;
import com.en.model.LabourBillModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesModel;

public class LabourInvoiceBroker extends BaseBroker{

	public LabourInvoiceBroker(Connection conn) {
		super(conn);
	}

	public String getLastBillNo(AccessPointModel access) {
		String lastBillNo = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(BILLNO) FROM LABOUR_BILL WHERE BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(maxBillNo);
			stmt.setInt(1, access.getAccessId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1) > 0){
				lastBillNo = access.getBillPrefix() + "LB"+ rs.getInt(1);
			} else {
				lastBillNo = "--";
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
		
		return lastBillNo;
	}

	public synchronized MessageModel addBill(LabourBillModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long salesId = 1;
		String billPrefix = "";

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(BILLNO) FROM LABOUR_BILL WHERE BRANCHID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";
		
		// query to insert the bill details
		String insertBillDetails = "INSERT INTO LABOUR_BILL (BILLNO, BILLDATE, BRANCHID, CUSTID, REMARKS, AMOUNT, SALESMANID) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxBillNo);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					salesId = rs.getInt(1) + 1;
				}
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getBillPrefix);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					billPrefix = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(insertBillDetails);
				
				stmt.setLong(1, salesId);
				stmt.setInt(2, model.getBranch().getAccessId());
				stmt.setInt(3, model.getCustomer().getCustomerId());
				stmt.setString(4, model.getRemarks());
				stmt.setDouble(5, model.getTotalAmt());
				stmt.setString(6, model.getSalesman().getUserId());
				
				stmt.execute();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + "LB" + salesId);
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting sales details!!");
			msgs.addNewMessage(msg);
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
		
		return msgs;
	}

	public LabourBillModel getBillDetails(String billNo) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		LabourBillModel model = new LabourBillModel();
		
		String billPrefix = billNo.substring(0,3);
		String billNumb = billNo.substring(5);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL, WITHTIN, NOTIN, STATE FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.BILLNO, A.BILLDATE, A.CUSTID, B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, B.AREA, B.CUSTOMERTYPE, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, B.EMAIL1, A.REMARKS, A.AMOUNT, A.SALESMANID FROM LABOUR_BILL A, CUSTOMER B, CITY C WHERE B.CITY = C.CITY AND A.BILLNO = ? AND A.BRANCHID = ? AND A.CUSTID = B.CUSTOMERID";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME FROM USER WHERE USERID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBranchDetails);
			stmt.setString(1, billPrefix);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.getBranch().setAccessId(rs.getInt("ACCESSID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setAddress(rs.getString("ADDRESS"));
				model.getBranch().setCity(rs.getString("CITY"));
				model.getBranch().setState(rs.getString("STATE"));
				model.getBranch().setPincode(rs.getString("PINCODE"));
				model.getBranch().setVat(rs.getString("VAT"));
				model.getBranch().setCst(rs.getString("CST"));
				model.getBranch().setPhone1(rs.getString("PHONE1"));
				model.getBranch().setPhone2(rs.getString("PHONE2"));
				model.getBranch().setStdcode(rs.getString("STDCODE"));
				model.getBranch().setEmail(rs.getString("EMAIL"));
				model.getBranch().setWithTin(rs.getString("WITHTIN"));
				model.getBranch().setNoTin(rs.getString("NOTIN"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, billNumb);
			stmt.setInt(2, model.getBranch().getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setSaleid(rs.getInt("BILLNO"));
				model.setSalesdate(rs.getString("BILLDATE"));
				model.setRemarks(rs.getString("REMARKS"));
				model.setTotalAmt(rs.getDouble("AMOUNT"));
				if(rs.getString("SALESMANID") != null){
					model.getSalesman().setUserId(rs.getString("SALESMANID"));
					stmt1 = getConnection().prepareStatement(getSalesmanName);
					stmt1.setString(1, rs.getString("SALESMANID"));
					rs1 = stmt1.executeQuery();
					while(rs1.next()){
						model.getSalesman().setUserName(rs1.getString("USERNAME"));
					}
				}
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setAddress(rs.getString("ADDRESS"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setPincode(rs.getString("PINCODE"));
				model.getCustomer().setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setTin(rs.getString("TIN"));
				model.getCustomer().setCst(rs.getString("CST"));
				model.getCustomer().setState(rs.getString("STATE"));
				model.getCustomer().setStdcode(rs.getString("STDCODE"));
				model.getCustomer().setPhone1(rs.getString("PHONE1"));
				model.getCustomer().setPhone2(rs.getString("PHONE2"));
				model.getCustomer().setMobile1(rs.getString("MOBILE1"));
				model.getCustomer().setMobile2(rs.getString("MOBILE2"));
				model.getCustomer().setEmail(rs.getString("EMAIL"));
				model.getCustomer().setEmail1(rs.getString("EMAIL1"));
				model.getCustomer().setContactPerson(rs.getString("CONTACTPERSON"));
			}	
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(rs1 != null)
					rs1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel editBill(LabourBillModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String accessId = "";

		// query to check the existence of the bill no
//		String maxBillNo = "SELECT MAX(SALESID) FROM SALES WHERE BRANCHID = ?";

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";

		// query to insert the bill details
		String updateBillDetails = "UPDATE LABOUR_BILL SET CUSTID = ? , SALESMANID = ?, REMARKS = ?, AMOUNT = ? " +
				"WHERE BILLNO = ? AND BRANCHID = ?";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, model.getBranch().getBillPrefix());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				stmt.setInt(1, model.getCustomer().getCustomerId());
				stmt.setString(2, model.getSalesman().getUserId());
				stmt.setString(3, model.getRemarks());
				stmt.setDouble(4, model.getTotalAmt());
				stmt.setInt(5, model.getSaleid());
				stmt.setString(6, accessId);
				
				stmt.execute();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getBranch().getBillPrefix()+"LB"+ model.getSaleid());
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing sales details!!");
			msgs.addNewMessage(msg);
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
		
		return msgs;
	}

	public LabourBillModel[] getTodaySalesDetails(String fromDate, String toDate, String accessId) {
		ArrayList<LabourBillModel> todayBills = new ArrayList<LabourBillModel>(0);
		LabourBillModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.BILLNO, A.BILLDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				"A.AMOUNT AS AMT " +
				"FROM LABOUR_BILL A,  CUSTOMER B " +
				"WHERE A.CUSTID = B.CUSTOMERID " +
				"AND A.BILLDATE >= ? " +
				"AND A.BILLDATE <= ? " +
				"AND A.BRANCHID = ? " +
				"GROUP BY A.BILLNO " +
				"ORDER BY A.BILLNO";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new LabourBillModel();
				model.setSaleid(rs.getInt("BILLNO"));
				model.setSalesdate(rs.getString("BILLDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				todayBills.add(model);
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
		
		return (LabourBillModel[]) todayBills.toArray(new LabourBillModel[0]);
	}

	public LabourBillModel[] getExportMaster(String fromDate,
			String toDate) {
		
		ArrayList<LabourBillModel> lst = new ArrayList<LabourBillModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		LabourBillModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.BILLNO, A.BILLDATE, A.BRANCHID, A.CUSTID, B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, A.PAYMODE, D.BILL_PREFIX, A.SALESMANID, " +
				"A.REMARKS, A.AMOUNT AS AMT FROM LABOUR_BILL A, CUSTOMER B, CITY C, ACCESS_POINT D WHERE B.CITY = C.CITY AND A.BRANCHID = D.ACCESSID AND A.CUSTID = B.CUSTOMERID " +
				"AND A.BILLDATE >= ? AND A.BILLDATE <= ? GROUP BY A.BRANCHID, A.BILLNO ORDER BY A.BRANCHID, A.BILLNO";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME FROM USER WHERE USERID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new LabourBillModel();
				model.setSaleid(rs.getInt("BILLNO"));
				model.setSalesdate(rs.getString("BILLDATE"));
				model.setRemarks(rs.getString("REMARKS"));
				if(rs.getString("SALESMANID") != null){
					model.getSalesman().setUserId(rs.getString("SALESMANID"));
					stmt1 = getConnection().prepareStatement(getSalesmanName);
					stmt1.setString(1, rs.getString("SALESMANID"));
					rs1 = stmt1.executeQuery();
					while(rs1.next()){
						model.getSalesman().setUserName(rs1.getString("USERNAME"));
					}
				}
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setAddress(rs.getString("ADDRESS"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setPincode(rs.getString("PINCODE"));
				model.getCustomer().setTin(rs.getString("TIN"));
				model.getCustomer().setCst(rs.getString("CST"));
				model.getCustomer().setState(rs.getString("STATE"));
				model.getCustomer().setStdcode(rs.getString("STDCODE"));
				model.getCustomer().setPhone1(rs.getString("PHONE1"));
				model.getCustomer().setPhone2(rs.getString("PHONE2"));
				model.getCustomer().setMobile1(rs.getString("MOBILE1"));
				model.getCustomer().setMobile2(rs.getString("MOBILE2"));
				model.getCustomer().setEmail(rs.getString("EMAIL"));
				model.getCustomer().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.setTotalAmt(rs.getDouble("AMT"));
				lst.add(model);
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(rs1 != null)
					rs1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (LabourBillModel[]) lst.toArray(new LabourBillModel[0]);
	}

}
