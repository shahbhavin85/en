
var vItemPriceRegex = /(^\d{0,10}[.]{1}\d{0,2}$)|(^\d{0,10}$)/;
var vCouponNoRegex = /^\d{1,3}$/;

function fnOfferChange(value){
	if(value == 1){
		document.getElementById("offerItem").style.display = "block";
	} else {
		document.getElementById("offerItem").style.display = "none";
	}
}

function fnAddItem(){
	var vItemId = document.forms[0].sItem1.value;
	var vItemNo = document.forms[0].sItem1[document.forms[0].sItem1.selectedIndex].innerHTML;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	for(var i=1; i<rowsInTable; i++){ 
		if(vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[1].innerHTML == vItemNo){
			alert('Item already added.');
			return;
		}
	}
	var vQty = document.forms[0].txtQuantity.value;
	if(!vCouponNoRegex.test(vQty)){
		alert('Please enter a valid quantity.');
		document.forms[0].txtQuantity.focus();
		return;
	}
	insertRow(vItemId,vItemNo,vQty);
}

function insertRow(vItemId,vItemNo,vQty){
	var vItemTable = document.getElementById('enqItem');
	row=document.createElement("TR");
	cell1 = document.createElement("TD");
	cell1.setAttribute("align", "center");
	cell2 = document.createElement("TD");
	cell2.setAttribute("align", "center");
	cell3 = document.createElement("TD");
	cell3.setAttribute("align", "center");
	cell4 = document.createElement("TD");
	cell4.setAttribute("align", "center");
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
	cell3.appendChild(textnode3);
	cell4.appendChild(buttonnode4);
	row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	row.appendChild(cell4);
	vItemTable.appendChild(row);
	document.forms[0].sItem1.selectedIndex = 0;
	document.forms[0].txtQuantity.value = "";
	document.forms[0].sItem1.focus();
}

function resetEqItem(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	for(var i=1; i<rowsInTable; i++)
		vItemTable.deleteRow(1);
	document.getElementById("offerItem").style.display = "none";
}

function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(row.rowIndex);
}



function validateAddOffer(){
	var vOfferPrice = document.forms[0].txtOfferPrice.value;
	var vFromDate = document.forms[0].txtFromDate.value;
	var vToDate = document.forms[0].txtToDate.value;
	if (trim(vOfferPrice) == '' || !vItemPriceRegex.test(vOfferPrice)) {
		alert('Please provide a offer price.');
		document.forms[0].txtOfferPrice.value = "";
		document.forms[0].txtOfferPrice.focus();
		return false;
	} else if(!checkdate(vFromDate, "From Date")){
		document.forms[0].txtFromDate.focus();
		return false;
	} else if(!checkdate(vToDate, "To Date")){
		document.forms[0].txtToDate.focus();
		return false;
	} else if(new Date(vFromDate.split("-")[1]+"-"+vFromDate.split("-")[0]+"-"+vFromDate.split("-")[2]) >
	 			new Date(vToDate.split("-")[1]+"-"+vToDate.split("-")[0]+"-"+vToDate.split("-")[2])){
		alert("To Date should be greater than From Date.");
		document.forms[0].txtToDate.focus();
		return false;
	}
	return true;
}

function checkdate(input, fieldName) {
	var validformat = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
	if (!validformat.test(input))
		alert("Invalid "+fieldName+" Format.");
	else { // Detailed check for valid date ranges
		var monthfield = input.split("-")[1];
		var dayfield = input.split("-")[0];
		var yearfield = input.split("-")[2];
		var dayobj = new Date(yearfield, monthfield - 1, dayfield);
		if ((dayobj.getMonth() + 1 != monthfield)
				|| (dayobj.getDate() != dayfield)
				|| (dayobj.getFullYear() != yearfield))
			alert("Invalid Day, Month, or Year range detected for "+fieldName+".");
		else
			returnval = true;
	}
	return returnval;
}

function fnSaveOffer(){
	if(validateAddOffer()){
		buildOfferItemData();
		document.forms[0].HANDLERS.value = 'OFFER_HANDLER';
		document.forms[0].ACTIONS.value = 'ADD_OFFER';
		fnFinalSubmit();
	}
}

function buildOfferItemData(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		itemdata = itemdata + vTR.getElementsByTagName("td")[0].innerHTML + "|" + vTR.getElementsByTagName("td")[1].innerHTML + "|" + vTR.getElementsByTagName("td")[2].innerHTML + "#";
	}
	document.forms[0].txtOfferItems.value = itemdata;
}

function fnGetOfferDetails(){
	document.forms[0].HANDLERS.value = 'OFFER_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_OFFER';
	fnFinalSubmit();
}

function fnModifyOffer(){
	if(validateAddOffer()){
		buildOfferItemData();
		document.forms[0].HANDLERS.value = 'OFFER_HANDLER';
		document.forms[0].ACTIONS.value = 'UPDT_OFFER';
		fnFinalSubmit();
	}
}

function fnGoToList(){
	document.forms[0].HANDLERS.value = 'OFFER_HANDLER';
	document.forms[0].ACTIONS.value = 'INIT_OFFER_LIST';
	fnFinalSubmit();
}