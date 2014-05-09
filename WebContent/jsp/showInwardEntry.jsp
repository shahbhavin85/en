<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.InwardEntryItemModel"%>
<%@page import="com.en.model.InwardEntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.InwardEntryModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.CustomerGroupModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/inwardEntry.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="adminMenu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Inward Entry Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Entry No</td>
					<td colspan="3"><b><%=((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryNo() %></b></td>
				</tr>
				<tr>
					<td align="right">BE No</td>
					<td><b><%=((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getBENo()%></b></td>
					<td align="right">Entry Date</td>
					<td><b><%=((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryDate()%></b></td>
				</tr>
				<tr>
					<td align="right">Total CBM</td>
					<td><b><%=Utils.get2Decimal(((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getCbm())%></b></td>
					<td align="right">Total CBM</td>
					<td><b><%=Utils.get2Decimal(((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getExchangeRate())%></b></td>
				</tr>
				<tr>
					<td align="right">China Expenses</td>
					<td><b><%=Utils.get2Decimal(((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getSourceExp())%></b></td>
					<td align="right">India Expenses</td>
					<td><b><%=Utils.get2Decimal(((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getDestinationExp())%></b></td>
				</tr>
				<tr>
					<td align="right">Remarks</td><td colspan="5"><b><%=((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() %></b></td>
				</tr>
			</table>				
			<br/>
			<center>
			<table border="1" cellspacing="0" align="center" id="enqItem" style="width: 950px;">
				<tr>
					<td align="center"><b>Item Id</b></td>
					<td align="center" width="25%"><b>Item No</b></td>
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Total Duty</b></td>
					<td align="center"><b>CBM</b></td>
					<td align="center"><b>China Exp</b></td>
					<td align="center"><b>India Exp</b></td>
					<td align="center" style="width: 120px;"><b>Cost Price</b></td>
				</tr>
				<%
					double total = 0;
					InwardEntryItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? (((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getItems()) : new InwardEntryItemModel[0];
					for(int i=0; i<salesItems.length; i++){
						InwardEntryModel model = (InwardEntryModel)request.getAttribute(Constant.FORM_DATA);
						double temp = ((model.getCbm() == 0) ? 0 :(((model.getDestinationExp()+(model.getExchangeRate()*model.getSourceExp()))/model.getCbm())*salesItems[i].getCbm()));
						total = total + Utils.get2DecimalDouble(salesItems[i].getRate()+((salesItems[i].getDuty()+temp)/salesItems[i].getQty()));
				%>
				<tr id=s<%=i%>>
					<td align="center"><%=salesItems[i].getItem().getItemId() %></td>
					<td align="center"><%=salesItems[i].getItem().getItemNumber() %></td>
					<td align="center"><%=salesItems[i].getQty()%></td>
					<td align="center"><%=salesItems[i].getRate()%></td>
					<td align="center"><%=salesItems[i].getDuty()%></td>
					<td align="center"><%=salesItems[i].getCbm()%></td>
					<td align="center"><%=Utils.get2Decimal(((model.getCbm() == 0) ? 0 : (model.getExchangeRate()*model.getSourceExp()/model.getCbm()))*salesItems[i].getCbm())%></td>
					<td align="center"><%=Utils.get2Decimal((((model.getCbm() == 0) ? 0 : model.getDestinationExp()/model.getCbm()))*salesItems[i].getCbm())%></td>
					<td align="center"><%=Utils.get2Decimal(salesItems[i].getRate()+((salesItems[i].getDuty()+temp)/salesItems[i].getQty()))%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th align="right" colspan="8">GRAND TOTAL</th>
					<th style="width:65px;" align="center"><%=Utils.get2Decimal(total)%></th>
				</tr>
			</table>
			</center>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Edit Bill"  onclick="editEntry('<%=((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryNo()%>')" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="txtInvoiceNo"/>
</form>

</body>
</html>