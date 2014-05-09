package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.en.model.ActionModel;
import com.en.model.EnquiryModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OfferItemModel;
import com.en.util.ActionType;
import com.en.util.Utils;

public class EnquiryBroker extends BaseBroker {

	public EnquiryBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new enquiry
	 * 
	 * @param model
	 * @return EnquiryModel with enquiry details
	 */
	public EnquiryModel addEnquiry(EnquiryModel model){
		
		// local variables
		long enqNo = 1;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the max enquiry no
		String getMaxEnqNo = "SELECT MAX(ENQNO) FROM ENQUIRY_MASTER";
		
		// query to insert the enquiry details
		String insertEnqDtls = "INSERT INTO ENQUIRY_MASTER (ENQNO, CUSTOMERID, PRIORITY, REFERENCE, ENQDATE, CURRENT_POINT, UPDATE_USER, CREATE_USER, REMARK, STATUS)" +
				" VALUES (?,?,?,?,CURRENT_DATE,?,?,?,?,'Open')";
		
		// query to insert the enquired item
		String insertEnqItems = "INSERT INTO ENQUIRY_ITEM (ITEMID, QTY, ENQNO, OFFERID) VALUES (?,?,?,?)";
		
		// query to add appointment action
		String insertAppointment = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, SALESMAN, APPTIME, APPDATE, ACTIONBY, ACTIONDT) " +
				"VALUES (1,?,1,?,?,?,?,CURRENT_DATE)";
		
