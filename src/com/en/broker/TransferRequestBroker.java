package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TransferModel;
import com.en.util.RequestAlertDateComparator;

public class TransferRequestBroker extends BaseBroker {

	public TransferRequestBroker(Connection conn) {
		super(conn);
	}

	public MessageModel addTransferRequest(TransferModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		long transferId = 1;
		String billPrefix = "";

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(REQUESTID) FROM TRANSFER_REQUEST WHERE FROMID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";

		// query to insert the bill details
		String insertMasterDetails = "INSERT INTO TRANSFER_REQUEST (REQUESTID, REQUESTDATE, FROMID, TOID, REMARK) " +
				"VALUES (?,CURRENT_DATE,?,?,?)";
		
		//query to insert the bill items
		String insertItemsDetails = "INSERT INTO TRANSFER_REQUEST_ITEM (REQUESTID, BRANCHID, ITEMID, QTY, DESP) " +
				"VALUES (?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxBillNo);
				stmt.setInt(1, model.getFromBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					transferId = rs.getInt(1) + 1;
				}
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getBillPrefix);
				stmt.setInt(1, model.getFromBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					billPrefix = rs.getString(1);
				}

				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(insertMasterDetails);
				stmt1 = getConnection().prepareStatement(insertItemsDetails);
				
