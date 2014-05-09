<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AlertModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/enquiryRpt.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Enquiry Alert Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Access Point :</td>
					<td colspan="3">
						<select name="sAccessName" style="width: 200px;" <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ? "disabled=\"disabled\"" : ""%>>
							<option value="--">-------</option>
							<%
								AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
								AccessPointModel temp = null;
								for(int i=0; i<accessPoints.length; i++){
									temp = accessPoints[i];
							%>
							<option value="<%=temp.getAccessId()%>" <%=((request.getAttribute(Constant.ACCESS_POINT) != null && 
								Integer.parseInt((String)request.getAttribute(Constant.ACCESS_POINT)) == temp.getAccessId()) || (!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && Integer.parseInt((String)request.getSession().getAttribute(Constant.ACCESS_POINT)) == temp.getAccessId())) ? "selected=\"selected\"" : "" %>><%=temp.getFullAccess()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=request.getAttribute("fromDate")!= null ? request.getAttribute("fromDate") : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
					</td>
					<td align="right">To Date<span style="color: red">*</span> :</td>
					<td><input name="txtToDate" id="txtToDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=request.getAttribute("toDate")!= null ? request.getAttribute("toDate") : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" value="Get Report" onclick="fnGetReport();"/></td>
				</tr>
			</table>
			<%
				if(request.getAttribute("reportList") != null){
			%>
			<table width="100%" border = 1 cellpadding="2">
				<tr>
					<th width="10%">Enq No</th>
					<th width="25%">Customer</th>
					<th width="10%">Alert Date</th>
					<th width="10%">Current Point</th>
					<th width="30%">Remark</th>
					<th>Actions</th>
				</tr>
			<%
					AlertModel[] rptList = (AlertModel[]) request.getAttribute("reportList");
					for(int i=0; i<rptList.length; i++){
			%>
				<tr>
					<td align="center"><a href="javascript:fnOpenEnquiryPopup(<%=rptList[i].getEnqNo()%>);"><%=rptList[i].getEnqNo()%></a></td>
					<td align="center"><%=rptList[i].getCustomer().getFullCustomerName()%></td>
					<td align="center"><%=Utils.convertToAppDate(rptList[i].getAppDt())%></td>
					<td align="center"><%=rptList[i].getCurrentpoint().getFullAccess()%></td>
					<td align="left"><%=rptList[i].getRemarks()%></td>
					<td align="center"><input type="button" onclick="fnGetEnquiryDetails(<%=rptList[i].getEnqNo()%>);" value="View and take actions"/></td>
				</tr>
			<%
					} 
					if(rptList.length > 0){
			%>
				<tr><td colspan="6" align="center"><input type="button" value="Print Report" onclick="fnPrintReport();"/></td></tr>
			<%
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
	<input name="txtEnqNo" type="hidden"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>