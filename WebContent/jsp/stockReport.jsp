<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.StockItemModel"%>
<%@page import="com.en.model.StockModel"%>
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
StockItemModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? (StockItemModel[])(((StockModel)request.getAttribute(Constant.FORM_DATA)).getItems()).toArray(new StockItemModel[0]) : new StockItemModel[0];
StockModel model = (request.getAttribute(Constant.FORM_DATA) != null) ? (StockModel) request.getAttribute(Constant.FORM_DATA) : null;
%>
<script type="text/javascript">
function fnGetLedgerRpt(){
	if(document.forms[0].sAccessName.value == '--'){
		alert('Please select the branch');
		return;
	}
	if(document.forms[0].txtFromDate.value == ''){
		alert('Please provide from date');
		return;
	}
	if(document.forms[0].txtToDate.value == ''){
		alert('Please provide to date');
		return;
	}
	document.forms[0].HANDLERS.value = "STOCK_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_STOCK_REGISTER_RPT";
	fnFinalSubmit();
}
<%
	if((request.getAttribute(Constant.FORM_DATA)) != null){
%>
function fnGetStockDetailsByItem(id){
	window.open("/en/app?HANDLERS=STOCK_REPORT_HANDLER&ACTIONS=GET_STOCK_ITEM_RPT&itemId="+id+"&branch=<%=model.getBranch().getAccessId()%>&fromDate=<%=model.getFromDate()%>&toDate=<%=model.getToDate()%>","","status=1,scrollbars=1,width=665,height=500");
}
<%
	}
%>
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Stock Register Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Item :</td>
					<td>
						<select name="sItem" style="width: 200px;" tabindex="1">
							<option value="--">-------</option>
							<%
								ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
														(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];
								for(int i=0; i<items.length;i++){
							%>
							<option value="<%=items[i].getItemId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((StockModel)request.getAttribute(Constant.FORM_DATA)).getItem().getItemId() == items[i].getItemId()) ? "selected=\"selected\"" : "" %>><%=items[i].getItemNumber()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Select Access Point <span style="color: red">*</span> :</td>
					<td>
						<select name="sAccessName" style="width: 200px;">
							<%
								if(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() < 11){
									if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
									}
									AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
									AccessPointModel temp = null;
									for(int i=0; i<accessPoints.length; i++){
										temp = accessPoints[i];
							%>
							<option value="<%=temp.getAccessId()%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
								((StockModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId() == temp.getAccessId()) ? "selected=\"selected\"" : "" %>><%=temp.getAccessName()%></option>
							<%
									}
								} else {
							%>
							<option value="<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId()%>" selected="selected"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessName()%> - <%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getCity()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" readonly="readonly" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null && !((StockModel)request.getAttribute(Constant.FORM_DATA)).getFromDate().equals("")) ?  ((StockModel)request.getAttribute(Constant.FORM_DATA)).getFromDate() : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         To Date<span style="color: red">*</span> :
					<input name="txtToDate" readonly="readonly" id="txtToDate" style="width: 195px;" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null && !((StockModel)request.getAttribute(Constant.FORM_DATA)).getToDate().equals("")) ?  ((StockModel)request.getAttribute(Constant.FORM_DATA)).getToDate() : "" %>"><img
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
			%>
			<table cellspacing="0" cellpadding="5" border="1" style="margin-top: 10px; width: 950px;">
<!-- 				<tr> -->
<!-- 					<td colspan="5" align="center"><input type="button" value="Print" onclick="fnPrintLedger();"/>&nbsp;&nbsp;<input type="button" value="Export" onclick="fnExportLedger();"/>&nbsp;&nbsp;<input type="button" value="Email" onclick="fnEmailLedger();"/></td> -->
<!-- 				</tr> -->
				<tr>
					<th style="width: 50px;">ITEMID</th>
					<th>ITEM NUMBER / NAME</th>
					<th style="width: 120px;">STOCK ON <%=(request.getAttribute(Constant.FORM_DATA) != null && !((StockModel)request.getAttribute(Constant.FORM_DATA)).getFromDate().equals("")) ?  Utils.convertToAppDateDDMMYY(Utils.convertToSQLDate(((StockModel)request.getAttribute(Constant.FORM_DATA)).getFromDate())) : "" %></th>
					<th style="width: 120px;">INWARDS (+)</th>
					<th style="width: 100px;">OUTWARDS (-)</th>
					<th style="width: 100px;">STOCK ON <%=(request.getAttribute(Constant.FORM_DATA) != null && !((StockModel)request.getAttribute(Constant.FORM_DATA)).getToDate().equals("")) ?  Utils.convertToAppDateDDMMYY(Utils.convertToSQLDate(((StockModel)request.getAttribute(Constant.FORM_DATA)).getToDate())) : "" %></th>
					<th style="width: 100px;">DETAILS</th>
				</tr>
				<%
						String bgColor = "";
						for(int i=0; i<entries.length; i++){
				%>
				<tr>
					<td align="center"><%=entries[i].getItemId()%></td>
					<td align="left"><%=entries[i].getItemNumber() + " / " + entries[i].getItemName() %></td>
					<td align="center"><%=entries[i].getOpenQty()%></td>
					<td align="center"><%=entries[i].getPlusQty()%></td>
					<td align="center"><%=entries[i].getMinusQty()%></td>
					<td align="center"><%=entries[i].getOpenQty() + entries[i].getPlusQty() - entries[i].getMinusQty()%></td>
					<td align="center"><input type="button" value="Details" onclick="fnGetStockDetailsByItem(<%=entries[i].getItemId()%>);"/></td>
				</tr>
				<%
						}
				%>
<!-- 				<tr> -->
<!-- 					<td colspan="5" align="center"><input type="button" value="Print" onclick="fnPrintLedger();"/>&nbsp;&nbsp;<input type="button" value="Export" onclick="fnExportLedger();"/>&nbsp;&nbsp;<input type="button" value="Email" onclick="fnEmailLedger();"/></td> -->
<!-- 				</tr> -->
			</table>
			<%	
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtId"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>