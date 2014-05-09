<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.en.util.DateUtil"%>
<%@page import="com.en.util.Utils"%>
<%@page import="com.en.model.AdminItemModel"%>
<%@page import="com.en.model.ItemModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>|| <%=Constant.TITLE %> || Powered By CONOSCENZ TECHNOLOGY</title>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<style type="text/css">
input {
font-size : 11px;
}
</style>
</head>
<body>
<jsp:include page="adminMenu.jsp"></jsp:include>
<form method="post" name="frmUserHome">
	<div class="formClass">
		<fieldset>
			<legend class="screenHeader">Admin Master Screen</legend><br>
			<jsp:include page="messages.jsp"></jsp:include>
			<b>Please click the save button to save the modified data.</b>
			<br/><br/>
			<center>
				<input type="button" value= "Save" onclick="fnSave();"/>
			</center><br/>
			<table border="1" cellspacing="0" align="center" width="100%" style="font-size: 11px; font-family: tahoma;">
				<tr>
					<th rowspan="2">&nbsp;</th>
					<th rowspan="2" style="width: 200px;">ITEM NUMBER - (ITEM NAME)<br/>CHINA NAME</th>
					<th rowspan="2">INDIA PRICE</th>
					<th rowspan="2">CHINA PRICE</th>
					<th rowspan="2">IS INDIAN</th>
					<th rowspan="2">LAST UPDATED</th>
					<th colspan="3" style="background-color: #66FF99;">COST PRICE</th>
					<th rowspan="2" style="background-color: pink;">CP</th>
					<th rowspan="2" style="background-color: pink;">TRANSFER<br/> TAX</th>
					<th rowspan="2" style="background-color: pink;">NETT CP</th>
					<th colspan="3" style="background-color: #FF9900;">SELL PRICE</th>
					<th colspan="2" style="background-color: #CCDDEE;">MARGIN</th>
					<th colspan="3" style="background-color: yellow;">RETAIL</th>
					<th colspan="3" style="background-color: aqua;">WHOLESALE</th>
					<th colspan="3" style="background-color: silver;">FRANCHISE</th>
				</tr>
				<tr>
					<th style="background-color: #66FF99;">CURR</th>
					<th style="background-color: #66FF99;">AVG</th>
					<th style="background-color: #66FF99;">%</th>
					<th style="background-color: #FF9900;">CURR</th>
					<th style="background-color: #FF9900;">AVG</th>
					<th style="background-color: #FF9900;">%</th>
					<th style="background-color: #CCDDEE;">AMT</th>
					<th style="background-color: #CCDDEE;">%</th>
					<th style="background-color: yellow;">MAR. AMT</th>
					<th style="background-color: yellow;">MAR. %</th>
					<th style="background-color: yellow;">RSP</th>
					<th style="background-color: aqua;">MAR. AMT</th>
					<th style="background-color: aqua;">MAR. %</th>
					<th style="background-color: aqua;">WSP</th>
					<th style="background-color: silver;">MAR. AMT</th>
					<th style="background-color: silver;">MAR. %</th>
					<th style="background-color: silver;">FSP</th>
				</tr>
			<%
				AdminItemModel[] items = (request.getAttribute(Constant.FORM_DATA) != null) ? (AdminItemModel[])(request.getAttribute(Constant.FORM_DATA)) : new AdminItemModel[0];
				if(items.length > 0){
					double cp = 0;
			%>
			<%
					for(int i=0; i<items.length; i++){
						cp = (items[i].isIndian()) ? items[i].getServerCP() : items[i].getCurrCP();
			%>		
					<tr id="tr<%=items[i].getItemId()%>">
						<td align="center"><input type="checkbox" name="chkItem" value="<%=items[i].getItemId() %>" onchange="fnRowSelect(this, <%=items[i].getItemId() %>);"/> </td>
						<td align="center"><%=items[i].getItemNumber() +" - ("+items[i].getItemName()+")" %><br/><input disabled="disabled" type="text" value="<%=items[i].getRefName()%>" maxlength="100" style="width: 195px;" name="name<%=items[i].getItemId()%>"/></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getServerCP()) %></td>
						<td align="center"><%=Utils.get2Decimal(items[i].getChinaRate()) %></td>
						<td align="center"><input type="checkbox" name="chkIndian<%=items[i].getItemId()%>" disabled="disabled" value = "1" <%=(items[i].isIndian()) ? "checked=\"checked\"" : "" %>></td>
						<td align="center"><%=(items[i].getUpdateDate().equals("0000-00-00")) ? "--" : Utils.convertToAppDateDDMM(items[i].getUpdateDate()) %></td>
						<td align="center" style="background-color: #66FF99;"><%=Utils.get2Decimal(items[i].getCurrCP()) %></td>
						<td align="center" style="background-color: #66FF99;"><%=Utils.get2Decimal(items[i].getAvgCP()) %></td>
						<td align="center" style="background-color: #66FF99; font-weight: bold; color: <%=(items[i].getAvgCP() != 0 && Utils.get2DecimalDouble((items[i].getLastCP()-items[i].getAvgCP())/items[i].getAvgCP()*100) > 0) ? "Green" : "Red"%>"><%=(items[i].getAvgCP() != 0) ? Utils.get2Decimal((items[i].getLastCP()-items[i].getAvgCP())/items[i].getAvgCP()*100) : 0.00%>%</td>
						<td align="center" style="background-color: pink; font-weight: bold;"><%=cp%><br/><input disabled="disabled" name="cp<%=items[i].getItemId()%>" onblur="fnCheck(this,0,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="4" value="<%=cp%>" /></td>
						<td align="center" style="background-color: pink; font-weight: bold;"><%=items[i].getTT() %>%<br/><input disabled="disabled" name="tt<%=items[i].getItemId()%>" onblur="fnCheck(this,0,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="4" value="<%=items[i].getTT() %>" /></td>
						<td align="center" style="background-color: pink; font-weight: bold;"><%=Utils.get2Decimal(cp*((100+items[i].getTT())/100))%><br/><input readonly="readonly" name="ncp<%=items[i].getItemId()%>"  style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="4" value="<%=Utils.get2Decimal(cp*((100+items[i].getTT())/100))%>" /></td>
						<td align="center" style="background-color: #FF9900;"><%=Utils.get2Decimal(items[i].getCurrSP()) %></td>
						<td align="center" style="background-color: #FF9900;"><%=Utils.get2Decimal(items[i].getAvgSP())%></td>
						<td align="center" style="background-color: #FF9900;"><%=(items[i].getCurrSP() != 0) ? Utils.get2Decimal(100-Utils.get2DecimalDouble(items[i].getAvgSP()/items[i].getCurrSP()*100)) : 0.00%>%</td>
						<td align="center" style="background-color: #CCDDEE;"><%=(items[i].getAvgCP() != 0) ? Utils.get2Decimal((items[i].getAvgSP() - items[i].getAvgCP())) : 0.00%></td>
						<td align="center" style="background-color: #CCDDEE;"><%=(items[i].getAvgCP() != 0) ? Utils.get2Decimal((items[i].getAvgSP() - items[i].getAvgCP())/items[i].getAvgCP()*100) : 0.00%>%</td>
						<td align="center" style="background-color: yellow; font-weight: bold;"><%= Utils.get2Decimal(items[i].getSP1() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100)) %><br/><input disabled="disabled" name="ma1<%=items[i].getItemId()%>" onblur="fnCheck(this,11,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="4" value="<%=Utils.get2Decimal(items[i].getSP1() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100)) %>" /></td>
						<td align="center" style="background-color: yellow; font-weight: bold;"><%=(cp != 0) ? Utils.get2Decimal((items[i].getSP1() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100))*100/cp) : 0.00%>%<br/><input disabled="disabled" name="mp1<%=items[i].getItemId()%>" onblur="fnCheck(this,12,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="6" value="<%=(cp != 0) ? Utils.get2Decimal((items[i].getSP1() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100))*100/cp) : 0.00%>" /></td>
						<td align="center" style="background-color: yellow; font-weight: bold;"><%=items[i].getSP1() %><br/><input disabled="disabled" name="sp1<%=items[i].getItemId()%>" onblur="fnCheck(this,13,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="8" value="<%=items[i].getSP1() %>" /></td>
						<td align="center" style="background-color: aqua; font-weight: bold;"><%=Utils.get2Decimal(items[i].getSP2() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100)) %><br/><input disabled="disabled" name="ma2<%=items[i].getItemId()%>" onblur="fnCheck(this,21,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="6" value="<%=Utils.get2Decimal(items[i].getSP2() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100)) %>" /></td>
						<td align="center" style="background-color: aqua; font-weight: bold;"><%=(cp != 0) ? Utils.get2Decimal((items[i].getSP2() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100))*100/cp) : 0.00%>%<br/><input disabled="disabled" name="mp2<%=items[i].getItemId()%>" onblur="fnCheck(this,22,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="4" value="<%=(cp != 0) ? Utils.get2Decimal((items[i].getSP2() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100))*100/cp) : 0.00%>" /></td>
						<td align="center" style="background-color: aqua; font-weight: bold;"><%=items[i].getSP2() %><br/><input disabled="disabled" name="sp2<%=items[i].getItemId()%>" onblur="fnCheck(this,23,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="8" value="<%=items[i].getSP2() %>" /></td>
						<td align="center" style="background-color: silver; font-weight: bold;"><%=Utils.get2Decimal(items[i].getSP3() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100)) %><br/><input disabled="disabled" name="ma3<%=items[i].getItemId()%>" onblur="fnCheck(this,31,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="6" value="<%=Utils.get2Decimal(items[i].getSP3() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100)) %>" /></td>
						<td align="center" style="background-color: silver; font-weight: bold;"><%=(cp != 0) ? Utils.get2Decimal((items[i].getSP3() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100))*100/cp) : 0.00%>%<br/><input disabled="disabled" name="mp3<%=items[i].getItemId()%>" onblur="fnCheck(this,32,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="4" value="<%=(cp != 0) ? Utils.get2Decimal((items[i].getSP3() - Utils.get2DecimalDouble(cp * (100+items[i].getTT()) /100))*100/cp) : 0.00%>" /></td>
						<td align="center" style="background-color: silver; font-weight: bold;"><%=items[i].getSP3() %><br/><input disabled="disabled" name="sp3<%=items[i].getItemId()%>" onblur="fnCheck(this,33,<%=items[i].getItemId()%>);" style="text-align: right;" type="text" onkeypress="return numbersonly(this, event, true)" size="8" value="<%=items[i].getSP3() %>" /></td>
					</tr>		
			<%
					}
				}
			%>
			</table><br/>
			<center>
				<input type="button" value= "Save" onclick="fnSave();"/>
			</center>
		</fieldset>
		<jsp:include page="appFooter.jsp"></jsp:include>
	</div>
	<input type="hidden" name="<%=Constant.HANDLERS%>"/>
	<input type="hidden" name="<%=Constant.ACTIONS%>"/>
