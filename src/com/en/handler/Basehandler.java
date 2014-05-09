package com.en.handler;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.broker.AccessPointBroker;
import com.en.broker.AdminBroker;
import com.en.broker.AdminMasterBroker;
import com.en.broker.ApprovalBroker;
import com.en.broker.AttendanceBroker;
import com.en.broker.BackupBroker;
import com.en.broker.BranchDayEntryBroker;
import com.en.broker.BranchDayEntryReportBroker;
import com.en.broker.BranchStockEntryBroker;
import com.en.broker.CollectionBroker;
import com.en.broker.CreditNoteBroker;
import com.en.broker.CreditNoteRptBroker;
import com.en.broker.CustomerBroker;
import com.en.broker.CustomerEmailRptBroker;
import com.en.broker.CustomerGroupBroker;
import com.en.broker.CustomerLedgerRptBroker;
import com.en.broker.DBConnection;
import com.en.broker.EnquiryBroker;
import com.en.broker.EnquiryRptBroker;
import com.en.broker.HomeSettingBroker;
import com.en.broker.InwardEntryBroker;
import com.en.broker.ItemBroker;
import com.en.broker.ItemCategoryBroker;
import com.en.broker.ItemGroupBroker;
import com.en.broker.LabourBillRptBroker;
import com.en.broker.LabourInvoiceBroker;
import com.en.broker.LedgerAdjustmentBroker;
import com.en.broker.LoginBroker;
import com.en.broker.MessengerBroker;
import com.en.broker.OfferBroker;
import com.en.broker.OrderBroker;
import com.en.broker.OrderReportBroker;
import com.en.broker.PromotionalMailBroker;
import com.en.broker.PurchaseBroker;
import com.en.broker.PurchaseReportBroker;
import com.en.broker.PurchaseReturnBroker;
import com.en.broker.PurchaseReturnRptBroker;
import com.en.broker.QuotationBroker;
import com.en.broker.SalesBroker;
import com.en.broker.SalesReportBroker;
import com.en.broker.SalesTaxReportBroker;
import com.en.broker.SalesmanDailyBroker;
import com.en.broker.SalesmanEnquiryBroker;
import com.en.broker.StockRegisterBroker;
import com.en.broker.SyncDataBroker;
import com.en.broker.TaxBroker;
import com.en.broker.TransferBroker;
import com.en.broker.TransferReportBroker;
import com.en.broker.TransferRequestBroker;
import com.en.broker.UserAccessBroker;
import com.en.broker.UserBroker;
import com.en.util.ActionType;

public abstract class Basehandler implements ActionType {

	abstract public String handlerRequest(HttpServletRequest request,
			HttpServletResponse response);

