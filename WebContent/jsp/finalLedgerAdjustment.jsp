<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.DayBookEntryModel"%>
<%@page import="com.en.model.CustomerModel"%>
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
<script type="text/javascript" src="js/ledgerAdjustment.js"></script>
<script type="text/javascript">
</script>
<script type="text/javascript" src="js/timepicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Ledger Adjustment Entries</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerName()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCity()	 : ""%>" style="width: 500px;"/>
					</td>
				</tr>
				<tr>
					<td align="right">Ajdustment Type<span style="color: red">*</span> :</td>
					<td><select  name="sType" style="width: 505px;" disabled="disabled">
							<option value="--">-----</option>
							<option value="1" <%=(request.getAttribute("sType") != null && ((String)request.getAttribute("sType")).equals("1")) ? "selected=\"selected\"" : "" %>>ORDER ADVANCE TO SALES BILL</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Order No<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtOrderNo" readonly="readonly" value="<%=(request.getAttribute("orderNo") != null) ? request.getAttribute("orderNo"): ""%>" style="width: 500px;"/>
					</td>
				</tr>
				<tr>
					<td align="right"> Amount<span style="color: red">*</span> : </td>
					<td><input type="text" onblur="fnRefreshPending();" readonly="readonly" maxlength="13" name="txtAmount" size="25" value="<%=request.getAttribute("amt")%>"/>&nbsp;&nbsp;Remaining Amount<span style="color: red">*</span> :<input type="text" readonly="readonly" maxlength="13" name="txtRemainAmount" size="25" value="0"/></td>
				</tr>
			</table>
			<table>
				<tr>
					<td colspan="4">
						<table width="100%" border="1" id="billTable">
							<tr>
								<th style="width: 50px;">Select</th>
								<th>Bill No.</th>
								<th>Bill Dt.</th>
								<th>Total Amt</th>
								<th>Paid Dt.</th>
								<th>Paid Amt</th>
								<th>Pending Amt</th>
								<th>Paying Amt</th>
								<th>Adjust Amt</th>
							</tr>
							<%
								double roundoff = 0;
								SalesModel[] pendingBills = (request.getAttribute(Constant.FORM_DATA) != null) ? (SalesModel[]) request.getAttribute(Constant.FORM_DATA) : new SalesModel[0];
								for(int i=0; i<pendingBills.length; i++){
									roundoff = (1-Utils.get2DecimalDouble((pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())%1)) ;
							%>
							<tr id="<%=pendingBills[i].getBranch().getBillPrefix()+"|"+pendingBills[i].getBranch().getAccessId()+"|"+Utils.padBillNo(pendingBills[i].getSaleid()) %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white" %>; color: blue;">
								<td align="center"><input id="v<%=i+1%>" type="checkbox" onchange="fnChkChange(this, 'p<%=i+1%>','pp<%=i+1%>','pa<%=i+1%>')"></td>
								<td align="center"><a style="color: #800517;" href="javascript: fnShowSales('<%=pendingBills[i].getBranch().getBillPrefix()+Utils.padBillNo(pendingBills[i].getSaleid()) %>');"><%=pendingBills[i].getBranch().getBillPrefix()+Utils.padBillNo(pendingBills[i].getSaleid()) %></a></td>
								<td align="center"><%=Utils.convertToAppDateDDMMYY(pendingBills[i].getSalesdate()) %></td>
								<td align="right"><%=Utils.get2Decimal(Utils.get2DecimalDouble(pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())+roundoff) %></td>
								<td align="center"><%=(pendingBills[i].getPayDate() == null || pendingBills[i].getPayDate().equals("0000-00-00")) ? "--" : Utils.convertToAppDateDDMMYY(pendingBills[i].getPayDate()) %></td>
								<td align="right"><%=Utils.get2Decimal(pendingBills[i].getPayAmt()) %></td>
								<td align="center"><input type="text" size="25" style="text-align: right;" readonly="readonly" id="pp<%=i+1%>" value=<%=Utils.get2Decimal(Utils.get2DecimalDouble(Utils.get2DecimalDouble(pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())+roundoff) - Utils.get2DecimalDouble(pendingBills[i].getPayAmt()) ) %> /></td>
								<td align="center"><input type="text" size="25" style="text-align: right;" onblur="fnAmtChange(this, 'pp<%=i+1%>', 'v<%=i+1%>');" id="p<%=i+1%>" readonly="readonly" onkeypress="return numbersonly(this, event, true)" name="p<%=pendingBills[i].getBranch().getBillPrefix()+Utils.padBillNo(pendingBills[i].getSaleid())%>" id="p<%=pendingBills[i].getBranch().getBillPrefix()+Utils.padBillNo(pendingBills[i].getSaleid())%>" value="0"/></td>
								<td align="center"><input type="text" size="25" style="text-align: right;" onblur="fnAdjAmtChange(this, 'p<%=i+1%>', 'pp<%=i+1%>', 'v<%=i+1%>');" id="pa<%=i+1%>" readonly="readonly" name="pa<%=pendingBills[i].getBranch().getBillPrefix()+Utils.padBillNo(pendingBills[i].getSaleid())%>" id="p<%=pendingBills[i].getBranch().getBillPrefix()+Utils.padBillNo(pendingBills[i].getSaleid())%>" value="0"/></td>
							</tr>
							<%
								}
							%>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" name="btnSave" value="Save" onclick="fnSaveSubmit();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ?  
							((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerId() : "" %>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="payDtls" value=""/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
fnRefreshPending();
function fnBack(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_DAY_ENTRIES";
	fnFinalSubmit();
}
</script>
</html>

