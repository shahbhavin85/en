
/*var vCustomerNameRegex = /^[a-zA-Z0-9_&\/@. ]{1,100}$/;
var vCustomerRegex = /^[a-zA-Z0-9_&\/@. ]{0,50}$/;*/
var vCustomerTinRegex = /^[a-zA-Z0-9]{0,50}$/;
var vCustomerNameRegex = /^[a-zA-Z0-9 ]{0,50}$/;
var vPinStdPhMoRegex = /^\d{0,15}$/;
var vAlphabetRegex = /^[a-zA-Z ]{1,50}$/;
var vEmailRegex = /^[A-Za-z0-9_\+-]+(\.[A-Za-z0-9_\+-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.([A-Za-z]{2,4})$/;
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
//	var vTransport = document.forms[0].txtTransport.value;
	if (trim(vCustomerName) == '' || !vCustomerNameRegex.test(vCustomerName)) {
		alert('Please provide a valid customer name.(Customer Name cannot contain . or -)');
		document.forms[0].txtCustomerName.focus();
	} else if (vCustomerType == "--") {
		alert('Please provide a valid customer type.');
		document.forms[0].sCustomerType.focus();
	} else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vArea) == '') {
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
	} else {
		document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
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
//	var vTransport = document.forms[0].txtTransport.value;
	if (trim(vCustomerName) == '') {
		alert('Please provide a valid customer name.');
		document.forms[0].txtCustomerName.focus();
	} else if (vCustomerType == "--") {
		alert('Please provide a valid customer type.');
		document.forms[0].sCustomerType.focus();
	} else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vArea) == '') {
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
	} else {
		document.forms[0].HANDLERS.value = 'CUSTOMER_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_CUSTOMER';
		fnFinalSubmit();
	}
}

function fnGoToGroup(){
	document.forms[0].HANDLERS.value = 'CUSTOMER_GRP_HANDLER';
	document.forms[0].ACTIONS.value = '';
	fnFinalSubmit();
}

function fnOpenCityPopup(){
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOn';
	document.documentElement.style.overflow = "hidden";
	document.getElementById("taskWindow").style.display = 'block';
	return;
//	window.showModalDialog('/en/app?HANDLERS=CUSTOMER_HANDLER','','');
}

function fnCancel(){
	document.documentElement.style.overflow = "visible";
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOff';
	document.getElementById("taskWindow").style.display = 'none';
	return;	
}

function fnOpenTypePopup(){
	window.showModalDialog('/en/app?HANDLERS=CUSTOMER_HANDLER&ACTIONS=ADD_CUST_TYPE','','');
}

function fnAddCity(value,text){
	var opt = document.createElement("option");
	document.forms[0].txtCity.options.add(opt);
    opt.text = text;
    opt.value = value;
    opt.selected = 'selected';
    document.forms[0].txtCity.focus();
}

function fnAddCustomerType(value,text){
	var opt = document.createElement("option");
	document.forms[0].sCustomerType.options.add(opt);
    opt.text = text;
    opt.value = value;
    opt.selected = 'selected';
    document.forms[0].sCustomerType.focus();
}



function fnGetCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForCustList);
	} else {
   		document.getElementById("list").style.display = 'none';
	}
}

function fnCallBackForCustList(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	var t = "";
    	for(var i=0; i<retObj.list.length; i++){
    		t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" )'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnChangePostalAddress(){
	var temp = "";
	temp = temp + "<font style='background-color: aqua;'>"+((document.forms[0].txtCustomerName.value != "") ? document.forms[0].txtCustomerName.value+"</font><BR/>" : "");
	temp = temp + "<font style='background-color: #ffffcc;'>"+((document.forms[0].taAddress.value != "") ? document.forms[0].taAddress.value+"</font><BR/>" : "");
	temp = temp + "<font style='background-color: #99dddd;'>"+((document.forms[0].txtCity.value != "") ? document.forms[0].txtCity.value+"" : "");
	temp = temp + "<font style='background-color: #99dddd;'>"+((document.forms[0].txtPinCode.value != "") ? " - "+document.forms[0].txtPinCode.value+"</font><BR/>" : "</font><BR/>");
	temp = temp + "<font style='background-color: #ccaa44;'>"+((document.forms[0].txtPhone1.value != "") ? "Phone : "+ ((document.forms[0].txtPhone2.value != "") ? document.forms[0].txtStdCode.value+" - "+document.forms[0].txtPhone1.value+" / "+document.forms[0].txtPhone2.value+"</font><BR/>" : document.forms[0].txtStdCode.value+" - "+document.forms[0].txtPhone1.value+"</font><BR/>") : "");
	temp = temp + "<font style='background-color: #ccff66;'>"+((document.forms[0].txtMobile1.value != "") ? "Mobile : "+ ((document.forms[0].txtMobile2.value != "") ? document.forms[0].txtMobile1.value+" / "+document.forms[0].txtMobile2.value : document.forms[0].txtMobile1.value) : "");
	document.getElementById("postalAdd").innerHTML = temp;
}

function fnOpenChkClicked(t){
	document.forms[0].txtOpenBal.readOnly = (!t.checked) ;
}

function fnNewCountry(n){
	document.forms[0].district.value = '';
	document.forms[0].state.value = '--';
	document.forms[0].newCountry.value = '';
	if(n==1){
		document.forms[0].district.disabled = true;
		document.forms[0].state.disabled = true;
		document.forms[0].newCountry.disabled = false;
	} else {
		document.forms[0].district.disabled = false;
		document.forms[0].state.disabled = false;
		document.forms[0].newCountry.disabled = true;
	}
}

function fnSaveCity(){
	var urlString = "/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=ADD_CITY";
	if(document.forms[0].rNewCountry[0].checked){
		urlString = urlString + "&city="+document.forms[0].city.value+"&district="+document.forms[0].district.value+"&state="+document.forms[0].state.value;
	} else {
		urlString = urlString + "&city="+document.forms[0].city.value+"&country="+document.forms[0].newCountry.value;
	}
//	alert(urlString);
	fnAjaxTxn(urlString+"&temp="+Math.random() ,fnCallBackForSaveCity);
}

function fnCallBackForSaveCity(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		var temp = xmlhttp.responseText;
    	var retObj = JSON.parse(temp);
    	fnAddCity(retObj.city,retObj.city);
    	if(retObj.country){
    		fnAddCountry(retObj.country, retObj.country);
    	}
    	fnRefreshCityWindow();
    	fnCancel();
    }
}

function fnRefreshCityWindow(){
	document.forms[0].rNewCountry[0].checked = true;
	document.forms[0].city.value = '';
	document.forms[0].district.value = '';
	document.forms[0].state.value = '--';
	document.forms[0].newCountry.value = '';
	document.forms[0].district.disabled = false;
	document.forms[0].state.disabled = false;
	document.forms[0].newCountry.disabled = true;
}

function fnAddCountry(value,text){
	var opt = document.createElement("option");
	document.forms[0].state.options.add(opt);
    opt.text = text;
    opt.value = value;
}