
var vItemCatRegex = /^[a-zA-Z0-9_&\/@. ]{1,50}$/;

function fnSaveItemCat(){
	var vItemCat = document.forms[0].txtItemCategory.value;
	if (trim(vItemCat) == '' || !vItemCatRegex.test(vItemCat)) {
		alert('Please provide a valid category name.');
		document.forms[0].txtItemCategory.value = "";
		document.forms[0].txtItemCategory.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ITM_CAT_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_ITM_CATEGORY';
		fnFinalSubmit();
	}
}

function fnGetItemCategoryDetails(){
	document.forms[0].HANDLERS.value = 'ITM_CAT_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ITM_CATEGORY';
	fnFinalSubmit();	
}

function fnUpdateItemCategoryDetails(){
	var vItemCat = document.forms[0].txtItemCategory.value;
	if (trim(vItemCat) == '' || !vItemCatRegex.test(vItemCat)) {
		alert('Please provide a valid category name.');
		document.forms[0].txtItemCategory.value = "";
		document.forms[0].txtItemCategory.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ITM_CAT_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_ITM_CATEGORY';
		fnFinalSubmit();
	}
}