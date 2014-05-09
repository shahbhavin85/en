// common.js

function fnLogin(){
	fnFinalSubmit();
}

function fnLogout(){
	document.forms[0].HANDLER.value = "LOGOUT_HANDLER";
	fnFinalSubmit();
}

function fnHelp(){
	alert('Help Click');
}

function trim(str)
{
    while (str.substring(0,1) == ' '){
        str = str.substring(1, str.length);
    }
    while (str.substring(str.length-1, str.length) == ' '){
        str = str.substring(0,str.length-1);
    }
    return str;
}

function fnOpenScreen(pageName){
	
	alert('Coming Soon!!!!');	
	//window.open(pageName+'.html','_self');
}

function fnGoToPage(handler, action){
	document.forms[0].HANDLERS.value = handler;
	document.forms[0].ACTIONS.value = action;
	fnFinalSubmit();
}

function fnGoToAdminPage(handler, action){
	document.forms[0].HANDLERS.value = handler;
	document.forms[0].ACTIONS.value = action;
	fnFinalSubmit();
}

var CSSrules = {
    'textarea' : function(element){
            element.onkeydown = function(event){
                return doKeyPress(element,event);
            }
            ,
            element.onpaste = function(){
                return doPaste(element);
            }
            ,
            element.onkeyup = function(){
                return doKeyUp(element);
            }
            ,
            element.onblur = function(){
                return doKeyUp(element);
            }
    }
};

var detect = navigator.userAgent.toLowerCase();

// Keep user from entering more than maxLength characters
function doKeyPress(obj,evt){
    maxLength = obj.getAttribute("maxlength");
    var e = window.event ? event.keyCode : evt.which;
    if ( (e == 32) || (e == 13) || (e > 47)) { //IE
        if(maxLength && (obj.value.length > maxLength-1)) {
            if (window.event) {
                window.event.returnValue = null;
            } else {
                evt.cancelDefault;
                return false;
            }
        }
    }
}
function doKeyUp(obj){
    maxLength = obj.getAttribute("maxlength");
     if(maxLength && obj.value.length > maxLength){
           obj.value = obj.value.substr(0,maxLength);
     }
    sr = obj.getAttribute("showremain");
    if (sr) {
        document.getElementById(sr).innerHTML = maxLength-obj.value.length;
    }
}

// Cancel default behavior and create a new paste routine
function doPaste(obj){
maxLength = obj.getAttribute("maxlength");
     if(maxLength){
        if ((window.event) && (detect.indexOf("safari") + 1 == 0)) { //IE
          var oTR = obj.document.selection.createRange();
          var iInsertLength = maxLength - obj.value.length + oTR.text.length;
          try {
          var sData = window.clipboardData.getData("Text").substr(0,iInsertLength);
          oTR.text = sData;
          }
          catch (err) {
          }
          if (window.event) { //IE
            window.event.returnValue = null;
     } else {
            //not IE
            obj.value = obj.value.substr(0,maxLength);
            return false;
        }
        }
     }
}

var Behaviour = {
        list : new Array,
       
        register : function(sheet){
                Behaviour.list.push(sheet);
        },
       
        start : function(){
                Behaviour.addLoadEvent(function(){
                        Behaviour.apply();
                });
        },
       
        apply : function(){
                for (var h=0;sheet=Behaviour.list[h];h++){
                        for (selector in sheet){
                                list = document.getElementsBySelector(selector);
                               
                                if (!list){
                                        continue;
                                }

                                for (var i=0;element=list[i];i++){
                                        sheet[selector](element);
                                }
                        }
                }
        },
       
        addLoadEvent : function(func){
                var oldonload = window.onload;
               
                if (typeof window.onload != 'function') {
                        window.onload = func;
                } else {
                        window.onload = function() {
                                oldonload();
                                func();
                        };
                }
        }
};

Behaviour.register(CSSrules);

function fnLogout(){
	document.forms[0].HANDLERS.value = 'LOGOUT_HANDLER';
	document.forms[0].ACTIONS.value = '';
	fnFinalSubmit();
}

function fnShowEditPass(){
	window.open('/en/jsp/showPassword.jsp',"","status=1,scrollbars=1, top=250, left=250,width="+200+",height="+100);
}

function numbersonly(myfield, e, dec)
{
var key;
var keychar;

if(document.getSelection() != null && (document.getSelection()+"").length != 0){
	myfield.value = "";
}

if (window.event)
   key = window.event.keyCode;
else if (e)
   key = e.which;
else
   return true;
keychar = String.fromCharCode(key);

// control keys
if ((key==null) || (key==0) || (key==8) || 
    (key==9) || (key==13) || (key==27) )
   return true;

// numbers
else if ((("0123456789").indexOf(keychar) > -1) && ((myfield.value).indexOf(".") == -1 || ((myfield.value).indexOf(".") != -1 && (myfield.value.substring((myfield.value).indexOf(".")+1)).length != 2)))
   return true;

//decimal point jump
else if (dec && (keychar == ".") && (myfield.value).indexOf(".") == -1)
   {
   //myfield.form.elements[dec].focus();
   return true;
   }
else
   return false;
}

