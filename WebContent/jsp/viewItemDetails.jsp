<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
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
			<legend class="screenHeader">View Item Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td style="vertical-align: top;">
						<table cellspacing="5">
							<tr>
								<td align="right">Select Item :</td>
								<td>
									<select name="sItem" style="width: 200px;" tabindex="1" onchange="fnGetItemDetails(this);">
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
										<option value="<%=items[i].getItemId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
												((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemId() == items[i].getItemId()) ? "selected=\"selected\"" : "" %>><%=items[i].getItemNumber()%></option>
										<%
											}
										%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<fieldset>
										<legend style="font-weight: bold; color: green;">Master Details</legend>
										<table cellspacing="8">
											<tr>
												<td align="right">Item Cateory :</td>
												<td>
														<%								
															ItemCategoryModel[] itemCats = request.getAttribute(Constant.ITEM_CATEGORY) != null ?
																					(ItemCategoryModel[]) request.getAttribute(Constant.ITEM_CATEGORY) : new ItemCategoryModel[0];
															for(int i=0; i<itemCats.length;i++){
																if(request.getAttribute(Constant.FORM_DATA) != null && 
																		((ItemCategoryModel)request.getAttribute(Constant.FORM_DATA)).getItemCatId() == itemCats[i].getItemCatId()){
														%>
															<%=itemCats[i].getItemCategory()%>
														<%
																}
															}
														%>
												</td>
											</tr>
											<tr>
												<td align="right">Item Name  :</td>
												<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemName() : ""%></td>
											</tr>
											<tr>
												<td align="right">Item Number  :</td>
												<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemNumber() : ""%></td>
											</tr>
											<tr>
												<td align="right">Item Label Desc  :</td>
												<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemLabel() : ""%></td>
											</tr>
											<tr>
												<td align="right">Item Price  :</td>
												<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemPrice() : ""%> Rs.</td>
											</tr>
											<tr>
												<td align="right">Delivery Time :</td>
												<td><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getDevTime() : ""%> Days</td>
											</tr>
											<tr>
												<td align="right">View Catalogue :</td>
												<td> <input type="button" name="Download" value="Download" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
														((ItemModel)request.getAttribute(Constant.FORM_DATA)).isCatalogue()) ? "" : "disabled=\"disabled\""%> onclick="fnDownloadCatalogue();"></td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
							<%
								if(request.getAttribute("checklist")!=null && ((HashMap)request.getAttribute("checklist")).keySet().size()>0){
							%>
							<tr>
								<td colspan="2">
									<fieldset>
										<legend style="font-weight: bold; color: green;">Item Check List</legend>
										<table cellspacing="8">
							<%
									Iterator itr = ((HashMap)request.getAttribute("checklist")).keySet().iterator();
									String desc = "";
									while(itr.hasNext()){
										desc = (String)itr.next();
							%>
											<tr>
												<td align="right"><%=desc%> :- </td>
												<td style="background-color: black; padding :3px; color: white; font-weight: bold;" align="center"><%=((HashMap)request.getAttribute("checklist")).get(desc) %></td>
											</tr>
							<%
									}
							%>
										</table>
									</fieldset>
								</td>
							</tr>
							<%
								}
							%>
						</table>
					</td>
					<td rowspan="5"><img id="imgId" alt="No Photo" src="<%=(request.getAttribute(Constant.FORM_DATA) != null && 
							((ItemModel)request.getAttribute(Constant.FORM_DATA)).isPhoto()) ? "/HESH_ITEM_IMG/"+((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemId()+".jpg" : "/en/images/na.jpg" %>" style="max-height : 5.83in; min-height: 5.83in; min-width: 4.13in; max-width: 4.13px;"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/> 
</form>
<script type="text/javascript">
function fnGetItemDetails(itemId){
	if(itemId.value != '--'){
		document.forms[0].HANDLERS.value = 'ITM_DTLS_HANDLER';
		document.forms[0].ACTIONS.value = 'VIEW_ITEM_DETAILS';
		fnFinalSubmit();
	}
}
<%
	if(request.getAttribute(Constant.FORM_DATA) != null){
%>
function fnDownloadCatalogue(){
	window.open('/HESH_ITEM_CATALOGUE/'+<%=((ItemModel)request.getAttribute(Constant.FORM_DATA)).getItemId()%>+'.pdf',"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
<%
	}
%>
</script>
</body>
</html>