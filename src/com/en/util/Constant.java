package com.en.util;

public interface Constant extends RequestContants{
	
	// handlers
	final public static String LOGIN_HANDLER = "LOGIN_HANDLER";
	final public static String LOGOUT_HANDLER = "LOGOUT_HANDLER";
	final public static String AJAX_HANDLER = "AJAX_HANDLER";
	final public static String HOME_HANDLER = "HOME_HANDLER";
	final public static String ITM_GRP_HANDLER = "ITM_GRP_HANDLER";
	final public static String ITM_CAT_HANDLER = "ITM_CAT_HANDLER";
	final public static String ITM_HANDLER = "ITM_HANDLER";
	final public static String ITM_DTLS_HANDLER = "ITM_DTLS_HANDLER";
	final public static String CUSTOMER_HANDLER = "CUSTOMER_HANDLER";
	final public static String CUSTOMER_GRP_HANDLER = "CUSTOMER_GRP_HANDLER";
	final public static String ACCESS_POINT_HANDLER = "ACCESS_POINT_HANDLER";
	final public static String TAX_HANDLER = "TAX_HANDLER";
	final public static String SALES_HANDLER = "SALES_HANDLER";
	final public static String MESSENGER_HANDLER = "MESSENGER_HANDLER";
	final public static String CREDIT_NOTE_HANDLER = "CREDIT_NOTE_HANDLER";
	final public static String LABOUR_BILL_HANDLER = "LABOUR_BILL_HANDLER";
	final public static String BRANCH_STOCK_HANDLER = "BRANCH_STOCK_HANDLER";
	final public static String STOCK_REPORT_HANDLER = "STOCK_REPORT_HANDLER";
	final public static String ADMIN_STOCK_REPORT_HANDLER = "ADMIN_STOCK_REPORT_HANDLER";
	final public static String SALES_REPORT_HANDLER = "SALES_REPORT_HANDLER";
	final public static String SALES_TAX_REPORT_HANDLER = "SALES_TAX_REPORT_HANDLER";
	final public static String CUSTOMER_EMAIL_RPT_HANDLER = "CUSTOMER_EMAIL_RPT_HANDLER";
	final public static String ORDER_REPORT_HANDLER = "ORDER_REPORT_HANDLER";
	final public static String CREDIT_NOTE_REPORT_HANDLER = "CREDIT_NOTE_REPORT_HANDLER";
	final public static String LABOUR_BILL_REPORT_HANDLER = "LABOUR_BILL_REPORT_HANDLER";
	final public static String PURCHASE_REPORT_HANDLER = "PURCHASE_REPORT_HANDLER";
	final public static String PURCHASE_RETURN_REPORT_HANDLER = "PURCHASE_RETURN_REPORT_HANDLER";
	final public static String TRANSFER_REPORT_HANDLER = "TRANSFER_REPORT_HANDLER";
	final public static String COLLECTION_HANDLER = "COLLECTION_HANDLER";
	final public static String EXCEL_HANDLER = "EXCEL_HANDLER";
	final public static String APPROVAL_HANDLER = "APPROVAL_HANDLER";
	final public static String PROMOTIONAL_MAIL_HANDLER = "PROMOTIONAL_MAIL_HANDLER";
	final public static String PROMOTIONAL_SMS_HANDLER = "PROMOTIONAL_SMS_HANDLER";
	final public static String PURCHASE_HANDLER = "PURCHASE_HANDLER";
	final public static String PURCHASE_RETURN_HANDLER = "PURCHASE_RETURN_HANDLER";
	final public static String QUOTATION_HANDLER = "QUOTATION_HANDLER";
	final public static String EXHIBITION_QUOTATION_HANDLER = "EXHIBITION_QUOTATION_HANDLER";
	final public static String SALESMAN_QUOTATION_HANDLER = "SALESMAN_QUOTATION_HANDLER";
	final public static String ORDER_HANDLER = "ORDER_HANDLER";
	final public static String SALESMAN_ORDER_HANDLER = "SALESMAN_ORDER_HANDLER";
	final public static String EXHIBITION_ORDER_HANDLER = "EXHIBITION_ORDER_HANDLER";
	final public static String TRANSFER_HANDLER = "TRANSFER_HANDLER";
	final public static String TRANSFER_REQUEST_HANDLER = "TRANSFER_REQUEST_HANDLER";
	final public static String USER_HANDLER = "USER_HANDLER";
	final public static String USER_ACCESS_HANDLER = "USER_ACCESS_HANDLER";
	final public static String OFFER_HANDLER = "OFFER_HANDLER";
	final public static String ENQUIRY_HANDLER = "ENQUIRY_HANDLER";
	final public static String ENQUIRY_RPT_HANDLER = "ENQUIRY_RPT_HANDLER";
	final public static String SALESMAN_ENQUIRY_HANDLER = "SALESMAN_ENQUIRY_HANDLER";
	final public static String SALESMAN_DLY_HANDLER = "SALESMAN_DLY_HANDLER";
	final public static String BRANCH_DLY_ENTRY_HANDLER = "BRANCH_DLY_ENTRY_HANDLER";
	final public static String LEDGER_ADJUSTMENT_HANDLER = "LEDGER_ADJUSTMENT_HANDLER";
	final public static String BRANCH_DLY_ENTRY_RPT_HANDLER = "BRANCH_DLY_ENTRY_RPT_HANDLER";
	final public static String CUSTOMER_LEDGER_RPT_HANDLER = "CUSTOMER_LEDGER_RPT_HANDLER";
	final public static String BRANCH_STOCK_ENTRY_HANDLER = "BRANCH_STOCK_ENTRY_HANDLER";
	final public static String ATTENDANCE_HANDLER = "ATTENDANCE_HANDLER";
	final public static String SALARY_HANDLER = "SALARY_HANDLER";

