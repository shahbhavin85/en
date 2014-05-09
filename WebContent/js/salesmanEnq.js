function fnGetPendingAppointment(){
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_PENDING_APPOINTMENT';
	fnFinalSubmit();	
}

function fnOpenEnquiryPopup(enqNo){
	window.open('/en/app?HANDLERS=SALESMAN_ENQUIRY_HANDLER&ACTIONS=GET_ENQUIRY&txtEnqNo='+enqNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnReplyToAppointment(enqNo, appDt){
	document.forms[0].txtAppDt.value = appDt;
	document.forms[0].txtEnqNo.value = enqNo;
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_REPLY_PENDING_APPOINTMENT';
	fnFinalSubmit();		
}

function fnDelayToAppointment(enqNo, appDt){
	document.forms[0].txtAppDt.value = appDt;
	document.forms[0].txtEnqNo.value = enqNo;
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_DELAY_PENDING_APPOINTMENT';
	fnFinalSubmit();		
}

function fnCancelToAppointment(enqNo, appDt){
	document.forms[0].txtAppDt.value = appDt;
	document.forms[0].txtEnqNo.value = enqNo;
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_CANCEL_PENDING_APPOINTMENT';
	fnFinalSubmit();		
}

function fnGoBack(){
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = '';
	fnFinalSubmit();	
}

function fnConfirmReply(){
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'REPLY_PENDING_APPOINTMENT';
	fnFinalSubmit();		
}

function fnConfirmDelay(){
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'DELAY_PENDING_APPOINTMENT';
	fnFinalSubmit();		
}

function fnConfirmCancel(){
	document.forms[0].HANDLERS.value = 'SALESMAN_ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'CANCEL_PENDING_APPOINTMENT';
	fnFinalSubmit();		
}

function fnReplyChange(value){
	fnReplyDisable();
	if(value == 'alert') {
		document.forms[0].txtAlertDate.disabled = false;
		document.forms[0].taAlertRemark.disabled = false;
	} else if(value == 'reApp') {
		document.forms[0].txtReAppointDate.disabled = false;
		document.forms[0].txtReAppointTime.disabled = false;
		document.forms[0].taReRemark.disabled = false;
	} else if(value == 'close') {
		document.forms[0].taCloseRemark.disabled = false;
	}
}

function fnReplyDisable(){
	document.forms[0].txtAlertDate.disabled = 'disabled';
	document.forms[0].taAlertRemark.disabled = 'disabled';
	if(vType != 6){
		document.forms[0].txtReAppointDate.disabled = 'disabled';
		document.forms[0].txtReAppointTime.disabled = 'disabled';
		document.forms[0].taReRemark.disabled = 'disabled';
		document.forms[0].taCloseRemark.disabled = 'disabled';
	}
	
	document.forms[0].txtAlertDate.value = document.forms[0].txtAlertDate.defaultValue;
	document.forms[0].taAlertRemark.value = document.forms[0].taAlertRemark.defaultValue;
	if(vType != 6){
		document.forms[0].txtReAppointDate.value = document.forms[0].txtReAppointDate.defaultValue;
		document.forms[0].txtReAppointTime.value = document.forms[0].txtReAppointTime.defaultValue;
		document.forms[0].taReRemark.value = document.forms[0].taReRemark.defaultValue;
		document.forms[0].taCloseRemark.value = document.forms[0].taCloseRemark.defaultValue;
	}
}