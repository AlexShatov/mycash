package ru.mycash.listener;

import javax.servlet.http.*;

import ru.mycash.MySqlDaoFactory;
import ru.mycash.dao.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletException;

@WebListener

public class MyCashHttpSessionListener implements HttpSessionListener{
	
	 MySqlDaoFactory factory = null;
	
	public MyCashHttpSessionListener() throws DaoException{
		factory = new MySqlDaoFactory();
	}

	public void sessionCreated(HttpSessionEvent se) {
		try {
			MySqlUserDao userDao = factory.getMySqlUserDao();
			MySqlCountDao countDao = factory.getMySqlCountDao();
			MySqlIncomeCategoryDao incCategoryDao = factory.getMySqlIncomeCategoryDao();
			MySqlIncomeDao incomeDao = factory.getMySqlIncomeDao();
			MySqlExpenseCategoryDao expCategoryDao = factory.getMySqlExpenseCategoryDao();
			MySqlExpenseDao expenseDao = factory.getMySqlExpenseDao();
			se.getSession().setAttribute("user_dao", userDao);
			se.getSession().setAttribute("count_dao", countDao);
			se.getSession().setAttribute("income_cat_dao", incCategoryDao);
			se.getSession().setAttribute("income_dao", incomeDao);
			se.getSession().setAttribute("expense_dao", expenseDao);
			se.getSession().setAttribute("expense_cat_dao", expCategoryDao);
		}
		catch(DaoException e){
			se.getSession().setAttribute("listenerException", new ServletException("operation with user data failed",e));
		}
	}
	
	public void sessionDestroyed(HttpSessionEvent se) {
		MySqlUserDao userDao = (MySqlUserDao) se.getSession().getAttribute("user_dao");
		MySqlCountDao countDao = (MySqlCountDao) se.getSession().getAttribute("count_dao");
		MySqlIncomeCategoryDao IncCategoryDao = (MySqlIncomeCategoryDao) se.getSession().getAttribute("income_cat_dao");
		MySqlIncomeDao incomeDao = (MySqlIncomeDao) se.getSession().getAttribute("income_dao");
		MySqlExpenseCategoryDao expCategoryDao = (MySqlExpenseCategoryDao) se.getSession().getAttribute("expense_cat_dao");
		MySqlExpenseDao expenseDao = (MySqlExpenseDao) se.getSession().getAttribute("expense_dao");
		ServletException listenerException = new ServletException("operation with user data failed");
		if(userDao != null) {
			try {
				userDao.close();
			}catch(DaoException e){
				listenerException.addSuppressed(e);
			}
		}
		if(countDao != null) {
			try {
				countDao.close();
			}catch(DaoException e) {
				listenerException.addSuppressed(e);
			}
		}
		if(IncCategoryDao != null) {
			try {
				IncCategoryDao.close();
			}catch(DaoException e) {
				listenerException.addSuppressed(e);
			}
		}
		if(incomeDao != null) {
			try {
				incomeDao.close();
			}catch(DaoException e) {
				listenerException.addSuppressed(e);
			}
		}
		if(expenseDao != null) {
			try {
				expenseDao.close();
			}catch(DaoException e) {
				listenerException.addSuppressed(e);
			}
		}
		if(expCategoryDao != null) {
			try {
				expCategoryDao.close();
			}catch(DaoException e) {
				listenerException.addSuppressed(e);
			}
		}
		if(listenerException.getSuppressed().length != 0) {
			se.getSession().setAttribute("listenerException", listenerException);
		}
	}
}
