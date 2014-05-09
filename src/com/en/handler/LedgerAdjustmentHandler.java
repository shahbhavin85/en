package com.en.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.CustomerModel;
import com.en.model.EntryModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesModel;

public class LedgerAdjustmentHandler extends Basehandler {

	public static String getPageName() {
		return LEDGER_ADJUSTMENT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initLedgerAdjustment.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_ORDER_ADVANCE)){
        	pageType = getOrderAdjustment(request);			
		} else if(strAction.equals(GET_ORDER_ADVANCE_PENDING_SALES)){
			pageType = getOrderAdvancePendingBill(request);			
		} else if(strAction.equals(ADD_ORDER_ADVANCE_PENDING_SALES)){
			pageType = addOrderAdvancePendingBill(request);			
		} else if(strAction.equals(VIEW_LEDGER_ADJUSTMENT)){
			pageType = viewLedgerAdjustment(request);
		} else if(strAction.equals(DELETE_LEDGER_ADJUSTMENT)){
			pageType = deleteLedgerAdjustment(request);
		}
		return pageType;
	}

	private String deleteLedgerAdjustment(HttpServletRequest request) {
		String idx = request.getParameter("idx");
		MessageModel msg = getLedgerAdjustmentBroker().deleteAdjustment(idx);
		request.setAttribute(MESSAGES, msg);
		return viewLedgerAdjustment(request);
	}

	private String viewLedgerAdjustment(HttpServletRequest request) {
		String custId = request.getParameter("sCustomer");
		if(custId != null && !custId.equals("")){
			EntryModel[] models = getLedgerAdjustmentBroker().getAdjustmentEntries(custId);
			CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(custId));
			request.setAttribute(CUSTOMER, customer);
			if(models.length > 0){
				request.setAttribute(FORM_DATA, models);
			} else {
				MessageModel msg = new MessageModel();
				msg.addNewMessage(new Message(ERROR, "No records exists."));
				request.setAttribute(MESSAGES, msg);
			}
		}
		return "viewLedgerAdjustment.jsp";
	}

	private String addOrderAdvancePendingBill(HttpServletRequest request) {
		EntryModel[] billEntry = populateBillPayEntryModel(request);
		String orderNo = request.getParameter("txtOrderNo");
		String custId = request.getParameter("sCustomer");
		MessageModel msg = getLedgerAdjustmentBroker().adjustOrderAdvanceBillPending(orderNo, billEntry, custId);
		request.setAttribute(MESSAGES, msg);
		return "initLedgerAdjustment.jsp";
	}

	private String getOrderAdvancePendingBill(HttpServletRequest request) {
		String custId = request.getParameter("sCustomer");
		String orderNo = request.getParameter("rNo");
		request.setAttribute("orderNo", orderNo.substring(0,orderNo.indexOf('~')));
		request.setAttribute("amt", orderNo.substring(orderNo.indexOf('~')+1));
		CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(custId));
		SalesModel[] models = getCollectionBroker().getOutstandingRpt(custId,  new String[] {"--"},"--");
		request.setAttribute(CUSTOMER, customer);
		request.setAttribute("sType", request.getParameter("sType"));
		if(models.length > 0){
			request.setAttribute(FORM_DATA, models);
			return "finalLedgerAdjustment.jsp";
		} else {
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No records exists."));
			request.setAttribute(MESSAGES, msg);
			return "initLedgerAdjustment.jsp";
		}
	}

	private String getOrderAdjustment(HttpServletRequest request) {
		String custId = request.getParameter("sCustomer");
		EntryModel[] models = getLedgerAdjustmentBroker().getOrderAdvanceModel(custId);
		CustomerModel customer = getCustomerBroker().getCustomerDtls(Integer.parseInt(custId));
		request.setAttribute(CUSTOMER, customer);
		request.setAttribute("sType", request.getParameter("sType"));
		if(models.length > 0){
			request.setAttribute(FORM_DATA, models);
			return "ledgerAdjustment.jsp";
		} else {
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No records exists."));
			request.setAttribute(MESSAGES, msg);
			return "initLedgerAdjustment.jsp";
		}
	}

	private EntryModel[] populateBillPayEntryModel(HttpServletRequest request) {
		ArrayList<EntryModel> billPayDtls = new ArrayList<EntryModel>(0); 
		EntryModel model = new EntryModel();
		String pendingBills = request.getParameter("payDtls");
		StringTokenizer stRow = new StringTokenizer(pendingBills,"#");
		int billBranchId = 0, billNo =0;
		String billPrefix = "";
		double amt = 0, adjAmt = 0;
		while (stRow.hasMoreElements()) {
			model = new EntryModel();
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			billPrefix = stCell.nextToken();
			billBranchId = Integer.parseInt(stCell.nextToken());
			billNo = Integer.parseInt(stCell.nextToken());
			amt = Double.parseDouble(stCell.nextToken());
			adjAmt = Double.parseDouble(stCell.nextToken());
			model.setBillNo(billNo);
			model.getBillBranch().setBillPrefix(billPrefix);
			model.getBillBranch().setAccessId(billBranchId);
			model.setAmount(amt);
			model.setAdjAmt(adjAmt);
			billPayDtls.add(model);
		}
		return (EntryModel[])billPayDtls.toArray(new EntryModel[0]);
	}

}
