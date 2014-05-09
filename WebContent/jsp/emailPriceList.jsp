<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddItemCat">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">E-Mail Price List</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td align="right">Email 1 :</td>
					<td><input maxlength="50" name="txtEmail" value="" style="width: 500px;"></td>
				</tr>
				<tr>
					<td align="right">Email 2 :</td>
					<td><input maxlength="50" name="txtEmail1" value="" style="width: 500px;"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" id="btnMail" disabled="disabled" value="Send Mail" onclick="fnSendMail();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/> 
</form>
<script type="text/javascript">
function fnSendMail(){
	document.forms[0].HANDLERS.value = 'ITM_HANDLER';
	document.forms[0].ACTIONS.value = 'EMAIL_PRICE_LIST_1';
	fnFinalSubmit();
}

function fnGetCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForCustList);
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
    		t = t+"<li onclick='fnSelect(\""+retObj.list[i].email+"\",\""+retObj.list[i].email1+"\",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\","+retObj.list[i].custId+")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnClearCustomer(){
	document.getElementById("btnMail").disabled = true;
	document.forms[0].txtCust.value = "";
	document.forms[0].txtEmail.value = "";
	document.forms[0].txtEmail1.value = "";
	document.getElementById("img").style.visibility = "hidden";
	document.forms[0].sCustomer.value = "";
	document.forms[0].txtCust.readOnly = false;
	document.forms[0].txtCust.focus();
}

function fnChangeBtnStatus(itemCombo){
	if(document.forms[0].sCustomer.value != ""){
		document.getElementById("btnMail").disabled = false;
	} else {
		document.getElementById("btnMail").disabled = true;
	}
}

function fnSelect(e1,e2, txt, id){
	document.getElementById("btnMail").disabled = false;
	document.forms[0].txtCust.value = txt;
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtEmail.value = e1;
	document.forms[0].txtEmail1.value = e2;
	document.getElementById("list").style.display = 'none';
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
}
</script>
</body>
</html>