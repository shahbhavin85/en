
var vItemNameRegex = /^[a-zA-Z0-9_&\/@. ]{1,50}$/;
//var vItemNumberRegex = /^[a-zA-Z0-9-]{1,20}$/;
var vItemPriceRegex = /(^\d{0,10}[.]{1}\d{0,2}$)|(^\d{0,10}$)/;
var vItemDevTimeRegex = /(^\d{0,3}$)/;

function fnSaveItem(){
//	var vItemName = document.forms[0].txtItemName.value;
	var vItemNumber = document.forms[0].txtItemNumber.value;
	var vItemPrice = document.forms[0].txtItemPrice.value;
	var vDevTime = document.forms[0].txtDevTime.value;
	/*if (trim(vItemName) == '' || !vItemNameRegex.test(vItemName)) {
		alert('Please provide a valid item name.');
		document.forms[0].txtItemName.focus();
	} else */if (trim(vItemNumber) == '') {
		alert('Please provide a valid item number.');
		document.forms[0].txtItemNumber.focus();
	} else if (trim(vItemPrice) == '' || !vItemPriceRegex.test(vItemPrice)) {
		alert('Please provide a valid price.');
		document.forms[0].txtItemPrice.focus();
	} else if (!vItemDevTimeRegex.test(vDevTime)) {
		alert('Please provide a valid delivery time.');
		document.forms[0].txtDevTime.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ITM_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_ITM';
		fnFinalSubmit();
	}
}

function fnGetItemDetails(){
	document.forms[0].HANDLERS.value = 'ITM_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ITM';
	fnFinalSubmit();	
}

function fnUpdateItemDetails(){
	//var vItemName = document.forms[0].txtItemName.value;
	var vItemNumber = document.forms[0].txtItemNumber.value;
	var vItemPrice = document.forms[0].txtItemPrice.value;
	var vDevTime = document.forms[0].txtDevTime.value;
	/*if (trim(vItemName) == '' || !vItemNameRegex.test(vItemName)) {
		alert('Please provide a valid item name.');
		document.forms[0].txtItemName.focus();
	} else*/ if (trim(vItemNumber) == '') {
		alert('Please provide a valid item number.');
		document.forms[0].txtItemNumber.focus();
	} else if (trim(vItemPrice) == '' || !vItemPriceRegex.test(vItemPrice)) {
		alert('Please provide a valid price.');
		document.forms[0].txtItemPrice.focus();
	} else if (!vItemDevTimeRegex.test(vDevTime)) {
		alert('Please provide a valid delivery time.');
		document.forms[0].txtDevTime.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ITM_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_ITM';
		fnFinalSubmit();
	}
}