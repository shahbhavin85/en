<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.TransferItemModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.TransferModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
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
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Stock Transfer Invoice</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="0">
				<tr>
					<td align="right">From : </td>
					<td>
						<select name="sFrom">
						<%
							AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
							AccessPointModel temp = null;
							for(int i=0; i<accessPoints.length; i++){
								temp = accessPoints[i];
								if(Integer.parseInt((String)request.getSession().getAttribute(Constant.ACCESS_POINT)) == temp.getAccessId()){
						%>
						<option value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
						<%
								}
							}
						%>
						</select>
					</td>
					<td align="right">To : </td>
					<td>
						<select name="sTo">
							<option value="--">-----</option>
						<%
							int k=0;
							for(int i=0; i<accessPoints.length; i++){
								temp = accessPoints[i];
								if(Integer.parseInt((String)request.getSession().getAttribute(Constant.ACCESS_POINT)) != temp.getAccessId()){
						%>
						<option style="background-color: <%=(k%2==0) ? "#E0E0E0" : "white"%>" <%=(((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getAccessId() == temp.getAccessId()) ? "selected=\"selected\"" : "" %> value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
						<%
								k++;
								}
							}
						%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">Despatched By : </td><td valign="top"><input type="text" size="35" name="txtDespatch" value = ""></td>
					<td align="right" valign="top">Remarks : </td>
					<td><textarea  name="txtRemarks" style="width: 195px; height : 50px;" maxlength="120"><%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getRemark() %></textarea></td>
				</tr>
			</table>
			<table style="width: 950px;">
				<tr><td style="padding-left: 15px;"> Item Name: <br/>
					<select name="sItemName" onchange="fnChangeItemNo(this);">
						<option value="--">-----</option>
						<%							
							String initPrice = "";
							ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
													(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];
							initPrice = (items.length > 0) ? items[0].getItemPrice() : "0"; 
							for(int i=0; i<items.length;i++){
						%>
							<option id="<%=Utils.get2Decimal(Double.parseDouble(items[i].getItemPrice())) %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" value="<%=items[i].getItemId()%>"><%=items[i].getItemName()+" - "+items[i].getItemNumber()%></option>
						<%
							}
						%>
					</select></td>
					<td style="padding-left: 5px;"> Item No.: <br/>
					<select name="sItem"  onchange="fnChangeItemName(this);">
						<option value="--">-----</option>
						<%							
							for(int i=0; i<items.length;i++){
						%>
							<option id="<%=Utils.get2Decimal(Double.parseDouble(items[i].getItemPrice())) %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" value="<%=items[i].getItemId()%>"><%=items[i].getItemNumber()%></option>
						<%
							}
						%>
					</select></td>
					<td style="padding-left: 5px;">Desc : <br/><input type="text" name="txtDesc" style="width: 100px;" maxlength="50"/></td>
					<td style="padding-left: 5px;">Rate : <br/><input type="text" name="txtRate" style="width: 75px;" value="<%=initPrice%>"/></td>
					<td style="padding-left: 5px;">Qty : <br/><input type="text" name="txtQuantity" style="width: 50px;" value="1"/></td>
					<td align="left" valign="bottom"><input type="button" value="Add" onclick="fnAddItem();"/></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" style="margin-left : 20px; width: 950px;">
				<tr>
					<td align="center"><b>Item Id</b></td>
					<td align="center" width="40%"><b>Item No</b></td>
					<td align="center" width="20%"><b>Desc</b></td>
					<td align="center"><b>Ordered Quantity</b></td>
					<td align="center"><b>Sent Quantity</b></td>
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Amount</b></td>
				</tr>
				<%
					double qty = 0;
					TransferItemModel[] item = (request.getAttribute(Constant.FORM_DATA) != null) ? ((TransferItemModel[]) (((TransferModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new TransferItemModel[0]))) : new TransferItemModel[0];
					for(int i=0; i<item.length; i++){
						
				%>
				<tr align="center">
					<td align="center"><%=item[i].getItem().getItemId()%></td>
					<td align="center"><%=item[i].getItem().getItemNumber()%></td>
					<td align="center"><%=item[i].getDesc()%></td>
					<td align="center"><%=item[i].getQty()%></td>
					<td align="center"><%=item[i].getSentQty()%></td>
					<td align="center"><input type="text" onblur="fnChangeQty(<%=i%>);" style="width: 50px; text-align: right;" name="txtQty<%=i%>" id="txtQty<%=i%>" <%=((item[i].getQty() - item[i].getSentQty()) > 0) ? "" : "disabled=\"disabled\"" %> value="<%=((item[i].getQty() - item[i].getSentQty()) > 0) ? (item[i].getQty() - item[i].getSentQty()) : "0"%>"></td>
					<td align="right" style="width: 120px;"><%=(item[i].getRate())%></td>
					<td align="right" style="width: 120px;"><%=((item[i].getQty() - item[i].getSentQty()) * item[i].getRate())%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Generate Tranfer Memo" onclick="fnGenerateBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="ORDER_NO" value="<%=request.getAttribute("ORDER_NO")%>"/>
	<input type="hidden" name="txtSalesItems"/>
</form>
<script type="text/javascript">
function fnChangeQty(idx){
	var vItemTable = document.getElementById('enqItem');
	var vTR = vItemTable.getElementsByTagName("tr")[idx+1];
	vTR.getElementsByTagName("td")[7].innerHTML = document.getElementById("txtQty"+idx).value*vTR.getElementsByTagName("td")[6].innerHTML;
	fnRefreshTotal();
}


function fnGenerateBill(){
	document.forms[0].genBill.disabled = true;
// 	if(isClick){
// 		isClick = false;
// 	} else {
// 		isClick = true;
// 	}
	setTimeout('fnFinalGenerateBill()',100);
}


function fnFinalGenerateBill(){
	if(document.forms[0].genBill.disabled == true){
		var vItemTable = document.getElementById('enqItem');
		var rowsInTable = vItemTable.rows.length;
		if(document.forms[0].sTo.value == '--'){
			document.forms[0].genBill.disabled = false;
			alert('Please select the to branch.');
			ocument.forms[0].sTo.focus();
			return;
		}
		if(rowsInTable == 1){
			document.forms[0].genBill.disabled = false;
// 			isClick = false;
			alert('Please enter the item details');
			return;
		}
		buildItemData();
		document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_REQUEST_TRANSFER";
		fnFinalSubmit();
	}
}

function buildItemData(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		itemdata = itemdata + vTR.getElementsByTagName("td")[0].innerHTML + "|" + vTR.getElementsByTagName("td")[1].innerHTML + "|" + vTR.getElementsByTagName("td")[2].innerHTML + "|" + document.getElementById("txtQty"+(i-1)).value + "|" + vTR.getElementsByTagName("td")[6].innerHTML + "#";
	}
// 	alert(itemdata);
	document.forms[0].txtSalesItems.value = itemdata;
}
</script>
</body>
</html>