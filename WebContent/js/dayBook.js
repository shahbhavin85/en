function fnChanged(combo){
	fnReset();
	if (combo.value == 4 || combo.value == 3 || combo.value == 2 || combo.value == 1 || combo.value == 46 || combo.value == 47 || combo.value == 48 || combo.value == 49 || 
			combo.value == 44 || combo.value == 43 || combo.value == 42 || combo.value == 41 || combo.value == 71 || combo.value == 72 || combo.value == 73){
		document.getElementById("custRow").style.display = 'table-row';
		document.getElementById("custDtlsRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'none';
		document.getElementById("payRow").style.display = 'none';
		document.getElementById("bankRow").style.display = 'none';
		document.getElementById("amtRow").style.display = 'none';
		document.getElementById("remarkRow").style.display = 'none';
		document.getElementById("btnRow").style.display = 'none';
		document.getElementById("branchRow").style.display = 'none';
		document.getElementById("userRow").style.display = 'none';
//	} else if (combo.value == 53  || combo.value == 52 || combo.value == 51 || combo.value == 57 || combo.value == 61 || combo.value == 65 || combo.value == 62 || combo.value == 63 || combo.value == 64){
//		document.getElementById("custRow").style.display = 'none';
//		document.getElementById("custDtlsRow").style.display = 'none';
//		document.getElementById("bankRow").style.display = 'none';
//		document.getElementById("amtRow").style.display = 'table-row';
//		document.getElementById("remarkRow").style.display = 'table-row';
//		document.getElementById("btnRow").style.display = 'table-row';
//		document.getElementById("branchRow").style.display = 'none';
//		document.getElementById("userRow").style.display = 'none';
//	} else if (combo.value == 9 || combo.value == 54 ){
	} else if (combo.value == 9){
		document.getElementById("custRow").style.display = 'none';
		document.getElementById("custDtlsRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'none';
		document.getElementById("payRow").style.display = 'none';
		document.getElementById("bankRow").style.display = 'table-row';
		document.getElementById("amtRow").style.display = 'table-row';
		document.getElementById("remarkRow").style.display = 'table-row';
		document.getElementById("btnRow").style.display = 'table-row';
		document.getElementById("branchRow").style.display = 'none';
		document.getElementById("userRow").style.display = 'none';
//	} else if (combo.value == 56 || combo.value == 55 || combo.value == 21 || combo.value == 81 || combo.value == 11){
	} else if (combo.value == 21 || combo.value == 11){
		document.getElementById("custRow").style.display = 'table-row';
		document.getElementById("custDtlsRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'none';
		document.getElementById("payRow").style.display = 'none';
		document.getElementById("bankRow").style.display = 'none';
		document.getElementById("amtRow").style.display = 'table-row';
		document.getElementById("remarkRow").style.display = 'table-row';
		document.getElementById("btnRow").style.display = 'table-row';
		document.getElementById("branchRow").style.display = 'none';
		document.getElementById("userRow").style.display = 'none';
//	} else if (combo.value == 5 || combo.value == 60){
	} else if (combo.value == 5){
		document.getElementById("custRow").style.display = 'none';
		document.getElementById("custDtlsRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'none';
		document.getElementById("payRow").style.display = 'none';
		document.getElementById("bankRow").style.display = 'none';
		document.getElementById("amtRow").style.display = 'table-row';
		document.getElementById("remarkRow").style.display = 'table-row';
		document.getElementById("btnRow").style.display = 'table-row';
		document.getElementById("branchRow").style.display = 'table-row';
		document.getElementById("userRow").style.display = 'none';
//	} else if (combo.value == 58 || combo.value == 59 || combo.value == 10){
//	} else if (combo.value == 10){
//		document.getElementById("custRow").style.display = 'none';
//		document.getElementById("custDtlsRow").style.display = 'none';
//		document.getElementById("custDtlsRow1").style.display = 'none';
//		document.getElementById("payRow").style.display = 'none';
//		document.getElementById("bankRow").style.display = 'none';
//		document.getElementById("amtRow").style.display = 'table-row';
//		document.getElementById("remarkRow").style.display = 'table-row';
//		document.getElementById("btnRow").style.display = 'table-row';
//		document.getElementById("branchRow").style.display = 'none';
//		document.getElementById("userRow").style.display = 'table-row';
	} else if(combo.value == 22 || combo.value == 24){
		document.getElementById("custRow").style.display = 'table-row';
		document.getElementById("custDtlsRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'none';
		document.getElementById("payRow").style.display = 'none';
		document.getElementById("bankRow").style.display = 'table-row';
		document.getElementById("amtRow").style.display = 'table-row';
		document.getElementById("remarkRow").style.display = 'table-row';
		document.getElementById("btnRow").style.display = 'table-row';
		document.getElementById("branchRow").style.display = 'none';
		document.getElementById("userRow").style.display = 'none';
	} else if(combo.value == 23){
		document.getElementById("custRow").style.display = 'table-row';
		document.getElementById("custDtlsRow").style.display = 'table-row';
		document.getElementById("payRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'table-row';
		document.getElementById("bankRow").style.display = 'table-row';
		document.getElementById("amtRow").style.display = 'table-row';
		document.getElementById("remarkRow").style.display = 'table-row';
		document.getElementById("btnRow").style.display = 'table-row';
		document.getElementById("branchRow").style.display = 'none';
		document.getElementById("userRow").style.display = 'none';
	} else {
		document.getElementById("custRow").style.display = 'table-row';
		document.getElementById("payRow").style.display = 'table-row';
		document.getElementById("custDtlsRow").style.display = 'none';
		document.getElementById("custDtlsRow1").style.display = 'table-row';
		document.getElementById("bankRow").style.display = 'table-row';
		document.getElementById("amtRow").style.display = 'table-row';
		document.getElementById("remarkRow").style.display = 'table-row';
		document.getElementById("btnRow").style.display = 'table-row';
		document.getElementById("branchRow").style.display = 'table-row';
		document.getElementById("userRow").style.display = 'table-row';
		document.forms[0].txtChqNo.disabled = true;
		document.forms[0].sHeshBank.disabled = true;
		document.forms[0].txtChqDate.disabled = true;
		if(combo.value == 63 || combo.value == 62){
			document.forms[0].txtAccessId.disabled = true;
			document.forms[0].txtCust.disabled = true;
			document.forms[0].txtUserId.disabled = true;
		}
		if(combo.value == 54){
			document.forms[0].sHeshBank.disabled = false;
			document.forms[0].txtAccessId.disabled = true;
			document.forms[0].txtCust.disabled = true;
			document.forms[0].txtUserId.disabled = true;
			document.forms[0].rPayMode[1].disabled = true;
			document.forms[0].rPayMode[2].disabled = true;
		}
		if(combo.value != 60){
			document.forms[0].txtAccessId.disabled = true;
		} else {
			document.forms[0].txtCust.disabled = true;
			document.forms[0].txtUserId.disabled = true;
			document.forms[0].rPayMode[1].disabled = true;
			document.forms[0].rPayMode[2].disabled = true;
		}
		if(combo.value != 58 && combo.value != 59){
			document.forms[0].txtUserId.disabled = true;
		} else {
			document.forms[0].txtCust.disabled = true;
			document.forms[0].txtAccessId.disabled = true;
		}
		if(combo.value == 10){
			document.forms[0].txtUserId.disabled = false;
			document.forms[0].txtCust.disabled = true;
		} 
	}
	
//	if(combo.value > 50){} 
}

function fnChangeMode(id){
	if(id == 0){
		document.forms[0].txtChqNo.disabled = true;
		document.forms[0].sHeshBank.disabled = true;
		document.forms[0].txtChqDate.disabled = true;
	} else if (id == 1){
		document.forms[0].txtChqNo.disabled = false;
		document.forms[0].sHeshBank.disabled = false;
		document.forms[0].txtChqDate.disabled = false;
	} else {
		document.forms[0].txtChqNo.disabled = true;
		document.forms[0].sHeshBank.disabled = false;
		document.forms[0].txtChqDate.disabled = true;
	}
}


function fnShowPurchase(billNo){
	window.open('/en/app?HANDLERS=PURCHASE_REPORT_HANDLER&ACTIONS=GET_PURCHASE&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnReset(){
	document.forms[0].sCustomer.value = '--';
	document.forms[0].txtChqNo.value = '';
	document.forms[0].txtChqNo.disabled = false;
	document.forms[0].txtCust.disabled = false;
	document.forms[0].txtCustBank.value = '';
	document.forms[0].sHeshBank.value = '--';
	document.forms[0].sHeshBank.disabled = false;
	document.forms[0].txtChqDate.disabled = false;
	document.forms[0].txtAccessId.disabled = false;
	document.forms[0].txtUserId.disabled = false;
	document.forms[0].txtAccessId.value = '--';
	document.forms[0].txtUserId.value = '--';
	document.forms[0].txtAmount.value = '';
	document.forms[0].rPayMode[1].disabled = false;
	document.forms[0].rPayMode[2].disabled = false;
	document.forms[0].taRemark.value = '';
}

function fnAddNewEntry(){
	var valid = true;
	if(document.forms[0].sType.value > 50 && document.forms[0].sType.value != 71 && document.forms[0].sType.value != 72 && document.forms[0].sType.value != 73){
		valid = validateDebitEntry();
	} else {
		valid = validateData();
	}
	if(valid){
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_DAY_ENTRY";
		fnFinalSubmit();
	}
}

function validateDebitEntry(){
	var vPayMode = document.forms[0].rPayMode[0].checked ? 0 : (document.forms[0].rPayMode[1].checked ? 1 : 2);
	var eType = document.forms[0].sType.value;
	if(vPayMode == 1){
		if(document.forms[0].txtChqNo.value == ''){
			alert('Please give the cheque no.');
			document.forms[0].txtChqNo.focus();
			return false;
		}
		if(document.forms[0].txtChqDate.value == ''){
			alert('Please give the cheque date.');
			document.forms[0].txtChqDate.focus();
			return false;
		}
	}
	if(vPayMode == 1 || vPayMode == 2 || eType == 54){
		if(document.forms[0].sHeshBank.value == '--'){
			alert('Please select the Bank.');
			document.forms[0].sHeshBank.focus();
			return false;
		}
	}
	if(eType == 60){
		if(document.forms[0].txtAccessId.value == '--'){
			alert('Please select the Branch.');
			document.forms[0].txtAccessId.focus();
			return false;
		}
	}
	if(eType == 58 || eType == 59){
		if(document.forms[0].txtUserId.value == '--'){
			alert('Please select the Staff.');
			document.forms[0].txtUserId.focus();
			return false;
		}
	}
	if(document.forms[0].txtAmount.value == '' || isNaN(document.forms[0].txtAmount.value)){
		alert('Please give the amount.');
		document.forms[0].txtAmount.value == '';
		document.forms[0].txtAmount.focus();
		return false;
	}
	return true;
}
	
function fnBack(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "";
	fnFinalSubmit();
}

function fnDeleteEntry(entryId, type){
	document.forms[0].txtIndex.value = entryId;
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	if(type == 41 || type == 42 || type == 43 || type == 44)
		document.forms[0].ACTIONS.value = "DEL_BILL_PAYMENT";
	else if(type == 46 || type == 47 || type == 48 || type == 49)
		document.forms[0].ACTIONS.value = "DEL_LABOUR_BILL_PAYMENT";
	else if(type == 1 || type == 2 || type == 3 || type == 4)
		document.forms[0].ACTIONS.value = "DEL_ORDER_BILL_PAYMENT";
	else if(type == 71 || type == 72 || type == 73)
		document.forms[0].ACTIONS.value = "DEL_PURCHASE_BILL_PAYMENT";
	else 
		document.forms[0].ACTIONS.value = "DEL_DAY_ENTRY";
	fnFinalSubmit();
}

function fnPrintRpt(){
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_HANDLER&ACTIONS=PRNT_DAY_BOOK&txtId='+document.forms[0].txtId.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnSyncSalesData(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "SYNC_SALES_DATA";
	fnFinalSubmit();
}

function fnBillPayment(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_BILL_PAYMENT";
	fnFinalSubmit();
}

function fnGetPendingBills(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_BILL_PAYMENT";
	fnFinalSubmit();
}

function fnGetPendingOrders(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ORDER_BILL_PAYMENT";
	fnFinalSubmit();
}

function fnGetPendingPayments(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_PURCHASE_BILL_PAYMENT";
	fnFinalSubmit();
}

function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnPayChange(combo){
	if (combo.value == 41){
		document.forms[0].txtChqNo.value = '';
		document.forms[0].txtCustBank.value = '';
		document.forms[0].txtChqDate.value = '';
		document.forms[0].sHeshBank.value = '--';
		document.forms[0].sHeshBank.disabled = true;
		document.forms[0].txtChqNo.disabled = true;
		document.forms[0].txtChqDate.disabled = true;
//		document.forms[0].chqDtDIV.style.display = 'none';
		document.forms[0].txtCustBank.disabled = true;	
	} else if (combo.value == 42){
		document.forms[0].txtChqNo.value = '';
		document.forms[0].txtCustBank.value = '';
		document.forms[0].txtChqDate.value = '';
		document.forms[0].sHeshBank.value = 'KOTAK';
		document.forms[0].sHeshBank.disabled = false;
		document.forms[0].txtChqNo.disabled = true;
		document.forms[0].txtChqDate.disabled = true;
//		document.forms[0].chqDtDIV.style.display = 'none';
		document.forms[0].txtCustBank.disabled = true;	
	} else if (combo.value == 43){
		document.forms[0].txtChqNo.value = '';
		document.forms[0].txtCustBank.value = '';
		document.forms[0].sHeshBank.value = '--';
		document.forms[0].txtChqDate.value = '';
		document.forms[0].sHeshBank.disabled = false;
		document.forms[0].txtChqNo.disabled = false;
		document.forms[0].txtChqDate.disabled = false;
//		document.forms[0].chqDtDIV.style.display = 'block';
		document.forms[0].txtCustBank.disabled = false;	
	}
}

function fnFilterTable(chk){
	if(j==1){
		chk.checked = true;
		return;
	} else {
		var fV = chk.value;
		var vTable = document.getElementById('billTable');
		var rowsInTable = vTable.rows.length;
		var status =  (chk.checked) ? "table-row" : "none";
		for(var i=1; i<rowsInTable; i++){ 
			if(vTable.getElementsByTagName("tr")[i].id.substring(0,3) == fV){
				vTable.getElementsByTagName("tr")[i].style.display = status;
			}
		}
		for (var i=0 ;i<=iTableRow; i++){
			document.getElementById('v'+i).checked = false; 
			document.getElementById('p'+i).value = 0; 
			document.getElementById('p'+i).readOnly = true;
		}
		fnRefreshPending();
		return;
	}
}

function fnOrderChkChange(chk, id, ppid){
	var amt = document.forms[0].txtRemainAmount.value;
	if(amt == 0 && chk.checked){
		alert('No more pending amount.');
		chk.checked = false;
		return;
	}
	if(chk.checked){
		document.getElementById(id).readOnly = false;
		var pending = document.getElementById(ppid).value;
		if(amt - pending < 0)
			document.getElementById(id).value = amt;
		else 
			document.getElementById(id).value = pending;
		document.getElementById(id).focus();
	} else {
		document.getElementById(id).value = 0; 
		document.getElementById(id).readOnly = true;
	}
	fnRefreshOrderPending();
}

function fnRefreshOrderPending(){
	var pendingAmt = document.forms[0].txtAmount.value;
	for (var i=0 ;i<=iTableRow; i++){
		if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
			pendingAmt = pendingAmt - document.getElementById("p"+i).value;
		}
	}
	document.forms[0].txtRemainAmount.value = pendingAmt;
	document.forms[0].btnSave.disabled =  (document.forms[0].txtRemainAmount.value  != 0);
}

function fnPurchaseChkChange(chk, id, ppid, paid){
	var amt = document.forms[0].txtRemainAmount.value;
	if(amt == 0 && chk.checked){
		alert('No more pending amount.');
		chk.checked = false;
		return;
	}
	if(chk.checked){
		document.getElementById(id).readOnly = false;
		document.getElementById(paid).readOnly = false;
		var pending = document.getElementById(ppid).value;
		if(amt - pending < 0)
			document.getElementById(id).value = amt;
		else 
			document.getElementById(id).value = pending;
		document.getElementById(id).focus();
	} else {
		document.getElementById(id).value = 0; 
		document.getElementById(paid).value = 0;
		document.getElementById(paid).readOnly = true;
		document.getElementById(id).readOnly = true;
	}
	fnRefreshPurchasePending();
}

function fnRefreshPurchasePending(){
	var pendingAmt = document.forms[0].txtAmount.value;
	for (var i=0 ;i<=iTableRow; i++){
		if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
			pendingAmt = pendingAmt - document.getElementById("p"+i).value - document.getElementById("pa"+i).value;
		}
	}
	document.forms[0].txtRemainAmount.value = pendingAmt;
	document.forms[0].btnSave.disabled =  (document.forms[0].txtRemainAmount.value  != 0);
}

function fnChkChange(chk, id, ppid, paid){
	var amt = document.forms[0].txtRemainAmount.value;
	if(amt == 0 && chk.checked){
		alert('No more pending amount.');
		chk.checked = false;
		return;
	}
	if(chk.checked){
		document.getElementById(id).readOnly = false;
		document.getElementById(paid).readOnly = false;
		var pending = document.getElementById(ppid).value;
		if(amt - pending < 0)
			document.getElementById(id).value = amt;
		else 
			document.getElementById(id).value = pending;
		document.getElementById(id).focus();
	} else {
		document.getElementById(id).value = 0; 
		document.getElementById(paid).value = 0;
		document.getElementById(paid).readOnly = true;
		document.getElementById(id).readOnly = true;
	}
	fnRefreshPending();
}

function fnRefreshPending(){
	var pendingAmt = document.forms[0].txtAmount.value;
	for (var i=0 ;i<=iTableRow; i++){
		if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
			pendingAmt = pendingAmt - parseFloat(document.getElementById("p"+i).value).toFixed(2) - parseFloat(document.getElementById("pa"+i).value).toFixed(2);
		}
	}
	document.forms[0].txtRemainAmount.value = parseFloat(pendingAmt).toFixed(2);
	document.forms[0].btnSave.disabled =  (document.forms[0].txtRemainAmount.value  != 0);
}

function fnAmtChange(text,id, vId){
	var rate = text.value;
	var pending = document.getElementById(id).value;
	var amt = document.forms[0].txtRemainAmount.value;
	if(pending-rate < 0){
		if(amt - pending < 0)
			text.value = amt;
		else 
			text.value = pending;
	}
	if(rate == 0){
		text.readOnly = true;
		document.getElementById(vId).checked = false;
	}
	fnRefreshPending();
}

function fnOrderAmtChange(text,id, vId){
	var rate = text.value;
	var pending = document.getElementById(id).value;
	var amt = document.forms[0].txtRemainAmount.value;
	if(pending-rate < 0){
		if(amt - pending < 0)
			text.value = amt;
		else 
			text.value = pending;
	}
	if(rate == 0){
		text.readOnly = true;
		document.getElementById(vId).checked = false;
	}
	fnRefreshOrderPending();
}

function fnAdjAmtChange(text, pid, id, vId){
	var rate = text.value;
	if(rate == ''){
		text.value = 0;
		rate = 0;
	}
	if(isNaN(rate)){
		rate = 0;
		text.value = rate;
	}
//	var pending = document.getElementById(id).value;
//	var paying = document.getElementById(pid).value;
	var amt = document.forms[0].txtRemainAmount.value;
//	alert(rate);
	if(amt-rate < 0){
		rate = 0;
		text.value = rate;
		/*if(amt - pending < 0)
			text.value = amt;
		else 
			text.value = pending;*/
	} else {
		if(rate <0){
			document.getElementById(pid).value = document.getElementById(pid).value - rate;
		}
	}
	/*if(rate == 0){
		text.readOnly = true;
		document.getElementById(vId).checked = false;
	}*/
	fnRefreshPending();
}

function fnSaveOrderPayment(){
	if(validateBillPayment()){
		var payDetails = "";
		var vTable = document.getElementById('billTable');
		for (var i=0 ;i<=iTableRow; i++){
			if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
				payDetails += vTable.getElementsByTagName("tr")[i+1].id.substring(4)+"|"+document.getElementById('p'+i).value+"#";
			}
		}
		document.forms[0].payDtls.value = payDetails;
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_ORDER_BILL_PAYMENT";
		fnFinalSubmit();
	}
}

function fnSavePurchasePayment(){
	if(validateBillPayment()){
		var payDetails = "";
		var vTable = document.getElementById('billTable');
		for (var i=0 ;i<=iTableRow; i++){
			if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
				payDetails += vTable.getElementsByTagName("tr")[i+1].id.substring(4)+"|"+document.getElementById('p'+i).value+"|"+document.getElementById('pa'+i).value+"#";
			}
		}
		document.forms[0].payDtls.value = payDetails;
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_PURCHASE_BILL_PAYMENT";
		fnFinalSubmit();
	}
}

function fnSaveLabourBill(){
	if(validateBillPayment()){
		var payDetails = "";
		var vTable = document.getElementById('billTable');
		for (var i=0 ;i<=iTableRow; i++){
			if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
				payDetails += vTable.getElementsByTagName("tr")[i+1].id.substring(4)+"|"+document.getElementById('p'+i).value+"|"+document.getElementById('pa'+i).value+"#";
			}
		}
	//	alert(payDetails);
		document.forms[0].payDtls.value = payDetails;
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_LABOUR_BILL_PAYMENT";
		fnFinalSubmit();
	}
}

function fnSave(){
	if(validateBillPayment()){
		var payDetails = "";
		var vTable = document.getElementById('billTable');
		for (var i=0 ;i<=iTableRow; i++){
			if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
				payDetails += vTable.getElementsByTagName("tr")[i+1].id.substring(4)+"|"+document.getElementById('p'+i).value+"|"+document.getElementById('pa'+i).value+"#";
			}
		}
	//	alert(payDetails);
		document.forms[0].payDtls.value = payDetails;
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_BILL_PAYMENT";
		fnFinalSubmit();
	}
}

