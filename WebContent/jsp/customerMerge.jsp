<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnGetCustomerList(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_CUSTOMER_LST';
	fnFinalSubmit();
}
function fnRowSelect(cmb, id){
	if(cmb.checked){
		document.getElementById("tr"+id).style["font-weight"] = 'bold';
		document.getElementById("tr"+id).style.color = 'red';
		document.getElementById("tr"+id).style.border = 'red';
		document.getElementById("tr"+id).style.background = 'yellow';
		eval('document.forms[0].name'+id).focus();
	} else { 
		document.getElementById("tr"+id).style["font-weight"] = 'normal';
		document.getElementById("tr"+id).style.color = 'black';
		document.getElementById("tr"+id).style.border = 'black';
		document.getElementById("tr"+id).style.background = 'transparent';
	}
	return;
}
function fnDelete(id, custName){
	var r=confirm("Are you sure you want to delete \""+custName+"\"?");
	if (r==true) {
		document.forms[0].txtCustId.value = id;
		document.forms[0].HANDLERS.value = "CUSTOMER_HANDLER";
		document.forms[0].ACTIONS.value = "DEL_CUSTOMER";
		fnFinalSubmit();
	}
}
function fnMerge(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'MERGE_CUSTOMER_1';
	fnFinalSubmit();
}
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Customer Merge</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">City <span style="color: red">*</span> :</td>
					<td>
						<select name="txtCity" onchange="fnGetCustomerList();" >
							<option value="">-------</option>
							<%
								
								String[] cities = request.getAttribute(Constant.CITIES) != null ?
														(String[]) request.getAttribute(Constant.CITIES) : new String[0];
								for(int i=0; i<cities.length;i++){
							%>
							<option value="<%=cities[i]%>" <%=(request.getAttribute("txtCity") != null && (((String) request.getAttribute("txtCity")).toUpperCase().equals(cities[i].toUpperCase()))) ? "selected = \"selected\"" : "" %>><%=cities[i]%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
			</table>
			<%
				CustomerModel[] customers = (request.getAttribute("custLst") != null) ? (CustomerModel[]) request.getAttribute("custLst") : new CustomerModel[0];
				if(customers.length>0){
			%>
			<table border="1" width="100%" style="font-size: 11px;">
				<tr>
					<td colspan="12" align="center"><input type="button" value="Merge" onclick="fnMerge();"/></td>
				</tr>
				<tr style="background-color: black; color: white;">
					<th>&nbsp;</th>
					<th>Customer Name & Address</th>
					<th>TXN<br/>CNT</th>
					<th>TIN</th>
					<th>CST</th>
					<th>Std Code</th>
					<th>Phone1</th>
					<th>Phone2</th>
					<th>Mobile1</th>
					<th>Mobile2</th>
					<th>Email1<br/>Email2</th>
					<th>Checked</th>
				</tr>
			<%
				for(int i=0; i<customers.length; i++){
			%>
				<tr id="tr<%=customers[i].getCustomerId()%>">
					<td align="center"><input onchange="fnRowSelect(this, <%=customers[i].getCustomerId() %>);" type="checkbox" name="custChk" value="<%=customers[i].getCustomerId()%>"/><br/><%if(customers[i].getTxnCnt()==0){ %><input type="button" value="Delete" onclick="fnDelete(<%=customers[i].getCustomerId()%>,'<%=customers[i].getCustomerName()+"-"+customers[i].getCity()%>');"/><%} %> </td>
					<td><%=customers[i].getLabel()%></td>
					<td align="center" style="font-size: 14px; font-weight: bold;"><%=customers[i].getTxnCnt()%></td>
					<td align="center"><%=customers[i].getTin()%></td>
					<td align="center"><%=customers[i].getCst() %></td>
					<td align="center"><%=customers[i].getStdcode() %></td>
					<td align="center"><%=customers[i].getPhone1() %></td>
					<td align="center"><%=customers[i].getPhone2() %></td>
					<td align="center"><%=customers[i].getMobile1() %></td>
					<td align="center"><%=customers[i].getMobile2() %></td>
					<td align="center"><%=customers[i].getEmail() %><br/><%=customers[i].getEmail1() %></td>
					<td align="center" <%=customers[i].isChecked() ? "style='background-color:aqua;'" : "" %>><%=customers[i].isChecked() ? "YES" : "NO" %></td>
				</tr>
			<%
				}
			%>
				<tr>
					<td colspan="12" align="center"><input type="button" value="Merge" onclick="fnMerge();"/></td>
				</tr>
			</table>
			<%	
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtCustId">
	<input type="hidden" name="isChecked" value="<%=(request.getAttribute("checked") == null) ? "N" : "Y"%>"/>
</form>

</body>
</html>