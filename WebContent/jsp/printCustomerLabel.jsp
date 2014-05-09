<%@page import="com.en.model.CustomerModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Customer Label</title>
</head>
<body>
<table cellspacing="0" border="1" bordercolor="white" style="page-break-after: always;border-color: white; border: 2mm white; border-spacing : 0mm; margin-left : 5mm; margin-top: 11mm; font-size: 9px; font-family: tahoma;">
<%
	CustomerModel[] custList = (request.getAttribute("custLst") != null) ? (CustomerModel[]) request.getAttribute("custLst") : new CustomerModel[0];
	for(int i=0; i<custList.length; i++){
		if(i%3 == 0 && i%24!=0){
			if(i%3==0 && i!=0){
%>
	</tr>
<%
			}
%>
	<tr style="border-spacing: 2px; border-color: white;">
<%		} %>
<%
	if(i%24==0 && i != 0){
%>
	</table>
	<table cellspacing="0" bordercolor="white" border="1" style="page-break-after: always;border-color: white; border: 2mm white; border-spacing : 0mm; margin-left : 5mm; margin-top: 11mm; font-size: 9px; font-family: tahoma;">
		<tr style="border-spacing: 2px; border-color: white;">
<%
	}
%>
	<td style="width: <%=(i%3!=0) ? "62mm" : "60mm" %>; border-color:white; height: 32mm; border-spacing: 0mm;  padding-left: <%=(i%3!=0) ? "7mm" : "5mm" %>;"><%=custList[i].getLabel()+((!custList[i].getPhone1().equals("")) ? ((!custList[i].getPhone2().equals("")) ? "<br/> Phone : "+custList[i].getPhone1()+"/"+custList[i].getPhone2() : "<br/> Phone : "+custList[i].getPhone1()) : ((!custList[i].getMobile1().equals("")) ? ((!custList[i].getMobile2().equals("")) ? "<br/> Mobile : "+custList[i].getMobile1()+"/"+custList[i].getMobile2() : "<br/> Phone : "+custList[i].getMobile1()) : ""))%></td>
	<%-- <td style="width: 63mm; height: 32mm; padding-left: 7mm; padding-top: 2px; border-spacing: 0px;" valign="middle"><%=custList[i].getLabel()+((!custList[i].getPhone1().equals("")) ? ((!custList[i].getPhone2().equals("")) ? "<br/> Phone : "+custList[i].getPhone1()+"/"+custList[i].getPhone2() : "<br/> Phone : "+custList[i].getPhone1()) : "")%></td> --%>
<%	
	}
%>
	</tr>
	</table>
</body>
</html>