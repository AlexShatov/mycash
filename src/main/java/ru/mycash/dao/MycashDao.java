package ru.mycash.dao;

import org.hibernate.Session;
import org.hibernate.HibernateException;

public class MycashDao {
	
	
	public void closeSession(Session session) throws DaoException {
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
