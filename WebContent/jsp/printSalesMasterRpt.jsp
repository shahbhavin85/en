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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Sales Master Report</title>
</head>
<body onload="window.print();">
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><td colspan="12" align="center">SALES MASTER REPORT</td></tr>
		<tr>
			<td colspan="2" align="right">Customer</td>
			<td colspan="10">
					<%
						if(request.getAttribute(Constant.CUSTOMER) != null){
					%>
						<%=((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerName() %> - <%=((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getArea()%> - <%=((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCity() %>
					<%
						
						} else {
					%>
						--
					<%
						}
					%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">Salesman</td>
			<td colspan="10">
				<%
				UserModel[] users = request.getAttribute(Constant.USERS) != null ?
						(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
				boolean isUser = false;
						for(int i=0; i<users.length;i++){
							if(request.getAttribute("sUser") != null && ((String)request.getAttribute("sUser")).equals(users[i].getUserId())){
							
				%>
				<%=users[i].getUserName()%>
				<%
								isUser = true;
								break;
							}
					}
					if(!isUser){
				%>
					--
				<%
					}
				%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right" valign="top">Branch</td>
			<td colspan="10">
			<%
				String[] accessSelected = (request.getAttribute("cBranch") != null) ? ((String[])request.getAttribute("cBranch")) : new String[] {"--"};
				AccessPointModel[] accessPoints = request.getAttribute(Constant.USERS) != null ?
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
			<td colspan="2" align="right">Bill Type</td>
			<td colspan="10">
			<%
				selected = new ArrayList(0);
				String[] billTypeSelected = (request.getAttribute("sBillType") != null) ? ((String[])request.getAttribute("sBillType")) : new String[0];
				for(int k=0; k<billTypeSelected.length;k++){
					selected.add((String)billTypeSelected[k]);	
				}
			%>
					<%=(billTypeSelected.length == 0 || (billTypeSelected.length > 0 && selected.indexOf("1")!=-1)) ? "VAT," : ""%>
					<%=(billTypeSelected.length == 0 || (billTypeSelected.length > 0 && selected.indexOf("2")!=-1)) ? "CST, " : ""%>
					<%=(billTypeSelected.length == 0 || (billTypeSelected.length > 0 && selected.indexOf("3")!=-1)) ? "CST AGAINT FORM 'C'" : ""%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">Paymode</td>
			<td colspan="10">
			<%
				selected = new ArrayList(0);
				String[] payModeSelected = (request.getAttribute("sPayMode") != null) ? ((String[])request.getAttribute("sPayMode")) : new String[0];
				for(int k=0; k<payModeSelected.length;k++){
					selected.add((String)payModeSelected[k]);	
				}
			%>
					<%=(payModeSelected.length == 0 || (payModeSelected.length > 0 && selected.indexOf("1")!=-1)) ? "CASH, " : ""%>
					<%=(payModeSelected.length == 0 || (payModeSelected.length > 0 && selected.indexOf("2")!=-1)) ? "CC CARD, " : ""%>
					<%=(payModeSelected.length == 0 || (payModeSelected.length > 0 && selected.indexOf("3")!=-1)) ? "CREDIT" : ""%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">Bill From Date / To Date</td>
			<td colspan="10"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
	<%
		SalesModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (SalesModel[]) request.getAttribute(Constant.FORM_DATA) : new SalesModel[0];
		if(todayBills.length>0){
	%>
		<tr>
			<th style="width: 60px;">BILL NO</th>
			<th style="width: 50px;">BILL DATE</th>
			<th style="width: 50px;">COLL. DAYS</th>
			<th style="width: 80px;">TAX TYPE</th>
			<th style="width: 50px;">PAYMODE</th>
			<th style="width: 50px;">DUE DATE</th>
			<th style="width: 80px;">SALESMAN</th>
			<th>CUSTOMER</th>
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
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
			Date tempDate = null;
			Date tempPaidDate = null;
			for(int i=0; i<todayBills.length;i++){
				if(todayBills[i].getStatus() == 1){
				
		%>
			<tr style="background-color: black; color: white;">
				<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></td>
				<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
				<td align="center" valign="top">--</td>
				<td align="center" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
				<td align="center" valign="top"><%=(todayBills[i].getPaymode() == 1) ? "CASH" : (todayBills[i].getPaymode() == 2) ? "CC CARD" : "CREDIT"%></td>
				<td align="center" valign="top">--</td>
				<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
				<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
				<td align="center" valign="top" colspan="4">CANCELLED</td>
			</tr>
		<%
				} else {
					roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) ;
					gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff);
					pendintTotal = pendintTotal + ((todayBills[i].getPaymode() == 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-Utils.get2DecimalDouble(todayBills[i].getPayAmt())) : 0);
					paidTotal = paidTotal + ((todayBills[i].getPaymode() != 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) : Utils.get2DecimalDouble(todayBills[i].getPayAmt()));
					tempDate = (formatter.parse(Utils.convertToSQLDate(todayBills[i].getSalesdate())));
					if(todayBills[i].getPayDate() != null)
						tempPaidDate = (formatter.parse(Utils.convertToSQLDate(todayBills[i].getPayDate())));
		%>
			<tr>
				<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></td>
				<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
				<td align="center" valign="top"><%=(todayBills[i].getPaymode() == 3) ? (((Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt())) != 0)) ? ((todayDate.getTime() - tempDate.getTime()) / (1000*60*60*24))+" Days" : ((tempPaidDate.getTime() - tempDate.getTime()) / (1000*60*60*24))+" Days") : "--" %></td>
				<td align="center" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
				<td align="center" valign="top"><%=(todayBills[i].getPaymode() == 1) ? "CASH" : (todayBills[i].getPaymode() == 2) ? "CC CARD" : "CREDIT"%></td>
				<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getDueDate()) %></td>
				<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
				<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) %></td>
				<td align="center" valign="top"><%=(todayBills[i].getPaymode() != 3) ? Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) : (todayBills[i].getPayDate()!=null && !todayBills[i].getPayDate().equals("0000-00-00")) ? Utils.convertToAppDateDDMMYY(todayBills[i].getPayDate()) : ""  %></td>
				<td align="right" valign="top"><%=(todayBills[i].getPaymode() != 3) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) : Utils.get2Decimal(todayBills[i].getPayAmt()) %></td>
				<td align="right" valign="top"><%=(todayBills[i].getPaymode() == 3 && (Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt())) != 0)) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt()) : "--"%></td>
			</tr>
		<%
				}
			}
		%>
		<tr>
			<th colspan="8" align="right">GRAND TOTAL(Rs.)</th>
			<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
			<th align="center" valign="top">--</th>
			<th align="right"><%=Utils.get2Decimal(paidTotal) %></th>
			<th align="right"><%=Utils.get2Decimal(pendintTotal) %></th>
		</tr> 
	</table>
	<%
		}
	%>
</body>
</html>