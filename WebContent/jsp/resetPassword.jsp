<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
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
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Reset Password</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">User Name <span style="color: red">*</span> :</td>
					<td>
						<select name="txtUserId" style="width: 200px;" onchange="populateUserId(this);">
							<option value="--">-------</option>
							<%
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">User Id <span style="color: red">*</span> :</td>
					<td><input type="text" name="txtuser" maxlength="25" readonly="readonly"/></td>
				</tr>
				<tr>
					<td align="right">Password <span style="color: red">*</span> :</td>
					<td><input type="password" name="txtPassword" maxlength="25"/></td>
				</tr>
				<tr>
					<td align="right">Confirm Password <span style="color: red">*</span> :</td>
					<td><input type="password" name="txtRePassword" maxlength="25"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdatePassword();" value="Reset Password"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
function fnUpdatePassword(){
	if(document.forms[0].txtUserId.value == '--'){
		alert('Please select the user id.');
		return;
	} else if(trim(document.forms[0].txtPassword.value) == '' || trim(document.forms[0].txtRePassword.value) == '') {
		alert('Password text field cannot be blank.');
		return;
	} else if(trim(document.forms[0].txtPassword.value) != trim(document.forms[0].txtRePassword.value)) {
		alert('Confirm Password is different from Password.');
		return;
	} else {
		document.forms[0].HANDLERS.value = 'USER_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_PASSWORD';
		fnFinalSubmit();
	}
}

function populateUserId(comp){
	document.forms[0].txtuser.value = comp.value;
}
</script>
</body>
</html>