	final public static String SYNC_DATA_HANDLER = "SYNC_DATA_HANDLER";
	final public static String INWARD_ENTRY_HANDLER = "INWARD_ENTRY_HANDLER";
	final public static String ADMIN_MASTER_HANDLER = "ADMIN_MASTER_HANDLER";
	
	// actions of login handler
	final public static String CHECK_ACCESS = "CHECK_ACCESS";
	final public static String CHANGE_ACCESS_POINT = "CHANGE_ACCESS_POINT";
	final public static String CHANGE_PASSWORD = "CHANGE_PASSWORD";
	final public static String INIT_PASSWORD = "INIT_PASSWORD";
	
	// actions of approval handler
	final public static String INIT_APP_SALES_LST = "INIT_APP_SALES_LST";
	final public static String APPROVE_SALES = "APPROVE_SALES";
	final public static String UNAPPROVE_SALES = "UNAPPROVE_SALES";
	
	// actions of add item group handler
	final public static String ADD_ITM_GROUP = "ADD_ITM_GROUP";
	final public static String MOD_ITM_GROUP = "MOD_ITM_GROUP";
	final public static String GET_ITM_GROUP = "GET_ITM_GROUP";
	final public static String UPDT_ITM_GROUP = "UPDT_ITM_GROUP";
	
	// actions of add item category handler
	final public static String ADD_ITM_CATEGORY = "ADD_ITM_CATEGORY";
	final public static String MOD_ITM_CATEGORY = "MOD_ITM_CATEGORY";
	final public static String GET_ITM_CATEGORY = "GET_ITM_CATEGORY";
	final public static String UPDT_ITM_CATEGORY = "UPDT_ITM_CATEGORY";

	// actions of sales handler
	final public static String ADD_ORDER_SALES = "ADD_ORDER_SALES";
	final public static String ADD_SALES = "ADD_SALES";
	final public static String CANCEL_SALES = "CANCEL_SALES";
	final public static String INIT_CANCEL_SALES = "INIT_CANCEL_SALES";
	final public static String PRNT_SALES = "PRNT_SALES";
	final public static String PRNT_DC = "PRNT_DC";
	final public static String INIT_EXPT_DATA = "INIT_EXPT_DATA";
	final public static String EXPT_MASTER_DATA = "EXPT_MASTER_DATA";
	final public static String EXPT_APPVD_DATA = "EXPT_APPVD_DATA";
	final public static String EXPT_ITEM_DATA = "EXPT_ITEM_DATA";
	final public static String INIT_VIEW_EDIT_SALES = "INIT_VIEW_EDIT_SALES";
	final public static String GET_SALES = "GET_SALES";
	final public static String EMAIL_SALES = "EMAIL_SALES";
	final public static String INIT_GET_SALES_DATE = "INIT_GET_SALES_DATE";
	final public static String GET_SALES_DATE = "GET_SALES_DATE";
	final public static String EDIT_SALES_DATE = "EDIT_SALES_DATE";
	final public static String INIT_EDIT_SALES = "INIT_EDIT_SALES";
	final public static String ACC_EDIT_SALES = "ACC_EDIT_SALES";
	final public static String CNFM_EDIT_SALES = "CNFM_EDIT_SALES";
	final public static String EDIT_SALES = "EDIT_SALES";
	final public static String EDIT_SALES_LR_DTLS = "EDIT_SALES_LR_DTLS";
	final public static String INIT_EDIT_SALES_LR_DTLS = "INIT_EDIT_SALES_LR_DTLS";
	final public static String EDIT_SALES_NO_LR_DTLS = "EDIT_SALES_NO_LR_DTLS";
	final public static String GET_EDIT_SALES_LR_DTLS = "GET_EDIT_SALES_LR_DTLS";
	
	// actions of purchase handler
	final public static String ADD_PURCHASE = "ADD_PURCHASE";
	final public static String INIT_EXPT_PURCHASE = "INIT_EXPT_PURCHASE";
	final public static String EXPT_MASTER_PURCHASE = "EXPT_MASTER_PURCHASE";
	final public static String EXPT_ITEM_PURCHASE = "EXPT_ITEM_PURCHASE";
	final public static String INIT_VIEW_EDIT_PURCHASE = "INIT_VIEW_EDIT_PURCHASE";
	final public static String GET_PURCHASE = "GET_PURCHASE";
	final public static String INIT_EDIT_PURCHASE = "INIT_EDIT_PURCHASE";
	final public static String CNFM_EDIT_PURCHASE = "CNFM_EDIT_PURCHASE";
	final public static String EDIT_PURCHASE = "EDIT_PURCHASE";
	
