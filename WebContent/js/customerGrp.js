
var vCustomerGrpRegex = /^[a-zA-Z0-9_&\/@. ]{1,200}$/;

function fnSaveCustomerGrp(){
	var vCustomerGrp = document.forms[0].txtCustomerGrp.value;
	if (!vCustomerGrpRegex.test(vCustomerGrp)) {
		alert('Please provide a valid group name.');
		document.forms[0].txtCustomerGrp.value = "";
		document.forms[0].txtCustomerGrp.focus();
	} else {
		document.forms[0].HANDLERS.value = 'CUSTOMER_GRP_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_CUSTOMER_GRP';
		fnFinalSubmit();
	}
}

function fnGetCustomerGrpDetails(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_GRP_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_CUSTOMER_GRP';
	fnFinalSubmit();	
}

function fnUpdateCustomerGrpDetails(){
	var vCustomerGrp = document.forms[0].txtCustomerGrp.value;
	if (!vCustomerGrpRegex.test(vCustomerGrp)) {
		alert('Please provide a valid group name.');
		document.forms[0].txtCustomerGrp.value = "";
		document.forms[0].txtCustomerGrp.focus();
	} else {
		document.forms[0].HANDLERS.value = 'CUSTOMER_GRP_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_CUSTOMER_GRP';
		fnFinalSubmit();
	}
}