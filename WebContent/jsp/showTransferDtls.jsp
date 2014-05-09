<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.TransferItemModel"%>
<%@page import="com.en.model.TransferModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.SalesModel"%>
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
<script type="text/javascript" src="js/transfer1.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Stock Transfer Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Transfer No</td>
					<td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getBillPrefix()+"ST"+((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferid() %></b></td>
					<td align="right">Date</td>
					<td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferdate()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">From</td>
					<td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getAccessName() %></b></td>
					<td align="right">To</td>
					<td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getToBranch().getAccessName()%></b></td>
				</tr>
				<tr>
					<td align="right">Despatched By</td><td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransport() %></b></td>
					<td align="right">LR No / Dt</td><td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getLrno()+" / "+((TransferModel)request.getAttribute(Constant.FORM_DATA)).getLrdt() %></b></td>
				</tr>
				<tr>
					<td align="right">Approved By : </td><td><b> <%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getToApprover().getUserName() %></b></td>
					<td align="right">Received Date : </td><td><b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getToAppDate() %></b></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="margin-top: 10px; width: 950px;">
				<tr>
					<th  style="width:50px;">S No.</th>
					<th >Description of Goods</th>
					<th style="width:50px;">Qty</th>
					<th style="width:50px;">Rate<br/>(Rs.)</th>
					<th style="width:65px;">Amount<br/>(Rs.)</th>
				</tr>
				<%
					double total = 0;
					double qty = 0;
					TransferItemModel[] items = (request.getAttribute(Constant.FORM_DATA) != null) ? ((TransferItemModel[]) (((TransferModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new TransferItemModel[0]))) : new TransferItemModel[0];
					for(int i=0; i<items.length; i++){
						
				%>
				<tr align="center">
					<td align="center" style="width:50px;"><%=(i+1)%></td>
					<td align="left"><%=items[i].getItem().getItemNumber() + " / "+ items[i].getItem().getItemName() + items[i].getDesc()%></td>
					<td style="width:50px;"><%=items[i].getQty()%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(items[i].getRate())%></td>
					<td style="width:65px;"><%=Utils.get2Decimal(items[i].getQty() * items[i].getRate())%></td>
				</tr>
				<%
						qty = qty + (items[i].getQty());
						total = total + (items[i].getQty() * (items[i].getRate()));
					}
				%>
				<tr>
					<th align="right" colspan="2">GRAND TOTAL</th>
					<th style="width:50px;"><%=qty%></th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;"><%=Utils.get2Decimal(total)%></th>
				</tr>
				<tr>
					<td colspan="5" align="center"><input type="button" value="Close" onclick="window.close();"/></td>
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