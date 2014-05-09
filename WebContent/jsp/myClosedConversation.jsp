<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<style type="text/css">
@-webkit-keyframes invalid {
  from { background-color: red; }
  to { background-color: inherit; }
}
@-moz-keyframes invalid {
  from { background-color: red; }
  to { background-color: inherit; }
}
@-o-keyframes invalid {
  from { background-color: red; }
  to { background-color: inherit; }
}
@keyframes invalid {
  from { background-color: red; }
  to { background-color: inherit; }
}
.invalid {
  -webkit-animation: invalid 1s infinite; /* Safari 4+ */
  -moz-animation:    invalid 1s infinite; /* Fx 5+ */
  -o-animation:      invalid 1s infinite; /* Opera 12+ */
  animation:         invalid 1s infinite; /* IE 10+ */
}
</style>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">MY CLOSED CONVERSATION</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table style="min-width: 850px;">
				<tr>
					<td align="center"><input type="button" value="BACK TO INBOX" onclick="fnGoToPage('MESSENGER_HANDLER','MY_CONVERSATION');"/></td>
				</tr>
			</table>
			<table border="1" style="min-width: 850px;">
				<tr>
					<th>SUBJECT</th>
					<th>OWNER</th>
					<th>STARTED FROM</th>
					<th>ACTIONS</th>
				</tr>
				<%
					ConversationModel[] lst = (request.getAttribute(Constant.FORM_DATA) != null) ? (ConversationModel[])request.getAttribute(Constant.FORM_DATA) : new ConversationModel[0];
					if(lst.length == 0){
				%>
					<tr><th colspan="5">No Conversation Open</th></tr>
				<%		
					} else {
						for(int i=0; i<lst.length; i++){
				%>
					<tr <%=(lst[i].getNewCount() > 0) ? "class=\"invalid\"" : "" %> >
						<td><%=lst[i].getSubject() %></td>
						<td align="center"><%=lst[i].getOwner().getUserName() %></td>
						<td align="center"><%=(new SimpleDateFormat("dd/MM/yyyy hh:mm a")).format(lst[i].getTimeStamp()) %></td>
						<td align="center"><input type="button" value="Open" onclick="fnOpenConversation(<%=lst[i].getConversationId()%>)"/></td>
					</tr>
				<%		
						}
					}
				%>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtConversationId" value=""/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
function fnOpenConversation(id){
	document.forms[0].txtConversationId.value = id;
	document.forms[0].HANDLERS.value = 'MESSENGER_HANDLER';
	document.forms[0].ACTIONS.value = 'SHOW_CONVERSATION';
	fnFinalSubmit();
}
</script>
</body>
</html>