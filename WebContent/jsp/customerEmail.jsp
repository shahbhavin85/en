<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
			<legend class="screenHeader">Email Customer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerName()+" - "+((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCity()	 : ""%>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
<!-- 						<select name="sCustomer" onchange="fnGetCustomerDetails();"> -->
<%-- 							<% --%>
<!-- 								if(request.getAttribute(Constant.FORM_DATA) == null){ -->
<!-- 							%> -->
<!-- 							<option value="--">-------</option> -->
<%-- 							<% --%>
<!-- 								} -->
								
<!-- 								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ? -->
<!-- 														(CustomerModel[]) request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0]; -->
<!-- 								for(int i=0; i<customers.length;i++){ -->
<!-- 							%> -->
<%-- 							<option value="<%=customers[i].getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null &&  --%>
<%-- 									((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerId() == customers[i].getCustomerId()) ? "selected=\"selected\"" : "" %>><%=customers[i].getCustomerName()%> - <%=customers[i].getArea()%> - <%=customers[i].getCity()%></option> --%>
<%-- 							<% --%>
<!-- 								} -->
<!-- 							%> -->
<!-- 						</select> -->
					</td>
				</tr>
				<% if(request.getAttribute(Constant.FORM_DATA) != null){ %>
				<tr id=1>
					<td align="right">Phone :</td>
					<td><input type="text" style="width: 500px;" disabled="disabled" value='<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getStdcode()+" - "+((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPhone1() +" / "+((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPhone2() : ""%>' /></td>
				</tr>
				<tr id=2>
					<td align="right">Mobile 1 :</td>
					<td><input type="text" style="width: 500px;" name="txtMobile1" value='<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getMobile1() : ""%>' /></td>
				</tr>
				<tr id=3>
					<td align="right">Mobile 2 :</td>
					<td><input type="text" style="width: 500px;" name="txtMobile2" value='<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getMobile2() : ""%>' /></td>
				</tr>
				<tr id=4>
					<td align="right">Email 1 :</td>
					<td><input type="text" style="width: 500px;" name="txtEmail1" value='<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail() : ""%>' /></td>
				</tr>
				<tr id=5>
					<td align="right">Email 2 :</td>
					<td><input type="text" style="width: 500px;" name="txtEmail2" value='<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail1() : ""%>' /></td>
				</tr>
				<tr id=6>
					<td align="right">Subject :</td>
					<td><input type="text" maxlength="100" style="width: 500px;" name="txtSubject" /></td>
				</tr>
				<tr id=7>
					<td align="right">Message :</td>
					<td><textarea rows="6" maxlenght=500 cols="96" name="taMessage"></textarea> </td>
				</tr>
				<tr id=8>
					<td align="right">Attachment : </td>
					<td><input type="file" id="txtFile" name="txtFile" value="" size="100"/></td>
				</tr>
				<tr id=9>
					<td colspan="2" align="center"><input type="button" value="Email" onclick="fnSendMail();"/></td>
				</tr>
				<%} %>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerId() : ""%>">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">


function fnSendMail(){
	if(document.forms[0].txtSubject.value == ""){
		alert("Please enter a subject");
		return;
	}
	if(document.forms[0].taMessage.value == ""){
		alert("Please enter a Message");
		return;
	}
	document.forms[0].enctype="multipart/form-data";
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'EMAIL_CUSTOMER';
	fnFinalSubmit();
}
</script>
</body>
</html>