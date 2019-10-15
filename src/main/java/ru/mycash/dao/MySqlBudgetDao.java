package ru.mycash.dao;

import java.util.ArrayList;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ru.mycash.domain.BudgetEntry;

import ru.mycash.util.HibernateUtil;

public class MySqlBudgetDao {

	private static final String queryForRead = "from BudgetEntry where id =:id";
	private static final String queryForGetAll = "from BudgetEntry where user_id =:user_id";

	public void insert (BudgetEntry budget) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(budget);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to insert budget entry", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public BudgetEntry read(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		BudgetEntry entry = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <BudgetEntry> query= session.createQuery(queryForRead, BudgetEntry.class);
			query.setParameter("id", id);
			entry = (BudgetEntry) query.uniqueResult();
			transaction.commit();
			return entry;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to read bydget entry", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void update(BudgetEntry budget) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(budget);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to update budget entry", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void delete(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <BudgetEntry> query= session.createQuery(queryForRead, BudgetEntry.class);
			query.setParameter("id", id);
			BudgetEntry entry = (BudgetEntry) query.uniqueResult();
			session.delete(entry);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to delete budget entry", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<BudgetEntry> getAll(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<BudgetEntry> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <BudgetEntry> query= session.createQuery(queryForGetAll, BudgetEntry.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all budget entries", e);
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
