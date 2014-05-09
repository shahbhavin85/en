package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AdminItemModel;
import com.en.model.InwardEntryItemModel;
import com.en.model.InwardEntryModel;
import com.en.model.MessageModel;

public class InwardEntryHandler extends Basehandler {

	public static String getPageName() {
		return INWARD_ENTRY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "inwardEntry.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_INWARD_ENTRY)){
        	pageType = addInwardEntry(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_QUOTATION)){
			pageType = "viewInwardEntry.jsp";			
		} else if(strAction.equals(GET_INWARD_ENTRY)){
			pageType = getEntryDetails(request);			
		} else if(strAction.equals(INIT_EDIT_INWARD_ENTRY)){
			pageType = initEdit(request);			
		} else if(strAction.equals(EDIT_INWARD_ENTRY)){
			pageType = editEntry(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String getEntryDetails(HttpServletRequest request) {
		String pageType = "showInwardEntry.jsp";
		String entryNo = request.getParameter("txtInvoiceNo");
		InwardEntryModel model = getInwardEntryBroker().getEntryDetails(entryNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String editEntry(HttpServletRequest request) {
		String pageType = "showInwardEntry.jsp";
		InwardEntryModel model = populateDataModel(request);		
		MessageModel msg = getInwardEntryBroker().editEntry(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getInwardEntryBroker().getEntryDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editInwardEntry.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initEdit(HttpServletRequest request) {
		String pageType = "editInwardEntry.jsp";
		String entryNo = request.getParameter("txtInvoiceNo");
		InwardEntryModel model = getInwardEntryBroker().getEntryDetails(entryNo);
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return pageType;
	}

	private String addInwardEntry(HttpServletRequest request) {
		String pageType = "showInwardEntry.jsp";
		InwardEntryModel model = populateDataModel(request);		
		MessageModel msg = getInwardEntryBroker().addEntry(model);
		if(msg.getMessages().size() > 1 && (msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getInwardEntryBroker().getEntryDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "inwardEntry.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private InwardEntryModel populateDataModel(HttpServletRequest request) {
		InwardEntryModel model = new InwardEntryModel();
		InwardEntryItemModel item = null;
		if(request.getParameter("txtBENo") != null && !request.getParameter("txtBENo").trim().equals(""))
			model.setBENo(request.getParameter("txtBENo"));
		if(request.getParameter("txtEntryDate") != null && !request.getParameter("txtEntryDate").trim().equals(""))
			model.setEntryDate(request.getParameter("txtEntryDate"));
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemarks(request.getParameter("txtRemarks"));
		if(request.getParameter("txtCBM") != null && !request.getParameter("txtCBM").trim().equals(""))
			model.setCbm(Double.parseDouble(request.getParameter("txtCBM")));
		if(request.getParameter("txtExchangeRate") != null && !request.getParameter("txtExchangeRate").trim().equals(""))
			model.setExchangeRate(Double.parseDouble(request.getParameter("txtExchangeRate")));
		if(request.getParameter("txtSourceExp") != null && !request.getParameter("txtSourceExp").trim().equals(""))
			model.setSourceExp(Double.parseDouble(request.getParameter("txtSourceExp")));
		if(request.getParameter("txtDestinationExp") != null && !request.getParameter("txtDestinationExp").trim().equals(""))
			model.setDestinationExp(Double.parseDouble(request.getParameter("txtDestinationExp")));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setEntryNo(Integer.parseInt(request.getParameter("txtInvoiceNo")));
		String salesItems = request.getParameter("txtSalesItems");
		StringTokenizer stRow = new StringTokenizer(salesItems,"#");
		while (stRow.hasMoreElements()) {
			item = new InwardEntryItemModel();
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			AdminItemModel it = new AdminItemModel();
			it.setItemId(Integer.parseInt(stCell.nextToken()));
			it.setItemNumber(stCell.nextToken());
			item.setItem(it);
			item.setQty(Double.parseDouble(stCell.nextToken()));
			item.setRate(Double.parseDouble(stCell.nextToken()));
			item.setDuty(Double.parseDouble(stCell.nextToken()));
			item.setCbm(Double.parseDouble(stCell.nextToken()));
			model.addItem(item);
		}
		return model;
	}

	private void initialize(HttpServletRequest request) {
		AdminItemModel[] items = getSyncDataBroker().getItemDetails();
		request.setAttribute(ITEMS, items);	
	}

}
