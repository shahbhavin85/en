<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/itemGrp.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddNewItemGrp">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Modify Item Group</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Item Group <span style="color: red">*</span> :</td>
					<td>
						<select name="sItemGroup" style="width: 200px;" onchange="fnGetItemGrpDetails();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								
								ItemGroupModel[] itemGrps = request.getAttribute(Constant.ITEM_GROUPS) != null ?
														(ItemGroupModel[]) request.getAttribute(Constant.ITEM_GROUPS) : new ItemGroupModel[0];
								for(int i=0; i<itemGrps.length;i++){
							%>
							<option value="<%=itemGrps[i].getItemGroupId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((ItemGroupModel)request.getAttribute(Constant.FORM_DATA)).getItemGroupId() == itemGrps[i].getItemGroupId()) ? "selected=\"selected\"" : "" %>><%=itemGrps[i].getItemGroup()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Item Group <span style="color: red">*</span> :</td>
					<td><input name="txtItemGrp" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ?  ((ItemGroupModel)request.getAttribute(Constant.FORM_DATA)).getItemGroup() : "" %>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdateItemGrpDetails();" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%>
						value="Modify"> <!--input type="reset" value="Delete" onclick="javascript: document.frmLoginPage.txtUser.focus();"--></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>