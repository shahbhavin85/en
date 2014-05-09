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
<%@page import="com.en.model.LabourBillModel"%>
<%
response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=labour_bill_master_report_"+DateUtil.getCurrDt()+".xls"); %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Export Labour Bill Master Report</title>
</head>
<body>
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><td colspan="8" align="center">LABOUR BILL MASTER REPORT</td></tr>
		<tr>
			<td colspan="2" align="right">Customer</td>
			<td colspan="6">
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
			<td colspan="6">
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
			<td colspan="6">
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
			<td colspan="2" align="right">Bill From Date / To Date</td>
			<td colspan="6"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
	<%
		LabourBillModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (LabourBillModel[]) request.getAttribute(Constant.FORM_DATA) : new LabourBillModel[0];
		if(todayBills.length>0){
	%>
		<tr>
			<th style="width: 60px;">BILL NO</th>
			<th style="width: 50px;">BILL DATE</th>
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
				roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt())%1)) ;
				gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())+roundOff);
				pendintTotal = pendintTotal + (Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())+roundOff-Utils.get2DecimalDouble(todayBills[i].getPayAmt())));
				paidTotal = paidTotal + (Utils.get2DecimalDouble(todayBills[i].getPayAmt()));
				tempDate = (formatter.parse(Utils.convertToSQLDate(todayBills[i].getSalesdate())));
				if(todayBills[i].getPayDate() != null)
					tempPaidDate = (formatter.parse(Utils.convertToSQLDate(todayBills[i].getPayDate())));
		%>
			<tr>
				<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+"LB"+Utils.padBillNo(todayBills[i].getSaleid()) %></td>
				<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
				<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
				<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())+roundOff) %></td>
				<td align="center" valign="top"><%=(todayBills[i].getPayDate()!=null && !todayBills[i].getPayDate().equals("0000-00-00")) ? Utils.convertToAppDateDDMMYY(todayBills[i].getPayDate()) : ""  %></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(todayBills[i].getPayAmt()) %></td>
				<td align="right" valign="top"><%=((Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())+roundOff-todayBills[i].getPayAmt())) != 0)) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())+roundOff-todayBills[i].getPayAmt()) : "--"%></td>
			</tr>
		<%
			}
		%>
		<tr>
			<th colspan="4" align="right">GRAND TOTAL(Rs.)</th>
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