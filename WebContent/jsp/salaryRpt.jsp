<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.en.model.TargetModel"%>
<%@page import="com.en.model.EntryModel"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalaryModel"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/user.js"></script>
<script type="text/javascript">
function fnGetSalaryDetails(){
	if(document.forms[0].txtUserId.value == "--"){
		alert('Please select the Staff');
		return;
	}
	document.forms[0].HANDLERS.value = 'SALARY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_STAFF_SALARY_DETAILS';
	fnFinalSubmit();
}

function fnSaveSalaryDetails(){
	if(document.forms[0].txtPaidOn.value == "--"){
		alert('Please select paid on date.');
		return;
	}
	document.forms[0].HANDLERS.value = 'SALARY_HANDLER';
	document.forms[0].ACTIONS.value = 'SAVE_STAFF_SALARY_DETAILS';
	fnFinalSubmit();
}

function fnCalculateSalary(){
	vSalaryPart = parseFloat(document.getElementById("lblSalary").innerHTML);
	vExpense1 = <%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getUser().getExpense1() : 0%>;
	vExpense2 = <%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getUser().getExpense2() : 0%>;
	vExpense3 = <%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getUser().getExpense3() : 0%>;
	vExpense4 = <%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getUser().getExpense4() : 0%>;
	vExpense5 = <%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getUser().getExpense5() : 0%>;
	
	vActualExpense1 = parseFloat(document.forms[0].txtExpense1.value);
	vActualExpense2 = parseFloat(document.forms[0].txtExpense2.value);
	vActualExpense3 = parseFloat(document.forms[0].txtExpense3.value);
	vActualExpense4 = parseFloat(document.forms[0].txtExpense4.value);
	vActualExpense5 = parseFloat(document.forms[0].txtExpense5.value);
	vAdvanceAdjust = parseFloat(document.forms[0].txtAdvances.value);
	
	vActualExpense = 0;
	
	if(vActualExpense1>vExpense1){
		vActualExpense = vActualExpense + (vActualExpense1 - vExpense1);
	}
	if(vActualExpense2>vExpense2){
		vActualExpense = vActualExpense + (vActualExpense2 - vExpense2);
	}
	if(vActualExpense3>vExpense3){
		vActualExpense = vActualExpense + (vActualExpense3 - vExpense3);
	}
	if(vActualExpense4>vExpense4){
		vActualExpense = vActualExpense + (vActualExpense4 - vExpense4);
	}
	if(vActualExpense5>vExpense5){
		vActualExpense = vActualExpense + (vActualExpense5 - vExpense5);
	}
	
	document.getElementById("lblExpense").innerHTML = vActualExpense > 0 ? '-'+parseFloat(vActualExpense).toFixed(2) : '0.00';
	
	document.getElementById("lblPayable").innerHTML = parseFloat(vSalaryPart - vActualExpense - vAdvanceAdjust).toFixed(2);
	
	return;
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<%
int iYear = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getYear() : Integer.parseInt(DateUtil.getCurrYear());
int iMonth = (request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getMonth() : Integer.parseInt(DateUtil.getCurrMonth());
%>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Salary Calculation</legend>
			<jsp:include page="messages.jsp"></jsp:include>
				<table cellpadding="3">
					<tr>
						<td align="right">Staff <span style="color: red">*</span> :</td>
						<td>
						<%
							String userId = "";
						%>
							<select  name="txtUserId" style="width: 200px;">
								<option value="--">------</option>
								<%
									
									UserModel[] users = request.getAttribute(Constant.USERS) != null ?
															(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
									for(int i=0; i<users.length;i++){
								%>
								<option value="<%=users[i].getUserId()%>"  <%=((request.getAttribute(Constant.FORM_DATA) != null && 
										((SalaryModel) request.getAttribute(Constant.FORM_DATA)).getUser().getUserId().equals(users[i].getUserId()))) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
								<%
									}
								%>
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
							<input type="button" value="Get Details" onclick="fnGetSalaryDetails();"/>
						</td>
					</tr>
				</table>
				<%
					if(request.getAttribute(Constant.FORM_DATA) != null){
						double nettAdvance = 0;
						SalaryModel previous = (SalaryModel)request.getAttribute(Constant.FORM_DATA);
						EntryModel[] entries = (EntryModel[]) request.getAttribute(Constant.CURRENT_ADVANCES);
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.YEAR,previous.getYear());
						calendar.set(Calendar.MONTH,previous.getMonth()-1);
						int monthMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						nettAdvance = previous.getAdvances();
						for(int i=0; i<entries.length; i++){
							nettAdvance = nettAdvance + ((entries[i].getEntryType()<50) ? -entries[i].getAmount() : entries[i].getAmount());
						}
						String totalSalary = Utils.get2Decimal(Utils.get2DecimalDouble((previous.getPresentDays()+previous.getHoliDays())*Double.parseDouble(previous.getUser().getSalary())/monthMaxDays) - previous.getLatePenalty() - previous.getPenalty() + (previous.getOt()*1.5*Double.parseDouble(previous.getUser().getSalary())/(monthMaxDays*10)));

				%>
				<table width="100%">
					<tr>
						<td style="width: 50%">
							<table border="1" cellpadding="10" style="width: 100%;">
								<tr>
									<th>BASE SALARY AMOUNT</th>
									<th><%=Utils.get2Decimal(Double.parseDouble(previous.getUser().getSalary())) %></th>
								</tr>
								<tr>
									<th>PRESENT DAYS</th>
									<th><%=previous.getPresentDays() %></th>
								</tr>
								<tr>
									<th>ABSENT DAYS</th>
									<th><%=previous.getLeaveDays() %></th>
								</tr>
								<tr>
									<th>HOLIDAYS</th>
									<th><%=previous.getHoliDays() %></th>
								</tr>
								<tr>
									<th>SALARY DAYS</th>
									<th><%=previous.getPresentDays() + previous.getHoliDays() %></th>
								</tr>
								<tr>
									<th>SUB SALARY</th>
									<th><%=	Utils.get2Decimal((previous.getPresentDays()+previous.getHoliDays())*Double.parseDouble(previous.getUser().getSalary())/monthMaxDays) %></th>
								</tr>
								<tr>
									<th>LATE FINES (-)</th>
									<th><%=	Utils.get2Decimal(previous.getLatePenalty()) %></th>
								</tr>
								<tr>
									<th>PENALTY (-)</th>
									<th><%=	Utils.get2Decimal(previous.getPenalty()) %></th>
								</tr>
								<tr>
									<th>OVER TIME - <%=previous.getOt()%> (+)</th>
									<th><%=	Utils.get2Decimal(previous.getOt()*1.5*Double.parseDouble(previous.getUser().getSalary())/(monthMaxDays*10)) %></th>
								</tr>
								<tr>
									<th>NETT SALARY</th>
									<th><%=totalSalary%></th>
								</tr>
							</table>
						</td>
						<td style="width: 100%; vertical-align: top;">
							<table border="1" style="width: 100%;">
								<tr style="height : 37px;">
									<th>Expense 1</th>
									<th><%=Utils.get2Decimal(previous.getUser().getExpense1())%></th>
								</tr>
								<tr style="height : 37px;">
									<th>Actual Expense 1</th>
									<th><input type="text" name="txtExpense1" onblur="fnCalculateSalary()" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getExpense1() : 0%>"/> </th>
								</tr>
								<tr style="height : 37px;">
									<th>Expense 2</th>
									<th><%=Utils.get2Decimal(previous.getUser().getExpense2())%></th>
								</tr>
								<tr style="height : 37px;">
									<th>Actual Expense 2</th>
									<th><input type="text" name="txtExpense2" onblur="fnCalculateSalary()" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getExpense2() : 0%>"/> </th>
								</tr>
								<tr style="height : 37px;">
									<th>Expense 3</th>
									<th><%=Utils.get2Decimal(previous.getUser().getExpense3())%></th>
								</tr>
								<tr style="height : 37px;">
									<th>Actual Expense 3</th>
									<th><input type="text" name="txtExpense3" onblur="fnCalculateSalary()" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getExpense3() : 0%>"/> </th>
								</tr>
								<tr style="height : 37px;">
									<th>Expense 4</th>
									<th><%=Utils.get2Decimal(previous.getUser().getExpense4())%></th>
								</tr>
								<tr style="height : 37px;">
									<th>Actual Expense 4</th>
									<th><input type="text" name="txtExpense4" onblur="fnCalculateSalary()" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getExpense4() : 0%>"/> </th>
								</tr>
								<tr style="height : 37px;">
									<th>Expense 5</th>
									<th><%=Utils.get2Decimal(previous.getUser().getExpense5())%></th>
								</tr>
								<tr style="height : 37px;">
									<th>Actual Expense 5</th>
									<th><input type="text" name="txtExpense5" onblur="fnCalculateSalary()" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getExpense5() : 0%>"/> </th>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<table border="1" style="width: 100%; margin: auto;" cellpadding="10">
								<tr>
									<th style="width: 50%; text-align: right;">Salary Part : </th>
									<th style="width: 50%; text-align: left; background-color: white; font-size: 22px;"><label id="lblSalary"><%=totalSalary%></label></th>
								</tr>
								<tr>
									<th style="width: 50%; text-align: right;">(Advance Outstanding : <%=nettAdvance %>) Advance Adjust : </th>
									<th style="width: 50%; text-align: left; background-color: white; font-size: 22px;"><input type="text" onblur="fnCalculateSalary()" name="txtAdvances" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getAdvanceAdjust() : 0%>"/></th>
								</tr>
								<tr>
									<th style="width: 50%; text-align: right;">Expense Part : </th>
									<th style="width: 50%; text-align: left; background-color: white; font-size: 22px;"><label id="lblExpense"></label></th>
								</tr>
								<tr>
									<th style="width: 50%; text-align: right;">Nett Payable : </th>
									<th style="width: 50%; text-align: left; background-color: white; font-size: 22px;"><label id="lblPayable"></label></th>
								</tr>
								<tr style="height: 50px;">
									<th style="width: 50%; text-align: right;">Paid On : </th>
									<th style="width: 50%; text-align: left; background-color: white; font-size: 22px;"><input type="text" name="txtPaidOn" readonly="readonly" onclick="scwShow(scwID('txtPaidOn'),event);" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalaryModel)request.getAttribute(Constant.FORM_DATA)).getPaidOn() : ""%>"><img
			                                    src='images/date.gif'
			                                    title='Click Here' alt='Click Here'
			                                    onclick="scwShow(scwID('txtPaidOn'),event);" />
                                  	</th>
								</tr>
								<tr>
									<td colspan="2" style="text-align: center;"><input type="button" value="Save" onclick="fnSaveSalaryDetails()"/></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
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
<script type="text/javascript">
fnCalculateSalary();
</script>
</html>