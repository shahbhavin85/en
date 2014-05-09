package com.en.handler;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AdminItemModel;
import com.en.model.MessageModel;

public class SyncDataHandler extends Basehandler {

	public static String getPageName() {
		return SYNC_DATA_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "syncScreen.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(SYNC_DATA_TO_DB)){
        	syncDataToDB(request);
		} else if(strAction.equals(SYNC_DATA_FROM_DB)){
			syncDataFromDB(request);
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void initialize(HttpServletRequest request) {
		AdminItemModel[] items = getSyncDataBroker().getItemDetails();
		request.setAttribute(FORM_DATA, items);
	}

	private void syncDataFromDB(HttpServletRequest request) {
		MessageModel msg = getSyncDataBroker().syncDataFromDB();
		request.setAttribute(MESSAGES, msg);
		initialize(request);
	}

	private void syncDataToDB(HttpServletRequest request) {
		String[] itemIds = request.getParameterValues("chk");
//		System.out.println(itemIds.length);
		HashMap<String, String[]> itmPrice = new HashMap<String, String[]>(0);
		String[] temp = null;
		for(int i=0; i<itemIds.length; i++){
			temp = new String[2];
			temp[0] = request.getParameter("nSP"+itemIds[i]);
			temp[1] = request.getParameter("nWP"+itemIds[i]);
			itmPrice.put(itemIds[i], temp);
		}
		MessageModel msg = getSyncDataBroker().syncDataToDB(itmPrice);
		request.setAttribute(MESSAGES, msg);
		syncDataFromDB(request);
//		initialize(request);
	}
}
