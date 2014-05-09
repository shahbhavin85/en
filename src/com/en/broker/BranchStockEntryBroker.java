package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.AccessPointModel;
import com.en.model.BranchStockEntryItemModel;
import com.en.model.BranchStockEntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.StockItemModel;
import com.en.model.StockModel;

public class BranchStockEntryBroker extends BaseBroker {

	public BranchStockEntryBroker(Connection conn) {
		super(conn);
	}

	public BranchStockEntryModel getStockDetails(String day,String month, String year, AccessPointModel access) {
		BranchStockEntryModel model = new BranchStockEntryModel();
		model.setDay(Integer.parseInt(day));
		model.setMonth(Integer.parseInt(month));
		model.setYear(Integer.parseInt(year));
		
		HashMap<Integer, BranchStockEntryItemModel> dtls = new HashMap<Integer, BranchStockEntryItemModel>(0);
		BranchStockEntryItemModel item = null;
		Iterator<StockItemModel> temp = null;
		StockItemModel stockItem = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME FROM ITEM A ORDER BY A.ITEMNUMBER";

		// query to get all the existing item group
		String getBranchItemStock = "SELECT ITEMID, QTY, CHECKEDBY FROM BRANCH_STOCK WHERE BRANCHID = ? AND STOCKDATE = ? ORDER BY ITEMID";
		
		try{
			
			stmt = getConnection().prepareStatement(getItems);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				item = new BranchStockEntryItemModel();
				item.getItem().setItemId(rs.getInt("ITEMID"));
				item.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
				item.getItem().setItemName(rs.getString("ITEMNAME"));
				
				dtls.put(rs.getInt("ITEMID"), item);
			}
			
			
			StockModel stock = new StockModel();
			stock.setFromDate(day+"-"+month+"-"+year);
			stock.setToDate(day+"-"+month+"-"+year);
			stock.setBranch(access);
			
			stock = (new StockRegisterBroker(getConnection())).getStockReportALL(stock);
			
			temp =  stock.getItems().iterator();
			
			while(temp.hasNext()){
				stockItem = temp.next();
				item = dtls.get(stockItem.getItemId());
				item.setStock(stockItem.getOpenQty()+stockItem.getPlusQty()-stockItem.getMinusQty());
				dtls.put(stockItem.getItemId(), item);
			}
			
			
			stmt = getConnection().prepareStatement(getBranchItemStock);
			stmt.setInt(1, access.getAccessId());
			stmt.setString(2, year+"-"+month+"-"+day);
			
			rs = stmt.executeQuery();
			
			boolean isFirst = true;
			
			while(rs.next()){
				if(isFirst)
					model.setCheckedBy((new UserBroker(getConnection())).getUserDtls(rs.getString("CHECKEDBY")));
				item = dtls.get(rs.getInt("ITEMID"));
				item.setQty(rs.getDouble("QTY"));
				dtls.put(rs.getInt("ITEMID"), item);
			}
			
			Iterator<Integer> keys = dtls.keySet().iterator();
			
			while(keys.hasNext()){
				model.addItem(dtls.get(keys.next()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
				if(rs != null)
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

	public MessageModel updateStock(BranchStockEntryModel model) {
		MessageModel msgs = new MessageModel();

		PreparedStatement stmt = null;

		// query to get all the existing item group
		String getItems = "DELETE FROM BRANCH_STOCK WHERE BRANCHID = ? AND STOCKDATE = ?";

		// query to get all the existing item group
		String insertItems = "INSERT INTO BRANCH_STOCK (BRANCHID, STOCKDATE, ITEMID, QTY, CHECKEDBY) VALUES (?,?,?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(getItems);
			stmt.setInt(1, model.getBranch().getAccessId());
			stmt.setString(2, model.getSQLDate());
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(insertItems);
			
			BranchStockEntryItemModel[] items = (BranchStockEntryItemModel[]) model.getItems().toArray(new BranchStockEntryItemModel[0]);
			
			for(int i=0; i<items.length; i++){
				stmt.setInt(1, model.getBranch().getAccessId());
				stmt.setString(2, model.getSQLDate());
				stmt.setInt(3, items[i].getItem().getItemId());
				stmt.setDouble(4, items[i].getQty());
				stmt.setString(5, model.getCheckedBy().getUserId());
				
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			
			msgs.addNewMessage(new Message(SUCCESS, "Stock Entry updated successfully."));
			
		} catch (Exception e) {
			e.printStackTrace();
			msgs.addNewMessage(new Message(ERROR, "Error occured while updating the Stock Entry details."));
			try{
				if(stmt != null)
					stmt.close();
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
		
		return msgs;
	}

}
