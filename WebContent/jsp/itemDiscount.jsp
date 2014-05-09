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
		document.getElementById("dis"+id).disabled = false;
		document.getElementById("txtFromDate"+id).disabled = false;
		document.getElementById("txtToDate"+id).disabled = false;
		document.getElementById("tr"+id).style.background = 'yellow';
	} else {
		document.getElementById("dis"+id).disabled = document.getElementById("dis"+id).defaultValue;
		document.getElementById("txtFromDate"+id).disabled = document.getElementById("txtFromDate"+id).defaultValue;
		document.getElementById("txtToDate"+id).disabled = document.getElementById("txtToDate"+id).defaultValue;
		document.getElementById("dis"+id).disabled = true;
		document.getElementById("txtFromDate"+id).disabled = true;
		document.getElementById("txtToDate"+id).disabled = true;
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
	document.forms[0].HANDLERS.value = 'ITM_HANDLER';
	document.forms[0].ACTIONS.value = 'ITM_DIS';
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Item Discount Screen</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<center>
				<input id="btnToServer" disabled="disabled" type="button" value= "Save" onclick="fnSave();"/>
			</center>
			<br/>
			<%
				ItemModel[] items = (request.getAttribute(Constant.FORM_DATA) != null) ? (ItemModel[])(request.getAttribute(Constant.FORM_DATA)) : new ItemModel[0];
				if(items.length > 0){
			%>
				<table border="1" align="center" width="100%">
					<tr>
						<th>&nbsp;</th>
						<th>ITEM ID</th>
						<th>ITEM NAME / ITEM NUMBER</th>
						<th>PRICE</th>
						<th>DISCOUNT</th>
						<th>FROM DATE</th>
						<th>TO DATE</th>
					</tr>
			<%
					for(int i=0; i<items.length; i++){
			%>		
					<tr id="tr<%=items[i].getItemId()%>">
						<td align="center"><input onchange="fnSelectRow(this,<%=items[i].getItemId()%>)" type="checkbox" name="chk" value="<%=items[i].getItemId() %>"/> </td>
						<td align="center"><%=items[i].getItemId() %></td>
						<td align="center"><%=items[i].getItemName() %> / <%=items[i].getItemNumber() %></td>
						<td align="center"><%=items[i].getItemPrice() %></td>
						<td align="center"><input style="text-align: right;" type="text" name="dis<%=items[i].getItemId()%>" id="dis<%=items[i].getItemId()%>" value = "<%=items[i].getDiscount() %>" disabled="disabled"/>%</td>
						<td align="center"><input name="txtFromDate<%=items[i].getItemId()%>" id="txtFromDate<%=items[i].getItemId()%>" style="width: 195px;" readonly="readonly" disabled="disabled" onclick="scwShow(scwID('txtFromDate<%=items[i].getItemId()%>'),event);" 
							value="<%=(!items[i].getFromDate().equals("0000-00-00")) ?  items[i].getFromDate() : "" %>"><img
		                                    src='images/date.gif'
		                                    title='Click Here' alt='Click Here'
		                                    onclick="scwShow(scwID('txtFromDate<%=items[i].getItemId()%>'),event);" /></td>
						<td align="center"><input name="txtToDate<%=items[i].getItemId()%>" id="txtToDate<%=items[i].getItemId()%>" style="width: 195px;" readonly="readonly" disabled="disabled" onclick="scwShow(scwID('txtToDate<%=items[i].getItemId()%>'),event);" 
							value="<%=(!items[i].getToDate().equals("0000-00-00")) ?  items[i].getToDate() : "" %>"><img
		                                    src='images/date.gif'
		                                    title='Click Here' alt='Click Here'
		                                    onclick="scwShow(scwID('txtToDate<%=items[i].getItemId()%>'),event);" /></td>
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
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>