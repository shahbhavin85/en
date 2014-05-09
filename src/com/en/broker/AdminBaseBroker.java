package com.en.broker;

import java.sql.Connection;

public class AdminBaseBroker extends BaseBroker {

	private Connection dbConn = null;

	public AdminBaseBroker(Connection conn) {
		super(conn);
	}
	
	protected Connection getAdminConnection() {
		
		try{
			if(this.dbConn == null){
				this.dbConn = (new DBConnection()).getAdminConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.dbConn;
	}

}
