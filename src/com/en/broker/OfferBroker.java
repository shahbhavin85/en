package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OfferItemModel;
import com.en.model.OfferModel;

public class OfferBroker extends BaseBroker {

	public OfferBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new offer
	 * 
	 * @param OfferModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addOffer(OfferModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long offerId = 1;
		
		// query to get the max of offer id
		String getMaxOfferId = "SELECT MAX(OFFERID) FROM OFFER";
		
		// query to add the Offer details
		String insertOffer = "INSERT INTO OFFER (OFFERID, NAME, OFFERPRICE, FROMDATE, TODATE, PACKING, FORWARDING, INSTALLATION, STATUS) VALUES (?,?,?,?,?,?,?,?,'A')";
		
		// query to add offer item details
		String insertOfferItems = "INSERT INTO OFFER_ITEM (OFFERID, ITEMID, QTY) VALUES (?,?,?)";
		
		try{
			stmt = getConnection().prepareStatement(getMaxOfferId);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1) > 0){
				offerId = rs.getLong(1) + 1;
			}
			
			stmt = getConnection().prepareStatement(insertOffer);
			stmt.setLong(1, offerId);
			stmt.setString(2, model.getOfferName());
			stmt.setString(3, model.getOfferPrice());
			stmt.setString(4, model.getSQLFromDate());
			stmt.setString(5, model.getSQLToDate());
			stmt.setDouble(6, model.getPacking());
			stmt.setDouble(7, model.getForwarding());
			stmt.setDouble(8, model.getInstallation());
			
			stmt.execute();
			
			if(model.getOfferItems().size()>0){
				stmt.clearBatch();
				stmt = getConnection().prepareStatement(insertOfferItems);
				OfferItemModel temp = null;
				Iterator<OfferItemModel> offerItems = model.getOfferItems().iterator();
				while(offerItems.hasNext()){
					temp = offerItems.next();
					stmt.setLong(1, offerId);
					stmt.setInt(2, temp.getItem().getItemId());
					stmt.setInt(3, temp.getQty());
					
					stmt.addBatch();
				}
				
				stmt.executeBatch();
			}
			
			
			msg = new Message(SUCCESS, "Offer added successfully. Offer No is :"+offerId);
			messages.addNewMessage(msg);
		
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
		String getItemGroups = "SELECT ITEMID, ITEMNUMBER FROM ITEM WHERE STATUS = 'A' ORDER BY ITEMNUMBER";
		
