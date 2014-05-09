package com.en.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.en.handler.AdminMasterHandler;
import com.en.handler.Basehandler;
import com.en.handler.InwardEntryHandler;
import com.en.handler.SyncDataHandler;
import com.en.util.Constant;

public class AdminServlet extends HttpServlet implements Constant {

	private static final long serialVersionUID = 1L;
	
	HashMap<String,Basehandler> pageMap = new HashMap<String,Basehandler>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		ApplicationProperties.getProperties(request);
		processPage(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processPage(request, response);
	}

	public void init() throws ServletException {
		super.init();
		initailizeMap();
	}

	private void initailizeMap() {
				
		pageMap.put(SyncDataHandler.getPageName(), new SyncDataHandler());
		
		pageMap.put(InwardEntryHandler.getPageName(), new InwardEntryHandler());
		
		pageMap.put(AdminMasterHandler.getPageName(), new AdminMasterHandler());
		
	}



	private void processPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String screenType = "";
		String strPage = request.getParameter(HANDLERS);//KakadaConstant.PURCHASE_HANDLER;
		request.setAttribute(HANDLERS, request.getParameter(HANDLERS));
		request.setAttribute(ACTIONS, request.getParameter(ACTIONS));
		Basehandler handler = (Basehandler) pageMap.get(strPage);
		if(handler == null)
			screenType = "/jsp/userAdminHome.jsp";
		else 
			screenType = "/jsp/"+handler.handlerRequest(request, response);
		dispatchRequest(request, response, screenType);
	}

	private void dispatchRequest(HttpServletRequest request,
			HttpServletResponse response, String screenType) {
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context
				.getRequestDispatcher(screenType);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
