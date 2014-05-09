<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.model.PurchaseModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.TransferModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%

response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=purchase_master_"+DateUtil.getCurrDt()+".xls");

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
Export Transfer Master Data
</title>
</head>
<body onload="window.print();" style="font-size: small;">
<center>
<table style="font-size: 12px; page-break-after: always;" width="100%" cellspacing="0" border="1" bordercolor="#000000">
<tr>
<td><B>Purchase Id</B></td>
<td><B>Branch Prefix</B></td>
<td><B>Received Date</B></td>
<td><B>Invoice No</B></td>
<td><B>Invoice Date</B></td>
<td><B>Discount</B></td>
<td><B>Extra</B></td>
<td><B>Bill Type</B></td>
<td><B>Remark</B></td>
<td><B>Amount</B></td>
<td><B>Supplier Id</B></td>
<td><B>Supplier Name</B></td>
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
</tr>
<%
try {
PurchaseModel[] dtls = (PurchaseModel[]) request.getAttribute("FORM_DATA");
PurchaseModel model = null;

for(int i=0; i<dtls.length; i++){
	
	model = dtls[i];

%>
<tr>
<td><%=model.getPurchaseId() %></td>
<td><%=model.getBranch().getBillPrefix() %></td>
<td><%=Utils.convertToAppDateWithSlash(model.getRecdDt()) %></td>
<td><%=model.getInvNo() %></td>
<td><%=Utils.convertToAppDateWithSlash(model.getInvDt()) %></td>
<td><%=Utils.get2Decimal(model.getDiscount()) %></td>
<td><%=Utils.get2Decimal(model.getExtra()) %></td>
<td><%=model.getBillType() %></td>
<td><%=model.getRemark() %></td>
<td><%=model.getTotalAmt() %></td>
<td><%=model.getSupplier().getCustomerId() %></td>
<td><%=model.getSupplier().getCustomerName() %></td>
<td><%=model.getSupplier().getContactPerson() %></td>
<td><%=model.getSupplier().getAddress() %></td>
<td><%=model.getSupplier().getCity() %></td>
<td><%=model.getSupplier().getState() %></td>
<td><%=model.getSupplier().getPincode() %></td>
<td><%=model.getSupplier().getTin() %></td>
<td><%=model.getSupplier().getCst() %></td>
<td><%=model.getSupplier().getStdcode() %></td>
<td><%=model.getSupplier().getPhone1() %></td>
<td><%=model.getSupplier().getPhone2() %></td>
<td><%=model.getSupplier().getMobile1() %></td>
<td><%=model.getSupplier().getMobile2() %></td>
<td><%=model.getSupplier().getEmail() %></td>
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