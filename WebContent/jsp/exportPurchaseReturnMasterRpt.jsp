<%@page import="com.en.model.PurchaseReturnModel"%>
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
response.setHeader("Content-disposition","inline; filename=purchase_return_master_report_"+DateUtil.getCurrDt()+".xls"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Sales Master Report</title>
</head>
<body onload="window.print();">
	<table width="100%" border="1" cellspacing="1" style="font-size: 11px; font-family: calibri;">
		<tr><th colspan="5" align="center">PURCHASE RETURN MASTER REPORT</th></tr>
		<tr>
			<td colspan="2" align="right">Supplier</td>
			<td colspan="3">
					<%
						String state = "";
						CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ?
												(CustomerModel[])request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0];
						CustomerModel temp = null;
						boolean isCustomer = false;
							for(int i=0; i<customers.length;i++){
								temp=customers[i];
								if(request.getAttribute("sCustomer") != null && ((String)request.getAttribute("sCustomer")).equals(temp.getCustomerId()+"")){
								
					%>
					<%=temp.getCustomerName()%> - <%=temp.getArea()%>
					<%
									isCustomer = true;
									break;
								}
						}
						if(!isCustomer){
					%>
						--
					<%
						}
					%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right" valign="top">Branch</td>
			<td colspan="3">
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
			<td colspan="3">
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
			<td colspan="2" align="right">Recd From Date / To Date</td>
			<td colspan="3"><%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") + " / " : "" %><%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %></td>
		</tr>
		<%
			PurchaseReturnModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (PurchaseReturnModel[]) request.getAttribute(Constant.FORM_DATA) : new PurchaseReturnModel[0];
			if(todayBills.length>0){
		%>
			<tr>
				<th style="width: 70px;">RETURN ID</th>
				<th style="width: 50px;">RETURN DATE</th>
				<th style="width: 80px;">TAX TYPE</th>
				<th>SUPPLIER</th>
				<th style="width: 60px;">TOTAL AMOUNT</th>
			</tr>
			<%
				double gTotal = 0;
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date redDate = new Date();
				redDate.setTime( redDate.getTime() + 2*1000*60*60*24 );
				Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
				for(int i=0; i<todayBills.length;i++){
					gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()));
			%>
				<tr>
					<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+"PR"+Utils.padBillNo(todayBills[i].getPurchaseId()) %></td>
					<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getReturnDt()) %></td>
					<td align="center" ><%=(todayBills[i].getBillType() == 1) ? "VAT" : (todayBills[i].getBillType() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
					<td><%=todayBills[i].getSupplier().getCustomerName()+" - "+todayBills[i].getSupplier().getArea()+" - "+todayBills[i].getSupplier().getCity() %></td>
					<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())) %></td>
				</tr>
			<%
				}
			%>
			<tr>
				<th colspan="4" align="right">GRAND TOTAL(Rs.)</th>
				<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
			</tr> 
		</table>
		<%
			}
		%>
</body>
</html>