	// actions of purchase return handler
	final public static String ADD_PURCHASE_RETURN = "ADD_PURCHASE_RETURN";
	final public static String PRNT_PURCHASE_RETURN = "PRNT_PURCHASE_RETURN";
	final public static String INIT_EXPT_PURCHASE_RETURN = "INIT_EXPT_PURCHASE_RETURN";
	final public static String EXPT_MASTER_PURCHASE_RETURN = "EXPT_MASTER_PURCHASE_RETURN";
	final public static String EXPT_ITEM_PURCHASE_RETURN = "EXPT_ITEM_PURCHASE_RETURN";
	final public static String INIT_VIEW_EDIT_PURCHASE_RETURN = "INIT_VIEW_EDIT_PURCHASE_RETURN";
	final public static String GET_PURCHASE_RETURN = "GET_PURCHASE_RETURN";
	final public static String INIT_EDIT_PURCHASE_RETURN = "INIT_EDIT_PURCHASE_RETURN";
	final public static String CNFM_EDIT_PURCHASE_RETURN = "CNFM_EDIT_PURCHASE_RETURN";
	final public static String EDIT_PURCHASE_RETURN = "EDIT_PURCHASE_RETURN";
	
	// actions of purchase handler
	final public static String ADD_QUOTATION = "ADD_QUOTATION";
	final public static String PRNT_QUOTATION = "PRNT_QUOTATION";
	final public static String INIT_EXPT_QUOTATION = "INIT_EXPT_QUOTATION";
	final public static String EXPT_MASTER_QUOTATION = "EXPT_MASTER_QUOTATION";
	final public static String EXPT_ITEM_QUOTATION = "EXPT_ITEM_QUOTATION";
	final public static String INIT_VIEW_EDIT_QUOTATION = "INIT_VIEW_EDIT_QUOTATION";
	final public static String GET_QUOTATION = "GET_QUOTATION";
	final public static String SHOW_QUOTATION = "SHOW_QUOTATION";
	final public static String CLOSE_QUOTATION = "CLOSE_QUOTATION";
	final public static String INIT_EDIT_QUOTATION = "INIT_EDIT_QUOTATION";
	final public static String CNFM_EDIT_QUOTATION = "CNFM_EDIT_QUOTATION";
	final public static String EDIT_QUOTATION = "EDIT_QUOTATION";
	final public static String EMAIL_QUOTATION = "EMAIL_QUOTATION";
	final public static String INIT_FOLLOWUP = "INIT_FOLLOWUP";
	final public static String INIT_ITEM_FOLLOWUP = "INIT_ITEM_FOLLOWUP";
	final public static String INIT_CUSTOMER_FOLLOWUP = "INIT_CUSTOMER_FOLLOWUP";
	final public static String INIT_FOLLOWUP_1 = "INIT_FOLLOWUP_1";
	final public static String FOLLOWUP = "FOLLOWUP";
	final public static String TRANSFER_FOLLOWUP = "TRANSFER_FOLLOWUP";
	final public static String INIT_TRANSFER_FOLLOWUP = "INIT_TRANSFER_FOLLOWUP";
	final public static String CONVERT_TO_ORDER = "CONVERT_TO_ORDER";

	// actions of purchase handler
	final public static String ADD_ORDER = "ADD_ORDER";
	final public static String CANCEL_ORDER = "CANCEL_ORDER";
	final public static String COMPLETE_ORDER = "COMPLETE_ORDER";
	final public static String PRNT_ORDER = "PRNT_ORDER";
	final public static String INIT_EXPT_ORDER = "INIT_EXPT_ORDER";
	final public static String EXPT_MASTER_ORDER = "EXPT_MASTER_ORDER";
	final public static String EXPT_ITEM_ORDER = "EXPT_ITEM_ORDER";
	final public static String INIT_VIEW_EDIT_ORDER = "INIT_VIEW_EDIT_ORDER";
	final public static String GET_ORDER = "GET_ORDER";
	final public static String INIT_EDIT_ORDER = "INIT_EDIT_ORDER";
	final public static String CNFM_EDIT_ORDER = "CNFM_EDIT_ORDER";
	final public static String EDIT_ORDER = "EDIT_ORDER";
	final public static String CONVERT_TO_SALES = "CONVERT_TO_SALES";
	final public static String CONVERT_TO_TRANSFER = "CONVERT_TO_TRANSFER";
	final public static String SHOW_ORDER = "SHOW_ORDER";
	final public static String EMAIL_ORDER = "EMAIL_ORDER";
	final public static String ORDER_REQUEST = "ORDER_REQUEST";
	final public static String MULTI_EMAIL = "MULTI_EMAIL";

	// actions of purchase handler
	final public static String ADD_INWARD_ENTRY = "ADD_INWARD_ENTRY";
	final public static String INIT_VIEW_EDIT_INWARD_ENTRY = "INIT_VIEW_EDIT_INWARD_ENTRY";
	final public static String GET_INWARD_ENTRY = "GET_INWARD_ENTRY";
	final public static String INIT_EDIT_INWARD_ENTRY = "INIT_EDIT_INWARD_ENTRY";
	final public static String EDIT_INWARD_ENTRY = "EDIT_INWARD_ENTRY";
	
	// actions of sales handler
	final public static String ADD_TRANSFER= "ADD_TRANSFER";
	final public static String ADD_REQUEST_TRANSFER= "ADD_REQUEST_TRANSFER";
	final public static String PRNT_TRANSFER= "PRNT_TRANSFER";
	final public static String INIT_VIEW_EDIT_TRANSFER = "INIT_VIEW_EDIT_TRANSFER";
	final public static String GET_TRANSFER = "GET_TRANSFER";
	final public static String INIT_EDIT_TRANSFER = "INIT_EDIT_TRANSFER";
	final public static String EDIT_TRANSFER = "EDIT_TRANSFER";
	final public static String INIT_APP_TRANSFER = "INIT_APP_TRANSFER";
	final public static String GET_APP_TRANSFER = "GET_APP_TRANSFER";
	final public static String APP_TRANSFER = "APP_TRANSFER";
	final public static String EDIT_TRANSFER_LR_DTLS = "EDIT_TRANSFER_LR_DTLS";
	final public static String INIT_EDIT_TRANSFER_LR_DTLS = "INIT_EDIT_TRANSFER_LR_DTLS";
	final public static String GET_EDIT_TRANSFER_LR_DTLS = "GET_EDIT_TRANSFER_LR_DTLS";
	
