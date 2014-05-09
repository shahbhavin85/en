
var vCustomerNameRegex = /^[a-zA-Z0-9_&\/@. ]{1,100}$/;
var vBillPreRegex = /^[a-zA-Z0-9]{3}$/;
var vCustomerRegex = /^[a-zA-Z0-9_&\/@. ]{0,50}$/;
var vCustomerTinRegex = /^[a-zA-Z0-9]{1,50}$/;
var vPinStdPhMoRegex = /^\d{0,15}$/;
var vAlphabetRegex = /^[a-zA-Z ]{1,50}$/;
var vEmailRegex = /^[a-z0-9_\+-]+(\.[a-z0-9_\+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2,4})$/;
var vWebsiteRegex = /^(http:\/\/)?(www.)?([a-z0-9_-])+\.[a-z]{2,4}(\/[a-z0-9_.\/#?&=]*)?$/i;

function fnSaveAccessPoint(){
	var vCustomerName = document.forms[0].txtAccessName.value;
	var vBillPre = document.forms[0].txtBillPre.value;
	var vAddress = document.forms[0].taAddress.value;
	var vCity = document.forms[0].txtCity.value;
	var vDistrict = document.forms[0].txtDistrict.value;
	var vState = document.forms[0].txtState.value;
	var vPinCode = document.forms[0].txtPinCode.value;
	var vStdCode = document.forms[0].txtStdCode.value;
	var vPhone1 = document.forms[0].txtPhone1.value;
	var vPhone2 = document.forms[0].txtPhone2.value;
	var vMobile1 = document.forms[0].txtMobile1.value;
	var vMobile2 = document.forms[0].txtMobile2.value;
	if (trim(vCustomerName) == '' || !vCustomerNameRegex.test(vCustomerName)) {
		alert('Please provide a valid access point name.');
		document.forms[0].txtAccessName.focus();
	} else if (trim(vBillPre) == '' || !vBillPreRegex.test(vBillPre)) {
		alert('Please provide a valid bill Prefix.');
		document.forms[0].txtBillPre.focus();
	} else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vCity) == '' || !vCustomerRegex.test(vCity)) {
		alert('Please provide a valid city.');
		document.forms[0].txtCity.focus();
	} else if (!vCustomerRegex.test(vDistrict)) {
		alert('Please provide a valid district.');
		document.forms[0].txtDistrict.focus();
	} else if (vState == '--') {
		alert('Please provide a valid state.');
		document.forms[0].txtState.focus();
	} else if (!vPinStdPhMoRegex.test(vPinCode)) {
		alert('Please provide a valid pin code.');
		document.forms[0].txtPinCode.focus();
	} else if (!vPinStdPhMoRegex.test(vStdCode)) {
		alert('Please provide a valid std code.');
		document.forms[0].txtStdCode.focus();
	} else if (!vPinStdPhMoRegex.test(vPhone1)) {
		alert('Please provide a valid phone number.');
		document.forms[0].txtPhone1.focus();
	} else if (!vPinStdPhMoRegex.test(vPhone2)) {
		alert('Please provide a valid phone number.');
		document.forms[0].txtPhone2.focus();
	} else if (!vPinStdPhMoRegex.test(vMobile1)) {
		alert('Please provide a valid mobile number.');
		document.forms[0].txtMobile1.focus();
	} else if (!vPinStdPhMoRegex.test(vMobile2)) {
		alert('Please provide a valid mobile number.');
		document.forms[0].txtMobile2.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ACCESS_POINT_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_ACCESS_POINT';
		fnFinalSubmit();
	}
}

function fnGetAccessPointDetails(){
	document.forms[0].HANDLERS.value = 'ACCESS_POINT_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_ACCESS_POINT';
	fnFinalSubmit();	
}

function fnUpdateAccessPointDetails(){
	var vCustomerName = document.forms[0].txtAccessName.value;
	var vBillPre = document.forms[0].txtBillPre.value;
	var vAddress = document.forms[0].taAddress.value;
	var vCity = document.forms[0].txtCity.value;
	var vDistrict = document.forms[0].txtDistrict.value;
	var vState = document.forms[0].txtState.value;
	var vPinCode = document.forms[0].txtPinCode.value;
	var vStdCode = document.forms[0].txtStdCode.value;
	var vPhone1 = document.forms[0].txtPhone1.value;
	var vPhone2 = document.forms[0].txtPhone2.value;
	var vMobile1 = document.forms[0].txtMobile1.value;
	var vMobile2 = document.forms[0].txtMobile2.value;
	if (trim(vCustomerName) == '' || !vCustomerNameRegex.test(vCustomerName)) {
		alert('Please provide a valid access point name.');
		document.forms[0].txtAccessName.focus();
	} else if (trim(vBillPre) == '' || !vBillPreRegex.test(vBillPre)) {
		alert('Please provide a valid bill Prefix.');
		document.forms[0].txtBillPre.focus();
	}  else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vCity) == '' || !vCustomerRegex.test(vCity)) {
		alert('Please provide a valid city.');
		document.forms[0].txtCity.focus();
	} else if (!vCustomerRegex.test(vDistrict)) {
		alert('Please provide a valid district.');
		document.forms[0].txtDistrict.focus();
	} else if (vState == '--') {
		alert('Please provide a valid state.');
		document.forms[0].txtState.focus();
	} else if (!vPinStdPhMoRegex.test(vPinCode)) {
		alert('Please provide a valid pin code.');
		document.forms[0].txtPinCode.focus();
	} else if (!vPinStdPhMoRegex.test(vStdCode)) {
		alert('Please provide a valid std code.');
		document.forms[0].txtStdCode.focus();
	} else if (!vPinStdPhMoRegex.test(vPhone1)) {
		alert('Please provide a valid phone number.');
		document.forms[0].txtPhone1.focus();
	} else if (!vPinStdPhMoRegex.test(vPhone2)) {
		alert('Please provide a valid phone number.');
		document.forms[0].txtPhone2.focus();
	} else if (!vPinStdPhMoRegex.test(vMobile1)) {
		alert('Please provide a valid mobile number.');
		document.forms[0].txtMobile1.focus();
	} else if (!vPinStdPhMoRegex.test(vMobile2)) {
		alert('Please provide a valid mobile number.');
		document.forms[0].txtMobile2.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ACCESS_POINT_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_ACCESS_POINT';
		fnFinalSubmit();
	}
}