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
			<legend class="screenHeader">Modify User</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<table cellpadding="3">
				<tr>
					<td align="right">User Id <span style="color: red">*</span> :</td>
					<td>
						<select name="txtUserId" style="width: 200px;" onchange="fnGetUserDetails();">
							<%
								if(request.getAttribute(Constant.FORM_DATA) == null){
							%>
							<option value="--">-------</option>
							<%
								}
								
								UserModel[] users = request.getAttribute(Constant.USERS) != null ?
														(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
								for(int i=0; i<users.length;i++){
							%>
							<option value="<%=users[i].getUserId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
									((UserModel)request.getAttribute(Constant.FORM_DATA)).getUserId().equals(users[i].getUserId())) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()+" ("+users[i].getUserId()+")"%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
			</table>
			<fieldset>
					<legend class="pageSubHeader">Profile</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Full Name <span style="color: red">*</span> : </td>
							<td colspan="3"><input type="text" maxlength="25" name="txtFullName" style="width: 350px;"  value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getUserName() : ""%>"></td>
						</tr>
						<tr>
							<td align="right">Manager Id <span style="color: red">*</span> :</td>
							<td>
								<select name="txtManagerId" style="width: 200px;">
									<option value="--">-------</option>
									<%
										for(int i=0; i<users.length;i++){
											if(request.getAttribute(Constant.FORM_DATA) != null && 
													((UserModel)request.getAttribute(Constant.FORM_DATA)).getUserId().equals(users[i].getUserId()))
												continue;
									%>
									<option value="<%=users[i].getUserId()%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
											((UserModel)request.getAttribute(Constant.FORM_DATA)).getManager().equals(users[i].getUserId())) ? "selected=\"selected\"" : "" %>><%=users[i].getUserName()%></option>
									<%
										}
									%>
								</select>
							</td>
							<td align="right">Branch <span style="color: red">*</span> :</td>
							<td>
								<select name="sBranch" style="width: 200px;">
									<option value="--">-------</option>
									<option value="2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((UserModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId() == 1) ? "selected=\"selected\"" : "" %>>Accounts</option>
									<%
										AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
										AccessPointModel temp = null;
										for(int i=0; i<accessPoints.length; i++){
											temp = accessPoints[i];
									%>
									<option value="<%=temp.getAccessId()%>" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((UserModel)request.getAttribute(Constant.FORM_DATA)).getBranch().getAccessId() == temp.getAccessId()) ? "selected=\"selected\"" : "" %>><%=temp.getAccessName()%></option>
									<%
										}
									%>
								</select>
							</td>
						</tr>
						<tr>
							<td align="right">Type <span style="color: red">*</span> :</td>
							<td colspan="3">
								<input type="radio" name="rType" id="rType1" value="1" checked="checked"/><label for="rType1">Admin</label>
								<input type="radio" name="rType" id="rType2" value="2" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((UserModel)request.getAttribute(Constant.FORM_DATA)).getType() == 2) ? "checked=\"checked\"" : "" %>/><label for="rType2">Accounts</label>
								<input type="radio" name="rType" id="rType3" value="3" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((UserModel)request.getAttribute(Constant.FORM_DATA)).getType() == 3) ? "checked=\"checked\"" : "" %>/><label for="rType3">Sales</label>
								<input type="radio" name="rType" id="rType4" value="4" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((UserModel)request.getAttribute(Constant.FORM_DATA)).getType() == 4) ? "checked=\"checked\"" : "" %>/><label for="rType4">Labour</label>
								<input type="radio" name="rType" id="rType5" value="5" <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((UserModel)request.getAttribute(Constant.FORM_DATA)).getType() == 5) ? "checked=\"checked\"" : "" %>/><label for="rType5">Office Person</label>
							</td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Official</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Mobile <span style="color: red">*</span> : </td>
							<td><input type="text" maxlength="10" name="txtMobile1" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getMobile1() : ""%>"></td>	
