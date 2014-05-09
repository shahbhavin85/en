<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AdminItemModel"%>
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
function fnSyncDataFromDB(){
	document.forms[0].HANDLERS.value = 'SYNC_DATA_HANDLER';
	document.forms[0].ACTIONS.value = 'SYNC_DATA_FROM_DB';
	fnFinalSubmit();
}
function fnSelectRow(c,id){
	if(c.checked){
		document.getElementById("nSP"+id).disabled = false;
		document.getElementById("nWP"+id).disabled = false;
		document.getElementById("tr"+id).style.background = 'yellow';
	} else {
		document.getElementById("nSP"+id).value = document.getElementById("nSP"+id).defaultValue;
		document.getElementById("nSP"+id).disabled = true;
		document.getElementById("nWP"+id).value = document.getElementById("nWP"+id).defaultValue;
		document.getElementById("nWP"+id).disabled = true;
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
function fnSyncDataToDB(){
	document.forms[0].HANDLERS.value = 'SYNC_DATA_HANDLER';
	document.forms[0].ACTIONS.value = 'SYNC_DATA_TO_DB';
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="adminMenu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Sync Data Screen</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<b>Please click the following buttons to sync the data to or from database.</b>
			<br/><br/>
			<center>
				<input type="button" value= "Sync Data From Server" onclick="fnSyncDataFromDB();"/>
				<input type="button" id="btnToServer" value= "Sync Selected Item Data To Server" onclick="fnSyncDataToDB();" disabled="disabled"/>
			</center>
			<br/>
			<%
				AdminItemModel[] items = (request.getAttribute(Constant.FORM_DATA) != null) ? (AdminItemModel[])(request.getAttribute(Constant.FORM_DATA)) : new AdminItemModel[0];
				if(items.length > 0){
			%>
				<table border="1" align="center" width="100%">
					<tr>
						<th>&nbsp;</th>
						<th>ITEM ID</th>
						<th>ITEM NAME / ITEM NUMBER</th>
						<th>SERVER RSP</th>
						<th>NEW RSP</th>
						<th>ADJUST RSP</th>
						<th>RSP DIFF %</th>
						<th>SERVER WSP</th>
						<th>NEW WSP</th>
						<th>ADJUST WSP</th>
						<th>WSP DIFF %</th>
					</tr>
			<%
					for(int i=0; i<items.length; i++){
			%>		
					<tr id="tr<%=items[i].getItemId()%>">
						<td align="center"><input onchange="fnSelectRow(this,<%=items[i].getItemId()%>)" type="checkbox" name="chk" value="<%=items[i].getItemId() %>"/> </td>
						<td align="center"><%=items[i].getItemId() %></td>
						<td align="center"><%=items[i].getItemName() %> / <%=items[i].getItemNumber() %></td>
						<td align="center"><%=items[i].getItemPrice() %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getSP1()) %></td>
						<td align="center"><input type="text" id="nSP<%=items[i].getItemId()%>" name="nSP<%=items[i].getItemId()%>" value="<%=Utils.get2Decimal(items[i].getSP1()) %>" style="text-align: right;" disabled="disabled"/></td>
						<td align="center" style="color: <%=(Double.parseDouble(items[i].getItemPrice()) != 0 && (Double.parseDouble(items[i].getItemPrice())-items[i].getSP1())*100/Double.parseDouble(items[i].getItemPrice()) != 0) ? (((Double.parseDouble(items[i].getItemPrice())-items[i].getSP1())*100/Double.parseDouble(items[i].getItemPrice()) > 0) ? "green" : "red") : "black" %>" ><%=(Double.parseDouble(items[i].getItemPrice()) != 0) ? Utils.get2Decimal((Double.parseDouble(items[i].getItemPrice())-items[i].getSP1())*100/Double.parseDouble(items[i].getItemPrice()))+"%" : "--" %></td>
						<td align="center"><%=items[i].getWprice() %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getSP2()) %></td>
						<td align="center"><input type="text" id="nWP<%=items[i].getItemId()%>" name="nWP<%=items[i].getItemId()%>" value="<%=Utils.get2Decimal(items[i].getSP2()) %>" style="text-align: right;" disabled="disabled"/></td>
						<td align="center" style="color: <%=(items[i].getWprice() != 0 && (items[i].getWprice()-items[i].getSP2())*100/items[i].getWprice() != 0) ? (((items[i].getWprice()-items[i].getSP1())*100/items[i].getWprice() > 0) ? "green" : "red") : "black" %>" ><%=(items[i].getWprice() != 0) ? Utils.get2Decimal((items[i].getWprice()-items[i].getSP2())*100/items[i].getWprice())+"%" : "--" %></td>
					</tr>		
			<%
					}
			%>
				</table>
			<%
				}
			%>
			<br/>
			<center>
				<input type="button" value= "Sync Data From Server" onclick="fnSyncDataFromDB();"/>
				<input type="button" value= "Sync Selected Item Data To Server" disabled="disabled"/>
			</center>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>