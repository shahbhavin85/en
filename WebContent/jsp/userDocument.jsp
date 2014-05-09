<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/userAccess.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View/Upload User Document</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">User Name <span style="color: red">*</span> :</td>
					<td>
						<select name="txtUserId" style="width: 200px;" onchange="fnGetUploadedList();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"  <%=(request.getAttribute(Constant.USER) != null && 
									request.getAttribute(Constant.USER).equals(users[i].getUserId())) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
			</table>
			<%	
				if(request.getAttribute(Constant.FORM_DATA) != null){
			%>
			<table style="width: 650px;" border=1 cellpadding="3">
				<tr>
					<th>S No.</th>
					<th width="80%">Document Name</th>
					<th>Link</th>
				</tr>
			<%
					String [][] docs = (request.getAttribute(Constant.FORM_DATA)!=null) ? (String[][]) request.getAttribute(Constant.FORM_DATA) : new String[0][0];
					if(docs.length == 0){
			%>
				<tr>
					<th colspan="3">No documents uploaded</th>
				</tr>
			<%
					} else {
						for(int i=0; i<docs.length; i++){
			%>
				<tr>
					<td align="center"><%=i+1 %></td>
					<td style="font-weight: bold;"><%=docs[i][0] %></td>
					<td align="center"><a href="javascript: window.open('../USER_DOCS/<%=request.getAttribute(Constant.USER)%>/<%=docs[i][1]%>');">Link</a></td>
				</tr>
			<%
						}
					}
			%>
			</table>
			<table style="width: 650px;">
				<tr>
					<th><input type="button" value="Upload new document" onclick="fnOpenUploadWindow()"></th>
				</tr>
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<div id="taskWindow" style="display: none; width:400px; background-color:#D3BECF; border:#2E0854 solid 3px; -moz-border-radius: 10px; border-radius: 10px; -moz-box-shadow: -2px -2px 3px #d9d9d9; -webkit-box-shadow: -2px -2px 3px #d9d9d9; box-shadow: -2px -2px 3px #d9d9d9; padding:10px; position: fixed; margin-left:30%; top:10%; z-index:1002;">
		<fieldset>
			<legend style="font-weight: bold; color: blue;">Comment</legend>
			<table cellspacing="5">
				<tr>
					<td align="right" valign="top">Document Name :</td>
					<td><textarea style="text-transform: uppercase;" name="taDescription" cols="50" rows="5"></textarea></td>
				</tr>
				<tr>
					<td align="right">File :</td>
					<td><input name="file" type="file"/></td>
				</tr>
				<tr>
					<td align="center" colspan="2"><input type="button" value="Upload" onclick="fnSave();"/> <input type="button" value="Reset" onclick="fnReset();"/> <input type="button" value="Cancel" onclick="fnCancel();"/></td>
				</tr>
			</table>
		</fieldset>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
<script type="text/javascript">
function fnOpenUploadWindow(){
	document.forms[0].txtUserId.value = '<%=request.getAttribute(Constant.USER)%>';
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOn';
	document.documentElement.style.overflow = "hidden";
	document.getElementById("taskWindow").style.display = 'block';
	return;
}

function fnReset(){
	document.forms[0].taDescription.value= '';
	document.forms[0].file.value = '';
	return;
}

function fnCancel(){
	document.forms[0].taDescription.value= '';
	document.forms[0].file.value = '';
	document.documentElement.style.overflow = "visible";
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOff';
	document.getElementById("taskWindow").style.display = 'none';
	return;	
}

function fnSave(){
	if(document.forms[0].taDescription.value == ''){
		alert('Please provide document name.');
		return;
	}
	if(document.forms[0].file.value == ''){
		alert('Please provide file path.');
		return;
	}
	document.forms[0].enctype="multipart/form-data";
	document.forms[0].HANDLERS.value = 'USER_HANDLER';
	document.forms[0].ACTIONS.value = 'UPLOAD_USER_DOC';
	fnFinalSubmit();
}

function fnGetUploadedList(){
	document.forms[0].HANDLERS.value = 'USER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_USER_DOC';
	fnFinalSubmit();
}
</script>
</html>