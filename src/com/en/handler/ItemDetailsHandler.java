package com.en.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.broker.ItemBroker;
import com.en.model.CustomerModel;
import com.en.model.ItemCategoryModel;
import com.en.model.ItemGroupModel;
import com.en.model.ItemModel;
import com.en.model.Message;
import com.en.model.MessageModel;
import com.en.model.UserModel;
import com.en.util.EmailUtil;

public class ItemDetailsHandler extends Basehandler {

	public static String getPageName() {
		return ITM_DTLS_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "itemDetails.jsp";
		String strAction = "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
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
		// action to taken
        if(strAction.equals(UPLOAD_PICTURE)){
			pageType = uploadPicture(request,response);			
		} else if(strAction.equals(INIT_UPLOAD_CATALOGUE)){
			initialize(request);
			pageType = "uploadCatalogue.jsp";			
		} else if(strAction.equals(INIT_VIEW_ITEM_DETAILS)){
			initialize(request);
			pageType = "viewItemDetails.jsp";			
		} else if(strAction.equals(INIT_MAIL_ITEM_DETAILS)){
			initializeMail(request);
			pageType = "mailItemDetails.jsp";			
		} else if(strAction.equals(MAIL_ITEM_DETAILS)){
			mailCatalogue(request);
			initializeMail(request);
			pageType = "mailItemDetails.jsp";			
		} else if(strAction.equals(VIEW_ITEM_DETAILS)){
			getItemDetails(request);
			initialize(request);
			pageType = "viewItemDetails.jsp";			
		} else if(strAction.equals(UPLOAD_CATALOGUE)){
			pageType = uploadCatalogue(request,response);			
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void mailCatalogue(HttpServletRequest request) {
		ItemModel item = getItemBroker().getItemDtls(Integer.parseInt(request.getParameter("sItem")));
		CustomerModel model = new CustomerModel();
		model.setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		model.setEmail(request.getParameter("txtEmail"));
		model.setEmail1(request.getParameter("txtEmail1"));
		getCustomerBroker().updateCustomerEmails(model);
		request.setAttribute(MESSAGES, EmailUtil.sendItemCatalogue(model, item, (UserModel) request.getSession().getAttribute(USER)));
		return;
	}

	private void initializeMail(HttpServletRequest request) {
		ItemModel[] items = getItemBroker().getItem();	
		ArrayList<ItemModel> itemsWithCatalogue = new ArrayList<ItemModel>(0);
		for(int i=0; i<items.length; i++){
			if(items[i].isCatalogue())
				itemsWithCatalogue.add(items[i]);
		}
		request.setAttribute(ITEMS, (ItemModel[]) itemsWithCatalogue.toArray(new ItemModel[0]));
	}

	private void getItemDetails(HttpServletRequest request) {
		ItemBroker broker = getItemBroker();
		ItemModel model = broker.getItemDtls(Integer.parseInt(request.getParameter("sItem")));
		request.setAttribute(FORM_DATA, model);
		HashMap<String,Integer> items = getItemBroker().getItemCheckList(model.getItemId()+"");
		request.setAttribute("checklist", items);
		ItemCategoryModel[] itemCats = getItemCategoryBroker().getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		return;
	}

	private String uploadCatalogue(HttpServletRequest request,
			HttpServletResponse response) {
		MessageModel msgs = new MessageModel();
		try{
			HashMap<String, String> reqMap = new HashMap<String, String>(0);
			String itemId = "";
			
			@SuppressWarnings("unchecked")
			List<FileItem> list = (List<FileItem>) request.getAttribute(MUTLIPART_DATA);

			String imgDir = "webapps//HESH_ITEM_CATALOGUE//";
//			String imgDir = "ITEM_CATALOGUE//";
			
			File dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			Iterator<FileItem> itr = list.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					reqMap.put(name, value);
					if(name.equals("sItem")){
						itemId = value;
					}
				}
			}
			itr = list.iterator();
			File saveFile = new File(imgDir + itemId + ".pdf");
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (!item.isFormField()) {
					item.write(saveFile);
				}
			}
			
			getItemBroker().uploadCatalogue(itemId);

			msgs.addNewMessage(new Message(SUCCESS, "Catalogue Uploaded successfully!!!"));
	        
	        initialize(request);
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while uploading Catalogue!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
        
		return "uploadCatalogue.jsp";
	}

	private String uploadPicture(HttpServletRequest request,
			HttpServletResponse response) {
		MessageModel msgs = new MessageModel();
		try{
			HashMap<String, String> reqMap = new HashMap<String, String>(0);
			String itemId = "";
			
			@SuppressWarnings("unchecked")
			List<FileItem> list = (List<FileItem>) request.getAttribute(MUTLIPART_DATA);

			String imgDir = "webapps//HESH_ITEM_IMG//";
//			String imgDir = "ITEM_IMG//";
			
			File dir = new File(imgDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			Iterator<FileItem> itr = list.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					reqMap.put(name, value);
					if(name.equals("sItem")){
						itemId = value;
					}
				}
			}
			itr = list.iterator();
			File saveFile = new File(imgDir + itemId + ".jpg");
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (!item.isFormField()) {
					item.write(saveFile);
				}
			}
			
			getItemBroker().uploadPicture(itemId);

			msgs.addNewMessage(new Message(SUCCESS, "Photo Uploaded successfully!!!"));
	        
	        initialize(request);
	        
		} catch (Exception e) {
	        msgs.addNewMessage(new Message(ERROR, "Error occured while uploading photo!!!"));
	        e.printStackTrace();
		}

        request.setAttribute(MESSAGES, msgs);
        
		return "itemDetails.jsp";
	}

	private void initialize(HttpServletRequest request) {
		ItemCategoryModel[] items = getItemBroker().getItem();
		request.setAttribute(ITEMS, items);
		return;
	}

}