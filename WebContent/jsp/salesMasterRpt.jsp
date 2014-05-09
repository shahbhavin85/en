<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
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
<script type="text/javascript" src="js/salesRpt.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Sales Master Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Customer<span style="color: red">*</span> :</td>
					<td>
						<input type="text" name="txtCust" autocomplete="off" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerName()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getArea()+" - "+((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCity()	 : ""%>" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: <%=(request.getAttribute(Constant.CUSTOMER) != null) ? "visible" : "hidden"%>;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div>
					</td>
<!-- 					<td align="right">Customer<span style="color: red">*</span> :</td> -->
<!-- 					<td><select name="sCustomer"> -->
<!-- 							<option value="--">-----</option> -->
<%-- 							<% --%>
<!-- 								String state = ""; -->
<!-- 								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ? -->
<!-- 														(CustomerModel[])request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0]; -->
<!-- 								CustomerModel temp = null; -->
<!-- 									for(int i=0; i<customers.length;i++){ -->
<!-- 										temp=customers[i]; -->
										
<!-- 							%> -->
<%-- 							<option value="<%=temp.getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute("sCustomer") != null && ((String)request.getAttribute("sCustomer")).equals(temp.getCustomerId()+"")) ? "selected=\"selected\"" : "" %>><%=temp.getCustomerName()%> - <%=temp.getArea()%></option> --%>
<%-- 							<% --%>
<!-- 								} -->
<!-- 							%> -->
<!-- 						</select> -->
<!-- 					</td> -->
				</tr>
				<tr>
					<td align="right">Salesman<span style="color: red">*</span> :</td>
					<td><select  name="sUser" style="width: 200px;">
					<%
						if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == 1){

					%>
							<option value="<%=((UserModel) request.getSession().getAttribute(Constant.USER)).getUserId()%>"><%=((UserModel) request.getSession().getAttribute(Constant.USER)).getUserName()%></option>
					<%
						} else {
					%>
							<option value="--">-----</option>
							<%
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute("sUser") != null && ((String)request.getAttribute("sUser")).equals(users[i].getUserId())) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
							<%
								}
						}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td align="right" valign="top">Branch<span style="color: red">*</span> :</td>
					<td>
					<%
						ArrayList selected = new ArrayList(0);
						String[] accessSelected = (request.getAttribute("cBranch") != null) ? ((String[])request.getAttribute("cBranch")) : new String[0];
						if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
					%>
						<input type="checkbox" onchange="fnChangeBranch(this);" style="margin-left : 7px;" name="cBranch" <%=((accessSelected.length > 0 && accessSelected[0].equals("--")) || accessSelected.length == 0) ? "checked=\"checked\"" : ""%> id="cBranch0" value="--"/><label for="cBranch0">ALL</label>
						<br>
					<% } %>
						<table width="100%">
						<%
							if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
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
				<tr>
					<td align="right">Bill Type<span style="color: red">*</span> :</td>
					<td>
					<%
						selected = new ArrayList(0);
						String[] billTypeSelected = (request.getAttribute("sBillType") != null) ? ((String[])request.getAttribute("sBillType")) : new String[0];
						for(int k=0; k<billTypeSelected.length;k++){
							selected.add((String)billTypeSelected[k]);	
						}
					%>
					<table>
						<tr>
							<td>
							<input type="checkbox" onchange="fnChangeType(this);" name="sBillType" <%=(billTypeSelected.length == 0 || (billTypeSelected.length > 0 && selected.indexOf("1")!=-1)) ? "checked=\"checked\"" : ""%> id="sBillType1" value="1"/><label for="sBillType1">VAT BILL</label>
							</td>
							<td>
							<input type="checkbox" onchange="fnChangeType(this);" name="sBillType" <%=(billTypeSelected.length == 0 || (billTypeSelected.length > 0 && selected.indexOf("2")!=-1)) ? "checked=\"checked\"" : ""%> id="sBillType2" value="2"/><label for="sBillType2">CST BILL</label>
							</td>
							<td>
							<input type="checkbox" onchange="fnChangeType(this);" name="sBillType" <%=(billTypeSelected.length == 0 || (billTypeSelected.length > 0 && selected.indexOf("3")!=-1)) ? "checked=\"checked\"" : ""%> id="sBillType3" value="3"/><label for="sBillType3">CST AGAINST FORM 'C' BILL</label>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td align="right">Paymode<span style="color: red">*</span> :</td>
					<td>
					<%
						selected = new ArrayList(0);
						String[] payModeSelected = (request.getAttribute("sPayMode") != null) ? ((String[])request.getAttribute("sPayMode")) : new String[0];
						for(int k=0; k<payModeSelected.length;k++){
							selected.add((String)payModeSelected[k]);	
						}
					%>
					<table>
						<tr>
							<td>
							<input type="checkbox" onchange="fnChangeMode(this);" name="sPayMode" <%=(payModeSelected.length == 0 || (payModeSelected.length > 0 && selected.indexOf("1")!=-1)) ? "checked=\"checked\"" : ""%> id="sPayMode1" value="1"/><label for="sPayMode1">CASH</label>
							</td>
							<td>
							<input type="checkbox" onchange="fnChangeMode(this);" name="sPayMode" <%=(payModeSelected.length == 0 || (payModeSelected.length > 0 && selected.indexOf("2")!=-1)) ? "checked=\"checked\"" : ""%> id="sPayMode2" value="2"/><label for="sPayMode2">CC CARD</label>
							</td>
							<td>
							<input type="checkbox" onchange="fnChangeMode(this);" name="sPayMode" <%=(payModeSelected.length == 0 || (payModeSelected.length > 0 && selected.indexOf("3")!=-1)) ? "checked=\"checked\"" : ""%> id="sPayMode3" value="3"/><label for="sPayMode3">CREDIT</label>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td align="right">Bill From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         Bill To Date<span style="color: red">*</span> :
					<input name="txtToDate" id="txtToDate" style="width: 195px;" onclick="scwShow(scwID('txtToDate'),event);" 
					value="<%=(request.getAttribute("txtToDate") != null && !((String)request.getAttribute("txtToDate")).equals("")) ?  (String)request.getAttribute("txtToDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtToDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Details" onclick="fnGetMasterRpt();"/></td>
				</tr>
			</table>
			<%
				String collectedRatio = "0";
				SalesModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (SalesModel[]) request.getAttribute(Constant.FORM_DATA) : new SalesModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%"><tr><td align="center">Collected Payment Ratio(%) : <label id="collectRatio" style="color: red; font-size: 20px;"></label></td></tr></table>
			<table width="100%"><tr><td align="center"><input type="button" value="Print Report" onclick="fnPrintMasterRpt();"/>&nbsp;&nbsp;<input type="button" value="Export Report" onclick="fnExporMasterRpt();"/></td></tr></table>
