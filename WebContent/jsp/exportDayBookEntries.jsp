<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%

response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=branch_day_book_"+DateUtil.getCurrDt()+".xls");

long totalQuantity = 0;
double totalTax = 0.00;
double totalDiscount = 0.00;
double totalAmount = 0.00;

%>

<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>
Export Branch Day Book Data
</title>
</head>
<body onload="window.print();" style="font-size: small;">
<center>
<table style="font-size: 12px; page-break-after: always;" width="100%" cellspacing="0" border="1" bordercolor="#000000">
<tr>
<td><B>Branch Prefix</B></td>
<td><B>Entry Date</B></td>
<td><B>Entry Id</B></td>
<td><B>Entry Type</B></td>
<td><B>Amount</B></td>
<td><B>Hesh Bank</B></td>
<td><B>Customer Bank</B></td>
<td><B>Cheque No</B></td>
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
<td><B>Branch</B></td>
<td><B>PAID / RECEIVED</B></td>
</tr>
<%
try {
EntryModel[] dtls = (EntryModel[]) request.getAttribute("FORM_DATA");
EntryModel model = null;

for(int i=0; i<dtls.length; i++){
	
	model = dtls[i];

%>
<tr>
<td><%=model.getBranch().getBillPrefix() %></td>
<td><%=Utils.convertToAppDateWithSlash(model.getEntryDate()) %></td>
<td><%=model.getEntryId() %></td>
<td><%=Utils.getEntryType(model.getEntryType()) %></td>
<td><%=Utils.get2Decimal(model.getAmount()) %></td>
<td><%=model.getBankName() %></td>
<td><%=model.getCustBankName() %></td>
<td><%=model.getChqNo() %></td>
<td><%=model.getRemark() %></td>
<td><%=(model.getCustomer().getCustomerId() == 0) ? "" : model.getCustomer().getCustomerId() %></td>
<td><%=model.getCustomer().getCustomerName() %></td>
<td><%=model.getCustomer().getContactPerson() %></td>
<td><%=Utils.convertStringWithSpace(model.getCustomer().getAddress()) %></td>
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
<td><%=model.getBillBranch().getBillPrefix() %></td>
<td><%=model.getEntryType() > 50 ? "P" : "R" %></td>
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