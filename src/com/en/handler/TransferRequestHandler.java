package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TransferModel;

public class TransferRequestHandler extends Basehandler {

	public static String getPageName() {
		return TRANSFER_REQUEST_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "transferRequest.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_TRANSFER_REQUEST)){
			 pageType = addTransferRequest(request);			
//		} else if(strAction.equals(PRNT_TRANSFER)){
//			 pageType = printTransfer(request);			
//		} else if(strAction.equals(INIT_EXPT_DATA)){
//			 pageType = "exportTransfer.jsp";			
//		} else if(strAction.equals(EXPT_MASTER_DATA)){
//			 pageType = exportMasterData(request);			
//		} else if(strAction.equals(EXPT_ITEM_DATA)){
//			 pageType = exportItemData(request);			
//		} else if(strAction.equals(EXPT_APPVD_DATA)){
//			 pageType = exportApprovedData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_TRANSFER_REQUEST)){
//			getTodayTransferDetails(request);
			 pageType = "viewTransferRequest.jsp";			
		} else if(strAction.equals(GET_TRANSFER_REQUEST)){
			 pageType = getTransferRequestDetails(request);			
		} else if(strAction.equals(SHOW_TRANSFER_REQUEST)){
			getTransferRequestDetails(request);
			pageType = "viewTranferRequestDtls.jsp";			
//		} else if(strAction.equals(INIT_APP_TRANSFER)){
//			 pageType = initializeApproval(request);			
//		} else if(strAction.equals(GET_APP_TRANSFER)){
//			 pageType = getApprovalDetails(request);			
//		} else if(strAction.equals(APP_TRANSFER)){
//			 pageType = approveTransfer(request);			
//		} else if(strAction.equals(EDIT_TRANSFER_LR_DTLS)){
//			pageType = editTransferLRDetails(request);			
//		}  else if(strAction.equals(EDIT_TRANSFER)){
//			 pageType = editTransfer(request);			
//		} else if(strAction.equals(INIT_EDIT_TRANSFER_LR_DTLS)){
//			pageType = initEditTransferLRDetails(request);			
//		} else if(strAction.equals(GET_EDIT_TRANSFER_LR_DTLS)){
//			pageType = getEditTransferLRDetails(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String getTransferRequestDetails(HttpServletRequest request) {
		String pageType = "showTransferRequest.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel model = getTransferRequestBroker().getTranferRequestDetails(billNo);
		if(model.getTransferid() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewTransferRequest.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String addTransferRequest(HttpServletRequest request) {
		String pageType = "showTransferRequest.jsp";
		TransferModel model = populateDataModel(request);		
		MessageModel msg = getTransferRequestBroker().addTransferRequest(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getTransferRequestBroker().getTranferRequestDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "transferRequest.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private TransferModel populateDataModel(HttpServletRequest request) {
		TransferModel model = new TransferModel();
		if(request.getParameter("txtRemarks") != null && !request.getParameter("txtRemarks").trim().equals(""))
			model.setRemark(request.getParameter("txtRemarks"));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.setTransferid(Integer.parseInt(request.getParameter("txtInvoiceNo").substring(5)));
		if(request.getParameter("txtInvoiceNo") != null && !request.getParameter("txtInvoiceNo").trim().equals(""))
			model.getFromBranch().setBillPrefix(request.getParameter("txtInvoiceNo").substring(0,3));
//		if(model.getOfferType().equals("1") && request.getParameter("txtOfferItems") != null && !request.getParameter("txtOfferItems").trim().equals("")){
			String salesItems = request.getParameter("txtSalesItems");
			StringTokenizer stRow = new StringTokenizer(salesItems,"#");
			while (stRow.hasMoreElements()) {
				String tempRow = stRow.nextToken();
				StringTokenizer stCell = new StringTokenizer(tempRow,"|");
				ItemModel it = new ItemModel();
				it.setItemId(Integer.parseInt(stCell.nextToken()));
				it.setItemNumber(stCell.nextToken());
				model.addItem(it, stCell.nextToken(), Double.parseDouble(stCell.nextToken()), 0);
			}
//		}
		model.getFromBranch().setAccessId(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		model.getToBranch().setAccessId(Integer.parseInt((String)request.getParameter("sTo")));
		return model;
	}

	private void initialize(HttpServletRequest request) {
		AccessPointModel[] accessPoints = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, accessPoints);
		ItemModel[] items = getItemBroker().getTransferItem();
		request.setAttribute(ITEMS, items);
		return;
	}

}
