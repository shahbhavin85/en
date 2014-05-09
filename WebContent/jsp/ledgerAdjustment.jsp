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
			<legend class="screenHeader">Ledger Adjustment Entries</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerName()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCity()	 : ""%>" re style="width: 500px;"/>
					</td>
				</tr>
				<tr>
					<td align="right">Ajdustment Type<span style="color: red">*</span> :</td>
					<td><select  name="sType" style="width: 505px;" disabled="disabled">
							<option value="--">-----</option>
							<option value="1" <%=(request.getAttribute("sType") != null && ((String)request.getAttribute("sType")).equals("1")) ? "selected=\"selected\"" : "" %>>ORDER ADVANCE TO SALES BILL</option>
						</select>
					</td>
				</tr>
			</table>
			<table style="width: 950px;" border="1" id="billTable">
				<tr>
					<th style="width: 50px;">Select</th>
					<th>Order No</th>
					<th width="70%">Customer</th> 
					<th>Pending AdvanceAmt</th>
				</tr>
				<%
				EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? (EntryModel[]) request.getAttribute(Constant.FORM_DATA) : new EntryModel[0];
				for(int i=0; i<entries.length; i++){
				%>
				<tr>
					<td align="center"><input name="rNo" value="<%=entries[i].getBillBranch().getBillPrefix()+"O"+Utils.padBillNo(entries[i].getBillNo())%>~<%=Utils.get2Decimal(entries[i].getAmount())%>" onchange="fnSelected();" type="radio"></td>
					<td align="center"><%=entries[i].getBillBranch().getBillPrefix()+"O"+Utils.padBillNo(entries[i].getBillNo()) %></td>
					<td align="left"><%=entries[i].getCustomer().getLabel() %></td>
					<td align="right"><%=Utils.get2Decimal(entries[i].getAmount()) %></td>
				</tr>
				<%
					}
				%>
				<tr>
					<td colspan="4" align="center"><input type="button" name="btnNext" value="Next" disabled="disabled" onclick="fnNext();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerId() : ""%>">
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>