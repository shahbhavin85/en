function fnGenerateBill(){
	if(document.forms[0].sCustomer.value == ''){
		document.forms[0].genBill.disabled = false;
		isClick = false;
		alert('Please select the customer');
		document.forms[0].sCustomer.focus();
		return;
	}
	if(document.forms[0].sUser.value == '--'){
		document.forms[0].genBill.disabled = false;
		isClick = false;
		alert('Please select the salesman');
		document.forms[0].sUser.focus();
		return;
	}
	document.forms[0].HANDLERS.value = "LABOUR_BILL_HANDLER";
	document.forms[0].ACTIONS.value = "ADD_LABOUR_BILL";
	fnFinalSubmit();
}

function fnEditGenerateBill(){
	if(document.forms[0].sCustomer.value == ''){
		document.forms[0].genBill.disabled = false;
		isClick = false;
		alert('Please select the customer');
		document.forms[0].sCustomer.focus();
		return;
	}
	if(document.forms[0].sUser.value == '--'){
		document.forms[0].genBill.disabled = false;
		isClick = false;
		alert('Please select the salesman');
		document.forms[0].sUser.focus();
		return;
	}
	document.forms[0].HANDLERS.value = "LABOUR_BILL_HANDLER";
	document.forms[0].ACTIONS.value = "EDIT_LABOUR_BILL";
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
    			t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
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
	document.forms[0].txtCust.focus();
}

function fnSelect(id, txt, dtls){
	document.forms[0].txtCust.value = txt;
	document.getElementById("list").style.display = 'none';
//	fnChangeCustomer(dtls);
	document.forms[0].sCustomer.value = id;
	document.forms[0].txtCust.readOnly = true;
	document.getElementById("img").style.visibility = "visible";
}