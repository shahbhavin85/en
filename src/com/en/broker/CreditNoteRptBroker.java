package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.CreditNoteModel;

public class CreditNoteRptBroker extends BaseBroker{

	public CreditNoteRptBroker(Connection conn) {
		super(conn);
	}

	public CreditNoteModel[] getMasterRpt(String customer, String salesman,
			String[] branch, String fromdt, String todt, String[] billtype) {
		ArrayList<CreditNoteModel> reportBills = new ArrayList<CreditNoteModel>(0);
		CreditNoteModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.CREDITNOTEID, A.NOTEDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				"A.TAXTYPE, SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.LESS, D.USERNAME " +
				"FROM CREDIT_NOTE A, CREDIT_NOTE_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.CREDITNOTEID = C.CREDITNOTEID " +
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
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.NOTEDATE >= ? AND A.NOTEDATE <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"GROUP BY A.CREDITNOTEID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY A.NOTEDATE, A.BRANCHID, A.CREDITNOTEID";
		
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
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CreditNoteModel();
				model.setSaleid(rs.getInt("CREDITNOTEID"));
				model.setSalesdate(rs.getString("NOTEDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setLess(rs.getDouble("LESS"));
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
		return (CreditNoteModel[]) reportBills.toArray(new CreditNoteModel[0]);
	}

}
