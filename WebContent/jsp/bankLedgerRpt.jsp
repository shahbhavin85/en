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
function fnGetReport(){
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_RPT_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_BANK_LEDGER_RPT';
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
	params = "&sHeshBank="+document.forms[0].sHeshBank.value;
	params = params+"&cBranch="+getCheckBoxValues(document.forms[0].cBranch);
	params = params+"&txtFromDate="+document.forms[0].txtFromDate.value;
	params = params+"&txtToDate="+document.forms[0].txtToDate.value;
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_RPT_HANDLER&ACTIONS=PRNT_BANK_LEDGER_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExport(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_RPT_HANDLER";
	document.forms[0].ACTIONS.value = "EXPT_BANK_LEDGER_RPT";
	document.forms[0].submit();
}



function fnGetCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_SALES_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
//     		if(retObj.list[i].count > 0){
//     			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
//     		} else {
    			t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
//     		}
       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnClearCustomer(){
	document.forms[0].txtCust.value = "";
	document.getElementById("img").style.visibility = "hidden";
	document.forms[0].sCustomer.value = "";
	document.forms[0].txtCust.readOnly = false;
	document.forms[0].txtCust.focus();
}

function fnSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Bank Ledger Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right"> Bank :</td>
					<td colspan="3">
						<select name="sHeshBank"  style="min-width: 200px;">
							<option value = "KOTAK">KOTAK</option>
							<option value = "ICICI">ICICI</option>
							<option value = "SBT">SBT</option>
							<option value = "BOM">BOM</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">Branch<span style="color: red">*</span> :</td>
					<td colspan="3">
					<%
						ArrayList selected = new ArrayList(0);
						String[] accessSelected = (request.getAttribute("cBranch") != null) ? ((String[])request.getAttribute("cBranch")) : new String[0];
						if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
							for(int k=0; k<accessSelected.length;k++){
								selected.add((String)accessSelected[k]);	
							}
						}
						if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
					%>
						<table width="100%">
						<tr>
							<td><input type="checkbox" onchange="fnChangeBranch(this);" name="cBranch" <%=((accessSelected.length > 0 && accessSelected[0].equals("--")) || accessSelected.length == 0) ? "checked=\"checked\"" : ""%> id="cBranch0" value="--"/><label for="cBranch0">ALL</label></td>
							<td><input type="checkbox" onchange="fnChangeBranch(this);" name="cBranch" <%=((accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf("2")!=-1) || accessSelected.length == 0)) ? "checked=\"checked\"" : ""%> id="cBranch100" value="2"/><label for="cBranch100">Accounts</label></td>
							<td><input type="checkbox" onchange="fnChangeBranch(this);" name="cBranch" <%=((accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf("3")!=-1) || accessSelected.length == 0)) ? "checked=\"checked\"" : ""%> id="cBranch101" value="3"/><label for="cBranch101">Exhibitions</label></td>
						</tr>
					<% } %>
						<%
							if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
						%>
						<tr>
							<td>
							<input type="checkbox" name="cBranch" checked="checked" id="cBranch" value="<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId()%>"/><label for="cBranch"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessName()%></label>
							</td>
						</tr>
						<%
							} else {
								AccessPointModel[] accessPoints = request.getAttribute(Constant.ACCESS_POINTS) != null ?
														(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
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
							<input type="checkbox" onchange="fnChangeBranch(this);" name="cBranch" <%=((accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf(accessPoints[i].getAccessId()+"")!=-1) || accessSelected.length == 0)) ? "checked=\"checked\"" : ""%> id="cBranch<%=i+1%>" value="<%=accessPoints[i].getAccessId()%>"/><label for="cBranch<%=i+1%>"><%=accessPoints[i].getAccessName()%></label>
							</td>
							<%
								}
							}
						%>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" /></td>
                    <td align="right">To Date<span style="color: red">*</span> :
					<input name="txtToDate" id="txtToDate" style="width: 195px;" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
					<td colspan="4" align="center"><input type="button" value="Get Report" onclick="fnGetReport();"/></td>
				</tr>
			</table>
			<%
				double chqTotal = 0;
				EntryModel[] chequeEntries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((EntryModel[])request.getAttribute(Constant.FORM_DATA)) : new EntryModel[0];;
				if(chequeEntries.length > 0){
			%>
			<table width="100%" border="1"  style="font-family: calibri; font-size: 14px;">
				<tr>
					<th style="width: 50px;">Date</th>
					<th>Branch</th>
					<th>Entry Type</th>
					<th>Particulars</th>
					<th style="width: 120px;">CR. (DEPOSIT)</th>
					<th style="width: 120px;">DR. (WITHDRAW)</th>
<!-- 					<th style="width: 100px;">Balance</th> -->
				</tr>
				<%
					for(int i=0; i<chequeEntries.length; i++){
						chqTotal = chqTotal+ chequeEntries[i].getAmount();
						
				%>
				<tr>
					<td  align="center" valign="top"><%=Utils.convertToAppDateDDMMYY((chequeEntries[i]).getEntryDate())%></td>
					<td  align="center" valign="top"><%=(chequeEntries[i]).getBranch().getAccessName() +" - "+(chequeEntries[i]).getBranch().getCity()%></td>
					<td  align="center" valign="top"><%=Utils.getEntryType((chequeEntries[i]).getEntryType())%></td>
					<td ><%=(chequeEntries[i]).getDesc()%></td>
					<td  align="right" valign="top"><%=(chequeEntries[i].getCrdr().equals("C")) ? Utils.get2Decimal((chequeEntries[i]).getAmount()) : "--"%></td>
					<td  align="right" valign="top"><%=(chequeEntries[i].getCrdr().equals("D")) ? Utils.get2Decimal((chequeEntries[i]).getAmount()) : "--"%></td>
<%-- 					<td  align="right" valign="top"><%=Utils.get2Decimal((chequeEntries[i]).getAmount())%></td> --%>
				</tr>
				<%
					}
				%>
<!-- 				<tr> -->
<!-- 					<th colspan="4" align="right" style=" ">Total</th> -->
<%-- 					<th align="right" style=" "><%=Utils.get2Decimal(chqTotal)%></th> --%>
<%-- 					<th align="right" style=" "><%=Utils.get2Decimal(chqTotal)%></th> --%>
<%-- 					<th align="right" style=" "><%=Utils.get2Decimal(chqTotal)%></th> --%>
<!-- 				</tr> -->
			</table>
			<table width="100%"><tr><td align="center"><input type="button" value="Print" onclick="fnPrint();"/><input type="button" value="Export" onclick="fnExport();"/></td></tr></table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerId() : ""%>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>