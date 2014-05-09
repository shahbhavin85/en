<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.util.DateUtil"%>
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
<script type="text/javascript">
function fnGetSalesDtls(){
	var vRegex = /^\d{1,5}$/;
	if(!vRegex.test(document.forms[0].txtInvNo.value)){
		alert('Please provide a valid bill No.');
		return;
	}
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "GET_EDIT_SALES_LR_DTLS";
	fnFinalSubmit();
}

function fnEditSalesLRDtls(id){
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>'+id;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "GET_EDIT_SALES_LR_DTLS";
	fnFinalSubmit();
}

function fnEditSalesNoLRDtls(id){
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>'+id;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_SALES_NO_LR_DTLS";
	fnFinalSubmit();
}

function fnCancel(){
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_SALES_LR_DTLS";
	fnFinalSubmit();
}

function fnUpdateLRDtls(){
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_SALES_LR_DTLS";
	fnFinalSubmit();
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Edit Sales LR Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
<%
		if(request.getAttribute(Constant.FORM_DATA)==null){
%>
			<table>
				<tr>
					<td align="right">Invoice No<span style="color: red">*</span> :</td>
					<td><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%> - <input name="txtInvNo" <%=(request.getAttribute(Constant.FORM_DATA)!=null) ? "readOnly=\"readOnly\"" : "" %> value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() : "" ) %>" style="width: 195px;" maxlength="5">
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/></td>
				</tr>
			</table>
			<table cellpadding="3" border=1 style="width: 850px;">
				<tr>
					<th>Invoice No</th>
					<th width="50%">Customer</th>
					<th>Transport/Lr No/Lr Dt</th>
					<th>Action</th>
				</tr>
<%
			SalesModel[] lst = (request.getAttribute("SALES_DATA") != null) ? (SalesModel[])request.getAttribute("SALES_DATA") : new SalesModel[0];
			if(lst.length == 0){
%>
				<tr>
					<th colspan="4">No Bill Exists</th>
				</tr>
<%
			} else {
				for(int i=0; i<lst.length; i++){
%>
				<tr>
					<td align="center"><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())+Utils.padBillNo(lst[i].getSaleid())%></td>
					<td><%=lst[i].getCustomer().getLabel() %></td>
					<td align="center"><%=lst[i].getTransport()+" / "+lst[i].getLrno()+" / "+lst[i].getLrdt() %></td>
					<td align="center"><input type="button" onclick="fnEditSalesLRDtls(<%=lst[i].getSaleid()%>)" value="Add LR Details" /><input type="button" onclick="fnEditSalesNoLRDtls(<%=lst[i].getSaleid()%>)" value="No LR Details" /></td>
				</tr>
<%
				}
			}
%>
			</table>
