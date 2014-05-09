package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.en.model.AdminItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;

public class AdminMasterBroker extends AdminBaseBroker {

	public AdminMasterBroker(Connection conn) {
		super(conn);
	}

	public AdminItemModel[] getItemDetails() {
		ArrayList<AdminItemModel> items = new ArrayList<AdminItemModel>(0);
		AdminItemModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		// query to get all the existing item group
		String getItemGroups = "SELECT A.ITEMID, A.ITEMNAME, A.ITEMNUMBER, A.PRICE, A.TAX, A.TT, A.SP1, A.SP2, A.UPDT, A.ISINDIAN, A.SP3, A.AVGSP, A.REFNAME, ((SUM(B.RATE*B.QTY)+SUM(B.DUTY)+SUM((B.CBM*(C.DESTINATIONEXP+(C.SOURCEEXP*C.EXCHANGERATE)))/C.TOTALCBM))/SUM(B.QTY)) AS AVGCP, A.CP " +
				"FROM ADMIN_ITEM A LEFT JOIN INWARD_ENTRY_ITEM B ON A.ITEMID = B.ITEMID LEFT JOIN INWARD_ENTRY C ON C.ENTRYNO = B.ENTRYNO GROUP BY A.ITEMID ORDER BY A.ITEMNUMBER";
		
		// query to get the current cost price
		String getCurrentCP = "SELECT (A.RATE/B.EXCHANGERATE) AS CHINARATE ,B.ENTRYDATE, B.TOTALCBM, (((A.RATE*A.QTY)+A.DUTY+(A.CBM*(B.DESTINATIONEXP+(B.SOURCEEXP*B.EXCHANGERATE))/B.TOTALCBM))/A.QTY) AS CURRCP, A.RATE FROM INWARD_ENTRY_ITEM A, INWARD_ENTRY B WHERE A.ENTRYNO = B.ENTRYNO AND A.ITEMID = ? ORDER BY B.ENTRYDATE DESC LIMIT 2";
		
		try{
			
			stmt = getAdminConnection().prepareStatement(getItemGroups);
			stmt1 = getAdminConnection().prepareStatement(getCurrentCP);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new AdminItemModel();
				model.setItemId(rs.getInt("ITEMID"));
				model.setItemNumber(rs.getString("ITEMNUMBER"));
				model.setItemName(rs.getString("ITEMNAME"));
				model.setCurrSP(rs.getDouble("PRICE"));
				model.setSP1((rs.getDouble("SP1") == 0) ? rs.getDouble("PRICE") : rs.getDouble("SP1"));
				model.setSP2((rs.getDouble("SP2") == 0) ? rs.getDouble("PRICE") : rs.getDouble("SP2"));
				model.setSP3((rs.getDouble("SP3") == 0) ? rs.getDouble("PRICE") : rs.getDouble("SP3"));
				model.setRefName(rs.getString("REFNAME"));
				model.setTT	(rs.getDouble("TT"));
				model.setAvgSP(rs.getDouble("AVGSP"));
				model.setServerCP(rs.getDouble("CP"));
				model.setUpdateDate((rs.getString("UPDT") != null) ?rs.getString("UPDT") : "0000-00-00");
				if(rs.getString("AVGCP") != null){
					model.setAvgCP(rs.getDouble("AVGCP"));
				}
				if(rs.getInt("ISINDIAN") == 1){
					model.setIndian();
				}
				
				stmt1.setInt(1, rs.getInt("ITEMID"));
				
				rs1 = stmt1.executeQuery(); 
				
				if(rs1.next()){
					model.setCurrCP((rs1.getDouble("TOTALCBM") == 0) ?  rs1.getDouble("RATE") : rs1.getDouble("CURRCP"));
					model.setChinaRate(rs1.getDouble("CHINARATE"));
				}
				
				if(rs1.next()){
					model.setLastCP((rs1.getDouble("TOTALCBM") == 0) ?  rs1.getDouble("RATE") : rs1.getDouble("CURRCP"));
				}
				
				items.add(model);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(getConnection() != null)
					getConnection().rollback();
				if(getAdminConnection() != null)
					getAdminConnection().rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try{
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(stmt1 != null)
					stmt1.close();
				if(rs1 != null)
					rs1.close();
				if(getConnection() != null)
					getConnection().commit();
				if(getAdminConnection() != null)
					getAdminConnection().commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return (AdminItemModel[]) items.toArray(new AdminItemModel[0]);
	}

	public MessageModel saveData(AdminItemModel[] items) {
		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;

		String updateAdminItemData = "UPDATE ADMIN_ITEM SET SP1 = ?, SP2 =? , SP3 = ?, TT = ?, REFNAME = ?, ISINDIAN = ?, UPDT = CURRENT_DATE WHERE ITEMID = ?";
		
		try{
						
			stmt = getAdminConnection().prepareStatement(updateAdminItemData);
			
			for(int k=0; k<items.length; k++){
				stmt.setDouble(1, items[k].getSP1());
				stmt.setDouble(2, items[k].getSP2());
				stmt.setDouble(3, items[k].getSP3());
				stmt.setDouble(4, items[k].getTT());
				stmt.setString(5, items[k].getRefName());
				stmt.setInt(6, items[k].isIndian() ? 1 : 0);
				stmt.setInt(7, items[k].getItemId());
				
				stmt.addBatch();
			}
					
			stmt.executeBatch();
			
			getAdminConnection().commit();
			
			msg.addNewMessage(new Message(SUCCESS, "Data Saved Successfully."));
			
		} catch (Exception e) {
			try{
				getAdminConnection().rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
			msg.addNewMessage(new Message(ERROR, "Error occured while saving the data from database"));
			e.printStackTrace();
		}
		
		return msg;
	}

	public void removeData() {
		PreparedStatement stmt = null;

		String deleteTable1 = "delete from admin_item";
		String deleteTable2 = "delete from inward_entry";
		String deleteTable3 = "delete from inward_entry_item";
		
		try{
						
			stmt = getAdminConnection().prepareStatement(deleteTable1);
			stmt.execute();
			stmt = getAdminConnection().prepareStatement(deleteTable2);
			stmt.execute();
			stmt = getAdminConnection().prepareStatement(deleteTable3);
			stmt.execute();
			
			getAdminConnection().commit();
			
		} catch (Exception e) {
			try{
				getAdminConnection().rollback();
			} catch (Exception ex) {
				// TODO: handle exception
			}
			e.printStackTrace();
		}
		
		return;
	}

}