function validateBillPayment(){
	if(iType == 46 || iType == 41 || iType == 71  || iType == 3){
		// no validation required
	} else if(iType == 42 || iType == 2){
		if(document.forms[0].sHeshBank.value == '--'){
			alert('Please select the Bank.');
			document.forms[0].sHeshBank.focus();
			return false;
		}
	} else if(iType == 48 || iType == 43 || iType == 1){
		if(document.forms[0].sHeshBank.value == '--'){
			alert('Please select the Bank.');
			document.forms[0].sHeshBank.focus();
			return false;
		}
		if(document.forms[0].txtCustBank.value == ''){
			alert('Please give the customer bank.');
			document.forms[0].txtCustBank.focus();
			return false;
		}
		if(document.forms[0].txtChqNo.value == ''){
			alert('Please give the cheque no.');
			document.forms[0].txtChqNo.focus();
			return false;
		}
		if(document.forms[0].txtChqDate.value == ''){
			alert('Please give the cheque date.');
			document.forms[0].txtChqDate.focus();
			return false;
		}
		if(document.forms[0].txtDepDate.value == ''){
			alert('Please give the deposit date.');
			document.forms[0].txtDepDate.focus();
			return false;
		}
	} else if(iType == 73){

		if(document.forms[0].txtCustBank.value == ''){
			alert('Please give the customer bank.');
			document.forms[0].txtCustBank.focus();
			return false;
		}
		if(document.forms[0].txtChqNo.value == ''){
			alert('Please give the cheque no.');
			document.forms[0].txtChqNo.focus();
			return false;
		}
	} else {
		if(document.forms[0].sHeshBank.value == '--'){
			alert('Please select the Bank.');
			document.forms[0].sHeshBank.focus();
			return false;
		}
	}
	return true;
}

