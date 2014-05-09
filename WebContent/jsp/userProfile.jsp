<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/user.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmModifyUser">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">User Profile</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<fieldset>
					<legend class="pageSubHeader">Profile</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Full Name <span style="color: red">*</span> : </td>
							<td colspan="3"><input readonly="readonly" type="text" maxlength="25" name="txtFullName" style="width: 350px;"  value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getUserName()%>"></td>
						</tr>
						<tr>
							<td align="right">Manager Id <span style="color: red">*</span> :</td>
							<td>
								<input readonly="readonly" type="text" name="txtManagerId" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getManager()%>"/>
							</td>
							<td align="right">Branch :</td>
							<td>
								<input readonly="readonly" type="text" name="sBranch" style="width: <%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBranch().getAccessName()%>"/>
							</td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Official</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Mobile <span style="color: red">*</span> : </td>
							<td><input readonly="readonly" type="text" maxlength="10" name="txtMobile1" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getMobile1()%>"></td>	
<!-- 						</tr> -->
<!-- 						<tr> -->
							<td align="right">Phone : </td>
							<td><input readonly="readonly" type="text" maxlength="10" name="txtPhone1" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getPhone1()%>"></td>
<!-- 						</tr> -->
<!-- 						<tr> -->
							<td align="right">Email <span style="color: red">*</span> : </td>
							<td><input readonly="readonly" type="text" maxlength="50" name="txtEmail1" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getEmail1()%>"></td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Personal</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">D O B : </td>
							<td><input type="text" readonly="readonly" maxlength="10" name="txtDOB" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getDob()%>"></td>	
							<td align="right">Blood Group : </td>
							<td><input type="text" readonly="readonly" maxlength="10" name="txtBloodGroup" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBloodGroup()%>"></td>
						</tr>
						<tr>
							<td align="right">Mobile : </td>
							<td><input type="text" readonly="readonly" maxlength="10" name="txtMobile2" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getMobile2()%>"></td>	
							<td align="right">Phone : </td>
							<td><input type="text" readonly="readonly" maxlength="10" name="txtPhone2" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getPhone2()%>"></td>	
							<td align="right">Email : </td>
							<td><input type="text" readonly="readonly" maxlength="50" name="txtEmail2" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getEmail2()%>"></td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Qualification : </td>
							<td colspan = 5><textarea readonly="readonly" rows="5" cols="100" name="taQualification"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getQualification()%></textarea> </td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Personal Identity Mark : </td>
							<td colspan = 5><textarea readonly="readonly" rows="5" cols="100" name="taIdentityMark"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getPersonalIdentityMark()%></textarea> </td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Present Address : </td>
							<td colspan = 5><textarea readonly="readonly" rows="5" cols="100" name="taPresentAddress"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getPresentAddress()%></textarea> </td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Permanent Address : </td>
							<td colspan = 5><textarea readonly="readonly" rows="5" cols="100" name="taPermanentAddress"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getAddress()%></textarea> </td>
						</tr>
						<tr>
							<td align="right">Father Name : </td>
							<td><input readonly="readonly" type="text" maxlength="50" name="txtFatherName" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getFatherName()%>"></td>
							<td align="right">Mother Name : </td>
							<td><input readonly="readonly" type="text" maxlength="50" name="txtMotherName" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getMotherName()%>"></td>
						</tr>
						<tr>
							<td align="right">Spouse Name : </td>
							<td><input readonly="readonly" type="text" maxlength="50" name="txtSpouseName" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getSpouseName()%>"></td>
							<td align="right">D O A : </td>
							<td><input readonly="readonly" type="text" maxlength="10" name="txtDOA" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getDoa()%>"></td>
						</tr>
						<tr>
							<td align="right">Child 1 : </td>
							<td><input type="text" readonly="readonly" maxlength="50" name="txtChild1" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getChild1()%>"></td>
							<td align="right">Child 2 : </td>
							<td><input type="text" readonly="readonly" maxlength="50" name="txtChild2" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getChild2()%>"></td>
						</tr>
						<tr>
							<td align="right">Child 3 : </td>
							<td><input type="text" readonly="readonly" maxlength="50" name="txtChild3" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getChild3()%>"></td>
							<td align="right">Child 4 : </td>
							<td><input type="text" readonly="readonly" maxlength="50" name="txtChild4" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getChild4()%>"></td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Past Experience</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Company : </td>
							<td><input type="text" readonly="readonly" maxlength="100" name="txtPastCompany" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getPastCompany()%>"></td>	
<!-- 						</tr> -->
<!-- 						<tr> -->
							<td align="right">Period : </td>
							<td><input type="text" readonly="readonly" maxlength="30" name="txtPeriod" style="width: 200px;" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getPeriod()%>"></td>
						</tr>
						<tr>
							<td align="right">Details : </td>
							<td colspan="3"><textarea readonly="readonly" rows="5" cols="100" name="txtDetails"><%=((UserModel)request.getSession().getAttribute(Constant.USER)).getDetails()%></textarea></td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Bank Details</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Bank 1 Name :</td>
							<td><input readonly="readonly" maxlength="100" name="txtBankName1" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankName1()%>" style="width: 195px;"></td>
							<td align="right">Bank 2 Name :</td>
							<td><input readonly="readonly" maxlength="100" name="txtBankName2" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankName2()%>" style="width: 195px;"></td>				
						</tr>				
						<tr>
							<td align="right">Bank 1 Branch :</td>
							<td><input readonly="readonly" maxlength="100" name="txtBankBranch1" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankBranch1()%>" style="width: 195px;"></td>
							<td align="right">Bank 2 Branch :</td>
							<td><input readonly="readonly" maxlength="100" name="txtBankBranch2" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankBranch2()%>" style="width: 195px;"></td>				
						</tr>
						<tr>
							<td align="right">Bank 1 AC Number :</td>
							<td><input readonly="readonly" maxlength="25" name="txtBankAc1" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankAc1()%>" style="width: 195px;"></td>
							<td align="right">Bank 2 AC Number :</td>
							<td><input readonly="readonly" maxlength="25" name="txtBankAc2" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankAc2()%>" style="width: 195px;"></td>				
						</tr>
						<tr>
							<td align="right">Bank 1 IFSC :</td>
							<td><input readonly="readonly" maxlength="25" name="txtBankIFSC1" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankIfsc1()%>" style="width: 195px;"></td>
							<td align="right">Bank 2 IFSC :</td>
							<td><input readonly="readonly" maxlength="25" name="txtBankIFSC2" value="<%=((UserModel)request.getSession().getAttribute(Constant.USER)).getBankIfsc2()%>" style="width: 195px;"></td>				
						</tr>				
					</table>
				</fieldset>
			</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>

</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>