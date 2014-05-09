function fnBack(){
	document.forms[0].HANDLERS.value = "COLLECTION_HANDLER";
	document.forms[0].ACTIONS.value = "BACK_OUTSTANDING_RPT";
	fnFinalSubmit();
}

function fnSave(){
	var iCnt = document.forms[0].chkBill.length;
	var selected = false;
	if(document.forms[0].chkBill[0] == undefined){
		if(document.forms[0].chkBill.checked){
			selected = true;
		}
	} else {
		if(iCnt == 1){
			if(document.forms[0].chkBill.checked){
				selected = true;
			}
		} else {
			for(var i=0; i<iCnt; i++){
				if(document.forms[0].chkBill[i].checked){
					selected = true;
					break;
				}
			}
		}
	}
	if(selected){
		if(document.forms[0].txtFollowDate.value == ''){
			alert('Please select the follow up date.');
			document.forms[0].txtFollowDate.focus();
			return;
		} else if(document.forms[0].txtRemarks.value == ''){
			alert('Please give some follow up remarks.');
			document.forms[0].txtRemarks.focus();
			return;
		}
		var vFollowDt = new Date(document.forms[0].txtFollowDate.value.substring(6), document.forms[0].txtFollowDate.value.substring(3,5)-1, document.forms[0].txtFollowDate.value.substring(0,2));
		var vCurrDt = new Date(document.forms[0].currDate.value.substring(6), document.forms[0].currDate.value.substring(3,5)-1, document.forms[0].currDate.value.substring(0,2));
		if(vFollowDt>=vCurrDt){
			document.forms[0].HANDLERS.value = "COLLECTION_HANDLER";
			document.forms[0].ACTIONS.value = "ADD_RMK_CUST_OUTSTANDING_RPT";
			fnFinalSubmit();
		} else {
			alert('Follow up date should be same or after '+document.forms[0].currDate.value);
			return;
		}
	} else {
		alert('Please select the bills.');
		return;
	}
}

function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}
