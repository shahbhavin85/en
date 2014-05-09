package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.CustomerModel;
import com.en.model.EntryModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.SalesItemModel;
import com.en.model.TransferItemModel;
import com.en.model.UserModel;
import com.en.util.LedgerDateComparator;
import com.en.util.OrderAlertDateComparator;
import com.en.util.Utils;

public class OrderBroker extends BaseBroker {

	public OrderBroker(Connection conn) {
		super(conn);
	}
	
	public OrderModel[] getExhibitionOrderDetails(){
		ArrayList<OrderModel> todayBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, Z.BILL_PREFIX, A.ISEMAIL " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, ACCESS_POINT Z " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = Z.ACCESSID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.FROMEX = 1 " +
				"GROUP BY A.ORDERID " +
				"ORDER BY A.ORDERID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				if(rs.getInt("ISEMAIL") == 1){
					model.setEmail();
				}
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
		
		return (OrderModel[]) todayBills.toArray(new OrderModel[0]);
	}

	public OrderModel[] getTodayOrderDetails(String fromDate,
			String toDate, String accessId) {
		ArrayList<OrderModel> todayBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, Z.BILL_PREFIX, A.ISEMAIL " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, ACCESS_POINT Z " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = Z.ACCESSID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.ORDERDATE >= ? " +
				"AND A.ORDERDATE <= ? " +
				"AND A.BRANCHID = ? " +
				"GROUP BY A.ORDERID " +
				"ORDER BY A.ORDERID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				if(rs.getInt("ISEMAIL") == 1){
					model.setEmail();
				}
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
		
		return (OrderModel[]) todayBills.toArray(new OrderModel[0]);
	}

	public synchronized MessageModel editOrder(OrderModel model) {
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
		String updateBillDetails = "UPDATE ORDER_MASTER SET CUSTID = ? , TAXTYPE = ?, SALESMANID = ? , REMARKS = ?, PACKING = ?, FORWARD = ?, INSTALLATION = ?, LESS = ?, " +
				"PRIORITY = ?, DEVDAYS = ?, FORMNO = ?, DEVADD = ?, PAYTYPE = ?, CREDDAYS = ?, DOWNPAY = ?, EMINO = ?, EMIAMT = ?, EMIDAYS = ? " +
				"WHERE ORDERID = ? AND BRANCHID = ?";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";

		//query to insert the bill items
		String deleteBillItems = "DELETE FROM ORDER_ITEM WHERE ORDERID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO ORDER_ITEM (ORDERID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
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
				stmt.setInt(1, model.getOrderId());
				stmt.setString(2, accessId);
				
				stmt.execute();
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				stmt1 = getConnection().prepareStatement(insertBillItems);
				stmt2 = getConnection().prepareStatement(getTaxSlab);
				
				stmt.setInt(1, model.getCustomer().getCustomerId());
				stmt.setInt(2, model.getTaxtype());
				stmt.setString(3, model.getSalesman().getUserId());
				stmt.setString(4, model.getRemarks());
				stmt.setDouble(5, model.getPacking());
				stmt.setDouble(6, model.getForwarding());
				stmt.setDouble(7, model.getInstallation());
				stmt.setDouble(8, model.getLess());
				stmt.setInt(9, model.getPriority());
				stmt.setInt(10, model.getDevDays());
				stmt.setString(11, model.getFormNo());
				stmt.setString(12, model.getDevAddress());
				stmt.setDouble(13, model.getPayType());
				stmt.setInt(14, model.getCredDays());
				stmt.setDouble(15, model.getDownPay());
				stmt.setInt(16, model.getEMINo());
				stmt.setDouble(17, model.getEMIAmt());
				stmt.setInt(18, model.getEMIDays());
				stmt.setInt(19, model.getOrderId());
				stmt.setString(20, accessId);
				
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
					
					stmt1.setLong(1, model.getOrderId());
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
				
				msg = new Message(SUCCESS, "Order Details Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getBranch().getBillPrefix() + "O" + model.getOrderId());
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing order details!!");
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

	public OrderModel getOrderDetails(String billNo) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		OrderModel model = new OrderModel();
		
		String billPrefix = billNo.substring(0,3);
		String billNumb = billNo.substring(4);
		
		// query to get the branch details
		String getBranchDetails = "SELECT A.ACCESSID, A.BILL_PREFIX, A.NAME, A.ADDRESS, A.CITY, A.PINCODE, A.VAT, A.CST, A.STDCODE, A.PHONE1, A.PHONE2, A.EMAIL, A.WITHTIN, A.NOTIN, V.STATE, " +
				"A.BANKNAME1, A.BANKBRANCH1, A.BANKAC1, A.BANKIFSC1, A.BANKNAME2, A.BANKBRANCH2, A.BANKAC2, A.BANKIFSC2, A.MOBILE1, A.MOBILE2 FROM ACCESS_POINT A, CITY V WHERE A.CITY = V.CITY AND A.BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.ORDERID, A.ORDERDATE, A.CUSTID, B.CONTACTPERSON, B.AREA, B.CUSTOMERTYPE, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, B.EMAIL1, A.TAXTYPE, A.SALESMANID, A.REMARKS, A.ISLOCK, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, " +
				"A.PRIORITY, A.DEVDAYS, A.FORMNO, A.DEVADD, A.PAYTYPE, A.CREDDAYS, A.DOWNPAY, A.EMINO, A.EMIAMT, A.EMIDAYS " +
				"FROM ORDER_MASTER A, CUSTOMER B, CITY C WHERE B.CITY = C.CITY AND A.ORDERID = ? AND A.BRANCHID = ? AND A.CUSTID = B.CUSTOMERID";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.RATE, B.TAX, B.DESP, B.DISRATE, B.SENTQTY " +
				"FROM ITEM A, ORDER_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.ORDERID = ? " +
				"AND B.BRANCHID = ?";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME, MOBILE1 FROM USER WHERE USERID = ?";
		
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
				model.getBranch().setBankName1(rs.getString("BANKNAME1"));
				model.getBranch().setBankBranch1(rs.getString("BANKBRANCH1"));
				model.getBranch().setBankAc1(rs.getString("BANKAC1"));
				model.getBranch().setBankIfsc1(rs.getString("BANKIFSC1"));
				model.getBranch().setBankName2(rs.getString("BANKNAME2"));
				model.getBranch().setBankBranch2(rs.getString("BANKBRANCH2"));
				model.getBranch().setBankAc2(rs.getString("BANKAC2"));
				model.getBranch().setBankIfsc2(rs.getString("BANKIFSC2"));
				model.getBranch().setMobile1(rs.getString("MOBILE1"));
				model.getBranch().setMobile2(rs.getString("MOBILE2"));
			}
			
			// executing the query to get the bill no by reference
			stmt = getConnection().prepareStatement(getBillDetails);
			stmt.setString(1, billNumb);
			stmt.setInt(2, model.getBranch().getAccessId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setRemarks(rs.getString("REMARKS"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.setPriority(rs.getInt("PRIORITY"));
				model.setDevDays(rs.getInt("DEVDAYS"));
				model.setFormNo(rs.getString("FORMNO"));
				model.setDevAddress(rs.getString("DEVADD"));
				model.setPayType(rs.getInt("PAYTYPE"));
				model.setCredDays(rs.getInt("CREDDAYS"));
				model.setDownPay(rs.getDouble("DOWNPAY"));
				model.setEMINo(rs.getInt("EMINO"));
				model.setEMIAmt(rs.getDouble("EMIAMT"));
				model.setEMIDays(rs.getInt("EMIDAYS"));
				model.setLock(rs.getString("ISLOCK").equals("Y"));
				if(rs.getString("SALESMANID") != null){
					model.getSalesman().setUserId(rs.getString("SALESMANID"));
					stmt1 = getConnection().prepareStatement(getSalesmanName);
					stmt1.setString(1, rs.getString("SALESMANID"));
					rs1 = stmt1.executeQuery();
					while(rs1.next()){
						model.getSalesman().setUserName(rs1.getString("USERNAME"));
						model.getSalesman().setMobile1(rs1.getString("MOBILE1"));
					}
				}
				model.getCustomer().setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setAddress(rs.getString("ADDRESS"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.getCustomer().setPincode(rs.getString("PINCODE"));
				model.getCustomer().setTin(rs.getString("TIN"));
				model.getCustomer().setCst(rs.getString("CST"));
				model.getCustomer().setArea(rs.getString("AREA"));
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
				model.addItem(itm, rs.getString("DESP"), rs.getDouble("QTY"), rs.getDouble("RATE"), rs.getDouble("TAX"), rs.getDouble("DISRATE"), rs.getDouble("SENTQTY"));
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

	public synchronized MessageModel addOrder(OrderModel model) {
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
		String maxBillNo = "SELECT MAX(ORDERID) FROM ORDER_MASTER WHERE BRANCHID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";
		
		// query to insert the bill details
		String insertBillDetails = "INSERT INTO ORDER_MASTER (ORDERID, ORDERDATE, BRANCHID, CUSTID, TAXTYPE, SALESMANID, REMARKS, PACKING, FORWARD, INSTALLATION, LESS, ADVANCE, PRIORITY, DEVDAYS, FORMNO, DEVADD, PAYTYPE, CREDDAYS, DOWNPAY, EMINO, EMIAMT, EMIDAYS, FROMEX) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO ORDER_ITEM (ORDERID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
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
				stmt.setString(6, model.getRemarks());
				stmt.setDouble(7, model.getPacking());
				stmt.setDouble(8, model.getForwarding());
				stmt.setDouble(9, model.getInstallation());
				stmt.setDouble(10, model.getLess());
				stmt.setDouble(11, model.getAdvance());
				stmt.setInt(12, model.getPriority());
				stmt.setInt(13, model.getDevDays());
				stmt.setString(14, model.getFormNo());
				stmt.setString(15, model.getDevAddress());
				stmt.setDouble(16, model.getPayType());
				stmt.setInt(17, model.getCredDays());
				stmt.setDouble(18, model.getDownPay());
				stmt.setInt(19, model.getEMINo());
				stmt.setDouble(20, model.getEMIAmt());
				stmt.setInt(21, model.getEMIDays());
				stmt.setInt(22, model.getFromEx());
				
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
				
				msg = new Message(SUCCESS, "Order Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + "O" +salesId);
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting order details!!");
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
	
	public HashMap<Integer, Double> getRequestedDetails(String accessId){
		
		HashMap<Integer, Double> items = new HashMap<Integer, Double>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT ((SUM(C.QTY))-(SUM(C.SENTQTY))) AS PQTY, C.ITEMID " +
				"FROM TRANSFER_REQUEST_ITEM C " +
				"WHERE C.BRANCHID = ? ";
		outstandingBill	= outstandingBill +	"GROUP BY C.ITEMID ORDER BY C.ITEMID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			stmt.setString(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				items.put(rs.getInt("ITEMID"), rs.getDouble("PQTY"));
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
		return items;
	}
	
	public HashMap<String ,Object> getFollowupItemDetails(String accessId){
		HashMap<String, Object> reportBills = new HashMap<String, Object>(0);
		HashMap<String, SalesItemModel> masterDtls = new HashMap<String, SalesItemModel>(0);
		HashMap<String, ArrayList<EntryModel>> formData = new HashMap<String, ArrayList<EntryModel>>(0);
		SalesItemModel item = null;
		ArrayList<EntryModel> orders = null;
		EntryModel model = null;
		String itemId = "";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double total = 0;
		double sentTotal = 0;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				"A.TAXTYPE, C.QTY, C.SENTQTY, C.ITEMID, Z.ITEMNAME, Z.ITEMNUMBER " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E, ITEM Z " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND C.ITEMID = Z.ITEMID " +
				"AND A.BRANCHID = ? " +
				"AND A.CUSTID = B.CUSTOMERID HAVING (QTY - SENTQTY) > 0 ";
		outstandingBill	= outstandingBill +	"ORDER BY C.ITEMID, A.ORDERID";
		
		String requestBills = "SELECT A.REQUESTID, A.REQUESTDATE, A.FROMID, E.BILL_PREFIX, E.NAME, E.CITY, C.QTY, C.SENTQTY, C.ITEMID, Z.ITEMNAME, Z.ITEMNUMBER "
				+ "FROM TRANSFER_REQUEST A, TRANSFER_REQUEST_ITEM C, ACCESS_POINT E, ITEM Z  "
				+ "WHERE A.REQUESTID = C.REQUESTID  "
				+ "AND A.FROMID = C.BRANCHID  "
				+ "AND A.FROMID = E.ACCESSID  "
				+ "AND C.ITEMID = Z.ITEMID  "
				+ "AND A.TOID = ? "
				+ "HAVING (QTY - SENTQTY) > 0 ORDER BY C.ITEMID;";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			stmt.setString(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(!itemId.equals(rs.getString("ITEMID"))){
					if(!itemId.equals("")){
						item.setQty(total);
						item.setSentQty(sentTotal);
						masterDtls.put(itemId, item);
						formData.put(itemId, orders);
					}
					itemId = rs.getString("ITEMID");
					item = new SalesItemModel();
					item.getItem().setItemId(rs.getInt("ITEMID"));
					item.getItem().setItemName(rs.getString("ITEMNAME"));
					item.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
					orders = new ArrayList<EntryModel>(0);
					total = 0;
					sentTotal = 0;
				}
				
				model = new EntryModel();
				model.setEntryId(rs.getInt("ORDERID")); 
				model.setEntryDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX")+"O");
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setQty(rs.getDouble("QTY"));
				model.setSentQty(rs.getDouble("SENTQTY"));
				total = total + rs.getDouble("QTY");
				sentTotal = sentTotal + rs.getDouble("SENTQTY");
				orders.add(model);
			}
			
			if(item != null){
				item.setQty(total);
				masterDtls.put(itemId, item);
				formData.put(itemId, orders);
			}
			
			stmt = getConnection().prepareStatement(requestBills);
			stmt.setString(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(masterDtls.get(rs.getString("ITEMID")) == null){
					itemId = rs.getString("ITEMID");
					item = new SalesItemModel();
					item.getItem().setItemId(rs.getInt("ITEMID"));
					item.getItem().setItemName(rs.getString("ITEMNAME"));
					item.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
					orders = new ArrayList<EntryModel>(0);
					total = 0;
					sentTotal = 0;
				} else {
					item = masterDtls.get(rs.getString("ITEMID"));
					total = item.getQty();
					sentTotal = item.getSentQty();
					orders = formData.get(rs.getString("ITEMID"));
				}
				itemId = rs.getString("ITEMID");
				model = new EntryModel();
				model.setEntryId(rs.getInt("REQUESTID"));
				model.setEntryDate(rs.getString("REQUESTDATE"));
				model.getBranch().setAccessId(rs.getInt("FROMID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX")+"TR");
				model.getBranch().setAccessName(rs.getString("NAME"));
				model.getBranch().setCity(rs.getString("CITY"));
				model.setQty(rs.getDouble("QTY"));
				model.setSentQty(rs.getDouble("SENTQTY"));
				total = total + rs.getDouble("QTY");
				sentTotal = sentTotal + rs.getDouble("SENTQTY");
				orders.add(model);
				item.setQty(total);
				item.setSentQty(sentTotal);
				masterDtls.remove(itemId);
				masterDtls.put(itemId, item);
				formData.remove(itemId);
				formData.put(itemId, orders);
			}
			
			Iterator<String> itr = formData.keySet().iterator();

			while(itr.hasNext()){
				itemId = itr.next();
				orders = formData.get(itemId);
				Collections.sort(orders, new LedgerDateComparator());
				formData.put(itemId, orders);
			}
			
			if(masterDtls.size() == 0){
				reportBills = null;
			} else {
				reportBills.put(FORM_DATA, formData);
				reportBills.put("MASTER_DTLS", masterDtls);
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
		return reportBills;
	}
	
	public HashMap<Integer, Double> getCurrentStockDtls(String accessId){
		HashMap<Integer, Double> items = new HashMap<Integer, Double>(0);
		
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
		int itemId = 0;
		double qty = 0;

		// query to get all the existing item group
		String getItems = "SELECT A.ITEMID, A.ITEMNUMBER, A.ITEMNAME, A.ITEMLABEL, A.PRICE, B.DISCOUNT, B.FROMDATE, B.TODATE, A.WPRICE FROM ITEM A LEFT JOIN ITEM_DISCOUNT B ON A.ITEMID = B.ITEMID AND B.TODATE >= CURRENT_DATE WHERE A.STATUS = 'A' ORDER BY A.ITEMNUMBER";

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
			
			stmt2 = getConnection().prepareStatement(getItems);
			stmt3 = getConnection().prepareStatement(getPurchaseQty);
			stmt4 = getConnection().prepareStatement(getTransferInQty);
			stmt5 = getConnection().prepareStatement(getSalesReturnQty);
			stmt6 = getConnection().prepareStatement(getSoldQty);
			stmt7 = getConnection().prepareStatement(getTranferOutQty);
			stmt8 = getConnection().prepareStatement(getPurchaseReturnQty);
			stmt9 = getConnection().prepareStatement(getOpeningQty);
			stmt10 = getConnection().prepareStatement(getSalesReturnedQty);
					
			rs2 = stmt2.executeQuery();
			
			while(rs2.next()){		
				qty = 0;
				itemId = rs2.getInt("ITEMID");
				
				stmt10.setInt(1, itemId);
				stmt10.setString(2, accessId);
				
				rs3 = stmt10.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty + rs3.getDouble(1);
				}
				
				stmt9.setInt(1, itemId);
				stmt9.setString(2, accessId);
				
				rs3 = stmt9.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty + rs3.getDouble(1);
				}
				
				stmt3.setInt(1, itemId);
				stmt3.setString(2, accessId);
				
				rs3 = stmt3.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty + rs3.getDouble(1);
				}
				
				stmt4.setInt(1, itemId);
				stmt4.setString(2, accessId);
				
				rs3 = stmt4.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty + rs3.getDouble(1);
				}
				
				stmt5.setInt(1, itemId);
				stmt5.setString(2, accessId);
				
				rs3 = stmt5.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty + rs3.getDouble(1);
				}
				
				stmt6.setInt(1, itemId);
				stmt6.setString(2, accessId);
				
				rs3 = stmt6.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty - rs3.getDouble(1);
				}
				
				stmt7.setInt(1, itemId);
				stmt7.setString(2, accessId);
				
				rs3 = stmt7.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty - rs3.getDouble(1);
				}
				
				stmt8.setInt(1, itemId);
				stmt8.setString(2, accessId);
				
				rs3 = stmt8.executeQuery();
				
				if(rs3.next() && rs3.getString(1) != null){
					qty = qty - rs3.getDouble(1);
				}
				
				items.put(itemId, qty);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
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

	public OrderModel[] getFollowupOrderDetails(String accessId) {
		ArrayList<OrderModel> todayBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.ORDERID, A.ORDERDATE, D.BILL_PREFIX, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, SUM(C.SENTQTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS SENTAMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, Z.ALERTDATE, Z.ALERTREMARK, A.CNT, Y.USERNAME AS FOLLOWUPUSER, COALESCE(P.PAMT,0) AS PAYAMT " +
				"FROM ORDER_ITEM C, CUSTOMER B, ACCESS_POINT D, ORDER_MASTER A " +
				"LEFT OUTER JOIN ORDER_ALERT Z ON A.BRANCHID = Z.BRANCHID AND A.ORDERID = Z.ORDERID AND A.CNT = Z.SNO  LEFT OUTER JOIN USER Y ON Z.USER = Y.USERID  " +
				"LEFT OUTER JOIN (SELECT M.FROMNO, M.FROMBRANCH, SUM(M.AMOUNT) AS PAMT FROM BILL_TO_BILL_ADJUST M WHERE M.TYPE = 1 GROUP BY M.FROMBRANCH, M.FROMNO) P " +
				" ON A.ORDERID = P.FROMNO AND A.BRANCHID = P.FROMBRANCH " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = D.ACCESSID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.BRANCHID = ? " +
				"AND A.STATUS = 0 " +
				"GROUP BY A.ORDERID " +
				"ORDER BY A.ORDERID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setSentAmt(rs.getDouble("SENTAMT"));
				model.setUsedAdvance(rs.getDouble("PAYAMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setFollowupCnt(rs.getInt("CNT"));
				model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") : rs.getString("ORDERDATE")); 
				model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
				model.setFollowupUser((rs.getString("FOLLOWUPUSER") != null) ? rs.getString("FOLLOWUPUSER") :""); 
				todayBills.add(model);
			}
			
			Collections.sort(todayBills, new OrderAlertDateComparator());
			
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
		
		return (OrderModel[]) todayBills.toArray(new OrderModel[0]);
	}

	public void addFollowupRemarks(String billNos, String followupDt,
			String followupRemark, String userId) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		
		int idx = 1;
		int branchId = 0;
		
		// query to get the details
		String getMaxCount = "SELECT BRANCHID, CNT FROM ORDER_MASTER WHERE ORDERID = ? AND BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) ";
		
		// query to update the count in sales
		String updateSalesCount = "UPDATE ORDER_MASTER SET CNT = CNT + 1 WHERE ORDERID = ? AND BRANCHID = ? ";
		
		// query to insert the alert record 
		String insertFollowupRecord = "INSERT INTO ORDER_ALERT (ORDERID, BRANCHID, ALERTDATE, ALERTREMARK, SNO, USER) VALUES (?,?,?,?,?,?)";
		
		try{
		
			stmt = getConnection().prepareStatement(getMaxCount);
			stmt1 = getConnection().prepareStatement(updateSalesCount);
			stmt2 = getConnection().prepareStatement(insertFollowupRecord);
			
//			for(int i=0; i<billNos.length; i++){
			
			stmt.setInt(1, Integer.parseInt(billNos.substring(4)));
			stmt.setString(2, billNos.substring(0,3));
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				idx = rs.getInt("CNT") + 1;
				branchId = rs.getInt("BRANCHID");
			}

			stmt1.setInt(1, Integer.parseInt(billNos.substring(4)));
			stmt1.setInt(2, branchId);
			
			stmt1.execute();
			
			stmt2.setInt(1, Integer.parseInt(billNos.substring(4)));
			stmt2.setInt(2, branchId);
			stmt2.setString(3, Utils.convertToSQLDate(followupDt));
			stmt2.setString(4, followupRemark);
			stmt2.setInt(5, idx);
			stmt2.setString(6, userId);
			
			stmt2.execute();
//			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex) {
			}
		}
		
		return;
	}

	public OrderModel[] getFollowupDetails(String billNo) {
		ArrayList<OrderModel> followupLst = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String followupDtls = "SELECT A.SNO, B.USERNAME, A.ORDERID, C.BILL_PREFIX, A.ALERTDATE, A.ALERTREMARK FROM ORDER_ALERT A, USER B, ACCESS_POINT C " +
				"WHERE A.USER = B.USERID " +
				"AND A.BRANCHID = C.ACCESSID " +
				"AND A.ORDERID = ? " +
				"AND A.BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) " +
				"ORDER BY A.SNO";
		
		try{
			
			stmt = getConnection().prepareStatement(followupDtls);
			stmt.setString(1, billNo.substring(4));
			stmt.setString(2, billNo.substring(0,3));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
					model = new OrderModel();
					model.setOrderId(rs.getInt("ORDERID"));
					model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
					model.setFollowupCnt(rs.getInt("SNO"));
					model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") :""); 
					model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
					model.setFollowupUser((rs.getString("USERNAME") != null) ? rs.getString("USERNAME") :""); 
					followupLst.add(model);
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
		return (OrderModel[]) followupLst.toArray(new OrderModel[0]);
	}

	public CustomerModel[] getCustomer() {
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.CITY, A.AREA, A.CUSTOMERTYPE, A.CONTACTPERSON, A.PHONE1, A.MOBILE1, A.TIN, A.CST, A.TRANSPORT, B.STATE FROM CUSTOMER A, CITY B WHERE A.CITY=B.CITY AND STATUS = 'A' " +
				"AND A.CUSTOMERID IN (SELECT DISTINCT(CUSTID) FROM ORDER_MASTER) ORDER BY TRIM(A.CUSTOMERNAME), A.AREA, A.CITY";
		
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

	public EntryModel[] getAdvancePayDtls(String billNo) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT A.BRANCH, A.DAY, B.TYPE, B.AMOUNT, B.CHQNO, B.BANKNAME, B.CUSTBANK, B.BRANCH AS BILLBRANCH, B.BILLNO " +
				"FROM BRANCH_DAY_BOOK_MASTER A, BRANCH_DAY_BOOK_ENTRIES B " +
				"WHERE A.ID = B.ID " +
				"AND B.BRANCH = (SELECT C.ACCESSID FROM ACCESS_POINT C WHERE C.BILL_PREFIX = ?) " +
				"AND B.TYPE IN (1,2,3,4) " +
				"AND B.BILLNO = ? " +
				"ORDER BY A.DAY ";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setString(1, billNo.substring(0,3));
			stmt.setInt(2, Integer.parseInt(billNo.substring(4)));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("DAY"));
				entry.setBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
				entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BILLBRANCH")));
				entry.setBillNo(rs.getInt("BILLNO"));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setBankName(rs.getString("BANKNAME"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

	public OrderModel[] getFollowupExhibitionOrderDetails() {
		ArrayList<OrderModel> todayBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.ORDERID, A.ORDERDATE, D.BILL_PREFIX, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, A.STATUS, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, Z.ALERTDATE, Z.ALERTREMARK, A.CNT, Y.USERNAME AS FOLLOWUPUSER " +
				"FROM ORDER_ITEM C, CUSTOMER B, ACCESS_POINT D, ORDER_MASTER A " +
				"LEFT OUTER JOIN ORDER_ALERT Z ON A.BRANCHID = Z.BRANCHID AND A.ORDERID = Z.ORDERID AND A.CNT = Z.SNO  LEFT OUTER JOIN USER Y ON Z.USER = Y.USERID  " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = D.ACCESSID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.FROMEX = 1 " +
				"GROUP BY A.ORDERID " +
				"ORDER BY A.ORDERID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setFollowupCnt(rs.getInt("CNT"));
				model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") : rs.getString("ORDERDATE")); 
				model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
				model.setFollowupUser((rs.getString("FOLLOWUPUSER") != null) ? rs.getString("FOLLOWUPUSER") :""); 
				model.setStatus(rs.getInt("STATUS"));
				todayBills.add(model);
			}
			
			Collections.sort(todayBills, new OrderAlertDateComparator());
			
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
		
		return (OrderModel[]) todayBills.toArray(new OrderModel[0]);
	}

	public void updateSentEmail(String billNo) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String accessId = "";

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";
		
		// query to insert the bill details
		String updateBillDetails = "UPDATE ORDER_MASTER SET ISEMAIL = 1  " +
				"WHERE ORDERID = ? AND BRANCHID = ?";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, billNo.substring(0,3));
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);

				stmt.setString(1, billNo.substring(4));
				stmt.setString(2, accessId);
				
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
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public void completeOrder(String orderno) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String accessId = "";

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";
		
		// query to check the order status
		String updateOrderItems = "UPDATE ORDER_ITEM SET SENTQTY = QTY WHERE ORDERID = ? AND BRANCHID = ?";
		
		// query to check the order status
		String updateOrderStatus = "UPDATE ORDER_MASTER SET STATUS = 2 WHERE ORDERID = ? AND BRANCHID = ?";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, orderno.substring(0,3));
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateOrderItems);
				stmt.setString(1, orderno.substring(4));
				stmt.setString(2, accessId);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateOrderStatus);
				stmt.setString(1, orderno.substring(4));
				stmt.setString(2, accessId);
				
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
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public MessageModel cancelOrder(String orderno) {

		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String accessId = "";

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";
		
		// query to check the order status
		String updateOrderStatus = "UPDATE ORDER_MASTER SET STATUS = 1 WHERE ORDERID = ? AND BRANCHID = ?";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, orderno.substring(0,3));
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
					
				stmt = getConnection().prepareStatement(updateOrderStatus);
				stmt.setString(1, orderno.substring(4));
				stmt.setString(2, accessId);
				
				stmt.execute();
				
				getConnection().commit();
				
				msg.addNewMessage(new Message(SUCCESS, "Order cancelled successfully!!"));
				
		} catch (Exception e) {
			msg.addNewMessage(new Message(ERROR, "Error occured while cancelling the order."));
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
		
		return msg;
	}

	public void adjustPartBill(String orderno, ArrayList<SalesItemModel> items) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		SalesItemModel item = null;
		
		String accessId = "";
		boolean isComplete = true;

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";
		
		// query to insert the bill details
		String updateBillDetails = "UPDATE ORDER_ITEM SET SENTQTY = SENTQTY + ?  " +
				"WHERE ORDERID = ? AND BRANCHID = ? AND ITEMID = ?";
		
		// query to check the order status
		String getOrderItems = "SELECT ITEMID, (QTY-SENTQTY) FROM ORDER_ITEM WHERE ORDERID = ? AND BRANCHID = ?";
		
		// query to check the order status
		String updateOrderStatus = "UPDATE ORDER_MASTER SET STATUS = 2 WHERE ORDERID = ? AND BRANCHID = ?";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, orderno.substring(0,3));
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				Iterator<SalesItemModel> itr = items.iterator();
				
				while(itr.hasNext()){
					item = itr.next();
					
					stmt.setDouble(1, item.getQty());
					stmt.setString(2, orderno.substring(4));
					stmt.setString(3, accessId);
					stmt.setInt(4, item.getItem().getItemId());
					
					stmt.addBatch();
				}
				
				stmt.executeBatch();
				
				stmt = getConnection().prepareStatement(getOrderItems);
				stmt.setString(1, orderno.substring(4));
				stmt.setString(2, accessId);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					if(rs.getInt(2)>0){
						isComplete = false;
						break;
					}
				}
				
				if(isComplete){
					stmt = getConnection().prepareStatement(updateOrderStatus);
					stmt.setString(1, orderno.substring(4));
					stmt.setString(2, accessId);
					
					stmt.execute();
				}
				
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
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public void addTransferFollowupRemarks(String billNos, String followupDt,
			String followupRemark, String userId) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		
		int idx = 1;
		int branchId = 0;
		
		// query to get the details
		String getMaxCount = "SELECT FROMID, CNT FROM TRANSFER_REQUEST WHERE REQUESTID = ? AND FROMID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) ";
		
		// query to update the count in sales
		String updateSalesCount = "UPDATE TRANSFER_REQUEST SET CNT = CNT + 1 WHERE REQUESTID = ? AND FROMID = ? ";
		
		// query to insert the alert record 
		String insertFollowupRecord = "INSERT INTO TRANSFER_REQUEST_ALERT (REQUESTID, BRANCHID, ALERTDATE, ALERTREMARK, SNO, USER) VALUES (?,?,?,?,?,?)";
		
		try{
		
			stmt = getConnection().prepareStatement(getMaxCount);
			stmt1 = getConnection().prepareStatement(updateSalesCount);
			stmt2 = getConnection().prepareStatement(insertFollowupRecord);
			
//			for(int i=0; i<billNos.length; i++){
			
			stmt.setInt(1, Integer.parseInt(billNos.substring(5)));
			stmt.setString(2, billNos.substring(0,3));
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				idx = rs.getInt("CNT") + 1; 
				branchId = rs.getInt("FROMID");
			}

			stmt1.setInt(1, Integer.parseInt(billNos.substring(5)));
			stmt1.setInt(2, branchId);
			
			stmt1.execute();
			
			stmt2.setInt(1, Integer.parseInt(billNos.substring(5)));
			stmt2.setInt(2, branchId);
			stmt2.setString(3, Utils.convertToSQLDate(followupDt));
			stmt2.setString(4, followupRemark);
			stmt2.setInt(5, idx);
			stmt2.setString(6, userId);
			
			stmt2.execute();
