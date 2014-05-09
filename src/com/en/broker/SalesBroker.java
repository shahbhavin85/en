package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;
import com.en.model.SalesModel;
import com.en.util.StringUtility;
import com.en.util.Utils;

public class SalesBroker extends BaseBroker {

	public SalesBroker(Connection conn) {
		super(conn);
	}
	
	public synchronized MessageModel addSales(SalesModel model){
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
		String maxBillNo = "SELECT MAX(SALESID) FROM SALES WHERE BRANCHID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";
		
		String updateCustomerDtls = "UPDATE CUSTOMER SET TIN = ?, CST = ?, CONTACTPERSON = ?, MOBILE1 = ? WHERE CUSTOMERID = ?";

		// query to insert the bill details
		String insertBillDetails = "INSERT INTO SALES (SALESID, SALESDATE, BRANCHID, CUSTID, PAYMODE, TAXTYPE, SALESMANID, PACKING, FORWARD, INSTALLATION, LESS, TRANSPORT, CESS, REMARKS, CREDITDAYS, CTNS, PACKEDBY, DESPATCHEDBY) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO SALES_ITEM (SALESID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
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
				stmt.setInt(4, model.getPaymode());
				stmt.setInt(5, model.getTaxtype());
				stmt.setString(6, model.getSalesman().getUserId());
				stmt.setDouble(7, model.getPacking());
				stmt.setDouble(8, model.getForwarding());
				stmt.setDouble(9, model.getInstallation());
				stmt.setDouble(10, model.getLess());
				stmt.setString(11, model.getTransport());
				stmt.setFloat(12, taxRates.get("cess"));
				stmt.setString(13, model.getRemarks());
				stmt.setInt(14, model.getCreditDays());
				stmt.setInt(15, model.getCtns());
				stmt.setString(16, model.getPackedBy().getUserId());
				stmt.setString(17, model.getDespatchedBy().getUserId());
				
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
				
				stmt = getConnection().prepareStatement(updateCustomerDtls);
				stmt.setString(1, model.getCustomer().getTin());
				stmt.setString(2, model.getCustomer().getCst());
				stmt.setString(3, model.getCustomer().getContactPerson());
				stmt.setString(4, model.getCustomer().getMobile1());
				stmt.setInt(5, model.getCustomer().getCustomerId());
				
				stmt.execute();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + salesId);
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
	
