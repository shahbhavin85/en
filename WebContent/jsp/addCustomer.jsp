<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/customers.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add New Customer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" width="100%">
				<tr><td colspan="2" align="right">Customer Data Count : <%=request.getAttribute("count") %><br/>Uncheck Count : <%=request.getAttribute("uncheck") %></td></tr>
			</table>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer M/s. <span style="color: red">*</span> :</td>
					<td><input name="txtCustomerName" onblur="fnChangePostalAddress();" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerName() : ""%>" style="width: 195px;"></td>
					<td rowspan="20" valign="top" align="left">
						<fieldset style="width: 400px; height: 200px;">
							<legend style="font-size: 16px; font-style: italic; font-weight: bold;">Postal Address</legend>
							<div><label id="postalAdd" style="font-size: 15px; font-weight: bold;"> </label></div>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td align="right">Contact Person :</td>
					<td><input name="txtContactPerson" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getContactPerson() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Customer Type<span style="color: red">*</span> :
					</td>
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
						</select> <%if(((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("0")){ %>&nbsp;<a href="javascript:fnOpenTypePopup();">Add Customer Type</a><%} %>
					</td>
				</tr>
				<tr>
					<td align="right">Address :</td>
					<td><textarea onblur="fnChangePostalAddress();" name="taAddress" style="width: 195px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getAddress() : ""%></textarea></td>
				</tr>
				<tr>
					<td align="right">Landmark / Area <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtArea" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getArea() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td>
						<select name="txtCity" style="width: 200px;" onblur="fnChangePostalAddress();">
							<option value="">-------</option>
							<%
								
								String[] cities = request.getAttribute(Constant.CITIES) != null ?
														(String[]) request.getAttribute(Constant.CITIES) : new String[0];
								for(int i=0; i<cities.length;i++){
							%>
							<option value="<%=cities[i]%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCity() == cities[i]) ? "selected=\"selected\"" : "" %>><%=cities[i]%></option>
							<%
								}
							%>
						</select> <%if(((String)request.getSession().getAttribute(Constant.ACCESS_POINT)).equals("0")){ %>&nbsp;<a href="javascript:fnOpenCityPopup();">Add City</a><%} %>
					</td>
				</tr>
				<!-- tr>
					<td align="right">District :</td>
					<td><input maxlength="50" name="txtDistrict" value="<!%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getDistrict() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">State :</td>
					<td><input maxlength="50" name="txtState" value="<!%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getState() : ""%>" style="width: 195px;"></td>
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
					<td><input maxlength="13" name="txtOpenBal" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getOpenBal() : "0"%>" style="width: 195px;"></td>
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
					<td colspan="3" align="center"><input type="button" onclick="fnSaveCustomer();"
						value="Save"> <input type="reset" value="Reset" onclick="javascript: document.frmAddCustomer.txtCustomerName.focus();"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" value="--" name="sCustomerGrp">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="isChecked" value="<%=(request.getAttribute("checked") == null) ? "N" : "Y"%>"/>
	<div id="taskWindow" style="display: none; width:450px; background-color:#D3BECF; border:#2E0854 solid 3px; -moz-border-radius: 10px; border-radius: 10px; -moz-box-shadow: -2px -2px 3px #d9d9d9; -webkit-box-shadow: -2px -2px 3px #d9d9d9; box-shadow: -2px -2px 3px #d9d9d9; padding:10px; position: fixed; margin-left:30%; top:10%; z-index:1002;">
		<fieldset>
			<legend style="font-weight: bold; color: blue;">ADD CITY</legend>
			<table cellpadding="3">
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="city" value="<%=(request.getAttribute("city") != null) ? (request.getAttribute("city")) : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">District :</td>
					<td><input maxlength="50" name="district" value="<%=(request.getAttribute("district") != null) ? (request.getAttribute("district")) : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">State / Other Countries :</td>
					<td>
						<select name="state">
							<option value = "--">-------</option>
							<option value = "ANDAMAN AND NICOBAR ISLANDS">ANDAMAN AND NICOBAR ISLANDS</option>
							<option value = "ANDHRA PRADESH">ANDHRA PRADESH</option>
							<option value = "ARUNACHAL PRADESH">ARUNACHAL PRADESH</option>
							<option value = "ASSAM">ASSAM</option>
							<option value = "BIHAR">BIHAR</option>
							<option value = "CHANDIGARH">CHANDIGARH</option>
							<option value = "CHHATTISGARH">CHHATTISGARH</option>
							<option value = "DAMAN AND DIU">DAMAN AND DIU</option>
							<option value = "DELHI">DELHI</option>
							<option value = "GOA">GOA</option>
							<option value = "GUJARAT">GUJARAT</option>
							<option value = "HARYANA">HARYANA</option>
							<option value = "HIMACHAL PRADESH">HIMACHAL PRADESH</option>
							<option value = "JAMMU AND KASHMIR">JAMMU AND KASHMIR</option>
							<option value = "JHARKHAND">JHARKHAND</option>
							<option value = "KARNATAKA">KARNATAKA</option>
							<option value = "KERALA">KERALA</option>
							<option value = "LAKSHADEEP">LAKSHADEEP</option>
							<option value = "MADYA PRADESH">MADYA PRADESH</option>
							<option value = "MAHARASHTRA">MAHARASHTRA</option>
							<option value = "MANIPUR">MANIPUR</option>
							<option value = "MEGHALAYA">MEGHALAYA</option>
							<option value = "MIZORAM">MIZORAM</option>
							<option value = "NAGALAND">NAGALAND</option>
							<option value = "ORISSA">ORISSA</option>
							<option value = "PONDICHERRY">PONDICHERRY</option>
							<option value = "PUNJAB">PUNJAB</option>
							<option value = "RAJASTHAN">RAJASTHAN</option>
							<option value = "SIKKIM">SIKKIM</option>
							<option value = "TAMIL NADU">TAMIL NADU</option>
							<option value = "TRIPURA">TRIPURA</option>
							<option value = "UTTAR PRADESH">UTTAR PRADESH</option>
							<option value = "UTTARANCHAL">UTTARANCHAL</option>
							<option value = "WEST BENGAL">WEST BENGAL</option>
							<%
								String[] countries = request.getAttribute(Constant.COUNTRIES) == null ? new String[0] : (String[]) request.getAttribute(Constant.COUNTRIES);
								for(int i=0; i<countries.length; i++){
							%>
							<option value = "<%=countries[i] %>" style="background-color: black; color: white;"><%=countries[i]%></option>
							<%
								}
							%>
						</select> 
					</td>
				</tr>
				<tr>
					<td align="right">Add Country :</td>
					<td><input type="radio" name="rNewCountry" id="rNewCountry0" value=0 onchange="fnNewCountry(0);" checked="checked"><label for="rNewCountry0">No</label><input type="radio" name="rNewCountry" id="rNewCountry1" value=1 onchange="fnNewCountry(1);"><label for="rNewCountry1">Yes</label> </td>
				</tr>
				<tr>
					<td align="right">New Country : </td>
					<td><input type="text" disabled="disabled" name="newCountry" style="width: 195px;" maxlength="50" value = ""/></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnSaveCity();"
						value="Save">
						<input type="button" value="Cancel" onclick="fnCancel();"> </td>
				</tr>
			</table>
		</fieldset>
	</div>
</form>
<script type="text/javascript">
fnChangePostalAddress();
</script>
</body>
</html>