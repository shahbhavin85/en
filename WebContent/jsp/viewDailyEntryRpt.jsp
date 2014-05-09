<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.DayBookEntryModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesmanDlyItmModel"%>
<%@page import="com.en.model.SalesmanDlyRptModel"%>
<%@page import="com.en.model.UserModel"%>
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
<script type="text/javascript">
function fnGetReport(){
	document.forms[0].HANDLERS.value = 'BRANCH_DLY_ENTRY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_DLY_ENTRY_STATUS_RPT';
	fnFinalSubmit();
}

function fnPrint(id){
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_HANDLER&ACTIONS=PRNT_DAY_BOOK&txtId='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">View Branch Daily Entry Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Branch <span style="color: red">*</span> :</td>
					<td>
					<%
						String branchId = "";
						if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")){
							branchId = (String)request.getSession().getAttribute(Constant.ACCESS_POINT);
						} else if(request.getAttribute(Constant.FORM_DATA) != null){
							branchId = ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId()+"";
						}
					%>
						<select  name="txtAccessId" style="width: 200px;" <%=(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) ? "disabled=\"disabled\"" : ""%>>
							<option value="2"  <%=((!branchId.equals("") &&
									(Integer.parseInt(branchId) == (2))) || (request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && ((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == (2)) ) ? "selected=\"selected\"" : "" %>>Accounts</option>
							<%
								
								AccessPointModel[] access = request.getAttribute(Constant.ACCESS_POINT) != null ?
														(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINT) : new AccessPointModel[0];
								for(int i=0; i<access.length;i++){
							%>
							<option value="<%=access[i].getAccessId()%>"  <%=((!branchId.equals("") &&
									(Integer.parseInt(branchId) == (access[i].getAccessId()))) || (request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && ((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == (access[i].getAccessId())) ) ? "selected=\"selected\"" : "" %>><%=access[i].getAccessName()+" - "+access[i].getCity()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right">Report Date<span style="color: red">*</span> :</td>
					<td><input name="txtRptDate" id="txtRptDate" style="width: 195px;" readonly="readonly" onclick="scwShow(scwID('txtRptDate'),event);" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryDate()) : DateUtil.getCurrDt()%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtRptDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Report" onclick="fnGetReport();"/></td>
				</tr>
			</table>
			<%
				if(request.getAttribute(Constant.FORM_DATA) != null){
					ArrayList chequeEntries = new ArrayList(0);
					ArrayList creditEntries = new ArrayList(0);
					DayBookEntryModel model = (DayBookEntryModel) request.getAttribute(Constant.FORM_DATA);
			%>
			<table width="100%">
				<tr><th align="center" style="font-family: calibri; font-size: 14px;"> Status : <%=model.getDisplayStatus()%> &nbsp;&nbsp; Appoved By : <%=model.getApprover().getUserName() %></th></tr>
			</table>
			<table border="1" width="100%" cellspacing="0" style="font-family: calibri; font-size: 14px; border-color: transparent;">
				<tr>
					<th colspan="4" align="center">Cash Book</th></tr>
				<tr>
					<th style="width: 100px; border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Date</th>
					<th style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Description</th>
					<th style="width: 100px; border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Cr. - Received</th>
					<th style="width: 100px; border-left-color: transparent; border-right-color: transparent;border-top-color: black;border-bottom-color: black;">Dr. - Paid</th>
				</tr>
				<%
					if(model.getOpeningBal() != 0){
				%>
					<tr>
						<td style="border-left-color: transparent; border-right-color: gray;border-bottom-color: black;">&nbsp;</td>
						<td style="border-left-color: transparent; border-right-color: gray;border-bottom-color: black;" align="right"><b>Opening Balance</b></td>
						<td style="border-left-color: transparent; border-right-color: gray;border-bottom-color: black;" align="right"><b><%=(model.getOpeningBal() > 0) ? Utils.get2Decimal(model.getOpeningBal()) : "&nbsp;" %></b></td>
						<td style="border-left-color: transparent; border-right-color: transparent;border-bottom-color: black;" align="right"><b><%=(model.getOpeningBal() < 0) ? Utils.get2Decimal(0-model.getOpeningBal()) : "&nbsp;" %></b></td>
					</tr>
				<%
					}
					double crTotal = (model.getOpeningBal() > 0) ? model.getOpeningBal() : 0;
					double drTotal = (model.getOpeningBal() < 0) ? (0-model.getOpeningBal()) : 0;
					EntryModel[] entries = model.getEntries();
					for(int i=0 ; i<entries.length; i++){
						if(!entries[i].isCash()){
							if(entries[i].getEntryType() == 1 || entries[i].getEntryType() == 32 || entries[i].getEntryType() == 42){
								chequeEntries.add(entries[i]);
							} else {
								creditEntries.add(entries[i]);
							}
							continue;
						}
						if(entries[i].getEntryType() < 51){
							crTotal = crTotal + entries[i].getAmount();
				%>
					<tr>
						<td valign="top" align="center" style="border-left-color: transparent; border-right-color: gray;border-bottom-color: gray;"><%=Utils.convertToAppDate(model.getEntryDate()) %></td>
						<td align="left" style="border-left-color: transparent; border-right-color: gray;border-bottom-color: gray;"><%=Utils.getEntryType(entries[i].getEntryType())%><br/><%=entries[i].getDesc() %><br/>User : <%=entries[i].getUser() %></td>
						<td align="right" valign="top" style="border-left-color: transparent; border-right-color: gray;border-bottom-color: gray;"><%=Utils.get2Decimal(entries[i].getAmount())%></td>
						<td align="right" style="border-left-color: transparent; border-right-color: transparent;border-bottom-color: gray;">&nbsp;</td>
					</tr>
				<%
						} else {
							drTotal = drTotal + entries[i].getAmount();
				%>
					<tr>
						<td align="center" valign="top" style="border-left-color: transparent; border-right-color: gray;border-bottom-color: gray;"><%=Utils.convertToAppDate(model.getEntryDate()) %></td>
						<td align="left" style="border-left-color: transparent; border-right-color: gray;border-bottom-color: gray;"><%=Utils.getEntryType(entries[i].getEntryType())%><br/><%=entries[i].getDesc() %><br/>User : <%=entries[i].getUser() %></td>
						<td align="right" style="border-left-color: transparent; border-right-color: gray;border-bottom-color: gray;">&nbsp;</td>
						<td align="right" valign="top" style="border-left-color: transparent; border-right-color: transparent;border-bottom-color: gray;"><%=Utils.get2Decimal(entries[i].getAmount())%></td>
					</tr>
				<%
						}
					}
				%>
					<tr>
						<th style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;">&nbsp;</th>
						<th align="right" style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;">Sub - Total</th>
						<th align="right" style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;"><%=Utils.get2Decimal(crTotal) %></th>
						<th align="right" style=" border-left-color: transparent; border-right-color: transparent;border-top-color: black;"><%=Utils.get2Decimal(drTotal) %></th>
					</tr>
					<tr>
						<th style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
						<th style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;" align="right">Closing Balance</th>
						<th style=" border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;" align="right"><%=(crTotal - drTotal > 0) ? Utils.get2Decimal(crTotal - drTotal) : "&nbsp;" %></th>
						<th style=" border-left-color: transparent; border-right-color: transparent;border-top-color: black;border-bottom-color: black;" align="right"><%=(crTotal - drTotal < 0) ? Utils.get2Decimal(drTotal - crTotal) : "&nbsp;" %></th>
					</tr>
			</table>
			<br/>
			<%
				double chqTotal = 0;
				if(chequeEntries.size() > 0){
			%>
			<center><font style="font-family: calibri; font-size: 14px; font-weight: bold;">Cheque Entries</font></center>  
			<table width="100%"  border="1" cellspacing="0" style="font-family: calibri; font-size: 14px; border-color: transparent;">
				<tr>
					<th style="width: 100px;border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">S.No</th>
					<th style="border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Desciption</th>
					<th style="width: 120px;border-left-color: transparent; border-right-color: transparent;border-top-color: black;border-bottom-color: black;">Amount</th>
				</tr>
				<%
					for(int i=0; i<chequeEntries.size(); i++){
						chqTotal = chqTotal+ ((EntryModel)chequeEntries.get(i)).getAmount();
						
				%>
				<tr>
					<td style="border-left-color: transparent; border-right-color: gray;" align="center" valign="top"><%=i+1%></td>
					<td style="border-left-color: transparent; border-right-color: gray;"><%=((EntryModel)chequeEntries.get(i)).getDesc()%>User : <%=((EntryModel)chequeEntries.get(i)).getUser()%></td>
					<td style="border-left-color: transparent; border-right-color: transparent;" align="right" valign="top"><%=Utils.get2Decimal(((EntryModel)chequeEntries.get(i)).getAmount())%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th style="border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
					<th align="right" style="border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Total</th>
					<th align="right" style="border-left-color: transparent; border-right-color: transparent;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(chqTotal)%></th>
				</tr>
			</table>
			<br/>
			<%
				}
				double creTotal = 0;
				if(creditEntries.size() > 0){
			%>
			<center><font style="font-family: calibri; font-size: 14px; font-weight: bold;">Credit Card Entries / Direct Bank Deposit (Cash / Fund Transfer)</font></center> 
			<table width="100%" border="1" cellspacing="0" style="font-family: calibri; font-size: 14px; border-color: transparent;">
				<tr>
					<th style="width: 100px;border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">S.No</th>
					<th style="border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Desciption</th>
					<th style="width: 120px;border-left-color: transparent; border-right-color: transparent;border-top-color: black;border-bottom-color: black;">Amount</th>
				</tr>
				<%
					for(int i=0; i<creditEntries.size(); i++){
						creTotal = creTotal+ ((EntryModel)creditEntries.get(i)).getAmount();
						
				%>
				<tr>
					<td style="border-left-color: transparent; border-right-color: gray;" align="center" valign="top"><%=i+1%></td>
					<td style="border-left-color: transparent; border-right-color: gray;"><%=((EntryModel)creditEntries.get(i)).getDesc()%>User : <%=((EntryModel)creditEntries.get(i)).getUser()%></td>
					<td style="border-left-color: transparent; border-right-color: transparent;" align="right" valign="top"><%=Utils.get2Decimal(((EntryModel)creditEntries.get(i)).getAmount())%></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th style="border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">&nbsp;</th>
					<th align="right" style="border-left-color: transparent; border-right-color: gray;border-top-color: black;border-bottom-color: black;">Total</th>
					<th align="right" style="border-left-color: transparent; border-right-color: transparent;border-top-color: black;border-bottom-color: black;"><%=Utils.get2Decimal(creTotal)%></th>
				</tr>
			</table>
			<%
				}
			%>
			<table width="100%"><tr><td align="center"><input type="button" value="Print" onclick="fnPrint('<%=model.getId()%>');"/></td></tr></table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>