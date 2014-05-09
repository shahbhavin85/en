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
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnSave(){
	if(getCheckBoxValues(document.forms[0].custChk) == ""){
		alert('Please select the master record');
		return;
	} else {
		document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
		document.forms[0].ACTIONS.value = 'MERGE_CUSTOMER_2';
		fnFinalSubmit();
	}
}
function getCheckBoxValues(cb){
	var val = "";
	var iL = cb.length;
	for(var k=0; k<iL; k++){
		if(cb[k].checked){
			val = val + cb[k].value+"|";
		}
	}
	return val;
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Customer Merge</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<center><font style="font-size: 15px; color: blue; font-weight: bold;"> Please select the master record</font></center><br/>
			<%
				String ids = "";
				CustomerModel[] customers = (request.getAttribute("custLst") != null) ? (CustomerModel[]) request.getAttribute("custLst") : new CustomerModel[0];
				if(customers.length>0){
			%>
			<table border="1" width="100%" style="font-size: 11px;">
				<tr style="background-color: black; color: white;">
					<th>&nbsp;</th>
					<th>Customer Name</th>
					<th>Address<br/>Area<br/>Pin Code</th>
					<th>TIN<br/>CST</th>
					<th>Std Code - Phone1 / Phone2</th>
					<th>Mobile1 / Mobile2</th>
					<th>Email1<br/>Email2</th>
					<th>Contact Person</th>
					<th>Open Bal.</th>
					<th>TXN<br/>CNT</th>
				</tr>
			<%
				for(int i=0; i<customers.length; i++){
					ids = ids + customers[i].getCustomerId() +"|";
			%>
				<tr id="tr<%=customers[i].getCustomerId()%>">
					<td align="center" valign="top"><input type="radio" onchange="fnChanged(this);" name="custChk" value="<%=customers[i].getCustomerId()%>"/></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtName<%=customers[i].getCustomerId()%>" name="txtName<%=customers[i].getCustomerId()%>" maxlength="50" value="<%=customers[i].getCustomerName()%>" style="width: 195px;"></td>
					<td align="center" valign="top"><textarea disabled="disabled" id="taAddress<%=customers[i].getCustomerId() %>" name="taAddress<%=customers[i].getCustomerId() %>" style="width: 195px; height : 50px;" maxlength="250"><%=customers[i].getAddress()%></textarea>
									<br/><input disabled="disabled" id="txtArea<%=customers[i].getCustomerId()%>" name="txtArea<%=customers[i].getCustomerId()%>" maxlength="50" value="<%=customers[i].getArea()%>" style="width: 195px;">
									<br/><input disabled="disabled" id="txtPincode<%=customers[i].getCustomerId()%>" name="txtPincode<%=customers[i].getCustomerId()%>" maxlength="6" value="<%=customers[i].getPincode()%>" style="width: 195px;"></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtTIN<%=customers[i].getCustomerId()%>" name="txtTIN<%=customers[i].getCustomerId()%>" maxlength="25" value="<%=customers[i].getTin()%>" style="width: 100px;">
									<br/><input disabled="disabled" id="txtCST<%=customers[i].getCustomerId()%>" name="txtCST<%=customers[i].getCustomerId()%>" maxlength="25" value="<%=customers[i].getCst()%>" style="width: 100px;"></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtStdCode<%=customers[i].getCustomerId()%>" name="txtStdCode<%=customers[i].getCustomerId()%>" maxlength="6" value="<%=customers[i].getStdcode()%>" style="width: 60px;"> - 
									<input disabled="disabled" id="txtPhone1<%=customers[i].getCustomerId()%>" name="txtPhone1<%=customers[i].getCustomerId()%>" maxlength="10" value="<%=customers[i].getPhone1()%>" style="width: 100px;">
									<br/><input disabled="disabled" id="txtPhone2<%=customers[i].getCustomerId()%>" name="txtPhone2<%=customers[i].getCustomerId()%>" maxlength="10" value="<%=customers[i].getPhone2()%>" style="width: 100px;"></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtMobile1<%=customers[i].getCustomerId()%>" name="txtMobile1<%=customers[i].getCustomerId()%>" maxlength="10" value="<%=customers[i].getMobile1()%>" style="width: 100px;">
									<br/><input disabled="disabled" id="txtMobile2<%=customers[i].getCustomerId()%>" name="txtMobile2<%=customers[i].getCustomerId()%>" maxlength="10" value="<%=customers[i].getMobile2()%>" style="width: 100px;"></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtEmail<%=customers[i].getCustomerId()%>" name="txtEmail<%=customers[i].getCustomerId()%>" maxlength="50" value="<%=customers[i].getEmail()%>" style="width: 195px;">
									<br/><input disabled="disabled" id="txtEmail1<%=customers[i].getCustomerId()%>" name="txtEmail1<%=customers[i].getCustomerId()%>" maxlength="50" value="<%=customers[i].getEmail1()%>" style="width: 195px;"></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtContactPerson<%=customers[i].getCustomerId()%>" name="txtContactPerson<%=customers[i].getCustomerId()%>" maxlength="50" value="<%=customers[i].getContactPerson()%>" style="width: 100px;"></td>
					<td align="center" valign="top"><input disabled="disabled" id="txtOpenBal<%=customers[i].getCustomerId()%>" name="txtOpenBal<%=customers[i].getCustomerId()%>" maxlength="50" value="<%=customers[i].getOpenBal()%>" style="width: 100px;"></td>
					<td align="center" valign="top" style="font-size: 14px; font-weight: bold;"><%=customers[i].getTxnCnt()%></td>
				</tr>
			<%
				}
			%>
				<tr>
					<td colspan="10" align="center"><input type="button" value="Save" onclick="fnSave();"/></td>
				</tr>
			</table>
			<%	
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCity" value="<%=(request.getAttribute("txtCity"))%>">
	<input type="hidden" name="ids" value="<%=ids%>">
</form>
<script type="text/javascript">
var prevVal = "";
function fnChanged(k){
	if(prevVal != "")
		fnChangeStatus(prevVal, true);
	prevVal = k.value;
	fnChangeStatus(prevVal, false);
}
function fnChangeStatus(id, state){
	document.getElementById("txtName"+id).disabled = state;
	document.getElementById("taAddress"+id).disabled = state;
	document.getElementById("txtArea"+id).disabled = state;
	document.getElementById("txtPincode"+id).disabled = state;
	document.getElementById("txtTIN"+id).disabled = state;
	document.getElementById("txtCST"+id).disabled = state;
	document.getElementById("txtStdCode"+id).disabled = state;
	document.getElementById("txtPhone1"+id).disabled = state;
	document.getElementById("txtPhone2"+id).disabled = state;
	document.getElementById("txtMobile1"+id).disabled = state;
	document.getElementById("txtMobile2"+id).disabled = state;
	document.getElementById("txtEmail"+id).disabled = state;
	document.getElementById("txtEmail1"+id).disabled = state;
	document.getElementById("txtContactPerson"+id).disabled = state;
	document.getElementById("txtOpenBal"+id).disabled = state;
}
</script>
</body>
</html>