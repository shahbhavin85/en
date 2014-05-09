<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.LabourBillModel"%>
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
<script type="text/javascript" src="js/labourInvoice.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Edit Labour Invoice</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="2" style="width: 950px;">
				<tr>
					<td align="right">Invoice No<span style="color: red">*</span> :</td>
					<td colspan="3"><input name="txtInvoiceNo" readonly="readonly" style="width: 75px;" value="<%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"LB"+Utils.padBillNo(((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSaleid())%>"></td>
				</tr>
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td colspan="3">
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerName()+" - "+ ((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getArea()+" - "+((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCity() : "" %>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
				</tr>
				<tr>
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
									((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Description<span style="color: red">*</span> :</td>
					<td colspan="3">
						<textarea  name="txtRemarks" style="width: 550px; height : 100px;" maxlength="120"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() : "" %></textarea>
					</td>
				</tr>
				<tr>
					<td align="right">Amount<span style="color: red">*</span> :</td>
					<td colspan="3">
						<input type="text" style="width: 110px; text-align: right;" name="txtTotal" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getTotalAmt() : "0" %>" onkeypress="return numbersonly(this, event, true)">
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" name="genBill" value="Edit & Generate Bill" onclick="fnEditGenerateBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  
							((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId() : "" %>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtSalesItems"/>
</form>
</body>
</html>