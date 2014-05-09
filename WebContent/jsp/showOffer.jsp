<%@page import="com.en.util.StringUtility"%>
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.en.model.OfferModel"%>
<%@page import="com.en.util.Constant"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Offer Details</title>
<link rel="stylesheet" type="text/css" href="css/common.css" />

	<script type="text/javascript">
		var mainObj = new Array();
		var offerObj;
		var tempObj = new Array();
<%
	double total = 0;
	OfferModel[] offers = (request.getAttribute(Constant.OFFER_ITEMS) != null) ? (OfferModel[]) request.getAttribute(Constant.OFFER_ITEMS) : new OfferModel[0];
	for(int i=0; i<offers.length; i++){
		total = 0;
		OfferItemModel[] items = (OfferItemModel[])offers[i].getOfferItems().toArray(new OfferItemModel[0]);
%>
		offerObj = new Object();
		offerObj.offerName = "<%=offers[i].getOfferName()%>";
		offerObj.packing = "<%=offers[i].getPacking()%>";
		offerObj.forwarding = "<%=offers[i].getForwarding()%>";
		offerObj.installation = "<%=offers[i].getInstallation()%>";
		offerObj.offerPrice = "<%=offers[i].getOfferPrice()%>";
		tempObj = new Array();
<%
		for(int k=0; k<items.length; k++){
			total = total + Double.parseDouble(items[k].getItem().getItemPrice()) * items[k].getQty();
%>
		var obj = new Object();
		obj.id = <%=items[k].getItem().getItemId()%>;
		obj.name = '<%=items[k].getItem().getItemNumber()%>';
		obj.qty = <%=items[k].getQty()%>;
		obj.price = <%=items[k].getItem().getItemPrice()%>;
		tempObj.push(obj);
<%
		}
%>
offerObj.total = <%=total%>;
offerObj.items = tempObj;
mainObj.push(offerObj);
<%
	}
%>
</script>
</head>
<body>
<form method="post" name="frmAddOffer">
	<div class="loginClass" style="min-width: 625px;">
		<fieldset>
			<legend class="screenHeader">Offer List</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table border="1" width="100%">
				<tr>
					<th>&nbsp;</th>
					<th>OFFER NAME</th>
					<th>OFFER PRICE</th>
					<th>OFFER ITEMS</th>
					<th>OTHER CHARGES</th>
					<th>OFFER VALIDITY</th>
					<th>OFFER QUANTITY</th>
				</tr>
				<%
					for(int i=0; i<offers.length; i++){
						OfferItemModel[] items = (OfferItemModel[])offers[i].getOfferItems().toArray(new OfferItemModel[0]);
				%> 
				<tr>
					<td align="center"><input id="v<%=i%>" type="radio" name="sRow" onchange="fnSelectOffer(<%=i%>);"/></td>
					<td align="center"><%=offers[i].getOfferName() %></td>
					<td align="center"><%=offers[i].getOfferPrice() %></td>
					<td><%=offers[i].getOfferDesc() %></td>
					<td><%=(offers[i].getPacking() != 0) ? "PACKING : "+Utils.get2Decimal(offers[i].getPacking())+"<br/>" : ""  %>
						<%=(offers[i].getForwarding() != 0) ? "FORWARDING : "+Utils.get2Decimal(offers[i].getForwarding())+"<br/>" : ""  %>
						<%=(offers[i].getInstallation() != 0) ? "INSTALLATION : "+Utils.get2Decimal(offers[i].getInstallation())+"<br/>" : ""  %></td>
					<td align="center">From <%=Utils.convertToAppDateDDMMYY(offers[i].getFromDate())%> To <%=Utils.convertToAppDateDDMMYY(offers[i].getToDate())%></td>
					<td align="center"><input disabled="disabled" type="text" name="qty<%=i%>" id="qty<%=i%>" value="0" style="text-align: right; width: 100px;" onkeypress="return numbersonly(this, event, true)"/></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th colspan="7"><input type="button" value="Add" name="btnAdd" disabled="disabled" onclick="fnAddItems();"/>&nbsp;&nbsp;<input type="button" value="Close" onclick="window.close();"/></th>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtOfferId"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript">
var count = <%=offers.length%>;
var data, txtBox;
function fnSelectOffer(idx){
	document.forms[0].btnAdd.disabled = false;
	for(var i=0; i<count; i++){
		document.getElementById("qty"+i).value = 0;
		document.getElementById("qty"+i).disabled = true;
	}
	document.getElementById("qty"+idx).value = '';
	document.getElementById("qty"+idx).disabled = false;
	txtBox = document.getElementById("qty"+idx);
	document.getElementById("qty"+idx).focus();
	data = mainObj[idx];
}

function fnAddItems(){
	var offerQty = parseFloat(txtBox.value);
	var desc = data.offerName;
	var offerPrice = parseFloat(data.offerPrice);
	var offerTotal = parseFloat(data.total);
	var itemPrice = 0;
	for(var i=0; i<data.items.length;i++){
		itemPrice = ((offerPrice * (parseFloat(data.items[i].price)*parseFloat(data.items[i].qty)) / offerTotal) / parseFloat(data.items[i].qty)).toFixed(2);
		window.opener.fnAddItemRow(data.items[i].id,data.items[i].name,offerQty * data.items[i].qty, itemPrice, desc);
	}
	window.opener.fnAddPacking(offerQty*parseFloat(data.packing));
	window.opener.fnAddForwarding(offerQty*parseFloat(data.packing));
	window.opener.fnAddInstallation(offerQty*parseFloat(data.packing));
	window.opener.fnRefreshTotal();
	window.close();
}
</script>
</html>