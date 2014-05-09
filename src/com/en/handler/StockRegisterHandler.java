package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.StockItemModel;
import com.en.model.StockModel;

public class StockRegisterHandler extends Basehandler {

	public static String getPageName() {
		return STOCK_REPORT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "stockReport.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
		if(strAction.equals(GET_STOCK_REGISTER_RPT)){
			getStockReport(request);
			initialize(request);
		} else if(strAction.equals(GET_STOCK_ITEM_RPT)){
			getStockItemReport(request);
			pageType = "stockItemReport.jsp";
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void getStockItemReport(HttpServletRequest request) {
		String branch = request.getParameter("branch");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String itemid = request.getParameter("itemId");
		StockItemModel[] items = getStockRegisterBroker().getItemRpt(itemid,branch,fromDate,toDate);
		ItemModel item = getItemBroker().getItemDtls(Integer.parseInt(itemid));
		request.setAttribute(FORM_DATA, items);
		request.setAttribute(ACCESS_POINT_DTLS, getAccessPointBroker().getAccessPointDtls(Integer.parseInt(branch)));
		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);
		request.setAttribute(ITEMS, item);
	}

	private void getStockReport(HttpServletRequest request) {
		StockModel model = new StockModel();
		if(!request.getParameter("sItem").equals("--")){
			model.getItem().setItemId(Integer.parseInt(request.getParameter("sItem")));
		}
		model.getBranch().setAccessId(Integer.parseInt(request.getParameter("sAccessName")));
		model.setFromDate(request.getParameter("txtFromDate"));
		model.setToDate(request.getParameter("txtToDate"));
		if(request.getParameter("sItem").equals("--")){
			model = getStockRegisterBroker().getStockReportALL(model);
		} else {
			model = getStockRegisterBroker().getStockReportWithItemId(model);
		}
		request.setAttribute(FORM_DATA, model);
	}

	private void initialize(HttpServletRequest request) {
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		ItemModel[] items = getItemBroker().getALLItems();
		request.setAttribute(ITEMS, items);
		return;
	}

}
