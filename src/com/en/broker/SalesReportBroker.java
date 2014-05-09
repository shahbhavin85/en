package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import com.en.model.SalesModel;
import com.en.util.SalesDateComparator;

public class SalesReportBroker extends BaseBroker {

	public SalesReportBroker(Connection conn) {
		super(conn);
	}

	public SalesModel[] getMasterRpt(String customer, String salesman,
			String[] branch, String fromdt, String todt, String[] billtype, String[] payMode) {
		ArrayList<SalesModel> reportBills = new ArrayList<SalesModel>(0);
		SalesModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				" A.PAYMODE, DATE_ADD(A.SALESDATE,INTERVAL A.CREDITDAYS DAY) AS DUEDATE, A.PAYDATE, A.PAYAMT, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, D.USERNAME, A.STATUS " +
				"FROM SALES A, SALES_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.CUSTID = ? ";
		}
		if(!salesman.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SALESMANID = ? ";
		}
		if(branch.length!=0 && !branch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<branch.length; k++){
				outstandingBill	= outstandingBill +	"A.BRANCHID = ? ";
				if(k !=branch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(billtype.length !=3){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<billtype.length; k++){
				outstandingBill	= outstandingBill +	"A.TAXTYPE = ? ";
				if(k !=billtype.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(payMode.length !=3){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<payMode.length; k++){
				outstandingBill	= outstandingBill +	"A.PAYMODE = ? ";
				if(k !=payMode.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SALESDATE >= ? AND A.SALESDATE <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"GROUP BY A.SALESID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY A.SALESDATE, A.BRANCHID, A.SALESID";

		// query to check the existence of the bill no
		String outstandingCancelBill = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				" A.PAYMODE, DATE_ADD(A.SALESDATE,INTERVAL A.CREDITDAYS DAY) AS DUEDATE, A.PAYDATE, A.PAYAMT, A.TAXTYPE, " +
				"A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, D.USERNAME, A.STATUS " +
				"FROM SALES A, SALES_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.STATUS = 1 " +
				"AND A.CUSTID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingCancelBill	= outstandingCancelBill +	"AND A.CUSTID = ? ";
		}
		if(!salesman.equals("")){
			outstandingCancelBill	= outstandingCancelBill +	"AND A.SALESMANID = ? ";
		}
		if(branch.length!=0 && !branch[0].equals("--")){
			outstandingCancelBill	= outstandingCancelBill +	"AND (";
			for(int k=0; k<branch.length; k++){
				outstandingCancelBill	= outstandingCancelBill +	"A.BRANCHID = ? ";
				if(k !=branch.length-1){
					outstandingCancelBill	= outstandingCancelBill +	"OR ";
				}
			}
			outstandingCancelBill	= outstandingCancelBill +	") ";
		}
		if(billtype.length !=3){
			outstandingCancelBill	= outstandingCancelBill +	"AND (";
			for(int k=0; k<billtype.length; k++){
				outstandingCancelBill	= outstandingCancelBill +	"A.TAXTYPE = ? ";
				if(k !=billtype.length-1){
					outstandingCancelBill	= outstandingCancelBill +	"OR ";
				}
			}
			outstandingCancelBill	= outstandingCancelBill +	") ";
		}
		if(payMode.length !=3){
			outstandingCancelBill	= outstandingCancelBill +	"AND (";
			for(int k=0; k<payMode.length; k++){
				outstandingCancelBill	= outstandingCancelBill +	"A.PAYMODE = ? ";
				if(k !=payMode.length-1){
					outstandingCancelBill	= outstandingCancelBill +	"OR ";
				}
			}
			outstandingCancelBill	= outstandingCancelBill +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingCancelBill	= outstandingCancelBill +	"AND A.SALESDATE >= ? AND A.SALESDATE <= ? ";
		}
		

		outstandingCancelBill	= outstandingCancelBill +	"GROUP BY A.SALESID, A.BRANCHID ";
		outstandingCancelBill	= outstandingCancelBill +	"ORDER BY A.SALESDATE, A.BRANCHID, A.SALESID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(!salesman.equals("")){
				stmt.setString(i, salesman);
				i++;
			}
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
					i++;
				}
			}
			if(billtype.length !=3){
				for(int k=0; k<billtype.length; k++){
					stmt.setString(i, billtype[k]);
					i++;
				}
			}
			if(payMode.length !=3){
				for(int k=0; k<payMode.length; k++){
					stmt.setString(i, payMode[k]);
					i++;
				}
			}
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
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
				model.setStatus(rs.getInt("STATUS"));
				reportBills.add(model);
			}
			
			i = 1;
			
			stmt = getConnection().prepareStatement(outstandingCancelBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(!salesman.equals("")){
				stmt.setString(i, salesman);
				i++;
			}
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
					i++;
				}
			}
			if(billtype.length !=3){
				for(int k=0; k<billtype.length; k++){
					stmt.setString(i, billtype[k]);
					i++;
				}
			}
			if(payMode.length !=3){
				for(int k=0; k<payMode.length; k++){
					stmt.setString(i, payMode[k]);
					i++;
				}
			}
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
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
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setDueDate(rs.getString("DUEDATE"));
				model.setPayDate(rs.getString("PAYDATE"));
				model.setPayAmt(rs.getDouble("PAYAMT"));
				model.getSalesman().setUserName(rs.getString("USERNAME"));
				model.setStatus(rs.getInt("STATUS"));
				reportBills.add(model);
			}
			
			Collections.sort(reportBills, new SalesDateComparator());
			
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
		return (SalesModel[]) reportBills.toArray(new SalesModel[0]);
	}

}
