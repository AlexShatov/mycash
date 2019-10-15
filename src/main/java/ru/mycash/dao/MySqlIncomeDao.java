package ru.mycash.dao;

import java.util.*;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ru.mycash.domain.Income;
import ru.mycash.util.HibernateUtil;

public class MySqlIncomeDao {

	private static final String queryForRead = "from Income where id =:id";
	private static final String queryForGetAll = "from Income where user_id =:user_id";
	private static final String queryForGetAllActive = "from Income where user_id =:user_id and is_active = true";
	private static final String queryForGetallForPeriod  = "from Income where inc_date between :start_date and :end_date and user_id =:user_id";
	
	
	public void insert (Income income) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(income);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to insert income", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public Income read(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		Income income = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Income> query= session.createQuery(queryForRead, Income.class);
			query.setParameter("id", id);
			income = (Income) query.uniqueResult();
			transaction.commit();
			return income;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to read income", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void update(Income income) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(income);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to update income", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void delete(int incomeId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Income> query= session.createQuery(queryForRead, Income.class);
			query.setParameter("id", incomeId);
			Income income = (Income) query.uniqueResult();
			session.delete(income);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to delete income", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<Income> getAll(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Income> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Income> query= session.createQuery(queryForGetAll, Income.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all incomes", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<Income> getAllActive(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Income> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Income> query= session.createQuery(queryForGetAllActive, Income.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all active incomes", e);
		}
		finally{
			closeSession(session);
		}
	}	
	
	public ArrayList<Income> getAllForPeriod(String startDate,
			String endDate, int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Income> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Income> query= session.createQuery(queryForGetallForPeriod, Income.class);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get incomes for period", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void deactivate(int incomeId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Income> query= session.createQuery(queryForRead, Income.class);
			query.setParameter("id", incomeId);
			Income income = (Income) query.uniqueResult();
			if(income == null) {
				throw new DaoException("Income doesn't exist");
			}
			income.setIsActive(false);
			session.update(income);
			transaction.commit();
		} 
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to deactivate income", e);
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

