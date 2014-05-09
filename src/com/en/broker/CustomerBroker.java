package com.en.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.en.model.CustomerEmailModel;
import com.en.model.CustomerModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.StringUtility;

public class CustomerBroker extends BaseBroker{

	/**
	 * Constructor
	 * @param Connection conn
	 */
	public CustomerBroker(Connection conn) {
		super(conn);
	}
	
	/**
	 * Method to add a new customer
	 * 
	 * @param CustomerModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel addCustomer(CustomerModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// local variables
		long customerId = 1;
		
		// query to get the max of customer id
		String getMaxCustomerId = "SELECT MAX(CUSTOMERID) FROM CUSTOMER";
		
		// query to check the existence of customer
		String checkCustomer = "SELECT COUNT(*) FROM CUSTOMER WHERE CUSTOMERNAME = ? AND CITY = ? AND AREA = ? AND STATUS = 'A'";
		
		// query to add the customer details
		String insertCustomer = "INSERT INTO CUSTOMER (CUSTOMERID, CUSTOMERNAME, CUSTOMERTYPE, ADDRESS, CITY," +
				"PINCODE, STDCODE, PHONE1, PHONE2, MOBILE1, MOBILE2, GRADE, EMAIL, WEBSITE, TIN, CST, TRANSPORT, CONTACTPERSON, REMARK, AREA, CUSTGROUPID, ISSUPPLIER, OPENBAL, EMAIL1, COLLECTIONPERSON, OPENBALREMARKS, EMAIL2, EMAIL3, EMAIL4, EMAIL5, STATUS) VALUES " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'A')";
		
		//query to trim n convert to upper case
		String trimCustomerName = "UPDATE CUSTOMER SET CUSTOMERNAME = UPPER(TRIM(CUSTOMERNAME)), AREA = UPPER(AREA), CITY = UPPER(CITY)";
		
		try{
			
			stmt = getConnection().prepareStatement(checkCustomer);
			stmt.setString(1, model.getCustomerName());
			stmt.setString(2, model.getCity());
			stmt.setString(3, model.getArea());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, model.getCustomerName()+" in "+model.getArea()+" "+model.getCity()+" already exists.");
				messages.addNewMessage(msg);
			} else {
				stmt = getConnection().prepareStatement(getMaxCustomerId);
				
				rs = stmt.executeQuery();
				
				if(rs.next() && rs.getLong(1) > 0){
					customerId = rs.getLong(1) + 1;
				}
				
				stmt = getConnection().prepareStatement(insertCustomer);
				stmt.setLong(1, customerId);
				stmt.setString(2, model.getCustomerName());
				stmt.setString(3, model.getCustomerType());
				stmt.setString(4, model.getAddress());
				stmt.setString(5, model.getCity());
				stmt.setString(6, model.getPincode());
				stmt.setString(7, model.getStdcode());
				stmt.setString(8, model.getPhone1());
				stmt.setString(9, model.getPhone2());
				stmt.setString(10, model.getMobile1());
				stmt.setString(11, model.getMobile2());
				stmt.setString(12, model.getGrade());
				stmt.setString(13, model.getEmail());
				stmt.setString(14, model.getWebsite());
				stmt.setString(15, model.getTin());
				stmt.setString(16, model.getCst());
				stmt.setString(17, model.getTransport());
				stmt.setString(18, model.getContactPerson());
				stmt.setString(19, model.getRemark());
				stmt.setString(20, model.getArea());
				stmt.setInt(21, model.getCustGroupId());
				stmt.setString(22, model.isSupplier() ? "Y" : "N");
				stmt.setDouble(23, model.getOpenBal());
				stmt.setString(24, model.getEmail1());
				stmt.setString(25, model.getCollectionPerson().getUserId());
				stmt.setString(26, model.getOpenBalRemark());
				stmt.setString(27, model.getEmail2());
				stmt.setString(28, model.getEmail3());
				stmt.setString(29, model.getEmail4());
				stmt.setString(30, model.getEmail5());				
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Customer details added successfully.");
					messages.addNewMessage(msg);
					msg = new Message("CustomerId", customerId+"");
				} else {
					msg = new Message(ALERT, "Error occured while adding a customer details.");
				}
				messages.addNewMessage(msg);
			}
			
			stmt = getConnection().prepareStatement(trimCustomerName);
			
			stmt.execute();
			
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
	 * Method to get list all the available city
	 *  
	 * @return array of String
	 */
	public String[] getCityList(){
		ArrayList<String> cities = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT CITY FROM CITY ORDER BY CITY";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				cities.add(rs.getString("CITY"));
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
		
		return (String[]) cities.toArray(new String[0]);
	}
	
	/**
	 * Method to get list all the available city
	 *  
	 * @return array of String
	 */
	public String[] getStateList(){
		ArrayList<String> cities = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT DISTINCT(STATE) FROM CITY ORDER BY STATE";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				cities.add(rs.getString("STATE"));
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
		
		return (String[]) cities.toArray(new String[0]);
	}
	
	/**
	 * Method to get the count of customers in db
	 * 
	 * @return int for count
	 */
	public int getCustomerCount(){
		int count = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT COUNT(*) FROM CUSTOMER WHERE STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				count = rs.getInt(1);
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
		
		return count;
	}
	
