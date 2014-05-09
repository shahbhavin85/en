<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.EntryModel"%>
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
<script type="text/javascript" src="js/ledgerAdjustment.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View Ledger Adjustment Entries</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerName()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCity()	 : ""%>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" <%=(request.getAttribute(Constant.CUSTOMER) == null) ? "style=\"visibility: hidden;\"" : "" %>/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Details" onclick="fnGetAdjustDetails();"/></td>
				</tr>
			</table>
			<%
				EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? (EntryModel[])request.getAttribute(Constant.FORM_DATA) : new EntryModel[0];
				if(entries.length>0){
			%>
				<table border="1" style="width: 950px;">
					<tr>
						<th>Type</th>
						<th>Date</th>
						<th>Description</th>
						<th>Amount</th>
						<th>Adjust</th>
						<th>Action</th>
					</tr>
			<%
					for(int i=0; i<entries.length; i++){
			%>
					<tr>
						<td align="center"><%=Utils.getAdjustEntryType(entries[i].getEntryType())%></td>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(entries[i].getEntryDate()) %></td>
						<td align="center"><%=entries[i].getRemark()%></td>
						<td align="center"><%=Utils.get2Decimal(entries[i].getAmount()) %></td>
						<td align="center"><%=Utils.get2Decimal(entries[i].getAdjAmt()) %></td>
						<td align="center"><input type="button" value="DELETE" onclick="fnDeleteEntry(<%=entries[i].getEntryType()%>,<%=entries[i].getEntryId()%>)"/></td>
					</tr>
			<%
					}
			%>
				</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerId() : ""%>">
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="idx"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>