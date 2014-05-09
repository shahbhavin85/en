<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.en.model.ConversationModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.ConversationModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" href="css/jquery.treeview.css" />

<script src="js/jquery.js" type="text/javascript"></script>
<script src="js/jquery.treeview.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$("#tree").treeview({
			collapsed: false,
			animated: "medium",
			control:"#sidetreecontrol",
			persist: "location"
		});
	})
	
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader"><%=((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getSubject().toUpperCase()%></legend>
			<jsp:include page="messages.jsp"></jsp:include>
				<table width="100%" border="1" cellpadding="5" style="min-width: 300px;border:#837E7C 1px; background-color: white;">
					<tr>
						<td valign="top">Owner : <%=((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getOwner().getUserName()%></td>
						<td>Time Posted : <%=(new SimpleDateFormat("dd/MM/yyyy hh:mm a")).format(((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getTimeStamp())%></td>
					</tr>
					<tr>
						<td colspan="2">USERS : 
							<% 
								for (int i=0; i<((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getRecipients().size(); i++){
							%>
							<label style="font-weight : bolder;  color: <%=((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getRecipients().get(i).getColor() %>; padding: 2px; margin: 2px;"><%=((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getRecipients().get(i).getUserName()%></label>
							<%
								}
							%>
							</td>
					</tr>
				</table>
			<%
				if(((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getStatus() == 0){
			%>
			<table width="100%" align="center">
				<tr>
					<td align="center">
			<%
				if(((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId().equals(((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getOwner().getUserId())){
			%>
					<input type="button" value="MAIN QUESTION" onclick="fnComment(0);"/>&nbsp;&nbsp;
			<%
				}
			%>
					<input type="button" value="REFRESH" onclick="fnRefresh();"/>
			<%
				if(((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId().equals(((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getOwner().getUserId())){
			%>
					&nbsp;&nbsp;<input type="button" value="CLOSE" onclick="fnClose();"/>
			<%
				}
			%>
					</td>
				</tr>
			</table>
			<%
				} else {
					if(((UserModel)request.getSession().getAttribute(Constant.USER)).getUserId().equals(((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getOwner().getUserId())){
			%>
			<table width="100%" align="center">
				<tr>
					<td align="center">
						<input type="button" value="RE OPEN" onclick="fnReopen();"/>
					</td>
				</tr>
			</table>
			<%
					}
				}
				if(((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getTasks().size() > 0){
			%>
				<%=((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getChildHtml()%>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<div id="taskWindow" style="display: none; width:400px; background-color:#D3BECF; border:#2E0854 solid 3px; -moz-border-radius: 10px; border-radius: 10px; -moz-box-shadow: -2px -2px 3px #d9d9d9; -webkit-box-shadow: -2px -2px 3px #d9d9d9; box-shadow: -2px -2px 3px #d9d9d9; padding:10px; position: fixed; margin-left:30%; top:10%; z-index:1002;">
		<fieldset>
			<legend style="font-weight: bold; color: blue;">Comment</legend>
			<table cellspacing="5">
				<tr>
					<td align="right" valign="top">Parent ID :</td>
					<td><input type="text" name="txtParentId" readonly="readonly" /></td>
				</tr>
				<tr>
					<td align="right" valign="top">Description :</td>
					<td><textarea style="text-transform: uppercase;" name="taDescription" cols="50" rows="5"></textarea></td>
				</tr>
				<tr>
					<td align="right">File :</td>
					<td><input name="file" type="file"/></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input type="button" value="Send" onclick="fnSave();"/> <input type="button" value="Reset" onclick="fnReset();"/> <input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="txtConversationId" value="<%=((ConversationModel) request.getAttribute(Constant.FORM_DATA)).getConversationId()%>"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
function fnComment(parentId){
	<%
	if(((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getStatus() == 0){
	%>
	document.forms[0].txtParentId.value = parentId;
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOn';
	document.documentElement.style.overflow = "hidden";
	document.getElementById("taskWindow").style.display = 'block';
	return;
	<%
	} else {
	%>
	alert('Conversation is closed');
	return;
	<%
	}
	%>
}

function fnReset(){
	//document.forms[0].txtParentId.value= '';
	document.forms[0].taDescription.value= '';
	document.forms[0].file.value = '';
	return;
}

function fnCancel(){
	document.forms[0].txtParentId.value= '';
	document.forms[0].taDescription.value= '';
	document.forms[0].file.value = '';
	document.documentElement.style.overflow = "visible";
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOff';
	document.getElementById("taskWindow").style.display = 'none';
	return;	
}

function fnSave(){
	if(document.forms[0].taDescription.value == ''){
		alert('Please provide description.');
		return;
	}
	document.forms[0].enctype="multipart/form-data";
	document.forms[0].HANDLERS.value = 'MESSENGER_HANDLER';
	document.forms[0].ACTIONS.value = 'ADD_COMMENT';
	fnFinalSubmit();
}

function fnRefresh(){
	document.forms[0].HANDLERS.value = 'MESSENGER_HANDLER';
	document.forms[0].ACTIONS.value = 'SHOW_CONVERSATION';
	fnFinalSubmit();
}

function fnClose(){
	document.forms[0].HANDLERS.value = 'MESSENGER_HANDLER';
	document.forms[0].ACTIONS.value = 'CLOSE_CONVERSATION';
	fnFinalSubmit();
}

function fnReopen(){
	document.forms[0].HANDLERS.value = 'MESSENGER_HANDLER';
	document.forms[0].ACTIONS.value = 'REOPEN_CONVERSATION';
	fnFinalSubmit();
}

<%
if(((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getStatus() == 0){
%>
setTimeout('fnRefresh()',300000);
<%
}

Iterator<Integer> itr = ((ConversationModel)request.getAttribute(Constant.FORM_DATA)).getNewMessages().iterator();
while(itr.hasNext()){
%>
	document.getElementById("task<%=itr.next()%>").style.background = "yellow";
<%
}
%>
</script>
</body>
</html>