<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
function fnExportMasterDtls(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'SALES_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
	//window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=EXPT_MASTER_DATA&txtFromDate='+document.forms[0].txtFromDate.value+"&txtToDate="+document.forms[0].txtToDate.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExportItemDtls(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'SALES_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_ITEM_DATA';
	document.forms[0].submit();
}

</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Export Sales Data</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
					<td align="right">To Date<span style="color: red">*</span> :</td>
					<td><input name="txtToDate" id="txtToDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Master Details" onclick="fnExportMasterDtls();"/>&nbsp;&nbsp;&nbsp;<input type="button" value="Export Item Details" onclick="fnExportItemDtls();"/>
					</td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>