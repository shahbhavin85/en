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
<script type="text/javascript" src="js/item.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddItem">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add New Item</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Item Cateory<span style="color: red">*</span> :</td>
					<td>
						<select name="sItemCategory" style="width: 200px;" tabindex="1">
							<%								
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
					<td align="right">Item Name <span style="color: red">*</span> :</td>
					<td><input name="txtItemName" maxlength="50" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemName() : ""%>" style="width: 195px;" tabindex="2"></td>
				</tr>
				<tr>
					<td align="right">Item Number <span style="color: red">*</span> :</td>
					<td><input name="txtItemNumber" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemNumber() : ""%>" style="width: 195px;" tabindex="3"></td>
				</tr>
				<tr>
					<td align="right">Item Label Desc <span style="color: red">*</span> :</td>
					<td><input name="txtItemDesc" maxlength="25" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemLabel() : ""%>" style="width: 195px;" tabindex="3"></td>
				</tr>
				<tr>
					<td align="right">Item Price <span style="color: red">*</span> :</td>
					<td><input name="txtItemPrice" style="width: 195px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemPrice() : ""%>" tabindex="4"> Rs.</td>
				</tr>
				<tr>
					<td align="right">Tax Slab <span style="color: red">*</span> :</td>
					<td>
						<select name="sTaxSlab" style="width: 200px;">
							<option value="1" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getTax().equals("1")) ? "selected=\"selected\"" : "" %>>SLAB 1</option>
							<option value="2" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getTax().equals("2")) ? "selected=\"selected\"" : "" %>>SLAB 2</option>
							<option value="3" <%=(request.getAttribute(Constant.FORM_DATA) != null && ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getTax().equals("3")) ? "selected=\"selected\"" : "" %>>SLAB 3</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Show in Price List<span style="color: red">*</span> :</td>
					<td>
						<input type="radio" name="rPL" value=0 <%=((request.getAttribute(Constant.FORM_DATA) != null && ((ItemModel)request.getAttribute(Constant.FORM_DATA)).isInPL())) ? "checked=\"checked\"" : "" %>  id="rPL0" /><label for="rPL0">YES</label>
						<input type="radio" name="rPL" value=1 id="rPL1" <%=(request.getAttribute(Constant.FORM_DATA) == null || request.getAttribute(Constant.FORM_DATA) != null && !(((ItemModel)request.getAttribute(Constant.FORM_DATA))).isInPL()) ? "checked=\"checked\"" : "" %>/><label for="rPL1">NO</label>
					</td>
				<tr/>
				<tr>
					<td align="right">Delivery Time :</td>
					<td><input name="txtDevTime" style="width: 195px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getDevTime() : ""%>" tabindex="7"> Days</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnSaveItem();"  tabindex="8"
						value="Save"> <input type="reset" value="Reset"  tabindex="9" onclick="javascript: document.frmAddItem.sItemCategory.focus();"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript" src="js/dateUtil.js"></script>

</body>
</html>