<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.InwardEntryModel"%>
<%@page import="com.en.model.InwardEntryItemModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.AdminItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript" src="js/inwardEntry.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="adminMenu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Edit Inward Entry</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="0" cellspacing="5">
				<tr>
					<td  style="padding-left: 20px;" align="right">Entry No :</td>
					<td colspan="3"><input type="text" name="txtInvoiceNo" readonly="readonly" maxlength="20" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryNo() : ""%>"></td>
				</tr>
				<tr>
					<td  style="padding-left: 20px;" align="right">BE No :</td>
					<td><input type="text" name="txtBENo" maxlength="20" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getBENo() : ""%>"></td>
					<td align="right">Entry Date :</td>
					<td><input name="txtEntryDate" id="txtEntryDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtEntryDate'),event);" 
					value="<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? Utils.convertToAppDate(((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryDate()) : DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtEntryDate'),event);" /></td>
                    <td rowspan="3" valign="top">
                    	<fieldset>
                    		<legend>Calculator</legend>
	                    	<table cellspacing="0" style="width: 250px;">
	                    		<tr><td>Total Duty:</td><td><input type="text" name="tempDuty" size="5"/></td><td>Total CBM:</td><td><input name="tempCBM" type="text" size="5"/></td></tr>
	                    		<tr><td>Total Qty :</td><td><input type="text" name="tempQty" size="5"/></td><td>Item Qty:</td><td><input name="tempItm" type="text" size="5"/></td></tr>
	                    		<tr><td colspan="4" align="center"><input type="button" value="Calculate" onclick="fnCalculate();"/></td></tr>
	                    		<tr><td colspan="4" align="center">Item CBM : <label id="lblCBM" style="color: red; font-weight: bold;">0.00</label>&nbsp;&nbsp;Item Duty : <label id="lblDuty" style="color: red; font-weight: bold;">0.00</label></td></tr>
	                    	</table>
                    	</fieldset>
                    </td>
                 </tr>
                 <tr>
					<td align="right">TOTAL CBM :</td>
					<td><input type="text" style="text-align: right;" name="txtCBM" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getCbm() : ""%>"  onkeypress="return numbersonly(this, event, true)"></td>
					<td align="right">Exchange Rate :</td>
					<td><input type="text" style="text-align: right;" name="txtExchangeRate" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getExchangeRate() : ""%>"  onkeypress="return numbersonly(this, event, true)"></td>
                 </tr>
                 <tr>
					<td align="right">China Expenses :</td> 
					<td><input type="text" style="text-align: right;" name="txtSourceExp" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getSourceExp() : ""%>"  onkeypress="return numbersonly(this, event, true)"></td>
					<td align="right">India Expenses :</td>
					<td><input type="text" style="text-align: right;" name="txtDestinationExp" value = "<%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getDestinationExp() : ""%>"  onkeypress="return numbersonly(this, event, true)"></td>
                 </tr>
			</table>
			<table style="width: 950px;">
				<tr><td style="padding-left: 15px;"> Item Name: <br/>
					<select name="sItemName" style="width: 320px;" onchange="fnChangeItemNo(this);">
						<option value="--">-----</option>
						<%							
							String initPrice = "";
							AdminItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
													(AdminItemModel[]) request.getAttribute(Constant.ITEMS) : new AdminItemModel[0];
							initPrice = (items.length > 0) ? items[0].getItemPrice() : "0"; 
							for(int i=0; i<items.length;i++){
						%>
							<option id="<%=items[i].getItemPrice() %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" value="<%=items[i].getItemId()%>"><%=items[i].getItemName()+" - "+items[i].getItemNumber()%></option>
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
							<option id="<%=items[i].getItemPrice() %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" value="<%=items[i].getItemId()%>"><%=items[i].getItemNumber()%></option>
						<%
							}
						%>
					</select></td>
					<td style="padding-left: 5px;">Qty : <br/><input type="text" name="txtQuantity" style="width: 50px;" value="" onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">Rate : <br/><input type="text" name="txtRate" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">Total Duty : <br/><input type="text" name="txtDuty" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td style="padding-left: 5px;">CBM : <br/><input type="text" name="txtItemCBM" style="width: 75px;" value=""  onkeypress="return numbersonly(this, event, true);"/></td>
					<td align="left" valign="bottom"><input type="button" value="Add" onclick="fnAddItem();"/></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" style="margin-left : 20px; width: 950px;">
				<tr>
					<td align="center"><b>Action</b></td>
					<td align="center"><b>Item Id</b></td>
					<td align="center" width="25%"><b>Item No</b></td>
					<td align="center"><b>Quantity</b></td>
					<td align="center"><b>Rate</b></td>
					<td align="center"><b>Total Duty</b></td>
					<td align="center"><b>CBM</b></td>
					<td align="center"><b>China Exp</b></td>
					<td align="center"><b>India Exp</b></td>
					<td align="center" style="width: 120px;"><b>Cost Price</b></td>
				</tr>
				<%
					InwardEntryItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? (((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getItems()) : new InwardEntryItemModel[0];
					for(int i=0; i<salesItems.length; i++){
						InwardEntryModel model = (InwardEntryModel)request.getAttribute(Constant.FORM_DATA);
						double temp = ((model.getCbm() == 0) ? 0 :(((model.getDestinationExp()+(model.getExchangeRate()*model.getSourceExp()))/model.getCbm())*salesItems[i].getCbm()));
				%>
				<tr id=s<%=i%>>
					<td align="center"><input type="button" value="Delete" onclick="fnDeleteItem('s<%=i%>');" /></td>
					<td align="center"><%=salesItems[i].getItem().getItemId() %></td>
					<td align="center"><%=salesItems[i].getItem().getItemNumber() %></td>
					<td align="center"><%=salesItems[i].getQty()%></td>
					<td align="center"><%=salesItems[i].getRate()%></td>
					<td align="center"><%=salesItems[i].getDuty()%></td>
					<td align="center"><%=salesItems[i].getCbm()%></td>
					<td align="center"><%=Utils.get2Decimal(((model.getCbm() == 0) ? 0 : (model.getExchangeRate()*model.getSourceExp()/model.getCbm()))*salesItems[i].getCbm())%></td>
					<td align="center"><%=Utils.get2Decimal((((model.getCbm() == 0) ? 0 : model.getDestinationExp()/model.getCbm()))*salesItems[i].getCbm())%></td>
					<td align="center"><%=Utils.get2Decimal(salesItems[i].getRate()+((salesItems[i].getDuty()+temp)/salesItems[i].getQty()))%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="margin-top : 10px; margin-left : 20px; width: 950px;" cellspacing="3" cellpadding="0">
				<tr>
					<td width="7%" valign="top" style="padding-top: 3px;" align="right">Remarks :</td>
					<td valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 195px; height : 50px;" maxlength="120"><%=((request.getAttribute(Constant.FORM_DATA)) != null) ? ((InwardEntryModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() : ""%></textarea></td>
					<td align="right" valign="top">Grand Total (Without Taxes) :</td>
					<td style="width: 120px;" valign="top" align="right"><label id="gTotal" style="padding-right: 10px; font-weight: bolder; font-size: 18px;">0.00</label></td>
				</tr>
			</table>
			<table width="80%">
				<tr>
					<td align="center"><input type="button" name="genBill" value="Save Entry" onclick="fnEditBill();" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtSalesItems"/>
</form>
<script type="text/javascript">
fnRefreshTotal();
fnChangeMasterStatus(true);
</script>
</body>
</html>