<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesmanDlyItmModel"%>
<%@page import="com.en.model.SalesmanDlyRptModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.OfferModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/viewDlyRpt.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View Salesman Daily Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">User Name <span style="color: red">*</span> :</td>
					<td>
					<%
						String userId = "";
					%>
						<select  name="txtUserId" style="width: 200px;">
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"  <%=((request.getAttribute(Constant.FORM_DATA) != null && 
									(((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getUser().getUserId().equals(users[i].getUserId())))) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Report Date<span style="color: red">*</span> :</td>
					<td><input name="txtRptDate" id="txtRptDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtRptDate'),event);" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptDt()) : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtRptDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Report" onclick="fnGetReport();"/></td>
				</tr>
			</table>
			<%
				double expense = 0;
				if(request.getAttribute(Constant.FORM_DATA) != null){
			%>
			<table width="100%" style="font-family: calibri;">
				<tr>
					<td align="left"><label style="font-size: 16px; font-weight: bold;"><u>Daily Report</u></label></td>
					<td align="right"><label style="font-size: 16px; font-weight: bold;"><u>Status</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getStatus() : ""%></label></td>
				</tr>
				<tr>
					<td align="left"><label style="font-size: 16px; font-weight: bold;"><u>User Id</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getUser().getUserName() : ""%></label></td>
					<td align="right"><label style="font-size: 16px; font-weight: bold;"><u>Report Date</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptDt()) : ""%></label></td>
				</tr>
				<tr>
					<td align="left"><label style="font-size: 16px; font-weight: bold;"><u>Submit Date/Time</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getSentDt() : ""%></label></td>
					<td align="right"><label style="font-size: 16px; font-weight: bold;"><u>Approved By</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null && !((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getApprover().getUserName().equals("")) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getApprover().getUserName() : "--"%></label></td>
				</tr>
				<tr style="background-color: yellow;">
					<td align="center" colspan="2"><label style="font-size: 13px; font-weight: bold;">Order : </label><label style="font-size: 16px; font-weight: bold; color: red;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getOrders() : "--" %></label>&nbsp;&nbsp;&nbsp;&nbsp;||&nbsp;&nbsp;&nbsp;&nbsp; 
						<label style="font-size: 13px; font-weight: bold;">Quotation : </label><label style="font-size: 16px; font-weight: bold; color: red;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getQuotation() : "--" %></label>&nbsp;&nbsp;&nbsp;&nbsp;||&nbsp;&nbsp;&nbsp;&nbsp; 
						<label style="font-size: 13px; font-weight: bold;">Expense : </label><label style="font-size: 16px; font-weight: bold; color: red;" id="lblExpense"></label></td>
				</tr>
			</table>
			<table width="100%" cellspacing="0" border="1" style="margin-top: 10px;">
				<tr>
					<th style="width: 80px;">IN TIME</th>
					<th style="width: 80px;">OUT TIME</th>
					<th width="30%">DESCRIPTION</th>
					<th style="width: 80px;">LODGING</th>
					<th style="width: 80px;">FOOD</th>
					<th style="width: 80px;">RAIL/BUS</th>
					<th style="width: 80px;">LOCAL TRANSPORT</th>
					<th style="width: 80px;">COURIER</th>
					<th style="width: 80px;">OTHERS</th>
					<th width="20%">REMARKS</th>
				</tr>
				<%
					if(request.getAttribute(Constant.FORM_DATA) != null){
						SalesmanDlyItmModel[] items = (SalesmanDlyItmModel[])((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getDtls().toArray(new SalesmanDlyItmModel[0]);
						for(int i=0; i<items.length; i++){
				%>
					<tr style="height: 30px;">
						<td align="center"><%=items[i].getAppInTime()%></td>
						<td align="center"><%=items[i].getAppOutTime()%></td>
				<%
							if(items[i].getType().equals("0")){
				%>
						<td>Meeting with <%=items[i].getCustomer().getCustomerName()+" - "+items[i].getCustomer().getArea()+" - "+items[i].getCustomer().getCity() %></td>
				<%
							} else {
				%>
						<td>Others</td>
				<%
							}
				%>
						<td align="center"><%=Utils.get2Decimal(items[i].getLodging()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getFood()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getRailbus()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getLocaltransport()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getCourier()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getOthers()) %></td>
						<td><%=(items[i].getRemark() != null) ? items[i].getRemark() : "--" %></td>
					</tr>
				<%
							expense = expense + items[i].getLodging() + items[i].getFood() + items[i].getRailbus() + items[i].getLocaltransport() + items[i].getCourier() + items[i].getOthers();
						}
					}
				%>
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
if(document.getElementById('lblExpense'))
	document.getElementById('lblExpense').innerHTML = "<%=Utils.get2Decimal(expense)%>";
</script>
</html>