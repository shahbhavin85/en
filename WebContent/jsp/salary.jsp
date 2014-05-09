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
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<%	
	SalaryModel previous = (SalaryModel)request.getAttribute(Constant.PREVIOUS_SALARY);
	SalaryModel model = (SalaryModel)request.getAttribute(Constant.FORM_DATA);
	EntryModel[] entries = (EntryModel[]) request.getAttribute(Constant.CURRENT_ADVANCES);
	TargetModel target = (TargetModel) request.getAttribute(Constant.CURRENT_TARGET);
	double nettAdvance = 0;

	Calendar today = new GregorianCalendar();
	Calendar end = new GregorianCalendar();

	Date dt = (new SimpleDateFormat("yyyy-MM-dd")).parse((DateUtil.getCurrentQuarterDate(today))[1]);
	end.setTime(dt);
	
	double days = (end.getTimeInMillis() - today.getTimeInMillis()) / (1000*60*60*24);
	
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR,model.getYear());
	calendar.set(Calendar.MONTH,model.getMonth()-1);
	int monthMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	String totalSalary = Utils.get2Decimal(Utils.get2DecimalDouble((model.getPresentDays()+model.getHoliDays())*Double.parseDouble(model.getUser().getSalary())/monthMaxDays) - model.getLatePenalty() - model.getPenalty() + (model.getOt()*Double.parseDouble(model.getUser().getSalary())/(monthMaxDays*10)));
	calendar.set(Calendar.MONTH,previous.getMonth()-1);
	monthMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	String previousSalary = Utils.get2Decimal(Utils.get2DecimalDouble((previous.getPresentDays()+previous.getHoliDays())*Double.parseDouble(previous.getUser().getSalary())/monthMaxDays) - previous.getLatePenalty() - previous.getPenalty() + (previous.getOt()*Double.parseDouble(previous.getUser().getSalary())/(monthMaxDays*10)));
%>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">User Accounts Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table width="100%">
				<tr>
					<td width="25%" valign="top"> 
						<fieldset>
							<legend class="pageSubHeader">Previous Month Salary Details</legend>
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
									<th>PENALTY(-)</th>
									<th><%=	Utils.get2Decimal(previous.getPenalty()) %></th>
								</tr>
								<tr>
									<th>OVER TIME - <%=previous.getOt()%> (+)</th>
									<th><%=	Utils.get2Decimal(previous.getOt()*Double.parseDouble(previous.getUser().getSalary())/(monthMaxDays*10)) %></th>
								</tr>
								<tr>
									<th>NETT SALARY</th>
									<th><%=previousSalary%></th>
								</tr>
							</table>
						</fieldset>
					</td>
					<td width="25%" valign="top"> 
						<fieldset>
							<legend class="pageSubHeader">Current Month Salary Details</legend>
							<table border="1" cellpadding="10" style="width: 100%;">
								<tr>
									<th>BASE SALARY AMOUNT</th>
									<th><%=Utils.get2Decimal(Double.parseDouble(model.getUser().getSalary())) %></th>
								</tr>
								<tr>
									<th>PRESENT DAYS</th>
									<th><%=model.getPresentDays() %></th>
								</tr>
								<tr>
									<th>ABSENT DAYS</th>
									<th><%=model.getLeaveDays() %></th>
								</tr>
								<tr>
									<th>HOLIDAYS</th>
									<th><%=model.getHoliDays() %></th>
								</tr>
								<tr>
									<th>SALARY DAYS</th>
									<th><%=model.getPresentDays() + model.getHoliDays() %></th>
								</tr>
								<tr>
									<th>SUB SALARY</th>
									<th><%=	Utils.get2Decimal((model.getPresentDays()+model.getHoliDays())*Double.parseDouble(model.getUser().getSalary())/monthMaxDays) %></th>
								</tr>
								<tr>
									<th>LATE FINES (-)</th>
									<th><%=	Utils.get2Decimal(model.getLatePenalty()) %></th>
								</tr>
								<tr>
									<th>PENALTY(-)</th>
									<th><%=	Utils.get2Decimal(model.getPenalty()) %></th>
								</tr>
								<tr>
									<th>OVER TIME - <%=model.getOt()%> (+)</th>
									<th><%=	Utils.get2Decimal(model.getOt()*Double.parseDouble(model.getUser().getSalary())/(monthMaxDays*10)) %></th>
								</tr>
								<tr>
									<th>NETT SALARY</th>
									<th><%=totalSalary%></th>
								</tr>
							</table>
						</fieldset>
					</td>
					<td width="25%" valign="top"> 
						<fieldset>
							<legend class="pageSubHeader">Current Month Advance Details</legend>
							<table border="1" cellpadding="10" style="width: 100%;">
								<tr>
									<th>ADVANCE TILL PREVIOUS MONTH</th>
									<th><%=Utils.get2Decimal(model.getAdvances())%></th>
								</tr>
								<%
									nettAdvance = model.getAdvances();
									for(int i=0; i<entries.length; i++){
										nettAdvance = nettAdvance + ((entries[i].getEntryType()<50) ? -entries[i].getAmount() : entries[i].getAmount());
								%>
								<tr>
									<th align="left"><%="DATE : "+Utils.convertToAppDateDDMMYY(entries[i].getEntryDate())+"<br/>"+Utils.getEntryType(entries[i].getEntryType())+"<br/>"+entries[i].getDesc()%></th>
									<th><%=(entries[i].getEntryType()>50) ? Utils.get2Decimal(entries[i].getAmount()) : "-"+Utils.get2Decimal(entries[i].getAmount())%></th>
								</tr>
								<%
									}
								%>
								<tr>
									<th>NETT ADVANCE</th>
									<th><%=Utils.get2Decimal(nettAdvance)%></th>
								</tr>
							</table>
						</fieldset>
					</td>
					<td width="25%" valign="top"> 
						<fieldset>
							<legend class="pageSubHeader">Current Quarter Target Details</legend>
							<table border="1" cellpadding="10" style="width: 100%;">
								<tr>
									<th>TARGET</th>
									<th><%=Utils.get2Decimal(target.getTarget()) %></th>
								</tr>
								<tr>
									<th>SALES</th>
									<th><%=Utils.get2Decimal(target.getSalesAmt()) %></th>
								</tr>
								<tr>
									<th>PENDING</th>
									<th><%=Utils.get2Decimal(target.getTarget() - target.getSalesAmt()) %></th>
								</tr>
								<tr>
									<th>DAYS LEFT</th>
									<th><%=days %></th>
								</tr>
								<tr>
									<th>COMMISSION*</th>
									<th><%=target.getCommission() %>%</th>
								</tr>
								<tr>
									<th>COMMISSION AMOUNT*</th>
									<th>Rs. <%=Utils.get2Decimal(target.getCommission()*target.getSalesAmt()/100) %>*</th>
								</tr>
							</table>
							<span style="color: red;">* Commission Amount will be credited to your salary only after target it achieved.</span>
						</fieldset>
					</td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>