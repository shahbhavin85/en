<table width="100%" border="0" cellpadding="0" cellspacing="0"
	style="background-color: #8A2BE2; color: white; min-width: 1000px;">
	<tr>
		<td valign="top">
			<div id="ddtopmenubar" class="mattblackmenu">
				<ul>
					<li>
						<a href="#" rel="masterSubMenu"><b>Master Settings</b></a>
					</li>
					<li>
						<a href="#" rel="orderMenu"><b>Order</b></a>
					</li>
					<li>
						<a href="#" rel="salesSubMenu"><b>Sales</b> </a>
					</li>
					<li>
						<a href="#" rel="salarySubMenu"><b>Salary</b> </a>
					</li>
					<li>
						<a href="#" rel="dailyApplSubMenu"><b>Daily Approvals</b> </a>
					</li>
					<li>
						<a href="#" rel="branchEntryMenu"><b>Cash / Bank Entries</b> </a>
					</li>
					<li>
						<a href="#" rel="collectionSubMenu"><b>Collection</b></a>
					</li>
					<li>
						<a href="#" rel="reportMenu"><b>Reports</b> </a>
					</li>
					<li>
						<a href="#" rel="catalogueSubMenu"><b>Catalogue</b> </a>
					</li>
					<li>
						<a href="#" rel="dbSubMenu"><b>DB Related</b> </a>
					</li>
				</ul>
			</div></td>
		<td align="right" style="padding-right: 20px;"><a href="javascript:fnHelp();"><img alt="help" src="images/help.gif"><font style="font-size: small; color: orange; font-weight: bold; text-decoration: underline;">Help</font></a></td>
	</tr>
</table>
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
<ul id="masterSubMenu" class="ddsubmenustyle">
	<li><a href="#">Item Settings</a>
		<ul>
			<li><a href="#">Item Groups</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_GRP_HANDLER','');">New Group</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_GRP_HANDLER','MOD_ITM_GROUP');">Modify Group</a>\
					</li>
				</ul>
			</li>
			<li><a href="#">Item Category</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_CAT_HANDLER','');">New Category</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_CAT_HANDLER','MOD_ITM_CATEGORY');">Modify Category</a>
					</li>
				</ul>
			</li>
			<li><a href="#">Item</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_HANDLER','');">New Item</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_HANDLER','MOD_ITM');">Modify Item</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('TAX_HANDLER','INIT_ITEM_TAX');">Item Tax Details</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('ITM_HANDLER','INIT_ITM_CHECK_LIST');">Item Check List</a>
					</li>
				</ul>
			</li>
		</ul>
	</li>
	<li><a href="#">Customer Settings</a>
		<ul>
			<li><a href="#">Customer Group</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_GRP_HANDLER','');">New Customer Group</a>
					</li>
					<li>
						<a href="#" onclick="fnGoToPage('CUSTOMER_GRP_HANDLER','MOD_CUSTOMER_GRP');">Modify Customer Group</a>
					</li>
				</ul>
			</li>
			<li><a href="#">Customer</a>
				<ul>
					<li>
						<a href="#"
						onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER');">New
							Customer</a></li>
					<li>
						<a href="#"
						onclick="fnGoToPage('CUSTOMER_HANDLER','MOD_CUSTOMER');">Modify
							Customer</a></li>
					<li>
						<a href="#"
						onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER_LABEL');">Customer
							Label</a></li>
					<li>
						<a href="#"
						onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_CUSTOMER_MERGE');">Customer
							Merge</a></li>
				</ul></li>
			<li>
				<a href="#"
				onclick="fnGoToPage('CUSTOMER_HANDLER','INIT_VIEW_CUSTOMER');">View
					Customer Details</a>
		</ul></li>
	<li><a href="#">User Settings</a>
		<ul>
			<li><a href="#">User</a>
				<ul>
					<li>
						<a href="#" onclick="fnGoToPage('USER_HANDLER','');">New
							User</a></li>
					<li>
						<a href="#"
						onclick="fnGoToPage('USER_HANDLER','MOD_USER');">Modify User</a></li>
				</ul></li>
			<li>
				<a href="#"
				onclick="fnGoToPage('USER_HANDLER','INIT_PASSWORD');">Reset User
					Password</a></li>
			<li>
				<a href="#" onclick="fnGoToPage('USER_ACCESS_HANDLER','');">User
					Access Setting</a></li>
		</ul></li>
	<li><a href="#">Price List</a>
		<ul>
			<li><a href="#" onclick="fnPrintPriceList();">Retail Price
					List</a></li>
			<li><a href="#" onclick="fnPrintWholesalesPriceList();">Wholesales
					Price List</a></li>
			<li><a href="#" onclick="fnPrintOfferPriceList();">Offer
					Price List</a></li>
			<li><a href="#"
				onclick="fnGoToPage('ITM_HANDLER','EMAIL_PRICE_LIST');">Email
					Price List</a></li>
		</ul></li>
	<li><a href="#">Email List</a></li>
