package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.ItemGroupBroker;
import com.en.model.ItemGroupModel;
import com.en.model.MessageModel;

public class ItemGroupHandler extends Basehandler {

	public static String getPageName() {
		return ITM_GRP_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addItemGroup.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_ITM_GROUP)){
			addItemGroup(request,response);			
		} else if(strAction.equals(MOD_ITM_GROUP)){
			initializeModify(request);
			pageType = "modifyItemGroup.jsp";			
		} else if(strAction.equals(UPDT_ITM_GROUP)){
			updateItemGroup(request);
			pageType = "modifyItemGroup.jsp";			
		} else if(strAction.equals(GET_ITM_GROUP)){
			getItemGroup(request);
			pageType = "modifyItemGroup.jsp";			
		}
		return pageType;
	}

	private void updateItemGroup(HttpServletRequest request) {
		ItemGroupBroker broker = getItemGroupBroker();
		ItemGroupModel model = populateDataModel(request);
		request.setAttribute(FORM_DATA, model);
		MessageModel msgModel = getItemGroupBroker().updateItemGroup(model);
		request.setAttribute(MESSAGES, msgModel);
		ItemGroupModel[] itemGrps = broker.getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void getItemGroup(HttpServletRequest request) {
		ItemGroupBroker broker = getItemGroupBroker();
		ItemGroupModel model = broker.getItemGroupDtls(Integer.parseInt(request.getParameter("sItemGroup")));
		request.setAttribute(FORM_DATA, model);
		ItemGroupModel[] itemGrps = broker.getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void initializeModify(HttpServletRequest request) {
		ItemGroupModel[] itemGrps = getItemGroupBroker().getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void addItemGroup(HttpServletRequest request,
			HttpServletResponse response) {
		ItemGroupModel model = populateDataModel(request);
		MessageModel msgModel = getItemGroupBroker().addItemGroup(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		return;
	}

	private ItemGroupModel populateDataModel(HttpServletRequest request) {
		ItemGroupModel model = new ItemGroupModel();
		if(request.getParameter("txtItemGrp") != null && !request.getParameter("txtItemGrp").trim().equals(""))
			model.setItemGroup(request.getParameter("txtItemGrp").trim());
		if(request.getParameter("sItemGroup") != null && !request.getParameter("sItemGroup").trim().equals(""))
			model.setItemGroupId(Integer.parseInt(request.getParameter("sItemGroup")));
		return model;
	}

}