function fnCustomerChange(combo){
	var vCombo = document.forms[0].sType;
	if(combo.value != '' && (vCombo.value == 44 || vCombo.value == 43 || vCombo.value == 42 || vCombo.value == 41)){
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "GET_BILL_PAYMENT";
		fnFinalSubmit();
	}
	if(combo.value != '' && (vCombo.value == 46 || vCombo.value == 47 || vCombo.value == 48 || vCombo.value == 49)){
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "GET_LABOUR_BILL_PAYMENT";
		fnFinalSubmit();
	}
	if(combo.value != '' && (vCombo.value == 71 || vCombo.value == 72 || vCombo.value == 73)){
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "GET_PURCHASE_BILL_PAYMENT";
		fnFinalSubmit();
	}
	if(combo.value != '' && (vCombo.value == 1 || vCombo.value == 2 || vCombo.value == 3 || vCombo.value == 4)){
		document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "GET_ORDER_BILL_PAYMENT";
		fnFinalSubmit();
	}
}

function fnChangeCheck(chk, btnId){
	if(chk.checked){
		document.getElementById(btnId).disabled = false;
	} else {
		document.getElementById(btnId).disabled = true;
	}
}

function fnSyncReview(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "SYNC_REVIEW_DATA";
	fnFinalSubmit();
}