</ul>
<ul id="salarySubMenu" class="ddsubmenustyle">
	<li><a href="#"
		onclick="fnGoToPage('SALARY_HANDLER','INIT_SALARY_RPT');">Salary Calculation</a></li>
</ul>
<ul id="salesSubMenu" class="ddsubmenustyle">
	<li><a href="#" onclick="fnShowEditPass();">Show Edit Password</a>
	</li>
	<li><a href="#"
		onclick="fnGoToPage('SALES_HANDLER','INIT_GET_SALES_DATE');">Change
			/ Cancel Bill</a></li>
</ul>
<ul id="dailyApplSubMenu" class="ddsubmenustyle">
	<li><a href="#"
		onclick="fnGoToPage('APPROVAL_HANDLER','INIT_APP_SALES_LST');">Sales
			Bill</a></li>
	<li><a href="#"
		onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','INIT_APPROVE_DAY_ENTRIES');">Cash
			/ Bank Entries</a></li>
	<li><a href="#">Stock Transfer</a></li>
	<li><a href="#">Purchase Bill</a></li>
</ul>
<ul id="branchEntryMenu" class="ddsubmenustyle">
	<li><a href="#"
		onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','');">Cash Entry</a></li>
	<li><a href="#"
		onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','VIEW_DLY_ENTRY_RPT');">View
			Daily Report</a></li>
	<li><a href="#"
		onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','INIT_UNDEPOSIT_CHQ');">Undeposited
			Cheques</a></li>
	<li><a href="#"
		onclick="fnGoToPage('LEDGER_ADJUSTMENT_HANDLER','');">Entry
			Adjustment</a></li>
	<li><a href="#"
		onclick="fnGoToPage('LEDGER_ADJUSTMENT_HANDLER','VIEW_LEDGER_ADJUSTMENT');">View
			Entry Adjustment</a></li>
	<li><a href="#"
		onclick="fnGoToPage('BRANCH_DLY_ENTRY_HANDLER','INIT_APPROVE_DAY_ENTRIES');">Awaiting
			Approval Report</a></li>
</ul>
<ul id="collectionSubMenu" class="ddsubmenustyle">
	<li><a href="#" onclick="fnGoToPage('COLLECTION_HANDLER','')">Outstanding
			Report</a></li>
</ul>
<ul id="reportMenu" class="ddsubmenustyle">
	<li><a href="#">Sales Reports</a>
		<ul>
			<li><a href="#" onclick="fnGoToPage('SALES_REPORT_HANDLER','');">Master
					Report</a></li>
		</ul></li>
	<li><a href="#">Sales Return Reports</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('CREDIT_NOTE_REPORT_HANDLER','');">Master
					Report</a></li>
		</ul></li>
	<li><a href="#">Labour Bill Reports</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('LABOUR_BILL_REPORT_HANDLER','');">Master
					Report</a></li>
		</ul></li>
	<li><a href="#">Purchase Reports</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('PURCHASE_REPORT_HANDLER','');">Master
					Report</a></li>
		</ul></li>
	<li><a href="#">Purchase Return Reports</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('PURCHASE_RETURN_REPORT_HANDLER','');">Master
					Report</a></li>
		</ul></li>
	<li><a href="#">Transfer Reports</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('TRANSFER_REPORT_HANDLER','');">Master
					Report</a></li>
		</ul></li>
	<li><a href="#">Day Book Reports</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('BRANCH_DLY_ENTRY_RPT_HANDLER','');">Cheque
					Entries</a></li>
			<li><a href="#"
				onclick="fnGoToPage('BRANCH_DLY_ENTRY_RPT_HANDLER','INIT_BANK_ENTRIES_RPT');">Bank
					Deposit Entries</a></li>
		</ul></li>
	<li><a href="#">Ledger Rpt</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('BRANCH_DLY_ENTRY_RPT_HANDLER','INIT_BANK_LEDGER_RPT');">Bank
					Ledger</a></li>
			<li><a href="#"
				onclick="fnGoToPage('CUSTOMER_LEDGER_RPT_HANDLER','INIT_LEDGER_RPT');">Customer
					Ledger</a></li>
		</ul></li>
<!-- 	<li><a href="#">Salary Rpt</a></li> -->
	<li><a href="#"
		onclick="fnGoToPage('ADMIN_STOCK_REPORT_HANDLER','');">Stock
			Report</a></li>
</ul>
<ul id="dbSubMenu" class="ddsubmenustyle">
	<li><a href="#" onclick="fnGoToPage('EXCEL_HANDLER','');">Export
			Excel</a></li>
	<li><a href="#">Transaction Settings</a>
		<ul>
			<li><a href="#"
				onclick="fnGoToPage('EXCEL_HANDLER','INIT_LOCK_TXN');">Lock
					Transactions</a></li>
			<li><a href="#"
				onclick="fnGoToPage('EXCEL_HANDLER','INIT_UNLOCK_TXN');">Unlock
					Transactions</a></li>
		</ul></li>
	<li><a href="#" onclick="fnGoToPage('EXCEL_HANDLER','BACK_UP');">Back
			Up</a></li>
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