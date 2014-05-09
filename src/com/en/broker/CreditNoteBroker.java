package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.en.model.AccessPointModel;
import com.en.model.CreditNoteModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;
import com.en.model.SalesModel;

public class CreditNoteBroker extends BaseBroker {

	public CreditNoteBroker(Connection conn) {
		super(conn);
	}

	public synchronized MessageModel addCreditNote(CreditNoteModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		long salesId = 1;
		String billPrefix = "";
		HashMap<String, Float> taxRates = new HashMap<String, Float>(0);
		String taxStr = "";
		double taxRate = 0;

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(CREDITNOTEID) FROM CREDIT_NOTE WHERE BRANCHID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";
		
		// query to insert the bill details
		String insertBillDetails = "INSERT INTO CREDIT_NOTE (CREDITNOTEID, NOTEDATE, BRANCHID, CUSTID, TAXTYPE, SALESMANID, LESS, REMARKS) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?,?,?)";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO CREDIT_NOTE_ITEM (CREDITNOTEID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
				"VALUES (?,?,?,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(maxBillNo);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					salesId = rs.getInt(1) + 1;
				}
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getBillPrefix);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					billPrefix = rs.getString(1);
				}

				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getTaxRate);
				stmt.setInt(1, model.getBranch().getAccessId());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					taxRates.put("vat1", rs.getFloat(1));
					taxRates.put("vat2", rs.getFloat(2));
					taxRates.put("vat3", rs.getFloat(3));
					taxRates.put("cst1", rs.getFloat(4));
					taxRates.put("cst2", rs.getFloat(5));
					taxRates.put("cst3", rs.getFloat(6));
					taxRates.put("cstc1", rs.getFloat(7));
					taxRates.put("cstc2", rs.getFloat(8));
					taxRates.put("cstc3", rs.getFloat(9));
					taxRates.put("cess", rs.getFloat(10));
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(insertBillDetails);
				stmt1 = getConnection().prepareStatement(insertBillItems);
				stmt2 = getConnection().prepareStatement(getTaxSlab);
				
				stmt.setLong(1, salesId);
				stmt.setInt(2, model.getBranch().getAccessId());
				stmt.setInt(3, model.getCustomer().getCustomerId());
				stmt.setInt(4, model.getTaxtype());
				stmt.setString(5, model.getSalesman().getUserId());
				stmt.setDouble(6, model.getLess());
				stmt.setString(7, model.getRemarks());
				
				stmt.execute();
				
				if(model.getTaxtype() == 1){
					taxStr = "vat";
				} else if(model.getTaxtype() == 2){
					taxStr = "cst";
				} else {
					taxStr = "cstc";
				}
				
				for(int i=0; i<model.getItems().size(); i++){
					taxRate = 0;
					
					stmt2.setInt(1, ((model.getItems())).get(i).getItem().getItemId());
					stmt2.setInt(2, model.getBranch().getAccessId());
					
					rs = stmt2.executeQuery();
					
					if(rs.next()){
						taxRate = taxRates.get(taxStr+rs.getInt(1));
					}
					
					stmt1.setLong(1, salesId);
					stmt1.setInt(2, model.getBranch().getAccessId());
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setDouble(5, ((model.getItems())).get(i).getRate());
					stmt1.setDouble(6, taxRate);
					stmt1.setDouble(7, model.getItems().get(i).getDisrate());
					stmt1.setString(8, model.getItems().get(i).getDesc());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix +"CN"+ salesId);
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

	public CreditNoteModel getNoteDetails(String billNo) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		CreditNoteModel model = new CreditNoteModel();
		
		String billPrefix = billNo.substring(0,3);
		String billNumb = billNo.substring(5);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL, WITHTIN, NOTIN, STATE FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.CREDITNOTEID, A.NOTEDATE, A.CUSTID, B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, B.AREA, B.CUSTOMERTYPE, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, B.EMAIL1, A.TAXTYPE, A.SALESMANID, " +
				"A.LESS, A.REMARKS, A.ISLOCK FROM CREDIT_NOTE A, CUSTOMER B, CITY C WHERE B.CITY = C.CITY AND A.CREDITNOTEID = ? AND A.BRANCHID = ? AND A.CUSTID = B.CUSTOMERID";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.RATE, B.TAX, B.DESP, B.DISRATE, A.PRICE " +
				"FROM ITEM A, CREDIT_NOTE_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.CREDITNOTEID = ? " +
				"AND B.BRANCHID = ?";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME FROM USER WHERE USERID = ?";
		
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
				model.getBranch().setState(rs.getString("STATE"));
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
				model.setSaleid(rs.getInt("CREDITNOTEID"));
				model.setSalesdate(rs.getString("NOTEDATE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setLess(rs.getDouble("LESS"));
				model.setRemarks(rs.getString("REMARKS"));
				model.setLock(rs.getString("ISLOCK").equals("Y"));
				if(rs.getString("SALESMANID") != null){
					model.getSalesman().setUserId(rs.getString("SALESMANID"));
					stmt1 = getConnection().prepareStatement(getSalesmanName);
					stmt1.setString(1, rs.getString("SALESMANID"));
					rs1 = stmt1.executeQuery();
					while(rs1.next()){
						model.getSalesman().setUserName(rs1.getString("USERNAME"));
					}
				}
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setAddress(rs.getString("ADDRESS"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setPincode(rs.getString("PINCODE"));
				model.getCustomer().setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setTin(rs.getString("TIN"));
				model.getCustomer().setCst(rs.getString("CST"));
				model.getCustomer().setState(rs.getString("STATE"));
				model.getCustomer().setStdcode(rs.getString("STDCODE"));
				model.getCustomer().setPhone1(rs.getString("PHONE1"));
				model.getCustomer().setPhone2(rs.getString("PHONE2"));
				model.getCustomer().setMobile1(rs.getString("MOBILE1"));
				model.getCustomer().setMobile2(rs.getString("MOBILE2"));
				model.getCustomer().setEmail(rs.getString("EMAIL"));
				model.getCustomer().setEmail1(rs.getString("EMAIL1"));
				model.getCustomer().setContactPerson(rs.getString("CONTACTPERSON"));
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
				itm.setItemPrice(rs.getString("PRICE"));
				model.addItem(itm, rs.getString("DESP"), rs.getDouble("QTY"), rs.getDouble("RATE"), rs.getDouble("TAX"), rs.getDouble("DISRATE"));
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

	public String getLastBillNo(AccessPointModel access) {
		String lastBillNo = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(CREDITNOTEID) FROM CREDIT_NOTE WHERE BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(maxBillNo);
			stmt.setInt(1, access.getAccessId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1) > 0){
				lastBillNo = access.getBillPrefix()+"CN"+ rs.getInt(1);
			} else {
				lastBillNo = "--";
			}
			
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
		
		return lastBillNo;
	}

	public CreditNoteModel[] getTodayNoteDetails(String fromDate, String toDate, String accessId) {
		ArrayList<CreditNoteModel> todayBills = new ArrayList<CreditNoteModel>(0);
		CreditNoteModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.CREDITNOTEID, A.NOTEDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.LESS " +
				"FROM CREDIT_NOTE A, CREDIT_NOTE_ITEM C, CUSTOMER B " +
				"WHERE A.CREDITNOTEID = C.CREDITNOTEID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.NOTEDATE >= ? " +
				"AND A.NOTEDATE <= ? " +
				"AND A.BRANCHID = ? " +
				"GROUP BY A.CREDITNOTEID " +
				"ORDER BY A.CREDITNOTEID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CreditNoteModel();
				model.setSaleid(rs.getInt("CREDITNOTEID"));
				model.setSalesdate(rs.getString("NOTEDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setLess(rs.getDouble("LESS"));
				todayBills.add(model);
			}
			
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
		
		return (CreditNoteModel[]) todayBills.toArray(new CreditNoteModel[0]);
	}

	public MessageModel editCreditNote(CreditNoteModel model) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		String accessId = "";
		HashMap<String, Float> taxRates = new HashMap<String, Float>(0);
		String taxStr = "";
		double taxRate = 0;

		// query to check the existence of the bill no
//		String maxBillNo = "SELECT MAX(SALESID) FROM SALES WHERE BRANCHID = ?";

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";

		// query to insert the bill details
		String updateBillDetails = "UPDATE CREDIT_NOTE SET CUSTID = ? , TAXTYPE = ?, SALESMANID = ? , LESS = ?, REMARKS = ? " +
				"WHERE CREDITNOTEID = ? AND BRANCHID = ?";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";

		//query to insert the bill items
		String deleteBillItems = "DELETE FROM CREDIT_NOTE_ITEM WHERE CREDITNOTEID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO CREDIT_NOTE_ITEM (CREDITNOTEID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
				"VALUES (?,?,?,?,?,?,?,?)";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, model.getBranch().getBillPrefix());
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}

				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getTaxRate);
				stmt.setString(1,accessId);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					taxRates.put("vat1", rs.getFloat(1));
					taxRates.put("vat2", rs.getFloat(2));
					taxRates.put("vat3", rs.getFloat(3));
					taxRates.put("cst1", rs.getFloat(4));
					taxRates.put("cst2", rs.getFloat(5));
					taxRates.put("cst3", rs.getFloat(6));
					taxRates.put("cstc1", rs.getFloat(7));
					taxRates.put("cstc2", rs.getFloat(8));
					taxRates.put("cstc3", rs.getFloat(9));
					taxRates.put("cess", rs.getFloat(10));
				}
				
				stmt = getConnection().prepareStatement(deleteBillItems);
				stmt.setInt(1, model.getSaleid());
				stmt.setString(2, accessId);
				
				stmt.execute();
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				stmt1 = getConnection().prepareStatement(insertBillItems);
				stmt2 = getConnection().prepareStatement(getTaxSlab);
				
				stmt.setInt(1, model.getCustomer().getCustomerId());
				stmt.setInt(2, model.getTaxtype());
				stmt.setString(3, model.getSalesman().getUserId());
				stmt.setDouble(4, model.getLess());
				stmt.setString(5, model.getRemarks());
				stmt.setInt(6, model.getSaleid());
				stmt.setString(7, accessId);
				
				stmt.execute();
				
				if(model.getTaxtype() == 1){
					taxStr = "vat";
				} else if(model.getTaxtype() == 2){
					taxStr = "cst";
				} else {
					taxStr = "cstc";
				}
				
				for(int i=0; i<model.getItems().size(); i++){
					taxRate = 0;
					
					stmt2.setInt(1, ((model.getItems())).get(i).getItem().getItemId());
					stmt2.setString(2, accessId);
					
					rs = stmt2.executeQuery();
					
					if(rs.next()){
						taxRate = taxRates.get(taxStr+rs.getInt(1));
					}
					
					stmt1.setLong(1, model.getSaleid());
					stmt1.setString(2, accessId);
					stmt1.setInt(3, ((model.getItems())).get(i).getItem().getItemId());
					stmt1.setDouble(4, ((model.getItems())).get(i).getQty());
					stmt1.setDouble(5, ((model.getItems())).get(i).getRate());
					stmt1.setDouble(6, taxRate);
					stmt1.setDouble(7, model.getItems().get(i).getDisrate());
					stmt1.setString(8, model.getItems().get(i).getDesc());
					
					stmt1.addBatch();
				}
				
				stmt1.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getBranch().getBillPrefix() +"CN"+ model.getSaleid());
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

	public CreditNoteModel[] getExportSalesReturnMaster(
			String fromDate, String toDate) {
		
		ArrayList<CreditNoteModel> lst = new ArrayList<CreditNoteModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		CreditNoteModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.CREDITNOTEID, A.NOTEDATE, A.BRANCHID, A.CUSTID, B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, A.TAXTYPE, D.BILL_PREFIX, A.SALESMANID, " +
				"A.LESS, A.REMARKS, SUM(E.QTY*((100+E.TAX)*((100-E.DISRATE)*E.RATE/100)/100)) AS AMT FROM CREDIT_NOTE A, CUSTOMER B, CITY C, ACCESS_POINT D, CREDIT_NOTE_ITEM E WHERE B.CITY = C.CITY AND A.CREDITNOTEID = E.CREDITNOTEID AND A.BRANCHID = E.BRANCHID AND A.BRANCHID = D.ACCESSID AND A.CUSTID = B.CUSTOMERID " +
				"AND A.NOTEDATE >= ? AND A.NOTEDATE <= ? GROUP BY A.BRANCHID, A.CREDITNOTEID ORDER BY A.BRANCHID, A.CREDITNOTEID";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME FROM USER WHERE USERID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CreditNoteModel();
				model.setSaleid(rs.getInt("CREDITNOTEID"));
				model.setSalesdate(rs.getString("NOTEDATE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setRemarks(rs.getString("REMARKS"));
				model.setLess(rs.getDouble("LESS"));
				if(rs.getString("SALESMANID") != null){
					model.getSalesman().setUserId(rs.getString("SALESMANID"));
					stmt1 = getConnection().prepareStatement(getSalesmanName);
					stmt1.setString(1, rs.getString("SALESMANID"));
					rs1 = stmt1.executeQuery();
					while(rs1.next()){
						model.getSalesman().setUserName(rs1.getString("USERNAME"));
					}
				}
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setAddress(rs.getString("ADDRESS"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setPincode(rs.getString("PINCODE"));
				model.getCustomer().setTin(rs.getString("TIN"));
				model.getCustomer().setCst(rs.getString("CST"));
				model.getCustomer().setState(rs.getString("STATE"));
				model.getCustomer().setStdcode(rs.getString("STDCODE"));
				model.getCustomer().setPhone1(rs.getString("PHONE1"));
				model.getCustomer().setPhone2(rs.getString("PHONE2"));
				model.getCustomer().setMobile1(rs.getString("MOBILE1"));
				model.getCustomer().setMobile2(rs.getString("MOBILE2"));
				model.getCustomer().setEmail(rs.getString("EMAIL"));
				model.getCustomer().setContactPerson(rs.getString("CONTACTPERSON"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.setTotalAmt(rs.getDouble("AMT"));
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
		
		return (CreditNoteModel[]) lst.toArray(new CreditNoteModel[0]);
	}

	public SalesItemModel[] getExportSalesReturnItems(String fromDate,
			String toDate) {
		
		ArrayList<SalesItemModel> lst = new ArrayList<SalesItemModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		SalesItemModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.CREDITNOTEID, A.QTY, A.RATE, A.TAX, A.DISRATE, B.ITEMID, B.ITEMNUMBER, B.ITEMNAME, D.BILL_PREFIX, A.BRANCHID " +
				"FROM CREDIT_NOTE_ITEM A, ITEM B, CREDIT_NOTE C, ACCESS_POINT D WHERE B.ITEMID = A.ITEMID AND C.BRANCHID = D.ACCESSID AND A.CREDITNOTEID = C.CREDITNOTEID " +
				"AND A.BRANCHID = C.BRANCHID  " +
				"AND C.NOTEDATE >= ? AND C.NOTEDATE <= ? ORDER BY A.BRANCHID, A.CREDITNOTEID";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new SalesItemModel();
				model.getItem().setItemId(rs.getInt("ITEMID"));
				model.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
				model.getItem().setItemName(rs.getString("ITEMNAME"));
				model.setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setBranchId(rs.getInt("BRANCHID"));
				model.setSalesId(rs.getString("CREDITNOTEID"));
				model.setQty(rs.getDouble("QTY"));
				model.setRate(rs.getDouble("RATE"));
				model.setTaxrate(rs.getDouble("TAX"));
				model.setDisrate(rs.getDouble("DISRATE"));
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
		
		return (SalesItemModel[]) lst.toArray(new SalesItemModel[0]);
	}

}