function fnBackToDayBook(){
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_DAY_ENTRIES";
	fnFinalSubmit();
}

function validateData(){
	if(document.getElementById("custRow").style.display == 'table-row' && document.forms[0].sType.value != 10){
		if(document.forms[0].sCustomer.value == '--'){
			alert('Please select the customer.');
			document.forms[0].sCustomer.focus();
			return false;
		}
	}
	if(document.getElementById("custDtlsRow").style.display == 'table-row'){
		if(document.forms[0].txtCustBank.value == ''){
			alert('Please give the customer bank.');
			document.forms[0].txtCustBank.focus();
			return false;
		}
		if(document.forms[0].txtChqNo.value == ''){
			alert('Please give the cheque no.');
			document.forms[0].txtChqNo.focus();
			return false;
		}
		if(document.forms[0].txtChqDate.value == ''){
			alert('Please give the cheque date.');
			document.forms[0].txtChqDate.focus();
			return false;
		}
		if(document.forms[0].txtDepDate.value == ''){
			alert('Please give the deposit date.');
			document.forms[0].txtDepDate.focus();
			return false;
		}
	}
//	alert(document.forms[0].sHeshBank.disabled);
	if(document.getElementById("bankRow").style.display == 'table-row'){
		if(document.forms[0].sHeshBank.value == '--' && document.forms[0].sHeshBank.disabled == false){
			alert('Please select the Bank.');
			document.forms[0].sHeshBank.focus();
			return false;
		}
	}
	if(document.getElementById("branchRow").style.display == 'table-row' && document.forms[0].sType.value != 10){
		if(document.forms[0].txtAccessId.value == '--'){
			alert('Please give the branch.');
			document.forms[0].txtAccessId.focus();
			return false;
		}
	}
	if(document.getElementById("userRow").style.display == 'table-row'){
		if(document.forms[0].txtUserId.value == '--'){
			alert('Please give the staff.');
			document.forms[0].txtUserId.focus();
			return false;
		}
	}
	if(document.getElementById("amtRow").style.display == 'table-row'){
		if(document.forms[0].txtAmount.value == '' || isNaN(document.forms[0].txtAmount.value)){
			alert('Please give the amount.');
			document.forms[0].txtAmount.value == '';
			document.forms[0].txtAmount.focus();
			return false;
		}
	}
	return true;
}

