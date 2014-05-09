package com.en.util;



public class MenuUtil implements Constant{

//	public static void setMenu(HttpServletRequest request){
//		ArrayList<Menu> menuLst = new ArrayList<Menu>(0);
//		MenuItem item = null;
//		
//		//----- Sales menu starts------------------------------------------------------------------
//		Menu sales = new Menu("Sales",new ArrayList<Object>(0),"sales");
//		
//		// sales bill
//		item = new MenuItem("Sales Bill",SALES_HANDLER,"","salesBill");
//		sales.getMenuList().add(item);
//
//		// reprint / cancel bill
//		item = new MenuItem("Reprint / Cancel Bill",SALES_HANDLER,INIT_CNCL_SALES,"cancelBill");
//		sales.getMenuList().add(item);
//		
//		menuLst.add(sales);
//		//----- Sales menu ends--------------------------------------------------------------------
//		
//		//----- Returns menu starts----------------------------------------------------------------
//		Menu returns = new Menu("Returns",new ArrayList<Object>(0),"returns");
//		
//		// customer returns
//		item = new MenuItem("Customer Return",RETURNS_HANDLER,"","custReturn");
//		returns.getMenuList().add(item);
//
//		// reprint / cancel returns
//		item = new MenuItem("Reprint / Cancel Returns",RETURNS_HANDLER,INIT_CNCL_RETN,"cancelReturn");
//		returns.getMenuList().add(item);
//		
//		menuLst.add(returns);
//		//----- Returns menu ends------------------------------------------------------------------
//		
//		//----- Company menu starts-------------------------------------------------------------------
//		Menu company = new Menu("Company Setting",new ArrayList<Object>(0),"compMenu");
//
//		// Modify/Delete Item
//		item = new MenuItem("Modify Company Details",COMPANY_HANDLER,"","mdComp");
//		company.getMenuList().add(item);	
//		
//		menuLst.add(company);
//		//----- Item menu ends---------------------------------------------------------------------
//		
//		//----- Item menu starts-------------------------------------------------------------------
//		Menu itemMenu = new Menu("Item Setting",new ArrayList<Object>(0),"itemMenu");
//
//		// Add New Item 
//		item = new MenuItem("Add New Item",ITEM_HANDLER,"","newItem");
//		itemMenu.getMenuList().add(item);	
//		
//		// Modify/Delete Item
//		item = new MenuItem("Modify/Delete Item",ITEM_HANDLER,INIT_MD_ITEM_DTLS,"mdItem");
//		itemMenu.getMenuList().add(item);	
//		
//		menuLst.add(itemMenu);
//		//----- Item menu ends---------------------------------------------------------------------
//		
//		//----- Salesman menu starts---------------------------------------------------------------
//		Menu salesman = new Menu("Salesman",new ArrayList<Object>(0),"salesman");
//		
//		// sales bill
//		item = new MenuItem("Add Salesman",SALESMAN_HANDLER,"","addsalesman");
//		salesman.getMenuList().add(item);
//
//		// cancel bill
//		item = new MenuItem("Modify/Delete Salesman",SALESMAN_HANDLER,MD_SALESMAN,"mdsalesman");
//		salesman.getMenuList().add(item);
//		
//		menuLst.add(salesman);
//		//----- Salesman menu ends-----------------------------------------------------------------
//				
//		request.setAttribute(DISP_MENU, menuLst);
//	}
//	
//	public static void setAdminMenu(HttpServletRequest request){
//		ArrayList<Menu> menuLst = new ArrayList<Menu>(0);
//		MenuItem item = null;
//		
//		//----- Modify Bill Details menu starts ---------------------------------------------------
//		
//		Menu sales = new Menu("Settings", new ArrayList<Object>(0),"sales");
//		
//		item = new MenuItem("Add Purchase",PURCHASE_HANDLER,"","purchase");
//		sales.getMenuList().add(item);
//		
//		item = new MenuItem("Delete Bills",BILL_HANDLER,"","delBill");
//		sales.getMenuList().add(item);
//		
//		item = new MenuItem("Delete Hotel Bills",H_BILL_HANDLER,"","delHoteBill");
//		sales.getMenuList().add(item);
//		
//		menuLst.add(sales);
//		
//		//----- Modify Bill Details menu ends -----------------------------------------------------
//		
//		//----- Modify Bill Details menu starts ---------------------------------------------------
//		
//		Menu crop = new Menu("Corporate Bill Settings", new ArrayList<Object>(0),"sales");
//		
//		item = new MenuItem("Add New Bill",CORP_SALES_HANDLER,"","crop1");
//		crop.getMenuList().add(item);
//		
//		item = new MenuItem("Reprint/Cancel Bills",CORP_SALES_HANDLER,INIT_CNCL_SALES,"delBill1");
//		crop.getMenuList().add(item);
//		
//		menuLst.add(crop);
//		
//		//----- Modify Bill Details menu ends -----------------------------------------------------
//		
//		//----- Item menu starts-------------------------------------------------------------------
//		Menu itemMenu = new Menu("Item Setting",new ArrayList<Object>(0),"itemMenu");
//
//		// Add New Item 
//		item = new MenuItem("Add New Item",ITEM_HANDLER,"","newItem");
//		itemMenu.getMenuList().add(item);	
//		
//		// Modify/Delete Item
//		item = new MenuItem("Modify/Delete Item",ITEM_HANDLER,INIT_MD_ITEM_DTLS,"mdItem");
//		itemMenu.getMenuList().add(item);	
//		
//		menuLst.add(itemMenu);
//		//----- Item menu ends---------------------------------------------------------------------
//		
//		//----- Returns menu starts----------------------------------------------------------------
//		Menu reports = new Menu("Reports",new ArrayList<Object>(0),"reports");
//		
//		// sales menu
//		Menu salesRpt = new Menu("Sales Reports",new ArrayList<Object>(0),"salesRpt");
//		
//		item = new MenuItem("Item Wise Sales Report",SALES_RPT_HANDLER,"","itemSalesRpt");
//		salesRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Company Wise Sales Report",SALES_RPT_HANDLER,INIT_CMPY_SAL_RPT,"cmpySalesRpt");
//		salesRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Bill Wise Sales Report",SALES_RPT_HANDLER,INIT_BILL_SAL_RPT,"billSalesRpt");
//		salesRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Date Wise Sales Report",SALES_RPT_HANDLER,INIT_DATE_SAL_RPT,"dateSalesRpt");
//		salesRpt.getMenuList().add(item);
//
//		reports.getMenuList().add(salesRpt);
//		// returns menu
//		Menu returnsRpt = new Menu("Returns Reports",new ArrayList<Object>(0),"returnsRpt");
//		
//		item = new MenuItem("Item Wise Returns Report",RETURNS_RPT_HANDLER,"","itemReturnsRpt");
//		returnsRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Company Wise Returns Report",RETURNS_RPT_HANDLER,INIT_CMPY_RETN_RPT,"cmpyReturnsRpt");
//		returnsRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Bill Wise Returns Report",RETURNS_RPT_HANDLER,INIT_BILL_RETN_RPT,"billReturnsRpt");
//		returnsRpt.getMenuList().add(item);
//
//		reports.getMenuList().add(returnsRpt);
//
//		// returns menu
//		Menu hotelRpt = new Menu("Hotel Reports",new ArrayList<Object>(0),"hotelRpt");
//		
//		item = new MenuItem("Item Wise Sales Report",H_SALES_RPT_HANDLER,"","itemhotelRpt");
//		hotelRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Bill Wise Sales Report",H_SALES_RPT_HANDLER,INIT_BILL_SAL_RPT,"billhotelRpt");
//		hotelRpt.getMenuList().add(item);
//		
//		item = new MenuItem("Date Wise Sales Report",H_SALES_RPT_HANDLER,INIT_DATE_SAL_RPT,"datehotelRpt");
//		hotelRpt.getMenuList().add(item);
//
//		reports.getMenuList().add(hotelRpt);
//
//		item = new MenuItem("Purchase Report",PURCHASE_RPT_HANDLER,"","purchaseRpt");
//		reports.getMenuList().add(item);		
//
//		menuLst.add(reports);
//		//----- Returns menu ends------------------------------------------------------------------
//		
//		request.setAttribute(DISP_MENU, menuLst);
//	}
//	
//	public static void setOfficeMenu(HttpServletRequest request){
//		ArrayList<Menu> menuLst = new ArrayList<Menu>(0);
//		MenuItem item = null;
//
//		//----- Item menu starts-------------------------------------------------------------------
//		Menu itemMenu = new Menu("Item Setting",new ArrayList<Object>(0),"itemMenu");
//
//		// Add New Item 
//		item = new MenuItem("Add New Item",ITEM_HANDLER,"","newItem");
//		itemMenu.getMenuList().add(item);	
//		
//		// Modify/Delete Item
//		item = new MenuItem("Modify/Delete Item",ITEM_HANDLER,INIT_MD_ITEM_DTLS,"mdItem");
//		itemMenu.getMenuList().add(item);	
//		
//		menuLst.add(itemMenu);
//		//----- Item menu ends---------------------------------------------------------------------
//		
//		//----- Modify Bill Details menu starts ---------------------------------------------------
//		
//		Menu office = new Menu("Office", new ArrayList<Object>(0),"office");
//		
//		item = new MenuItem("Add Incoming Finish Good Entry",TRANSFER_HANDLER,INIT_OFFICE_ENTRY,"purchase");
//		office.getMenuList().add(item);
//		
//		item = new MenuItem("Modify/Delete Incoming Finish Good Entry","","","delBill");
//		office.getMenuList().add(item);
//		
//		item = new MenuItem("Stickers for Packing Department","","","sticker");
//		office.getMenuList().add(item);
//		
//		menuLst.add(office);
//		
//		//----- Modify Bill Details menu ends -----------------------------------------------------
//		
//		//----- Modify Bill Details menu starts ---------------------------------------------------
//		
//		Menu transfer = new Menu("Transfers", new ArrayList<Object>(0),"transfer");
//		
//		Menu sttopdtnfr = new Menu("Store to Packing Department", new ArrayList<Object>(0),"sttopdtnfr");
//		
//		item = new MenuItem("Add New Transfer",TRANSFER_HANDLER,INIT_STORE_PD_ENTRY,"trans1");
//		sttopdtnfr.getMenuList().add(item);
//		
//		item = new MenuItem("Modify/Delete Transfer","","","trans2");
//		sttopdtnfr.getMenuList().add(item);
//		
//		transfer.getMenuList().add(sttopdtnfr);
//		
//		Menu sttoshoptnfr = new Menu("Store to Shop", new ArrayList<Object>(0),"sttoshoptnfr");
//		
//		item = new MenuItem("Add New Transfer",TRANSFER_HANDLER,INIT_STORE_SHOP_ENTRY,"trans3");
//		sttoshoptnfr.getMenuList().add(item);
//		
//		item = new MenuItem("Modify/Delete Transfer","","","trans4");
//		sttoshoptnfr.getMenuList().add(item);
//		
//		transfer.getMenuList().add(sttoshoptnfr);
//		
//		Menu pdtoshoptnfr = new Menu("Packing Department to Shop", new ArrayList<Object>(0),"pdtoshoptnfr");
//		
//		item = new MenuItem("Add New Transfer",TRANSFER_HANDLER,INIT_PD_SHOP_ENTRY,"trans5");
//		pdtoshoptnfr.getMenuList().add(item);
//		
//		item = new MenuItem("Modify/Delete Transfer","","","trans6");
//		pdtoshoptnfr.getMenuList().add(item);
//		
//		transfer.getMenuList().add(pdtoshoptnfr);
//		
//		menuLst.add(transfer);
//		
//		//----- Modify Bill Details menu ends -----------------------------------------------------
//		
//		request.setAttribute(DISP_MENU, menuLst);
//	}	
//	
//	public static void setHotelMenu(HttpServletRequest request){
//		ArrayList<Menu> menuLst = new ArrayList<Menu>(0);
//		MenuItem item = null;
//		
//		//----- Sales menu starts------------------------------------------------------------------
//		Menu sales = new Menu("Sales",new ArrayList<Object>(0),"sales");
//		
//		// sales bill
//		item = new MenuItem("Sales Bill",SALES_HANDLER_H,"","salesBill");
//		sales.getMenuList().add(item);
//
//		// reprint / cancel bill
//		item = new MenuItem("Reprint / Cancel Bill",SALES_HANDLER_H,INIT_CNCL_SALES,"cancelBill");
//		sales.getMenuList().add(item);
//		
//		menuLst.add(sales);
//		//----- Sales menu ends--------------------------------------------------------------------
//		
//		//----- Item menu starts-------------------------------------------------------------------
//		Menu itemMenu = new Menu("Item Setting",new ArrayList<Object>(0),"itemMenu");
//
//		// Add New Item 
//		item = new MenuItem("Add New Item",ITEM_HANDLER_H,"","newItem");
//		itemMenu.getMenuList().add(item);	
//		
//		// Modify/Delete Item
//		item = new MenuItem("Modify/Delete Item",ITEM_HANDLER_H,INIT_MD_ITEM_DTLS,"mdItem");
//		itemMenu.getMenuList().add(item);	
//		
//		menuLst.add(itemMenu);
//		//----- Item menu ends---------------------------------------------------------------------
//				
//		request.setAttribute(DISP_MENU, menuLst);
//	}
}
