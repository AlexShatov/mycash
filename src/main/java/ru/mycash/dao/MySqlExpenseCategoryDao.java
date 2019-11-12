package ru.mycash.dao;

import java.util.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mycash.domain.ExpenseCategory;

public class MySqlExpenseCategoryDao{
	
	@Autowired
	private SessionFactory factory;
	
	private static final String queryForRead = "from ExpenseCategory where id =:id ";	
	private static final String queryForGetAll = "from ExpenseCategory where user_id =:user_id";
	private static final String queryForGetAllActive = "from ExpenseCategory where user_id =:user_id and is_active = true";
	private static final String queryForGetByName = "from ExpenseCategory where category_name=:category_name and user_id =:user_id";
	
	public void insert (ExpenseCategory expenseCat) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			expenseCat.setIsActive(true);
			session.save(expenseCat);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to insert expense category", e);
		}
	}
	
	public ExpenseCategory read(int id) throws DaoException{
		Session session = null;
		ExpenseCategory expenseCat = null;
		try{
			session = factory.getCurrentSession();
			Query <ExpenseCategory> query= session.createQuery(queryForRead, ExpenseCategory.class);
			query.setParameter("id", id);
			expenseCat = (ExpenseCategory) query.uniqueResult();
			return expenseCat;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to read expense category", e);
		}
	}
	
	public void update(ExpenseCategory expenseCat) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			session.update(expenseCat);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update expense category", e);
		}
	}
	
	public void delete(int expenseCatId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <ExpenseCategory> query= session.createQuery(queryForRead, ExpenseCategory.class);
			query.setParameter("id", expenseCatId);
			ExpenseCategory expenseCat = (ExpenseCategory) query.uniqueResult();
			session.delete(expenseCat);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete expense category", e);
		}
	}
	
	public ArrayList<ExpenseCategory> getAll(int userId) throws DaoException{
		Session session = null;
		ArrayList<ExpenseCategory> result = null;
		try{
			session = factory.getCurrentSession();
			Query <ExpenseCategory> query= session.createQuery(queryForGetAll, ExpenseCategory.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<ExpenseCategory>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all expense categories", e);
		}
	}
	
	public ArrayList<ExpenseCategory> getAllActive(int userId) throws DaoException{
		Session session = null;
		ArrayList<ExpenseCategory> result = null;
		try{
			session = factory.getCurrentSession();
			Query <ExpenseCategory> query= session.createQuery(queryForGetAllActive, ExpenseCategory.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<ExpenseCategory>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all active expense categories", e);
		}
	}
	
	public ExpenseCategory getByName(int userId, String categoryName) throws DaoException{
		Session session = null;
		ExpenseCategory expenseCat = null;
		try{
			session = factory.getCurrentSession();
			Query <ExpenseCategory> query= session.createQuery(queryForGetByName, ExpenseCategory.class);
			query.setParameter("category_name", categoryName);
			query.setParameter("user_id", userId);
			expenseCat = (ExpenseCategory) query.uniqueResult();
			return expenseCat;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get expense category by name", e);
		}
	}
	
	public void deactivate(int expenseCatId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <ExpenseCategory> query= session.createQuery(queryForRead, ExpenseCategory.class);
			query.setParameter("id", expenseCatId);
			ExpenseCategory expenseCat = (ExpenseCategory) query.uniqueResult();
			if(expenseCat == null) {
				throw new DaoException("Expense category doesn't exist");
			}
			expenseCat.setIsActive(false);
			session.update(expenseCat);
			session.flush();
		} 
		catch (HibernateException e){
			throw new DaoException("Failed to deactivate expense category", e);
		}
	}
}