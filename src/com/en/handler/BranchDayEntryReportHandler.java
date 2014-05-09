package com.en.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.Constant;
import com.en.util.Utils;

public class BranchDayEntryReportHandler extends Basehandler {

	public static String getPageName() {
		return BRANCH_DLY_ENTRY_RPT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "chqEntriesRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_CHQ_ENTRIES_RPT)){
        	pageType = getChequeEntries(request);			
		} else if(strAction.equals(PRNT_CHQ_ENTRIES_RPT)){
			pageType = printChequeEntries(request);			
		} else if(strAction.equals(EXPT_CHQ_ENTRIES_RPT)){
			pageType = exportChequeEntries(request);			
		} else if(strAction.equals(INIT_BANK_ENTRIES_RPT)){
			initialize(request);
			pageType = "bankDepositRpt.jsp";
		} else if(strAction.equals(GET_BANK_ENTRIES_RPT)){
        	pageType = getBankEntries(request);			
		} else if(strAction.equals(PRNT_BANK_ENTRIES_RPT)){
			pageType = printBankEntries(request);			
		} else if(strAction.equals(EXPT_BANK_ENTRIES_RPT)){
			pageType = exportBankEntries(request);			
		} else if(strAction.equals(INIT_BANK_LEDGER_RPT)){
			initialize(request);
			pageType = "bankLedgerRpt.jsp";
		} else if(strAction.equals(GET_BANK_LEDGER_RPT)){
        	pageType = getBankLedger(request);			
		} else if(strAction.equals(PRNT_BANK_LEDGER_RPT)){
			pageType = printBankLedger(request);			
		} else if(strAction.equals(EXPT_BANK_LEDGER_RPT)){
			pageType = exportBankLedger(request);			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private String exportBankLedger(HttpServletRequest request) {
		printBankLedger(request);
		return "exportBankLedgerRpt.jsp";
	}

	private String printBankLedger(HttpServletRequest request) {
		String pageType = "printBankLedgerRpt.jsp";
		String heshBank = (request.getParameter("sHeshBank").equals("--")) ? "" : request.getParameter("sHeshBank");
		request.setAttribute("sHeshBank", request.getParameter("sHeshBank"));
		StringTokenizer branchST = new StringTokenizer(request.getParameter("cBranch"),"|");
		ArrayList<String> branchArr = new ArrayList<String>(0);
		while(branchST.hasMoreTokens()){
			branchArr.add(branchST.nextToken());
		}
		request.setAttribute("cBranch", (String[]) branchArr.toArray(new String[0]));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		EntryModel[] entries = getBranchDayEntryReportBroker().getBankLedger(heshBank, (String[]) branchArr.toArray(new String[0]), fromdt, todt);
		request.setAttribute(FORM_DATA, entries);
		initialize(request);
		return pageType;
	}

	private String getBankLedger(HttpServletRequest request) {
		String pageType = "bankLedgerRpt.jsp";
		String heshBank = request.getParameter("sHeshBank");
		request.setAttribute("sHeshBank", request.getParameter("sHeshBank"));
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		EntryModel[] entries = getBranchDayEntryReportBroker().getBankLedger(heshBank,branch,fromdt,todt);
		if(entries.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "bankLedgerRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, entries);
		}
		initialize(request);
		return pageType;
	}

	private String exportBankEntries(HttpServletRequest request) {
		printBankEntries(request);
		return "exportBankDepositRpt.jsp";
	}

	private String printBankEntries(HttpServletRequest request) {
		String pageType = "printBankDepositRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		StringTokenizer branchST = new StringTokenizer(request.getParameter("cBranch"),"|");
		ArrayList<String> branchArr = new ArrayList<String>(0);
		while(branchST.hasMoreTokens()){
			branchArr.add(branchST.nextToken());
		}
		request.setAttribute("cBranch", (String[]) branchArr.toArray(new String[0]));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		EntryModel[] entries = getBranchDayEntryReportBroker().getBankEntries(customer,(String[]) branchArr.toArray(new String[0]),fromdt,todt);
		request.setAttribute(FORM_DATA, entries);
		initialize(request);
		return pageType;
	}

	private String getBankEntries(HttpServletRequest request) {
		String pageType = "bankDepositRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		EntryModel[] entries = getBranchDayEntryReportBroker().getBankEntries(customer,branch,fromdt,todt);
		if(entries.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "chqEntriesRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, entries);
		}
		initialize(request);
		return pageType;
	}

	private String exportChequeEntries(HttpServletRequest request) {
		printChequeEntries(request);
		return "exportChqEntriesRpt.jsp";
	}

	private String printChequeEntries(HttpServletRequest request) {
		String pageType = "printChqEntriesRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		StringTokenizer branchST = new StringTokenizer(request.getParameter("cBranch"),"|");
		ArrayList<String> branchArr = new ArrayList<String>(0);
		while(branchST.hasMoreTokens()){
			branchArr.add(branchST.nextToken());
		}
		request.setAttribute("cBranch", (String[]) branchArr.toArray(new String[0]));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		EntryModel[] entries = getBranchDayEntryReportBroker().getChqEntries(customer,(String[]) branchArr.toArray(new String[0]),fromdt,todt);
		request.setAttribute(FORM_DATA, entries);
		initialize(request);
		return pageType;
	}

	private String getChequeEntries(HttpServletRequest request) {
		String pageType = "chqEntriesRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		EntryModel[] entries = getBranchDayEntryReportBroker().getChqEntries(customer,branch,fromdt,todt);
		if(entries.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "chqEntriesRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, entries);
		}
		initialize(request);
		return pageType;
	}

	private void initialize(HttpServletRequest request) {
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
	}

}
