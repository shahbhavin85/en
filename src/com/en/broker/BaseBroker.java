package com.en.broker;

import java.sql.Connection;

import com.en.util.Constant;

public class BaseBroker implements Constant{

	private Connection dbConn = null;
	
	public BaseBroker(Connection conn){
		this.dbConn = conn;
	}
	
	protected Connection getConnection() {
		return this.dbConn;
	}
}
