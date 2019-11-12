package ru.mycash.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mycash.domain.Count;

import java.util.ArrayList;

public class MySqlCountDao{
	
	@Autowired
	private SessionFactory factory;
	
	private static final String queryForRead = "from Count where id =:id ";	
	private static final String queryForGetAll = "from Count where user_id =:user_id";
	private static final String queryForGetAllActive = "from Count where user_id =:user_id and is_active = true";
	private static final String queryForGetByName = "from Count where count_name=:count_name and user_id =:user_id";
	
	public void insert (Count count) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			count.setIsActive(true);
			session.save(count);
		}
		catch (HibernateException e){
			throw new DaoException("Failed to insert count", e);
		}
	}
	
	public Count read(int id) throws DaoException{
		Session session = null;
		Count count = null;
		try{
			session = factory.getCurrentSession();
			Query <Count> query= session.createQuery(queryForRead, Count.class);
			query.setParameter("id", id);
			count = (Count) query.uniqueResult();
			return count;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to read count", e);
		}
	}
	
	public void update(Count count) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			session.update(count);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update count", e);
		}
	}
	
	public void delete(int countId) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			Query <Count> query= session.createQuery(queryForRead, Count.class);
			query.setParameter("id", countId);
			Count count = (Count) query.uniqueResult();
			session.delete(count);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete count", e);
		}
	} 
	
	public ArrayList<Count> getAll(int userId) throws DaoException{
		Session session = null;
		ArrayList<Count> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Count> query= session.createQuery(queryForGetAll, Count.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<Count>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all counts for user", e);
		}
	}
	
	public ArrayList<Count> getAllActive(int userId) throws DaoException{
		Session session = null;
		ArrayList<Count> result = null;
		try{
			session = factory.getCurrentSession();
			Query <Count> query= session.createQuery(queryForGetAllActive, Count.class);
			query.setParameter("user_id", userId);
			result = (ArrayList<Count>)query.list();
			return result;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get all active counts for user", e);
		}
	}
	
	public Count getByName(String countName, int userId) throws DaoException{
		Session session = null;
		Count count = null;
		try{
			session = factory.getCurrentSession();
			Query <Count> query= session.createQuery(queryForGetByName, Count.class);
			query.setParameter("count_name", countName);
			query.setParameter("user_id", userId);
			count = (Count) query.uniqueResult();
			return count;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get count by name", e);
		}
	}
	
	public void deactivate(int countId) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <Count> query= session.createQuery(queryForRead, Count.class);
			query.setParameter("id", countId);
			Count count = (Count) query.uniqueResult();
			if(count == null) {
				throw new DaoException("Count doesn't exist");
			}
			count.setIsActive(false);
			session.update(count);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to deactivate count", e);
		}
	}
}