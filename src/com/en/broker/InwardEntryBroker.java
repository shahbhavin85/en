package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.en.model.AdminItemModel;
import com.en.model.InwardEntryItemModel;
import com.en.model.InwardEntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.Utils;

public class InwardEntryBroker extends AdminBaseBroker {

	public InwardEntryBroker(Connection conn) {
		super(conn);
	}

	public synchronized MessageModel addEntry(InwardEntryModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		long entryNo = 1;

		// query to check the existence of the bill no
		String maxEntryNo = "SELECT MAX(ENTRYNO) FROM INWARD_ENTRY";

		// query to insert the bill details
		String insertEntryDetails = "INSERT INTO INWARD_ENTRY (ENTRYNO, BENO, TOTALCBM, EXCHANGERATE, SOURCEEXP, DESTINATIONEXP, ENTRYDATE, REMARKS) " +
				"VALUES (?,?,?,?,?,?,?,?)";
		
		//query to insert the bill items
		String insertEntryItems = "INSERT INTO INWARD_ENTRY_ITEM (ENTRYNO, ITEMID, QTY, RATE, CBM, DUTY) " +
				"VALUES (?,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getAdminConnection().prepareStatement(maxEntryNo);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					entryNo = rs.getInt(1) + 1;
				}
				
				// executing the query to insert bill and bill item details
				stmt = getAdminConnection().prepareStatement(insertEntryDetails);
				stmt1 = getAdminConnection().prepareStatement(insertEntryItems);
				
				stmt.setLong(1, entryNo);
				stmt.setString(2, model.getBENo());
				stmt.setDouble(3, model.getCbm());
				stmt.setDouble(4, model.getExchangeRate());
				stmt.setDouble(5, model.getSourceExp());
				stmt.setDouble(6, model.getDestinationExp());
				stmt.setString(7, Utils.convertToSQLDate(model.getEntryDate()));
				stmt.setString(8, model.getRemarks());
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().length; i++){
					
					stmt1.setLong(1, entryNo);
					stmt1.setInt(2, model.getItems()[i].getItem().getItemId());
					stmt1.setDouble(3, model.getItems()[i].getQty());
					stmt1.setDouble(4, model.getItems()[i].getRate());
					stmt1.setDouble(5, model.getItems()[i].getCbm());
					stmt1.setDouble(6, model.getItems()[i].getDuty());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getAdminConnection().commit();
				
				msg = new Message(SUCCESS, "Entry Added successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, entryNo+"");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting entry details!!");
			msgs.addNewMessage(msg);
			try{
				getAdminConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return msgs;
	}

	public InwardEntryModel getEntryDetails(String entryNo) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		InwardEntryModel model = new InwardEntryModel();
		
		// query to get the bill no details
		String getEntryDetails = "SELECT ENTRYNO, BENO, TOTALCBM, EXCHANGERATE, SOURCEEXP, DESTINATIONEXP, ENTRYDATE, REMARKS FROM INWARD_ENTRY WHERE ENTRYNO = ?";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.RATE, B.CBM, B.DUTY " +
				"FROM ADMIN_ITEM A, INWARD_ENTRY_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.ENTRYNO = ? ";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getAdminConnection().prepareStatement(getEntryDetails);
			stmt.setString(1, entryNo);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setEntryNo(rs.getInt("ENTRYNO"));
				model.setBENo(rs.getString("BENO"));
				model.setCbm(rs.getDouble("TOTALCBM"));
				model.setExchangeRate(rs.getDouble("EXCHANGERATE"));
				model.setSourceExp(rs.getDouble("SOURCEEXP"));
				model.setDestinationExp(rs.getDouble("DESTINATIONEXP"));
				model.setEntryDate(rs.getString("ENTRYDATE"));
				model.setRemarks(rs.getString("REMARKS"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getAdminConnection().prepareStatement(getItemDetails);
			stmt.setString(1, entryNo);
			
			rs = stmt.executeQuery();
			AdminItemModel itm = null;
			InwardEntryItemModel inwardItem = null;
			
			while(rs.next()){
				inwardItem = new InwardEntryItemModel(); 
				itm = new AdminItemModel();
				itm.setItemId(rs.getInt("ITEMID"));
				itm.setItemNumber(rs.getString("ITEMNUMBER"));
				itm.setItemName(rs.getString("ITEMNAME"));
				inwardItem.setItem(itm);
				inwardItem.setQty(rs.getDouble("QTY"));
				inwardItem.setRate(rs.getDouble("RATE"));
				inwardItem.setCbm(rs.getDouble("CBM"));
				inwardItem.setDuty(rs.getDouble("DUTY"));
				model.addItem(inwardItem);
			}
			
			getAdminConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getAdminConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(rs1 != null)
					rs1.close();
				if(stmt != null)
					stmt.close();
				if(stmt1 != null)
					stmt1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel editEntry(InwardEntryModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;

		// query to insert the bill details
		String updateEntryDetails = "UPDATE INWARD_ENTRY SET BENO = ? , TOTALCBM = ?, EXCHANGERATE = ?, SOURCEEXP = ? , DESTINATIONEXP = ?, ENTRYDATE = ?, REMARKS = ? WHERE ENTRYNO = ?";
		
		//query to insert the bill items
		String deleteEntryItems = "DELETE FROM INWARD_ENTRY_ITEM WHERE ENTRYNO = ?";
		
		//query to insert the bill items
		String insertEntryItems = "INSERT INTO INWARD_ENTRY_ITEM (ENTRYNO, ITEMID, QTY, RATE, CBM, DUTY) " +
				"VALUES (?,?,?,?,?,?)";
		
		try {
				
				stmt = getAdminConnection().prepareStatement(deleteEntryItems);
				stmt.setInt(1, model.getEntryNo());
				
				stmt.execute();
					
				// executing the query to insert bill and bill item details
				stmt = getAdminConnection().prepareStatement(updateEntryDetails);
				stmt1 = getAdminConnection().prepareStatement(insertEntryItems);
				
				stmt.setString(1, model.getBENo());
				stmt.setDouble(2, model.getCbm());
				stmt.setDouble(3, model.getExchangeRate());
				stmt.setDouble(4, model.getSourceExp());
				stmt.setDouble(5, model.getDestinationExp());
				stmt.setString(6, Utils.convertToSQLDate(model.getEntryDate()));
				stmt.setString(7, model.getRemarks());
				stmt.setInt(8, model.getEntryNo());
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().length; i++){
					
					stmt1.setLong(1, model.getEntryNo());
					stmt1.setInt(2, model.getItems()[i].getItem().getItemId());
					stmt1.setDouble(3, model.getItems()[i].getQty());
					stmt1.setDouble(4, model.getItems()[i].getRate());
					stmt1.setDouble(5, model.getItems()[i].getCbm());
					stmt1.setDouble(6, model.getItems()[i].getDuty());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getAdminConnection().commit();
				
				msg = new Message(SUCCESS, "Entry Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getEntryNo()+"");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing entry details!!");
			msgs.addNewMessage(msg);
			try{
				getAdminConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return msgs;
	}

}
