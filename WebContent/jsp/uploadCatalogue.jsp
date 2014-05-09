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
<form method="post" name="frmAddItemCat">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Upload Item Catalogue</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Select Item<span style="color: red">*</span> :</td>
					<td>
						<select name="sItem" style="width: 200px;" tabindex="1" onchange="fnChangeItem(this);">
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
					<td align="right">Catalogue (pdf File) : </td>
					<td><input type="file" disabled="disabled" id="txtFile" name="txtFile" accept="application/pdf" value="" size="100"/></td>
				</tr>
				<tr>
					<td valign="top" colspan="2" align="center"><input name="btnSave" disabled="disabled" type="button" onclick="fnUpdateItemDetails();"
						value="Save"><input name="btnCatalogue" disabled="disabled" type="button" onclick="fnGetCatalogue();"
						value="Catalogue"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
<script type="text/javascript">
function Checkfiles()
{
var fup = document.getElementById('txtFile');
var fileName = fup.value;
var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
if(ext == "pdf" || ext == "PDF")
{
return true;
} 
else
{
alert("Upload pdf file only");
fup.focus();
return false;
}
}

function fnUpdateItemDetails(){
	if(!Checkfiles()){
		return;
	}
	document.forms[0].enctype="multipart/form-data";
	document.forms[0].HANDLERS.value = 'ITM_DTLS_HANDLER';
	document.forms[0].ACTIONS.value = 'UPLOAD_CATALOGUE';
	fnFinalSubmit();
}

function fnChangeItem(itemId){
	if(itemId.value != '--'){
		document.forms[0].btnSave.disabled = false; 
		document.forms[0].txtFile.disabled = false; 
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_CHECK_CATALOGUE&itemId='+encodeURIComponent(itemId.value)+'&temp='+Math.random() ,fnCallBackForPhotoCheck);
		var outerPane = document.getElementById('FreezePane');
		if (outerPane) outerPane.className = 'FreezePaneOn';
		document.documentElement.style.overflow = "hidden";
	} else { 
		document.forms[0].txtFile.disabled = true;
		document.forms[0].btnSave.disabled = true;
	}
}

function fnCallBackForPhotoCheck(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
		if(retObj.status == true){
			document.forms[0].btnCatalogue.disabled = false;
		} else {
			document.forms[0].btnCatalogue.disabled = true;
		}
		var outerPane = document.getElementById('FreezePane');
		if (outerPane) outerPane.className = 'FreezePaneOff';
		document.documentElement.style.overflow = "visible";
    }
}

function fnGetCatalogue(){
	window.open('/HESH_ITEM_CATALOGUE/'+document.forms[0].sItem.value+'.pdf');
}
</script>
</body>
</html>