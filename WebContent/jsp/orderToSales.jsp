<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.en.model.AccessPointModel"%>
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
<script type="text/javascript" src="js/orderToSales.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Order To Sales Invoice</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="2" style="width: 950px;">
				<tr>
					<td align="right" width="11%">Last Bill No :</td>
					<td colspan="3">  <b>  <%=request.getAttribute(Constant.LAST_BILL) %></b>
						&nbsp;&nbsp;&nbsp;Branch State : <b><label style="background: aqua; color: brown; padding: 2px;" id="branchState"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getState() %></label></b>
						&nbsp;&nbsp;&nbsp;Customer State : <b><label style="background: yellow; color: red; padding: 2px;" id="custState"></label></b>&nbsp;&nbsp;&nbsp;Customer Type : <b><label style="background: yellow; color: red; padding: 2px;" id="custType"></label></b> 
					</td>
				</tr>
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td colspan="3">
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerName()+" - "+ ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getArea()+" - "+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCity() : "" %>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
<!-- 						<select name="sCustomer" onchange="fnChangeCustomer(this);"> -->
<!-- 							<option value="--">-----</option> -->
<%-- 							<% --%>
<!-- 								String state = ""; -->
<!-- 								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ? -->
<!-- 														(CustomerModel[])request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0]; -->
<!-- 								CustomerModel temp = null; -->
<!-- 									for(int i=0; i<customers.length;i++){ -->
<!-- 										temp=customers[i]; -->
										
<!-- 							%> -->
<%-- 							<option id='<%=temp.getId() %>' style="<%=(temp.getBillCount() > 0) ? "font-weight: bold;" : ""%> background-color: <%=(temp.getBillCount() > 0) ? "yellow" : ((i%2==0) ? "#E0E0E0" : "white")%>" --%>
<%-- 								 value="<%=temp.getCustomerId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null &&  --%>
<%-- 									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId() == temp.getCustomerId()) ? "selected=\"selected\"" : "" %>><%=temp.getCustomerName()%> - <%=temp.getArea()%> <%=(temp.getBillCount() > 0) ? " :: ("+temp.getBillCount()+")" : "" %></option> --%>
<%-- 							<% --%>
<!-- 								} -->
<!-- 							%> -->
<!-- 						</select> -->
					</td>
				</tr>
				<tr>
					<td align="right">TIN : </td>
					<td><input maxlength="25" name="txtTin" value=""/></td>
					<td align="right">CST : </td>
					<td><input maxlength="25" name="txtCst" value=""/></td>
				</tr>
				<tr>
					<td align="right">Contact Person : </td>
					<td><input maxlength="25" name="txtContactPerson" value=""/></td>
					<td align="right">Mobile : </td>
					<td><input maxlength="25" name="txtMobile1" value=""/></td>
				</tr>
				<tr>
					<td align="right">Paymode<span style="color: red">*</span> :</td>
					<td>
						<input onchange="fnChangePaymode(this);" type="radio" value="1" onclick="fnPayModeClicked();" name="rPayMode" id="rPaymode1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 1) ? "checked=\"checked\"" : "" %> /><label for="rPaymode1" style="padding: 2px; color: red; background-color: yellow;">Cash</label>
						<input onchange="fnChangePaymode(this);" type="radio" value="2" onclick="fnPayModeClicked();" name="rPayMode" id="rPaymode2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 2) ? "checked=\"checked\"" : "" %>/><label for="rPaymode2" style="padding: 2px; color: white; background-color: black;">CC Card</label>
						<input onchange="fnChangePaymode(this);" type="radio" value="3" onclick="fnPayModeClicked();" name="rPayMode" id="rPaymode3" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 3) ? "checked=\"checked\"" : "" %>/><label for="rPaymode3" style="padding: 2px; color: orange; background-color: brown;">Credit</label>
						<input type="text" style="width: 25px;  text-align: right;" name="txtCreditDays" disabled="disabled" value = "" maxlength="3" onkeypress="return numbersonly(this, event, false)"> (Days)
					</td>
					<td align="right">Tax Type<span style="color: red">*</span> :</td>
					<td>
						<input type="radio" value="1" name="rTaxType" id="rTaxType1" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label for="rTaxType1" style="padding: 2px; color: red; background-color: yellow;">VAT</label>
						<input type="radio" value="2" name="rTaxType" id="rTaxType2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) ? "checked=\"checked\"" : "" %>/><label for="rTaxType2" style="padding: 2px; color: white; background-color: black;">CST</label>
						<input type="radio" value="3" name="rTaxType" id="rTaxType3" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 3) ? "checked=\"checked\"" : "" %>/><label for="rTaxType3" style="padding: 2px; color: orange; background-color: brown;">CST Against from 'C'</label>
					</td>
				</tr>
				<tr><td align="right">Salesman<span style="color: red">*</span> :</td>
					<td>
						<select  name="sUser" style="width: 200px;" <%=(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) ? "disabled=\"disabled\"" : ""%>>
							<option value="--">-----</option>
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
					<td align="right" valign="top">Mode of Transport :</td>
					<td><input type="text" name="txtDespatch" maxlength="50" value = "">
					 &nbsp; CTNS : <b><input type="text" maxlength="3" name="txtCtns" value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCtns() : "0" ) %>"/></b>
					</td>
				</tr>
				<tr>
					<td align="right">Packed By : </td>
					<td><select  name="sPackedBy" style="width: 200px;">
							<option value="--">-----</option>
							<%
								
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPackedBy().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select></td>
					<td align="right">Despatched By : </td>
					<td valign="top"><select  name="sDespatchedBy" style="width: 200px;">
							<option value="--">-----</option>
							<%
								
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getDespatchedBy().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" style="margin-left : 10px; margin-top : 10px; width: 950px;">
				<tr>
					<td align="center"><b>Item Id</b></td>
					<td align="center" width="20%"><b>Item No</b></td>
					<td align="center" width="15%"><b>Desc</b></td>
					<td align="center"><b>Ordered Quantity</b></td>
					<td align="center"><b>Sent Quantity</b></td>
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Discount</b></td>
					<td align="center" style="width: 120px;"><b>Ordered Amount</b></td>
					<td align="center" style="width: 120px;"><b>Amount</b></td>
				</tr>
				<%
					SalesItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesItemModel[]) (((SalesModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new SalesItemModel[0]))) : new SalesItemModel[0];
					for(int i=0; i<salesItems.length; i++){
				%>
				<tr id=s<%=i%>>
					<td align="center"><%=salesItems[i].getItem().getItemId() %></td>
					<td align="center"><%=salesItems[i].getItem().getItemNumber() %></td>
					<td align="center"><%=salesItems[i].getDesc() %></td>
					<td align="center"><%=salesItems[i].getQty()%></td>
					<td align="center"><%=salesItems[i].getSentQty()%></td>
					<td align="center"><input type="text" onblur="fnChangeQty(<%=i%>);" style="width: 50px; text-align: right;" name="txtQty<%=i%>" id="txtQty<%=i%>" <%=((salesItems[i].getQty() - salesItems[i].getSentQty()) > 0) ? "" : "disabled=\"disabled\"" %> value="<%=((salesItems[i].getQty() - salesItems[i].getSentQty()) > 0) ? (salesItems[i].getQty() - salesItems[i].getSentQty()) : "0"%>"></td>
					<td align="center"><%=salesItems[i].getRate()%></td>
					<td align="center"><%=salesItems[i].getDisrate()%></td>
					<td align="right" style="width: 120px;"><%=((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate()%></td>
					<td align="right" style="width: 120px;"><%=((100-salesItems[i].getDisrate())/100)*(salesItems[i].getQty() - salesItems[i].getSentQty()) * salesItems[i].getRate()%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="margin-top : 10px; margin-left : 10px; width: 950px;" cellspacing="3" cellpadding="0">
				<tr>
					<td rowspan="7" width="9%" valign="top" style="padding-top: 3px;" align="right">Remarks :</td>
					<td rowspan="7" valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 550px; height : 100px;" maxlength="120"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() : "" %></textarea></td>
					<td align="right">Installation Charges (+) :</td>
					<td  style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtInstall" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true);"></td>
				</tr>
				<tr>
					<td align="right">Packing Charges (+) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtPacking" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Forwarding Charges (+) :</td>
					<td style="width: 120px;"><input type="text" style="width: 110px; text-align: right;" name="txtForwarding" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right" valign="top">LUMSUM Less (-) :</td>
					<td style="width: 120px;" valign="top"><input type="text" style="width: 110px; text-align: right;" name="txtLess" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess() : "0" %>" onblur="fnChangeTotal(this);" onkeypress="return numbersonly(this, event, true)"></td>
				</tr>
				<tr>
					<td align="right">Grand Total (Without Taxes) :</td>
					<td style="width: 120px;" align="right"><label id="gTotal" style="padding-right: 10px; font-weight: bolder; font-size: 18px;">0.00</label></td>
				</tr>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Generate Bill" onclick="fnGenerateBill();" /><input type="button" value="Back" onclick="fnBack();"/></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId() : "" %>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="ORDER_NO" value="<%=request.getAttribute("ORDER_NO")%>"/>
	<input type="hidden" name="txtSalesItems"/>
</form>
<script type="text/javascript">
fnRefreshTotal();
<%
	if(request.getAttribute(Constant.FORM_DATA) != null){
		if((request.getAttribute(Constant.FORM_DATA) != null && 
				((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() != 0)){
%>
fnPayModeClicked();
<%	}%>
fnChangeCustomer('<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getState().toUpperCase()+"!"+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerType()+"<"+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin() +">"+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst()+"~"+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getContactPerson()+"|"+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getMobile1()%>');
<%
	}
%>
function fnBack(){
	document.forms[0].HANDLERS.value = "ORDER_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_FOLLOWUP";
	fnFinalSubmit();
}
fnRefreshTotal();
</script>
</body>
</html>