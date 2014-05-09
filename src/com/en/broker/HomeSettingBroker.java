package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.Message;
import com.en.model.MessageModel;

public class HomeSettingBroker extends BaseBroker {

	public HomeSettingBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * 
	 * @param details
	 * @return
	 */
	public MessageModel updateHomeSettingDtls(HashMap<String, String> details){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to update home setting details
		String updateHomeDtls = "UPDATE HOME_SETTINGS SET DESP = ? WHERE DESPKEY = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(updateHomeDtls);
			
			Iterator<String> keys = details.keySet().iterator();
			String key = "";
			
			while(keys.hasNext()){
				key = keys.next();
				stmt.setString(1, details.get(key));
				stmt.setString(2, key);
				stmt.execute();
			}
			
			msg = new Message(SUCCESS, "Home Setting details updated successfully.");
			messages.addNewMessage(msg);
			
			getConnection().commit();
			
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
			}}
		
		return messages;
	}
	
	public HashMap<String, String> getHomeDetails(){
		HashMap<String, String> homeDetails = new HashMap<String, String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get home setting details
		String getHomeDtls = "SELECT DESPKEY, DESP FROM HOME_SETTINGS";
		
		try{
			
			stmt = getConnection().prepareStatement(getHomeDtls);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				homeDetails.put(rs.getString("DESPKEY"), rs.getString("DESP"));
			}
			
			stmt.executeBatch();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
				if(getConnection() != null)
					getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}}
		
		return homeDetails;
	}

}
