<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.OrderModel"%>
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
<script type="text/javascript" src="js/dayBook.js"></script>
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
			<legend class="screenHeader">Order Advance Payment</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" style="margin-left: 20px; width: 950px;">
				<tr>
					<td align="right">Entry Date<span style="color: red">*</span> :</td><td><input name="txtRptDate" id="txtRptDate" style="width: 195px;" readonly="readonly" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryDate()) : ""%>"></td>
				</tr>
				<tr>
					<td align="right"> Customer<span style="color: red">*</span> :</td>
					<td colspan="3">
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ?  
							((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerName()+" - "+ ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCity() : "" %>" onkeyup="fnGetOrderCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: <%=(request.getAttribute(Constant.CUSTOMER) != null) ? "visible" : "hidden"%>;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
<!-- 						<select name="sCustomer" onchange="fnGetPendingPayments();"> -->
<!-- 							<option value="--">-------</option> -->
<%-- 							<% --%>
								
<!-- 								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ? -->
<!-- 														(CustomerModel[]) request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0]; -->
<!-- 								for(int i=0; i<customers.length;i++){ -->
<!-- 							%> -->
<%-- 							<option value="<%=customers[i].getCustomerId()%>" <%=(request.getAttribute("sCustomer") != null && Integer.parseInt((String)request.getAttribute("sCustomer")) == customers[i].getCustomerId()) ? "selected=\"selected\"" : "" %> style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>"><%=customers[i].getCustomerName()%> - <%=customers[i].getArea()%> - <%=customers[i].getCity()%></option> --%>
<%-- 							<% --%>
<!-- 								} -->
<!-- 							%> -->
<!-- 						</select> -->
					</td>
				</tr>
				<%
					int k = 0, j=0;
					ArrayList<AccessPointModel> branches = new ArrayList<AccessPointModel>(0);
					ArrayList<Integer> branchId = new ArrayList<Integer>(0);
					OrderModel[] pendingBills = (request.getAttribute(Constant.PENDING_BILLS_LIST)!=null) ? (OrderModel[])request.getAttribute(Constant.PENDING_BILLS_LIST) : new OrderModel[0];
					if(pendingBills.length!=0){
						for(int i=0; i<pendingBills.length; i++){
							if(branchId.indexOf(pendingBills[i].getBranch().getAccessId()) == -1){
								branchId.add(pendingBills[i].getBranch().getAccessId());
								branches.add(pendingBills[i].getBranch());
								j++;
							}
						}
						branchId = null;
						AccessPointModel[] accessPoints = (AccessPointModel[])branches.toArray(new AccessPointModel[0]);
				%>
				<tr>
					<td align="right">Paymode<span style="color: red">*</span> :</td>
					<td colspan="1">
				<%
					if(((String)request.getAttribute("sType")).equals("3")){
				%>
						<input type="radio" onchange="fnPayChange(this);" value="3" name="sType" id="rPaymode1" checked="checked"/><label for="rPaymode1">Cash</label>
				<%
					} else if(((String)request.getAttribute("sType")).equals("2")){
				%>
						<input type="radio" onchange="fnPayChange(this);" value="2" checked="checked" name="sType" id="rPaymode2" /><label for="rPaymode2">CC Card</label>
				<%
					} else if(((String)request.getAttribute("sType")).equals("4")){
				%>
						<input type="radio" onchange="fnPayChange(this);" value="4" checked="checked" name="sType" id="rPaymode4" /><label for="rPaymode4">Customer Direct Bank Deposit (Cash / Fund Transfer)</label>
				<%
					} else {
				%>
						<input type="radio" onchange="fnPayChange(this);" value="1" checked="checked" name="sType" id="rPaymode3" /><label for="rPaymode3">Cheque</label>
				<%
					}
				%>
					</td>
				<%
					if(!((String)request.getAttribute("sType")).equals("3")){
				%>
					<td align="right"> Bank :</td>
					<td colspan="1">
						<select name="sHeshBank" style="min-width: 200px;">
							<option value = "--">------</option>
							<option value = "KOTAK">KOTAK</option>
							<option value = "ICICI">ICICI</option>
							<option value = "SBT">SBT</option>
							<option value = "BOM">BOM</option>
						</select>
					</td>
				<%
					}
				%>
				</tr>
				<%
					if(((String)request.getAttribute("sType")).equals("1")){
				%>
				<tr>
				<td align="right"> Customer Bank<span style="color: red">*</span> :</td>
				<td colspan="1"><input type="text"  maxlength="30" name="txtCustBank" size="35"/></td>
				<td align="right"> Deposit Dt<span style="color: red">*</span> :</td>
				<td colspan="1"><input name="txtDepDate" id="txtDepDate" size="10"  readonly="readonly" onfocus="scwShow(scwID('txtDepDate'),event);" onclick="scwShow(scwID('txtDepDate'),event);" 
					value=""></td>
				</tr>
				<tr>
				<td align="right"> Cheque No<span style="color: red">*</span> :</td>
				<td colspan="1"><input type="text"  maxlength="10" name="txtChqNo" size="10"/></td>
				<td align="right"> Cheque Dt<span style="color: red">*</span> :</td>
				<td colspan="1"><input name="txtChqDate" id="txtChqDate" size="10"  readonly="readonly" onfocus="scwShow(scwID('txtChqDate'),event);" onclick="scwShow(scwID('txtChqDate'),event);" 
					value=""></td>
				</tr>
				<%
					}
				%>
				<tr>
					<td align="right">Remark :</td>
					<td colspan="3"><textarea  name="taRemark" style="width: 400px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td align="right"> Amount<span style="color: red">*</span> :</td>
					<td colspan="1"><input type="text" onblur="fnRefreshPurchasePending();" onkeypress="return numbersonly(this, event, true)" maxlength="13" name="txtAmount" size="25" value="0"/></td>
					<td align="right">Remaining Amount<span style="color: red">*</span> :</td>
					<td colspan="1"><input type="text" readonly="readonly" maxlength="13" name="txtRemainAmount" size="25" value="0"/></td>
				</tr>
				<tr>
					<td align="right"> Branch<span style="color: red">*</span> :</td>
					<td colspan="3">
						<table>
						<%
							for(int i=0; i<accessPoints.length;i++){
								if(i%3==0){
									if(i!=0){
						%>
							</tr>
						<%
									}
						%>
							<tr>
						<%
								}
						%>
						<td>
						<input type="checkbox" onchange="fnFilterTable(this);" name="cBranch" checked="checked" id="cBranch<%=i+1%>" value="<%=accessPoints[i].getBillPrefix()%>"/><label for="cBranch<%=i+1%>"><%=accessPoints[i].getAccessName()%></label>
						</td>
						<%
							}
						%>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<table width="100%" border="1" id="billTable">
							<tr>
								<th style="width: 50px;">Select</th>
								<th>Order No</th>
								<th>Order Dt.</th>
								<th>Total Amt</th>
								<th>Adv. Paid Amt</th>
								<th>Pending Amt</th>
								<th>Paying Amt</th>
							</tr>
							<%
							double roundoff = 0;
							for(int i=0; i<pendingBills.length; i++){
								k = i;
								roundoff = (1-Utils.get2DecimalDouble((pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())%1)) ;
							%>
							<tr id="<%=pendingBills[i].getBranch().getBillPrefix()+"|"+pendingBills[i].getBranch().getAccessId()+"|"+Utils.padBillNo(pendingBills[i].getOrderId()) %>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white" %>; color: blue;">
								<td align="center"><input id="v<%=i%>" type="checkbox" onchange="fnOrderChkChange(this, 'p<%=i%>','pp<%=i%>')"></td>
								<td align="center"><a style="color: #800517;" href="javascript: fnShowOrder('<%=pendingBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(pendingBills[i].getOrderId()) %>');"><%=pendingBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(pendingBills[i].getOrderId()) %></a></td>
								<td align="center"><%=Utils.convertToAppDateDDMMYY(pendingBills[i].getOrderDate()) %></td>
								<td align="right"><%=Utils.get2Decimal(Utils.get2DecimalDouble(pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())+roundoff) %></td>
								<td align="right"><%=Utils.get2Decimal(pendingBills[i].getAdvance()) %></td>
								<td align="center"><input type="text" size="25" style="text-align: right;" readonly="readonly" id="pp<%=i%>" value=<%=Utils.get2Decimal(Utils.get2DecimalDouble(Utils.get2DecimalDouble(pendingBills[i].getTotalAmt()+pendingBills[i].getInstallation()+pendingBills[i].getPacking()+pendingBills[i].getForwarding()-pendingBills[i].getLess())+roundoff) - Utils.get2DecimalDouble(pendingBills[i].getAdvance()) ) %> /></td>
								<td align="center"><input type="text" size="25" style="text-align: right;" onblur="fnOrderAmtChange(this, 'pp<%=i%>', 'v<%=i%>');" id="p<%=i%>" readonly="readonly" onkeypress="return numbersonly(this, event, true)" name="p<%=pendingBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(pendingBills[i].getOrderId())%>" id="p<%=pendingBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(pendingBills[i].getOrderId())%>" value="0"/></td>
							</tr>
							<%
								}
							%>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" name="btnSave" disabled="disabled" value="Save" onclick="fnSaveOrderPayment();"/> <input type="button" name="btnBack" value="Back" onclick="fnBack();"/> </td>
				</tr>
				<%
					}
				%>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ?  
							((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerId() : "" %>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getId() : ""%>"/>
	<input type="hidden" name="payDtls" value=""/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
var iTableRow = <%=k%>;
var iBranch = <%=j%>;
var iType = <%=(String)request.getAttribute("sType")%>;
function fnBack(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_DAY_ENTRIES";
	fnFinalSubmit();
}
</script>
</html>

