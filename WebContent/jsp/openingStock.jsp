<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnSave(){
	document.forms[0].HANDLERS.value = 'BRANCH_STOCK_HANDLER';
	document.forms[0].ACTIONS.value = 'UPDATE_OPEN_STOCK';
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Opening Stock Details</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<center>
				<input id="btnToServer" type="button" value= "Save" onclick="fnSave();"/>
			</center>
			<br/>
			<%
				SalesItemModel[] items = (request.getAttribute(Constant.FORM_DATA) != null) ? (SalesItemModel[])(request.getAttribute(Constant.FORM_DATA)) : new SalesItemModel[0];
				if(items.length > 0){
			%>
				<table border="1" align="center" width="100%">
					<tr>
						<th>ITEM ID</th>
						<th>ITEM NAME / ITEM NUMBER</th>
						<th>QTY</th>
					</tr>
			<%
					for(int i=0; i<items.length; i++){
			%>		
					<tr>
						<td align="center"><input style="text-align: center; width: 50px;" type="text" name="id<%=i%>" value = "<%=items[i].getItem().getItemId()%>" readonly="readonly"/></td>
						<td align="center"><%=items[i].getItem().getItemName() %> / <%=items[i].getItem().getItemNumber() %></td>
						<td align="center"><input style="text-align: right;" type="text" name="qty<%=i%>"value = "<%=items[i].getQty() %>"/></td>
					</tr>		
			<%
					}
			%>
				</table>
			<%
				}
			%>
			<br/>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="count" value="<%=items.length%>"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>