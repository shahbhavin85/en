package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.QuotationModel;
import com.en.model.UserModel;
import com.en.util.QuotationAlertDateComparator;
import com.en.util.Utils;

public class QuotationBroker extends BaseBroker {

	public QuotationBroker(Connection conn) {
		super(conn);
	}

	public synchronized MessageModel addQuotation(QuotationModel model) {
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
		String maxBillNo = "SELECT MAX(QUOTATIONID) FROM QUOTATION WHERE BRANCHID = ?";

		String getBillPrefix = "SELECT BILL_PREFIX FROM ACCESS_POINT WHERE ACCESSID= ?";
		
		// query to insert the bill details
		String insertBillDetails = "INSERT INTO QUOTATION (QUOTATIONID, QUOTATIONDATE, BRANCHID, CUSTID, TAXTYPE, SALESMANID, REMARKS, PACKING, FORWARD, INSTALLATION, LESS, VALIDTILL, SAMPLE, FROMEX) " +
				"VALUES (?,CURRENT_DATE,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO QUOTATION_ITEM (QUOTATIONID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
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
				stmt.setString(11, model.getValidDate());
				stmt.setInt(12, model.getSample());
				stmt.setInt(13, model.getFromEx());
				
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
				
				msg = new Message(SUCCESS, "Quotation Generated successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, billPrefix + "Q" +salesId);
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while inserting quotation details!!");
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

	public QuotationModel getQuotationDetails(String billNo) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		
		QuotationModel model = new QuotationModel();
		
		String billPrefix = billNo.substring(0,3);
		String billNumb = billNo.substring(4);
		
		// query to get the branch details
		String getBranchDetails = "SELECT ACCESSID, BILL_PREFIX, NAME, ADDRESS, CITY, PINCODE, VAT, CST, STDCODE, PHONE1, PHONE2, EMAIL, WITHTIN, NOTIN FROM ACCESS_POINT WHERE BILL_PREFIX = ? ";
			
		// query to get the bill no details
		String getBillDetails = "SELECT A.QUOTATIONID, A.QUOTATIONDATE, A.CUSTID, B.CONTACTPERSON, B.AREA, B.CUSTOMERTYPE, B.CUSTOMERNAME, B.ADDRESS, B.CITY, C.STATE, B.PINCODE, B.STDCODE, B.PHONE1, B.PHONE2, " +
				"B.TIN, B.CST, B.MOBILE1, B.MOBILE2, B.EMAIL, B.EMAIL1, A.TAXTYPE, A.SALESMANID, A.REMARKS, A.ISLOCK, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.SAMPLE, A.VALIDTILL, A.STATUS, A.ORDERNO " +
				"FROM QUOTATION A, CUSTOMER B, CITY C WHERE B.CITY = C.CITY AND A.QUOTATIONID = ? AND A.BRANCHID = ? AND A.CUSTID = B.CUSTOMERID";
		
		// query to get the items of the bill no
		String getItemDetails = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, B.QTY, B.RATE, B.TAX, B.DESP, B.DISRATE " +
				"FROM ITEM A, QUOTATION_ITEM B " +
				"WHERE A.ITEMID = B.ITEMID " +
				"AND B.QUOTATIONID = ? " +
				"AND B.BRANCHID = ?";
		
		// query to get the salesman name
		String getSalesmanName = "SELECT USERNAME, MOBILE1, MOBILE2 FROM USER WHERE USERID = ?";
		
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
				model.setQuotationId(rs.getInt("QUOTATIONID"));
				model.setOrderNo(rs.getString("ORDERNO"));
				model.setQuotationdate(rs.getString("QUOTATIONDATE"));
				model.setTaxtype(rs.getInt("TAXTYPE"));
				model.setRemarks(rs.getString("REMARKS"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setLess(rs.getDouble("LESS"));
				model.setValidDate(rs.getString("VALIDTILL"));
				model.setStatus(rs.getString("STATUS"));
				model.setSample(rs.getInt("SAMPLE"));
				model.setLock(rs.getString("ISLOCK").equals("Y"));
				if(rs.getString("SALESMANID") != null){
					model.getSalesman().setUserId(rs.getString("SALESMANID"));
					stmt1 = getConnection().prepareStatement(getSalesmanName);
					stmt1.setString(1, rs.getString("SALESMANID"));
					rs1 = stmt1.executeQuery();
					while(rs1.next()){
						model.getSalesman().setUserName(rs1.getString("USERNAME"));
						model.getSalesman().setMobile1(rs1.getString("MOBILE1"));
						model.getSalesman().setMobile2(rs1.getString("MOBILE2"));
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
	
	public QuotationModel[] getExhibitionQuotationDetails(){
		ArrayList<QuotationModel> todayBills = new ArrayList<QuotationModel>(0);
		QuotationModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.QUOTATIONID, A.QUOTATIONDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.VALIDTILL, A.SAMPLE, Z.BILL_PREFIX " +
				"FROM QUOTATION A, QUOTATION_ITEM C, CUSTOMER B, ACCESS_POINT Z " +
				"WHERE A.QUOTATIONID = C.QUOTATIONID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.BRANCHID = Z.ACCESSID " +
				"AND A.FROMEX = 1 " +
				"GROUP BY A.QUOTATIONID " +
				"ORDER BY A.QUOTATIONID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new QuotationModel();
				model.setQuotationId(rs.getInt("QUOTATIONID"));
				model.setQuotationdate(rs.getString("QUOTATIONDATE"));
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
				model.setValidDate(rs.getString("VALIDTILL"));
				model.setSample(rs.getInt("SAMPLE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
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
		
		return (QuotationModel[]) todayBills.toArray(new QuotationModel[0]);
	}

	public QuotationModel[] getTodayQuotationDetails(String fromDate,
			String toDate, String accessId) {
		ArrayList<QuotationModel> todayBills = new ArrayList<QuotationModel>(0);
		QuotationModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.QUOTATIONID, A.QUOTATIONDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.VALIDTILL, A.SAMPLE, Z.BILL_PREFIX " +
				"FROM QUOTATION A, QUOTATION_ITEM C, CUSTOMER B, ACCESS_POINT Z " +
				"WHERE A.QUOTATIONID = C.QUOTATIONID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.BRANCHID = Z.ACCESSID " +
				"AND A.QUOTATIONDATE >= ? " +
				"AND A.QUOTATIONDATE <= ? " +
				"AND A.BRANCHID = ? " +
				"GROUP BY A.QUOTATIONID " +
				"ORDER BY A.QUOTATIONID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, fromDate);
			stmt.setString(2, toDate);
			stmt.setString(3, accessId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new QuotationModel();
				model.setQuotationId(rs.getInt("QUOTATIONID"));
				model.setQuotationdate(rs.getString("QUOTATIONDATE"));
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
				model.setValidDate(rs.getString("VALIDTILL"));
				model.setSample(rs.getInt("SAMPLE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
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
		
		return (QuotationModel[]) todayBills.toArray(new QuotationModel[0]);
	}

	public QuotationModel[] getFollowupQuotationDetails(String branchId) {
		ArrayList<QuotationModel> todayBills = new ArrayList<QuotationModel>(0);
		QuotationModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.QUOTATIONID, A.QUOTATIONDATE, D.BILL_PREFIX, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.SAMPLE, A.LESS, A.VALIDTILL, Z.ALERTDATE, Z.ALERTREMARK, A.CNT, Y.USERNAME AS FOLLOWUPUSER  " +
				"FROM QUOTATION_ITEM C, CUSTOMER B, ACCESS_POINT D, QUOTATION A " +
				"LEFT OUTER JOIN QUOTATION_ALERT Z ON A.BRANCHID = Z.BRANCHID AND A.QUOTATIONID = Z.QUOTATIONID AND A.CNT = Z.SNO  LEFT OUTER JOIN USER Y ON Z.USER = Y.USERID  " +
				"WHERE A.QUOTATIONID = C.QUOTATIONID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = D.ACCESSID " +
				"AND A.BRANCHID = ? " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.STATUS = 0 " +
				"GROUP BY A.QUOTATIONID " +
				"ORDER BY A.QUOTATIONID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, branchId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new QuotationModel();
				model.setQuotationId(rs.getInt("QUOTATIONID"));
				model.setQuotationdate(rs.getString("QUOTATIONDATE"));
				model.getBranch().setAccessId(rs.getInt("BRANCHID"));
				model.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				model.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				model.getCustomer().setArea(rs.getString("AREA"));
				model.getCustomer().setCity(rs.getString("CITY"));
				model.setTotalAmt(rs.getDouble("AMT"));
				model.setPacking(rs.getDouble("PACKING"));
				model.setInstallation(rs.getDouble("INSTALLATION"));
				model.setSample(rs.getInt("SAMPLE"));
				model.setForwarding(rs.getDouble("FORWARD"));
				model.setLess(rs.getDouble("LESS"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
				model.setValidDate((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") : rs.getString("VALIDTILL"));
				model.setFollowupCnt(rs.getInt("CNT"));
				model.setFollowupDt((rs.getString("ALERTDATE") != null) ? rs.getString("ALERTDATE") :""); 
				model.setFollowupRemark((rs.getString("ALERTREMARK") != null) ? rs.getString("ALERTREMARK") :""); 
				model.setFollowupUser((rs.getString("FOLLOWUPUSER") != null) ? rs.getString("FOLLOWUPUSER") :""); 
				todayBills.add(model);
			}
			
			Collections.sort(todayBills, new QuotationAlertDateComparator());
			
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
		
		return (QuotationModel[]) todayBills.toArray(new QuotationModel[0]);
	}

	public MessageModel editQuotation(QuotationModel model) {
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
		String updateBillDetails = "UPDATE QUOTATION SET CUSTID = ? , TAXTYPE = ?, SALESMANID = ? , REMARKS = ?, PACKING = ?, FORWARD = ?, INSTALLATION = ?, LESS = ?, VALIDTILL = ?, SAMPLE = ? " +
				"WHERE QUOTATIONID = ? AND BRANCHID = ?";
		
		// String get tax rate
		String getTaxRate = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3, CESS FROM TAX WHERE ACCESSID = ? ";
		
		// String get tax rate
		String getTaxSlab= "SELECT SLAB FROM ITEM_TAX WHERE ITEMID = ? AND ACCESSID = ? ";

		//query to insert the bill items
		String deleteBillItems = "DELETE FROM QUOTATION_ITEM WHERE QUOTATIONID = ? AND BRANCHID = ?";
		
		//query to insert the bill items
		String insertBillItems = "INSERT INTO QUOTATION_ITEM (QUOTATIONID, BRANCHID, ITEMID, QTY, RATE, TAX, DISRATE, DESP) " +
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
				stmt.setInt(1, model.getQuotationId());
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
				stmt.setString(9, model.getValidDate());
				stmt.setInt(10, model.getSample());
				stmt.setInt(11, model.getQuotationId());
				stmt.setString(12, accessId);
				
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
					
					stmt1.setLong(1, model.getQuotationId());
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
				
				msg = new Message(SUCCESS, "Quotation Edited successfully!!");
				msgs.addNewMessage(msg);
				msg = new Message(SUCCESS, model.getBranch().getBillPrefix() + "Q" + model.getQuotationId());
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while editing quotation details!!");
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

	public void addFollowupRemarks(String billNos, String followupDt,
			String followupRemark, String userId) {
		
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		
		int idx = 1;
		int branchId = 0;
		
		// query to get the details
		String getMaxCount = "SELECT BRANCHID, CNT FROM QUOTATION WHERE QUOTATIONID = ? AND BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) ";
		
		// query to update the count in sales
		String updateSalesCount = "UPDATE QUOTATION SET CNT = CNT + 1 WHERE QUOTATIONID = ? AND BRANCHID = ? ";
		
		// query to insert the alert record 
		String insertFollowupRecord = "INSERT INTO QUOTATION_ALERT (QUOTATIONID, BRANCHID, ALERTDATE, ALERTREMARK, SNO, USER) VALUES (?,?,?,?,?,?)";
		
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
				// TODO: handle exception
			}
		}
		
		return;
	}

	public QuotationModel[] getFollowupDetails(String billNo) {
		ArrayList<QuotationModel> followupLst = new ArrayList<QuotationModel>(0);
		QuotationModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String followupDtls = "SELECT A.SNO, B.USERNAME, A.QUOTATIONID, C.BILL_PREFIX, A.ALERTDATE, A.ALERTREMARK FROM QUOTATION_ALERT A, USER B, ACCESS_POINT C " +
				"WHERE A.USER = B.USERID " +
				"AND A.BRANCHID = C.ACCESSID " +
				"AND A.QUOTATIONID = ? " +
				"AND A.BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?) " +
				"ORDER BY A.SNO";
		
		try{
			
			stmt = getConnection().prepareStatement(followupDtls);
			stmt.setString(1, billNo.substring(4));
			stmt.setString(2, billNo.substring(0,3));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
					model = new QuotationModel();
					model.setQuotationId(rs.getInt("QUOTATIONID"));
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
		return (QuotationModel[]) followupLst.toArray(new QuotationModel[0]);
	}

	public void closeQuotation(String billNo) {
		
		PreparedStatement stmt = null;

		// query to clsoe the quotation
		String closeQuotationQuery = "UPDATE QUOTATION SET STATUS = 1 WHERE QUOTATIONID = ? AND BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?)";
		
		try{
			
			// executing the query to lock the sales bill
			stmt = getConnection().prepareStatement(closeQuotationQuery);
			stmt.setString(1, billNo.substring(4));
			stmt.setString(2, billNo.substring(0,3));
			
			stmt.execute();
			
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
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public void closeOrderQuotation(String billNo, int orderId) {
		
		PreparedStatement stmt = null;

		// query to clsoe the quotation
		String closeQuotationQuery = "UPDATE QUOTATION SET STATUS = 1, ORDERNO = ? WHERE QUOTATIONID = ? AND BRANCHID = (SELECT ACCESSID FROM ACCESS_POINT WHERE BILL_PREFIX = ?)";
		
		try{
			
			// executing the query to lock the sales bill
			stmt = getConnection().prepareStatement(closeQuotationQuery);
			stmt.setInt(1, orderId);
			stmt.setString(2, billNo.substring(4));
			stmt.setString(3, billNo.substring(0,3));
			
			stmt.execute();
			
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
				if(stmt != null)
					stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return;
	}

	public QuotationModel[] getSalesmanQuotationDetails(UserModel salesman) {
		ArrayList<QuotationModel> todayBills = new ArrayList<QuotationModel>(0);
		QuotationModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		// query to check the existence of the bill no
		String todayBillDtls = "SELECT A.QUOTATIONID, A.QUOTATIONDATE, A.BRANCHID, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.TAXTYPE, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.VALIDTILL, A.SAMPLE, Z.BILL_PREFIX " +
				"FROM QUOTATION A, QUOTATION_ITEM C, CUSTOMER B, ACCESS_POINT Z " +
				"WHERE A.QUOTATIONID = C.QUOTATIONID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.BRANCHID = Z.ACCESSID " +
				"AND A.SALESMANID = ? " +
				"AND A.STATUS = 0 " +
				"GROUP BY A.QUOTATIONID " +
				"ORDER BY A.QUOTATIONID";
		
		
		try{
			
			stmt = getConnection().prepareStatement(todayBillDtls);
			stmt.setString(1, salesman.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new QuotationModel();
				model.setQuotationId(rs.getInt("QUOTATIONID"));
				model.setQuotationdate(rs.getString("QUOTATIONDATE"));
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
				model.setValidDate(rs.getString("VALIDTILL"));
				model.setSample(rs.getInt("SAMPLE"));
				model.getBranch().setBillPrefix(rs.getString("BILL_PREFIX"));
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
		
		return (QuotationModel[]) todayBills.toArray(new QuotationModel[0]);
	}

}
