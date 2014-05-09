package com.en.handler;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.MessageModel;
import com.en.model.SalesItemModel;

public class BranchStockHandler extends Basehandler {

	public static String getPageName() {
		return BRANCH_STOCK_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "printStockReport.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(INIT_OPEN_STOCK)){
        	pageType = getOpeningStock(request);			
		} else if(strAction.equals(UPDATE_OPEN_STOCK)){
			pageType = updateOpeningStock(request);			
		} else {
			getCurrentStockReport(request);
		}
		return pageType;
	}

	private String updateOpeningStock(HttpServletRequest request) {
		int cnt = Integer.parseInt(request.getParameter("count"));
		ArrayList<SalesItemModel> items = new ArrayList<SalesItemModel>(0);
		SalesItemModel temp = null;
		for(int i=0; i<cnt; i++){
			temp = new SalesItemModel();
			temp.getItem().setItemId(Integer.parseInt(request.getParameter("id"+i)));
			temp.setQty(Double.parseDouble(request.getParameter("qty"+i)));
			items.add(temp);
		}
		MessageModel msg = getItemBroker().updateItemStock(items, ((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		request.setAttribute(MESSAGES, msg);
		return getOpeningStock(request);
	}

	private String getOpeningStock(HttpServletRequest request) {
		SalesItemModel[] items = getItemBroker().getOpeningStock(((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		request.setAttribute(FORM_DATA, items);
		return "openingStock.jsp";
	}

	private void getCurrentStockReport(HttpServletRequest request) {
		HashMap<String,HashMap<String,SalesItemModel[]>> items = getItemBroker().getStockReport(((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		request.setAttribute("items", items);
		return;
	}

}
