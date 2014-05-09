<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.PurchaseReturnModel"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.PurchaseReturnModel"%>
<%@page import="com.en.util.DateUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript" src="js/purchaseReturnRpt.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Purchase Master Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Supplier<span style="color: red">*</span> :</td>
					<td><select name="sCustomer">
							<option value="--">-----</option>
							<%
								String state = "";
								CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ?
														(CustomerModel[])request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0];
								CustomerModel temp = null;
									for(int i=0; i<customers.length;i++){
										temp=customers[i];
										
							%>
							<option value="<%=temp.getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>" <%=(request.getAttribute("sCustomer") != null && ((String)request.getAttribute("sCustomer")).equals(temp.getCustomerId()+"")) ? "selected=\"selected\"" : "" %>><%=temp.getCustomerName()%> - <%=temp.getArea()%></option>
							<%
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
						if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
					%>
						<input type="checkbox" onchange="fnChangeBranch(this);" style="margin-left : 7px;" name="cBranch" <%=((accessSelected.length > 0 && accessSelected[0].equals("--")) || accessSelected.length == 0) ? "checked=\"checked\"" : ""%> id="cBranch0" value="--"/><label for="cBranch0">ALL</label>
						<br>
					<% } %>
						<table width="100%">
						<%
							if(!request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
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
					<td align="right">Received From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         Received To Date<span style="color: red">*</span> :
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
				PurchaseReturnModel[] todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (PurchaseReturnModel[]) request.getAttribute(Constant.FORM_DATA) : new PurchaseReturnModel[0];
				if(todayBills.length>0){
			%>
			<table width="100%"><tr><td align="center"><input type="button" value="Print Report" onclick="fnPrintMasterRpt();"/>&nbsp;&nbsp;<input type="button" value="Export Report" onclick="fnExporMasterRpt();"/></td></tr></table>
			<table width="100%" border="1" cellspacing="2" style="font-size: 11px;">
				<tr>
					<th colspan="10" align="center">Purchase Master Details</th>
				</tr>
				<tr>
					<th style="width: 60px;">RETURN ID</th>
					<th style="width: 50px;">RETURN DATE</th>
					<th style="width: 80px;">TAX TYPE</th>
					<th>SUPPLIER</th>
					<th style="width: 60px;">TOTAL AMOUNT</th>
				</tr>
				<%
					double gTotal = 0;
					String taxBackColor = "";
					String fontColor = "";
					double pendintTotal = 0;
					double paidTotal = 0;
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date redDate = new Date();
					redDate.setTime( redDate.getTime() + 2*1000*60*60*24 );
					Date todayDate = (formatter.parse(Utils.convertToSQLDate(DateUtil.getCurrDt())));
					for(int i=0; i<todayBills.length;i++){
						gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()));
						fontColor="blue";
				%>
					<tr style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white" %>; color: <%=fontColor%>;">
						<td align="center" valign="top"><a style="color: #800517;" href="javascript: fnShowPurchase('<%=todayBills[i].getBranch().getBillPrefix()+"PR"+Utils.padBillNo(todayBills[i].getPurchaseId()) %>');"><%=todayBills[i].getBranch().getBillPrefix()+"PR"+Utils.padBillNo(todayBills[i].getPurchaseId()) %></a></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getReturnDt()) %></td>
						<td align="center" style="background-color: <%=taxBackColor %>;" valign="top"><%=(todayBills[i].getBillType() == 1) ? "VAT" : (todayBills[i].getBillType() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
						<td><%=todayBills[i].getSupplier().getCustomerName()+" - "+todayBills[i].getSupplier().getArea()+" - "+todayBills[i].getSupplier().getCity() %></td>
						<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt())) %></td>
					</tr>
				<%
					}
				%>
				<tr>
					<th colspan="4" align="right">GRAND TOTAL(Rs.)</th>
					<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
				</tr> 
			</table>
			<table width="100%"><tr><td align="center"><input type="button" value="Print Report" onclick="fnPrintMasterRpt();"/>&nbsp;&nbsp;<input type="button" value="Export Report" onclick="fnExporMasterRpt();"/></td></tr></table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input name="txtInvoiceNo" type="hidden" style="width: 195px;">
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>