package com.en.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.BranchDayEntryBroker;
import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.DayBookEntryModel;
import com.en.model.EntryModel;
import com.en.model.LabourBillModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.PurchaseModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.en.util.Utils;

public class BranchDayEntryHandler extends Basehandler {

	public static String getPageName() {
		return BRANCH_DLY_ENTRY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initDayEntry.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_DAY_ENTRIES)){
        	pageType = getDayEntries(request);			
		} else if(strAction.equals(ADD_DAY_ENTRY)){
			pageType = addDayEntry(request);			
		} else if(strAction.equals(DEL_DAY_ENTRY)){
			pageType = delDayEntry(request);			
		} else if(strAction.equals(PRNT_DAY_BOOK)){
			pageType = printDayBook(request);			
		} else if(strAction.equals(SYNC_SALES_DATA)){
			pageType = syncSalesData(request);			
		} else if(strAction.equals(SYNC_REVIEW_DATA)){
			pageType = syncReviewDayBook(request);			
		} else if(strAction.equals(SEND_APPROVE_DAY_ENTRIES)){
			pageType = sendForApproval(request);			
		} else if(strAction.equals(REJ_APPROVE_DAY_ENTRIES)){
			pageType = rejectData(request);			
		} else if(strAction.equals(APPROVE_DAY_ENTRIES)){
			pageType = approveData(request);			
		} else if(strAction.equals(SHOW_APPROVE_DAY_ENTRIES)){
			pageType = showApprovalData(request);			
		} else if(strAction.equals(INIT_APPROVE_DAY_ENTRIES)){
			pageType = initApprovalReport(request);			
		} else if(strAction.equals(VIEW_DLY_ENTRY_RPT)){
			pageType = initViewReport(request);			
		} else if(strAction.equals(GET_DLY_ENTRY_STATUS_RPT)){
			pageType = getViewReport(request);			
		} else if(strAction.equals(GET_PD_CHQ_LST)){
			pageType = getPDChqLst(request);			
		} else if(strAction.equals(GET_BANK_DEP_LST)){
			pageType = getBankDepositLst(request);			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = getExportData(request);			
		} else if(strAction.equals(INIT_BILL_PAYMENT)){
			pageType = initializeBillPayment(request);			
		} else if(strAction.equals(GET_BILL_PAYMENT)){
			pageType = getPeningBillLst(request);			
		} else if(strAction.equals(ADD_BILL_PAYMENT)){
			pageType = addBillPayment(request);			
		} else if(strAction.equals(DEL_BILL_PAYMENT)){
			pageType = delBillPaymentEntry(request);			
		} else if(strAction.equals(GET_LABOUR_BILL_PAYMENT)){
			pageType = getPeningLabourBillLst(request);			
		} else if(strAction.equals(ADD_LABOUR_BILL_PAYMENT)){
			pageType = addLabourBillPayment(request);			
		} else if(strAction.equals(DEL_LABOUR_BILL_PAYMENT)){
			pageType = delLabourBillPaymentEntry(request);			
		} else if(strAction.equals(INIT_PURCHASE_BILL_PAYMENT)){
			pageType = initializePurchaseBillPayment(request);			
		} else if(strAction.equals(GET_PURCHASE_BILL_PAYMENT)){
			pageType = getPeningPurchaseBillLst(request);			
		} else if(strAction.equals(ADD_PURCHASE_BILL_PAYMENT)){
			pageType = addPurchaseBillPayment(request);			
		} else if(strAction.equals(DEL_PURCHASE_BILL_PAYMENT)){
			pageType = delPurchaseBillPaymentEntry(request);			
		} else if(strAction.equals(INIT_ORDER_BILL_PAYMENT)){
			pageType = initializeOrderBillPayment(request);			
		} else if(strAction.equals(GET_ORDER_BILL_PAYMENT)){
			pageType = getPeningOrderBillLst(request);			
		} else if(strAction.equals(ADD_ORDER_BILL_PAYMENT)){
			pageType = addOrderBillPayment(request);			
		} else if(strAction.equals(DEL_ORDER_BILL_PAYMENT)){
			pageType = delOrderBillPaymentEntry(request);			
		} else if(strAction.equals(INIT_UNDEPOSIT_CHQ)){
			pageType = getUndepositedChqsLst(request);			
		} else if(strAction.equals(DEPOSIT_CHQ)){
			pageType = depositedChq(request);			
		}
		return pageType;
	}

	private String delLabourBillPaymentEntry(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String entryId = request.getParameter("txtIndex");
		String id = request.getParameter("txtId");
		String date = request.getParameter("txtRptDate");
		MessageModel msg = broker.delLabourBillPaymentEntry(id, entryId);
		request.setAttribute(MESSAGES, msg);
		DayBookEntryModel model = broker.getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), Utils.convertToSQLDate(date));
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

	private String addLabourBillPayment(HttpServletRequest request) {
		EntryModel[] billEntry = populateLabourBillPayEntryModel(request);
		MessageModel msg = getBranchDayEntryBroker().addLabourBillPaymentEntry(billEntry);
		request.setAttribute(MESSAGES, msg);
		return getDayEntries(request);
	}

	private EntryModel[] populateLabourBillPayEntryModel(
			HttpServletRequest request) {
		ArrayList<EntryModel> billPayDtls = new ArrayList<EntryModel>(0); 
		EntryModel model = new EntryModel();
		int branchid = Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT));
		int type = Integer.parseInt(request.getParameter("sType"));
		String entryDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String chqDt = (request.getParameter("txtChqDate") != null && !request.getParameter("txtChqDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtChqDate")) : "";
		String depDt = (request.getParameter("txtDepDate") != null && !request.getParameter("txtDepDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtDepDate")) : "";
		int Id = (request.getParameter("txtId") != null && !request.getParameter("txtId").trim().equals("")) ? Integer.parseInt(request.getParameter("txtId")) : 0;
		String heshBank = "";
		if(type == 47 || type == 48 || type == 49){
			heshBank = (request.getParameter("sHeshBank") != null && !request.getParameter("sHeshBank").trim().equals("--")) ? request.getParameter("sHeshBank") : "";
		}
		int custId = Integer.parseInt(request.getParameter("sCustomer"));
		String custBank = "";
		String chqNo = "";
		if(type == 48){
			custBank = (request.getParameter("txtCustBank") != null && !request.getParameter("txtCustBank").trim().equals("")) ? request.getParameter("txtCustBank"): "";
			chqNo = (request.getParameter("txtChqNo") != null && !request.getParameter("txtChqNo").trim().equals("")) ? request.getParameter("txtChqNo") : "";
		}
		String pendingBills = request.getParameter("payDtls");
		StringTokenizer stRow = new StringTokenizer(pendingBills,"#");
		int billBranchId = 0, billNo =0;
		double amt = 0, adjAmt = 0;
		while (stRow.hasMoreElements()) {
			model = new EntryModel();
			model.setUser(((UserModel)request.getSession().getAttribute(USER)).getUserId());
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			billBranchId = Integer.parseInt(stCell.nextToken());
			billNo = Integer.parseInt(stCell.nextToken());
			amt = Double.parseDouble(stCell.nextToken());
			adjAmt = Double.parseDouble(stCell.nextToken());
			model.setBillNo(billNo);
			model.getBillBranch().setAccessId(billBranchId);
			model.setBranchId(branchid);
			model.setEntryType(type);
			if(type == 48){
				model.setChqDt(chqDt);
				model.setEntryDate(depDt);
			} else {
				model.setEntryDate(entryDt);
			}
			model.setId(Id);
			if(type == 47 || type == 48 || type == 49){
				model.setBankName(heshBank);
			}
			model.setAmount(amt);
			model.setAdjAmt(adjAmt);
			model.getCustomer().setCustomerId(custId);
			if(type == 48){
				model.setChqNo(chqNo);
				model.setCustBankName(custBank);
			}
			model.setCrdr("C");
			if(model.getEntryType()!=47 && model.getEntryType()!=48 && model.getEntryType()!=49){
				model.setCash(true);
			} else {
				model.setCash(false);
			}
			if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
				model.setRemark(request.getParameter("taRemark"));
			billPayDtls.add(model);
		}
		return (EntryModel[])billPayDtls.toArray(new EntryModel[0]);
	}

	private String getPeningLabourBillLst(HttpServletRequest request) {
		initializeLabourBillPayment(request);
		LabourBillModel[] models = getCollectionBroker().getOutstandingLabourBillRpt(request.getParameter("sCustomer"), new String[] {"--"});
		if(models.length > 0){
			request.setAttribute("sType", request.getParameter("sType"));
			request.setAttribute(PENDING_BILLS_LIST, models);
			request.setAttribute("sCustomer", request.getParameter("sCustomer"));
			CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
			request.setAttribute(CUSTOMER, customer);
			return "initLabourBillCollection.jsp";
		} else {
			MessageModel msgs = new MessageModel();
			msgs.addNewMessage(new Message(ERROR, "No bill payment is pending."));
			request.setAttribute(MESSAGES, msgs);
			return getDayEntries(request);
		}
	}

	private String initializeLabourBillPayment(HttpServletRequest request) {
//		CustomerModel[] customers = getSalesBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		DayBookEntryModel model = null;
		model = getBranchDayEntryBroker().getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), date);
		if(model == null){
			model = new DayBookEntryModel();
			model.setEntryDate(date);
		}
		request.setAttribute(FORM_DATA, model);
		return "initLabourBillCollection.jsp";
	}

	private String delOrderBillPaymentEntry(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String entryId = request.getParameter("txtIndex");
		String id = request.getParameter("txtId");
		String date = request.getParameter("txtRptDate");
		MessageModel msg = broker.delOrderEntry(id, entryId);
		request.setAttribute(MESSAGES, msg);
		DayBookEntryModel model = broker.getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), Utils.convertToSQLDate(date));
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

	private String addOrderBillPayment(HttpServletRequest request) {
		EntryModel[] billEntry = populateOrderBillPayEntryModel(request);
		MessageModel msg = getBranchDayEntryBroker().addOrderAdvanceEntry(billEntry);
		request.setAttribute(MESSAGES, msg);
		return getDayEntries(request);
	}

	private String getPeningOrderBillLst(HttpServletRequest request) {
		initializeOrderBillPayment(request);
		OrderModel[] models = getBranchDayEntryBroker().getOrderDtls(request.getParameter("sCustomer"), new String[] {"--"});
		if(models.length > 0){
			request.setAttribute("sType", request.getParameter("sType"));
			request.setAttribute(PENDING_BILLS_LIST, models);
			request.setAttribute("sCustomer", request.getParameter("sCustomer"));
			CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
			request.setAttribute(CUSTOMER, customer);
			return "initOrderBillPayment.jsp";
		} else {
			MessageModel msgs = new MessageModel();
			msgs.addNewMessage(new Message(ERROR, "No bill payment is pending."));
			request.setAttribute(MESSAGES, msgs);
			return getDayEntries(request);
		}
	}

	private String initializeOrderBillPayment(HttpServletRequest request) {
//		CustomerModel[] customers = getOrderBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		DayBookEntryModel model = null;
		model = getBranchDayEntryBroker().getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), date);
		if(model == null){
			model = new DayBookEntryModel();
			model.setEntryDate(date);
		}
		request.setAttribute(FORM_DATA, model);
		return "initOrderBillPayment.jsp";
	}

	private String delPurchaseBillPaymentEntry(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String entryId = request.getParameter("txtIndex");
		String id = request.getParameter("txtId");
		String date = request.getParameter("txtRptDate");
		MessageModel msg = broker.delPurchaseEntry(id, entryId);
		request.setAttribute(MESSAGES, msg);
		DayBookEntryModel model = broker.getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), Utils.convertToSQLDate(date));
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

	private String addPurchaseBillPayment(HttpServletRequest request) {
		EntryModel[] billEntry = populatePurchaseBillPayEntryModel(request);
		MessageModel msg = getBranchDayEntryBroker().addPurchasePaymentEntry(billEntry);
		request.setAttribute(MESSAGES, msg);
		return getDayEntries(request);
	}

	private String getPeningPurchaseBillLst(HttpServletRequest request) {
		initializePurchaseBillPayment(request);
		PurchaseModel[] models = getBranchDayEntryBroker().getPurchaseDtls(request.getParameter("sCustomer"), new String[] {"--"});
		if(models.length > 0){
			request.setAttribute("sType", request.getParameter("sType"));
			request.setAttribute(PENDING_BILLS_LIST, models);
			request.setAttribute("sCustomer", request.getParameter("sCustomer"));
			CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
			request.setAttribute(CUSTOMER, customer);
			return "initPurchaseBillPayment.jsp";
		} else {
			MessageModel msgs = new MessageModel();
			msgs.addNewMessage(new Message(ERROR, "No bill payment is pending."));
			request.setAttribute(MESSAGES, msgs);
			return getDayEntries(request);
		}
	}

	private String initializePurchaseBillPayment(HttpServletRequest request) {
		CustomerModel[] customers = getPurchaseBroker().getCustomer();
		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		DayBookEntryModel model = null;
		model = getBranchDayEntryBroker().getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), date);
		if(model == null){
			model = new DayBookEntryModel();
			model.setEntryDate(date);
		}
		request.setAttribute(FORM_DATA, model);
		return "initPurchaseBillPayment.jsp";
	}

	private String depositedChq(HttpServletRequest request) {
		MessageModel msg = getBranchDayEntryBroker().depositChq(request.getParameter("id"), request.getParameter("entryId"));
		request.setAttribute(MESSAGES, msg);
		return getUndepositedChqsLst(request);
	}

	private String getUndepositedChqsLst(HttpServletRequest request) {
		EntryModel[] undepostedChqLst = getBranchDayEntryBroker().getUndepositedChqLst(((AccessPointModel) request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		request.setAttribute(FORM_DATA, undepostedChqLst);
		return "undepositedChqs.jsp";
	}

	private String getBankDepositLst(HttpServletRequest request) {
		EntryModel[] pdChqs = getBranchDayEntryBroker().getBankDepositEntries(((AccessPointModel) request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		request.setAttribute(PD_CHEQUES_LIST, pdChqs);
		request.setAttribute("pageTitle", "Bank Direct Deposits");
		return "showPDCheques.jsp";
	}

	private String getPDChqLst(HttpServletRequest request) {
		EntryModel[] pdChqs = getBranchDayEntryBroker().getPDChequesEntries(((AccessPointModel) request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		request.setAttribute(PD_CHEQUES_LIST, pdChqs);
		request.setAttribute("pageTitle", "Post Date Cheques");
		return "showPDCheques.jsp";
	}

	private String syncReviewDayBook(HttpServletRequest request) {
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		getBranchDayEntryBroker().syncSalesData(date, (AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		DayBookEntryModel model = getBranchDayEntryBroker().getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), date);
		request.setAttribute(FORM_DATA, model);
		return "syncReviewDayBook.jsp";
	}

	private String delBillPaymentEntry(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String entryId = request.getParameter("txtIndex");
		String id = request.getParameter("txtId");
		String date = request.getParameter("txtRptDate");
		MessageModel msg = broker.delBillPaymentEntry(id, entryId);
		request.setAttribute(MESSAGES, msg);
		DayBookEntryModel model = broker.getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), Utils.convertToSQLDate(date));
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

	private String addBillPayment(HttpServletRequest request) {
		EntryModel[] billEntry = populateBillPayEntryModel(request);
		MessageModel msg = getBranchDayEntryBroker().addBillPaymentEntry(billEntry);
		request.setAttribute(MESSAGES, msg);
		return getDayEntries(request);
	}

	private String getPeningBillLst(HttpServletRequest request) {
		initializeBillPayment(request);
		SalesModel[] models = getCollectionBroker().getOutstandingRpt(request.getParameter("sCustomer"), new String[] {"--"},"--");
		if(models.length > 0){
			request.setAttribute("sType", request.getParameter("sType"));
			request.setAttribute(PENDING_BILLS_LIST, models);
			request.setAttribute("sCustomer", request.getParameter("sCustomer"));
			CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
			request.setAttribute(CUSTOMER, customer);
			return "initBillCollection.jsp";
		} else {
			MessageModel msgs = new MessageModel();
			msgs.addNewMessage(new Message(ERROR, "No bill payment is pending."));
			request.setAttribute(MESSAGES, msgs);
			return getDayEntries(request);
		}
	}

	private String initializeBillPayment(HttpServletRequest request) {
//		CustomerModel[] customers = getSalesBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		DayBookEntryModel model = null;
		model = getBranchDayEntryBroker().getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), date);
		if(model == null){
			model = new DayBookEntryModel();
			model.setEntryDate(date);
		}
		request.setAttribute(FORM_DATA, model);
		return "initBillCollection.jsp";
	}

	private String getExportData(HttpServletRequest request) {
		String pageType = "exportDayBookEntries.jsp";
		EntryModel[] model = getBranchDayEntryBroker().getExportData(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String getViewReport(HttpServletRequest request) {
		AccessPointModel[] accessPoint = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINT, accessPoint);
		String accessId = request.getParameter("txtAccessId");
		if(!request.getSession().getAttribute(ACCESS_POINT).equals("0")){
			accessId = ((AccessPointModel) request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId()+"";
		} 
		if(request.getSession().getAttribute(ACCESS_POINT).equals("2")){
			accessId = "2";
		}
		String rptDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		DayBookEntryModel model = getBranchDayEntryBroker().getBranchDayEntries(accessId, rptDt);
		request.setAttribute(FORM_DATA, model);
		return "viewDailyEntryRpt.jsp";
	}

	private String initViewReport(HttpServletRequest request) {
		AccessPointModel[] accessPoint = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINT, accessPoint);
		return "viewDailyEntryRpt.jsp";
	}

	private String rejectData(HttpServletRequest request) {
		String id = request.getParameter("txtId");
		MessageModel msg = getBranchDayEntryBroker().rejectApproval(id);
		request.setAttribute(MESSAGES, msg);
		return initApprovalReport(request);
	}

	private String approveData(HttpServletRequest request) {
		String id = request.getParameter("txtId");
		MessageModel msg = getBranchDayEntryBroker().approveBook(id, (UserModel)request.getSession().getAttribute(USER));
		request.setAttribute(MESSAGES, msg);
		return initApprovalReport(request);
	}

	private String showApprovalData(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String id = request.getParameter("txtId");
		DayBookEntryModel model = broker.getBranchDayEntries(Long.parseLong(id));
		request.setAttribute(FORM_DATA, model);
		SalesModel[] sales = getSalesBroker().getTodaySalesDetails(model.getEntryDate(), model.getEntryDate(), model.getBranch().getAccessId()+"");	
		request.setAttribute(SALES_DATA, sales);	
		return "showDayBookApproval.jsp";
	}

	private String initApprovalReport(HttpServletRequest request) {
		String pageType = "dayBookApproval.jsp";
		HashMap<String, DayBookEntryModel[]> awaitingLst = getBranchDayEntryBroker().getAwaitingList();
		request.setAttribute(FORM_DATA, awaitingLst);
		return pageType;
	}

	private String sendForApproval(HttpServletRequest request) {
		String id = request.getParameter("txtId");
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		MessageModel msg = getBranchDayEntryBroker().sendForApproval(id, date, (AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(MESSAGES, msg);
		return getDayEntries(request);
	}

	private String syncSalesData(HttpServletRequest request) {
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		MessageModel msg = getBranchDayEntryBroker().syncSalesData(date, (AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(MESSAGES, msg);
		return getDayEntries(request);
	}

	private String printDayBook(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String id = request.getParameter("txtId");
		DayBookEntryModel model = broker.getBranchDayEntries(Long.parseLong(id));
		request.setAttribute(FORM_DATA, model);
		EntryModel[] pdChqs = broker.getPDChequesEntries(model.getBranch().getAccessId());
		request.setAttribute(PD_CHEQUES_LIST, pdChqs);
		return "printDayBook.jsp";
	}

	private String delDayEntry(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		String entryId = request.getParameter("txtIndex");
		String id = request.getParameter("txtId");
		String date = request.getParameter("txtRptDate");
		MessageModel msg = broker.delEntry(id,entryId);
		request.setAttribute(MESSAGES, msg);
		DayBookEntryModel model = broker.getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), Utils.convertToSQLDate(date));
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

	private String addDayEntry(HttpServletRequest request) {
		BranchDayEntryBroker broker = getBranchDayEntryBroker();
		EntryModel entry = populateEntryModel(request);
		MessageModel msg = broker.addEntry(entry);
		request.setAttribute(MESSAGES, msg);
		DayBookEntryModel model = broker.getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), entry.getEntryDate());
		if(model == null){
			model = new DayBookEntryModel();
			model.setEntryDate(entry.getEntryDate());
		}
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

	private EntryModel populateEntryModel(HttpServletRequest request) {
		EntryModel model = new EntryModel();
		model.setUser(((UserModel)request.getSession().getAttribute(USER)).getUserId());
		model.setBranchId((((String)request.getSession().getAttribute(ACCESS_POINT)).equals("2")) ? 2 : (((String)request.getSession().getAttribute(ACCESS_POINT)).equals("3")) ? 3 : ((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		if(request.getParameter("sType") != null && !request.getParameter("sType").trim().equals(""))
			model.setEntryType(Integer.parseInt(request.getParameter("sType")));
		if(request.getParameter("txtRptDate") != null && !request.getParameter("txtRptDate").trim().equals(""))
			model.setEntryDate(Utils.convertToSQLDate(request.getParameter("txtRptDate")));
		if(request.getParameter("txtId") != null && !request.getParameter("txtId").trim().equals(""))
			model.setId(Integer.parseInt(request.getParameter("txtId")));
		if(request.getParameter("sHeshBank") != null && !request.getParameter("sHeshBank").trim().equals("--"))
			model.setBankName(request.getParameter("sHeshBank"));
		if(request.getParameter("txtAmount") != null && !request.getParameter("txtAmount").trim().equals(""))
			model.setAmount(Double.parseDouble(request.getParameter("txtAmount")));
		if(model.getEntryType() == 1 || model.getEntryType() == 2 || model.getEntryType() == 3 || model.getEntryType() == 4 || 
				model.getEntryType() == 81 || model.getEntryType() == 11 || model.getEntryType() == 21 || model.getEntryType() == 22 || model.getEntryType() == 23 || model.getEntryType() == 24 || model.getEntryType() == 55 || model.getEntryType() == 56 || model.getEntryType() > 50){
			if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals("--"))
				model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
			if(model.getEntryType() == 1 || model.getEntryType() == 23){
				if(request.getParameter("txtChqNo") != null && !request.getParameter("txtChqNo").trim().equals(""))
					model.setChqNo(request.getParameter("txtChqNo"));
				if(request.getParameter("txtCustBank") != null && !request.getParameter("txtCustBank").trim().equals(""))
					model.setCustBankName(request.getParameter("txtCustBank"));
				if(request.getParameter("txtChqDate") != null && !request.getParameter("txtChqDate").trim().equals(""))
					model.setChqDt(Utils.convertToSQLDate(request.getParameter("txtChqDate")));
				if(request.getParameter("txtDepDate") != null && !request.getParameter("txtDepDate").trim().equals(""))
					model.setEntryDate(Utils.convertToSQLDate(request.getParameter("txtDepDate")));
			}
		}
		if(model.getEntryType() == 5 || model.getEntryType() == 60){
			if(request.getParameter("txtAccessId") != null && !request.getParameter("txtAccessId").trim().equals("--"))
				model.getBranch().setAccessId(Integer.parseInt(request.getParameter("txtAccessId")));
		}
		if(model.getEntryType() == 10 || model.getEntryType() == 58 || model.getEntryType() == 59){
			if(request.getParameter("txtUserId") != null && !request.getParameter("txtUserId").trim().equals("--"))
				model.getStaff().setUserId(request.getParameter("txtUserId"));
		}
		if(model.getEntryType()  > 50){
			model.setCrdr("D");
		} else {
			model.setCrdr("C");
		}
		if(model.getEntryType()!=81 && model.getEntryType()!=1 && model.getEntryType()!=2 && model.getEntryType()!=4 && model.getEntryType()!=22 && model.getEntryType()!=23 && model.getEntryType()!=24){
			model.setCash(true);
		} else {
			model.setCash(false);
		}
		if(model.getEntryType() > 50 && model.getEntryType() != 71 && model.getEntryType() != 72 && model.getEntryType() != 73){
			model.setPayMode(Integer.parseInt(request.getParameter("rPayMode")));
			if(model.getPayMode() == 1){
				if(request.getParameter("txtChqNo") != null && !request.getParameter("txtChqNo").trim().equals(""))
					model.setChqNo(request.getParameter("txtChqNo"));
				if(request.getParameter("txtChqDate") != null && !request.getParameter("txtChqDate").trim().equals(""))
					model.setChqDt(Utils.convertToSQLDate(request.getParameter("txtChqDate")));
			}
			if(model.getPayMode() != 0){
				model.setCash(false);
			}
		}
		if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
			model.setRemark(request.getParameter("taRemark"));
		return model;
	}

	private EntryModel[] populateBillPayEntryModel(HttpServletRequest request) {
		ArrayList<EntryModel> billPayDtls = new ArrayList<EntryModel>(0); 
		EntryModel model = new EntryModel();
		int branchid = Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT));
		int type = Integer.parseInt(request.getParameter("sType"));
		String entryDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String chqDt = (request.getParameter("txtChqDate") != null && !request.getParameter("txtChqDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtChqDate")) : "";
		String depDt = (request.getParameter("txtDepDate") != null && !request.getParameter("txtDepDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtDepDate")) : "";
		int Id = (request.getParameter("txtId") != null && !request.getParameter("txtId").trim().equals("")) ? Integer.parseInt(request.getParameter("txtId")) : 0;
		String heshBank = "";
		if(type == 42 || type == 43 || type == 44){
			heshBank = (request.getParameter("sHeshBank") != null && !request.getParameter("sHeshBank").trim().equals("--")) ? request.getParameter("sHeshBank") : "";
		}
		int custId = Integer.parseInt(request.getParameter("sCustomer"));
		String custBank = "";
		String chqNo = "";
		if(type == 43){
			custBank = (request.getParameter("txtCustBank") != null && !request.getParameter("txtCustBank").trim().equals("")) ? request.getParameter("txtCustBank"): "";
			chqNo = (request.getParameter("txtChqNo") != null && !request.getParameter("txtChqNo").trim().equals("")) ? request.getParameter("txtChqNo") : "";
		}
		String pendingBills = request.getParameter("payDtls");
		StringTokenizer stRow = new StringTokenizer(pendingBills,"#");
		int billBranchId = 0, billNo =0;
		double amt = 0, adjAmt = 0;
		while (stRow.hasMoreElements()) {
			model = new EntryModel();
			model.setUser(((UserModel)request.getSession().getAttribute(USER)).getUserId());
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			billBranchId = Integer.parseInt(stCell.nextToken());
			billNo = Integer.parseInt(stCell.nextToken());
			amt = Double.parseDouble(stCell.nextToken());
			adjAmt = Double.parseDouble(stCell.nextToken());
			model.setBillNo(billNo);
			model.getBillBranch().setAccessId(billBranchId);
			model.setBranchId(branchid);
			model.setEntryType(type);
			if(type == 43){
				model.setChqDt(chqDt);
				model.setEntryDate(depDt);
			} else {
				model.setEntryDate(entryDt);
			}
			model.setId(Id);
			if(type == 42 || type == 43 || type == 44){
				model.setBankName(heshBank);
			}
			model.setAmount(amt);
			model.setAdjAmt(adjAmt);
			model.getCustomer().setCustomerId(custId);
			if(type == 43){
				model.setChqNo(chqNo);
				model.setCustBankName(custBank);
			}
			model.setCrdr("C");
			if(model.getEntryType()!=42 && model.getEntryType()!=43 && model.getEntryType()!=44){
				model.setCash(true);
			} else {
				model.setCash(false);
			}
			if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
				model.setRemark(request.getParameter("taRemark"));
			billPayDtls.add(model);
		}
		return (EntryModel[])billPayDtls.toArray(new EntryModel[0]);
	}

	private EntryModel[] populatePurchaseBillPayEntryModel(HttpServletRequest request) {
		ArrayList<EntryModel> billPayDtls = new ArrayList<EntryModel>(0); 
		EntryModel model = new EntryModel();
		int branchid = Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT));
		int type = Integer.parseInt(request.getParameter("sType"));
		String entryDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String chqDt = (request.getParameter("txtChqDate") != null && !request.getParameter("txtChqDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtChqDate")) : "";
		int Id = (request.getParameter("txtId") != null && !request.getParameter("txtId").trim().equals("")) ? Integer.parseInt(request.getParameter("txtId")) : 0;
		String heshBank = "";
		if(type == 72 || type == 73){
			heshBank = (request.getParameter("sHeshBank") != null && !request.getParameter("sHeshBank").trim().equals("--")) ? request.getParameter("sHeshBank") : "";
		}
		int custId = Integer.parseInt(request.getParameter("sCustomer"));
		String chqNo = "";
		if(type == 72 || type == 73){
			chqNo = (request.getParameter("txtChqNo") != null && !request.getParameter("txtChqNo").trim().equals("")) ? request.getParameter("txtChqNo") : "";
		}
		String pendingBills = request.getParameter("payDtls");
		StringTokenizer stRow = new StringTokenizer(pendingBills,"#");
		int billBranchId = 0, billNo =0;
		double amt = 0, adjAmt = 0;
		while (stRow.hasMoreElements()) {
			model = new EntryModel();
			model.setUser(((UserModel)request.getSession().getAttribute(USER)).getUserId());
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			billBranchId = Integer.parseInt(stCell.nextToken());
			billNo = Integer.parseInt(stCell.nextToken());
			amt = Double.parseDouble(stCell.nextToken());
			adjAmt = Double.parseDouble(stCell.nextToken());
			model.setBillNo(billNo);
			model.getBillBranch().setAccessId(billBranchId);
			model.setBranchId(branchid);
			model.setEntryType(type);
			if(type == 73){
				model.setEntryDate(entryDt);
				model.setChqNo(chqNo);
			} else if(type == 72){
				model.setChqDt(chqDt);
				model.setEntryDate(entryDt);
				model.setChqNo(chqNo);
			} else {
				model.setEntryDate(entryDt);
			}
			model.setId(Id);
			if(type == 72 || type == 73){
				model.setBankName(heshBank);
			}
			model.setAmount(amt);
			model.setAdjAmt(adjAmt);
			model.getCustomer().setCustomerId(custId);
			model.setCrdr("D");
			if(model.getEntryType()!=72 && model.getEntryType()!=73){
				model.setCash(true);
			} else {
				model.setCash(false);
			}
			if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
				model.setRemark(request.getParameter("taRemark"));
			billPayDtls.add(model);
		}
		return (EntryModel[])billPayDtls.toArray(new EntryModel[0]);
	}

	private EntryModel[] populateOrderBillPayEntryModel(
			HttpServletRequest request) {
		ArrayList<EntryModel> billPayDtls = new ArrayList<EntryModel>(0); 
		EntryModel model = new EntryModel();
		int branchid = Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT));
		int type = Integer.parseInt(request.getParameter("sType"));
		String entryDt = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		String chqDt = (request.getParameter("txtChqDate") != null && !request.getParameter("txtChqDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtChqDate")) : "";
		String depDt = (request.getParameter("txtDepDate") != null && !request.getParameter("txtDepDate").equals("")) ?Utils.convertToSQLDate(request.getParameter("txtDepDate")) : "";
		int Id = (request.getParameter("txtId") != null && !request.getParameter("txtId").trim().equals("")) ? Integer.parseInt(request.getParameter("txtId")) : 0;
		String heshBank = "";
		if(type == 2 || type == 1 || type == 4){
			heshBank = (request.getParameter("sHeshBank") != null && !request.getParameter("sHeshBank").trim().equals("--")) ? request.getParameter("sHeshBank") : "";
		}
		int custId = Integer.parseInt(request.getParameter("sCustomer"));
		String custBank = "";
		String chqNo = "";
		if(type == 1){
			custBank = (request.getParameter("txtCustBank") != null && !request.getParameter("txtCustBank").trim().equals("")) ? request.getParameter("txtCustBank"): "";
			chqNo = (request.getParameter("txtChqNo") != null && !request.getParameter("txtChqNo").trim().equals("")) ? request.getParameter("txtChqNo") : "";
		}
		String pendingBills = request.getParameter("payDtls");
		StringTokenizer stRow = new StringTokenizer(pendingBills,"#");
		int billBranchId = 0, billNo =0;
		double amt = 0;
		while (stRow.hasMoreElements()) {
			model = new EntryModel();
			model.setUser(((UserModel)request.getSession().getAttribute(USER)).getUserId());
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			billBranchId = Integer.parseInt(stCell.nextToken());
			billNo = Integer.parseInt(stCell.nextToken());
			amt = Double.parseDouble(stCell.nextToken());
			model.setBillNo(billNo);
			model.getBillBranch().setAccessId(billBranchId);
			model.setBranchId(branchid);
			model.setEntryType(type);
			if(type == 1){
				model.setChqDt(chqDt);
				model.setEntryDate(depDt);
			} else {
				model.setEntryDate(entryDt);
			}
			model.setId(Id);
			if(type == 2 || type == 1 || type == 4){
				model.setBankName(heshBank);
			}
			model.setAmount(amt);
			model.getCustomer().setCustomerId(custId);
			if(type == 1){
				model.setChqNo(chqNo);
				model.setCustBankName(custBank);
			}
			model.setCrdr("C");
			if(model.getEntryType()!=2 && model.getEntryType()!=1 && model.getEntryType()!=4){
				model.setCash(true);
			} else {
				model.setCash(false);
			}
			if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
				model.setRemark(request.getParameter("taRemark"));
			billPayDtls.add(model);
		}
		return (EntryModel[])billPayDtls.toArray(new EntryModel[0]);
	}

	private String getDayEntries(HttpServletRequest request) {
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		String date = Utils.convertToSQLDate(request.getParameter("txtRptDate"));
		DayBookEntryModel model = null;
		model = getBranchDayEntryBroker().getBranchDayEntries((String)request.getSession().getAttribute(ACCESS_POINT), date);
		if(model == null){
			model = new DayBookEntryModel();
			model.setEntryDate(date);
		}
		request.setAttribute(FORM_DATA, model);
		return "dayEntry.jsp";
	}

}
