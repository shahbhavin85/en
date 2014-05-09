var isPaymodeClicked = false;

function fnFocus(){
	document.forms[0].sCustomer.focus();
}

function fnChangeItemNo(ele){ 
	document.forms[0].sItem.value = ele.value;
	document.forms[0].txtRate.value = ele.options[ele.selectedIndex].id.substring(0,ele.options[ele.selectedIndex].id.indexOf('|'));
	if(parseFloat(document.forms[0].txtRate.value)==0){
		document.forms[0].txtRate.readOnly = false;
	} else {
		document.forms[0].txtRate.readOnly = true;
	}
	document.forms[0].txtDiscount.value = ele.options[ele.selectedIndex].id.substring(ele.options[ele.selectedIndex].id.indexOf('|')+1);
}

function fnChangeItemName(ele){
	document.forms[0].sItemName.value = ele.value;
	document.forms[0].txtRate.value = ele.options[ele.selectedIndex].id.substring(0,ele.options[ele.selectedIndex].id.indexOf('|'));
	if(parseFloat(document.forms[0].txtRate.value)==0){
		document.forms[0].txtRate.readOnly = false;
	} else {
		document.forms[0].txtRate.readOnly = true;
	}
	document.forms[0].txtDiscount.value = ele.options[ele.selectedIndex].id.substring(ele.options[ele.selectedIndex].id.indexOf('|')+1);
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
	var vDis = (document.forms[0].txtDiscount.value == '') ? 0 : document.forms[0].txtDiscount.value;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(rowsInTable == 15){
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
	if(!vRateRegex.test(vRate)){
		alert('Please enter a valid rate.');
		document.forms[0].txtRate.focus();
		return;
	}  
	vRate = parseFloat(vRate);
	var vAmt = (((100-parseFloat(vDis))/100)*parseFloat(vQty) * vRate).toFixed(2);        
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
	cell8.setAttribute("align", "right");
	textnode1=document.createTextNode(vItemId);
	textnode2=document.createTextNode(vItemNo);
	textnode3=document.createTextNode(vDesc);
	textnode4=document.createTextNode(vQty);
	textnode5=document.createTextNode(vRate);
	textnode6=document.createTextNode(vDis);
	textnode7=document.createTextNode(vAmt);
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
	cell8.appendChild(textnode7);
	/*row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	row.appendChild(cell4);
	vItemTable.appendChild(row);*/
	rowNo++;
	document.forms[0].sItem.value = "--";
	document.forms[0].sItemName.value = "--";
	document.forms[0].txtQuantity.value = 1;
	document.forms[0].txtDiscount.value = 0;
	document.forms[0].txtDesc.value = "";
	document.forms[0].txtRate.value = "";
	fnRefreshTotal();
	document.forms[0].sItemName.focus();
}

function fnRefreshTotal(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var total = 0;
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		total = total + parseFloat(vTR.getElementsByTagName("td")[7].innerHTML);
	}
	var vInstall = (document.forms[0].txtInstall.value == '' ) ? 0 : parseFloat(document.forms[0].txtInstall.value);
	var vPacking = (document.forms[0].txtPacking.value == '' ) ? 0 : parseFloat(document.forms[0].txtPacking.value);
	var vForward = (document.forms[0].txtForwarding.value == '' ) ? 0 : parseFloat(document.forms[0].txtForwarding.value);
	var vLess = parseFloat(document.forms[0].txtLess.value);
	total = total + vInstall + vPacking + vForward - vLess;
	document.getElementById("gTotal").innerHTML = (parseFloat(total)).toFixed(2);
}

function fnChangeTotal(field){
	if(field.value == "" || field.value == "."){
		field.value = 0;
	}
	fnRefreshTotal();
}

function fnDeleteItem(row){
	var vItemTable = document.getElementById('enqItem');
	vItemTable.deleteRow(document.getElementById(row).rowIndex);
	fnRefreshTotal();
}

