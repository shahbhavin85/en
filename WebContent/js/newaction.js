function fnGo1(){
	if(document.forms[0].sType.value == '3'){
		alert('Quotation facility coming soon!!!');
		return;
	}
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_NEW_ACTION2';
	fnFinalSubmit();	
}

function fnForward(){
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'ADD_NEW_ACTION';
	fnFinalSubmit();	
}

function fnAlert(){
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'ADD_NEW_ACTION';
	fnFinalSubmit();	
}

function fnCancel(){
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ENQUIRY';
	fnFinalSubmit();	
}

function fnAddAppointment(){
	if(document.forms[0].sUserId.value == "--"){
		alert('Please select the salesman');
		return;
	}
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'ADD_NEW_ACTION';
	fnFinalSubmit();	
}

function fnAppointmentReply(){
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'ADD_NEW_ACTION';
	fnFinalSubmit();
}