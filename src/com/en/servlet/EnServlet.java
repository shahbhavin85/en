package com.en.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.en.handler.AccessPointHandler;
import com.en.handler.ActionHandler;
import com.en.handler.Basehandler;
import com.en.handler.LoginHandler;
import com.en.util.Constant;

public class EnServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processPage(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processPage(request, response);
	}

	public void init() throws ServletException {
		super.init();
	}

	private void processPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String screenType = "";
			String strPage = request.getParameter(HANDLERS);//KakadaConstant.PURCHASE_HANDLER;
			request.setAttribute(HANDLERS, request.getParameter(HANDLERS));
			request.setAttribute(ACTIONS, request.getParameter(ACTIONS));
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			Basehandler handler = (Basehandler) pageMap.get(strPage);
	        if(isMultipart && request.getSession().getAttribute(Constant.USER) != null){
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;
				items = upload.parseRequest(request);
				Iterator<FileItem> itr = items.iterator();
				String handlers = "";
				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						if(name.equals(HANDLERS)){
							handlers = value;
							break;
						}
					} else {
						continue;
					}
				}
				request.setAttribute(MUTLIPART_DATA, items);
	        	handler = (Basehandler) pageMap.get(handlers);
	        }
			if(handler == null || request.getSession().getAttribute(Constant.USER) == null){
				handler = new LoginHandler();
			}
			screenType = "/jsp/"+handler.handlerRequest(request, response);
			if(request.getSession().getAttribute(Constant.ACCESS_POINT) != null && request.getSession().getAttribute(Constant.ACCESS_POINT).equals("2")){
				double balance = AccessPointHandler.getTotalCashOnHand(request);
				request.setAttribute(BALANCE, balance);
			}
			if(handler != null || request.getSession().getAttribute(Constant.USER) != null){
				ActionHandler.getPendingRequestnDemand(request);
			}
			request.setAttribute(MUTLIPART_DATA, null);
			dispatchRequest(request, response, screenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
