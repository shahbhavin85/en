package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.TransferModel;

public class TransferReportBroker extends BaseBroker {

	public TransferReportBroker(Connection conn) {
		super(conn);
	}

	public TransferModel[] getMasterRpt(String[] fromBranch, String[] toBranch,
			String fromdt, String todt) {
		ArrayList<TransferModel> reportBills = new ArrayList<TransferModel>(0);
		TransferModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int i=1;

		// query to check the existence of the bill no
		String outstandingBill = "SELECT A.TRANSFERID, A.TRANSFERDATE, A.FROMID, A.TOID, A.REMARK, SUM(B.QTY*B.PRICE) AS AMT, A.TOAPPED " +
				" FROM TRANSFER A, TRANSFER_ITEM B " +
				"WHERE A.TRANSFERID = B.TRANSFERID " +
				"AND A.FROMID = B.BRANCHID ";
		
		if(fromBranch.length!=0 && !fromBranch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<fromBranch.length; k++){
				outstandingBill	= outstandingBill +	"A.FROMID = ? ";
				if(k !=fromBranch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		
		if(toBranch.length!=0 && !toBranch[0].equals("--")){
			outstandingBill	= outstandingBill +	"AND (";
			for(int k=0; k<toBranch.length; k++){
				outstandingBill	= outstandingBill +	"A.TOID = ? ";
				if(k !=toBranch.length-1){
					outstandingBill	= outstandingBill +	"OR ";
				}
			}
			outstandingBill	= outstandingBill +	") ";
		}
		if(!fromdt.equals("") && !todt.equals("")){
			outstandingBill	= outstandingBill +	"AND A.TRANSFERDATE >= ? AND A.TRANSFERDATE <= ? ";
		}
		

		outstandingBill	= outstandingBill +	"GROUP BY A.FROMID, A.TRANSFERID ORDER BY A.TRANSFERDATE, A.FROMID, A.TRANSFERID";
		
		try{
			
			stmt = getConnection().prepareStatement(outstandingBill);
			if(fromBranch.length!=0 && !fromBranch[0].equals("--")){
				for(int k=0; k<fromBranch.length; k++){
					stmt.setString(i, fromBranch[k]);
					i++;
				}
			}
			if(toBranch.length!=0 && !toBranch[0].equals("--")){
				for(int k=0; k<toBranch.length; k++){
					stmt.setString(i, toBranch[k]);
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
				model = new TransferModel();
				model.setTransferid(rs.getInt("TRANSFERID"));
				model.setTransferdate(rs.getString("TRANSFERDATE"));
				model.setFromBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("FROMID")));
				model.setToBranch((new AccessPointBroker(getConnection())).getAccessPointDtls(rs.getInt("TOID")));
				model.setTotalAmt(rs.getDouble("AMT"));
				if((rs.getString("TOAPPED")).equals("Y")){
					model.setToApproved();
				}
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
		return (TransferModel[]) reportBills.toArray(new TransferModel[0]);
	}

}