	// actions of sales handler
	final public static String ADD_TRANSFER_REQUEST= "ADD_TRANSFER_REQUEST";
	final public static String PRNT_TRANSFER_REQUEST= "PRNT_TRANSFER_REQUEST";
	final public static String INIT_VIEW_EDIT_TRANSFER_REQUEST = "INIT_VIEW_EDIT_TRANSFER_REQUEST";
	final public static String GET_TRANSFER_REQUEST = "GET_TRANSFER_REQUEST";
	final public static String SHOW_TRANSFER_REQUEST = "SHOW_TRANSFER_REQUEST";
	final public static String INIT_EDIT_TRANSFER_REQUEST = "INIT_EDIT_TRANSFER_REQUEST";
	final public static String EDIT_TRANSFER_REQUEST = "EDIT_TRANSFER_REQUEST";
	final public static String INIT_APP_TRANSFER_REQUEST = "INIT_APP_TRANSFER_REQUEST";
	final public static String GET_APP_TRANSFER_REQUEST = "GET_APP_TRANSFER_REQUEST";
	final public static String APP_TRANSFER_REQUEST = "APP_TRANSFER_REQUEST";
	final public static String EDIT_TRANSFER_REQUEST_LR_DTLS = "EDIT_TRANSFER_REQUEST_LR_DTLS";
	final public static String INIT_EDIT_TRANSFER_REQUEST_LR_DTLS = "INIT_EDIT_TRANSFER_REQUEST_LR_DTLS";
	final public static String GET_EDIT_TRANSFER_REQUEST_LR_DTLS = "GET_EDIT_TRANSFER_REQUEST_LR_DTLS";
	
	// actions of add item handler
	final public static String ADD_ITM = "ADD_ITM";
	final public static String MOD_ITM = "MOD_ITM";
	final public static String GET_ITM = "GET_ITM";
	final public static String UPDT_ITM = "UPDT_ITM";
	final public static String INIT_ITM_DIS = "INIT_ITM_DIS";
	final public static String ITM_DIS = "ITM_DIS";
	final public static String INIT_ITM_CHECK_LIST = "INIT_ITM_CHECK_LIST";
	final public static String GET_ITM_CHECK_LIST = "GET_ITM_CHECK_LIST";
	final public static String UPDATE_ITM_CHECK_LIST = "UPDATE_ITM_CHECK_LIST";
	final public static String PRICE_LIST = "PRICE_LIST";
	final public static String WHOLESALES_PRICE_LIST = "WHOLESALES_PRICE_LIST";
	final public static String OFFER_PRICE_LIST = "OFFER_PRICE_LIST";
	final public static String LABEL = "LABEL";
	final public static String UPLOAD_PICTURE = "UPLOAD_PICTURE";
	final public static String INIT_UPLOAD_CATALOGUE = "INIT_UPLOAD_CATALOGUE";
	final public static String UPLOAD_CATALOGUE = "UPLOAD_CATALOGUE";
	final public static String INIT_VIEW_ITEM_DETAILS = "INIT_VIEW_ITEM_DETAILS";
	final public static String VIEW_ITEM_DETAILS = "VIEW_ITEM_DETAILS";
	final public static String INIT_MAIL_ITEM_DETAILS = "INIT_MAIL_ITEM_DETAILS";
	final public static String GET_MAIL_ITEM_DETAILS = "GET_MAIL_ITEM_DETAILS";
	final public static String MAIL_ITEM_DETAILS = "MAIL_ITEM_DETAILS";
	final public static String EMAIL_PRICE_LIST = "EMAIL_PRICE_LIST";
	final public static String EMAIL_PRICE_LIST_1 = "EMAIL_PRICE_LIST_1";
	
	// actions of add customer handler
	final public static String ADD_CUSTOMER = "ADD_CUSTOMER";
	final public static String MOD_CUSTOMER = "MOD_CUSTOMER";
	final public static String GET_CUSTOMER = "GET_CUSTOMER";
	final public static String DEL_CUSTOMER = "DEL_CUSTOMER";
	final public static String INIT_CUSTOMER = "INIT_CUSTOMER";
	final public static String CHECK_CUSTOMER_1 = "CHECK_CUSTOMER_1";
	final public static String CHECK_CUSTOMER_2 = "CHECK_CUSTOMER_2";
	final public static String MERGE_CUSTOMER_1 = "MERGE_CUSTOMER_1";
	final public static String MERGE_CUSTOMER_2 = "MERGE_CUSTOMER_2";
	final public static String INIT_CUSTOMER_MERGE = "INIT_CUSTOMER_MERGE";
	final public static String GET_CUSTOMER_LST = "GET_CUSTOMER_LST";
	final public static String UPDT_CUSTOMER = "UPDT_CUSTOMER";
	final public static String ADD_CITY = "ADD_CITY";
	final public static String GET_CITIES = "GET_CITIES";
	final public static String ADD_CUST_TYPE = "ADD_CUST_TYPE";
	final public static String INIT_CUSTOMER_LABEL = "INIT_CUSTOMER_LABEL";
	final public static String PRINT_CUSTOMER_LABEL = "PRINT_CUSTOMER_LABEL";
	final public static String EMAIL_CUSTOMER = "EMAIL_CUSTOMER";
	final public static String INIT_EMAIL_CUSTOMER = "INIT_EMAIL_CUSTOMER";
	final public static String GET_EMAIL_CUSTOMER = "GET_EMAIL_CUSTOMER";
	final public static String INIT_VIEW_EMAIL_CUSTOMER = "INIT_VIEW_EMAIL_CUSTOMER";
	final public static String VIEW_EMAIL_CUSTOMER = "VIEW_EMAIL_CUSTOMER";
	final public static String RESEND_EMAIL_CUSTOMER = "RESEND_EMAIL_CUSTOMER";
	
