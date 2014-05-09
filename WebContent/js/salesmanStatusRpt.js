function fnGetReport(){ 
	var tempIn = document.forms[0].txtFromDate.value;
	if(tempIn == ""){
		alert('Please select from date.');
		return;
	}
	var vInTime = Date.parse(tempIn);
	var tempOut = document.forms[0].txtToDate.value;
	if(tempOut == ""){
		alert('Please select to date.');
		return;
	}
	var vOutTime = Date.parse(tempOut);
	if(vInTime >= vOutTime){
		alert('To date should be greater than From date');
		return;
	}
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_SALESMAN_STATUS_RPT';
	fnFinalSubmit();		
}

function fnPrintReport(){
	document.forms[0].reset();
	window.open('/en/app?HANDLERS=SALESMAN_DLY_HANDLER&ACTIONS=PRINT_SALESMAN_STATUS_RPT&txtToDate='+document.forms[0].txtToDate.value+'&txtFromDate='+document.forms[0].txtFromDate.value+'&txtUserId='+document.forms[0].txtUserId.value+'&sStatus='+document.forms[0].sStatus.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}