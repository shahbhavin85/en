
function fnGetMasterRpt(){
	document.forms[0].HANDLERS.value = "CUSTOMER_EMAIL_RPT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_MASTER_RPT";
	fnFinalSubmit();
}

function fnShowSales(billNo){
	window.open('/en/app?HANDLERS=ORDER_HANDLER&ACTIONS=SHOW_ORDER&txtInvoiceNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
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
	params = params+"&rOrderOf="+getCheckBoxValues(document.forms[0].rOrderOf);
	params = params+"&txtFromDate="+document.forms[0].txtFromDate.value;
	params = params+"&txtToDate="+document.forms[0].txtToDate.value;
	window.open('/en/app?HANDLERS=ORDER_REPORT_HANDLER&ACTIONS=PRNT_MASTER_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExporMasterRpt(){
	document.forms[0].HANDLERS.value = "ORDER_REPORT_HANDLER";
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
	window.open('/en/app?HANDLERS=ORDER_REPORT_HANDLER&ACTIONS=EXPT_MASTER_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));*/
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
//    		if(retObj.list[i].count > 0){
//    			t = t+"<li style='background-color : yellow; color: red; font-weight : bold;' onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city+" :: ("+retObj.list[i].count+")")+"</li>";
//    		} else {
    			t = t+"<li onclick='fnSelect("+retObj.list[i].custId+",\""+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"\" , \""+(retObj.list[i].state.toUpperCase()+"!"+retObj.list[i].custType+"<"+retObj.list[i].tin +">"+retObj.list[i].cst+"~"+retObj.list[i].person+"|"+retObj.list[i].mobile)+"\")'>"+(retObj.list[i].custName+" - "+retObj.list[i].area+" - "+retObj.list[i].city)+"</li>";
//    		}
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

function fnItemMasterRpt(){
	document.forms[0].HANDLERS.value = "ORDER_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_ITEM_RPT";
	fnFinalSubmit();
}