<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.SalesModel"%>
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
<script type="text/javascript" src="js/outStandingRpt.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Outstanding Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right" valign="top">Branch<span style="color: red">*</span> :</td>
					<td>
					<%
						String[] accessSelected = (request.getAttribute("cBranch") != null) ? ((String[])request.getAttribute("cBranch")) : new String[0];
						if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")){
					%>
						<input type="checkbox" onchange="fnChangeBranch(this);" style="margin-left : 7px;" name="cBranch" <%=((accessSelected.length > 0 && accessSelected[0].equals("--")) || accessSelected.length == 0) ? "checked=\"checked\"" : ""%> id="cBranch0" value="--"/><label for="cBranch0">ALL</label>
						<br>
					<% } %>
						<table width="100%">
						<%
							if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
						%>
						<tr>
							<td>
							<input type="checkbox" name="cBranch" checked="checked" id="cBranch" value="<%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId()%>"/><label for="cBranch"><%=((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessName()%></label>
							</td>
						</tr>
						<%
							} else {
								AccessPointModel[] accessPoints = request.getAttribute(Constant.ACCESS_POINTS) != null ?
														(AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS) : new AccessPointModel[0];
								ArrayList selected = new ArrayList(0);
								if(accessSelected.length > 0 && !accessSelected[0].equals("--")){
									for(int k=0; k<accessSelected.length;k++){
										selected.add((String)accessSelected[k]);	
									}
								}
								for(int i=0; i<accessPoints.length;i++){
									if(i%3==0){
										if(i!=0){
							%>
								</tr>
							<%
										}
							%>
								<tr>
							<%
									}
							%>
							<td>
							<input type="checkbox" onchange="fnChangeBranch(this);" name="cBranch" <%=((accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf(accessPoints[i].getAccessId()+"")!=-1) || accessSelected.length == 0)) ? "checked=\"checked\"" : ""%> id="cBranch<%=i+1%>" value="<%=accessPoints[i].getAccessId()%>"/><label for="cBranch<%=i+1%>"><%=accessPoints[i].getAccessName()%></label>
							</td>
							<%
								}
							}
						%>
						</table>
					</td>
				</tr>
				<tr><td align="right">Salesman<span style="color: red">*</span> :</td>
					<td>
						<%
						if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")){
						%>
						<select  name="sUser" style="width: 200px;">
							<option value="<%=((UserModel) request.getSession().getAttribute(Constant.USER)).getUserId()%>"><%=((UserModel) request.getSession().getAttribute(Constant.USER)).getUserName() %></option>
						</select>
						<%
						} else {
						%>
						<select  name="sUser" style="width: 200px;">
							<option value="--">-----</option>
							<%
								String userId = (request.getAttribute(Constant.USER) == null) ? "" : (String) request.getAttribute(Constant.USER); 
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" 
								<%=(userId.equals(users[i].getUserId())) ? "selected=\"selected\"" : ""%>><%=users[i].getUserName()%></option>
							<%
								}
							%>
						</select>
						<%
						} 
						%>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Details" onclick="fnGetSalesDtls();"/></td>
				</tr>
			</table>
			<%
				SalesModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (SalesModel[]) request.getAttribute(Constant.FORM_DATA) : new SalesModel[0];
				if(todayBills.length>0){
					HashMap<Integer, Double> totalOutstanding = (request.getAttribute("totalOutstandingList") != null) ? ((HashMap<Integer,Double>) request.getAttribute("totalOutstandingList")) : new HashMap<Integer, Double>(0);  
			%>
			<table width="100%" border="1" cellspacing="1" style="font-size: 11px;">
				<tr>
					<th colspan="10" align="center">Outstanding Bills</th>
				</tr>
				<tr>
					<th style="width: 60px;">BILL NO</th>
					<th style="width: 50px;">BILL DATE</th>
					<th style="width: 50px;">DUE DAYS</th>
					<th width="25%">CUSTOMER / LEDFER</th>
					<th style="width: 80px;">BILL<br/>AMOUNT</th>
					<th style="width: 80px;">PART PAYMENT</th>
					<th style="width: 80px;">PENDING AMOUNT</th>
					<th style="width: 80px;">TOTAL OUTSTANDING</th>
					
					<th>CALL FOLLOW UP REMARKS</th>
					<th style="width: 60px;">CALL ENTRY</th>
				</tr>
				<%
					double gTotal = 0;
					double roundOff = 0;
					double gPendingTotal = 0;
					double gPaidTotal = 0;
					String backColor = "";
					String fontColor = "";
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date redDate = new Date();
					redDate.setTime( redDate.getTime() + 2*1000*60*60*24 );
					Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
					for(int i=0; i<todayBills.length;i++){
						roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) ;
						gPendingTotal = gPendingTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt());
						gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff);
						gPaidTotal = gPaidTotal + Utils.get2DecimalDouble(todayBills[i].getPayAmt());
						if((formatter.parse((todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupDt() :todayBills[i].getDueDate())).getTime() < todayDate.getTime() ){
							backColor="#FF6A6A";
							fontColor="white";
						} else if((formatter.parse((todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupDt() :todayBills[i].getDueDate())).getTime() < redDate.getTime() ){
							backColor="#FFFF7E";
							fontColor="blue";
						} else {
							backColor="#D3BECF";
							fontColor="blue";
						}
						Date salesDate = formatter.parse(todayBills[i].getSalesdate());
						long days = (todayDate.getTime() - salesDate.getTime()) / (1000*60*60*24);
				%>
					<tr style="background-color: <%=backColor %>; color: <%=fontColor%>;">
						<td align="center" valign="top"><a title="View sales bill" style="color: #800517;" href="javascript: fnShowSales('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></a><br><input style="white-space:normal; font-size : 11px;" type="button" value="Ledger" onclick="fnLedger('<%=todayBills[i].getCustomer().getCustomerId()%>')"/></td>
						<td align="center" valign="top" style="color: black;"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
						<td align="center" valign="top"><%=(todayBills[i].getFollowupCnt() >0) ? Utils.convertToAppDateDDMMYY(todayBills[i].getFollowupDt()) : Utils.convertToAppDateDDMMYY(todayBills[i].getDueDate()) %><BR><%=days %> Days</td>
						<td><label style="color: black; padding-bottom: 2px;"><%=todayBills[i].getCustomer().getCustomerName()%></label><%=" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %> <%if(!todayBills[i].getCustomer().getCollectionPerson().getUserId().equals("")){ %><label style="color: red; background-color: yellow; margin-top: 2px; font-weight: bold;">COLLECTION TO BE DONE BY <%=todayBills[i].getCustomer().getCollectionPerson().getUserName()%></label> <%}%> </td>
						<td align="right" valign="top" style="font-weight: bold;"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) %></td>
						<td align="center" valign="top" style="font-weight: bold;"><%if(todayBills[i].getPayDate()!=null && !todayBills[i].getPayDate().equals("0000-00-00")){%><a title="Click to See Bill Payment Details" href="javascript:fnShowBillPayment('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');"><%=Utils.get2Decimal(todayBills[i].getPayAmt()) %><br/><%=(todayBills[i].getPayDate()!=null && !todayBills[i].getPayDate().equals("0000-00-00")) ? Utils.convertToAppDateDDMMYY(todayBills[i].getPayDate()) : "" %></a><%} else { %>--<%}%></td>
						<td align="right" valign="top" style="font-weight: bold; font-size: 13px;"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt()) %></td>
						<td align="right" valign="top" style="font-weight: bold; font-size: 13px;"><a title="Click to See the details of total outstanding" href="javascript: fnShowOutstandingDtls('<%=todayBills[i].getCustomer().getCustomerId()%>');"><%=Utils.get2Decimal(totalOutstanding.get(todayBills[i].getCustomer().getCustomerId())) %></a></td>
						<td align="left" valign="top" style="font-size: 12px;"><%=(todayBills[i].getFollowupCnt() >0) ? "<b>User : </b>"+todayBills[i].getFollowupUser()+"<br/>"+todayBills[i].getFollowupRemark(): "--" %></td>
						<td align="center" valign="top"><input type="button" style="white-space:normal; font-size : 11px;" title="Open to give follow up" value="Open" onclick="fnOpenFollowupScreen('<%=todayBills[i].getCustomer().getCustomerId()%>');"/><br/><%if(todayBills[i].getFollowupCnt() >0){%><a href="javascript: fnShowFollowupDtls('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');">Cnt : <%=(todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupCnt() : "--" %></a><%} else {%>Cnt : <%=(todayBills[i].getFollowupCnt() >0) ? todayBills[i].getFollowupCnt() : "--" %><%}%></td>
					</tr>
				<%
					}
				%>
				<tr>
					<th colspan="4" align="right">GRAND TOTAL(Rs.)</th>
					<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(gPaidTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(gPendingTotal) %></th>
					<th align="center">&nbsp;</th>
					<th align="center">&nbsp;</th>
					<th align="center">&nbsp;</th>
				</tr> 
			</table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
<%
	String branchStr = "";
	String[] branches = (String[])request.getAttribute("cBranch");
	for(int i=0; i<branches.length; i++){
		branchStr = branchStr + branches[i] + "|";
	}
%>
	<input type="hidden" name="branches" value="<%=branchStr%>"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="custId"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>