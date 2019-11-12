package ru.mycash.dao;


import java.util.*;
import javax.persistence.RollbackException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mycash.domain.IncomeCategory;

public class MySqlIncomeCategoryDao{
	
	@Autowired
	private SessionFactory factory;
	
	private static final String queryForRead = "from IncomeCategory where id =:id";	
	private static final String queryForGetAll = "from IncomeCategory where user_id =:user_id";
	private static final String queryForGetAllActive = "from IncomeCategory where user_id =:user_id and is_active = true";
	private static final String queryForGetByName = "from IncomeCategory where category_name=:category_name and user_id =:user_id";
	
	public void insert (IncomeCategory incomeCat) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			incomeCat.setIsActive(true);
			session.save(incomeCat);
			session.flush();
		}
		catch (HibernateException  e){
			throw new DaoException("Failed to insert income category", e);
		}
	}
	
	public IncomeCategory read(int id) throws DaoException{
		Session session = null;
		IncomeCategory incomeCat = null;
		try{
			session = factory.getCurrentSession();
			Query <IncomeCategory> query= session.createQuery(queryForRead, IncomeCategory.class);
			query.setParameter("id", id);
			incomeCat = (IncomeCategory) query.uniqueResult();
			return incomeCat;
		}
		catch (HibernateException  e){
			throw new DaoException("Failed to read income category", e);
		}
	}
	
	public void update(IncomeCategory incomeCat) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			session.update(incomeCat);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update income category", e);
		}
	}
	
	public void delete(int incomeCatId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <IncomeCategory> query= session.createQuery(queryForRead, IncomeCategory.class);
			query.setParameter("id", incomeCatId);
			IncomeCategory incomeCat = (IncomeCategory) query.uniqueResult();
			session.delete(incomeCat);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete income category", e);
		}
	}
	
	public ArrayList<IncomeCategory> getAll(int userId) throws DaoException{
		Session session = null;
		ArrayList<IncomeCategory> result = null;
		try{
			session = factory.getCurrentSession();
			Query <IncomeCategory> query= session.createQuery(queryForGetAll, IncomeCategory.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<IncomeCategory>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all income categories", e);
		}
	}
	
	public ArrayList<IncomeCategory> getAllActive(int userId) throws DaoException{
		Session session = null;
		ArrayList<IncomeCategory> result = null;
		try{
			session = factory.getCurrentSession();
			Query <IncomeCategory> query= session.createQuery(queryForGetAllActive, IncomeCategory.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<IncomeCategory>)query.list();
			return result;
		}
		catch (HibernateException  e){
			throw new DaoException("Failed to get all active income categories", e);
		}
	}
	
	public IncomeCategory getByName(int userId, String categoryName) throws DaoException{
		Session session = null;
		IncomeCategory incomeCat = null;
		try{
			session = factory.getCurrentSession();
			Query <IncomeCategory> query= session.createQuery(queryForGetByName, IncomeCategory.class);
			query.setParameter("category_name", categoryName);
			query.setParameter("user_id", userId);
			incomeCat = (IncomeCategory) query.uniqueResult();
			return incomeCat;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get income category by name", e);
		}
	}
	
	public void deactivate(int incomeCatId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <IncomeCategory> query= session.createQuery(queryForRead, IncomeCategory.class);
			query.setParameter("id", incomeCatId);
			IncomeCategory incomeCat = (IncomeCategory) query.uniqueResult();
			if(incomeCat == null) {
				throw new DaoException("Income category doesn't exist");
			}
			incomeCat.setIsActive(false);
			session.update(incomeCat);
			session.flush();
		} 
		catch (HibernateException | RollbackException | IllegalStateException  e){
			throw new DaoException("Failed to deactivate income category", e);
		}
	}
}