<!-- 						</tr> -->
<!-- 						<tr> -->
							<td align="right">Email <span style="color: red">*</span> : </td>
							<td><input type="text" maxlength="50" name="txtEmail1" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getEmail1() : ""%>"></td>
						</tr>
						<tr>
							<td align="right">Phone : </td>
							<td><input type="text" maxlength="10" name="txtPhone1" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getPhone1() : ""%>"></td>
							<td align="right">Salary : </td>
							<td><input type="text" maxlength="10" name="txtSalary" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getSalary() : "0"%>"></td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Personal</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">D O B : </td>
							<td><input type="text" maxlength="10" name="txtDOB" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getDob() : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtDOB'),event);" /></td>	
							<td align="right">Blood Group : </td>
							<td><input type="text" maxlength="10" name="txtBloodGroup" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBloodGroup() : ""%>"></td>
						</tr>
						<tr>
							<td align="right">Mobile : </td>
							<td><input type="text" maxlength="10" name="txtMobile2" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getMobile2() : ""%>"></td>	
							<td align="right">Phone : </td>
							<td><input type="text" maxlength="10" name="txtPhone2" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getPhone2() : ""%>"></td>	
							<td align="right">Email : </td>
							<td><input type="text" maxlength="50" name="txtEmail2" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getEmail2() : ""%>"></td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Qualification : </td>
							<td colspan = 5><textarea rows="5" cols="100" name="taQualification"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getQualification() : ""%></textarea> </td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Personal Identity Mark : </td>
							<td colspan = 5><textarea rows="5" cols="100" name="taIdentityMark"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getPersonalIdentityMark() : ""%></textarea> </td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Present Address : </td>
							<td colspan = 5><textarea rows="5" cols="100" name="taPresentAddress"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getPresentAddress() : ""%></textarea> </td>
						</tr>
						<tr>	
							<td align="right" valign="top" style="padding-top: 5px;">Permanent Address : </td>
							<td colspan = 5><textarea rows="5" cols="100" name="taPermanentAddress"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getAddress() : ""%></textarea> </td>
						</tr>
						<tr>
							<td align="right">Father Name : </td>
							<td><input type="text" maxlength="50" name="txtFatherName" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getFatherName() : ""%>"></td>
							<td align="right">Mother Name : </td>
							<td><input type="text" maxlength="50" name="txtMotherName" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getMotherName() : ""%>"></td>
						</tr>
						<tr>
							<td align="right">Spouse Name : </td>
							<td><input type="text" maxlength="50" name="txtSpouseName" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getSpouseName() : ""%>"></td>
							<td align="right">D O A : </td>
							<td><input type="text" maxlength="10" name="txtDOA" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getDoa() : ""%>"><img
                                    src='images/date.gif'
                                    title='Click Here' alt='Click Here'
                                    onclick="scwShow(scwID('txtDOA'),event);" /></td>
						</tr>
						<tr>
							<td align="right">Child 1 : </td>
							<td><input type="text" maxlength="50" name="txtChild1" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getChild1() : ""%>"></td>
							<td align="right">Child 2 : </td>
							<td><input type="text" maxlength="50" name="txtChild2" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getChild2() : ""%>"></td>
						</tr>
						<tr>
							<td align="right">Child 3 : </td>
							<td><input type="text" maxlength="50" name="txtChild3" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getChild3() : ""%>"></td>
							<td align="right">Child 4 : </td>
							<td><input type="text" maxlength="50" name="txtChild4" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getChild4() : ""%>"></td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Past Experience</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Company : </td>
							<td><input type="text" maxlength="100" name="txtPastCompany" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getPastCompany() : ""%>"></td>	
<!-- 						</tr> -->
<!-- 						<tr> -->
							<td align="right">Period : </td>
							<td><input type="text" maxlength="30" name="txtPeriod" style="width: 200px;" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getPeriod() : ""%>"></td>
						</tr>
						<tr>
							<td align="right">Details : </td>
							<td colspan="3"><textarea rows="5" cols="100" name="txtDetails"><%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getDetails() : ""%></textarea></td>
						</tr>
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Expense Details</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Expense 1 :</td>
							<td><input maxlength="100" name="txtExpense1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getExpense1() : "0"%>" style="width: 195px;"></td>
							<td align="right">Expense 2 :</td>
							<td><input maxlength="100" name="txtExpense2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getExpense2() : "0"%>" style="width: 195px;"></td>
							<td align="right">Expense 3 :</td>
							<td><input maxlength="100" name="txtExpense3" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getExpense3() : "0"%>" style="width: 195px;"></td>				
						</tr>	
						<tr>
							<td align="right">Expense 4 :</td>
							<td><input maxlength="100" name="txtExpense4" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getExpense4() : "0"%>" style="width: 195px;"></td>
							<td align="right">Expense 5 :</td>
							<td><input maxlength="100" name="txtExpense5" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getExpense5() : "0"%>" style="width: 195px;"></td>
						</tr>			
					</table>
				</fieldset>
				<fieldset style="margin-top: 10px;">
					<legend class="pageSubHeader">Bank Details</legend>
					<table cellpadding="3">
						<tr>
							<td align="right">Bank 1 Name :</td>
							<td><input maxlength="100" name="txtBankName1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankName1() : ""%>" style="width: 195px;"></td>
							<td align="right">Bank 2 Name :</td>
							<td><input maxlength="100" name="txtBankName2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankName2() : ""%>" style="width: 195px;"></td>				
						</tr>				
						<tr>
							<td align="right">Bank 1 Branch :</td>
							<td><input maxlength="100" name="txtBankBranch1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankBranch1() : ""%>" style="width: 195px;"></td>
							<td align="right">Bank 2 Branch :</td>
							<td><input maxlength="100" name="txtBankBranch2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankBranch2() : ""%>" style="width: 195px;"></td>				
						</tr>
						<tr>
							<td align="right">Bank 1 AC Number :</td>
							<td><input maxlength="25" name="txtBankAc1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankAc1() : ""%>" style="width: 195px;"></td>
							<td align="right">Bank 2 AC Number :</td>
							<td><input maxlength="25" name="txtBankAc2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankAc2() : ""%>" style="width: 195px;"></td>				
						</tr>
						<tr>
							<td align="right">Bank 1 IFSC :</td>
							<td><input maxlength="25" name="txtBankIFSC1" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankIfsc1() : ""%>" style="width: 195px;"></td>
							<td align="right">Bank 2 IFSC :</td>
							<td><input maxlength="25" name="txtBankIFSC2" value="<%=(request.getAttribute(Constant.FORM_DATA) != null) ? ((UserModel)request.getAttribute(Constant.FORM_DATA)).getBankIfsc2() : ""%>" style="width: 195px;"></td>				
						</tr>				
					</table>
				</fieldset>
			<table width="100%">
				<tr>
					<td colspan="2" align="center"><input type="button" onclick="fnUpdateUserDetails();" <%=((request.getAttribute(Constant.FORM_DATA) != null && !request.getSession().getAttribute(Constant.ACCESS_POINT).equals("0")) || request.getAttribute(Constant.FORM_DATA) == null) ? "disabled=\"disabled\"" : ""%>
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
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>