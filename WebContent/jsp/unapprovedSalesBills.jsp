<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
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
<script type="text/javascript">
function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnCheckSales(salesid){
	document.forms[0].txtInvoiceNo.value = salesid;
	document.forms[0].HANDLERS.value = "APPROVAL_HANDLER";
	document.forms[0].ACTIONS.value = "GET_SALES";
	fnFinalSubmit();
}
</script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Un-Approved Sales Bills</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<%
				HashMap<String, SalesModel[]> dtls = (request.getAttribute(Constant.FORM_DATA)!=null) ? (HashMap<String, SalesModel[]>) request.getAttribute(Constant.FORM_DATA) : new HashMap<String, SalesModel[]>(0);
				if(dtls.keySet().size()>0){
					Iterator<String> keys = dtls.keySet().iterator();
					String temp = null;
					while(keys.hasNext()){
						temp = keys.next();
						SalesModel[] todayBills = dtls.get(temp);
						if(todayBills.length>0){
			%>
			<table width="100%" border="1">
				<tr>
					<th colspan="4" align="center"><%=temp.toUpperCase()%> Bill</th>
				</tr>
				<tr>
					<th style="width: 120px;">BILL NO</th>
					<th>CUSTOMER</th>
					<th style="width: 120px;">TOTAL AMOUNT</th>
					<th>ACTION</th>
				</tr>
				<%
							double gTotal = 0;
							double roundOff = 0;
							for(int i=0; i<todayBills.length;i++){
								roundOff = (1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1) < 0.5) ? Utils.get2DecimalDouble(1-Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) : - Utils.get2DecimalDouble(Utils.get2DecimalDouble((todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess())%1)) ;
								gTotal = gTotal + Utils.get2DecimalDouble(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff);
				%>
					<tr>
						<td align="center"><%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %></td>
						<td><%=todayBills[i].getCustomer().getCustomerName()+" - "+todayBills[i].getCustomer().getArea()+" - "+todayBills[i].getCustomer().getCity() %></td>
						<td align="right">Rs.<%=Utils.get2Decimal(todayBills[i].getTotalAmt()+todayBills[i].getInstallation()+todayBills[i].getPacking()+todayBills[i].getForwarding()-+todayBills[i].getLess()+roundOff) %></td>
						<td align="center"><input type="button" value="Check & Approve" onclick="fnCheckSales('<%=todayBills[i].getBranch().getBillPrefix()+Utils.padBillNo(todayBills[i].getSaleid()) %>');"/></td>
					</tr>
				<%
							}
				%>
				<tr>
					<th colspan="2" align="right">GRAND TOTAL</th>
					<th align="right"> Rs. <%=Utils.get2Decimal(gTotal) %></th>
					<th>&nbsp;</th>
				</tr> 
			</table>
			<%
						}
					}
				} else {
			%>
			<table width="100%"><tr><td align="center">No Data Existed.</td></tr></table>
			<%
				}
			%>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtInvoiceNo" value=""/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>