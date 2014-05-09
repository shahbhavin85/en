<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.en.util.Utils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
</head>
<body onload="window.print();" style="font-family: tahoma;">
<form method="post" name="frmAddAccessPoint">
	<table cellpadding="3" width="100%">
		<tr>
			<td align="center"><b><u>WHOLESALES PRICE LIST</u></b></td>
		</tr>
<!-- 		<tr> -->
<!-- 			<td align="center">W.E.F 04/11/2012</td> -->
<!-- 		</tr> -->
	</table>
	<%
		if(request.getAttribute("items") != null){
			HashMap<String,HashMap<String,ItemModel[]>> items = (HashMap<String,HashMap<String,ItemModel[]>>) request.getAttribute("items");
			String[] groupArr = (String[])items.keySet().toArray(new String[0]);
			Arrays.sort(groupArr);
			HashMap<String,ItemModel[]> tempMap = null;
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
	<table width="98%" cellpadding="2" cellspacing="0" style="font-size: 9px; margin: 10px; font-family: tahoma;">
		<tr><th colspan="4"><font style="font-size: 13px; font-style: italic;"><%=cName%></font> - <font style="font-size: 10px;"><%=gName%></font></th></tr>
		<tr style="color: white; background-color: black;">
			<th width="10%">ITEM ID</th>
			<th width="15%">ITEM NUMBER</th> 
			<th>ITEM NAME</th>
			<th width="10%">WSP<br/><font style="font-size: 8px;">(TAXES EXTRA)</font></th>
		</tr>
	<% 
					ItemModel[] item = (ItemModel[]) tempMap.get(cName);
					for(int i=0; i<item.length; i++){
	%>
		<tr style="background-color: <%=(i%2==0) ? "white" : "silver"%>; padding 2px;">
			<td align="center" style="padding: 3px;"><%=item[i].getItemId()%></td>
			<td align="center" style="padding: 3px;"><%=item[i].getItemNumber()%></td>
			<td align="center" style="padding: 3px;"><%=item[i].getItemName()%></td>
			<td align="center" style="padding: 3px;"><%=(Double.parseDouble(item[i].getItemWPrice()) == 0) ? "***" : item[i].getItemWPrice()%></td>
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
	<font style="font-size: 10px;">*** - Please ask the quote from the store.</font><BR/><BR/>
	<font style="font-size: 10px; font-weight: bold; text-decoration: underline;">TERMS & CONDITIONS:</font>
	<ul title="Terms & Conditions:" style="font-size: 10px;">
		<li>No return, no exchange allowed, discount rates against payment only.</li>
		<li>Sales tax extra as per the state government law. Packing & Forwarding extra.</li>
		<li>Company holds the right to alter, change or withdraw the scheme without prior notice.</li> 
	</ul>
</form>
</body>
</html>