<!-- 				<table width="100%"><tr><td align="right"><img alt="export to xls" src="images/xls.gif;" style="padding: 2px;" onmouseover="this.style.background='gray';" onmouseout="this.style.background='transparent';"/><span style="padding: 2px; vertical-align: " onmouseover="this.style.background='gray';" onmouseout="this.style.background='transparent';"><img alt="export to xls" src="images/pdf.gif;"/>PDF</span></td></tr></table> -->
			<table width="100%" border="1" cellspacing="2" style="font-size: 11px;">
				<tr>
					<th colspan="12" align="center">Sales Master Details</th>
				</tr>
				<tr>
					<th style="width: 60px;">BILL NO</th>
					<th style="width: 50px;">BILL DATE</th>
					<th style="width: 50px;">COLL.<br/> DAYS</th>
					<th style="width: 80px;">TAX TYPE</th>
					<th style="width: 50px;">PAYMODE</th>
					<th style="width: 50px;">DUE DATE</th>
					<th style="width: 80px;">SALESMAN</th>
					<th>CUSTOMER</th>
					<th style="width: 60px;">TOTAL AMOUNT</th>
					<th style="width: 50px;">PAY. DATE</th>
					<th style="width: 60px;">PAY. AMOUNT</th>
					<th style="width: 60px;">PENDING AMOUNT</th>
				</tr>
				<%
					double gTotal = 0;
					double pendintTotal = 0;
					double paidTotal = 0;
					double roundOff = 0;
					String taxBackColor = "";
					String payBackColor = "";
					String fontColor = "";
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
					Date tempDate = null;
					Date tempPaidDate = null;
					for(int i=0; i<todayBills.length;i++){
						if(todayBills[i].getStatus() == 1){
					%>
					<tr style="background-color: black; color: white;">
						<td align="center" valign="top"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
						<td align="center" valign="top">--</td>
						<td align="center" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
						<td align="center" valign="top"><%=(todayBills[i].getPaymode() == 1) ? "CASH" : (todayBills[i].getPaymode() == 2) ? "CC CARD" : "CREDIT"%></td>
						<td align="center" valign="top">--</td>
						<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
						<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="center" valign="top" colspan="4">CANCELLED</td>
					</tr>
				<%
						} else {
							roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) ;
							gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff);
							pendintTotal = pendintTotal + ((todayBills[i].getPaymode() == 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-Utils.get2DecimalDouble(todayBills[i].getPayAmt())) : 0);
							paidTotal = paidTotal + ((todayBills[i].getPaymode() != 3) ? Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) : Utils.get2DecimalDouble(todayBills[i].getPayAmt()));
							fontColor="blue";
							taxBackColor = (todayBills[i].getTaxtype() == 1) ? "#FFC1C1" : (todayBills[i].getTaxtype() == 2) ? "#FFFF7E" : "#BCED91";
							payBackColor = (todayBills[i].getPaymode() == 1) ? "#FEF1B5" : (todayBills[i].getPaymode() == 2) ? "#d8bfd8" : "#FF8C69";
							tempDate = (formatter.parse(Utils.convertToSQLDate(todayBills[i].getSalesdate())));
							if(todayBills[i].getPayDate() != null)
								tempPaidDate = (formatter.parse(Utils.convertToSQLDate(todayBills[i].getPayDate())));
				%>
					<tr style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white" %>; color: <%=fontColor%>;">
						<td align="center" valign="top"><a style="color: #800517;" href="javascript: fnShowSales('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></a></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) %></td>
						<td align="center" valign="top"><%=(todayBills[i].getPaymode() == 3) ? (((Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt())) != 0)) ? ((todayDate.getTime() - tempDate.getTime()) / (1000*60*60*24))+" Days" : ((tempPaidDate.getTime() - tempDate.getTime()) / (1000*60*60*24))+" Days") : "--" %></td>
						<td align="center" style="background-color: <%=taxBackColor %>;" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
						<td align="center" style="background-color: <%=payBackColor %>;" valign="top"><%=(todayBills[i].getPaymode() == 1) ? "CASH" : (todayBills[i].getPaymode() == 2) ? "CC CARD" : "CREDIT"%></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getDueDate()) %></td>
						<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
						<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) %></td>
						<td align="center" valign="top"><%=(todayBills[i].getPaymode() != 3) ? Utils.convertToAppDateDDMMYY(todayBills[i].getSalesdate()) : (todayBills[i].getPayDate()!=null && !todayBills[i].getPayDate().equals("0000-00-00")) ? Utils.convertToAppDateDDMMYY(todayBills[i].getPayDate()) : ""  %></td>
						<td align="right" valign="top"><%=(todayBills[i].getPaymode() != 3) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff) : ((todayBills[i].getPayAmt() != 0) ? "<a title=\"Click to See Bill Payment Details\" href=\"javascript:fnShowBillPayment('"+todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid())+"');\">"+Utils.get2Decimal(todayBills[i].getPayAmt())+"</a>" : "") %></td>
						<td align="right" valign="top"><%=(todayBills[i].getPaymode() == 3 && (Double.parseDouble(Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt())) != 0)) ? Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-todayBills[i].getPayAmt()) : "--"%></td>
					</tr>
				<%
								}
						}
						collectedRatio = Utils.get2Decimal((paidTotal/gTotal)*100);
				%>
				<tr>
					<th colspan="8" align="right">GRAND TOTAL(Rs.)</th>
					<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
					<th align="center" valign="top">--</th>
					<th align="right"><%=Utils.get2Decimal(paidTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(pendintTotal) %></th>
				</tr> 
			</table>
			<table width="100%"><tr><td align="center"><input type="button" value="Print Report" onclick="fnPrintMasterRpt();"/>&nbsp;&nbsp;<input type="button" value="Export Report" onclick="fnExporMasterRpt();"/></td></tr></table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="sCustomer" value="<%=(request.getAttribute(Constant.CUSTOMER) != null) ? ((CustomerModel)request.getAttribute(Constant.CUSTOMER)).getCustomerId() : ""%>">
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
document.getElementById("collectRatio").innerHTML = '<%=collectedRatio%>%';
</script>
</html>