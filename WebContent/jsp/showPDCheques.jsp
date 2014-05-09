<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.util.Constant"%>
<%@page import="com.en.model.EntryModel"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/dayBook.js"></script>
<script type="text/javascript">
</script>
<script type="text/javascript" src="js/timepicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<form method="post" name="frmViewEnquiry">
	<div class="loginClass" style="min-width: 625px;">
		<fieldset>
			<%
				EntryModel[] pdChqs = (request.getAttribute(Constant.PD_CHEQUES_LIST) != null) ? (EntryModel[]) request.getAttribute(Constant.PD_CHEQUES_LIST) : new EntryModel[0];
				double pdChqTotal = 0;
			%>
			<legend class="screenHeader"><%=(String) request.getAttribute("pageTitle")%></legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<center><font style="font-family: calibri; font-size: 13px; font-weight: bold;"><%=(String) request.getAttribute("pageTitle")%> Entries</font></center>  
			<table cellpadding="3" border=1 style="width: 600px;" align="center">
				<tr>
					<th style="width: 100px; ">S.No</th>
					<th style=" ">Desciption</th>
					<th style="width: 120px;">Amount</th>
				</tr>
				<%
					if(pdChqs.length>0){
					for(int i=0; i<pdChqs.length; i++){
						pdChqTotal = pdChqTotal+ pdChqs[i].getAmount();
						
				%>
				<tr>
					<td style=" " align="center" valign="top"><%=i+1%></td>
					<td style=" "><%=pdChqs[i].getDesc()%></td>
					<td style="" align="right" valign="top"><%=Utils.get2Decimal(pdChqs[i].getAmount())%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th style=" ">&nbsp;</th>
					<th align="right" style=" ">Total</th>
					<th align="right" style=""><%=Utils.get2Decimal(pdChqTotal)%></th>
				</tr>
				<%
					} else {
				%>
				<tr> <td colspan="3" align="center">No Entries</td> </tr>
				<%
					}
				%>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtIndex" value=""/>
</form>
</body>
</html>

