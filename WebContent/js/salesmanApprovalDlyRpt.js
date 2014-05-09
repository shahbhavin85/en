function fnBack(){
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'APPROVAL_SALESMAN_RPT';
	fnFinalSubmit();		
}

function fnPrintReport(){
	window.open('/en/app?HANDLERS=SALESMAN_DLY_HANDLER&ACTIONS=PRINT_SALESMAN_RPT&txtRptDate='+document.forms[0].txtRptDate.value+'&txtUserId='+document.forms[0].txtUserId.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnReject(){
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'REJECT_SALESMAN_RPT';
	fnFinalSubmit();		
}

function fnApprove(){
	if(document.forms[0].att.value == '--'){
		alert('Please provide the attendance for the day.');
		return;
	}
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'APPROVE_SALESMAN_RPT';
	fnFinalSubmit();		
}