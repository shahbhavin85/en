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
<script type="text/javascript" src="js/viewAlertCollection.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="loginClass" style="min-width: 625px;">
		<fieldset>
			<legend class="screenHeader">Customer Outstanding Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center">
				<tr>
					<td  style="width: 120px; padding-left: 20px;" align="right">Customer</td>
					<td colspan="5"><b><%=(((CustomerModel)request.getAttribute("customer")).getLabel())%></b></td>
				</tr>
				<tr>
					<td align="right">Contact Person</td>
					<td><b><%=(((CustomerModel)request.getAttribute("customer")).getContactPerson())%></b></td>
					<td align="right">Phone</td>
					<td><b><%=(((CustomerModel)request.getAttribute("customer")).getStdcode())%> - <%=((CustomerModel)request.getAttribute("customer")).getPhone1() %> / <%=((CustomerModel)request.getAttribute("customer")).getPhone2() %></b></td>
					<td align="right">Mobile</td>
					<td><b><%=(((CustomerModel)request.getAttribute("customer")).getMobile1())%> / <%=((CustomerModel)request.getAttribute("customer")).getMobile2() %></b></td>
				</tr>
			</table>
			<%
				int i=0;
				SalesModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (SalesModel[]) request.getAttribute(Constant.FORM_DATA) : new SalesModel[0];
			%>
			<table border="1" cellspacing="2" style="font-size: 11px; width: 950px;" align="center">
				<tr>
					<th style="width: 30px;">&nbsp;</th>
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
					for(i=0; i<todayBills.length;i++){
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
						<td align="center"><input type="checkbox" name="chkBill" value="<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>"/></td>
						<td align="center" valign="top"><a title="View sales bill" style="color: #800517;" href="javascript: fnShowSales('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></a></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
						<td align="center" style="color: blue; background-color: <%=taxBackColor %>; " valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
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
					<th colspan="6" align="right">GRAND TOTAL(Rs.)</th>
					<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
					<th align="center">--</th>
					<th align="right"><%=Utils.get2Decimal(paidTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(pendintTotal) %></th>
				</tr> 
			</table>
			<table border="1" style="margin-top: 10px; width: 950px;" align="center">
				<tr>
					<th style="width: 80px;">DATE</th>
					<th style="width: 150px;">TYPE</th>
					<th>REMARK</th>
					<th style="width: 100px;">AMOUNT</th>
				</tr>
				<%
					EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])request.getAttribute(Constant.CUST_LAST_TXN)) : new EntryModel[0];
					String bgColor = "";
					if(entries.length > 0){
						for(i=0; i<entries.length; i++){
				%>
				<tr>
					<td align="center"><%=Utils.convertToAppDate(entries[i].getEntryDate())%></td>
					<td align="center"><%=Utils.getEntryType(entries[i].getEntryType()) %></td>
					<td><%=entries[i].getDesc() %><br/>User : <%=entries[i].getUser()%></td>
					<td align="center"><%=Utils.get2Decimal(entries[i].getAmount())  %></td>
				</tr>
				<%
						}
					} else {
				%>
				<tr><td colspan="4" align="center">No Transactions</td></tr>
				<%
					}
				%>
			</table>
			<table style="width: 950px;" align="center">
				<tr>
					<td style="width: 120px;" align="right">Next Follow up Date</td>
					<td><input name="txtFollowDate" id="txtFollowDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtFollowDate'),event);" onfocus="scwShow(scwID('txtFollowDate'),event);"
					value=""><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFollowDate'),event);" /></td>
				</tr>
				<tr>
					<td valign="top" align="right">Remarks :</td>
					<td valign="top" style="padding-top: 3px;"><textarea  name="txtRemarks" style="width: 550px; height : 100px;" maxlength="300"></textarea></td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td align="center"><input type="button" value="Save Follow up Details" onclick="fnSave();"/><input type="button" value="Back" onclick="fnBack();"/></td>	 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
<%
	String branchStr = "";
	String[] branches = (String[])request.getAttribute("cBranch");
	for(int k=0; k<branches.length; k++){
		branchStr = branchStr + branches[k] + "|";
	}
%>
	<input type="hidden" name="branches" value="<%=branchStr%>"/>
	<input type="hidden" name="sUser" value="<%=request.getAttribute(Constant.USER)%>"/>
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="txtInvoiceNo"/>
	<input type="hidden" name="currDate" value="<%=DateUtil.getCurrDt()%>">
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>