<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.ActionType"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.OfferModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript" src="js/newaction.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">New Action</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Enquiry No.<span style="color: red">*</span> :</td>
					<td><input name="txtEnqNo" style="width: 80px;" maxlength="10" value="<%=request.getAttribute("EnqNo")%>" readonly="readonly"/></td>
				</tr>
				<tr>
					<td align="right">Action Type<span style="color: red;">*</span> :</td>
					<td>
							<select name="sType">
	<% 
							String lastAction = ((request.getAttribute("last_action")) != null) ? (String)request.getAttribute("last_action") : "";
							String[] actionType = ActionType.inquiry_actions;
							for(int i=0; i<actionType.length; i++){
								if(!lastAction.equals("")){
									if(lastAction.equals("1") || lastAction.equals("6")){	
										if(((request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) && (i == 6 || i == 4 || i == 8)) 
												|| (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) && (i == 6 || i == 8)){
	%>
								<option value="<%=i%>"><%=actionType[i]%></option>
	<%
										}
									} else if(lastAction.equals("7")) {
										if(i == 0){
	%>
								<option value="<%=i%>"><%=actionType[i]%></option>
	<%										
										}
									} else {
										if(i == 1 || i == 2 || i == 3 || i == 5 || i == 7){
	%>
								<option value="<%=i%>"><%=actionType[i]%></option>
	<%
										}
									}
								} else {
									if(i == 1 || i == 2 || i == 3 || i == 5 || i == 7){
	%>				
								<option value="<%=i%>"><%=actionType[i]%></option>
	<%	
									}
								}
							}
	%>
							</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Go>>" onclick="fnGo1();"/></td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
document.forms[0].sType.focus();
</script>
</body>
</html>