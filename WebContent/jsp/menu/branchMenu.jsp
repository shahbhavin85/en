<table width="100%" border="0" cellpadding="0" cellspacing="0" style="background-color: #8A2BE2; color: white; min-width: 1000px;">
	<tr>
		<td valign="top">
			<div id="ddtopmenubar" class="mattblackmenu">
				<ul>
					<li>
						<a href="#" rel="enquirySubMenu"><b>Enquiry</b> </a>
					</li>
					<li>
						<a href="#" rel="quotationMenu"><b>Quotation</b> </a>
					</li>
					<li>
						<a href="#" rel="orderMenu"><b>Order</b> </a>
					</li>
					<li>
						<a href="#" rel="salesSubMenu"><b>Sales</b> </a>
					</li>
					<li>
						<a href="#" rel="transferMenu"><b>Branch Transfers</b> </a>
					</li>
					<li>
						<a href="#" rel="purchaseMenu"><b>Purchase</b> </a>
					</li>
					<li>
						<a href="#" rel="branchEntryMenu"><b>Cash / Bank Entries</b> </a>
					</li>
					<li>
						<a href="#" rel="collectionSubMenu"><b>Collection</b> </a>
					</li>
					<li>
						<a href="#" rel="catalogueSubMenu"><b>Catalogue</b> </a>
					</li>
					<li>
						<a href="#" rel="reportMenu"><b>Reports</b> </a>
					</li>
					<li>
						<a href="#" rel="masterSubMenu"><b>Master Settings</b> </a>
					</li>
				</ul>
			</div>
		</td>
		<td align="right" style="padding-right: 20px;">
			<a href="javascript:fnHelp();"><img alt="help" src="images/help.gif"> <font style="font-size: small; color: orange; font-weight: bold; text-decoration: underline;">Help</font> </a>
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
		<a href="#" onclick="fnGoToPage('QUOTATION_HANDLER','');">Quotation Invoice</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('QUOTATION_HANDLER','INIT_VIEW_EDIT_QUOTATION');">View / Edit Quotation</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('QUOTATION_HANDLER','INIT_FOLLOWUP');">Quotation Followups</a>
	</li>
</ul>
<ul id="orderMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('ORDER_HANDLER','');">New Order</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ORDER_HANDLER','INIT_VIEW_EDIT_ORDER');">View / Edit Order</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ORDER_HANDLER','INIT_FOLLOWUP');">Order Followups</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ORDER_HANDLER','INIT_ITEM_FOLLOWUP');">Order Item Followups</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('ORDER_HANDLER','INIT_CUSTOMER_FOLLOWUP');">Order Customer Followups</a>
	</li>
</ul>
<ul id="salesSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('SALES_HANDLER','');">Sales Invoice</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALES_HANDLER','INIT_VIEW_EDIT_SALES');">View / Edit Invoice</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('SALES_HANDLER','INIT_EDIT_SALES_LR_DTLS');">Edit Sales LR Details</a>
	</li>
	<li>
		<a href="#">Labour Invoice</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('LABOUR_BILL_HANDLER','');">New Invoice</a>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('LABOUR_BILL_HANDLER','INIT_LABOUR_BILL');">View / Edit Invoice</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Sales Return Invoice</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('CREDIT_NOTE_HANDLER','');">New Invoice</a>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('CREDIT_NOTE_HANDLER','INIT_VIEW_EDIT_CREDIT_NOTE');">View / Edit Invoice</a>
			</li>
		</ul>
	</li>
</ul>
<ul id="transferMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('TRANSFER_HANDLER','');">Stock Transfer</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('TRANSFER_HANDLER','INIT_VIEW_EDIT_TRANSFER');">View / Edit Transfer</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('TRANSFER_HANDLER','INIT_EDIT_TRANSFER_LR_DTLS');">Edit Transfer LR Details</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('TRANSFER_HANDLER','INIT_APP_TRANSFER');">Awaiting Approval</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('TRANSFER_REQUEST_HANDLER','');">Transfer Request</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('TRANSFER_REQUEST_HANDLER','INIT_VIEW_EDIT_TRANSFER_REQUEST');">View / Edit Transfer Request</a>
	</li>
</ul>
<ul id="purchaseMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('PURCHASE_HANDLER','');">Purchase Invoice</a>
	</li>
	<li>
		<a href="#" onclick="fnGoToPage('PURCHASE_HANDLER','INIT_VIEW_EDIT_PURCHASE');">View / Edit Purchase</a>
	</li>
	<li>
		<a href="#">Purchase Return Invoice</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('PURCHASE_RETURN_HANDLER','');">New Invoice</a>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('PURCHASE_RETURN_HANDLER','INIT_VIEW_EDIT_PURCHASE_RETURN');">View / Edit Invoice</a>
			</li>
		</ul>
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
</ul>
<ul id="collectionSubMenu" class="ddsubmenustyle">
	<li>
		<a href="#" onclick="fnGoToPage('COLLECTION_HANDLER','')">Outstanding Report</a>
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
	<li>
		<a href="#">Sales Return Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('CREDIT_NOTE_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Labour Bill Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('LABOUR_BILL_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Purchase Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('PURCHASE_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Purchase Return Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('PURCHASE_RETURN_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Transfer Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('TRANSFER_REPORT_HANDLER','');">Master Report</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Day Book Reports</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_RPT_HANDLER','');">Cheque Entries</a>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_RPT_HANDLER','INIT_BANK_ENTRIES_RPT');">Bank Deposit Entries</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Ledger Rpt</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('BRANCH_DLY_ENTRY_RPT_HANDLER','INIT_BANK_LEDGER_RPT');">Bank Ledger</a>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('CUSTOMER_LEDGER_RPT_HANDLER','INIT_LEDGER_RPT');">Customer Ledger</a>
			</li>
		</ul>
	</li>
	<li>
		<a href="#">Stock Rpt</a>
		<ul>
			<li>
				<a href="#" onclick="fnGoToPage('BRANCH_STOCK_ENTRY_HANDLER','');">Branch Stock Entry</a>
			</li>
			<li>
				<a href="#" onclick="fnPrintStockReport();">Current Stock</a>
			</li>
			<li>
				<a href="#" onclick="fnGoToPage('STOCK_REPORT_HANDLER','');">Stock Report</a>
			</li>
		</ul>
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
	<li>
		<a href="#" onclick="fnGoToPage('BRANCH_STOCK_HANDLER','INIT_OPEN_STOCK');">Opening Stock</a>
	</li>
</ul>