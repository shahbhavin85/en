<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.SalesmanDlyItmModel"%>
<%@page import="com.en.model.SalesmanDlyRptModel"%>
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
<script type="text/javascript" src="js/salesmanDlyRpt.js"></script>
<script type="text/javascript" src="js/timepicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Salesman Daily Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" style="margin-left: 20px;">
				<tr>
					<td align="right">Report Date<span style="color: red">*</span> :</td><td colspan="3"><input name="txtRptDate" id="txtRptDate" style="width: 195px;" readonly="readonly" 
					value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptDt() : ""%>"></td>
				</tr>
				<tr>
				    <td align="right">In Time :</td><td><input id='inTime' name="txtInTime" readonly="readonly" type='text' value='' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime1,'inTime')" onclick="selectTime(document.forms[0].inTime1,'inTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime1" ALT="Pick a Time!" ONCLICK="selectTime(this,'inTime')" STYLE="cursor:hand;visibility: hidden;"></td>
				    
				    
				    <td>Out Time :</td><td><input id='outTime' type='text' name="txtOutTime" readonly="readonly" value='' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime2,'outTime')" onclick="selectTime(document.forms[0].inTime2,'outTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime2" ALT="Pick a Time!" ONCLICK="selectTime(this,'outTime')" STYLE="cursor:hand;visibility: hidden;"></td>
				</tr>
				<tr>
					<td align="right"> Type<span style="color: red">*</span> :</td><td colspan="3"><input type="radio" onchange="fnTypeChange(this);" name="rType" id="rType1" value="0" checked="checked"/><label for="rType1">Met Customer</label>&nbsp;&nbsp;&nbsp;<input type="radio" onchange="fnTypeChange(this);" name="rType" id="rType2" value="1"/><label for="rType2">Others</label> </td>
				</tr>
				<tr>
					<td align="right"> Customer<span style="color: red">*</span> :</td>
					<td colspan="3">
						<input type="text" name="txtCust" autocomplete="off" value="" onkeyup="fnGetCustomerList(this);" style="width: 500px;"/><img id="img" src="images/cross.gif" onclick="fnClearCustomer();" style="visibility: hidden;"/>
						<div id="list" class="suggestionsBox">
							<div class="suggestionList" id="autoSuggestionsList"></div>
						</div><input type="hidden" name="sCustomer" value="">&nbsp;&nbsp;
						<input type="button" name="Add_Customer" onclick="fnAddCustomer();" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Add Customer"/></td>
				</tr>
				<tr>
					<td align="right">Lodging :</td>
					<td><input type="text" name="txtLodging" value="0"/></td>
					<td align="right">Food :</td>
					<td><input type="text" name="txtFood" value="0"/></td>
				</tr>
				<tr>
					<td align="right">Rail / Bus :</td>
					<td><input type="text" name="txtRailBus" value="0"/></td>
					<td align="right">Local Transport :</td>
					<td><input type="text" name="txtLocalTransport" value="0"/></td>
				</tr>
				<tr>
					<td align="right">Courier :</td>
					<td><input type="text" name="txtCourier" value="0"/></td>
					<td align="right">Others :</td>
					<td><input type="text" name="txtOthers" value="0"/></td>
				</tr>
				<tr>
					<td align="right">Remark :</td>
					<td colspan="3"><textarea  name="taRemark" style="width: 195px; height : 50px;" maxlength="250"></textarea></td>
				</tr>
				<tr>
					<td colspan="4" align="center"><input type="button" value="Save" onclick="fnAddItm();"/><input type="reset" value="Reset" onclick="fnReset();"/><input type="button" value="New Order" onclick="fnGoToPage('SALESMAN_ORDER_HANDLER','');"/><input type="button" value="New Quotation" onclick="fnGoToPage('SALESMAN_QUOTATION_HANDLER','');"/></td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td align="left"><label style="font-size: 16px; font-weight: bold;"><u>Daily Report</u></label></td>
					<td align="right" colspan="2"><label style="font-size: 16px; font-weight: bold;"><u>Status</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getStatus() : ""%></label></td>
				</tr>
				<tr style="background-color: yellow;">
					<td align="center" colspan="3"><label style="font-size: 13px; font-weight: bold;">Order : </label><label style="font-size: 16px; font-weight: bold; color: red;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getOrders() : "--" %></label>&nbsp;&nbsp;&nbsp;&nbsp;||&nbsp;&nbsp;&nbsp;&nbsp; 
						<label style="font-size: 13px; font-weight: bold;">Quotation : </label><label style="font-size: 16px; font-weight: bold; color: red;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getQuotation() : "--" %></label>&nbsp;&nbsp;&nbsp;&nbsp;||&nbsp;&nbsp;&nbsp;&nbsp; 
						<label style="font-size: 13px; font-weight: bold;">Expense : </label><label style="font-size: 16px; font-weight: bold; color: red;" id="lblExpense"></label></td>
				</tr>
				<tr>
					<td colspan="3"><label style="font-size: 16px; font-weight: bold;"><u>Remark</u></label>:<label style="font-size: 16px;"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRemark() : ""%></label></td>
				</tr>
			</table>
			<table width="100%" cellspacing="0" border="1" style="margin-top: 10px;">
				<tr>
					<th style="width: 80px;">IN TIME</th>
					<th style="width: 80px;">OUT TIME</th>
					<th width="30%">DESCRIPTION</th>
					<th style="width: 80px;">LODGING</th>
					<th style="width: 80px;">FOOD</th>
					<th style="width: 80px;">RAIL/BUS</th>
					<th style="width: 80px;">LOCAL TRANSPORT</th>
					<th style="width: 80px;">COURIER</th>
					<th style="width: 80px;">OTHERS</th>
					<th width="20%">REMARKS</th>
					<th style="width: 100px;">ACTIONS</th>
				</tr>
				<%
					double expense = 0;
					if(request.getAttribute(Constant.FORM_DATA) != null){
						SalesmanDlyItmModel[] items = (SalesmanDlyItmModel[])((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getDtls().toArray(new SalesmanDlyItmModel[0]);
						for(int i=0; i<items.length; i++){
				%>
					<tr>
						<td align="center"><%=items[i].getAppInTime()%></td>
						<td align="center"><%=items[i].getAppOutTime()%></td>
				<%
							if(items[i].getType().equals("0")){
				%>
						<td>Meeting with <%=items[i].getCustomer().getCustomerName()+" - "+items[i].getCustomer().getArea()+" - "+items[i].getCustomer().getCity() %></td>
				<%
							} else {
				%>
						<td>Others</td>
				<%
							}
				%>
						<td align="center"><%=Utils.get2Decimal(items[i].getLodging()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getFood()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getRailbus()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getLocaltransport()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getCourier()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getOthers()) %></td>
						<td><%=(items[i].getRemark() != null) ? items[i].getRemark() : "--" %></td>
						<td align="center"><input type="button" <%=(items[i].getEditable().equals("N")) ? "disabled=\"disabled\"" : ""%> value="Delete" onclick="fnDeleteItem(<%=items[i].getIndex()%>);"/></td>
					</tr>
				<%
							expense = expense + items[i].getLodging() + items[i].getFood() + items[i].getRailbus() + items[i].getLocaltransport() + items[i].getCourier() + items[i].getOthers();
						}
						
						if(items.length > 0){
				%>
					<tr>
						<td colspan="11" align="center"><input type="button" value="Send for Approval" onclick="fnSubmitApproval();"/></td>
					</tr>
				<%
						}
					}
				%>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
	<input type="hidden" name="txtRptId" value="<%=(request.getAttribute(Constant.FORM_DATA) != null && (((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptId()) != 0) ? ((SalesmanDlyRptModel)request.getAttribute(Constant.FORM_DATA)).getRptId() : ""%>"/>
	<input type="hidden" name="txtIndex" value=""/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
<script type="text/javascript">
document.getElementById('lblExpense').innerHTML = "<%=Utils.get2Decimal(expense)%>";
function fnGetCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
    		t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" )'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnSelect(id, txt){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
}


function fnClearCustomer(){
	document.forms[0].txtCust.value = "";
	document.getElementById("img").style.visibility = "hidden";
	document.forms[0].sCustomer.value = "";
	document.forms[0].txtCust.readOnly = false;
}
</script>
</html>

