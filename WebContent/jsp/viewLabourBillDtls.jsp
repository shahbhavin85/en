<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.LabourBillModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.RequestUtil"%>
<%@page import="com.en.model.CustomerGroupModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">
function printSalesBill(billNo,copyType){
	window.open('/en/app?HANDLERS=LABOUR_BILL_HANDLER&ACTIONS=PRNT_LABOUR_BILL&billNo='+billNo+'&copies='+copyType,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Labour Invoice Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" border=1 style="width: 950px;" align="center" cellspacing="0">
				<tr>
					<td style="padding-left: 20px; width: 150px;" align="right">Bill No</td>
					<td><b><%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"LB"+((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSaleid() %></b></td>
				</tr>
				<tr>
					<td align="right">Date</td>
					<td><b><%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSalesdate()%></b></td>
				</tr>
				<tr>
					<td style="padding-left: 20px;" valign="top" align="right">Customer</td>
					<td><b><%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getLabel()%></b></td>
				</tr>
				<tr>
					<td align="right" valign="top">Salesman</td>
					<td valign="top"><b><%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSalesman().getUserName()%></b></td>
				</tr>
				<tr>
					<td align="right">Remarks</td>
					<td><b><%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getRemarks() %></b></td>
				</tr>
				<tr>
					<td align="right">Amount</td>
					<td><b><%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getTotalAmt() %></b></td>
				</tr>
			</table>
			<table style="width: 950px;" align="center">
				<tr>
					<td align="center"><input type="button" value="Close" onclick="window.close();" />
					&nbsp;<input type="button" value="Print All" onclick="printSalesBill('<%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"LB"+((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSaleid()%>',0)" />
					&nbsp;<input type="button" value="Original Copy" onclick="printSalesBill('<%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"LB"+((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSaleid()%>',1)" />
					&nbsp;<input type="button" value="Office Copy" onclick="printSalesBill('<%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"LB"+((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSaleid()%>',2)" />
					&nbsp;<input type="button" value="Email" onclick="editSalesBill('<%=((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getBillPrefix()+"LB"+((LabourBillModel)request.getAttribute(Constant.FORM_DATA)).getSaleid()%>')" /></td> 
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCopy"/>
	<input type="hidden" name="txtInvoiceNo"/>
</form>

</body>
</html>