	protected LoginBroker getLoginBroker() {
		LoginBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new LoginBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected ItemGroupBroker getItemGroupBroker() {
		ItemGroupBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new ItemGroupBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected ItemCategoryBroker getItemCategoryBroker() {
		ItemCategoryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new ItemCategoryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected ItemBroker getItemBroker() {
		ItemBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new ItemBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CustomerGroupBroker getCustomerGroupBroker() {
		CustomerGroupBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CustomerGroupBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CustomerBroker getCustomerBroker() {
		CustomerBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CustomerBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected AccessPointBroker getAccessPointBroker() {
		AccessPointBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new AccessPointBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected UserBroker getUserBroker() {
		UserBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new UserBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected UserAccessBroker getUserAccessBroker() {
		UserAccessBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new UserAccessBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected OfferBroker getOfferBroker() {
		OfferBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new OfferBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected HomeSettingBroker getHomeSettingBroker() {
		HomeSettingBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new HomeSettingBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected EnquiryBroker getEnquiryBroker() {
		EnquiryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new EnquiryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected SalesmanEnquiryBroker getSalesmanEnquiryBroker() {
		SalesmanEnquiryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new SalesmanEnquiryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected SalesmanDailyBroker getSalesmanDailyBroker() {
		SalesmanDailyBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new SalesmanDailyBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected EnquiryRptBroker getEnquiryRptBroker() {
		EnquiryRptBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new EnquiryRptBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected TaxBroker getTaxBroker() {
		TaxBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new TaxBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected SalesBroker getSalesBroker() {
		SalesBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new SalesBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected TransferBroker getTransferBroker() {
		TransferBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new TransferBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected BranchDayEntryBroker getBranchDayEntryBroker() {
		BranchDayEntryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new BranchDayEntryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected BackupBroker getBackupBroker() {
		BackupBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new BackupBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected PurchaseBroker getPurchaseBroker() {
		PurchaseBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new PurchaseBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CollectionBroker getCollectionBroker() {
		CollectionBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CollectionBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected OrderReportBroker getOrderReportBroker() {
		OrderReportBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new OrderReportBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected SalesReportBroker getSalesReportBroker() {
		SalesReportBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new SalesReportBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected PurchaseReportBroker getPurchaseReportBroker() {
		PurchaseReportBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new PurchaseReportBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected TransferReportBroker getTransferReportBroker() {
		TransferReportBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new TransferReportBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected AdminBroker getAdminBroker() {
		AdminBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new AdminBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected QuotationBroker getQuotationBroker() {
		QuotationBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new QuotationBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected BranchDayEntryReportBroker getBranchDayEntryReportBroker() {
		BranchDayEntryReportBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new BranchDayEntryReportBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected SyncDataBroker getSyncDataBroker() {
		SyncDataBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new SyncDataBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected InwardEntryBroker getInwardEntryBroker() {
		InwardEntryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new InwardEntryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected AdminMasterBroker getAdminMasterBroker() {
		AdminMasterBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new AdminMasterBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CustomerLedgerRptBroker getCustomerLedgerRptBroker() {
		CustomerLedgerRptBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CustomerLedgerRptBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected OrderBroker getOrderBroker() {
		OrderBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new OrderBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected PromotionalMailBroker getPromotionalMailBroker() {
		PromotionalMailBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new PromotionalMailBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected ApprovalBroker getApprovalBroker() {
		ApprovalBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new ApprovalBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected LabourInvoiceBroker getLabourInvoiceBroker() {
		LabourInvoiceBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new LabourInvoiceBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected LabourBillRptBroker getLabourBillRptBroker() {
		LabourBillRptBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new LabourBillRptBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CreditNoteBroker getCreditNoteBroker() {
		CreditNoteBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CreditNoteBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CreditNoteRptBroker getCreditNoteRptBroker() {
		CreditNoteRptBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CreditNoteRptBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected PurchaseReturnBroker getPurchaseReturnBroker() {
		PurchaseReturnBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new PurchaseReturnBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected PurchaseReturnRptBroker getPurchaseReturnRptBroker() {
		PurchaseReturnRptBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new PurchaseReturnRptBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected LedgerAdjustmentBroker getLedgerAdjustmentBroker() {
		LedgerAdjustmentBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new LedgerAdjustmentBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected CustomerEmailRptBroker getCustomerEmailRptBroker() {
		CustomerEmailRptBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new CustomerEmailRptBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected TransferRequestBroker getTransferRequestBroker() {
		TransferRequestBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new TransferRequestBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected StockRegisterBroker getStockRegisterBroker() {
		StockRegisterBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new StockRegisterBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected SalesTaxReportBroker getSalesTaxReportBroker() {
		SalesTaxReportBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new SalesTaxReportBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected BranchStockEntryBroker getBranchStockEntryBroker() {
		BranchStockEntryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new BranchStockEntryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected MessengerBroker getMessengerBroker() {
		MessengerBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new MessengerBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}

	protected AttendanceBroker getAttendanceBroker() {
		AttendanceBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new AttendanceBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broker;
	}
}
