<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.SalesModel"%>
<%@page import="com.en.util.DateUtil"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript" src="js/transferRpt.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Transfer Master Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right" valign="top">From Branch<span style="color: red">*</span> :</td>
					<td>
					<%
						String[] accessSelected = (request.getAttribute("cFromBranch") != null) ? ((String[])request.getAttribute("cFromBranch")) : new String[] {"--"};
					%>
						<input type="checkbox" onchange="fnChangeFromBranch(this);" style="margin-left : 7px;" name="cFromBranch" <%=(accessSelected.length > 0 && accessSelected[0].equals("--")) ? "checked=\"checked\"" : ""%> id="cFromBranch0" value="--"/><label for="cFromBranch0">ALL</label>
						<br>
						<table width="100%">
						<%
							AccessPointModel[] accessPoints = request.getAttribute(Constant.ACCESS_POINTS) != null ?
													(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
							ArrayList selected = new ArrayList(0);
							if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
								for(int k=0; k<accessSelected.length;k++){
									selected.add((String)accessSelected[k]);	
								}
							}
							for(int i=0; i<accessPoints.length;i++){
								if(i%3==0){
									if(i!=0){
						%>
							</tr>
						<%
									}
						%>
							<tr>
						<%
								}
						%>
						<td>
						<input type="checkbox" onchange="fnChangeFromBranch(this);" name="cFromBranch" <%=(accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf(accessPoints[i].getAccessId()+"")!=-1)) ? "checked=\"checked\"" : ""%> id="cFromBranch<%=i+1%>" value="<%=accessPoints[i].getAccessId()%>"/><label for="cFromBranch<%=i+1%>"><%=accessPoints[i].getAccessName()%></label>
						</td>
						<%
							}
						%>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">To Branch<span style="color: red">*</span> :</td>
					<td>
					<%
						accessSelected = (request.getAttribute("cToBranch") != null) ? ((String[])request.getAttribute("cToBranch")) : new String[] {"--"};
					%>
						<input type="checkbox" onchange="fnChangeToBranch(this);" style="margin-left : 7px;" name="cToBranch" <%=(accessSelected.length > 0 && accessSelected[0].equals("--")) ? "checked=\"checked\"" : ""%> id="cToBranch0" value="--"/><label for="cToBranch0">ALL</label>
						<br>
						<table width="100%">
						<%
							selected = new ArrayList(0);
							if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
								for(int k=0; k<accessSelected.length;k++){
									selected.add((String)accessSelected[k]);	
								}
							}
							for(int i=0; i<accessPoints.length;i++){
								if(i%3==0){
									if(i!=0){
						%>
							</tr>
						<%
									}
						%>
							<tr>
						<%
								}
						%>
						<td>
						<input type="checkbox" onchange="fnChangeToBranch(this);" name="cToBranch" <%=(accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf(accessPoints[i].getAccessId()+"")!=-1)) ? "checked=\"checked\"" : ""%> id="cToBranch<%=i+1%>" value="<%=accessPoints[i].getAccessId()%>"/><label for="cToBranch<%=i+1%>"><%=accessPoints[i].getAccessName()%></label>
						</td>
						<%
							}
						%>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right">Transfer From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         Transfer To Date<span style="color: red">*</span> :
					<input name="txtToDate" id="txtToDate" style="width: 195px;" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Details" onclick="fnGetMasterRpt();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>