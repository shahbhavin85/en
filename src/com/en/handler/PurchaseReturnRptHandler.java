package com.en.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.PurchaseReturnModel;
import com.en.util.Utils;

public class PurchaseReturnRptHandler extends Basehandler {

	public static String getPageName() {
		return PURCHASE_RETURN_REPORT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initPurchaseReturnMasterRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_MASTER_RPT)){
        	pageType = getMasterReport(request);			
		} else if(strAction.equals(PRNT_MASTER_RPT)){
			pageType = printMasterReport(request);			
		} else if(strAction.equals(EXPT_MASTER_RPT)){
			pageType = exportMasterReport(request);			
		} else if(strAction.equals(INIT_ITEM_RPT)){
			pageType = "initPurchaseItemRpt.jsp";			
		} else if(strAction.equals(GET_ITEM_RPT)){
			pageType = getItemReport(request);			
		} else if(strAction.equals(EXPT_ITEM_RPT)){
			pageType = exportItemReport(request);			
		} else if(strAction.equals(PRNT_ITEM_RPT)){
			pageType = printItemReport(request);			
		} else if(strAction.equals(GET_PURCHASE_RETURN)){
			pageType = getPurchaseDtls(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String getPurchaseDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		PurchaseReturnModel model = getPurchaseReturnBroker().getPurchaseReturnDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return "viewPurchaseReturnDtls.jsp";
	}

	public String getMasterReport(HttpServletRequest request) {
		String pageType = "PurchaseReturnMasterRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		String[] branch = request.getParameterValues("cBranch");
		request.setAttribute("cBranch", request.getParameterValues("cBranch"));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		String[] billtype = request.getParameterValues("sBillType"); 
		request.setAttribute("sBillType", request.getParameterValues("sBillType"));
		PurchaseReturnModel[] reportBills = getPurchaseReturnRptBroker().getMasterRpt(customer,branch,fromdt,todt,billtype);
		if(reportBills.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "initPurchaseReturnMasterRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, reportBills);
		}
		initialize(request);
		return pageType;
	}

	public String getItemReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String printMasterReport(HttpServletRequest request) {
		String pageType = "printPurchaseReturnMasterRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
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
		PurchaseReturnModel[] reportBills = getPurchaseReturnRptBroker().getMasterRpt(customer,(String[]) branchArr.toArray(new String[0]),fromdt,todt,
								(String[])billtypeArr.toArray(new String[0]));
		request.setAttribute(FORM_DATA, reportBills);
		initialize(request);
		return pageType;
	}

	public String exportMasterReport(HttpServletRequest request) {
		String pageType = "exportPurchaseReturnMasterRpt.jsp";
		String customer = (request.getParameter("sCustomer").equals("--")) ? "" : request.getParameter("sCustomer");
		request.setAttribute("sCustomer", request.getParameter("sCustomer"));
		String[] branch = request.getParameterValues("cBranch");
		request.setAttribute("cBranch", request.getParameterValues("cBranch"));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		String[] billtype = request.getParameterValues("sBillType"); 
		request.setAttribute("sBillType", request.getParameterValues("sBillType"));
		PurchaseReturnModel[] reportBills = getPurchaseReturnRptBroker().getMasterRpt(customer,branch,fromdt,todt,billtype);
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
		CustomerModel[] customers = getCustomerBroker().getSupplier();
		request.setAttribute(CUSTOMERS, customers);
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		return;
	}

}
