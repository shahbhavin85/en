package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.en.model.ItemModel;
import com.en.model.OrderModel;
import com.en.model.SalesItemModel;

public class OrderReportBroker extends BaseBroker {

	public OrderReportBroker(Connection conn) {
		super(conn);
	}

	public OrderModel[] getMasterRpt(String customer, String salesman,
			String[] branch, String fromdt, String todt, String[] billtype, boolean onlyExhibition) {
		ArrayList<OrderModel> reportBills = new ArrayList<OrderModel>(0);
		OrderModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, A.STATUS, " +
				"A.TAXTYPE, SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, D.USERNAME, A.FROMEX, A.ADVANCE " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.CUSTID = ? ";
		}
		if(!salesman.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SALESMANID = ? ";
		}
		if(onlyExhibition){
			outstandingBill	= outstandingBill +	"AND A.FROMEX = 1 ";
		}
		if(branch.length!=0 && !branch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<branch.length; k++){
				outstandingBill	= outstandingBill +	"A.BRANCHID = ? ";
				if(k !=branch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(billtype.length !=3){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<billtype.length; k++){
				outstandingBill	= outstandingBill +	"A.TAXTYPE = ? ";
				if(k !=billtype.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.ORDERDATE >= ? AND A.ORDERDATE <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"GROUP BY A.ORDERID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY A.ORDERDATE, A.BRANCHID, A.ORDERID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(!salesman.equals("")){
				stmt.setString(i, salesman);
				i++;
			}
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
					i++;
				}
			}
			if(billtype.length !=3){
				for(int k=0; k<billtype.length; k++){
					stmt.setString(i, billtype[k]);
					i++;
				}
			}
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
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
		return (OrderModel[]) reportBills.toArray(new OrderModel[0]);
	}

	public HashMap<String, Object> getItemRpt(String customer, String salesman,
			String[] branch, String fromdt, String todt, String[] billtype,
			boolean onlyExhibition) {
		HashMap<String, Object> reportBills = new HashMap<String, Object>(0);
		HashMap<String, SalesItemModel> masterDtls = new HashMap<String, SalesItemModel>(0);
		HashMap<String, OrderModel[]> formData = new HashMap<String, OrderModel[]>(0);
		SalesItemModel item = null;
		ArrayList<OrderModel> orders = null;
		OrderModel model = null;
		String itemId = "";
		ItemModel itm = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;
		double total = 0;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.ORDERID, A.ORDERDATE, A.BRANCHID, E.BILL_PREFIX, A.CUSTID, B.CUSTOMERNAME, B.AREA, B.CITY, " +
				"A.TAXTYPE, C.QTY, C.ITEMID, Z.ITEMNAME, Z.ITEMNUMBER " +
				"FROM ORDER_MASTER A, ORDER_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E, ITEM Z " +
				"WHERE A.ORDERID = C.ORDERID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND C.ITEMID = Z.ITEMID " +
				"AND A.CUSTID = B.CUSTOMERID ";
		
		if(!customer.equals("")){
			outstandingBill	= outstandingBill +	"AND A.CUSTID = ? ";
		}
		if(!salesman.equals("")){
			outstandingBill	= outstandingBill +	"AND A.SALESMANID = ? ";
		}
		if(onlyExhibition){
			outstandingBill	= outstandingBill +	"AND A.FROMEX = 1 ";
		}
		if(branch.length!=0 && !branch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<branch.length; k++){
				outstandingBill	= outstandingBill +	"A.BRANCHID = ? ";
				if(k !=branch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(billtype.length !=3){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<billtype.length; k++){
				outstandingBill	= outstandingBill +	"A.TAXTYPE = ? ";
				if(k !=billtype.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.ORDERDATE >= ? AND A.ORDERDATE <= ? ";
		}
		

//		outstandingBill	= outstandingBill +	"GROUP BY A.ORDERID, A.BRANCHID ";
		outstandingBill	= outstandingBill +	"ORDER BY C.ITEMID, A.ORDERID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(!salesman.equals("")){
				stmt.setString(i, salesman);
				i++;
			}
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
					i++;
				}
			}
			if(billtype.length !=3){
				for(int k=0; k<billtype.length; k++){
					stmt.setString(i, billtype[k]);
					i++;
				}
			}
			if(!fromdt.equals("") && !todt.equals("")){
				stmt.setString(i, fromdt);
				i++;
				stmt.setString(i, todt);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(!itemId.equals(rs.getString("ITEMID"))){
					if(!itemId.equals("")){
						item.setQty(total);
						masterDtls.put(itemId, item);
						formData.put(itemId, (OrderModel[])orders.toArray(new OrderModel[0]));
					}
					itemId = rs.getString("ITEMID");
					item = new SalesItemModel();
					item.getItem().setItemId(rs.getInt("ITEMID"));
					item.getItem().setItemName(rs.getString("ITEMNAME"));
					item.getItem().setItemNumber(rs.getString("ITEMNUMBER"));
					orders = new ArrayList<OrderModel>(0);
					total = 0;
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
				itm = new ItemModel();
				model.addItem(itm, "", rs.getDouble("QTY"), 0.00, 0.00);
				total = total + rs.getDouble("QTY");
				orders.add(model);
			}
			
			if(item != null){
				item.setQty(total);
				masterDtls.put(itemId, item);
				formData.put(itemId, (OrderModel[])orders.toArray(new OrderModel[0]));
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

}