	// actions of add customer handler
	final public static String ADD_CUSTOMER_GRP = "ADD_CUSTOMER_GRP";
	final public static String MOD_CUSTOMER_GRP = "MOD_CUSTOMER_GRP";
	final public static String GET_CUSTOMER_GRP = "GET_CUSTOMER_GRP";
	final public static String UPDT_CUSTOMER_GRP = "UPDT_CUSTOMER_GRP";
	final public static String INIT_VIEW_CUSTOMER = "INIT_VIEW_CUSTOMER";
	final public static String VIEW_CUSTOMER = "VIEW_CUSTOMER";
	
	// actions of add access point handler
	final public static String ADD_ACCESS_POINT = "ADD_ACCESS_POINT";
	final public static String MOD_ACCESS_POINT = "MOD_ACCESS_POINT";
	final public static String GET_ACCESS_POINT = "GET_ACCESS_POINT";
	final public static String UPDT_ACCESS_POINT = "UPDT_ACCESS_POINT";
	final public static String CASH_ON_HAND = "CASH_ON_HAND";
	
	// actions of add USER_ACCESS handler
	final public static String ADD_USER = "ADD_USER";
	final public static String MOD_USER = "MOD_USER";
	final public static String GET_USER = "GET_USER";
	final public static String UPDT_USER = "UPDT_USER";
	final public static String UPDT_PASSWORD = "UPDT_PASSWORD";
	final public static String INIT_USER_DOC = "INIT_USER_DOC";
	final public static String GET_USER_DOC = "GET_USER_DOC";
	final public static String UPLOAD_USER_DOC = "UPLOAD_USER_DOC";
	final public static String INIT_TARGET = "INIT_TARGET";
	final public static String PREV_TARGET = "PREV_TARGET";
	final public static String NEXT_TARGET = "NEXT_TARGET";
	final public static String SAVE_TARGET = "SAVE_TARGET";
	
	// actions of add user access handler
	final public static String GET_USER_ACCESS = "GET_USER_ACCESS";
	final public static String UPDT_USER_ACCESS = "UPDT_USER_ACCESS";
	
	// actions of add offer handler
	final public static String ADD_OFFER = "ADD_OFFER";
	final public static String INIT_OFFER_LIST = "INIT_OFFER_LIST";
	final public static String MOD_OFFER = "MOD_OFFER";
	final public static String GET_OFFER = "GET_OFFER";
	final public static String UPDT_OFFER = "UPDT_OFFER";
	
	// actions of add tax handler
	final public static String UPDT_TAX = "UPDT_TAX";
	final public static String GET_TAX = "GET_TAX";
	final public static String INIT_ITEM_TAX  = "INIT_ITEM_TAX";
	final public static String GET_ITEM_TAX  = "GET_ITEM_TAX";
	final public static String UPDT_ITEM_TAX  = "UPDT_ITEM_TAX";
	
	// actions of add offer handler
	final public static String UPDATE_HOME = "UPDATE_HOME";
	
	// actions of add enquiry handler
	final public static String INIT_ENQUIRY = "INIT_ENQUIRY";
	final public static String ADD_ENQUIRY = "ADD_ENQUIRY";
	final public static String VIEW_ENQUIRY = "VIEW_ENQUIRY";
	final public static String GET_ENQUIRY = "GET_ENQUIRY";
//	final public static String GET_CUSTOMER = "GET_CUSTOMER";
//	final public static String UPDT_CUSTOMER = "UPDT_CUSTOMER";
	
	// action of salesman enquiry handler
	final public static String GET_PENDING_APPOINTMENT = "GET_PENDING_APPOINTMENT";
	final public static String INIT_REPLY_PENDING_APPOINTMENT = "INIT_REPLY_PENDING_APPOINTMENT";
	final public static String INIT_DELAY_PENDING_APPOINTMENT = "INIT_DELAY_PENDING_APPOINTMENT";
	final public static String INIT_CANCEL_PENDING_APPOINTMENT = "INIT_CANCEL_PENDING_APPOINTMENT";
	final public static String REPLY_PENDING_APPOINTMENT = "REPLY_PENDING_APPOINTMENT";
	final public static String DELAY_PENDING_APPOINTMENT = "DELAY_PENDING_APPOINTMENT";
	final public static String CANCEL_PENDING_APPOINTMENT = "CANCEL_PENDING_APPOINTMENT";
	final public static String INIT_NEW_ACTION1 = "INIT_NEW_ACTION1";
	final public static String INIT_NEW_ACTION2 = "INIT_NEW_ACTION2";
	final public static String ADD_NEW_ACTION = "ADD_NEW_ACTION";
	final public static String PRINT_ENQUIRY = "PRINT_ENQUIRY";
	final public static String GET_ENQ_ALERT_RPT = "GET_ENQ_ALERT_RPT";
	final public static String PRINT_ENQ_ALERT_RPT = "PRINT_ENQ_ALERT_RPT"; 
	
