<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.DayBookEntryModel"%>
<%@page import="com.en.model.CustomerModel"%>
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
<script type="text/javascript" src="js/dayBook.js"></script>
<script type="text/javascript">
</script>
<script type="text/javascript" src="js/timepicker.js"></script>
<script type="text/javascript">
function fnSendApproval(){
	var r=confirm("I \"<%=(String)(((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName())%>\", hereby confirm that the above DAY BOOK is checked and prepared by me, any errors or difference in day book I will be liable to answer or bear any kind of loss occured to company due to my negligence.");
	if (r==true) {
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "SEND_APPROVE_DAY_ENTRIES";
		fnFinalSubmit();
	}
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Branch Cash Entry - Review</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" style="margin-left: 20px; width: 950px;">
				<tr>
					<td align="right">Entry Date<span style="color: red">*</span> :</td><td colspan="5"><input name="txtRptDate" id="txtRptDate" style="width: 195px;" readonly="readonly" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryDate()) : ""%>"></td>
				</tr>
			</table>
			<table style="width: 950px;">
				<tr>
					<td colspan="2" align="left"><label style="font-size: 16px; font-weight: bold;"><u>DAY BOOK ENTRIES</u></label></td>
					<td colspan="2" align="right"><label style="font-size: 16px; font-weight: bold;"><u>Status</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getDisplayStatus() : ""%></label></td>
				</tr>
				<tr>
				<%
					EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntries() : new EntryModel[0];
					double crTotal = 0;
					double drTotal = 0;
					for(int i=0; i<entries.length; i++){
						if(!entries[i].isCash())
							continue;
						if(entries[i].getEntryType() < 51)
							crTotal += entries[i].getAmount();
						else 
							drTotal += entries[i].getAmount();
					}
				%>
					<td align="center"><b>Opening Balanace : </b> <%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.get2Decimal(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getOpeningBal()) : "0.00" %></td>
					<td align="center"><b>Cr. Balanace (+) : </b> <%=Utils.get2Decimal(crTotal)%></td>
					<td align="center"><b>Dr. Balanace (-) : </b> <%=Utils.get2Decimal(drTotal) %></td>
					<td align="center"><b>Closing Balanace : </b> <%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.get2Decimal(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getOpeningBal()+crTotal-drTotal) : "0.00" %></td>				</tr>
			</table>
			<table cellspacing="0" border="1" style="margin-top: 10px; width: 950px;">
				<tr>
					<th style="width: 50px;">S No</th>
					<th style="width: 150px;">TYPE</th>
					<th>REMARK</th>
					<th style="width: 50px;">CR/DR</th>
					<th style="width: 100px;">AMOUNT</th>
				</tr>
				<%
					String bgColor = "";
					for(int i=0; i<entries.length; i++){
						if(entries[i].getEntryType() >= 41 && entries[i].getEntryType() <= 44)
							bgColor = "orange";
						else if(entries[i].getEntryType() >= 45 && entries[i].getEntryType() <= 49)
							bgColor = "#ffcc99";
						else if(entries[i].getEntryType() >= 31 && entries[i].getEntryType() <= 32)
							bgColor = "white";
						else if(entries[i].getEntryType() >= 1 && entries[i].getEntryType() <= 5)
							bgColor = "lightpink";
						else if(entries[i].getEntryType() >= 71 && entries[i].getEntryType() <= 73)
							bgColor = "lightgray";
						else if(entries[i].getEntryType() >= 21 && entries[i].getEntryType() <= 24)
							bgColor = "lightblue";
						else if(entries[i].getEntryType() == 9 || entries[i].getEntryType() == 10)
							bgColor = "lightgreen";
						else 
							bgColor = "aqua";
				%>
				<tr>
					<td align="center"><%=(i+1)%></td>
					<td align="center" style="background-color: <%=bgColor%>"><%=Utils.getEntryType(entries[i].getEntryType()) %></td>
					<td><%=entries[i].getDesc() %><br/>User : <%=entries[i].getUser()%></td>
					<td align="center"><%=entries[i].getDisplayCrdr() %></td>
					<td align="center"><%=Utils.get2Decimal(entries[i].getAmount())  %></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="width: 950px;"><tr><td align="center"><%if(request.getAttribute(Constant.FORM_DATA) != null && (((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("N") || ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("R"))){%><input type="button" value="Send For Approval" onclick ="fnSendApproval();">&nbsp;<%}%><input type="button" value="Print" onclick ="fnPrintRpt();"><input type="button" value="Back" onclick ="fnBackToDayBook();"></td></tr></table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getId() : ""%>"/>
	<input type="hidden" name="txtIndex" value=""/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>

