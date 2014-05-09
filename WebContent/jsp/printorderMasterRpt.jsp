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
<%@page import="com.en.model.OrderModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Order Master Report</title>
</head>
<body onload="window.print();">
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><td colspan="10" align="center">ORDER MASTER REPORT</td></tr>
		<tr>
			<td colspan="2" align="right">Customer</td>
			<td colspan="8">
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
			<td colspan="8">
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
			<td colspan="8">
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
			<td colspan="8">
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
			<td colspan="2" align="right">Order From Date / To Date</td>
			<td colspan="8"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
		<tr>
			<td colspan="2" align="right">Order of</td>
			<td colspan="8"><%=(request.getAttribute("rOrderOf") != null && ((String)request.getAttribute("rOrderOf")).equals("0")) ?  "ALL" : "ONLY EXHIBITION" %></td>
		</tr>
	<%
		OrderModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (OrderModel[]) request.getAttribute(Constant.FORM_DATA) : new OrderModel[0];
		if(todayBills.length>0){
	%>
		<tr>
			<th style="width: 60px;">ORDER NO</th>
			<th style="width: 50px;">ORDER DATE</th>
			<th style="width: 80px;">TAX TYPE</th>
			<th style="width: 80px;">SALESMAN</th>
			<th>CUSTOMER</th>
			<th style="width: 60px;">EXHIBITION</th>
			<th style="width: 60px;">TOTAL AMOUNT</th>
			<th style="width: 60px;">ADVANCE AMOUNT</th>
			<th style="width: 60px;">PENDING AMOUNT</th>
			<th style="width: 60px;">STATUS</th>
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
				roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) ;
				gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff);
				pendintTotal = pendintTotal + (Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-Utils.get2DecimalDouble(todayBills[i].getAdvance())));
				paidTotal = paidTotal + (Utils.get2DecimalDouble(todayBills[i].getAdvance()));
		%>
			<tr>
				<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %></td>
				<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getOrderDate()) %></td>
				<td align="center" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
				<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
				<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
				<td align="center" valign="top"><%=(todayBills[i].getFromEx() == 1) ? "YES" : "NO"%></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())) %></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getAdvance())) %></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())-todayBills[i].getAdvance()) %></td>
				<td valign="top"><%=todayBills[i].getStatusString() %></td>
			</tr>
		<%
			}
		%>
		<tr>
			<th colspan="6" align="right">GRAND TOTAL(Rs.)</th>
			<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
			<th align="right"><%=Utils.get2Decimal(paidTotal) %></th>
			<th align="right"><%=Utils.get2Decimal(pendintTotal) %></th>
			<th>&nbsp;</th>
		</tr> 
	</table>
	<%
		}
	%>
</body>
</html>