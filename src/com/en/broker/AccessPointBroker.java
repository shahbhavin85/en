package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.AccessPointModel;
import com.en.model.Message;
import com.en.model.MessageModel;

public class AccessPointBroker extends BaseBroker {

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public AccessPointBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new access point
	 * 
	 * @param AccessPointModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addAccessPoint(AccessPointModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long accessId = 1;
		
		// query to get the max of access point id
		String getMaxAccessId = "SELECT MAX(ACCESSID) FROM ACCESS_POINT";
		
		// query to check the existence of access point
		String checkAccess = "SELECT COUNT(*) FROM ACCESS_POINT WHERE NAME = ? AND CITY = ? AND STATUS = 'A'";
		
		// query to add the access point details details
		String insertAccess = "INSERT INTO ACCESS_POINT (ACCESSID, NAME, ADDRESS, CITY, DISTRICT, STATE, PINCODE, STDCODE, PHONE1, " +
				"PHONE2, MOBILE1, MOBILE2, FUNCTIONAL , BILL_PREFIX, VAT, CST, EMAIL, WITHTIN, NOTIN, STATUS, BANKNAME1, BANKBRANCH1, " +
				"BANKAC1, BANKIFSC1, BANKNAME2, BANKBRANCH2, BANKAC2, BANKIFSC2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'A',?,?,?,?,?,?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(checkAccess);
			stmt.setString(1, model.getAccessName());
			stmt.setString(2, model.getCity());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, model.getAccessName()+" - "+model.getCity()+" already exists.");
				messages.addNewMessage(msg);
			} else {
				stmt = getConnection().prepareStatement(getMaxAccessId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					accessId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertAccess);
				stmt.setLong(1, accessId);
				stmt.setString(2, model.getAccessName());
				stmt.setString(3, model.getAddress());
				stmt.setString(4, model.getCity());
				stmt.setString(5, model.getDistrict());
				stmt.setString(6, model.getState());
				stmt.setString(7, model.getPincode());
				stmt.setString(8, model.getStdcode());
				stmt.setString(9, model.getPhone1());
				stmt.setString(10, model.getPhone2());
				stmt.setString(11, model.getMobile1());
				stmt.setString(12, model.getMobile2());
				stmt.setString(13, model.getFunctional());
				stmt.setString(14, model.getBillPrefix());
				stmt.setString(15, model.getVat());
				stmt.setString(16, model.getCst());
				stmt.setString(17, model.getEmail());
				stmt.setString(18, model.getWithTin());
				stmt.setString(19, model.getNoTin());
				stmt.setString(20, model.getBankName1());
				stmt.setString(21, model.getBankBranch1());
				stmt.setString(22, model.getBankAc1());
				stmt.setString(23, model.getBankIfsc1());
				stmt.setString(24, model.getBankName2());
				stmt.setString(25, model.getBankBranch2());
				stmt.setString(26, model.getBankAc2());
				stmt.setString(27, model.getBankIfsc2());
				
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Access point added successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while adding an access point.");
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
	 * Method to get list all the available access point
	 *  
	 * @return array of AccessPointModel
	 */
	public AccessPointModel[] getAccessPoint(){
		ArrayList<AccessPointModel> accessPoints = new ArrayList<AccessPointModel>(0);
		AccessPointModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing access points
		String getAccessPoints = "SELECT A.ACCESSID, A.NAME, A.CITY, A.BILL_PREFIX, B.STATE FROM ACCESS_POINT A, CITY B WHERE A.CITY = B.CITY AND FUNCTIONAL = 'Y' AND STATUS = 'A' ORDER BY NAME";
		
		try{
			
			stmt = getConnection().prepareStatement(getAccessPoints);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new AccessPointModel();
				model.setAccessId(rs.getInt("ACCESSID"));
				model.setAccessName(rs.getString("NAME"));
				model.setCity(rs.getString("CITY"));
				model.setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setState(rs.getString("STATE"));
				accessPoints.add(model);
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
		
		return (AccessPointModel[]) accessPoints.toArray(new AccessPointModel[0]);
	}
	
	/**
	 * Method to get the details of the selected accessId
	 * 
	 * @param accessId
	 * @return AccessPointModel with access point details
	 */
	public AccessPointModel getAccessPointDtls(int accessId) {
		AccessPointModel model = new AccessPointModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the item group
		String getAccessPointDtls = "SELECT ACCESSID, NAME, ADDRESS, CITY, DISTRICT, STATE, PINCODE, STDCODE, PHONE1, " +
				"PHONE2, MOBILE1, MOBILE2, FUNCTIONAL , STATUS, BILL_PREFIX, VAT, CST, EMAIL, WITHTIN, NOTIN, BANKNAME1, " +
				"BANKBRANCH1, BANKAC1, BANKIFSC1, BANKNAME2, BANKBRANCH2, BANKAC2, BANKIFSC2 FROM ACCESS_POINT WHERE ACCESSID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getAccessPointDtls);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setAccessId(rs.getInt("ACCESSID"));
				model.setAccessName(rs.getString("NAME"));
				model.setAddress(rs.getString("ADDRESS"));
				model.setCity(rs.getString("CITY"));
				model.setDistrict(rs.getString("DISTRICT"));
				model.setState(rs.getString("STATE"));
				model.setPincode(rs.getString("PINCODE"));
				model.setStdcode(rs.getString("STDCODE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setPhone2(rs.getString("PHONE2"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setMobile2(rs.getString("MOBILE2"));
				model.setFunctional(rs.getString("FUNCTIONAL"));
				model.setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setVat(rs.getString("VAT"));
				model.setCst(rs.getString("CST"));
				model.setEmail(rs.getString("EMAIL"));
				model.setWithTin(rs.getString("WITHTIN"));
				model.setNoTin(rs.getString("NOTIN"));
				model.setBankName1(rs.getString("BANKNAME1"));
				model.setBankBranch1(rs.getString("BANKBRANCH1"));
				model.setBankAc1(rs.getString("BANKAC1"));
				model.setBankIfsc1(rs.getString("BANKIFSC1"));
				model.setBankName2(rs.getString("BANKNAME2"));
				model.setBankBranch2(rs.getString("BANKBRANCH2"));
				model.setBankAc2(rs.getString("BANKAC2"));
				model.setBankIfsc2(rs.getString("BANKIFSC2"));
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
	 * Method to update an existing access point
	 * 
	 * @param AccessPointModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateAccessPoint(AccessPointModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of access point
		String checkAccess = "SELECT COUNT(*) FROM ACCESS_POINT WHERE NAME = ? AND CITY = ? AND ACCESSID <> ? AND STATUS = 'A'";
		
		// query to update the item group details
		String updateItemCategory = "UPDATE ACCESS_POINT SET NAME = ?, ADDRESS = ?, CITY = ?, DISTRICT = ?, STATE = ?, PINCODE = ?, STDCODE = ?, PHONE1 = ?, " +
				"PHONE2 = ?, MOBILE1 = ?, MOBILE2 = ?, FUNCTIONAL = ?, BILL_PREFIX = ?, VAT = ?, CST = ?, EMAIL = ?, WITHTIN =?, NOTIN = ?, " +
				"BANKNAME1 = ?, BANKBRANCH1 = ?, BANKAC1 = ?, BANKIFSC1 = ?, BANKNAME2 = ?, BANKBRANCH2 = ?, BANKAC2 = ?, BANKIFSC2 = ? WHERE ACCESSID = ? AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(checkAccess);
			stmt.setString(1, model.getAccessName());
			stmt.setString(2, model.getCity());
			stmt.setInt(3, model.getAccessId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, model.getAccessName()+" - "+model.getCity()+" already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateItemCategory);
				stmt.setString(1, model.getAccessName());
				stmt.setString(2, model.getAddress());
				stmt.setString(3, model.getCity());
				stmt.setString(4, model.getDistrict());
				stmt.setString(5, model.getState());
				stmt.setString(6, model.getPincode());
				stmt.setString(7, model.getStdcode());
				stmt.setString(8, model.getPhone1());
				stmt.setString(9, model.getPhone2());
				stmt.setString(10, model.getMobile1());
				stmt.setString(11, model.getMobile2());
				stmt.setString(12, model.getFunctional());
				stmt.setString(13, model.getBillPrefix());
				stmt.setString(14, model.getVat());
				stmt.setString(15, model.getCst());
				stmt.setString(16, model.getEmail());
				stmt.setString(17, model.getWithTin());
				stmt.setString(18, model.getNoTin());
				stmt.setString(19, model.getBankName1());
				stmt.setString(20, model.getBankBranch1());
				stmt.setString(21, model.getBankAc1());
				stmt.setString(22, model.getBankIfsc1());
				stmt.setString(23, model.getBankName2());
				stmt.setString(24, model.getBankBranch2());
				stmt.setString(25, model.getBankAc2());
				stmt.setString(26, model.getBankIfsc2());
				stmt.setInt(27, model.getAccessId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Access point modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying an access point details.");
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