function fnShowPostDateChqs(){
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_HANDLER&ACTIONS=GET_PD_CHQ_LST',"","status=1,scrollbars=1,width=655,height=500");
}

function fnShowBankDepositEntries(){
	window.open('/en/app?HANDLERS=BRANCH_DLY_ENTRY_HANDLER&ACTIONS=GET_BANK_DEP_LST',"","status=1,scrollbars=1,width=655,height=500");
}

function fnShowOrder(id){
	window.open('/en/app?HANDLERS=ORDER_HANDLER&ACTIONS=SHOW_ORDER&txtInvoiceNo='+id,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnShowLabourSales(billNo){
	window.open('/en/app?HANDLERS=LABOUR_BILL_HANDLER&ACTIONS=GET_LABOUR_BILL_POPUP&txtInvoiceNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnGetEntryDetails(dt){
	document.forms[0].txtRptDate.value = dt;
	document.forms[0].HANDLERS.value = "BRANCH_DLY_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "GET_DAY_ENTRIES";
	fnFinalSubmit();
}



function fnGetCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_SALES_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
//    		if(retObj.list[i].count > 0){
//    			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
//    		} else {
    			t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
    		}
//       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnClearCustomer(){
	document.forms[0].txtCust.value = "";
	document.getElementById("img").style.visibility = "hidden";
	document.forms[0].sCustomer.value = "";
	document.forms[0].txtCust.readOnly = false;
	document.getElementById("custState").innerHTML = "";
	document.getElementById("custType").innerHTML = "";
//	document.forms[0].txtTin.value = "";
//	document.forms[0].txtCst.value = "";
//	document.forms[0].txtContactPerson.value = "";
//	document.forms[0].txtMobile1.value = "";
	document.forms[0].txtCust.focus();
}

function fnSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
	fnCustomerChange(document.forms[0].sCustomer);
}

function fnGetBillCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_SALES_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForBillCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForBillCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
//    		if(retObj.list[i].count > 0){
//    			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
//    		} else {
    			t = t+"<li onclick='fnBillSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
    		}
//       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnBillSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
	fnGetPendingBills();
}

function fnGetOrderCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_SALES_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForOrderCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForOrderCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
//    		if(retObj.list[i].count > 0){
//    			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
//    		} else {
    			t = t+"<li onclick='fnOrderSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
    		}
//       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnOrderSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
	fnGetPendingOrders();
}

function fnGetPurchaseCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_SALES_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForPurchaseCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForPurchaseCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
//    		if(retObj.list[i].count > 0){
//    			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
//    		} else {
    			t = t+"<li onclick='fnPurchaseSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
    		}
//       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnPurchaseSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
	fnGetPendingPayments();
}