	/**
	 * Method to get list all the available customers
	 *  
	 * @return array of CustomerModel
	 */
	public CustomerModel[] getCustomer1(){
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to trim the customer name
		String trimCustomerName = "UPDATE CUSTOMER SET CUSTOMERNAME = TRIM(CUSTOMERNAME)";
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.CITY, A.AREA, A.CUSTOMERTYPE, A.CONTACTPERSON, A.PHONE1, A.MOBILE1, A.TIN, A.CST, A.TRANSPORT, B.STATE FROM CUSTOMER A, CITY B WHERE A.CITY=B.CITY AND STATUS = 'A' ORDER BY A.CUSTOMERNAME, A.AREA, A.CITY";
		
		try{
			
			stmt = getConnection().prepareStatement(trimCustomerName);
			
			stmt.execute();
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerModel();
				model.setCustomerId(rs.getInt("CUSTOMERID"));
				model.setCustomerName(StringUtility.replaceCots(rs.getString("CUSTOMERNAME")));
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
	
	/**
	 * Method to get list all the available customers
	 *  
	 * @return array of CustomerModel
	 */
	public CustomerModel[] getSupplier(){
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT CUSTOMERID, CUSTOMERNAME, CITY, AREA, CUSTOMERTYPE, CONTACTPERSON, PHONE1, TIN, CST, TRANSPORT FROM CUSTOMER WHERE STATUS = 'A' AND ISSUPPLIER = 'Y' ORDER BY CUSTOMERNAME";
		
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
				model.setPhone1(rs.getString("PHONE1"));
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
	
	/**
	 * Method to get the details of the selected customerId
	 * 
	 * @param customerId
	 * @return CustomerModel with customer details
	 */
	public CustomerModel getCustomerDtls(int customerId) {
		CustomerModel model = new CustomerModel();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the customer
		String getItemDtls = "SELECT CUSTOMERID, CUSTOMERNAME, CUSTOMERTYPE, ADDRESS, CITY, " +
				"PINCODE, STDCODE, PHONE1, PHONE2, MOBILE1, MOBILE2, GRADE, EMAIL, EMAIL1, WEBSITE, " +
				"TIN, CST, TRANSPORT, CONTACTPERSON, REMARK, COLLECTIONPERSON, AREA, CUSTGROUPID, ISSUPPLIER, " +
				"OPENBAL, OPENBALREMARKS, EMAIL2, EMAIL3, EMAIL4, EMAIL5 FROM CUSTOMER WHERE CUSTOMERID = ? AND STATUS = 'A'";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			stmt.setInt(1, customerId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				model.setCustomerId(rs.getInt("CUSTOMERID"));
				model.setCustomerName(rs.getString("CUSTOMERNAME"));
				model.setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.setAddress(rs.getString("ADDRESS"));
				model.setArea(rs.getString("AREA"));
				model.setCity(rs.getString("CITY"));
				model.setPincode(rs.getString("PINCODE"));
				model.setStdcode(rs.getString("STDCODE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setPhone2(rs.getString("PHONE2"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setMobile2(rs.getString("MOBILE2"));
				model.setGrade(rs.getString("GRADE"));
				model.setEmail(rs.getString("EMAIL"));
				model.setEmail1(rs.getString("EMAIL1"));
				model.setEmail2(rs.getString("EMAIL2"));
				model.setEmail3(rs.getString("EMAIL3"));
				model.setEmail4(rs.getString("EMAIL4"));
				model.setEmail5(rs.getString("EMAIL5"));
				model.setWebsite(rs.getString("WEBSITE"));
				model.setTin(rs.getString("TIN"));
				model.setCst(rs.getString("CST"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setContactPerson(rs.getString("CONTACTPERSON"));
				model.setRemark(rs.getString("REMARK"));
				model.setCustGroupId(rs.getInt("CUSTGROUPID"));
				model.setOpenBal(rs.getDouble("OPENBAL"));
				model.setOpenBalRemark(rs.getString("OPENBALREMARKS"));
				if(rs.getString("ISSUPPLIER").equals("Y")){
					model.setSupplier(true);
				}
				if(!rs.getString("COLLECTIONPERSON").equals("")){
					model.setCollectionPerson((new UserBroker(getConnection())).getUserDtls(rs.getString("COLLECTIONPERSON")));
				}
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
	
	/**
	 * Method to update an existing customer
	 * 
	 * @param CustomerModel model
	 * @return MessageModel with all the messages
	 */
	public MessageModel updateCustomer(CustomerModel model){
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to check the existence of item category
		String checkCustomer = "SELECT COUNT(*) FROM CUSTOMER WHERE CUSTOMERNAME = ? AND CITY =? AND AREA = ? AND CUSTOMERID <> ? AND STATUS = 'A'";
		
		// query to update the item group details
		String updateCustomer = "UPDATE CUSTOMER SET CUSTOMERNAME = ? , CUSTOMERTYPE = ?, ADDRESS = ?, CITY = ?," +
				" PINCODE = ?, STDCODE = ?, PHONE1 = ?, PHONE2 = ?, MOBILE1 = ?, MOBILE2 = ?, GRADE = ?, EMAIL = ?," +
				"WEBSITE = ?, TIN = ?, TRANSPORT = ?, CONTACTPERSON = ?, REMARK = ?, AREA = ?, CUSTGROUPID = ?, CST = ?," +
				" ISSUPPLIER = ?, OPENBAL = ?, EMAIL1 = ?, COLLECTIONPERSON = ?, OPENBALREMARKS = ?, EMAIL2 = ?, " +
				"EMAIL3 = ?, EMAIL4 = ?, EMAIL5 = ? WHERE CUSTOMERID = ? AND STATUS = 'A'";
		
		//query to trim n convert to upper case
		String trimCustomerName = "UPDATE CUSTOMER SET CUSTOMERNAME = UPPER(TRIM(CUSTOMERNAME)), AREA = UPPER(AREA), CITY = UPPER(CITY)";
		
		try{
			
			stmt = getConnection().prepareStatement(checkCustomer);
			stmt.setString(1, model.getCustomerName());
			stmt.setString(2, model.getCity());
			stmt.setString(3, model.getArea());
			stmt.setInt(4, model.getCustomerId());
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				msg = new Message(ALERT, model.getCustomerName()+" in "+model.getArea()+" "+model.getCity()+" already exists.");
				messages.addNewMessage(msg);
			} else {
				
				stmt = getConnection().prepareStatement(updateCustomer);
				stmt.setString(1, model.getCustomerName());
				stmt.setString(2, model.getCustomerType());
				stmt.setString(3, model.getAddress());
				stmt.setString(4, model.getCity());
				stmt.setString(5, model.getPincode());
				stmt.setString(6, model.getStdcode());
				stmt.setString(7, model.getPhone1());
				stmt.setString(8, model.getPhone2());
				stmt.setString(9, model.getMobile1());
				stmt.setString(10, model.getMobile2());
				stmt.setString(11, model.getGrade());
				stmt.setString(12, model.getEmail());
				stmt.setString(13, model.getWebsite());
				stmt.setString(14, model.getTin());
				stmt.setString(15, model.getTransport());
				stmt.setString(16, model.getContactPerson());
				stmt.setString(17, model.getRemark());
				stmt.setString(18, model.getArea());
				stmt.setInt(19, model.getCustGroupId());
				stmt.setString(20, model.getCst());
				stmt.setString(21, model.isSupplier() ? "Y" : "N");
				stmt.setDouble(22, model.getOpenBal());
				stmt.setString(23, model.getEmail1());
				stmt.setString(24, model.getCollectionPerson().getUserId());
				stmt.setString(25, model.getOpenBalRemark());
				stmt.setString(26, model.getEmail2());
				stmt.setString(27, model.getEmail3());
				stmt.setString(28, model.getEmail4());
				stmt.setString(29, model.getEmail5());
				stmt.setInt(30, model.getCustomerId());
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Customer details modified successfully.");
				} else {
					msg = new Message(ALERT, "Error occured while modifying a customer details.");
				}
				messages.addNewMessage(msg);
			}
			
			stmt = getConnection().prepareStatement(trimCustomerName);
			
			stmt.execute();
			
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

	public JSONObject addCity(String city, String district, String state, String country) {
		JSONObject temp = new JSONObject();
		
		PreparedStatement stmt = null;
		
		// query to add the city details
		String insertCountry = "INSERT INTO COUNTRY (COUNTRY) VALUES (?)";
		
		// query to add the city details
		String insertCity = "INSERT INTO CITY (CITY, DISTRICT, STATE, COUNTRY) VALUES (?,?,?,?)";
		
		try{
			
				if(country != null && !country.equals("")){
					stmt = getConnection().prepareStatement(insertCountry);
					stmt.setString(1, country);
					stmt.execute();
					state = country;
					district = "";				
					temp.put("country", country);
				}
				
				stmt = getConnection().prepareStatement(insertCity);
				stmt.setString(1, city);
				stmt.setString(2, district);
				stmt.setString(3, state);
				stmt.setString(4, country);
				
				stmt.execute();
				
				getConnection().commit();
				
				temp.put("city", city);				
			
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
		
		return temp;
	}

	public MessageModel addCity(String city, String district, String state) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to add the city details
		String insertCity = "INSERT INTO CITY (CITY, DISTRICT, STATE) VALUES (?,?,?)";
		
		try{
			
				stmt = getConnection().prepareStatement(insertCity);
				stmt.setString(1, city);
				stmt.setString(2, district);
				stmt.setString(3, state);
				
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "City added successfully.");
					messages.addNewMessage(msg);
				} else {
					msg = new Message(ALERT, "Error occured while adding a city.");
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

	public String[] getCityListForState(String state) {
		ArrayList<String> cities = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT CITY FROM CITY WHERE STATE = ? ORDER BY CITY";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			stmt.setString(1, state);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				cities.add(rs.getString("CITY"));
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
		
		return (String[]) cities.toArray(new String[0]);
	}

	public CustomerModel[] getCustomerLst(String city, String state,
			String type) {
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.ADDRESS, A.CUSTOMERTYPE, A.CITY, Z.STATE, " +
								"A.PINCODE, A.STDCODE, A.PHONE1, A.PHONE2, A.MOBILE1, A.MOBILE2, A.GRADE, " +
								"A.EMAIL, A.EMAIL1, A.EMAIL2, A.EMAIL3, A.EMAIL4, A.EMAIL5, A.WEBSITE, A.TIN, A.CST, A.TRANSPORT, A.CONTACTPERSON, " +
								"A.REMARK, A.COLLECTIONPERSON, A.AREA, A.CUSTGROUPID, A.ISSUPPLIER, A.OPENBAL, " +
								"COUNT(B.CUSTOMER) AS DBE, COUNT(C.CUSTOMERID) AS DRD, COUNT(D.CUSTOMERID) AS ENQ, " +
								"COUNT(E.SUPPID) AS PUR, COUNT(F.CUSTID) AS SAL, COUNT(G.CUSTID) AS QUO, A.CHK " +
								"FROM CITY Z, CUSTOMER A " +
								"LEFT JOIN BRANCH_DAY_BOOK_ENTRIES B ON A.CUSTOMERID = B.CUSTOMER " +
								"LEFT JOIN DAILY_REPORT_DTLS C ON A.CUSTOMERID = C.CUSTOMERID " +
								"LEFT JOIN ENQUIRY_MASTER D ON A.CUSTOMERID = D.CUSTOMERID " +
								"LEFT JOIN PURCHASE E ON A.CUSTOMERID = E.SUPPID " +
								"LEFT JOIN SALES F ON A.CUSTOMERID = F.CUSTID " +
								"LEFT JOIN QUOTATION G ON A.CUSTOMERID = G.CUSTID  WHERE A.STATUS = 'A' AND A.CITY = Z.CITY ";
		if(!city.equals("")){
			getCustomers = getCustomers +" AND  A.CITY = ?";
		}
		if(city.equals("") && !state.equals("")){
			getCustomers = getCustomers +" AND  A.CITY IN (SELECT Z.CITY FROM CITY Z WHERE Z.STATE = ?)";
		}
		if(!type.equals("")){
			getCustomers = getCustomers +" AND  A.CUSTOMERTYPE = ?";
		}
		getCustomers = getCustomers+ " GROUP BY A.CUSTOMERID";
		getCustomers = getCustomers+ " ORDER BY TRIM(A.CUSTOMERNAME)";
				
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			int i=1;
			if(!city.equals("")){
				stmt.setString(i, city);
				i++;
			}
			if(city.equals("") && !state.equals("")){
				stmt.setString(i, state);
				i++;
			}
			if(!type.equals("")){
				stmt.setString(i, type);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerModel();
				model.setCustomerId(rs.getInt("CUSTOMERID"));
				model.setCustomerName(rs.getString("CUSTOMERNAME"));
				model.setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.setAddress(rs.getString("ADDRESS"));
				model.setArea(rs.getString("AREA"));
				model.setCity(rs.getString("CITY"));
				model.setState(rs.getString("STATE"));
				model.setPincode(rs.getString("PINCODE"));
				model.setStdcode(rs.getString("STDCODE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setPhone2(rs.getString("PHONE2"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setMobile2(rs.getString("MOBILE2"));
				model.setGrade(rs.getString("GRADE"));
				model.setEmail(rs.getString("EMAIL"));
				model.setEmail1(rs.getString("EMAIL1"));
				model.setEmail2(rs.getString("EMAIL2"));
				model.setEmail3(rs.getString("EMAIL3"));
				model.setEmail4(rs.getString("EMAIL4"));
				model.setEmail5(rs.getString("EMAIL5"));
				model.setWebsite(rs.getString("WEBSITE"));
				model.setTin(rs.getString("TIN"));
				model.setCst(rs.getString("CST"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setContactPerson(rs.getString("CONTACTPERSON"));
				model.setRemark(rs.getString("REMARK"));
				model.setCustGroupId(rs.getInt("CUSTGROUPID"));
				model.setOpenBal(rs.getDouble("OPENBAL"));
				if(rs.getString("ISSUPPLIER").equals("Y")){
					model.setSupplier(true);
				}
				if(rs.getInt("CHK") == 1){
					model.setChecked(true);
				}
				if(!rs.getString("COLLECTIONPERSON").equals("")){
					model.setCollectionPerson((new UserBroker(getConnection())).getUserDtls(rs.getString("COLLECTIONPERSON")));
				}
				model.setTxnCnt(rs.getInt("DBE")+rs.getInt("DRD")+rs.getInt("ENQ")+rs.getInt("PUR")+rs.getInt("SAL")+rs.getInt("QUO"));
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

	public CustomerModel[] getPrintCustomerLst(String city, String state,
			String type) {
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERNAME, A.ADDRESS, A.CITY, Z.STATE, " +
								"A.PINCODE, A.STDCODE, A.PHONE1, A.PHONE2, A.MOBILE1, A.MOBILE2, A.AREA " +
								"FROM CITY Z, CUSTOMER A WHERE A.STATUS = 'A' AND A.CITY = Z.CITY ";
		if(!city.equals("")){
			getCustomers = getCustomers +" AND  A.CITY = ?";
		}
		if(city.equals("") && !state.equals("")){
			getCustomers = getCustomers +" AND  A.CITY IN (SELECT Z.CITY FROM CITY Z WHERE Z.STATE = ?)";
		}
		if(!type.equals("")){
			getCustomers = getCustomers +" AND  A.CUSTOMERTYPE = ?";
		}
		getCustomers = getCustomers+ " GROUP BY A.CUSTOMERID";
		getCustomers = getCustomers+ " ORDER BY A.CITY, TRIM(A.CUSTOMERNAME)";
				
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			int i=1;
			if(!city.equals("")){
				stmt.setString(i, city);
				i++;
			}
			if(city.equals("") && !state.equals("")){
				stmt.setString(i, state);
				i++;
			}
			if(!type.equals("")){
				stmt.setString(i, type);
				i++;
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerModel();
				model.setCustomerName(rs.getString("CUSTOMERNAME"));
				model.setAddress(rs.getString("ADDRESS"));
				model.setArea(rs.getString("AREA"));
				model.setCity(rs.getString("CITY"));
				model.setState(rs.getString("STATE"));
				model.setPincode(rs.getString("PINCODE"));
				model.setStdcode(rs.getString("STDCODE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setPhone2(rs.getString("PHONE2"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setMobile2(rs.getString("MOBILE2"));
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

	public CustomerModel[] getCustomerLst(String[] ids) {
		ArrayList<CustomerModel> customers = new ArrayList<CustomerModel>(0);
		CustomerModel model = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.ADDRESS, A.CUSTOMERTYPE, A.CITY, " +
								"A.PINCODE, A.STDCODE, A.PHONE1, A.PHONE2, A.MOBILE1, A.MOBILE2, A.GRADE, " +
								"A.EMAIL, A.EMAIL1, A.EMAIL2, A.EMAIL3, A.EMAIL4, A.EMAIL5, A.WEBSITE, A.TIN, A.CST, A.TRANSPORT, A.CONTACTPERSON, " +
								"A.REMARK, A.COLLECTIONPERSON, A.AREA, A.CUSTGROUPID, A.ISSUPPLIER, A.OPENBAL, " +
								"COUNT(B.CUSTOMER) AS DBE, COUNT(C.CUSTOMERID) AS DRD, COUNT(D.CUSTOMERID) AS ENQ, " +
								"COUNT(E.SUPPID) AS PUR, COUNT(F.CUSTID) AS SAL, COUNT(G.CUSTID) AS QUO , COUNT(H.CUSTID) AS ODR " +
								"FROM CUSTOMER A " +
								"LEFT JOIN BRANCH_DAY_BOOK_ENTRIES B ON A.CUSTOMERID = B.CUSTOMER " +
								"LEFT JOIN DAILY_REPORT_DTLS C ON A.CUSTOMERID = C.CUSTOMERID " +
								"LEFT JOIN ENQUIRY_MASTER D ON A.CUSTOMERID = D.CUSTOMERID " +
								"LEFT JOIN PURCHASE E ON A.CUSTOMERID = E.SUPPID " +
								"LEFT JOIN SALES F ON A.CUSTOMERID = F.CUSTID " +
								"LEFT JOIN QUOTATION G ON A.CUSTOMERID = G.CUSTID " +
								"LEFT JOIN ORDER_MASTER H ON A.CUSTOMERID = H.CUSTID  WHERE A.STATUS = 'A' AND (";
		for(int k=0; k<ids.length; k++){
			getCustomers = getCustomers + "A.CUSTOMERID = ? ";
			if(k!=ids.length-1){
				getCustomers = getCustomers + " OR ";
			}
		}
		getCustomers = getCustomers+ ") GROUP BY A.CUSTOMERID";
		getCustomers = getCustomers+ " ORDER BY TRIM(A.CUSTOMERNAME)";
				
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			for(int k=0; k<ids.length; k++){
				stmt.setInt((k+1), Integer.parseInt(ids[k]));
			}
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				model = new CustomerModel();
				model.setCustomerId(rs.getInt("CUSTOMERID"));
				model.setCustomerName(rs.getString("CUSTOMERNAME"));
				model.setCustomerType(rs.getString("CUSTOMERTYPE"));
				model.setAddress(rs.getString("ADDRESS"));
				model.setArea(rs.getString("AREA"));
				model.setCity(rs.getString("CITY"));
				model.setPincode(rs.getString("PINCODE"));
				model.setStdcode(rs.getString("STDCODE"));
				model.setPhone1(rs.getString("PHONE1"));
				model.setPhone2(rs.getString("PHONE2"));
				model.setMobile1(rs.getString("MOBILE1"));
				model.setMobile2(rs.getString("MOBILE2"));
				model.setGrade(rs.getString("GRADE"));
				model.setEmail(rs.getString("EMAIL"));
				model.setEmail1(rs.getString("EMAIL1"));
				model.setEmail2(rs.getString("EMAIL2"));
				model.setEmail3(rs.getString("EMAIL3"));
				model.setEmail4(rs.getString("EMAIL4"));
				model.setEmail5(rs.getString("EMAIL5"));
				model.setWebsite(rs.getString("WEBSITE"));
				model.setTin(rs.getString("TIN"));
				model.setCst(rs.getString("CST"));
				model.setTransport(rs.getString("TRANSPORT"));
				model.setContactPerson(rs.getString("CONTACTPERSON"));
				model.setRemark(rs.getString("REMARK"));
				model.setCustGroupId(rs.getInt("CUSTGROUPID"));
				model.setOpenBal(rs.getDouble("OPENBAL"));
				if(rs.getString("ISSUPPLIER").equals("Y")){
					model.setSupplier(true);
				}
				if(!rs.getString("COLLECTIONPERSON").equals("")){
					model.setCollectionPerson((new UserBroker(getConnection())).getUserDtls(rs.getString("COLLECTIONPERSON")));
				}
				model.setTxnCnt(rs.getInt("DBE")+rs.getInt("DRD")+rs.getInt("ENQ")+rs.getInt("PUR")+rs.getInt("SAL")+rs.getInt("QUO")+rs.getInt("ODR"));
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

	public String[] getCustomerTypes() {
		ArrayList<String> customers = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT TYPE FROM CUSTOMER_TYPE ORDER BY TYPE";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
						
			rs = stmt.executeQuery();
			
			while(rs.next()){
				customers.add(rs.getString("TYPE"));
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
		
		return (String[]) customers.toArray(new String[0]);
	}

	public MessageModel addCustomerType(String custType) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to add the city details
		String insertCity = "INSERT INTO CUSTOMER_TYPE VALUES (?)";
		
		try{
				
				stmt = getConnection().prepareStatement(insertCity);
				stmt.setString(1, custType);
				
				
				if(stmt.executeUpdate() > 0){
					msg = new Message(SUCCESS, "Customer Type added successfully.");
					messages.addNewMessage(msg);
				} else {
					msg = new Message(ALERT, "Error occured while adding a customer type.");
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

	public MessageModel adChkCustomer(CustomerModel model) {
		MessageModel msg = new MessageModel();
		CustomerModel temp = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get details of the customer
		String getItemDtls = "SELECT CUSTOMERID, CUSTOMERNAME, CUSTOMERTYPE, ADDRESS, CITY, " +
				"PINCODE, STDCODE, PHONE1, PHONE2, MOBILE1, MOBILE2, GRADE, EMAIL, EMAIL1, EMAIL2, EMAIL3, " +
				"EMAIL4, EMAIL5, WEBSITE, TIN, CST, TRANSPORT, CONTACTPERSON, REMARK, AREA, CUSTGROUPID, ISSUPPLIER, OPENBAL FROM CUSTOMER WHERE (CUSTOMERNAME = ? AND CITY =?) OR (TIN = ? AND TIN <> '') OR ((MOBILE1 = ? OR MOBILE1 = ?) AND MOBILE1 <>'') OR ((MOBILE2 = ? OR MOBILE2 = ?) AND MOBILE2 <> '') AND STATUS = 'A' LIMIT 1";

		try{
			
			stmt = getConnection().prepareStatement(getItemDtls);
			stmt.setString(1, model.getCustomerName());
			stmt.setString(2, model.getCity());
			stmt.setString(3, model.getTin());
			stmt.setString(4, model.getMobile1());
			stmt.setString(5, model.getMobile2());
			stmt.setString(6, model.getMobile1());
			stmt.setString(7, model.getMobile2());
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				temp = new CustomerModel();
				temp.setCustomerId(rs.getInt("CUSTOMERID"));
				temp.setCustomerName(rs.getString("CUSTOMERNAME"));
				temp.setCustomerType(rs.getString("CUSTOMERTYPE"));
				temp.setAddress(rs.getString("ADDRESS"));
				temp.setArea(rs.getString("AREA"));
				temp.setCity(rs.getString("CITY"));
				temp.setPincode(rs.getString("PINCODE"));
				temp.setStdcode(rs.getString("STDCODE"));
				temp.setPhone1(rs.getString("PHONE1"));
				temp.setPhone2(rs.getString("PHONE2"));
				temp.setMobile1(rs.getString("MOBILE1"));
				temp.setMobile2(rs.getString("MOBILE2"));
				temp.setGrade(rs.getString("GRADE"));
				temp.setEmail(rs.getString("EMAIL"));
				temp.setEmail1(rs.getString("EMAIL1"));
				temp.setEmail2(rs.getString("EMAIL2"));
				temp.setEmail3(rs.getString("EMAIL3"));
				temp.setEmail4(rs.getString("EMAIL4"));
				temp.setEmail5(rs.getString("EMAIL5"));
				temp.setWebsite(rs.getString("WEBSITE"));
				temp.setTin(rs.getString("TIN"));
				temp.setCst(rs.getString("CST"));
				temp.setTransport(rs.getString("TRANSPORT"));
				temp.setContactPerson(rs.getString("CONTACTPERSON"));
				temp.setRemark(rs.getString("REMARK"));
				temp.setCustGroupId(rs.getInt("CUSTGROUPID"));
				temp.setOpenBal(rs.getDouble("OPENBAL"));
				if(rs.getString("ISSUPPLIER").equals("Y")){
					temp.setSupplier(true);
				}
				msg.addNewMessage(new Message(ALERT, "Are you planning to add the following customer.<br/><br/><label style=\"background : yellow; padding: 2px;\">"+temp.getLabel()+"<br/>TIN:"+temp.getTin()+"<br/>Mobile:"+temp.getMobile1()+"/"+temp.getMobile2()+"</label><br/><br/> If not please click save to save the customer record."));
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
		
		return msg;
	}

	public String[] getDistinctCityList() {
		ArrayList<String> cities = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT DISTINCT(UPPER(CITY)) AS CITY FROM CUSTOMER ORDER BY CITY";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				cities.add(rs.getString("CITY"));
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
		
		return (String[]) cities.toArray(new String[0]);
	}

	public MessageModel deleteCustomer(int custId) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to update the item group details
		String updateCustomer = "DELETE FROM CUSTOMER WHERE CUSTOMERID = ?";
		
		try{
			
			stmt = getConnection().prepareStatement(updateCustomer);
			stmt.setInt(1, custId);
			
			if(stmt.executeUpdate()>0){
				msg = new Message(SUCCESS, "Customer deleted successfully.");
			} else {
				msg = new Message(ALERT, "Error occured while modifying a customer details.");
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

	public MessageModel mergeCustomer(String[] ids, String masterId) {
		MessageModel messages = new MessageModel();
		Message msg = null;
		
		PreparedStatement stmt = null;
		
		// query to update day book table 
		String updateDayBook = "UPDATE BRANCH_DAY_BOOK_ENTRIES SET CUSTOMER = ? WHERE CUSTOMER = ?";
		
		// query to update daily rpt table
		String updateDailyRpt = "UPDATE DAILY_REPORT_DTLS SET CUSTOMERID = ? WHERE CUSTOMERID = ?";
		
		// query to update enquiry table
		String updateEnquiry = "UPDATE ENQUIRY_MASTER SET CUSTOMERID = ? WHERE CUSTOMERID = ?";
		
		// query to update purchase table
		String updatePurchase = "UPDATE PURCHASE SET SUPPID = ? WHERE SUPPID = ?";
		
		// query to update sales table
		String updateSales = "UPDATE SALES SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update quotation table
		String updateQuotation = "UPDATE QUOTATION SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update quotation table
		String updateOrder = "UPDATE ORDER_MASTER SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update adjustment table
		String updateAdjustTable = "UPDATE BILL_TO_BILL_ADJUST SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update credit note table
		String updateCreditNote = "UPDATE CREDIT_NOTE SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update customer_email_reference table
		String updateEMailReference = "UPDATE CUSTOMER_EMAIL_REFERENCE SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update labour bill table
		String updateLabourBill = "UPDATE LABOUR_BILL SET CUSTID = ? WHERE CUSTID = ?";
		
		// query to update purchase return table
		String updatePurchaseReturn = "UPDATE PURCHASE_RETURN SET SUPPID = ? WHERE SUPPID = ?";
		
		// query to update the item group details
		String deleteCustomer = "DELETE FROM CUSTOMER WHERE CUSTOMERID = ?";
		
		try{
			
			for(int k=0; k<ids.length; k++){
				
				stmt = getConnection().prepareStatement(updateDayBook);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateDailyRpt);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateEnquiry);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updatePurchase);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateSales);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateQuotation);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateOrder);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateAdjustTable);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateCreditNote);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateEMailReference);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updatePurchaseReturn);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(updateLabourBill);
				stmt.setString(1, masterId);
				stmt.setString(2, ids[k]);
				
				stmt.execute();
				
				stmt = getConnection().prepareStatement(deleteCustomer);
				stmt.setString(1, ids[k]);
				
				stmt.execute();
				
			}
			
			messages.addNewMessage(new Message(SUCCESS, "Customer merge successfully."));
			
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

	public JSONArray getAjaxCustList(String custLike) {
		JSONObject temp = null;
		JSONArray lst = new JSONArray();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT A.CUSTOMERID, A.CUSTOMERNAME, A.CITY, A.AREA, A.CUSTOMERTYPE, A.CONTACTPERSON, A.PHONE1, A.MOBILE1, A.TIN, A.CST, A.TRANSPORT, B.STATE, A.EMAIL1, A.EMAIL FROM CUSTOMER A, CITY B WHERE A.CITY=B.CITY AND STATUS = 'A' AND CONCAT(A.CUSTOMERNAME,A.AREA,A.CITY) LIKE ? ORDER BY A.CUSTOMERNAME, A.AREA, A.CITY LIMIT 20";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			stmt.setString(1, custLike+"%");
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				temp = new JSONObject();
				
				temp.put("custId", rs.getInt("CUSTOMERID"));
				temp.put("custName", StringUtility.replaceCots(rs.getString("CUSTOMERNAME")));
				temp.put("custType", rs.getString("CUSTOMERTYPE"));
				temp.put("area", rs.getString("AREA"));
				temp.put("city", rs.getString("CITY"));
				temp.put("state", rs.getString("STATE"));
				temp.put("phone", rs.getString("PHONE1"));
				temp.put("mobile", rs.getString("MOBILE1"));
				temp.put("tin", rs.getString("TIN"));
				temp.put("cst", rs.getString("CST"));
				temp.put("transport", rs.getString("TRANSPORT"));
				temp.put("person", rs.getString("CONTACTPERSON"));
				temp.put("email", rs.getString("EMAIL"));
				temp.put("email1", rs.getString("EMAIL1"));
				
				lst.add(temp);
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
					getConnection().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return lst;
	}

	public void updateMergeCustomer(CustomerModel model) {
		
		PreparedStatement stmt = null;
		
		// query to update the item group details
		String updateCustomer = "UPDATE CUSTOMER SET CUSTOMERNAME = ? , ADDRESS = ?, PINCODE = ?, STDCODE = ?, PHONE1 = ?, PHONE2 = ?, MOBILE1 = ?, MOBILE2 = ?, EMAIL = ?," +
				" TIN = ?, CONTACTPERSON = ?, AREA = ?, CST = ?, EMAIL1 = ?, OPENBAL = ?, EMAIL2 =?, EMAIL3 = ?, EMAIL4 = ?, EMAIL5 = ?" +
				" WHERE CUSTOMERID = ? AND STATUS = 'A'";
		
		//query to trim n convert to upper case
		String trimCustomerName = "UPDATE CUSTOMER SET CUSTOMERNAME = UPPER(TRIM(CUSTOMERNAME)), AREA = UPPER(AREA), CITY = UPPER(CITY)";
		
		try{
			
			stmt = getConnection().prepareStatement(updateCustomer);
			stmt.setString(1, model.getCustomerName());
			stmt.setString(2, model.getAddress());
			stmt.setString(3, model.getPincode());
			stmt.setString(4, model.getStdcode());
			stmt.setString(5, model.getPhone1());
			stmt.setString(6, model.getPhone2());
			stmt.setString(7, model.getMobile1());
			stmt.setString(8, model.getMobile2());
			stmt.setString(9, model.getEmail());
			stmt.setString(10, model.getTin());
			stmt.setString(11, model.getContactPerson());
			stmt.setString(12, model.getArea());
			stmt.setString(13, model.getCst());
			stmt.setString(14, model.getEmail1());
			stmt.setDouble(15, model.getOpenBal());
			stmt.setString(16, model.getEmail1());
			stmt.setString(17, model.getEmail1());
			stmt.setString(18, model.getEmail1());
			stmt.setString(19, model.getEmail1());
			stmt.setInt(20, model.getCustomerId());
			
			stmt.executeUpdate();
			
			stmt = getConnection().prepareStatement(trimCustomerName);
			
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

	public int getUncheckedCustomerCount() {
		int count = 0;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT COUNT(*) FROM CUSTOMER WHERE CHK = 0 AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				count = rs.getInt(1);
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
		
		return count;
	}

	public boolean checkCustomer(String custId) {
		boolean returnVal = false;
		
		PreparedStatement stmt = null;
		
		// query to update the item group details
		String updateCustomer = "UPDATE CUSTOMER SET CHK = 1 WHERE CUSTOMERID = ? AND STATUS = 'A'";
		
		try{
			
			stmt = getConnection().prepareStatement(updateCustomer);
			stmt.setString(1, custId);
			
			if(stmt.executeUpdate() > 0){
				returnVal = true;
			} 
			
			getConnection().commit();
			
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
		
		return returnVal;
	}

	public int emailCustomer(String custId, String email1, String email2, String subject,
			String message, String fileName, String mobile1, String mobile2) {
		int refNo = 1;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to update the item group details
		String updateCustomer = "UPDATE CUSTOMER SET EMAIL = ?, EMAIL1 = ?, MOBILE1 = ?, MOBILE2 = ? WHERE CUSTOMERID = ? AND STATUS = 'A'";
		
		// query to update the item group details
		String getMaxRefNo = "SELECT MAX(EMAILID) FROM CUSTOMER_EMAIL_REFERENCE";
		
		// query to update the item group details
		String insertEmailDtls = "INSERT INTO CUSTOMER_EMAIL_REFERENCE (EMAILID, CUSTID, EMAILDATE, SUBJECT, MESSAGE, FILENAME) VALUES (?,?,CURRENT_DATE,?,?,?)";
		
		try{
			
			stmt = getConnection().prepareStatement(updateCustomer);
			stmt.setString(1, email1);
			stmt.setString(2, email2);
			stmt.setString(3, mobile1);
			stmt.setString(4, mobile2);
			stmt.setString(5, custId);
			
			stmt.executeUpdate();
			
			stmt = getConnection().prepareStatement(getMaxRefNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getInt(1) > 0){
				refNo = rs.getInt(1) + 1;
			}
			
			stmt = getConnection().prepareStatement(insertEmailDtls);
			
			stmt.setInt(1, refNo);
			stmt.setString(2, custId);
			stmt.setString(3, subject);
			stmt.setString(4, message);
			stmt.setString(5, fileName);
			
			stmt.executeUpdate();
			
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
		
		return refNo;
	}

	public CustomerEmailModel getEmailDtls(String emailNo) {
		CustomerEmailModel result = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to update the item group details
		String getEmailDtls = "SELECT EMAILID, CUSTID, EMAILDATE, SUBJECT, MESSAGE, FILENAME FROM CUSTOMER_EMAIL_REFERENCE WHERE EMAILID = ? ";
		
		try{
			
			stmt = getConnection().prepareStatement(getEmailDtls);
			stmt.setString(1, emailNo);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				result = new CustomerEmailModel();
				result.setEmailNo(rs.getInt("EMAILID"));
				result.getCustomer().setCustomerId(rs.getInt("CUSTID"));
				result.setEmailDate(rs.getString("EMAILDATE"));
				result.setSubject(rs.getString("SUBJECT"));
				result.setMessage(rs.getString("MESSAGE"));
				result.setFilename(rs.getString("FILENAME"));
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
		
		return result;
	}

	public void updateCustomerEmails(CustomerModel model) {
		
		PreparedStatement stmt = null;
		
		// query to update the item group details
		String updateCustomer = "UPDATE CUSTOMER SET  EMAIL = ?, EMAIL1 = ? WHERE CUSTOMERID = ? AND STATUS = 'A'";
		
		//query to trim n convert to upper case
		String trimCustomerName = "UPDATE CUSTOMER SET CUSTOMERNAME = UPPER(TRIM(CUSTOMERNAME)), AREA = UPPER(AREA), CITY = UPPER(CITY)";
		
		try{
			
				
			stmt = getConnection().prepareStatement(updateCustomer);
			stmt.setString(1, model.getEmail());
			stmt.setString(2, model.getEmail1());
			stmt.setInt(3, model.getCustomerId());
			
			stmt.executeUpdate();
			
			stmt = getConnection().prepareStatement(trimCustomerName);
			
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

	public String[] getCountryList() {
		ArrayList<String> cities = new ArrayList<String>(0);
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// query to get all the existing customers
		String getCustomers = "SELECT COUNTRY FROM COUNTRY ORDER BY COUNTRY";
		
		try{
			
			stmt = getConnection().prepareStatement(getCustomers);
			
			rs = stmt.executeQuery();
			
			while(rs.next()){
				cities.add(rs.getString("COUNTRY"));
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
		
		return (String[]) cities.toArray(new String[0]);
	}

}