</form>
</body>
<script type="text/javascript">
function fnRowSelect(cmb, id){
	var vTR = document.getElementById("tr"+id);
	if(cmb.checked){
		eval('document.forms[0].name'+id).disabled = false;
		eval('document.forms[0].cp'+id).disabled = false;
		eval('document.forms[0].tt'+id).disabled = false;
		eval('document.forms[0].ma1'+id).disabled = false;
		eval('document.forms[0].mp1'+id).disabled = false;
		eval('document.forms[0].sp1'+id).disabled = false;
		eval('document.forms[0].ma2'+id).disabled = false;
		eval('document.forms[0].mp2'+id).disabled = false;
		eval('document.forms[0].sp2'+id).disabled = false;
		eval('document.forms[0].ma3'+id).disabled = false;
		eval('document.forms[0].mp3'+id).disabled = false;
		eval('document.forms[0].sp3'+id).disabled = false;
		eval('document.forms[0].chkIndian'+id).disabled = false;
		vTR.style["font-weight"] = 'bold';
		vTR.style.color = 'white';
		vTR.style.border = 'red';
		vTR.style.background = 'black';
		for(var k=0; k<26; k++){
			vTR.getElementsByTagName("td")[k].style.background = 'black';
		}
		eval('document.forms[0].name'+id).focus();
	} else {
		eval('document.forms[0].name'+id).disabled = true;
		eval('document.forms[0].cp'+id).disabled = true;
		eval('document.forms[0].tt'+id).disabled = true;
		eval('document.forms[0].ma1'+id).disabled = true;
		eval('document.forms[0].mp1'+id).disabled = true;
		eval('document.forms[0].sp1'+id).disabled = true;
		eval('document.forms[0].ma2'+id).disabled = true;
		eval('document.forms[0].mp2'+id).disabled = true;
		eval('document.forms[0].sp2'+id).disabled = true;
		eval('document.forms[0].ma3'+id).disabled = true;
		eval('document.forms[0].mp3'+id).disabled = true;
		eval('document.forms[0].sp3'+id).disabled = true;
		eval('document.forms[0].chkIndian'+id).disabled = true;
		eval('document.forms[0].name'+id).value = eval('document.forms[0].name'+id).defaultValue;
		eval('document.forms[0].cp'+id).value = eval('document.forms[0].cp'+id).defaultValue;
		eval('document.forms[0].tt'+id).value = eval('document.forms[0].tt'+id).defaultValue;
		eval('document.forms[0].ma1'+id).value = eval('document.forms[0].ma1'+id).defaultValue; 
		eval('document.forms[0].mp1'+id).value = eval('document.forms[0].mp1'+id).defaultValue; 
		eval('document.forms[0].sp1'+id).value = eval('document.forms[0].sp1'+id).defaultValue; 
		eval('document.forms[0].ma2'+id).value = eval('document.forms[0].ma2'+id).defaultValue; 
		eval('document.forms[0].mp2'+id).value = eval('document.forms[0].mp2'+id).defaultValue; 
		eval('document.forms[0].sp2'+id).value = eval('document.forms[0].sp2'+id).defaultValue; 
		eval('document.forms[0].ma3'+id).value = eval('document.forms[0].ma3'+id).defaultValue; 
		eval('document.forms[0].mp3'+id).value = eval('document.forms[0].mp3'+id).defaultValue; 
		eval('document.forms[0].sp3'+id).value = eval('document.forms[0].sp3'+id).defaultValue; 
		eval('document.forms[0].ncp'+id).value = eval('document.forms[0].ncp'+id).defaultValue; 
		eval('document.forms[0].chkIndian'+id).value = eval('document.forms[0].chkIndian'+id).defaultValue;
		vTR.style["font-weight"] = 'normal';
		vTR.style.color = 'black';
		vTR.style.border = 'black';
		vTR.style.background = 'transparent';
		var color = 'transparent';
		for(var k=0; k<26; k++){
			if(k>5)
				color = '#66FF99';
			if(k>8)
				color = 'pink';
			if(k>11)
				color = '#FF9900';
			if(k>14)
				color = '#CCDDEE';
			if(k>16)
				color = 'yellow';
			if(k>19)
				color = 'aqua';
			if(k>22)
				color = 'silver';
			vTR.getElementsByTagName("td")[k].style.background = color;
		}
	}
	return;
}

