package by.itacademy.matveenko.jd2.controller.impl;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.itacademy.matveenko.jd2.bean.News;
import by.itacademy.matveenko.jd2.controller.AttributsName;
import by.itacademy.matveenko.jd2.controller.Command;
import by.itacademy.matveenko.jd2.controller.JspPageName;
import by.itacademy.matveenko.jd2.controller.PageUrl;
import by.itacademy.matveenko.jd2.service.INewsService;
import by.itacademy.matveenko.jd2.service.ServiceException;
import by.itacademy.matveenko.jd2.service.ServiceProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class GoToNewsList implements Command {
	
	private final INewsService newsService = ServiceProvider.getInstance().getNewsService();
	private static final Logger log = LogManager.getRootLogger();
			
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession getSession = request.getSession(true);
		List<News> newsList;
		Integer pageNumber;
		Integer pageSize = 5;
		Integer countPage = 0;
		
		try {
			pageNumber = Integer.parseInt(request.getParameter("pageNo"));
		} catch (NumberFormatException e) {
			pageNumber = 1;
		}		
		try {
			newsList = newsService.newsList(pageNumber, pageSize);
			countPage = newsService.countPage(pageSize);
			request.setAttribute("countPage", countPage);
			request.setAttribute(AttributsName.NEWS, newsList);
			request.setAttribute(AttributsName.PRESENTATION, AttributsName.NEWS_LIST);
			getSession.setAttribute(AttributsName.PAGE_URL, PageUrl.NEWS_LIST_PAGE);
			request.getRequestDispatcher(JspPageName.BASELAYOUT_PAGE).forward(request, response);			
		} catch (ServiceException e) {
			log.error(e);
			response.sendRedirect(JspPageName.ERROR_PAGE);
		}		
	}
}