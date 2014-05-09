package com.en.handler;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.BranchStockEntryItemModel;
import com.en.model.BranchStockEntryModel;
import com.en.model.MessageModel;
import com.en.model.UserModel;

public class BranchStockEntryHandler extends Basehandler {

	public static String getPageName() {
		return BRANCH_STOCK_ENTRY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "branchStockEntry.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_STOCK_DTLS)){
			getStockDetails(request);	
        } else if(strAction.equals(UPDT_STOCK_DTLS)){
        	updateStockDetails(request);
        }
		return pageType;
	}

	private void updateStockDetails(HttpServletRequest request) {
		int iCnt = Integer.parseInt(request.getParameter("count"));
		String day = request.getParameter("sDay");
		String month = request.getParameter("sMonth");
		String year = request.getParameter("sYear");
		ArrayList<BranchStockEntryItemModel> items = new ArrayList<BranchStockEntryItemModel>(0);
		BranchStockEntryItemModel itm = null;
		for(int i=0; i<iCnt; i++){
			if(Double.parseDouble(request.getParameter("qty"+i)) > 0){
				itm = new BranchStockEntryItemModel();
				itm.getItem().setItemId(Integer.parseInt(request.getParameter("id"+i)));
				itm.setQty(Double.parseDouble(request.getParameter("qty"+i)));
				items.add(itm);
			}
		}
		BranchStockEntryModel model = new BranchStockEntryModel();
		model.setBranch((AccessPointModel) request.getSession().getAttribute(ACCESS_POINT_DTLS));
		model.setDay(Integer.parseInt(day));
		model.setMonth(Integer.parseInt(month));
		model.setYear(Integer.parseInt(year));
		model.setCheckedBy((UserModel)request.getSession().getAttribute(USER));
		model.setItems(items);
		MessageModel msgs = getBranchStockEntryBroker().updateStock(model);
		request.setAttribute(MESSAGES, msgs);
		getStockDetails(request);
	}

	private void getStockDetails(HttpServletRequest request) {
		String day = request.getParameter("sDay");
		String month = request.getParameter("sMonth");
		String year = request.getParameter("sYear");
		BranchStockEntryModel model = getBranchStockEntryBroker().getStockDetails(day, month, year, (AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		request.setAttribute(FORM_DATA, model);
	}

}
