<%@page import="com.en.util.Utils"%>
<div align="center" id="FreezePane" class="FreezePaneOn"></div>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript">
    ddlevelsmenu.setup("ddtopmenubar", "topbar");
	document.documentElement.style.overflow = "hidden";
</script> 
<table width="100%" class="appHeader">
	<tr>
	<td valign="middle"><label style="color: white; font-size: 20px; "><%=Constant.TITLE %></label></td>
	<td align="right" valign="bottom" style="font-size: 11px; color: white;"><div style="padding-bottom: 4px;"><%if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == 1){ %><span  onclick="fnGoToPage('SALARY_HANDLER','GET_SALARY_DETAILS');" style="background-color: yellow; font-weight : bolder; color: red; padding-left: 3px; padding-right: 3px; font-style: italic; cursor: pointer;" title="Click to get the details">Current Month Salary : Rs. <%=request.getAttribute(Constant.CURRENT_SALARY) %></span> | <%} %><a id="inbox" href="#" style="font-size: 13px; font-weight: bold; padding: 2px;" onclick="fnGoToPage('MESSENGER_HANDLER','MY_CONVERSATION');">Inbox(<%=(Integer)request.getAttribute(Constant.NEW_INBOX_COUNT) %>)</a> |  Access Point : <font style="background-color: yellow; font-weight : bolder; color: red; padding-left: 3px; padding-right: 3px; font-style: italic; "><span onclick="fnToggleAccessPointLst();" style="cursor: pointer;"><%=((request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")) || request.getSession().getAttribute(Constant.ACCESS_POINT).equals("3") || (request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1")) || (request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0"))) ? ((request.getSession().getAttribute(Constant.ACCESS_POINT).equals("1") ? "Employee Management" : (request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0") ? "Admin" : request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2") ? "Accounts" : "Exhibition"))) : (((AccessPointModel)request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessName())%></span><img onclick="fnToggleAccessPointLst();" alt="arrow" style="padding-left: 5px; cursor: pointer;" src="images/arrow-down.gif"> </font></div><span style="background-color: yellow; font-weight : bolder; color: <%=((Double)request.getAttribute(Constant.PENDING_TARGET) > 0) ? "green" : "red"%>; padding-left: 3px; padding-right: 3px; font-style: italic; cursor: pointer;">PENDING TARGET : Rs. <%=Utils.get2Decimal((Double)request.getAttribute(Constant.PENDING_TARGET)) %></span> | <%if(request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){%><a href="#" style="font-size: 15px; font-weight: bold;" onclick="fnGetCashOnHand();">Total Cash : Rs. <%=Utils.get2Decimal((Double)request.getAttribute(Constant.BALANCE))%></a> | <%}%>Pending Approval : <a href="#">Request(<%=request.getAttribute(Constant.PENDING_APP_REQUEST) %>)</a> / <a href="#" >Demand(<%=request.getAttribute(Constant.PENDING_APP_DEMAND) %>)</a> | Welcome <a href="#" onclick="fnGoToPage('EXCEL_HANDLER','USER_PROFILE');" title="Click to see profile"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName() %></a> | <a href="javascript: fnLogout();">Logout</a></td>
	</tr>
</table>
<%
	if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == 2){
%>
<jsp:include page="accountsMenu.jsp"></jsp:include>
<%
	} else if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == 3) {
%>
<jsp:include page="exhibitionMenu.jsp"></jsp:include>
<%
	} else if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() > 10) {
%>
<jsp:include page="branchMenu.jsp"></jsp:include>
<%
	} else if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == 0)  {
%>
<jsp:include page="administratorMenu.jsp"></jsp:include>
<%
	} else if(((AccessPointModel) request.getSession().getAttribute(Constant.ACCESS_POINT_DTLS)).getAccessId() == 1)  {
%>
<jsp:include page="salesmanMenu.jsp"></jsp:include>
<%
	}
%>
<%
	if(request.getSession().getAttribute(Constant.USERS_ACCESS_LIST) != null && ((AccessPointModel[])request.getSession().getAttribute(Constant.USERS_ACCESS_LIST)).length > 0){
		AccessPointModel[] accessLst = (AccessPointModel[])request.getSession().getAttribute(Constant.USERS_ACCESS_LIST);
%>
<div id="accessPntList" style="position:absolute; background: #8A2BE2; border: 1px; border-color: black; border-style: solid; top: 28px; right: 6px; display: none;">
	<table border=0 style="width : 200px; padding: 2px; color: white; font-weight: bold; font-size: 9px;">
<%
		for(int i=0; i<accessLst.length; i++){
%> 
	<tr><td align="right" style="padding: 2px; cursor: pointer;" onmouseover="this.style.background = 'orange';" onmouseout="this.style.background = 'transparent';"  onclick="fnChangeAccessPoint(<%=accessLst[i].getAccessId()%>);"><%=(accessLst[i].getAccessId() < 11) ? accessLst[i].getAccessName() : accessLst[i].getAccessName()%></td></tr>
<%
		}
%>
	</table>
</div>
<%
	}
%>
<script type="text/javascript">
function fnToggleAccessPointLst(){
	document.getElementById("accessPntList").style.display = (document.getElementById("accessPntList").style.display == 'none') ? 'block' : 'none';
}

function fnChangeAccessPoint(id){
	if(id != <%=request.getSession().getAttribute(Constant.ACCESS_POINT)%>){
		document.forms[0].changeAccessPoint.value = id;
		document.forms[0].HANDLERS.value = "LOGIN_HANDLER";
		document.forms[0].ACTIONS.value = "CHANGE_ACCESS_POINT";
		fnFinalSubmit();
	}
	fnToggleAccessPointLst();
}
</script>