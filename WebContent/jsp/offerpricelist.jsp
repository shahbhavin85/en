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
<style type="text/css">
.header {
	font-size: 14px;
	font-family: tahoma;
}

.pl {
	font-size: 9px; 
	margin: 10px; 
	font-family: tahoma;
}

.pl th{
	color: white; 
	background-color: black;
}

.pl-alternative tr{
	background-color: silver;
}

.pl-basic tr{
	background-color: white;
}
</style>
</head>
<body onload="window.print();">
<form method="post" name="frmAddAccessPoint">
	<table class="header" cellpadding="3" width="100%">
		<tr>
			<td align="center"><b><u>OFFER PRICE LIST</u></b></td>
		</tr>
		<tr>
			<td align="center">W.E.F 04/11/2012</td>
		</tr>
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
	<table width="98%" cellpadding="2" cellspacing="0" class="pl">
		<tr><th colspan="7" style="background-color: white; color: black;"><font style="font-size: 13px; font-style: italic;"><%=cName%></font> - <font style="font-size: 10px;"><%=gName%></font></th></tr>
		<tr>
			<th width="10%">ITEM ID</th>
			<th width="15%">ITEM NUMBER</th> 
			<th>ITEM NAME</th>
			<th width="10%">MRP<br/><font style="font-size: 8px;">(TAXES EXTRA)</font></th>
			<th width="10%">DISCOUNT</th>
			<th width="10%">DISCOUNT PRICE</th>
			<th width="20%">PERIOD</th>
		</tr>
	<% 
					ItemModel[] item = (ItemModel[]) tempMap.get(cName);
					for(int i=0; i<item.length; i++){
	%>
		<tr style="background-color: <%=(i%2==0) ? "white" : "silver"%>; padding 2px;">
			<td align="center" style="padding: 3px;"><%=item[i].getItemId()%></td>
			<td align="center" style="padding: 3px;"><%=item[i].getItemNumber()%></td>
			<td align="center" style="padding: 3px;"><%=item[i].getItemName()%></td>
			<td align="center" style="padding: 3px;"><%=(Double.parseDouble(item[i].getItemPrice()) == 0) ? "***" : item[i].getItemPrice()%></td>
			<td align="center" style="padding: 3px;"><%=(item[i].getDiscount() != null && !item[i].getDiscount().equals("") && Double.parseDouble(item[i].getDiscount()) > 0) ? item[i].getDiscount()+"%" : "NA"%></td>
			<td align="center" style="padding: 3px;"><%=(item[i].getDiscount() != null && !item[i].getDiscount().equals("") && Double.parseDouble(item[i].getDiscount()) > 0) ? Utils.get2Decimal(Double.parseDouble(item[i].getItemPrice()) * ((100-Double.parseDouble(item[i].getDiscount()))/100)) : "NA"%></td>
			<td align="center" style="padding: 3px;"><%=(item[i].getDiscount() != null && !item[i].getDiscount().equals("") && Double.parseDouble(item[i].getDiscount()) > 0) ? Utils.convertToAppDateDDMMYY(item[i].getFromDate())+" To "+ Utils.convertToAppDateDDMMYY(item[i].getToDate()) : "NA"%></td>
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
	<ul style="font-size: 10px;">
		<li>SCHEME VALIDITY :: limited period offer 04 Nov 2012 to 20 Nov 2012 (17 Days Offer).</li>
		<li>Book your order within the offer date with 30% advance payment, and schedule your delivery as per your convience. [any enquires before 20th Nov and order after 20th Nov discount scheme will not be applicable].</li>
		<li>Sale discount will  be calculated as per the MRP Price list.</li>
		<li>Cannot be clubbed with other offer.</li>
		<li>No return, no exchange allowed, discount rates against payment only.</li>
		<li>Sales tax extra as per the state government law. Packing & Forwarding extra.</li>
		<li>Company holds the right to alter, change or withdraw the scheme without prior notice.</li> 
	</ul>
</form>
</body>
</html>