<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.model.LabourBillModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%

response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=labour_bill_master_"+DateUtil.getCurrDt()+".xls");

String title = (String) request.getAttribute("title");
String[] headers = (String[]) request.getAttribute("headers");

long totalQuantity = 0;
double totalTax = 0.00;
double totalDiscount = 0.00;
double totalAmount = 0.00;

%>

<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>
Export Sales Master Data
</title>
</head>
<body onload="window.print();" style="font-size: small;">
<center>
<table style="font-size: 12px; page-break-after: always;" width="100%" cellspacing="0" border="1" bordercolor="#000000">
<tr>
<td><B>Bill No</B></td>
<td><B>Bill Prefix</B></td>
<td><B>Bill Date</B></td>
<td><B>Salesman</B></td>
<td><B>Remark</B></td>
<td><B>Customer Id</B></td>
<td><B>Customer Name</B></td>
<td><B>Contact Person</B></td>
<td><B>Address</B></td>
<td><B>City</B></td>
<td><B>State</B></td>
<td><B>Pincode</B></td>
<td><B>VAT</B></td>
<td><B>CST</B></td>
<td><B>Stdcode</B></td>
<td><B>Phone1</B></td>
<td><B>Phone2</B></td>
<td><B>Mobile1</B></td>
<td><B>Mobile2</B></td>
<td><B>Email</B></td>
<td><B>Amount</B></td>
</tr>
<%
try {
LabourBillModel[] dtls = (LabourBillModel[]) request.getAttribute("FORM_DATA");
LabourBillModel model = null;

for(int i=0; i<dtls.length; i++){
	
	model = dtls[i];

%>
<tr>
<td><%=model.getSaleid() %></td>
<td><%=model.getBranch().getBillPrefix() %></td>
<td><%=Utils.convertToAppDateWithSlash(model.getSalesdate()) %></td>
<td><%=model.getSalesman().getUserName() %></td>
<td><%=model.getRemarks() %></td>
<td><%=model.getCustomer().getCustomerId() %></td>
<td><%=model.getCustomer().getCustomerName() %></td>
<td><%=model.getCustomer().getContactPerson() %></td>
<td><%=model.getCustomer().getAddress() %></td>
<td><%=model.getCustomer().getCity() %></td>
<td><%=model.getCustomer().getState() %></td>
<td><%=model.getCustomer().getPincode() %></td>
<td><%=model.getCustomer().getTin() %></td>
<td><%=model.getCustomer().getCst() %></td>
<td><%=model.getCustomer().getStdcode() %></td>
<td><%=model.getCustomer().getPhone1() %></td>
<td><%=model.getCustomer().getPhone2() %></td>
<td><%=model.getCustomer().getMobile1() %></td>
<td><%=model.getCustomer().getMobile2() %></td>
<td><%=model.getCustomer().getEmail() %></td>
<td><%=Utils.get2Decimal(model.getTotalAmt()) %></td>
</tr>
<%
}
}
catch(Exception ex) {
    out.println ("<br>Exception in Done Purchase "+ex);
}
%>
</table>
</center>
</body>
</html>