function fnPayModeClicked(){
	isPaymodeClicked = true;
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
function getCheckedValue(radioObj) {
	if(!radioObj)
		return "";
	var radioLength = radioObj.length;
	if(radioLength == undefined)
		if(radioObj.checked)
			return radioObj.value;
		else
			return "";
	for(var i = 0; i < radioLength; i++) {
		if(radioObj[i].checked) {
			return radioObj[i].value;
		}
	}
	return "";
}

function fnFinalGenerateBill(){
	if(isClick && document.forms[0].genBill.disabled == true){
		var vItemTable = document.getElementById('enqItem');
		var rowsInTable = vItemTable.rows.length;
		if(document.forms[0].sCustomer.value == ''){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please select the customer');
			document.forms[0].sCustomer.focus();
			return;
		}
		if(!isPaymodeClicked){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please provide paymode');
			return;
		}
		if(document.forms[0].rPayMode[2].checked && document.forms[0].txtCreditDays.value == ''){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please provide credit days');
			document.forms[0].txtCreditDays.focus();
			return;
		}
		if(document.getElementById("branchState").innerHTML != document.getElementById("custState").innerHTML && getCheckedValue(document.forms[0].rTaxType) == 1 && !document.forms[0].rPayMode[0].checked){
			alert('Customer is out of Branch State. Please select CST or CST AGAINST FORM \'C\' in tax type.');
			document.forms[0].genBill.disabled = false;
			isClick = false;
			return;
		}
		if(document.getElementById("branchState").innerHTML != document.getElementById("custState").innerHTML && getCheckedValue(document.forms[0].rTaxType) != 1 && document.forms[0].rPayMode[0].checked){
			document.forms[0].rTaxType[0].checked = 'checked';
		}
		if(document.getElementById("branchState").innerHTML == document.getElementById("custState").innerHTML && getCheckedValue(document.forms[0].rTaxType) != 1){
			alert('Customer is within Branch State. Please select VAT in tax type.');
			document.forms[0].genBill.disabled = false;
			isClick = false;
			return;
		}
		if(document.forms[0].sUser.value == '--'){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please select the salesman');
			document.forms[0].sUser.focus();
			return;
		}
		if(rowsInTable == 1){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please enter the item details');
			return;
		}
		if(document.forms[0].txtInstall.value == ''){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please provide installation charges');
			document.forms[0].txtInstall.focus();
			return;
		}
		if(document.forms[0].txtPacking.value == ''){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please provide packing charges');
			document.forms[0].txtPacking.focus();
			return;
		}
		if(document.forms[0].txtForwarding.value == ''){
			document.forms[0].genBill.disabled = false;
			isClick = false;
			alert('Please provide forwarding charges');
			document.forms[0].txtForwarding.focus();
			return;
		}
		buildSalesItemData();
		document.forms[0].HANDLERS.value = "SALES_HANDLER";
		document.forms[0].ACTIONS.value = "ADD_SALES";
		fnFinalSubmit();
	}
}

function buildSalesItemData(){
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	var itemdata = "";
	for(var i=1; i<rowsInTable; i++){
		var vTR = vItemTable.getElementsByTagName("tr")[i];
		for(var j=1; j<8; j++){
			itemdata = itemdata + vTR.getElementsByTagName("td")[j].innerHTML;
			if(j!=7){
				itemdata = itemdata + "|";
			} else {
				itemdata = itemdata + "#";
			}
		}
	}
	document.forms[0].txtSalesItems.value = itemdata;
}

function printDC(billNo){
	window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=PRNT_DC&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function printSalesBill(billNo, copyType){
	window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=PRNT_SALES&billNo='+billNo+'&copies='+copyType,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function checkList(billNo){
	window.open('/en/app?HANDLERS=SALES_HANDLER&ACTIONS=GET_ITM_CHECK_LIST&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
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
	if(document.forms[0].sCustomer.value == ''){
		document.forms[0].genBill.disabled = false;
		alert('Please select the customer');
		document.forms[0].sCustomer.focus();
		return;
	}
	if(document.forms[0].rPayMode[2].checked && document.forms[0].txtCreditDays.value == ''){
		document.forms[0].genBill.disabled = false;
		alert('Please provide credit days');
		document.forms[0].txtCreditDays.focus();
		return;
	}
//	if(document.getElementById("branchState").innerHTML != document.getElementById("custState").innerHTML && getCheckedValue(document.forms[0].rTaxType) == 1 && !document.forms[0].rPayMode[0].checked){
//		alert('Customer is out of Branch State. Please select CST or CST AGAINST FORM \'C\' in tax type.');
//		document.forms[0].genBill.disabled = false;
//		return;
//	}
//	if(document.getElementById("branchState").innerHTML != document.getElementById("custState").innerHTML && getCheckedValue(document.forms[0].rTaxType) != 1 && document.forms[0].rPayMode[0].checked){
//		document.forms[0].rTaxType[0].checked = 'checked';
//	}
//	if(document.getElementById("branchState").innerHTML == document.getElementById("custState").innerHTML && getCheckedValue(document.forms[0].rTaxType) != 1){
//		alert('Customer is within Branch State. Please select VAT in tax type.');
//		document.forms[0].genBill.disabled = false;
//		return;
//	}
	if(document.forms[0].sUser.value == '--'){
		document.forms[0].genBill.disabled = false;
		alert('Please select the salesman');
		document.forms[0].sUser.focus();
		return;
	}
	if(rowsInTable == 1){
		document.forms[0].genBill.disabled = false;
		alert('Please enter the item details');
		return;
	}
	buildSalesItemData();
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_SALES";
	fnFinalSubmit();
}

function fnChangeCustomer(dtls){
//	var opt = document.forms[0].sCustomer.options[document.forms[0].sCustomer.selectedIndex];
	document.getElementById("custState").innerHTML = dtls.substring(0,dtls.indexOf("!"));
	document.getElementById("custType").innerHTML = dtls.substring(dtls.indexOf("!")+1, dtls.indexOf("<"));
	document.forms[0].txtTin.value = dtls.substring(dtls.indexOf("<")+1, dtls.indexOf(">"));
	checkBlank(document.forms[0].txtTin);
	document.forms[0].txtCst.value = dtls.substring(dtls.indexOf(">")+1, dtls.indexOf("~"));
	checkBlank(document.forms[0].txtCst);
	document.forms[0].txtContactPerson.value = dtls.substring(dtls.indexOf("~")+1, dtls.indexOf("|"));
	checkBlank(document.forms[0].txtContactPerson);
	document.forms[0].txtMobile1.value = dtls.substring(dtls.indexOf("|")+1);
	checkBlank(document.forms[0].txtMobile1);
	return;
}

function checkBlank(ele){
	if(ele.value.length != 0){
		ele.readOnly = true;
	} else {
		ele.readOnly = false;
	}
}

function fnChangePaymode(r){
	if(r.value == 3){
		document.forms[0].txtCreditDays.disabled = false;
		document.forms[0].txtCreditDays.focus();
	} else {
		document.forms[0].txtCreditDays.disabled = true;
		document.forms[0].txtCreditDays.value = '';
	}
}

function emailSales(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "EMAIL_SALES";
	fnFinalSubmit();
}

function smsSales(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "SEND_SMS";
	fnFinalSubmit();
}

function smsSalesLR(billNo){
	document.forms[0].txtInvoiceNo.value = billNo;
	document.forms[0].HANDLERS.value = "SALES_HANDLER";
	document.forms[0].ACTIONS.value = "SEND_LR_SMS";
	fnFinalSubmit();
}

function fnGetCustomerList(txt){
	if(txt.value.length >= 3){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_GET_SALES_CUST_LST&custLike='+encodeURIComponent(txt.value)+'&temp='+Math.random() ,fnCallBackForCustList);
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
    		if(retObj.list[i].count > 0){
    			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
    		} else {
    			t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
    		}
       	}
   		document.getElementById("autoSuggestionsList").innerHTML = t;
   		document.getElementById("list").style.display = 'block';
    }
}

function fnClearCustomer(){
	document.forms[0].txtCust.value = "";
	document.getElementById("img").style.visibility = "hidden";
	document.forms[0].sCustomer.value = "";
	document.forms[0].txtCust.readOnly = false;
	document.getElementById("custState").innerHTML = "";
	document.getElementById("custType").innerHTML = "";
	document.forms[0].txtTin.value = "";
	document.forms[0].txtCst.value = "";
	document.forms[0].txtContactPerson.value = "";
	document.forms[0].txtMobile1.value = "";
	document.forms[0].txtCust.focus();
}

function fnSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
	fnChangeCustomer(dtls);
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
}

function fnGetOffers(){
//	alert(document.forms[0].sItem.value);
	window.open('/en/app?HANDLERS=ENQUIRY_HANDLER&ACTIONS=GET_OFFER','','status=1,scrollbars=1,width=800,height=500');
}

function fnAddItemRow(id,name,qty,price, desc){
	var vItemId = id;
	var vItemNo = name;
	var vDesc = desc;
	var vQty = qty;
	var vRate = price;
	var vDis = 0;
	var vItemTable = document.getElementById('enqItem');
	var rowsInTable = vItemTable.rows.length;
	if(rowsInTable == 15){
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
	if(!vRateRegex.test(vRate)){
		alert('Please enter a valid rate.');
		document.forms[0].txtRate.focus();
		return;
	}  
	vRate = parseFloat(vRate);
	var vAmt = (((100-parseFloat(vDis))/100)*parseFloat(vQty) * vRate).toFixed(2);        
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
	cell8.setAttribute("align", "right");
	textnode1=document.createTextNode(vItemId);
	textnode2=document.createTextNode(vItemNo);
	textnode3=document.createTextNode(vDesc);
	textnode4=document.createTextNode(vQty);
	textnode5=document.createTextNode(vRate);
	textnode6=document.createTextNode(vDis);
	textnode7=document.createTextNode(vAmt);
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
	cell8.appendChild(textnode7);
	/*row.appendChild(cell1);
	row.appendChild(cell2);
	row.appendChild(cell3);
	row.appendChild(cell4);
	vItemTable.appendChild(row);*/
	rowNo++;
	document.forms[0].sItem.value = "--";
	document.forms[0].sItemName.value = "--";
	document.forms[0].txtQuantity.value = 1;
	document.forms[0].txtDiscount.value = 0;
	document.forms[0].txtDesc.value = "";
	document.forms[0].txtRate.value = "";
	fnRefreshTotal();
	document.forms[0].sItemName.focus();
}

function fnAddPacking(val){
	document.forms[0].txtPacking.value = parseFloat(document.forms[0].txtPacking.value) + val;
}

function fnAddForwarding(val){
	document.forms[0].txtForwarding.value = parseFloat(document.forms[0].txtForwarding.value) + val;
}

function fnAddInstallation(val){
	document.forms[0].txtInstall.value = parseFloat(document.forms[0].txtInstall.value) + val;
}