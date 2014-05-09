package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TaxModel;

public class TaxBroker extends BaseBroker {

	public TaxBroker(Connection conn) {
		super(conn);
	}

	public MessageModel changeTaxDetails(TaxModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to delete the previous tax details
		String deletePreviousDtls = "DELETE FROM TAX WHERE ACCESSID = ?";
		
		// query to insert the new tax details
		String insertNewDtls = "INSERT INTO TAX (ACCESSID, VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(deletePreviousDtls);
			stmt.setInt(1, model.getAccessPoint().getAccessId());
			
			stmt.executeUpdate();
			
			stmt = getConnection().prepareStatement(insertNewDtls);
				
			stmt.setLong(1, model.getAccessPoint().getAccessId());
			stmt.setDouble(2, model.getVat1());
			stmt.setDouble(3, model.getVat2());
			stmt.setDouble(4, model.getVat3());
			stmt.setDouble(5, model.getCst1());
			stmt.setDouble(6, model.getCst2());
			stmt.setDouble(7, model.getCst3());
			stmt.setDouble(8, model.getCst1c());
			stmt.setDouble(9, model.getCst2c());
			stmt.setDouble(10, model.getCst3c());
			stmt.setDouble(11, model.getCess());
				
			if(stmt.executeUpdate() > 0){
				msg = new Message(SUCCESS, "Tax Details modified successfully.");
			} else {
				msg = new Message(ALERT, "Error occured while modifying an tax details.");
			}
			messages.addNewMessage(msg);
			
			getConnection().commit();
			
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
	
	public TaxModel getTaxDetails(int accessId){
		TaxModel model = new TaxModel();
		
		model.getAccessPoint().setAccessId(accessId);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the tax details
		String getTaxDetails = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3,CESS FROM TAX WHERE ACCESSID = ?";
		
		try{
			
			
			stmt = getConnection().prepareStatement(getTaxDetails);
			stmt.setLong(1, model.getAccessPoint().getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setVat1(rs.getDouble("VAT1"));
				model.setVat2(rs.getDouble("VAT2"));
				model.setVat3(rs.getDouble("VAT3"));
				model.setCst1(rs.getDouble("CST1"));
				model.setCst2(rs.getDouble("CST2"));
				model.setCst3(rs.getDouble("CST3"));
				model.setCst1c(rs.getDouble("CSTC1"));
				model.setCst2c(rs.getDouble("CSTC2"));
				model.setCst3c(rs.getDouble("CSTC3"));
				model.setCst3c(rs.getDouble("CESS"));
			}
			
			getConnection().commit();
			
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
		
		return model;
	}

	public HashMap<Integer, Integer> getItemTaxDetails(int itemId) {
		HashMap<Integer, Integer> taxDtls = new HashMap<Integer, Integer>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the tax details
		String getTaxDetails = "SELECT ACCESSID, SLAB FROM ITEM_TAX WHERE ITEMID = ?";
		
		try{
			
			
			stmt = getConnection().prepareStatement(getTaxDetails);
			stmt.setInt(1, itemId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				taxDtls.put(rs.getInt(1), rs.getInt(2));
			}
			
			getConnection().commit();
			
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
		
		return taxDtls;
	}

	public MessageModel updateItemTaxDetails(int itemId,
			HashMap<Integer, String> taxDtls) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to delete the previous tax details
		String deletePreviousDtls = "DELETE FROM ITEM_TAX WHERE ITEMID = ?";
		
		// query to insert the new tax details
		String insertNewDtls = "INSERT INTO ITEM_TAX (ITEMID, ACCESSID, SLAB) VALUES (?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(deletePreviousDtls);
			stmt.setInt(1, itemId);
			
			stmt.executeUpdate();
			
			stmt = getConnection().prepareStatement(insertNewDtls);
				
			Set<Integer> keys = taxDtls.keySet();
			Iterator<Integer> keyItr = keys.iterator();
			int key = 0;
			
			while(keyItr.hasNext()){
				key = keyItr.next();
				stmt.setInt(1, itemId);
				stmt.setInt(2, key);
				stmt.setString(3, taxDtls.get(key));
				stmt.addBatch();
			}
				
			stmt.executeBatch();
			stmt.clearBatch();
			
			getConnection().commit();
			
			msg = new Message(SUCCESS, "Item Tax Details modified successfully.");
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
	
}
