package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ActionModel;
import com.en.model.CustomerModel;
import com.en.model.EnquiryModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OfferModel;
import com.en.model.UserModel;
import com.en.util.ActionType;
import com.en.util.Constant;

public class EnquiryHandler extends Basehandler {

	public static String getPageName() {
		return ENQUIRY_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addEnquiry.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_CUSTOMER)){
        	addCustomer(request,response);	
			pageType = "addCustomer1.jsp";		
		} else if(strAction.equals(ADD_ENQUIRY)){
			addEnquiry(request,response);
			pageType = "getEnquiry.jsp";
		} else if(strAction.equals(MOD_CUSTOMER)){
			String[] cities = getCustomerBroker().getCityList();
			request.setAttribute(CITIES, cities);
			pageType = "addCustomer1.jsp";
		} else if(strAction.equals(GET_OFFER)){
			getOffer(request,response);
			pageType = "showOffer.jsp";
		} else if(strAction.equals(INIT_ENQUIRY)){
			initialize(request);
		} else if(strAction.equals(GET_ENQUIRY)){
			pageType = getEnquiry(request);
//			pageType = "getEnquiry.jsp";
		} else if(strAction.equals(VIEW_ENQUIRY)){
			pageType = "viewEnquiry.jsp";
//			initialize(request);
		} else if(strAction.equals(INIT_NEW_ACTION1)){
			initializeAction1(request);
			pageType = "inquiry_action1.jsp";
//			initialize(request);
		} else if(strAction.equals(INIT_NEW_ACTION2)){
			initializeAction2(request);
			pageType = "inquiry_action2.jsp";
//			initialize(request);
		} else if(strAction.equals(ADD_NEW_ACTION)){
			addAction(request);
			pageType = "getEnquiry.jsp";
//			initialize(request);
		} else if(strAction.equals(PRINT_ENQUIRY)){
			getEnquiry(request);
			pageType = "printEnquiry.jsp";
//			initialize(request);
		} else {
			String[] cities = getCustomerBroker().getCityList();
			request.setAttribute(CITIES, cities);
			String[] custTypes = getCustomerBroker().getCustomerTypes();
			request.setAttribute(CUST_TYPE, custTypes);
			pageType = "addCustomer1.jsp";	
		}
		return pageType;
	}

	private void addAction(HttpServletRequest request) {
		ActionModel action = new ActionModel();
		action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
		int actionType = Integer.parseInt(request.getParameter("sType"));
		action.setActionType(actionType+"");
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		if(actionType == 0 || actionType == 7 || actionType == 8) {
			action.setRemarks(request.getParameter("taRemark"));
		} else if(actionType == 1 || actionType == 6) {
			action.getSalesman().setUserId(request.getParameter("sUserId"));
			action.setAppDt(request.getParameter("txtAppointDate"));
			if(request.getParameter("txtAppointTime") != null && !request.getParameter("txtAppointTime").trim().equals("")){
				String appTime = "";
				if(request.getParameter("txtAppointTime").substring(request.getParameter("txtAppointTime").length()-2).equals("am")){
					if(request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-2).length() == 5){
						appTime = "0"+request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-3)+":00";
					} else if(request.getParameter("txtAppointTime").substring(0,2).equals("12")){
						appTime = "00:00:00";
					} else {
						appTime = request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-3)+":00";
					}
				} else {
					if(request.getParameter("txtAppointTime").substring(0,2).equals("12")){
						appTime = "12"+request.getParameter("txtAppointTime").substring(2,request.getParameter("txtAppointTime").length()-3)+":00";
					} else {
						appTime = (Integer.parseInt(request.getParameter("txtAppointTime").substring(0,(request.getParameter("txtAppointTime").indexOf(':'))))+12)+request.getParameter("txtAppointTime").substring(request.getParameter("txtAppointTime").indexOf(':'),request.getParameter("txtAppointTime").length()-3)+":00";
					}
				}
				
				action.setAppTm(appTime);
				action.setRemarks(request.getParameter("taRemark"));
			}
		} else if(actionType == 2){
			EnquiryModel model = getEnquiryBroker().getEnquiryDetails(enqNo);
			action.getPrevACPoint().setAccessId(model.getAccessPoint().getAccessId());
			action.getNewACPoint().setAccessId(Integer.parseInt(request.getParameter("current_point")));
			action.setRemarks(request.getParameter("taRemark"));
		} else if(actionType == 4) {
			action.setAppDt(request.getParameter("txtAppointDate"));
			if(request.getParameter("txtInTime") != null && !request.getParameter("txtInTime").trim().equals("")){
				String inTime = "";
				if(request.getParameter("txtInTime").substring(request.getParameter("txtInTime").length()-2).equals("am")){
					if(request.getParameter("txtInTime").substring(0,request.getParameter("txtInTime").length()-2).length() == 5){
						inTime = "0"+request.getParameter("txtInTime").substring(0,request.getParameter("txtInTime").length()-3)+":00";
					} else if(request.getParameter("txtInTime").substring(0,2).equals("12")){
						inTime = "00:00:00";
					} else {
						inTime = request.getParameter("txtInTime").substring(0,request.getParameter("txtInTime").length()-3)+":00";
					}
				} else {
					if(request.getParameter("txtInTime").substring(0,2).equals("12")){
						inTime = "12"+request.getParameter("txtInTime").substring(2,request.getParameter("txtInTime").length()-3)+":00";
					} else {
						inTime = (Integer.parseInt(request.getParameter("txtInTime").substring(0,(request.getParameter("txtInTime").indexOf(':'))))+12)+request.getParameter("txtInTime").substring(request.getParameter("txtInTime").indexOf(':'),request.getParameter("txtInTime").length()-3)+":00";
					}
				}
				action.setInTm(inTime);
			}
			if(request.getParameter("txtOutTime") != null && !request.getParameter("txtOutTime").trim().equals("")){
				String outTime = "";
				if(request.getParameter("txtOutTime").substring(request.getParameter("txtOutTime").length()-2).equals("am")){
					if(request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-2).length() == 5){
						outTime = "0"+request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-3)+":00";
					} else if(request.getParameter("txtOutTime").substring(0,2).equals("12")){
						outTime = "00:00:00";
					} else {
						outTime = request.getParameter("txtOutTime").substring(0,request.getParameter("txtOutTime").length()-3)+":00";
					}
				} else {
					if(request.getParameter("txtOutTime").substring(0,2).equals("12")){
						outTime = "12"+request.getParameter("txtOutTime").substring(2,request.getParameter("txtOutTime").length()-3)+":00";
					} else {
						outTime = (Integer.parseInt(request.getParameter("txtOutTime").substring(0,(request.getParameter("txtOutTime").indexOf(':'))))+12)+request.getParameter("txtOutTime").substring(request.getParameter("txtOutTime").indexOf(':'),request.getParameter("txtOutTime").length()-3)+":00";
					}
				}
				action.setOutTm(outTime);
			}
			action.setRemarks(request.getParameter("taRemark"));
		} else if(actionType == 5) {
			action.setAppDt(request.getParameter("txtAppointDate"));
			action.setRemarks(request.getParameter("taRemark"));
		}
		MessageModel msgs = getEnquiryBroker().addAction(action, enqNo);
		if(msgs.getMessages().size() > 0 && msgs.getMessages().get(0).getType().equals(ERROR)){
			request.setAttribute(MESSAGES, msgs);
		}
		getEnquiry(request);
	}

	private void initializeAction2(HttpServletRequest request) {
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		request.setAttribute("EnqNo", enqNo);
		EnquiryModel model = getEnquiryBroker().getEnquiryDetails(enqNo);
		request.setAttribute(FORM_DATA,model);
		int actionType = Integer.parseInt(request.getParameter("sType"));
		if(actionType == 1 || actionType == 6){
			UserModel[] users = getUserBroker().getUsers();
			request.setAttribute(USERS, users);
		} else if(actionType == 2){
			AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
			request.setAttribute(ACCESS_POINTS, accessPoints);
		} 
		request.setAttribute("sType", actionType);
		return;
	}

	private void initializeAction1(HttpServletRequest request) {
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		request.setAttribute("EnqNo", enqNo);
		String lastAction = getEnquiryBroker().getLastAction(enqNo);
		request.setAttribute("last_action", lastAction);
		return;
	}

	private String getEnquiry(HttpServletRequest request) {
		String pageType = "";
		long enqNo = Long.parseLong(request.getParameter("txtEnqNo"));
		EnquiryModel model = getEnquiryBroker().getEnquiryDetails(enqNo);
		if(model == null){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "Enquiry No "+enqNo+" doesnot exist."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewEnquiry.jsp";
		} else if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && 
				!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && 
				!request.getSession().getAttribute(Constant.ACCESS_POINT).equals(model.getAccessPoint().getAccessId()+"")){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "Your Access point dont have access on Enquiry No "+enqNo+"."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewEnquiry.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
			pageType = "getEnquiry.jsp";
		}
		return pageType;
	}

	private void getOffer(HttpServletRequest request,
			HttpServletResponse response) {
//		int itemId = Integer.parseInt(request.getParameter("ITEMID"));
//		ItemModel item = getItemBroker().getItemDtls(itemId);
//		request.setAttribute("ITEMID", item.getItemId());
//		request.setAttribute("ITEMNUMBER", item.getItemNumber());
		OfferModel[] offers = getOfferBroker().getCurrentOfferList();
		request.setAttribute("OFFER_ITEMS", offers);
		return;
	}

	private void addEnquiry(HttpServletRequest request,
			HttpServletResponse response) {
			EnquiryModel model = populateDataModel(request);
			model = getEnquiryBroker().addEnquiry(model);
			request.setAttribute(FORM_DATA, model);
	}

	private void addCustomer(HttpServletRequest request,
			HttpServletResponse response) {
		CustomerModel model = populateCustomerDataModel(request);
		EnquiryModel eModel = new EnquiryModel();
		eModel.setCustomer(model);
		MessageModel msgModel = getCustomerBroker().addCustomer(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS)){
			eModel.getCustomer().setCustomerId(Integer.parseInt(msgModel.getMessages().get(1).getMsg()));
		}
