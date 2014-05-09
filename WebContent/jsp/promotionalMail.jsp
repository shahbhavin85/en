<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.OfferItemModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.OfferModel"%>
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
<script type="text/javascript" src="js/animatedCollapse.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Promotional Mail</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right" style="width: 80px;">To : </td>
					<td>
						<select name="sTo" onchange="fnChanged(this);">
							<option value="0">ALL</option>
							<option value="1">BY CUSTOMER TYPE</option>
							<option value="2">BY CITY</option>
							<option value="3">BY STATE</option>
							<!--option value="4">SPECIFIC</option-->
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding-left: 93px;">
						<select name="sCustomerType" style="width: 200px; display: none;">
							<%
								String[] customerTypes = (String[]) request.getAttribute(Constant.CUST_TYPE);
								for(int i=0; i<customerTypes.length; i++){
							%>
							<option value="<%=customerTypes[i]%>"><%=customerTypes[i] %></option>
							<%
								}
							%>
						</select>
						<select name="txtCity" style="width: 200px; display: none;">
							<option value="">-------</option>
							<%
								String[] cities = request.getAttribute(Constant.CITIES) != null ?
														(String[]) request.getAttribute(Constant.CITIES) : new String[0];
								for(int i=0; i<cities.length;i++){
							%>
							<option value="<%=cities[i]%>"  ><%=cities[i]%></option>
							<%
								}
							%>
						</select>
						<select name="txtState" style="width: 200px; display: none;">
							<option value="">---ALL---</option>
							<%
								
								String[] states = request.getAttribute(Constant.STATES) != null ?
														(String[]) request.getAttribute(Constant.STATES) : new String[0];
								for(int i=0; i<states.length;i++){
							%>
							<option value="<%=states[i]%>"><%=states[i]%></option>
										
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Mail Subject : </td>
					<td><input type="text" name="txtSubject" value="" size="100" maxlength="100"/></td>
				</tr>
				<tr>
					<td align="right">Image : </td>
					<td><input type="file" id="txtFile" name="txtFile" accept="image/*" value="" size="100"/></td>
				</tr>
				<tr>
					<td colspan="2" align="center""><input type="button" value="Send Email" onclick="fnSendMail();"/></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript">
function Checkfiles()
{
var fup = document.getElementById('txtFile');
var fileName = fup.value;
var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
if(ext == "jpg" || ext == "JPG")
{
return true;
} 
else
{
alert("Upload Gif or Jpg images only");
fup.focus();
return false;
}
}

function fnSendMail(){
	if(document.forms[0].txtSubject.value == ""){
		alert("Please enter a subject");
		return;
	}
	if(!Checkfiles()){
		return;
	}
	document.forms[0].enctype="multipart/form-data";
	document.forms[0].HANDLERS.value = 'PROMOTIONAL_MAIL_HANDLER';
	document.forms[0].ACTIONS.value = 'SEND_MAIL';
	fnFinalSubmit();
}

function fnChanged(c){
	document.forms[0].sCustomerType.style.display = 'none';
	document.forms[0].txtCity.style.display = 'none';
	document.forms[0].txtState.style.display = 'none';
	if(c.value == 1){
		document.forms[0].sCustomerType.style.display = 'block';
	} else if(c.value == 2){
		document.forms[0].txtCity.style.display = 'block';
	} else if(c.value == 3){
		document.forms[0].txtState.style.display = 'block';
	}
}
</script>
</html>