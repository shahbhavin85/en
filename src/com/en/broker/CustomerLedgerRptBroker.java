package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.en.model.EntryModel;
import com.en.util.LedgerDateComparator;
import com.en.util.Utils;

public class CustomerLedgerRptBroker extends BaseBroker {

	public CustomerLedgerRptBroker(Connection conn) {
		super(conn);
	}

	public EntryModel[] getLedgerRpt(String customer, String fromDt,
			String toDt) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		HashMap<Integer, EntryModel> temp = new HashMap<Integer, EntryModel>(0);
		double openBal = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double roundOff = 0;
		
		// query to get opening balance
		String getOpeningBal = "SELECT OPENBAL, OPENBALREMARKS FROM CUSTOMER WHERE CUSTOMERID = ?";
		
		// query to get the sales total
		String getOpenSalesDtls = "SELECT A.SALESID, A.BRANCHID, SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADJAMT " +
				"FROM SALES A, SALES_ITEM C " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = ? " +
				"AND A.SALESDATE < ? " +
				"GROUP BY A.SALESID, A.BRANCHID";
		
		// query to get the sales total
		String getOpenSalesReturnDtls = "SELECT A.CREDITNOTEID, A.BRANCHID, SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.LESS " +
				"FROM CREDIT_NOTE A, CREDIT_NOTE_ITEM C " +
				"WHERE A.CREDITNOTEID = C.CREDITNOTEID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.CUSTID = ? " +
				"AND A.NOTEDATE < ? " +
				"GROUP BY A.CREDITNOTEID, A.BRANCHID";
		
		// query to get the sales total
		String getOpenLabourSalesDtls = "SELECT SUM(AMOUNT) AS AMT, SUM(A.ADJAMT) AS ADJUSTAMT " +
				"FROM LABOUR_BILL A " +
				"WHERE A.CUSTID = ? " +
				"AND A.BILLDATE < ? ";
		
		// query to get the sales total
		String getOpenPurchaseDtls = "SELECT A.PURCHASEID, A.BRANCHID, SUM(C.QTY*((100+C.TAX)*C.RATE)/100) AS AMT, A.DISCOUNT, A.EXTRA " +
				"FROM PURCHASE A, PURCHASE_ITEM C " +
				"WHERE A.PURCHASEID = C.PURCHASEID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.SUPPID = ? " +
				"AND A.RECDDT < ? " +
				"GROUP BY A.PURCHASEID, A.BRANCHID";
		
		// query to get the sales total
		String getOpenPurchaseReturnDtls = "SELECT A.RETURNID, A.BRANCHID, SUM(C.QTY*((100+C.TAX)*C.RATE)/100) AS AMT " +
				"FROM PURCHASE_RETURN A, PURCHASE_RETURN_ITEM C " +
				"WHERE A.RETURNID = C.RETURNID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.SUPPID = ? " +
				"AND A.RETURNDATE < ? " +
				"GROUP BY A.RETURNID, A.BRANCHID";
		
		// query to get the details
		String getOpenDBDtls = "SELECT C.DAY, A.TYPE, A.CRDR, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CHQNO, A.CHQDT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C  WHERE A.ID = C.ID AND (A.TYPE = 81 OR A.TYPE = 11 OR A.TYPE = 46 OR A.TYPE = 47 OR A.TYPE = 48 OR A.TYPE = 49 OR A.TYPE = 1 OR A.TYPE = 2 OR A.TYPE = 3 OR A.TYPE = 4 OR A.TYPE = 5 OR A.TYPE = 21 OR A.TYPE = 22 OR A.TYPE = 23 OR A.TYPE = 24 OR A.TYPE = 31 OR A.TYPE = 32 OR A.TYPE = 41 OR A.TYPE = 42 OR A.TYPE = 43 OR A.TYPE = 44 OR A.TYPE = 55 OR A.TYPE = 71 OR A.TYPE = 72 OR A.TYPE = 73) AND C.DAY < ? AND A.CUSTOMER = ? AND A.CUSTOMER = B.CUSTOMERID ORDER BY C.DAY";
		
