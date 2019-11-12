package ru.mycash.dao;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import ru.mycash.domain.BudgetEntry;

public class MySqlBudgetDao{

	private static final String queryForRead = "from BudgetEntry where id =:id";
	private static final String queryForGetAll = "from BudgetEntry where user_id =:user_id";
	
	@Autowired
	private SessionFactory factory;

	public void insert (BudgetEntry budget) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			session.save(budget);
			session.flush();
		}
		catch (HibernateException  e){
			throw new DaoException("Failed to insert budget entry", e);
		}
	}
	
	public BudgetEntry read(int id) throws DaoException{
		Session session = null;
		BudgetEntry entry = null;
		try{
			session = factory.getCurrentSession();
			Query <BudgetEntry> query= session.createQuery(queryForRead, BudgetEntry.class);
			query.setParameter("id", id);
			entry = (BudgetEntry) query.uniqueResult();
			return entry;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to read bydget entry", e);
		}
	}
	
	public void update(BudgetEntry budget) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			session.update(budget);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update budget entry", e);
		}
	}
	
	public void delete(int id) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <BudgetEntry> query= session.createQuery(queryForRead, BudgetEntry.class);
			query.setParameter("id", id);
			BudgetEntry entry = (BudgetEntry) query.uniqueResult();
			session.delete(entry);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete budget entry", e);
		}
	}
	
	public ArrayList<BudgetEntry> getAll(int userId) throws DaoException{
		Session session = null;
		ArrayList<BudgetEntry> result = null;
		try{
			session = factory.getCurrentSession();
			Query <BudgetEntry> query= session.createQuery(queryForGetAll, BudgetEntry.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<BudgetEntry>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all budget entries", e);
		}
	}

}
