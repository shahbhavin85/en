package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.OfferBroker;
import com.en.model.ItemCategoryModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.OfferModel;

public class OfferHandler extends Basehandler {

	public static String getPageName() {
		return OFFER_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addOffer.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_OFFER)){
			addOffer(request,response);			
		} else if(strAction.equals(MOD_OFFER)){
//			initializeModify(request);
			pageType = "modifyOffer.jsp";			
		} else if(strAction.equals(INIT_OFFER_LIST)){
			initializeOfferList(request);
			pageType = "offerList.jsp";			
		} else if(strAction.equals(UPDT_OFFER)){
			updateOffer(request);
			pageType = "modifyOffer.jsp";			
		} else if(strAction.equals(GET_OFFER)){
			getOfferDtls(request);
			pageType = "modifyOffer.jsp";			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void initializeOfferList(HttpServletRequest request) {
		OfferModel[] offers = getOfferBroker().getOfferList();
		request.setAttribute(FORM_DATA, offers);
		return;
	}

	private void updateOffer(HttpServletRequest request) {
		OfferBroker broker = getOfferBroker();
		OfferModel model = populateDataModel(request);
		MessageModel msgModel = broker.updateOffer(model);
		request.setAttribute(MESSAGES, msgModel);
		model = broker.getOfferDtls(model.getOfferid());
		request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private void getOfferDtls(HttpServletRequest request) {
		OfferModel model = getOfferBroker().getOfferDtls(Integer.parseInt(request.getParameter("txtOfferId")));
		request.setAttribute(FORM_DATA, model);
		if(model == null){
			MessageModel msgModel = new MessageModel();
			msgModel.addNewMessage(new Message(ERROR, "Offer Id is deleted or doesn't exists."));
			request.setAttribute(MESSAGES, msgModel);
		}
		initialize(request);
		return;
	}

	private void initialize(HttpServletRequest request) {
		ItemCategoryModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		return;
	}

	private void addOffer(HttpServletRequest request,
			HttpServletResponse response) {
		OfferModel model = populateDataModel(request);
		MessageModel msgModel = getOfferBroker().addOffer(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private OfferModel populateDataModel(HttpServletRequest request) {
		OfferModel model = new OfferModel();
		if(request.getParameter("txtOfferId") != null && !request.getParameter("txtOfferId").trim().equals(""))
			model.setOfferid(Integer.parseInt(request.getParameter("txtOfferId")));
		if(request.getParameter("txtOfferPrice") != null && !request.getParameter("txtOfferPrice").trim().equals(""))
			model.setOfferPrice(request.getParameter("txtOfferPrice"));
		if(request.getParameter("txtOfferName") != null && !request.getParameter("txtOfferName").trim().equals(""))
			model.setOfferName(request.getParameter("txtOfferName"));
		if(request.getParameter("txtFromDate") != null && !request.getParameter("txtFromDate").trim().equals(""))
			model.setFromDate(request.getParameter("txtFromDate"));
		if(request.getParameter("txtToDate") != null && !request.getParameter("txtToDate").trim().equals(""))
			model.setToDate(request.getParameter("txtToDate"));
		if(request.getParameter("txtPacking") != null && !request.getParameter("txtPacking").trim().equals(""))
			model.setPacking(Double.parseDouble(request.getParameter("txtPacking")));
		if(request.getParameter("txtForwarding") != null && !request.getParameter("txtForwarding").trim().equals(""))
			model.setForwarding(Double.parseDouble(request.getParameter("txtForwarding")));
		if(request.getParameter("txtInstall") != null && !request.getParameter("txtInstall").trim().equals(""))
			model.setInstallation(Double.parseDouble(request.getParameter("txtInstall")));
		String offerItems = request.getParameter("txtOfferItems");
		StringTokenizer stRow = new StringTokenizer(offerItems,"#");
		while (stRow.hasMoreElements()) {
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			ItemModel it = new ItemModel();
			it.setItemId(Integer.parseInt(stCell.nextToken()));
			it.setItemNumber(stCell.nextToken());
			model.addOfferItem(it, Integer.parseInt(stCell.nextToken()));
		}
		return model;
	}

}
