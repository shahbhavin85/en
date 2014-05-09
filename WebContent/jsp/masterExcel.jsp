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
function fnExportPurchaseMaster(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'PURCHASE_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
	//window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=EXPT_MASTER_DATA&txtFromDate='+document.forms[0].txtFromDate.value+"&txtToDate="+document.forms[0].txtToDate.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExportPurchaseItem(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'PURCHASE_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_ITEM_DATA';
	document.forms[0].submit();
}

function fnExportSalesMaster(){
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

function fnExportSalesItem(){
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

function fnExportTransferMaster(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'TRANSFER_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
	//window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=EXPT_MASTER_DATA&txtFromDate='+document.forms[0].txtFromDate.value+"&txtToDate="+document.forms[0].txtToDate.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExportTransferItem(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'TRANSFER_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_ITEM_DATA';
	document.forms[0].submit();
}

function fnExportTransferAppvd(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'TRANSFER_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_APPVD_DATA';
	document.forms[0].submit();
}

function fnExportDayBook(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
}

function fnExportLabourMaster(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'LABOUR_BILL_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
}

function fnExportSalesReturnMaster(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'CREDIT_NOTE_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
}

function fnExportSalesReturnItem(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'CREDIT_NOTE_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_ITEM_DATA';
	document.forms[0].submit();
}

function fnExportPurchaseReturnMaster(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'PURCHASE_RETURN_HANDLER';
	document.forms[0].ACTIONS.value = 'EXPT_MASTER_DATA';
	document.forms[0].submit();
}

function fnExportPurchaseReturnItem(){
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return;
	}
	document.forms[0].HANDLERS.value = 'PURCHASE_RETURN_HANDLER';
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
			<legend class="screenHeader">Export Excel</legend>
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
						<input type="button" value="Export Sales Master" style="width: 200px;" onclick="fnExportSalesMaster();"/>&nbsp;&nbsp;&nbsp;<input type="button" style="width: 200px;" value="Export Sales Item" onclick="fnExportSalesItem();"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Labour Master" style="width: 200px;" onclick="fnExportLabourMaster();"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Sales Return Master" style="width: 200px;" onclick="fnExportSalesReturnMaster();"/>&nbsp;&nbsp;&nbsp;<input type="button" style="width: 200px;" value="Export Sales Return Item" onclick="fnExportSalesReturnItem();"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Transfer Master" style="width: 200px;" onclick="fnExportTransferMaster();"/>&nbsp;&nbsp;&nbsp;<input type="button" style="width: 200px;" value="Export Transfer Item" onclick="fnExportTransferItem();"/>&nbsp;&nbsp;&nbsp;<input type="button" style="width: 200px;" value="Export Transfer Approved" onclick="fnExportTransferAppvd();"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Purchase Master" style="width: 200px;" onclick="fnExportPurchaseMaster();"/>&nbsp;&nbsp;&nbsp;<input type="button" style="width: 200px;" value="Export Purchase Item" onclick="fnExportPurchaseItem();"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Purchase Return Master" style="width: 200px;" onclick="fnExportPurchaseReturnMaster();"/>&nbsp;&nbsp;&nbsp;<input type="button" style="width: 200px;" value="Export Purchase Return Item" onclick="fnExportPurchaseReturnItem();"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<input type="button" value="Export Day Book Data" style="width: 200px;" onclick="fnExportDayBook();"/>
					</td>
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