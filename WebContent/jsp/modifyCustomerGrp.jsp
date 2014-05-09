<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.CustomerGroupModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/customerGrp.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddNewItemGrp">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Modify Customer Group</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Customer Group <span style="color: red">*</span> :</td>
					<td>
						<select name="sCustomerGrp" style="width: 200px;" onchange="fnGetCustomerGrpDetails();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								
								CustomerGroupModel[] custGrps = request.getAttribute(Constant.CUSTOMERS_GROUPS) != null ?
														(CustomerGroupModel[]) request.getAttribute(Constant.CUSTOMERS_GROUPS) : new CustomerGroupModel[0];
								for(int i=0; i<custGrps.length;i++){
							%>
							<option value="<%=custGrps[i].getCustGroupId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((CustomerGroupModel)request.getAttribute(Constant.FORM_DATA)).getCustGroupId() == custGrps[i].getCustGroupId()) ? "selected=\"selected\"" : "" %>><%=custGrps[i].getCustGroup()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Customer Group <span style="color: red">*</span> :</td>
					<td><input name="txtCustomerGrp" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  ((CustomerGroupModel)request.getAttribute(Constant.FORM_DATA)).getCustGroup() : "" %>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdateCustomerGrpDetails();" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%>
						value="Modify"> <!--input type="reset" value="Delete" onclick="javascript: document.frmLoginPage.txtUser.focus();"--></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>