package ru.mycash.test.util;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ru.mycash.dao.DaoException;
import ru.mycash.dao.MycashDao;

import ru.mycash.util.HibernateUtil;

public class TestDbCleaner extends MycashDao{


	private static final String queryForUsers = "delete User where id > 1";
	private static final String queryForCounts = "delete Count where id > 1";
	private static final String queryForIncomeCategories = "delete IncomeCategory where id > 1";
	private static final String queryForExpenseCategories = "delete ExpenseCategory where id > 1";
	private static final String queryForIncomes = "delete Income where id > 1";
	private static final String queryForExpenses = "delete Expense where id > 1";
	private static final String queryForBudget = "delete BudgetEntry where id > 1";
	
	
	public void cleanUsers() throws DaoException{
		clean(queryForUsers);
	}
	
	public void cleanCounts() throws DaoException{
		clean(queryForCounts);
	}
	
	public void cleanIncomeCategories() throws DaoException{
		clean(queryForIncomeCategories);
	}
	
	public void cleanExpenseCategories() throws DaoException{
		clean(queryForExpenseCategories);
	}
	
	public void cleanIncomes() throws DaoException{
		clean(queryForIncomes);
	}
	
	public void cleanExpenses() throws DaoException{
		clean(queryForExpenses);
	}
	
	public void cleanBudget() throws DaoException{
		clean(queryForBudget);
	}
	
	public void clean(String query) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.createQuery(query).executeUpdate();
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to clean table in DB", e);
		}
		finally{
			closeSession(session);
		}
	}
}
