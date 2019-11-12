package ru.mycash.dao;

import java.util.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mycash.domain.Expense;

public class MySqlExpenseDao{
	
	@Autowired
	private SessionFactory factory;
	
	private static final String queryForRead = "from Expense where id =:id";
	private static final String queryForGetAll = "from Expense where user_id =:user_id";
	private static final String queryForGetAllActive = "from Expense where user_id =:user_id and is_active = true";
	private static final String queryForGetallForPeriod  = "from Expense where exp_date between :start_date and"
															+ " :end_date and user_id =:user_id and is_active = true";
	
	public void insert (Expense expense) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			expense.setIsActive(true);
			session.save(expense);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to insert expense", e);
		}
	}
	
	public Expense read(int id) throws DaoException{
		Session session = null;
		Expense expense = null;
		try{
			session = factory.getCurrentSession();
			Query <Expense> query= session.createQuery(queryForRead, Expense.class);
			query.setParameter("id", id);
			expense = (Expense) query.uniqueResult();
			return expense;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to read expense", e);
		}
	}
	
	public void update(Expense expense) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			session.update(expense);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update expense", e);
		}
	}
	
	public void delete(int expenseId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <Expense> query= session.createQuery(queryForRead, Expense.class);
			query.setParameter("id", expenseId);
			Expense expense = (Expense) query.uniqueResult();
			session.delete(expense);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete expense", e);
		}
	}
	
	public ArrayList<Expense> getAll(int userId) throws DaoException{
		Session session = null;
		ArrayList<Expense> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Expense> query= session.createQuery(queryForGetAll, Expense.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<Expense>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all expenses", e);
		}
	}
	
	public ArrayList<Expense> getAllActive(int userId) throws DaoException{
		Session session = null;
		ArrayList<Expense> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Expense> query= session.createQuery(queryForGetAllActive, Expense.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<Expense>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all active expenses", e);
		}
	}
	
	public ArrayList<Expense> getAllForPeriod(String startDate, 
			String endDate, int userId) throws DaoException{
		Session session = null;
		ArrayList<Expense> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Expense> query= session.createQuery(queryForGetallForPeriod, Expense.class);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("user_id", userId);
			result = (ArrayList<Expense>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get expenses for period", e);
		}
	}
	
	public void deactivate(int expenseId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <Expense> query= session.createQuery(queryForRead, Expense.class);
			query.setParameter("id", expenseId);
			Expense expense = (Expense) query.uniqueResult();
			if(expense == null) {
				throw new DaoException("Expense doesn't exist");
			}
			expense.setIsActive(false);
			session.update(expense);
			session.flush();
		} 
		catch (HibernateException  e){
			throw new DaoException("Failed to deactivate expense", e);
		}
	}
}