	// actions of salesman daily handler
	final public static String INIT_SALESMAN_DLY = "INIT_SALESMAN_DLY";
	final public static String INIT_SALESMAN_DLY1 = "INIT_SALESMAN_DLY1";
	final public static String ADD_SALESMAN_ITM = "ADD_SALESMAN_ITM";
	final public static String DEL_SALESMAN_ITM = "DEL_SALESMAN_ITM";
	final public static String SUB_SALESMAN_APP = "SUB_SALESMAN_APP";
	final public static String VIEW_SALESMAN_RPT = "VIEW_SALESMAN_RPT";
	final public static String GET_SALESMAN_RPT = "GET_SALESMAN_RPT";
	final public static String PRINT_SALESMAN_RPT = "PRINT_SALESMAN_RPT";
	final public static String REJECT_SALESMAN_RPT = "REJECT_SALESMAN_RPT";
	final public static String APPROVE_SALESMAN_RPT = "APPROVE_SALESMAN_RPT";
	final public static String INIT_SALESMAN_STATUS_RPT = "INIT_SALESMAN_STATUS_RPT";
	final public static String GET_SALESMAN_STATUS_RPT = "GET_SALESMAN_STATUS_RPT";
	final public static String PRINT_SALESMAN_STATUS_RPT = "PRINT_SALESMAN_STATUS_RPT";
	final public static String APPROVAL_SALESMAN_RPT = "APPROVAL_SALESMAN_RPT";
	final public static String APPROVAL_SALESMAN_RPT_DTLS = "APPROVAL_SALESMAN_RPT_DTLS";
	
	// actions of branch daily entry handler
	final public static String GET_DAY_ENTRIES = "GET_DAY_ENTRIES";
	final public static String ADD_DAY_ENTRY = "ADD_DAY_ENTRY";
	final public static String DEL_DAY_ENTRY = "DEL_DAY_ENTRY";
	final public static String APPROVE_DAY_ENTRIES = "APPROVE_DAY_ENTRIES";
	final public static String INIT_APPROVE_DAY_ENTRIES = "INIT_APPROVE_DAY_ENTRIES";
	final public static String SHOW_APPROVE_DAY_ENTRIES = "SHOW_APPROVE_DAY_ENTRIES";
	final public static String SEND_APPROVE_DAY_ENTRIES = "SEND_APPROVE_DAY_ENTRIES";
	final public static String REJ_APPROVE_DAY_ENTRIES = "REJ_APPROVE_DAY_ENTRIES";
	final public static String PRNT_DAY_BOOK = "PRNT_DAY_BOOK";
	final public static String SYNC_SALES_DATA = "SYNC_SALES_DATA";
	final public static String SYNC_REVIEW_DATA = "SYNC_REVIEW_DATA";
	final public static String VIEW_DLY_ENTRY_RPT = "VIEW_DLY_ENTRY_RPT";
	final public static String GET_PD_CHQ_LST = "GET_PD_CHQ_LST";
	final public static String GET_BANK_DEP_LST = "GET_BANK_DEP_LST";
	final public static String INIT_DLY_ENTRY_STATUS_RPT = "INIT_DLY_ENTRY_STATUS_RPT";
	final public static String GET_DLY_ENTRY_STATUS_RPT = "GET_DLY_ENTRY_STATUS_RPT";
	final public static String INIT_BILL_PAYMENT = "INIT_BILL_PAYMENT";
	final public static String GET_BILL_PAYMENT = "GET_BILL_PAYMENT";
	final public static String ADD_BILL_PAYMENT = "ADD_BILL_PAYMENT";
	final public static String DEL_BILL_PAYMENT = "DEL_BILL_PAYMENT";
	final public static String GET_LABOUR_BILL_PAYMENT = "GET_LABOUR_BILL_PAYMENT";
	final public static String ADD_LABOUR_BILL_PAYMENT = "ADD_LABOUR_BILL_PAYMENT";
	final public static String DEL_LABOUR_BILL_PAYMENT = "DEL_LABOUR_BILL_PAYMENT";
	final public static String INIT_PURCHASE_BILL_PAYMENT = "INIT_PURCHASE_BILL_PAYMENT";
	final public static String GET_PURCHASE_BILL_PAYMENT = "GET_PURCHASE_BILL_PAYMENT";
	final public static String ADD_PURCHASE_BILL_PAYMENT = "ADD_PURCHASE_BILL_PAYMENT";
	final public static String DEL_PURCHASE_BILL_PAYMENT = "DEL_PURCHASE_BILL_PAYMENT";
	final public static String INIT_ORDER_BILL_PAYMENT = "INIT_ORDER_BILL_PAYMENT";
	final public static String GET_ORDER_BILL_PAYMENT = "GET_ORDER_BILL_PAYMENT";
	final public static String ADD_ORDER_BILL_PAYMENT = "ADD_ORDER_BILL_PAYMENT";
	final public static String DEL_ORDER_BILL_PAYMENT = "DEL_ORDER_BILL_PAYMENT";
	final public static String INIT_UNDEPOSIT_CHQ = "INIT_UNDEPOSIT_CHQ";
	final public static String GER_UNDEPOSIT_CHQ = "GET_UNDEPOSIT_CHQ";
	final public static String DEPOSIT_CHQ = "DEPOSIT_CHQ";

	
	// actions of collection handler
	final public static String GET_OUTSTANDING_RPT = "GET_OUTSTANDING_RPT";
	final public static String GET_CUST_OUTSTANDING_RPT = "GET_CUST_OUTSTANDING_RPT";
	final public static String VIEW_CUST_OUTSTANDING_RPT = "VIEW_CUST_OUTSTANDING_RPT";
	final public static String BACK_OUTSTANDING_RPT = "BACK_OUTSTANDING_RPT";
	final public static String ADD_RMK_CUST_OUTSTANDING_RPT = "ADD_RMK_CUST_OUTSTANDING_RPT";
	final public static String PRNT_OUTSTANDING_RPT = "PRNT_OUTSTANDING_RPT";
	final public static String GET_BILL_PAYMENT_DTLS = "GET_BILL_PAYMENT_DTLS";
	final public static String GET_FOLLOWUP_DTLS = "GET_FOLLOWUP_DTLS";
	final public static String GET_TRANSFER_FOLLOWUP_DTLS = "GET_TRANSFER_FOLLOWUP_DTLS";
	
