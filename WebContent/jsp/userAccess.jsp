<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/userAccess.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">User Access</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">User Name <span style="color: red">*</span> :</td>
					<td>
						<select name="txtUserId" style="width: 200px;" onchange="fnGetUserAccessDetails();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">ADMIN : </td>
					<td><input type="radio" name="r0" id="rAdminYes" onchange="fnAdminChange();" value= "Y"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("0") != -1) ? "checked=\"checked\"" : "" %> /><label for="rAdminYes">Yes</label>&nbsp;&nbsp;
					<input type="radio" name="r0" id="rAdminNo" onchange="fnAdminChange();" value= "N" <%=((request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("0") == -1) || request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : "" %>/><label for="rAdminNo">No</label></td>
				</tr>
				<tr>
					<td align="right">Salesman : </td>
					<td><input type="radio" name="r1" id="r1Yes" value= "Y" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("1") != -1) ? "checked=\"checked\"" : "" %>/><label for="r1Yes">Yes</label>&nbsp;&nbsp;
					<input type="radio" name="r1" id="r1No" value= "N" <%=((request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("1") == -1) || request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : "" %>/><label for="r1No">No</label></td>
				</tr>
				<tr>
					<td align="right">Accounts : </td>
					<td><input type="radio" name="r2" id="r2Yes" value= "Y" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("2") != -1) ? "checked=\"checked\"" : "" %>/><label for="r2Yes">Yes</label>&nbsp;&nbsp;
					<input type="radio" name="r2" id="r2No" value= "N" <%=((request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("2") == -1) || request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : "" %>/><label for="r2No">No</label></td>
				</tr>
				<tr>
					<td align="right">Exhibition : </td>
					<td><input type="radio" name="r3" id="r3Yes" value= "Y" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("3") != -1) ? "checked=\"checked\"" : "" %>/><label for="r3Yes">Yes</label>&nbsp;&nbsp;
					<input type="radio" name="r3" id="r3No" value= "N" <%=((request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf("3") == -1) || request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : "" %>/><label for="r3No">No</label></td>
				</tr>
				<%
					AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
					AccessPointModel temp = null;
					int k = 4;
					for(int i=0; i<accessPoints.length; i++){
						temp = accessPoints[i];
				%>
				<tr>
					<td align="right"><%=temp.getAccessName() %> - <%=temp.getCity() %> : </td>
					<td><input type="radio" name="r<%=temp.getAccessId()%>" id="r<%=k%>Yes" value= "Y" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf(temp.getAccessId()+"") != -1) ? "checked=\"checked\"" : "" %>/><label for="r<%=temp.getAccessId()%>Yes">Yes</label>&nbsp;&nbsp;
					<input type="radio" name="r<%=temp.getAccessId()%>" id="r<%=k%>No" value= "N" <%=((request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoints().indexOf(temp.getAccessId()+"") == -1) || request.getAttribute(Constant.FORM_DATA) == null) ? "checked=\"checked\"" : "" %>/><label for="r<%=temp.getAccessId()%>No">No</label></td>
				</tr>
				<%
					k++;
					}
				%>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdateUserAccessDetails();" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%>
						value="Modify"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtAccessNos" value="<%=(accessPoints.length+4)%>"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>