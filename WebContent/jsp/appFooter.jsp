<%@page import="com.en.util.Constant"%>
<!-- <table style="width: 100%;"><tr><td align="right" valign="top"><label style="color: blue; font-size: 9px; vertical-align: middle;">Powered By <a href="javascript:window.open('http://www.conoscenz.com');" style="font-family: Castellar;"><img alt="conoscenz" style="height: 16px; width: 16px;" src="images/conoscenz_logo.png"> CONOSCENZ TECHNOLOGY</a></label></td></tr></table> -->
<table style="width: 100%;"><tr><td align="right"><label style="color: blue; font-size: 9px;">Powered By <a href="javascript:window.open('http://www.conoscenz.com');" style="font-family: Castellar;">CONOSCENZ TECHNOLOGY</a></label></td></tr></table>
<link href="images/favicon.ico" rel="icon" type="image/x-icon" />
<input type="hidden" name="changeAccessPoint" />
<%
	if(request.getAttribute(Constant.NEW_INBOX_COUNT) != null && (Integer) request.getAttribute(Constant.NEW_INBOX_COUNT) > 0){
%>
<script type="text/javascript">
setTimeout("blinkColor()",500);
</script>
<%
	}
%>