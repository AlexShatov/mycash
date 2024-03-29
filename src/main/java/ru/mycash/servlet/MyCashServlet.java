package ru.mycash.servlet;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import ru.mycash.dao.*;
import ru.mycash.domain.*;
import ru.mycash.service.MyCashMainService;
import ru.mycash.util.InputMatcher;

public class MyCashServlet extends HttpServlet{
	
	private static InputMatcher matcher;
	
	@Autowired
	private MyCashMainService service;
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
		matcher = new InputMatcher();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		doGetOrPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		doGetOrPost(req, resp);
	}
	
	private void doGetOrPost (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		String action = req.getParameter("action");
		try {
			if("logout".equals(action)) {
				logout(req, resp);
			}else if ("authorize".equals(action)) {
				authorize(req, resp);
			}else if ("get_auth".equals(action)){
				getAuth(req, resp);
			} else if ("register".equals(action)) {
				register(req, resp);
			}else if("get_reg".equals(action)){
				getReg(req,resp);
			}else if ("get_stats".equals(action)) {
				getStats(req,resp);
			}else if ("get_cabinet".equals(action)) {
				getCabinet(req, resp);
			}else if("change_pass".equals(action)) {
				changePass(req, resp);
			}else if("get_counts".equals(action)){
				getCounts(req,resp);
			}else if("delete_count".equals(action)) {
				deleteCount(req,resp);
			}else if("add_count".equals(action)) {
				addCount(req, resp);
			}else if("get_error_page".equals(action)) {
				getErrorPage(req, resp);
			}else if("get_incomes".equals(action)){
				getIncomes(req,resp);
			}else if("delete_income".equals(action)){
				deleteIncome(req, resp);
			}else if("add_income".equals(action)){
				addIncome(req, resp);
			}else if("get_expenses".equals(action)){
				getExpenses(req, resp);
			}else if("add_expense".equals(action)){
				addExpense(req,resp);
			}else if("delete_expense".equals(action)){
				deleteExpense(req, resp);
			}else{
				resp.sendRedirect(req.getContextPath() + "/?action=get_stats");
			}
		} catch (DaoException e) {
			throw new ServletException("Operation with user data failed", e);
		} catch(ParseException e) {
			throw new ServletException("Exception occured while parsing Date", e);
		}
	}
	
	private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		req.getSession().invalidate();
		resp.sendRedirect(req.getContextPath() + "/");
	}
	
	private void authorize(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		String login = req.getParameter("login");
		String pass = req.getParameter("pass");	
		User user = new User();
		if(matcher.matchLogin(login)) {
			user.setLogin(login);
		}else {
			req.setAttribute("login_placeholder", "incorrect login");
			getServletContext().getRequestDispatcher("/authorization.jsp").forward(req,resp);
		}
		if(matcher.matchPassword(pass)) {
			user.setPassword(pass);
		}else {
			req.setAttribute("pass_placeholder", "incorrect password");
			getServletContext().getRequestDispatcher("/authorization.jsp").forward(req,resp);
		}
		User existingUser = service.getUserByLogin(login);
		if (existingUser != null) {
			if(existingUser.getPassword().equals(user.getPassword())) {
				req.getSession().setAttribute("loginedUser", existingUser);
				resp.sendRedirect(req.getContextPath() + "/?action=get_stats");
			}else {
				req.setAttribute("pass_placeholder", "incorrect password");
				getServletContext().getRequestDispatcher("/authorization.jsp").forward(req,resp); 
			}
		}else {
			req.setAttribute("login_placeholder", "incorrect login");
			getServletContext().getRequestDispatcher("/authorization.jsp").forward(req,resp); 
		}	
	}
	
	private void getAuth(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		getServletContext().getRequestDispatcher("/authorization.jsp").forward(req,resp);
	}
	
	private void register(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		String login = req.getParameter("login");
		String pass = req.getParameter("pass");
		String email = req.getParameter("email");
		User user = new User();
		if(matcher.matchLogin(login)) {
			user.setLogin(login);
		}else {
			req.setAttribute("login_placeholder", "incorrect login");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req,resp);
		}
		if(matcher.matchPassword(pass)) {
			user.setPassword(pass);
		}else {
			req.setAttribute("pass_placeholder", "incorrect password");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req,resp);
		}
		if(matcher.matchMail(email)) {
			user.setMail(email);
		}else {
			req.setAttribute("mail_placeholder", "incorrect email");
			getServletContext().getRequestDispatcher("/registration.jsp").forward(req,resp);
		}
		if(service.getUserByLogin(login) != null) {
			req.setAttribute("login_placeholder", "user already exists");
			getServletContext().getRequestDispatcher("/?action=get_reg").forward(req,resp);
		}else {
			service.insertUser(user);
			resp.sendRedirect(req.getContextPath()  + "/?action=get_auth");
		}
	}
	
	private void getReg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		getServletContext().getRequestDispatcher("/registration.jsp").forward(req,resp);
	}
	
	private void getStats(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		req.setAttribute("login", loginedUser.getLogin());
		getServletContext().getRequestDispatcher("/stats.jsp").forward(req,resp);
	}
	
	private void getCabinet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		req.setAttribute("login", loginedUser.getLogin());
		getServletContext().getRequestDispatcher("/cabinet.jsp").forward(req,resp);
	}
	
	private void changePass(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		String oldPass = req.getParameter("old_pass");
		String newPass = req.getParameter("new_pass");
		String repeatPass = req.getParameter("repeat_new_pass");
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		String passFromDb = service.getUserByLogin(loginedUser.getLogin()).getPassword();
		if(matcher.matchPassword(newPass)) {
			if(matcher.matchPassword(repeatPass)) {
				if(newPass.equals(repeatPass)) {
					if(oldPass != null && oldPass.equals(passFromDb) & matcher.matchPassword(oldPass)) {
						loginedUser.setPassword(newPass);
						service.updateUser(loginedUser);
						req.setAttribute("old_pass_placeholder", "successfully changed");
						getServletContext().getRequestDispatcher("/cabinet.jsp").forward(req,resp);
					}else {
						req.setAttribute("old_pass_placeholder", "incorrect password");
						getServletContext().getRequestDispatcher("/cabinet.jsp").forward(req,resp);
					}
				}else {
					req.setAttribute("new_pass_placeholder", "passwords mismatch");
					req.setAttribute("rep_pass_placeholder", "passwords mismatch");
					getServletContext().getRequestDispatcher("/cabinet.jsp").forward(req,resp);
				}
			}else {
				req.setAttribute("rep_pass_placeholder", "incorrect password");
				getServletContext().getRequestDispatcher("/cabinet.jsp").forward(req,resp);
			}
		}else {
			req.setAttribute("new_pass_placeholder", "incorrect password");
			getServletContext().getRequestDispatcher("/cabinet.jsp").forward(req,resp);
		}	
	}
	
	private void getIncomes (HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		int userId = loginedUser.getId();
		String startDate = req.getParameter("start_date");
		String endDate = req.getParameter("end_date");
		if(startDate == null || endDate == null) {
			startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			endDate = startDate;
		}
		if(matcher.matchDate(startDate) && matcher.matchDate(endDate)) {
			HashMap<String, ArrayList<?>> pageData = service.getDataForIncomesPage(userId, startDate, endDate);
			req.setAttribute("login", loginedUser.getLogin());
			req.setAttribute("incomes", pageData.get("incomes"));
			req.setAttribute("categories", pageData.get("categories"));
			req.setAttribute("counts", pageData.get("counts"));
			getServletContext().getRequestDispatcher("/incomes.jsp").forward(req,resp);
		}else {
			resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
		}
	}
	
	
	private void addIncome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, 
			DaoException, ParseException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		int userId = loginedUser.getId();
		String annotation = req.getParameter("annotation");
		String categoryName = req.getParameter("category");
		String countName = req.getParameter("count");
		String amountStr = req.getParameter("amount");
		String dateStr = req.getParameter("date");
		if(matcher.matchName(annotation)) {
			if(matcher.matchName(categoryName)) {
				if(matcher.matchName(countName)) {
					if(matcher.matchAmount(amountStr)) {
						if(matcher.matchDate(dateStr)) {
							service.addIncome(userId, dateStr, categoryName, annotation, amountStr, countName, loginedUser);
							resp.sendRedirect(req.getContextPath() + "/?action=get_incomes");
						}else {
							resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
						}
					}else {
						req.setAttribute("amount_holder", "incorrect amount");
						getIncomes(req,resp);
					}
				}else {
					resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
				}
			}else {
				resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
			}
		}else {
			req.setAttribute("annotation_holder", "unacceptable symbols");
		}
	}
	
	private void deleteIncome (HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		int incomeId = Integer.parseInt(req.getParameter("income_id"));
		service.deleteIncome(incomeId);
		resp.sendRedirect(req.getContextPath()  + "/?action=get_incomes");
	}	
	
	private void addExpense(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, 
			DaoException, ParseException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		int userId = loginedUser.getId();
		String annotation = req.getParameter("annotation");
		String category = req.getParameter("category");
		String countName = req.getParameter("count");
		String amountStr = req.getParameter("amount");
		String dateStr = req.getParameter("date");
		if(matcher.matchName(annotation)) {
			if(matcher.matchName(category)) {
				if(matcher.matchName(countName)) {
					if(matcher.matchAmount(amountStr)) {
						if(matcher.matchDate(dateStr)) {
							service.addExpense(userId, dateStr, category, annotation, amountStr, 
									countName, loginedUser);
							resp.sendRedirect(req.getContextPath() + "/?action=get_expenses");
						}else {
							resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
						}
					}else {
						req.setAttribute("amount_holder", "incorrect amount");
						getIncomes(req,resp);
					}
				}else {
					resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
				}
			}else {
				resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
			}
		}else {
			req.setAttribute("annotation_holder", "unacceptable symbols");
		}
	}
	
	private void deleteExpense (HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		int expenseId = Integer.parseInt(req.getParameter("expense_id"));
		service.deleteExpense(expenseId);
		resp.sendRedirect(req.getContextPath()  + "/?action=get_expenses");
	}
	
	private void getExpenses (HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		int userId = loginedUser.getId();
		String startDate = req.getParameter("start_date");
		String endDate = req.getParameter("end_date");
		if(startDate == null || endDate == null) {
			startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			endDate = startDate;
		}
		HashMap<String, ArrayList<?>> pageData = service.getDataForExpensePage(userId, startDate, endDate);
		if(matcher.matchDate(startDate) && matcher.matchDate(endDate)) {
			req.setAttribute("login", loginedUser.getLogin());
			req.setAttribute("expenses", pageData.get("expenses"));
			req.setAttribute("categories", pageData.get("categories"));
			req.setAttribute("counts", pageData.get("counts"));
			getServletContext().getRequestDispatcher("/expenses.jsp").forward(req,resp);
		}else {
			resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
		}
	}
	
	private void getCounts(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		int userId = loginedUser.getId(); 
		ArrayList<Count> counts = service.getDataForCountPage(userId);
		req.setAttribute("counts", counts);
		req.setAttribute("login", loginedUser.getLogin());
		getServletContext().getRequestDispatcher("/counts.jsp").forward(req,resp);
	}
	
	private void deleteCount(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		int countId = Integer.parseInt(req.getParameter("count_id"));
		service.deleteCount(countId);
		resp.sendRedirect(req.getContextPath()  + "/?action=get_counts");
	}
	
	private void addCount(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException, DaoException{
		User loginedUser = (User)req.getSession().getAttribute("loginedUser");
		String countName = req.getParameter("count_name");
		String balance = req.getParameter("balance");
		String currency = req.getParameter("currency");
		if(matcher.matchName(countName)) {
			if(matcher.matchAmount(balance)) {
				if(matcher.matchCurrency(currency)) {
					service.insertCount(currency, balance, countName, loginedUser);
					resp.sendRedirect(req.getContextPath()  + "/?action=get_counts");
				}else {
					req.setAttribute("err_message", "Invalid currency input while adding count");
					resp.sendRedirect(req.getContextPath() + "/?action=get_error_page");
				}
			} else {
				req.setAttribute("balance_holder", "incorrect amount");
				getCounts(req,resp);
			}
		}else {
			req.setAttribute("count_name_holder", "incorrect name");
			getCounts(req,resp);
		}
	}
	
	private void getErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		getServletContext().getRequestDispatcher("/500.jsp").forward(req,resp);
	}
}
