	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@page import="java.util.HashMap"%>
<%@page import="com.en.model.DayBookEntryModel"%>
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
	<link rel="stylesheet" type="text/css" href="css/common.css" />
	<script type="text/javascript">
	function fnViewReport(id){
		document.forms[0].txtId.value = id;
		document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
		document.forms[0].ACTIONS.value = 'SHOW_APPROVE_DAY_ENTRIES';
		fnFinalSubmit();		
	}
	</script>
	</head>
	<body>
	<jsp:include page="menu/menu.jsp"></jsp:include>
	<form method="post" name="frmViewEnquiry">
		<div class="formClass">
			<fieldset>
				<legend class="screenHeader">Day Book Approval Report</legend>
				<jsp:include page="messages.jsp"></jsp:include>
			<%
				if(request.getAttribute(Constant.FORM_DATA) != null){
					if(request.getAttribute(Constant.FORM_DATA) != null && ((HashMap<String, DayBookEntryModel[]>)(request.getAttribute(Constant.FORM_DATA))).keySet().size() > 0){
						String key = "";
						Iterator<String> keys = ((HashMap<String, DayBookEntryModel[]>)(request.getAttribute(Constant.FORM_DATA))).keySet().iterator();
						while(keys.hasNext()){
							key = keys.next();
							DayBookEntryModel[] items = (DayBookEntryModel[])((HashMap<String, DayBookEntryModel[]>)(request.getAttribute(Constant.FORM_DATA))).get(key);
				%>
					<table width="100%" border="1" style="margin-top: 10px;">
						<tr><th colspan=4><%=key%></th></tr>
						<tr>
							<th style="width: 80px;">S. NO.</th>
							<th style="width: 100px;">DATE</th>
							<th>BRANCH</th>
							<th style="width: 200px;">ACTION</th>
						</tr>
			<%				
							for(int i=0; i<items.length; i++){
				%>
					<tr>
						<td align="center"><%=(i+1)%></td>
						<td align="center"><%=Utils.convertToAppDate(items[i].getEntryDate()) %></td>
						<td align="center"><%=items[i].getBranch().getAccessName()%></td>
						<td align="center"><input type="button" value="Click to View and Approve" onclick="fnViewReport('<%=items[i].getId()%>');"/></td>
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
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtId" value="">
	</form>
	</body>
	<script type="text/javascript" src="js/dateUtil.js"></script>
	</html>