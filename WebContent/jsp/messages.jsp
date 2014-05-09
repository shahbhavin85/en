<%@page import="java.util.Iterator"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.Message"%>
<%@page import="com.en.model.MessageModel"%>
<%

MessageModel msgs = request.getAttribute(Constant.MESSAGES)!= null ? 
		(MessageModel) request.getAttribute(Constant.MESSAGES) 
		: new MessageModel();

String type = "";
String msg = "";
Message message = null;
Iterator<Message> msgItr = null;
		
if(msgs != null && msgs.getMessages().size() > 0) {
%>
	<fieldset id="message">
		<legend class="subHeader">Messages</legend>
		<ul>
<%
	msgItr = msgs.getMessages().iterator();
	while(msgItr.hasNext()){
		message = (Message) msgItr.next();
		type = message.getType();
		msg = message.getMsg();
		if(type.equalsIgnoreCase(Constant.ALERT)){
%>
			<li><font style="color: #444444; font-style: italic;"><%=msg%></font></li>
<%
		} else if(type.equalsIgnoreCase(Constant.ERROR)){
%>
			<li><font style="color: red; font-style: italic;"><%=msg%></font></li>
<%
		} else if(type.equalsIgnoreCase(Constant.SUCCESS)){
%>
			<li><font style="color: green; font-style: italic;"><%=msg%></font></li>
<%
		}
	}
%>
		</ul>
	</fieldset>
<%
}
%>