<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.TaxModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/tax.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddAccessPoint">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Tax Details</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3" width="100%">
					<tr>
						<td style="width: 120px;" align="right">Select Access Point :</td>
						<td>
							<select name="sAccessName" style="width: 200px;" onchange="fnGetTaxDetails();">
								<%
									if(request.getAttribute(Constant.FORM_DATA) == null){
								%>
								<option value="--">-------</option>
								<%
									}
									AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
									AccessPointModel temp = null;
									for(int i=0; i<accessPoints.length; i++){
										temp = accessPoints[i];
								%>
								<option value="<%=temp.getAccessId()%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((TaxModel)request.getAttribute(Constant.FORM_DATA)).getAccessPoint().getAccessId() == temp.getAccessId()) ? "selected=\"selected\"" : "" %>><%=temp.getAccessName()%></option>
								<%
									}
								%>	
							</select>
						</td>
					</tr>
				<tr>
					<td colspan="2">
						<table>
							<tr>
								<th>Particulars</th>
								<th>SLAB 1</th>
								<th>SLAB 2</th>
								<th>SLAB 3</th>
							</tr>
							<tr>
								<th>VAT</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtVat1" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getVat1() : "0"%>"/>%</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtVat2" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getVat2() : "0"%>"/>%</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtVat3" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getVat3() : "0"%>"/>%</th>
							</tr>
							<tr>
								<th>CST</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtCst1" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCst1() : "0"%>"/>%</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtCst2" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCst2() : "0"%>"/>%</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtCst3" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCst3() : "0"%>"/>%</th>
							</tr>
							<tr>
								<th>CST Against form 'C'</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtCst1c" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCst1c() : "0"%>"/>%</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtCst2c" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCst2c() : "0"%>"/>%</th>
								<th><input type="text" onblur="fnCheckValue(this);" name="txtCst3c" size="5" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCst3c() : "0"%>"/>%</th>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right"> CESS Rate: </td>
					<td><input type="text" onblur="fnCheckValue(this);" size="5" name="txtCess" value="<%= request.getAttribute(Constant.FORM_DATA) != null ? ((TaxModel)request.getAttribute(Constant.FORM_DATA)).getCess() : "0"%>">%</td>
				</tr>
				<tr>
					<td colspan="2" style="padding-left:  200px;"><input type="button" onclick="fnUpdateTaxDetails();" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%>
						value="Modify"></td>
				</tr>
			</table>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
</html>