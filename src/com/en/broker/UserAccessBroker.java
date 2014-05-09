package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;

public class UserAccessBroker extends BaseBroker {

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public UserAccessBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to get the user access details of the selected userId
	 * 
	 * @param userId
	 * @return UserModel with user access details
	 */
	public UserModel getUserAccessDtls(String userId) {
		UserModel model = new UserModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the user
		String getUserDtls = "SELECT ACCESSID FROM USER_ACCESS " +
				"WHERE USERID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getUserDtls);
			stmt.setString(1, userId);
			
			rs = stmt.executeQuery();
			
			model.setUserId(userId);
			
			while(rs.next()){
				model.addAccessPoints(rs.getString("ACCESSID"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(getConnection() != null)
					getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(getConnection() != null)
					getConnection().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return model;
	}
	
	/**
	 * Method to update an user access
	 * 
	 * @param UserModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateUserAccess(UserModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		boolean check = false;
		
		// query to delete the previous access details
		String deleteUserAccess = "DELETE FROM USER_ACCESS WHERE USERID = ?";
		
		// query to insert the user access details
		String insertUserAccess = "INSERT INTO USER_ACCESS (USERID, ACCESSID) VALUES (?,?)";
		
		try{
				
			stmt = getConnection().prepareStatement(deleteUserAccess);
			stmt.setString(1, model.getUserId());
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(insertUserAccess);
			Iterator<String> accessPtsItr = model.getAccessPoints().iterator();
			while(accessPtsItr.hasNext()){
				stmt.setString(1, model.getUserId());
				stmt.setString(2, accessPtsItr.next());
				stmt.addBatch();
				check = true;
			}
			
			if((check && stmt.executeBatch()[0] > 0) || !check){
				msg = new Message(SUCCESS, "User Access modified successfully.");
			} else {
				msg = new Message(ALERT, "Error occured while modifying an User Access.");
			}
			messages.addNewMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
			try{
				if(stmt != null)
					stmt.close();
				if(getConnection() != null)
					getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(getConnection() != null)
					getConnection().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return messages;
	}

}
