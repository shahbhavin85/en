<%@page import="com.en.util.Constant"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" style="background-color: #8A2BE2; color: white; min-width: 1000px;">
	<tr>
		<td valign="top">
			<div id="ddtopmenubar" class="mattblackmenu">
				<ul>
					<li>
						<a href="#" rel="enquirySubMenu"><b>Enquiry</b>
						</a>
					</li>
					<li>
						<a href="#" rel="quotationMenu"><b>Quotation</b>
						</a>
					</li>
					<li>
						<a href="#" rel="orderMenu"><b>Order</b>
						</a>
					</li>
					<li>
						<a href="#" rel="branchEntryMenu"><b>Cash / Bank Entries</b>
						</a>
					</li>
					<li>
						<a href="#" rel="messageSubMenu"><b>Messages</b> </a>
					</li>
					<li>
						<a href="#" rel="masterSubMenu"><b>Master Settings</b>
						</a>
					</li>
				</ul>
			</div>
		</td>
		<td align="right" style="padding-right: 20px;">
			<a href="javascript:fnHelp();"><img alt="help" src="images/help.gif">
				<font style="font-size: small; color: orange; font-weight: bold; text-decoration: underline;">Help</font>
			</a>
		</td>
	</tr>
</table>
<ul id="enquirySubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('ENQUIRY_HANDLER','INIT_ENQUIRY');">New Enquiry</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ENQUIRY_HANDLER','VIEW_ENQUIRY');">View Enquiry</a>
	</li>
	<li>
		<a href="#">Enquiry Status Report</a>
	</li>
	<li>
		<a onclick="fnGoToPage('ENQUIRY_RPT_HANDLER','');">Enquiry Alert Report</a>
	</li>
</ul>
<ul id="quotationMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('EXHIBITION_QUOTATION_HANDLER','');">Quotation Invoice</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('EXHIBITION_QUOTATION_HANDLER','INIT_VIEW_EDIT_QUOTATION');">View / Edit Quotation</a>
	</li>
</ul>
<ul id="orderMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('EXHIBITION_ORDER_HANDLER','');">New Order</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('EXHIBITION_ORDER_HANDLER','INIT_VIEW_EDIT_ORDER');">View / Edit Order</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('EXHIBITION_ORDER_HANDLER','INIT_FOLLOWUP');">Order Followups</a>
	</li>
</ul>
<ul id="branchEntryMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','');">Cash Entry</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','VIEW_DLY_ENTRY_RPT');">View Daily Report</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','INIT_UNDEPOSIT_CHQ');">Undeposited Cheques</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','INIT_APPROVE_DAY_ENTRIES');">Awaiting Approval Report</a>
	</li>
</ul>
<ul id="masterSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#">Customer Settings</a>
		<ul>
			<li>
				<a href="#">Customer Group</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_GRP_HANDLER','');">New Customer Group</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_GRP_HANDLER','MOD_CUSTOMER_GRP');">Modify Customer Group</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="#">Customer</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER');">New Customer</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','MOD_CUSTOMER');">Modify Customer</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER_LABEL');">Customer Label</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER_MERGE');">Customer Merge</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_VIEW_CUSTOMER');">View Customer Details</a>
		</ul>
	</li>
	<li>
		<a href="#">Price List</a>
		<ul>
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
	</li>
</ul>
<ul id="messageSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('MESSENGER_HANDLER','');">New Conversation</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('MESSENGER_HANDLER','MY_CONVERSATION');">My Conversations</a>
	</li>
</ul>