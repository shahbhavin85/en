package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.PurchaseReturnModel;

public class PurchaseReturnRptBroker extends BaseBroker{

	public PurchaseReturnRptBroker(Connection conn) {
		super(conn);
	}

	public PurchaseReturnModel[] getMasterRpt(String customer, String[] branch,
			String fromdt, String todt, String[] billtype) {
		ArrayList<PurchaseReturnModel> reportBills = new ArrayList<PurchaseReturnModel>(0);
		PurchaseReturnModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.RETURNID, A.RETURNDATE, A.BRANCHID, E.BILL_PREFIX, A.SUPPID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				"A.TAXTYPE, SUM(C.QTY*((100+C.TAX)*C.RATE/100)) AS AMT " +
				"FROM PURCHASE_RETURN A, PURCHASE_RETURN_ITEM C, CUSTOMER B, ACCESS_POINT E " +
				"WHERE A.RETURNID = C.RETURNID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SUPPID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SUPPID = ? ";
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
			outstandingBill	= outstandingBill +	"AND A.RECDDT >= ? AND A.RECDDT <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"GROUP BY A.RETURNID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY A.RETURNID, A.BRANCHID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
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
				model = new PurchaseReturnModel();
				model.setPurchaseId(rs.getInt("RETURNID"));
				model.setReturnDt(rs.getString("RETURNDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getSupplier().setCustomerId(rs.getInt("SUPPID"));
				model.getSupplier().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getSupplier().setArea(rs.getString("AREA"));
				model.getSupplier().setCity(rs.getString("CITY"));
				model.setBillType(rs.getInt("TAXTYPE"));
				model.setTotalAmt(rs.getDouble("AMT"));
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
		return (PurchaseReturnModel[]) reportBills.toArray(new PurchaseReturnModel[0]);
	}

}
