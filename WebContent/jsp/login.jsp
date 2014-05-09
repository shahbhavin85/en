<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.en.util.Constant"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
	<script type="text/javascript" src="js/common.js"></script>
	<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body> 
	<table width="100%" class="appHeader">
	<tr> 
	<td valign="bottom"><span style="color:  white; font-size: 20px;"><%=Constant.TITLE %></span></td>
	</tr>
	</table>
	<form method="post" name="frmLoginPage">
		<div class="loginClass">
			<fieldset>
				<legend class="screenHeader">Login</legend>
				<jsp:include page="messages.jsp"></jsp:include>
				<table cellpadding="3">
					<tr>
						<td align="right">User Name :</td>
						<td><input name="txtUser" style="width: 195px;"></td>
					</tr>
					<tr>
						<td align="right">Password :</td>
						<td><input name="txtPwd" style="width: 195px;" type="password"></td>
					</tr>
					<tr>
						<td align="right">Access Point :</td>
						<td>
							<select name="sAccessPoint" style="width: 200px;">
								<option value="0">ADMIN</option>
								<option value="1">EMPLOYEE MANAGEMENT</option>
								<option value="2">ACCOUNTS</option>
								<option value="3">EXHIBITIONS</option>
								<%
									ArrayList accessPoints = (ArrayList) request.getAttribute(Constant.ACCESS_POINTS);
									Iterator accessPointItr = accessPoints.iterator();
									AccessPointModel temp = null;
									while(accessPointItr.hasNext()){
										temp = (AccessPointModel) accessPointItr.next();
								%>
								<option value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
								<%
									}
								%>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center"><input type="button"
							value="Login" onclick="fnFinalSubmit();"> <input type="reset" value="Reset" onclick="javascript: document.frmLoginPage.txtUser.focus();"></td>
					</tr>
				</table>
			</fieldset>
			<jsp:include page="appFooter.jsp"></jsp:include>
		</div>
		<input type="hidden" name="<%=Constant.HANDLERS%>" value="<%=Constant.LOGIN_HANDLER%>"/>
		<input type="hidden" name="<%=Constant.ACTIONS%>" value="<%=Constant.CHECK_ACCESS%>"/>
	</form>

</body>
<script type="text/javascript">
	document.forms[0].txtUser.focus();
</script>
</html>