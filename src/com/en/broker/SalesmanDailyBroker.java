package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesmanDlyItmModel;
import com.en.model.SalesmanDlyRptModel;
import com.en.model.UserModel;
import com.en.util.Utils;

public class SalesmanDailyBroker extends BaseBroker {

	public SalesmanDailyBroker(Connection conn) {
		super(conn);
	}
	
	public String checkStatus(String userId, String rptDt){
		String result = "Open";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the current status of the report
		String reportStatus = "SELECT STATUS FROM DAILY_REPORT_MASTER WHERE SALESMANID = ? AND RPTDT = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(reportStatus);
			stmt.setString(1, userId);
			stmt.setString(2, rptDt);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				result = rs.getString("STATUS");
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public SalesmanDlyRptModel getDailyReport(String userId, String reportDt){
		SalesmanDlyRptModel model = null;
		SalesmanDlyItmModel item = null;
		
		String rptDt = Utils.convertToSQLDate(reportDt);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		long rptId = 0;		
		boolean isPresent = false;
		
		// query to get the master details
		String getMasterDetails = "SELECT A.RPTID, A.SALESMANID, A.RPTDT, A.STATUS, B.USERNAME, A.SENTDATE, A.APPROVEDBY, A.REMARK, B.TYPE FROM DAILY_REPORT_MASTER A, USER B " +
				"WHERE A.SALESMANID = B.USERID AND A.SALESMANID = ? AND A.RPTDT = ?";
		
		// query to get the details
		String getItemList = "SELECT RPTID, IDX, INTIME, OUTTIME, CUSTOMERID, REMARK, TYPE, EDITABLE, LODGING, FOOD, RAILBUS, LOCAL, COURIER, OTHERS FROM DAILY_REPORT_DTLS WHERE RPTID = ? ORDER BY INTIME";
		
		// query to get customer name
		String getCustomerDtls = "SELECT CUSTOMERNAME, AREA, CITY FROM CUSTOMER WHERE CUSTOMERID = ?";
		
		// query to get the order count 
		String getOrderCount = "SELECT COUNT(*) FROM ORDER_MASTER WHERE SALESMANID = ? AND ORDERDATE = ?";
		
		// query to get the order count 
		String getQuotationCount = "SELECT COUNT(*) FROM QUOTATION WHERE SALESMANID = ? AND QUOTATIONDATE = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getMasterDetails);
			stmt.setString(1, userId);
			stmt.setString(2, Utils.convertToSQLDate(rptDt));
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model = new SalesmanDlyRptModel();
				isPresent = true;
				rptId = rs.getLong("RPTID");
				model.setRptId(rs.getLong("RPTID"));	
				model.setRptDt(reportDt);
				model.setStatus(rs.getString("STATUS"));
				model.setUser((new UserBroker(getConnection())).getUserDtls(userId));
				model.setSentDt(rs.getString("SENTDATE"));
				model.setRemark(rs.getString("REMARK"));
				if(!rs.getString("APPROVEDBY").equals("")){
					model.setApprover((new UserBroker(getConnection())).getUserDtls(rs.getString("APPROVEDBY")));
				}
			}
			
			if(isPresent){
				stmt = getConnection().prepareStatement(getItemList);
				stmt.setLong(1, rptId);
				stmt1 = getConnection().prepareStatement(getCustomerDtls);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					item = new SalesmanDlyItmModel();
					item.setIndex(rs.getLong("IDX"));
					item.setInTime(rs.getString("INTIME"));
					item.setOutTime(rs.getString("OUTTIME"));
					item.setType(rs.getString("TYPE"));
					item.setRemark(rs.getString("REMARK"));
					item.setEditable(rs.getString("EDITABLE"));
					item.setLodging(rs.getDouble("LODGING"));
					item.setFood(rs.getDouble("FOOD"));
					item.setRailbus(rs.getDouble("RAILBUS"));
					item.setLocaltransport(rs.getDouble("LOCAL"));
					item.setCourier(rs.getDouble("COURIER"));
					item.setOthers(rs.getDouble("OTHERS"));
					if(rs.getString("TYPE").equals("0")){
						stmt1.setString(1, rs.getString("CUSTOMERID"));
						
						rs1 = stmt1.executeQuery();
						
						if(rs1.next()){
							item.getCustomer().setCustomerId(rs.getInt("CUSTOMERID"));
							item.getCustomer().setCustomerName(rs1.getString("CUSTOMERNAME"));
							item.getCustomer().setArea(rs1.getString("AREA"));
							item.getCustomer().setCity(rs1.getString("CITY"));
						}
					}
					model.addDtls(item);
				}
			}
			
