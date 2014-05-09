<%@page import="com.en.model.EntryModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.en.util.Utils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Bank Ledger Report</title>
</head>
<body>
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><td colspan="6" align="center">BANK LEDGER REPORT</td></tr>
		<tr>
			<td colspan="2" align="right">HESH BANK</td>
			<td colspan="4"><%=(request.getAttribute("sHeshBank") != null) ? (String)request.getAttribute("sHeshBank")	 : "--"%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right" valign="top">Branch</td>
			<td colspan="4">
			<%
				String[] accessSelected = (request.getAttribute("cBranch") != null) ? ((String[])request.getAttribute("cBranch")) : new String[] {"--"};
				AccessPointModel[] accessPoints = request.getAttribute(Constant.ACCESS_POINTS) != null ?
										(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
				ArrayList selected = new ArrayList(0);
				if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
					for(int k=0; k<accessSelected.length;k++){
						selected.add((String)accessSelected[k]);	
					}
				}
				if(accessSelected[0].equals("--") || selected.indexOf("2") != -1){
			%>Account,<%
				}
				for(int i=0; i<accessPoints.length;i++){
					if(accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf(accessPoints[i].getAccessId()+"")!=-1)){
			%>
				<%=accessPoints[i].getAccessName()%>, 
			<%
					}
				}
			%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">From Date / To Date</td>
			<td colspan="4"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
	<%
		double chqTotal = 0;
		EntryModel[] chequeEntries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])request.getAttribute(Constant.FORM_DATA)) : new EntryModel[0];;
		if(chequeEntries.length > 0){
	%>
		<tr>
			<th style="width: 50px;">Date</th>
			<th>Branch</th>
			<th>Entry Type</th>
			<th>Particulars</th>
			<th style="width: 120px;">CR. (DEPOSIT)</th>
			<th style="width: 120px;">DR. (WITHDRAW)</th>
<!-- 					<th style="width: 100px;">Balance</th> -->
		</tr>
		<%
			for(int i=0; i<chequeEntries.length; i++){
				chqTotal = chqTotal+ chequeEntries[i].getAmount();
				
		%>
		<tr>
			<td  align="center" valign="top"><%=Utils.convertToAppDateDDMMYY((chequeEntries[i]).getEntryDate())%></td>
			<td  align="center" valign="top"><%=(chequeEntries[i]).getBranch().getAccessName() +" - "+(chequeEntries[i]).getBranch().getCity()%></td>
			<td  align="center" valign="top"><%=Utils.getEntryType((chequeEntries[i]).getEntryType())%></td>
			<td ><%=(chequeEntries[i]).getDesc()%></td>
			<td  align="right" valign="top"><%=(chequeEntries[i].getCrdr().equals("C")) ? Utils.get2Decimal((chequeEntries[i]).getAmount()) : "--"%></td>
			<td  align="right" valign="top"><%=(chequeEntries[i].getCrdr().equals("D")) ? Utils.get2Decimal((chequeEntries[i]).getAmount()) : "--"%></td>
<%-- 					<td  align="right" valign="top"><%=Utils.get2Decimal((chequeEntries[i]).getAmount())%></td> --%>
		</tr>
		<%
			}
		}
	%>
	</table>
</body>
</html>