//			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex) {
			}
		}
		
		return;
	}

	public OrderModel[] getTransferFollowupDetails(String billNo) {
		ArrayList<OrderModel> followupLst = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String followupDtls = "SELECT A.SNO, B.USERNAME, A.REQUESTID, C.BILL_PREFIX, A.ALERTDATE, A.ALERTREMARK FROM TRANSFER_REQUEST_ALERT A, USER B, ACCESS_POINT C " +
				"WHERE A.USER = B.USERID " +
				"AND A.BRANCHID = C.ACCESSID " +
				"AND A.REQUESTID = ? " +
				"AND A.BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) " +
				"ORDER BY A.SNO";
		
		try{
			
			stmt = getConnection().prepareStatement(followupDtls);
			stmt.setString(1, billNo.substring(5));
			stmt.setString(2, billNo.substring(0,3));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
					model = new OrderModel();
					model.setOrderId(rs.getInt("REQUESTID"));
					model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
					model.setFollowupCnt(rs.getInt("SNO"));
					model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") :""); 
					model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
					model.setFollowupUser((rs.getString("USERNAME") != null) ? rs.getString("USERNAME") :""); 
					followupLst.add(model);
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
		return (OrderModel[]) followupLst.toArray(new OrderModel[0]);
	}

	public void adjustPartRequestBill(String orderno,
			ArrayList<TransferItemModel> items) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		TransferItemModel item = null;
		
		String accessId = "";
		boolean isComplete = true;

		String getAccessId = "SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX= ?";
		
		// query to insert the bill details
		String updateBillDetails = "UPDATE TRANSFER_REQUEST_ITEM SET SENTQTY = SENTQTY + ?  " +
				"WHERE REQUESTID = ? AND BRANCHID = ? AND ITEMID = ?";
		
		// query to check the order status
		String getOrderItems = "SELECT ITEMID, (QTY-SENTQTY) FROM TRANSFER_REQUEST_ITEM WHERE REQUESTID = ? AND BRANCHID = ?";
		
		// query to check the order status
		String updateOrderStatus = "UPDATE TRANSFER_REQUEST SET STATUS = 2 WHERE REQUESTID = ? AND FROMID = ?";
		
		try {
				
				// executing the query to get the next bill no
				stmt = getConnection().prepareStatement(getAccessId);
				stmt.setString(1, orderno.substring(0,3));
				
				rs = stmt.executeQuery();
				
				if(rs.next()){
					accessId = rs.getString(1);
				}
					
				// executing the query to insert bill and bill item details
				stmt = getConnection().prepareStatement(updateBillDetails);
				
				Iterator<TransferItemModel> itr = items.iterator();
				
				while(itr.hasNext()){
					item = itr.next();
					
					stmt.setDouble(1, item.getQty());
					stmt.setString(2, orderno.substring(5));
					stmt.setString(3, accessId);
					stmt.setInt(4, item.getItem().getItemId());
					
					stmt.addBatch();
				}
				
				stmt.executeBatch();
				
				stmt = getConnection().prepareStatement(getOrderItems);
				stmt.setString(1, orderno.substring(5));
				stmt.setString(2, accessId);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					if(rs.getInt(2)>0){
						isComplete = false;
						break;
					}
				}
				
				if(isComplete){
					stmt = getConnection().prepareStatement(updateOrderStatus);
					stmt.setString(1, orderno.substring(5));
					stmt.setString(2, accessId);
					
					stmt.execute();
				}
				
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
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public OrderModel[] getSalesmanOrderDetails(UserModel salesman) {
		ArrayList<OrderModel> todayBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, Z.BILL_PREFIX, A.ISEMAIL " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, ACCESS_POINT Z " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = Z.ACCESSID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.SALESMANID = ? " +
				"AND A.STATUS = 0 " +
				"GROUP BY A.ORDERID " +
				"ORDER BY A.ORDERID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, salesman.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				if(rs.getInt("ISEMAIL") == 1){
					model.setEmail();
				}
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
		
		return (OrderModel[]) todayBills.toArray(new OrderModel[0]);
	}

	public OrderModel[] getFollowupSalesmanOrderDetails(UserModel salesman) {
		ArrayList<OrderModel> todayBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.ORDERID, A.ORDERDATE, D.BILL_PREFIX, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADVANCE, Z.ALERTDATE, Z.ALERTREMARK, A.CNT, Y.USERNAME AS FOLLOWUPUSER " +
				"FROM ORDER_ITEM C, CUSTOMER B, ACCESS_POINT D, ORDER_MASTER A " +
				"LEFT OUTER JOIN ORDER_ALERT Z ON A.BRANCHID = Z.BRANCHID AND A.ORDERID = Z.ORDERID AND A.CNT = Z.SNO  LEFT OUTER JOIN USER Y ON Z.USER = Y.USERID  " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = D.ACCESSID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.SALESMANID = ? " +
				"GROUP BY A.ORDERID " +
				"ORDER BY A.ORDERID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, salesman.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setFollowupCnt(rs.getInt("CNT"));
				model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") : rs.getString("ORDERDATE")); 
				model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
				model.setFollowupUser((rs.getString("FOLLOWUPUSER") != null) ? rs.getString("FOLLOWUPUSER") :""); 
				todayBills.add(model);
			}
			
			Collections.sort(todayBills, new OrderAlertDateComparator());
			
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
		
		return (OrderModel[]) todayBills.toArray(new OrderModel[0]);
	}

	public HashMap<String, ArrayList<OrderModel>> getFollowupCustomerDetails(String accessId) {
		HashMap<String, ArrayList<OrderModel>> result = new HashMap<String, ArrayList<OrderModel>>(0);
		ArrayList<OrderModel> reportBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.STATUS, " +
				"A.TAXTYPE, SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, D.USERNAME, A.FROMEX, A.ADVANCE " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.STATUS = 0 " +
				"AND A.CUSTID = B.CUSTOMERID ";
		outstandingBill	= outstandingBill +	"AND A.BRANCHID = ? ";
		outstandingBill	= outstandingBill +	"GROUP BY A.ORDERID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY B.CUSTOMERNAME, A.CUSTID, A.ORDERDATE, A.BRANCHID, A.ORDERID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			stmt.setString(1, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(result.get(rs.getString("CUSTOMERNAME")+"-"+rs.getString("AREA")+"-"+rs.getString("CITY")) != null){
					reportBills = result.get(rs.getString("CUSTOMERNAME")+"-"+rs.getString("AREA")+"-"+rs.getString("CITY"));
				} else {
					reportBills = new ArrayList<OrderModel>(0);
				}
				model = new OrderModel();
				model.setOrderId(rs.getInt("ORDERID"));
				model.setOrderDate(rs.getString("ORDERDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				if(rs.getInt("FROMEX") == 1){
					model.setFromEx(1);
				}
				model.setAdvance(rs.getDouble("ADVANCE"));
				model.getSalesman().setUserName(rs.getString("USERNAME"));
				model.setStatus(rs.getInt("STATUS"));
				reportBills.add(model);
				result.put(rs.getString("CUSTOMERNAME")+"-"+rs.getString("AREA")+"-"+rs.getString("CITY"), reportBills);
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
		return result;
	}

}
