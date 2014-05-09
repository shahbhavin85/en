function fnGoForSalesmanDlyRpt(){
	var vEnqNo = document.forms[0].txtRptDate.value;
	if (trim(vEnqNo) == '') {
		alert('Please provide a report date.');
		document.forms[0].txtRptDate.focus();
	} else {
		document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
		document.forms[0].ACTIONS.value = 'INIT_SALESMAN_DLY1';
		fnFinalSubmit();		
	}
}

function fnTypeChange(radio){
	var vCustEle = document.forms[0].txtCust;
	if(radio.value == 0){
		vCustEle.disabled = false;
		document.forms[0].Add_Customer.disabled = false;
	} else {
		vCustEle.disabled = true;
		document.forms[0].sCustomer.value = '--';		
		document.forms[0].Add_Customer.disabled = true;
	}
}

function fnReset(){
	var vCustEle = document.forms[0].sCustomer;
	vCustEle.disabled = false;
}

function fnDeleteItem(idx){
	document.forms[0].txtIndex.value = idx;
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'DEL_SALESMAN_ITM';
	fnFinalSubmit();	
}

function fnAddItm(){ 
	/*var tempIn = document.forms[0].txtInTime.value;
	if(tempIn == ""){
		alert('Please select in time.');
		return;
	}
	tempInVal = tempIn.substring(tempIn.length-2);
	tempIn = tempIn.substring(0,tempIn.length-3);
	var vInTime = Date.parse('01/01/2011 '+tempIn+':00');
	if(tempInVal == 'pm'){
		if(tempIn.substring(0,tempIn.indexOf(':')) != '12'){
			tempIn = (parseInt(tempIn.substring(0,tempIn.indexOf(':'))) + 12 )+tempIn.substring(tempIn.indexOf(':')); 
		} else {
			tempIn = (parseInt(tempIn.substring(0,tempIn.indexOf(':'))))+tempIn.substring(tempIn.indexOf(':')); 
		}
		vInTime = Date.parse('01/01/2011 '+tempIn+':00');
	}
	var tempOut = document.forms[0].txtOutTime.value;
	if(tempOut == ""){
		alert('Please select out time.');
		return;
	}
	tempOutVal = tempOut.substring(tempOut.length-2);
	tempOut = tempOut.substring(0,tempOut.length-3);
	var vOutTime = Date.parse('01/01/2011 '+tempOut+':00');
	if(tempOutVal == 'pm'){
		if(tempOut.substring(0,tempOut.indexOf(':')) != '12'){
			tempOut = (parseInt(tempOut.substring(0,tempOut.indexOf(':'))) + 12 )+tempOut.substring(tempOut.indexOf(':'));
		} else {
			tempOut = (parseInt(tempOut.substring(0,tempOut.indexOf(':'))))+tempOut.substring(tempOut.indexOf(':'));
		}
		vOutTime = Date.parse('01/01/2011 '+tempOut+':00');
	}
	if(vInTime >= vOutTime){
		alert('Out time should be greater than In time');
		return;
	}*/
	if(document.forms[0].rType[0].checked && document.forms[0].sCustomer.value == '--'){
		alert('Please select the customer');
		return;
	}
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'ADD_SALESMAN_ITM';
	fnFinalSubmit();		
}

function fnSubmitApproval(){
	document.forms[0].HANDLERS.value = 'SALESMAN_DLY_HANDLER';
	document.forms[0].ACTIONS.value = 'SUB_SALESMAN_APP';
	fnFinalSubmit();		
}

function fnAddCustomer(){
	window.showModalDialog('/en/app?HANDLERS=ENQUIRY_HANDLER','','');
}

function fnAddCustomer1(id, name){
	var opt = document.createElement("option");
	document.forms[0].sCustomer.options.add(opt);
    opt.text = name;
    opt.value = id;
    opt.selected = 'selected';
    document.forms[0].sCustomer.focus();
}