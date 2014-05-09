package com.en.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.EntryModel;
import com.en.model.MessageModel;
import com.en.model.SalesModel;
import com.en.util.EmailUtil;

public class ApprovalHandler extends Basehandler {

	public static String getPageName() {
		return APPROVAL_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "masterExcel.jsp";
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(INIT_APP_SALES_LST)){
        	pageType = getUnapprovedSalesBills(request);		
		} else if(strAction.equals(GET_SALES)){
			pageType = getSalesBillDtls(request);
		} else if(strAction.equals(APPROVE_SALES)){
			pageType = approveSalesBillDtls(request);
		}
		return pageType;
	}

	private String approveSalesBillDtls(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		MessageModel msg = getApprovalBroker().approvedSalesBills(billNo);
		request.setAttribute(MESSAGES, msg);
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		EmailUtil.sendSalesMail(model);
		(new SalesHandler()).sendSalesDtlsSMS(model);
		return getUnapprovedSalesBills(request);
	}

	private String getSalesBillDtls(HttpServletRequest request) {
		String pageType = "showSalesApproval.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		SalesModel model = getSalesBroker().getSalesDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		EntryModel[] entries = getCollectionBroker().getPaymentDetails(billNo);
		request.setAttribute(PAYMENT_DATA, entries);
		EntryModel[] adjustEntries = getCollectionBroker().getAdjustDetails(billNo);
		request.setAttribute(ADJUST_ENTRIES, adjustEntries);
		return pageType;
	}

	private String getUnapprovedSalesBills(HttpServletRequest request) {
		String pageName = "unapprovedSalesBills.jsp";
		HashMap<String, SalesModel[]> details = getApprovalBroker().getUnapprovedSalesBills();
		request.setAttribute(FORM_DATA, details);
		return pageName;
	}

}
