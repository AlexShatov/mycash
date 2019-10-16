package ru.mycash.dao;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import ru.mycash.util.HibernateUtil;

import ru.mycash.domain.Count;

import java.util.ArrayList;

public class MySqlCountDao extends MycashDao{
	
	private static final String queryForRead = "from Count where id =:id ";	
	private static final String queryForGetAll = "from Count where user_id =:user_id";
	private static final String queryForGetAllActive = "from Count where user_id =:user_id and is_active = true";
	private static final String queryForGetByName = "from Count where count_name=:count_name and user_id =:user_id";
	
	public void insert (Count count) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(count);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to insert count", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public Count read(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		Count count = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Count> query= session.createQuery(queryForRead, Count.class);
			query.setParameter("id", id);
			count = (Count) query.uniqueResult();
			transaction.commit();
			return count;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to read count", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void update(Count count) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(count);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to update count", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void delete(int countId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Count> query= session.createQuery(queryForRead, Count.class);
			query.setParameter("id", countId);
			Count count = (Count) query.uniqueResult();
			session.delete(count);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to delete count", e);
		}
		finally{
			closeSession(session);
		}
	} 
	
	public ArrayList<Count> getAll(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Count> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Count> query= session.createQuery(queryForGetAll, Count.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all counts for user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public ArrayList<Count> getAllActive(int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		ArrayList<Count> result = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Count> query= session.createQuery(queryForGetAllActive, Count.class);
			query.setParameter("user_id", userId);
			result = (ArrayList)query.list();
			return result;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get all active counts for user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public Count getByName(String countName, int userId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		Count count = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Count> query= session.createQuery(queryForGetByName, Count.class);
			query.setParameter("count_name", countName);
			query.setParameter("user_id", userId);
			count = (Count) query.uniqueResult();
			return count;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get count by name", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void deactivate(int countId) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <Count> query= session.createQuery(queryForRead, Count.class);
			query.setParameter("id", countId);
			Count count = (Count) query.uniqueResult();
			if(count == null) {
				throw new DaoException("Count doesn't exist");
			}
			count.setIsActive(false);
			session.update(count);
			transaction.commit();	
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to deactivate count", e);
		}
		finally{
			closeSession(session);
		}
	}
}