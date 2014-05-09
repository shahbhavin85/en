package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.LabourBillModel;
import com.en.model.MessageModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class LabourInvoiceHandler extends Basehandler {

	public static String getPageName() {
		return LABOUR_BILL_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "labourBill.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(SALES_LABOUR_BILL)){
        	salesLabourBill(request);			
		} else if(strAction.equals(ADD_LABOUR_BILL)){
			pageType = addLabourBill(request);			
		} else if(strAction.equals(PRNT_LABOUR_BILL)){
			pageType = printLabourBill(request);			
		} else if(strAction.equals(GET_LABOUR_BILL)){
			pageType = getBillDetails(request);			
		} else if(strAction.equals(INIT_EDIT_LABOUR_BILL)){
			pageType = initailiseEdit(request);			
		} else if(strAction.equals(EDIT_LABOUR_BILL)){
			pageType = editBillDetails(request);			
		} else if(strAction.equals(GET_LABOUR_BILL_POPUP)){
			getBillDetails(request);
			pageType = "viewLabourBillDtls.jsp";
		} else if(strAction.equals(INIT_LABOUR_BILL)){
			getTodayBillDetails(request);
			pageType = "viewLabourBill.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
//		} else if(strAction.equals(INIT_VIEW_EDIT_SALES)){
//			getTodayBillDetails(request);
//			pageType = "viewSales.jsp";			
//		} else if(strAction.equals(GET_SALES)){
//			pageType = getSalesDetails(request);			
//		} else if(strAction.equals(EMAIL_SALES)){
//			pageType = emailSalesDetails(request);			
//		} else if(strAction.equals(INIT_GET_SALES_DATE)){
//			pageType = initializeEditSalesDate(request);			
//		} else if(strAction.equals(GET_SALES_DATE)){
//			pageType = getEditSalesDate(request);			
//		} else if(strAction.equals(EDIT_SALES_DATE)){
//			pageType = editSalesDate(request);			
//		} else if(strAction.equals(INIT_EDIT_SALES)){
//			pageType = initializeEdit(request);			
//		} else if(strAction.equals(CNFM_EDIT_SALES)){
//			pageType = confirmEdit(request);			
//		} else if(strAction.equals(ACC_EDIT_SALES)){
//			pageType = confirmAccEdit(request);			
//		} else if(strAction.equals(EDIT_SALES)){
//			pageType = editSales(request);		
//			if(request.getParameter("ACC") != null && request.getParameter("ACC").equals("Y")){
//				pageType = "showSalesApproval.jsp";
//			}
//		} else if(strAction.equals(EDIT_SALES_LR_DTLS)){
//			pageType = editSalesLRDetails(request);			
//		} else if(strAction.equals(INIT_EDIT_SALES_LR_DTLS)){
//			pageType = initEditSalesLRDetails(request);			
//		} else if(strAction.equals(GET_EDIT_SALES_LR_DTLS)){
//			pageType = getEditSalesLRDetails(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportLabourMasterData.jsp";
		LabourBillModel[] model = getLabourInvoiceBroker().getExportMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private void getTodayBillDetails(HttpServletRequest request) {
		LabourBillModel[] model = getLabourInvoiceBroker().getTodaySalesDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);	
		return;
	}

	private String editBillDetails(HttpServletRequest request) {
		String pageType = "showLabourBill.jsp";
		LabourBillModel model = populateDataModel(request);		
		MessageModel msg = getLabourInvoiceBroker().editBill(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getLabourInvoiceBroker().getBillDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editLabourBill.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initailiseEdit(HttpServletRequest request) {
		LabourBillModel	model = getLabourInvoiceBroker().getBillDetails(request.getParameter("txtInvoiceNo"));
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return "editLabourBill.jsp";
	}

	private String getBillDetails(HttpServletRequest request) {
		String pageType = "showLabourBill.jsp";
		LabourBillModel	model = getLabourInvoiceBroker().getBillDetails(request.getParameter("txtInvoiceNo"));
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String printLabourBill(HttpServletRequest request) {
		String pageType = "printLabourBill.jsp";
		int copy = Integer.parseInt(request.getParameter("copies"));
		String[] copies = null;
		if(copy == 0){
			copies = (String[]) new String[] {"ORIGINAL","OFFICE COPY"};
		} else if(copy == 1){
			copies = (String[]) new String[] {"ORIGINAL"};
		} else {
			copies = (String[]) new String[] {"OFFICE COPY"};
		}
		request.setAttribute("copy", copies);	
		LabourBillModel model = getLabourInvoiceBroker().getBillDetails(request.getParameter("billNo"));
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addLabourBill(HttpServletRequest request) {
		String pageType = "showLabourBill.jsp";
		LabourBillModel model = populateDataModel(request);		
		MessageModel msg = getLabourInvoiceBroker().addBill(model);
		if(msg.getMessages().size() > 1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getLabourInvoiceBroker().getBillDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "labourBill.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private LabourBillModel populateDataModel(HttpServletRequest request) {
		LabourBillModel model = new LabourBillModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("sUser") != null && !request.getParameter("sUser").trim().equals(""))
			model.getSalesman().setUserId(request.getParameter("sUser"));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemarks(request.getParameter("txtRemarks"));
		if(request.getParameter("txtTotal") != null && !request.getParameter("txtTotal").trim().equals(""))
			model.setTotalAmt(Double.parseDouble(request.getParameter("txtTotal")));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setSaleid(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(5)));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.getBranch().setBillPrefix(request.getParameter("txtInvoiceNo").substring(0,3));
		model.getBranch().setAccessId(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		return model;
	}

	private void salesLabourBill(HttpServletRequest request) {
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		LabourBillModel lModel = new LabourBillModel();
		lModel.setCustomer(model.getCustomer());
		request.setAttribute(FORM_DATA, lModel);	
		initialize(request);
	}

	private void initialize(HttpServletRequest request) {
		CustomerModel[] customers = getSalesBroker().getSalesCustomer();
		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		String lastBill = getLabourInvoiceBroker().getLastBillNo((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(LAST_BILL, lastBill);
		return;
	}

}