	// actions of report handler
	final public static String GET_MASTER_RPT = "GET_MASTER_RPT";
	final public static String PRNT_MASTER_RPT = "PRNT_MASTER_RPT";
	final public static String EXPT_MASTER_RPT = "EXPT_MASTER_RPT";
	final public static String INIT_ITEM_RPT = "INIT_ITEM_RPT";
	final public static String GET_ITEM_RPT = "GET_ITEM_RPT";
	final public static String PRNT_ITEM_RPT = "PRNT_ITEM_RPT";
	final public static String EXPT_ITEM_RPT = "EXPT_ITEM_RPT";
	
	// actions of report handler
	final public static String GET_STOCK_REGISTER_RPT = "GET_STOCK_REGISTER_RPT";
	final public static String GET_STOCK_ITEM_RPT = "GET_STOCK_ITEM_RPT";
//	final public static String PRNT_MASTER_RPT = "PRNT_MASTER_RPT";
//	final public static String EXPT_MASTER_RPT = "EXPT_MASTER_RPT";
//	final public static String INIT_ITEM_RPT = "INIT_ITEM_RPT";
//	final public static String GET_ITEM_RPT = "GET_ITEM_RPT";
//	final public static String PRNT_ITEM_RPT = "PRNT_ITEM_RPT";
//	final public static String EXPT_ITEM_RPT = "EXPT_ITEM_RPT";

	// actions of report handler
	final public static String GET_CHQ_ENTRIES_RPT = "GET_CHQ_ENTRIES_RPT";
	final public static String PRNT_CHQ_ENTRIES_RPT = "PRNT_CHQ_ENTRIES_RPT";
	final public static String EXPT_CHQ_ENTRIES_RPT = "EXPT_CHQ_ENTRIES_RPT";
	final public static String INIT_BANK_ENTRIES_RPT = "INIT_BANK_ENTRIES_RPT";
	final public static String GET_BANK_ENTRIES_RPT = "GET_BANK_ENTRIES_RPT";
	final public static String PRNT_BANK_ENTRIES_RPT = "PRNT_BANK_ENTRIES_RPT";
	final public static String EXPT_BANK_ENTRIES_RPT = "EXPT_BANK_ENTRIES_RPT";
	final public static String INIT_BANK_LEDGER_RPT = "INIT_BANK_LEDGER_RPT";
	final public static String GET_BANK_LEDGER_RPT = "GET_BANK_LEDGER_RPT";
	final public static String PRNT_BANK_LEDGER_RPT = "PRNT_BANK_LEDGER_RPT";
	final public static String EXPT_BANK_LEDGER_RPT = "EXPT_BANK_LEDGER_RPT";
	
	// actions of sales handler
	final public static String SYNC_DATA_FROM_DB = "SYNC_DATA_FROM_DB";
	final public static String SYNC_DATA_TO_DB = "SYNC_DATA_TO_DB";
	
	//action of admin master data
	final public static String SAVE_DATA = "SAVE_DATA";
	final public static String REMOVE_DATA = "REMOVE_DATA";
	
	//action of promotional mail
	final public static String SEND_MAIL = "SEND_MAIL";
	final public static String SEND_SMS = "SEND_SMS";
	final public static String SEND_LR_SMS = "SEND_LR_SMS";

	// actions of branch daily entry handler
	final public static String GET_LEDGER_RPT = "GET_LEDGER_RPT";
	final public static String PRNT_LEDGER_RPT = "PRNT_LEDGER_RPT";
	final public static String EXPT_LEDGER_RPT = "EXPT_LEDGER_RPT";
	final public static String EMAIL_LEDGER_RPT = "EMAIL_LEDGER_RPT";
	final public static String INIT_LEDGER_RPT = "INIT_LEDGER_RPT";
	
	// actions of ajax handler
	final public static String AJAX_GET_CUST_LST = "AJAX_GET_CUST_LST";
	final public static String AJAX_GET_SALES_CUST_LST = "AJAX_GET_SALES_CUST_LST";
	final public static String AJAX_CHECK_CUSTOMER = "AJAX_CHECK_CUSTOMER";
	final public static String AJAX_CHECK_CATALOGUE = "AJAX_CHECK_CATALOGUE";
	final public static String AJAX_CHECK_PHOTO = "AJAX_CHECK_PHOTO";
	final public static String AJAX_REFRESH = "AJAX_REFRESH";
	final public static String AJAX_GET_SALES_CUST_RPT_LST = "AJAX_GET_SALES_CUST_RPT_LST";
	
