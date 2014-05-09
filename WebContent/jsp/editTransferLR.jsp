<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.TransferModel"%>
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
function fnGetSalesDtls(){
	var vRegex = /^\d{1,5}$/;
	if(!vRegex.test(document.forms[0].txtInvNo.value)){
		alert('Please provide a valid bill No.');
		return;
	}
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>ST'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "GET_EDIT_TRANSFER_LR_DTLS";
	fnFinalSubmit();
}

function fnCancel(){
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_TRANSFER_LR_DTLS";
	fnFinalSubmit();
}

function fnUpdateLRDtls(){
	document.forms[0].txtInvoiceNo.value = '<%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())%>ST'+document.forms[0].txtInvNo.value;
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_TRANSFER_LR_DTLS";
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
			<legend class="screenHeader">Edit Transfer LR Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Stock Transfer No<span style="color: red">*</span> :</td>
					<td><%=(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getBillPrefix())+"ST"%> - <input name="txtInvNo" <%=(request.getAttribute(Constant.FORM_DATA)!=null) ? "readOnly=\"readOnly\"" : "" %> value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransferid() : "" ) %>" style="width: 195px;" maxlength="5">
					<%if(request.getAttribute(Constant.FORM_DATA)==null){ %>&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetSalesDtls();"/><%}%></td>
				</tr>
				<%if(request.getAttribute(Constant.FORM_DATA)!=null) {%>
				<tr>
					<td align="right">Despatched By :</td>
					<td><input type="text" maxlength="50" name="txtDespatch" value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((TransferModel)request.getAttribute(Constant.FORM_DATA)).getTransport() : "" ) %>"/> </td>
				</tr>
				<tr>
					<td align="right">LR No. :</td>
					<td><input type="text" maxlength="15" name="txtLRNo" value = "<%=((request.getAttribute(Constant.FORM_DATA)!=null) ? ((TransferModel)request.getAttribute(Constant.FORM_DATA)).getLrno() : "" ) %>"/> </td>
				</tr>
				<tr>
					<td align="right">LR Dt. :</td>
					<td><input name="txtLRDt" id="txtLRDt" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtLRDt'),event);" 
					value = "<%=(request.getAttribute(Constant.FORM_DATA)!=null && !((TransferModel)request.getAttribute(Constant.FORM_DATA)).getLrdt().equals("")) ? Utils.convertToAppDate(((TransferModel)request.getAttribute(Constant.FORM_DATA)).getLrdt()) : DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtLRDt'),event);" /> </td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Modify" onclick="fnUpdateLRDtls();"/>&nbsp;&nbsp;<input type="button" value="Cancel" onclick="fnCancel();"/> </td>
				</tr>
				<%} %>
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