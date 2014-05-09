package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TransferItemModel;
import com.en.model.TransferModel;
import com.en.model.UserModel;
import com.en.util.Utils;

public class TransferBroker extends BaseBroker {

	public TransferBroker(Connection conn) {
		super(conn);
	}

	public MessageModel addTransfer(TransferModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		long transferId = 1;
		String billPrefix = "";

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(TRANSFERID) FROM TRANSFER WHERE FROMID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";

		// query to insert the bill details
		String insertMasterDetails = "INSERT INTO TRANSFER (TRANSFERID, TRANSFERDATE, FROMID, TOID, TRANSPORT, REMARK) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?)";
		
		//query to insert the bill items
		String insertItemsDetails = "INSERT INTO TRANSFER_ITEM (TRANSFERID, BRANCHID, ITEMID, QTY, DESP, PRICE) " +
				"VALUES (?,?,?,?,?,?)";
		
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
				stmt.setString(4, model.getTransport());
				stmt.setString(5, model.getRemark());
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().size(); i++){
					
					stmt1.setLong(1, transferId);
					stmt1.setInt(2, model.getFromBranch().getAccessId());
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setString(5, model.getItems().get(i).getDesc());
					stmt1.setDouble(6, model.getItems().get(i).getRate());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Tranfer Memo Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + "ST" +transferId);
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting sales details!!");
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

