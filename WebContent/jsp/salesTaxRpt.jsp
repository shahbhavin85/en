<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.StockModel"%>
<%@page import="java.util.HashMap"%>
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
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmViewEnquiry">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Branch Sales Tax Report</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right" valign="top">Branch<span style="color: red">*</span> :</td>
					<td>
					<%
						ArrayList selected = new ArrayList(0);
						String[] accessSelected = (request.getAttribute("cBranch") != null) ? ((String[])request.getAttribute("cBranch")) : new String[0];
						if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
					%>
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
							<input type="checkbox" name="cBranch" <%=((accessSelected.length > 0 && (accessSelected[0].equals("--") || selected.indexOf(accessPoints[i].getAccessId()+"")!=-1) || accessSelected.length == 0)) ? "checked=\"checked\"" : ""%> id="cBranch<%=i+1%>" value="<%=accessPoints[i].getAccessId()%>"/><label for="cBranch<%=i+1%>"><%=accessPoints[i].getAccessName()%></label>
							</td>
							<%
								}
							}
						%>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right">From Date<span style="color: red">*</span> :</td>
					<td><input name="txtFromDate" id="txtFromDate" style="width: 195px;" onclick="scwShow(scwID('txtFromDate'),event);" 
					value="<%=(request.getAttribute("txtFromDate") != null && !((String)request.getAttribute("txtFromDate")).equals("")) ?  (String)request.getAttribute("txtFromDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtFromDate'),event);" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         To Date<span style="color: red">*</span> :
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
			String[] taxTypes = {"VAT1","VAT2","VAT3","CST1","CST2","CST3","CST1C","CST2C","CST3C"};
			if(request.getAttribute(Constant.FORM_DATA) != null){
				HashMap<String, HashMap<String, HashMap<String, Object>>> result = (HashMap<String, HashMap<String, HashMap<String, Object>>>)request.getAttribute(Constant.FORM_DATA);
				HashMap<String, HashMap<String, Object>> temp = null;
				Iterator<String> itr = result.keySet().iterator();
				while(itr.hasNext()){
					temp = result.get(itr.next());
%>
			<table cellspacing="0" cellpadding="5" border="1" style="margin-top: 10px; width: 950px;">
				<tr>
					<th colspan="4">TAX DETAILS FOR <%=((AccessPointModel)((HashMap<String, Object>)temp.get("dtls")).get("branch")).getAccessName()+" - "+((AccessPointModel)((HashMap<String, Object>)temp.get("dtls")).get("branch")).getCity() %></th>
				</tr>
				<tr>
					<th style="width: 450px;">PARTICULARS</th>
					<th style="width: 150px;">AMOUNT</th>
					<th style="width: 150px;">TAX</th>
					<th>TOTAL</th>
				</tr>
<%
					String desc = "";
					String slab = "";
					for(int j=0; j<taxTypes.length; j++){
						desc = (j<3) ? "VAT" : (j<6) ? "CST" : "CST AGAINST FOR 'C'";
						slab = (j%3 == 0) ? "1" : (j%3 == 1) ? "2" : "3";
%>
				<tr align="center">
					<td>SLAB <%=slab%> - <%=desc%> @ <%=(Double)((HashMap<String, Object>)temp.get(taxTypes[j])).get("RATE") %>%</td>
					<td>Rs. <%=Utils.get2Decimal((Double)((HashMap<String, Object>)temp.get(taxTypes[j])).get("AMT")) %></td>
					<td>Rs. <%=Utils.get2Decimal((Double)((HashMap<String, Object>)temp.get(taxTypes[j])).get("TAXAMT")) %></td>
					<td>Rs. <%=Utils.get2Decimal(Utils.get2DecimalDouble((Double)((HashMap<String, Object>)temp.get(taxTypes[j])).get("AMT"))+Utils.get2DecimalDouble((Double)((HashMap<String, Object>)temp.get(taxTypes[j])).get("TAXAMT"))) %></td>
				</tr>
<% 	
					}
%>
			</table>
<%
				}
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
function fnChangeBranch(branch){
	var iLen = document.forms[0].cBranch.length;
	if(branch.checked){
		for(var i=0; i<iLen; i++){
			document.forms[0].cBranch[i].checked = true;
		}
	} else {
		for(var i=0; i<iLen; i++){
			document.forms[0].cBranch[i].checked = false;
		}
	}
}

function fnGetMasterRpt(){
	document.forms[0].HANDLERS.value = "SALES_TAX_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_MASTER_RPT";
	fnFinalSubmit();
}
</script>
</html>