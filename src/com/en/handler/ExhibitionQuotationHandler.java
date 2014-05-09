package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OrderModel;
import com.en.model.PurchaseItemModel;
import com.en.model.PurchaseModel;
import com.en.model.QuotationModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.en.util.Constant;
import com.en.util.EmailUtil;
import com.en.util.Utils;

public class ExhibitionQuotationHandler extends Basehandler {

	public static String getPageName() {
		return EXHIBITION_QUOTATION_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "exhibitionQuotation.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_QUOTATION)){
        	pageType = addQuotation(request);			
		} else if(strAction.equals(PRNT_QUOTATION)){
			pageType = printQuotation(request);			
		} else if(strAction.equals(PRNT_DC)){
			pageType = printDC(request);			
		} else if(strAction.equals(INIT_EXPT_DATA)){
			pageType = "exportPurchase.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_QUOTATION)){
			getTodayQuotationDetails(request);
			pageType = "viewQuotation.jsp";			
		} else if(strAction.equals(GET_QUOTATION)){
			pageType = getQuotationDetails(request);			
		} else if(strAction.equals(CLOSE_QUOTATION)){
			pageType = closeQuotation(request);			
		} else if(strAction.equals(SHOW_QUOTATION)){
			getQuotationDetails(request);
			pageType = "viewQuotationDtls.jsp";
		} else if(strAction.equals(CONVERT_TO_ORDER)){
			pageType = convertToOrder(request);
		} else if(strAction.equals(INIT_EDIT_QUOTATION)){
			pageType = initEdit(request);			
		} else if(strAction.equals(EDIT_QUOTATION)){
			pageType = editQuotation(request);			
		} else if(strAction.equals(EMAIL_QUOTATION)){
			pageType = emailQuotationDetails(request);			
		} else if(strAction.equals(INIT_FOLLOWUP_1)){
			pageType = getQuotationDetailsForFollowup(request);
		} else if(strAction.equals(FOLLOWUP)){
			pageType = getAddRemark(request);
		} else if(strAction.equals(GET_FOLLOWUP_DTLS)){
			pageType = showFollowUpDtls(request);
		} else if(strAction.equals(INIT_FOLLOWUP)){
			pageType = initializeFollowup(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String emailQuotationDetails(HttpServletRequest request) {
		String pageType = "showQuotation.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		QuotationModel model = getQuotationBroker().getQuotationDetails(billNo);
		MessageModel msg = EmailUtil.sendQuotationMail(model);
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String closeQuotation(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		getQuotationBroker().closeQuotation(billNo);
		return initializeFollowup(request);
	}

	private String convertToOrder(HttpServletRequest request) {
		String billNo = request.getParameter("txtInvoiceNo");
		QuotationModel model = getQuotationBroker().getQuotationDetails(billNo);
		OrderModel order = new OrderModel();
		order.setCustomer(model.getCustomer());
		order.setItems(model.getItems());
		order.setInstallation(model.getInstallation());
		order.setPacking(model.getPacking());
		order.setForwarding(model.getForwarding());
		order.setLess(model.getLess());
		order.setTaxtype(model.getTaxtype());
		order.setSalesman(model.getSalesman());
		order.setRemarks("Order For QuotationNo :"+model.getBranch().getBillPrefix()+"Q"+Utils.padBillNo(model.getQuotationId())+"\n"+model.getRemarks());
		request.setAttribute(FORM_DATA, order);
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		request.setAttribute("QUOTATIONNO", billNo);
		return "order.jsp";
	}

	private String showFollowUpDtls(HttpServletRequest request) {
		String billNo = request.getParameter("billNo");
		request.setAttribute("billNo", billNo);
		QuotationModel[] model = getQuotationBroker().getFollowupDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return "viewQuotationFollowupDtls.jsp";
	}

	private String getQuotationDetailsForFollowup(HttpServletRequest request) {
		String pageType = "quotationFollowup1.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		QuotationModel model = getQuotationBroker().getQuotationDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getAddRemark(HttpServletRequest request) {
		String billNos = request.getParameter("chkBill");
		String followupDt = request.getParameter("txtFollowDate");
		String followupRemark = request.getParameter("txtRemarks");
		getQuotationBroker().addFollowupRemarks(billNos, followupDt, followupRemark, ((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId());
		return initializeFollowup(request);
	}

	private String initializeFollowup(HttpServletRequest request) {
		QuotationModel[] model = getQuotationBroker().getFollowupQuotationDetails((String)request.getSession().getAttribute(Constant.ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		return "quotationFollowup.jsp";
	}

	private void getTodayQuotationDetails(HttpServletRequest request) {
		QuotationModel[] model = getQuotationBroker().getExhibitionQuotationDetails();	
		request.setAttribute(FORM_DATA, model);
		return;
	}

	private String printDC(HttpServletRequest request) {
		String pageType = "printDC.jsp";
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String editQuotation(HttpServletRequest request) {
		String pageType = "showQuotation.jsp";
		QuotationModel model = populateDataModel(request);		
		MessageModel msg = getQuotationBroker().editQuotation(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getQuotationBroker().getQuotationDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editQuotation.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initEdit(HttpServletRequest request) {
		String pageType = "editQuotation.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		QuotationModel model = getQuotationBroker().getQuotationDetails(billNo);
		if(model.getQuotationId() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewQuotation.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
			initialize(request);
		}
		return pageType;
	}

	private String getQuotationDetails(HttpServletRequest request) {
		String pageType = "showQuotation.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		QuotationModel model = getQuotationBroker().getQuotationDetails(billNo);
		if(model.getQuotationId()== 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			getTodayQuotationDetails(request);
			pageType = "viewQuotation.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String exportItemData(HttpServletRequest request) {
		String pageType = "exportPurchaseItem.jsp";
		PurchaseItemModel[] model = getPurchaseBroker().getExportPurchaseItems(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportPurchaseMaster.jsp";
		PurchaseModel[] model = getPurchaseBroker().getExportPurchaseMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String printQuotation(HttpServletRequest request) {
		String pageType = "printQuotation.jsp";
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
		QuotationModel model = getQuotationBroker().getQuotationDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addQuotation(HttpServletRequest request) {
		String pageType = "showQuotation.jsp";
		QuotationModel model = populateDataModel(request);		
		MessageModel msg = getQuotationBroker().addQuotation(model);
		if(msg.getMessages().size()>1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getQuotationBroker().getQuotationDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "exhibitionQuotation.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private QuotationModel populateDataModel(HttpServletRequest request) {
		QuotationModel model = new QuotationModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("sUser") != null && !request.getParameter("sUser").trim().equals(""))
			model.getSalesman().setUserId(request.getParameter("sUser"));
		if(request.getParameter("rTaxType") != null && !request.getParameter("rTaxType").trim().equals(""))
			model.setTaxtype(Integer.parseInt(request.getParameter("rTaxType")));
		if(request.getParameter("rSample") != null && !request.getParameter("rSample").trim().equals(""))
			model.setSample(Integer.parseInt(request.getParameter("rSample")));
		if(request.getParameter("txtPacking") != null && !request.getParameter("txtPacking").trim().equals(""))
			model.setPacking(Double.parseDouble(request.getParameter("txtPacking")));
		if(request.getParameter("txtForwarding") != null && !request.getParameter("txtForwarding").trim().equals(""))
			model.setForwarding(Double.parseDouble(request.getParameter("txtForwarding")));
		if(request.getParameter("txtInstall") != null && !request.getParameter("txtInstall").trim().equals(""))
			model.setInstallation(Double.parseDouble(request.getParameter("txtInstall")));
		if(request.getParameter("txtLess") != null && !request.getParameter("txtLess").trim().equals(""))
			model.setLess(Double.parseDouble(request.getParameter("txtLess")));
		if(request.getParameter("txtValidDt") != null && !request.getParameter("txtValidDt").trim().equals(""))
			model.setValidDate(Utils.convertToSQLDate(request.getParameter("txtValidDt")));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemarks(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setQuotationId(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(4)));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.getBranch().setBillPrefix(request.getParameter("txtInvoiceNo").substring(0,3));
//		if(model.getOfferType().equals("1") && request.getParameter("txtOfferItems") != null && !request.getParameter("txtOfferItems").trim().equals("")){
			String salesItems = request.getParameter("txtSalesItems");
			StringTokenizer stRow = new StringTokenizer(salesItems,"#");
			while (stRow.hasMoreElements()) {
				String tempRow = stRow.nextToken();
				StringTokenizer stCell = new StringTokenizer(tempRow,"|");
				ItemModel it = new ItemModel();
				it.setItemId(Integer.parseInt(stCell.nextToken()));
				it.setItemNumber(stCell.nextToken());
				model.addItem(it, stCell.nextToken(), Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()));
			}
//		}
		model.setFromEx(1);
		model.getBranch().setAccessId(Integer.parseInt(request.getParameter("sAccessPoint")));
		return model;
	}

	private void initialize(HttpServletRequest request) {
//		CustomerModel[] customers = getCustomerBroker().getCustomer();
//		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

}
