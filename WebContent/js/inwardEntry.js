function fnChangeItemNo(ele){ 
	document.forms[0].sItem.value = ele.value;
	document.forms[0].txtRate.value = ele.options[ele.selectedIndex].id;
}

function fnChangeItemName(ele){
	document.forms[0].sItemName.value = ele.value;
	document.forms[0].txtRate.value = ele.options[ele.selectedIndex].id;
}

var vQtyRegex = /(^\d{1,5}[.]{1}\d{0,2}$)|(^\d{1,5}$)/;
var vRateRegex = /(^\d{1,10}[.]{1}\d{0,2}$)|(^\d{1,10}$)/;
var rowNo = 1;

function fnAddItem(){
	var check = fnCheckMasterValue();
	if(check){
		if(document.forms[0].sItem.value == '--'){
			alert('Please select the item.');
			return;
		}
		var vItemId = document.forms[0].sItem.value;
		var vItemNo = document.forms[0].sItem[document.forms[0].sItem.selectedIndex].innerHTML;
		var vQty = document.forms[0].txtQuantity.value;
		var vRate = document.forms[0].txtRate.value;
		var vDuty = document.forms[0].txtDuty.value;
		var vCBM = document.forms[0].txtItemCBM.value;
		var vTotalCBM = parseFloat(document.forms[0].txtCBM.value);
		var vExchangeRate = parseFloat(document.forms[0].txtExchangeRate.value);
		var vSourceExp = parseFloat(document.forms[0].txtSourceExp.value);
		var vDestinationExp = parseFloat(document.forms[0].txtDestinationExp.value);
		var vItemTable = document.getElementById('enqItem');
		var rowsInTable = vItemTable.rows.length;
		/*if(rowsInTable == 15){
			alert('Bill can contain a maximum of 10 items');
			return;
		}*/
		var vTableCBM = 0;
		for(var i=1; i<rowsInTable; i++){ 
			if(vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[1].innerHTML == vItemNo){
				alert('Item already added.');
				return;
			}
		}
		if(!vQtyRegex.test(vQty)){
			alert('Please enter a valid quantity.');
			document.forms[0].txtQuantity.focus();
			return;
		}  
		if(!vRateRegex.test(vRate)){
			alert('Please enter a valid rate.');
			document.forms[0].txtRate.focus();
			return;
		}  
		if(!vRateRegex.test(vDuty)){
			alert('Please enter a valid duty.');
			document.forms[0].txtDuty.focus();
			return;
		} 
		if(!vRateRegex.test(vCBM)){
			alert('Please enter a valid CBM.');
			document.forms[0].txtItemCBM.focus();
			return;
		} 
		for(var i=1; i<rowsInTable; i++){ 
			vTableCBM = vTableCBM + parseFloat(vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[6].innerHTML);
		}
		if(parseFloat(vTableCBM)+parseFloat(vCBM) > vTotalCBM){
			alert('CBM is greater than total CBM.');
			document.forms[0].txtItemCBM.focus();
			return;
		}
		fnChangeMasterStatus(true);
		vRate = (parseFloat(vRate)*vExchangeRate).toFixed(2);
		vCBM = parseFloat(vCBM);
		vDuty = parseFloat(vDuty);
		vQty = parseFloat(vQty);
		var vChinaExp = (vTotalCBM == 0) ? 0 : parseFloat((vCBM/vTotalCBM)*vExchangeRate*vSourceExp).toFixed(2);
		var vIndiaExp = (vTotalCBM == 0) ? 0 : parseFloat((vCBM/vTotalCBM)*vDestinationExp).toFixed(2);
		var vCP = (parseFloat(vRate)+((vDuty+parseFloat(vChinaExp)+parseFloat(vIndiaExp))/vQty)).toFixed(2);
		row = vItemTable.insertRow(rowsInTable); 
		row.setAttribute("id",rowNo);
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
		var cell6 = row.insertCell(5);
		cell6.setAttribute("align", "center");
		var cell7 = row.insertCell(6);
		cell7.setAttribute("align", "center");
		var cell8 = row.insertCell(7);
		cell8.setAttribute("align", "center");
		var cell9 = row.insertCell(8);
		cell9.setAttribute("align", "center");
		var cell10 = row.insertCell(9);
		cell10.setAttribute("align", "center");
		textnode1=document.createTextNode(vItemId);
		textnode2=document.createTextNode(vItemNo);
		textnode3=document.createTextNode(vQty);
		textnode4=document.createTextNode(vRate);
		textnode5=document.createTextNode(vDuty);
		textnode6=document.createTextNode(vCBM);
		textnode7=document.createTextNode(vChinaExp);
		textnode8=document.createTextNode(vIndiaExp);
		textnode9=document.createTextNode(vCP);
		buttonnode4=document.createElement("INPUT");
		buttonnode4.setAttribute("type", "button");
		buttonnode4.setAttribute("value", "Delete");
		buttonnode4.setAttribute("onclick", "fnDeleteItem("+rowNo+");");
		cell1.appendChild(buttonnode4);
		cell2.appendChild(textnode1);
		cell3.appendChild(textnode2);
		cell4.appendChild(textnode3);
		cell5.appendChild(textnode4);
		cell6.appendChild(textnode5);
		cell7.appendChild(textnode6);
		cell8.appendChild(textnode7);
		cell9.appendChild(textnode8);
		cell10.appendChild(textnode9);
		rowNo++;
		document.forms[0].sItem.value = "--";
		document.forms[0].sItemName.value = "--";
		document.forms[0].txtQuantity.value = "";
		document.forms[0].txtRate.value = "";
		document.forms[0].txtItemCBM.value = "";
		document.forms[0].txtDuty.value = "";
		fnRefreshTotal();
		document.forms[0].sItemName.focus();
	}
	return;
}

