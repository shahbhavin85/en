<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.CustomerEmailModel"%>
<%@page import="com.en.util.Utils"%>
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnGetCustomerDetails(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_EMAIL_CUSTOMER';
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
    		t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" )'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnSelect(id, txt){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_EMAIL_CUSTOMER';
	fnFinalSubmit();	
}

function fnGetEmailDtls(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'VIEW_EMAIL_CUSTOMER';
	fnFinalSubmit();	
}


function fnClearCustomer(){
	document.forms[0].txtCust.value = "";
	document.getElementById("img").style.visibility = "hidden";
	document.forms[0].sCustomer.value = "";
	for(var i=1; i<10;i++)
		document.getElementById(i).style.display = 'none';
	document.forms[0].txtCust.readOnly = false;
	document.forms[0].txtCust.focus();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer"> 
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View Email Customer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">E-Mail Ref No. :</td>
					<td><input type="text" name="txtRefNo" />&nbsp;&nbsp;<input type="button" value="Get Details" onclick="fnGetEmailDtls();"/> </td>
				</tr>
				<% if(request.getAttribute(Constant.FORM_DATA) != null){ %>
				<tr>
					<td align="right">E-mail No :</td>
					<td>
						<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getEmailNo() : ""%>
					</td>
				</tr>
				<tr>
					<td align="right">E-mail Date :</td>
					<td>
						<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDateDDMMYY(((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getEmailDate()) : ""%>
					</td>
				</tr>
				<tr>
					<td align="right">Customer :</td>
					<td>
						<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerName()+" - "+((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getArea()+" - "+((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCity() : ""%>
					</td>
				</tr>
				<tr id=1>
					<td align="right">Phone :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getStdcode()+" - "+((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getPhone1() +" / "+((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getPhone2() : ""%></td>
				</tr>
				<tr id=2>
					<td align="right">Mobile 1 :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getMobile1() : ""%></td>
				</tr>
				<tr id=3>
					<td align="right">Mobile 2 :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getMobile2() : ""%></td>
				</tr>
				<tr id=4>
					<td align="right">Email 1 :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getEmail() : ""%></td>
				</tr>
				<tr id=5>
					<td align="right">Email 2 :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getEmail1() : ""%></td>
				</tr>
				<tr id=6>
					<td align="right">Subject :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getSubject() : ""%></td>
				</tr>
				<tr id=7>
					<td align="right">Message :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getMessage() : ""%></td>
				</tr>
				<tr id=8>
					<td align="right">Attachment : </td>
					<td><%if(request.getAttribute(Constant.FORM_DATA) != null){ %><a href="javascript: window.open('/CUSTOMER_MAIL_FILES/<%=((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getFilename()%>');"><%=((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getFilename()%></a><%} %></td>
				</tr>
				<tr id=9>
					<td colspan="2" align="center"><input type="button" value="Email" onclick="fnSendMail(this, <%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getEmailNo() : ""%>);"/></td>
				</tr>
				<%} %>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerEmailModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId() : ""%>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">


function fnSendMail(btn, id){
	btn.disabled = true;
	document.forms[0].txtRefNo.value = id;
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'RESEND_EMAIL_CUSTOMER';
	fnFinalSubmit();
}
</script>
</body>
</html>