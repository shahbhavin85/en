function fnFocus(){
	document.forms[0].sCustomer.focus();
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
var vTaxRegex = /(^\d{1,2}[.]{1}\d{0,2}$)|(^\d{1,10}$)/;
var rowNo = 1;

function fnAddItem(){
	if(document.forms[0].sItem.value == '--'){
		alert('Please select the item.');
		return;
	}
	var vItemId = document.forms[0].sItem.value;
	var vItemNo = document.forms[0].sItem[document.forms[0].sItem.selectedIndex].innerHTML;
	var vQty = document.forms[0].txtQuantity.value;
	var vRate = document.forms[0].txtRate.value;
	var vTax = document.forms[0].txtTax.value;
	var vDis = document.forms[0].txtDis.value;
	if(vDis!='' && vDis != 0){
		vRate = (((100-parseFloat(vDis))/100)*vRate).toFixed(2);
	}
	var vCess = document.forms[0].txtCess.value;
	if(vCess!='' && vCess != 0){
		vRate = (((100+parseFloat(vCess))/100)*vRate).toFixed(2);
	}
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(rowsInTable == 301){
		alert('Bill can contain a maximum of 300 items');
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
	if(!vRateRegex.test(vRate)){
		alert('Please enter a valid rate.');
		document.forms[0].txtRate.focus();
		return;
	}   
	if(!vTaxRegex.test(vTax)){
		alert('Please enter a valid tax.');
		document.forms[0].txtTax.focus();
		return;
	} 
	vRate = parseFloat(vRate);
	var vAmt = (((100+parseFloat(vTax))/100)*parseFloat(vQty) * vRate).toFixed(2);        
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
	textnode3=document.createTextNode(vQty);
	textnode4=document.createTextNode(vRate);
	textnode5=document.createTextNode(vTax);
	textnode6=document.createTextNode(vAmt);
	// textnode4=document.createTextNode(vQty);
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
	rowNo++;
	document.forms[0].sItem.value = "--";
	document.forms[0].sItemName.value = "--";
	document.forms[0].txtDis.value = "";
	document.forms[0].txtTax.value = "";
	document.forms[0].txtQuantity.value = "";
	document.forms[0].txtRate.value = "";
	fnRefreshTotal();
	document.forms[0].sItemName.focus();
}

function fnChangeTotal(field){
	if(field.value == "" || field.value == "."){
		field.value = 0;
	}
	fnRefreshTotal();
}


function fnRefreshTotal(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var total = 0;
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		total = total + parseFloat(vTR.getElementsByTagName("td")[6].innerHTML);
	}
	var vExtra = parseFloat(document.forms[0].txtExtra.value);
	var vDiscount = parseFloat(document.forms[0].txtDiscount.value);
	total = total + vExtra - vDiscount;
	document.getElementById("gTotal").innerHTML = (parseFloat(total)).toFixed(2);
}

function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(document.getElementById(row).rowIndex);
	fnRefreshTotal();
}

function fnGenerateBill(){
	document.forms[0].genBill.disabled = true;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(document.forms[0].sCustomer.value == '--'){
		document.forms[0].genBill.disabled = false;
		alert('Please select the customer');
		ocument.forms[0].sCustomer.focus();
		return;
	}
	if(document.forms[0].txtInvNo.value == ''){
		document.forms[0].genBill.disabled = false;
		alert('Please enter the invoice no.');
		ocument.forms[0].txtInvNo.focus();
		return;
	}
	if(rowsInTable == 1){
		document.forms[0].genBill.disabled = false;
		alert('Please enter the item details');
		return;
	}
	buildSalesItemData();
	document.forms[0].HANDLERS.value = "PURCHASE_HANDLER";
	document.forms[0].ACTIONS.value = "ADD_PURCHASE";
	fnFinalSubmit();
}

function buildSalesItemData(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		for(var j=1; j<7; j++){
			itemdata = itemdata + vTR.getElementsByTagName("td")[j].innerHTML;
			if(j!=6){
				itemdata = itemdata + "|";
			} else {
				itemdata = itemdata + "#";
			}
		}
	}
	document.forms[0].txtPurchaseItems.value = itemdata;
}

function printDC(billNo){
	window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=PRNT_DC&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function printSalesBill(billNo, copyType){
	window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=PRNT_SALES&billNo='+billNo+'&copies='+copyType,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function editSalesBill(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "INIT_EDIT_SALES";
	fnFinalSubmit();
}

function fnEditGenerateBill(){
	document.forms[0].genBill.disabled = true;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(rowsInTable == 1){
		document.forms[0].genBill.disabled = false;
		alert('Please enter the item details');
		return;
	}
	buildSalesItemData();
	document.forms[0].HANDLERS.value = "PURCHASE_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_PURCHASE";
	fnFinalSubmit();
}