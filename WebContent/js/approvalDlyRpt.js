function fnViewReport(userId, rptDt){
	document.forms[0].txtUserId.value = userId;
	document.forms[0].txtRptDate.value = rptDt;
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'APPROVAL_SALESMAN_RPT_DTLS';
	fnFinalSubmit();		
}