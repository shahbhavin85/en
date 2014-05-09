package com.en.handler;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.model.ConversationModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.TaskModel;
import com.en.model.UserModel;

public class MessengerHandler extends Basehandler {

	public static String getPageName() {
		return MESSENGER_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "newConversation.jsp";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		String strAction = "";
		if(isMultipart){
			@SuppressWarnings("unchecked")
			List<FileItem> list = (List<FileItem>) request.getAttribute(MUTLIPART_DATA);
			Iterator<FileItem> itr = list.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					if(name.equals(ACTIONS)){
						strAction = value;
						break;
					}
				} else {
					continue;
				}
			}
		} else {
			strAction = request.getParameter(ACTIONS) == null ? "" : request.getParameter(ACTIONS);
		}
        if(strAction.equals(START_CONVERSATION)){
        	pageType = startConversation(request);			
		} else if(strAction.equals(ADD_COMMENT)){
        	pageType = addComment(request);			
		} else if(strAction.equals(MY_CONVERSATION)){
        	pageType = getMyConversations(request);			
		} else if(strAction.equals(MY_CLOSED_CONVERSATION)){
        	pageType = getMyClosedConversations(request);			
		} else if(strAction.equals(SHOW_CONVERSATION)){
        	pageType = showConversation(request);			
		} else if(strAction.equals(CLOSE_CONVERSATION)){
        	pageType = closeConversation(request);			
		} else if(strAction.equals(REOPEN_CONVERSATION)){
        	pageType = reopenConversation(request);			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private String getMyClosedConversations(HttpServletRequest request) {
		ConversationModel[] conversations = getMessengerBroker().getMyClosedConversations((UserModel) request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, conversations);
		return "myClosedConversation.jsp";
	}

	private String reopenConversation(HttpServletRequest request) {
        getMessengerBroker().changeConversationStatus(Integer.parseInt(request.getParameter("txtConversationId")),0);
		return showConversation(request);
	}

	private String closeConversation(HttpServletRequest request) {
        getMessengerBroker().changeConversationStatus(Integer.parseInt(request.getParameter("txtConversationId")),1);
		return showConversation(request);
	}

	private String showConversation(HttpServletRequest request) {
        ConversationModel model = getMessengerBroker().getConversation(Integer.parseInt(request.getParameter("txtConversationId")),(UserModel)request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, model);
		return "showConversation.jsp";
	}

	private String getMyConversations(HttpServletRequest request) {
		ConversationModel[] conversations = getMessengerBroker().getMyConversations((UserModel) request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, conversations);
		return "myConversation.jsp";
	}

	private String addComment(HttpServletRequest request) {
		MessageModel msgs = new MessageModel();
		try{
			HashMap<String, String> reqMap = new HashMap<String, String>(0);
			
			@SuppressWarnings("unchecked")
			List<FileItem> list = (List<FileItem>) request.getAttribute(MUTLIPART_DATA);

//			String imgDir = "E://HESH_MESSENGER_FILES//";
			String fileName = "";
			String imgDir = "webapps//HESH_MESSENGER_FILES//";
//			String imgDir = "ITEM_IMG//";
			
			Iterator<FileItem> itr = list.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					reqMap.put(name, value);
				} else {
					fileName = item.getName();
				}
			}
			
			TaskModel task = new TaskModel();
			task.setConversationId(Integer.parseInt(reqMap.get("txtConversationId")));
			task.setParentId(Integer.parseInt(reqMap.get("txtParentId")));
			task.setDescription(reqMap.get("taDescription"));
			if(fileName != null && !fileName.equals("")){
				task.setAttachment(true);
				task.setFileName(fileName);
			}
			task.setOwner((UserModel)request.getSession().getAttribute(USER));
			
			int taskId = getMessengerBroker().addComment(task);
			
			if(task.isAttachment()){
				File dir = new File(imgDir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				imgDir = imgDir + "//"+taskId+"//";
				dir = new File(imgDir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				itr = list.iterator();
				File saveFile = new File(imgDir + fileName);
				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (!item.isFormField()) {
						item.write(saveFile);
					}
				}
			}

	        ConversationModel model = getMessengerBroker().getConversation(Integer.parseInt(reqMap.get("txtConversationId")), (UserModel)request.getSession().getAttribute(USER));
			request.setAttribute(FORM_DATA, model);
			
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while uploading photo!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
		
		return "showConversation.jsp";
	}

	private String startConversation(HttpServletRequest request) {
		String subject = request.getParameter("txtSubject");
		String[] users = request.getParameterValues("chkUsers");
		ConversationModel model = new ConversationModel();
		model.setSubject(subject);
		model.setOwner((UserModel)request.getSession().getAttribute(USER));
		UserModel usr = null;
		for(int i=0; i<users.length; i++){
			usr = new UserModel();
			usr.setUserId(users[i]);
			model.getRecipients().add(usr);
		}
		MessageModel msg = getMessengerBroker().startConversation(model);
		model = getMessengerBroker().getConversation(Integer.parseInt(msg.getMessages().get(1).getMsg()), (UserModel)request.getSession().getAttribute(USER));
		request.setAttribute(FORM_DATA, model);
		return "showConversation.jsp";
	}

	private void initialize(HttpServletRequest request) {
		UserModel[] users = getUserBroker().getUsers();
		request.setAttribute(USERS, users);
		return;
	}

}
