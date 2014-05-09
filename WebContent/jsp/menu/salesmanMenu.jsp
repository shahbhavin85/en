<table width="100%" border="0" cellpadding="0" cellspacing="0" style="background-color: #8A2BE2; color: white; min-width: 1000px;">
	<tr>
		<td valign="top">
			<div id="ddtopmenubar" class="mattblackmenu">
				<ul>
					<li>
						<a href="#" rel="masterSubMenu"><b>Master Settings</b> </a>
					</li>
					<li>
						<a href="#" rel="enquirySubMenu"><b>Enquiry</b> </a>
					</li>
					<li>
						<a href="#" rel="quotationMenu"><b>Quotation</b></a>
					</li>
					<li>
						<a href="#" rel="orderMenu"><b>Order</b></a>
					</li>
					<li>
						<a href="#" rel="collectionSubMenu"><b>Collection</b></a>
					</li>
					<li>
						<a href="#" rel="salesmanSubMenu"><b>My Reporting</b> </a>
					</li>
					<li>
						<a href="#" rel="attendanceMenu"><b>Attendance</b> </a>
					</li>
					<li>
						<a href="#" rel="reportMenu"><b>Reports</b> </a>
					</li>
					<li>
						<a href="#" rel="catalogueSubMenu"><b>Catalogue</b> </a>
					</li>
					<li>
						<a href="#" rel="priceListSubMenu"><b>Price List</b> </a>
					</li>
				</ul>
			</div>
		</td>
		<td align="right" style="padding-right: 20px;">
			<a href="javascript:fnHelp();"><img alt="help" src="images/help.gif"> <font style="font-size: small; color: orange; font-weight: bold; text-decoration: underline;">Help</font> </a>
		</td>
	</tr>
</table>
<ul id="collectionSubMenu" class="ddsubmenustyle">
	<li><a href="#" onclick="fnGoToPage('COLLECTION_HANDLER','')">Outstanding Report</a></li>
</ul>
<ul id="masterSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#">View Profile</a>
	</li>
	<li>
		<a href="#">Customer Settings</a>
		<ul>
			<li>
				<a href="#">Customer</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER');">New Customer</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_VIEW_CUSTOMER');">View Customer Details</a>
			</li>
			<li>
				<a href="#">Customer Email</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_EMAIL_CUSTOMER');">New Email</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_VIEW_EMAIL_CUSTOMER');">View Email</a>
					</li>
				</ul>
			</li>
		</ul>
	</li>
</ul>
<ul id="enquirySubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('ENQUIRY_HANDLER','INIT_ENQUIRY');">New Enquiry</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_ENQUIRY_HANDLER','');">Enquiry Appointment</a>
	</li>
</ul>
<ul id="quotationMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_QUOTATION_HANDLER','');">Quotation Invoice</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_QUOTATION_HANDLER','INIT_VIEW_EDIT_QUOTATION');">View / Edit Quotation</a>
	</li>
</ul>
<ul id="orderMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_ORDER_HANDLER','');">New Order</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_ORDER_HANDLER','INIT_VIEW_EDIT_ORDER');">View / Edit Order</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_ORDER_HANDLER','INIT_FOLLOWUP');">Order Followups</a>
	</li>
</ul>
<ul id="salesmanSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_DLY_HANDLER','INIT_SALESMAN_DLY');">Daily Report</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_DLY_HANDLER','APPROVAL_SALESMAN_RPT');">Awaiting Approval Report</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_DLY_HANDLER','VIEW_SALESMAN_RPT');">View Daily Report</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALESMAN_DLY_HANDLER','INIT_SALESMAN_STATUS_RPT');">Status Report</a>
	</li>
</ul>
<ul id="catalogueSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('ITM_DTLS_HANDLER','INIT_VIEW_ITEM_DETAILS');">View Catalogue</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ITM_DTLS_HANDLER','INIT_VIEW_ITEM_DETAILS');">Download Catalogue</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ITM_DTLS_HANDLER','INIT_MAIL_ITEM_DETAILS');">Email Catalogue</a>
	</li>
	<li>
		<a href="#">View Showroom Catalogue</a>
	</li>
</ul>
<ul id="priceListSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnPrintPriceList();">Retail Price List</a>
	</li>
	<li>
		<a href="#" onclick="fnPrintWholesalesPriceList();">Wholesales Price List</a>
	</li>
	<li>
		<a href="#" onclick="fnPrintOfferPriceList();">Offer Price List</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ITM_HANDLER','EMAIL_PRICE_LIST');">Email Price List</a>
	</li>
</ul>
<ul id="attendanceMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('ATTENDANCE_HANDLER','');">Attendance</a>
	</li>
</ul>
<ul id="reportMenu" class="ddsubmenustyle">
	<li>
		<a href="#">Order Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('ORDER_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Sales Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('SALES_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
</ul>