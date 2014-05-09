package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.AppointmentModel;

public class SalesmanEnquiryBroker extends BaseBroker {

	public SalesmanEnquiryBroker(Connection conn) {
		super(conn);
	}

	public AppointmentModel[] getPendingAppointment(String date,
			String userId) {
		ArrayList<AppointmentModel> appointments = new ArrayList<AppointmentModel>(0);
		AppointmentModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the appointment
		String getAppointmentList = "SELECT A.APPDATE, A.APPTIME, A.SALESMAN, A.ENQNO, B.CUSTOMERNAME, B.AREA AS CUSTOMERAREA, B.CITY AS CUSTOMERCITY, A.REMARK " +
				"FROM ENQUIRY_ACTION A, CUSTOMER B, ENQUIRY_MASTER C " +
				"WHERE A.ENQNO = C.ENQNO " +
				"AND A.ACTIONTYPE = C.LAST_ACTION " +
				"AND (C.LAST_ACTION = 1 OR C.LAST_ACTION = 6) " +
				"AND B.CUSTOMERID = C.CUSTOMERID " +
				"AND A.SALESMAN = ? " +
				"AND A.APPDATE = ? " +
				"AND A.ACTIONNO = (SELECT MAX(Z.ACTIONNO) FROM ENQUIRY_ACTION Z WHERE Z.ENQNO = A.ENQNO) " +
				"ORDER BY A.APPTIME";
		
		try{
			
			stmt = getConnection().prepareStatement(getAppointmentList);
			stmt.setString(1, userId);
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new AppointmentModel();
				model.setAppDt(rs.getString("APPDATE"));
				model.setAppTm(rs.getString("APPTIME"));
				model.setEnqNo(rs.getLong("ENQNO"));
				model.setRemarks(rs.getString("REMARK"));
				model.getSalesman().setUserId(userId);
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("CUSTOMERAREA"));
				model.getCustomer().setCity(rs.getString("CUSTOMERCITY"));
				appointments.add(model);
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (AppointmentModel[]) appointments.toArray(new AppointmentModel[0]);
	}

	public boolean isPendingAppointment(String date,
			String userId) {
		boolean isPending = false;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the appointment
		String getAppointmentList = "SELECT COUNT(*) " +
				"FROM ENQUIRY_ACTION A, ENQUIRY_MASTER C " +
				"WHERE A.ENQNO = C.ENQNO " +
				"AND A.ACTIONTYPE = C.LAST_ACTION " +
				"AND (C.LAST_ACTION = 1 OR C.LAST_ACTION = 6) " +
				"AND A.SALESMAN = ? " +
				"AND A.APPDATE = ? " +
				"AND A.ACTIONNO = (SELECT MAX(Z.ACTIONNO) FROM ENQUIRY_ACTION Z WHERE Z.ENQNO = A.ENQNO) " +
				"ORDER BY A.APPTIME";
		
		try{
			
			stmt = getConnection().prepareStatement(getAppointmentList);
			stmt.setString(1, userId);
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				isPending = true;
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return isPending;
	}

}
