<%@page import="com.en.util.Constant"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.LabourBillModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<% LabourBillModel model = (LabourBillModel) request.getAttribute("FORM_DATA"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Sales Bill</title>
</head>
<body onload="window.print();">
<%
	String[] copyType = (String[]) request.getAttribute("copy");
	for(int a=0; a<copyType.length; a++){
		if(a!=0){
%>
<table style="width: 100%; padding-left : 0.4in; padding-right: 0.4in;"><tr><td>
<%		
		} else {
%>
<table style="width: 100%; padding: 0.4in; padding-left : 0.4in; padding-right: 0.4in; padding-top: 0.4in; padding-bottom: 0px;"><tr><td>
<%
		}
%>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><U>LABOUR INVOICE</U></label> <label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><%=((model.getCustomer().getTin().equals("") && !model.getBranch().getNoTin().equals(""))) ? "<br/>"+model.getBranch().getNoTin() : (!model.getBranch().getNoTin().equals("")) ? "<br/>"+model.getBranch().getWithTin() : "" %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 15px;"><%=copyType[a] %></label></center><br/>
<center></center>
<center><label style="font-family: Cambria; font-size: 22px; font-stretch: wider; font-weight: bold;"><%=Constant.TITLE%></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 14px;"><%= model.getBranch().getAddress() %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 12px;"> <b> Phone No:</b> <%= model.getBranch().getStdcode() + " - " +model.getBranch().getPhone1() + "/" +model.getBranch().getPhone2() %>  <b> Email: </b><%=model.getBranch().getEmail() %></label></center>
<center><label style="font-family: Calibri; font-weight: bold; font-size: 12px;"> <b> PAN No:</b> AABCH5436K</center>
<table width="100%" style="font-family: Calibri; font-size: 12px;">
	<tr>
		<td align="left"><b>Invoice No.:</b> <%=model.getBranch().getBillPrefix()+"LB"+Utils.padBillNo(model.getSaleid())%></td>
		<td align="right"><b> Date:</b> <%=Utils.convertToAppDateDDMMYY(model.getSalesdate())%></td>
	</tr>
</table>
<table width="100%" style="font-family: Calibri; font-size: 11px;" cellspacing="0" border="1">
	<tr>
		<td align="left" rowspan="5" valign="top" width="3%"><b>M/s:</b></td>
		<td align="left" rowspan="5" valign="top" width="62%"><%=model.getCustomer().getLabel()%></td>
		<td align="left" width="35%" colspan="2"><b> VAT No.:</b> <%=(model.getCustomer().getTin().equals("")) ? "URD" : model.getCustomer().getTin()%></td>
	</tr>
	<tr>
		<td align="left" width="35%" colspan="2"><b> CST No.:</b> <%=model.getCustomer().getCst()%></td>
	</tr>
	<tr>
		<td align="left" width="35%" colspan="2"><b> Contact Person:</b> <%=model.getCustomer().getContactPerson() %></td>
	</tr>
	<tr>
		<td align="left" width="35%" colspan="2"><b> Contact Phone:</b> <%=model.getCustomer().getStdcode()+"-"+model.getCustomer().getPhone1()+"/"+model.getCustomer().getPhone2()%></td>
	</tr>
	<tr>
		<td align="left" width="35%" colspan="2"><b> Contact Mobile:</b> <%=model.getCustomer().getMobile1()+"/"+model.getCustomer().getMobile2()%></td>
	</tr>
	<tr>
		<th>Representative</th>
		<td colspan="3" style="font-weight: bold;"><%=model.getSalesman().getUserName()%></td>
	</tr>
	<tr>
		<th colspan="2">Description</th>
		<th colspan="2">Amount</th>
	</tr>
	<tr>
		<td colspan="2" valign="top" style="height: 50px; font-weight: bold;"><%=model.getRemarks()%></td>
		<td colspan="2" style="font-weight: bold;font-size: 18px;" align="center">Rs.<%=Utils.get2Decimal(model.getTotalAmt())%></td>
	</tr> 
	<%
		if(((String)copyType[a]).equals("OFFICE COPY")){
	%>
	<tr>
		<td colspan="4" style="font-weight: bold;">
			<table width="100%" border="1" cellspacing="0">
				<tr><th colspan="2">PAYMENT DETAILS</th></tr>
				<tr>
					<td width="50%">
						<table cellspacing="0" width="100%" cellpadding="0">
							<tr><th>CHEQUE RECEIVED</th></tr>
							<tr><td style="font-weight: bold;">Cheque Amt : </td></tr>
							<tr><td style="font-weight: bold;">Cheque Dt : </td></tr>
							<tr><td style="font-weight: bold;">Cheque No : </td></tr>
							<tr><td style="font-weight: bold;">Bank : </td></tr>
						</table>
					</td>
					<td width="50%">
						<table cellspacing="0" width="100%" cellpadding="0">
							<tr><th>CASH RECEIVED</th></tr>
							<tr><td style="font-weight: bold;">Amt : </td></tr>
							<tr><td style="font-weight: bold;">In Words : </td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="height: 40px;" valign="top">For <b>Customer Seal & Signature</b></td>
		<td colspan="2"  valign="top">For <b><%=Constant.TITLE%></b></td>
	</tr> 
</table>
	<%
		} else {
	%>
	<tr>
		<td colspan="2" style="height: 100px;" valign="top">For <b>Customer Seal & Signature</b></td>
		<td colspan="2"  valign="top">For <b><%=Constant.TITLE%></b></td>
	</tr>
</table>
<center><br/><label style="font-size:9px; font-family: Calibri;font-weight : bold;"><u>Registered Office:</u></label><br/><label style="font-size:11px; font-family: Calibri;"><b><%=Constant.TITLE%>,</b> <%=Constant.CORP_ADDRESS%> Email : <%=Constant.CORP_EMAIL%></label></center>
	<%
		}
	%>
</td></tr></table>
<%} %>
</body>
</html>