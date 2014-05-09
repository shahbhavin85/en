<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
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
<script type="text/javascript">
function fnConfirmSalesDtls(){
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "CNFM_EDIT_SALES";
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
			<legend class="screenHeader">Confirm Edit Sales Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Invoice No<span style="color: red">*</span> :</td>
					<td><input name="txtInvoiceNo" readonly="readonly" style="width: 195px;" value="<%=((SalesModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+Utils.padBillNo(((SalesModel)request.getAttribute(Constant.FORM_DATA)).getSaleid())%>"></td>
				</tr>
				<tr>
					<td align="right">Edit Password<span style="color: red">*</span> :</td>
					<td><input name="txtPassword" maxlength="10" style="width: 195px;"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Validate Password" onclick="fnConfirmSalesDtls();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>