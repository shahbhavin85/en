package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;
import com.en.util.Utils;

public class ItemBroker extends BaseBroker{

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public ItemBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new Item
	 * 
	 * @param ItemModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addItem(ItemModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long itemId = 1;
		
		// query to get the max of item id
		String getMaxItemId = "SELECT MAX(ITEMID) FROM ITEM";
		
		// query to check the existence of item
		String checkItem = "SELECT COUNT(*) FROM ITEM WHERE ITEMNUMBER = ? AND STATUS = 'A'";
		
		// query to add the item details
		String insertItem = "INSERT INTO ITEM (ITEMID, CATEGORYID, ITEMNAME, ITEMNUMBER, ITEMLABEL, PRICE, DEVTIME, TAX, PL, ALTERDATE, STATUS) VALUES (?,?,?,?,?,?,?,?,?,CURRENT_DATE,'A')";
		
		// query to add the item tax details
		String insertTax = "INSERT INTO ITEM_TAX (ITEMID, ACCESSID, SLAB) VALUES (?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(checkItem);
			stmt.setString(1, model.getItemNumber());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Item Number already exists.");
				messages.addNewMessage(msg);
			} else {
				stmt = getConnection().prepareStatement(getMaxItemId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					itemId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertItem);
				stmt.setLong(1, itemId);
				stmt.setInt(2, model.getItemCatId());
				stmt.setString(3, model.getItemName());
				stmt.setString(4, model.getItemNumber());
				stmt.setString(5, model.getItemLabel());
				stmt.setString(6, model.getItemPrice());
				stmt.setString(7, model.getDevTime());
				stmt.setString(8, model.getTax());
				stmt.setInt(9, model.isInPL() ? 0 : 1);
				
				stmt.executeUpdate();
				
				AccessPointModel[] branches = (new AccessPointBroker(getConnection())).getAccessPoint();
				
				stmt = getConnection().prepareStatement(insertTax);
				
				for(int i=0; i<branches.length; i++){
					if(branches[i].getAccessId() > 1){
						stmt.setLong(1, itemId);
						stmt.setInt(2, branches[i].getAccessId());
						stmt.setString(3, model.getTax());
						stmt.addBatch();
					}
				}
				
				if(branches.length >1)
					stmt.executeBatch();
				
				msg = new Message(SUCCESS, "Item added successfully.");
				messages.addNewMessage(msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}
	
	/**
	 * Method to get list all the available items
	 *  
	 * @return array of ItemModel
	 */
	public ItemModel[] getItem(){
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		ItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT ITEMID, ITEMNUMBER, ITEMNAME, ITEMLABEL, PRICE, CATALOGUE FROM ITEM WHERE STATUS = 'A' ORDER BY ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemLabel(rs.getString("ITEMLABEL"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemPrice(rs.getString("PRICE"));
				if(rs.getInt("CATALOGUE") == 1)
					model.setCatalogue();
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
		
		return (ItemModel[]) items.toArray(new ItemModel[0]);
	}
	
	/**
	 * Method to get list all the available items
	 *  
	 * @return array of ItemModel
	 */
	public ItemModel[] getSalesItem(){
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		ItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.ITEMLABEL, A.PRICE, B.DISCOUNT FROM ITEM A LEFT JOIN ITEM_DISCOUNT B ON A.ITEMID = B.ITEMID AND B.FROMDATE <= CURRENT_DATE AND B.TODATE >= CURRENT_DATE WHERE A.STATUS = 'A' AND A.FORSALES = 0 ORDER BY A.ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemLabel(rs.getString("ITEMLABEL"));
				model.setItemPrice(rs.getString("PRICE"));
				if(rs.getString("DISCOUNT") != null){
					model.setDiscount(rs.getString("DISCOUNT"));
				}
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
		
		return (ItemModel[]) items.toArray(new ItemModel[0]);
	}
	
	/**
	 * Method to get list all the available offer items
	 *  
	 * @return array of ItemModel
	 */
	public ItemModel[] getALLItems(){
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		ItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT ITEMID, ITEMNAME, ITEMNUMBER, ITEMLABEL, PRICE  FROM ITEM WHERE STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemLabel(rs.getString("ITEMLABEL"));
				model.setItemPrice(rs.getString("PRICE"));
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
		
		return (ItemModel[]) items.toArray(new ItemModel[0]);
	}
	
	/**
	 * Method to get the details of the selected itemId
	 * 
	 * @param itemId
	 * @return ItemModel with item details
	 */
	public ItemModel getItemDtls(int itemId) {
		ItemModel model = new ItemModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the item group
		String getItemDtls = "SELECT ITEMID, CATEGORYID, ITEMNAME, ITEMNUMBER, ITEMLABEL, PRICE, DEVTIME, TAX, PL, FORSALES, CATALOGUE, PHOTO FROM ITEM WHERE ITEMID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			stmt.setInt(1, itemId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemCatId(rs.getInt("CATEGORYID"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemLabel(rs.getString("ITEMLABEL"));
				model.setItemPrice(rs.getString("PRICE"));
				model.setDevTime(rs.getString("DEVTIME"));
				model.setTax(rs.getString("TAX"));
				if(rs.getInt("PL") == 1)
					model.setInPL();
				if(rs.getInt("FORSALES") == 1)
					model.setInSales();
				if(rs.getInt("CATALOGUE") == 1)
					model.setCatalogue();
				if(rs.getInt("PHOTO") == 1)
					model.setPhoto();
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
		
		return model;
	}
	
	/**
	 * Method to update an existing Item
	 * 
	 * @param ItemModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateItem(ItemModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of item category
		String checkItemCategory = "SELECT COUNT(*) FROM ITEM WHERE ITEMNUMBER = ? AND ITEMID <> ? AND STATUS = 'A'";
		
		// query to update the item group details
		String updateItemCategory = "UPDATE ITEM SET CATEGORYID = ? , ITEMNAME = ?, ITEMNUMBER = ?, PRICE = ?, DEVTIME = ?, ITEMLABEL = ?, PL=?, FORSALES = ?, " +
				"ALTERDATE = CURRENT_DATE WHERE ITEMID = ? AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(checkItemCategory);
			stmt.setString(1, model.getItemNumber());
			stmt.setInt(2, model.getItemId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "Item Number already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateItemCategory);
				stmt.setInt(1, model.getItemCatId());
				stmt.setString(2, model.getItemName());
				stmt.setString(3, model.getItemNumber());
				stmt.setString(4, model.getItemPrice());
				stmt.setString(5, model.getDevTime());
				stmt.setString(6, model.getItemLabel());
				stmt.setInt(7, model.isInPL() ? 0 : 1);
				stmt.setInt(8, model.isInSales() ? 0 : 1);
				stmt.setInt(9, model.getItemId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Item modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying an Item.");
				}
				messages.addNewMessage(msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}

	public ItemModel[] getItemDiscountDtls() {
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		ItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.ITEMLABEL, A.PRICE, B.DISCOUNT, B.FROMDATE, B.TODATE FROM ITEM A LEFT JOIN ITEM_DISCOUNT B ON A.ITEMID = B.ITEMID AND B.TODATE >= CURRENT_DATE WHERE A.STATUS = 'A' ORDER BY A.ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemLabel(rs.getString("ITEMLABEL"));
				model.setItemPrice(rs.getString("PRICE"));
				if(rs.getString("DISCOUNT") != null){
					model.setDiscount(rs.getString("DISCOUNT"));
					model.setFromDate(Utils.convertToAppDate(rs.getString("FROMDATE")));
					model.setToDate(Utils.convertToAppDate(rs.getString("TODATE")));
				}
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
		
		return (ItemModel[]) items.toArray(new ItemModel[0]);
	}

	public HashMap<String,HashMap<String,ItemModel[]>> getItemPriceList() {
		HashMap<String, HashMap<String, ItemModel[]>> items = new HashMap<String,HashMap<String,ItemModel[]>>(0);
		ItemModel model = null;
		ArrayList<ItemModel> temp = null;
		HashMap<String, ItemModel[]> tempMap = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		int groupId = 0;
		int catId = 0;

		// query to get all the existing item group
		String getGroups = "SELECT GROUPID, GROUPNAME FROM ITEM_GROUP ORDER BY GROUPNAME";
		
		// query to get all the existing item group
		String getCategory = "SELECT CATEGORYID, CATEGORYNAME FROM ITEM_CATEGORY WHERE GROUPID = ? ORDER BY CATEGORYNAME";
		
		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.ITEMLABEL, A.PRICE, B.DISCOUNT, B.FROMDATE, B.TODATE, A.WPRICE FROM ITEM A LEFT JOIN ITEM_DISCOUNT B ON A.ITEMID = B.ITEMID AND B.TODATE >= CURRENT_DATE WHERE A.CATEGORYID = ? AND A.STATUS = 'A' AND PL = 0 ORDER BY A.ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getGroups);
			stmt1 = getConnection().prepareStatement(getCategory);
			stmt2 = getConnection().prepareStatement(getItems);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				tempMap = new HashMap<String, ItemModel[]>(0); 
				
				groupId = rs.getInt("GROUPID");
				
				stmt1.setInt(1, groupId);
				
				rs1 = stmt1.executeQuery();
				
				while(rs1.next()){
					temp = new ArrayList<ItemModel>(0);
					
					catId = rs1.getInt("CATEGORYID");
					
					stmt2.setInt(1, catId);
					
					rs2 = stmt2.executeQuery();
					
					while(rs2.next()){				
						model = new ItemModel();
						model.setItemId(rs2.getInt("ITEMID"));
						model.setItemNumber(rs2.getString("ITEMNUMBER"));
						model.setItemName(rs2.getString("ITEMNAME"));
						model.setItemLabel(rs2.getString("ITEMLABEL"));
						model.setItemPrice(rs2.getString("PRICE"));
						model.setItemWPrice(rs2.getString("WPRICE"));
						if(rs2.getString("DISCOUNT") != null){
							model.setDiscount(rs2.getString("DISCOUNT"));
							model.setFromDate(rs2.getString("FROMDATE"));
							model.setToDate(rs2.getString("TODATE"));
						}
						temp.add(model);
					}
					
					if(temp.size()>0){
						tempMap.put(rs1.getString("CATEGORYNAME"), (ItemModel[])temp.toArray(new ItemModel[0]));
					}
				}
				
				if(tempMap.keySet().size()>0){
					items.put(rs.getString("GROUPNAME"), tempMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(stmt1 != null)
					stmt1.close();
				if(rs1 != null)
					rs1.close();
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
		
		return items;
	}

	public HashMap<String,HashMap<String,ItemModel[]>> getBranchItemPriceList(AccessPointModel access) {
		HashMap<String, HashMap<String, ItemModel[]>> items = new HashMap<String,HashMap<String,ItemModel[]>>(0);
		ItemModel model = null;
		ArrayList<ItemModel> temp = null;
		HashMap<String, ItemModel[]> tempMap = null;
		HashMap<Integer, Double> taxRates = new HashMap<Integer, Double>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		int groupId = 0;
		int catId = 0;

		// query to get all the existing item group
		String getGroups = "SELECT GROUPID, GROUPNAME FROM ITEM_GROUP ORDER BY GROUPNAME";
		
		// query to get all the existing item group
		String getCategory = "SELECT CATEGORYID, CATEGORYNAME FROM ITEM_CATEGORY WHERE GROUPID = ? ORDER BY CATEGORYNAME";
		
		// query to the tax slabs
		String getTaxSlabs = "SELECT VAT1, VAT2, VAT3 FROM TAX WHERE ACCESSID = ?";
		
		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.ITEMLABEL, A.PRICE, B.DISCOUNT, B.FROMDATE, B.TODATE, C.SLAB, A.WPRICE FROM ITEM_TAX C, ITEM A LEFT JOIN ITEM_DISCOUNT B ON A.ITEMID = B.ITEMID AND B.TODATE >= CURRENT_DATE WHERE A.CATEGORYID = ? AND A.STATUS = 'A' AND PL = 0 AND C.ITEMID = A.ITEMID AND C.ACCESSID = ? ORDER BY A.ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getTaxSlabs);
			stmt.setInt(1, access.getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				taxRates.put(1, rs.getDouble("VAT1"));
				taxRates.put(2, rs.getDouble("VAT2"));
				taxRates.put(3, rs.getDouble("VAT3"));
			}
			
			stmt = getConnection().prepareStatement(getGroups);
			stmt1 = getConnection().prepareStatement(getCategory);
			stmt2 = getConnection().prepareStatement(getItems);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				tempMap = new HashMap<String, ItemModel[]>(0); 
				
				groupId = rs.getInt("GROUPID");
				
				stmt1.setInt(1, groupId);
				
				rs1 = stmt1.executeQuery();
				
				while(rs1.next()){
					temp = new ArrayList<ItemModel>(0);
					
					catId = rs1.getInt("CATEGORYID");
					
					stmt2.setInt(1, catId);
					stmt2.setInt(2, access.getAccessId());
					
					rs2 = stmt2.executeQuery();
					
					while(rs2.next()){				
						model = new ItemModel();
						model.setItemId(rs2.getInt("ITEMID"));
						model.setItemNumber(rs2.getString("ITEMNUMBER"));
						model.setItemName(rs2.getString("ITEMNAME"));
						model.setItemLabel(rs2.getString("ITEMLABEL"));
						model.setItemPrice(rs2.getString("PRICE"));
						model.setItemWPrice(rs2.getString("WPRICE"));
						if(rs2.getString("DISCOUNT") != null){
							model.setDiscount(rs2.getString("DISCOUNT"));
							model.setFromDate(rs2.getString("FROMDATE"));
							model.setToDate(rs2.getString("TODATE"));
						}
						model.setTax(taxRates.get(rs2.getInt("SLAB"))+"");
						temp.add(model);
					}
					
					if(temp.size()>0){
						tempMap.put(rs1.getString("CATEGORYNAME"), (ItemModel[])temp.toArray(new ItemModel[0]));
					}
				}
				
				if(tempMap.keySet().size()>0){
					items.put(rs.getString("GROUPNAME"), tempMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(stmt1 != null)
					stmt1.close();
				if(rs1 != null)
					rs1.close();
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
		
		return items;
	}
	
	public MessageModel additemDiscount(ArrayList<ItemModel> items) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		Iterator<ItemModel> itr = null;
		ItemModel item = null;
		
		// query to check the existence of item category
		String deletePreviousDtls = "DELETE FROM ITEM_DISCOUNT WHERE ITEMID = ?";
		
		// query to update the item group details
		String insertNewDtls = "INSERT INTO ITEM_DISCOUNT (ITEMID, DISCOUNT, FROMDATE, TODATE) VALUES (?,?,?,?)";
		
		try{
			
			itr = items.iterator();
			
			while(itr.hasNext()){
				item = itr.next();
			
				stmt = getConnection().prepareStatement(deletePreviousDtls);
				stmt.setInt(1, item.getItemId());
				stmt.execute();
				
				stmt = getConnection().prepareStatement(insertNewDtls);
				stmt.setInt(1, item.getItemId());
				stmt.setString(2, item.getDiscount());
				stmt.setString(3, item.getFromDate());
				stmt.setString(4, item.getToDate());
				
				stmt.executeUpdate();
			}
			
			msg = new Message(SUCCESS, "Item discount details updated successfully.");
			messages.addNewMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}

	public ItemModel[] getTransferItem() {
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		ItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, B.RATE FROM ITEM A LEFT JOIN PURCHASE_ITEM B ON A.ITEMID = B.ITEMID AND CONCAT(B.PURCHASEID, B.BRANCHID) =  (SELECT CONCAT(C.PURCHASEID, C.BRANCHID) FROM PURCHASE C ORDER BY C.RECDDT LIMIT 1) WHERE STATUS = 'A' ORDER BY ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemPrice((rs.getString("RATE") != null) ? rs.getString("RATE") : "0");
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
		
		return (ItemModel[]) items.toArray(new ItemModel[0]);
	}

	public HashMap<String, HashMap<String, SalesItemModel[]>> getStockReport(int branchId) {
		HashMap<String, HashMap<String, SalesItemModel[]>> items = new HashMap<String,HashMap<String,SalesItemModel[]>>(0);
		SalesItemModel model = null;
		ArrayList<SalesItemModel> temp = null;
		HashMap<String, SalesItemModel[]> tempMap = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement stmt7 = null;
		PreparedStatement stmt8 = null;
		PreparedStatement stmt9 = null;
		PreparedStatement stmt10 = null;
		ResultSet rs3 = null;
		int groupId = 0;
		int catId = 0;
		int itemId = 0;
		double qty = 0;

		// query to get all the existing item group
		String getGroups = "SELECT GROUPID, GROUPNAME FROM ITEM_GROUP ORDER BY GROUPNAME";
		
		// query to get all the existing item group
		String getCategory = "SELECT CATEGORYID, CATEGORYNAME FROM ITEM_CATEGORY WHERE GROUPID = ? ORDER BY CATEGORYNAME";
		
		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.ITEMLABEL, A.PRICE, B.DISCOUNT, B.FROMDATE, B.TODATE, A.WPRICE FROM ITEM A LEFT JOIN ITEM_DISCOUNT B ON A.ITEMID = B.ITEMID AND B.TODATE >= CURRENT_DATE WHERE A.CATEGORYID = ? AND A.STATUS = 'A' ORDER BY A.ITEMNUMBER";

		// query to get the qty sold
		String getOpeningQty = "SELECT QTY FROM OPENING_ITEM_STOCK WHERE ITEMID = ? AND BRANCHID = ?";
		
		// query to get the qty sold
		String getSoldQty = "SELECT SUM(QTY) FROM SALES_ITEM WHERE ITEMID = ? AND BRANCHID = ?";

		// query to get the qty sold
		String getSalesReturnedQty = "SELECT SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE ITEMID = ? AND BRANCHID = ?";

		// query to get the qty sold
		String getPurchaseQty = "SELECT SUM(QTY) FROM PURCHASE_ITEM WHERE ITEMID = ? AND BRANCHID = ?";

		// query to get the qty sold
		String getTransferInQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND (TRANSFERID ,BRANCHID) IN (SELECT A.TRANSFERID, A.FROMID FROM TRANSFER A WHERE A.TOID = ?)";

		// query to get the qty sold
		String getTranferOutQty = "SELECT SUM(QTY) FROM TRANSFER_ITEM WHERE ITEMID = ? AND BRANCHID = ?";

		// query to get the qty sold
		String getSalesReturnQty = "SELECT SUM(QTY) FROM CREDIT_NOTE_ITEM WHERE ITEMID = ? AND BRANCHID = ?";

		// query to get the qty sold
		String getPurchaseReturnQty = "SELECT SUM(QTY) FROM PURCHASE_RETURN_ITEM WHERE ITEMID = ? AND BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getGroups);
			stmt1 = getConnection().prepareStatement(getCategory);
			stmt2 = getConnection().prepareStatement(getItems);
			stmt3 = getConnection().prepareStatement(getPurchaseQty);
			stmt4 = getConnection().prepareStatement(getTransferInQty);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty);
			stmt6 = getConnection().prepareStatement(getSoldQty);
			stmt7 = getConnection().prepareStatement(getTranferOutQty);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty);
			stmt9 = getConnection().prepareStatement(getOpeningQty);
			stmt10 = getConnection().prepareStatement(getSalesReturnedQty);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				tempMap = new HashMap<String, SalesItemModel[]>(0); 
				
				groupId = rs.getInt("GROUPID");
				
				stmt1.setInt(1, groupId);
				
				rs1 = stmt1.executeQuery();
				
				while(rs1.next()){
					temp = new ArrayList<SalesItemModel>(0);
					
					catId = rs1.getInt("CATEGORYID");
					
					stmt2.setInt(1, catId);
					
					rs2 = stmt2.executeQuery();
					
					while(rs2.next()){		
						qty = 0;
						model = new SalesItemModel();
						itemId = rs2.getInt("ITEMID");
						model.getItem().setItemId(rs2.getInt("ITEMID"));
						model.getItem().setItemNumber(rs2.getString("ITEMNUMBER"));
						model.getItem().setItemName(rs2.getString("ITEMNAME"));
						
						stmt10.setInt(1, itemId);
						stmt10.setInt(2, branchId);
						
						rs3 = stmt10.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty + rs3.getDouble(1);
						}
						
						stmt9.setInt(1, itemId);
						stmt9.setInt(2, branchId);
						
						rs3 = stmt9.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty + rs3.getDouble(1);
						}
						
						stmt3.setInt(1, itemId);
						stmt3.setInt(2, branchId);
						
						rs3 = stmt3.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty + rs3.getDouble(1);
						}
						
						stmt4.setInt(1, itemId);
						stmt4.setInt(2, branchId);
						
						rs3 = stmt4.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty + rs3.getDouble(1);
						}
						
						stmt5.setInt(1, itemId);
						stmt5.setInt(2, branchId);
						
						rs3 = stmt5.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty + rs3.getDouble(1);
						}
						
						stmt6.setInt(1, itemId);
						stmt6.setInt(2, branchId);
						
						rs3 = stmt6.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty - rs3.getDouble(1);
						}
						
						stmt7.setInt(1, itemId);
						stmt7.setInt(2, branchId);
						
						rs3 = stmt7.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty - rs3.getDouble(1);
						}
						
						stmt8.setInt(1, itemId);
						stmt8.setInt(2, branchId);
						
						rs3 = stmt8.executeQuery();
						
						if(rs3.next() && rs3.getString(1) != null){
							qty = qty - rs3.getDouble(1);
						}
						
						model.setQty(qty);
						
						temp.add(model);
					}
					
					if(temp.size()>0){
						tempMap.put(rs1.getString("CATEGORYNAME"), (SalesItemModel[])temp.toArray(new SalesItemModel[0]));
					}
				}
				
				if(tempMap.keySet().size()>0){
					items.put(rs.getString("GROUPNAME"), tempMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(stmt1 != null)
					stmt1.close();
				if(rs1 != null)
					rs1.close();
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
		
		return items;
	}

	public SalesItemModel[] getOpeningStock(int accessId) {
		SalesItemModel model = null;
		ArrayList<SalesItemModel> temp = new ArrayList<SalesItemModel>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, B.QTY FROM ITEM A LEFT JOIN OPENING_ITEM_STOCK B ON (B.ITEMID = A.ITEMID AND B.BRANCHID = ?) ORDER BY A.ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getItems);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new SalesItemModel();
				model.getItem().setItemId(rs.getInt("ITEMID"));
				model.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
				model.getItem().setItemName(rs.getString("ITEMNAME"));
				model.setQty((rs.getString("QTY") != null) ? rs.getDouble("QTY") : 0);
				
				temp.add(model);
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
		
		return (SalesItemModel[]) temp.toArray(new SalesItemModel[0]);
	}

	public MessageModel updateItemStock(ArrayList<SalesItemModel> items,
			int accessId) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to check the existence of item category
		String deletePreviousData = "DELETE FROM OPENING_ITEM_STOCK WHERE BRANCHID = ?";
		
		// query to update the item group details
		String insertNewData = "INSERT INTO OPENING_ITEM_STOCK (ITEMID, BRANCHID, QTY) VALUES (?,?,?)";
		
		try{
				
			stmt = getConnection().prepareStatement(deletePreviousData);
			stmt.setInt(1, accessId);
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(insertNewData);
			
			for(int i=0; i<items.size(); i++){
				stmt.setInt(1, items.get(i).getItem().getItemId());
				stmt.setInt(2, accessId);
				stmt.setDouble(3, items.get(i).getQty());
				
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			msg = new Message(SUCCESS, "Opening stock updated successfully.");
			messages.addNewMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}

	public HashMap<String, Integer> getItemCheckList(String itemId) {
		HashMap<String, Integer> checkLst = new HashMap<String, Integer>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to get all the existing item group
		String getItems = "SELECT ITEMID, DESP, QTY FROM ITEM_CHECK_LIST WHERE ITEMID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getItems);
			stmt.setString(1, itemId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				checkLst.put(rs.getString("DESP"), rs.getInt("QTY"));
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
		return checkLst;
	}

	public MessageModel updateItemCheckList(String itemId, HashMap<String, Integer> dtls) {
		MessageModel messages = new MessageModel();
		
		PreparedStatement stmt = null;

		// query to get all the existing item group
		String getItems = "DELETE FROM ITEM_CHECK_LIST WHERE ITEMID = ?";
		
		// query to insert the check list details
		String insertDetails = "INSERT INTO ITEM_CHECK_LIST VALUES (?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(getItems);
			stmt.setString(1, itemId);
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(insertDetails);
			
			Iterator<String> itr = dtls.keySet().iterator();
			String key = "";
			
			while(itr.hasNext()){
				key = itr.next();
				stmt.setString(1, itemId);
				stmt.setString(2, key);
				stmt.setInt(3, dtls.get(key));
				
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			
			messages.addNewMessage(new Message(SUCCESS, "Updated Successfully!!"));
			
		} catch (Exception e) {
			e.printStackTrace();
			messages.addNewMessage(new Message(ERROR, e.getMessage()));
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
		return messages;
	}

	public boolean checkItemPhoto(String itemId) {
		boolean isPresent = false;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the item group
		String getItemDtls = "SELECT PHOTO FROM ITEM WHERE ITEMID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			stmt.setString(1, itemId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				if(rs.getInt("PHOTO") == 1)
					isPresent = true;
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
		
		return isPresent;
	}

	public boolean checkItemCatalogue(String itemId) {
		boolean isPresent = false;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the item group
		String getItemDtls = "SELECT CATALOGUE FROM ITEM WHERE ITEMID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			stmt.setString(1, itemId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				if(rs.getInt("CATALOGUE") == 1)
					isPresent = true;
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
		
		return isPresent;
	}

	public void uploadPicture(String itemId) {
		
		PreparedStatement stmt = null;
		
		// query to update the item group details
		String updateItemCategory = "UPDATE ITEM SET PHOTO = 1 " +
				" WHERE ITEMID = ? AND STATUS = 'A'";
		
		try{				
			stmt = getConnection().prepareStatement(updateItemCategory);
			stmt.setString(1, itemId);
			
			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
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
		
		return;
	}

	public void uploadCatalogue(String itemId) {
		
		PreparedStatement stmt = null;
		
		// query to update the item group details
		String updateItemCategory = "UPDATE ITEM SET CATALOGUE = 1 " +
				" WHERE ITEMID = ? AND STATUS = 'A'";
		
		try{				
			stmt = getConnection().prepareStatement(updateItemCategory);
			stmt.setString(1, itemId);

			stmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
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
		
		return;
	}

	public ItemModel[] getUpdatedItems() {
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		ItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT ITEMID, ITEMNAME, ITEMNUMBER, ITEMLABEL, PRICE, ALTERDATE  FROM ITEM WHERE STATUS = 'A' AND ALTERDATE <= CURRENT_DATE AND ALTERDATE >= DATE_SUB(CURRENT_DATE, INTERVAL 14 DAY) ORDER BY ALTERDATE DESC";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setItemLabel(rs.getString("ITEMLABEL"));
				model.setAlterDate(rs.getString("ALTERDATE"));
				model.setItemPrice(rs.getString("PRICE"));
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
		
		return (ItemModel[]) items.toArray(new ItemModel[0]);
	}

	public String getPriceListVersion() {
		String version = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT MAX(ALTERDATE) FROM ITEM";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				version = (rs.getString(1).substring(2,4))+"."+(rs.getString(1).substring(5,7))+"."+(rs.getString(1).substring(8));
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
		
		return version;
	}

}
