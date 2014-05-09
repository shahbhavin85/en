package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.AlertModel;
import com.en.util.Utils;

public class EnquiryRptBroker extends BaseBroker {

	public EnquiryRptBroker(Connection conn) {
		super(conn);
	}

	public AlertModel[] getAlertReport(String accesspoint, String fromDate,
			String toDate) {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>(0);
		AlertModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the appointment
		String getAppointmentList = "SELECT A.APPDATE, A.ENQNO, B.CUSTOMERNAME, B.AREA AS CUSTOMERAREA, B.CITY AS CUSTOMERCITY, A.REMARK, D.NAME, D.CITY " +
				"FROM ENQUIRY_ACTION A, CUSTOMER B, ENQUIRY_MASTER C, ACCESS_POINT D " +
				"WHERE A.ENQNO = C.ENQNO " +
				"AND A.ACTIONTYPE = C.LAST_ACTION " +
				"AND C.LAST_ACTION = 5 " +
				"AND B.CUSTOMERID = C.CUSTOMERID " +
				"AND C.CURRENT_POINT = D.ACCESSID " +
				"AND A.APPDATE >= ? " +
				"AND A.APPDATE <= ? " +
				"AND A.ACTIONNO = (SELECT MAX(Z.ACTIONNO) FROM ENQUIRY_ACTION Z WHERE Z.ENQNO = A.ENQNO) "; 
		if(!accesspoint.equals("--")){
			getAppointmentList = getAppointmentList + "AND C.CURRENT_POINT = ? ";
		}
		getAppointmentList = getAppointmentList + "ORDER BY A.APPDATE";
		
		try{
			
			stmt = getConnection().prepareStatement(getAppointmentList);
			stmt.setString(1, Utils.convertToSQLDate(fromDate));
			stmt.setString(2, Utils.convertToSQLDate(toDate));
			if(!accesspoint.equals("--")){
				stmt.setString(3, accesspoint);
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new AlertModel();
				model.setAppDt(rs.getString("APPDATE"));
				model.setEnqNo(rs.getLong("ENQNO"));
				model.setRemarks(rs.getString("REMARK"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("CUSTOMERAREA"));
				model.getCustomer().setCity(rs.getString("CUSTOMERCITY"));
				model.getCurrentpoint().setAccessName(rs.getString("NAME"));
				model.getCurrentpoint().setCity(rs.getString("CITY"));
				alerts.add(model);
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (AlertModel[]) alerts.toArray(new AlertModel[0]);
	}

}