function autoCompleteDB()
{
	this.aNames=new Array();
}

autoCompleteDB.prototype.assignArray=function(aList)
{
	this.aNames=aList;
};

autoCompleteDB.prototype.getMatches=function(str,aList,maxSize)
{
	/* debug */ //alert(maxSize+"ok getmatches");
	var ctr=0;
	for(var i in this.aNames)
	{
		if(this.aNames[i].toLowerCase().indexOf(str.toLowerCase())==0) /*looking for case insensitive matches */
		{
			aList.push(this.aNames[i]);
			ctr++;
		}
		if(ctr==(maxSize-1)) /* counter to limit no of matches to maxSize */
			break;
	}
};

function autoComplete(aNames,oText,oDiv,maxSize)
{

	this.oText=oText;
	this.oDiv=oDiv;
	this.maxSize=maxSize;
	this.cur=-1;

	
	/*debug here */
	//alert(oText+","+this.oDiv);
	
	this.db=new autoCompleteDB();
	this.db.assignArray(aNames);
	
	oText.onkeyup=this.keyUp;
	oText.onkeydown=this.keyDown;
	oText.autoComplete=this;
	oText.onblur=this.hideSuggest;
}

autoComplete.prototype.hideSuggest=function()
{
	this.autoComplete.oDiv.style.visibility="hidden";
};

autoComplete.prototype.selectText=function(iStart,iEnd)
{
	if(this.oText.createTextRange) /* For IE */
	{
		var oRange=this.oText.createTextRange();
		oRange.moveStart("character",iStart);
		oRange.moveEnd("character",iEnd-this.oText.value.length);
		oRange.select();
	}
	else if(this.oText.setSelectionRange) /* For Mozilla */
	{
		this.oText.setSelectionRange(iStart,iEnd);
	}
	this.oText.focus();
};

autoComplete.prototype.textComplete=function(sFirstMatch)
{
	if(this.oText.createTextRange || this.oText.setSelectionRange)
	{
		var iStart=this.oText.value.length;
		this.oText.value=sFirstMatch;
		this.selectText(iStart,sFirstMatch.length);
	}
};

autoComplete.prototype.keyDown=function(oEvent)
{
	oEvent=window.event || oEvent;
	iKeyCode=oEvent.keyCode;

	switch(iKeyCode)
	{
		case 38: //up arrow
			this.autoComplete.moveUp();
			break;
		case 40: //down arrow
			this.autoComplete.moveDown();
			break;
		case 13: //return key
			window.focus();
			break;
	}
};

autoComplete.prototype.moveDown=function()
{
	if(this.oDiv.childNodes.length>0 && this.cur<(this.oDiv.childNodes.length-1))
	{
		++this.cur;
		for(var i=0;i<this.oDiv.childNodes.length;i++)
		{
			if(i==this.cur)
			{
				this.oDiv.childNodes[i].className="over";
				this.oText.value=this.oDiv.childNodes[i].innerHTML;
			}
			else
			{
				this.oDiv.childNodes[i].className="";
			}
		}
	}
};

autoComplete.prototype.moveUp=function()
{
	if(this.oDiv.childNodes.length>0 && this.cur>0)
	{
		--this.cur;
		for(var i=0;i<this.oDiv.childNodes.length;i++)
		{
			if(i==this.cur)
			{
				this.oDiv.childNodes[i].className="over";
				this.oText.value=this.oDiv.childNodes[i].innerHTML;
			}
			else
			{
				this.oDiv.childNodes[i].className="";
			}
		}
	}
};

autoComplete.prototype.keyUp=function(oEvent)
{
	oEvent=oEvent || window.event;
	var iKeyCode=oEvent.keyCode;
	if(iKeyCode==8 || iKeyCode==46)
	{
		this.autoComplete.onTextChange(false); /* without autocomplete */
	}
	else if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) 
	{
        //ignore
    } 
	else 
	{
		this.autoComplete.onTextChange(true); /* with autocomplete */
	}
};

autoComplete.prototype.positionSuggest=function() /* to calculate the appropriate poistion of the dropdown */
{
	var oNode=this.oText;
	var x=0,y=oNode.offsetHeight;

	while(oNode.offsetParent && oNode.offsetParent.tagName.toUpperCase() != 'BODY')
	{
		x+=oNode.offsetLeft;
		y+=oNode.offsetTop;
		oNode=oNode.offsetParent;
	}

	x+=oNode.offsetLeft;
	y+=oNode.offsetTop;

	this.oDiv.style.top=y+"px";
	this.oDiv.style.left=x+"px";
};

