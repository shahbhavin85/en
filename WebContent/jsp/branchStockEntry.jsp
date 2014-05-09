<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="com.en.model.BranchStockEntryModel"%>
<%@page import="com.en.model.BranchStockEntryItemModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript">
function fnGetReport(){
	document.forms[0].HANDLERS.value = 'BRANCH_STOCK_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_STOCK_DTLS';
	fnFinalSubmit();
}
<%
int iDay = (request.getAttribute(Constant.FORM_DATA) != null) ? ((BranchStockEntryModel)request.getAttribute(Constant.FORM_DATA)).getDay() : 0;
int iYear = (request.getAttribute(Constant.FORM_DATA) != null) ? ((BranchStockEntryModel)request.getAttribute(Constant.FORM_DATA)).getYear() : Integer.parseInt(DateUtil.getCurrYear());
int iMonth = (request.getAttribute(Constant.FORM_DATA) != null) ? ((BranchStockEntryModel)request.getAttribute(Constant.FORM_DATA)).getMonth() : Integer.parseInt(DateUtil.getCurrMonth());
%>
</script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Branch Stock Entry Details</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<table style="margin-left: 25px;">
				<tr>
					<td>Day : </td>
					<td>
						<select name="sDay">
							<option value = "01" <%=(iDay == 1) ? "selected=\"selected\"" : "" %>>1</option>
							<option value = "16" <%=(iDay == 16) ? "selected=\"selected\"" : "" %>>16</option>
						</select>
					</td>
					<td>Month : </td>
					<td>
						<select name="sMonth">
							<option value = "01" <%=(iMonth == 1) ? "selected=\"selected\"" : "" %>>JANUARY</option>
							<option value = "02" <%=(iMonth == 2) ? "selected=\"selected\"" : "" %>>FEBRUARY</option>
							<option value = "03" <%=(iMonth == 3) ? "selected=\"selected\"" : "" %>>MARCH</option>
							<option value = "04" <%=(iMonth == 4) ? "selected=\"selected\"" : "" %>>APRIL</option>
							<option value = "05" <%=(iMonth == 5) ? "selected=\"selected\"" : "" %>>MAY</option>
							<option value = "06" <%=(iMonth == 6) ? "selected=\"selected\"" : "" %>>JUNE</option>
							<option value = "07" <%=(iMonth == 7) ? "selected=\"selected\"" : "" %>>JULY</option>
							<option value = "08" <%=(iMonth == 8) ? "selected=\"selected\"" : "" %>>AUGUST</option>
							<option value = "09" <%=(iMonth == 9) ? "selected=\"selected\"" : "" %>>SEPTEMBER</option>
							<option value = "10" <%=(iMonth == 10) ? "selected=\"selected\"" : "" %>>OCTOBER</option>
							<option value = "11" <%=(iMonth == 11) ? "selected=\"selected\"" : "" %>>NOVEMBER</option>
							<option value = "12" <%=(iMonth == 12) ? "selected=\"selected\"" : "" %>>DECEMBER</option>
						</select>
					</td>
					<td>Year : </td>
					<td>
						<select name="sYear">
							<option value="<%=iYear-4%>"><%=iYear-4%></option>
							<option value="<%=iYear-3%>"><%=iYear-3%></option>
							<option value="<%=iYear-2%>"><%=iYear-2%></option>
							<option value="<%=iYear-1%>"><%=iYear-1%></option>
							<option value="<%=iYear%>" selected="selected"><%=iYear%></option>
						</select>
					</td>
					<td>
						<input type="button" value="Get Details" onclick="fnGetReport();"/>
					</td>
				</tr>
			</table>
			<%
				BranchStockEntryModel model = (request.getAttribute(Constant.FORM_DATA) != null) ? (BranchStockEntryModel)(request.getAttribute(Constant.FORM_DATA)) : new BranchStockEntryModel();
				if(model.getItems().size()>0){
			%>
			<table style="margin-left: 25px;">
				<tr>
					<td>CHECKED BY : <b><%=model.getCheckedBy().getUserName() %></b></td>
				</tr>
			</table>
			<div style="height: 400px; overflow: hidden; width: 852px; margin-left: 25px; display: block;">
				<table border="1" style="width: 835px; display: block; height : 40px;vertical-align: middle;">
					<tr style="height: 35px;">
						<th style="width: 100px;">ITEM ID</th>
						<th style="width: 100px;">ITEM NUMBER</th>
						<th style="width: 350px;">ITEM NAME</th>
						<th style="width: 150px;">SYSTEM STOCK ON <%=iDay+"/"+iMonth+"/"+iYear %></th>
						<th style="width: 130px;">PHYSICAL STOCK ON <%=iDay+"/"+iMonth+"/"+iYear %></th>
					</tr>
				</table>
				<table border="1" style="height: 353px; margin-top : 2px; overflow: auto; width: 850px; display: block;vertical-align: middle;">
			<%
					Iterator<BranchStockEntryItemModel> items = model.getItems().iterator();
					BranchStockEntryItemModel item = null;
					int i = -1;
					while(items.hasNext()){
						item = items.next();
						i++;
			%>
					<tr align="center" style="font-weight: bold;display: table-row;vertical-align: inherit;">
						<td style="width: 100px;"><input type="text" style="text-align: center; width: 60px;" readonly="readonly" name="id<%=i%>" value="<%=item.getItem().getItemId() %>"/></td>
						<td style="width: 100px;"><%=item.getItem().getItemNumber()%></td>
						<td style="width: 350px;"><%=item.getItem().getItemName()%></td>
						<td style="width: 150px;"><%=(item.getStock() == 0) ? "--" : item.getStock()%></td>
						<td style="width: 130px;"><input type="text" style="text-align: center; width: 60px;" name="qty<%=i%>" value = "<%=item.getQty()%>" /></td>
					</tr>
			<%
					}
			%>
				</table>
			</div>
			<table style="width: 850px;margin-top: 10px; margin-left: 25px;">
				<tr>
					<td colspan="2" align="center" style="font-weight: bold;"><input type="checkbox" onclick="document.forms[0].btnModify.disabled =  !this.checked;"> I "<%=(String)(((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName())%>", hereby confirm that the above stock details are checked by me personally and I approve the same.</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" name="btnModify" disabled="disabled" value="Save" onclick="fnSaveDetails();"/></td>
				</tr>
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="count" value="<%=model.getItems().size()%>"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
function fnSaveDetails(){
	document.forms[0].HANDLERS.value = "BRANCH_STOCK_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "UPDT_STOCK_DTLS";
	fnFinalSubmit();
}
</script>
</html>