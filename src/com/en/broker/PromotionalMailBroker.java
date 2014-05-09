package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PromotionalMailBroker extends BaseBroker {

	public PromotionalMailBroker(Connection conn) {
		super(conn);
	}

	public synchronized void addMailData(String subject, String img) {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long mailId = 1;
		
		// query to get the max of item group id
		String getMaxItemGroupId = "SELECT MAX(MAILID) FROM PROMOTIONAL_MAIL";
		
		// query to add the item group details
		String insertItemGroup = "INSERT INTO PROMOTIONAL_MAIL (MAILID, SUBJECT, IMG, CNT) VALUES (?,?,?,1)";
		
		try{
			
			stmt = getConnection().prepareStatement(getMaxItemGroupId);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1) > 0){
				mailId = rs.getLong(1) + 1;
			}
			
			stmt = getConnection().prepareStatement(insertItemGroup);
			stmt.setLong(1, mailId);
			stmt.setString(2, subject);
			stmt.setString(3, img);
				
			stmt.execute();
			
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
		
		return;
	}

	public String[] getEmails(int i, String string) {
		ArrayList<String> emails = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the customer
		String getItemDtls = "SELECT EMAIL, EMAIL1 FROM CUSTOMER WHERE STATUS = 'A' AND ISSUPPLIER = 'N' AND (EMAIL <> '' OR EMAIL1 <> '') ";
		if(i == 1){
			getItemDtls = getItemDtls + "AND CUSTOMERTYPE = ?";
		} else if(i == 2){
			getItemDtls = getItemDtls + "AND CITY = ?";
		} else if(i == 3){
			getItemDtls = getItemDtls + "AND CITY IN (SELECT Z.CITY FROM CITY Z WHERE Z.STATE = ?)";
		}
	
		//query to get the mobile no of staff
		String getStaffEmails = "SELECT EMAIL2, EMAIL1 FROM USER";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			if(i!=0){
				stmt.setString(1, string);
			}
			
			rs = stmt.executeQuery();
			
			while (rs.next()){
				if(rs.getString("EMAIL") !=null && !rs.getString("EMAIL").equals("")){  emails.add(rs.getString("EMAIL").toLowerCase());}
				if(rs.getString("EMAIL1") !=null && !rs.getString("EMAIL1").equals("")){  emails.add(rs.getString("EMAIL1").toLowerCase());}
			}
			
			stmt = getConnection().prepareStatement(getStaffEmails);
			
			rs = stmt.executeQuery();
			
			while (rs.next()){
				if(rs.getString("EMAIL2") !=null && !rs.getString("EMAIL2").equals("")){  emails.add(rs.getString("EMAIL2").toLowerCase());}
				if(rs.getString("EMAIL1") !=null && !rs.getString("EMAIL1").equals("")){  emails.add(rs.getString("EMAIL1").toLowerCase());}
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
		
		return (String[]) emails.toArray(new String[0]);
	}

	public String[] getMobileNos(int i, String string) {
		ArrayList<String> emails = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the customer
		String getItemDtls = "SELECT MOBILE1, MOBILE2 FROM CUSTOMER WHERE STATUS = 'A' AND ISSUPPLIER = 'N' AND (MOBILE1 <> '' OR MOBILE2 <> '') ";
		if(i == 1){
			getItemDtls = getItemDtls + "AND CUSTOMERTYPE = ?";
		} else if(i == 2){
			getItemDtls = getItemDtls + "AND CITY = ?";
		} else if(i == 3){
			getItemDtls = getItemDtls + "AND CITY IN (SELECT Z.CITY FROM CITY Z WHERE Z.STATE = ?)";
		}
		
		//query to get the mobile no of staff
		String getStaffMobile = "SELECT MOBILE1, MOBILE2 FROM USER";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			if(i!=0){
				stmt.setString(1, string);
			}
			
			rs = stmt.executeQuery();
			
			while (rs.next()){
				if(rs.getString("MOBILE1") !=null && !rs.getString("MOBILE1").equals("")){  emails.add(rs.getString("MOBILE1"));}
				if(rs.getString("MOBILE2") !=null && !rs.getString("MOBILE2").equals("")){  emails.add(rs.getString("MOBILE2"));}
			}
			
			stmt = getConnection().prepareStatement(getStaffMobile);
			
			rs = stmt.executeQuery();
			
			while (rs.next()){
				if(rs.getString("MOBILE1") !=null && !rs.getString("MOBILE1").equals("")){  emails.add(rs.getString("MOBILE1"));}
				if(rs.getString("MOBILE2") !=null && !rs.getString("MOBILE2").equals("")){  emails.add(rs.getString("MOBILE2"));}
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
		
		return (String[]) emails.toArray(new String[0]);
	}
	

}
