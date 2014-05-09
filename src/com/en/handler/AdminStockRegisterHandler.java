package com.en.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.StockModel;

public class AdminStockRegisterHandler extends Basehandler {

	public static String getPageName() {
		return ADMIN_STOCK_REPORT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "adminStockReport.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
		if(strAction.equals(GET_STOCK_REGISTER_RPT)){
			getStockReport(request);
			initialize(request);
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void getStockReport(HttpServletRequest request) {
		StockModel model = new StockModel();
		HashMap<String, StockModel> result = new HashMap<String, StockModel>(0);
		HashMap<String, AccessPointModel> access = new HashMap<String, AccessPointModel>(0);
		String[] accessPoints = request.getParameterValues("cBranch");
		ItemModel[] itemGrps = getItemBroker().getItem();
		request.setAttribute(ITEMS, itemGrps);
		for(int i=0; i<accessPoints.length; i++){
			model = new StockModel();
			access.put(accessPoints[i], getAccessPointBroker().getAccessPointDtls(Integer.parseInt(accessPoints[i])));
			model.setBranch(getAccessPointBroker().getAccessPointDtls(Integer.parseInt(accessPoints[i])));
			model.setFromDate(request.getParameter("txtStockDate"));
			model = getStockRegisterBroker().getStockOnDateReport(model, itemGrps);
			model.createMap();
			result.put(accessPoints[i], model);
		}
		request.setAttribute(ACCESS_POINT, access);
		request.setAttribute(FORM_DATA, result);
	}

	private void initialize(HttpServletRequest request) {
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		return;
	}

}
