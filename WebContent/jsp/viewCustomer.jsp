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
	document.forms[0].ACTIONS.value = 'VIEW_CUSTOMER';
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
	document.forms[0].ACTIONS.value = 'VIEW_CUSTOMER';
	fnFinalSubmit();	
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View Customer</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/>
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
				<tr>
					<td align="right">Customer M/s. <span style="color: red">*</span> :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerName() : ""%></td>
				</tr>
				<tr>
					<td align="right">Select Customer Group :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustGroupId() : ""%></td>
				</tr>
				<tr>
					<td align="right">Contact Person :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getContactPerson() : ""%></td>
				</tr>
				<tr>
					<td align="right">Customer Type<span style="color: red">*</span> :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCustomerType() : ""%></td>
				</tr>
				<tr>
					<td align="right">Address :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getAddress() : ""%></td>
				</tr>
				<tr>
					<td align="right">Area <span style="color: red">*</span> :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getArea() : ""%></td>
				</tr>
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCity() : ""%></td>
				</tr>
				<tr>
					<td align="right">Pin Code :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPincode() : ""%></td>
				</tr>
				<tr>
					<td align="right">Std Code - Phone :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getStdcode() : ""%>-<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPhone1() : ""%>/<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getPhone2() : ""%></td>
				</tr>
				<tr>
					<td align="right">Mobile :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getMobile1() : ""%>/<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getMobile2() : ""%></td>
				</tr>
				<tr>
					<td align="right">Grade<span style="color: red">*</span> :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getGrade() : ""%></td>
				</tr>
				<tr>
					<td align="right">Email :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getEmail() : ""%></td>
				</tr>
				<tr>
					<td align="right">Website :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getWebsite() : ""%></td>
				</tr>
				<tr>
					<td align="right">VAT :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getTin() : ""%></td>
				</tr>
				<tr>
					<td align="right">CST :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getCst() : ""%></td>
				</tr>
				<tr>
					<td align="right">Transport :</td>	
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getTransport() : ""%></td>
				</tr>
				<tr>
					<td align="right">Remarks :</td>
					<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((CustomerModel)request.getAttribute(Constant.FORM_DATA)).getRemark() : ""%></td>
				</tr>
				<%} %>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>