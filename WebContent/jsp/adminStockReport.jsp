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
			<legend class="screenHeader">Branch Stock Report</legend>
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
					<td align="right">Stock Date<span style="color: red">*</span> :</td>
					<td><input name="txtStockDate" id="txtStockDate" style="width: 195px;" onclick="scwShow(scwID('txtStockDate'),event);" 
					value="<%=(request.getAttribute("txtStockDate") != null && !((String)request.getAttribute("txtStockDate")).equals("")) ?  (String)request.getAttribute("txtStockDate") : "" %>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtStockDate'),event);" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="button" value="Get Details" onclick="fnGetMasterRpt();"/></td>
				</tr>
			</table>
<%
			if(request.getAttribute(Constant.FORM_DATA) != null){
				HashMap<String, StockModel> result = (HashMap<String, StockModel>)request.getAttribute(Constant.FORM_DATA);
				HashMap<String, AccessPointModel> access = (HashMap<String, AccessPointModel>)request.getAttribute(Constant.ACCESS_POINT);
				ItemModel[] items = (ItemModel[]) request.getAttribute(Constant.ITEMS);
				AccessPointModel tempAccess = null;
				StockModel tempStock = null;
				Iterator<String> itr = result.keySet().iterator();
%>
			<table cellspacing="0" cellpadding="5" border="1" style="margin-top: 10px; width: 950px;">
				<tr>
					<th style="width: 50px;">ITEMID</th>
					<th>ITEM NUMBER / NAME</th>
<%
					while(itr.hasNext()){
						tempAccess = access.get(itr.next());
%>
					<th><%=tempAccess.getAccessName()+" - "+tempAccess.getCity()%></th>
<%
					}
%>
					<th>TOTAL</th>
				</tr>
<%
					double total = 0;
					for(int i=0; i<items.length; i++){
						total = 0;
%>
				<tr>
					<td align="center"><%=items[i].getItemId()%></td>
					<td align="left"><%=items[i].getItemNumber() + " / " + items[i].getItemName() %></td>
<%
						itr = result.keySet().iterator();
						while(itr.hasNext()){
							tempStock = result.get(itr.next());
							total = total + tempStock.getItemModel(items[i].getItemId()).getOpenQty();
%>
					<td align="center"><%=tempStock.getItemModel(items[i].getItemId()).getOpenQty()%></td>
<%
						}
%>
					<td align="center"><%=total%></td>
				</tr>
<%
					}
%>
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
	document.forms[0].HANDLERS.value = "ADMIN_STOCK_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_STOCK_REGISTER_RPT";
	fnFinalSubmit();
}
</script>
</html>