		String updateLastAction = "UPDATE ENQUIRY_MASTER SET LAST_ACTION = ? WHERE ENQNO = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getMaxEnqNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1)>0){
				enqNo = rs.getLong(1)+1;
			}
			
			stmt = getConnection().prepareStatement(insertEnqDtls);
			stmt.setLong(1, enqNo);
			stmt.setInt(2, model.getCustomer().getCustomerId());
			stmt.setString(3, model.getPriority());
			stmt.setString(4, model.getReference());
			stmt.setInt(5, model.getAccessPoint().getAccessId());
			stmt.setString(6, model.getCreatedBy().getUserId());
			stmt.setString(7, model.getUpdatedBy().getUserId());
			stmt.setString(8, model.getRemarks());
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(insertEnqItems);
			
			OfferItemModel[] items = model.getItem();
			
			for(int i=0; i<items.length; i++){
				stmt.setInt(1, items[i].getItem().getItemId());
				stmt.setInt(2, items[i].getQty());
				stmt.setLong(3, enqNo);
				stmt.setString(4, items[i].getOfferNo());
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			
			if(model.getActions().length > 0){
				
				stmt = getConnection().prepareStatement(insertAppointment);
				stmt.setLong(1, enqNo);
				stmt.setString(2, model.getActions()[0].getSalesman().getUserId());
				stmt.setString(3, model.getActions()[0].getAppTm());
				stmt.setString(4, Utils.convertToSQLDate(model.getActions()[0].getAppDt()));
				stmt.setString(5, model.getActions()[0].getAddedBy().getUserId());
				
				stmt.execute();				
				
				stmt = getConnection().prepareStatement(updateLastAction);
				stmt.setString(1, ActionType.APPOINTMENT);
				stmt.setLong(2, enqNo);
				
				stmt.execute();
			}
			
			getConnection().commit();
			
			model = getEnquiryDetails(enqNo);
			
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return model;
	}
	
	public EnquiryModel getEnquiryDetails(long enqNo){
		EnquiryModel model = new EnquiryModel();
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		boolean isPresent = false;
		
		// query to get the enquiry detials
		String getEnquiryDtls = "SELECT A.ENQNO, A.CUSTOMERID, B.CUSTOMERNAME, B.AREA AS CUSTOMERAREA, B.CITY AS CUSTOMERCITY," +
				" A.PRIORITY, A.REFERENCE, DATE_FORMAT(A.ENQDATE, '%d-%m-%Y') AS ENDATE, " +
				"C.NAME, C.CITY AS ACCESSCITY, C.ACCESSID, A.UPDATE_USER, A.CREATE_USER, A.REMARK, A.STATUS, A.LAST_ACTION FROM ENQUIRY_MASTER A, CUSTOMER B, ACCESS_POINT C " +
				"WHERE A.CUSTOMERID = B.CUSTOMERID " +
				"AND A.CURRENT_POINT = C.ACCESSID " +
				"AND A.ENQNO = ?";
		
		// query to get the list of items enquiried
		String getEnquiryItems = "SELECT A.ITEMID, B.ITEMNUMBER, B.ITEMNAME, A.QTY, A.OFFERID FROM ENQUIRY_ITEM A, ITEM B" +
				" WHERE A.ITEMID = B.ITEMID" +
				" AND A.ENQNO = ?";
		
		// query to get the list of actions
		String getEnquiryActions = "SELECT ACTIONNO, ENQNO, ACTIONTYPE, SALESMAN, APPTIME, APPDATE, " +
				"PREV_AC_POINT, CURR_AC_POINT, QUOTATIONNO, ACTIONBY, ACTIONDT, PREV_AC_POINT, CURR_AC_POINT, REMARK, INTIME, OUTTIME FROM ENQUIRY_ACTION " +
				"WHERE ENQNO = ? ORDER BY ACTIONNO DESC";
		
		// query to get the access point details
		String getACPointDtls = "SELECT NAME, CITY FROM ACCESS_POINT WHERE ACCESSID = ?";
				
		try{
			
			stmt = getConnection().prepareStatement(getEnquiryDtls);
			stmt.setLong(1, enqNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setEnquiryNo(rs.getLong("ENQNO"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTOMERID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("CUSTOMERAREA"));
				model.getCustomer().setCity(rs.getString("CUSTOMERCITY"));
				model.setPriority(rs.getString("PRIORITY"));
				model.setReference(rs.getString("REFERENCE"));
				model.setEnquiryDt(rs.getString("ENDATE"));
				model.getAccessPoint().setAccessName(rs.getString("NAME"));
				model.getAccessPoint().setCity(rs.getString("ACCESSCITY"));
				model.getAccessPoint().setAccessId(rs.getInt("ACCESSID"));
				model.getCreatedBy().setUserId(rs.getString("CREATE_USER"));
				model.getUpdatedBy().setUserId(rs.getString("UPDATE_USER"));
				model.setRemarks(rs.getString("REMARK"));
				model.setStatus(rs.getString("STATUS"));
				model.setLastAction(rs.getString("LAST_ACTION"));
				isPresent = true;
			}
			
			if(isPresent){
				stmt = getConnection().prepareStatement(getEnquiryItems);
				stmt.setLong(1, enqNo);
				
				rs = stmt.executeQuery();
				ItemModel temp = null;
				
				while(rs.next()){
					temp = new ItemModel();
					temp.setItemId(rs.getInt("ITEMID"));
					temp.setItemNumber(rs.getString("ITEMNUMBER"));
					temp.setItemName(rs.getString("ITEMNAME"));
					model.addItem(temp, rs.getInt("QTY"),rs.getString("OFFERID"));
				}
				
				stmt = getConnection().prepareStatement(getEnquiryActions);
				stmt.setLong(1, enqNo);
				
				rs = stmt.executeQuery();
				ActionModel act = null;
				
				while(rs.next()){
					act = new ActionModel();
					if(rs.getString("ACTIONTYPE").equals(ActionType.APPOINTMENT) || rs.getString("ACTIONTYPE").equals(ActionType.APPOINTMENT_DELAY)){
						act.getSalesman().setUserId(rs.getString("SALESMAN"));
						act.setAppDt(rs.getString("APPDATE"));
						act.setAppTm(rs.getString("APPTIME"));
					} else if(rs.getString("ACTIONTYPE").equals(ActionType.FORWARD)){
						stmt1 = getConnection().prepareStatement(getACPointDtls);
						stmt1.setString(1, rs.getString("PREV_AC_POINT"));
						
						rs1 = stmt1.executeQuery();
						
						while(rs1.next()){
							act.getPrevACPoint().setAccessId(rs.getInt("PREV_AC_POINT"));
							act.getPrevACPoint().setAccessName(rs1.getString("NAME"));
							act.getPrevACPoint().setCity(rs1.getString("CITY"));
						}
						stmt1 = getConnection().prepareStatement(getACPointDtls);
						stmt1.setString(1, rs.getString("CURR_AC_POINT"));
						
						rs1 = stmt1.executeQuery();
						
						while(rs1.next()){
							act.getNewACPoint().setAccessId(rs.getInt("CURR_AC_POINT"));
							act.getNewACPoint().setAccessName(rs1.getString("NAME"));
							act.getNewACPoint().setCity(rs1.getString("CITY"));
						}
					} else if(rs.getString("ACTIONTYPE").equals(ActionType.APPOINTMENT_REPLY)){
						act.setAppDt(rs.getString("APPDATE"));
						act.setInTm(rs.getString("INTIME"));
						act.setOutTm(rs.getString("OUTTIME"));
						act.getSalesman().setUserId(rs.getString("SALESMAN"));
					} else if(rs.getString("ACTIONTYPE").equals(ActionType.ENQUIRY_ALERT)){
						act.setAppDt(rs.getString("APPDATE"));
					}
					act.getAddedBy().setUserId(rs.getString("ACTIONBY"));
					act.setActionDt(rs.getString("ACTIONDT"));
					act.setActionNo(rs.getInt("ACTIONNO"));
					act.setActionType(rs.getString("ACTIONTYPE"));
					act.setRemarks(rs.getString("REMARK"));
					model.addAction(act);
				}
			} else {
				model = null;
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
	private String getActionQuery(int actionType){
		String query = "";
		switch(actionType){
				case 0:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, ACTIONBY, ACTIONDT, REMARK) VALUES (?,?,0,?,CURRENT_DATE,?)";
					break;
				case 1:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, SALESMAN, APPTIME, APPDATE, ACTIONBY, ACTIONDT, REMARK) " +
							"VALUES (?,?,1,?,?,?,?,CURRENT_DATE,?)";
					break;
				case 2:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, PREV_AC_POINT, CURR_AC_POINT, ACTIONBY, ACTIONDT, REMARK) " +
							"VALUES (?,?,2,?,?,?,CURRENT_DATE,?)";
					break;
				case 3:
					query = "";
					break;
				case 4:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, SALESMAN, ACTIONTYPE, INTIME, OUTTIME, APPDATE, ACTIONBY, ACTIONDT, REMARK) " +
							"VALUES (?,?,?,4,?,?,?,?,CURRENT_DATE,?)";
					break;
				case 5:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, APPDATE, ACTIONBY, ACTIONDT, REMARK) " +
							"VALUES (?,?,5,?,?,CURRENT_DATE,?)";
					break;
				case 6:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, SALESMAN, APPTIME, APPDATE, ACTIONBY, ACTIONDT, REMARK) " +
							"VALUES (?,?,6,?,?,?,?,CURRENT_DATE,?)";
					break;
				case 7:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, ACTIONBY, ACTIONDT, REMARK) VALUES (?,?,7,?,CURRENT_DATE,?)";
					break;
				case 8:
					query = "INSERT INTO ENQUIRY_ACTION (ACTIONNO, ENQNO, ACTIONTYPE, ACTIONBY, ACTIONDT, REMARK) VALUES (?,?,8,?,CURRENT_DATE,?)";
					break;
		}
		return query;
	}

	public MessageModel addAction(ActionModel action, long enqNo) {
		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		int actionNo = 1;
		
		String getMaxActionNo = "SELECT MAX(ACTIONNO) FROM ENQUIRY_ACTION WHERE ENQNO = ?";
		
		String updateLastAction = "UPDATE ENQUIRY_MASTER SET LAST_ACTION = ? WHERE ENQNO = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getMaxActionNo);
			stmt.setLong(1, enqNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1)>0){
				actionNo = rs.getInt(1)+1;
			}
			
			String query = getActionQuery(Integer.parseInt(action.getActionType()));
			
			stmt = getConnection().prepareStatement(query);
			
			if(action.getActionType().equals(ActionType.FORWARD)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setInt(3, action.getPrevACPoint().getAccessId());
				stmt.setInt(4, action.getNewACPoint().getAccessId());
				stmt.setString(5, action.getAddedBy().getUserId());
				stmt.setString(6, action.getRemarks());
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement("UPDATE ENQUIRY_MASTER SET CURRENT_POINT = ? WHERE ENQNO = ?");
				stmt.setInt(1, action.getNewACPoint().getAccessId());
				stmt.setLong(2, enqNo);
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.APPOINTMENT)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, action.getSalesman().getUserId());
				stmt.setString(4, action.getAppTm());
				stmt.setString(5, Utils.convertToSQLDate(action.getAppDt()));
				stmt.setString(6, action.getAddedBy().getUserId());
				stmt.setString(7, action.getRemarks());
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.APPOINTMENT_REPLY)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, action.getAddedBy().getUserId());
				stmt.setString(4, action.getInTm());
				stmt.setString(5, action.getOutTm());
				stmt.setString(6, Utils.convertToSQLDate(action.getAppDt()));
				stmt.setString(7, action.getAddedBy().getUserId());
				stmt.setString(8, action.getRemarks());
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.CLOSED)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, action.getAddedBy().getUserId());
				stmt.setString(4, action.getRemarks());
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement("UPDATE ENQUIRY_MASTER SET STATUS = 'Closed' WHERE ENQNO = ?");
				stmt.setLong(1, enqNo);
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.CLOSED_APPROVAL)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, action.getAddedBy().getUserId());
				stmt.setString(4, action.getRemarks());
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement("UPDATE ENQUIRY_MASTER SET STATUS = 'Closed_Approval' WHERE ENQNO = ?");
				stmt.setLong(1, enqNo);
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.ENQUIRY_ALERT)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, Utils.convertToSQLDate(action.getAppDt()));
				stmt.setString(4, action.getAddedBy().getUserId());
				stmt.setString(5, action.getRemarks());
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.APPOINTMENT_DELAY)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, action.getSalesman().getUserId());
				stmt.setString(4, action.getAppTm());
				stmt.setString(5, Utils.convertToSQLDate(action.getAppDt()));
				stmt.setString(6, action.getAddedBy().getUserId());
				stmt.setString(7, action.getRemarks());
				
				stmt.execute();
			} else if(action.getActionType().equals(ActionType.APPOINTMENT_CANCEL)){
				stmt.setInt(1, actionNo);
				stmt.setLong(2, enqNo);
				stmt.setString(3, action.getAddedBy().getUserId());
				stmt.setString(4, action.getRemarks());
				
				stmt.execute();
			}
			
			stmt = getConnection().prepareStatement(updateLastAction);
			stmt.setString(1, action.getActionType());
			stmt.setLong(2, enqNo);
			
			stmt.execute();
			
			getConnection().commit();
			
		} catch (Exception ex){
			ex.printStackTrace();
			Message msgs = new Message(ERROR, "Error occured while adding action.");
			msg.addNewMessage(msgs);
			try{
				getConnection().rollback();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return msg;
	}

	public String getLastAction(long enqNo) {
		String lastAction = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the enquiry detials
		String getLastAction = "SELECT LAST_ACTION FROM ENQUIRY_MASTER WHERE ENQNO = ?";
				
		try{
			
			stmt = getConnection().prepareStatement(getLastAction);
			stmt.setLong(1, enqNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				lastAction = rs.getString("LAST_ACTION");
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lastAction;
	}

}
