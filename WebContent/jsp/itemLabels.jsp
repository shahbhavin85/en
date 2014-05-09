<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnSelectRow(c,id){
	if(c.checked){
		document.getElementById("desc"+id).disabled = false;
		document.getElementById("no"+id).disabled = false;
		document.getElementById("tr"+id).style.background = 'yellow';
	} else {
		document.getElementById("desc"+id).disabled = document.getElementById("desc"+id).defaultValue;
		document.getElementById("no"+id).disabled = document.getElementById("no"+id).defaultValue;
		document.getElementById("desc"+id).disabled = true;
		document.getElementById("no"+id).disabled = true;
		document.getElementById("tr"+id).style.background = 'transparent';
	}
	btnStatus();
}
function btnStatus(){
	document.getElementById("btnToServer").disabled = true;
// 	var chk = document.forms[0].chk;
	var options = document.forms[0].elements['chk'];
	for (var i = 0; i < options.length; i++)
	{
		if(options[i].checked)
		{
			document.getElementById("btnToServer").disabled = false;
			break;
		}
	}
	return;
}
function fnSave(){
	fnFinalSubmit();
}
</script>
</head>
<body>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Showroom Label</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<center>
				<input id="btnToServer" disabled="disabled" type="button" value= "Print" onclick="fnSave();"/>
			</center>
			<br/>
			<%
				ItemModel[] items = (request.getAttribute(Constant.ITEMS) != null) ? (ItemModel[])(request.getAttribute(Constant.ITEMS)) : new ItemModel[0];
				if(items.length > 0){
			%>
				<table border="1" align="center" width="100%">
					<tr>
						<th>&nbsp;</th>
						<th>ITEM ID</th>
						<th>ITEM NAME / ITEM NUMBER</th>
						<th>LABEL DESC</th>
						<th>MRP</th>
						<th>DESC</th>
						<th>NOS</th>
					</tr>
			<%
					for(int i=0; i<items.length; i++){
			%>		
					<tr id="tr<%=items[i].getItemId()%>">
						<td align="center"><input onchange="fnSelectRow(this,<%=items[i].getItemId()%>)" type="checkbox" name="chk" value="<%=items[i].getItemId() %>"/> </td>
						<td align="center"><%=items[i].getItemId() %></td>
						<td align="center"><%=items[i].getItemName() %> / <%=items[i].getItemNumber() %></td>
						<td align="center"><%=items[i].getItemLabel() %></td>
						<td align="center"><%=items[i].getItemPrice() %></td>
						<td align="center"><input style="text-align: right;" type="text" name="desc<%=items[i].getItemId()%>" id="desc<%=items[i].getItemId()%>" value = "" disabled="disabled"/></td>
						<td align="center"><input style="text-align: right;" type="text" name="no<%=items[i].getItemId()%>" id="no<%=items[i].getItemId()%>" value = "1" disabled="disabled"/></td>
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
	<input type="hidden" name="PRINT" value="Y"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>