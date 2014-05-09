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
import com.en.model.PurchaseReturnModel;
import com.en.model.SalesModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class PurchaseReturnHandler extends Basehandler {

	public static String getPageName() {
		return PURCHASE_RETURN_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "purchaseReturn.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_PURCHASE_RETURN)){
        	pageType = addPurchaseReturn(request);			
		} else if(strAction.equals(PRNT_PURCHASE_RETURN)){
			pageType = printPurchaseReturn(request);			
		} else if(strAction.equals(PRNT_DC)){
			pageType = printDC(request);			
		} else if(strAction.equals(INIT_EXPT_DATA)){
			pageType = "exportPurchase.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_PURCHASE_RETURN)){
			getTodayPurchaseReturnDetails(request);
			pageType = "viewPurchaseReturn.jsp";			
		} else if(strAction.equals(GET_PURCHASE_RETURN)){
			pageType = getPurchaseReturnDetails(request);			
		} else if(strAction.equals(INIT_EDIT_PURCHASE_RETURN)){
			pageType = initEdit(request);			
		} else if(strAction.equals(EDIT_PURCHASE_RETURN)){
			pageType = editPurchaseReturn(request);			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private void getTodayPurchaseReturnDetails(HttpServletRequest request) {
		PurchaseReturnModel[] model = getPurchaseReturnBroker().getTodayPurchaseReturnDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		return;
	}

	private String printDC(HttpServletRequest request) {
		String pageType = "printDC.jsp";
		SalesModel model = getSalesBroker().getSalesDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String editPurchaseReturn(HttpServletRequest request) {
		String pageType = "showPurchaseReturn.jsp";
		PurchaseReturnModel model = populateDataModel(request);		
		MessageModel msg = getPurchaseReturnBroker().editPurchaseReturn(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getPurchaseReturnBroker().getPurchaseReturnDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editPurchaseReturn.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initEdit(HttpServletRequest request) {
		String pageType = "editPurchaseReturn.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		PurchaseReturnModel model = getPurchaseReturnBroker().getPurchaseReturnDetails(billNo);
		if(model.getPurchaseId() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewPurchaseReturn.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
			initialize(request);
		}
		return pageType;
	}

	private String getPurchaseReturnDetails(HttpServletRequest request) {
		String pageType = "showPurchaseReturn.jsp";
		String billNo = request.getParameter("txtPurchaseId");
		PurchaseReturnModel model = getPurchaseReturnBroker().getPurchaseReturnDetails(billNo);
		if(model.getPurchaseId()== 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewPurchaseReturn.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String exportItemData(HttpServletRequest request) {
		String pageType = "exportPurchaseReturnItem.jsp";
		PurchaseItemModel[] model = getPurchaseReturnBroker().getExportPurchaseReturnItems(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportPurchaseReturnMaster.jsp";
		PurchaseReturnModel[] model = getPurchaseReturnBroker().getExportPurchaseReturnMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String printPurchaseReturn(HttpServletRequest request) {
		String pageType = "printPurchaseReturn.jsp";
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
		PurchaseReturnModel model = getPurchaseReturnBroker().getPurchaseReturnDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addPurchaseReturn(HttpServletRequest request) {
		String pageType = "showPurchaseReturn.jsp";
		PurchaseReturnModel model = populateDataModel(request);		
		MessageModel msg = getPurchaseReturnBroker().addPurchaseReturn(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getPurchaseReturnBroker().getPurchaseReturnDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "purchaseReturn.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private PurchaseReturnModel populateDataModel(HttpServletRequest request) {
		PurchaseReturnModel model = new PurchaseReturnModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getSupplier().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("rTaxType") != null && !request.getParameter("rTaxType").trim().equals(""))
			model.setBillType(Integer.parseInt(request.getParameter("rTaxType")));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemark(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setPurchaseId(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(5)));
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