	public SalesModel getSalesDetails(String billNo){
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		SalesModel model = new SalesModel();
		
		String billPrefix = billNo.substring(0,3);
		String billNumb = billNo.substring(3);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL, WITHTIN, NOTIN, STATE FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.SALESID, A.SALESDATE, A.CUSTID, B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, B.AREA, B.CUSTOMERTYPE, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, B.EMAIL1, A.PAYMODE, A.TAXTYPE, A.SALESMANID, A.PACKING, A.FORWARD, A.INSTALLATION, " +
				"A.LESS, A.TRANSPORT, A.LRNO, A.LRDT, A.CESS, A.REMARKS, A.CREDITDAYS, A.ISLOCK, A.CTNS, A.PACKEDBY, A.DESPATCHEDBY, A.ADJAMT FROM SALES A, CUSTOMER B, CITY C WHERE B.CITY = C.CITY AND A.SALESID = ? AND A.BRANCHID = ? AND A.CUSTID = B.CUSTOMERID AND A.STATUS = 0";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.RATE, B.TAX, B.DESP, B.DISRATE, A.PRICE " +
				"FROM ITEM A, SALES_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.SALESID = ? " +
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
				model.setSaleid(rs.getInt("SALESID"));
				model.setSalesdate(rs.getString("SALESDATE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setPaymode(rs.getInt("PAYMODE"));
				model.setCreditDays(rs.getInt("CREDITDAYS"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setCess(rs.getDouble("CESS"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setRemarks(rs.getString("REMARKS"));
				model.setLrno(rs.getString("LRNO"));
				model.setLrdt(rs.getString("LRDT"));
				model.setLock(rs.getString("ISLOCK").equals("Y"));
				model.setCtns(rs.getInt("CTNS"));
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
				if(!rs.getString("PACKEDBY").equals("")){
					model.setPackedBy((new UserBroker(getConnection())).getUserDtls(rs.getString("PACKEDBY")));
				}
				if(!rs.getString("DESPATCHEDBY").equals("")){
					model.setDespatchedBy((new UserBroker(getConnection())).getUserDtls(rs.getString("DESPATCHEDBY")));
				}
				model.setAdjAmt(rs.getDouble("ADJAMT"));
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

	public SalesItemModel[] getExportSalesItems(String fromDate,
			String toDate) {
		
		ArrayList<SalesItemModel> lst = new ArrayList<SalesItemModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		SalesItemModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.SALESID, A.QTY, A.RATE, A.TAX, A.DISRATE, B.ITEMID, B.ITEMNUMBER, B.ITEMNAME, D.BILL_PREFIX, A.BRANCHID " +
				"FROM SALES_ITEM A, ITEM B, SALES C, ACCESS_POINT D WHERE B.ITEMID = A.ITEMID AND C.BRANCHID = D.ACCESSID AND A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID  " +
				"AND C.SALESDATE >= ? AND C.SALESDATE <= ? ORDER BY A.BRANCHID, A.SALESID";
		
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
				model.setSalesId(rs.getString("SALESID"));
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

	public SalesModel[] getExportSalesMaster(String fromDate,
			String toDate) {
		
		ArrayList<SalesModel> lst = new ArrayList<SalesModel>(0);
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		SalesModel model = null;
		
		// query to get the bill no details
		String getBillDetails = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, A.CUSTID, B.CONTACTPERSON, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, A.PAYMODE, A.TAXTYPE, D.BILL_PREFIX, A.SALESMANID, A.PACKING, A.FORWARD, A.INSTALLATION, " +
				"A.LESS, A.CESS, A.REMARKS, SUM(E.QTY*((100+E.TAX)*((100-E.DISRATE)*E.RATE/100)/100)) AS AMT, A.CREDITDAYS FROM SALES A, CUSTOMER B, CITY C, ACCESS_POINT D, SALES_ITEM E WHERE B.CITY = C.CITY AND A.SALESID = E.SALESID AND A.BRANCHID = E.BRANCHID AND A.BRANCHID = D.ACCESSID AND A.CUSTID = B.CUSTOMERID " +
				"AND A.SALESDATE >= ? AND A.SALESDATE <= ? GROUP BY A.BRANCHID, A.SALESID ORDER BY A.BRANCHID, A.SALESID";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME FROM USER WHERE USERID = ?";
		
		try{
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new SalesModel();
				model.setSaleid(rs.getInt("SALESID"));
				model.setSalesdate(rs.getString("SALESDATE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setPaymode(rs.getInt("PAYMODE"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setCess(rs.getDouble("CESS"));
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
		
		return (SalesModel[]) lst.toArray(new SalesModel[0]);
	}
	
	public synchronized MessageModel editSales(SalesModel model){
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
		String updateBillDetails = "UPDATE SALES SET CUSTID = ? , PAYMODE = ?, TAXTYPE = ?, SALESMANID = ? , PACKING = ?, " +
				"FORWARD = ?, INSTALLATION = ?, LESS = ?, TRANSPORT = ?,  REMARKS = ?, CREDITDAYS = ?, CTNS = ?, PACKEDBY = ?, DESPATCHEDBY = ? " +
				"WHERE SALESID = ? AND BRANCHID = ?";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		String updateCustomerDtls = "UPDATE CUSTOMER SET TIN = ?, CST = ?, CONTACTPERSON = ?, MOBILE1 = ? WHERE CUSTOMERID = ?";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";

		//query to insert the bill items
		String deleteBillItems = "DELETE FROM SALES_ITEM WHERE SALESID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO SALES_ITEM (SALESID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
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
				stmt.setInt(2, model.getPaymode());
				stmt.setInt(3, model.getTaxtype());
				stmt.setString(4, model.getSalesman().getUserId());
				stmt.setDouble(5, model.getPacking());
				stmt.setDouble(6, model.getForwarding());
				stmt.setDouble(7, model.getInstallation());
				stmt.setDouble(8, model.getLess());
				stmt.setString(9, model.getTransport());
				stmt.setString(10, model.getRemarks());
				stmt.setInt(11, model.getCreditDays());
				stmt.setInt(12, model.getCtns());
				stmt.setString(13, model.getPackedBy().getUserId());
				stmt.setString(14, model.getDespatchedBy().getUserId());
				stmt.setInt(15, model.getSaleid());
				stmt.setString(16, accessId);
				
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
				
				stmt = getConnection().prepareStatement(updateCustomerDtls);
				stmt.setString(1, model.getCustomer().getTin());
				stmt.setString(2, model.getCustomer().getCst());
				stmt.setString(3, model.getCustomer().getContactPerson());
				stmt.setString(4, model.getCustomer().getMobile1());
				stmt.setInt(5, model.getCustomer().getCustomerId());
				
				stmt.execute();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Bill Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getBranch().getBillPrefix() + model.getSaleid());
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

	public String getLastBillNo(AccessPointModel access) {
		String lastBillNo = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String maxBillNo = "SELECT MAX(SALESID) FROM SALES WHERE BRANCHID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(maxBillNo);
			stmt.setInt(1, access.getAccessId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getLong(1) > 0){
				lastBillNo = access.getBillPrefix() + rs.getInt(1);
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

	public SalesModel[] getTodaySalesDetails(String fromDate, String toDate, String accessId) {
		ArrayList<SalesModel> todayBills = new ArrayList<SalesModel>(0);
		SalesModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.PAYMODE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS " +
				"FROM SALES A, SALES_ITEM C, CUSTOMER B " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.SALESDATE >= ? " +
				"AND A.SALESDATE <= ? " +
				"AND A.BRANCHID = ? " +
				"GROUP BY A.SALESID " +
				"ORDER BY A.SALESID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new SalesModel();
				model.setSaleid(rs.getInt("SALESID"));
				model.setSalesdate(rs.getString("SALESDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setPaymode(rs.getInt("PAYMODE"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
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
		
		return (SalesModel[]) todayBills.toArray(new SalesModel[0]);
	}

	public MessageModel editSalesLRDetails(String billNo, String lrNo,
			String lrDt, String transport, String packedBy, String despatchedBy, int accessId, String ctns) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to insert the bill details
		String updateBillDetails = "UPDATE SALES SET LRNO = ? , LRDT = ?, TRANSPORT = ?, CTNS = ?, PACKEDBY = ?, DESPATCHEDBY = ? " +
				"WHERE SALESID = ? AND BRANCHID = ?";
		
		try {
				
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				stmt.setString(1, lrNo);
				stmt.setString(2, lrDt);
				stmt.setString(3, transport);
				stmt.setString(4, ctns);
				stmt.setString(5, packedBy);
				stmt.setString(6, despatchedBy);
				stmt.setString(7, billNo.substring(3));
				stmt.setInt(8, accessId);
				
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

	public CustomerModel[] getCustomer() {
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.CITY, A.AREA, A.CUSTOMERTYPE, A.CONTACTPERSON, A.PHONE1, A.MOBILE1, A.TIN, A.CST, A.TRANSPORT, B.STATE FROM CUSTOMER A, CITY B WHERE A.CITY=B.CITY AND STATUS = 'A' " +
				"AND A.CUSTOMERID IN (SELECT DISTINCT(CUSTID) FROM SALES) ORDER BY TRIM(A.CUSTOMERNAME), A.AREA, A.CITY";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerModel();
				model.setCustomerId(rs.getInt("CUSTOMERID"));
				model.setCustomerName(StringUtility.replaceCots(rs.getString("CUSTOMERNAME")));
				model.setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.setArea(rs.getString("AREA"));
				model.setCity(rs.getString("CITY"));
				model.setState(rs.getString("STATE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setTin(rs.getString("TIN"));
				model.setCst(rs.getString("CST"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setContactPerson(rs.getString("CONTACTPERSON"));
				customers.add(model);
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
		
		return (CustomerModel[]) customers.toArray(new CustomerModel[0]);
	}

	public MessageModel editSalesDate(String billNo, String salesDt, String accessId) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean toContinue = true;
		String myFormatString = "dd-MM-yyyy"; // for example
		SimpleDateFormat df = new SimpleDateFormat(myFormatString);
		
		int prevBillNo = (Integer.parseInt(billNo.substring(3)) != 1) ? Integer.parseInt(billNo.substring(3)) -1 : 0;
		int nextBillNo = Integer.parseInt(billNo.substring(3)) +1;
		
		// query to get the sales date
		String getSalesdate  = "SELECT SALESDATE FROM SALES WHERE SALESID = ? AND BRANCHID = ?";

		// query to insert the bill details
		String updateBillDetails = "UPDATE SALES SET SALESDATE = ? " +
				"WHERE SALESID = ? AND BRANCHID = ?";
		
		try {
			Date editDate = df.parse(Utils.convertToAppDate(salesDt));
//			System.out.println(df.format(editDate));
			
			if(prevBillNo != 0){
				Date prevDate = null;
				stmt = getConnection().prepareStatement(getSalesdate);
				stmt.setInt(1, prevBillNo);
				stmt.setString(2, accessId);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					prevDate = df.parse(Utils.convertToAppDate(rs.getString("SALESDATE")));
					if(editDate.before(prevDate)){
						toContinue = false;
						msg = new Message(ERROR, "Previous Bill was on "+df.format(prevDate));
					}
				}
			}
			
			if(toContinue){
				Date nextDate = null;
				
				stmt = getConnection().prepareStatement(getSalesdate);
				stmt.setInt(1, nextBillNo);
				stmt.setString(2, accessId);
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					nextDate = df.parse(Utils.convertToAppDate(rs.getString("SALESDATE")));
					if(editDate.after(nextDate)){
						toContinue = false;
						msg = new Message(ERROR, "Next Bill was on "+df.format(nextDate));
					}
				}
			}
			
			if(toContinue){
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				stmt.setString(1, salesDt);
				stmt.setString(2, billNo.substring(3));
				stmt.setString(3, accessId);
				
				stmt.execute();
				
				
				msg = new Message(SUCCESS, "Bill Sales Date Edited successfully!!");
			}
			
			getConnection().commit();
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

	public CustomerModel[] getSalesCustomer() {
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.CITY, A.AREA, A.CUSTOMERTYPE, A.CONTACTPERSON, A.PHONE1, A.MOBILE1, A.TIN, A.CST, A.TRANSPORT, B.STATE, COUNT(C.SALESID) AS BILLCOUNT FROM CITY B, CUSTOMER A LEFT JOIN SALES C ON C.CUSTID = A.CUSTOMERID WHERE A.CITY=B.CITY AND STATUS = 'A' GROUP BY A.CUSTOMERID ORDER BY TRIM(A.CUSTOMERNAME)";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerModel();
				model.setCustomerId(rs.getInt("CUSTOMERID"));
				model.setCustomerName(rs.getString("CUSTOMERNAME"));
				model.setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.setArea(rs.getString("AREA"));
				model.setCity(rs.getString("CITY"));
				model.setState(rs.getString("STATE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setTin(rs.getString("TIN"));
				model.setCst(rs.getString("CST"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setContactPerson(rs.getString("CONTACTPERSON"));
				model.setBillCount(rs.getInt("BILLCOUNT"));
				customers.add(model);
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
		
		return (CustomerModel[]) customers.toArray(new CustomerModel[0]);
	}

	public JSONArray getAjaxCustList(String custLike) {
		JSONObject temp = null;
		JSONArray lst = new JSONArray();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.CITY, A.AREA, A.CUSTOMERTYPE, A.CONTACTPERSON, A.PHONE1, A.MOBILE1, A.TIN, A.CST, A.TRANSPORT, B.STATE, COUNT(C.SALESID) AS BILLCOUNT FROM CITY B, CUSTOMER A LEFT JOIN SALES C ON C.CUSTID = A.CUSTOMERID WHERE A.CITY=B.CITY AND A.STATUS = 'A' AND CONCAT(A.CUSTOMERNAME,A.AREA,A.CITY) LIKE ? GROUP BY A.CUSTOMERID ORDER BY TRIM(A.CUSTOMERNAME), A.AREA, A.CITY LIMIT 20";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			stmt.setString(1, custLike+"%");
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				temp = new JSONObject();
				
				temp.put("custId", rs.getInt("CUSTOMERID"));
				temp.put("custName", StringUtility.replaceCots(rs.getString("CUSTOMERNAME")));
				temp.put("custType", rs.getString("CUSTOMERTYPE"));
				temp.put("area", rs.getString("AREA"));
				temp.put("city", rs.getString("CITY"));
				temp.put("state", rs.getString("STATE"));
				temp.put("phone", rs.getString("PHONE1"));
				temp.put("mobile", rs.getString("MOBILE1"));
				temp.put("tin", rs.getString("TIN"));
				temp.put("cst", rs.getString("CST"));
				temp.put("transport", rs.getString("TRANSPORT"));
				temp.put("person", rs.getString("CONTACTPERSON"));
				temp.put("count", rs.getInt("BILLCOUNT"));
				
				lst.add(temp);
			}
			
			getConnection().commit();
			
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
					getConnection().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return lst;
	}

	public HashMap<String, Integer> getSalesItemCheckList(String billNo) {
		HashMap<String, Integer> dtls = new HashMap<String, Integer>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the itemid for the sold item
		String getItems = "SELECT A.ITEMID, C.ITEMNUMBER, B.DESP, (B.QTY*A.QTY) AS TOTALQTY FROM SALES_ITEM A, ITEM_CHECK_LIST B, ITEM C WHERE A.ITEMID = C.ITEMID AND A.ITEMID = B.ITEMID AND A.SALESID = ? AND A.BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?)";
		
		try{
			
			stmt = getConnection().prepareStatement(getItems);
			
			stmt.setString(1, billNo.substring(3));
			stmt.setString(2, billNo.substring(0,3));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				dtls.put(rs.getString("ITEMNUMBER")+" - "+rs.getString("DESP"), rs.getInt("TOTALQTY"));
			}
			
			getConnection().commit();
			
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
					getConnection().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return dtls;
	}

	public MessageModel cancelSales(String billNo, String accessId) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check for payment
		String checkPayment = "SELECT PAYAMT FROM SALES WHERE SALESID = ? AND BRANCHID = ?";
		
		// query to insert the bill details
		String updateBillDetails = "UPDATE SALES SET STATUS = 1 " +
				"WHERE SALESID = ? AND BRANCHID = ?";
		
		String deleteBillItems = "DELETE FROM SALES_ITEM WHERE SALESID = ? AND BRANCHID = ?";
		
		try {
			
			// executing the query to insert bill and bill item details
			stmt = getConnection().prepareStatement(checkPayment);
			
			stmt.setString(1, billNo.substring(3));
			stmt.setString(2, accessId);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getDouble("PAYAMT") > 0){
				
				msg = new Message(ALERT, "Bill cannot be cancelled as payment is received or adjusted against this bill.");
				
			} else {
			
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				stmt.setString(1, billNo.substring(3));
				stmt.setString(2, accessId);
				
				stmt.execute();
	
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(deleteBillItems);
				
				stmt.setString(1, billNo.substring(3));
				stmt.setString(2, accessId);
				
				stmt.execute();
				
				msg = new Message(SUCCESS, "Bill cancelled successfully!!");
			
			}
			
			getConnection().commit();
			msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while cancelling sales bill!!");
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

	public void updateCustomerDetails(String custId, String tin, String cst,
			String mobile1, String mobile2, String email1, String email2) {
		PreparedStatement stmt = null;
		
		String updateCustomerDtls = "UPDATE CUSTOMER SET TIN = ?, CST = ?, MOBILE1 = ?, MOBILE2 = ?, EMAIL = ?, EMAIL1 = ? WHERE CUSTOMERID = ?";
		
		try {
			
			// executing the query to insert bill and bill item details
			stmt = getConnection().prepareStatement(updateCustomerDtls);
			
			stmt.setString(1, tin);
			stmt.setString(2, cst);
			stmt.setString(3, mobile1);
			stmt.setString(4, mobile2);
			stmt.setString(5, email1);
			stmt.setString(6, email2);
			stmt.setString(7, custId);
			
			stmt.execute();
			
			getConnection().commit();
				
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public SalesModel[] getPendingSalesLRList(AccessPointModel access) {
		ArrayList<SalesModel> todayBills = new ArrayList<SalesModel>(0);
		SalesModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TRANSPORT, A.LRNO, A.LRDT " +
				"FROM SALES A, CUSTOMER B " +
				"WHERE A.CUSTID = B.CUSTOMERID " +
				"AND A.LRNO = '' " +
				"AND A.BRANCHID = ? " +
				"GROUP BY A.SALESID " +
				"ORDER BY A.SALESID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setInt(1, access.getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new SalesModel();
				model.setSaleid(rs.getInt("SALESID"));
				model.setSalesdate(rs.getString("SALESDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setLrdt(rs.getString("LRDT"));
				model.setLrno(rs.getString("LRNO"));
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
		
		return (SalesModel[]) todayBills.toArray(new SalesModel[0]);
	}

	public void updateSalesNoLRDtls(String billNo) {
		PreparedStatement stmt = null;
		
		String updateCustomerDtls = "UPDATE SALES SET LRNO = '-' WHERE SALESID = ? AND BRANCHID = (SELECT A.ACCESSID FROM ACCESS_POINT A WHERE A.BILL_PREFIX = ?)";
		
		try {
			
			// executing the query to insert bill and bill item details
			stmt = getConnection().prepareStatement(updateCustomerDtls);
			
			stmt.setString(1, billNo.substring(3));
			stmt.setString(2, billNo.substring(0,3));
			
			stmt.execute();
			
			getConnection().commit();
				
		} catch (Exception e) {
			try{
				getConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try{
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}
	
	

}
