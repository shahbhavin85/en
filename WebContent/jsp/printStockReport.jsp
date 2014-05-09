<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
</head>
<body onload="window.print();" style="font-family: tahoma;">
<form method="post" name="frmAddAccessPoint">
	<table cellpadding="3" width="100%" style="font-size: 12px;">
		<tr>
			<td align="center"><b><u>STOCK REPORT - (<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessName()%>)</u></b></td>
		</tr>
		<tr>
			<td align="center">AS ON <%=Utils.convertToAppDateDDMMYY(DateUtil.getCurrDt()) %></td>
		</tr>
	</table>
	<%
		if(request.getAttribute("items") != null){
			HashMap<String,HashMap<String,SalesItemModel[]>> items = (HashMap<String,HashMap<String,SalesItemModel[]>>) request.getAttribute("items");
			String[] groupArr = (String[])items.keySet().toArray(new String[0]);
			Arrays.sort(groupArr);
			HashMap<String,SalesItemModel[]> tempMap = null;
			String gName = "", cName = "";
			for(int j=0; j<groupArr.length; j++){
				gName = groupArr[j];
	%>
	<%
				tempMap = items.get(gName);
				String[] catArr = (String[])tempMap.keySet().toArray(new String[0]);
				Arrays.sort(catArr);
				for(int k=0; k<catArr.length; k++){
					cName = catArr[k];
	%>
	<table width="98%" cellpadding="2" border="1" cellspacing="0" style="font-size: 10px; margin: 10px; font-family: tahoma; border: 1px;" bordercolor="gray">
		<tr><th colspan="4" style="border-left-color: white;border-right : white;border-top-color : white;"><font style="font-size: 13px; font-style: italic;"><%=cName%></font> - <font style="font-size: 10px;"><%=gName%></font></th></tr>
		<tr style="color: white; background-color: black;">
			<th width="10%">ITEM ID</th>
			<th width="15%">ITEM NUMBER</th> 
			<th>ITEM NAME</th>
			<th width="10%">STOCK</th>
		</tr>
	<% 
					SalesItemModel[] item = (SalesItemModel[]) tempMap.get(cName);
					for(int i=0; i<item.length; i++){
	%>
		<tr style="background-color: <%=(i%2==0) ? "silver" : "white"%>; padding 2px;">
			<td align="center" style="padding: 3px;"><%=item[i].getItem().getItemId()%></td>
			<td align="center" style="padding: 3px;"><%=item[i].getItem().getItemNumber()%></td>
			<td align="center" style="padding: 3px;"><%=item[i].getItem().getItemName()%></td>
			<td align="center" style="padding: 3px;"><%=(item[i].getQty() == 0) ? "N/A" : item[i].getQty()%></td>
		</tr>
	<%
					} 
	%>
	</table>
	<%
				}
			}
		}
	%>
	<font style="font-size: 10px;">N/A - Not Available.</font><BR/><BR/>
</form>
</body>
</html>