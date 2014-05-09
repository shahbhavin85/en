package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.AttendanceModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.UserModel;
import com.en.util.Utils;

public class AttendanceBroker extends BaseBroker {

	public AttendanceBroker(Connection conn) {
		super(conn);
	}

	public AttendanceModel[] getAttendanceDtls(String userId, String attDate) {
		ArrayList<AttendanceModel> lst = new ArrayList<AttendanceModel>();
		AttendanceModel temp = null;
		
		attDate = Utils.convertToSQLDate(attDate);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		String getUserList = "SELECT USERID, USERNAME, TYPE FROM USER WHERE MANAGER = ?";
		
		String getUserAttendance = "SELECT TYPE, OT, INTIME, OUTTIME, LATE, PANELTY, REMARK FROM USER_ATTENDANCE WHERE USERID = ? AND ATTDATE = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getUserList);
			stmt1 = getConnection().prepareStatement(getUserAttendance);
			stmt.setString(1, userId);
			stmt1.setString(2, attDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				temp = new AttendanceModel();
				temp.getUser().setUserName(rs.getString("USERNAME"));
				temp.getUser().setUserId(rs.getString("USERID"));
				temp.getUser().setType(rs.getInt("TYPE"));
				
				stmt1.setString(1, rs.getString("USERID"));
				
				rs1 = stmt1.executeQuery();
				
				if(rs1.next()){
					temp.setType(rs1.getString("TYPE"));
					temp.setOt(rs1.getString("OT"));
					temp.setInTime(rs1.getString("INTIME"));
					temp.setOutTime(rs1.getString("OUTTIME"));
					temp.setLateFine(rs1.getString("LATE"));
					temp.setPanelty(rs1.getString("PANELTY"));
					temp.setRemarks(rs1.getString("REMARK"));
				}
				lst.add(temp);
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
				if(rs1 != null)
					rs1.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (AttendanceModel[]) lst.toArray(new AttendanceModel[0]);
	}

	@SuppressWarnings("resource")
	public MessageModel updateAttendance(String attDate,
			ArrayList<AttendanceModel> lst) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		attDate = Utils.convertToSQLDate(attDate);
		AttendanceModel[] arrModel = (AttendanceModel[])lst.toArray(new AttendanceModel[0]);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to delete the preivous details
		String deleteDetails = "DELETE FROM USER_ATTENDANCE WHERE USERID = ? AND ATTDATE = ? AND (USERTYPE = 4 OR USERTYPE = 5)";
		
		// query to insert the new details
		String insertDetails = "INSERT INTO USER_ATTENDANCE (USERID, ATTDATE, TYPE, OT, INTIME, OUTTIME, LATE, PANELTY, USERTYPE, REMARK) VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		try {
				
				stmt = getConnection().prepareStatement(deleteDetails);
				
				for(int i=0; i<arrModel.length; i++){
					stmt.setString(1, arrModel[i].getUser().getUserId());
					stmt.setString(2, attDate);
					stmt.addBatch();
				}
				
				stmt.executeBatch();
				stmt.clearBatch();
				
				stmt = getConnection().prepareStatement(insertDetails);
				
				for(int i=0; i<arrModel.length; i++){
					stmt.setString(1, arrModel[i].getUser().getUserId());
					stmt.setString(2, attDate);
					stmt.setString(3, arrModel[i].getType());
					stmt.setString(4, arrModel[i].getOt());
					stmt.setString(5, arrModel[i].getInTime());
					stmt.setString(6, arrModel[i].getOutTime());
					stmt.setString(7, arrModel[i].getLateFine());
					stmt.setString(8, arrModel[i].getPanelty());
					stmt.setInt(9, arrModel[i].getUser().getType());
					stmt.setString(10, arrModel[i].getRemarks());
					stmt.addBatch();
				}
				
				stmt.executeBatch();
				stmt.clearBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Attendance details updated successfully!!");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while updating attendance details!!");
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

	@SuppressWarnings("resource")
	public MessageModel updateUserAttendance(String attDate,
			AttendanceModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		attDate = Utils.convertToSQLDate(attDate);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to delete the preivous details
		String deleteDetails = "DELETE FROM USER_ATTENDANCE WHERE USERID = ? AND ATTDATE = ?";
		
		// query to insert the new details
		String insertDetails = "INSERT INTO USER_ATTENDANCE (USERID, ATTDATE, TYPE, OT, INTIME, OUTTIME, LATE, PANELTY, USERTYPE, REMARK) VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		try {
				
				stmt = getConnection().prepareStatement(deleteDetails);
				
				stmt.setString(1, model.getUser().getUserId());
				stmt.setString(2, attDate);

				stmt.execute();
					
				stmt = getConnection().prepareStatement(insertDetails);
				
				stmt.setString(1, model.getUser().getUserId());
				stmt.setString(2, attDate);
				stmt.setString(3, model.getType());
				stmt.setString(4, model.getOt());
				stmt.setString(5, model.getInTime());
				stmt.setString(6, model.getOutTime());
				stmt.setString(7, model.getLateFine());
				stmt.setString(8, model.getPanelty());
				stmt.setInt(9, model.getUser().getType());
				stmt.setString(10, model.getRemarks());
				
				stmt.execute();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Attendance details updated successfully!!");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while updating attendance details!!");
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

}
