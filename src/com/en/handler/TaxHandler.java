package com.en.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.AccessPointBroker;
import com.en.model.AccessPointModel;
import com.en.model.ItemCategoryModel;
import com.en.model.MessageModel;
import com.en.model.TaxModel;

public class TaxHandler extends Basehandler {

	public static String getPageName() {
		return TAX_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "tax.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_TAX)){
			getTaxDetails(request);			
		} else if(strAction.equals(UPDT_TAX)){
			updateTaxDetails(request);
		} else if(strAction.equals(INIT_ITEM_TAX)){
			pageType = "itemTax.jsp";
			initializeItem(request);
		} else if(strAction.equals(GET_ITEM_TAX)){
			pageType = "itemTax.jsp";
			getItemTaxDetails(request);
		} else if(strAction.equals(UPDT_ITEM_TAX)){
			updateItemTaxDetails(request);
			pageType = "itemTax.jsp";			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private void updateItemTaxDetails(HttpServletRequest request) {
		int itemId = Integer.parseInt(request.getParameter("sItem"));
		AccessPointModel[] ac = getAccessPointBroker().getAccessPoint();
		HashMap<Integer, String> taxDtls = new HashMap<Integer, String>(0);
		for(int i=0; i<ac.length; i++){
			taxDtls.put(ac[i].getAccessId(), request.getParameter("sTaxSlab"+ac[i].getAccessId()));
		}
		MessageModel msg = getTaxBroker().updateItemTaxDetails(itemId, taxDtls);
		request.setAttribute(MESSAGES, msg);
		HashMap<Integer, Integer> dtls = getTaxBroker().getItemTaxDetails(itemId);
		request.setAttribute(FORM_DATA, dtls);
		initialize(request);
		initializeItem(request);
		request.setAttribute("itemId", Integer.parseInt(request.getParameter("sItem")));
		return;
	}

	private void initializeItem(HttpServletRequest request) {
		ItemCategoryModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		return;
	}

	private void getItemTaxDetails(HttpServletRequest request) {
		int itemId = Integer.parseInt(request.getParameter("sItem"));
		HashMap<Integer, Integer> dtls = getTaxBroker().getItemTaxDetails(itemId);
		request.setAttribute(FORM_DATA, dtls);
		initialize(request);
		initializeItem(request);
		request.setAttribute("itemId", Integer.parseInt(request.getParameter("sItem")));
		return;
	}

	private void updateTaxDetails(HttpServletRequest request) {
		TaxModel data = populateDataModel(request);
		MessageModel msg = getTaxBroker().changeTaxDetails(data);
		request.setAttribute(MESSAGES, msg);
		request.setAttribute(FORM_DATA, data);
		initialize(request);
		return;
	}

	private TaxModel populateDataModel(HttpServletRequest request) {
		TaxModel model = new TaxModel();
		if(request.getParameter("sAccessName") != null && !request.getParameter("sAccessName").trim().equals(""))
			model.getAccessPoint().setAccessId(Integer.parseInt(request.getParameter("sAccessName")));
		if(request.getParameter("txtVat1") != null && !request.getParameter("txtVat1").trim().equals(""))
			model.setVat1(Double.parseDouble(request.getParameter("txtVat1")));
		if(request.getParameter("txtVat2") != null && !request.getParameter("txtVat2").trim().equals(""))
			model.setVat2(Double.parseDouble(request.getParameter("txtVat2")));
		if(request.getParameter("txtVat3") != null && !request.getParameter("txtVat3").trim().equals(""))
			model.setVat3(Double.parseDouble(request.getParameter("txtVat3")));
		if(request.getParameter("txtCst1") != null && !request.getParameter("txtCst1").trim().equals(""))
			model.setCst1(Double.parseDouble(request.getParameter("txtCst1")));
		if(request.getParameter("txtCst2") != null && !request.getParameter("txtCst2").trim().equals(""))
			model.setCst2(Double.parseDouble(request.getParameter("txtCst2")));
		if(request.getParameter("txtCst3") != null && !request.getParameter("txtCst3").trim().equals(""))
			model.setCst3(Double.parseDouble(request.getParameter("txtCst3")));
		if(request.getParameter("txtCst1c") != null && !request.getParameter("txtCst1c").trim().equals(""))
			model.setCst1c(Double.parseDouble(request.getParameter("txtCst1c")));
		if(request.getParameter("txtCst2c") != null && !request.getParameter("txtCst2c").trim().equals(""))
			model.setCst2c(Double.parseDouble(request.getParameter("txtCst2c")));
		if(request.getParameter("txtCst3c") != null && !request.getParameter("txtCst3c").trim().equals(""))
			model.setCst3c(Double.parseDouble(request.getParameter("txtCst3c")));
		if(request.getParameter("txtCess") != null && !request.getParameter("txtCess").trim().equals(""))
			model.setCess(Double.parseDouble(request.getParameter("txtCess")));
		return model;
	}

	private void getTaxDetails(HttpServletRequest request) {
		int accessId = Integer.parseInt(request.getParameter("sAccessName"));
		TaxModel model = getTaxBroker().getTaxDetails(accessId);
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private void initialize(HttpServletRequest request) {
		AccessPointBroker broker = getAccessPointBroker();
		AccessPointModel[] accessPoints = broker.getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

}
