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
	<script type="text/javascript" src="js/salesmanApprovalDlyRpt.js"></script>
	<link rel="stylesheet" type="text/css" href="css/common.css" />
	</head>
	<body>
	<jsp:include page="menu/menu.jsp"></jsp:include>
	<form method="post" name="frmViewEnquiry">
		<div class="formClass">
			<fieldset>
				<legend class="screenHeader">View Salesman Daily Report</legend>
				<jsp:include page="messages.jsp"></jsp:include>
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
					<td align="left">&nbsp;</td>
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
					<tr>
						<td align="right">Remarks</td>
						<td colspan="9"><textarea name="taRemark" rows="5" cols="100"><%=((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRemark() %></textarea> </td>
					</tr>
					<tr>
						<th colspan="10">Attendance : 
							<select  name= "att">
								<option value = "--">-----</option>
								<option value = 0>Full Day</option>
								<option value = 1>Half Day</option>
								<option value = 2>Leave</option>
								<option value = 3>Holiday</option>
							</select>&nbsp;&nbsp;In Time : 
							<input  type="text" maxlength="10" style="width: 60px;" name="inTime" value=""/>
						&nbsp;&nbsp;Out Time : 
							<input  type="text" maxlength="10" style="width: 60px;" name="outTime" value=""/>
						&nbsp;&nbsp;Late Fine : 
							<input  type="text" maxlength="10" style="width: 60px;" name="lateFine" value=""/>
						&nbsp;&nbsp;Penalty : 
							<input  type="text" maxlength="10" style="width: 60px;" name="panelty" value=""/>
						&nbsp;&nbsp;Penalty Remarks : 
							<textarea  name="remark"></textarea>
						</th>
					</tr>
					<tr>
						<td colspan="10" align="center"><input style="width: 75px;" type="button" value="Approve" onclick="fnApprove();"/>&nbsp;&nbsp;&nbsp;
													   <input style="width: 75px;" type="button" value="Reject" onclick="fnReject();"/>&nbsp;&nbsp;&nbsp;
													   <input style="width: 75px;" type="button" value="Print" onclick="fnPrintReport();"/>&nbsp;&nbsp;&nbsp;
													   <input style="width: 75px;" type="button" value="Back" onclick="fnBack();"/></td>
					</tr>
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtUserType" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getUser().getType() : ""%>">
	<input type="hidden" name="txtUserId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getUser().getUserId() : ""%>">
	<input type="hidden" name="txtRptDate" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptDt() : ""%>">
	<input type="hidden" name="txtRptId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptId() : ""%>">
	</form>
	</body>
	<script type="text/javascript" src="js/dateUtil.js"></script>
	<script type="text/javascript">
	if(document.getElementById('lblExpense'))
		document.getElementById('lblExpense').innerHTML = "<%=Utils.get2Decimal(expense)%>";
	</script>
	</html>