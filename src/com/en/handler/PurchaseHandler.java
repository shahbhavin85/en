package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.CustomerModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.PurchaseItemModel;
import com.en.model.PurchaseModel;
import com.en.model.SalesModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class PurchaseHandler extends Basehandler {

	public static String getPageName() {
		return PURCHASE_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "purchase.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_PURCHASE)){
        	pageType = addPurchase(request);			
		} else if(strAction.equals(PRNT_SALES)){
			pageType = printSales(request);			
		} else if(strAction.equals(PRNT_DC)){
			pageType = printDC(request);			
		} else if(strAction.equals(INIT_EXPT_DATA)){
			pageType = "exportPurchase.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_PURCHASE)){
			getTodayPurchaseDetails(request);
			pageType = "viewPurchase.jsp";			
		} else if(strAction.equals(GET_PURCHASE)){
			pageType = getPurchaseDetails(request);			
		} else if(strAction.equals(INIT_EDIT_PURCHASE)){
			pageType = initEdit(request);			
		} else if(strAction.equals(EDIT_PURCHASE)){
			pageType = editPurchase(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private void getTodayPurchaseDetails(HttpServletRequest request) {
		PurchaseModel[] model = getPurchaseBroker().getTodayPurchaseDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		return;
	}

	private String printDC(HttpServletRequest request) {
		String pageType = "printDC.jsp";
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String editPurchase(HttpServletRequest request) {
		String pageType = "showPurchase.jsp";
		PurchaseModel model = populateDataModel(request);		
		MessageModel msg = getPurchaseBroker().editPurchase(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getPurchaseBroker().getPurchaseDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editPurchase.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initEdit(HttpServletRequest request) {
		String pageType = "editPurchase.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		PurchaseModel model = getPurchaseBroker().getPurchaseDetails(billNo);
		if(model.getPurchaseId() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewPurchase.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
			initialize(request);
		}
		return pageType;
	}

	private String getPurchaseDetails(HttpServletRequest request) {
		String pageType = "showPurchase.jsp";
		String billNo = request.getParameter("txtPurchaseId");
		PurchaseModel model = getPurchaseBroker().getPurchaseDetails(billNo);
		if(model.getPurchaseId()== 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewPurchase.jsp";
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

	private String printSales(HttpServletRequest request) {
		String pageType = "printSalesBill.jsp";
		int copy = Integer.parseInt(request.getParameter("copies"));
		String[] copies = null;
		if(copy == 0){
			copies = (String[]) new String[] {"ORIGINAL","TRANSPORT COPY","OFFICE COPY"};
		} else if(copy == 1){
			copies = (String[]) new String[] {"ORIGINAL"};
		} else if(copy == 2){
			copies = (String[]) new String[]  {"TRANSPORT COPY"};
		} else {
			copies = (String[]) new String[] {"OFFICE COPY"};
		}
		request.setAttribute("copy", copies);	
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addPurchase(HttpServletRequest request) {
		String pageType = "showPurchase.jsp";
		PurchaseModel model = populateDataModel(request);		
		MessageModel msg = getPurchaseBroker().addPurchase(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getPurchaseBroker().getPurchaseDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "purchase.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private PurchaseModel populateDataModel(HttpServletRequest request) {
		PurchaseModel model = new PurchaseModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getSupplier().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("rTaxType") != null && !request.getParameter("rTaxType").trim().equals(""))
			model.setBillType(Integer.parseInt(request.getParameter("rTaxType")));
		if(request.getParameter("txtInvNo") != null && !request.getParameter("txtInvNo").trim().equals(""))
			model.setInvNo(request.getParameter("txtInvNo"));
		if(request.getParameter("txtInvDt") != null && !request.getParameter("txtInvDt").trim().equals(""))
			model.setInvDt(request.getParameter("txtInvDt"));
		if(request.getParameter("txtRecdDt") != null && !request.getParameter("txtRecdDt").trim().equals(""))
			model.setRecdDt(request.getParameter("txtRecdDt"));
		if(request.getParameter("txtDiscount") != null && !request.getParameter("txtDiscount").trim().equals(""))
			model.setDiscount(Double.parseDouble(request.getParameter("txtDiscount")));
		if(request.getParameter("txtExtra") != null && !request.getParameter("txtExtra").trim().equals(""))
			model.setExtra(Double.parseDouble(request.getParameter("txtExtra")));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemark(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setPurchaseId(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(4)));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.getBranch().setBillPrefix(request.getParameter("txtInvoiceNo").substring(0,3));
//		if(model.getOfferType().equals("1") && request.getParameter("txtOfferItems") != null && !request.getParameter("txtOfferItems").trim().equals("")){
			String salesItems = request.getParameter("txtPurchaseItems");
			StringTokenizer stRow = new StringTokenizer(salesItems,"#");
			while (stRow.hasMoreElements()) {
				String tempRow = stRow.nextToken();
				StringTokenizer stCell = new StringTokenizer(tempRow,"|");
				ItemModel it = new ItemModel();
				it.setItemId(Integer.parseInt(stCell.nextToken()));
				it.setItemNumber(stCell.nextToken());
				model.addItem(it, Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()));
			}
//		}
		model.getBranch().setAccessId(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		return model;
	}

	private void initialize(HttpServletRequest request) {
		CustomerModel[] customers = getCustomerBroker().getSupplier();
		request.setAttribute(CUSTOMERS, customers);
		ItemModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		return;
	}

}
