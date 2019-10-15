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
		
	}
}
