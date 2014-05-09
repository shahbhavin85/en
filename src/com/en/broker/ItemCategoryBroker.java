package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.ItemCategoryModel;
import com.en.model.Message;
import com.en.model.MessageModel;

public class ItemCategoryBroker extends BaseBroker {

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public ItemCategoryBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new Item category
	 * 
	 * @param ItemCategoryModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addItemCateogry(ItemCategoryModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long categoryId = 1;
		
		// query to check the existence of item category
		String checkItemCategory = "SELECT COUNT(*) FROM ITEM_CATEGORY WHERE CATEGORYNAME = ? AND GROUPID = ? AND STATUS = 'A'";
		
		// query to get the max of item category id
		String getMaxItemCategoryId = "SELECT MAX(CATEGORYID) FROM ITEM_CATEGORY";
		
		// query to add the item category details
		String insertItemCategory = "INSERT INTO ITEM_CATEGORY (CATEGORYID, CATEGORYNAME, GROUPID, STATUS) VALUES (?,?,?,'A')";
		
		try{
			
			stmt = getConnection().prepareStatement(checkItemCategory);
			stmt.setString(1, model.getItemCategory());
			stmt.setInt(2, model.getItemGroupId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Item Category - Item Group combination already exists.");
				messages.addNewMessage(msg);
			} else {
				stmt = getConnection().prepareStatement(getMaxItemCategoryId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					categoryId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertItemCategory);
				stmt.setLong(1, categoryId);
				stmt.setString(2, model.getItemCategory());
				stmt.setInt(3, model.getItemGroupId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Item Category added successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while adding an Item Category.");
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
	 * Method to get list all the available item categories
	 *  
	 * @return array of ItemCategoryModel
	 */
	public ItemCategoryModel[] getItemCategory(){
		ArrayList<ItemCategoryModel> itemCats = new ArrayList<ItemCategoryModel>(0);
		ItemCategoryModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT A.GROUPNAME, A.GROUPID, B.CATEGORYID, CATEGORYNAME FROM ITEM_GROUP A, ITEM_CATEGORY B WHERE B.GROUPID = A.GROUPID AND B.STATUS = 'A' ORDER BY GROUPNAME";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemCategoryModel();
				model.setItemGroupId(rs.getInt("GROUPID"));
				model.setItemGroup(rs.getString("GROUPNAME"));
				model.setItemCatId(rs.getInt("CATEGORYID"));
				model.setItemCategory(rs.getString("CATEGORYNAME"));
				itemCats.add(model);
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
		
		return (ItemCategoryModel[]) itemCats.toArray(new ItemCategoryModel[0]);
	}
	
	/**
	 * Method to get the details of the selected categoryid
	 * 
	 * @param categoryId
	 * @return ItemCategoryModel with item group details
	 */
	public ItemCategoryModel getItemCategoryDtls(int categoryId) {
		ItemCategoryModel model = new ItemCategoryModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the item group
		String getItemGrpDtls = "SELECT GROUPID, CATEGORYNAME FROM ITEM_CATEGORY WHERE CATEGORYID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getItemGrpDtls);
			stmt.setInt(1, categoryId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setItemGroupId(rs.getInt("GROUPID"));
				model.setItemCategory(rs.getString("CATEGORYNAME"));
				model.setItemCatId(categoryId);
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
	 * Method to update an existing Item category
	 * 
	 * @param ItemGroupModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateItemCategory(ItemCategoryModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of item category
		String checkItemCategory = "SELECT COUNT(*) FROM ITEM_CATEGORY WHERE CATEGORYNAME = ? AND GROUPID = ? AND STATUS = 'A'";
		
		// query to update the item group details
		String updateItemCategory = "UPDATE ITEM_CATEGORY SET CATEGORYNAME = ? , GROUPID = ? WHERE CATEGORYID = ? AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(checkItemCategory);
			stmt.setString(1, model.getItemCategory());
			stmt.setInt(2, model.getItemGroupId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Item Category - Item Group combination already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateItemCategory);
				stmt.setString(1, model.getItemCategory());
				stmt.setInt(2, model.getItemGroupId());
				stmt.setInt(3, model.getItemCatId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Item Category modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying an Item category.");
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
