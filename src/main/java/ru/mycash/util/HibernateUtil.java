package ru.mycash.util;

	import org.hibernate.SessionFactory;
	import org.hibernate.cfg.Configuration;
	import org.hibernate.HibernateException;

public class HibernateUtil {
	
	private static SessionFactory factory = buildFactory();
	
	private static SessionFactory buildFactory() throws HibernateException{
		try {
			return new Configuration().configure().buildSessionFactory();
		}
		catch(HibernateException e){
			throw new HibernateException("Failed to build SessionFactory while"
					+ " running HibernateUtil() constructor", e);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return factory;
	}
}
