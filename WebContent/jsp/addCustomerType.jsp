<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.RequestContants"%>
<%@page import="com.mysql.jdbc.Constants"%>
<%@page import="com.en.model.MessageModel"%>
<%@page import="com.en.model.EnquiryModel"%>
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
function fnSaveCity(){
	fnFinalSubmit();
}
</script>
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add Customer Type</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer Type <span style="color: red">*</span> :</td>
					<td><input maxlength="50" name="txtType" value="<%=(request.getAttribute("type") != null) ? (request.getAttribute("type")) : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnSaveCity();"
						value="Save"> <input type="reset" value="Reset" onclick="javascript: document.frmAddCustomer.txtType.focus();"></td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="hdnAdd" value ='1'/>
</form>
<script type="text/javascript">
<%
	MessageModel msgs = request.getAttribute(Constant.MESSAGES)!= null ? 
			(MessageModel) request.getAttribute(Constant.MESSAGES) 
			: new MessageModel();
	if(msgs.getMessages().size()>0 && msgs.getMessages().get(0).getType().equals(RequestContants.SUCCESS)){	
%>
	window.opener.fnAddCustomerType('<%=request.getAttribute("type")%>','<%=request.getAttribute("type")%>');
	window.close();
<%
	}
%>
</script>
</body>
</html>