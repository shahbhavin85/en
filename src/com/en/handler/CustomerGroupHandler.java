package com.en.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.CustomerGroupBroker;
import com.en.model.CustomerGroupModel;
import com.en.model.MessageModel;

public class CustomerGroupHandler extends Basehandler {

	public static String getPageName() {
		return CUSTOMER_GRP_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addCustomerGrp.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_CUSTOMER_GRP)){
			addCustomerGrp(request, response);			
		} else if(strAction.equals(MOD_CUSTOMER_GRP)){
			initializeModify(request);
			pageType = "modifyCustomerGrp.jsp";			
		} else if(strAction.equals(UPDT_CUSTOMER_GRP)){
			updateCustomerGrp(request);
			pageType = "modifyCustomerGrp.jsp";			
		} else if(strAction.equals(GET_CUSTOMER_GRP)){
			getCustomerGrp(request);
			pageType = "modifyCustomerGrp.jsp";			
		}
		return pageType;
	}

	private void updateCustomerGrp(HttpServletRequest request) {
		CustomerGroupBroker broker = getCustomerGroupBroker();
		CustomerGroupModel model = populateDataModel(request);
		MessageModel msgModel = broker.updateCustomerGroup(model);
		request.setAttribute(MESSAGES, msgModel);
		model = broker.getCustGroupDtls(model.getCustGroupId());
		request.setAttribute(FORM_DATA, model);
		CustomerGroupModel[] customers = broker.getCustGroups();
		request.setAttribute(CUSTOMERS_GROUPS, customers);
		return;
	}

	private void getCustomerGrp(HttpServletRequest request) {
		CustomerGroupBroker broker = getCustomerGroupBroker();
		CustomerGroupModel model = broker.getCustGroupDtls(Integer.parseInt(request.getParameter("sCustomerGrp")));
		request.setAttribute(FORM_DATA, model);
		CustomerGroupModel[] customers = broker.getCustGroups();
		request.setAttribute(CUSTOMERS_GROUPS, customers);
		return;
	}

	private void initializeModify(HttpServletRequest request) {
		CustomerGroupModel[] customerGrps = getCustomerGroupBroker().getCustGroups();
		request.setAttribute(CUSTOMERS_GROUPS, customerGrps);
		return;
	}

	private void addCustomerGrp(HttpServletRequest request,
			HttpServletResponse response) {
		CustomerGroupModel model = populateDataModel(request);
		MessageModel msgModel = getCustomerGroupBroker().addCustomerGroup(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		return;
	}

	private CustomerGroupModel populateDataModel(HttpServletRequest request) {
		CustomerGroupModel model = new CustomerGroupModel();
		if(request.getParameter("txtCustomerGrp") != null && !request.getParameter("txtCustomerGrp").trim().equals(""))
			model.setCustGroup(request.getParameter("txtCustomerGrp").trim());
		if(request.getParameter("sCustomerGrp") != null && !request.getParameter("sCustomerGrp").trim().equals(""))
			model.setCustGroupId(Integer.parseInt(request.getParameter("sCustomerGrp")));
		return model;
	}

}
