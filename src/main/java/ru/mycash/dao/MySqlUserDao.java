package ru.mycash.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import ru.mycash.domain.User;

public class MySqlUserDao{
	
	@Autowired
	private SessionFactory factory;
	
	private static final String queryForRead = "from User where id=:id";
	private static final String queryForGetByLogin = "from User where login=:login";
	
	public void insert (User user) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			user.setIsActive(true);
	        session.save(user);
	        session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to insert user", e);
		}
	}
	
	public User read(int id) throws DaoException{
		Session session = null;
		User user = null;
		try{
			session = factory.getCurrentSession();
			Query <User> query= session.createQuery(queryForRead, User.class);
			query.setParameter("id", id);
			user = (User) query.uniqueResult();
			return user;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to read user", e);
		}
	}
	
	public void update(User user) throws DaoException{
		Session session = null;
		try{
			session = factory.getCurrentSession();
			session.update(user);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to update user", e);
		}
	}
	
	public void delete(int id) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <User> query= session.createQuery(queryForRead, User.class);
			query.setParameter("id", id);
			User user = (User) query.uniqueResult();
			session.delete(user);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to delete user", e);
		}
	}
	
	public User getByLogin(String login) throws DaoException{
		Session session = null;
		User user = null;
		try{
			session = factory.getCurrentSession();
			Query <User> query= session.createQuery(queryForGetByLogin, User.class);
			query.setParameter("login", login);
			user = (User) query.uniqueResult();
			return user;
		}
		catch (HibernateException e){
			throw new DaoException("Failed to get user", e);
		}
	}
	
	public void deactivate(int id) throws DaoException{
		Session session = null;
		try {
			session = factory.getCurrentSession();
			Query <User> query= session.createQuery(queryForRead, User.class);
			query.setParameter("id", id);
			User user = (User) query.uniqueResult();
			if(user == null) {
				throw new DaoException("User doesn't exist");
			}
			user.setIsActive(false);
			session.update(user);
			session.flush();
		}
		catch (HibernateException e){
			throw new DaoException("Failed to deactivate user", e);
		}
	}
}
