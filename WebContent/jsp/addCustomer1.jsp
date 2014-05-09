<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.RequestContants"%>
<%@page import="com.mysql.jdbc.Constants"%>
<%@page import="com.en.model.MessageModel"%>
<%@page import="com.en.model.EnquiryModel"%>
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.CustomerGroupModel"%>
<%@page import="com.en.model.EnquiryModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/customers1.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add New Customer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer M/s. <span style="color: red">*</span> :</td>
					<td><input name="txtCustomerName" onblur="fnChangePostalAddress();" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getCustomerName() : ""%>" style="width: 195px;"></td>
					<td rowspan="18" valign="top" align="left">
						<fieldset style="width: 400px; height: 200px;">
							<legend style="font-size: 16px; font-style: italic; font-weight: bold;">Postal Address</legend>
							<div style="background-color: yellow;"><label id="postalAdd" style="font-size: 15px; font-weight: bold;"></label></div>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td align="right">Contact Person :</td>
					<td><input name="txtContactPerson" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getContactPerson() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Customer Type<span style="color: red">*</span> :</td>
					<td>
						<select name="sCustomerType" style="width: 200px;">
							<option value="--">-------</option>
							<%
								String[] customerTypes = (String[]) request.getAttribute(Constant.CUST_TYPE);
								for(int i=0; i<customerTypes.length; i++){
									if(request.getAttribute(Constant.FORM_DATA) != null && (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getCustomerType().equals(customerTypes[i])){
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
					<td><textarea  name="taAddress" onblur="fnChangePostalAddress();" style="width: 195px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getAddress() : ""%></textarea></td>
				</tr>
				<tr>
					<td align="right">Landmark / Area <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtArea" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getArea() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td>
						<select name="txtCity" onblur="fnChangePostalAddress();" style="width: 200px;">
							<option value="">-------</option>
							<%
								
								String[] cities = request.getAttribute(Constant.CITIES) != null ?
														(String[]) request.getAttribute(Constant.CITIES) : new String[0];
								for(int i=0; i<cities.length;i++){
							%>
							<option value="<%=cities[i]%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									(((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getCity() == cities[i]) ? "selected=\"selected\"" : "" %>><%=cities[i]%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<!-- tr>
					<td align="right">District :</td>
					<td><input maxlength="50" name="txtDistrict" value="<!%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getDistrict() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">State :</td>
					<td><input maxlength="50" name="txtState" value="<!%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getState() : ""%>" style="width: 195px;"></td>
				</tr-->
				<tr>
					<td align="right">Pin Code :</td>
					<td><input maxlength="6" onblur="fnChangePostalAddress();" name="txtPinCode" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getPincode() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Std Code - Phone :</td>
					<td><input maxlength="6" onblur="fnChangePostalAddress();" name="txtStdCode" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getStdcode() : ""%>" style="width: 60px;">&nbsp;-&nbsp;
					<input maxlength="10" onblur="fnChangePostalAddress();" name="txtPhone1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getPhone1() : ""%>" style="width: 195px;"> &nbsp;&nbsp;/&nbsp;&nbsp;
					<input maxlength="10" onblur="fnChangePostalAddress();" name="txtPhone2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getPhone2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Mobile :</td>
					<td><input maxlength="10" onblur="fnChangePostalAddress();" name="txtMobile1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getMobile1() : ""%>" style="width: 195px;"> &nbsp;&nbsp;/&nbsp;&nbsp;
					<input maxlength="10" onblur="fnChangePostalAddress();" name="txtMobile2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getMobile2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Is Supplier :</td>
					<td valign="middle">
						<%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ? (((request.getAttribute(Constant.FORM_DATA)  == null) || request.getAttribute(Constant.FORM_DATA) != null && 
								!((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().isSupplier()) ? "No" : "Yes") : "" %>
						<input type="radio" value="N" <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> name="rSupplier" id="rN" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									!((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().isSupplier()) ? "checked=\"checked\"" : "" %> <%=(request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%> /><label <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> for="rN">No</label>
						<input type="radio" value="Y" <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> name="rSupplier" id="rY" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
								((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().isSupplier())  ? "checked=\"checked\"" : "" %>/><label <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ?"style=\"visibility: hidden;\"" : ""%> for="rY">Yes</label>
					</td>
				</tr>
				<tr>
					<td align="right">Grade<span style="color: red">*</span> :</td>
					<td>
						<select name="sGrade" style="width: 200px;">
							<option value="A" <%=(request.getAttribute(Constant.FORM_DATA) != null && (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getGrade().equals("A")) ? "selected=\"selected\"" : "" %>>A</option>
							<option value="B" <%=(request.getAttribute(Constant.FORM_DATA) != null && (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getGrade().equals("B")) ? "selected=\"selected\"" : "" %>>B</option>
							<option value="C" <%=(request.getAttribute(Constant.FORM_DATA) != null && (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getGrade().equals("C")) ? "selected=\"selected\"" : "" %>>C</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Email 1:</td>
					<td><input maxlength="50" name="txtEmail" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getEmail() : ""%>" style="width: 195px;"></td>
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
					<td><input maxlength="50" name="txtWebsite" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getWebsite() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">VAT :</td>
					<td><input maxlength="25" name="txtTin" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">CST :</td>
					<td><input maxlength="25" name="txtCst" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Transport :</td>
					<td><input maxlength="50" name="txtTransport" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getTransport() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Remarks :</td>
					<td><textarea  name="taRemarks" style="width: 195px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer()).getRemark() : ""%></textarea></td>
				</tr>
				<tr>
					<td colspan="3" align="center"><input type="button" onclick="fnSaveCustomer();"
						value="Save"> <input type="reset" value="Reset" onclick="javascript: document.frmAddCustomer.txtCustomerName.focus();"></td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
<%
	MessageModel msgs = request.getAttribute(Constant.MESSAGES)!= null ? 
			(MessageModel) request.getAttribute(Constant.MESSAGES) 
			: new MessageModel();
	if(msgs.getMessages().size() > 1 && msgs.getMessages().get(0).getType().equals(RequestContants.SUCCESS)){	
		CustomerModel cm1 = ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer();
%>
	window.opener.fnAddCustomer1('<%=msgs.getMessages().get(1).getMsg()%>','<%=cm1.getCustomerName()+" - "+cm1.getArea()+" - "+cm1.getCity()%>');
	window.close();
<%
	}
%>
</script>
</body>
</html>