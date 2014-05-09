<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.PurchaseItemModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.PurchaseModel"%>
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
<script type="text/javascript" src="js/purchase.js"></script>
<script type="text/javascript" src="js/dateUtil.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Purchase Invoice</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Supplier<span style="color: red">*</span> :</td>
					<td colspan="3">
						<select name="sCustomer">
							<option value="--">-----</option>
							<%
								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ?
														(CustomerModel[]) request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0];
								for(int i=0; i<customers.length;i++){
							%>
							<option value="<%=customers[i].getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getSupplier().getCustomerId() == customers[i].getCustomerId()) ? "selected=\"selected\"" : "" %>><%=customers[i].getCustomerName()%> - <%=customers[i].getArea()%> - <%=customers[i].getCity()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr style="padding-top: 5px;">
					<td  style="padding-left: 20px;" align="right">Invoice No <span style="color: red">*</span> :</td>
					<td><input type="text" name="txtInvNo" maxlength="15" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getInvNo() : ""%>"></td>
					<td align="right">Invoice Date :</td>
					<td><input name="txtInvDt" id="txtInvDt" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtInvDt'),event);" 
					value="<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? Utils.convertToAppDate(((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getInvDt()) : DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtInvDt'),event);" /></td>
                 </tr>
                 <tr>
					<td  style="padding-left: 20px;" align="right">Received Date :</td>
					<td><input name="txtRecdDt" id="txtRecdDt" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtRecdDt'),event);" 
					value="<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? Utils.convertToAppDate(((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getRecdDt()) : DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtRecdDt'),event);" /></td>		
					<td align="right">Tax Type<span style="color: red">*</span> :</td>
					<td>
						<input type="radio" value="1" name="rTaxType" id="rTaxType1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBillType() == 1) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label for="rTaxType1" style="padding: 2px; color: red; background-color: yellow;">VAT</label>
						<input type="radio" value="2" name="rTaxType" id="rTaxType2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBillType() == 2) ? "checked=\"checked\"" : "" %>/><label for="rTaxType2" style="padding: 2px; color: white; background-color: black;">CST</label>
						<input type="radio" value="3" name="rTaxType" id="rTaxType3" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getBillType() == 3) ? "checked=\"checked\"" : "" %>/><label for="rTaxType3" style="padding: 2px; color: orange; background-color: brown;">CST Against from 'C'</label>
					</td>
				</tr>
			</table>
			<table style="width: 950px;">
				<tr><td style="padding-left: 15px;"> Item Name: <br/>
					<select name="sItemName" style="width: 320px;" onchange="fnChangeItemNo(this);">
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
					<td style="padding-left: 5px;"> Item No.: <br/>
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
					<td style="padding-left: 5px;">Dis. %: <br/><input type="text" name="txtDis" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">Cess %: <br/><input type="text" name="txtCess" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">Tax %: <br/><input type="text" name="txtTax" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">Rate : <br/><input type="text" name="txtRate" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">Qty : <br/><input type="text" name="txtQuantity" style="width: 50px;" value="" onkeypress="return numbersonly(this, event, true);"/></td>
					<td align="left" valign="bottom"><input type="button" value="Add" onclick="fnAddItem();"/></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" style="margin-left : 20px; width: 950px;">
				<tr>
					<td align="center"><b>Action</b></td>
					<td align="center"><b>Item Id</b></td>
					<td align="center" width="40%"><b>Item No</b></td>
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Tax</b></td>
					<td align="center" style="width: 120px;"><b>Amount</b></td>
				</tr>
				<%
					PurchaseItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((PurchaseItemModel[]) (((PurchaseModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new PurchaseItemModel[0]))) : new PurchaseItemModel[0];
					for(int i=0; i<salesItems.length; i++){
				%>
				<tr id=s<%=i%>>
					<td align="center"><input type="button" value="Delete" onclick="fnDeleteItem('s<%=i%>');" /></td>
					<td align="center"><%=salesItems[i].getItem().getItemId() %></td>
					<td align="center"><%=salesItems[i].getItem().getItemNumber() %></td>
					<td align="center"><%=salesItems[i].getQty()%></td>
					<td align="center"><%=salesItems[i].getRate()%></td>
					<td align="center"><%=salesItems[i].getTax()%></td>
					<td align="center"><%=salesItems[i].getQty() * salesItems[i].getRate() * ((100+salesItems[i].getTax())/100)%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="margin-top : 10px; margin-left : 20px; width: 950px;" cellspacing="3" cellpadding="0">
				<tr>
					<td rowspan="5" width="7%" valign="top" style="padding-top: 3px;" align="right">Remarks :</td>
					<td rowspan="5" valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 195px; height : 50px;" maxlength="120"></textarea></td>
					<td align="right">Extra (+) :</td>
					<td  style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtExtra" value = "0" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true);"></td>
				</tr>
				<tr>
					<td align="right">Discount (-) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtDiscount" value = "0" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right" valign="top">Grand Total (Without Taxes) :</td>
					<td style="width: 120px;" valign="top" align="right"><label id="gTotal" style="padding-right: 10px; font-weight: bolder; font-size: 18px;">0.00</label></td>
				</tr>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Generate Purchase Memo" onclick="fnGenerateBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtPurchaseItems"/>
</form>

</body>
</html>