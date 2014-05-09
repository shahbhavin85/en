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

public class BranchDayEntryReportBroker extends BaseBroker {

	public BranchDayEntryReportBroker(Connection conn) {
		super(conn);
	}

	public EntryModel[] getChqEntries(String customer, String[] branch,
			String fromdt, String todt) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CHQNO, A.CHQDT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO, D.NAME AS BRANCHNAME, D.CITY AS BRANCHCITY FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C, ACCESS_POINT D  WHERE A.ID = C.ID AND (A.TYPE = 1 OR A.TYPE = 43) AND A.CUSTOMER = B.CUSTOMERID AND C.BRANCH = D.ACCESSID ";

		if(!customer.equals("")){
			getItemList	= getItemList +	"AND A.CUSTOMER = ? ";
		}
		if(branch.length!=0 && !branch[0].equals("--")){
			getItemList	= getItemList +	"AND (";
			for(int k=0; k<branch.length; k++){
				getItemList	= getItemList +	"C.BRANCH = ? ";
				if(k !=branch.length-1){
					getItemList	= getItemList +	"OR ";
				}
			}
			getItemList	= getItemList +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			getItemList	= getItemList +	"AND C.DAY >= ? AND C.DAY <= ? ";
		}
		
	  	getItemList = getItemList + "ORDER BY C.DAY";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
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
				entry = new EntryModel();
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setRemark(rs.getString("REMARK"));
				entry.setEntryDate(rs.getString("DAY"));
				entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
				entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				entry.getCustomer().setArea(rs.getString("AREA"));
				entry.getCustomer().setCity(rs.getString("CITY"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				entry.setChqNo(rs.getString("CHQNO"));
				entry.setChqDt(rs.getString("CHQDT"));
				if(rs.getInt("BRANCH") != -1){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
					entry.setBillNo(rs.getInt("BILLNO"));
				}
				entry.getBranch().setAccessName(rs.getString("BRANCHNAME"));
				entry.getBranch().setCity(rs.getString("BRANCHCITY"));
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

	public EntryModel[] getBankEntries(String customer, String[] branch,
			String fromdt, String todt) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.AMOUNT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO, D.NAME AS BRANCHNAME, D.CITY AS BRANCHCITY FROM BRANCH_DAY_BOOK_ENTRIES A, CUSTOMER B, BRANCH_DAY_BOOK_MASTER C, ACCESS_POINT D  WHERE A.ID = C.ID AND (A.TYPE = 4 OR A.TYPE = 44) AND A.CUSTOMER = B.CUSTOMERID AND C.BRANCH = D.ACCESSID ";

		if(!customer.equals("")){
			getItemList	= getItemList +	"AND A.CUSTOMER = ? ";
		}
		if(branch.length!=0 && !branch[0].equals("--")){
			getItemList	= getItemList +	"AND (";
			for(int k=0; k<branch.length; k++){
				getItemList	= getItemList +	"C.BRANCH = ? ";
				if(k !=branch.length-1){
					getItemList	= getItemList +	"OR ";
				}
			}
			getItemList	= getItemList +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			getItemList	= getItemList +	"AND C.DAY >= ? AND C.DAY <= ? ";
		}
		
	  	getItemList = getItemList + "ORDER BY C.DAY";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			if(!customer.equals("")){
				stmt.setString(i, customer);
				i++;
			}
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
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
				entry = new EntryModel();
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(rs.getInt("TYPE"));
				entry.setRemark(rs.getString("REMARK"));
				entry.setEntryDate(rs.getString("DAY"));
				entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
				entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
				entry.getCustomer().setArea(rs.getString("AREA"));
				entry.getCustomer().setCity(rs.getString("CITY"));
				entry.setCustBankName(rs.getString("CUSTBANK"));
				if(rs.getInt("BRANCH") != -1){
					entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
					entry.setBillNo(rs.getInt("BILLNO"));
				}
				entry.getBranch().setAccessName(rs.getString("BRANCHNAME"));
				entry.getBranch().setCity(rs.getString("BRANCHCITY"));
				list.add(entry);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return (EntryModel[])list.toArray(new EntryModel[0]);
	}

	public EntryModel[] getBankLedger(String heshBank, String[] branch,
			String fromdt, String todt) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		HashMap<Integer, EntryModel> temp = new HashMap<Integer, EntryModel>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, A.AMOUNT, A.CUSTOMER, A.CUSTBANK, A.REMARK, A.BRANCH, A.BILLNO, D.NAME AS BRANCHNAME, A.CHQNO, A.CHQDT, A.CUSTBANK, "+ 
						"D.CITY AS BRANCHCITY, B.CUSTOMERNAME, B.AREA, A.TYPE, B.CITY, A.PAYMODE, A.CRDR, A.BANKNAME, A.REFNO "+
						"FROM BRANCH_DAY_BOOK_MASTER C, ACCESS_POINT D, BRANCH_DAY_BOOK_ENTRIES A "+
						"LEFT OUTER JOIN CUSTOMER B ON A.CUSTOMER = B.CUSTOMERID "+
						"WHERE A.ID = C.ID AND  C.BRANCH = D.ACCESSID AND A.BANKNAME = ? ";

		if(branch.length!=0 && !branch[0].equals("--")){
			getItemList	= getItemList +	"AND (";
			for(int k=0; k<branch.length; k++){
				getItemList	= getItemList +	"C.BRANCH = ? ";
				if(k !=branch.length-1){
					getItemList	= getItemList +	"OR ";
				}
			}
			getItemList	= getItemList +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			getItemList	= getItemList +	"AND C.DAY >= ? AND C.DAY <= ? ";
		}
		
	  	getItemList = getItemList + "ORDER BY C.DAY, A.REFNO";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setString(i, heshBank);
			i++;
			if(branch.length!=0 && !branch[0].equals("--")){
				for(int k=0; k<branch.length; k++){
					stmt.setString(i, branch[k]);
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
						entry = new EntryModel();
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
						entry.getBranch().setAccessName(rs.getString("BRANCHNAME"));
						entry.getBranch().setCity(rs.getString("BRANCHCITY"));
						entry.setLedgerDesc(Utils.getEntryType(rs.getInt("TYPE"))+"<br/>"+((entry.getSemiLedgerDesc().substring(entry.getSemiLedgerDesc().length()-5,entry.getSemiLedgerDesc().length()).equals("<br/>")) ? entry.getSemiLedgerDesc().substring(0, entry.getSemiLedgerDesc().length()-5)+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")" : (entry.getSemiLedgerDesc())+"--(Rs. "+Utils.get2Decimal(rs.getDouble("AMOUNT"))+")"));
					}
					temp.put(rs.getInt("REFNO"), entry);
				} else {
					entry = new EntryModel();
					entry.setAmount(rs.getDouble("AMOUNT"));
					entry.setEntryType(rs.getInt("TYPE"));
					entry.setRemark(rs.getString("REMARK"));
					entry.setEntryDate(rs.getString("DAY"));
					entry.setPayMode(rs.getInt("PAYMODE"));
					entry.setCrdr(rs.getString("CRDR"));
					if(entry.getEntryType() == 54){
						entry.setCrdr("C");
					}
					if(entry.getEntryType() == 9){
						entry.setCrdr("D");
					}
					entry.setChqNo(rs.getString("CHQNO"));
					entry.setChqDt(rs.getString("CHQDT"));
					entry.setBankName(rs.getString("BANKNAME"));
					if(rs.getInt("CUSTOMER") != 0){
						entry.getCustomer().setCustomerId(rs.getInt("CUSTOMER"));
						entry.getCustomer().setCustomerName(rs.getString("CUSTOMERNAME"));
						entry.getCustomer().setArea(rs.getString("AREA"));
						entry.getCustomer().setCity(rs.getString("CITY"));
						entry.setCustBankName(rs.getString("CUSTBANK"));
					}
					if(rs.getInt("BRANCH") != -1){
						entry.setBillBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("BRANCH")));
						entry.setBillNo(rs.getInt("BILLNO"));
					}
					entry.getBranch().setAccessName(rs.getString("BRANCHNAME"));
					entry.getBranch().setCity(rs.getString("BRANCHCITY"));
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
