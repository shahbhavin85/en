<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
	<script type="text/javascript" src="js/common.js"></script>
	<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">User Home</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<%
				if(Integer.parseInt((String)request.getSession().getAttribute(Constant.ACCESS_POINT)) == 3 || Integer.parseInt((String)request.getSession().getAttribute(Constant.ACCESS_POINT)) == 2 || Integer.parseInt((String)request.getSession().getAttribute(Constant.ACCESS_POINT)) >10){
			%>
			<center><label style="font-size: 15px; padding: 2px; color : red; background-color: yellow; font-weight: bold;">TODAY CASH ON HAND : Rs. <%=request.getAttribute(Constant.OPEN_BALANCE)%></label></center><br><br>
			<%
				}
			%>
			<label style="padding-left : 20px;color : blue;">Welcome <%=((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName() %></label><br><br>
			<label style="padding-left : 20px;">Please select required option from the menu.</label><br><br>
			<%
				if(request.getAttribute(Constant.UPDATED_ITEMS) != null && ((ItemModel[]) request.getAttribute(Constant.UPDATED_ITEMS)).length > 0){
					ItemModel[] items = (ItemModel[]) request.getAttribute(Constant.UPDATED_ITEMS);
			%>
			<div style="overflow: hidden; width: 852px; margin-left: 25px; display: block;">
				<table border="1" style="width: 835px; display: block; vertical-align: middle;">
					<tr><th colspan="5">CHANGES IN PRICE LIST - (LATEST VERSION <%=request.getAttribute(Constant.PL_VERSION) %>)</th></tr>
					<tr>
						<th style="width: 120px;">ITEM ID</th>
						<th style="width: 175px;">ITEM NUMBER</th>
						<th style="width: 300px;">ITEM NAME</th>
						<th style="width: 120px;">ITEM PRICE</th>
						<th style="width: 120px;">LAST UPDATED ON</th>
					</tr>
			<%
				if(items.length>12){
			%>
				</table>
				<table border="1" style="height: 250px; margin-top : 2px; overflow: auto; width: 850px; display: block;vertical-align: middle;">
			<%
				}
					long refTime = (new Date().getTime() - (72*60*60*1000)); 
					for(int i =0; i<items.length; i++){
			%>
				<tr align="center" style="<%=((new SimpleDateFormat("yyyy-MM-dd").parse(items[i].getAlterDate())).getTime() > refTime) ? "background-color : yellow;" : ""%>">
					<td style="width: 120px;"><%=items[i].getItemId() %></td>
					<td style="width: 175px;"><%=items[i].getItemNumber() %></td>
					<td style="width: 300px;"><%=items[i].getItemName() %></td>
					<td style="width: 120px;">Rs. <%=Utils.get2Decimal(Double.parseDouble(items[i].getItemPrice())) %></td>
					<td style="width: 120px;"><%=Utils.convertToAppDateDDMMYY(items[i].getAlterDate()) %></td>
				</tr>
			<%
					}
			%>
			</table></div><br/><br/>
			<%
				}
			%>
			<table width="100%">
			<tr>
			<td width="33%">
			<fieldset>
				<legend style="font-size: 15px; font-weight: bold; color: blue; font-style: italic;">Today's Thought</legend>
				<br><textarea style="width: 100%; height: 80px; background: transparent; border-color: transparent; color: green; font-style: italic;"><%=(request.getAttribute(Constant.THOUGHT) != null ? (String)request.getAttribute(Constant.THOUGHT) : "") %></textarea><br>
			</fieldset>
			</td>
			<td width="33%">
			<fieldset>
				<legend style="font-size: 15px; font-weight: bold; color: blue; font-style: italic;">Today's Good News</legend>
				<br><textarea style="width: 100%; height: 80px; background: transparent; border-color: transparent; color: green; font-style: italic;"><%=(request.getAttribute(Constant.GOOD_NEWS) != null ? (String)request.getAttribute(Constant.GOOD_NEWS) : "") %></textarea><br>
			</fieldset>
			</td>
			<td width="33%">
			<fieldset>
				<legend style="font-size: 15px; font-weight: bold; color: blue; font-style: italic;">Jackpot</legend>
				<br><textarea style="width: 100%; height: 80px; background: transparent; border-color: transparent; color: green; font-style: italic;"><%=(request.getAttribute(Constant.JACKPOT) != null ? (String)request.getAttribute(Constant.JACKPOT) : "") %></textarea><br>
			</fieldset>
			</td>
			</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>