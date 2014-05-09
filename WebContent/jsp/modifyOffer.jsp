<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
			<legend class="screenHeader">Modify Offer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Offer Id <span style="color: red">*</span> :</td>
					<td><input name="txtOfferId" style="width: 50px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getOfferid() : ""%>" tabindex="4">
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Offer Details" onclick="fnGetOfferDetails();"/></td>
				</tr>
				<tr>
					<td align="right">Offer Name <span style="color: red">*</span> :</td>
					<td><input name="txtOfferName" style="width: 195px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getOfferName() : ""%>" tabindex="4"></td>
				</tr>
				<tr>
					<td align="right">Offer Price <span style="color: red">*</span> :</td>
					<td><input name="txtOfferPrice" style="width: 195px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getOfferPrice() : ""%>" tabindex="4"> Rs.</td>
				</tr>
				<tr>
					<td align="right">From Date :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getAppFromDate() : ""%>" tabindex="6"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" /></td>
				</tr>
				<tr>
					<td align="right">To Date :</td>
					<td><input name="txtToDate" id="txtToDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getAppToDate() : ""%>" tabindex="6"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" /></td>
				</tr>
				<tr>
					<td align="right">Installation Charges :</td>
					<td><input type="text" style="width: 110px; text-align: right;" name="txtInstall" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getInstallation() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true);"></td>
				</tr>
				<tr>
					<td align="right">Packing Charges :</td>
					<td><input type="text" style="width: 110px; text-align: right;" name="txtPacking" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getPacking() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Forwarding Charges :</td>
					<td><input type="text" style="width: 110px; text-align: right;" name="txtForwarding" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getForwarding() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td colspan="2">
					<table width="100%" border="1">
						<tr><td colspan="5"><b>Items Offered</b></td></tr>
						<tr><td colspan="2"> Item : 
							<select name="sItem1" style="width: 200px;">
								<%					
									ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
											(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];		
									for(int i=0; i<items.length;i++){
								%>
									<option value="<%=items[i].getItemId()%>"><%=items[i].getItemNumber()%></option>
								<%
									}
								%>
							</select></td>
							<td colspan="2">Numbers : <input type="text" name="txtQuantity" style="width: 100px;"/></td>
							<td><input type="button" value="Add" onclick="fnAddItem();"/></td>
						</tr>
					</table>
					<table width="100%" border="1" id="enqItem">
						<tr>
							<td align="center"><b>Item Id</b></td>
							<td align="center" width="60%"><b>Item No</b></td>
							<td align="center"><b>Quantity</b></td>
							<td align="center"><b>Action</b></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnModifyOffer();"  tabindex="8"
						value="Modify">&nbsp;&nbsp;<input type="button" onclick="fnGoToList();"  tabindex="8"
						value="Offer List"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtSelectedOfferId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getOfferid() : ""%>"/>
	<input type="hidden" name="txtOfferItems"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">

<%
	if((request.getAttribute(Constant.FORM_DATA) != null)){
		ArrayList itemList = ((OfferModel)request.getAttribute(Constant.FORM_DATA)).getOfferItems();
		Iterator itemItr = itemList.iterator();
		while(itemItr.hasNext()){
			OfferItemModel temp = (OfferItemModel)itemItr.next();
%>
insertRow('<%=temp.getItem().getItemId()%>','<%=temp.getItem().getItemNumber()%>','<%=temp.getQty()%>');
<%
		}
	}
%>
</script>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>