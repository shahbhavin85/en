package com.en.handler;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.en.model.UserModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AjaxHandler extends AjaxBaseHandler {

	public static String getPageName() {
		return AJAX_HANDLER;
	}
	
	public JSONObject handleAjaxRequest(HttpServletRequest request) {
		JSONObject returnObj = null;
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(AJAX_GET_CUST_LST)){
        	returnObj = getCustomerList(request);			
		} else if(strAction.equals(AJAX_GET_SALES_CUST_LST)){
        	returnObj = getSalesCustomerList(request);	
		} else if(strAction.equals(AJAX_CHECK_CUSTOMER)){
        	returnObj = checkCustomer(request);	
		} else if(strAction.equals(AJAX_CHECK_CATALOGUE)){
        	returnObj = checkCatalogue(request);	
		} else if(strAction.equals(AJAX_CHECK_PHOTO)){
        	returnObj = checkPhoto(request);	
		} else if(strAction.equals(AJAX_REFRESH)){
			System.out.println(new Date() + " :: Session Refreshed :: "+((UserModel)request.getSession().getAttribute(USER)).getUserId());
		} else if(strAction.equals(ADD_CITY)){
        	returnObj = addCity(request);	
		}
		return returnObj;
	}

	private JSONObject addCity(HttpServletRequest request) {
		String city = request.getParameter("city");
		String district = request.getParameter("district");
		String state = request.getParameter("state");
		String country = request.getParameter("country");
		JSONObject obj = getCustomerBroker().addCity(city, district, state, country);
		return obj;
	}

	private JSONObject checkPhoto(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		boolean getAjaxCustLst = getItemBroker().checkItemPhoto(itemId);
		JSONObject obj = new JSONObject();
		obj.put("status", getAjaxCustLst);
		obj.put("itemId", itemId);
		return obj;
	}

	private JSONObject checkCatalogue(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		boolean getAjaxCustLst = getItemBroker().checkItemCatalogue(itemId);
		JSONObject obj = new JSONObject();
		obj.put("status", getAjaxCustLst);
		obj.put("itemId", itemId);
		return obj;
	}

	private JSONObject checkCustomer(HttpServletRequest request) {
		String custId = request.getParameter("custId");
		boolean getAjaxCustLst = getCustomerBroker().checkCustomer(custId);
		JSONObject obj = new JSONObject();
		obj.put("status", getAjaxCustLst);
		return obj;
	}

	private JSONObject getSalesCustomerList(HttpServletRequest request) {
		String custLike = request.getParameter("custLike");
		JSONArray getAjaxCustLst = getSalesBroker().getAjaxCustList(custLike);
		JSONObject obj = new JSONObject();
		obj.put("list", getAjaxCustLst);
		return obj;
	}

	private JSONObject getCustomerList(HttpServletRequest request) {
		String custLike = request.getParameter("custLike");
		JSONArray getAjaxCustLst = getCustomerBroker().getAjaxCustList(custLike);
		JSONObject obj = new JSONObject();
		obj.put("list", getAjaxCustLst);
		return obj;
	}

}
