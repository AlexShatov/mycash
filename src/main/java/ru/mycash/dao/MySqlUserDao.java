package ru.mycash.dao;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import ru.mycash.domain.User;
import ru.mycash.util.HibernateUtil;

public class MySqlUserDao{
	
	private static final String queryForRead = "from User where id=:id";
	private static final String queryForGetByLogin = "from User where login=:login";
	
	public void insert (User user) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session= HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
	        session.save(user);
	        transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to insert user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public User read(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		User user = null;
		try{
			session= HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <User> query= session.createQuery(queryForRead, User.class);
			query.setParameter("id", id);
			user = (User) query.uniqueResult();
			transaction.commit();
			return user;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to read user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void update(User user) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try{
			session= HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(user);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to update user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void delete(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session= HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <User> query= session.createQuery(queryForRead, User.class);
			query.setParameter("id", id);
			User user = (User) query.uniqueResult();
			session.delete(user);
			transaction.commit();
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to delete user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public User getByLogin(String login) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		User user = null;
		try{
			session= HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <User> query= session.createQuery(queryForGetByLogin, User.class);
			query.setParameter("login", login);
			user = (User) query.uniqueResult();
			return user;
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to get user", e);
		}
		finally{
			closeSession(session);
		}
	}
	
	public void deactivate(int id) throws DaoException{
		Session session = null;
		Transaction transaction = null;
		try {
			session= HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query <User> query= session.createQuery(queryForRead, User.class);
			query.setParameter("id", id);
			User user = (User) query.uniqueResult();
			if(user == null) {
				throw new DaoException("User doesn't exist");
			}
			user.setIsActive(false);
			session.update(user);
			transaction.commit();	
		}
		catch (HibernateException | RollbackException | IllegalStateException  e){
			if(transaction != null) {
				transaction.rollback();
			}
			throw new DaoException("Failed to deactivate user", e);
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