<%
		} else {
%>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px;" align="right">Bill No</td>
					<td><b><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%> - <input name="txtInvNo" <%=(request.getAttribute(Constant.FORM_DATA)!=null) ? "readOnly=\"readOnly\"" : "" %> value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() : "" ) %>" style="width: 85px;" maxlength="5"></b></td>
					<td align="right">Date</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSalesdate()%></b></td>
					<td align="right" valign="top">Salesman</td>
					<td valign="top"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserName()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" valign="top" align="right">Customer</td>
					<td><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getLabel()%></b></td>
					<td align="right">Tax Type</td><td><b><%if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 1) {%>VAT BILL<%} else if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTaxtype() == 2) {%>CST BILL<%} else {%>CST AGAINST FORM 'C' BILL<%}%></b></td>
					<td align="right">Pay Mode</td><td><b><%if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 1) {%>CASH<%} else if(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPaymode() == 2) {%>CREDIT CARD<%} else {%>CREDIT OF <%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCreditDays()%> DAYS<%}%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">TIN/CST</td>
					<td colspan="2"><input type="text" name="txtTIN" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getTin()%>" />  / <input type="text" name="txtCST" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCst()%>" /></td>
					<td align="right">Contact Person / Phone No</td>
					<td colspan="2"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getContactPerson()+ " / "+((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getPhone1()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">Mobile 1</td>
					<td colspan="2"><input type="text" name="txtMobile1" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getMobile1()%>" /></td>
					<td style="padding-left: 20px;" align="right">Email 1</td>
					<td colspan="2"><input type="text" style="width: 250px;" name="txtEmail1" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getEmail()%>" /></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">Mobile 2</td>
					<td colspan="2"><input type="text" name="txtMobile2" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getMobile2()%>" /></td>
					<td style="padding-left: 20px;" align="right">Email 2</td>
					<td colspan="2"><input type="text" style="width: 250px;" name="txtEmail2" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getEmail1()%>" /></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">Mode of Transport</td>
					<td><input type="text" maxlength="50" style="width: 200px;" name="txtDespatch" value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getTransport() : "" ) %>"/></td>
					<td align="right">LR No</td>
					<td><input type="text" maxlength="15" name="txtLRNo" value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLrno() : "" ) %>"/></td>
					<td align="right" valign="top">LR Date</td>
					<td valign="top"><input name="txtLRDt" id="txtLRDt" style="width: 80px;" readonly="readonly" onclick="scwShow(scwID('txtLRDt'),event);" 
					value = "<%=(request.getAttribute(Constant.FORM_DATA)!=null && !((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLrdt().equals("")) ? Utils.convertToAppDate(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLrdt()) : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtLRDt'),event);" /></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" align="right">CTNS</td>
					<td><b><input type="text" maxlength="3" name="txtCtns" value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getCtns() : "0" ) %>"/></b></td>
					<td align="right">Packed By</td>
					<td><select  name="sPackedBy" style="width: 200px;">
							<option value="--">-----</option>
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPackedBy().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select></td>
					<td align="right" valign="top">Despatched By</td>
					<td valign="top"><select  name="sDespatchedBy" style="width: 200px;">
							<option value="--">-----</option>
							<%
								
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((SalesModel)request.getAttribute(Constant.FORM_DATA)).getDespatchedBy().getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select></td>
				</tr>
				<tr>
					<td align="right">Remarks</td><td colspan="5"><b><%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() %></b></td>
				</tr>
			</table>
			<table border="1" cellspacing="0" id="enqItem" align="center" style="width: 950px;margin-top: 10px;">
				<tr>
					<th  style="width:50px;">S No.</th>
					<th>Description of Goods</th>
					<th style="width:50px;">Qty</th>
					<th style="width:50px;">Rate<br/>(Rs.)</th>
					<th style="width:50px;">Dis<br/>(%)</th>
					<th style="width:65px;">Gross Rate<br/>(Rs.)</th>
					<th style="width:50px;">Tax<br/>(%)</th>
					<th style="width:50px;">Tax<br/>(Rs.)</th>
					<th style="width:65px;">Nett Rate<br/>(Rs.)</th>
				</tr>
				<%
					double total = 0;
					double stotal = 0;
					double ttotal = 0;
					double qty = 0;
					SalesItemModel[] salesItems = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesItemModel[]) (((SalesModel)request.getAttribute(Constant.FORM_DATA)).getItems().toArray(new SalesItemModel[0]))) : new SalesItemModel[0];
					for(int i=0; i<salesItems.length; i++){
						
				%>
				<tr align="center">
					<td align="center" style="width:50px;"><%=(i+1)%></td>
					<td align="left"><%=salesItems[i].getItem().getItemNumber() + " / "+ salesItems[i].getItem().getItemName() + " " + salesItems[i].getDesc()%></td>
					<td style="width:50px;"><%=salesItems[i].getQty()%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getRate())%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getDisrate())%>%</td>
					<td style="width:65px;"><%=Utils.get2Decimal(((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate())%></td>
					<td style="width:50px;"><%=Utils.get2Decimal(salesItems[i].getTaxrate())%>%</td>
					<td style="width:50px;"><%=Utils.get2Decimal(((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate() * salesItems[i].getTaxrate())/100)%></td>
					<td style="width:65px;"><%=Utils.get2Decimal((((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate())+(((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty()*(salesItems[i].getRate() * salesItems[i].getTaxrate())/100)))%></td>
				</tr>
				<%
						qty = qty + (salesItems[i].getQty());
						stotal = stotal + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * salesItems[i].getRate();
						ttotal = ttotal + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate() * salesItems[i].getTaxrate())/100;
						total = total + ((100-salesItems[i].getDisrate())/100)*salesItems[i].getQty() * (salesItems[i].getRate()+((salesItems[i].getRate() * salesItems[i].getTaxrate())/100));
					}
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation() >0){
						total = total + ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation();
				%>
				<tr align="center">
					<th align="right" colspan="2">INSTALLTION(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getInstallation())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking() >0){
						total = total + ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking();
				%>
				<tr align="center">
					<th align="right" colspan="2">PACKING(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getPacking())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding() >0){
						total = total + ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding();
				%>
				<tr align="center">
					<th align="right" colspan="2">FORWARDING(+)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getForwarding())%></td>
				</tr>
				<% }
					if(request.getAttribute(Constant.FORM_DATA) != null && ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess() >0){
						total = total - ((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess();
				%>
				<tr align="center">
					<th align="right" colspan="2">LESS(-)</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getLess())%></td>
				</tr>
				<% }
					double roundOff = (1-Utils.get2DecimalDouble(total%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble(total%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble(total%1)) ;
					if(roundOff != 0){
						total = total + roundOff;
				%>
				<tr align="center">
					<th align="right" colspan="2">ROUNDOFF</th>
					<th style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;">&nbsp;</th>
					<td style="width:65px;"><%=Utils.get2Decimal(roundOff)%></td>
				</tr>
				<% }  %>
				<tr>
					<th align="right" colspan="2">GRAND TOTAL</th>
					<th style="width:50px;"><%=qty%></th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:65px;"><%=Utils.get2Decimal(stotal)%></th>
					<th align="left" style="width:50px;">&nbsp;</th>
					<th style="width:50px;"><%=Utils.get2Decimal(ttotal)%></th>
					<th style="width:65px;"><%=Utils.get2Decimal(total)%></th>
				</tr>
			</table>
			<table align="center" style="width: 950px;margin-top: 10px;">
				<tr>
					<td colspan="2" align="center" style="font-weight: bold;"><input type="checkbox" onclick="document.forms[0].btnModify.disabled =  !this.checked;"> On update of the LR Details. Details will be Email and SMS to Customer email and mobile no.</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" name="btnModify" disabled="disabled" value="Update" onclick="fnUpdateLRDtls();"/>&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/> </td>
				</tr>
			</table>
		<input name="txtCustId" type="hidden" style="width: 195px;" value="<%=((SalesModel) request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId()%>">
<%
		}
%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>