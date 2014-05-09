package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.CreditNoteModel;
import com.en.model.CustomerModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;
import com.en.model.SalesModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class CreditNoteHandler extends Basehandler {

	public static String getPageName() {
		return CREDIT_NOTE_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "creditNote.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_CREDIT_NOTE)){
        	pageType = addCreditNote(request);			
		} else if(strAction.equals(PRNT_CREDIT_NOTE)){
			pageType = printCreditNote(request);			
//		} else if(strAction.equals(PRNT_DC)){
//			pageType = printDC(request);			
//		} else if(strAction.equals(INIT_EXPT_DATA)){
//			pageType = "exportSales.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			pageType = exportItemData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_CREDIT_NOTE)){
			getTodayBillDetails(request);
			pageType = "viewCreditNote.jsp";			
		} else if(strAction.equals(GET_CREDIT_NOTE)){
			pageType = getNoteDetails(request);			
		} else if(strAction.equals(VIEW_CREDIT_NOTE)){
			pageType = viewNoteDetails(request);			
//			} else if(strAction.equals(EMAIL_SALES)){
//			pageType = emailSalesDetails(request);			
//		} else if(strAction.equals(INIT_GET_SALES_DATE)){
//			pageType = initializeEditSalesDate(request);			
//		} else if(strAction.equals(GET_SALES_DATE)){
//			pageType = getEditSalesDate(request);			
//		} else if(strAction.equals(EDIT_SALES_DATE)){
//			pageType = editSalesDate(request);			
		} else if(strAction.equals(INIT_EDIT_CREDIT_NOTE)){
			pageType = initializeEdit(request);			
//		} else if(strAction.equals(CNFM_EDIT_SALES)){
//			pageType = confirmEdit(request);			
//		} else if(strAction.equals(ACC_EDIT_SALES)){
//			pageType = confirmAccEdit(request);			
		} else if(strAction.equals(EDIT_CREDIT_NOTE)){
			pageType = editCreditNote(request);		
			if(request.getParameter("ACC") != null && request.getParameter("ACC").equals("Y")){
				pageType = "showSalesApproval.jsp";
			}
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

	private String exportItemData(HttpServletRequest request) {
		String pageType = "exportSalesReturnItemData.jsp";
		SalesItemModel[] model = getCreditNoteBroker().getExportSalesReturnItems(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportSalesReturnMasterData.jsp";
		CreditNoteModel[] model = getCreditNoteBroker().getExportSalesReturnMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String viewNoteDetails(HttpServletRequest request) {
		String pageType = "viewCreditNoteDtls.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		CreditNoteModel model = getCreditNoteBroker().getNoteDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String editCreditNote(HttpServletRequest request) {
		String pageType = "showCreditNote.jsp";
		CreditNoteModel model = populateDataModel(request);		
		MessageModel msg = getCreditNoteBroker().editCreditNote(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getCreditNoteBroker().getNoteDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editCreditNote.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getNoteDetails(HttpServletRequest request) {
		String pageType = "showCreditNote.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		CreditNoteModel model = getCreditNoteBroker().getNoteDetails(billNo);
		if(model.getSaleid() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			CreditNoteModel[] models = getCreditNoteBroker().getTodayNoteDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
			request.setAttribute(FORM_DATA, models);	
			pageType = "viewCreditNote.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String printCreditNote(HttpServletRequest request) {
		String pageType = "printCreditNote.jsp";
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
		CreditNoteModel model = getCreditNoteBroker().getNoteDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addCreditNote(HttpServletRequest request) {
		String pageType = "showCreditNote.jsp";
		CreditNoteModel model = populateDataModel(request);		
		MessageModel msg = getCreditNoteBroker().addCreditNote(model);
		if(msg.getMessages().size() > 1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getCreditNoteBroker().getNoteDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "sales.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private void getTodayBillDetails(HttpServletRequest request) {
		CreditNoteModel[] model = getCreditNoteBroker().getTodayNoteDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);	
		return;
	}

	private String initializeEdit(HttpServletRequest request) {
		String pageType = "editCreditNote.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		CreditNoteModel model = getCreditNoteBroker().getNoteDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return pageType;
	}

	private CreditNoteModel populateDataModel(HttpServletRequest request) {
		CreditNoteModel model = new CreditNoteModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("txtTin") != null && !request.getParameter("txtTin").trim().equals(""))
			model.getCustomer().setTin(request.getParameter("txtTin"));
		if(request.getParameter("txtCst") != null && !request.getParameter("txtCst").trim().equals(""))
			model.getCustomer().setCst(request.getParameter("txtCst"));
		if(request.getParameter("txtContactPerson") != null && !request.getParameter("txtContactPerson").trim().equals(""))
			model.getCustomer().setContactPerson(request.getParameter("txtContactPerson"));
		if(request.getParameter("txtMobile1") != null && !request.getParameter("txtMobile1").trim().equals(""))
			model.getCustomer().setMobile1(request.getParameter("txtMobile1"));
		if(request.getParameter("sUser") != null && !request.getParameter("sUser").trim().equals(""))
			model.getSalesman().setUserId(request.getParameter("sUser"));
		if(request.getParameter("rTaxType") != null && !request.getParameter("rTaxType").trim().equals(""))
			model.setTaxtype(Integer.parseInt(request.getParameter("rTaxType")));
		if(request.getParameter("txtLess") != null && !request.getParameter("txtLess").trim().equals(""))
			model.setLess(Double.parseDouble(request.getParameter("txtLess")));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemarks(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setSaleid(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(5)));
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
		model.getBranch().setAccessId(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		return model;
	}

	private void initialize(HttpServletRequest request) {
		CustomerModel[] customers = getSalesBroker().getSalesCustomer();
		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getSalemanUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getSalesItem();
		request.setAttribute(ITEMS, items);
		String lastBill = getCreditNoteBroker().getLastBillNo((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(LAST_BILL, lastBill);
		return;
	}

}
