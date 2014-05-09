<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.OrderModel"%>
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
<script type="text/javascript" src="js/orderRpt.js"></script>
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Customer Order Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<%
				HashMap<String, ArrayList<OrderModel>> result = (request.getAttribute(Constant.FORM_DATA) != null) ? (HashMap<String, ArrayList<OrderModel>>) request.getAttribute(Constant.FORM_DATA) : new HashMap<String, ArrayList<OrderModel>>(0);
				if(result.keySet().size() > 0){
					ArrayList<String> lst = new ArrayList<String>(0);
					lst.addAll(result.keySet());
					Collections.sort(lst);
					Iterator<String> itr = lst.iterator();
					while(itr.hasNext()){
						OrderModel[] todayBills = (OrderModel[]) result.get(itr.next()).toArray(new OrderModel[0]);
						if(todayBills.length>0){
			%>
			<table width="100%" border="1" cellspacing="2" style="font-size: 11px; margin-bottom: 15px;">
				<tr style="background-color: black; color: white;">
					<th colspan="1" valign="top" align="right">CUSTOMER</th>
					<th colspan="7" align="left"><%=todayBills[0].getCustomer().getLabel()%></th>
				</tr>
				<tr>
					<th style="width: 60px;">ORDER NO</th>
					<th style="width: 50px;">ORDER DATE</th>
					<th style="width: 80px;">TAX TYPE</th>
					<th style="width: 80px;">SALESMAN</th>
					<th style="width: 60px;">EXHIBITION</th>
					<th style="width: 60px;">TOTAL AMOUNT</th>
					<th style="width: 60px;">ADVANCE AMOUNT</th>
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
									roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1) < 0.51) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())%1)) ;
									gTotal = gTotal + Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff);
									pendintTotal = pendintTotal + (Utils.get2DecimalDouble(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())+roundOff-Utils.get2DecimalDouble(todayBills[i].getAdvance())));
									paidTotal = paidTotal + (Utils.get2DecimalDouble(todayBills[i].getAdvance()));
									fontColor="blue";
									taxBackColor = (todayBills[i].getTaxtype() == 1) ? "#FFC1C1" : (todayBills[i].getTaxtype() == 2) ? "#FFFF7E" : "#BCED91";
				%>
					<tr style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white" %>; color: <%=fontColor%>;">
						<td align="center" valign="top"><a style="color: #800517;" href="javascript: fnShowSales('<%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %>');"><%=todayBills[i].getBranch().getBillPrefix()+"O"+Utils.padBillNo(todayBills[i].getOrderId()) %></a></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(todayBills[i].getOrderDate()) %></td>
						<td align="center" style="background-color: <%=taxBackColor %>;" valign="top"><%=(todayBills[i].getTaxtype() == 1) ? "VAT" : (todayBills[i].getTaxtype() == 2) ? "CST" : "CST AGAINST FORM 'C'" %></td>
						<td align="center" valign="top"><%=todayBills[i].getSalesman().getUserName()%></td>
						<td align="center" valign="top"><%=(todayBills[i].getFromEx() == 1) ? "YES" : "NO"%></td>
						<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())) %></td>
						<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getAdvance())) %></td>
						<td align="right" valign="top"><%=Utils.get2Decimal(Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-todayBills[i].getLess())-todayBills[i].getAdvance()) %></td>
					</tr>
				<%
							}
				%>
				<tr>
					<th colspan="5" align="right">GRAND TOTAL(Rs.)</th>
					<th align="right"><%=Utils.get2Decimal(gTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(paidTotal) %></th>
					<th align="right"><%=Utils.get2Decimal(pendintTotal) %></th>
				</tr> 
			</table>
			<%
						}
					}
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
</script>
</html>