function fnNewCustomer(){
	var newFormStatus = false;
	var enquiryFormStatus = false;
	if(document.forms[0].newCustomer.checked){
		newFormStatus = false;
		enquiryFormStatus = true;		
	} else {
		newFormStatus = true;
		enquiryFormStatus = false;
	}
	
	document.forms[0].txtCustomerName.disabled = newFormStatus;
	document.forms[0].taAddress.disabled = newFormStatus;
	document.forms[0].txtCity.disabled = newFormStatus;
	document.forms[0].txtDistrict.disabled = newFormStatus;
	document.forms[0].txtState.disabled = newFormStatus;
	document.forms[0].txtPinCode.disabled = newFormStatus;
	document.forms[0].txtStdCode.disabled = newFormStatus;
	document.forms[0].txtPhone1.disabled = newFormStatus;
	document.forms[0].txtPhone2.disabled = newFormStatus;
	document.forms[0].txtMobile1.disabled = newFormStatus;
	document.forms[0].txtMobile2.disabled = newFormStatus;
	document.forms[0].txtEmail.disabled = newFormStatus;
	document.forms[0].txtWebsite.disabled = newFormStatus;
	document.forms[0].txtTin.disabled = newFormStatus;
	document.forms[0].txtTransport.disabled = newFormStatus;
	document.forms[0].sGrade.disabled = newFormStatus;
	document.forms[0].sCustomerType.disabled = newFormStatus;
	document.forms[0].btnSave.disabled = newFormStatus;
	document.forms[0].btnReset.disabled = newFormStatus;
	
	document.forms[0].sCustomer.disabled = enquiryFormStatus;
	document.forms[0].sPriority.disabled = enquiryFormStatus;
	document.forms[0].sReference.disabled = enquiryFormStatus;
	document.forms[0].btnNext.disabled = enquiryFormStatus;
}

