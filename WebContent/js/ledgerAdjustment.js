
function fnTypeChange(combo){
	if(combo.value == 0){
		combo.style.background = '#FFC1C1';
	} else if(combo.value == 1){
		combo.style.background = '#FFFF7E';
	} else if(combo.value == 2){
		combo.style.background = '#BCED91';
	} else {
		combo.style.background = 'white';
	}
}


function fnGetDetails(){
	document.forms[0].HANDLERS.value = "LEDGER_ADJUSTMENT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ORDER_ADVANCE";
	fnFinalSubmit();
}

function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=COLLECTION_HANDLER&ACTIONS=GET_SALES&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnChangeBranch(branch){
	var iLen = document.forms[0].cBranch.length;
	if(branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cBranch[i].checked = true;
		}
	} else if(!branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cBranch[i].checked = false;
		}
	} else if(!branch.checked){
		document.forms[0].cBranch[0].checked = false;
	} else if(branch.value != 0){
		var isAllChecked = true;
		for(var i=1; i<iLen; i++){
			if(!document.forms[0].cBranch[i].checked){
				isAllChecked = false;
			}
		}
		document.forms[0].cBranch[0].checked = isAllChecked;
	}
}

function fnChangeType(type){
	var vType = document.forms[0].sBillType;
	if(!type.checked){
		if(!vType[0].checked && !vType[1].checked && !vType[2].checked)
			type.checked = true;
	}
}

function fnChangeType(mode){
	var vMode = document.forms[0].sPayMode;
	if(!mode.checked){
		if(!vMode[0].checked && !vMode[1].checked && !vMode[2].checked)
			mode.checked = true;
	}
}

function getCheckBoxValues(cb){
	var val = "";
	var iL = cb.length;
	for(var k=0; k<iL; k++){
		if(cb[k].checked){
			val = val + cb[k].value+"|";
		}
	}
	return val;
}

