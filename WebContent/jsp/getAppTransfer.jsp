<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
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
<script type="text/javascript">
function fnApprove(transferId){
	if(document.forms[0].txtAppDate.value == ''){
		alert('Please select the Received date before approving.');
		return;
	}
	var r=confirm("I \"<%=(String)(((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName())%>\", hereby confirm that the above invoice goods  I have received and checked and I approve the same.");
	if (r==true) {
		document.forms[0].txtInvoiceNo.value = transferId;
		document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
		document.forms[0].ACTIONS.value = "APP_TRANSFER";
		fnFinalSubmit();
	}
}

function fnBack(){
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_APP_TRANSFER";
	fnFinalSubmit();
}

function printSalesBill(billNo, copyType){
	window.open('/en/app?HANDLERS=TRANSFER_HANDLER&ACTIONS=PRNT_TRANSFER&billNo='+billNo+'&copies='+copyType,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Stock Transfer Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 width="90%" align="center" cellspacing="0">
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
					<td align="left">Approved By : <b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getToApprover().getUserName() %></b></td>
					<td align="left">Received Date : <b><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getToAppDate() %></b></td>
				</tr>
			</table>
			<table width="90%" border="1" cellspacing="0" id="enqItem" align="center" style="margin-top: 10px;">
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
			</table>
			<table width="100%">
				<tr>
					<td align="center"><%if(!((TransferModel) request.getAttribute(Constant.FORM_DATA)).isToApproved()) { %>Received Date : &nbsp;&nbsp;<input name="txtAppDate" id="txtAppDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtAppDate'),event);" 
					value=""><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtAppDate'),event);" /> &nbsp;&nbsp;
						<input type="button" value="Approve" onclick="fnApprove('<%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getBillPrefix()+"ST"+((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferid()%>')" <%=(((TransferModel) request.getAttribute(Constant.FORM_DATA)).isToApproved()) ? "disabled = \"disabled\"" : "" %>/><%} else { %><input type="button" <%=(!((TransferModel) request.getAttribute(Constant.FORM_DATA)).isToApproved()) ? "disabled = \"disabled\"" : "" %> value="Print Branch Copy" onclick="printSalesBill('<%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getBillPrefix()+"ST"+((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferid()%>',1)" /><%} %>&nbsp;&nbsp;<input type="button" value="Go Back" onclick="fnBack()"/></td> 
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
<%if(!((TransferModel) request.getAttribute(Constant.FORM_DATA)).isToApproved()) { %>
<script type="text/javascript" src="js/dateUtil.js"></script>
<%} %>
</html>