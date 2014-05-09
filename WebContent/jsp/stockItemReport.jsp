<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.StockItemModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div align="center" id="FreezePane" class="FreezePaneOn"></div>
	<div class="loginClass" style="min-width: 625px;">
		<fieldset>
			<legend class="screenHeader">Item Transaction Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 600px;" align="center">
				<tr>
					<td  style="width: 120px; padding-left: 20px;" align="right">ITEM</td>
					<td><b><%=(((ItemModel)request.getAttribute(Constant.ITEMS)).getItemNumber())+" / "+(((ItemModel)request.getAttribute(Constant.ITEMS)).getItemName())%></b></td>
				</tr>
				<tr>
					<td  style="width: 120px; padding-left: 20px;" align="right">BRANCH</td>
					<td><b><%=(((AccessPointModel)request.getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessName())+"  - "+(((AccessPointModel)request.getAttribute(Constant.ACCESS_POINT_DTLS)).getCity())%></b></td>
				</tr>
			</table>
			<%
				StockItemModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (StockItemModel[]) request.getAttribute(Constant.FORM_DATA) : new StockItemModel[0];
			%>
			<table border="1" cellspacing="2" cellpadding="5" style="font-size: 11px; width: 600px;" align="center">
				<tr>
					<th style="width: 60px;">DATE</th>
					<th>PARTICULARS</th>
					<th style="width: 80px;">INWARDS</th>
					<th style="width: 80px;">OUTWARDS</th>
					<th style="width: 80px;">NETT</th>
				</tr>
					<tr>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(todayBills[0].getFromDate())%></td>
						<td align="right"><%=todayBills[0].getDesc()%></td>
						<td align="right">&nbsp;</td>
						<td align="right">&nbsp;</td>
						<td align="center"><%=todayBills[0].getOpenQty()%></td>
					</tr>
				<%
					double plusTotal = 0;
					double minusTotal = 0;
					double nett = todayBills[0].getOpenQty();
					for(int i=1; i<todayBills.length-1;i++){
						plusTotal = plusTotal + todayBills[i].getPlusQty();
						minusTotal = minusTotal + todayBills[i].getMinusQty();
						nett = nett + todayBills[i].getPlusQty() - todayBills[i].getMinusQty();
				%>
					<tr>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getFromDate())%></td>
						<td align="left"><%=todayBills[i].getDesc()%></td>
						<td align="center"><%=todayBills[i].getPlusQty() == 0 ? "--" : todayBills[i].getPlusQty()%></td>
						<td align="center"><%=todayBills[i].getMinusQty() == 0 ? "--" : todayBills[i].getMinusQty()%></td>
						<td align="center"><%=nett%></td>
					</tr>
				<%
					}
				%>
					<tr>
						<td align="center"><%=Utils.convertToAppDateDDMMYY(todayBills[todayBills.length-1].getFromDate())%></td>
						<td align="right"><%=todayBills[todayBills.length-1].getDesc()%></td>
						<td align="center"><%=plusTotal%></td>
						<td align="center"><%=minusTotal%></td>
						<td align="center"><%=todayBills[todayBills.length-1].getOpenQty()%></td>
					</tr>
					<tr>
						<td align="center" colspan="5"><input type="button" value="Close" onclick="window.close();"/><input type="button" value="Print" onclick="window.print();"></td>
					</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript">
function fnShowSales(id){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowPurchase(id){
	window.open('/en/app?HANDLERS=PURCHASE_REPORT_HANDLER&ACTIONS=GET_PURCHASE&billNo='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowTransfer(id){
	window.open('/en/app?HANDLERS=TRANSFER_REPORT_HANDLER&ACTIONS=GET_TRANSFER&billNo='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowPurchaseReturn(id){
	window.open('/en/app?HANDLERS=PURCHASE_RETURN_REPORT_HANDLER&ACTIONS=GET_PURCHASE_RETURN&billNo='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowCreditNote(id){
	window.open('/en/app?HANDLERS=CREDIT_NOTE_HANDLER&ACTIONS=VIEW_CREDIT_NOTE&txtInvoiceNo='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
</script>
</html>