			stmt = getConnection().prepareStatement(getQuotationCount);
			stmt.setString(1, userId);
			stmt.setString(2, rptDt);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				if(model == null){
					model = new SalesmanDlyRptModel();
					model.setRptDt(rptDt);
				}
				model.setQuotation(rs.getInt(1));
			}
			
			stmt = getConnection().prepareStatement(getOrderCount);
			stmt.setString(1, userId);
			stmt.setString(2, rptDt);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				if(model == null){
					model = new SalesmanDlyRptModel();
					model.setRptDt(rptDt);
				}
				model.setOrders(rs.getInt(1));
			}
			
			if(model == null){
				model = new SalesmanDlyRptModel();
				model.setRptDt(rptDt);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return model;
	}

	@SuppressWarnings("resource")
	public MessageModel addItm(SalesmanDlyItmModel item, String userId, String reportDt){
		MessageModel model = new MessageModel();
		Message msg = null;
		
		String rptDt1 = Utils.convertToSQLDate(reportDt); 
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long rptId = 1;
		boolean isMasterPresent = false;
		boolean exit = false;
		
		// query to get rpt id
		String getRptId = "SELECT RPTID FROM DAILY_REPORT_MASTER WHERE SALESMANID = ? AND RPTDT = ?";
		
		// query to get max rpt id
		String getMaxRptId = "SELECT MAX(RPTID) FROM DAILY_REPORT_MASTER";
		
		// query to insert master details
		String insertMasterDtls = "INSERT INTO DAILY_REPORT_MASTER(RPTID, SALESMANID, RPTDT, STATUS) VALUE (?,?,?,'Open')";

		// query to insert appointment type details 
		String insertAppItemDtls = "INSERT INTO DAILY_REPORT_DTLS(RPTID, IDX, INTIME, OUTTIME, CUSTOMERID, REMARK, TYPE, EDITABLE, LODGING, FOOD, RAILBUS, LOCAL, COURIER, OTHERS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// query to insert other type details 
		String insertOtherItemDtls = "INSERT INTO DAILY_REPORT_DTLS(RPTID, IDX, INTIME, OUTTIME, REMARK, TYPE, EDITABLE, LODGING, FOOD, RAILBUS, LOCAL, COURIER, OTHERS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		// query to check times
//		String checkInTime = "SELECT COUNT(*) FROM DAILY_REPORT_DTLS WHERE RPTID = ? AND INTIME <= ? AND OUTTIME > ?";
		
		// query to check times
//		String checkOutTime = "SELECT COUNT(*) FROM DAILY_REPORT_DTLS WHERE RPTID = ? AND INTIME < ? AND OUTTIME >= ?";
		
		// query to get max idx
		String getMaxIdx = "SELECT MAX(IDX) FROM DAILY_REPORT_DTLS WHERE RPTID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			stmt.setString(1, userId);
			stmt.setString(2, rptDt1);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				rptId = rs.getLong("RPTID");
				isMasterPresent = true;
			}
			
			if(!isMasterPresent){
				stmt = getConnection().prepareStatement(getMaxRptId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1)>0){
					rptId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertMasterDtls);
				stmt.setLong(1, rptId);
				stmt.setString(2, userId);
				stmt.setString(3, rptDt1);
				
				int z = stmt.executeUpdate();
				
				if(z>0){
					if(item.getType().equals("0")){
						stmt = getConnection().prepareStatement(insertAppItemDtls);
						stmt.setLong(1, rptId);
						stmt.setInt(2, 1);
						stmt.setString(3, item.getInTime());
						stmt.setString(4, item.getOutTime());
						stmt.setInt(5, item.getCustomer().getCustomerId());
						stmt.setString(6, item.getRemark());
						stmt.setString(7, item.getType());
						stmt.setString(8, item.getEditable());
						stmt.setDouble(9, item.getLodging());
						stmt.setDouble(10, item.getFood());
						stmt.setDouble(11, item.getRailbus());
						stmt.setDouble(12, item.getLocaltransport());
						stmt.setDouble(13, item.getCourier());
						stmt.setDouble(14, item.getOthers());
					} else {
						stmt = getConnection().prepareStatement(insertOtherItemDtls);
						stmt.setLong(1, rptId);
						stmt.setInt(2, 1);
						stmt.setString(3, item.getInTime());
						stmt.setString(4, item.getOutTime());
						stmt.setString(5, item.getRemark());
						stmt.setString(6, item.getType());
						stmt.setString(7, item.getEditable());
						stmt.setDouble(8, item.getLodging());
						stmt.setDouble(9, item.getFood());
						stmt.setDouble(10, item.getRailbus());
						stmt.setDouble(11, item.getLocaltransport());
						stmt.setDouble(12, item.getCourier());
						stmt.setDouble(13, item.getOthers());
					}
					
					stmt.executeUpdate();
				}
				
				msg = new Message(SUCCESS, "Added Successfully.");
				model.addNewMessage(msg);
				
			} else {
//				stmt = getConnection().prepareStatement(checkInTime);
//				stmt.setLong(1, rptId);
//				stmt.setString(2, item.getInTime());
//				stmt.setString(3, item.getInTime());
//				
//				rs = stmt.executeQuery();
//				
//				if(rs.next() && rs.getInt(1)>0){
//					msg = new Message(ERROR, "In Time entered cannot be in between the time of other details. Please check the table below.");
//					model.addNewMessage(msg);
//					exit = true;
//				}
//				
//				if(exit){
//					stmt = getConnection().prepareStatement(checkOutTime);
//					stmt.setLong(1, rptId);
//					stmt.setString(2, item.getOutTime());
//					stmt.setString(3, item.getOutTime());
//					
//					rs = stmt.executeQuery();
//					
//					if(rs.next() && rs.getInt(1)>0){
//						msg = new Message(ERROR, "Out Time entered cannot be in between the time of other details. Please check the table below.");
//						model.addNewMessage(msg);
//						exit = true;
//					}
//				}
				
				if(!exit){
					int idx = 1;
					
					stmt = getConnection().prepareStatement(getMaxIdx);
					stmt.setLong(1, rptId);
					
					rs = stmt.executeQuery();
					
					if(rs.next() && rs.getInt(1)>0){
						idx = rs.getInt(1)+1;
					}
					
					if(item.getType().equals("0")){
						stmt = getConnection().prepareStatement(insertAppItemDtls);
						stmt.setLong(1, rptId);
						stmt.setInt(2, idx);
						stmt.setString(3, item.getInTime());
						stmt.setString(4, item.getOutTime());
						stmt.setInt(5, item.getCustomer().getCustomerId());
						stmt.setString(6, item.getRemark());
						stmt.setString(7, item.getType());
						stmt.setString(8, item.getEditable());
						stmt.setDouble(9, item.getLodging());
						stmt.setDouble(10, item.getFood());
						stmt.setDouble(11, item.getRailbus());
						stmt.setDouble(12, item.getLocaltransport());
						stmt.setDouble(13, item.getCourier());
						stmt.setDouble(14, item.getOthers());
					} else {
						stmt = getConnection().prepareStatement(insertOtherItemDtls);
						stmt.setLong(1, rptId);
						stmt.setInt(2, idx);
						stmt.setString(3, item.getInTime());
						stmt.setString(4, item.getOutTime());
						stmt.setString(5, item.getRemark());
						stmt.setString(6, item.getType());
						stmt.setString(7, item.getEditable());
						stmt.setDouble(8, item.getLodging());
						stmt.setDouble(9, item.getFood());
						stmt.setDouble(10, item.getRailbus());
						stmt.setDouble(11, item.getLocaltransport());
						stmt.setDouble(12, item.getCourier());
						stmt.setDouble(13, item.getOthers());
					}
					
					stmt.executeUpdate();
					
					msg = new Message(SUCCESS, "Added Successfully.");
					model.addNewMessage(msg);
				}
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel delItm(String index, String rptId){
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String getRptId = "DELETE FROM DAILY_REPORT_DTLS WHERE RPTID = ? AND IDX = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			stmt.setString(1, rptId);
			stmt.setString(2, index);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Deleted Successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while deleting the entry.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel submitApproval(String rptId) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String getRptId = "UPDATE DAILY_REPORT_MASTER SET STATUS = 'Awaiting Approval', SENTDATE = CURRENT_TIMESTAMP WHERE RPTID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			stmt.setString(1, rptId);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Submit for approval Successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while submitting report for approval.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public SalesmanDlyRptModel[] getPendingApprovalReport(UserModel userModel) {
		ArrayList<SalesmanDlyRptModel> list = new ArrayList<SalesmanDlyRptModel>(0);
		SalesmanDlyRptModel temp = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get rpt id
		String getRptId = "SELECT B.USERID, A.RPTID, A.RPTDT, B.USERNAME, A.STATUS, A.SENTDATE FROM DAILY_REPORT_MASTER A, USER B WHERE B.MANAGER = ? AND A.STATUS = 'Awaiting Approval' AND A.SALESMANID = B.USERID";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			stmt.setString(1, userModel.getUserId());
			
			rs = stmt.executeQuery();

			while(rs.next()){
				temp = new SalesmanDlyRptModel();
				temp.setRptDt(rs.getString("RPTDT"));
				temp.setRptId(rs.getInt("RPTID"));
				temp.setStatus(rs.getString("STATUS"));
				temp.getUser().setUserId(rs.getString("USERID"));
				temp.getUser().setUserName(rs.getString("USERNAME"));
				temp.setSentDt(rs.getString("SENTDATE"));
				list.add(temp);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return (SalesmanDlyRptModel[])list.toArray(new SalesmanDlyRptModel[0]);
	}

	public MessageModel approveReport(String rptId, UserModel user) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String getRptId = "UPDATE DAILY_REPORT_MASTER SET STATUS = 'Approved', APPROVEDBY = ? WHERE RPTID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, rptId);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Report approved Successfully.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occered while approving report.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public MessageModel rejectReport(String rptId, String remark) {
		MessageModel model = new MessageModel();
		Message msg = null;

		PreparedStatement stmt = null;
		
		// query to get rpt id
		String getRptId = "UPDATE DAILY_REPORT_MASTER SET STATUS = 'Rejected', REMARK = ? WHERE RPTID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getRptId);
			stmt.setString(1, remark);
			stmt.setString(2, rptId);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Report Rejected.");
				model.addNewMessage(msg);
			} else {
				msg = new Message(ALERT, "Error occured while rejecting report.");
				model.addNewMessage(msg);
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
			try{
				getConnection().rollback();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		
		return model;
	}

	public ArrayList<String[]> getStatusReport(String userId, String fromDate,
			String toDate, String status) {
		ArrayList<String[]> data = new ArrayList<String[]>(0);
		ArrayList<String[]> tempData = new ArrayList<String[]>(0);
		String[] temp = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		// query to get the report details
		String reportQuery = "SELECT RPTDT, STATUS, SENTDATE, APPROVEDBY, RPTDT, SALESMANID FROM DAILY_REPORT_MASTER WHERE SALESMANID = ? AND RPTDT >= ? AND RPTDT <= ? AND (STATUS = 'APPROVED' OR STATUS = 'AWAITING APPROVAL') ORDER BY RPTDT";
		
		String getUserAttendance = "SELECT TYPE, OT, INTIME, OUTTIME, LATE, PANELTY, REMARK FROM USER_ATTENDANCE WHERE USERID = ? AND ATTDATE = ?";
		
		try{
			
			stmt1 = getConnection().prepareStatement(getUserAttendance);
			
			stmt = getConnection().prepareStatement(reportQuery);
			stmt.setString(1, userId);
			stmt.setString(2, Utils.convertToSQLDate(fromDate));
			stmt.setString(3, Utils.convertToSQLDate(toDate));
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				temp = new String[5];
				temp[0] = rs.getString(1);
				temp[1] = rs.getString(2);
				temp[2] = rs.getString(3);
				temp[3] = rs.getString(4);
				stmt1.setString(1, rs.getString("SALESMANID"));
				stmt1.setString(2, rs.getString("RPTDT"));
				rs1 = stmt1.executeQuery();
				if(rs1.next()) {
					temp[4] = (rs1.getInt("TYPE") == 0) ? "FULL DAY" : ((rs1.getInt("TYPE") == 1) ? "HALF DAY" : ((rs1.getInt("TYPE") == 2) ? "LEAVE" : "HOLIDAY" ));
				} else {
					temp[4] = "NA";
				}
				tempData.add(temp);
			}
			
			Iterator<String[]> tempItr = tempData.iterator();
			ArrayList<String> tmp = new ArrayList<String>(0);
			
			if(status.equals("2")){
				while(tempItr.hasNext()){
					temp = tempItr.next();
					if(temp[1].equals("Approved")){
						data.add(temp);
					}
				}
			} else if(status.equals("1")){
				while(tempItr.hasNext()){
					temp = tempItr.next();
					if(temp[1].equals("Awaiting Approval")){
						data.add(temp);
					}
				}
			} else if(status.equals("0")){
				Date fromDt = (new SimpleDateFormat("dd-MM-yyyy")).parse(fromDate);
				Date toDt = (new SimpleDateFormat("dd-MM-yyyy")).parse(toDate);
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date tempDt = fromDt;
				Calendar c = Calendar.getInstance();
				c.setTime(tempDt);
				while(tempItr.hasNext()){
					temp = tempItr.next();
					tmp.add(temp[0]);
				}
				while(tempDt.getTime() <= toDt.getTime()){
					if(tmp.indexOf(sf.format(tempDt)) == -1){
						temp = new String[5];
						temp[0] = sf.format(tempDt);
						temp[1] = "Open Or Rejected";
						temp[2] = "--";
						temp[3] = "--";
						stmt1.setString(1, userId);
						stmt1.setString(2, sf.format(tempDt));
						rs1 = stmt1.executeQuery();
						if(rs1.next()) {
							temp[4] = (rs1.getInt("TYPE") == 0) ? "FULL DAY" : ((rs1.getInt("TYPE") == 1) ? "HALF DAY" : ((rs1.getInt("TYPE") == 2) ? "LEAVE" : "HOLIDAY" ));
						} else {
							temp[4] = "NA";
						}
						data.add(temp);
					}
					c.setTime(tempDt);
					c.add(Calendar.DATE, 1);
					tempDt = (new SimpleDateFormat("dd-MM-yyyy")).parse((new SimpleDateFormat("dd-MM-yyyy")).format(c.getTime()));
				}
			} else {
				Date fromDt = (new SimpleDateFormat("dd-MM-yyyy")).parse(fromDate);
				Date toDt = (new SimpleDateFormat("dd-MM-yyyy")).parse(toDate);
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date tempDt = fromDt;
				Calendar c = Calendar.getInstance();
				c.setTime(tempDt);
				while(tempItr.hasNext()){
					temp = tempItr.next();
					tmp.add(temp[0]);
				}
				while(tempDt.getTime() <= toDt.getTime()){
					if(tmp.indexOf(sf.format(tempDt)) == -1){
						temp = new String[5];
						temp[0] = sf.format(tempDt);
						temp[1] = "Open Or Rejected";
						temp[2] = "--";
						temp[3] = "--";
						stmt1.setString(1, userId);
						stmt1.setString(2, sf.format(tempDt));
						rs1 = stmt1.executeQuery();
						if(rs1.next()) {
							temp[4] = (rs1.getInt("TYPE") == 0) ? "FULL DAY" : ((rs1.getInt("TYPE") == 1) ? "HALF DAY" : ((rs1.getInt("TYPE") == 2) ? "LEAVE" : "HOLIDAY" ));
						} else {
							temp[4] = "NA";
						}
						data.add(temp);
					} else {
						temp = tempData.get(tmp.indexOf(sf.format(tempDt)));
						data.add(temp);
					}
					c.setTime(tempDt);
					c.add(Calendar.DATE, 1);
					tempDt = (new SimpleDateFormat("dd-MM-yyyy")).parse((new SimpleDateFormat("dd-MM-yyyy")).format(c.getTime()));
				}
			}
			
			getConnection().commit();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return data;
	}

}
