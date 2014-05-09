
var vCustomerNameRegex = /^[a-zA-Z0-9_&\/@. ]{1,100}$/;
var vCustomerRegex = /^[a-zA-Z0-9_&\/@. ]{0,50}$/;
var vCustomerTinRegex = /^[a-zA-Z0-9]{1,50}$/;
var vPinStdPhMoRegex = /^\d{10}$/;
var vAlphabetRegex = /^[a-zA-Z ]{1,50}$/;
var vWebsiteRegex = /^(http:\/\/)?(www.)?([a-z0-9_-])+\.[a-z]{2,4}(\/[a-z0-9_.\/#?&=]*)?$/i;
var vUserIdRegex = /^[a-zA-Z0-9_.]{5,20}$/;
var vEmailRegex = /^[A-Za-z0-9_\+-]+(\.[A-Za-z0-9_\+-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.([A-Za-z]{2,4})$/;

function fnSaveUser(){
	var vUserId = document.forms[0].txtUserId.value;
	var vPassword = document.forms[0].txtPassword.value;
	var vUserName = document.forms[0].txtFullName.value;
//	var vAddress = document.forms[0].taAddress.value;
//	var vPresentAddress = document.forms[0].taPresentAddress.value;
//	var vStdCode = document.forms[0].txtStdCode.value;
//	var vPhone1 = document.forms[0].txtPhone1.value;
//	var vPhone2 = document.forms[0].txtPhone2.value;
	var vMobile1 = document.forms[0].txtMobile1.value;
	var vEmail1 = document.forms[0].txtEmail1.value;
//	var vMobile2 = document.forms[0].txtMobile2.value;
	if (!vUserIdRegex.test(vUserId)) {
		alert('Please provide a valid user id.');
		document.forms[0].txtUserId.focus();
	} else if (vPassword.length < 8 || vPassword.indexOf(" ") != -1) {
		alert('Password should be minimum 8 characters long and space is not allowed.');
		document.forms[0].txtPassword.focus();
	} else if (trim(vUserName) == '' || !vCustomerNameRegex.test(vUserName)) {
		alert('Please provide a valid user name.');
		document.forms[0].txtFullName.focus();
	} else if (document.forms[0].txtManagerId.value == "--") {
		alert('Please select a manager id.');
		document.forms[0].txtManagerId.focus();
	} else if (document.forms[0].sBranch.value == "--") {
		alert('Please select a branch.');
		document.forms[0].sBranch.focus();
//	} else if (vAddress.length > 250) {
//		alert('Address should be less than 250 characters.');
//		document.forms[0].taPresentAddress.focus();
//	} else if (vPresentAddress.length > 250) {
//		alert('Address should be less than 250 characters.');
//		document.forms[0].taAddress.focus();
//	} else if (!vPinStdPhMoRegex.test(vStdCode)) {
//		alert('Please provide a valid std code.');
//		document.forms[0].txtStdCode.focus();
//	} else if (!vPinStdPhMoRegex.test(vPhone1)) {
//		alert('Please provide a valid phone number.');
//		document.forms[0].txtPhone1.focus();
//	} else if (!vPinStdPhMoRegex.test(vPhone2)) {
//		alert('Please provide a valid phone number.');
//		document.forms[0].txtPhone2.focus();
	} else if (!vPinStdPhMoRegex.test(vMobile1)) {
		alert('Please provide a valid official mobile number.');
		document.forms[0].txtMobile1.focus();
//	} else if (!vPinStdPhMoRegex.test(vMobile2)) {
//		alert('Please provide a valid mobile number.');
//		document.forms[0].txtMobile2.focus();
	} else if (vEmail1 == "" || !vEmailRegex.test(vEmail1)) {
		alert('Please provide a valid email.');
		document.forms[0].txtEmail1.focus();
	}  else {
		document.forms[0].HANDLERS.value = 'USER_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_USER';
		fnFinalSubmit();
	}
}

function fnGetUserDetails(){
	document.forms[0].HANDLERS.value = 'USER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_USER';
	fnFinalSubmit();	
}

function fnUpdateUserDetails(){
	var vUserName = document.forms[0].txtFullName.value;
//	var vAddress = document.forms[0].taAddress.value;
//	var vPresentAddress = document.forms[0].taPresentAddress.value;
//	var vStdCode = document.forms[0].txtStdCode.value;
//	var vPhone1 = document.forms[0].txtPhone1.value;
//	var vPhone2 = document.forms[0].txtPhone2.value;
	var vMobile1 = document.forms[0].txtMobile1.value;
	var vEmail1 = document.forms[0].txtEmail1.value;
//	var vMobile2 = document.forms[0].txtMobile2.value;
	if (trim(vUserName) == '' || !vCustomerNameRegex.test(vUserName)) {
		alert('Please provide a valid user name.');
		document.forms[0].txtFullName.focus();
	} else if (document.forms[0].txtManagerId.value == "--") {
		alert('Please select a manager id.');
		document.forms[0].txtManagerId.focus();
	} else if (document.forms[0].sBranch.value == "--") {
		alert('Please select a branch.');
		document.forms[0].sBranch.focus();
//	} else if (vAddress.length > 250) {
//		alert('Address should be less than 250 characters.');
//		document.forms[0].taPresentAddress.focus();
//	} else if (vPresentAddress.length > 250) {
//		alert('Address should be less than 250 characters.');
//		document.forms[0].taAddress.focus();
//	} else if (!vPinStdPhMoRegex.test(vStdCode)) {
//		alert('Please provide a valid std code.');
//		document.forms[0].txtStdCode.focus();
//	} else if (!vPinStdPhMoRegex.test(vPhone1)) {
//		alert('Please provide a valid phone number.');
//		document.forms[0].txtPhone1.focus();
//	} else if (!vPinStdPhMoRegex.test(vPhone2)) {
//		alert('Please provide a valid phone number.');
//		document.forms[0].txtPhone2.focus();
	} else if (!vPinStdPhMoRegex.test(vMobile1)) {
		alert('Please provide a valid official mobile number.');
		document.forms[0].txtMobile1.focus();
//	} else if (!vPinStdPhMoRegex.test(vMobile2)) {
//		alert('Please provide a valid mobile number.');
//		document.forms[0].txtMobile2.focus();
	} else if (vEmail1 == "" || !vEmailRegex.test(vEmail1)) {
		alert('Please provide a valid official email.');
		document.forms[0].txtEmail1.focus();
	}  else {
		document.forms[0].HANDLERS.value = 'USER_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_USER';
		fnFinalSubmit();
	}
}