package com.en.handler;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import com.en.broker.BranchDayEntryBroker;
import com.en.broker.DBConnection;
import com.en.broker.MessengerBroker;
import com.en.broker.UserBroker;
import com.en.model.SalaryModel;
import com.en.model.UserModel;
import com.en.util.Constant;
import com.en.util.DateUtil;
import com.en.util.Utils;

public class ActionHandler implements Constant{

	public static void getPendingRequestnDemand(HttpServletRequest request) {
		MessengerBroker broker = null;
		try {
			if((UserModel) request.getSession().getAttribute(USER) != null){
				Connection conn = (new DBConnection()).getConnection();
				broker = new MessengerBroker(conn);
				int inboxCount = broker.getInboxCount((UserModel) request.getSession().getAttribute(USER));
				request.setAttribute(NEW_INBOX_COUNT, inboxCount);
//				Date dt = (new SimpleDateFormat("dd-MM-yyyy")).parse(DateUtil.getCurrDt());
				Calendar c = new GregorianCalendar();
//				c.setTime(dt);
//				System.out.println(c.get(Calendar.MONTH));
				int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
				SalaryModel details = (new UserBroker(conn)).getSalaryDetails(((UserModel) request.getSession().getAttribute(USER)),c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR));
				request.setAttribute(CURRENT_SALARY, Utils.get2Decimal(Utils.get2DecimalDouble((details.getPresentDays()+details.getHoliDays())*Double.parseDouble(details.getUser().getSalary())/monthMaxDays) - details.getLatePenalty() - details.getPenalty() + (details.getOt()*Double.parseDouble(details.getUser().getSalary())/(monthMaxDays*10))));
				String[] dates = DateUtil.getCurrentQuarterDate(new GregorianCalendar());
				double pendingTarget = (new UserBroker(conn)).getPendingTarget((UserModel) request.getSession().getAttribute(USER), dates[0], dates[1]);
				request.setAttribute(PENDING_TARGET, pendingTarget);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute(PENDING_APP_REQUEST, 5);
		request.setAttribute(PENDING_APP_DEMAND, 2);
		return;
	}

	public static void getOpeninfBalance(int accessId,
			HttpServletRequest request) {
		BranchDayEntryBroker broker = null;
		try {
			Connection conn = (new DBConnection()).getConnection();
			broker = new BranchDayEntryBroker(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double openingBal = broker.getBranchCashBalance(accessId);	
		request.setAttribute(OPEN_BALANCE, Utils.get2Decimal(openingBal));
		return;
	}

}
