<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<form method="post" name="frmAddOffer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Home Page Settings</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">Thought of the Day <span style="color: red">*</span> :</td>
					<td><textarea  name="THOUGHT" style="width: 350px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.THOUGHT) != null ? (String)request.getAttribute(Constant.THOUGHT) : "") %></textarea></td>
				</tr>
				<tr>
					<td align="right">Jackpot <span style="color: red">*</span> :</td>
					<td><textarea  name="JACKPOT" style="width: 350px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.JACKPOT) != null ? (String)request.getAttribute(Constant.JACKPOT) : "") %></textarea></td>
				</tr>
				<tr>
					<td align="right">Good News <span style="color: red">*</span> :</td>
					<td><textarea  name="GOOD_NEWS" style="width: 350px; height : 50px;" maxlength="250"><%=(request.getAttribute(Constant.GOOD_NEWS) != null ? (String)request.getAttribute(Constant.GOOD_NEWS) : "") %></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="Update"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>" value="<%=Constant.HOME_HANDLER%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>" value="<%=Constant.UPDATE_HOME%>"/>
</form>
</body>
</html>