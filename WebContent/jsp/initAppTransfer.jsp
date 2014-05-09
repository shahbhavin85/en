	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@page import="com.en.model.TransferModel"%>
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
	<script type="text/javascript">
	function fnViewReport(transferId){
		document.forms[0].txtInvoiceNo.value = transferId;
		document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
		document.forms[0].ACTIONS.value = "GET_APP_TRANSFER";
		fnFinalSubmit();
	}
	</script>
	<link rel="stylesheet" type="text/css" href="css/common.css" />
	</head>
	<body>
	<jsp:include page="menu/menu.jsp"></jsp:include>
	<form method="post" name="frmViewEnquiry">
		<div class="formClass">
			<fieldset>
				<legend class="screenHeader">Awaiting Approval Transfer Report</legend>
				<jsp:include page="messages.jsp"></jsp:include>
			<%
				if(request.getAttribute(Constant.FORM_DATA) != null){
			%>
			<table width="100%" cellspacing="0" border="1" style="margin-top: 10px;">
				<tr>
					<th style="width: 80px;">S. NO.</th>
					<th>STOCK TRANSFER NO</th>
					<th>TRANSFER DATE</th>
					<th>FROM</th>
					<th>ACTION</th>
				</tr>
				<%
					if(request.getAttribute(Constant.FORM_DATA) != null && ((TransferModel[])(request.getAttribute(Constant.FORM_DATA))).length > 0){
						TransferModel[] items = (TransferModel[])(request.getAttribute(Constant.FORM_DATA));
						for(int i=0; i<items.length; i++){
				%>
					<tr>
						<td align="center"><%=(i+1)%></td>
						<td align="center"><%=items[i].getFromBranch().getBillPrefix()+"ST" + items[i].getTransferid()%></td>
						<td align="center"><%=items[i].getTransferdate() %></td>
						<td align="center"><%=items[i].getFromBranch().getAccessName()+" - "+items[i].getFromBranch().getCity() %></td>
						<td align="center"><input type="button" value="Click to View and Approve" onclick="fnViewReport('<%=items[i].getFromBranch().getBillPrefix()+"ST" + items[i].getTransferid()%>');"/></td>
					</tr>
				<%
						}
					} else {
				%>
					<tr><td colspan="5" align="center">No transfer for approval.</td></tr>
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
	<input type="hidden" name="txtInvoiceNo" value="">
	</form>
	</body>
	</html>