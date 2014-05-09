package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalaryModel;
import com.en.model.TargetModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class UserBroker extends BaseBroker {

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public UserBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new User
	 * 
	 * @param UserModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addUser(UserModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of userid
		String checkUserId = "SELECT COUNT(*) FROM USER WHERE USERID = ?";
		
		// query to check the existence of username
		String checkUserName = "SELECT COUNT(*) FROM USER WHERE USERNAME = ?";
		
		// query to add the user details
		String insertUser = "INSERT INTO USER (USERID, PASSWORD, USERNAME, MANAGER, BRANCH, TYPE, MOBILE1, PHONE1, EMAIL1, MOBILE2,"
				+ "PHONE2, EMAIL2, SALARY, DOB, BLOODGROUP, QUALIFICATION, IDENTITYMARK, PRESENTADDRESS, PERMANENTADDRESS, FATHERNAME, MOTHERNAME, "
				+ "SPOUSENAME, DOA, CHILD1, CHILD2, CHILD3, CHILD4, COMPANY, PERIOD, DETAILS, BANKNAME1, BANKBRANCH1, BANKAC1, BANKIFSC1,"
				+ "BANKNAME2, BANKBRANCH2, BANKAC2, BANKIFSC2, EXPENSE1, EXPENSE2, EXPENSE3, EXPENSE4, EXPENSE5, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'A')";
		
		try{
			
			stmt = getConnection().prepareStatement(checkUserId);
			stmt.setString(1, model.getUserId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, model.getUserId()+" already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(checkUserName);
				stmt.setString(1, model.getUserName());
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getInt(1) > 0){
					msg = new Message(ALERT, "User Name : "+model.getUserName()+" already exists.");
					messages.addNewMessage(msg);
				} else {
				
					stmt = getConnection().prepareStatement(insertUser);
					stmt.setString(1, model.getUserId());
					stmt.setString(2, model.getPassword());
					stmt.setString(3, model.getUserName());
					stmt.setString(4, model.getManager());
					stmt.setInt(5, model.getBranch().getAccessId());
					stmt.setInt(6, model.getType());
					stmt.setString(7, model.getMobile1());
					stmt.setString(8, model.getPhone1());
					stmt.setString(9, model.getEmail1());
					stmt.setString(10, model.getMobile2());
					stmt.setString(11, model.getPhone2());
					stmt.setString(12, model.getEmail2());
					stmt.setString(13, model.getSalary());
					stmt.setString(14, model.getDob());
					stmt.setString(15, model.getBloodGroup());
					stmt.setString(16, model.getQualification());
					stmt.setString(17, model.getPersonalIdentityMark());
					stmt.setString(18, model.getPresentAddress());
					stmt.setString(19, model.getAddress());
					stmt.setString(20, model.getFatherName());
					stmt.setString(21, model.getMotherName());
					stmt.setString(22, model.getSpouseName());
					stmt.setString(23, model.getDoa());
					stmt.setString(24, model.getChild1());
					stmt.setString(25, model.getChild2());
					stmt.setString(26, model.getChild3());
					stmt.setString(27, model.getChild4());
					stmt.setString(28, model.getPastCompany());
					stmt.setString(29, model.getPeriod());
					stmt.setString(30, model.getDetails());
					stmt.setString(31, model.getBankName1());
					stmt.setString(32, model.getBankBranch1());
					stmt.setString(33, model.getBankAc1());
					stmt.setString(34, model.getBankIfsc1());
					stmt.setString(35, model.getBankName2());
					stmt.setString(36, model.getBankBranch2());
					stmt.setString(37, model.getBankAc2());
					stmt.setString(38, model.getBankIfsc2());
					stmt.setDouble(39, model.getExpense1());
					stmt.setDouble(40, model.getExpense2());
					stmt.setDouble(41, model.getExpense3());
					stmt.setDouble(42, model.getExpense4());
					stmt.setDouble(43, model.getExpense5());
					
					if(stmt.executeUpdate() > 0){
						msg = new Message(SUCCESS, "User added successfully.");
					} else {
						msg = new Message(ALERT, "Error occured while adding an User.");
					}
					messages.addNewMessage(msg);
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
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
		
		return messages;
	}
	
	/**
	 * Method to get list all the available users
	 *  
	 * @return array of UserModel
	 */
	public UserModel[] getUsers(){
		ArrayList<UserModel> users = new ArrayList<UserModel>(0);
		UserModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getUsers = "SELECT USERID, USERNAME FROM USER WHERE STATUS = 'A' ORDER BY USERNAME";
		
		try{
			
			stmt = getConnection().prepareStatement(getUsers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new UserModel();
				model.setUserId(rs.getString("USERID"));
				model.setUserName(rs.getString("USERNAME"));
				users.add(model);
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
		
		return (UserModel[]) users.toArray(new UserModel[0]);
	}
	
	/**
	 * Method to get the details of the selected userId
	 * 
	 * @param userIdId
	 * @return UserModel with user details
	 */
	public UserModel getUserDtls(String userId) {
		UserModel model = new UserModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the user
		String getUserDtls = "SELECT USERID, PASSWORD, USERNAME, MANAGER, BRANCH, TYPE, MOBILE1, PHONE1, EMAIL1, MOBILE2,"
				+ "PHONE2, SALARY, EMAIL2, DOB, BLOODGROUP, QUALIFICATION, IDENTITYMARK, PRESENTADDRESS, PERMANENTADDRESS, FATHERNAME, MOTHERNAME, "
				+ "SPOUSENAME, DOA, CHILD1, CHILD2, CHILD3, CHILD4, COMPANY, PERIOD, DETAILS, BANKNAME1, BANKBRANCH1, BANKAC1, BANKIFSC1,"
				+ "BANKNAME2, BANKBRANCH2, BANKAC2, BANKIFSC2, EXPENSE1, EXPENSE2, EXPENSE3, EXPENSE4, EXPENSE5, STATUS FROM USER " +
				"WHERE USERID = ?";

		try{
			
			stmt = getConnection().prepareStatement(getUserDtls);
			stmt.setString(1, userId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setUserId(rs.getString("USERID"));
				model.setPassword(rs.getString("PASSWORD"));
				
				model.setUserName(rs.getString("USERNAME"));	
				model.setManager(rs.getString("MANAGER"));
				model.getBranch().setAccessId(rs.getInt("BRANCH"));
				model.setType(rs.getInt("TYPE"));
				
				model.setMobile1(rs.getString("MOBILE1"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setEmail1(rs.getString("EMAIL1"));
				model.setSalary(rs.getString("SALARY"));
				
				model.setDob(rs.getString("DOB"));
				model.setBloodGroup(rs.getString("BLOODGROUP"));
				model.setMobile2(rs.getString("MOBILE2"));
				model.setPhone2(rs.getString("PHONE2"));
				model.setEmail2(rs.getString("EMAIL2"));
				model.setQualification(rs.getString("QUALIFICATION"));
				model.setPersonalIdentityMark(rs.getString("IDENTITYMARK"));
				model.setPresentAddress(rs.getString("PRESENTADDRESS"));
				model.setAddress(rs.getString("PERMANENTADDRESS"));
				model.setFatherName(rs.getString("FATHERNAME"));
				model.setMotherName(rs.getString("MOTHERNAME"));
				model.setSpouseName(rs.getString("SPOUSENAME"));
				model.setDoa(rs.getString("DOA"));
				model.setChild1(rs.getString("CHILD1"));
				model.setChild2(rs.getString("CHILD2"));
				model.setChild3(rs.getString("CHILD3"));
				model.setChild4(rs.getString("CHILD4"));
				
				model.setPastCompany(rs.getString("COMPANY"));
				model.setPeriod(rs.getString("PERIOD"));
				model.setDetails(rs.getString("DETAILS"));
				
				model.setBankName1(rs.getString("BANKNAME1"));
				model.setBankBranch1(rs.getString("BANKBRANCH1"));
				model.setBankAc1(rs.getString("BANKAC1"));
				model.setBankIfsc1(rs.getString("BANKIFSC1"));
				model.setBankName2(rs.getString("BANKNAME2"));
				model.setBankBranch2(rs.getString("BANKBRANCH2"));
				model.setBankAc2(rs.getString("BANKAC2"));
				model.setBankIfsc2(rs.getString("BANKIFSC2"));
				
				model.setExpense1(rs.getDouble("EXPENSE1"));
				model.setExpense2(rs.getDouble("EXPENSE2"));
				model.setExpense3(rs.getDouble("EXPENSE3"));
				model.setExpense4(rs.getDouble("EXPENSE4"));
				model.setExpense5(rs.getDouble("EXPENSE5"));
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return model;
	}
	
	/**
	 * Method to update an existing user
	 * 
	 * @param UserModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateUser(UserModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of username
		String checkUserName = "SELECT COUNT(*) FROM USER WHERE USERNAME = ? AND USERID <> ?";
		
		// query to update the user details
		String updateUser = "UPDATE USER SET USERNAME = ?, MANAGER = ?, BRANCH = ?, TYPE = ?, MOBILE1 = ?, PHONE1 = ?, EMAIL1 = ?, MOBILE2 = ?,"
				+ "PHONE2 = ?, EMAIL2 = ?, SALARY = ?, DOB = ?, BLOODGROUP = ?, QUALIFICATION = ?, IDENTITYMARK = ?, PRESENTADDRESS = ?, PERMANENTADDRESS = ?, FATHERNAME = ?, MOTHERNAME = ?, "
				+ "SPOUSENAME = ?, DOA = ?, CHILD1 = ?, CHILD2 = ?, CHILD3 = ?, CHILD4 = ?, COMPANY = ?, PERIOD = ?, DETAILS = ?, BANKNAME1 = ?, BANKBRANCH1 = ?, BANKAC1 = ?, BANKIFSC1 = ?,"
				+ "BANKNAME2 = ?, BANKBRANCH2 = ?, BANKAC2 = ?, BANKIFSC2 = ?, EXPENSE1 = ?, EXPENSE2 = ?, EXPENSE3 = ?, EXPENSE4 = ?, EXPENSE5 = ? " +
				" WHERE USERID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(checkUserName);
			stmt.setString(1, model.getUserName());
			stmt.setString(2, model.getUserId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, "User Name : "+ model.getUserName()+" already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateUser);
				stmt.setString(1, model.getUserName());
				stmt.setString(2, model.getManager());
				stmt.setInt(3, model.getBranch().getAccessId());
				stmt.setInt(4, model.getType());
				stmt.setString(5, model.getMobile1());
				stmt.setString(6, model.getPhone1());
				stmt.setString(7, model.getEmail1());
				stmt.setString(8, model.getMobile2());
				stmt.setString(9, model.getPhone2());
				stmt.setString(10, model.getEmail2());
				stmt.setString(11, model.getSalary());
				stmt.setString(12, model.getDob());
				stmt.setString(13, model.getBloodGroup());
				stmt.setString(14, model.getQualification());
				stmt.setString(15, model.getPersonalIdentityMark());
				stmt.setString(16, model.getPresentAddress());
				stmt.setString(17, model.getAddress());
				stmt.setString(18, model.getFatherName());
				stmt.setString(19, model.getMotherName());
				stmt.setString(20, model.getSpouseName());
				stmt.setString(21, model.getDoa());
				stmt.setString(22, model.getChild1());
				stmt.setString(23, model.getChild2());
				stmt.setString(24, model.getChild3());
				stmt.setString(25, model.getChild4());
				stmt.setString(26, model.getPastCompany());
				stmt.setString(27, model.getPeriod());
				stmt.setString(28, model.getDetails());
				stmt.setString(29, model.getBankName1());
				stmt.setString(30, model.getBankBranch1());
				stmt.setString(31, model.getBankAc1());
				stmt.setString(32, model.getBankIfsc1());
				stmt.setString(33, model.getBankName2());
				stmt.setString(34, model.getBankBranch2());
				stmt.setString(35, model.getBankAc2());
				stmt.setString(36, model.getBankIfsc2());
				stmt.setDouble(37, model.getExpense1());
				stmt.setDouble(38, model.getExpense2());
				stmt.setDouble(39, model.getExpense3());
				stmt.setDouble(40, model.getExpense4());
				stmt.setDouble(41, model.getExpense5());
				stmt.setString(42, model.getUserId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "User modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying an User.");
				}
				messages.addNewMessage(msg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
			try{
				if(stmt != null)
					stmt.close();
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
		
		return messages;
	}

	public UserModel[] getSalemanUsers() {
		ArrayList<UserModel> users = new ArrayList<UserModel>(0);
		UserModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getUsers = "SELECT A.USERID, A.USERNAME FROM USER A, USER_ACCESS B WHERE A.USERID = B.USERID AND A.USERID <> 'admin' AND B.ACCESSID = 1 AND A.STATUS = 'A' ORDER BY A.USERNAME";
		
		try{
			
			stmt = getConnection().prepareStatement(getUsers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new UserModel();
				model.setUserId(rs.getString("USERID"));
				model.setUserName(rs.getString("USERNAME"));
				users.add(model);
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
		
		return (UserModel[]) users.toArray(new UserModel[0]);
	}

	public MessageModel updatePassword(String userId, String pwd) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to update the user details
		String updateUser = "UPDATE USER SET PASSWORD = ?" +
				" WHERE USERID = ?";
		
		try{
				
			stmt = getConnection().prepareStatement(updateUser);
			stmt.setString(1, pwd);
			stmt.setString(2, userId);
			
			if(stmt.executeUpdate() > 0){
				msg = new Message(SUCCESS, "User password modified successfully.");
			} else {
				msg = new Message(ALERT, "Error occured while modifying an User password.");
			}
			messages.addNewMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new Message(ERROR, e.getMessage());
			messages.addNewMessage(msg);
			try{
				if(stmt != null)
					stmt.close();
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
		
		return messages;
	}

	public String[][] getUserDocumentList(String userId) {
		ArrayList<String[]> users = new ArrayList<String[]>(0);
		String[] temp = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getUsers = "SELECT DOCNAME, FILENAME FROM USER_DOCUMENT WHERE USERID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(getUsers);
			stmt.setString(1, userId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				temp = new String[2];
				temp[0] = rs.getString("DOCNAME");
				temp[1] = rs.getString("FILENAME");
				users.add(temp);
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
		
		return (String[][]) users.toArray(new String[0][0]);
	}

	public void uploadUserDocument(String userId, String documentName,
			String fileName) {
		
		PreparedStatement stmt = null;
		
		// query to get all the existing item group
		String getUsers = "INSERT INTO USER_DOCUMENT (USERID, DOCNAME, FILENAME) VALUES (?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(getUsers);
			stmt.setString(1, userId);
			stmt.setString(2, documentName);
			stmt.setString(3, fileName);
			
			stmt.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
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
		
		return;
	}

	public UserModel[] getManagerUsers(UserModel manager) {
		ArrayList<UserModel> users = new ArrayList<UserModel>(0);
		UserModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing item group
		String getUsers = "SELECT A.USERID, A.USERNAME FROM USER A WHERE A.MANAGER = ? AND A.STATUS = 'A' ORDER BY A.USERNAME";
		
		try{
			
			stmt = getConnection().prepareStatement(getUsers);
			stmt.setString(1, manager.getUserId());
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new UserModel();
				model.setUserId(rs.getString("USERID"));
				model.setUserName(rs.getString("USERNAME"));
				users.add(model);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return (UserModel[]) users.toArray(new UserModel[0]);
	}

	public SalaryModel getSalaryDetails(UserModel user, int month, int year) {
		SalaryModel model = new SalaryModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String salaryQuery = "SELECT SALARY, PRESENTDAYS, ABSENTDAYS, HOLIDAYS, LATEFINES, PENALTY, OT, PAIDON, EXPENSE1, EXPENSE2, EXPENSE3, EXPENSE4, EXPENSE5, ADVANCE FROM STAFF_SALARY WHERE USER = ? AND MONTH = ? AND YEAR = ?";
		
		String attendanceQuery = "SELECT TYPE, OT, INTIME, OUTTIME, LATE, PANELTY FROM USER_ATTENDANCE WHERE USERID = ? AND MONTH(ATTDATE) = ? AND YEAR(ATTDATE) = ?";
		
		try{
			
			model.setUser(getUserDtls(user.getUserId()));
			model.setMonth(month);
			model.setYear(year);
			
			stmt = getConnection().prepareStatement(salaryQuery);
			stmt.setString(1, user.getUserId());
			stmt.setInt(2, month);
			stmt.setInt(3, year);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				
				model.getUser().setSalary(rs.getString("SALARY"));
				model.setPresentDays(rs.getDouble("PRESENTDAYS"));
				model.setLeaveDays(rs.getDouble("ABSENTDAYS"));
				model.setHoliDays(rs.getDouble("HOLIDAYS"));
				model.setLatePenalty(rs.getDouble("LATEFINES"));
				model.setPenalty(rs.getDouble("PENALTY"));
				model.setOt(rs.getDouble("OT"));
				model.setPaidOn(rs.getString("PAIDON"));
				model.setExpense1(rs.getDouble("EXPENSE1"));
				model.setExpense2(rs.getDouble("EXPENSE2"));
				model.setExpense3(rs.getDouble("EXPENSE3"));
				model.setExpense4(rs.getDouble("EXPENSE4"));
				model.setExpense5(rs.getDouble("EXPENSE5"));
				model.setAdvanceAdjust(rs.getDouble("ADVANCE"));
				
			} else {
				
				double presentDays = 0;
				double absentDays = 0;
				double holidays = 0;
				double lateFines = 0;
				double penalty = 0;
				double ot = 0;
				
				stmt = getConnection().prepareStatement(attendanceQuery);
				stmt.setString(1, user.getUserId());
				stmt.setInt(2, month);
				stmt.setInt(3, year);
				
				rs = stmt.executeQuery();
				
				while(rs.next()){
					if(rs.getInt("TYPE") == 0 || rs.getInt("TYPE") == 1) {
						presentDays = presentDays + ((rs.getInt("TYPE") == 0) ? 1 : 0.5);
						absentDays = absentDays + ((rs.getInt("TYPE") == 0) ? 0 : 0.5);
					} else if(rs.getInt("TYPE") == 3)
						holidays = holidays + 1;
					 else 
						absentDays = absentDays + 1;
					lateFines = lateFines + rs.getDouble("LATE");
					penalty = penalty + rs.getDouble("PANELTY");
					ot = ot + rs.getDouble("OT");
				}
				model.setPresentDays(presentDays);
				model.setLeaveDays(absentDays);
				model.setHoliDays(holidays);
				model.setLatePenalty(lateFines);
				model.setPenalty(penalty);
				model.setOt(ot);
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
		
		return model;
	}

	public TargetModel[] getUserTargets(String fromDate, String toDate) {
		ArrayList<TargetModel> lst = new ArrayList<TargetModel>(0);
		UserModel user = null;
		TargetModel model = null;
//		double salesAmt = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
//		PreparedStatement stmt2 = null;
//		ResultSet rs2 = null;
		
		String getUserDtls = "SELECT USERID, PASSWORD, USERNAME, MANAGER, BRANCH, TYPE, MOBILE1, PHONE1, EMAIL1, MOBILE2,"
				+ "PHONE2, SALARY, EMAIL2, DOB, BLOODGROUP, QUALIFICATION, IDENTITYMARK, PRESENTADDRESS, PERMANENTADDRESS, FATHERNAME, MOTHERNAME, "
				+ "SPOUSENAME, DOA, CHILD1, CHILD2, CHILD3, CHILD4, COMPANY, PERIOD, DETAILS, BANKNAME1, BANKBRANCH1, BANKAC1, BANKIFSC1,"
				+ "BANKNAME2, BANKBRANCH2, BANKAC2, BANKIFSC2, STATUS FROM USER " +
				"WHERE TYPE = 3";
		
		String getTargetDetails = "SELECT TARGET, COMMISSION FROM STAFF_TARGET WHERE USERID = ? AND FROMDATE = ? AND TODATE = ?";
		
//		String getSalesAmount = "SELECT A.SALESID, SUM(C.QTY*((100+C.TAX)*((100-C.DISRATE)*C.RATE/100)/100)) AS AMT, A.PACKING, A.FORWARD, A.INSTALLATION, A.LESS " +
//				"FROM SALES A, SALES_ITEM C " +
//				"WHERE A.SALESID = C.SALESID " +
//				"AND A.BRANCHID = C.BRANCHID " +
//				"AND A.SALESMANID = ? " +
//				"AND A.SALESDATE >= ? " +
//				"AND A.SALESDATE <= ? " +	
//				"GROUP BY A.SALESID " +	
//				"ORDER BY A.SALESID";
		
		try{
			
			stmt = getConnection().prepareStatement(getUserDtls);
			stmt1 = getConnection().prepareStatement(getTargetDetails);
//			stmt2 = getConnection().prepareStatement(getSalesAmount);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
//				salesAmt = 0;
				
				model = new TargetModel();
				user = new UserModel();
				user.setUserId(rs.getString("USERID"));
				user.setPassword(rs.getString("PASSWORD"));
				
				user.setUserName(rs.getString("USERNAME"));	
				user.setManager(rs.getString("MANAGER"));
				user.getBranch().setAccessId(rs.getInt("BRANCH"));
				user.setType(rs.getInt("TYPE"));
				
				user.setMobile1(rs.getString("MOBILE1"));
				user.setPhone1(rs.getString("PHONE1"));
				user.setEmail1(rs.getString("EMAIL1"));
				user.setSalary(rs.getString("SALARY"));
				
				user.setDob(rs.getString("DOB"));
				user.setBloodGroup(rs.getString("BLOODGROUP"));
				user.setMobile2(rs.getString("MOBILE2"));
				user.setPhone2(rs.getString("PHONE2"));
				user.setEmail2(rs.getString("EMAIL2"));
				user.setQualification(rs.getString("QUALIFICATION"));
				user.setPersonalIdentityMark(rs.getString("IDENTITYMARK"));
				user.setPresentAddress(rs.getString("PRESENTADDRESS"));
				user.setAddress(rs.getString("PERMANENTADDRESS"));
				user.setFatherName(rs.getString("FATHERNAME"));
				user.setMotherName(rs.getString("MOTHERNAME"));
				user.setSpouseName(rs.getString("SPOUSENAME"));
				user.setDoa(rs.getString("DOA"));
				user.setChild1(rs.getString("CHILD1"));
				user.setChild2(rs.getString("CHILD2"));
				user.setChild3(rs.getString("CHILD3"));
				user.setChild4(rs.getString("CHILD4"));
				
				user.setPastCompany(rs.getString("COMPANY"));
				user.setPeriod(rs.getString("PERIOD"));
				user.setDetails(rs.getString("DETAILS"));
				
				user.setBankName1(rs.getString("BANKNAME1"));
				user.setBankBranch1(rs.getString("BANKBRANCH1"));
				user.setBankAc1(rs.getString("BANKAC1"));
				user.setBankIfsc1(rs.getString("BANKIFSC1"));
				user.setBankName2(rs.getString("BANKNAME2"));
				user.setBankBranch2(rs.getString("BANKBRANCH2"));
				user.setBankAc2(rs.getString("BANKAC2"));
				user.setBankIfsc2(rs.getString("BANKIFSC2"));

				model.setUser(user);
				
				stmt1.setString(1, user.getUserId());
				stmt1.setString(2, fromDate);
				stmt1.setString(3, toDate);
				
				rs1 = stmt1.executeQuery();
				
				if(rs1.next()){
					model.setTarget(rs1.getDouble("TARGET"));
					model.setCommission(rs1.getDouble("COMMISSION"));
				}
				
//				stmt2.setString(1, user.getUserId());
//				stmt2.setString(2, fromDate);
//				stmt2.setString(3, toDate);
//				
//				rs2= stmt2.executeQuery();
//				
//				while(rs2.next()){
//					salesAmt = rs2.getDouble("AMT") + rs2.getDouble("PACKING") + rs2.getDouble("FORWARD") + rs2.getDouble("INSTALLATION") - rs2.getDouble("LESS");
//				}
//				model.setSalesAmt(salesAmt);
				
				lst.add(model);
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
		
		return (TargetModel[]) lst.toArray(new TargetModel[0]);
	}

	public MessageModel updateTarget(ArrayList<TargetModel> lst,
			String fromDate, String toDate) {
		MessageModel msgs = new MessageModel();
		Message msg = null;
		TargetModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to delete the preivous details
		String deleteDetails = "DELETE FROM STAFF_TARGET WHERE FROMDATE = ? AND TODATE = ?";
		
		// query to insert the new details
		String insertDetails = "INSERT INTO STAFF_TARGET (USERID, FROMDATE, TODATE, TARGET, COMMISSION) VALUES (?,?,?,?,?)";
		
		try {
				
				stmt = getConnection().prepareStatement(deleteDetails);
				
				stmt.setString(1, fromDate);
				stmt.setString(2, toDate);

				stmt.execute();
					
				stmt = getConnection().prepareStatement(insertDetails);
				
				Iterator<TargetModel> itr = lst.iterator();
				
				while(itr.hasNext()){
					model = itr.next();
					stmt.setString(1, model.getUser().getUserId());
					stmt.setString(2, fromDate);
					stmt.setString(3, toDate);
					stmt.setDouble(4, model.getTarget());
					stmt.setDouble(5, model.getCommission());
					stmt.addBatch();
				}
				
				stmt.executeBatch();
				
				getConnection().commit();
				
				msg = new Message(SUCCESS, "Target details updated successfully!!");
				msgs.addNewMessage(msg);
				
		} catch (Exception e) {
			msg = new Message(ERROR, "Error occured while updating target details!!");
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

	public double getPendingTarget(UserModel user, String fromDate, String toDate) {
		double pendingTarget = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to delete the preivous details
		String getTargetDetails = "SELECT TARGET FROM STAFF_TARGET WHERE USERID = ? AND FROMDATE = ? AND TODATE = ?";// query to check the existence of the bill no
		
		
		String getSalesDtls = "SELECT A.SALESID, A.BRANCHID, SUM(C.QTY*(((100-C.DISRATE)*C.RATE/100))) AS AMT " +
				"FROM SALES A, SALES_ITEM C " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.SALESMANID = ? " +
				"AND A.SALESDATE >= ? " +
				"AND A.SALESDATE <= ? "; 
		getSalesDtls	= getSalesDtls +	"GROUP BY A.SALESID, A.BRANCHID ";
		getSalesDtls	= getSalesDtls +	"ORDER BY A.BRANCHID, A.SALESID";
		
		try {
				
				stmt = getConnection().prepareStatement(getTargetDetails);
				
				stmt.setString(1, user.getUserId());
				stmt.setString(2, fromDate);
				stmt.setString(3, toDate);

				rs = stmt.executeQuery();
				
				if(rs.next()){
					pendingTarget =  - rs.getDouble("TARGET");
				}
				
				stmt = getConnection().prepareStatement(getSalesDtls);
				
				stmt.setString(1, user.getUserId());
				stmt.setString(2, fromDate);
				stmt.setString(3, toDate);

				rs = stmt.executeQuery();
				
				while(rs.next()){
					pendingTarget = pendingTarget + rs.getDouble("AMT");
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
		
		return pendingTarget;
	}

	public double getTotalAdvance(UserModel user, int month, int year) {
		double total = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String date = year+"-"+(month < 10 ? "0"+month : month)+"-01";
		
		// query to delete the previous access details
		String getAdvancesRecevied = "SELECT SUM(A.AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER B WHERE A.STAFF = ? AND A.TYPE = 58 AND A.ID = B.ID AND B.DAY < ? ";
		
		// query to delete the previous access details
		String getAdvancesReturned = "SELECT SUM(A.AMOUNT) FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER B WHERE A.STAFF = ? AND A.TYPE = 10 AND A.ID = B.ID AND B.DAY < ? ";
		
		
		try{
				
			stmt = getConnection().prepareStatement(getAdvancesRecevied);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				total = rs.getDouble(1);
			}
			
			stmt = getConnection().prepareStatement(getAdvancesReturned);
			stmt.setString(1, user.getUserId());
			stmt.setString(2, date);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				total = total - rs.getDouble(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
				if(stmt != null)
					stmt.close();
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
		
		return total;
	}

	public EntryModel[] getAdvanceEntries(UserModel user, int month, int year) {
		ArrayList<EntryModel> list = new ArrayList<EntryModel>(0);
		EntryModel entry = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get the details
		String getItemList = "SELECT C.DAY, A.TYPE, A.REMARK, A.STAFF, A.AMOUNT, D.NAME AS BRANCHNAME, D.CITY AS BRANCHCITY FROM BRANCH_DAY_BOOK_ENTRIES A, BRANCH_DAY_BOOK_MASTER C, ACCESS_POINT D  WHERE A.ID = C.ID AND (A.TYPE = 10 OR A.TYPE = 58) AND C.BRANCH = D.ACCESSID AND A.STAFF = ? AND MONTH(C.DAY) = ? AND YEAR(C.DAY) = ? ORDER BY C.DAY";
		
		try{
			stmt = getConnection().prepareStatement(getItemList);
			stmt.setString(1, user.getUserId());
			stmt.setInt(2, month);
			stmt.setInt(3, year);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				entry = new EntryModel();
				entry.setAmount(rs.getDouble("AMOUNT"));
				entry.setEntryType(rs.getInt("TYPE")); 
				entry.setRemark(rs.getString("REMARK"));
				entry.setEntryDate(rs.getString("DAY"));
				entry.setStaff(getUserDtls(rs.getString("STAFF")));
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

	public TargetModel getTargetDetails(UserModel user) {
		TargetModel model = new TargetModel();
		double salesAmt = 0;
		String[] dates = DateUtil.getCurrentQuarterDate(new GregorianCalendar());
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;
		
		String getTargetDetails = "SELECT TARGET, COMMISSION FROM STAFF_TARGET WHERE USERID = ? AND FROMDATE = ? AND TODATE = ?";
		
		String getSalesAmount = "SELECT A.SALESID, SUM(C.QTY*(((100-C.DISRATE)*C.RATE/100))) AS AMT " +
				"FROM SALES A, SALES_ITEM C " +
				"WHERE A.SALESID = C.SALESID " +
				"AND A.BRANCHID = C.BRANCHID " +
				"AND A.SALESMANID = ? " +
				"AND A.SALESDATE >= ? " +
				"AND A.SALESDATE <= ? " +	
				"GROUP BY A.SALESID " +	
				"ORDER BY A.SALESID";
		
		try{
			
			stmt = getConnection().prepareStatement(getTargetDetails);
			stmt1 = getConnection().prepareStatement(getSalesAmount);
			
			model.setUser(user);
				
			stmt.setString(1, user.getUserId());
			stmt.setString(2, dates[0]);
			stmt.setString(3, dates[1]);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setTarget(rs.getDouble("TARGET"));
				model.setCommission(rs.getDouble("COMMISSION"));
			}
				
			stmt1.setString(1, user.getUserId());
			stmt1.setString(2, dates[0]);
			stmt1.setString(3, dates[1]);
			
			rs1= stmt1.executeQuery();
			
			while(rs1.next()){
				salesAmt = salesAmt + rs1.getDouble("AMT");
			}
			model.setSalesAmt(salesAmt);
				
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
		
		return model;
	}

	public MessageModel saveStaffSalary(SalaryModel model) {
		
		MessageModel msg = new MessageModel();
		
		PreparedStatement stmt = null;
		
		String deleteSalary = "DELETE FROM STAFF_SALARY WHERE USER = ? AND MONTH = ? AND YEAR = ?";
		
		String insertSalary = "INSERT INTO STAFF_SALARY (USER, SALARY, MONTH, YEAR, PRESENTDAYS, ABSENTDAYS, HOLIDAYS, LATEFINES, PENALTY, OT, PAIDON, EXPENSE1, EXPENSE2, EXPENSE3, EXPENSE4, EXPENSE5, ADVANCE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(deleteSalary);
			stmt.setString(1, model.getUser().getUserId());
			stmt.setInt(2, model.getMonth());
			stmt.setInt(3, model.getYear());
			
			stmt.executeUpdate();
			
			
			stmt = getConnection().prepareStatement(insertSalary);
			stmt.setString(1, model.getUser().getUserId());
			stmt.setString(2, model.getUser().getSalary());
			stmt.setInt(3, model.getMonth());
			stmt.setInt(4, model.getYear());
			stmt.setDouble(5, model.getPresentDays());
			stmt.setDouble(6, model.getLeaveDays());
			stmt.setDouble(7, model.getHoliDays());
			stmt.setDouble(8, model.getLatePenalty());
			stmt.setDouble(9, model.getPenalty());
			stmt.setDouble(10, model.getOt());
			stmt.setString(11, Utils.convertToSQLDate(model.getPaidOn()));
			stmt.setDouble(12, model.getExpense1());
			stmt.setDouble(13, model.getExpense2());
			stmt.setDouble(14, model.getExpense3());
			stmt.setDouble(15, model.getExpense4());
			stmt.setDouble(16, model.getExpense5());
			stmt.setDouble(17, model.getAdvanceAdjust());
			
			stmt.executeUpdate();
			
			msg.addNewMessage(new Message(SUCCESS, "Salary Details saved successfully."));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg.addNewMessage(new Message(ERROR, "Error occured while saving Salary Details."));
			try{
				if(stmt != null)
					stmt.close();
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
		
		return msg;
	}

}
