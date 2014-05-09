
function fnGetUserAccessDetails(){
	document.forms[0].HANDLERS.value = 'USER_ACCESS_HANDLER';
	document.forms[0].ACTIONS.value = 'GET_USER_ACCESS';
	fnFinalSubmit();	
}

function fnAdminChange(){
	var iLength = parseInt(document.forms[0].txtAccessNos.value);
	if(document.forms[0].r0[0].checked){
		for(var i=1; i<iLength; i++){
			var temp = "r"+i;
			if(document.getElementById(temp+"Yes")){
				document.getElementById(temp+"Yes").checked = true;
				document.getElementById(temp+"Yes").disabled = true;
				document.getElementById(temp+"No").disabled = true;
			}
		}
	} else {
		for(var i=1; i<iLength; i++){
			var temp = "r"+i;
			if(document.getElementById(temp+"Yes")){
				document.getElementById(temp+"Yes").disabled = false;
				document.getElementById(temp+"No").disabled = false;
			}
		}
	}
}

function fnUpdateUserAccessDetails(){
	var iLength = parseInt(document.forms[0].txtAccessNos.value);
	for(var i=1; i<iLength; i++){
		var temp = "r"+i;
		if(document.getElementById(temp+"Yes")){
			document.getElementById(temp+"Yes").disabled = false;
			document.getElementById(temp+"No").disabled = false;
		}
	}
	document.forms[0].HANDLERS.value = 'USER_ACCESS_HANDLER';
	document.forms[0].ACTIONS.value = 'UPDT_USER_ACCESS';
	fnFinalSubmit();	
}