function fnPrintMasterRpt(){
	var params = "";
	params = "&sCustomer="+document.forms[0].sCustomer.value;
	params = params+"&sUser="+document.forms[0].sUser.value;
	params = params+"&cBranch="+getCheckBoxValues(document.forms[0].cBranch);
	params = params+"&sBillType="+getCheckBoxValues(document.forms[0].sBillType);
	params = params+"&sPayMode="+getCheckBoxValues(document.forms[0].sPayMode);
	params = params+"&txtFromDate="+document.forms[0].txtFromDate.value;
	params = params+"&txtToDate="+document.forms[0].txtToDate.value;
	window.open('/en/app?HANDLERS=SALES_REPORT_HANDLER&ACTIONS=PRNT_MASTER_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExporMasterRpt(){
	document.forms[0].HANDLERS.value = "SALES_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "EXPT_MASTER_RPT";
	document.forms[0].submit();
	/*var params = "";
	params = "&sCustomer="+document.forms[0].sCustomer.value;
	params = params+"&sUser="+document.forms[0].sUser.value;
	params = params+"&cBranch="+getCheckBoxValues(document.forms[0].cBranch);
	params = params+"&sBillType="+getCheckBoxValues(document.forms[0].sBillType);
	params = params+"&sPayMode="+getCheckBoxValues(document.forms[0].sPayMode);
	params = params+"&txtFromDate="+document.forms[0].txtFromDate.value;
	params = params+"&txtToDate="+document.forms[0].txtToDate.value;
	window.open('/en/app?HANDLERS=SALES_REPORT_HANDLER&ACTIONS=EXPT_MASTER_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));*/
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
//	document.getElementById("custState").innerHTML = "";
//	document.getElementById("custType").innerHTML = "";
//	document.forms[0].txtTin.value = "";
//	document.forms[0].txtCst.value = "";
//	document.forms[0].txtContactPerson.value = "";
//	document.forms[0].txtMobile1.value = "";
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

function fnSelected(){
	document.forms[0].btnNext.disabled = false;
}

function fnNext(){
	document.forms[0].sType.disabled = false;
	document.forms[0].HANDLERS.value = "LEDGER_ADJUSTMENT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ORDER_ADVANCE_PENDING_SALES";
	fnFinalSubmit();
}

function fnRefreshPending(){
	var pendingAmt = document.forms[0].txtAmount.value;
	var vTable = document.getElementById('billTable');
	var iTableRow = vTable.rows.length;
	for (var i=1 ;i<=iTableRow; i++){ //alert(document.getElementById('v'+i) && document.getElementById('v'+i).checked);
		if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
			pendingAmt = pendingAmt - parseFloat(document.getElementById("p"+i).value).toFixed(2) - parseFloat(document.getElementById("pa"+i).value).toFixed(2);
		}
	}
	document.forms[0].txtRemainAmount.value = parseFloat(pendingAmt).toFixed(2);
	//document.forms[0].btnSave.disabled =  (document.forms[0].txtRemainAmount.value  != 0);
}

function fnChkChange(chk, id, ppid, paid){
	var amt = document.forms[0].txtRemainAmount.value;
	if(amt == 0 && chk.checked){
		alert('No more pending amount.');
		chk.checked = false;
		return;
	}
	if(chk.checked){
		document.getElementById(id).readOnly = false;
		document.getElementById(paid).readOnly = false;
		var pending = document.getElementById(ppid).value;
		if(amt - pending < 0)
			document.getElementById(id).value = amt;
		else 
			document.getElementById(id).value = pending;
		document.getElementById(id).focus();
	} else {
		document.getElementById(id).value = 0; 
		document.getElementById(paid).value = 0;
		document.getElementById(paid).readOnly = true;
		document.getElementById(id).readOnly = true;
	}
	fnRefreshPending();
}

function fnAmtChange(text,id, vId){
	var rate = text.value;
	var pending = document.getElementById(id).value;
	var amt = document.forms[0].txtRemainAmount.value;
	if(pending-rate < 0){
		if(amt - pending < 0)
			text.value = amt;
		else 
			text.value = pending;
	}
	if(rate == 0){
		text.readOnly = true;
		document.getElementById(vId).checked = false;
	}
	fnRefreshPending();
}

function fnAdjAmtChange(text, pid, id, vId){
	var rate = text.value;
	if(rate == ''){
		text.value = 0;
		rate = 0;
	}
	if(isNaN(rate)){
		rate = 0;
		text.value = rate;
	}
	var amt = document.forms[0].txtRemainAmount.value;
	if(amt-rate < 0){
		rate = 0;
		text.value = rate;
	} else {
		if(rate <0){
			document.getElementById(pid).value = document.getElementById(pid).value - rate;
		}
	}
	fnRefreshPending();
}

function fnSaveSubmit(){
	var payDetails = "";
	document.forms[0].sType.disabled = false;
	var vTable = document.getElementById('billTable');
	var iTableRow = vTable.rows.length;
	for (var i=1 ;i<=iTableRow; i++){
		if(document.getElementById('v'+i) && document.getElementById('v'+i).checked){
			payDetails += vTable.getElementsByTagName("tr")[i].id+"|"+document.getElementById('p'+i).value+"|"+document.getElementById('pa'+i).value+"#";
		}
	}
	document.forms[0].payDtls.value = payDetails;
	document.forms[0].HANDLERS.value = "LEDGER_ADJUSTMENT_HANDLER"; 
	document.forms[0].ACTIONS.value = "ADD_ORDER_ADVANCE_PENDING_SALES";
	fnFinalSubmit();
}

function fnGetAdjustDetails(){
	document.forms[0].HANDLERS.value = "LEDGER_ADJUSTMENT_HANDLER"; 
	document.forms[0].ACTIONS.value = "VIEW_LEDGER_ADJUSTMENT";
	fnFinalSubmit();
}

function fnDeleteEntry(type, idx){
	document.forms[0].idx.value = idx; 
	document.forms[0].HANDLERS.value = "LEDGER_ADJUSTMENT_HANDLER"; 
	document.forms[0].ACTIONS.value = "DELETE_LEDGER_ADJUSTMENT";
	fnFinalSubmit();
}