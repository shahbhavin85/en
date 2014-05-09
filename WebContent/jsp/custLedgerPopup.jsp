<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.EntryModel"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.OfferModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<%
EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])request.getAttribute(Constant.FORM_DATA)) : new EntryModel[0];
%>
<script type="text/javascript">
function fnGetLedgerRpt(){
	document.forms[0].HANDLERS.value = "CUSTOMER_LEDGER_RPT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_LEDGER_RPT";
	fnFinalSubmit();
}
<%
if(entries.length > 0){
%>
function fnPrintLedger(){
	window.open('/en/app?HANDLERS=CUSTOMER_LEDGER_RPT_HANDLER&ACTIONS=PRNT_LEDGER_RPT&sCustomer=<%=(request.getAttribute("sCustomer"))%>&txtFromDate=<%=(request.getAttribute("txtFromDate"))%>&txtToDate=<%=(request.getAttribute("txtToDate"))%>',"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExportLedger(){
	document.forms[0].sCustomer.value = <%=(request.getAttribute("sCustomer"))%>;
	document.forms[0].txtFromDate.value = document.forms[0].txtFromDate.defaultValue;
	document.forms[0].txtToDate.value = document.forms[0].txtToDate.defaultValue;
	document.forms[0].HANDLERS.value = "CUSTOMER_LEDGER_RPT_HANDLER";
	document.forms[0].ACTIONS.value = "EXPT_LEDGER_RPT";
	document.forms[0].submit();
}

function fnEmailLedger(){
	document.forms[0].sCustomer.value = <%=(request.getAttribute("sCustomer"))%>;
	document.forms[0].txtFromDate.value = document.forms[0].txtFromDate.defaultValue;
	document.forms[0].txtToDate.value = document.forms[0].txtToDate.defaultValue;
	document.forms[0].HANDLERS.value = "CUSTOMER_LEDGER_RPT_HANDLER";
	document.forms[0].ACTIONS.value = "EMAIL_LEDGER_RPT";
	fnFinalSubmit();
}
<%
}
%>
</script>
</head>
<body>
<form method="post" name="frmViewEnquiry">
	<div align="center" id="FreezePane" class="FreezePaneOn"></div>
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Customer Ledger Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td><input type="text" readonly="readonly" value="<%=(String)request.getAttribute("sCustomer")%>" name="sCustomer"/></td>
				</tr>
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") : DateUtil.getFirstDt() %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         To Date<span style="color: red">*</span> :
					<input name="txtToDate" id="txtToDate" style="width: 195px;" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : DateUtil.getCurrDt() %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Details" onclick="fnGetLedgerRpt();"/></td>
				</tr>
			</table>
			<%
				if(entries.length > 0){
					double crTotal = 0;
					double drTotal = 0;
					double oTotal = 0;
			%>
			<table cellspacing="0" cellpadding="5" border="1" style="margin-top: 10px; width: 950px;">
				<tr>
					<th style="width: 50px;">Date</th>
					<th>Particulars</th>
					<th style="width: 120px;">DR. (RECEIVABLE)</th>
					<th style="width: 120px;">CR. (PAYABLE)</th>
					<th style="width: 100px;">Balance</th>
				</tr>
				<%
						String bgColor = "";
						for(int i=0; i<entries.length; i++){
							if(entries[i].getCrdr().equals("D")) {
								crTotal += entries[i].getAmount();
								oTotal = oTotal + entries[i].getAmount();
							} else { 
								drTotal += entries[i].getAmount();
								oTotal = oTotal - entries[i].getAmount();
							}
				%>
				<tr>
					<td align="center"><%=Utils.convertToAppDateDDMMYY(entries[i].getEntryDate())%></td>
					<td align="left"><%=entries[i].getLedgerDesc() %></td>
					<td align="right"><%=(entries[i].getCrdr().equals("D")) ? Utils.get2Decimal(entries[i].getAmount()) : "--"%></td>
					<td align="right"><%=(entries[i].getCrdr().equals("C")) ? Utils.get2Decimal(entries[i].getAmount()) : "--"%></td>
					<td align="right"><%=(oTotal > 0) ? Utils.get2Decimal(oTotal)+"Dr." : Utils.get2Decimal(0-oTotal)+"Cr."  %></td>
				</tr>
				<%
						}
				%>
				<tr>
					<td align="center"><%=Utils.convertToAppDateDDMMYY(Utils.convertToSQLDate((String)request.getAttribute("txtToDate")))%></td>
					<td align="left">Closing Balance - <%=(oTotal > 0) ? "Receivable" : "Payable" %></td>
					<td align="right"><%=Utils.get2Decimal(crTotal)%></td>
					<td align="right"><%=Utils.get2Decimal(drTotal)%></td>
					<td align="right"><%=(oTotal > 0) ? Utils.get2Decimal(oTotal)+"Dr." : Utils.get2Decimal(0-oTotal)+"Cr."  %></td>
				</tr>
				<tr>
					<td colspan="5" align="center"><input type="button" value="Close" onclick="window.close();"/>&nbsp;&nbsp;<input type="button" value="Print" onclick="fnPrintLedger();"/>&nbsp;&nbsp;<input type="button" value="Export" onclick="fnExportLedger();"/>&nbsp;&nbsp;<input type="button" value="Email" onclick="fnEmailLedger();"/></td>
				</tr>
			</table>
			<%	
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="POPUP" value="Y"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>