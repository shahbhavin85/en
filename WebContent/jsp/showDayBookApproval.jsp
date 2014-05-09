<%@page import="com.en.model.SalesModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.DayBookEntryModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<script type="text/javascript">
function fnBack(){
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_APPROVE_DAY_ENTRIES';
	fnFinalSubmit();	
}

function fnPrint(id){
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_HANDLER&ACTIONS=PRNT_DAY_BOOK&txtId='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnApprove(id){
	document.forms[0].txtId.value = id;
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'APPROVE_DAY_ENTRIES';
	fnFinalSubmit();	
}

function fnReject(id){
	document.forms[0].txtId.value = id;
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'REJ_APPROVE_DAY_ENTRIES';
	fnFinalSubmit();	
}
</script>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Day Book Details</legend>
	<%	
		ArrayList chequeEntries = new ArrayList(0);
		ArrayList creditEntries = new ArrayList(0);
		DayBookEntryModel model = (DayBookEntryModel) request.getAttribute(Constant.FORM_DATA);
	%>
	<table width="100%">
		<tr><th align="center" style="font-family: calibri;"><%=model.getBranch().getAccessName() + " - " + model.getBranch().getCity() + " For Date "+ Utils.convertToAppDate(model.getEntryDate()) %></th></tr>
		<tr><th align="center" style="font-family: calibri;"> Status : <%=model.getDisplayStatus()%> &nbsp;&nbsp; Appoved By : <%=model.getApprover().getUserName() %></th></tr>
	</table>
	<table border="1" width="100%"  bordercolor="black" style="font-family: calibri; font-size: 13px;">
		<tr>
			<th colspan="4" align="center">Cash Book</th></tr>
		<tr>
			<th style="width: 100px; border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Date</th>
			<th style=" border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Description</th>
			<th style="width: 100px; border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Cr. - Received</th>
			<th style="width: 100px; border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Dr. - Paid</th>
		</tr>
		<%
			if(model.getOpeningBal() != 0){
		%>
			<tr>
				<td style="border-left-color: black; border-right-color: black;border-bottom-color: black;">&nbsp;</td>
				<td style="border-left-color: black; border-right-color: black;border-bottom-color: black;" align="right"><b>Opening Balance</b></td>
				<td style="border-left-color: black; border-right-color: black;border-bottom-color: black;" align="right"><b><%=(model.getOpeningBal() > 0) ? Utils.get2Decimal(model.getOpeningBal()) : "&nbsp;" %></b></td>
				<td style="border-left-color: black; border-right-color: black;border-bottom-color: black;" align="right"><b><%=(model.getOpeningBal() < 0) ? Utils.get2Decimal(0-model.getOpeningBal()) : "&nbsp;" %></b></td>
			</tr>
		<%
			}
			double crTotal = (model.getOpeningBal() > 0) ? model.getOpeningBal() : 0;
			double drTotal = (model.getOpeningBal() < 0) ? (0-model.getOpeningBal()) : 0;
			EntryModel[] entries = model.getEntries();
			for(int i=0 ; i<entries.length; i++){
				if(!entries[i].isCash()){
					if(entries[i].getEntryType() == 1 || entries[i].getEntryType() == 32 || entries[i].getEntryType() == 42){
						chequeEntries.add(entries[i]);
					} else {
						creditEntries.add(entries[i]);
					}
					continue;
				}
				if(entries[i].getEntryType() < 51){
					crTotal = crTotal + entries[i].getAmount();
		%>
			<tr>
				<td valign="top" align="center" style="border-left-color: black; border-right-color: black;"><%=Utils.convertToAppDate(model.getEntryDate()) %></td>
				<td align="left" style="border-left-color: black; border-right-color: black;"><%=Utils.getEntryType(entries[i].getEntryType())%><br/><%=entries[i].getDesc() %></td>
				<td align="right" valign="top" style="border-left-color: black; border-right-color: black;"><%=Utils.get2Decimal(entries[i].getAmount())%></td>
				<td align="right" style="border-left-color: black; border-right-color: black;">&nbsp;</td>
			</tr>
		<%
				} else {
					drTotal = drTotal + entries[i].getAmount();
		%>
			<tr>
				<td align="center" valign="top" style="border-left-color: black; border-right-color: black;"><%=Utils.convertToAppDate(model.getEntryDate()) %></td>
				<td align="left" style="border-left-color: black; border-right-color: black;"><%=Utils.getEntryType(entries[i].getEntryType())%><br/><%=entries[i].getDesc() %></td>
				<td align="right" style="border-left-color: black; border-right-color: black;">&nbsp;</td>
				<td align="right" valign="top" style="border-left-color: black; border-right-color: black;"><%=Utils.get2Decimal(entries[i].getAmount())%></td>
			</tr>
		<%
				}
			}
		%>
			<tr>
				<th style=" border-left-color: black; border-right-color: black;border-top-color: black;">&nbsp;</th>
				<th align="right" style=" border-left-color: black; border-right-color: black;border-top-color: black;">Sub - Total</th>
				<th align="right" style=" border-left-color: black; border-right-color: black;border-top-color: black;"><%=Utils.get2Decimal(crTotal) %></th>
				<th align="right" style=" border-left-color: black; border-right-color: black;border-top-color: black;"><%=Utils.get2Decimal(drTotal) %></th>
			</tr>
			<tr>
				<th style=" border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
				<th style=" border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;" align="right">Closing Balance</th>
				<th style=" border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;" align="right"><%=(crTotal - drTotal > 0) ? Utils.get2Decimal(crTotal - drTotal) : "&nbsp;" %></th>
				<th style=" border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;" align="right"><%=(crTotal - drTotal < 0) ? Utils.get2Decimal(drTotal - crTotal) : "&nbsp;" %></th>
			</tr>
	</table>
	<br/>
	<%
		double chqTotal = 0;
		if(chequeEntries.size() > 0){
	%>
	<center><font style="font-family: calibri; font-size: 13px; font-weight: bold;">Cheque Entries</font></center>  
	<table width="100%"  bordercolor="black" border="1"  style="font-family: calibri; font-size: 13px;">
		<tr>
			<th style="width: 100px;border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">S.No</th>
			<th style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Desciption</th>
			<th style="width: 120px;border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Amount</th>
		</tr>
		<%
			for(int i=0; i<chequeEntries.size(); i++){
				chqTotal = chqTotal+ ((EntryModel)chequeEntries.get(i)).getAmount();
				
		%>
		<tr>
			<td style="border-left-color: black; border-right-color: black;" align="center" valign="top"><%=i+1%></td>
			<td style="border-left-color: black; border-right-color: black;"><%=((EntryModel)chequeEntries.get(i)).getDesc()%></td>
			<td style="border-left-color: black; border-right-color: black;" align="right" valign="top"><%=Utils.get2Decimal(((EntryModel)chequeEntries.get(i)).getAmount())%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
			<th align="right" style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Total</th>
			<th align="right" style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(chqTotal)%></th>
		</tr>
	</table>
	<br/>
	<%
		}
		double creTotal = 0;
		if(creditEntries.size() > 0){
	%>
	<center><font style="font-family: calibri; font-size: 13px; font-weight: bold;">Credit Card Entries / Direct Bank Deposit (Cash / Fund Transfer)</font></center> 
	<table width="100%" border="1" bordercolor="black"  style="font-family: calibri; font-size: 13px;">
		<tr>
			<th style="width: 100px;border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">S.No</th>
			<th style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Desciption</th>
			<th style="width: 120px;border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Amount</th>
		</tr>
		<%
			for(int i=0; i<creditEntries.size(); i++){
				creTotal = creTotal+ ((EntryModel)creditEntries.get(i)).getAmount();
				
		%>
		<tr>
			<td style="border-left-color: black; border-right-color: black;" align="center" valign="top"><%=i+1%></td>
			<td style="border-left-color: black; border-right-color: black;"><%=((EntryModel)creditEntries.get(i)).getDesc()%></td>
			<td style="border-left-color: black; border-right-color: black;" align="right" valign="top"><%=Utils.get2Decimal(((EntryModel)creditEntries.get(i)).getAmount())%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
			<th align="right" style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;">Total</th>
			<th align="right" style="border-left-color: black; border-right-color: black;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(creTotal)%></th>
		</tr>
	</table>
	<%
		}
	%>
	<%
		SalesModel[] todayBills = (request.getAttribute(Constant.SALES_DATA)!=null) ? (SalesModel[]) request.getAttribute(Constant.SALES_DATA) : new SalesModel[0];
		if(todayBills.length>0){
	%>
	<table width="100%" border="1">
		<tr>
			<th style="width: 120px;">BILL NO</th>
			<th>CUSTOMER</th>
			<th style="width: 120px;">TOTAL AMOUNT</th>
		</tr>
		<%
			double gTotal = 0;
			double roundOff = 0;
			for(int i=0; i<todayBills.length;i++){
				roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) ;
				gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff);
		%>
			<tr>
				<td align="center"><a href="JavaScript:fnGetLinkSalesDtls('<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>')"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></a></td>
				<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
				<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff) %></td>
			</tr>
		<%
			}
		%>
		<tr>
			<th colspan="2" align="right">GRAND TOTAL</th>
			<th align="right"> Rs. <%=Utils.get2Decimal(gTotal) %></th>
		</tr> 
	</table>
	<%
		}
	%>
	<table width="100%"><tr><td align="center"><input type="button" value="Print" onclick="fnPrint('<%=model.getId()%>');"/>&nbsp;&nbsp;<input type="button" value="Approve" onclick="fnApprove('<%=model.getId()%>');" />&nbsp;&nbsp;<input type="button" value="Reject" onclick="fnReject('<%=model.getId()%>');" />&nbsp;&nbsp;<input type="button" value="Back" onclick="fnBack();"/> </td></tr></table>
	</fieldset>
	<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtId" value="">
</form>
</body>
</html>