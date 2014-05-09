
function fnGetMasterRpt(){
	document.forms[0].HANDLERS.value = "TRANSFER_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "GET_MASTER_RPT";
	fnFinalSubmit();
}

function fnShowTransfer(billNo){
	window.open('/en/app?HANDLERS=TRANSFER_REPORT_HANDLER&ACTIONS=GET_TRANSFER&billNo='+billNo,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnChangeFromBranch(branch){
	var iLen = document.forms[0].cFromBranch.length;
	if(branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cFromBranch[i].checked = true;
		}
	} else if(!branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cFromBranch[i].checked = false;
		}
	} else if(!branch.checked){
		document.forms[0].cFromBranch[0].checked = false;
	} else if(branch.value != 0){
		var isAllChecked = true;
		for(var i=1; i<iLen; i++){
			if(!document.forms[0].cFromBranch[i].checked){
				isAllChecked = false;
			}
		}
		document.forms[0].cFromBranch[0].checked = isAllChecked;
	}
}

function fnChangeToBranch(branch){
	var iLen = document.forms[0].cToBranch.length;
	if(branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cToBranch[i].checked = true;
		}
	} else if(!branch.checked && branch.value == '--'){
		for(var i=1; i<iLen; i++){
			document.forms[0].cToBranch[i].checked = false;
		}
	} else if(!branch.checked){
		document.forms[0].cToBranch[0].checked = false;
	} else if(branch.value != 0){
		var isAllChecked = true;
		for(var i=1; i<iLen; i++){
			if(!document.forms[0].cToBranch[i].checked){
				isAllChecked = false;
			}
		}
		document.forms[0].cToBranch[0].checked = isAllChecked;
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
	params = params+"&cFromBranch="+getCheckBoxValues(document.forms[0].cFromBranch);
	params = params+"&cToBranch="+getCheckBoxValues(document.forms[0].cToBranch);
	params = params+"&txtFromDate="+document.forms[0].txtFromDate.value;
	params = params+"&txtToDate="+document.forms[0].txtToDate.value;
	window.open('/en/app?HANDLERS=TRANSFER_REPORT_HANDLER&ACTIONS=PRNT_MASTER_RPT'+params,"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnExporMasterRpt(){
	document.forms[0].HANDLERS.value = "TRANSFER_REPORT_HANDLER";
	document.forms[0].ACTIONS.value = "EXPT_MASTER_RPT";
	document.forms[0].submit();
}