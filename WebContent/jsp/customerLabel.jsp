<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.ItemModel"%>
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
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddItemCat">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Print Customer Label</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer Type<span style="color: red">*</span> :</td>
					<td>
						<select name="sCustomerType" style="width: 200px;">
							<option value="">---ALL---</option>
							<%
								String[] customerTypes = RequestUtil.custCategory;
								for(int i=0; i<customerTypes.length; i++){
									if(request.getAttribute("type") != null && ((String)request.getAttribute("type")).equals(customerTypes[i])){
							%>
							<option value="<%=customerTypes[i]%>" selected="selected"><%=customerTypes[i] %></option>							
							<%
									} else {
							%>
							<option value="<%=customerTypes[i]%>"><%=customerTypes[i] %></option>
							<%
									}}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">State <span style="color: red">*</span> :</td>
					<td>
						<select name="txtState" style="width: 200px;" onchange="fnGetCities();">
							<option value="">---ALL---</option>
							<%
								
								String[] states = request.getAttribute(Constant.STATES) != null ?
														(String[]) request.getAttribute(Constant.STATES) : new String[0];
								for(int i=0; i<states.length;i++){
							%>
							<option value="<%=states[i]%>"  <%=(request.getAttribute("state") != null && 
									((String)request.getAttribute("state")).equals(states[i])) ? "selected=\"selected\"" : "" %>><%=states[i]%></option>
										
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td>
						<select name="txtCity" style="width: 200px;" <%=(request.getAttribute(Constant.CITIES) != null) ? "" : "disabled=\"disabled\"" %>>
							<option value="">---ALL---</option>
							<%
								
								String[] cities = request.getAttribute(Constant.CITIES) != null ?
														(String[]) request.getAttribute(Constant.CITIES) : new String[0];
								for(int i=0; i<cities.length;i++){
							%>
							<option value="<%=cities[i]%>"><%=cities[i]%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Generate Labels" onclick="fnGenerateLabel()"/></td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
function fnGetCities(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_CITIES';
	fnFinalSubmit();
}
function fnGenerateLabel(){
	var vType = document.forms[0].sCustomerType.value;
	var vState = document.forms[0].txtState.value;
	if(vState != ""){
		var vCity = document.forms[0].txtCity.value;
		window.open('/en/app?HANDLERS=CUSTOMER_HANDLER&ACTIONS=PRINT_CUSTOMER_LABEL&city='+vCity+'&state='+vState+'&type='+vType);
	} else {
		window.open('/en/app?HANDLERS=CUSTOMER_HANDLER&ACTIONS=PRINT_CUSTOMER_LABEL&city=&state='+vState+'&type='+vType);
	}
}
</script>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>