
var vItemGrpRegex = /^[a-zA-Z0-9_&\/@. ]{1,50}$/;

function fnSaveItemGrp(){
	var vItemGrp = document.forms[0].txtItemGrp.value;
	if (!vItemGrpRegex.test(vItemGrp)) {
		alert('Please provide a valid group name.');
		document.forms[0].txtItemGrp.value = "";
		document.forms[0].txtItemGrp.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ITM_GRP_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_ITM_GROUP';
		fnFinalSubmit();
	}
}

function fnGetItemGrpDetails(){
	document.forms[0].HANDLERS.value = 'ITM_GRP_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ITM_GROUP';
	fnFinalSubmit();	
}

function fnUpdateItemGrpDetails(){
	var vItemGrp = document.forms[0].txtItemGrp.value;
	if (!vItemGrpRegex.test(vItemGrp)) {
		alert('Please provide a valid group name.');
		document.forms[0].txtItemGrp.value = "";
		document.forms[0].txtItemGrp.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ITM_GRP_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_ITM_GROUP';
		fnFinalSubmit();
	}
}