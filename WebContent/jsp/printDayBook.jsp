<%@page import="java.util.ArrayList"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.DayBookEntryModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Day Book</title>
</head>
<body onload="window.print();">
	<%	
		ArrayList chequeEntries = new ArrayList(0);
		ArrayList creditEntries = new ArrayList(0);
		DayBookEntryModel model = (DayBookEntryModel) request.getAttribute(Constant.FORM_DATA);
	%>
	<table width="100%">
		<tr><th align="center" style="font-family: calibri;"><%=model.getBranch().getAccessName() + " - " + model.getBranch().getCity() + " For Date "+ Utils.convertToAppDate(model.getEntryDate()) %></th></tr>
		<tr><th align="center" style="font-family: calibri;"> Status : <%=model.getDisplayStatus()%> &nbsp;&nbsp; Appoved By : <%=model.getApprover().getUserName() %></th></tr>
	</table>
	<table border="1" width="100%" cellspacing="0" bordercolor="white" style="font-family: calibri; font-size: 10px;">
		<tr>
			<th colspan="4" align="center">Cash Book</th></tr>
		<tr>
			<th style="width: 100px; border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Date</th>
			<th style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Description</th>
			<th style="width: 100px; border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Cr. - Received</th>
			<th style="width: 100px; border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;">Dr. - Paid</th>
		</tr>
		<%
			if(model.getOpeningBal() != 0){
		%>
			<tr>
				<td style="border-left-color: white; border-right-color: #C0C0C0;border-bottom-color: black;">&nbsp;</td>
				<td style="border-left-color: white; border-right-color: #C0C0C0;border-bottom-color: black;" align="right"><b>Opening Balance</b></td>
				<td style="border-left-color: white; border-right-color: #C0C0C0;border-bottom-color: black;" align="right"><b><%=(model.getOpeningBal() > 0) ? Utils.get2Decimal(model.getOpeningBal()) : "&nbsp;" %></b></td>
				<td style="border-left-color: white; border-right-color: white;border-bottom-color: black;" align="right"><b><%=(model.getOpeningBal() < 0) ? Utils.get2Decimal(0-model.getOpeningBal()) : "&nbsp;" %></b></td>
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
				<td valign="top" align="center" style="border-left-color: white; border-right-color: #C0C0C0;"><%=Utils.convertToAppDate(model.getEntryDate()) %></td>
				<td align="left" style="border-left-color: white; border-right-color: #C0C0C0;"><%=Utils.getEntryType(entries[i].getEntryType())%><br/><%=entries[i].getDesc() %></td>
				<td align="right" valign="top" style="border-left-color: white; border-right-color: #C0C0C0;"><%=Utils.get2Decimal(entries[i].getAmount())%></td>
				<td align="right" style="border-left-color: white; border-right-color: white;">&nbsp;</td>
			</tr>
		<%
				} else {
					drTotal = drTotal + entries[i].getAmount();
		%>
			<tr>
				<td align="center" valign="top" style="border-left-color: white; border-right-color: #C0C0C0;"><%=Utils.convertToAppDate(model.getEntryDate()) %></td>
				<td align="left" style="border-left-color: white; border-right-color: #C0C0C0;"><%=Utils.getEntryType(entries[i].getEntryType())%><br/><%=entries[i].getDesc() %></td>
				<td align="right" style="border-left-color: white; border-right-color: #C0C0C0;">&nbsp;</td>
				<td align="right" valign="top" style="border-left-color: white; border-right-color: white;"><%=Utils.get2Decimal(entries[i].getAmount())%></td>
			</tr>
		<%
				}
			}
		%>
			<tr>
				<th style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;">&nbsp;</th>
				<th align="right" style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;">Sub - Total</th>
				<th align="right" style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;"><%=Utils.get2Decimal(crTotal) %></th>
				<th align="right" style=" border-left-color: white; border-right-color: white;border-top-color: black;"><%=Utils.get2Decimal(drTotal) %></th>
			</tr>
			<tr>
				<th style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
				<th style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;" align="right">Closing Balance</th>
				<th style=" border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;" align="right"><%=(crTotal - drTotal > 0) ? Utils.get2Decimal(crTotal - drTotal) : "&nbsp;" %></th>
				<th style=" border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;" align="right"><%=(crTotal - drTotal < 0) ? Utils.get2Decimal(drTotal - crTotal) : "&nbsp;" %></th>
			</tr>
	</table>
	<br/>
	<%
		double chqTotal = 0;
		if(chequeEntries.size() > 0){
	%>
	<center><font style="font-family: calibri; font-size: 10px; font-weight: bold;">Cheque Entries</font></center>  
	<table width="100%"  bordercolor="white" border="1" cellspacing="0" style="font-family: calibri; font-size: 10px;">
		<tr>
			<th style="width: 100px;border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">S.No</th>
			<th style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Desciption</th>
			<th style="width: 120px;border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;">Amount</th>
		</tr>
		<%
			for(int i=0; i<chequeEntries.size(); i++){
				chqTotal = chqTotal+ ((EntryModel)chequeEntries.get(i)).getAmount();
				
		%>
		<tr>
			<td style="border-left-color: white; border-right-color: #C0C0C0;" align="center" valign="top"><%=i+1%></td>
			<td style="border-left-color: white; border-right-color: #C0C0C0;"><%=((EntryModel)chequeEntries.get(i)).getDesc()%></td>
			<td style="border-left-color: white; border-right-color: white;" align="right" valign="top"><%=Utils.get2Decimal(((EntryModel)chequeEntries.get(i)).getAmount())%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
			<th align="right" style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Total</th>
			<th align="right" style="border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(chqTotal)%></th>
		</tr>
	</table>
	<br/>
	<%
		}
		double creTotal = 0;
		if(creditEntries.size() > 0){
	%>
	<center><font style="font-family: calibri; font-size: 10px; font-weight: bold;">Credit Card Entries / Direct Bank Deposit (Cash / Fund Transfer)</font></center> 
	<table width="100%" border="1" bordercolor="white" cellspacing="0" style="font-family: calibri; font-size: 10px;">
		<tr>
			<th style="width: 100px;border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">S.No</th>
			<th style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Desciption</th>
			<th style="width: 120px;border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;">Amount</th>
		</tr>
		<%
			for(int i=0; i<creditEntries.size(); i++){
				creTotal = creTotal+ ((EntryModel)creditEntries.get(i)).getAmount();
				
		%>
		<tr>
			<td style="border-left-color: white; border-right-color: #C0C0C0;" align="center" valign="top"><%=i+1%></td>
			<td style="border-left-color: white; border-right-color: #C0C0C0;"><%=((EntryModel)creditEntries.get(i)).getDesc()%></td>
			<td style="border-left-color: white; border-right-color: white;" align="right" valign="top"><%=Utils.get2Decimal(((EntryModel)creditEntries.get(i)).getAmount())%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
			<th align="right" style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Total</th>
			<th align="right" style="border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(creTotal)%></th>
		</tr>
	</table>
	<%
		}
		EntryModel[] pdChqs = (request.getAttribute(Constant.PD_CHEQUES_LIST) != null) ? (EntryModel[]) request.getAttribute(Constant.PD_CHEQUES_LIST) : new EntryModel[0];
		double pdChqTotal = 0;
		if(pdChqs.length > 0){
	%>
	<br/>
	<center><font style="font-family: calibri; font-size: 10px; font-weight: bold;">Post Date Cheque Entries</font></center>  
	<table width="100%"  bordercolor="white" border="1" cellspacing="0" style="font-family: calibri; font-size: 10px;">
		<tr>
			<th style="width: 100px;border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">S.No</th>
			<th style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Desciption</th>
			<th style="width: 120px;border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;">Amount</th>
		</tr>
		<%
			for(int i=0; i<pdChqs.length; i++){
				pdChqTotal = pdChqTotal+ pdChqs[i].getAmount();
				
		%>
		<tr>
			<td style="border-left-color: white; border-right-color: #C0C0C0;" align="center" valign="top"><%=i+1%></td>
			<td style="border-left-color: white; border-right-color: #C0C0C0;"><%=pdChqs[i].getDesc()%></td>
			<td style="border-left-color: white; border-right-color: white;" align="right" valign="top"><%=Utils.get2Decimal(pdChqs[i].getAmount())%></td>
		</tr>
		<%
			}
		%>
		<tr>
			<th style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
			<th align="right" style="border-left-color: white; border-right-color: #C0C0C0;border-top-color: black;border-bottom-color: black;">Total</th>
			<th align="right" style="border-left-color: white; border-right-color: white;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(pdChqTotal)%></th>
		</tr>
	</table>
	<br/>
	<%
		}
	%>
</body>
</html>