function fnRefreshTotal(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var total = 0;
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		total = total + parseFloat(vTR.getElementsByTagName("td")[9].innerHTML);
	}
	/*var vInstall = parseFloat(document.forms[0].txtInstall.value);
	var vPacking = parseFloat(document.forms[0].txtPacking.value);
	var vForward = parseFloat(document.forms[0].txtForwarding.value);
	var vLess = parseFloat(document.forms[0].txtLess.value);
	total = total + vInstall + vPacking + vForward - vLess;*/
	document.getElementById("gTotal").innerHTML = (parseFloat(total)).toFixed(2);
}

function fnCheckMasterValue(){
	var iCBM = document.forms[0].txtCBM.value;
	var iExchangeRate = document.forms[0].txtExchangeRate.value;
	var iChinExpenses = document.forms[0].txtSourceExp.value;
	var iIndiaExpenses = document.forms[0].txtDestinationExp.value;
	
	if(trim(iCBM) == ""){
		alert('Please give the Total CBM');
		document.forms[0].txtCBM.focus();
		return false;
	}
	
	if(trim(iExchangeRate) == ""){
		alert('Please give the Exchange Rate');
		document.forms[0].txtExchangeRate.focus();
		return false;
	}
	
	if(trim(iChinExpenses) == ""){
		alert('Please give the China Expenses');
		document.forms[0].txtSourceExp.focus();
		return false;
	}
	
	if(trim(iIndiaExpenses) == ""){
		alert('Please give the India Expenses');
		document.forms[0].txtDestinationExp.focus();
		return false;
	}
	
	return true;
}

function fnChangeMasterStatus(state){
//	alert(state);
	document.forms[0].txtCBM.readOnly = state;
	document.forms[0].txtExchangeRate.readOnly = state;
	document.forms[0].txtSourceExp.readOnly = state;
	document.forms[0].txtDestinationExp.readOnly = state;
}

function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(document.getElementById(row).rowIndex);
	if(vItemTable.rows.length == 1){
		fnChangeMasterStatus(false);
	}
	fnRefreshTotal();
}

