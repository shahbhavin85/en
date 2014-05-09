<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.EnquiryModel"%>
<%@page import="com.en.model.CustomerModel"%>
<%@page import="com.en.model.ItemCategoryModel"%>
<%@page import="com.en.model.ItemGroupModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/enquiry.js"></script>
<script type="text/javascript" src="js/tabber.js"></script>
<script type="text/javascript" src="js/timepicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/tabber.css" />
</head>
<body>
<jsp:include page="menu/menu.jsp"></jsp:include>
<form method="post" name="frmAddCustomer">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Add Enquiry</legend>
			<jsp:include page="messages.jsp"></jsp:include>
			<div class="tabber" id ="tab">
			<div class="tabbertab" title="Step 1" id ="tab1">
				<table cellspacing="3" width="100%">
					<tr>
						<td align="center" colspan="6">
							<input type="button" name="btnNext" onclick="fnTab1Next();" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Next"/>
						</td>
					</tr>	
					<tr>
						<td colspan="6" align="center">
							<table>
								<tr><td colspan="5"><b>Items Interested</b></td></tr>
								<tr><td> Item No.: 
									<select name="sItemName" style="width: 200px;" onchange="fnChangeItemNo(this);">
										<%							
											ItemModel[] items = request.getAttribute(Constant.ITEMS) != null ?
																	(ItemModel[]) request.getAttribute(Constant.ITEMS) : new ItemModel[0];
											for(int i=0; i<items.length;i++){
										%>
											<option value="<%=items[i].getItemId()%>"><%=items[i].getItemName()+" - "+items[i].getItemNumber()%></option>
										<%
											}
										%>
									</select></td>
									<td> Item No.: 
									<select name="sItem" style="width: 200px;" onchange="fnChangeItemName(this);">
										<%							
											for(int i=0; i<items.length;i++){
										%>
											<option value="<%=items[i].getItemId()%>"><%=items[i].getItemNumber()%></option>
										<%
											}
										%>
									</select></td>
									<td colspan="2">Numbers : <input type="text" name="txtQuantity" style="width: 100px;"/></td>
								</tr>
								<tr>
									<td colspan="5" align="center"><input type="button" value="Add" onclick="fnAddItem();"/>&nbsp;&nbsp;<input type="button" value="Offers" onclick="fnGetOffers();"/></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="6" width="50%" valign="top" align="center">
							<table width="50%" border="1" id="enqItem">
								<tr>
									<td align="center"><b>Item Id</b></td>
									<td align="center" width="60%"><b>Item No</b></td>
									<td align="center"><b>Offer No</b></td>
									<td align="center"><b>Quantity</b></td>
									<td align="center"><b>Action</b></td>
								</tr>
							</table>
						</td>
						<!--td colspan="3" width="50%" valign="top">
							<table width="100%" border = '1' id="offerData">
								<tr>
									<th colspan="5" align="center"> <B>CURRENT ITEMS & OFFERS</B> <br/>
								</tr>
								<tr>
									<td align="center" width="15%"><b>Offer No</b></td>
									<td align="center"><b>Offer Desc</b></td>
									<td align="center" width="15%"><b>From</b></td>
									<td align="center" width="15%"><b>To</b></td>
								</tr>
							</table>
						</td-->
					</tr>			
				</table>
			</div>
			
			<div class="tabbertab" title="Step 2" id ="tab2">
				<table cellpadding="3">
					<tr>
						<td align="right" width="120px;">Customer<span style="color: red">*</span> :</td>
						<td>
							<select name="sCustomer" style="width: 200px;">
								<option value="--">-------</option>
								<%								
									CustomerModel[] customers = request.getAttribute(Constant.CUSTOMERS) != null ?
															(CustomerModel[]) request.getAttribute(Constant.CUSTOMERS) : new CustomerModel[0];
									for(int i=0; i<customers.length;i++){
								%>
								<option value="<%=customers[i].getCustomerId()%>" style="background-color: <%=(i%2==0) ? "#E0E0E0" : "white"%>"  <%=(request.getAttribute(Constant.FORM_DATA) != null && 
										((EnquiryModel)request.getAttribute(Constant.FORM_DATA)).getCustomer().getCustomerId() == customers[i].getCustomerId()) ? "selected=\"selected\"" : "" %>><%=customers[i].getCustomerName()%> - <%=customers[i].getArea()%> - <%=customers[i].getCity()%></option>
								<%
									}
								%>
							</select>
						</td>
						<td align="right" width="120px;">Priority<span style="color: red">*</span> :</td>
						<td>
							<select name="sPriority" style="width: 200px;">
								<option value="2">HIGH</option>
								<option value="1" selected="selected">MEDIUM</option>
								<option value="0">LOW</option>
							</select>
						</td>
						<td colspan="2" align="right" width="120px;">Reference<span style="color: red">*</span> :</td>
						<td>
							<select name="sReference" style="width: 200px;">
								<option value="0">News Letter</option>
								<option value="1" selected="selected">Salesman Visit</option>
								<option value="2">Recommended</option>
								<option value="3">Website</option>
								<option value="4">Exhibition </option>
								<option value="5">Others</option>
							</select>
						</td>
						</tr>
						<tr>
						<td colspan="6" align="center">
							<input type="button" name="btnBack2" onclick="document.getElementById('tab').tabber.tabShow(0);" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Back"/>
							<input type="button" name="Add_Customer" onclick="fnAddCustomer();" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Add Customer"/>
							<input type="button" name="btnNext2" onclick="fnTab2Next();" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Next"/>
						</td>
					</tr>
				  <tr>
				    <td colspan="7">
					</td>
				  </tr>
				</table>
			</div>
			
			<div class="tabbertab" title="Step 3" id ="tab3">
				<table cellspacing="3" width="100%">
					<tr>
						<td colspan="6" align="center">
							Customer : <label id="lblCustomer3" style="padding-right: 25px;"></label>
							Priority : <label id="lblPriority3" style="padding-right: 25px;"></label>
							Reference : <label id="lblReference3"></label>
						</td>
					</tr>	
					<tr>
						<td colspan="6" align="center">
							<input type="button" name="btnBack2" onclick="document.getElementById('tab').tabber.tabShow(1);" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Back"/>
						</td>
					</tr>
					<tr>
						<td colspan="4" width="66%" valign="top">
						<fieldset>
						<legend class="subHeader"><input type="checkbox" name="appointment" id=appointment onchange="fnAppointment(this);"/><label for="appointment">Appointment</label></legend>
							<table>
								<tr>
									<td align="right">User Id <span style="color: red">*</span> :</td>
									<td>
										<select name="sUserId" id="sUserId" disabled="disabled" style="width: 200px;">
										
											<option value="--">-------</option>
											<%												
												UserModel[] users = request.getAttribute(Constant.USERS) != null ?
																		(UserModel[]) request.getAttribute(Constant.USERS) : new UserModel[0];
												for(int i=0; i<users.length;i++){
											%>
											<option value="<%=users[i].getUserId()%>"><%=users[i].getUserName()%></option>
											<%
												}
											%>
										</select>
									</td>
								</tr>
								<tr>
									<td align="right">Appointment Date :</td>
									<td><input name="txtAppointDate" id="txtAppointDate" style="width: 195px;" disabled="disabled" readonly="readonly" onclick="scwShow(scwID('txtAppointDate'),event);" 
									value="<%=(request.getAttribute(Constant.FORM_DATA) != null && !((ItemModel)request.getAttribute(Constant.FORM_DATA)).getSQLOfferDate().equalsIgnoreCase("0000-00-00")) ? ((ItemModel)request.getAttribute(Constant.FORM_DATA)).getAppOfferDate() : ""%>"><img
				                                    src='images/date.gif'
				                                    title='Click Here' alt='Click Here'
				                                    onclick="scwShow(scwID('txtAppointDate'),event);" /></td>
								</tr>
								<tr>
									<td align="right">Appointment Time :</td>
									<td><input id='txtAppointTime' name="txtAppointTime" style="width: 195px;" readonly="readonly" type='text' value='9:45 am' size=8 maxlength=8 ONBLUR="validateDatePicker(this);" onfocus="selectTime(document.forms[0].inTime1,'txtAppointTime')" onclick="selectTime(document.forms[0].inTime1,'txtAppointTime')"><IMG SRC="images/timepicker.gif" BORDER="0" name="inTime1" ALT="Pick a Time!" ONCLICK="selectTime(this,'txtAppointTime')" STYLE="cursor:hand;visibility: hidden;"></td>
								</tr>
							</table>
						</fieldset>
						<!-- fieldset>
						<legend class="subHeader"><input type="checkbox" name="Forward" id="Forward" onchange="fnForwardRequest(this);"/><label for=Forward>Forward Request</label></legend>
							<table>
								<tr>
									<td align="right">Access Point <span style="color: red">*</span> :</td>
									<td>
										<select name="sAccessName" id="sAccessName" disabled="disabled" style="width: 200px;">
											<option value="--">-------</option>
											<!%
												AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
												AccessPointModel temp = null;
												for(int i=0; i<accessPoints.length; i++){
													temp = accessPoints[i];
											%>
											<option value="<!%=temp.getAccessId()%>"><!%=temp.getAccessName()%> - <!%=temp.getCity()%></option>
											<!%
												}
											%>
										</select>
									</td>
								</tr>
							</table>
						</fieldset-->
						<table>
							<tr>
								<td align="right" style="padding-left: 18px;">Remarks :</td>
								<td><textarea  name="taRemark" style="width: 300px; height : 50px;" maxlength="250"></textarea></td>
								<td align="right">Access Point <span style="color: red">*</span> :</td>
								<td>
									<select name="current_point" id="current_point" style="width: 200px;">
										<option value="--">-------</option>
										<%
										AccessPointModel[] accessPoints = (AccessPointModel[]) request.getAttribute(Constant.ACCESS_POINTS);
										AccessPointModel temp = null;
											for(int i=0; i<accessPoints.length; i++){
												temp = accessPoints[i];
										%>
										<option value="<%=temp.getAccessId()%>"><%=temp.getAccessName()%></option>
										<%
											}
										%>
									</select>
								</td>
							</tr>
						</table>
						</td>
						<td colspan="2" width="33%" valign="top" style="padding-top: 10px;">
							<table border="1" width="100%" id="enqItem3">
								<tr>
									<td align="center"><b>Item Id</b></td>
									<td align="center" width="60%"><b>Item No</b></td>
									<td align="center"><b>Offer No</b></td>
									<td align="center"><b>Quantity</b></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="6" align="center"><input type="button" onclick="fnAddEnquiry();" name="submitEnquiry" style="margin-left: 20px; padding-left: 15px; padding-right: 15px;" value="Submit Enquiry"/></td>
					</tr>		
					</table>
				</div>
			</div>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="txtEnqItem"/>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript" src="js/dateUtil.js"></script>
</html>