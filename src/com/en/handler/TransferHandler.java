package com.en.handler;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TransferItemModel;
import com.en.model.TransferModel;
import com.en.model.UserModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class TransferHandler extends Basehandler {

	public static String getPageName() {
		return TRANSFER_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "transfer.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(ADD_TRANSFER)){
			 pageType = addTransfer(request);			
		} else if(strAction.equals(ADD_REQUEST_TRANSFER)){
			 pageType = addRequestTransfer(request);			
		} else if(strAction.equals(PRNT_TRANSFER)){
			 pageType = printTransfer(request);			
		} else if(strAction.equals(INIT_EXPT_DATA)){
			 pageType = "exportTransfer.jsp";			
		} else if(strAction.equals(EXPT_MASTER_DATA)){
			 pageType = exportMasterData(request);			
		} else if(strAction.equals(EXPT_ITEM_DATA)){
			 pageType = exportItemData(request);			
		} else if(strAction.equals(EXPT_APPVD_DATA)){
			 pageType = exportApprovedData(request);			
		} else if(strAction.equals(INIT_VIEW_EDIT_TRANSFER)){
			getTodayTransferDetails(request);
			 pageType = "viewTransfer.jsp";			
		} else if(strAction.equals(GET_TRANSFER)){
			 pageType = getTransferDetails(request);			
		} else if(strAction.equals(INIT_EDIT_TRANSFER)){
			 pageType = initializeEdit(request);			
		} else if(strAction.equals(INIT_APP_TRANSFER)){
			 pageType = initializeApproval(request);			
		} else if(strAction.equals(GET_APP_TRANSFER)){
			 pageType = getApprovalDetails(request);			
		} else if(strAction.equals(APP_TRANSFER)){
			 pageType = approveTransfer(request);			
		} else if(strAction.equals(EDIT_TRANSFER_LR_DTLS)){
			pageType = editTransferLRDetails(request);			
		}  else if(strAction.equals(EDIT_TRANSFER)){
			 pageType = editTransfer(request);			
		} else if(strAction.equals(INIT_EDIT_TRANSFER_LR_DTLS)){
			pageType = initEditTransferLRDetails(request);			
		} else if(strAction.equals(GET_EDIT_TRANSFER_LR_DTLS)){
			pageType = getEditTransferLRDetails(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String addRequestTransfer(HttpServletRequest request) {
		String pageType = "showTransfer.jsp";
		TransferModel model = populateDataModel(request);		
		String orderno = request.getParameter("ORDER_NO");
		MessageModel msg = getTransferBroker().addTransfer(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			getOrderBroker().adjustPartRequestBill(orderno, model.getItems());
			model = getTransferBroker().getTranferDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "transfer.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getEditTransferLRDetails(HttpServletRequest request) {
		String pageType = "editTransferLR.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel model = getTransferBroker().getTranferDetails(billNo);
		if(model.getTransferid() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
		} else if(model.isToApproved()){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" is already approved."));
			request.setAttribute(MESSAGES, msg);
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String initEditTransferLRDetails(HttpServletRequest request) {
		String pageType = "editTransferLR.jsp";
		return pageType;
	}

	private String editTransferLRDetails(HttpServletRequest request) {
		String pageType = "showTransfer.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String lrNo = request.getParameter("txtLRNo");
		String transport = request.getParameter("txtDespatch");
		String lrDt = Utils.convertToSQLDate(request.getParameter("txtLRDt"));
		MessageModel msg = getTransferBroker().editTransferLRDetails(billNo, lrNo, lrDt, transport, ((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId());
		if((msg.getMessages().get(0)).getType().equals(SUCCESS)){
			TransferModel model = getTransferBroker().getTranferDetails(billNo);
			request.setAttribute(FORM_DATA, model);
		} else {
			pageType = getEditTransferLRDetails(request);
		}
		return pageType;
	}

	private String exportApprovedData(HttpServletRequest request) {
		String pageType = "exportTransferAppvd.jsp";
		TransferModel[] model = getTransferBroker().getExportTransferApproved(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private void getTodayTransferDetails(HttpServletRequest request) {
		TransferModel[] model = getTransferBroker().getTodayTransferDetails(Utils.convertToSQLDate(DateUtil.getCurrDt()), Utils.convertToSQLDate(DateUtil.getCurrDt()), (String)request.getSession().getAttribute(ACCESS_POINT));	
		request.setAttribute(FORM_DATA, model);
		return;
	}

	private String exportItemData(HttpServletRequest request) {
		String pageType = "exportTransferItem.jsp";
		TransferItemModel[] model = getTransferBroker().getExportTransferItems(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String exportMasterData(HttpServletRequest request) {
		String pageType = "exportTransferMaster.jsp";
		TransferModel[] model = getTransferBroker().getExportTransferMaster(Utils.convertToSQLDate(request.getParameter("txtFromDate")), Utils.convertToSQLDate(request.getParameter("txtToDate")));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String approveTransfer(HttpServletRequest request) {
		String pageType = "getAppTransfer.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		String recdDt = request.getParameter("txtAppDate");
		MessageModel msg = getTransferBroker().approveTranfer(billNo,(UserModel) request.getSession().getAttribute(USER), recdDt);
		request.setAttribute(MESSAGES, msg);
		TransferModel model = getTransferBroker().getTranferDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getApprovalDetails(HttpServletRequest request) {
		String pageType = "getAppTransfer.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel model = getTransferBroker().getTranferDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initializeApproval(HttpServletRequest request) {
		String pageType = "initAppTransfer.jsp";
		TransferModel[] model = getTransferBroker().getAwaitingApprovalTranfer(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)));
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String initializeEdit(HttpServletRequest request) {
		String pageType = "editTransfer.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel model = getTransferBroker().getTranferDetails(billNo);
		initialize(request);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String editTransfer(HttpServletRequest request) {
		String pageType = "showTransfer.jsp";
		TransferModel model = populateDataModel(request);		
		MessageModel msg = getTransferBroker().editTransfer(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getTransferBroker().getTranferDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "editTransfer.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private String getTransferDetails(HttpServletRequest request) {
		String pageType = "showTransfer.jsp";
		String billNo = request.getParameter("txtInvoiceNo");
		TransferModel model = getTransferBroker().getTranferDetails(billNo);
		if(model.getTransferid() == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, billNo +" doesnot exists."));
			request.setAttribute(MESSAGES, msg);
			pageType = "viewTransfer.jsp";
		} else {
			request.setAttribute(FORM_DATA, model);
		}
		return pageType;
	}

	private String printTransfer(HttpServletRequest request) {
		String pageType = "printTransfer.jsp";
		int copy = Integer.parseInt(request.getParameter("copies"));
		String[] copies = null;
		if(copy == 0){
			copies = (String[]) new String[] {"ORIGINAL","TRANSPORT COPY","OFFICE COPY"};
		} else if(copy == 1){
			copies = (String[]) new String[] {"ORIGINAL"};
		} else if(copy == 2){
			copies = (String[]) new String[]  {"TRANSPORT COPY"};
		} else {
			copies = (String[]) new String[] {"OFFICE COPY"};
		}
		request.setAttribute("copy", copies);	
		TransferModel model = getTransferBroker().getTranferDetails(request.getParameter("billNo"));	
		request.setAttribute(FORM_DATA, model);	
		return pageType;
	}

	private String addTransfer(HttpServletRequest request) {
		String pageType = "showTransfer.jsp";
		TransferModel model = populateDataModel(request);		
		MessageModel msg = getTransferBroker().addTransfer(model);
		if((msg.getMessages().get(1)).getType().equals(SUCCESS)){
			model = getTransferBroker().getTranferDetails((msg.getMessages().get(1)).getMsg());
		} else {
			pageType = "transfer.jsp";
			initialize(request);
		}
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	private TransferModel populateDataModel(HttpServletRequest request) {
		TransferModel model = new TransferModel();
		if(request.getParameter("txtDespatch") != null && !request.getParameter("txtDespatch").trim().equals(""))
			model.setTransport(request.getParameter("txtDespatch"));
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
				model.addItem(it, stCell.nextToken(), Double.parseDouble(stCell.nextToken()), Double.parseDouble(stCell.nextToken()));
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
