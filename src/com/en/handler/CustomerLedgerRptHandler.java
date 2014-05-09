package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.CustomerModel;
import com.en.model.EntryModel;
import com.en.model.MessageModel;
import com.en.util.EmailUtil;
import com.en.util.Utils;


public class CustomerLedgerRptHandler extends Basehandler {

	public static String getPageName() {
		return CUSTOMER_LEDGER_RPT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "customerLedger.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_LEDGER_RPT)){
        	getLedgerRpt(request);
        	if(request.getParameter("POPUP") != null && request.getParameter("POPUP").equals("Y")){
            	pageType = "custLedgerPopup.jsp";
        	}
        } else if(strAction.equals(PRNT_LEDGER_RPT)){
        	printLedgerRpt(request);
        	pageType = "printExportLedger.jsp";
        } else if(strAction.equals(EMAIL_LEDGER_RPT)){
        	emailLedgerRpt(request);
        	if(request.getParameter("POPUP") != null && request.getParameter("POPUP").equals("Y")){
            	pageType = "custLedgerPopup.jsp";
        	}
        } else if(strAction.equals(EXPT_LEDGER_RPT)){
        	request.setAttribute("export", "Y");
        	printLedgerRpt(request);
        	pageType = "printExportLedger.jsp";
        } else if(strAction.equals(INIT_LEDGER_RPT)){
        	initialize(request);
        } else {
        	request.setAttribute("sCustomer", request.getParameter("sCustomer"));
        	pageType = "custLedgerPopup.jsp";
        }
		return pageType;
	}

	private void emailLedgerRpt(HttpServletRequest request) {
		String customer = request.getParameter("sCustomer");
		request.setAttribute("sCustomer", customer);
		String fromDate = request.getParameter("txtFromDate");
		request.setAttribute("txtFromDate", fromDate);
		String toDate = request.getParameter("txtToDate");
		request.setAttribute("txtToDate", toDate);
		EntryModel[] entries = getCustomerLedgerRptBroker().getLedgerRpt(customer, Utils.convertToSQLDate(fromDate), Utils.convertToSQLDate(toDate));
		request.setAttribute(FORM_DATA, entries);
		CustomerModel customerDtls = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
		MessageModel model = EmailUtil.emailLedger(entries, customerDtls, fromDate, toDate);
		request.setAttribute(MESSAGES, model);
		initialize(request);
	}

	private void printLedgerRpt(HttpServletRequest request) {
		String customer = request.getParameter("sCustomer");
		request.setAttribute("sCustomer", customer);
		String fromDate = request.getParameter("txtFromDate");
		request.setAttribute("txtFromDate", fromDate);
		String toDate = request.getParameter("txtToDate");
		request.setAttribute("txtToDate", toDate);
		EntryModel[] entries = getCustomerLedgerRptBroker().getLedgerRpt(customer, Utils.convertToSQLDate(fromDate), Utils.convertToSQLDate(toDate));
		request.setAttribute(FORM_DATA, entries);
		CustomerModel customerDtls = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
		request.setAttribute(CUSTOMERS, customerDtls);
	}

	private void getLedgerRpt(HttpServletRequest request) {
		String customer = request.getParameter("sCustomer");
		request.setAttribute("sCustomer", customer);
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		String fromDate = request.getParameter("txtFromDate");
		request.setAttribute("txtFromDate", fromDate);
		String toDate = request.getParameter("txtToDate");
		request.setAttribute("txtToDate", toDate);
		EntryModel[] entries = getCustomerLedgerRptBroker().getLedgerRpt(customer, Utils.convertToSQLDate(fromDate), Utils.convertToSQLDate(toDate));
		request.setAttribute(FORM_DATA, entries);
		initialize(request);
	}

	private void initialize(HttpServletRequest request) {
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
	}

}
