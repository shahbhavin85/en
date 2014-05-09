package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BackupBroker extends BaseBroker {

	public BackupBroker(Connection conn) {
		super(conn);
	}
	
	public String getMysqlPath(){
		String path = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing access points
		String getMysqlPath = "SELECT @@BASEDIR AS PATH";
		
		try{
			
			stmt = getConnection().prepareStatement(getMysqlPath);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				path = rs.getString("PATH");
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
		
		return path;
	}

}
