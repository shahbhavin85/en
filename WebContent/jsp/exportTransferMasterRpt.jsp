<%@page import="com.en.model.TransferModel"%>
<%@page import="com.en.model.PurchaseModel"%>
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
<%@page import="com.en.model.SalesModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=transfer_master_report_"+DateUtil.getCurrDt()+".xls"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Sales Master Report</title>
</head>
<body>
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><td colspan="6" align="center">TRANSFER MASTER REPORT</td></tr>
		<tr>
			<td colspan="2" align="right" valign="top">From Branch</td>
			<td colspan="4">
			<%
				String[] accessSelected = (request.getAttribute("cFromBranch") != null) ? ((String[])request.getAttribute("cFromBranch")) : new String[] {"--"};
				AccessPointModel[] accessPoints = request.getAttribute(Constant.ACCESS_POINTS) != null ?
										(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
				ArrayList selected = new ArrayList(0);
				if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
					for(int k=0; k<accessSelected.length;k++){
						selected.add((String)accessSelected[k]);	
					}
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
			<td colspan="2" align="right" valign="top">To Branch</td>
			<td colspan="4">
			<%
				accessSelected = (request.getAttribute("cToBranch") != null) ? ((String[])request.getAttribute("cToBranch")) : new String[] {"--"};
				selected = new ArrayList(0);
				if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
					for(int k=0; k<accessSelected.length;k++){
						selected.add((String)accessSelected[k]);	
					}
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
			<td colspan="2" align="right">Recd From Date / To Date</td>
			<td colspan="4"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
		<%
			TransferModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (TransferModel[]) request.getAttribute(Constant.FORM_DATA) : new TransferModel[0];
			if(todayBills.length>0){
		%>
			<tr>
				<th style="width: 60px;">TRANSFER ID</th>
				<th style="width: 50px;">TRANSFER DATE</th>
				<th style="width: 50px;">STATUS</th>
				<th>FROM BRANCH</th>
				<th>TO BRANCH</th>
				<th style="width: 60px;">TOTAL AMOUNT</th>
			</tr>
			<%
				double gTotal = 0;
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				for(int i=0; i<todayBills.length;i++){
					gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()));
			%>
				<tr>
					<td align="center" valign="top"><%=todayBills[i].getFromBranch().getBillPrefix()+"ST"+Utils.padBillNo(todayBills[i].getTransferid()) %></td>
					<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getTransferdate()) %></td>
					<td align="center"><b><%=(todayBills[i].isToApproved()) ? "APPROVED" : "AWAITING" %></b></td>
					<td><%=todayBills[i].getFromBranch().getAccessName()+" - "+todayBills[i].getFromBranch().getCity() %></td>
					<td><%=todayBills[i].getToBranch().getAccessName()+" - "+todayBills[i].getToBranch().getCity() %></td>
					<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())) %></td>
				</tr>
			<%
				}
			%>
			<tr>
				<th colspan="5" align="right">GRAND TOTAL(Rs.)</th>
				<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
			</tr> 
		<%
			}
		%>
		</table>
</body>
</html>