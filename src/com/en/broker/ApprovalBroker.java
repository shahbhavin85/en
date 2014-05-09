package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesModel;

public class ApprovalBroker extends BaseBroker {

	public ApprovalBroker(Connection conn) {
		super(conn);
	}

	public HashMap<String, SalesModel[]> getUnapprovedSalesBills() {
		HashMap<String, SalesModel[]> dtls = new HashMap<String, SalesModel[]>(0);
		ArrayList<SalesModel> reportBills = null;
		SalesModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int accessId = 0;
		String branchName = "";

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				" A.PAYMODE, DATE_ADD(A.SALESDATE,INTERVAL A.CREDITDAYS DAY) AS DUEDATE, A.PAYDATE, A.PAYAMT, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, D.USERNAME " +
				"FROM SALES A, SALES_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.APPED = 0 ";
		outstandingBill	= outstandingBill +	"GROUP BY A.SALESID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY A.BRANCHID, A.SALESID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(accessId != rs.getInt("BRANCHID")){
					if(accessId !=0){
						dtls.put(branchName, (SalesModel[])reportBills.toArray(new SalesModel[0]));
					}
					reportBills = new ArrayList<SalesModel>(0);
					accessId = rs.getInt("BRANCHID");
					branchName = rs.getString("BILL_PREFIX");
				}
				model = new SalesModel();
				model.setSaleid(rs.getInt("SALESID"));
				model.setSalesdate(rs.getString("SALESDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setPaymode(rs.getInt("PAYMODE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setDueDate(rs.getString("DUEDATE"));
				model.setPayDate(rs.getString("PAYDATE"));
				model.setPayAmt(rs.getDouble("PAYAMT"));
				model.getSalesman().setUserName(rs.getString("USERNAME"));
				reportBills.add(model);
			}
			
			dtls.put(branchName, (SalesModel[])reportBills.toArray(new SalesModel[0]));
			
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
		return dtls;
	}

	public MessageModel approvedSalesBills(String billNo) {
		
		MessageModel msgs = new MessageModel();
		
		PreparedStatement stmt = null;

		// query to lock sales data
		String lockSalesData = "UPDATE SALES SET APPED = 1 WHERE SALESID = ? AND BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?)";
		
		try{
			
			// executing the query to lock the sales bill
			stmt = getConnection().prepareStatement(lockSalesData);
			stmt.setString(1, billNo.substring(3));
			stmt.setString(2, billNo.substring(0,3));
			
			stmt.execute();
			
			getConnection().commit();
			
			msgs.addNewMessage(new Message(SUCCESS, "Sales Bill : "+billNo+" approved successfully!!"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msgs.addNewMessage(new Message(ERROR, "Error occured while approving sales bill."));
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return msgs;
	}

}
