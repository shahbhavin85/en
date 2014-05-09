<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
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
<script type="text/javascript" src="js/offer.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddOffer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Offer List</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table border="1" width="100%">
				<tr>
					<th>Offer Id</th>
					<th>Offer Name</th>
					<th>Offer Price</th>
					<th>Offer Items</th>
					<th>Other Charges</th>
					<th>Offer Validity</th>
					<th>ACTION</th>
				</tr>
				<%
					OfferModel[] offers = (request.getAttribute(Constant.FORM_DATA) != null) ? (OfferModel[]) request.getAttribute(Constant.FORM_DATA) : new OfferModel[0];
					for(int i=0; i<offers.length; i++){
				%>
				<tr>
					<td align="center"><%=offers[i].getOfferid() %></td>
					<td align="center"><%=offers[i].getOfferName() %></td>
					<td align="center"><%=offers[i].getOfferPrice() %></td>
					<td><%=offers[i].getOfferDesc() %></td>
					<td><%=(offers[i].getPacking() != 0) ? "PACKING : "+Utils.get2Decimal(offers[i].getPacking())+"<br/>" : ""  %>
						<%=(offers[i].getForwarding() != 0) ? "FORWARDING : "+Utils.get2Decimal(offers[i].getForwarding())+"<br/>" : ""  %>
						<%=(offers[i].getInstallation() != 0) ? "INSTALLATION : "+Utils.get2Decimal(offers[i].getInstallation())+"<br/>" : ""  %></td>
					<td align="center">From <%=Utils.convertToAppDateDDMMYY(offers[i].getFromDate())%> To <%=Utils.convertToAppDateDDMMYY(offers[i].getToDate())%></td>
					<td align="center"><input type="button" value="EDIT" onclick="fnEdit(<%=offers[i].getOfferid()%>)"/></td>
				</tr>
				<%
					}
				%>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtOfferId"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
function fnEdit(id){
	document.forms[0].txtOfferId.value = id;
	document.forms[0].HANDLERS.value = 'OFFER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_OFFER';
	fnFinalSubmit();
}
</script>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>