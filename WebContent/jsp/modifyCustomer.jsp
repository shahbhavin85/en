<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.CustomerGroupModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<META NAME="ROBOTS" CONTENT="ALL"> 
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/customers.js"></script>
<script type="text/javascript">
function fnSelect(id, txt){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_CUSTOMER';
	fnFinalSubmit();	
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Modify Customer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" width="100%">
				<tr><td colspan="2" align="right">Customer Data Count : <%=request.getAttribute("count") %><br/>Uncheck Count : <%=request.getAttribute("uncheck") %></td></tr>
			</table>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerName()+" - "+((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCity()	 : ""%>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
					<td rowspan="21" valign="top" align="left">
						<fieldset style="width: 400px; height: 200px;">
							<legend style="font-size: 16px; font-style: italic; font-weight: bold;">Postal Address</legend>
							<div><label id="postalAdd" style="font-size: 15px; font-weight: bold;"></label></div>
						</fieldset>
					</td>
<!-- 					<td> -->
<!-- 						<select name="sCustomer" onchange="fnGetCustomerDetails();"> -->
<%-- 							<% --%>
<!-- 								if(request.getAttribute(Constant.FORM_DATA) == null){ -->
<!-- 							%> -->
<!-- 							<option value="--">-------</option> -->
<%-- 							<% --%>
<!-- 								} -->
								
<!-- 								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ? -->
<!-- 														(CustomerModel[]) request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0]; -->
<!-- 								for(int i=0; i<customers.length;i++){ -->
<!-- 							%> -->
<%-- 							<option value="<%=customers[i].getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null &&  --%>
<%-- 									((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerId() == customers[i].getCustomerId()) ? "selected=\"selected\"" : "" %>><%=customers[i].getCustomerName()%> - <%=customers[i].getArea()%> - <%=customers[i].getCity()%></option> --%>
<%-- 							<% --%>
<!-- 								} -->
<!-- 							%> -->
<!-- 						</select> -->
<!-- 					</td> -->
				</tr>
				<tr>
					<td align="right">Customer M/s. <span style="color: red">*</span> :</td>
					<td><input onblur="fnChangePostalAddress();" name="txtCustomerName" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerName() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Contact Person :</td>
					<td><input name="txtContactPerson" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getContactPerson() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Customer Type<span style="color: red">*</span> :</td>
					<td>
						<select name="sCustomerType" style="width: 200px;">
							<option value="--">-------</option>
							<%
								String[] customerTypes = (String[]) request.getAttribute(Constant.CUST_TYPE);
								for(int i=0; i<customerTypes.length; i++){
									if(request.getAttribute(Constant.FORM_DATA) != null && ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerType().equals(customerTypes[i])){
							%>
							<option value="<%=customerTypes[i]%>" selected="selected"><%=customerTypes[i] %></option>							
							<%
									} else {
							%>
							<option value="<%=customerTypes[i]%>"><%=customerTypes[i] %></option>
							<%
									}
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Address :</td>
					<td><textarea  name="taAddress" onblur="fnChangePostalAddress();" style="width: 195px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getAddress() : ""%></textarea></td>
				</tr>
				<tr>
					<td align="right">Area <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtArea" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getArea() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td>
						<select name="txtCity" onblur="fnChangePostalAddress();" style="width: 200px;">
							<option value="">-----</option>
							<%
								
								String[] cities = request.getAttribute(Constant.CITIES) != null ?
														(String[]) request.getAttribute(Constant.CITIES) : new String[0];
								for(int i=0; i<cities.length;i++){
							%>
							<option value="<%=cities[i]%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCity().equals(cities[i])) ? "selected=\"selected\"" : "" %>><%=cities[i]%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<!--tr>
					<td align="right">District :</td>
					<td><input maxlength="50" name="txtDistrict" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getDistrict() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">State :</td>
					<td><input maxlength="50" name="txtState" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getState() : ""%>" style="width: 195px;"></td>
				</tr-->
				<tr>
					<td align="right">Pin Code :</td>
					<td><input maxlength="6" onblur="fnChangePostalAddress();" name="txtPinCode" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPincode() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Std Code - Phone :</td>
					<td><input maxlength="6" onblur="fnChangePostalAddress();" name="txtStdCode" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getStdcode() : ""%>" style="width: 60px;">&nbsp;-&nbsp;
					<input maxlength="10" onblur="fnChangePostalAddress();" name="txtPhone1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPhone1() : ""%>" style="width: 195px;"> &nbsp;&nbsp;/&nbsp;&nbsp;
					<input maxlength="10" onblur="fnChangePostalAddress();" name="txtPhone2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPhone2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Mobile :</td>
					<td><input maxlength="10" onblur="fnChangePostalAddress();" name="txtMobile1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getMobile1() : ""%>" style="width: 195px;"> &nbsp;&nbsp;/&nbsp;&nbsp;
					<input maxlength="10" onblur="fnChangePostalAddress();" name="txtMobile2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getMobile2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Is Supplier :</td>
					<td valign="middle">
						<%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ? (((request.getAttribute(Constant.FORM_DATA)  == null) || request.getAttribute(Constant.FORM_DATA) != null && 
								!((CustomerModel)request.getAttribute(Constant.FORM_DATA)).isSupplier()) ? "No" : "Yes") : "" %>
						<input type="radio" value="N" <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> name="rSupplier" id="rN" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									!((CustomerModel)request.getAttribute(Constant.FORM_DATA)).isSupplier()) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> for="rN">No</label>
						<input type="radio" value="Y" <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> name="rSupplier" id="rY" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
								((CustomerModel)request.getAttribute(Constant.FORM_DATA)).isSupplier())  ? "checked=\"checked\"" : "" %>/><label <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> for="rY">Yes</label>
					</td>
				</tr>
				<tr>
					<td align="right">Grade<span style="color: red">*</span> :</td>
					<td>
						<select name="sGrade" style="width: 200px;">
							<option value="A" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getGrade().equals("A")) ? "selected=\"selected\"" : "" %>>A</option>
							<option value="B" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getGrade().equals("B")) ? "selected=\"selected\"" : "" %>>B</option>
							<option value="C" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getGrade().equals("C")) ? "selected=\"selected\"" : "" %>>C</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Opening Bal. :</td>
					<td><input maxlength="13" name="txtOpenBal" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getOpenBal() : "0"%>" style="width: 195px;" readonly="readonly"> <input <%=(((AccessPointModel)(request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS))).getAccessId() == 0 || ((AccessPointModel)(request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS))).getAccessId() == 2) ? "" : "disabled=\"disabled\"" %> type="checkbox" onclick="fnOpenChkClicked(this);"></td>
				</tr>
				<tr>
					<td align="right">Op Bal. Remarks :</td>
					<td><textarea name="taOpenbalRemarks" style="width: 195px; height : 50px;" maxlength="500"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getOpenBalRemark() : ""%></textarea></td>
				</tr>
				<tr>
					<td align="right">Email 1 :</td>
					<td><input maxlength="50" name="txtEmail" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Email 2 :</td>
					<td><input maxlength="50" name="txtEmail1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail1() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Email 3 :</td>
					<td><input maxlength="50" name="txtEmail2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Email 4 :</td>
					<td><input maxlength="50" name="txtEmail3" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail3() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Email 5 :</td>
					<td><input maxlength="50" name="txtEmail4" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail4() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Email 6 :</td>
					<td><input maxlength="50" name="txtEmail5" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail5() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Website :</td>
					<td><input maxlength="50" name="txtWebsite" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getWebsite() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">VAT :</td>
					<td><input maxlength="25" name="txtTin" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getTin() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">CST :</td>
					<td><input maxlength="25" name="txtCst" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCst() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Collection Person :</td>
					<td>
						<select  name="sUser" style="width: 200px;" <%=(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) ? "disabled=\"disabled\"" : ""%>>
							<option value="--">-----</option>
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCollectionPerson().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Transport :</td>
					<td><input maxlength="50" name="txtTransport" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getTransport() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Remarks :</td>
					<td><textarea  name="taRemarks" style="width: 195px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getRemark() : ""%></textarea></td>
				</tr>
				<tr>
					<td colspan="3" align="center"><input type="button" onclick="fnUpdateCustomerDetails();" 
						value="Modify"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" value="--" name="sCustomerGrp">
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerId() : ""%>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
fnChangePostalAddress();
</script>
</body>
</html>