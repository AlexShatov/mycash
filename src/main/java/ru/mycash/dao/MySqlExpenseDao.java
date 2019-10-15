package ru.mycash.dao;

import java.sql.*;
import java.io.*;
import java.util.*;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import ru.mycash.domain.Expense;
import ru.mycash.domain.Income;
import ru.mycash.util.HibernateUtil;

import java.text.*;

public class MySqlExpenseDao{
	
	private static final String queryForRead = "from Expense where id =:id";
	private static final String queryForGetAll = "from Expense where user_id =:user_id";
	private static final String queryForGetAllActive = "from Expense where user_id =:user_id and is_active = true";
	private static final String queryForGetallForPeriod  = "from Expense where exp_date between :start_date and :end_date and user_id =:user_id";
	
	public void insert (Expense expense) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(expense);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to insert expense", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public Expense read(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		Expense expense = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Expense> query= session.createQuery(queryForRead, Expense.class);
			query.setParameter("id", id);
			expense = (Expense) query.uniqueResult();
			transaction.commit();
			return expense;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to read expense", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void update(Expense expense) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(expense);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to update expense", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void delete(int expenseId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Expense> query= session.createQuery(queryForRead, Expense.class);
			query.setParameter("id", expenseId);
			Expense expense = (Expense) query.uniqueResult();
			session.delete(expense);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to delete expense", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<Expense> getAll(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Expense> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Expense> query= session.createQuery(queryForGetAll, Expense.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all expenses", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<Expense> getAllActive(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Expense> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Expense> query= session.createQuery(queryForGetAllActive, Expense.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all active expenses", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<Expense> getAllForPeriod(String startDate, 
			String endDate, int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Expense> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Expense> query= session.createQuery(queryForGetallForPeriod, Expense.class);
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
			throw new DaoException("Failed to get expenses for period", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void deactivate(int expenseId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Expense> query= session.createQuery(queryForRead, Expense.class);
			query.setParameter("id", expenseId);
			Expense expense = (Expense) query.uniqueResult();
			if(expense == null) {
				throw new DaoException("Expense doesn't exist");
			}
			expense.setIsActive(false);
			session.update(expense);
			transaction.commit();
		} 
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to deactivate expense", e);
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