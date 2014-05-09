<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.RequestContants"%>
<%@page import="com.mysql.jdbc.Constants"%>
<%@page import="com.en.model.MessageModel"%>
<%@page import="com.en.model.EnquiryModel"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnSaveCity(){
	document.forms[0].ACTIONS.value = 'ADD_CITY';
	fnFinalSubmit();
}
</script>
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add New City</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtCity" value="<%=(request.getAttribute("city") != null) ? (request.getAttribute("city")) : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">District <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtDistrict" value="<%=(request.getAttribute("district") != null) ? (request.getAttribute("district")) : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">State / Other Countries <span style="color: red">*</span> :</td>
					<td>
						<select name="txtState">
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
							<option value = "SRI LANKA" style="background-color: black; color: white;">SRI LANKA</option>
						</select> 
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnSaveCity();"
						value="Save"> <input type="reset" value="Reset" onclick="javascript: document.frmAddCustomer.txtCity.focus();">
						<input type="button" value="Close" onclick="window.close();"> </td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
<%
	MessageModel msgs = request.getAttribute(Constant.MESSAGES)!= null ? 
			(MessageModel) request.getAttribute(Constant.MESSAGES) 
			: new MessageModel();
	if(msgs.getMessages().size()>0 && msgs.getMessages().get(0).getType().equals(RequestContants.SUCCESS)){	
%>
	window.opener.fnAddCity('<%=request.getAttribute("city")%>','<%=request.getAttribute("city")%>');
	window.close();
<%
	}
%>
</script>
</body>
</html>