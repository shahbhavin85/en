package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.Utils;

public class AdminBroker extends BaseBroker {

	public AdminBroker(Connection conn) {
		super(conn);
	}

	public MessageModel unlockTransaction(String unlockDt) {
		
		MessageModel msgs = new MessageModel();
		
		PreparedStatement stmt = null;

		// query to unlock sales data
		String unlockSalesData = "UPDATE SALES SET ISLOCK = 'N' WHERE SALESDATE >= ?";

		// query to unlock sales data
		String unlockPurchaseData = "UPDATE PURCHASE SET ISLOCK = 'N' WHERE RECDDT >= ?";

		// query to unlock sales data
		String unlockTransferData = "UPDATE TRANSFER SET ISLOCK = 'N' WHERE TRANSFERDATE >= ?";
		
		try{
			
			// executing the query to unlock the sales bill
			stmt = getConnection().prepareStatement(unlockSalesData);
			stmt.setString(1, unlockDt);
			
			stmt.execute();

			// executing the query to unlock the purchase bill
			stmt = getConnection().prepareStatement(unlockPurchaseData);
			stmt.setString(1, unlockDt);
			
			stmt.execute();

			// executing the query to unlock the transfer bill
			stmt = getConnection().prepareStatement(unlockTransferData);
			stmt.setString(1, unlockDt);
			
			stmt.execute();
			
			getConnection().commit();
			
			msgs.addNewMessage(new Message(SUCCESS, "All Sales, Purchase and Transfer bills from "+Utils.convertToAppDate(unlockDt)+" is unlocked successfully!!"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msgs.addNewMessage(new Message(ERROR, "Error occured while unlocking transactions."));
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return msgs;
	}

	public MessageModel lockTransaction(String lockDt) {
		
		MessageModel msgs = new MessageModel();
		
		PreparedStatement stmt = null;

		// query to lock sales data
		String lockSalesData = "UPDATE SALES SET ISLOCK = 'Y' WHERE SALESDATE <= ?";

		// query to lock sales data
		String lockPurchaseData = "UPDATE PURCHASE SET ISLOCK = 'Y' WHERE RECDDT <= ?";

		// query to lock sales data
		String lockTransferData = "UPDATE TRANSFER SET ISLOCK = 'Y' WHERE TRANSFERDATE <= ?";
		
		try{
			
			// executing the query to lock the sales bill
			stmt = getConnection().prepareStatement(lockSalesData);
			stmt.setString(1, lockDt);
			
			stmt.execute();

			// executing the query to lock the purchase bill
			stmt = getConnection().prepareStatement(lockPurchaseData);
			stmt.setString(1, lockDt);
			
			stmt.execute();

			// executing the query to lock the transfer bill
			stmt = getConnection().prepareStatement(lockTransferData);
			stmt.setString(1, lockDt);
			
			stmt.execute();
			
			getConnection().commit();
			
			msgs.addNewMessage(new Message(SUCCESS, "All Sales, Purchase and Transfer bills till "+Utils.convertToAppDate(lockDt)+" is locked successfully!!"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msgs.addNewMessage(new Message(ERROR, "Error occured while locking transactions."));
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return msgs;
	}

}
