package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.AdminItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;

public class SyncDataBroker extends AdminBaseBroker {

	public SyncDataBroker(Connection conn) {
		super(conn);
	}
	
	public MessageModel syncDataFromDB() {
		MessageModel msg = new MessageModel();
		
		ArrayList<AdminItemModel> items = new ArrayList<AdminItemModel>(0);
		AdminItemModel item = null;
		Iterator<AdminItemModel> itr = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		String getItemData = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.PRICE, A.TAX, ROUND((SUM(B.QTY*B.RATE*(100-B.DISRATE)/100)/SUM(B.QTY)),2) AS AVGSP, A.WPRICE" +
				" FROM ITEM A LEFT JOIN SALES_ITEM B ON A.ITEMID = B.ITEMID GROUP BY A.ITEMID ORDER BY ITEMNUMBER";
		
		String getCP = "SELECT A.RATE, B.RECDDT FROM PURCHASE_ITEM A, PURCHASE B WHERE A.PURCHASEID = B.PURCHASEID AND A.BRANCHID = B.BRANCHID AND A.ITEMID = ? ORDER BY RECDDT DESC LIMIT 1";
		
		String checkItemPresent = "SELECT COUNT(*) FROM ADMIN_ITEM WHERE ITEMID = ?";
		
		String insertAdminItemData = "INSERT INTO ADMIN_ITEM (ITEMID, ITEMNAME, ITEMNUMBER, PRICE, TAX, AVGSP, WPRICE, CP) VALUES (?,?,?,?,?,?,?,?)";
		
		String updateAdminItemData = "UPDATE ADMIN_ITEM SET ITEMNAME = ?, ITEMNUMBER =? , PRICE = ?, TAX = ? , AVGSP = ?, WPRICE = ?, CP = ? WHERE ITEMID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemData);
			stmt1 = getConnection().prepareStatement(getCP);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				item = new AdminItemModel();
				item.setItemId(rs.getInt("ITEMID"));
				item.setItemName(rs.getString("ITEMNAME"));
				item.setItemNumber(rs.getString("ITEMNUMBER"));
				item.setItemPrice(rs.getString("PRICE"));
				item.setTax(rs.getString("TAX"));
				item.setAvgSP((rs.getString("AVGSP") != null) ? rs.getDouble("AVGSP") : rs.getDouble("PRICE"));
				item.setWprice(rs.getDouble("WPRICE"));
				stmt1.setInt(1, rs.getInt("ITEMID"));
				
				rs1 = stmt1.executeQuery();
				
				if(rs1.next()){
					item.setServerCP(rs1.getDouble("RATE"));
				}
				
				items.add(item);
			}
			
			item = null;
			
			itr = items.iterator();
			
			stmt = getAdminConnection().prepareStatement(updateAdminItemData);
			stmt1 = getAdminConnection().prepareStatement(insertAdminItemData);
			stmt2 = getAdminConnection().prepareStatement(checkItemPresent);
			
			while(itr.hasNext()){
				item = itr.next();
				
				stmt2.setInt(1, item.getItemId());
				
				rs = stmt2.executeQuery();
				
				if(rs.next() && rs.getInt(1) > 0){
				
					stmt.setString(1, item.getItemName());
					stmt.setString(2, item.getItemNumber());
					stmt.setString(3, item.getItemPrice());
					stmt.setString(4, item.getTax());
					stmt.setDouble(5, item.getAvgSP());
					stmt.setDouble(6, item.getWprice());
					stmt.setDouble(7, item.getServerCP());
					stmt.setInt(8, item.getItemId());
					
					stmt.execute();
					
				} else {
				
					stmt1.setInt(1, item.getItemId());
					stmt1.setString(2, item.getItemName());
					stmt1.setString(3, item.getItemNumber());
					stmt1.setString(4, item.getItemPrice());
					stmt1.setString(5, item.getTax());
					stmt1.setDouble(6, item.getAvgSP());
					stmt1.setDouble(7, item.getWprice());
					stmt1.setDouble(8, item.getServerCP());
					
					stmt1.execute();
				}
			}
			
			getConnection().commit();
			getAdminConnection().commit();
			
			msg.addNewMessage(new Message(SUCCESS, "Data Synced Successfully."));
			
		} catch (Exception e) {
			msg.addNewMessage(new Message(ERROR, "Error occured while syncing the data from database"));
			e.printStackTrace();
		}
		
		return msg;
	}

	public MessageModel syncDataToDB(HashMap<String, String[]> dtls) {
		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;
		String key = "";
		
		String updateItemMaster = "UPDATE ITEM SET PRICE = ?, WPRICE = ? WHERE ITEMID = ?";
		
		try{
			
			Iterator<String> keys = dtls.keySet().iterator();
			
			stmt = getConnection().prepareStatement(updateItemMaster);
			
			while(keys.hasNext()){
				key = keys.next();
				
				stmt.setString(1, dtls.get(key)[0]);
				stmt.setString(2, dtls.get(key)[1]);
				stmt.setString(3, key);
				
				stmt.addBatch();
				
			}
			
			stmt.executeBatch();
			
			getConnection().commit();
			
			msg.addNewMessage(new Message(SUCCESS, "Data Synced Successfully."));
			
		} catch (Exception e) {
			msg.addNewMessage(new Message(ERROR, "Error occured while syncing the data from database"));
			e.printStackTrace();
		}
		
		return msg;
	}

	public AdminItemModel[] getItemDetails() {
		ArrayList<AdminItemModel> items = new ArrayList<AdminItemModel>(0);
		AdminItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT ITEMID, ITEMNUMBER, ITEMNAME, PRICE, SP1, WPRICE, SP2, REFNAME, CP FROM ADMIN_ITEM ORDER BY ITEMNUMBER";
		
		try{
			
			stmt = getAdminConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new AdminItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemPrice(rs.getString("PRICE"));
				model.setSP1((rs.getDouble("SP1") == 0) ? rs.getDouble("PRICE") : rs.getDouble("SP1"));
				model.setWprice(rs.getDouble("WPRICE"));
				model.setSP2((rs.getDouble("SP2") == 0) ? rs.getDouble("WPRICE") : rs.getDouble("SP2"));
				model.setRefName(rs.getString("REFNAME"));
				model.setServerCP(rs.getDouble("CP"));
				items.add(model);
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
		
		return (AdminItemModel[]) items.toArray(new AdminItemModel[0]);
	}

}
