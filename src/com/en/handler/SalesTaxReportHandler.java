package com.en.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.Constant;
import com.en.util.Utils;

public class SalesTaxReportHandler extends Basehandler {

	public static String getPageName() {
		return SALES_TAX_REPORT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "salesTaxRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_MASTER_RPT)){
        	pageType = getSalesTaxReport(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String getSalesTaxReport(HttpServletRequest request) {
		String pageType = "salesTaxRpt.jsp";
		String[] branch = (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) ? new String[] {(String)request.getSession().getAttribute(Constant.ACCESS_POINT)} : request.getParameterValues("cBranch");
		request.setAttribute("cBranch", branch);
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		HashMap<String, HashMap<String, HashMap<String, Object>>> taxDetails = getSalesTaxReportBroker().getTaxRpt(branch,fromdt,todt);
		if(taxDetails == null || taxDetails.keySet().size() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
		} else {
			request.setAttribute(FORM_DATA, taxDetails);
		}
		initialize(request);
		return pageType;
	}

	public void initialize(HttpServletRequest request) {
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		return;
	}

}