autoComplete.prototype.onTextChange=function(bTextComplete)
{
	var txt=this.oText.value;
	var oThis=this;
	this.cur=-1;
	
	if(txt.length>0)
	{
		while(this.oDiv.hasChildNodes())
			this.oDiv.removeChild(this.oDiv.firstChild);
		
		var aStr=new Array();
		this.db.getMatches(txt,aStr,this.maxSize);
		if(!aStr.length) {this.hideSuggest ;return;};
		if(bTextComplete) this.textComplete(aStr[0]);
		this.positionSuggest();
		
		for(i in aStr)
		{
			var oNew=document.createElement('div');
			this.oDiv.appendChild(oNew);
			oNew.onmouseover=
			oNew.onmouseout=
			oNew.onmousedown=function(oEvent)
			{
				oEvent=window.event || oEvent;
				oSrcDiv=oEvent.target || oEvent.srcElement;

				//debug :window.status=oEvent.type;
				if(oEvent.type=="mousedown")
				{
					oThis.oText.value=this.innerHTML;
				}
				else if(oEvent.type=="mouseover")
				{
					this.className="over";
				}
				else if(oEvent.type=="mouseout")
				{
					this.className="";
				}
				else
				{
					this.oText.focus();
				}
			};
			oNew.innerHTML=aStr[i];
		}
		
		this.oDiv.style.visibility="visible";
	}
	else
	{
		this.oDiv.innerHTML="";
		this.oDiv.style.visibility="hidden";
	}
};

var xmlhttp;
function fnAjaxTxn(url,cfunc)
{
if (window.XMLHttpRequest)
  {// code for IE7+, Firefox, Chrome, Opera, Safari
  xmlhttp=new XMLHttpRequest();
  }
else
  {// code for IE6, IE5
  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  }

xmlhttp.onreadystatechange=cfunc;
xmlhttp.open("GET",url,true);
xmlhttp.send();
}

function fnRemove(){
	var r=confirm("Are you sure you want to remove all datas?");
	if (r==true) {
		document.forms[0].HANDLERS.value = "ADMIN_MASTER_HANDLER";
		document.forms[0].ACTIONS.value = "REMOVE_DATA";
		fnFinalSubmit();
	}
}

function fnPrintPriceList(){
	window.open('/en/app?HANDLERS=ITM_HANDLER&ACTIONS=PRICE_LIST',"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnPrintWholesalesPriceList(){
	window.open('/en/app?HANDLERS=ITM_HANDLER&ACTIONS=WHOLESALES_PRICE_LIST',"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnPrintOfferPriceList(){
	window.open('/en/app?HANDLERS=ITM_HANDLER&ACTIONS=OFFER_PRICE_LIST',"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

function fnFinalSubmit(){
//	alert('1');
	//scroll(0,0);
	var outerPane = document.getElementById('FreezePane');
//	var innerPane = document.getElementById('InnerFreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOn';
	document.documentElement.style.overflow = "hidden";
//	if (innerPane) innerPane.innerHTML = 'Processing..';
	document.forms[0].submit();
}

onload = function(){
	var outerPane = document.getElementById('FreezePane');
	if (outerPane) outerPane.className = 'FreezePaneOff';
	document.documentElement.style.overflow = "visible";
};

function fnGetCashOnHand(){
	window.open('/en/app?HANDLERS=ACCESS_POINT_HANDLER&ACTIONS=CASH_ON_HAND',"","status=1,scrollbars=1,width="+405+",height="+400);
}

function fnGoToPrintLabel(){
	window.open('/en/app?HANDLERS=ITM_HANDLER&ACTIONS=LABEL',"status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
	
}

function fnPrintStockReport(){
	window.open('/en/app?HANDLERS=BRANCH_STOCK_HANDLER',"","status=1,scrollbars=1, top=0, left=0,width="+(screen.width-10)+",height="+(screen.height-120));
}

setTimeout('fnRefreshSession()',1500000);

function fnRefreshSession(){
		fnAjaxTxn('/en/ajax?HANDLERS=AJAX_HANDLER&ACTIONS=AJAX_REFRESH&temp='+Math.random() ,fnCallBackForSessionRefresh);
}

function fnCallBackForSessionRefresh(res){
	if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		setTimeout('fnRefreshSession()',1500000);
	}
}

function blinkColor()
{
  document.getElementById("inbox").style.background="red";
  setTimeout("setblinkColor()",500);
}

function setblinkColor()
{
  document.getElementById("inbox").style.background="green";
  setTimeout("blinkColor()",500);
}

function dateFormat(date, format) {
    // Calculate date parts and replace instances in format string accordingly
    format = format.replace("DD", (date.getDate() < 10 ? '0' : '') + date.getDate()); // Pad with '0' if needed
    format = format.replace("MM", (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1)); // Months are zero-based
    format = format.replace("YYYY", date.getFullYear());
    return format;
}