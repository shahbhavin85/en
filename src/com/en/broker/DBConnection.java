package com.en.broker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.en.util.Constant;

public class DBConnection implements  Constant{

	Connection conn;
	
    public Connection getConnection()  throws  SQLException, ClassNotFoundException,
    		InstantiationException, IllegalAccessException {

        Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        try{
	        Connection conn =
	        DriverManager.getConnection (
	        "jdbc:mysql://localhost:3307/hesh10?zeroDateTimeBehavior=convertToNull","root","admin");
//	        DriverManager.getConnection (
//	    	        "jdbc:mysql://122.165.79.21:3307/hesh10?zeroDateTimeBehavior=convertToNull","root","admin");
	        this.conn = conn;
	        this.conn.setAutoCommit(false);
        } catch (Exception e) {
			e.printStackTrace();
		}
        return conn;
    }
	
    public Connection getAdminConnection()  throws  SQLException, ClassNotFoundException,
    		InstantiationException, IllegalAccessException {

        Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        Connection conn =
        DriverManager.getConnection (
        "jdbc:mysql://localhost:3307/heshadmin10?zeroDateTimeBehavior=convertToNull","root","admin");
        this.conn = conn;
        this.conn.setAutoCommit(false);
        return conn;
    }
}