		// query to get the details
		String getDBDtls = "SELECT A.REFNO, C.DAY, A.TYPE, A.CRDR, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CHQNO, A.CHQDT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO, A.USER FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C  WHERE A.ID = C.ID AND (A.TYPE = 81 OR A.TYPE = 11 OR A.TYPE = 46 OR A.TYPE = 47 OR A.TYPE = 48 OR A.TYPE = 49 OR A.TYPE = 1 OR A.TYPE = 2 OR A.TYPE = 3 OR A.TYPE = 4 OR A.TYPE = 5 OR A.TYPE = 21 OR A.TYPE = 22 OR A.TYPE = 23 OR A.TYPE = 24 OR A.TYPE = 31 OR A.TYPE = 32 OR A.TYPE = 41 OR A.TYPE = 42 OR A.TYPE = 43 OR A.TYPE = 44 OR A.TYPE = 55 OR A.TYPE = 71 OR A.TYPE = 72 OR A.TYPE = 73) AND C.DAY >= ? AND C.DAY <= ? AND A.CUSTOMER = ? AND A.CUSTOMER = B.CUSTOMERID ORDER BY C.DAY, A.REFNO";

		// query to check the existence of the bill no
		String salesLst = "SELECT A.SALESID, A.SALESDATE, A.BRANCHID, E.BILL_PREFIX, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS, A.ADJAMT, A.PAYMODE, A.PAYDATE " +
				"FROM SALES A, SALES_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.CUSTID = ? " +
				"AND A.SALESDATE >= ? AND A.SALESDATE <= ? " + 
				"GROUP BY A.SALESID, A.BRANCHID " +
				"ORDER BY A.SALESDATE, A.BRANCHID, A.SALESID";

		// query to check the existence of the bill no
		String cnLst = "SELECT A.CREDITNOTEID, A.NOTEDATE, A.BRANCHID, E.BILL_PREFIX, " +
				"SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.LESS " +
				"FROM CREDIT_NOTE A, CREDIT_NOTE_ITEM C, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.CREDITNOTEID = C.CREDITNOTEID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.CUSTID = ? " +
				"AND A.NOTEDATE >= ? AND A.NOTEDATE <= ? " + 
				"GROUP BY A.CREDITNOTEID, A.BRANCHID " +
				"ORDER BY A.NOTEDATE, A.BRANCHID, A.CREDITNOTEID";

		// query to check the existence of the bill no
		String labourBillLst = "SELECT A.BILLNO, A.BILLDATE, A.BRANCHID, E.BILL_PREFIX, " +
				"A.AMOUNT AS AMT, A.ADJAMT, A.PAYMODE, A.PAYDATE " +
				"FROM LABOUR_BILL A, CUSTOMER B, USER D, ACCESS_POINT E " +
				"WHERE A.BRANCHID = E.ACCESSID " +
				"AND A.SALESMANID = D.USERID " +
				"AND A.CUSTID = B.CUSTOMERID " +
				"AND A.CUSTID = ? " +
				"AND A.BILLDATE >= ? AND A.BILLDATE <= ? " + 
				"GROUP BY A.BILLNO, A.BRANCHID " +
				"ORDER BY A.BILLDATE, A.BRANCHID, A.BILLNO";
		
		// query to get the bill no details
		String purchaseLst = "SELECT A.PURCHASEID, A.BRANCHID, A.RECDDT, A.INVDT, A.INVNO, E.BILL_PREFIX, " +
				"A.DISCOUNT, A.EXTRA , SUM(D.QTY*(((100+D.TAX)*D.RATE)/100)) AS AMT, A.ADJAMT, A.PAYDATE " +
				"FROM PURCHASE A, PURCHASE_ITEM D, ACCESS_POINT E " +
				"WHERE A.SUPPID = ? " +
				"AND A.RECDDT >= ? " +
				"AND A.RECDDT <= ? " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.BRANCHID = D.BRANCHID " +
				"AND A.PURCHASEID = D.PURCHASEID " +
				"GROUP BY A.PURCHASEID, A.BRANCHID " +
				"ORDER BY A.RECDDT";
		
