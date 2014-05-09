<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.model.AccessPointModel"%>
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
<script type="text/javascript">
function fnDeposit(id, entryid){
	document.forms[0].id.value = id;
	document.forms[0].entryId.value = entryid;
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'DEPOSIT_CHQ';
	fnFinalSubmit();
}
function fnGetReport(){
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_RPT_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_CHQ_ENTRIES_RPT';
	fnFinalSubmit();
}

function fnChangeBranch(branch){
	var iLen = document.forms[0].cBranch.length;
	if(branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cBranch[i].checked = true;
		}
	} else if(!branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cBranch[i].checked = false;
		}
	} else if(!branch.checked){
		document.forms[0].cBranch[0].checked = false;
	} else if(branch.value != 0){
		var isAllChecked = true;
		for(var i=1; i<iLen; i++){
			if(!document.forms[0].cBranch[i].checked){
				isAllChecked = false;
			}
		}
		document.forms[0].cBranch[0].checked = isAllChecked;
	}
}

function getCheckBoxValues(cb){
	var val = "";
	var iL = cb.length;
	for(var k=0; k<iL; k++){
		if(cb[k].checked){
			val = val + cb[k].value+"|";
		}
	}
	return val;
}

function fnPrint(){
	var params = "";
	params = "&sCustomer="+document.forms[0].sCustomer.value;
	params = params+"&cBranch="+getCheckBoxValues(document.forms[0].cBranch);
	params = params+"&txtFromDate="+document.forms[0].txtFromDate.value;
	params = params+"&txtToDate="+document.forms[0].txtToDate.value;
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_RPT_HANDLER&ACTIONS=PRNT_CHQ_ENTRIES_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExport(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_RPT_HANDLER";
	document.forms[0].ACTIONS.value = "EXPT_CHQ_ENTRIES_RPT";
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
			<legend class="screenHeader">Undeposited Cheque List</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table width="100%" border="1"  style="font-family: calibri; font-size: 14px;">
				<tr>
					<th style="width: 100px; ">S.No</th>
					<th style="width: 120px; ">Date</th>
					<th style="width: 200px; ">Branch</th>
					<th style=" ">Desciption</th>
					<th style="width: 120px; ">Amount</th>
					<th style="width: 120px; ">Action</th>
				</tr>
			<%
				double chqTotal = 0;
				EntryModel[] chequeEntries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])request.getAttribute(Constant.FORM_DATA)) : new EntryModel[0];;
				if(chequeEntries.length > 0){
					for(int i=0; i<chequeEntries.length; i++){
						chqTotal = chqTotal+ chequeEntries[i].getAmount();
						
			%>
				<tr>
					<td  align="center" valign="top"><%=i+1%></td>
					<td  align="center" valign="top"><%=Utils.convertToAppDateDDMMYY((chequeEntries[i]).getEntryDate())%></td>
					<td  align="center" valign="top"><%=(chequeEntries[i]).getBranch().getAccessName() +" - "+(chequeEntries[i]).getBranch().getCity()%></td>
					<td ><%=(chequeEntries[i]).getDesc()%></td>
					<td  align="right" valign="top"><%=Utils.get2Decimal((chequeEntries[i]).getAmount())%></td>
					<td  align="center" valign="top"><input type="button" value="Deposit" onclick="fnDeposit(<%=chequeEntries[i].getId()%>,<%=chequeEntries[i].getEntryId()%>);"/></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th colspan="4" align="right" style=" ">Total</th>
					<th align="right" style=" "><%=Utils.get2Decimal(chqTotal)%></th>
					<th align="right" style=" ">&nbsp;</th>
				</tr>
			<%
				} else {
			%>
				<tr><td colspan="6" align="center">No Cheques to be deposited</td></tr>
			<%
				}
			%>
			</table>
			<!--table width="100%"><tr><td align="center"><input type="button" value="Print" onclick="fnPrint();"/><input type="button" value="Export" onclick="fnExport();"/></td></tr></table-->
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="entryId" />
	<input type="hidden" name="id" />
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>