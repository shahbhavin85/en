<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.util.Constant"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.TransferModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%

response.setHeader("Content-type","application/xls");
response.setHeader("Content-disposition","inline; filename=transfer_master_"+DateUtil.getCurrDt()+".xls");

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
<td><B>Transfer No</B></td>
<td><B>Branch Prefix</B></td>
<td><B>Transfer Date</B></td>
<td><B>From</B></td>
<td><B>From Id</B></td>
<td><B>To</B></td>
<td><B>To Id</B></td>
<td><B>Remark</B></td>
<td><B>Amount</B></td>
</tr>
<%
try {
TransferModel[] dtls = (TransferModel[]) request.getAttribute("FORM_DATA");
TransferModel model = null;

for(int i=0; i<dtls.length; i++){
	
	model = dtls[i];

%>
<tr>
<td><%=model.getTransferid() %></td>
<td><%=model.getFromBranch().getBillPrefix() %></td>
<td><%=Utils.convertToAppDateWithSlash(model.getTransferdate()) %></td>
<td><%=Constant.TITLE + " - " +model.getFromBranch().getCity() %></td>
<td><%=model.getFromBranch().getAccessId() %></td>
<td><%=Constant.TITLE + " - " +model.getToBranch().getCity() %></td>
<td><%=model.getToBranch().getAccessId() %></td>
<td><%=model.getRemark() %></td>
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