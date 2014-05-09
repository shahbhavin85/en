<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="com.en.model.AccessPointModel"%>
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
function fnGetTaxDetails(){
	document.forms[0].HANDLERS.value = "TAX_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ITEM_TAX";
	fnFinalSubmit();
}

function fnModifyTax(){
	document.forms[0].HANDLERS.value = "TAX_HANDLER";
	document.forms[0].ACTIONS.value = "UPDT_ITEM_TAX";
	fnFinalSubmit();
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddItemCat">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Item Tax Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Item<span style="color: red">*</span> :</td>
					<td>
						<select name="sItem" style="width: 200px;" tabindex="1" onchange="fnGetTaxDetails();">
							<%	
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
							}							
								ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
														(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];
								for(int i=0; i<items.length;i++){
							%>
							<option value="<%=items[i].getItemId()%>"  <%=(request.getAttribute("itemId") != null && 
									((Integer)request.getAttribute("itemId")) == items[i].getItemId()) ? "selected=\"selected\"" : "" %>><%=items[i].getItemNumber()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<%
					HashMap taxDtls = (HashMap) request.getAttribute(Constant.FORM_DATA);
					AccessPointModel[] ac = request.getAttribute(Constant.ACCESS_POINTS) != null ?(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
					for(int i=0; i<ac.length; i++){
				%>
				<tr>
					<td align="right"> TAX SLAB FOR <%=ac[i].getAccessName()+" - "+ac[i].getCity() %></td>
					<td>
						<select name="sTaxSlab<%=ac[i].getAccessId() %>" style="width: 200px;">
							<option value="1" <%=(taxDtls.get(ac[i].getAccessId()) != null && (Integer)(taxDtls.get(ac[i].getAccessId())) == 1) ? "selected=\"selected\"" : "" %>>SLAB 1</option>
							<option value="2" <%=(taxDtls.get(ac[i].getAccessId()) != null && (Integer)(taxDtls.get(ac[i].getAccessId())) == 2) ? "selected=\"selected\"" : "" %>>SLAB 2</option>
							<option value="3" <%=(taxDtls.get(ac[i].getAccessId()) != null && (Integer)(taxDtls.get(ac[i].getAccessId())) == 3) ? "selected=\"selected\"" : "" %>>SLAB 3</option>
						</select>
					 </td>
				</tr>
				<%
					}
					if(ac.length>0){
				%>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Modify" onclick="fnModifyTax();"></td>
				</tr>
				<%
					}
				%>
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