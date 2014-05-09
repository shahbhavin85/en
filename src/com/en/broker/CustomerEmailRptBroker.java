package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.CustomerEmailModel;

public class CustomerEmailRptBroker extends BaseBroker{

	public CustomerEmailRptBroker(Connection conn) {
		super(conn);
	}

	public CustomerEmailModel[] getMasterRpt(String customer, String fromdt, String todt) {
		ArrayList<CustomerEmailModel> reportBills = new ArrayList<CustomerEmailModel>(0);
		CustomerEmailModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.EMAILID, A.EMAILDATE, A.SUBJECT, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				" A.MESSAGE, A.SUBJECT, A.FILENAME FROM CUSTOMER_EMAIL_REFERENCE A, CUSTOMER B " +
				"WHERE A.CUSTID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.CUSTID = ? ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.EMAILDATE >= ? AND A.EMAILDATE <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"ORDER BY A.EMAILID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerEmailModel();
				model.setEmailNo(rs.getInt("EMAILID"));
				model.setEmailDate(rs.getString("EMAILDATE"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setSubject(rs.getString("SUBJECT"));
				model.setMessage(rs.getString("MESSAGE"));
				model.setFilename(rs.getString("FILENAME"));
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
		return (CustomerEmailModel[]) reportBills.toArray(new CustomerEmailModel[0]);
	}

}