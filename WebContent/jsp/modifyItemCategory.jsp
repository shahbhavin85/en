<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/itemCat.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModItemCat">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Modify Item Category</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Item Category<span style="color: red">*</span> :</td>
					<td>
						<select name="sItemCategory" style="width: 200px;" onchange="fnGetItemCategoryDetails();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								
								ItemCategoryModel[] itemCats = request.getAttribute(Constant.ITEM_CATEGORY) != null ?
														(ItemCategoryModel[]) request.getAttribute(Constant.ITEM_CATEGORY) : new ItemCategoryModel[0];
								for(int i=0; i<itemCats.length;i++){
							%>
							<option value="<%=itemCats[i].getItemCatId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((ItemCategoryModel)request.getAttribute(Constant.FORM_DATA)).getItemCatId() == itemCats[i].getItemCatId()) ? "selected=\"selected\"" : "" %>><%=itemCats[i].getItemGroup()%> - <%=itemCats[i].getItemCategory()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Item Category <span style="color: red">*</span> :</td>
					<td><input name="txtItemCategory" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemCategoryModel)request.getAttribute(Constant.FORM_DATA)).getItemCategory() : ""%>" style="width: 195px;"></td>
				</tr>
				<tr>
					<td align="right">Item Group <span style="color: red">*</span> :</td>
					<td>
						<select name="sItemGroup" style="width: 200px;">
						<%	 
								ItemGroupModel[] itemGrps = request.getAttribute(Constant.ITEM_GROUPS) != null ?
														(ItemGroupModel[]) request.getAttribute(Constant.ITEM_GROUPS) : new ItemGroupModel[0];
								for(int i=0; i<itemGrps.length;i++){
							%>
							<option value="<%=itemGrps[i].getItemGroupId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((ItemCategoryModel)request.getAttribute(Constant.FORM_DATA)).getItemGroupId() == itemGrps[i].getItemGroupId()) ? "selected=\"selected\"" : "" %>><%=itemGrps[i].getItemGroup()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdateItemCategoryDetails();"
						value="Modify" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%>></td>
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