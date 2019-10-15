package ru.mycash.dao;

import java.util.*;
import javax.persistence.RollbackException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.mycash.domain.ExpenseCategory;
import ru.mycash.util.HibernateUtil;

public class MySqlExpenseCategoryDao{
	
	private static final String queryForRead = "from ExpenseCategory where id =:id ";	
	private static final String queryForGetAll = "from ExpenseCategory where user_id =:user_id";
	private static final String queryForGetAllActive = "from ExpenseCategory where user_id =:user_id and is_active = true";
	private static final String queryForGetByName = "from ExpenseCategory where category_name=:category_name and user_id =:user_id";
	
	public void insert (ExpenseCategory expenseCat) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(expenseCat);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to insert expense category", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ExpenseCategory read(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ExpenseCategory expenseCat = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <ExpenseCategory> query= session.createQuery(queryForRead, ExpenseCategory.class);
			query.setParameter("id", id);
			expenseCat = (ExpenseCategory) query.uniqueResult();
			transaction.commit();
			return expenseCat;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to read expense category", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void update(ExpenseCategory expenseCat) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(expenseCat);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to update expense category", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void delete(int expenseCatId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <ExpenseCategory> query= session.createQuery(queryForRead, ExpenseCategory.class);
			query.setParameter("id", expenseCatId);
			ExpenseCategory expenseCat = (ExpenseCategory) query.uniqueResult();
			session.delete(expenseCat);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to delete expense category", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<ExpenseCategory> getAll(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<ExpenseCategory> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <ExpenseCategory> query= session.createQuery(queryForGetAll, ExpenseCategory.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all expense categories", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<ExpenseCategory> getAllActive(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<ExpenseCategory> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <ExpenseCategory> query= session.createQuery(queryForGetAllActive, ExpenseCategory.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all active expense categories", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ExpenseCategory getByName(int userId, String categoryName) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ExpenseCategory expenseCat = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <ExpenseCategory> query= session.createQuery(queryForGetByName, ExpenseCategory.class);
			query.setParameter("category_name", categoryName);
			query.setParameter("user_id", userId);
			expenseCat = (ExpenseCategory) query.uniqueResult();
			return expenseCat;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get expense category by name", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void deactivate(int expenseCatId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <ExpenseCategory> query= session.createQuery(queryForRead, ExpenseCategory.class);
			query.setParameter("id", expenseCatId);
			ExpenseCategory expenseCat = (ExpenseCategory) query.uniqueResult();
			if(expenseCat == null) {
				throw new DaoException("Expense category doesn't exist");
			}
			expenseCat.setIsActive(false);
			session.update(expenseCat);
			transaction.commit();
		} 
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to deactivate expense category", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	private void closeSession(Session session) throws DaoException{
		if (session != null){
			try{
				session.close();
			}
			catch (HibernateException e){
				throw new DaoException("Unable to close Session", e);
			}
		}
	}

}