var vCustomerNameRegex = /^[a-zA-Z0-9_&\/@. ]{1,100}$/;
var vCustomerRegex = /^[a-zA-Z0-9_&\/@. ]{0,50}$/;
var vCustomerTinRegex = /^[a-zA-Z0-9]{1,50}$/;
var vPinStdPhMoRegex = /^\d{0,15}$/;
var vAlphabetRegex = /^[a-zA-Z ]{1,50}$/;
var vEmailRegex = /^[a-z0-9_\+-]+(\.[a-z0-9_\+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*\.([a-z]{2,4})$/;
var vWebsiteRegex = /^(http:\/\/)?(www.)?([a-z0-9_-])+\.[a-z]{2,4}(\/[a-z0-9_.\/#?&=]*)?$/i;

function fnSaveCustomer(){
	var vCustomerName = document.forms[0].txtCustomerName.value;
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
	var vEmail = document.forms[0].txtEmail.value;
	var vWebsite = document.forms[0].txtWebsite.value;
	var vTin = document.forms[0].txtTin.value;
	var vTransport = document.forms[0].txtTransport.value;
	if (trim(vCustomerName) == '' || !vCustomerNameRegex.test(vCustomerName)) {
		alert('Please provide a valid customer name.');
		document.forms[0].txtCustomerName.focus();
	} else if (vAddress.length > 250) {
		alert('Address should be less than 250 characters.');
		document.forms[0].taAddress.focus();
	} else if (trim(vCity) == '' || !vCustomerRegex.test(vCity)) {
		alert('Please provide a valid city.');
		document.forms[0].txtCity.focus();
	} else if (!vCustomerRegex.test(vDistrict)) {
		alert('Please provide a valid district.');
		document.forms[0].txtDistrict.focus();
	} else if (!vCustomerRegex.test(vState)) {
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
	} else if (vEmail != "" && !vEmailRegex.test(vEmail)) {
		alert('Please provide a valid email.');
		document.forms[0].txtEmail.focus();
	} else if (vWebsite != "" && !vWebsiteRegex.test(vWebsite)) {
		alert('Please provide a valid website.');
		document.forms[0].txtWebsite.focus();
	} else if (trim(vTin) == '' || !vCustomerTinRegex.test(vTin)) {
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

function fnTab2Next(){
	if(document.forms[0].sCustomer.value != '--'){
		document.getElementById("lblCustomer3").innerHTML = document.forms[0].sCustomer[document.forms[0].sCustomer.selectedIndex].innerHTML;
		document.getElementById("lblPriority3").innerHTML = document.forms[0].sPriority[document.forms[0].sPriority.selectedIndex].innerHTML;
		document.getElementById("lblReference3").innerHTML = document.forms[0].sReference[document.forms[0].sReference.selectedIndex].innerHTML;
		if(document.getElementById("message")){
			document.getElementById("message").style.display = 'none';
		}
		document.getElementById('tab').tabber.tabShow(2);
	} else {
		alert('Please select a customer.');
	}
}


function filterOfferData() {
	var vFNo = (document.forms[0].fNo.value).trim().toLowerCase();
	var vFName = (document.forms[0].fName.value).trim().toLowerCase();
    try {
        rows = document.getElementById('offerData').getElementsByTagName("TR");
        for (var j = 3; j < rows.length; j++) {
            cells = rows[j].getElementsByTagName("TD");
            if((vFNo != '' && (cells[0].innerHTML).toLowerCase().indexOf(vFNo) == -1 && vFName == '') || (vFName != '' && (cells[1].innerHTML).toLowerCase().indexOf(vFName) == -1 && vFNo == '') || (vFNo != '' && vFName != ''&& ((cells[0].innerHTML).toLowerCase().indexOf(vFNo) == -1  || (cells[1].innerHTML).toLowerCase().indexOf(vFName) == -1))){
            	rows[j].style.display = 'none';
        	} else { 
        		if (rows[j].style.display == 'none')
        			rows[j].style.display = 'block';
        	}
        }
    }
    catch (er) {
        // Non-critical error
        alert(er.description);
    }
} 

function clearFilterOfferData(){
	document.forms[0].fNo.value = "";
	document.forms[0].fName.value = "";
	filterOfferData();
}

var vCouponNoRegex = /^\d{1,5}$/;

function fnAddItem(){
	var vItemId = document.forms[0].sItem.value;
	var vItemNo = document.forms[0].sItem[document.forms[0].sItem.selectedIndex].innerHTML;
	var vOfferId = '--';
	var vQty = document.forms[0].txtQuantity.value;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	for(var i=1; i<rowsInTable; i++){ 
		if(vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[1].innerHTML == vItemNo && 
				vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[3].innerHTML == vOfferId){
			alert('Item already added.');
			return;
		}
	}
	if(!vCouponNoRegex.test(vQty)){
		alert('Please enter a valid quantity.');
		document.forms[0].txtQuantity.focus();
		return;
	}           
	row = vItemTable.insertRow(rowsInTable); 
	var cell1 = row.insertCell(0); 
	cell1.setAttribute("align", "center");
	var cell2 = row.insertCell(1);
	cell2.setAttribute("align", "center");
	var cell3 = row.insertCell(2);
	cell3.setAttribute("align", "center");
	var cell4 = row.insertCell(3);
	cell4.setAttribute("align", "center");
	var cell5 = row.insertCell(4);
	cell5.setAttribute("align", "center");
	textnode1=document.createTextNode(vItemId);
	textnode2=document.createTextNode(vItemNo);
	textnode3=document.createTextNode(vQty);
	// textnode4=document.createTextNode(vQty);
	buttonnode4=document.createElement("INPUT");
	buttonnode4.setAttribute("type", "button");
	buttonnode4.setAttribute("value", "Delete");
	buttonnode4.setAttribute("onclick", "fnDeleteItem(row);");
	cell1.appendChild(textnode1);
	cell2.appendChild(textnode2);
	cell3.appendChild(document.createTextNode("--"));
	cell4.appendChild(textnode3);
	cell5.appendChild(buttonnode4);
	/*row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	row.appendChild(cell4);
	vItemTable.appendChild(row);*/
}

function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(row.rowIndex);
}

function fnTab1Next(){
	var vItemTable = document.getElementById('enqItem');
	var itemLen = vItemTable.rows.length; 
	if(itemLen > 1){
		var vEnqItem3 = document.getElementById("enqItem3");
		if(vEnqItem3.rows.length > 1){ 
			var vLen = vEnqItem3.rows.length;
			for(var k=1; k<vLen;k++){
				vEnqItem3.deleteRow(k);
			}
		}
        rows = document.getElementById('enqItem').getElementsByTagName("TR");
		for(var j=1;j<itemLen;j++){
            cells = rows[j].getElementsByTagName("TD");
			row=vEnqItem3.insertRow(j);
			var cell1 = row.insertCell(0);
			cell1.setAttribute("align", "center");
			var cell2 = row.insertCell(1);
			cell2.setAttribute("align", "center");
			var cell3 = row.insertCell(2);
			cell3.setAttribute("align", "center");
			var cell4 = row.insertCell(3);
			cell4.setAttribute("align", "center");
			textnode1=document.createTextNode(cells[0].innerHTML);
			textnode2=document.createTextNode(cells[1].innerHTML);
			textnode3=document.createTextNode(cells[2].innerHTML);
			textnode4=document.createTextNode(cells[3].innerHTML);
			cell1.appendChild(textnode1);
			cell2.appendChild(textnode2);
			cell3.appendChild(textnode3);
			cell4.appendChild(textnode4);
		}	
		document.getElementById('tab').tabber.tabShow(1);
	} else {
		alert('Please add interested items.');
		return;
	}
}

function fnForwardRequest(combo){
	document.getElementById("sAccessName").disabled =  !combo.checked;
}

function fnAppointment(combo){
	if(combo.checked){
		if(document.getElementById("sUserId").options.length == 1){
			combo.checked = false;
			alert('Please add some users.');
			return;
		}
	}
	document.getElementById("sUserId").disabled = !combo.checked;
	document.getElementById("txtAppointDate").disabled = !combo.checked;
	document.getElementById("txtAppointTime").disabled = !combo.checked;
}

function fnAddEnquiry(){
	if(checkAppointment() && document.getElementById("current_point").value != '--'){
		buildOfferItemData();
		document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_ENQUIRY';
		fnFinalSubmit();		
	}
	if(document.getElementById("current_point").value == '--'){
		alert('Please select the access point');
	}
}

function checkAppointment(){
	if(document.getElementById("appointment").checked){
		if(document.forms[0].sUserId.value == '--'){
			alert('Please select the user');
			return false;
		}
		if(document.forms[0].txtAppointDate.value == ""){
			alert('Please give an appointment date');
			return false;
		}
		if(document.forms[0].txtAppointTime.value == ""){
			alert('Please give an appointment time');
			return false;
		}
	}
	return true;
}

function checkForward(){
	if(document.getElementById("Forward").checked){
		
	}
	return true;
}

function fnGetOffers(){
//	alert(document.forms[0].sItem.value);
	window.showModalDialog('/en/app?HANDLERS=ENQUIRY_HANDLER&ACTIONS=GET_OFFER&ITEMID='+document.forms[0].sItem.value,'','');
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

function buildOfferItemData(){
	var vItemTable = document.getElementById('enqItem3');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		itemdata = itemdata + vTR.getElementsByTagName("td")[0].innerHTML + "|" + vTR.getElementsByTagName("td")[1].innerHTML + "|" + vTR.getElementsByTagName("td")[2].innerHTML + "|" + vTR.getElementsByTagName("td")[3].innerHTML + "#";
	}
	document.forms[0].txtEnqItem.value = itemdata;
}

var vEnqNoRegex = /^\d{0,10}$/;

function fnGetEnquiryDetails(){
	var vEnqNo = document.forms[0].txtEnqNo.value;
	if (trim(vEnqNo) == '' || !vEnqNoRegex.test(vEnqNo)) {
		alert('Please provide a valid enquiry no.');
		document.forms[0].txtEnqNo.focus();
	} else {
		document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
		document.forms[0].ACTIONS.value = 'GET_ENQUIRY';
		fnFinalSubmit();		
	}
}

function fnChangeItemNo(ele){
	document.forms[0].sItem.value = ele.value;
}

function fnChangeItemName(ele){
	document.forms[0].sItemName.value = ele.value;
}

function fnAddOfferItem(itemId,itemNo,offerId){
	var vItemId = itemId;
	var vItemNo = itemNo;
	var vQty = 1;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	for(var i=1; i<rowsInTable; i++){ 
		if(vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[1].innerHTML == vItemNo && 
				vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[3].innerHTML == offerId){
			alert('Item already added.');
			return;
		}
	}        
	row = vItemTable.insertRow(rowsInTable); 
	var cell1 = row.insertCell(0); 
	cell1.setAttribute("align", "center");
	var cell2 = row.insertCell(1);
	cell2.setAttribute("align", "center");
	var cell3 = row.insertCell(2);
	cell3.setAttribute("align", "center");
	var cell4 = row.insertCell(3);
	cell4.setAttribute("align", "center");
	var cell5 = row.insertCell(4);
	cell5.setAttribute("align", "center");
	textnode1=document.createTextNode(vItemId);
	textnode2=document.createTextNode(vItemNo);
	textnode3=document.createTextNode(vQty);
	// textnode4=document.createTextNode(vQty);
	buttonnode4=document.createElement("INPUT");
	buttonnode4.setAttribute("type", "button");
	buttonnode4.setAttribute("value", "Delete");
	buttonnode4.setAttribute("onclick", "fnDeleteItem(row);");
	cell1.appendChild(textnode1);
	cell2.appendChild(textnode2);
	cell3.appendChild(document.createTextNode(offerId));
	cell4.appendChild(textnode3);
	cell5.appendChild(buttonnode4);
}

function fnAddNewAction(){
	document.forms[0].HANDLERS.value = 'ENQUIRY_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_NEW_ACTION1';
	fnFinalSubmit();		
}

function fnPrintEnquiry(){
	window.open('/en/app?HANDLERS=ENQUIRY_HANDLER&ACTIONS=PRINT_ENQUIRY&txtEnqNo='+document.forms[0].txtEnqNo.value,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}