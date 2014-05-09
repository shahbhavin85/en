<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
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
<script type="text/javascript">
function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnLedger(custId){
	window.open('/en/app?HANDLERS=CUSTOMER_LEDGER_RPT_HANDLER&sCustomer='+custId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
</script>
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div align="center" id="FreezePane" class="FreezePaneOn"></div>
	<div class="loginClass" style="min-width: 625px;">
		<fieldset>
			<legend class="screenHeader">Customer Outstanding Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 600px;" align="center">
				<tr>
					<td  style="width: 120px; padding-left: 20px;" align="right">Customer</td>
					<td><b><%=(((CustomerModel)request.getAttribute("customer")).getLabel())%></b></td>
				</tr>
			</table>
			<%
				SalesModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (SalesModel[]) request.getAttribute(Constant.FORM_DATA) : new SalesModel[0];
			%>
			<table border="1" cellspacing="2" style="font-size: 11px; width: 600px;" align="center">
				<tr>
					<th style="width: 60px;">BILL NO</th>
					<th style="width: 50px;">BILL DATE</th>
					<th style="width: 80px;">TAX TYPE</th>
					<th style="width: 50px;">DUE DATE</th>
					<th style="width: 80px;">SALESMAN</th>
					<th style="width: 60px;">TOTAL AMOUNT</th>
					<th style="width: 50px;">PAY. DATE</th>
					<th style="width: 60px;">PAY. AMOUNT</th>
					<th style="width: 60px;">PENDING AMOUNT</th>
				</tr>
				<%
					int custId = 0;
					double gTotal = 0;
					double pendintTotal = 0;
					double paidTotal = 0;
					double roundOff = 0;
					String taxBackColor = "";
					String payBackColor = "";
					String backColor = "";
					String fontColor = "";
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date redDate = new Date();
					redDate.setTime( redDate.getTime() + 2*1000*60*60*24 );
					Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
					for(int i=0; i<todayBills.length;i++){
						custId = todayBills[i].getCustomer().getCustomerId();
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) ;
						gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff);
						pendintTotal = pendintTotal + ((todayBills[i].getPaymode() == 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-Utils.get2DecimalDouble(todayBills[i].getPayAmt())) : 0);
						paidTotal = paidTotal + ((todayBills[i].getPaymode() != 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) : Utils.get2DecimalDouble(todayBills[i].getPayAmt()));
						taxBackColor = (todayBills[i].getTaxtype() == 1) ? "#FFC1C1" : (todayBills[i].getTaxtype() == 2) ? "#FFFF7E" : "#BCED91";
						payBackColor = (todayBills[i].getPaymode() == 1) ? "#FEF1B5" : (todayBills[i].getPaymode() == 2) ? "#d8bfd8" : "#FF8C69";
						if((formatter.parse((todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupDt() :todayBills[i].getDueDate())).getTime() < todayDate.getTime() ){
							backColor="#FF6A6A";
							fontColor="white";
						} else if((formatter.parse((todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupDt() :todayBills[i].getDueDate())).getTime() < redDate.getTime() ){
							backColor="#FFFF7E";
							fontColor="blue";
						} else {
							backColor="#D3BECF";
							fontColor="blue";
						}
				%>
					<tr style="background-color: <%=backColor%>; color: <%=fontColor%>;">
						<td align="center" valign="top"><a title="View sales bill" style="color: #800517;" href="javascript: fnShowSales('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></a></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
						<td align="center" style="color: blue;background-color: <%=taxBackColor %>;" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
						<td align="center" valign="top"><%=(todayBills[i].getFollowupCnt() >0) ? Utils.convertToAppDateDDMMYY(todayBills[i].getFollowupDt()) : Utils.convertToAppDateDDMMYY(todayBills[i].getDueDate()) %></td>
						<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
						<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) %></td>
						<td align="center" valign="top"><%=(todayBills[i].getPaymode() != 3) ? Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) : (todayBills[i].getPayDate()!=null && !todayBills[i].getPayDate().equals("0000-00-00")) ? Utils.convertToAppDateDDMMYY(todayBills[i].getPayDate()) : ""  %></td>
						<td align="right" valign="top"><%=(todayBills[i].getPaymode() != 3) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) : Utils.get2Decimal(todayBills[i].getPayAmt()) %></td>
						<td align="right" valign="top"><%=(todayBills[i].getPaymode() == 3 || Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt() == 0) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt()) : "--"%></td>
					</tr>
				<%
					}
				%>
				<tr>
					<th colspan="5" align="right">GRAND TOTAL(Rs.)</th>
					<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
					<th align="center">--</th>
					<th align="right"><%=Utils.get2Decimal(paidTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(pendintTotal) %></th>
				</tr> 
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Close" onclick="javascript: window.close();"/><input type="button" value="Print"/><input type="button" value="Email" onclick="fnEmail();"/><input type="button" value="SMS" onclick="fnSMS();"/><input type="button" value="Ledger" onclick="fnLedger('<%=custId%>')"/></td>		 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="email" value="N"/>
	<input type="hidden" name="sms" value="N"/>
	<input type="hidden" name="txtInvoiceNo"/>
</form>

<script type="text/javascript">
function fnEmail(){
	document.forms[0].email.value = 'Y';
	fnFinalSubmit();	
}
function fnSMS(){
	document.forms[0].sms.value = 'Y';
	fnFinalSubmit();	
}
</script>
</body>
</html>