package ru.mycash.dao;

import java.util.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mycash.domain.Income;


public class MySqlIncomeDao{
	
	@Autowired
	private SessionFactory factory;

	private static final String queryForRead = "from Income where id =:id";
	private static final String queryForGetAll = "from Income where user_id =:user_id";
	private static final String queryForGetAllActive = "from Income where user_id =:user_id and is_active = true";
	private static final String queryForGetallForPeriod  = "from Income where inc_date between :start_date and"
			+ " :end_date and user_id =:user_id and is_active = true";
	
	
	public void insert (Income income) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			income.setIsActive(true);
			session.save(income);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to insert income", e);
		}
	}
	
	public Income read(int id) throws DaoException{
		Session session = null;
		Income income = null;
		try{
			session = factory.getCurrentSession();
			Query <Income> query= session.createQuery(queryForRead, Income.class);
			query.setParameter("id", id);
			income = (Income) query.uniqueResult();
			return income;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to read income", e);
		}
	}
	
	public void update(Income income) throws DaoException{
		Session session = null;

		try{
			session = factory.getCurrentSession();
			session.update(income);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update income", e);
		}
	}
	
	public void delete(int incomeId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <Income> query= session.createQuery(queryForRead, Income.class);
			query.setParameter("id", incomeId);
			Income income = (Income) query.uniqueResult();
			session.delete(income);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete income", e);
		}
	}
	
	public ArrayList<Income> getAll(int userId) throws DaoException{
		Session session = null;
		ArrayList<Income> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Income> query= session.createQuery(queryForGetAll, Income.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<Income>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all incomes", e);
		}
	}
	
	public ArrayList<Income> getAllActive(int userId) throws DaoException{
		Session session = null;
		ArrayList<Income> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Income> query= session.createQuery(queryForGetAllActive, Income.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<Income>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all active incomes", e);
		}
	}	
	
	public ArrayList<Income> getAllForPeriod(String startDate,
			String endDate, int userId) throws DaoException{
		Session session = null;
		ArrayList<Income> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Income> query= session.createQuery(queryForGetallForPeriod, Income.class);
			query.setParameter("start_date", startDate);
			query.setParameter("end_date", endDate);
			query.setParameter("user_id", userId);
			result = (ArrayList<Income>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get incomes for period", e);
		}
	}
	
	public void deactivate(int incomeId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <Income> query= session.createQuery(queryForRead, Income.class);
			query.setParameter("id", incomeId);
			Income income = (Income) query.uniqueResult();
			if(income == null) {
				throw new DaoException("Income doesn't exist");
			}
			income.setIsActive(false);
			session.update(income);
			session.flush();
		} 
		catch (HibernateException e){
			throw new DaoException("Failed to deactivate income", e);
		}
	}
}

