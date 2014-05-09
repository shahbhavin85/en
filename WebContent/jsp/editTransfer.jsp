<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.TransferItemModel"%>
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
<script type="text/javascript" src="js/transfer1.js"></script>
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
					<td align="right">Invoice No<span style="color: red">*</span> :</td><td colspan="3"> <input name="txtInvoiceNo" readonly="readonly" style="width: 195px;" value="<%=((TransferModel)request.getAttribute(Constant.FORM_DATA)).getFromBranch().getBillPrefix()+"ST"+Utils.padBillNo(((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferid())%>"></td>
				</tr>
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
						<option style="background-color: <%=(k%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && (((TransferModel)request.getAttribute(Constant.FORM_DATA))).getToBranch().getAccessId() == temp.getAccessId()) ? "selected=\"selected\"" : ""%> value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
						<%
								k++;
								}
							}
						%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">Despatched By : </td><td valign="top"><input type="text" size="35" name="txtDespatch" value = "<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransport())) : "" %>"></td>
					<td align="right" valign="top">Remarks : </td>
					<td><textarea  name="txtRemarks" style="width: 195px; height : 50px;" maxlength="120"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((((TransferModel)request.getAttribute(Constant.FORM_DATA)).getRemark())) : "" %></textarea></td>
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
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Amount</b></td>
					<td align="center"><b>Action</b></td>
				</tr>
					<%
						TransferItemModel[] transferItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((TransferItemModel[]) (((TransferModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new TransferItemModel[0]))) : new TransferItemModel[0];
						TransferItemModel model = null;
						for(int i=0; i<transferItems.length; i++){
							model = transferItems[i];
					%>
				<tr id=s<%=i%>>
					<td align="center"><%=model.getItem().getItemId() %></td>
					<td align="center"><%=model.getItem().getItemNumber() %></td>
					<td align="center"><%=model.getDesc() %></td>
					<td align="center"><%=model.getQty() %></td>
					<td align="center"><%=model.getRate()%></td>
					<td align="center"><%=model.getQty() * model.getRate()%></td>
					<td align="center"><input type="button" value="Delete" onclick="fnDeleteItem('s<%=i%>');" /></td>
				</tr>
					<%
						}
					%>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Edit & Generate Tranfer Memo" onclick="fnEditGenerateBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtSalesItems"/>
</form>

</body>
</html>