<%@page import="com.en.model.AccessPointModel"%>
<%@page import="com.en.model.UserModel"%>
<%@page import="com.en.util.Constant"%>
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript">
    ddlevelsmenu.setup("ddtopmenubar", "topbar");
</script> 
<table width="100%" class="appHeader">
	<tr>
	<td valign="middle"><label style="color: white; font-size: 20px; "><%=Constant.TITLE %></label></td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0" style="background-color: #8A2BE2; color: white; min-width: 1000px;">
    <tr>
        <td valign="top">
            <div id="ddtopmenubar" class="mattblackmenu">
                <ul>
                    <li><a href="#" onclick="fnGoToAdminPage('SYNC_DATA_HANDLER','');"><b>Sync DB Data</b></a></li>
                    <li><a href="#" rel="entryMenu"><b>Inward Entry</b></a></li>
                    <li><a href="#" onclick="fnGoToAdminPage('ADMIN_MASTER_HANDLER','');"><b>Edit Master Data</b></a></li>
                    <li><a href="#" onclick="fnRemove();"><b>Remove</b></a></li>
              	</ul>
            </div>
        </td>
        <td align="right" style="padding-right: 20px;"><a href="javascript : fnHelp();"><img alt="help" src="images/help.gif"><font style="font-size: small;color: orange; font-weight: bold; text-decoration: underline;">Help</font></a>
      </td>
    </tr>
</table>
<ul id="entryMenu" class="ddsubmenustyle">
	<li><a href="#" onclick="fnGoToPage('INWARD_ENTRY_HANDLER','');">New Entry</a></li>
    <li><a href="#" onclick="fnGoToPage('INWARD_ENTRY_HANDLER','INIT_VIEW_EDIT_QUOTATION');">View / Edit Entry</a></li>
</ul>