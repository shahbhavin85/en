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
<%@page import="com.en.model.CreditNoteModel"%>
<%
response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=credit_note_master_report_"+DateUtil.getCurrDt()+".xls"); 
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Credit Note Master Report</title>
</head>
<body>
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><th colspan="6" align="center">CREDIT NOTE MASTER REPORT</td></th>
		<tr>
			<td colspan="2" align="right">Customer</td>
			<td colspan="4">
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
			<td colspan="4">
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
			<td colspan="4">
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
			<td colspan="4">
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
			<td colspan="2" align="right">Bill From Date / To Date</td>
			<td colspan="4"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
	<%
		CreditNoteModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (CreditNoteModel[]) request.getAttribute(Constant.FORM_DATA) : new CreditNoteModel[0];
		if(todayBills.length>0){
	%>
		<tr>
			<th style="width: 60px;">CREDIT NOTE NO</th>
			<th style="width: 50px;">NOTE DATE</th>
			<th style="width: 80px;">TAX TYPE</th>
			<th style="width: 80px;">SALESMAN</th>
			<th>CUSTOMER</th>
			<th style="width: 60px;">TOTAL AMOUNT</th>
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
				roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()-todayBills[i].getLess())%1)) ;
				gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()-todayBills[i].getLess())+roundOff);
		%>
			<tr>
				<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+"CN"+Utils.padBillNo(todayBills[i].getSaleid()) %></td>
				<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
				<td align="center" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
				<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
				<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
				<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()-todayBills[i].getLess())+roundOff) %></td>
			</tr>
		<%
			}
		%>
		<tr>
			<th colspan="5" align="right">GRAND TOTAL(Rs.)</th>
			<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
		</tr> 
	</table>
	<%
		}
	%>
</body>
</html>