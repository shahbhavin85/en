function fnGetTaxDetails(){
	document.forms[0].HANDLERS.value = "TAX_HANDLER";
	document.forms[0].ACTIONS.value = "GET_TAX";
	fnFinalSubmit();
}

function fnUpdateTaxDetails(){
	document.forms[0].HANDLERS.value = "TAX_HANDLER";
	document.forms[0].ACTIONS.value = "UPDT_TAX";
	fnFinalSubmit();
}

function fnCheckValue(comp){
	var vRegex = /(^\d{0,2}[.]{1}\d{0,2}$)|(^\d{0,2}$)/;
	if(trim(comp.value) == '' || !vRegex.test(comp.value)){
		comp.value = 0;
	}
}