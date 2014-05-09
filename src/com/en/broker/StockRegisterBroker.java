package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.ItemModel;
import com.en.model.StockItemModel;
import com.en.model.StockModel;
import com.en.util.StockRegisterDateComparator;
import com.en.util.Utils;

public class StockRegisterBroker extends BaseBroker {

	public StockRegisterBroker(Connection conn) {
		super(conn);
	}

	public StockModel getStockReportALL(StockModel model) {
		HashMap<Integer, StockItemModel> itemMap = new HashMap<Integer, StockItemModel>(0);
		StockItemModel item = null;
		
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement stmt8 = null;
		ResultSet rs3 = null;
		int itemId = 0;

		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME FROM ITEM A ORDER BY A.ITEMNUMBER";

		// query to get the qty sold
		String getOpeningQty = "SELECT ITEMID, QTY FROM OPENING_ITEM_STOCK WHERE BRANCHID = ?";
		
		// query to get the qty sold
		String getSoldQty = "SELECT ITEMID, SUM(QTY) FROM SALES_ITEM WHERE BRANCHID = ? AND SALESID IN ( SELECT A.SALESID FROM SALES A WHERE A.SALESDATE < ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getPurchaseQty = "SELECT ITEMID, SUM(QTY) FROM PURCHASE_ITEM WHERE BRANCHID = ? AND PURCHASEID IN ( SELECT A.PURCHASEID FROM PURCHASE A WHERE A.RECDDT < ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getTransferInQty = "SELECT ITEMID, SUM(QTY) FROM TRANSFER_ITEM WHERE (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TRANSFERDATE < ? AND A.TOID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getTranferOutQty = "SELECT ITEMID, SUM(QTY) FROM TRANSFER_ITEM WHERE BRANCHID = ?  AND TRANSFERID IN ( SELECT A.TRANSFERID FROM TRANSFER A WHERE A.TRANSFERDATE < ? AND A.FROMID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getSalesReturnQty = "SELECT ITEMID, SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE BRANCHID = ? AND CREDITNOTEID IN ( SELECT A.CREDITNOTEID FROM CREDIT_NOTE A WHERE A.NOTEDATE < ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getPurchaseReturnQty = "SELECT ITEMID, SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE BRANCHID = ? AND RETURNID IN ( SELECT A.RETURNID FROM PURCHASE_RETURN A WHERE A.RETURNDATE < ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getSoldQty1 = "SELECT ITEMID, SUM(QTY) FROM SALES_ITEM WHERE BRANCHID = ? AND SALESID IN ( SELECT A.SALESID FROM SALES A WHERE A.SALESDATE >= ? AND A.SALESDATE <= ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getPurchaseQty1 = "SELECT ITEMID, SUM(QTY) FROM PURCHASE_ITEM WHERE BRANCHID = ? AND PURCHASEID IN ( SELECT A.PURCHASEID FROM PURCHASE A WHERE A.RECDDT >= ? AND A.RECDDT <= ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getTransferInQty1 = "SELECT ITEMID, SUM(QTY) FROM TRANSFER_ITEM WHERE (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TRANSFERDATE >= ? AND A.TRANSFERDATE <= ? AND A.TOID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getTranferOutQty1 = "SELECT ITEMID, SUM(QTY) FROM TRANSFER_ITEM WHERE BRANCHID = ?  AND TRANSFERID IN ( SELECT A.TRANSFERID FROM TRANSFER A WHERE A.TRANSFERDATE >= ? AND A.TRANSFERDATE <= ? AND A.FROMID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getSalesReturnQty1 = "SELECT ITEMID, SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE BRANCHID = ? AND CREDITNOTEID IN ( SELECT A.CREDITNOTEID FROM CREDIT_NOTE A WHERE A.NOTEDATE >= ? AND A.NOTEDATE <= ? AND A.BRANCHID = ?) GROUP BY ITEMID";

		// query to get the qty sold
		String getPurchaseReturnQty1 = "SELECT ITEMID, SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE BRANCHID = ? AND RETURNID IN ( SELECT A.RETURNID FROM PURCHASE_RETURN A WHERE A.RETURNDATE >= ? AND A.RETURNDATE <= ? AND A.BRANCHID = ?) GROUP BY ITEMID";
		
		try{
			
			stmt1 = getConnection().prepareStatement(getItems);
			stmt2 = getConnection().prepareStatement(getOpeningQty);
			stmt3 = getConnection().prepareStatement(getPurchaseQty);
			stmt4 = getConnection().prepareStatement(getTransferInQty);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty);
			stmt6 = getConnection().prepareStatement(getSoldQty);
			stmt7 = getConnection().prepareStatement(getTranferOutQty);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty);
			
			rs2 = stmt1.executeQuery();

			while (rs2.next()) {
				item = new StockItemModel();
				itemId = rs2.getInt("ITEMID");
				item.setItemId(rs2.getInt("ITEMID"));
				item.setItemNumber(rs2.getString("ITEMNUMBER"));
				item.setItemName(rs2.getString("ITEMNAME"));
				
				itemMap.put(itemId, item);
			}

			stmt2.setInt(1, model.getBranch().getAccessId());

			rs3 = stmt2.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt3.setInt(1, model.getBranch().getAccessId());
			stmt3.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt3.setInt(3, model.getBranch().getAccessId());

			rs3 = stmt3.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt4.setString(1, Utils.convertToSQLDate(model.getFromDate()));
			stmt4.setInt(2, model.getBranch().getAccessId());

			rs3 = stmt4.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt5.setInt(1, model.getBranch().getAccessId());
			stmt5.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt5.setInt(3, model.getBranch().getAccessId());

			rs3 = stmt5.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt6.setInt(1, model.getBranch().getAccessId());
			stmt6.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt6.setInt(3, model.getBranch().getAccessId());

			rs3 = stmt6.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() - rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt7.setInt(1, model.getBranch().getAccessId());
			stmt7.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt7.setInt(3, model.getBranch().getAccessId());

			rs3 = stmt7.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() - rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt8.setInt(1, model.getBranch().getAccessId());
			stmt8.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt8.setInt(3, model.getBranch().getAccessId());

			rs3 = stmt8.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setOpenQty(item.getOpenQty() - rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}
			
			stmt3 = getConnection().prepareStatement(getPurchaseQty1);
			stmt4 = getConnection().prepareStatement(getTransferInQty1);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty1);
			stmt6 = getConnection().prepareStatement(getSoldQty1);
			stmt7 = getConnection().prepareStatement(getTranferOutQty1);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty1);
			
			
			stmt3.setInt(1, model.getBranch().getAccessId());
			stmt3.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt3.setString(3, Utils.convertToSQLDate(model.getToDate()));
			stmt3.setInt(4, model.getBranch().getAccessId());

			rs3 = stmt3.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setPlusQty(item.getPlusQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}
				
			stmt4.setString(1, Utils.convertToSQLDate(model.getFromDate()));
			stmt4.setString(2, Utils.convertToSQLDate(model.getToDate()));
			stmt4.setInt(3, model.getBranch().getAccessId());

			rs3 = stmt4.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setPlusQty(item.getPlusQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt5.setInt(1, model.getBranch().getAccessId());
			stmt5.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt5.setString(3, Utils.convertToSQLDate(model.getToDate()));
			stmt5.setInt(4, model.getBranch().getAccessId());

			rs3 = stmt5.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setPlusQty(item.getPlusQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt6.setInt(1, model.getBranch().getAccessId());
			stmt6.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt6.setString(3, Utils.convertToSQLDate(model.getToDate()));
			stmt6.setInt(4, model.getBranch().getAccessId());

			rs3 = stmt6.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setMinusQty(item.getMinusQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt7.setInt(1, model.getBranch().getAccessId());
			stmt7.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt7.setString(3, Utils.convertToSQLDate(model.getToDate()));
			stmt7.setInt(4, model.getBranch().getAccessId());

			rs3 = stmt7.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setMinusQty(item.getMinusQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}

			stmt8.setInt(1, model.getBranch().getAccessId());
			stmt8.setString(2, Utils.convertToSQLDate(model.getFromDate()));
			stmt8.setString(3, Utils.convertToSQLDate(model.getToDate()));
			stmt8.setInt(4, model.getBranch().getAccessId());

			rs3 = stmt8.executeQuery();

			while(rs3.next()) {
				item = itemMap.get(rs3.getInt("ITEMID"));
				item.setMinusQty(item.getMinusQty() + rs3.getDouble(2));
				itemMap.put(rs3.getInt("ITEMID"), item);
			}
			
			Iterator<Integer> ids = itemMap.keySet().iterator();
			
			while(ids.hasNext()){
				model.getItems().add(itemMap.get(ids.next()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt1 != null)
					stmt1.close();
				if(stmt2 != null)
					stmt2.close();
				if(rs2 != null)
					rs2.close();
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

	public StockModel getStockReportWithItemId(StockModel model) {
		HashMap<Integer, StockItemModel> itemMap = new HashMap<Integer, StockItemModel>(0);
		StockItemModel item = null;
		
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement stmt8 = null;
		ResultSet rs3 = null;
		int itemId = 0;

		// query to get all the existing item group
		String getItemsById = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME FROM ITEM A WHERE A.ITEMID = ? ORDER BY A.ITEMNUMBER";

		// query to get the qty sold
		String getOpeningQty = "SELECT QTY FROM OPENING_ITEM_STOCK WHERE ITEMID = ? AND BRANCHID = ?";
		
		// query to get the qty sold
		String getSoldQty = "SELECT SUM(QTY) FROM SALES_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND SALESID IN ( SELECT A.SALESID FROM SALES A WHERE A.SALESDATE < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseQty = "SELECT SUM(QTY) FROM PURCHASE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND PURCHASEID IN ( SELECT A.PURCHASEID FROM PURCHASE A WHERE A.RECDDT < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getTransferInQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TRANSFERDATE < ? AND A.TOID = ?)";

		// query to get the qty sold
		String getTranferOutQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND BRANCHID = ?  AND TRANSFERID IN ( SELECT A.TRANSFERID FROM TRANSFER A WHERE A.TRANSFERDATE < ? AND A.FROMID = ?)";

		// query to get the qty sold
		String getSalesReturnQty = "SELECT SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND CREDITNOTEID IN ( SELECT A.CREDITNOTEID FROM CREDIT_NOTE A WHERE A.NOTEDATE < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseReturnQty = "SELECT SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND RETURNID IN ( SELECT A.RETURNID FROM PURCHASE_RETURN A WHERE A.RETURNDATE < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getSoldQty1 = "SELECT SUM(QTY) FROM SALES_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND SALESID IN ( SELECT A.SALESID FROM SALES A WHERE A.SALESDATE >= ? AND A.SALESDATE <= ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseQty1 = "SELECT SUM(QTY) FROM PURCHASE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND PURCHASEID IN ( SELECT A.PURCHASEID FROM PURCHASE A WHERE A.RECDDT >= ? AND A.RECDDT <= ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getTransferInQty1 = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TRANSFERDATE >= ? AND A.TRANSFERDATE <= ? AND A.TOID = ?)";

		// query to get the qty sold
		String getTranferOutQty1 = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND BRANCHID = ?  AND TRANSFERID IN ( SELECT A.TRANSFERID FROM TRANSFER A WHERE A.TRANSFERDATE >= ? AND A.TRANSFERDATE <= ? AND A.FROMID = ?)";

		// query to get the qty sold
		String getSalesReturnQty1 = "SELECT SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND CREDITNOTEID IN ( SELECT A.CREDITNOTEID FROM CREDIT_NOTE A WHERE A.NOTEDATE >= ? AND A.NOTEDATE <= ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseReturnQty1 = "SELECT SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND RETURNID IN ( SELECT A.RETURNID FROM PURCHASE_RETURN A WHERE A.RETURNDATE >= ? AND A.RETURNDATE <= ? AND A.BRANCHID = ?)";
		
		try{
			
			if(model.getItem().getItemId()>0){
				stmt1 = getConnection().prepareStatement(getItemsById);
				stmt1.setInt(1, model.getItem().getItemId());
			}
			
			stmt2 = getConnection().prepareStatement(getOpeningQty);
			stmt3 = getConnection().prepareStatement(getPurchaseQty);
			stmt4 = getConnection().prepareStatement(getTransferInQty);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty);
			stmt6 = getConnection().prepareStatement(getSoldQty);
			stmt7 = getConnection().prepareStatement(getTranferOutQty);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty);
			
			rs2 = stmt1.executeQuery();

			while (rs2.next()) {
				item = new StockItemModel();
				itemId = rs2.getInt("ITEMID");
				item.setItemId(rs2.getInt("ITEMID"));
				item.setItemNumber(rs2.getString("ITEMNUMBER"));
				item.setItemName(rs2.getString("ITEMNAME"));

				stmt2.setInt(1, itemId);
				stmt2.setInt(2, model.getBranch().getAccessId());

				rs3 = stmt2.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt3.setInt(1, itemId);
				stmt3.setInt(2, model.getBranch().getAccessId());
				stmt3.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt3.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt3.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt4.setInt(1, itemId);
				stmt4.setString(2, Utils.convertToSQLDate(model.getFromDate()));
				stmt4.setInt(3, model.getBranch().getAccessId());

				rs3 = stmt4.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt5.setInt(1, itemId);
				stmt5.setInt(2, model.getBranch().getAccessId());
				stmt5.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt5.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt5.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt6.setInt(1, itemId);
				stmt6.setInt(2, model.getBranch().getAccessId());
				stmt6.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt6.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt6.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() - rs3.getDouble(1));
				}

				stmt7.setInt(1, itemId);
				stmt7.setInt(2, model.getBranch().getAccessId());
				stmt7.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt7.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt7.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() - rs3.getDouble(1));
				}

				stmt8.setInt(1, itemId);
				stmt8.setInt(2, model.getBranch().getAccessId());
				stmt8.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt8.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt8.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() - rs3.getDouble(1));
				}
				
				itemMap.put(item.getItemId(), item);
			}
			
			stmt3 = getConnection().prepareStatement(getPurchaseQty1);
			stmt4 = getConnection().prepareStatement(getTransferInQty1);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty1);
			stmt6 = getConnection().prepareStatement(getSoldQty1);
			stmt7 = getConnection().prepareStatement(getTranferOutQty1);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty1);
			
			rs2 = stmt1.executeQuery();

			while (rs2.next()) {
				item = (itemMap.get(rs2.getInt("ITEMID")) != null) ? itemMap.get(rs2.getInt("ITEMID")) : new StockItemModel();
				itemId = rs2.getInt("ITEMID");
				
				stmt3.setInt(1, itemId);
				stmt3.setInt(2, model.getBranch().getAccessId());
				stmt3.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt3.setString(4, Utils.convertToSQLDate(model.getToDate()));
				stmt3.setInt(5, model.getBranch().getAccessId());

				rs3 = stmt3.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setPlusQty(item.getPlusQty() + rs3.getDouble(1));
				}

				stmt4.setInt(1, itemId);
				stmt4.setString(2, Utils.convertToSQLDate(model.getFromDate()));
				stmt4.setString(3, Utils.convertToSQLDate(model.getToDate()));
				stmt4.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt4.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setPlusQty(item.getPlusQty() + rs3.getDouble(1));
				}

