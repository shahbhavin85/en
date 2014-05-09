package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.en.model.AccessPointModel;
import com.en.model.UserModel;


public class LoginBroker extends BaseBroker {

	public LoginBroker(Connection conn) {
		super(conn);
	}
	
	public UserModel checkAccess(String userName, String password, String accessPoint){
		UserModel user = null;
		boolean isDataRight = false;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		StringBuffer checkAccess = new StringBuffer(0);
		checkAccess.append("SELECT * FROM USER WHERE USERID = ? AND PASSWORD = ?");
		
		StringBuffer checkPointAccess = new StringBuffer(0);
		checkPointAccess.append("SELECT COUNT(*) FROM USER_ACCESS WHERE USERID = ? AND ACCESSID = ?");
		
		try{
			
			stmt = getConnection().prepareStatement(checkPointAccess.toString());
			stmt.setString(1, userName);
			stmt.setString(2, accessPoint);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				isDataRight = true;
			}
			
			if(isDataRight){
			
				stmt = getConnection().prepareStatement(checkAccess.toString());
				stmt.setString(1, userName);
				stmt.setString(2, password);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					user = (new UserBroker(getConnection())).getUserDtls(rs.getString("USERID"));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				getConnection().rollback(); 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		} finally {
			try {
				getConnection().commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return user;
	}
	
	public boolean changePassword(String userName, String password, String newPassword){
		boolean isAccess = false;
		
		PreparedStatement stmt = null;
		
		StringBuffer checkAccess = new StringBuffer(0);
		checkAccess.append("UPDATE USER SET PASS = ? WHERE USER = ? AND PASS = ?");
		
		try{
			
			stmt = getConnection().prepareStatement(checkAccess.toString());
			stmt.setString(1, newPassword);
			stmt.setString(2, userName);
			stmt.setString(3, password);
			
			
			if(stmt.executeUpdate()>0){
				isAccess = true;
			}

			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isAccess;
	}

	public ArrayList<AccessPointModel> getAccessPoints() {
		ArrayList<AccessPointModel> accesspoints = new ArrayList<AccessPointModel>(0);
		AccessPointModel accesspoint = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the list of access points  
		StringBuffer getAccessPointQuery = new StringBuffer(0);
		getAccessPointQuery.append("SELECT ACCESSID, NAME, CITY FROM ACCESS_POINT WHERE STATUS = 'A' ORDER BY NAME");
			
		try {
				
			stmt = getConnection().prepareStatement(getAccessPointQuery.toString());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				accesspoint = new AccessPointModel();
				accesspoint.setAccessId(rs.getInt("ACCESSID"));
				accesspoint.setAccessName(rs.getString("NAME"));
				accesspoint.setCity(rs.getString("CITY"));
				accesspoints.add(accesspoint);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accesspoints;
	}

	public AccessPointModel[] getUserAccessList(UserModel user) {
		ArrayList<AccessPointModel> lst = new ArrayList<AccessPointModel>(0);
		AccessPointModel temp = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the user
		String getUserDtls = "SELECT A.ACCESSID, B.NAME, B.CITY FROM USER_ACCESS A, ACCESS_POINT B " +
				"WHERE A.USERID = ? AND A.ACCESSID = B.ACCESSID";

		try{ 
			
			stmt = getConnection().prepareStatement(getUserDtls);
			stmt.setString(1, user.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				temp = new AccessPointModel();
				temp.setAccessId(rs.getInt("ACCESSID"));
				temp.setAccessName(rs.getString("NAME"));
				temp.setCity(rs.getString("CITY"));
				lst.add(temp);
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
		
		return (AccessPointModel[]) lst.toArray(new AccessPointModel[0]);
	}



}