var isClick = false;

function fnGenerateBill(){
	document.forms[0].genBill.disabled = true;
	if(isClick){
		isClick = false;
	} else {
		isClick = true;
	}
	setTimeout('fnFinalGenerateBill()',100);
}

function fnFinalGenerateBill(){
	if(isClick && document.forms[0].genBill.disabled == true){
		var vItemTable = document.getElementById('enqItem');
		var rowsInTable = vItemTable.rows.length;
		if(document.forms[0].txtBENo.value == '--'){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please give the BE No');
			document.forms[0].txtBENo.focus();
			return;
		}
		if(rowsInTable == 1){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please enter the item details');
			return;
		}
		buildSalesItemData();
//		document.forms[0].genBill.disabled = false;
		document.forms[0].HANDLERS.value = "INWARD_ENTRY_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_INWARD_ENTRY";
		fnFinalSubmit();
	}
}

function buildSalesItemData(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		var idx = new Array(1,2,3,4,5,6);
		for(var j=0; j<idx.length; j++){
			itemdata = itemdata + vTR.getElementsByTagName("td")[idx[j]].innerHTML;
			if(j!=5){
				itemdata = itemdata + "|";
			} else {
				itemdata = itemdata + "#";
			}
		}
	}
//	alert(itemdata);
	document.forms[0].txtSalesItems.value = itemdata;
}

function editEntry(entryNo){
	document.forms[0].txtInvoiceNo.value = entryNo;
	document.forms[0].HANDLERS.value = "INWARD_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_INWARD_ENTRY";
	fnFinalSubmit();
}

function fnEditBill(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(document.forms[0].txtBENo.value == '--'){
		document.forms[0].genBill.disabled = false;
		isClick = false;
		alert('Please give the BE No');
		document.forms[0].txtBENo.focus();
		return;
	}
	if(rowsInTable == 1){
		document.forms[0].genBill.disabled = false;
		isClick = false;
		alert('Please enter the item details');
		return;
	}
	buildSalesItemData();
//		document.forms[0].genBill.disabled = false;
	document.forms[0].HANDLERS.value = "INWARD_ENTRY_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_INWARD_ENTRY";
	fnFinalSubmit();
}

function fnCalculate(){
	var totalCBM = document.forms[0].txtCBM.value;
	if(!vRateRegex.test(totalCBM)){
		alert('Please enter a valid CBM.');
		document.forms[0].txtCBM.focus();
		return;
	} 
	totalCBM = parseFloat(totalCBM);
	var tempDuty = document.forms[0].tempDuty.value;
	if(!vRateRegex.test(tempDuty)){
		alert('Please enter a valid CBM.');
		document.forms[0].tempDuty.focus();
		return;
	} 
	tempDuty = parseFloat(tempDuty);
	var tempCBM = document.forms[0].tempCBM.value;
	if(!vRateRegex.test(tempCBM)){
		alert('Please enter a valid CBM.');
		document.forms[0].tempCBM.focus();
		return;
	} 
	tempCBM = parseFloat(tempCBM);
	var tempQty = document.forms[0].tempQty.value;
	if(!vRateRegex.test(tempQty)){
		alert('Please enter a valid CBM.');
		document.forms[0].tempQty.focus();
		return;
	} 
	tempQty = parseFloat(tempQty);
	var tempItm = document.forms[0].tempItm.value;
	if(!vRateRegex.test(tempItm)){
		alert('Please enter a valid CBM.');
		document.forms[0].tempItm.focus();
		return;
	} 
	tempItm = parseFloat(tempItm);
	var vItmCBM = (tempCBM*tempItm/tempQty).toFixed(2);
	var vItmDuty = (tempDuty*tempItm/tempQty).toFixed(2);
	document.getElementById("lblCBM").innerHTML = vItmCBM;
	document.getElementById("lblDuty").innerHTML = vItmDuty;
}