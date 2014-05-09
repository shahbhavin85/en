package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.ItemCategoryBroker;
import com.en.model.ItemCategoryModel;
import com.en.model.ItemGroupModel;
import com.en.model.MessageModel;

public class ItemCategoryHandler extends Basehandler {

	public static String getPageName() {
		return ITM_CAT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addItemCategory.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_ITM_CATEGORY)){
			addItemCategory(request,response);			
		} else if(strAction.equals(MOD_ITM_CATEGORY)){
			initializeModify(request);
			pageType = "modifyItemCategory.jsp";			
		} else if(strAction.equals(UPDT_ITM_CATEGORY)){
			updateItemCategory(request);
			pageType = "modifyItemCategory.jsp";			
		} else if(strAction.equals(GET_ITM_CATEGORY)){
			getItemCategory(request);
			pageType = "modifyItemCategory.jsp";			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void updateItemCategory(HttpServletRequest request) {
		ItemCategoryBroker broker = getItemCategoryBroker();
		ItemCategoryModel model = populateDataModel(request);
		request.setAttribute(FORM_DATA, model);
		MessageModel msgModel = broker.updateItemCategory(model);
		request.setAttribute(MESSAGES, msgModel);
		ItemCategoryModel[] itemCats = broker.getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		ItemGroupModel[] itemGrps = getItemGroupBroker().getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void getItemCategory(HttpServletRequest request) {
		ItemCategoryBroker broker = getItemCategoryBroker();
		ItemGroupModel model = broker.getItemCategoryDtls(Integer.parseInt(request.getParameter("sItemCategory")));
		request.setAttribute(FORM_DATA, model);
		ItemCategoryModel[] itemCats = broker.getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		ItemGroupModel[] itemGrps = getItemGroupBroker().getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void initialize(HttpServletRequest request) {
		ItemGroupModel[] itemGrps = getItemGroupBroker().getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void initializeModify(HttpServletRequest request) {
		ItemCategoryModel[] itemCats = getItemCategoryBroker().getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		ItemGroupModel[] itemGrps = getItemGroupBroker().getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private void addItemCategory(HttpServletRequest request,
			HttpServletResponse response) {
		ItemCategoryModel model = populateDataModel(request);
		MessageModel msgModel = getItemCategoryBroker().addItemCateogry(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		ItemGroupModel[] itemGrps = getItemGroupBroker().getItemGroups();
		request.setAttribute(ITEM_GROUPS, itemGrps);
		return;
	}

	private ItemCategoryModel populateDataModel(HttpServletRequest request) {
		ItemCategoryModel model = new ItemCategoryModel();
		if(request.getParameter("txtItemCategory") != null && !request.getParameter("txtItemCategory").trim().equals(""))
			model.setItemCategory(request.getParameter("txtItemCategory").trim());
		if(request.getParameter("sItemGroup") != null && !request.getParameter("sItemGroup").trim().equals(""))
			model.setItemGroupId(Integer.parseInt(request.getParameter("sItemGroup")));
		if(request.getParameter("sItemCategory") != null && !request.getParameter("sItemCategory").trim().equals(""))
			model.setItemCatId(Integer.parseInt(request.getParameter("sItemCategory")));
		return model;
	}

}
