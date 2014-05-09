package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class SalesTaxReportBroker extends BaseBroker {

	public SalesTaxReportBroker(Connection conn) {
		super(conn);
	}

	public HashMap<String, HashMap<String, HashMap<String, Object>>> getTaxRpt(
			String[] branch, String fromdt, String todt) {
		
		HashMap<String, HashMap<String, HashMap<String, Object>>> result = new HashMap<String, HashMap<String,HashMap<String,Object>>>(0);
		
		HashMap<String, HashMap<String, Object>> branchTemp = null;
		
		HashMap<String, Object> taxTemp = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		//query to the tax details
		String getTaxDetails = "SELECT VAT1, VAT2, VAT3, CST1, CST2, CST3, CSTC1, CSTC2, CSTC3 FROM TAX WHERE ACCESSID = ?";
		
		//query to get the sales details
		String getSalesDetails = "SELECT A.TAXTYPE , B.TAX, SUM(((100-B.DISRATE)/100)*B.RATE*B.QTY) AS AMT, SUM(B.TAX*((100-B.DISRATE)/100)*B.RATE*B.QTY/100) AS TAXAMT FROM SALES A, SALES_ITEM B WHERE A.SALESID = B.SALESID AND A.BRANCHID = B.BRANCHID AND A.BRANCHID = ? AND A.SALESDATE >=  ? AND A.SALESDATE <= ? AND A.TAXTYPE = ? AND B.TAX = ?";
		
		try {
		
			for (int i=0; i<branch.length; i++){
				
				branchTemp = new HashMap<String, HashMap<String,Object>>(0);
				
				taxTemp = new HashMap<String, Object>();
				
				taxTemp.put("branch",((new AccessPointBroker(getConnection())).getAccessPointDtls(Integer.parseInt(branch[i]))));
				
				branchTemp.put("dtls", taxTemp);
				
				stmt = getConnection().prepareStatement(getTaxDetails);
				stmt.setString(1, branch[i]);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("VAT1"));
					taxTemp.put("TYPE", 1);
					branchTemp.put("VAT1", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("VAT2"));
					taxTemp.put("TYPE", 1);
					branchTemp.put("VAT2", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("VAT3"));
					taxTemp.put("TYPE", 1);
					branchTemp.put("VAT3", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("CST1"));
					taxTemp.put("TYPE", 2);
					branchTemp.put("CST1", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("CST2"));
					taxTemp.put("TYPE", 2);
					branchTemp.put("CST2", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("CST3"));
					taxTemp.put("TYPE", 2);
					branchTemp.put("CST3", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("CSTC1"));
					taxTemp.put("TYPE", 3);
					branchTemp.put("CST1C", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("CSTC2"));
					taxTemp.put("TYPE", 3);
					branchTemp.put("CST2C", taxTemp);
					taxTemp = new HashMap<String, Object>();
					taxTemp.put("RATE", rs.getDouble("CSTC3"));
					taxTemp.put("TYPE", 3);
					branchTemp.put("CST3C", taxTemp);
				}
				
				String[] taxTypes = {"VAT1","VAT2","VAT3","CST1","CST2","CST3","CST1C","CST2C","CST3C"};
				
				stmt = getConnection().prepareStatement(getSalesDetails);
				
				for(int k=0; k<taxTypes.length; k++){
					stmt.setString(1, branch[i]);
					stmt.setString(2, fromdt);
					stmt.setString(3, todt);
					stmt.setInt(4, (Integer)(branchTemp.get(taxTypes[k])).get("TYPE"));
					stmt.setDouble(5, (Double)(branchTemp.get(taxTypes[k])).get("RATE"));
					
					rs = stmt.executeQuery();
					
					if(rs.next()){
						(branchTemp.get(taxTypes[k])).put("AMT", rs.getDouble("AMT"));
						(branchTemp.get(taxTypes[k])).put("TAXAMT", rs.getDouble("TAXAMT"));
					} else {
						(branchTemp.get(taxTypes[k])).put("AMT", 0);
						(branchTemp.get(taxTypes[k])).put("TAXAMT", 0);
					}
				}
				
				result.put(branch[i], branchTemp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return result;
	}

	
	
}
