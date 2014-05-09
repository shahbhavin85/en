package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.CustomerGroupModel;
import com.en.model.Message;
import com.en.model.MessageModel;

public class CustomerGroupBroker extends BaseBroker{

	public CustomerGroupBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new Customer Group
	 * 
	 * @param CustomerGroupModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addCustomerGroup(CustomerGroupModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long groupId = 1;
		
		// query to check the existence of customer group
		String checkCustGroup = "SELECT COUNT(*) FROM CUSTOMER_GROUP WHERE GROUPNAME = ? AND STATUS = 'A'";
		
		// query to get the max of customer group id
		String getMaxCustGroupId = "SELECT MAX(GROUPID) FROM CUSTOMER_GROUP";
		
		// query to add the customer group details
		String insertItemGroup = "INSERT INTO CUSTOMER_GROUP (GROUPID, GROUPNAME, STATUS) VALUES (?,?,'A')";
		
		try{
			
			stmt = getConnection().prepareStatement(checkCustGroup);
			stmt.setString(1, model.getCustGroup());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Customer Group already exists.");
				messages.addNewMessage(msg);
			} else {
				stmt = getConnection().prepareStatement(getMaxCustGroupId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					groupId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertItemGroup);
				stmt.setLong(1, groupId);
				stmt.setString(2, model.getCustGroup());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Customer Group added successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while adding an Customer Group.");
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
	 * Method to get list all the available customer group
	 *  
	 * @return array of CustomerGroupModel
	 */
	public CustomerGroupModel[] getCustGroups(){
		ArrayList<CustomerGroupModel> custGrps = new ArrayList<CustomerGroupModel>(0);
		CustomerGroupModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customer group
		String getCustGroups = "SELECT GROUPID, GROUPNAME FROM CUSTOMER_GROUP WHERE STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerGroupModel();
				model.setCustGroupId(rs.getInt("GROUPID"));
				model.setCustGroup(rs.getString("GROUPNAME"));
				custGrps.add(model);
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
		
		return (CustomerGroupModel[]) custGrps.toArray(new CustomerGroupModel[0]);
	}
	
	/**
	 * Method to get the details of the selected groupid
	 * 
	 * @param groupId
	 * @return CustomerGroupModel with item group details
	 */
	public CustomerGroupModel getCustGroupDtls(int groupId) {
		CustomerGroupModel model = new CustomerGroupModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the customer group
		String getCustGrpDtls = "SELECT GROUPNAME FROM CUSTOMER_GROUP WHERE GROUPID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getCustGrpDtls);
			stmt.setInt(1, groupId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setCustGroupId(groupId);
				model.setCustGroup(rs.getString("GROUPNAME"));
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
	 * Method to update an existing customer Group
	 * 
	 * @param CustomerGroupModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateCustomerGroup(CustomerGroupModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of customer group
		String checkCustGroup = "SELECT COUNT(*) FROM CUSTOMER_GROUP WHERE GROUPNAME = ? AND STATUS = 'A'";
		
		// query to update the customer group details
		String updateCustGroup = "UPDATE CUSTOMER_GROUP SET GROUPNAME = ? WHERE GROUPID = ? AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(checkCustGroup);
			stmt.setString(1, model.getCustGroup());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Customer Group already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateCustGroup);
				stmt.setString(1, model.getCustGroup());
				stmt.setInt(2, model.getCustGroupId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Customer Group modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying an Customer Group.");
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