	public TransferModel getTranferDetails(String transferId) {
		
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
		String getBillDetails = "SELECT A.TRANSFERID, A.TRANSFERDATE, A.FROMID, A.TOID, A.TRANSPORT, A.REMARK, A.TOAPPED, A.APPDATE, A.APPER, A.LRNO, A.LRDT, A.ISLOCK FROM TRANSFER A WHERE A.TRANSFERID = ? AND A.FROMID = ? ";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.PRICE, B.DESP " +
				"FROM ITEM A, TRANSFER_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.TRANSFERID = ? " +
				"AND B.BRANCHID = ?";

		// query to get the branch details
		String getBranchDetailsById = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL FROM ACCESS_POINT WHERE ACCESSID = ? ";

		// query to get all the existing item group
		String getItemRate = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, B.RATE FROM ITEM A LEFT JOIN PURCHASE_ITEM B ON A.ITEMID = B.ITEMID AND CONCAT(B.PURCHASEID, B.BRANCHID) =  (SELECT CONCAT(C.PURCHASEID, C.BRANCHID) FROM PURCHASE C ORDER BY C.RECDDT LIMIT 1) WHERE ITEMID = ? AND STATUS = 'A' ORDER BY ITEMNUMBER";
		
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
				model.setTransferid(rs.getInt("TRANSFERID"));
				model.setTransferdate(rs.getString("TRANSFERDATE"));
				model.getToBranch().setAccessId(rs.getInt("TOID"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setRemark(rs.getString("REMARK"));
				model.setLrno(rs.getString("LRNO"));
				model.setLrdt(rs.getString("LRDT"));
				model.setLock(rs.getString("ISLOCK").equals("Y"));
				if(rs.getString("TOAPPED").equals("Y")){
					model.setToApproved();
					model.setToAppDate(rs.getString("APPDATE"));
					model.setToApprover((new UserBroker(this.getConnection())).getUserDtls(rs.getString("APPER")));
				}
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
			
			while(rs.next()){
				itm = new ItemModel();
				itm.setItemId(rs.getInt("ITEMID"));
				itm.setItemNumber(rs.getString("ITEMNUMBER"));
				itm.setItemName(rs.getString("ITEMNAME"));
				model.addItem(itm, rs.getString("DESP"), rs.getDouble("QTY"), rs.getDouble("PRICE"));
				
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

	public MessageModel editTransfer(TransferModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		String accessId = "";

		// query to check the existence of the bill no
//		String maxBillNo = "SELECT MAX(SALESID) FROM SALES WHERE BRANCHID = ?";

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";

		// query to insert the bill details
		String updateBillDetails = "UPDATE TRANSFER SET TOID = ? , TRANSPORT = ?,  REMARK = ? " +
				"WHERE TRANSFERID = ? AND FROMID = ?";
		
		//query to insert the bill items
		String deleteBillItems = "DELETE FROM TRANSFER_ITEM WHERE TRANSFERID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String insertItemsDetails = "INSERT INTO TRANSFER_ITEM (TRANSFERID, BRANCHID, ITEMID, QTY, DESP, PRICE) " +
				"VALUES (?,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, model.getFromBranch().getBillPrefix());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}

				
				
				stmt = getConnection().prepareStatement(deleteBillItems);
				stmt.setInt(1, model.getTransferid());
				stmt.setString(2, accessId);
				
				stmt.execute();
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				stmt1 = getConnection().prepareStatement(insertItemsDetails);
				
				stmt.setInt(1, model.getToBranch().getAccessId());
				stmt.setString(2, model.getTransport());
				stmt.setString(3, model.getRemark());
				stmt.setInt(4, model.getTransferid());
				stmt.setString(5, accessId);
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().size(); i++){
					
					stmt1.setLong(1, model.getTransferid());
					stmt1.setString(2, accessId);
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setString(5, model.getItems().get(i).getDesc());
					stmt1.setDouble(6, ((model.getItems())).get(i).getRate());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Transfer Details Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getFromBranch().getBillPrefix() + "ST" + model.getTransferid());
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing sales details!!");
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

	public TransferModel[] getAwaitingApprovalTranfer(int accessId) {
		
		ArrayList<TransferModel> todayBills = new ArrayList<TransferModel>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		TransferModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.TRANSFERID, A.TRANSFERDATE, B.NAME, B.CITY, B.BILL_PREFIX, A.FROMID, A.TOID FROM TRANSFER A, ACCESS_POINT B WHERE A.FROMID = B.ACCESSID AND A.TOAPPED = 'N' AND A.TOID = ? ";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setInt(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new TransferModel();
				model.setTransferid(rs.getInt("TRANSFERID"));
				model.setTransferdate(rs.getString("TRANSFERDATE"));
				model.getToBranch().setAccessId(rs.getInt("TOID"));
				model.getFromBranch().setAccessName(rs.getString("NAME"));
				model.getFromBranch().setCity(rs.getString("CITY"));
				model.getFromBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getFromBranch().setAccessId(rs.getInt("FROMID"));
				todayBills.add(model);
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
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (TransferModel[]) todayBills.toArray(new TransferModel[0]);
	}

	public MessageModel approveTranfer(String transferId, UserModel approver, String recdDt) {
		
		MessageModel msgs = new MessageModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String accessId = "";

		String billPrefix = transferId.substring(0,3);
		String transId = transferId.substring(5);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";

		
		// query to get the bill no details
		String updateBillDetails = "UPDATE TRANSFER SET TOAPPED = 'Y', APPDATE = ?, APPER = ?, APPONDATE = CURRENT_DATE WHERE TRANSFERID = ? AND FROMID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBranchDetails);
			stmt.setString(1, billPrefix);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				accessId = rs.getString("ACCESSID");
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(updateBillDetails);
			stmt.setString(1, Utils.convertToSQLDate(recdDt));
			stmt.setString(2, approver.getUserId());
			stmt.setString(3, transId);
			stmt.setString(4, accessId);
			
			stmt.executeUpdate();
			
			getConnection().commit();
			
			msgs.addNewMessage(new Message(SUCCESS, "Approved Successfully!!! You can take the print out of the office copy."));
			
		} catch (Exception e) {
			e.printStackTrace();
			msgs.addNewMessage(new Message(ERROR, "Error occured while approving the transfer."));
			try{
				getConnection().rollback(); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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

	public TransferItemModel[] getExportTransferItems(String fromDate,
			String toDate) {
		
		ArrayList<TransferItemModel> todayBills = new ArrayList<TransferItemModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		TransferItemModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.TRANSFERID, A.QTY, A.PRICE, B.ITEMID, B.ITEMNUMBER, B.ITEMNAME, D.BILL_PREFIX, A.BRANCHID " +
				"FROM TRANSFER_ITEM A, ITEM B, TRANSFER C, ACCESS_POINT D WHERE B.ITEMID = A.ITEMID AND C.FROMID = D.ACCESSID AND A.TRANSFERID = C.TRANSFERID " +
				"AND A.BRANCHID = C.FROMID  " +
				"AND C.TRANSFERDATE >= ? AND C.TRANSFERDATE <= ? ORDER BY A.BRANCHID, A.TRANSFERID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new TransferItemModel();
				model.getItem().setItemId(rs.getInt("ITEMID"));
				model.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
				model.getItem().setItemName(rs.getString("ITEMNAME"));
				model.setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setBranchId(rs.getInt("BRANCHID"));
				model.setTransferId(rs.getString("TRANSFERID"));
				model.setQty(rs.getDouble("QTY"));
				model.setRate(rs.getDouble("PRICE"));
				todayBills.add(model);
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
		
		return (TransferItemModel[]) todayBills.toArray(new TransferItemModel[0]);
	}

	public TransferModel[] getExportTransferMaster(String fromDate,
			String toDate) {
		
		ArrayList<TransferModel> todayBills = new ArrayList<TransferModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		TransferModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.TRANSFERID, A.TRANSFERDATE, A.FROMID, A.TOID, A.REMARK, SUM(B.QTY*B.PRICE) AS AMT FROM TRANSFER A, TRANSFER_ITEM B WHERE " +
				" TRANSFERDATE >= ? AND TRANSFERDATE <= ? AND A.TRANSFERID = B.TRANSFERID AND A.FROMID = B.BRANCHID GROUP BY A.FROMID, A.TRANSFERID ORDER BY A.FROMID, A.TRANSFERID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new TransferModel();
				model.setTransferid(rs.getInt("TRANSFERID"));
				model.setTransferdate(rs.getString("TRANSFERDATE"));
				model.setFromBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("FROMID")));
				model.setToBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("TOID")));
				model.setRemark(rs.getString("REMARK"));
				model.setTotalAmt(rs.getDouble("AMT"));
				todayBills.add(model);
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
		
		return (TransferModel[]) todayBills.toArray(new TransferModel[0]);
	}	

	public TransferModel[] getTodayTransferDetails(String fromDate, String toDate, String accessId) {
		ArrayList<TransferModel> todayBills = new ArrayList<TransferModel>(0);
		TransferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.TRANSFERID, A.TRANSFERDATE, A.FROMID, A.TOID, A.REMARK, SUM(B.QTY*B.PRICE) AS AMT FROM TRANSFER A, TRANSFER_ITEM B WHERE " +
				" A.TRANSFERDATE >= ? AND A.TRANSFERDATE <= ? AND A.FROMID = ? AND A.TRANSFERID = B.TRANSFERID AND A.FROMID = B.BRANCHID GROUP BY A.FROMID, A.TRANSFERID ORDER BY A.FROMID, A.TRANSFERID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new TransferModel();
				model.setTransferid(rs.getInt("TRANSFERID"));
				model.setTransferdate(rs.getString("TRANSFERDATE"));
				model.setFromBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("FROMID")));
				model.setToBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("TOID")));
				model.setRemark(rs.getString("REMARK"));
				model.setTotalAmt(rs.getDouble("AMT"));
				todayBills.add(model);
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
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return (TransferModel[]) todayBills.toArray(new TransferModel[0]);
	}

	public TransferModel[] getExportTransferApproved(String fromDate,
			String toDate) {
		
		ArrayList<TransferModel> todayBills = new ArrayList<TransferModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		TransferModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT TRANSFERID, FROMID, APPDATE, APPONDATE FROM TRANSFER WHERE " +
				" APPONDATE >= ? AND APPONDATE <= ? AND TOAPPED = 'Y' GROUP BY FROMID, TRANSFERID ORDER BY FROMID, TRANSFERID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new TransferModel();
				model.setTransferid(rs.getInt("TRANSFERID"));
				model.setFromBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("FROMID")));
				model.setToAppDate(rs.getString("APPDATE"));
				model.setAppOnDate(rs.getString("APPONDATE"));
				todayBills.add(model);
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
		
		return (TransferModel[]) todayBills.toArray(new TransferModel[0]);
	}

	public MessageModel editTransferLRDetails(String billNo, String lrNo,
			String lrDt, String transport, int accessId) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to insert the bill details
		String updateBillDetails = "UPDATE TRANSFER SET LRNO = ? , LRDT = ?, TRANSPORT = ? " +
				"WHERE TRANSFERID = ? AND FROMID = ?";
		
		try {
				
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				stmt.setString(1, lrNo);
				stmt.setString(2, lrDt);
				stmt.setString(3, transport);
				stmt.setString(4, billNo.substring(5));
				stmt.setInt(5, accessId);
				
				stmt.execute();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill LR Edited successfully!!");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing sales details!!");
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

}
