package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.LabourBillModel;

public class LabourBillRptBroker extends BaseBroker {

	public LabourBillRptBroker(Connection conn) {
		super(conn);
	}

	public LabourBillModel[] getMasterRpt(String customer, String salesman,
			String[] branch, String fromdt, String todt) {
		ArrayList<LabourBillModel> reportBills = new ArrayList<LabourBillModel>(0);
		LabourBillModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.BILLNO, A.BILLDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				" A.PAYDATE, A.PAYAMT, A.AMOUNT AS AMT, D.USERNAME " +
				"FROM LABOUR_BILL A, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.BRANCHID = E.ACCESSID " +
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
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SALESDATE >= ? AND A.SALESDATE <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"ORDER BY A.BILLDATE, A.BRANCHID, A.BILLNO";
		
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
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new LabourBillModel();
				model.setSaleid(rs.getInt("BILLNO"));
				model.setSalesdate(rs.getString("BILLDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPayDate(rs.getString("PAYDATE"));
				model.setPayAmt(rs.getDouble("PAYAMT"));
				model.getSalesman().setUserName(rs.getString("USERNAME"));
				reportBills.add(model);
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
		return (LabourBillModel[]) reportBills.toArray(new LabourBillModel[0]);
	}

}
