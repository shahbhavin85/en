function fnFocus(){
	document.forms[0].sTo.focus();
}

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
	if(document.forms[0].sItem.value == '--'){
		alert('Please select the item.');
		return;
	}
	var vItemId = document.forms[0].sItem.value;
	var vItemNo = document.forms[0].sItem[document.forms[0].sItem.selectedIndex].innerHTML;
	var vDesc = (document.forms[0].txtDesc.value == '') ? '--' : document.forms[0].txtDesc.value;
	var vQty = document.forms[0].txtQuantity.value;
	var vRate = document.forms[0].txtRate.value;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(rowsInTable == 25){
		alert('Bill can contain a maximum of 10 items');
		return;
	}
	/*for(var i=1; i<rowsInTable; i++){ 
		if(vItemTable.getElementsByTagName("tr")[i].getElementsByTagName("td")[1].innerHTML == vItemNo){
			alert('Item already added.');
			return;
		}
	}*/
	if(!vQtyRegex.test(vQty)){
		alert('Please enter a valid quantity.');
		document.forms[0].txtQuantity.focus();
		return;
	}  
	if(!vRateRegex.test(vRate) || parseFloat(vRate) == 0){
		alert('Please enter a valid rate.');
		document.forms[0].txtRate.focus();
		return;
	}  
	vRate = parseFloat(vRate);
	var vAmt = parseFloat(vQty) * vRate;           
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
	textnode1=document.createTextNode(vItemId);
	textnode2=document.createTextNode(vItemNo);
	textnode3=document.createTextNode(vDesc);
	textnode4=document.createTextNode(vQty);
	textnode5=document.createTextNode(vRate);
	textnode6=document.createTextNode(vAmt);
	// textnode4=document.createTextNode(vQty);
	buttonnode4=document.createElement("INPUT");
	buttonnode4.setAttribute("type", "button");
	buttonnode4.setAttribute("value", "Delete");
	buttonnode4.setAttribute("onclick", "fnDeleteItem("+rowNo+");");
	cell1.appendChild(textnode1);
	cell2.appendChild(textnode2);
	cell3.appendChild(textnode3);
	cell4.appendChild(textnode4);
	cell5.appendChild(textnode5);
	cell6.appendChild(textnode6);
	cell7.appendChild(buttonnode4);
	/*row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	row.appendChild(cell4);
	vItemTable.appendChild(row);*/
	rowNo++;
	document.forms[0].txtQuantity.value = 1;
	document.forms[0].txtRate.value = "";
	document.forms[0].sItemName.focus();
}


function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(document.getElementById(row).rowIndex);
}

function fnGenerateBill(){
	document.forms[0].genBill.disabled = true;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(document.forms[0].sTo.value == '--'){
		document.forms[0].genBill.disabled = false;
		alert('Please select the to branch.');
		ocument.forms[0].sTo.focus();
		return;
	}
	if(rowsInTable == 1){
		document.forms[0].genBill.disabled = false;
		alert('Please enter the item details');
		return;
	}
	buildItemData();
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "ADD_TRANSFER";
	fnFinalSubmit();
}

function buildItemData(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		itemdata = itemdata + vTR.getElementsByTagName("td")[0].innerHTML + "|" + vTR.getElementsByTagName("td")[1].innerHTML + "|" + vTR.getElementsByTagName("td")[2].innerHTML + "|" + vTR.getElementsByTagName("td")[3].innerHTML + "|" + vTR.getElementsByTagName("td")[4].innerHTML + "#";
	}
	document.forms[0].txtSalesItems.value = itemdata;
}

function printSalesBill(billNo, copyType){
	window.open('/en/app?HANDLERS=TRANSFER_HANDLER&ACTIONS=PRNT_TRANSFER&billNo='+billNo+'&copies='+copyType,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function editSalesBill(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_TRANSFER";
	fnFinalSubmit();
}

function fnEditGenerateBill(){
	document.forms[0].genBill.disabled = true;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(document.forms[0].sTo.value == '--'){
		document.forms[0].genBill.disabled = false;
		alert('Please select the to branch.');
		ocument.forms[0].sTo.focus();
		return;
	}
	if(rowsInTable == 1){
		document.forms[0].genBill.disabled = false;
		alert('Please enter the item details');
		return;
	}
	buildItemData();
	document.forms[0].HANDLERS.value = "TRANSFER_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_TRANSFER";
	fnFinalSubmit();
}