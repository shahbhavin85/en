<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.CustomerModel"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Print Item Label</title>
</head>
<body>
<table cellspacing="0" border="1" bordercolor="white" style="border-spacing : 0mm; margin-left : 2mm; margin-top: 12mm; font-size: 7px; font-family: tahoma;">
<%
	String[] custList = (request.getAttribute(Constant.FORM_DATA) != null) ? (String[]) request.getAttribute(Constant.FORM_DATA) : new String[0];
	for(int i=0; i<custList.length; i++){
		if(i%5 == 0 && i%65!=0){
			if(i%5==0 && i!=0){
%>
	</tr>
<%
			}
%>
	<tr>
<%		} %>
<%
	if(i%65==0 && i != 0){
%>
	</table>
	<table cellspacing="0" border="1" bordercolor="white" style="page-break-before: always; border-spacing : 0mm; margin-left : 4mm; margin-top: 12mm; font-size: 8px; font-family: tahoma;">
		<tr style="border-color: white;">
<%
	}
%>
	<td align="right" style="width: <%=(i%5!=0) ? "41mm" : "39mm" %>; border-color:white; height: <%=(i%5!=0) ? "18.5mm" : "19mm" %>; border-spacing: 0mm;  padding-left: <%=(i%5!=0) ? "4mm" : "2mm" %>;"><div align="center"><%=custList[i]%></div><table width="100%" cellspacing="0"><tr><td style="font-size: 1.5mm;  font-family: courier new;">* TAXES EXTRA<BR/>MARKETED BY:<br/><%=Constant.TITLE%><BR/>CHENNAI</td><td align="right"><img alt="hesh" src="images/logo_small.jpg" style="width: 6mm; padding-right: 2mm;"></td></tr></table></td>
	<%-- <td style="width: 63mm; height: 32mm; padding-left: 7mm; padding-top: 2px; border-spacing: 0px;" valign="middle"><%=custList[i].getLabel()+((!custList[i].getPhone1().equals("")) ? ((!custList[i].getPhone2().equals("")) ? "<br/> Phone : "+custList[i].getPhone1()+"/"+custList[i].getPhone2() : "<br/> Phone : "+custList[i].getPhone1()) : "")%></td> --%>
<%	
	}
%>
	</tr>
	</table>
</body>
</html>