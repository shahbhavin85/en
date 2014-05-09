package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.AlertModel;

public class EnquiryRptHandler extends Basehandler {

	public static String getPageName() {
		return ENQUIRY_RPT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "enqAlertRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_ENQ_ALERT_RPT)){
        	getEnquiryAlertRpt(request,response);	
			pageType = "enqAlertRpt.jsp";	
		} else if(strAction.equals(PRINT_ENQ_ALERT_RPT)){
        	getEnquiryAlertRpt(request,response);	
			pageType = "printAlertRpt.jsp";	
		} else {
			AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
			request.setAttribute(ACCESS_POINTS, accessPoints);
			pageType = "enqAlertRpt.jsp";	
		}
		return pageType;
	}

	private void getEnquiryAlertRpt(HttpServletRequest request,
			HttpServletResponse response) {
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		String accesspoint = request.getParameter("sAccessName");
		if(!request.getSession().getAttribute(ACCESS_POINT).equals("0")){
			accesspoint = ((String) request.getSession().getAttribute(ACCESS_POINT));
		} 
		String fromDate = request.getParameter("txtFromDate");
		String toDate = request.getParameter("txtToDate");
		AlertModel[] rptDtls = getEnquiryRptBroker().getAlertReport(accesspoint, fromDate, toDate);
		request.setAttribute("reportList", rptDtls);
		if(!accesspoint.equals("--")){
			request.setAttribute(ACCESS_POINT, accesspoint);
		}
		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);
	}

}
