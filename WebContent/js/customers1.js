
var vCustomerNameRegex = /^[a-zA-Z0-9_&\/@. ]{1,100}$/;
var vCustomerRegex = /^[a-zA-Z0-9_&\/@. ]{0,50}$/;
var vCustomerTinRegex = /^[a-zA-Z0-9]{0,50}$/;
var vPinStdPhMoRegex = /^\d{0,15}$/;
var vAlphabetRegex = /^[a-zA-Z ]{1,50}$/;
var vEmailRegex = /^[a-z0-9_\+-]+(\.[a-z0-9_\+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2,4})$/;
var vWebsiteRegex = /^(http:\/\/)?(www.)?([a-z0-9_-])+\.[a-z]{2,4}(\/[a-z0-9_.\/#?&=]*)?$/i;

function fnSaveCustomer(){
	var vCustomerName = document.forms[0].txtCustomerName.value;
	var vCustomerType = document.forms[0].sCustomerType.value;
	var vAddress = document.forms[0].taAddress.value;
	var vArea = document.forms[0].txtArea.value;
	var vPinCode = document.forms[0].txtPinCode.value;
	var vStdCode = document.forms[0].txtStdCode.value;
	var vPhone1 = document.forms[0].txtPhone1.value;
	var vPhone2 = document.forms[0].txtPhone2.value;
	var vMobile1 = document.forms[0].txtMobile1.value;
	var vMobile2 = document.forms[0].txtMobile2.value;
	var vEmail = document.forms[0].txtEmail.value;
	var vWebsite = document.forms[0].txtWebsite.value;
	var vCity = document.forms[0].txtCity.value;
	var vTin = document.forms[0].txtTin.value;
	var vTransport = document.forms[0].txtTransport.value;
	if (trim(vCustomerName) == '' || !vCustomerNameRegex.test(vCustomerName)) {
		alert('Please provide a valid customer name.');
		document.forms[0].txtCustomerName.focus();
	} else if (vCustomerType == "--") {
		alert('Please provide a valid customer type.');
		document.forms[0].sCustomerType.focus();
	} else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vArea) == '' || !vCustomerRegex.test(vArea)) {
		alert('Please provide a valid area.');
		document.forms[0].txtArea.focus();
	} else if (trim(vCity) == '') {
		alert('Please provide a valid city.');
		document.forms[0].txtCity.focus();
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
	} else if (vEmail != "" && !vEmailRegex.test(vEmail)) {
		alert('Please provide a valid email.');
		document.forms[0].txtEmail.focus();
	} else if (vWebsite != "" && !vWebsiteRegex.test(vWebsite)) {
		alert('Please provide a valid website.');
		document.forms[0].txtWebsite.focus();
	} else if (!vCustomerTinRegex.test(vTin)) {
		alert('Please provide a valid tin/cst number.');
		document.forms[0].txtTin.focus();
	} else if (!vCustomerRegex.test(vTransport)) {
		alert('Please provide a valid transport.');
		document.forms[0].txtTransport.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_CUSTOMER';
		fnFinalSubmit();
	}
}

function fnGetCustomerDetails(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_CUSTOMER';
	fnFinalSubmit();	
}

function fnUpdateCustomerDetails(){
	var vCustomerName = document.forms[0].txtCustomerName.value;
	var vCustomerType = document.forms[0].sCustomerType.value;
	var vAddress = document.forms[0].taAddress.value;
	var vArea = document.forms[0].txtArea.value;
	var vCity = document.forms[0].txtCity.value;
	var vPinCode = document.forms[0].txtPinCode.value;
	var vStdCode = document.forms[0].txtStdCode.value;
	var vPhone1 = document.forms[0].txtPhone1.value;
	var vPhone2 = document.forms[0].txtPhone2.value;
	var vMobile1 = document.forms[0].txtMobile1.value;
	var vMobile2 = document.forms[0].txtMobile2.value;
	var vEmail = document.forms[0].txtEmail.value;
	var vWebsite = document.forms[0].txtWebsite.value;
	var vTin = document.forms[0].txtTin.value;
	var vTransport = document.forms[0].txtTransport.value;
	if (trim(vCustomerName) == '' || !vCustomerNameRegex.test(vCustomerName)) {
		alert('Please provide a valid customer name.');
		document.forms[0].txtCustomerName.focus();
	} else if (vCustomerType == "--") {
		alert('Please provide a valid customer type.');
		document.forms[0].sCustomerType.focus();
	} else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vArea) == '' || !vCustomerRegex.test(vArea)) {
		alert('Please provide a valid area.');
		document.forms[0].txtArea.focus();
	} else if (trim(vCity) == '') {
		alert('Please provide a valid city.');
		document.forms[0].txtCity.focus();
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
	} else if (vEmail != "" && !vEmailRegex.test(vEmail)) {
		alert('Please provide a valid email.');
		document.forms[0].txtEmail.focus();
	} else if (vWebsite != "" && !vWebsiteRegex.test(vWebsite)) {
		alert('Please provide a valid website.');
		document.forms[0].txtWebsite.focus();
	} else if (!vCustomerTinRegex.test(vTin)) {
		alert('Please provide a valid tin/cst number.');
		document.forms[0].txtTin.focus();
	} else if (!vCustomerRegex.test(vTransport)) {
		alert('Please provide a valid transport.');
		document.forms[0].txtTransport.focus();
	} else {
		document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_CUSTOMER';
		fnFinalSubmit();
	}
}

function fnChangePostalAddress(){
	var temp = "";
	temp = temp + ((document.forms[0].txtCustomerName.value != "") ? document.forms[0].txtCustomerName.value+"<BR/>" : "");
	temp = temp + ((document.forms[0].taAddress.value != "") ? document.forms[0].taAddress.value+"<BR/>" : "");
	temp = temp + ((document.forms[0].txtCity.value != "") ? document.forms[0].txtCity.value+"" : "");
	temp = temp + ((document.forms[0].txtPinCode.value != "") ? " - "+document.forms[0].txtPinCode.value+"<BR/>" : "<BR/>");
	temp = temp + ((document.forms[0].txtPhone1.value != "") ? "Phone : "+ ((document.forms[0].txtPhone2.value != "") ? document.forms[0].txtStdCode.value+" - "+document.forms[0].txtPhone1.value+" / "+document.forms[0].txtPhone2.value+"<BR/>" : document.forms[0].txtStdCode.value+" - "+document.forms[0].txtPhone1.value+"<BR/>") : "");
	temp = temp + ((document.forms[0].txtMobile1.value != "") ? "Mobile : "+ ((document.forms[0].txtMobile2.value != "") ? document.forms[0].txtMobile1.value+" / "+document.forms[0].txtMobile2.value : document.forms[0].txtMobile1.value) : "");
	document.getElementById("postalAdd").innerHTML = temp;
}