				stmt.setLong(1, transferId);
				stmt.setInt(2, model.getFromBranch().getAccessId());
				stmt.setInt(3, model.getToBranch().getAccessId());
				stmt.setString(4, model.getRemark());
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().size(); i++){
					
					stmt1.setLong(1, transferId);
					stmt1.setInt(2, model.getFromBranch().getAccessId());
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setString(5, model.getItems().get(i).getDesc());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Tranfer Request Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + "TR" +transferId);
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting transfer request details!!");
			msgs.addNewMessage(msg);
			try{
				getConnection().rollback();
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

	public TransferModel getTranferRequestDetails(String transferId) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		TransferModel model = new TransferModel();
		
		String billPrefix = transferId.substring(0,3);
		String transId = transferId.substring(5);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.REQUESTID, A.REQUESTDATE, A.FROMID, A.TOID, A.REMARK FROM TRANSFER_REQUEST A WHERE A.REQUESTID = ? AND A.FROMID = ? ";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.DESP " +
				"FROM ITEM A, TRANSFER_REQUEST_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.REQUESTID = ? " +
				"AND B.BRANCHID = ?";
		
		// query to get the branch details
		String getBranchDetailsById = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL FROM ACCESS_POINT WHERE ACCESSID = ? ";

		// query to get all the existing item group
		String getItemRate = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, B.RATE FROM ITEM A LEFT JOIN PURCHASE_ITEM B ON A.ITEMID = B.ITEMID AND CONCAT(B.PURCHASEID, B.BRANCHID) =  (SELECT CONCAT(C.PURCHASEID, C.BRANCHID) FROM PURCHASE C WHERE CONCAT(C.PURCHASEID, C.BRANCHID) IN (SELECT CONCAT(Z.PURCHASEID, Z.BRANCHID) FROM PURCHASE_ITEM Z WHERE Z.ITEMID = A.ITEMID) ORDER BY C.RECDDT LIMIT 1) WHERE A.ITEMID = ? AND STATUS = 'A'";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBranchDetails);
			stmt.setString(1, billPrefix);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.getFromBranch().setAccessId(rs.getInt("ACCESSID"));
				model.getFromBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getFromBranch().setAccessName(rs.getString("NAME"));
				model.getFromBranch().setAddress(rs.getString("ADDRESS"));
				model.getFromBranch().setCity(rs.getString("CITY"));
				model.getFromBranch().setPincode(rs.getString("PINCODE"));
				model.getFromBranch().setVat(rs.getString("VAT"));
				model.getFromBranch().setCst(rs.getString("CST"));
				model.getFromBranch().setPhone1(rs.getString("PHONE1"));
				model.getFromBranch().setPhone2(rs.getString("PHONE2"));
				model.getFromBranch().setStdcode(rs.getString("STDCODE"));
				model.getFromBranch().setEmail(rs.getString("EMAIL"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, transId);
			stmt.setInt(2, model.getFromBranch().getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setTransferid(rs.getInt("REQUESTID"));
				model.setTransferdate(rs.getString("REQUESTDATE"));
				model.getToBranch().setAccessId(rs.getInt("TOID"));
				model.setRemark(rs.getString("REMARK"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBranchDetailsById);
			stmt.setInt(1, model.getToBranch().getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.getToBranch().setAccessId(rs.getInt("ACCESSID"));
				model.getToBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getToBranch().setAccessName(rs.getString("NAME"));
				model.getToBranch().setAddress(rs.getString("ADDRESS"));
				model.getToBranch().setCity(rs.getString("CITY"));
				model.getToBranch().setPincode(rs.getString("PINCODE"));
				model.getToBranch().setVat(rs.getString("VAT"));
				model.getToBranch().setCst(rs.getString("CST"));
				model.getToBranch().setPhone1(rs.getString("PHONE1"));
				model.getToBranch().setPhone2(rs.getString("PHONE2"));
				model.getToBranch().setStdcode(rs.getString("STDCODE"));
				model.getToBranch().setEmail(rs.getString("EMAIL"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getItemDetails);
			stmt.setInt(1, Integer.parseInt(transId));
			stmt.setInt(2, model.getFromBranch().getAccessId());
			
			stmt1 = getConnection().prepareStatement(getItemRate);
			
			rs = stmt.executeQuery();
			ItemModel itm = null;
			double rate = 0;
			
			while(rs.next()){
				itm = new ItemModel();
				itm.setItemId(rs.getInt("ITEMID"));
				itm.setItemNumber(rs.getString("ITEMNUMBER"));
				itm.setItemName(rs.getString("ITEMNAME"));
				
				stmt1.setInt(1, rs.getInt("ITEMID"));
				
				rs1 = stmt1.executeQuery();
				
				if(rs1.next()) { 
					rate = (rs1.getString("RATE") != null) ? rs1.getDouble("RATE") : 0;
				}

				model.addItem(itm, rs.getString("DESP"), rs.getDouble("QTY"), rate);
			}
			
			getConnection().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				getConnection().rollback(); 
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

	public TransferModel[] getFollowupRequestDetails(String accessId) {
		ArrayList<TransferModel> todayBills = new ArrayList<TransferModel>(0);
		TransferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.REQUESTID, A.REQUESTDATE, D.BILL_PREFIX, A.FROMID, D.NAME, D.CITY, " +
				"SUM(C.QTY) AS REQQTY, SUM(C.SENTQTY) AS SQTY, Z.ALERTDATE, Z.ALERTREMARK, A.CNT, Y.USERNAME AS FOLLOWUPUSER " +
				"FROM TRANSFER_REQUEST_ITEM C, ACCESS_POINT D, TRANSFER_REQUEST A " +
				"LEFT OUTER JOIN TRANSFER_REQUEST_ALERT Z ON A.FROMID = Z.BRANCHID AND A.REQUESTID = Z.REQUESTID AND A.CNT = Z.SNO  LEFT OUTER JOIN USER Y ON Z.USER = Y.USERID  " +
				"WHERE A.REQUESTID = C.REQUESTID " +
				"AND A.FROMID = D.ACCESSID " +
				"AND A.FROMID = C.BRANCHID " +
				"AND A.TOID = ? " +
				"AND A.STATUS = 0 " +
				"GROUP BY A.REQUESTID, A.FROMID " +
				"ORDER BY A.REQUESTID, A.FROMID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new TransferModel();
				model.setTransferid(rs.getInt("REQUESTID"));
				model.setTransferdate(rs.getString("REQUESTDATE"));
				model.getFromBranch().setAccessId(rs.getInt("FROMID"));
				model.getFromBranch().setAccessName(rs.getString("NAME"));
				model.getFromBranch().setCity(rs.getString("CITY"));
				model.setTotalQty(rs.getDouble("REQQTY"));
				model.setSentQty(rs.getDouble("SQTY"));
				model.getFromBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setFollowupCnt(rs.getInt("CNT"));
				model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") : rs.getString("REQUESTDATE")); 
				model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
				model.setFollowupUser((rs.getString("FOLLOWUPUSER") != null) ? rs.getString("FOLLOWUPUSER") :""); 
				todayBills.add(model);
			}
			
			Collections.sort(todayBills, new RequestAlertDateComparator());
			
		} catch (Exception e) {
			try{
				getConnection().rollback();
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
		
		return (TransferModel[]) todayBills.toArray(new TransferModel[0]);
	}

}
