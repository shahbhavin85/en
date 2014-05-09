<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%

response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=sales_return_items_"+DateUtil.getCurrDt()+".xls");

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
Export Sales Item Data
</title>
</head>
<body onload="window.print();" style="font-size: small;">
<center>
<table style="font-size: 12px; page-break-after: always;" width="100%" cellspacing="0" border="1" bordercolor="#000000">
<tr>
<td><B>Bill No</B></td>
<td><B>Bill Prefix</B></td>
<td><B>Item Id</B></td>
<td><B>Item Number</B></td>
<td><B>Item Name</B></td>
<td><B>Qty</B></td>
<td><B>Rate</B></td>
<td><B>Tax %</B></td>
<td><B>Dis Rate</B></td>
</tr>
<%
try {
SalesItemModel[] dtls = (SalesItemModel[]) request.getAttribute("FORM_DATA");
SalesItemModel model = null;

for(int i=0; i<dtls.length; i++){
	
	model = dtls[i];

%>
<tr>
<td><%=model.getSalesId() %></td>
<td><%=model.getBillPrefix() %></td>
<td><%=model.getItem().getItemId() %></td>
<td><%=model.getItem().getItemNumber() %></td>
<td><%=model.getItem().getItemName() %></td>
<td><%=model.getQty() %></td>
<td><%=model.getRate() %></td>
<td><%=model.getTaxrate()%></td>
<td><%=model.getDisrate() %></td>
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