	// actions of branch stock report handler
	final public static String INIT_OPEN_STOCK = "INIT_OPEN_STOCK";
	final public static String UPDATE_OPEN_STOCK = "UPDATE_OPEN_STOCK";
	
	// actions of labour bill handler
	final public static String SALES_LABOUR_BILL = "SALES_LABOUR_BILL";
	final public static String ADD_LABOUR_BILL = "ADD_LABOUR_BILL";
	final public static String GET_LABOUR_BILL = "GET_LABOUR_BILL";
	final public static String GET_LABOUR_BILL_POPUP = "GET_LABOUR_BILL_POPUP";
	final public static String INIT_LABOUR_BILL = "INIT_LABOUR_BILL";
	final public static String INIT_EDIT_LABOUR_BILL = "INIT_EDIT_LABOUR_BILL";
	final public static String EDIT_LABOUR_BILL = "EDIT_LABOUR_BILL";
	final public static String PRNT_LABOUR_BILL = "PRNT_LABOUR_BILL";

	// actions of credit note handler
	final public static String ADD_CREDIT_NOTE = "ADD_CREDIT_NOTE";
	final public static String PRNT_CREDIT_NOTE = "PRNT_CREDIT_NOTE";
	final public static String INIT_VIEW_EDIT_CREDIT_NOTE = "INIT_VIEW_EDIT_CREDIT_NOTE";
	final public static String GET_CREDIT_NOTE = "GET_CREDIT_NOTE";
	final public static String VIEW_CREDIT_NOTE = "VIEW_CREDIT_NOTE";
	final public static String EMAIL_CREDIT_NOTE = "EMAIL_CREDIT_NOTE";
	final public static String INIT_GET_CREDIT_NOTE_DATE = "INIT_GET_CREDIT_NOTE_DATE";
	final public static String GET_CREDIT_NOTE_DATE = "GET_CREDIT_NOTE_DATE";
	final public static String EDIT_CREDIT_NOTE_DATE = "EDIT_CREDIT_NOTE_DATE";
	final public static String INIT_EDIT_CREDIT_NOTE = "INIT_EDIT_CREDIT_NOTE";
	final public static String ACC_EDIT_CREDIT_NOTE = "ACC_EDIT_CREDIT_NOTE";
	final public static String CNFM_EDIT_CREDIT_NOTE = "CNFM_EDIT_CREDIT_NOTE";
	final public static String EDIT_CREDIT_NOTE = "EDIT_CREDIT_NOTE";
	final public static String EDIT_CREDIT_NOTE_LR_DTLS = "EDIT_CREDIT_NOTE_LR_DTLS";
	final public static String INIT_EDIT_CREDIT_NOTE_LR_DTLS = "INIT_EDIT_CREDIT_NOTE_LR_DTLS";
	final public static String GET_EDIT_CREDIT_NOTE_LR_DTLS = "GET_EDIT_CREDIT_NOTE_LR_DTLS";
	
	// actions of ledger adjustment handler
	final public static String VIEW_LEDGER_ADJUSTMENT = "VIEW_LEDGER_ADJUSTMENT";
	final public static String DELETE_LEDGER_ADJUSTMENT = "DELETE_LEDGER_ADJUSTMENT";
	final public static String GET_ORDER_ADVANCE = "GET_ORDER_ADVANCE";
	final public static String GET_ORDER_ADVANCE_PENDING_SALES = "GET_ORDER_ADVANCE_PENDING_SALES";
	final public static String ADD_ORDER_ADVANCE_PENDING_SALES = "ADD_ORDER_ADVANCE_PENDING_SALES";
	
	// actions of branch stock entry handler
	final public static String GET_STOCK_DTLS = "GET_STOCK_DTLS";
	final public static String UPDT_STOCK_DTLS = "UPDT_STOCK_DTLS";
	
	// actions of messenger handler
	final public static String START_CONVERSATION = "START_CONVERSATION";
	final public static String ADD_COMMENT = "ADD_COMMENT";
	final public static String MY_CONVERSATION = "MY_CONVERSATION";
	final public static String MY_CLOSED_CONVERSATION = "MY_CLOSED_CONVERSATION";
	final public static String SHOW_CONVERSATION = "SHOW_CONVERSATION";
	final public static String GET_CONVERSATION = "GET_CONVERSATION";
	final public static String CLOSE_CONVERSATION = "CLOSE_CONVERSATION";
	final public static String REOPEN_CONVERSATION = "REOPEN_CONVERSATION";
	
	// action of attendance handler
	final public static String GET_ATTENDANCE = "GET_ATTENDANCE";
	final public static String UPDATE_ATTENDANCE = "UPDATE_ATTENDANCE";
	
	// actions of salary handler
	final public static String GET_SALARY_DETAILS = "GET_SALARY_DETAILS";
	final public static String GET_STAFF_SALARY_DETAILS = "GET_STAFF_SALARY_DETAILS";
	final public static String INIT_SALARY_RPT = "INIT_SALARY_RPT";
	final public static String GET_SALARY_RPT = "GET_SALARY_RPT";
	final public static String SAVE_STAFF_SALARY_DETAILS = "SAVE_STAFF_SALARY_DETAILS";
}