function fnCheck(c, cId, itemId){
	if(c.value == ''){
		c.value = 0.00;
	}
	fnRecalculate(cId, itemId);
}

function fnRecalculate(id, itemId){
	var cp = eval('document.forms[0].cp'+itemId);
	var tt = eval('document.forms[0].tt'+itemId);
	var ncp = eval('document.forms[0].ncp'+itemId);
	var ma1 = eval('document.forms[0].ma1'+itemId);
	var mp1 = eval('document.forms[0].mp1'+itemId);
	var sp1 = eval('document.forms[0].sp1'+itemId);
	var ma2 = eval('document.forms[0].ma2'+itemId);
	var mp2 = eval('document.forms[0].mp2'+itemId);
	var sp2 = eval('document.forms[0].sp2'+itemId);
	var ma3 = eval('document.forms[0].ma3'+itemId);
	var mp3 = eval('document.forms[0].mp3'+itemId);
	var sp3 = eval('document.forms[0].sp3'+itemId);
	var tempCP = (parseFloat(cp.value)*(100+parseFloat(tt.value))/100).toFixed(2);
	if(id == 0){
		ncp.value = tempCP;
		ma1.value = ((parseFloat(sp1.value)-tempCP)).toFixed(2);
		mp1.value = ((parseFloat(sp1.value)-tempCP)*100/tempCP).toFixed(2);
		ma2.value = ((parseFloat(sp2.value)-tempCP)).toFixed(2);
		mp2.value = ((parseFloat(sp2.value)-tempCP)*100/tempCP).toFixed(2);
		ma3.value = ((parseFloat(sp3.value)-tempCP)).toFixed(2);
		mp3.value = ((parseFloat(sp3.value)-tempCP)*100/tempCP).toFixed(2);
		return;
	} else if(id == 11){
		sp1.value = ((parseFloat(ma1.value)+parseFloat(tempCP))).toFixed(2);
		mp1.value = ((parseFloat(sp1.value)-parseFloat(tempCP))*100/tempCP).toFixed(2);
		return;
	} else if(id == 21){
		sp2.value = ((parseFloat(ma2.value)+parseFloat(tempCP))).toFixed(2);
		mp2.value = ((parseFloat(sp2.value)-parseFloat(tempCP))*100/tempCP).toFixed(2);
		return;
	} else if(id == 31){
		sp3.value = ((parseFloat(ma3.value)+parseFloat(tempCP))).toFixed(2);
		mp3.value = ((parseFloat(sp3.value)-parseFloat(tempCP))*100/tempCP).toFixed(2);
		return;
	} else if(id == 12){
		sp1.value = (((100+parseFloat(mp1.value))*parseFloat(tempCP))/100).toFixed(2);
		ma1.value = ((parseFloat(sp1.value)-tempCP)).toFixed(2);
		return;
	} else if(id == 22){
		sp2.value = (((100+parseFloat(mp2.value))*parseFloat(tempCP))/100).toFixed(2);
		ma2.value = ((parseFloat(sp2.value)-tempCP)).toFixed(2);
		return;
	} else if(id == 32){
		sp3.value = (((100+parseFloat(mp3.value))*parseFloat(tempCP))/100).toFixed(2);
		ma3.value = ((parseFloat(sp3.value)-tempCP)).toFixed(2);
		return;
	} else if(id == 13){
		mp1.value = ((parseFloat(sp1.value)-parseFloat(tempCP))*100/tempCP).toFixed(2);
		ma1.value = ((parseFloat(sp1.value)-tempCP)).toFixed(2);
		return;
	} else if(id == 23){
		mp2.value = ((parseFloat(sp2.value)-parseFloat(tempCP))*100/tempCP).toFixed(2);
		ma2.value = ((parseFloat(sp2.value)-tempCP)).toFixed(2);
		return;
	} else if(id == 33){
		mp3.value = ((parseFloat(sp3.value)-parseFloat(tempCP))*100/tempCP).toFixed(2);
		ma3.value = ((parseFloat(sp3.value)-tempCP)).toFixed(2);
		return;
	}
}

function fnSave(){
	document.forms[0].HANDLERS.value = 'ADMIN_MASTER_HANDLER';
	document.forms[0].ACTIONS.value = 'SAVE_DATA';
	fnFinalSubmit();
}
</script>
</html>