		try{
			
			stmt = getConnection().prepareStatement(getItemGroups);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new ItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
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
	 * @param offerId
	 * @return OfferModel with offer details
	 */
	public OfferModel getOfferDtls(int offerId) {
		OfferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isValidOffer = false;
		
		// query to get details of the offer
		String getOfferDtls = "SELECT OFFERID, NAME, OFFERPRICE, FROMDATE, TODATE, PACKING, FORWARDING, INSTALLATION FROM OFFER WHERE OFFERID = ? AND STATUS = 'A'";
		
		// query to get the offer item details
		String getOfferItems = "SELECT A.ITEMNUMBER, B.ITEMID, B.QTY FROM ITEM A, OFFER_ITEM B WHERE A.ITEMID = B.ITEMID AND B.OFFERID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getOfferDtls);
			stmt.setInt(1, offerId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model = new OfferModel();
				model.setOfferid(rs.getInt("OFFERID"));
				model.setOfferName(rs.getString("NAME"));
				model.setOfferPrice(rs.getString("OFFERPRICE"));
				model.setFromDate(rs.getString("FROMDATE"));
				model.setToDate(rs.getString("TODATE"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARDING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				isValidOffer = true;
			}
			
			if(isValidOffer){
				stmt = getConnection().prepareStatement(getOfferItems);
				stmt.setInt(1, offerId);
				ItemModel temp = null;
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					temp = new ItemModel();
					temp.setItemId(rs.getInt("ITEMID"));
					temp.setItemNumber(rs.getString("ITEMNUMBER"));
					model.addOfferItem(temp, rs.getInt("QTY"));
				}
				
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
	 * Method to update an existing offer
	 * 
	 * @param OfferModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateOffer(OfferModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to update the offer details
		String updateOfferDetails = "UPDATE OFFER SET NAME = ? , OFFERPRICE = ?, FROMDATE = ?, TODATE = ?, PACKING = ?, FORWARDING = ?, INSTALLATION = ? WHERE OFFERID = ? AND STATUS = 'A'";
		
		// query to delete all the item related to offer
		String deleteOfferItem = "DELETE FROM OFFER_ITEM WHERE OFFERID = ?";

		// query to add offer item details
		String insertOfferItems = "INSERT INTO OFFER_ITEM (OFFERID, ITEMID, QTY) VALUES (?,?,?)";
		
		try{
				
			stmt = getConnection().prepareStatement(updateOfferDetails);
			stmt.setString(1, model.getOfferName());
			stmt.setString(2, model.getOfferPrice());
			stmt.setString(3, model.getSQLFromDate());
			stmt.setString(4, model.getSQLToDate());
			stmt.setDouble(5, model.getPacking());
			stmt.setDouble(6, model.getForwarding());
			stmt.setDouble(7, model.getInstallation());
			stmt.setInt(8, model.getOfferid());
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(deleteOfferItem);
			stmt.setInt(1, model.getOfferid());
			
			stmt.execute();
			
			stmt.clearBatch();
			stmt = getConnection().prepareStatement(insertOfferItems);
			OfferItemModel temp = null;
			Iterator<OfferItemModel> offerItems = model.getOfferItems().iterator();
			while(offerItems.hasNext()){
				temp = offerItems.next();
				stmt.setLong(1, model.getOfferid());
				stmt.setInt(2, temp.getItem().getItemId());
				stmt.setInt(3, temp.getQty());
				
				stmt.addBatch();
			}
			
			stmt.executeBatch();
				
			msg = new Message(SUCCESS, "Offer Details modified successfully.");
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
	
	public OfferModel[] getOfferOfItems(String itemId){
		ArrayList<OfferModel> models = new ArrayList<OfferModel>(0);
		OfferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		int offerId = 0;
		
		// query to get details of the offer
		String getOfferDtls = "SELECT A.OFFERID, A.NAME, A.OFFERPRICE, A.FROMDATE, A.TODATE, A.PACKING, A.FORWARDING, A.INSTALLATION, B.ITEMNUMBER, B.PRICE FROM OFFER A, ITEM B, OFFER_ITEM C WHERE C.OFFERID = A.OFFERID AND C.ITEMID = B.ITEMID AND C.ITEMID = ? AND A.STATUS = 'A'" +
				" AND A.FROMDATE <= CURRENT_DATE AND A.TODATE >= CURRENT_DATE";
		
		// query to get the offer item details
		String getOfferItems = "SELECT A.ITEMNUMBER, B.ITEMID, B.QTY, A.PRICE FROM ITEM A, OFFER_ITEM B WHERE A.ITEMID = B.ITEMID AND B.OFFERID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getOfferDtls);
			stmt.setString(1, itemId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OfferModel();
				offerId = rs.getInt("OFFERID");
				model.setOfferid(rs.getInt("OFFERID"));
				model.setOfferPrice(rs.getString("OFFERPRICE"));
				model.setFromDate(rs.getString("FROMDATE"));
				model.setToDate(rs.getString("TODATE"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARDING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));				
				
				stmt1 = getConnection().prepareStatement(getOfferItems);
				stmt1.setInt(1, offerId);
				ItemModel temp = null;
				
				rs1 = stmt1.executeQuery();
				
				while(rs1.next()){
					temp = new ItemModel();
					temp.setItemId(rs1.getInt("ITEMID"));
					temp.setItemNumber(rs1.getString("ITEMNUMBER"));
					temp.setItemPrice(rs1.getString("PRICE"));
					model.addOfferItem(temp, rs1.getInt("QTY"));
				}
				
				models.add(model);
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
		
		return (OfferModel[])models.toArray(new OfferModel[0]);
	}

	public OfferModel[] getOfferList() {
		ArrayList<OfferModel> models = new ArrayList<OfferModel>(0);
		OfferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		int offerId = 0;
		
		// query to get details of the offer
		String getOfferDtls = "SELECT A.OFFERID, A.NAME, A.OFFERPRICE, A.FROMDATE, A.TODATE, A.PACKING, A.FORWARDING, A.INSTALLATION FROM OFFER A WHERE A.STATUS = 'A'";
		
		// query to get the offer item details
		String getOfferItems = "SELECT A.ITEMNUMBER, B.ITEMID, B.QTY, A.PRICE FROM ITEM A, OFFER_ITEM B WHERE A.ITEMID = B.ITEMID AND B.OFFERID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getOfferDtls);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OfferModel();
				offerId = rs.getInt("OFFERID");
				model.setOfferid(rs.getInt("OFFERID"));
				model.setOfferName(rs.getString("NAME"));
				model.setOfferPrice(rs.getString("OFFERPRICE"));
				model.setFromDate(rs.getString("FROMDATE"));
				model.setToDate(rs.getString("TODATE"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARDING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));	
				
				stmt1 = getConnection().prepareStatement(getOfferItems);
				stmt1.setInt(1, offerId);
				ItemModel temp = null;
				
				rs1 = stmt1.executeQuery();
				
				while(rs1.next()){
					temp = new ItemModel();
					temp.setItemId(rs1.getInt("ITEMID"));
					temp.setItemNumber(rs1.getString("ITEMNUMBER"));
					temp.setItemPrice(rs1.getString("PRICE"));
					model.addOfferItem(temp, rs1.getInt("QTY"));
				}
				
				models.add(model);
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
		
		return (OfferModel[])models.toArray(new OfferModel[0]);
	}

	public OfferModel[] getCurrentOfferList() {
		ArrayList<OfferModel> models = new ArrayList<OfferModel>(0);
		OfferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		int offerId = 0;
		
		// query to get details of the offer
		String getOfferDtls = "SELECT A.OFFERID, A.NAME, A.OFFERPRICE, A.FROMDATE, A.TODATE, A.PACKING, A.FORWARDING, A.INSTALLATION FROM OFFER A WHERE A.FROMDATE <= CURRENT_DATE AND A.TODATE >= CURRENT_DATE AND A.STATUS = 'A'";
		
		// query to get the offer item details
		String getOfferItems = "SELECT A.ITEMNUMBER, B.ITEMID, B.QTY, A.PRICE FROM ITEM A, OFFER_ITEM B WHERE A.ITEMID = B.ITEMID AND B.OFFERID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getOfferDtls);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OfferModel();
				offerId = rs.getInt("OFFERID");
				model.setOfferid(rs.getInt("OFFERID"));
				model.setOfferName(rs.getString("NAME"));
				model.setOfferPrice(rs.getString("OFFERPRICE"));
				model.setFromDate(rs.getString("FROMDATE"));
				model.setToDate(rs.getString("TODATE"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARDING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));	
				
				stmt1 = getConnection().prepareStatement(getOfferItems);
				stmt1.setInt(1, offerId);
				ItemModel temp = null;
				
				rs1 = stmt1.executeQuery();
				
				while(rs1.next()){
					temp = new ItemModel();
					temp.setItemId(rs1.getInt("ITEMID"));
					temp.setItemNumber(rs1.getString("ITEMNUMBER"));
					temp.setItemPrice(rs1.getString("PRICE"));
					model.addOfferItem(temp, rs1.getInt("QTY"));
				}
				
				models.add(model);
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
		
		return (OfferModel[])models.toArray(new OfferModel[0]);
	}

}
