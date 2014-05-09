package com.en.handler;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.AccessPointModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TransferModel;
import com.en.util.Utils;

public class TransferReportHandler extends Basehandler {

	public static String getPageName() {
		return TRANSFER_REPORT_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "initTransferMasterRpt.jsp";
		// action to taken
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(GET_MASTER_RPT)){
        	pageType = getMasterReport(request);			
		} else if(strAction.equals(PRNT_MASTER_RPT)){
			pageType = printMasterReport(request);			
		} else if(strAction.equals(EXPT_MASTER_RPT)){
			pageType = exportMasterReport(request);			
		} else if(strAction.equals(INIT_ITEM_RPT)){
			pageType = "initTransferItemRpt.jsp";			
		} else if(strAction.equals(GET_ITEM_RPT)){
			pageType = getItemReport(request);			
		} else if(strAction.equals(EXPT_ITEM_RPT)){
			pageType = exportItemReport(request);			
		} else if(strAction.equals(PRNT_ITEM_RPT)){
			pageType = printItemReport(request);			
		} else if(strAction.equals(GET_TRANSFER)){
			pageType = getTransferDtls(request);			
		} else {
			initialize(request);	
		}
		return pageType;
	}

	private String getTransferDtls(HttpServletRequest request) {
		String pageType = "showTransferDtls.jsp";
		String billNo = request.getParameter("billNo");
		TransferModel model = getTransferBroker().getTranferDetails(billNo);
		request.setAttribute(FORM_DATA, model);
		return pageType;
	}

	public String getMasterReport(HttpServletRequest request) {
		String pageType = "transferMasterRpt.jsp";
		String[] fromBranch = request.getParameterValues("cFromBranch");
		request.setAttribute("cFromBranch", request.getParameterValues("cFromBranch"));
		String[] toBranch = request.getParameterValues("cToBranch");
		request.setAttribute("cToBranch", request.getParameterValues("cToBranch"));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		TransferModel[] reportBills = getTransferReportBroker().getMasterRpt(fromBranch,toBranch,fromdt,todt);
		if(reportBills.length == 0){
			MessageModel msg = new MessageModel();
			msg.addNewMessage(new Message(ERROR, "No data exists for selected criteria."));
			request.setAttribute(MESSAGES, msg);
			pageType = "initTransferMasterRpt.jsp";
		} else {
			request.setAttribute(FORM_DATA, reportBills);
		}
		initialize(request);
		return pageType;
	}

	public String getItemReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String printMasterReport(HttpServletRequest request) {
		String pageType = "printTransferMasterRpt.jsp";
		StringTokenizer fromBranchST = new StringTokenizer(request.getParameter("cFromBranch"),"|");
		ArrayList<String> fromBranchArr = new ArrayList<String>(0);
		while(fromBranchST.hasMoreTokens()){
			fromBranchArr.add(fromBranchST.nextToken());
		}
		request.setAttribute("cFromBranch", (String[]) fromBranchArr.toArray(new String[0]));
		StringTokenizer toBranchST = new StringTokenizer(request.getParameter("cFromBranch"),"|");
		ArrayList<String> toBranchArr = new ArrayList<String>(0);
		while(toBranchST.hasMoreTokens()){
			toBranchArr.add(toBranchST.nextToken());
		}
		request.setAttribute("cToBranch", (String[]) toBranchArr.toArray(new String[0]));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		TransferModel[] reportBills = getTransferReportBroker().getMasterRpt((String[]) fromBranchArr.toArray(new String[0]),
				(String[]) fromBranchArr.toArray(new String[0]),fromdt,todt);
		request.setAttribute(FORM_DATA, reportBills);
		initialize(request);
		return pageType;
	}

	public String exportMasterReport(HttpServletRequest request) {
		String pageType = "exportTransferMasterRpt.jsp";
		String[] fromBranch = request.getParameterValues("cFromBranch");
		request.setAttribute("cFromBranch", request.getParameterValues("cFromBranch"));
		String[] toBranch = request.getParameterValues("cToBranch");
		request.setAttribute("cToBranch", request.getParameterValues("cToBranch"));
		String fromdt = (request.getParameter("txtFromDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtFromDate"));
		request.setAttribute("txtFromDate", request.getParameter("txtFromDate"));
		String todt = (request.getParameter("txtToDate").equals("")) ? "" : Utils.convertToSQLDate(request.getParameter("txtToDate"));
		request.setAttribute("txtToDate", request.getParameter("txtToDate"));
		TransferModel[] reportBills = getTransferReportBroker().getMasterRpt(fromBranch,toBranch,fromdt,todt);
		request.setAttribute(FORM_DATA, reportBills);
		initialize(request);
		return pageType;
	}

	public String printItemReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public String exportItemReport(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public void initialize(HttpServletRequest request) {
		AccessPointModel[] branches = getAccessPointBroker().getAccessPoint();
		request.setAttribute(ACCESS_POINTS, branches);
		return;
	}

}
