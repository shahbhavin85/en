package com.en.handler;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AdminItemModel;
import com.en.model.MessageModel;

public class AdminMasterHandler extends Basehandler {

	public static String getPageName() {
		return ADMIN_MASTER_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "adminMaster.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(SAVE_DATA)){
        	saveData(request);
		} else if(strAction.equals(REMOVE_DATA)) { 
			removeData(request);
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void removeData(HttpServletRequest request) {
		getAdminMasterBroker().removeData();
	}

	private void initialize(HttpServletRequest request) {
		AdminItemModel[] items = getAdminMasterBroker().getItemDetails();
		request.setAttribute(FORM_DATA, items);
	}

	private void saveData(HttpServletRequest request) {
		String[] itemsId = request.getParameterValues("chkItem");
		ArrayList<AdminItemModel> items = new ArrayList<AdminItemModel>(0);
		if(itemsId.length>0){
			AdminItemModel temp = null;
			for(int i=0; i<itemsId.length; i++){
				temp = new AdminItemModel();
				temp.setItemId(Integer.parseInt(itemsId[i]));
				temp.setTT(Double.parseDouble(request.getParameter("tt"+itemsId[i])));
				temp.setSP1(Double.parseDouble(request.getParameter("sp1"+itemsId[i])));
				temp.setSP2(Double.parseDouble(request.getParameter("sp2"+itemsId[i])));
				temp.setSP3(Double.parseDouble(request.getParameter("sp3"+itemsId[i])));
				temp.setRefName(request.getParameter("name"+itemsId[i]));
				if(request.getParameter("chkIndian"+itemsId[i]) != null && request.getParameter("chkIndian"+itemsId[i]).equals("1")){
					temp.setIndian();
				}
				items.add(temp);
			}
		}
		MessageModel msg = getAdminMasterBroker().saveData((AdminItemModel[]) items.toArray(new AdminItemModel[0]));
		request.setAttribute(MESSAGES, msg);
		initialize(request);
	}

}
