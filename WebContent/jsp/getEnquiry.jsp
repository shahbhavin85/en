<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.ActionType"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.ActionModel"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.EnquiryModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/enquiry.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/tabber.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Enquiry Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table width="100%" border="1" cellspacing="0" style="font-weight: bold; color: #8A2BE2;">
				<tr>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Enquiry No</td>
					<td width="15%"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getEnquiryNo() : ""%></td>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Customer</td>
					<td width="55%" colspan="3"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerName()+" - "
							+((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getArea()+" - "
							+((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCity(): ""%></td>
				</tr>
				<tr>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Enquiry Date</td>
					<td width="15%"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getEnquiryDt() : ""%></td>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Priority</td>
					<td width="55%" colspan="3">
						<select name="sPriority" style="width: 200px;">
							<option value="2" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("2")) ? "selected=\"selected\"" : ""%>>HIGH</option>
							<option value="1" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("1")) ? "selected=\"selected\"" : ""%>>MEDIUM</option>
							<option value="0" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("0")) ? "selected=\"selected\"" : ""%>>LOW</option>
						</select>
					</td>
				</tr>
				<tr>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Enquiry Status</td>
					<td width="15%" style="font-style: italic;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getStatus() : "Open"%></td>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Reference</td>
					<td width="55%" colspan="5">
						<select name="sReference" style="width: 200px;">
							<option value="0" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("0")) ? "selected=\"selected\"" : ""%>>News Letter</option>
							<option value="1" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("1")) ? "selected=\"selected\"" : ""%>>Salesman Visit</option>
							<option value="2" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("2")) ? "selected=\"selected\"" : ""%>>Recommended</option>
							<option value="3" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("3")) ? "selected=\"selected\"" : ""%>>Website</option>
							<option value="4" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("4")) ? "selected=\"selected\"" : ""%>>Exhibition </option>
							<option value="5" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getPriority().equals("5")) ? "selected=\"selected\"" : ""%>>Others</option>
						</select>
					</td>
				</tr>
				<tr>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Created By</td>
					<td width="15%" style="font-style: italic;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCreatedBy().getUserId() : ""%></td>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Last Updated By</td>
					<td width="15%" style="font-style: italic;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getUpdatedBy().getUserId() : ""%></td>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Current Access Point</td>
					<td width="25%"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoint().getAccessName()+" - "
							+((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoint().getCity(): ""%></td>
				</tr>
				<tr>
					<td width="15%" style="color: yellow; background-color: #A8A8A8;">Remarks</td>
					<td colspan="5"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() : ""%></td>
				</tr>
				<tr>
					<td colspan="6" align="center"><input type="button" value="Print Details" onclick="fnPrintEnquiry();"/>
						<%if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")){%>&nbsp;&nbsp;&nbsp;<input type="button" value="Update Details"/>
						<%} %></td>
				</tr>
			</table><br/>
			<fieldset>
				<legend class="subHeader">List of Item Enquired</legend>
				<table width="100%" align="center" cellspacing="0" border=1 style="font-weight: bold; color: #8A2BE2;">
					<tr>
						<th width="10%" style="color: yellow; background-color: #A8A8A8;">S No.</th>
						<th width="20%" style="color: yellow; background-color: #A8A8A8;">Item No</th>
						<th width="40%" style="color: yellow; background-color: #A8A8A8;">Item Name</th>
						<th width="15%" style="color: yellow; background-color: #A8A8A8;">Offer No</th>
						<th width="15%" style="color: yellow; background-color: #A8A8A8;">Quantity</th>
					</tr>
	<%
					OfferItemModel[] items = request.getAttribute(Constant.FORM_DATA) != null ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getItem() : new OfferItemModel[0];
					for(int i=0; i<items.length; i++){
	%>
					<tr align="center">
						<td><%=(i+1)%></td>
						<td><%=items[i].getItem().getItemNumber() %></td>
						<td><%=items[i].getItem().getItemName() %></td>
						<td><%=items[i].getOfferNo() %></td>
						<td><%=items[i].getQty() %></td>
					</tr>
	<%
					}
	%>
				</table>
			</fieldset>
			<fieldset>
				<legend class="subHeader">Actions Taken</legend> 
				<%if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && 
						(request.getAttribute(Constant.FORM_DATA) != null && !(((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getLastAction() != null && (((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getLastAction().equals("0") ||
								((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getLastAction().equals("7"))))){%>
				<table width="100%"><tr><td align="right"><input type="button" value="Take More Actions" onclick="fnAddNewAction();"/></td></tr></table>
				<%}%>
					<table width="100%" align="center" cellspacing="0" border=1 style="font-weight: bold;">
						<tr>
							<th width="10%" style="color: yellow; background-color: #A8A8A8;">S No.</th>
							<th width="15%" style="color: yellow; background-color: #A8A8A8;">Action Date</th>
							<th width="15%" style="color: yellow; background-color: #A8A8A8;">Action Type</th>
							<th width="45%" style="color: yellow; background-color: #A8A8A8;">Desc</th>
							<th width="15%" style="color: yellow; background-color: #A8A8A8;">Action By</th>
						</tr>
	<%
					ActionModel[] actions = request.getAttribute(Constant.FORM_DATA) != null ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getActions() : new ActionModel[0];
					if(actions.length > 0) {
						for(int i=0; i<actions.length; i++){
	%>
						<tr style="background-color: <%=ActionType.inquiry_actions_bg[Integer.parseInt(actions[i].getActionType())] %>; 
							color: <%=ActionType.inquiry_actions_color[Integer.parseInt(actions[i].getActionType())] %>;">
							<td align="center" valign="top"><%=actions[i].getActionNo() %></td>
							<td align="center" valign="top"><%=Utils.convertToAppDate(actions[i].getActionDt())%></td>
							<td align="center" valign="top"><%=ActionType.inquiry_actions[Integer.parseInt(actions[i].getActionType())] %></td>
							<td><%=actions[i].getDesc() %></td>
							<td align="center" valign="top"><%=actions[i].getAddedBy().getUserId() %></td>
						</tr>
	<%
						}
					} else {
	%>
						<tr align="center"><td colspan="5">No actions taken yet.</td></tr>
	<%
					}
	%>
					</table>
			</fieldset>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtEnqNo" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getEnquiryNo() : ""%>"/>
</form>
</body>
</html>