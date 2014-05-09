package com.en.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.broker.ItemBroker;
import com.en.model.AccessPointModel;
import com.en.model.CustomerModel;
import com.en.model.ItemCategoryModel;
import com.en.model.ItemGroupModel;
import com.en.model.ItemModel;
import com.en.model.MessageModel;
import com.en.model.UserModel;
import com.en.util.EmailUtil;
import com.en.util.Utils;

public class ItemHandler extends Basehandler {

	public static String getPageName() {
		return ITM_HANDLER;
	}

	@Override
	public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String pageType = "addItem.jsp";
		// action to taken
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
        if(strAction.equals(ADD_ITM)){
			addItem(request,response);			
		} else if(strAction.equals(MOD_ITM)){
			initializeModify(request);
			pageType = "modifyItem.jsp";			
		} else if(strAction.equals(UPDT_ITM)){
			updateItem(request);
			pageType = "modifyItem.jsp";			
		} else if(strAction.equals(GET_ITM)){
			getItem(request);
			pageType = "modifyItem.jsp";			
		} else if(strAction.equals(OFFER_PRICE_LIST)){
			getItemPriceList(request);
			pageType = "offerpricelist.jsp";			
		} else if(strAction.equals(PRICE_LIST)){
			getItemPriceList(request);
			pageType = "hesh_retail_price_list.pdf";			
		} else if(strAction.equals(WHOLESALES_PRICE_LIST)){
			getWholesalesItemPriceList(request);
			pageType = "hesh_wholesales_price_list.pdf";				
		} else if(strAction.equals(INIT_ITM_DIS)){
			getItemDiscountDtls(request);
			pageType = "itemDiscount.jsp";			
		} else if(strAction.equals(ITM_DIS)){
			addItemDiscountDtls(request);
			pageType = "itemDiscount.jsp";			
		} else if(strAction.equals(INIT_ITM_CHECK_LIST)){
			initializeModify(request);
			pageType = "itemCheckList.jsp";			
		} else if(strAction.equals(EMAIL_PRICE_LIST)){
			pageType = "emailPriceList.jsp";			
		} else if(strAction.equals(EMAIL_PRICE_LIST_1)){
			emailPriceList(request);
			pageType = "emailPriceList.jsp";			
		} else if(strAction.equals(GET_ITM_CHECK_LIST)){
			getItemCheckList(request);
			initializeModify(request);
			pageType = "itemCheckList.jsp";			
		} else if(strAction.equals(UPDATE_ITM_CHECK_LIST)){
			updateCheckList(request);
			getItemCheckList(request);
			initializeModify(request);
			pageType = "itemCheckList.jsp";			
		} else if(strAction.equals(LABEL)){
			if(request.getParameter("PRINT") != null && request.getParameter("PRINT").equals("Y")){
				printLabel(request);
				pageType = "printLabel.jsp";
			} else {
				initializeModify(request);
				pageType = "itemLabels.jsp";
			}
		} else {
			initialize(request);
		}
		return pageType;
	}

	private void emailPriceList(HttpServletRequest request) {
		CustomerModel model = new CustomerModel();
		model.setCustomerId(Integer.parseInt(request.getParameter("sCustomer")));
		model.setEmail(request.getParameter("txtEmail"));
		model.setEmail1(request.getParameter("txtEmail1"));
		getCustomerBroker().updateCustomerEmails(model);
		HashMap<String,HashMap<String,ItemModel[]>> items = getItemBroker().getItemPriceList();
		AccessPointModel access = new AccessPointModel();
		String version = getItemBroker().getPriceListVersion();
		EmailUtil.createRetailPriceListPDF(items, access, version);
		request.setAttribute(MESSAGES, EmailUtil.sendPriceList(model, "hesh_retail_price_list_ver_"+version+".pdf", (UserModel) request.getSession().getAttribute(USER)));
		return;
	}

	private void getWholesalesItemPriceList(HttpServletRequest request) {
		HashMap<String,HashMap<String,ItemModel[]>> items = getItemBroker().getItemPriceList();
		String version = getItemBroker().getPriceListVersion();
		EmailUtil.createWholesalesPriceListPDF(items, version);
		Utils.copyFile(new File("E:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\hesh_wholesales_price_list_ver_"+version+".pdf"), new File("webapps\\en\\jsp\\hesh_wholesales_price_list.pdf"));
//		Utils.copyFile(new File("F:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\hesh_wholesales_price_list.pdf"), new File("..\\webapps\\en\\jsp\\hesh_wholesales_price_list.pdf"));
		request.setAttribute("items", items);
		return;
	}

	private void updateCheckList(HttpServletRequest request) {
		String itemId = request.getParameter("sItem");
		HashMap<String,Integer> items = new HashMap<String, Integer>(0);
		String offerItems = request.getParameter("txtOfferItems");
		StringTokenizer stRow = new StringTokenizer(offerItems,"#");
		while (stRow.hasMoreElements()) {
			String tempRow = stRow.nextToken();
			StringTokenizer stCell = new StringTokenizer(tempRow,"|");
			items.put(stCell.nextToken(), Integer.parseInt(stCell.nextToken()));
		}
		MessageModel msg = getItemBroker().updateItemCheckList(itemId, items);
		request.setAttribute(MESSAGES, msg);
		return;
	}

	private void getItemCheckList(HttpServletRequest request) {
		String itemId = request.getParameter("sItem");
		request.setAttribute("sItem", itemId);
		HashMap<String,Integer> items = getItemBroker().getItemCheckList(itemId);
		request.setAttribute(FORM_DATA, items);
		return;
	}

	private void printLabel(HttpServletRequest request) {
		ArrayList<String> labels = new ArrayList<String>(0);
		String[] itemIds = request.getParameterValues("chk");
		String tempDesc = "";
		ItemModel temp = null;
		ItemBroker broker = getItemBroker();
		for(int i=0; i<itemIds.length; i++){
			temp = broker.getItemDtls(Integer.parseInt(itemIds[i]));
			tempDesc = "<font>MODEL NO : "+temp.getItemNumber()+"</font><br/><font style='color:red; font-size:8px;'>"+temp.getItemLabel().toUpperCase()+"</font>"+((!request.getParameter("desc"+itemIds[i]).equals("")) ? "<br/><font>"+request.getParameter("desc"+itemIds[i]).toUpperCase()+"</font>" : "")+"<br/><font>MRP : "+temp.getItemPrice()+"</font>";
//			tempDesc = "<font style='color:red;'>"+temp.getItemName().toUpperCase()+"</font><br/><font style='color:blue;'>"+request.getParameter("desc"+itemIds[i]).toUpperCase()+"</font><br/><font style='color:blue;'>MRP : "+temp.getItemPrice()+"</font><br/><img src='images/help.gif' />";
			for(int k=0; k<Integer.parseInt(request.getParameter("no"+itemIds[i])); k++){
				labels.add(tempDesc);
			}
		}
		request.setAttribute(FORM_DATA, (String[])labels.toArray(new String[0]));
		return;
	}

	private void getItemPriceList(HttpServletRequest request){
		HashMap<String,HashMap<String,ItemModel[]>> items = null;
		if(((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS)).getAccessId() > 10){
			items = getItemBroker().getBranchItemPriceList((AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS));
		} else {
			items = getItemBroker().getItemPriceList();
		}
		String version = getItemBroker().getPriceListVersion();
		EmailUtil.createRetailPriceListPDF(items, (AccessPointModel)request.getSession().getAttribute(ACCESS_POINT_DTLS), version);
//		Utils.copyFile(new File("F:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\hesh_retail_price_list.pdf"), new File("..\\webapps\\en\\jsp\\hesh_retail_price_list.pdf"));
//		Utils.copyFile(new File("E:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\hesh_retail_price_list.pdf"), new File("hesh_retail_price_list.pdf"));
//		Utils.copyFile(new File("F:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\hesh_retail_price_list_ver_"+version+".pdf"), new File("webapps\\en\\jsp\\hesh_retail_price_list.pdf"));
		Utils.copyFile(new File("E:\\HESH_SOFTWARE_FILES\\PRICE_LIST_PDF\\hesh_retail_price_list_ver_"+version+".pdf"), new File("webapps\\en\\jsp\\hesh_retail_price_list.pdf"));
		request.setAttribute("items", items);
		return;
	}

	private void addItemDiscountDtls(HttpServletRequest request) {
		String[] itemIds = request.getParameterValues("chk");
		ArrayList<ItemModel> items = new ArrayList<ItemModel>(0);
		String dis = "", fromdt ="", todt = "";
		ItemModel temp = null;
		for(int i=0; i<itemIds.length; i++){
			temp = new ItemModel();
			dis = request.getParameter("dis"+itemIds[i]);
			fromdt = Utils.convertToSQLDate(request.getParameter("txtFromDate"+itemIds[i]));
			todt = Utils.convertToSQLDate(request.getParameter("txtToDate"+itemIds[i]));
			temp.setItemId(Integer.parseInt(itemIds[i]));
			temp.setDiscount(dis);
			temp.setFromDate(fromdt);
			temp.setToDate(todt);
			items.add(temp);
		}
		MessageModel msg = getItemBroker().additemDiscount(items);
		request.setAttribute(MESSAGES, msg);
		getItemDiscountDtls(request);
		return;
	}

	private void getItemDiscountDtls(HttpServletRequest request) {
		ItemModel[] items = getItemBroker().getItemDiscountDtls();
		request.setAttribute(FORM_DATA, items);
		return;
	}

	private void updateItem(HttpServletRequest request) {
		ItemBroker broker = getItemBroker();
		ItemModel model = populateDataModel(request);
		MessageModel msgModel = broker.updateItem(model);
		request.setAttribute(MESSAGES, msgModel);
		model = broker.getItemDtls(model.getItemId());
		request.setAttribute(FORM_DATA, model);
		ItemCategoryModel[] items = broker.getItem();
		request.setAttribute(ITEMS, items);
		ItemGroupModel[] itemCats = getItemCategoryBroker().getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		return;
	}

	private void getItem(HttpServletRequest request) {
		ItemBroker broker = getItemBroker();
		ItemGroupModel model = broker.getItemDtls(Integer.parseInt(request.getParameter("sItem")));
		request.setAttribute(FORM_DATA, model);
		ItemCategoryModel[] items = broker.getItem();
		request.setAttribute(ITEMS, items);
		initialize(request);
		return;
	}

	private void initialize(HttpServletRequest request) {
		ItemCategoryModel[] itemCats = getItemCategoryBroker().getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		return;
	}

	private void initializeModify(HttpServletRequest request) {
		ItemCategoryModel[] itemCats = getItemCategoryBroker().getItemCategory();
		request.setAttribute(ITEM_CATEGORY, itemCats);
		ItemModel[] itemGrps = getItemBroker().getItem();
		request.setAttribute(ITEMS, itemGrps);
		return;
	}

	private void addItem(HttpServletRequest request,
			HttpServletResponse response) {
		ItemModel model = populateDataModel(request);
		MessageModel msgModel = getItemBroker().addItem(model);
		request.setAttribute(MESSAGES, msgModel);
		if(!msgModel.getMessages().get(0).getType().equals(SUCCESS))
			request.setAttribute(FORM_DATA, model);
		initialize(request);
		return;
	}

	private ItemModel populateDataModel(HttpServletRequest request) {
		ItemModel model = new ItemModel();
		if(request.getParameter("sItemCategory") != null && !request.getParameter("sItemCategory").trim().equals(""))
			model.setItemCatId(Integer.parseInt(request.getParameter("sItemCategory")));
		if(request.getParameter("sItem") != null && !request.getParameter("sItem").trim().equals(""))
			model.setItemId(Integer.parseInt(request.getParameter("sItem")));
		if(request.getParameter("txtItemName") != null && !request.getParameter("txtItemName").trim().equals(""))
			model.setItemName(request.getParameter("txtItemName"));
		if(request.getParameter("txtItemNumber") != null && !request.getParameter("txtItemNumber").trim().equals(""))
			model.setItemNumber(request.getParameter("txtItemNumber"));
		if(request.getParameter("txtItemDesc") != null && !request.getParameter("txtItemDesc").trim().equals(""))
			model.setItemLabel(request.getParameter("txtItemDesc"));
		if(request.getParameter("txtItemPrice") != null && !request.getParameter("txtItemPrice").trim().equals(""))
			model.setItemPrice(request.getParameter("txtItemPrice"));
		if(request.getParameter("txtDevTime") != null && !request.getParameter("txtDevTime").trim().equals(""))
			model.setDevTime(request.getParameter("txtDevTime"));
		if(request.getParameter("sTaxSlab") != null && !request.getParameter("sTaxSlab").trim().equals(""))
			model.setTax(request.getParameter("sTaxSlab"));
		if(request.getParameter("rPL") != null && request.getParameter("rPL").trim().equals("1"))
			model.setInPL();
		if(request.getParameter("rForSales") != null && request.getParameter("rForSales").trim().equals("1"))
			model.setInSales();
		return model;
	}

}
