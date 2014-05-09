<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/accesspoint.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add New Access Point</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Access Point <span style="color: red">*</span> :</td>
					<td><input name="txtAccessName" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getAccessName() : ""%>" style="width: 195px;" maxlength="50"></td>
				</tr>
				<tr>
					<td align="right">Bill Prefix<span style="color: red">*</span> :</td>
					<td><input name="txtBillPre" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBillPrefix() : ""%>" style="width: 195px;" maxlength="3"></td>
				</tr>
				<tr>
					<td align="right">Address :</td>
					<td><textarea  name="taAddress" style="width: 195px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getAddress() : ""%></textarea></td>
				</tr>
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtCity" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getCity() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">District :</td>
					<td><input maxlength="50" name="txtDistrict" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getDistrict() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">State <span style="color: red">*</span> :</td>
					<td>
						<select name="txtState">
							<option value = "--">-------</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("ANDAMAN AND NICOBAR ISLANDS")) ? "selected=\"selected\"" : "" %> value = "ANDAMAN AND NICOBAR ISLANDS">ANDAMAN AND NICOBAR ISLANDS</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("ANDHRA PRADESH")) ? "selected=\"selected\"" : "" %> value = "ANDHRA PRADESH">ANDHRA PRADESH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("ARUNACHAL PRADESH")) ? "selected=\"selected\"" : "" %> value = "ARUNACHAL PRADESH">ARUNACHAL PRADESH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("ASSAM")) ? "selected=\"selected\"" : "" %> value = "ASSAM">ASSAM</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("BIHAR")) ? "selected=\"selected\"" : "" %> value = "BIHAR">BIHAR</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("CHANDIGARH")) ? "selected=\"selected\"" : "" %> value = "CHANDIGARH">CHANDIGARH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("CHHATTISGARH")) ? "selected=\"selected\"" : "" %> value = "CHHATTISGARH">CHHATTISGARH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("DAMAN AND DIU")) ? "selected=\"selected\"" : "" %> value = "DAMAN AND DIU">DAMAN AND DIU</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("DELHI")) ? "selected=\"selected\"" : "" %> value = "DELHI">DELHI</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("GOA")) ? "selected=\"selected\"" : "" %> value = "GOA">GOA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("GUJARAT")) ? "selected=\"selected\"" : "" %> value = "GUJARAT">GUJARAT</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("HARYANA")) ? "selected=\"selected\"" : "" %> value = "HARYANA">HARYANA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("HIMACHAL PRADESH")) ? "selected=\"selected\"" : "" %> value = "HIMACHAL PRADESH">HIMACHAL PRADESH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("JAMMU AND KASHMIR")) ? "selected=\"selected\"" : "" %> value = "JAMMU AND KASHMIR">JAMMU AND KASHMIR</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("JHARKHAND")) ? "selected=\"selected\"" : "" %> value = "JHARKHAND">JHARKHAND</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("KARNATAKA")) ? "selected=\"selected\"" : "" %> value = "KARNATAKA">KARNATAKA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("KERALA")) ? "selected=\"selected\"" : "" %> value = "KERALA">KERALA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("LAKSHADEEP")) ? "selected=\"selected\"" : "" %> value = "LAKSHADEEP">LAKSHADEEP</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("MADYA PRADESH")) ? "selected=\"selected\"" : "" %> value = "MADYA PRADESH">MADYA PRADESH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("MAHARASHTRA")) ? "selected=\"selected\"" : "" %> value = "MAHARASHTRA">MAHARASHTRA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("MANIPUR")) ? "selected=\"selected\"" : "" %> value = "MANIPUR">MANIPUR</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("MEGHALAYA")) ? "selected=\"selected\"" : "" %> value = "MEGHALAYA">MEGHALAYA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("MIZORAM")) ? "selected=\"selected\"" : "" %> value = "MIZORAM">MIZORAM</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("NAGALAND")) ? "selected=\"selected\"" : "" %> value = "NAGALAND">NAGALAND</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("ORISSA")) ? "selected=\"selected\"" : "" %> value = "ORISSA">ORISSA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("PONDICHERRY")) ? "selected=\"selected\"" : "" %> value = "PONDICHERRY">PONDICHERRY</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("PUNJAB")) ? "selected=\"selected\"" : "" %> value = "PUNJAB">PUNJAB</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("RAJASTHAN")) ? "selected=\"selected\"" : "" %> value = "RAJASTHAN">RAJASTHAN</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("SIKKIM")) ? "selected=\"selected\"" : "" %> value = "SIKKIM">SIKKIM</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("TAMIL NADU")) ? "selected=\"selected\"" : "" %> value = "TAMIL NADU">TAMIL NADU</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("TRIPURA")) ? "selected=\"selected\"" : "" %> value = "TRIPURA">TRIPURA</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("UTTAR PRADESH")) ? "selected=\"selected\"" : "" %> value = "UTTAR PRADESH">UTTAR PRADESH</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("UTTARANCHAL")) ? "selected=\"selected\"" : "" %> value = "UTTARANCHAL">UTTARANCHAL</option>
							<option <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getState().equals("WEST BENGAL")) ? "selected=\"selected\"" : "" %> value = "WEST BENGAL">WEST BENGAL</option>
						</select> 
					</td>
				</tr>
				<tr>
					<td align="right">Pin Code :</td>
					<td><input maxlength="6" name="txtPinCode" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getPincode() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">VAT :</td>
					<td><input maxlength="25" name="txtTin" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getVat() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">CST :</td>
					<td><input maxlength="25" name="txtCst" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getCst() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Desc With Cust Tin :</td>
					<td><input maxlength="30" name="txtWithTin" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getWithTin() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Desc W/o Cust Tin :</td>
					<td><input maxlength="30" name="txtNoTin" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getNoTin() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Std Code - Phone :</td>
					<td><input maxlength="6" name="txtStdCode" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getStdcode() : ""%>" style="width: 60px;">&nbsp;-&nbsp;
					<input maxlength="10" name="txtPhone1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getPhone1() : ""%>" style="width: 195px;"> &nbsp;&nbsp;/&nbsp;&nbsp;
					<input maxlength="10" name="txtPhone2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getPhone2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Mobile :</td>
					<td><input maxlength="10" name="txtMobile1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getMobile1() : ""%>" style="width: 195px;"> &nbsp;&nbsp;/&nbsp;&nbsp;
					<input maxlength="10" name="txtMobile2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getMobile2() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Email :</td>
					<td><input maxlength="50" name="txtEmail" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getEmail() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Functional : </td>
					<td><input type="radio" name="rFunctional" id="rFunctionalYes" value="Y" <%=((request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getFunctional().equals("Y")) || request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : ""%>/><label for="rFunctionalYes">Yes</label>&nbsp;&nbsp;
					<input type="radio" name="rFunctional" id="rFunctionalNo" value="N" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getFunctional().equals("N")) ? "checked=\"checked\"" : ""%>/><label for="rFunctionalNo">No</label> </td>
				</tr>
				<tr>
					<td align="right">Bank 1 Name :</td>
					<td><input maxlength="100" name="txtBankName1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankName1() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 1 Branch :</td>
					<td><input maxlength="100" name="txtBankBranch1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankBranch1() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 1 AC Number :</td>
					<td><input maxlength="25" name="txtBankAc1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankAc1() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 1 IFSC :</td>
					<td><input maxlength="25" name="txtBankIFSC1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankIfsc1() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 2 Name :</td>
					<td><input maxlength="100" name="txtBankName2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankName2() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 2 Branch :</td>
					<td><input maxlength="100" name="txtBankBranch2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankBranch2() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 2 AC Number :</td>
					<td><input maxlength="25" name="txtBankAc2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankAc2() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td align="right">Bank 2 IFSC :</td>
					<td><input maxlength="25" name="txtBankIFSC2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((AccessPointModel)request.getAttribute(Constant.FORM_DATA)).getBankIfsc2() : ""%>" style="width: 195px;"></td>				
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnSaveAccessPoint();"
						value="Save"> <input type="reset" value="Reset" onclick="javascript: document.frmAddAccessPoint.txtAccessName.focus();"></td>
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