		// query to get the bill no details
		String purchaseReturnLst = "SELECT A.RETURNID, A.BRANCHID, A.RETURNDATE,E.BILL_PREFIX, " +
				"SUM(D.QTY*(((100+D.TAX)*D.RATE)/100)) AS AMT " +
				"FROM PURCHASE_RETURN A, PURCHASE_RETURN_ITEM D, ACCESS_POINT E " +
				"WHERE A.SUPPID = ? " +
				"AND A.RETURNDATE >= ? " +
				"AND A.RETURNDATE <= ? " +
				"AND A.BRANCHID = E.ACCESSID " +
				"AND A.BRANCHID = D.BRANCHID " +
				"AND A.RETURNID = D.RETURNID " +
				"GROUP BY A.RETURNID, A.BRANCHID " +
				"ORDER BY A.RETURNDATE";
	
		try{
			stmt = getConnection().prepareStatement(getOpeningBal);
			stmt.setString(1, customer);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				openBal = openBal + rs.getDouble(1);
			}
			
			stmt = getConnection().prepareStatement(getOpenSalesDtls);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1)%1)) ;
				openBal = openBal + Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS")))+roundOff+rs.getDouble("ADJAMT")));
			}
			
			stmt = getConnection().prepareStatement(getOpenPurchaseReturnDtls);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1)%1)) ;
				openBal = openBal + Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS")))+roundOff+rs.getDouble("ADJAMT")));
			}
			
			stmt = getConnection().prepareStatement(getOpenLabourSalesDtls);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT"))%1)%1)) ;
				openBal = openBal + Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")))+roundOff+rs.getDouble("ADJUSTAMT")));
			}
			
			stmt = getConnection().prepareStatement(getOpenSalesReturnDtls);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				openBal = openBal - rs.getDouble("AMT")+rs.getDouble("LESS");
			}
			
			stmt = getConnection().prepareStatement(getOpenPurchaseDtls);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				openBal = openBal - rs.getDouble("AMT")+rs.getDouble("EXTRA")-rs.getDouble("DISCOUNT");
			}
			
			stmt = getConnection().prepareStatement(getOpenDBDtls);
			stmt.setString(2, customer);
			stmt.setString(1, fromDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				if(rs.getString("CRDR").equals("C")){
					openBal = openBal - Utils.get2DecimalDouble(rs.getDouble("AMOUNT"));
				} else {
					openBal = openBal + Utils.get2DecimalDouble(rs.getDouble("AMOUNT"));
				}
			}
			
			if(openBal != 0){
				entry = new EntryModel();
				entry.setEntryDate(fromDt);
				entry.setLedgerDesc("Opening Balance");
				if(openBal > 0){
					entry.setCrdr("D");
				} else {
					entry.setCrdr("C");
				}
				entry.setAmount((openBal > 0) ? openBal : (0-openBal));
				list.add(entry);
			}
			
			stmt = getConnection().prepareStatement(salesLst);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			stmt.setString(3, toDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS"))%1)%1)) ; 
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("SALESDATE"));
				entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")+rs.getDouble("PACKING")+rs.getDouble("FORWARD")+rs.getDouble("INSTALLATION")-rs.getDouble("LESS")))+roundOff)));
				entry.setCrdr("D");
				entry.setLedgerDesc("SALES BILL NO :"+rs.getString("BILL_PREFIX")+Utils.padBillNo(rs.getInt("SALESID"))+"<br/>"+((rs.getInt("PAYMODE") == 1) ? "CASH SALES" : ((rs.getInt("PAYMODE") == 2) ? "CREDIT CARD SALES" : "CREDIT SALES")));
				list.add(entry);
				if(rs.getDouble("ADJAMT") != 0){
					entry = new EntryModel();
					entry.setEntryDate(rs.getString("PAYDATE"));
					entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("ADJAMT") > 0) ? rs.getDouble("ADJAMT") : (0-rs.getDouble("ADJAMT"))))));
					entry.setCrdr((rs.getDouble("ADJAMT") > 0) ? "D" : "C");
					entry.setLedgerDesc("ADJUSTMENT (DISCOUNT) <BR/> SALES BILL NO :"+rs.getString("BILL_PREFIX")+Utils.padBillNo(rs.getInt("SALESID")));
					list.add(entry);
				}
			}
			
			stmt = getConnection().prepareStatement(purchaseReturnLst);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			stmt.setString(3, toDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT"))%1)%1)) ; 
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("RETURNDATE"));
				entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")))+roundOff)));
				entry.setCrdr("D");
				entry.setLedgerDesc("PURCHASE RETURN BILL NO :"+rs.getString("BILL_PREFIX")+"PR"+Utils.padBillNo(rs.getInt("RETURNID")));
				list.add(entry);
			}
			
			stmt = getConnection().prepareStatement(cnLst);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			stmt.setString(3, toDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT")-rs.getDouble("LESS"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT")-rs.getDouble("LESS"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT")-rs.getDouble("LESS"))%1)%1)) ; 
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("NOTEDATE"));
				entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")-rs.getDouble("LESS")))+roundOff)));
				entry.setCrdr("C");
				entry.setLedgerDesc("CREDIT NOTE NO :"+rs.getString("BILL_PREFIX")+"CN"+Utils.padBillNo(rs.getInt("CREDITNOTEID")));
				list.add(entry);
			}
			
			stmt = getConnection().prepareStatement(labourBillLst);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			stmt.setString(3, toDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				roundOff = (1-Utils.get2DecimalDouble((rs.getDouble("AMT"))%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(((rs.getDouble("AMT"))%1)%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(((rs.getDouble("AMT"))%1)%1)) ; 
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("BILLDATE"));
				entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("AMT")))+roundOff)));
				entry.setCrdr("D");
				entry.setLedgerDesc("LABOUR BILL NO :"+rs.getString("BILL_PREFIX")+"LB"+Utils.padBillNo(rs.getInt("BILLNO")));
				list.add(entry);
				if(rs.getDouble("ADJAMT") != 0){
					entry = new EntryModel();
					entry.setEntryDate(rs.getString("PAYDATE"));
					entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("ADJAMT") > 0) ? rs.getDouble("ADJAMT") : (0-rs.getDouble("ADJAMT"))))));
					entry.setCrdr((rs.getDouble("ADJAMT") > 0) ? "D" : "C");
					entry.setLedgerDesc("ADJUSTMENT (DISCOUNT) <BR/> LABOUR BILL NO :"+rs.getString("BILL_PREFIX")+"LB"+Utils.padBillNo(rs.getInt("BILLNO")));
					list.add(entry);
				}
			}
			
			stmt = getConnection().prepareStatement(purchaseLst);
			stmt.setString(1, customer);
			stmt.setString(2, fromDt);
			stmt.setString(3, toDt);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setEntryDate(rs.getString("RECDDT"));
				entry.setAmount(rs.getDouble("AMT")+rs.getDouble("EXTRA")-rs.getDouble("DISCOUNT"));
				entry.setCrdr("C");
				entry.setLedgerDesc("PURCHASE BILL NO :"+rs.getString("BILL_PREFIX")+"P"+Utils.padBillNo(rs.getInt("PURCHASEID"))+"<BR/> CUSTOMER INVOICE NO :"+rs.getString("INVNO")+" DT:"+Utils.convertToAppDateDDMMYY(rs.getString("INVDT")));
				list.add(entry);
				if(rs.getDouble("ADJAMT") != 0){
					entry = new EntryModel();
					entry.setEntryDate(rs.getString("PAYDATE"));
					entry.setAmount(Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble((rs.getDouble("ADJAMT") > 0) ? rs.getDouble("ADJAMT") : (0-rs.getDouble("ADJAMT"))))));
					entry.setCrdr((rs.getDouble("ADJAMT") > 0) ? "C" : "D");
					entry.setLedgerDesc("ADJUSTMENT (DISCOUNT) <BR/> PURCHASE NO :"+rs.getString("BILL_PREFIX")+"P"+Utils.padBillNo(rs.getInt("PURCHASEID")));
					list.add(entry);
				}
			}
			
			stmt = getConnection().prepareStatement(getDBDtls);
			stmt.setString(1, fromDt);
			stmt.setString(2, toDt);
			stmt.setString(3, customer);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				if((rs.getInt("TYPE") == 1 || rs.getInt("TYPE") == 2 || rs.getInt("TYPE") == 3 || rs.getInt("TYPE") == 4 || rs.getInt("TYPE") == 41 || rs.getInt("TYPE") == 42 
						 || rs.getInt("TYPE") == 43 || rs.getInt("TYPE") == 44 || rs.getInt("TYPE") == 46 || rs.getInt("TYPE") == 47 
								 || rs.getInt("TYPE") == 48 || rs.getInt("TYPE") == 49 || rs.getInt("TYPE") == 71 || rs.getInt("TYPE") == 72 || rs.getInt("TYPE") == 73) && rs.getInt("REFNO") != 0){
					if(temp.get(rs.getInt("REFNO")) != null){
						entry = temp.get(rs.getInt("REFNO"));
						entry.setAmount(entry.getAmount()+rs.getDouble("AMOUNT"));
						if(rs.getInt("BRANCH") != -1){
							entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
							entry.setBillNo(rs.getInt("BILLNO"));
						}
						entry.setLedgerDesc(entry.getLedgerDesc()+", "+entry.getBillRefNo()+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")");
					} else {
						entry.setEntryDate(rs.getString("DAY"));
						entry.setAmount(rs.getDouble("AMOUNT"));
						entry.setEntryType(rs.getInt("TYPE"));
						entry.setRemark(rs.getString("REMARK"));
						entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
						entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
						entry.getCustomer().setArea(rs.getString("AREA"));
						entry.getCustomer().setCity(rs.getString("CITY"));
						entry.setCustBankName(rs.getString("CUSTBANK"));
						entry.setChqNo(rs.getString("CHQNO"));
						entry.setChqDt(rs.getString("CHQDT"));
						entry.setCrdr(rs.getString("CRDR"));
						if(rs.getInt("BRANCH") != -1){
							entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
							entry.setBillNo(rs.getInt("BILLNO"));
						}
						entry.setLedgerDesc(Utils.getEntryType(rs.getInt("TYPE"))+"<br/>"+((entry.getSemiLedgerDesc().substring(entry.getSemiLedgerDesc().length()-5,entry.getSemiLedgerDesc().length()).equals("<br/>")) ? entry.getSemiLedgerDesc().substring(0, entry.getSemiLedgerDesc().length()-5)+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")" : (entry.getSemiLedgerDesc())+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")"));
					}
					entry.setUser(rs.getString("USER"));
					temp.put(rs.getInt("REFNO"), entry);
				} else {
					entry.setEntryDate(rs.getString("DAY"));
					entry.setAmount(rs.getDouble("AMOUNT"));
					entry.setEntryType(rs.getInt("TYPE"));
					entry.setRemark(rs.getString("REMARK"));
					entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
					entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
					entry.getCustomer().setArea(rs.getString("AREA"));
					entry.getCustomer().setCity(rs.getString("CITY")); 
					entry.setCustBankName(rs.getString("CUSTBANK"));
					entry.setChqNo(rs.getString("CHQNO"));
					entry.setChqDt(rs.getString("CHQDT"));
					entry.setCrdr(rs.getString("CRDR"));
					if(rs.getInt("BILLNO") != -1){
						entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
						entry.setBillNo(rs.getInt("BILLNO"));
						entry.setLedgerDesc(Utils.getEntryType(rs.getInt("TYPE"))+"<br/>"+((entry.getSemiLedgerDesc().substring(entry.getSemiLedgerDesc().length()-5,entry.getSemiLedgerDesc().length()).equals("<br/>")) ? entry.getSemiLedgerDesc().substring(0, entry.getSemiLedgerDesc().length()-5)+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")" : entry.getSemiLedgerDesc()+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")"));
					} else {
						entry.setLedgerDesc(Utils.getEntryType(rs.getInt("TYPE"))+"<br/>"+entry.getSemiDesc());
					}
					entry.setUser(rs.getString("USER"));
					list.add(entry);
				}
			}
			
			Iterator<Integer> keys = temp.keySet().iterator();
			int l = 0;
			
			while(keys.hasNext()){
				l = keys.next();
				list.add(temp.get(l));
			}

			Collections.sort(list, new LedgerDateComparator());
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

}
