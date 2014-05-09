
function fnTypeChange(combo){
	if(combo.value == 0){
		combo.style.background = '#FFC1C1';
	} else if(combo.value == 1){
		combo.style.background = '#FFFF7E';
	} else if(combo.value == 2){
		combo.style.background = '#BCED91';
	} else {
		combo.style.background = 'white';
	}
}


function fnGetSalesDtls(){
	document.forms[0].HANDLERS.value = "COLLECTION_HANDLER";
	document.forms[0].ACTIONS.value = "GET_OUTSTANDING_RPT";
	fnFinalSubmit();
}

function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnLedger(custId){
	window.open('/en/app?HANDLERS=CUSTOMER_LEDGER_RPT_HANDLER&sCustomer='+custId,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnChangeBranch(branch){
	var iLen = document.forms[0].cBranch.length;
	if(branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cBranch[i].checked = true;
		}
	} else if(!branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cBranch[i].checked = false;
		}
	} else if(!branch.checked){
		document.forms[0].cBranch[0].checked = false;
	} else if(branch.value != 0){
		var isAllChecked = true;
		for(var i=1; i<iLen; i++){
			if(!document.forms[0].cBranch[i].checked){
				isAllChecked = false;
			}
		}
		document.forms[0].cBranch[0].checked = isAllChecked;
	}
}

function fnShowBillPayment(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_BILL_PAYMENT_DTLS&billNo='+billNo,"","status=1,scrollbars=1,width=655,height=500");
}

function fnShowOutstandingDtls(custId){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_CUST_OUTSTANDING_RPT&custId='+custId+'&branches='+document.forms[0].branches.value+'&sUser='+document.forms[0].sUser.value,"","status=1,scrollbars=1,width=655,height=500");
}

function fnOpenFollowupScreen(custId){
	document.forms[0].reset();
	document.forms[0].custId.value = custId;
	document.forms[0].HANDLERS.value = "COLLECTION_HANDLER";
	document.forms[0].ACTIONS.value = "VIEW_CUST_OUTSTANDING_RPT";
	fnFinalSubmit();
}

function fnShowFollowupDtls(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_FOLLOWUP_DTLS&billNo='+billNo,"","status=1,scrollbars=1,width=655,height=500");
}