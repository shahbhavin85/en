package com.en.util;

public interface RequestContants {
	
	// jsp constants
//	public final static String TITLE = "Enquête";
	public final static String TITLE = "HESH OPTO-LAB PVT. LTD.";
	public final static String CORP_ADDRESS = "#141, BROADWAY, CHENNAI - 600108, INDIA";
	public final static String CORP_EMAIL = "heshstorebhavin@hotmail.com";
	
	// message type 
	public static final String ALERT = "ALERT";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";

	// session variables
	final public String USER = "user";
	final public String ACCESS_POINT = "point";
	final public String BALANCE = "BALANCE";
	final public String ACCESS_POINT_DTLS = "ACCESS_POINT_DTLS";
	final static String USERS_ACCESS_LIST = "USERS_ACCESS_LIST";
	final public String UPDATED_ITEMS = "UPDATED_ITEMS";
	final public String PL_VERSION = "PL_VERSION";
	
	// request parameter constants
	final public static String ACTIONS = "ACTIONS";
	final public static String HANDLERS = "HANDLERS";
	public static final String MESSAGES = "MESSAGES";
	public static final String FORM_DATA = "FORM_DATA";
	public static final String SALES_DATA = "SALES_DATA";
	public static final String PAYMENT_DATA = "PAYMENT_DATA";
	public static final String MUTLIPART_DATA = "MUTLIPART_DATA";
	public static final String CURRENT = "CURRENT";
	public static final String REQUESTED = "REQUESTED";
	public static final String TRANSFER_REQUEST_DATA = "TRANSFER_REQUEST_DATA";
	public static final String CUSTOMER = "CUSTOMER";
	public static final String THOUGHT = "THOUGHT";
	public static final String OPEN_BALANCE = "OPEN_BALANCE";
	public static final String GOOD_NEWS = "GOOD_NEWS";
	public static final String JACKPOT = "JACKPOT";
	public static final String TODAYS_ORDER = "TODAYS_ORDER";
	public static final String TODAYS_QUOTATION = "TODAYS_QUOTATION";
	public static final String CURRENT_ADVANCES = "CURRENT_ADVANCES";
	public static final String PREVIOUS_SALARY = "PREVIOUS_SALARY";
	final public static String PENDING_APP_REQUEST = "PENDING_APP_REQUEST";
	final public static String NEW_INBOX_COUNT = "NEW_INBOX_COUNT";
	final public static String CURRENT_SALARY = "CURRENT_SALARY";
	final public static String PENDING_TARGET = "PENDING_TARGET";
	final public static String CURRENT_TARGET = "CURRENT_TARGET";
	final public static String PENDING_APP_DEMAND = "PENDING_APP_DEMAND";
	final public static String BACK_UP = "BACK_UP";
	final public static String INIT_LOCK_TXN = "INIT_LOCK_TXN";
	final public static String LOCK_TXN = "LOCK_TXN";
	final public static String INIT_UNLOCK_TXN = "INIT_UNLOCK_TXN";
	final public static String UNLOCK_TXN = "UNLOCK_TXN";
	final public static String USER_PROFILE = "USER_PROFILE";
	
	// request variables	
	final static String ACCESS_POINTS = "ACCESS_POINTS";
	final static String ITEM_GROUPS = "ITEM_GROUPS";
	final static String ITEM_CATEGORY = "ITEM_CATEGORY";
	final static String USERS = "USERS";
	final static String ITEMS = "ITEMS";
	final static String OFFER_ITEMS = "OFFER_ITEMS";
	final static String CUSTOMERS_GROUPS = "CUSTOMERS_GROUPS";
	final static String CUSTOMERS = "CUSTOMERS";
	final static String CITIES = "CITIES";
	final static String COUNTRIES = "COUNTRIES";
	final static String CUST_TYPE = "CUST_TYPE";
	final static String STATES = "STATES";
	final static String LAST_BILL = "LAST_BILL";
	final static String PD_CHEQUES_LIST = "PD_CHEQUES_LIST";
	final static String PENDING_BILLS_LIST = "PENDING_BILLS_LIST";
	static final String CUST_LAST_TXN = "CUST_LAST_TXN";
	public static final String ADJUST_ENTRIES = "ADJUST_ENTRIES";
	
	public static final String AUTH_SMS_BASE_URL = "http://www.smsgatewaycenter.com/library/send_sms_2.php?UserName=heshtxn&Password=hesh12345&Mask=HESHST&Type=Individual";
	public static final String AUTH_SMS_BASE_URL_BULK = "http://www.smsgatewaycenter.com/library/send_sms_2.php?UserName=heshtxn&Password=hesh12345&Mask=HESHST&Type=Bulk";
	public static final String AUTH_SMS_PRO_URL = "http://www.smsgatewaycenter.com/library/send_sms_2.php?UserName=heshpro&Password=hesh12345&Mask=HESHPR&Type=Bulk";
	
	// request variable for enquiry
	final static String PENDING_APP_LIST = "PENDING_APP_LIST";
	
	final static String[] custCategory = { "RETAIL SHOP",
			"RETAIL SHOP : OPTOMETRIST", "RETAIL SHOP : HOSPITAL / CLINIC",
			"RETAIL SHOP : MULTI BRANCH STORE", "WHOLESALE : DOOR TO DOOR",
			"WHOLESALE SHOP : CITY BASE", "WHOLESALE SHOP : BULK BUYER",
			"OPTOMETRIST", "OPHTHALMOLOGIST", "DISTRIBUTOR",
			"MEDICAL SHOPS / COMPANY", "READY MADE GARMENTS STORE",
			"GIFT SHOPS", "SUPER MARKETS", "AGENTS", "OTHERS", "GRINDING SHOP","SURGICALS","INTERIOR DESIGNER","MNC COMPANY","PURCHASER","OPTHALMIC ASST." };
	
	final static String[] States = { "ANDAMAN AND NICOBAR ISLANDS",
			"ANDHRA PRADESH", "ARUNACHAL PRADESH", "ASSAM", "BIHAR",
			"CHANDIGARH", "CHHATTISGARH", "DAMAN AND DIU", "DELHI", "GOA",
			"GUJARAT", "HARYANA", "HIMACHAL PRADESH", "JAMMU AND KASHMIR",
			"JHARKHAND", "KARNATAKA", "KERALA", "LAKSHADEEP", "MADYA PRADESH",
			"MAHARASHTRA", "MANIPUR", "MEGHALAYA", "MIZORAM", "NAGALAND",
			"ORISSA", "PONDICHERRY", "PUNJAB", "RAJASTHAN", "SIKKIM",
			"TAMIL NADU", "TRIPURA", "UTTAR PRADESH", "UTTARANCHAL",
			"WEST BENGAL"};
	
	
}
