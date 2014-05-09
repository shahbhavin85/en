<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.ConversationModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.ConversationModel"%>
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
			<legend class="screenHeader">New Conversation</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Creator User <span style="color: red">*</span> :</td>
					<td style="font-weight: bold;"><%=((UserModel) request.getSession().getAttribute(Constant.USER )).getUserName()%></td>
				</tr>
				<tr>
					<td align="right">Subject <span style="color: red">*</span> :</td>
					<td><input name="txtSubject" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getSubject() : ""%>" style="width: 545px; text-transform: uppercase;" maxlength="50"></td>
				</tr>
				<tr>
					<td align="right">Users <span style="color: red">*</span> :</td>
					<td>
						<p style="height: 200px; overflow: auto; border: 1px solid #eee; background: white; color: #000; margin-bottom: 1.5em;">
						<%
							UserModel[] users = request.getAttribute(Constant.USERS) != null ?
													(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
							for(int i=0; i<users.length;i++){
								if(users[i].getUserId().equals(((UserModel) request.getSession().getAttribute(Constant.USER)).getUserId()))
									continue;
						%>
							<label><input type="checkbox"  name="chkUsers" value=<%=users[i].getUserId() %> /><%=users[i].getUserName() %></label><%=(i != users.length-1)  ? "<br />" : ""%>
						<%
							}
						%>
						</p>
												
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button"
						value="Start" onclick="fnAddConversation();"> <input type="reset" value="Reset" onclick="javascript: document.frmAddAccessPoint.txtSubject.focus();"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript">
function fnAddConversation(){
	if(document.forms[0].txtSubject.value == ""){
		alert('Please provide the subject');
		document.forms[0].txtSubject.focus();
		return;
	}
	var iCnt = document.forms[0].chkUsers.length;
	var selected = false;
	if(iCnt == 1){
		if(document.forms[0].chkUsers.checked){
			selected = true;
		}
	} else {
		if(iCnt == 1){
			if(document.forms[0].chkUsers.checked){
				selected = true;
			}
		} else {
			for(var i=0; i<iCnt; i++){
				if(document.forms[0].chkUsers[i].checked){
					selected = true;
					break;
				}
			}
		}
	}
	if(!selected) {
		alert('Please select the users.');
		return;
	}
	document.forms[0].HANDLERS.value = "MESSENGER_HANDLER";
	document.forms[0].ACTIONS.value = "START_CONVERSATION";
	fnFinalSubmit();
}
</script>
</html>