//		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, eModel);
//		initialize(request);
		String[] custTypes = getCustomerBroker().getCustomerTypes();
		request.setAttribute(CUST_TYPE, custTypes);
		String[] cities = getCustomerBroker().getCityList();
		request.setAttribute(CITIES, cities);
		return;
	}

	private void initialize(HttpServletRequest request) {
		CustomerModel[] customers = getCustomerBroker().getCustomer1();
		request.setAttribute(CUSTOMERS, customers);
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		ItemModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		ItemModel[] offerItems = getItemBroker().getALLItems();
		request.setAttribute(OFFER_ITEMS, offerItems);
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		return;
	}

	private EnquiryModel populateDataModel(HttpServletRequest request) {
		EnquiryModel model = new EnquiryModel();
		if(request.getParameter("sCustomer") != null && !request.getParameter("sCustomer").trim().equals(""))
			model.getCustomer().setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		if(request.getParameter("sPriority") != null && !request.getParameter("sPriority").trim().equals(""))
			model.setPriority(request.getParameter("sPriority"));
		if(request.getParameter("sReference") != null && !request.getParameter("sReference").trim().equals(""))
			model.setReference(request.getParameter("sReference"));
		if(request.getParameter("taRemark") != null && !request.getParameter("taRemark").trim().equals(""))
			model.setRemarks(request.getParameter("taRemark"));
		if(request.getParameter("current_point") != null && !request.getParameter("current_point").trim().equals(""))
			model.getAccessPoint().setAccessId(Integer.parseInt(request.getParameter("current_point")));
		if(request.getParameter("txtEnqItem") != null && !request.getParameter("txtEnqItem").trim().equals("")){
			String offerItems = request.getParameter("txtEnqItem");
			StringTokenizer stRow = new StringTokenizer(offerItems,"#");
			while (stRow.hasMoreElements()) {
				String tempRow = stRow.nextToken();
				StringTokenizer stCell = new StringTokenizer(tempRow,"|");
				ItemModel it = new ItemModel();
				it.setItemId(Integer.parseInt(stCell.nextToken()));
				it.setItemNumber(stCell.nextToken());
				String offerNo = stCell.nextToken();
				model.addItem(it, Integer.parseInt(stCell.nextToken()), offerNo);
			}
		}
		if(request.getParameter("appointment") != null){
			ActionModel action = new ActionModel();
			action.getSalesman().setUserId(request.getParameter("sUserId"));
			action.setAppDt(request.getParameter("txtAppointDate"));
			if(request.getParameter("txtAppointTime") != null && !request.getParameter("txtAppointTime").trim().equals("")){
				String appTime = "";
				if(request.getParameter("txtAppointTime").substring(request.getParameter("txtAppointTime").length()-2).equals("am")){
					if(request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-2).length() == 5){
						appTime = "0"+request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-3)+":00";
					} else if(request.getParameter("txtAppointTime").substring(0,2).equals("12")){
						appTime = "00:00:00";
					} else {
						appTime = request.getParameter("txtAppointTime").substring(0,request.getParameter("txtAppointTime").length()-3)+":00";
					}
				} else {
					if(request.getParameter("txtAppointTime").substring(0,2).equals("12")){
						appTime = "12"+request.getParameter("txtAppointTime").substring(2,request.getParameter("txtAppointTime").length()-3)+":00";
					} else {
						appTime = (Integer.parseInt(request.getParameter("txtAppointTime").substring(0,(request.getParameter("txtAppointTime").indexOf(':'))))+12)+request.getParameter("txtAppointTime").substring(request.getParameter("txtAppointTime").indexOf(':'),request.getParameter("txtAppointTime").length()-3)+":00";
					}
				}
				
				action.setAppTm(appTime);
			}
			action.setAddedBy((UserModel)request.getSession().getAttribute(USER));
			action.setActionType(ActionType.APPOINTMENT);
			model.addAction(action);
		}
		model.setCreatedBy((UserModel)request.getSession().getAttribute(USER));
		model.setUpdatedBy((UserModel)request.getSession().getAttribute(USER));
		return model;
	}

	private CustomerModel populateCustomerDataModel(HttpServletRequest request) {
		CustomerModel model = new CustomerModel();
		if(request.getParameter("txtCustomerName") != null && !request.getParameter("txtCustomerName").trim().equals(""))
			model.setCustomerName(request.getParameter("txtCustomerName"));
		if(request.getParameter("sCustomerType") != null && !request.getParameter("sCustomerType").trim().equals(""))
			model.setCustomerType(request.getParameter("sCustomerType"));
		if(request.getParameter("taAddress") != null && !request.getParameter("taAddress").trim().equals(""))
			model.setAddress(request.getParameter("taAddress"));
		if(request.getParameter("txtCity") != null && !request.getParameter("txtCity").trim().equals(""))
			model.setCity(request.getParameter("txtCity"));
		if(request.getParameter("txtDistrict") != null && !request.getParameter("txtDistrict").trim().equals(""))
			model.setDistrict(request.getParameter("txtDistrict"));
		if(request.getParameter("txtState") != null && !request.getParameter("txtState").trim().equals(""))
			model.setState(request.getParameter("txtState"));
		if(request.getParameter("txtPinCode") != null && !request.getParameter("txtPinCode").trim().equals(""))
			model.setPincode(request.getParameter("txtPinCode"));
		if(request.getParameter("txtStdCode") != null && !request.getParameter("txtStdCode").trim().equals(""))
			model.setStdcode(request.getParameter("txtStdCode"));
		if(request.getParameter("txtPhone1") != null && !request.getParameter("txtPhone1").trim().equals(""))
			model.setPhone1(request.getParameter("txtPhone1"));
		if(request.getParameter("txtPhone2") != null && !request.getParameter("txtPhone2").trim().equals(""))
			model.setPhone2(request.getParameter("txtPhone2"));
		if(request.getParameter("txtMobile1") != null && !request.getParameter("txtMobile1").trim().equals(""))
			model.setMobile1(request.getParameter("txtMobile1"));
		if(request.getParameter("txtMobile2") != null && !request.getParameter("txtMobile2").trim().equals(""))
			model.setMobile2(request.getParameter("txtMobile2"));
		if(request.getParameter("sGrade") != null && !request.getParameter("sGrade").trim().equals(""))
			model.setGrade(request.getParameter("sGrade"));
		if(request.getParameter("txtEmail") != null && !request.getParameter("txtEmail").trim().equals(""))
			model.setEmail(request.getParameter("txtEmail"));
		if(request.getParameter("txtEmail1") != null && !request.getParameter("txtEmail1").trim().equals(""))
			model.setEmail1(request.getParameter("txtEmail1"));
		if(request.getParameter("txtWebsite") != null && !request.getParameter("txtWebsite").trim().equals(""))
			model.setWebsite(request.getParameter("txtWebsite"));
		if(request.getParameter("txtTin") != null && !request.getParameter("txtTin").trim().equals(""))
			model.setTin(request.getParameter("txtTin"));
		if(request.getParameter("txtCst") != null && !request.getParameter("txtCst").trim().equals(""))
			model.setCst(request.getParameter("txtCst"));
		if(request.getParameter("txtTransport") != null && !request.getParameter("txtTransport").trim().equals(""))
			model.setTransport(request.getParameter("txtTransport"));
		if(request.getParameter("txtArea") != null && !request.getParameter("txtArea").trim().equals(""))
			model.setArea(request.getParameter("txtArea"));
		if(request.getParameter("rSupplier") != null && !request.getParameter("rSupplier").trim().equals("") && request.getParameter("rSupplier").trim().equals("Y"))
			model.setSupplier(true);
		return model;
	}

}
