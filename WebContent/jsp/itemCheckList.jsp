<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
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
<script type="text/javascript">
function fnGetItemCheckList(){
	document.forms[0].HANDLERS.value = 'ITM_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ITM_CHECK_LIST';
	fnFinalSubmit();
}
var vCouponNoRegex = /^\d{1,3}$/;

function fnAddItem(){
	var vDesc = document.forms[0].txtDesc.value;
	var vQty = document.forms[0].txtQuantity.value;
	if(!vCouponNoRegex.test(vQty)){
		alert('Please enter a valid quantity.');
		document.forms[0].txtQuantity.focus();
		return;
	}
	insertRow(vDesc,vQty);
}

function insertRow(vDesc,vQty){
	var vItemTable = document.getElementById('enqItem');
	row=document.createElement("TR");
	cell1 = document.createElement("TD");
	cell1.setAttribute("align", "center");
	cell2 = document.createElement("TD");
	cell2.setAttribute("align", "center");
	cell3 = document.createElement("TD");
	cell3.setAttribute("align", "center");
	textnode1=document.createTextNode(vDesc);
	textnode2=document.createTextNode(vQty);
	// textnode4=document.createTextNode(vQty);
	buttonnode4=document.createElement("INPUT");
	buttonnode4.setAttribute("type", "button");
	buttonnode4.setAttribute("value", "Delete");
	buttonnode4.setAttribute("onclick", "fnDeleteItem(this);");
	cell1.appendChild(textnode1);
	cell2.appendChild(textnode2);
	cell3.appendChild(buttonnode4);
	row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	vItemTable.appendChild(row);
	document.forms[0].txtDesc.value = "";
	document.forms[0].txtQuantity.value = "";
	document.forms[0].txtDesc.focus();
}

function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(row.parentNode.parentNode.rowIndex);
}

function fnUpdateItemCheckList(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		itemdata = itemdata + vTR.getElementsByTagName("td")[0].innerHTML + "|" + vTR.getElementsByTagName("td")[1].innerHTML + "#";
	}
	document.forms[0].txtOfferItems.value = itemdata;
	document.forms[0].HANDLERS.value = 'ITM_HANDLER';
	document.forms[0].ACTIONS.value = 'UPDATE_ITM_CHECK_LIST';
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddItemCat">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Item Check List</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Item<span style="color: red">*</span> :</td>
					<td>
						<select name="sItem" style="width: 200px;" tabindex="1" onchange="fnGetItemCheckList();">
							<%	
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
							}							
								ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
														(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];
								for(int i=0; i<items.length;i++){
							%>
							<option value="<%=items[i].getItemId()%>"  <%=(request.getAttribute("sItem") != null && 
									Integer.parseInt((String)request.getAttribute("sItem")) == items[i].getItemId()) ? "selected=\"selected\"" : "" %>><%=items[i].getItemNumber()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<%
					if(request.getAttribute(Constant.FORM_DATA) != null){
				%>
				<tr>
					<td colspan="2">
					<table width="100%" border="1">
						<tr><td colspan="5"><b>Check List</b></td></tr>
						<tr><td colspan="2"> Desc : 
							<input type="text" name="txtDesc"></td>
							<td colspan="2">Numbers : <input type="text" name="txtQuantity" style="width: 100px;"/></td>
							<td><input type="button" value="Add" onclick="fnAddItem();"/></td>
						</tr>
					</table>
					<table width="100%" border="1" id="enqItem">
						<tr>
							<td align="center" width="60%"><b>Description</b></td>
							<td align="center"><b>Quantity</b></td>
							<td align="center"><b>Action</b></td>
						</tr>
						<%
							HashMap<String, Integer> dtls = (request.getAttribute(Constant.FORM_DATA) != null) ? (HashMap<String, Integer>) request.getAttribute(Constant.FORM_DATA) : new HashMap<String, Integer>(0);
							Iterator<String> itr = dtls.keySet().iterator();
							String key = "";
							while(itr.hasNext()){
								key = itr.next();
						%>
						<tr>
							<td align="center"><%=key%></td>
							<td align="center"><%=dtls.get(key)%></td>
							<td align="center"><input type="button" value="Delete" onclick="fnDeleteItem(this);" /></td>
						</tr>
						<%
							}
						%>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdateItemCheckList();"
						value="Update" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%> tabindex="9"></td>
				</tr>
				<%
					}
				%>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtOfferItems"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>