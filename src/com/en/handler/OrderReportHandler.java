package com.en.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.UserModel;
import com.en.util.Constant;
import com.en.util.Utils;

public class OrderReportHandler extends Basehandler {

	public static String getPageName() {
		return ORDER_REPORT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initOrderMasterRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_MASTER_RPT)){
        	pageType = getMasterReport(request);			
		} else if(strAction.equals(PRNT_MASTER_RPT)){
			pageType = printMasterReport(request);			
		} else if(strAction.equals(EXPT_MASTER_RPT)){
			pageType = exportMasterReport(request);			
		} else if(strAction.equals(INIT_ITEM_RPT)){
			pageType = "initSalesItemRpt.jsp";			
		} else if(strAction.equals(GET_ITEM_RPT)){
			pageType = getItemReport(request);			
		} else if(strAction.equals(EXPT_ITEM_RPT)){
			pageType = exportItemReport(request);			
		} else if(strAction.equals(PRNT_ITEM_RPT)){
			pageType = printItemReport(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	public String getMasterReport(HttpServletRequest request) {
		String pageType = "orderMasterRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		String salesman = (request.getParameter("sUser").equals("--")) ? "" : request.getParameter("sUser");
		request.setAttribute("sUser", request.getParameter("sUser"));
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		String[] billtype = request.getParameterValues("sBillType"); 
		request.setAttribute("sBillType", request.getParameterValues("sBillType"));
		boolean rOrderOf = request.getParameter("rOrderOf").equals("0") ? false : true;
		request.setAttribute("rOrderOf", request.getParameter("rOrderOf"));
		OrderModel[] reportBills = getOrderReportBroker().getMasterRpt(customer,salesman,branch,fromdt,todt,billtype,rOrderOf);
		if(reportBills.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "initOrderMasterRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, reportBills);
		}
		initialize(request);
		return pageType;
	}

	public String getItemReport(HttpServletRequest request) {
		String pageType = "orderItemRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel cust = getCustomerBroker().getCustomerDtls(Integer.parseInt(customer));
			request.setAttribute(CUSTOMER, cust);
		}
		String salesman = (request.getParameter("sUser").equals("--")) ? "" : request.getParameter("sUser");
		request.setAttribute("sUser", request.getParameter("sUser"));
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		String[] billtype = request.getParameterValues("sBillType"); 
		request.setAttribute("sBillType", request.getParameterValues("sBillType"));
		boolean rOrderOf = request.getParameter("rOrderOf").equals("0") ? false : true;
		request.setAttribute("rOrderOf", request.getParameter("rOrderOf"));
		HashMap<String, Object> reportBills = getOrderReportBroker().getItemRpt(customer,salesman,branch,fromdt,todt,billtype,rOrderOf);
		if(reportBills == null){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "initOrderMasterRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, reportBills.get(FORM_DATA));
			request.setAttribute("MASTER_DTLS", reportBills.get("MASTER_DTLS"));
		}
		initialize(request);
		return pageType;
	}

	public String printMasterReport(HttpServletRequest request) {
		String pageType = "printorderMasterRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel custModel = getCustomerBroker().getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
			request.setAttribute(CUSTOMER, custModel);
		}
		String salesman = (request.getParameter("sUser").equals("--")) ? "" : request.getParameter("sUser");
		request.setAttribute("sUser", request.getParameter("sUser"));
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
		StringTokenizer billtypeST = new StringTokenizer(request.getParameter("sBillType"),"|");
		ArrayList<String> billtypeArr = new ArrayList<String>(0);
		while(billtypeST.hasMoreTokens()){
			billtypeArr.add(billtypeST.nextToken());
		}
		request.setAttribute("sBillType", (String[])billtypeArr.toArray(new String[0]));
		boolean rOrderOf = request.getParameter("rOrderOf").equals("0") ? false : true;
		request.setAttribute("rOrderOf", request.getParameter("rOrderOf"));
		OrderModel[] reportBills = getOrderReportBroker().getMasterRpt(customer,salesman,(String[]) branchArr.toArray(new String[0]),fromdt,todt,
								(String[])billtypeArr.toArray(new String[0]),rOrderOf);
		request.setAttribute(FORM_DATA, reportBills);
		initialize(request);
		return pageType;
	}

	public String exportMasterReport(HttpServletRequest request) {
		String pageType = "exportorderMasterRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		if(!customer.equals("")){
			CustomerModel custModel = getCustomerBroker().getCustomerDtls(Integer.parseInt(request.getParameter("sCustomer")));
			request.setAttribute(CUSTOMER, custModel);
		}
		String salesman = (request.getParameter("sUser").equals("--")) ? "" : request.getParameter("sUser");
		request.setAttribute("sUser", request.getParameter("sUser"));
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		String[] billtype = request.getParameterValues("sBillType"); 
		request.setAttribute("sBillType", request.getParameterValues("sBillType"));
		boolean rOrderOf = request.getParameter("rOrderOf").equals("0") ? false : true;
		request.setAttribute("rOrderOf", request.getParameter("rOrderOf"));
		OrderModel[] reportBills = getOrderReportBroker().getMasterRpt(customer,salesman,branch,fromdt,todt,billtype,rOrderOf);
		request.setAttribute(FORM_DATA, reportBills);
		initialize(request);
		return pageType;
	}

	public String printItemReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String exportItemReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize(HttpServletRequest request) {
//		CustomerModel[] customers = getSalesBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		return;
	}

}
