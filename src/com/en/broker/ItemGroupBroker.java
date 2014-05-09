package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.ItemGroupModel;
import com.en.model.Message;
import com.en.model.MessageModel;

public class ItemGroupBroker extends BaseBroker {

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public ItemGroupBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new Item Group
	 * 
	 * @param ItemGroupModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addItemGroup(ItemGroupModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long groupId = 1;
		
		// query to check the existence of item group
		String checkItemGroup = "SELECT COUNT(*) FROM ITEM_GROUP WHERE GROUPNAME = ? AND STATUS = 'A'";
		
		// query to get the max of item group id
		String getMaxItemGroupId = "SELECT MAX(GROUPID) FROM ITEM_GROUP";
		
		// query to add the item group details
		String insertItemGroup = "INSERT INTO ITEM_GROUP (GROUPID, GROUPNAME, STATUS) VALUES (?,?,'A')";
		
		try{
			
			stmt = getConnection().prepareStatement(checkItemGroup);
			stmt.setString(1, model.getItemGroup());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Item Group already exists.");
				messages.addNewMessage(msg);
			} else {
				stmt = getConnection().prepareStatement(getMaxItemGroupId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					groupId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertItemGroup);
				stmt.setLong(1, groupId);
				stmt.setString(2, model.getItemGroup());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Item Group added successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while adding an Item Group.");
				}
				messages.addNewMessage(msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}
	
	/**
	 * Method to get list all the available item group
	 *  
	 * @return array of ItemGroupModel
	 */
	public ItemGroupModel[] getItemGroups(){
		ArrayList<ItemGroupModel> itemGrps = new ArrayList<ItemGroupModel>(0);
		ItemGroupModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT GROUPID, GROUPNAME FROM ITEM_GROUP WHERE STATUS = 'A' ORDER BY GROUPNAME";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemGroupModel();
				model.setItemGroupId(rs.getInt("GROUPID"));
				model.setItemGroup(rs.getString("GROUPNAME"));
				itemGrps.add(model);
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
		
		return (ItemGroupModel[]) itemGrps.toArray(new ItemGroupModel[0]);
	}
	
	/**
	 * Method to get the details of the selected groupid
	 * 
	 * @param groupId
	 * @return ItemGroupModel with item group details
	 */
	public ItemGroupModel getItemGroupDtls(int groupId) {
		ItemGroupModel model = new ItemGroupModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the item group
		String getItemGrpDtls = "SELECT GROUPNAME FROM ITEM_GROUP WHERE GROUPID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getItemGrpDtls);
			stmt.setInt(1, groupId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setItemGroupId(groupId);
				model.setItemGroup(rs.getString("GROUPNAME"));
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
	 * Method to update an existing Item Group
	 * 
	 * @param ItemGroupModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateItemGroup(ItemGroupModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of item group
		String checkItemGroup = "SELECT COUNT(*) FROM ITEM_GROUP WHERE GROUPNAME = ? AND STATUS = 'A'";
		
		// query to update the item group details
		String updateItemGroup = "UPDATE ITEM_GROUP SET GROUPNAME = ? WHERE GROUPID = ? AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(checkItemGroup);
			stmt.setString(1, model.getItemGroup());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Item Group already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateItemGroup);
				stmt.setString(1, model.getItemGroup());
				stmt.setInt(2, model.getItemGroupId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Item Group modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying an Item Group.");
				}
				messages.addNewMessage(msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}

}
