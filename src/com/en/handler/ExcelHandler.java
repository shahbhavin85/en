package com.en.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class ExcelHandler extends Basehandler {

	public static String getPageName() {
		return EXCEL_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "masterExcel.jsp";
		String strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
        if(strAction.equals(BACK_UP)){
        	pageType = makeDBBackup(request);		
		} else if(strAction.equals(INIT_LOCK_TXN)){
			pageType = "lockTxn.jsp";
		} else if(strAction.equals(INIT_UNLOCK_TXN)){
			pageType = "unlockTxn.jsp";
		} else if(strAction.equals(LOCK_TXN)){
			lockTransaction(request);
			pageType = "lockTxn.jsp";
		} else if(strAction.equals(UNLOCK_TXN)){
			unlockTransaction(request);
			pageType = "unlockTxn.jsp";
		} else if(strAction.equals(USER_PROFILE)){
			pageType = "userProfile.jsp";
		}
		return pageType;
	}

	private void unlockTransaction(HttpServletRequest request) {
		String unlockDt = Utils.convertToSQLDate(request.getParameter("txtUnlockDate"));
		MessageModel msg = getAdminBroker().unlockTransaction(unlockDt);
		request.setAttribute(MESSAGES, msg);
		return;
	}

	private void lockTransaction(HttpServletRequest request) {
		String lockDt = Utils.convertToSQLDate(request.getParameter("txtLockDate"));
		MessageModel msg = getAdminBroker().lockTransaction(lockDt);
		request.setAttribute(MESSAGES, msg);
		return;
	}

	private String makeDBBackup(HttpServletRequest request) {
		try{
			if(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) == 2 || Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)) > 10){
				ActionHandler.getOpeninfBalance(Integer.parseInt((String)request.getSession().getAttribute(ACCESS_POINT)), request);
			}
			String mysqlDir = getBackupBroker().getMysqlPath() ;
			String backupDir = "E:\\DATA_BACK_UP\\";
			File dir = new File(backupDir);
			if (!dir.exists()) 
			{
				dir.mkdir();
			}
	    	String dbName = "hesh10";
	    	String dbUser = "root";
	    	String dbPass = "admin";
	    	String executeCmd = "";
	    	executeCmd = mysqlDir+"\\bin\\mysqldump -u "+dbUser+" -p"+dbPass+" "+dbName+" -r "+backupDir+"hesh10_"+DateUtil.getCurrDt()+".sql";
	    	Process runtimeProcess =Runtime.getRuntime().exec(executeCmd);
	    	int processComplete = runtimeProcess.waitFor();
			MessageModel msg = new MessageModel();
	    	if(processComplete == 0){
	    		msg.addNewMessage(new Message(SUCCESS,"Backup taken successfully"));
	    	} else {
	    		msg.addNewMessage(new Message(ERROR,"Could not take mysql backup"));
	    	}
	    	request.setAttribute(MESSAGES, msg);
	    	String filename = backupDir+"hesh10_"+DateUtil.getCurrDt()+".sql";
	        FileInputStream fis = new FileInputStream(filename);
	        byte[] b = new byte[fis.available()];
	        fis.read(b, 0, b.length);
	        ZipOutputStream zos = new ZipOutputStream(
	                        (OutputStream) new FileOutputStream(backupDir+"hesh10_"+DateUtil.getCurrDt()+".zip"));
	        ZipEntry entry = new ZipEntry(filename);
	        entry.setSize((long) b.length);
	        zos.setLevel(6);
	        zos.putNextEntry(entry);
	        zos.write(b, 0, b.length);
	        zos.finish();
	        zos.close();
	        fis.close();
	        File file = new File(filename);
	        file.delete();
	        // saving in other directory
	        backupDir = "F:\\DATA_BACK_UP\\";
			dir = new File(backupDir);
			if (!dir.exists()) 
			{
				dir.mkdir();
			}
	    	executeCmd = mysqlDir+"\\bin\\mysqldump -u "+dbUser+" -p"+dbPass+" "+dbName+" -r "+backupDir+"hesh10_"+DateUtil.getCurrDt()+".sql";
	    	runtimeProcess =Runtime.getRuntime().exec(executeCmd);
	    	processComplete = runtimeProcess.waitFor();
			msg = new MessageModel();
	    	if(processComplete == 0){
	    		msg.addNewMessage(new Message(SUCCESS,"Backup taken successfully"));
	    	} else {
	    		msg.addNewMessage(new Message(ERROR,"Could not take mysql backup"));
	    	}
	    	request.setAttribute(MESSAGES, msg);
	    	filename = backupDir+"hesh10_"+DateUtil.getCurrDt()+".sql";
	        fis = new FileInputStream(filename);
	        b = new byte[fis.available()];
	        fis.read(b, 0, b.length);
	        zos = new ZipOutputStream(
	                        (OutputStream) new FileOutputStream(backupDir+"hesh10_"+DateUtil.getCurrDt()+".zip"));
	        entry = new ZipEntry(filename);
	        entry.setSize((long) b.length);
	        zos.setLevel(6);
	        zos.putNextEntry(entry);
	        zos.write(b, 0, b.length);
	        zos.finish();
	        zos.close();
	        fis.close();
	        file = new File(filename);
	        file.delete();
			
	        HashMap<String, String> homeSetting = getHomeSettingBroker().getHomeDetails();
			if(homeSetting != null){
				Iterator<String> itr = homeSetting.keySet().iterator();
				String key = "";
				while (itr.hasNext()){
					key = itr.next();
					request.setAttribute(key, homeSetting.get(key));
				}
			}
			ActionHandler.getPendingRequestnDemand(request);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return "userhome.jsp";	
		}

}
