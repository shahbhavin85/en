<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.QuotationModel"%>
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
<script type="text/javascript" src="js/quotation.js"></script>
<script type="text/javascript" src="js/dateUtil.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Quotation</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="2" style="width: 950px;">
				<tr>
					<td align="right">Branch State :</td>
					<td colspan="3">
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
					<td colspan="3">
						<input type="text" name="txtCust" autocomplete="off" value="" autocomplete="off" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td align="right">Tax Type<span style="color: red">*</span> :</td>
					<td colspan="2">
						<input type="radio" value="1" name="rTaxType" id="rTaxType1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label for="rTaxType1" style="padding: 2px; color: red; background-color: yellow;">VAT</label>
						<input type="radio" value="2" name="rTaxType" id="rTaxType2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) ? "checked=\"checked\"" : "" %>/><label for="rTaxType2" style="padding: 2px; color: white; background-color: black;">CST</label>
						<input type="radio" value="3" name="rTaxType" id="rTaxType3" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 3) ? "checked=\"checked\"" : "" %>/><label for="rTaxType3" style="padding: 2px; color: orange; background-color: brown;">CST Against from 'C'</label>
					</td>
					<td>Samples<span style="color: red">*</span> :&nbsp;
						<input type="radio" value="1" name="rSample" id="rSample1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getSample() == 1) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label for="rSample1" style="padding: 2px; color: red; background-color: yellow;">NO</label>
						<input type="radio" value="2" name="rSample" id="rSample2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getSample() == 2) ? "checked=\"checked\"" : "" %>/><label for="rSample2" style="padding: 2px; color: white; background-color: black;">YES</label>
					</td>
				</tr>
				<tr>
					<td align="right">Valid Date :</td>
					<td>
					<%
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						Date validDt = DateUtil.addDays(new Date(), 30);
					%>
					<input name="txtValidDt" id="txtValidDt" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtValidDt'),event);" 
					value="<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? Utils.convertToAppDate(((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getValidDate()) : Utils.convertToAppDate(formatter.format(validDt))%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtValidDt'),event);" /></td>
					<td align="right">Salesman<span style="color: red">*</span> :</td>
					<td>
						<select  name="sUser" style="width: 200px;" <%=(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) ? "disabled=\"disabled\"" : ""%>>
							<option value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId()%>"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName()%></option>
						</select>
					</td>
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
					SalesItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesItemModel[]) (((QuotationModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new SalesItemModel[0]))) : new SalesItemModel[0];
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
					<td rowspan="5" width="9%" valign="top" style="padding-top: 3px;" align="right">Remarks :</td>
					<td rowspan="5" valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 550px; height : 100px;" maxlength="300"></textarea></td>
					<td align="right">Installation Charges (+) :</td>
					<td  style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtInstall" value = "0" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true);s"></td>
				</tr>
				<tr>
					<td align="right">Packing Charges (+) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtPacking" value = "0" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Forwarding Charges (+) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtForwarding" value = "0" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right" valign="top">LUMSUM Less (-) :</td>
					<td style="width: 120px;" valign="top"><input type="text" style="width: 110px; text-align: right;" name="txtLess" value = "0" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Grand Total (Without Taxes) :</td> 
					<td style="width: 120px;" align="right"><label id="gTotal" style="padding-right: 10px; font-weight: bolder; font-size: 18px;">0.00</label></td>
				</tr>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Generate Bill" onclick="fnSalesmanGenerateBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtSalesItems"/>
</form>
</body>
<script type="text/javascript">
function fnChangeAP(ap){
	document.getElementById("branchState").innerHTML = ap.options[ap.selectedIndex].id;
}
</script>
</html>