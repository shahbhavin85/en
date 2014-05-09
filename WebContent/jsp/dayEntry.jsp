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
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry"  autocomplete=off>
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Branch Cash Entry</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" style="margin-left: 20px; width: 950px;">
				<tr>
					<td colspan="2" align="center"><input type="button" value="Previous Day" onclick="fnGetEntryDetails('<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getPreviousDate()) : ""%>');"/>&nbsp;&nbsp;<input type="button" value="Next Day" onclick="fnGetEntryDetails('<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getNextDate()) : ""%>');"/></td>
				</tr>
				<tr>
					<td align="right" style="width: 300px;">Entry Date<span style="color: red">*</span> :</td><td colspan="5"><input name="txtRptDate" id="txtRptDate" style="width: 195px;" readonly="readonly" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.convertToAppDate(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntryDate()) : ""%>"></td>
				</tr>
				<%
					if(request.getAttribute(Constant.FORM_DATA) != null && (((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("N") || ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("R")) ){
				%>
				<tr>
					<td align="right">Entry Type<span style="color: red">*</span> :</td>
					<td colspan="5"> 
						<select name="sType" onchange="fnChanged(this);">
							<option value="--">-------</option>
							<optgroup style="background-color: Yellow;" label="Against Sales Bill - Received">
								<option value="41" style="background-color: orange;">Sales Bill Cash Received</option>
								<option value="43" style="background-color: orange;">Sales Bill Cheque / DD Received</option>
								<option value="42" style="background-color: orange;">Sales Bill Credit Card</option>
								<option value="44" style="background-color: orange;">Sales Bill Customer Direct Bank Deposit (Cash / Fund Transfer)</option>
							</optgroup>
							<optgroup style="background-color: Yellow;" label="Against Labour Bill - Received">
								<option value="46" style="background-color: #ffcc99;">Labour Bill Cash Received</option>
								<option value="48" style="background-color: #ffcc99;">Labour Bill Cheque / DD Received</option>
								<option value="47" style="background-color: #ffcc99;">Labour Bill Credit Card</option>
								<option value="49" style="background-color: #ffcc99;">Labour Bill Customer Direct Bank Deposit (Cash / Fund Transfer)</option>
							</optgroup>
							<optgroup style="background-color: Yellow;" label="Advance Order - Received">
								<option value="3" style="background-color: lightpink;">Order Advance Cash Received</option>
								<option value="1" style="background-color: lightpink;">Order Advance Cheque / DD Received</option>
								<option value="2" style="background-color: lightpink;">Order Advance Credit Card</option>
								<option value="4" style="background-color: lightpink;">Order Advance Customer Direct Bank Deposit (Cash / Fund Transfer)</option>
<!-- 								<option value="5" style="background-color: lightpink;">Branch Cash Received</option> -->
							</optgroup>
							<optgroup style="background-color: Yellow;" label="Against Customer Previous Balance - Received">
								<option value="21" style="background-color: lightblue;">Open Balance Cash Received</option>
								<option value="23" style="background-color: lightblue;">Open Balance Cheque / DD Received</option>
								<option value="22" style="background-color: lightblue;">Open Balance Credit Card</option>
								<option value="24" style="background-color: lightblue;">Open Balance Customer Direct Bank Deposit (Cash / Fund Transfer)</option>
							</optgroup>
							<optgroup style="background-color: Yellow;" label="Others - Received">
								<option value="9" style="background-color: lightgreen;">Bank Cash Received</option>
								<option value="10" style="background-color: lightgreen;">Staff Advance Returned</option>
								<option value="11" style="background-color: lightgreen;">Other Charges Recevied</option>
							</optgroup>
							<optgroup style="background-color: Yellow;" label="Against Purchase Bill - Paid">
								<option value="71" style="background-color: lightgray;">Purchase Bill Cash Paid</option>
								<option value="72" style="background-color: lightgray;">Purchase Bill Cheque / DD Paid</option>
								<option value="73" style="background-color: lightgray;">Purchase Fund Transfer / RTGS / NEFT</option>
							</optgroup>
							<optgroup style="background-color: Yellow;" label="Cash - Paid">
								<option value="82" style="background-color: aqua;">Assets</option>
								<option value="54" style="background-color: aqua;">Bank Deposit</option>
								<option value="89" style="background-color: aqua;">Bank Charges</option>
								<option value="60" style="background-color: aqua;">Branch Cash Paid</option>
								<option value="61" style="background-color: aqua;">Commission Paid</option>
								<option value="52" style="background-color: aqua;">Transport / Coolie / Courier</option>
								<option value="63" style="background-color: aqua;">Electricity Expenses</option>
								<option value="83" style="background-color: aqua;">FOREX</option>
								<option value="85" style="background-color: aqua;">Income Tax</option>
								<option value="88" style="background-color: aqua;">Insurance</option>
								<option value="51" style="background-color: aqua;">Office Expenses</option>
								<option value="55" style="background-color: aqua;">Paid to Supplier (Local Purchase)</option>
								<option value="57" style="background-color: aqua;">Printing / Stationary / Packing</option>
								<option value="64" style="background-color: aqua;">Promotion / Advertising Expenses</option>
								<option value="84" style="background-color: aqua;">Rent</option>
								<option value="58" style="background-color: aqua;">Salary Advance to Staff</option>
								<option value="59" style="background-color: aqua;">Salary to Staff</option>
								<option value="86" style="background-color: aqua;">Sales Tax</option>
								<option value="62" style="background-color: aqua;">Telephone Expenses</option>
								<option value="53" style="background-color: aqua;">Travelling Expenses</option>
								<option value="87" style="background-color: aqua;">TDS</option>
								<option value="65" style="background-color: aqua;">Wages</option>
								<option value="81" style="background-color: aqua;">Journal Entry</option>
								<option value="56" style="background-color: aqua;">Others Expenses</option>
							</optgroup>
						</select>
					</td>
				</tr>
				<tr id="payRow" style="display: none;">
					<td align="right"> Paid By :</td>
					<td colspan=5>
						<input type="radio" name="rPayMode" value="0" id="rPayMode0" checked="checked" onchange="fnChangeMode(0);"/><label for="rPayMode0">CASH</label>
						<input type="radio" name="rPayMode" value="1" id="rPayMode1" onchange="fnChangeMode(1);"/><label for="rPayMode1">CHEQUE</label>
						<input type="radio" name="rPayMode" value="2" id="rPayMode2" onchange="fnChangeMode(2);"/><label for="rPayMode2">NEFT / RTGS</label>
					</td>
				</tr>
				<tr id="custRow" style="display: none;">
					<td align="right"> Customer :</td>
					<td colspan=5>
						<input type="text" name="txtCust" autocomplete="off" value="" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
<!-- 						<select name="sCustomer" onchange="fnCustomerChange(this);"> -->
<!-- 							<option value="--">-------</option> -->
<%-- 							<% --%>
								
<!-- 								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ? -->
<!-- 														(CustomerModel[]) request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0]; -->
<!-- 								for(int i=0; i<customers.length;i++){ -->
<!-- 							%> -->
<%-- 							<option value="<%=customers[i].getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>"><%=customers[i].getCustomerName()%> - <%=customers[i].getArea()%> - <%=customers[i].getCity()%></option> --%>
<%-- 							<% --%>
<!-- 								} -->
<!-- 							%> -->
<!-- 						</select> -->
					</td>
				</tr>
				<tr id="custDtlsRow" style="display: none;">
					<td align="right" style="width: 295px;"> Customer Bank :</td>
					<td colspan="5"><input type="text"  maxlength="30" name="txtCustBank" size="35"/>
					Deposit Dt :
					<input name="txtDepDate" id="txtDepDate" size="10"  readonly="readonly" onfocus="scwShow(scwID('txtDepDate'),event);" onclick="scwShow(scwID('txtDepDate'),event);" 
						value=""></td>
				</tr>
				<tr id="custDtlsRow1" style="display: none;">
					<td align="right"> Cheque No :</td>
					<td colspan="5"><input type="text"  maxlength="10" name="txtChqNo" size="10"/>
					Cheque Dt :
					<input name="txtChqDate" id="txtChqDate" size="10"  readonly="readonly" onfocus="scwShow(scwID('txtChqDate'),event);" onclick="scwShow(scwID('txtChqDate'),event);" 
						value=""></td>
				</tr>
				<tr id="bankRow" style="display: none;">
					<td align="right"> Bank :</td>
					<td colspan="5">
						<select name="sHeshBank"  style="min-width: 200px;">
							<option value = "--">------</option>
							<option value = "KOTAK">KOTAK</option>
							<option value = "ICICI">ICICI</option>
							<option value = "SBT">SBT</option>
							<option value = "BOM">BOM</option>
						</select>
					</td>
				</tr>
				<tr id="branchRow" style="display: none;">
					<td align="right">Branch  :</td>
					<td colspan="5">
					<%
						String branchId = "";
						if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")){
							branchId = (String)request.getSession().getAttribute(Constant.ACCESS_POINT);
						} else if(request.getAttribute(Constant.FORM_DATA) != null){
							branchId = ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId()+"";
						}
					%>
						<select  name="txtAccessId" style="width: 200px;">
							<option value="--">-----</option>
							<%if(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() != 2){ %>
							<option value="2">Accounts</option>
							<%
							}if(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() != 3){ %>
							<option value="3">Exhibition</option>
							<%
							}
								AccessPointModel[] access = request.getAttribute(Constant.ACCESS_POINTS) != null ?
														(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
								for(int i=0; i<access.length;i++){
									if(((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == (access[i].getAccessId())){
										continue;
									}
							%>
							<option value="<%=access[i].getAccessId()%>"><%=access[i].getAccessName()+" - "+access[i].getCity()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr id="userRow" style="display: none;">
					<td align="right">Staff  :</td>
					<td>
						<select name="txtUserId" style="width: 200px;" onchange="fnGetUserDetails();">
							<option value="--">-------</option>
							<%
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr id="amtRow" style="display: none;">
					<td align="right"> Amount<span style="color: red">*</span> :</td>
					<td colspan="5"><input type="text" maxlength="13" name="txtAmount" size="25"/></td>
				</tr>
				<tr id="remarkRow" style="display: none;">
					<td align="right">Remark :</td>
					<td colspan="5"><textarea  name="taRemark" style="width: 400px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr id="btnRow" style="display: none;">
					<td colspan="6" align="center"><input type="button" value="Save" onclick="fnAddNewEntry();"/></td>
				</tr>
				<%
					} else {
				%>
				<tr>
					<td colspan="6" align="center"><input type="button" value="Back" onclick="fnBack();"/></td>
				</tr>
				<%
					}
				%>
			</table>
			<%
				EntryModel[] entries = (request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getEntries() : new EntryModel[0];
				double crTotal = 0;
				double openBal = Utils.get2DecimalDouble(((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getOpeningBal());
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
			<table style="width: 950px;">
				<%if(openBal+crTotal-drTotal>2000) {%>
				<tr>
					<td colspan="4" align="center" style="color: red; font-weight: bold; font-size: 15px; background: yellow; font-style: italic;">Please deposit Rs. <%=Utils.get2Decimal(openBal+crTotal-drTotal-1000)%> to KOTAK</font> bank.</td>
				</tr>
				<%}%>
				<tr>
					<td colspan="2" align="left"><label style="font-size: 16px; font-weight: bold;"><u>DAY BOOK ENTRIES</u></label><%if(request.getAttribute(Constant.FORM_DATA) != null && (((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("N") || ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("R")) ){%><input type="button" value="Sync Sales Data" style="margin-left: 20px;" onclick="fnSyncSalesData();"/><%}%></td>
					<td colspan="2" align="right"><label style="font-size: 16px; font-weight: bold;"><u>Status</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getDisplayStatus() : ""%></label></td>
				</tr>
				<tr>
					<td align="center"><b>Opening Balanace : </b> <%=Utils.get2Decimal(openBal)%></td>
					<td align="center"><b>Cr. Balanace (+) : </b> <%=Utils.get2Decimal(crTotal)%></td>
					<td align="center"><b>Dr. Balanace (-) : </b> <%=Utils.get2Decimal(drTotal) %></td>
					<td align="center"><b>Closing Balanace : </b> <%=(request.getAttribute(Constant.FORM_DATA) != null) ? Utils.get2Decimal(openBal+crTotal-drTotal) : "0.00" %></td>
				</tr>
			</table>
			<table cellspacing="0" border="1" style="margin-top: 10px; width: 950px;">
				<tr>
					<th style="width: 50px;">S No</th>
					<th style="width: 150px;">TYPE</th>
					<th>REMARK</th>
					<th style="width: 50px;">CR/DR</th>
					<th style="width: 100px;">AMOUNT</th>
					<th style="width: 100px;">USER</th>
					<th style="width: 100px;">ACTIONS</th>
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
					<td align="center"><%=entries[i].getUserName()  %></td>
					<td align="center"><%if((request.getAttribute(Constant.FORM_DATA) != null && (((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("N") || ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("R")) ) && entries[i].isEditable()){ %><input type="checkbox" onclick="fnChangeCheck(this,'btn<%=i%>')"><%} %> <input type="button" id="btn<%=i%>" disabled="disabled" value="Delete" <%=(entries[i].isEditable()) ? "" : "disabled='disabled'" %> onclick="fnDeleteEntry(<%=entries[i].getEntryId()%>, <%=entries[i].getEntryType()%>);"/></td>
				</tr>
				<%
					}
				%>
			</table>
			<table style="width: 950px;"><tr><td align="center"><%if(request.getAttribute(Constant.FORM_DATA) != null && (((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("N") || ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getStatus().equals("R"))){%><input type="button" value="Sync & Review" onclick ="fnSyncReview();">&nbsp;<%}%><input type="button" value="Print" onclick ="fnPrintRpt();"></td></tr></table>
			
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((DayBookEntryModel)request.getAttribute(Constant.FORM_DATA)).getId() : ""%>"/>
	<input type="hidden" name="txtIndex" value=""/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>

