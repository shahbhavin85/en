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
	document.forms[0].HANDLERS.value = 'ENQUIRY_RPT_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ENQ_ALERT_RPT';
	fnFinalSubmit();		
}

function fnOpenEnquiryPopup(enqNo){
	window.open('/en/app?HANDLERS=SALESMAN_ENQUIRY_HANDLER&ACTIONS=GET_ENQUIRY&txtEnqNo='+enqNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnGetEnquiryDetails(enqNo){
	document.forms[0].txtEnqNo.value = enqNo;
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ENQUIRY';
	fnFinalSubmit();		
}

function fnPrintReport(){
	document.forms[0].reset();
	window.open('/en/app?HANDLERS=ENQUIRY_RPT_HANDLER&ACTIONS=PRINT_ENQ_ALERT_RPT&txtToDate='+document.forms[0].txtToDate.value+'&txtFromDate='+document.forms[0].txtFromDate.value+'&sAccessName='+document.forms[0].sAccessName.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}