				stmt5.setInt(1, itemId);
				stmt5.setInt(2, model.getBranch().getAccessId());
				stmt5.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt5.setString(4, Utils.convertToSQLDate(model.getToDate()));
				stmt5.setInt(5, model.getBranch().getAccessId());

				rs3 = stmt5.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setPlusQty(item.getPlusQty() + rs3.getDouble(1));
				}

				stmt6.setInt(1, itemId);
				stmt6.setInt(2, model.getBranch().getAccessId());
				stmt6.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt6.setString(4, Utils.convertToSQLDate(model.getToDate()));
				stmt6.setInt(5, model.getBranch().getAccessId());

				rs3 = stmt6.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setMinusQty(item.getMinusQty() + rs3.getDouble(1));
				}

				stmt7.setInt(1, itemId);
				stmt7.setInt(2, model.getBranch().getAccessId());
				stmt7.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt7.setString(4, Utils.convertToSQLDate(model.getToDate()));
				stmt7.setInt(5, model.getBranch().getAccessId());

				rs3 = stmt7.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setMinusQty(item.getMinusQty() + rs3.getDouble(1));
				}

				stmt8.setInt(1, itemId);
				stmt8.setInt(2, model.getBranch().getAccessId());
				stmt8.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt8.setString(4, Utils.convertToSQLDate(model.getToDate()));
				stmt8.setInt(5, model.getBranch().getAccessId());

				rs3 = stmt8.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setMinusQty(item.getMinusQty() + rs3.getDouble(1));
				}
				
				itemMap.put(item.getItemId(), item);
			}
			
			Iterator<Integer> ids = itemMap.keySet().iterator();
			
			while(ids.hasNext()){
				model.getItems().add(itemMap.get(ids.next()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt1 != null)
					stmt1.close();
				if(stmt2 != null)
					stmt2.close();
				if(rs2 != null)
					rs2.close();
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

	public StockItemModel[] getItemRpt(String itemid, String branch,
			String fromDate, String toDate) {
		ArrayList<StockItemModel> dtls = new ArrayList<StockItemModel>(0);
		StockItemModel item = null;
		StockItemModel[] result = null;
		StockItemModel openItem = null;
		
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement stmt8 = null;
		ResultSet rs3 = null;
		HashMap<Integer, String> prefix = new HashMap<Integer, String>(0);
		double qty = 0;
		
		// query to get the bill prefix
		String getPrefex = "SELECT ACCESSID, BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID > 10";

		// query to get the qty sold
		String getOpeningQty = "SELECT QTY FROM OPENING_ITEM_STOCK WHERE ITEMID = ? AND BRANCHID = ?";
		
		// query to get the qty sold
		String getSoldQty = "SELECT SUM(QTY) FROM SALES_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND SALESID IN ( SELECT A.SALESID FROM SALES A WHERE A.SALESDATE < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseQty = "SELECT SUM(QTY) FROM PURCHASE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND PURCHASEID IN ( SELECT A.PURCHASEID FROM PURCHASE A WHERE A.RECDDT < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getTransferInQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TRANSFERDATE < ? AND A.TOID = ?)";

		// query to get the qty sold
		String getTranferOutQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND BRANCHID = ?  AND TRANSFERID IN ( SELECT A.TRANSFERID FROM TRANSFER A WHERE A.TRANSFERDATE < ? AND A.FROMID = ?)";

		// query to get the qty sold
		String getSalesReturnQty = "SELECT SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND CREDITNOTEID IN ( SELECT A.CREDITNOTEID FROM CREDIT_NOTE A WHERE A.NOTEDATE < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseReturnQty = "SELECT SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND RETURNID IN ( SELECT A.RETURNID FROM PURCHASE_RETURN A WHERE A.RETURNDATE < ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getSoldQty1 = "SELECT Z.SALESID, Z.BRANCHID , Z.QTY, Y.SALESDATE FROM SALES_ITEM Z, SALES Y WHERE Z.ITEMID = ? AND Z.BRANCHID = ? AND Z.SALESID = Y.SALESID AND Z.BRANCHID = Y.BRANCHID AND Y.SALESDATE >= ? AND Y.SALESDATE <= ?";

		// query to get the qty sold
		String getPurchaseQty1 = "SELECT Z.PURCHASEID, Z.BRANCHID, Z.QTY, Y.RECDDT FROM PURCHASE_ITEM Z, PURCHASE Y WHERE Z.ITEMID = ? AND Z.BRANCHID = ? AND Z.PURCHASEID = Y.PURCHASEID AND Z.BRANCHID = Y.BRANCHID AND Y.RECDDT >= ? AND Y.RECDDT <= ?";

		// query to get the qty sold
		String getTransferInQty1 = "SELECT Z.TRANSFERID, Z.BRANCHID, Z.QTY, Y.TRANSFERDATE FROM TRANSFER_ITEM Z, TRANSFER Y WHERE Z.ITEMID = ? AND Z.TRANSFERID = Y.TRANSFERID AND Z.BRANCHID = Y.FROMID AND Y.TRANSFERDATE >= ? AND Y.TRANSFERDATE <= ? AND Y.TOID = ?";

		// query to get the qty sold
		String getTranferOutQty1 = "SELECT Z.TRANSFERID, Z.BRANCHID, Z.QTY, Y.TRANSFERDATE FROM TRANSFER_ITEM Z, TRANSFER Y WHERE Z.ITEMID = ? AND Z.TRANSFERID = Y.TRANSFERID AND Z.BRANCHID = Y.FROMID AND Y.FROMID = ? AND Y.TRANSFERDATE >= ? AND Y.TRANSFERDATE <= ?";

		// query to get the qty sold
		String getSalesReturnQty1 = "SELECT Z.CREDITNOTEID, Z.BRANCHID, Z.QTY, Y.NOTEDATE FROM CREDIT_NOTE_ITEM Z, CREDIT_NOTE Y WHERE Z.ITEMID = ? AND Z.BRANCHID = ? AND Z.CREDITNOTEID = Y.CREDITNOTEID AND Z.BRANCHID = Y.BRANCHID AND Y.NOTEDATE >= ? AND Y.NOTEDATE <= ?";

		// query to get the qty sold
		String getPurchaseReturnQty1 = "SELECT Z.RETURNID, Z.BRANCHID, Z.QTY, Y.RETURNDATE FROM PURCHASE_RETURN_ITEM Z, PURCHASE_RETURN Y WHERE Z.ITEMID = ? AND Z.BRANCHID = ? AND Z.RETURNID = Y.RETURNID AND Z.BRANCHID = Y.BRANCHID AND Y.RETURNDATE >= ? AND Y.RETURNDATE <= ?";
		
		try{
			
			stmt1 = getConnection().prepareStatement(getPrefex);
			
			rs2 = stmt1.executeQuery();
			
			while(rs2.next()){
				prefix.put(rs2.getInt(1), rs2.getString(2));
			}
			
			stmt2 = getConnection().prepareStatement(getOpeningQty);
			stmt3 = getConnection().prepareStatement(getPurchaseQty);
			stmt4 = getConnection().prepareStatement(getTransferInQty);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty);
			stmt6 = getConnection().prepareStatement(getSoldQty);
			stmt7 = getConnection().prepareStatement(getTranferOutQty);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty);

			openItem = new StockItemModel();

			stmt2.setString(1, itemid);
			stmt2.setString(2, branch);

			rs3 = stmt2.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() + rs3.getDouble(1));
			}

			stmt3.setString(1, itemid);
			stmt3.setString(2, branch);
			stmt3.setString(3, Utils.convertToSQLDate(fromDate));
			stmt3.setString(4, branch);

			rs3 = stmt3.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() + rs3.getDouble(1));
			}

			stmt4.setString(1, itemid);
			stmt4.setString(2, Utils.convertToSQLDate(fromDate));
			stmt4.setString(3, branch);

			rs3 = stmt4.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() + rs3.getDouble(1));
			}

			stmt5.setString(1, itemid);
			stmt5.setString(2, branch);
			stmt5.setString(3, Utils.convertToSQLDate(fromDate));
			stmt5.setString(4, branch);

			rs3 = stmt5.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() + rs3.getDouble(1));
			}

			stmt6.setString(1, itemid);
			stmt6.setString(2, branch);
			stmt6.setString(3, Utils.convertToSQLDate(fromDate));
			stmt6.setString(4, branch);

			rs3 = stmt6.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() - rs3.getDouble(1));
			}

			stmt7.setString(1, itemid);
			stmt7.setString(2, branch);
			stmt7.setString(3, Utils.convertToSQLDate(fromDate));
			stmt7.setString(4, branch);

			rs3 = stmt7.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() - rs3.getDouble(1));
			}

			stmt8.setString(1, itemid);
			stmt8.setString(2, branch);
			stmt8.setString(3, Utils.convertToSQLDate(fromDate));
			stmt8.setString(4, branch);

			rs3 = stmt8.executeQuery();

			if (rs3.next() && rs3.getString(1) != null) {
				openItem.setOpenQty(openItem.getOpenQty() - rs3.getDouble(1));
			}
			
			openItem.setDesc("OPENING STOCK");
			openItem.setFromDate(Utils.convertToSQLDate(fromDate));
			qty = openItem.getOpenQty();

			stmt3 = getConnection().prepareStatement(getPurchaseQty1);

			stmt3.setString(1, itemid);
			stmt3.setString(2, branch);
			stmt3.setString(3, Utils.convertToSQLDate(fromDate));
			stmt3.setString(4, Utils.convertToSQLDate(toDate));

			rs3 = stmt3.executeQuery();

			while(rs3.next()) {
				item = new StockItemModel();
				item.setDesc("PURCHASE ID : <a href=\"javascript:fnShowPurchase('"+prefix.get(rs3.getInt("BRANCHID"))+"P"+Utils.padBillNo(rs3.getInt("PURCHASEID"))+"');\">"+prefix.get(rs3.getInt("BRANCHID"))+"P"+Utils.padBillNo(rs3.getInt("PURCHASEID"))+"</a>");
				item.setFromDate(rs3.getString("RECDDT"));
				item.setPlusQty(rs3.getDouble("QTY"));
				qty = qty + item.getPlusQty();
				dtls.add(item);
			}
			
			stmt4 = getConnection().prepareStatement(getTransferInQty1);

			stmt4.setString(1, itemid);
			stmt4.setString(2, Utils.convertToSQLDate(fromDate));
			stmt4.setString(3, Utils.convertToSQLDate(toDate));
			stmt4.setString(4, branch);

			rs3 = stmt4.executeQuery();

			while(rs3.next()) {
				item = new StockItemModel();
				item.setDesc("TRANSFER ID : <a href=\"javascript:fnShowTransfer('"+prefix.get(rs3.getInt("BRANCHID"))+"ST"+Utils.padBillNo(rs3.getInt("TRANSFERID"))+"');\">"+prefix.get(rs3.getInt("BRANCHID"))+"ST"+Utils.padBillNo(rs3.getInt("TRANSFERID"))+"</a>");
				item.setFromDate(rs3.getString("TRANSFERDATE"));
				item.setPlusQty(rs3.getDouble("QTY"));
				qty = qty + item.getPlusQty();
				dtls.add(item);
			}

			stmt5 = getConnection().prepareStatement(getSalesReturnQty1);

			stmt5.setString(1, itemid);
			stmt5.setString(2, branch);
			stmt5.setString(3, Utils.convertToSQLDate(fromDate));
			stmt5.setString(4, Utils.convertToSQLDate(toDate));

			rs3 = stmt5.executeQuery();

			while(rs3.next()) {
				item = new StockItemModel();
				item.setDesc("CREDIT NOTE ID : <a href=\"javascript:fnShowCreditNote('"+prefix.get(rs3.getInt("BRANCHID"))+"CN"+Utils.padBillNo(rs3.getInt("CREDITNOTEID"))+"');\">"+prefix.get(rs3.getInt("BRANCHID"))+"CN"+Utils.padBillNo(rs3.getInt("CREDITNOTEID"))+"</a>");
				item.setFromDate(rs3.getString("NOTEDATE"));
				item.setPlusQty(rs3.getDouble("QTY"));
				qty = qty + item.getPlusQty();
				dtls.add(item);
			}

			stmt6 = getConnection().prepareStatement(getSoldQty1);

			stmt6.setString(1, itemid);
			stmt6.setString(2, branch);
			stmt6.setString(3, Utils.convertToSQLDate(fromDate));
			stmt6.setString(4, Utils.convertToSQLDate(toDate));

			rs3 = stmt6.executeQuery();

			while(rs3.next()) {
				item = new StockItemModel();
				item.setDesc("SALES ID : <a href=\"javascript:fnShowSales('"+prefix.get(rs3.getInt("BRANCHID"))+Utils.padBillNo(rs3.getInt("SALESID"))+"');\">"+prefix.get(rs3.getInt("BRANCHID"))+Utils.padBillNo(rs3.getInt("SALESID"))+"</a>");
				item.setFromDate(rs3.getString("SALESDATE"));
				item.setMinusQty(rs3.getDouble("QTY"));
				qty = qty - item.getMinusQty();
				dtls.add(item);
			}

			stmt7 = getConnection().prepareStatement(getTranferOutQty1);

			stmt7.setString(1, itemid);
			stmt7.setString(2, branch);
			stmt7.setString(3, Utils.convertToSQLDate(fromDate));
			stmt7.setString(4, Utils.convertToSQLDate(toDate));

			rs3 = stmt7.executeQuery();

			while(rs3.next()) {
				item = new StockItemModel();
				item.setDesc("TRANSFER ID : <a href=\"javascript:fnShowTransfer('"+prefix.get(rs3.getInt("BRANCHID"))+"ST"+Utils.padBillNo(rs3.getInt("TRANSFERID"))+"');\">"+prefix.get(rs3.getInt("BRANCHID"))+"ST"+Utils.padBillNo(rs3.getInt("TRANSFERID"))+"</a>");
				item.setFromDate(rs3.getString("TRANSFERDATE"));
				item.setMinusQty(rs3.getDouble("QTY"));
				qty = qty - item.getMinusQty();
				dtls.add(item);
			}

			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty1);

			stmt8.setString(1, itemid);
			stmt8.setString(2, branch);
			stmt8.setString(3, Utils.convertToSQLDate(fromDate));
			stmt8.setString(4, Utils.convertToSQLDate(toDate));

			rs3 = stmt8.executeQuery();

			while(rs3.next()) {
				item = new StockItemModel();
				item.setDesc("PURCHASE RETURN ID : <a href=\"javascript:fnShowPurchaseReturn('"+prefix.get(rs3.getInt("BRANCHID"))+"PR"+Utils.padBillNo(rs3.getInt("RETURNID"))+"');\">"+prefix.get(rs3.getInt("BRANCHID"))+"PR"+Utils.padBillNo(rs3.getInt("RETURNID"))+"</a>");
				item.setFromDate(rs3.getString("RETURNDATE"));
				item.setMinusQty(rs3.getDouble("QTY"));
				qty = qty - item.getMinusQty();
				dtls.add(item);
			}
			
			Collections.sort(dtls, new StockRegisterDateComparator());
			
			result = new StockItemModel[dtls.size()+2];
			result[0] = openItem;
			Iterator<StockItemModel> itr = dtls.iterator();
			int i = 1;
			while(itr.hasNext()){
				result[i++] = itr.next();
			}
			item = new StockItemModel();
			item.setDesc("CLOSING STOCK");
			item.setFromDate(Utils.convertToSQLDate(toDate));
			item.setOpenQty(qty);
			result[i++] = item;
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt2 != null)
					stmt2.close();
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
		
		return result;
	}

	public StockModel getStockOnDateReport(StockModel model, ItemModel[] items) {
		HashMap<Integer, StockItemModel> itemMap = new HashMap<Integer, StockItemModel>(0);
		StockItemModel item = null;

		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement stmt8 = null;
		ResultSet rs3 = null;
		int itemId = 0;

		// query to get the qty sold
		String getOpeningQty = "SELECT QTY FROM OPENING_ITEM_STOCK WHERE ITEMID = ? AND BRANCHID = ?";
		
		// query to get the qty sold
		String getSoldQty = "SELECT SUM(QTY) FROM SALES_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND SALESID IN ( SELECT A.SALESID FROM SALES A WHERE A.SALESDATE <= ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseQty = "SELECT SUM(QTY) FROM PURCHASE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND PURCHASEID IN ( SELECT A.PURCHASEID FROM PURCHASE A WHERE A.RECDDT <= ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getTransferInQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TRANSFERDATE <= ? AND A.TOID = ?)";

		// query to get the qty sold
		String getTranferOutQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND BRANCHID = ?  AND TRANSFERID IN ( SELECT A.TRANSFERID FROM TRANSFER A WHERE A.TRANSFERDATE <= ? AND A.FROMID = ?)";

		// query to get the qty sold
		String getSalesReturnQty = "SELECT SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND CREDITNOTEID IN ( SELECT A.CREDITNOTEID FROM CREDIT_NOTE A WHERE A.NOTEDATE <= ? AND A.BRANCHID = ?)";

		// query to get the qty sold
		String getPurchaseReturnQty = "SELECT SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE ITEMID = ? AND BRANCHID = ? AND RETURNID IN ( SELECT A.RETURNID FROM PURCHASE_RETURN A WHERE A.RETURNDATE <= ? AND A.BRANCHID = ?)";

		try{
			
			stmt2 = getConnection().prepareStatement(getOpeningQty);
			stmt3 = getConnection().prepareStatement(getPurchaseQty);
			stmt4 = getConnection().prepareStatement(getTransferInQty);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty);
			stmt6 = getConnection().prepareStatement(getSoldQty);
			stmt7 = getConnection().prepareStatement(getTranferOutQty);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty);
			
			

			for (int i=0; i<items.length; i++) {
				item = new StockItemModel();
				itemId = items[i].getItemId();
				item.setItemId(items[i].getItemId());

				stmt2.setInt(1, itemId);
				stmt2.setInt(2, model.getBranch().getAccessId());

				rs3 = stmt2.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt3.setInt(1, itemId);
				stmt3.setInt(2, model.getBranch().getAccessId());
				stmt3.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt3.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt3.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt4.setInt(1, itemId);
				stmt4.setString(2, Utils.convertToSQLDate(model.getFromDate()));
				stmt4.setInt(3, model.getBranch().getAccessId());

				rs3 = stmt4.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt5.setInt(1, itemId);
				stmt5.setInt(2, model.getBranch().getAccessId());
				stmt5.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt5.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt5.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() + rs3.getDouble(1));
				}

				stmt6.setInt(1, itemId);
				stmt6.setInt(2, model.getBranch().getAccessId());
				stmt6.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt6.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt6.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() - rs3.getDouble(1));
				}

				stmt7.setInt(1, itemId);
				stmt7.setInt(2, model.getBranch().getAccessId());
				stmt7.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt7.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt7.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() - rs3.getDouble(1));
				}

				stmt8.setInt(1, itemId);
				stmt8.setInt(2, model.getBranch().getAccessId());
				stmt8.setString(3, Utils.convertToSQLDate(model.getFromDate()));
				stmt8.setInt(4, model.getBranch().getAccessId());

				rs3 = stmt8.executeQuery();

				if (rs3.next() && rs3.getString(1) != null) {
					item.setOpenQty(item.getOpenQty() - rs3.getDouble(1));
				}
				
				itemMap.put(item.getItemId(), item);
			}
			
			Iterator<Integer> ids = itemMap.keySet().iterator();
			
			while(ids.hasNext()){
				model.getItems().add(itemMap.get(ids.next()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
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

}
