<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.SalesItemModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.EntryModel"%>
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
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Order Item Followup</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<%
				String collectedRatio = "0";
				
				HashMap<String, SalesItemModel> masterDtls = (request.getAttribute("MASTER_DTLS")!=null) ? (HashMap<String, SalesItemModel>) request.getAttribute("MASTER_DTLS") : new HashMap<String, SalesItemModel>(0);
				HashMap<String, ArrayList<EntryModel>> todayBills = (request.getAttribute(Constant.FORM_DATA)!=null) ? (HashMap<String, ArrayList<EntryModel>>) request.getAttribute(Constant.FORM_DATA) : new HashMap<String, ArrayList<EntryModel>>(0);
				HashMap<Integer, Double> current = (request.getAttribute(Constant.CURRENT)!=null) ? (HashMap<Integer, Double>) request.getAttribute(Constant.CURRENT) : new HashMap<Integer, Double>(0);
				HashMap<Integer, Double> requested = (request.getAttribute(Constant.REQUESTED)!=null) ? (HashMap<Integer, Double>) request.getAttribute(Constant.REQUESTED) : new HashMap<Integer, Double>(0);
				if(masterDtls.keySet().size()>0){
			%>
<!-- 			<table width="100%"><tr><td align="center">Collected Payment Ratio(%) : <label id="collectRatio" style="color: red; font-size: 20px;"></label></td></tr></table> -->
<!-- 			<table width="100%"><tr><td align="center"><input type="button" value="Print Report" onclick="fnPrintMasterRpt();"/>&nbsp;&nbsp;<input type="button" value="Export Report" onclick="fnExporMasterRpt();"/><input type="button" value="Item Report" onclick="fnItemMasterRpt();"/>&nbsp;&nbsp;</td></tr></table> -->
<!-- 				<table width="100%"><tr><td align="right"><img alt="export to xls" src="images/xls.gif;" style="padding: 2px;" onmouseover="this.style.background='gray';" onmouseout="this.style.background='transparent';"/><span style="padding: 2px; vertical-align: " onmouseover="this.style.background='gray';" onmouseout="this.style.background='transparent';"><img alt="export to xls" src="images/pdf.gif;"/>PDF</span></td></tr></table> -->
				<%
					String key;
					SalesItemModel item = null;
					Iterator<String> itr = masterDtls.keySet().iterator();
					EntryModel[] model = null;
					while(itr.hasNext()){
						key = itr.next();
						item = masterDtls.get(key);
				%>
				<table width="100%" border="1" cellspacing="2" style="font-size: 11px; margin-top: 10px;">
					<tr style="font-size: 13px;">
						<th colspan="2">ITEM ID -ITEM NAME - ITEM NUMBER</th>
						<th style="width: 60px;">ORDERED</th>
						<th style="width: 60px;">SENT</th>
						<th style="width: 60px;">STOCK</th>
						<th style="width: 60px;">REQUESTED</th>
						<th style="width: 60px;">REQUIRED</th>
						<th style="width: 60px;">REQUEST</th>
					</tr>
					<tr style="background-color: <%=(item.getQty() - item.getSentQty() - ((current.get(item.getItem().getItemId()) != null) ? current.get(item.getItem().getItemId()) : 0) - ((requested.get(item.getItem().getItemId()) != null) ? requested.get(item.getItem().getItemId()) : 0) > 0) ? "red" : "black"%>; color: white;font-size: 13px;">
						<th colspan="2"><%=item.getItem().getItemId()%> - <%=item.getItem().getItemName() + " - " +item.getItem().getItemNumber()%></th>
						<th style="width: 60px;"><%=item.getQty()%></th>
						<th style="width: 60px;"><%=item.getSentQty()%></th>
						<th style="width: 60px;"><%=(current.get(item.getItem().getItemId()) != null) ? current.get(item.getItem().getItemId()) : 0%></th>
						<th style="width: 60px;"><%=(requested.get(item.getItem().getItemId()) != null) ? requested.get(item.getItem().getItemId()) : 0%></th>
						<th style="width: 60px;"><%=item.getQty() - item.getSentQty() - ((current.get(item.getItem().getItemId()) != null) ? current.get(item.getItem().getItemId()) : 0) - ((requested.get(item.getItem().getItemId()) != null) ? requested.get(item.getItem().getItemId()) : 0)%></th>
						<th style="width: 60px;"><input type="hidden" name="number<%=item.getItem().getItemId()%>" value="<%=item.getItem().getItemNumber()%>"/><input <%=(item.getQty() - item.getSentQty() - ((current.get(item.getItem().getItemId()) != null) ? current.get(item.getItem().getItemId()) : 0) - ((requested.get(item.getItem().getItemId()) != null) ? requested.get(item.getItem().getItemId()) : 0) < 1) ? "" : "" %> type="checkbox" name="items" value="<%=item.getItem().getItemId() %>" onclick="fnChangeCombo(<%=item.getItem().getItemId() %>, this);" /> - <input disabled="disabled" type="text" style="width: 50px; text-align: center;" value="<%=item.getQty() - item.getSentQty() - ((current.get(item.getItem().getItemId()) != null) ? current.get(item.getItem().getItemId()) : 0) - ((requested.get(item.getItem().getItemId()) != null) ? requested.get(item.getItem().getItemId()) : 0)%>" name="txt<%=item.getItem().getItemId()%>" id="txt<%=item.getItem().getItemId()%>"></th>
					</tr>
					<tr>
						<th style="width: 60px;">ORDER NO / REQUEST NO</th>
						<th style="width: 50px;">ORDER DATE / REQUEST DATE</th>
						<th colspan="3">CUSTOMER / BRANCH</th>
						<th style="width: 60px;">ORDERED <BR/>QUANTITY</th>
						<th style="width: 60px;">SENT <BR/>QUANTITY</th>
						<th style="width: 60px;">PENDING <BR/>QUANTITY</th>
					</tr>
				<%
						model = (EntryModel[])todayBills.get(key).toArray(new EntryModel[0]);
						for(int i=0; i<model.length; i++){
				%>
					<tr style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white" %>; color: blue;">
						<td align="center" valign="top"><a style="color: #800517;" href="<%if(model[i].getBranch().getBillPrefix().length() == 4){ %>javascript: fnGetLinkSalesDtls('<%=model[i].getBranch().getBillPrefix()+Utils.padBillNo(model[i].getEntryId()) %>');<%} else {%>javascript: fnGetLinkRequestDtls('<%=model[i].getBranch().getBillPrefix()+Utils.padBillNo(model[i].getEntryId()) %>');<%} %>"><%=model[i].getBranch().getBillPrefix()+Utils.padBillNo(model[i].getEntryId()) %></a></td>
						<td align="center" valign="top"><%=Utils.convertToAppDateDDMMYY(model[i].getEntryDate()) %></td>
						<td colspan="3"><%=(model[i].getBranch().getBillPrefix().length() == 4) ? (model[i].getCustomer().getCustomerName()+" - "+model[i].getCustomer().getArea()+" - "+model[i].getCustomer().getCity()) : (model[i].getBranch().getAccessName() +" - "+model[i].getBranch().getCity())%></td>
						<td align="center" valign="top"><%=model[i].getQty()%></td>
						<td align="center" valign="top"><%=model[i].getSentQty()%></td>
						<td align="center" valign="top"><%=model[i].getQty() - model[i].getSentQty()%></td>
					</tr>
				<%
						}
				%>
				</table>
				<%
					}
				%>
			<table width="100%"><tr><td align="center"><input type="button" value="Request" onclick="fnRequest();"/></td></tr></table>
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
<script type="text/javascript">
	
	function fnRequest(){
		var iCnt = document.forms[0].items.length;
		var selected = false;
		if(iCnt == 1){
			if(document.forms[0].items.checked)
				selected = true;
		} else {
			if(iCnt == 1){
				if(document.forms[0].items.checked)
					selected = true;
			} else {
				for(var i=0; i<iCnt; i++){
					if(document.forms[0].items[i].checked){
						selected = true;
						break;
					}
				}
			}
		}
		if(selected){
			document.forms[0].HANDLERS.value = "ORDER_HANDLER";
			document.forms[0].ACTIONS.value = "ORDER_REQUEST";
			fnFinalSubmit();
		} else {
			alert('Please select the items for request.');
			return;
		}
	}

	function fnGetLinkSalesDtls(qId){
		window.open('/en/app?HANDLERS=ORDER_HANDLER&ACTIONS=SHOW_ORDER&txtInvoiceNo='+qId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
	}
	
	function fnGetLinkRequestDtls(qId){
		window.open('/en/app?HANDLERS=TRANSFER_REQUEST_HANDLER&ACTIONS=SHOW_TRANSFER_REQUEST&txtInvoiceNo='+qId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
	}
	
	function fnChangeCombo(id, combo){
		document.getElementById("txt"+id).disabled = (combo.checked ? false : true); 
		if(combo.checked) 
			document.getElementById("txt"+id).focus();
	}
</script>
</html>