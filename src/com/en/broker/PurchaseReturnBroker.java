package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.PurchaseItemModel;
import com.en.model.PurchaseModel;
import com.en.model.PurchaseReturnModel;

public class PurchaseReturnBroker extends BaseBroker {

	public PurchaseReturnBroker(Connection conn) {
		super(conn);
	}

	public synchronized MessageModel addPurchaseReturn(PurchaseReturnModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		long purchaseId = 1;
		String billPrefix = "";

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(RETURNID) FROM PURCHASE_RETURN WHERE BRANCHID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";

		// query to insert the bill details
		String insertBillDetails = "INSERT INTO PURCHASE_RETURN (RETURNID, RETURNDATE, BRANCHID, SUPPID, TAXTYPE, REMARK) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?)";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO PURCHASE_RETURN_ITEM (RETURNID, BRANCHID, ITEMID, QTY, RATE, TAX) " +
				"VALUES (?,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxBillNo);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					purchaseId = rs.getInt(1) + 1;
				}
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getBillPrefix);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					billPrefix = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(insertBillDetails);
				stmt1 = getConnection().prepareStatement(insertBillItems);
				
				stmt.setLong(1, purchaseId);
				stmt.setInt(2, model.getBranch().getAccessId());
				stmt.setInt(3, model.getSupplier().getCustomerId());
				stmt.setInt(4, model.getBillType());
				stmt.setString(5, model.getRemark());
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().size(); i++){
					
					stmt1.setLong(1, purchaseId);
					stmt1.setInt(2, model.getBranch().getAccessId());
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setDouble(5, ((model.getItems())).get(i).getRate());
					stmt1.setDouble(6, ((model.getItems())).get(i).getTax());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Purchase Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + "PR"+ purchaseId);
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting purchase details!!");
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

	public PurchaseReturnModel getPurchaseReturnDetails(String billNo) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		PurchaseReturnModel model = new PurchaseReturnModel();
		
		String billPrefix = billNo.substring(0,3);
		String billNumb = billNo.substring(5);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL, WITHTIN, NOTIN " +
				"FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.RETURNID, A.RETURNDATE, A.BRANCHID, A.SUPPID, A.TAXTYPE, A.REMARK, " +
				"B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, A.ISLOCK " +
				"FROM PURCHASE_RETURN A, CUSTOMER B, CITY C " +
				"WHERE B.CITY = C.CITY " +
				"AND A.RETURNID = ? " +
				"AND A.BRANCHID = ? " +
				"AND A.SUPPID = B.CUSTOMERID";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.RATE, B.TAX " +
				"FROM ITEM A, PURCHASE_RETURN_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.RETURNID = ? " +
				"AND B.BRANCHID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBranchDetails);
			stmt.setString(1, billPrefix);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.getBranch().setAccessId(rs.getInt("ACCESSID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setAddress(rs.getString("ADDRESS"));
				model.getBranch().setCity(rs.getString("CITY"));
				model.getBranch().setPincode(rs.getString("PINCODE"));
				model.getBranch().setVat(rs.getString("VAT"));
				model.getBranch().setCst(rs.getString("CST"));
				model.getBranch().setPhone1(rs.getString("PHONE1"));
				model.getBranch().setPhone2(rs.getString("PHONE2"));
				model.getBranch().setStdcode(rs.getString("STDCODE"));
				model.getBranch().setEmail(rs.getString("EMAIL"));
				model.getBranch().setWithTin(rs.getString("WITHTIN"));
				model.getBranch().setNoTin(rs.getString("NOTIN"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, billNumb);
			stmt.setInt(2, model.getBranch().getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setPurchaseId(rs.getInt("RETURNID"));
				model.setReturnDt(rs.getString("RETURNDATE"));
				model.setBillType(rs.getInt("TAXTYPE"));
				model.setRemark(rs.getString("REMARK"));
				model.setLock(rs.getString("ISLOCK").equals("Y"));
				model.getSupplier().setCustomerId(rs.getInt("SUPPID"));
				model.getSupplier().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getSupplier().setAddress(rs.getString("ADDRESS"));
				model.getSupplier().setCity(rs.getString("CITY"));
				model.getSupplier().setPincode(rs.getString("PINCODE"));
				model.getSupplier().setTin(rs.getString("TIN"));
				model.getSupplier().setCst(rs.getString("CST"));
				model.getSupplier().setState(rs.getString("STATE"));
				model.getSupplier().setStdcode(rs.getString("STDCODE"));
				model.getSupplier().setPhone1(rs.getString("PHONE1"));
				model.getSupplier().setPhone2(rs.getString("PHONE2"));
				model.getSupplier().setMobile1(rs.getString("MOBILE1"));
				model.getSupplier().setMobile2(rs.getString("MOBILE2"));
				model.getSupplier().setEmail(rs.getString("EMAIL"));
				model.getSupplier().setContactPerson(rs.getString("CONTACTPERSON"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getItemDetails);
			stmt.setInt(1, Integer.parseInt(billNumb));
			stmt.setInt(2, model.getBranch().getAccessId());
			
			rs = stmt.executeQuery();
			ItemModel itm = null;
			
			while(rs.next()){
				itm = new ItemModel();
				itm.setItemId(rs.getInt("ITEMID"));
				itm.setItemNumber(rs.getString("ITEMNUMBER"));
				itm.setItemName(rs.getString("ITEMNAME"));
				model.addItem(itm,  rs.getDouble("QTY"), rs.getDouble("RATE"), rs.getDouble("TAX"));
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

	public PurchaseReturnModel[] getTodayPurchaseReturnDetails(String fromDate, String toDate, String accessId) {
		ArrayList<PurchaseReturnModel> todayBills = new ArrayList<PurchaseReturnModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		PurchaseReturnModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.RETURNID, A.RETURNDATE, A.BRANCHID, A.SUPPID, A.TAXTYPE, A.REMARK, " +
				"B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.AREA, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, D.BILL_PREFIX, SUM(((100+E.TAX)/100) * E.QTY * E.RATE) AS AMT " +
				"FROM PURCHASE_RETURN A, CUSTOMER B, CITY C, ACCESS_POINT D, PURCHASE_RETURN_ITEM E " +
				"WHERE B.CITY = C.CITY " +
				"AND A.RETURNID = E.RETURNID " +
				"AND A.BRANCHID = E.BRANCHID " +
				"AND A.BRANCHID = D.ACCESSID "+
				"AND A.RETURNDATE >= ? "+
				"AND A.RETURNDATE <= ? "+
				"AND A.BRANCHID = ? "+
				"AND A.SUPPID = B.CUSTOMERID " +
				"GROUP BY A.BRANCHID, A.RETURNID " +
				"ORDER BY A.BRANCHID, A.RETURNID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new PurchaseReturnModel();
				model.setPurchaseId(rs.getInt("RETURNID"));
				model.setReturnDt(rs.getString("RETURNDATE"));
				model.setBillType(rs.getInt("TAXTYPE"));
				model.setRemark(rs.getString("REMARK"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.getSupplier().setCustomerId(rs.getInt("SUPPID"));
				model.getSupplier().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getSupplier().setAddress(rs.getString("ADDRESS"));
				model.getSupplier().setCity(rs.getString("CITY"));
				model.getSupplier().setArea(rs.getString("AREA"));
				model.getSupplier().setPincode(rs.getString("PINCODE"));
				model.getSupplier().setTin(rs.getString("TIN"));
				model.getSupplier().setCst(rs.getString("CST"));
				model.getSupplier().setState(rs.getString("STATE"));
				model.getSupplier().setStdcode(rs.getString("STDCODE"));
				model.getSupplier().setPhone1(rs.getString("PHONE1"));
				model.getSupplier().setPhone2(rs.getString("PHONE2"));
				model.getSupplier().setMobile1(rs.getString("MOBILE1"));
				model.getSupplier().setMobile2(rs.getString("MOBILE2"));
				model.getSupplier().setEmail(rs.getString("EMAIL"));
				model.getSupplier().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
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
		
		return (PurchaseReturnModel[]) todayBills.toArray(new PurchaseReturnModel[0]);
	}

	public MessageModel editPurchaseReturn(PurchaseReturnModel model) {
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
		String updateBillDetails = "UPDATE PURCHASE_RETURN SET SUPPID = ? , TAXTYPE = ?, REMARK = ? " +
				"WHERE RETURNID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String deleteBillItems = "DELETE FROM PURCHASE_RETURN_ITEM WHERE RETURNID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO PURCHASE_RETURN_ITEM (RETURNID, BRANCHID, ITEMID, QTY, RATE, TAX) " +
				"VALUES (?,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, model.getBranch().getBillPrefix());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
				
				stmt = getConnection().prepareStatement(deleteBillItems);
				stmt.setInt(1, model.getPurchaseId());
				stmt.setString(2, accessId);
				
				stmt.execute();
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				stmt1 = getConnection().prepareStatement(insertBillItems);
				
				stmt.setInt(1, model.getSupplier().getCustomerId());
				stmt.setInt(2, model.getBillType());
				stmt.setString(3, model.getRemark());
				stmt.setInt(4, model.getPurchaseId());
				stmt.setString(5, accessId);
				
				stmt.execute();
				
				for(int i=0; i<model.getItems().size(); i++){
					stmt1.setLong(1, model.getPurchaseId());
					stmt1.setString(2, accessId);
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setDouble(5, ((model.getItems())).get(i).getRate());
					stmt1.setDouble(6, ((model.getItems())).get(i).getTax());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getBranch().getBillPrefix()+"PR"+ model.getPurchaseId());
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing purchase details!!");
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

	public PurchaseItemModel[] getExportPurchaseReturnItems(
			String fromDate, String toDate) {
		
		ArrayList<PurchaseItemModel> lst = new ArrayList<PurchaseItemModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		PurchaseItemModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.RETURNID, A.QTY, A.RATE, A.TAX,B.ITEMID, B.ITEMNUMBER, B.ITEMNAME, D.BILL_PREFIX, A.BRANCHID " +
				"FROM PURCHASE_RETURN_ITEM A, ITEM B, PURCHASE_RETURN C, ACCESS_POINT D WHERE B.ITEMID = A.ITEMID AND C.BRANCHID = D.ACCESSID AND A.RETURNID = C.RETURNID " +
				"AND A.BRANCHID = C.BRANCHID  " +
				"AND C.RETURNDATE >= ? AND C.RETURNDATE <= ? ORDER BY A.BRANCHID, A.RETURNID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new PurchaseItemModel();
				model.getItem().setItemId(rs.getInt("ITEMID"));
				model.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
				model.getItem().setItemName(rs.getString("ITEMNAME"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.setPurchaseId(rs.getInt("RETURNID"));
				model.setQty(rs.getDouble("QTY"));
				model.setRate(rs.getDouble("RATE"));
				model.setTax(rs.getDouble("TAX"));
				lst.add(model);
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
		
		return (PurchaseItemModel[]) lst.toArray(new PurchaseItemModel[0]);
	}

	public PurchaseReturnModel[] getExportPurchaseReturnMaster(
			String fromDate, String toDate) {
		
		ArrayList<PurchaseReturnModel> lst = new ArrayList<PurchaseReturnModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		PurchaseReturnModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.RETURNID, A.RETURNDATE, A.BRANCHID, A.SUPPID, A.TAXTYPE, A.REMARK, " +
				"B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, D.BILL_PREFIX, SUM(((100+E.TAX)/100) * E.QTY * E.RATE) AS AMT " +
				"FROM PURCHASE_RETURN A, CUSTOMER B, CITY C, ACCESS_POINT D, PURCHASE_RETURN_ITEM E " +
				"WHERE B.CITY = C.CITY " +
				"AND A.RETURNID = E.RETURNID " +
				"AND A.BRANCHID = E.BRANCHID " +
				"AND A.BRANCHID = D.ACCESSID "+
				"AND A.RETURNDATE >= ? "+
				"AND A.RETURNDATE <= ? "+
				"AND A.SUPPID = B.CUSTOMERID " +
				"GROUP BY A.BRANCHID, A.RETURNID " +
				"ORDER BY A.BRANCHID, A.RETURNID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new PurchaseReturnModel();
				model.setPurchaseId(rs.getInt("RETURNID"));
				model.setReturnDt(rs.getString("RETURNDATE"));
				model.setBillType(rs.getInt("TAXTYPE"));
				model.setRemark(rs.getString("REMARK"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.getSupplier().setCustomerId(rs.getInt("SUPPID"));
				model.getSupplier().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getSupplier().setAddress(rs.getString("ADDRESS"));
				model.getSupplier().setCity(rs.getString("CITY"));
				model.getSupplier().setPincode(rs.getString("PINCODE"));
				model.getSupplier().setTin(rs.getString("TIN"));
				model.getSupplier().setCst(rs.getString("CST"));
				model.getSupplier().setState(rs.getString("STATE"));
				model.getSupplier().setStdcode(rs.getString("STDCODE"));
				model.getSupplier().setPhone1(rs.getString("PHONE1"));
				model.getSupplier().setPhone2(rs.getString("PHONE2"));
				model.getSupplier().setMobile1(rs.getString("MOBILE1"));
				model.getSupplier().setMobile2(rs.getString("MOBILE2"));
				model.getSupplier().setEmail(rs.getString("EMAIL"));
				model.getSupplier().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				lst.add(model);
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
		
		return (PurchaseReturnModel[]) lst.toArray(new PurchaseReturnModel[0]);
	}
}
