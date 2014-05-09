<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.OrderModel"%>
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
<script type="text/javascript" src="js/order.js"></script>
<script type="text/javascript" src="js/dateUtil.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Order</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="2" style="width: 950px;">
				<tr>
					<td align="right">Branch State :</td>
					<td colspan="5">
						<b><label style="background: aqua; color: brown; padding: 2px;" id="branchState"></label></b>
						&nbsp;&nbsp;&nbsp;Customer State : <b><label style="background: yellow; color: red; padding: 2px;" id="custState"></label></b>&nbsp;&nbsp;&nbsp;Customer Type : <b><label style="background: yellow; color: red; padding: 2px;" id="custType"></label></b> 
					</td>
				</tr>
				<tr>
					<td align="right">Access Point :</td>
					<td>
						<select name="sAccessPoint" style="width: 200px;" onchange="fnChangeAP(this);">
							<option value="--">-----</option>
							<%
								AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
								AccessPointModel temp = null;
								for(int i=0; i<accessPoints.length; i++){
									temp = accessPoints[i];
							%>
							<option id="<%=temp.getState()%>" value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
					
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td colspan="5">
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerName()+" - "+ ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getArea()+" - "+((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCity() : "" %>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td align="right">Tax Type<span style="color: red">*</span> :</td>
					<td>
						<input type="radio" value="1" name="rTaxType" id="rTaxType1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((OrderModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label for="rTaxType1" style="padding: 2px; color: red; background-color: yellow;">VAT</label>
						<input type="radio" value="2" name="rTaxType" id="rTaxType2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((OrderModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) ? "checked=\"checked\"" : "" %>/><label for="rTaxType2" style="padding: 2px; color: white; background-color: black;">CST</label>
						<input type="radio" value="3" name="rTaxType" id="rTaxType3" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((OrderModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 3) ? "checked=\"checked\"" : "" %>/><label for="rTaxType3" style="padding: 2px; color: orange; background-color: brown;">CST Against from 'C'</label>
					</td>
					<td align="right">Salesman<span style="color: red">*</span> :</td>
					<td colspan="3">
						<select  name="sUser" style="width: 200px;" <%=(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) ? "disabled=\"disabled\"" : ""%>>
							<option value="--">-----</option>
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((OrderModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Priority<span style="color: red">*</span> :</td>
					<td>
						<select  name="sPriority" style="width: 200px;">
							<option value="--">-----</option>
							<option value="0" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPriority() == 0) ? "selected=\"selected\"" : "" %>>URGENT</option>
							<option value="1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPriority() == 1) ? "selected=\"selected\"" : "" %>>NORMAL</option>
						</select>
					</td>
					<td align="right">Delivery Days<span style="color: red">*</span> :</td>
					<td><input type="text" name="txtDevDays" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((OrderModel)request.getAttribute(Constant.FORM_DATA)).getDevDays() : "" %>" style="width: 100px;" maxlength="50"/></td>
					<td align="right">Order Form No<span style="color: red">*</span> :</td>
					<td><input type="text" name="txtFormNo" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((OrderModel)request.getAttribute(Constant.FORM_DATA)).getFormNo() : "" %>" style="width: 100px;" maxlength="50"/></td>
				</tr>
			</table>
			<table style="width: 950px;">
				<tr><td style="padding-left: 5px;" colspan="4"> Item Name: <br/>
					<select name="sItemName" onchange="fnChangeItemNo(this);">
						<option value="--">-----</option>
						<%							
							String initPrice = "";
							ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
													(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];
							initPrice = (items.length > 0) ? items[0].getItemPrice() : "0"; 
							for(int i=0; i<items.length;i++){
						%>
							<option id="<%=items[i].getItemPrice() %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" value="<%=items[i].getItemId()%>"><%=items[i].getItemName()+" - "+items[i].getItemNumber()%></option>
						<%
							}
						%>
					</select></td>
					<td style="padding-left: 2px;"> Item No.: <br/>
					<select name="sItem"  onchange="fnChangeItemName(this);">
						<option value="--">-----</option>
						<%							
							for(int i=0; i<items.length;i++){
						%>
							<option id="<%=items[i].getItemPrice() %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" value="<%=items[i].getItemId()%>"><%=items[i].getItemNumber()%></option>
						<%
							}
						%>
					</select></td>
					<td style="padding-left: 2px;">Description : <br/><input type="text" name="txtDesc" style="width: 100px;" maxlength="50"/></td>
					<td style="padding-left: 2px;">Rate : <br/><input type="text" name="txtRate" style="width: 75px;" value="" onkeypress="return numbersonly(this, event, true)"/></td>
					<td style="padding-left: 2px;">Dis. % : <br/><input type="text" name="txtDiscount" style="width: 50px;" value="0" onkeypress="return numbersonly(this, event, true)"/></td>
					<td style="padding-left: 2px;">Qty : <br/><input type="text" name="txtQuantity" style="width: 50px;" value="" onkeypress="return numbersonly(this, event, true)"/></td>
					<td align="center" valign="bottom"><input type="button" value="Add" onclick="fnAddItem();"/></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" style="margin-left : 10px; margin-top : 10px; width: 950px;">
				<tr>
					<td align="center"><b>Action</b></td>
					<td align="center"><b>Item Id</b></td>
					<td align="center" width="30%"><b>Item No</b></td>
					<td align="center" width="15%"><b>Desc</b></td>
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Discount</b></td>
					<td align="center" style="width: 120px;"><b>Amount</b></td>
				</tr>
				<%
					SalesItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesItemModel[]) (((OrderModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new SalesItemModel[0]))) : new SalesItemModel[0];
					for(int i=0; i<salesItems.length; i++){
				%>
				<tr id=s<%=i%>>
					<td align="center"><input type="button" value="Delete" onclick="fnDeleteItem('s<%=i%>');" /></td>
					<td align="center"><%=salesItems[i].getItem().getItemId() %></td>
					<td align="center"><%=salesItems[i].getItem().getItemNumber() %></td>
					<td align="center"><%=salesItems[i].getDesc() %></td>
					<td align="center"><%=salesItems[i].getQty()%></td>
					<td align="center"><%=salesItems[i].getRate()%></td>
					<td align="center"><%=salesItems[i].getDisrate()%></td>
					<td align="right" style="width: 120px;"><%=((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate()%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="margin-top : 10px; margin-left : 10px; width: 950px;" cellspacing="3" cellpadding="0">
				<tr>
					<td rowspan="3" width="9%" valign="top" style="padding-top: 3px;" align="right">Billing Instruction :</td>
					<td rowspan="3" valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 550px; height : 60px;" maxlength="300"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() : "" %></textarea></td>
					<td align="right">Installation Charges (+) :</td>
					<td  style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtInstall" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getInstallation() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true);"></td>
				</tr>
				<tr>
					<td align="right">Packing Charges (+) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtPacking" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPacking() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Forwarding Charges (+) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtForwarding" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getForwarding() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td rowspan="2" width="9%" valign="top" style="padding-top: 3px;" align="right">Delivery Address :</td>
					<td rowspan="2" valign="top" style="padding-top: 3px;"><textarea  name="txtDevAdd" style="width: 550px; height : 60px;" maxlength="300"></textarea></td>
					<td align="right" valign="top">LUMSUM Less (-) :</td>
					<td style="width: 120px;" valign="top"><input type="text" style="width: 110px; text-align: right;" name="txtLess" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getLess() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Grand Total (Without Taxes) :</td>
					<td style="width: 120px;" align="right"><label id="gTotal" style="padding-right: 10px; font-weight: bolder; font-size: 18px;">0.00</label></td>
				</tr>
				<tr>
					<td colspan="4" >
						<fieldset>
							<legend>Payment Terms</legend>
							<table width="100%">
								<tr>
									<td><input onclick="fnSelectedType(0)" type="radio" name="rPayType" checked="checked" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPayType() == 0) ? "checked=\"checked\"" : "" %> value="0" id="rPayType1"/> <label for="rPayType1">Cash</label>
									&nbsp;&nbsp;&nbsp;<input type="radio" onclick="fnSelectedType(1)" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPayType() == 1) ? "checked=\"checked\"" : "" %> name="rPayType" value="1" id="rPayType2"/> <label for="rPayType2">Credit</label>
									&nbsp;&nbsp;&nbsp;<input type="radio"  onclick="fnSelectedType(2)" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPayType() == 2) ? "checked=\"checked\"" : "" %> name="rPayType" value="2" id="rPayType3"/> <label for="rPayType3">EMI</label>
									<div id="divCredit" style="display: <%=(request.getAttribute(Constant.FORM_DATA) != null && ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPayType() == 1) ? "block" : "none"%>;">
										&nbsp;&nbsp;Credit Days : <input type="text" style="width: 50px; text-align: right;" name="txtCredDays" onkeypress="return numbersonly(this, event, true)" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCredDays() : "" %>">
									</div>
									<div id="divEMI" style="display: <%=(request.getAttribute(Constant.FORM_DATA) != null && ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getPayType() == 2) ? "block" : "none"%>;">
										&nbsp;&nbsp;DP : <input type="text" style="width: 100px; text-align: right;" name="txtDownPay" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getDownPay() : "" %>" onkeypress="return numbersonly(this, event, true)">
										&nbsp;&nbsp;Nos. : <input type="text" style="width: 50px; text-align: right;" name="txtEMINo" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getEMINo() : "" %>" onkeypress="return numbersonly(this, event, true)">
										&nbsp;&nbsp;Amt : <input type="text" style="width: 100px; text-align: right;" name="txtEMIAmt" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getEMIAmt() : "" %>" onkeypress="return numbersonly(this, event, true)">
										&nbsp;&nbsp;Days : <input type="text" style="width: 50px; text-align: right;" name="txtEMIDays" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((OrderModel)request.getAttribute(Constant.FORM_DATA)).getEMIDays() : "" %>" onkeypress="return numbersonly(this, event, true)"></div>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Generate Bill" onclick="fnGenerateExhibitionBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId() : "" %>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtSalesItems"/>
	<input type="hidden" name="quotationId" value="<%=(request.getAttribute("QUOTATIONNO") != null) ? (String) request.getAttribute("QUOTATIONNO") : ""%>">
</form>
<script type="text/javascript">
<%if(request.getAttribute(Constant.FORM_DATA) != null) {%>
fnRefreshTotal();
fnChangeCustomer('<%=((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getState().toUpperCase()+"!"+((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerType()+"<"+((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin() +">"+((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst()+"~"+((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getContactPerson()+"|"+((OrderModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getMobile1()%>');
<%}%>

function fnChangeAP(ap){
	document.getElementById("branchState").innerHTML = ap.options[ap.selectedIndex].id;
}
</script>
</body>
</html>