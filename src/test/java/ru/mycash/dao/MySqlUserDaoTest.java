package ru.mycash.dao;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.*;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.User;


public class MySqlUserDaoTest {
	
	private MySqlDaoFactory factory = null;
	private MySqlUserDao userDao = null;
	private User insertedUser = null;
	

	@Test (priority = 1)
	public void testInit() throws DaoException{
		factory = new MySqlDaoFactory();
		userDao = factory.getMySqlUserDao();
		assertNotNull(userDao);
	}
	
	@Test(priority = 2, dependsOnMethods= {"testInit"})
	public void testRead() throws DaoException{
		User user = userDao.read(1);
		assertEquals("first", user.getLogin());
		assertEquals("1Qqqqq", user.getPassword());
		assertEquals("user1@mail.ru", user.getMail());
		assertEquals((Integer)1, user.getId());
		assertEquals((Boolean)true, user.getIsActive());
	}
	
	@Test(priority = 3, dependsOnMethods= {"testInit"})
	public void testReadNull() throws DaoException{
		User user = userDao.read(2);
		assertNull(user);
	}	
	
	@Test(priority = 4, dependsOnMethods= {"testInit"})
	public void testGetByLogin() throws DaoException{
		User user = userDao.getByLogin("first");
		assertEquals("first", user.getLogin());
		assertEquals("1Qqqqq", user.getPassword());
		assertEquals("user1@mail.ru", user.getMail());
		assertEquals((Integer)1, user.getId());
		assertEquals((Boolean)true, user.getIsActive());
	}
	
	@Test(priority = 5, dependsOnMethods= {"testInit"})
	public void testGetByLoginNull() throws DaoException{
		User user = userDao.getByLogin("testNull");
		assertNull(user);
	}
	
	@Test(priority = 6, dependsOnMethods= {"testGetByLogin"})
	public void testInsert() throws DaoException{
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setMail("test@gmail.com");
		userDao.insert(user);
		insertedUser = userDao.getByLogin("test");
		assertEquals(user.getId(), insertedUser.getId());
		assertEquals("test", insertedUser.getLogin());
		assertEquals("2Qqqqq", insertedUser.getPassword());
		assertEquals((Boolean)true, insertedUser.getIsActive());
		assertEquals("test@gmail.com", insertedUser.getMail());
	}
	
	@Test(priority = 7, dependsOnMethods= {"testRead", "testInsert"})
	public void testDeactivate() throws DaoException {
		int insertedId = insertedUser.getId();
		userDao.deactivate(insertedId);
		User user = userDao.read(insertedId);
		assertEquals((Boolean)false, user.getIsActive());
	}
	
	@Test(priority = 8, dependsOnMethods= {"testInsert"})
	public void testDelete() throws DaoException{
		userDao.delete(insertedUser.getId());
		assertNull(userDao.read(insertedUser.getId()));
	}
	

	@Test(priority = 9, dependsOnMethods= {"testInit"})
	public void testClose(){
		try {
			userDao.close();
		}catch(DaoException e) {
			fail("testClose() failed with exception");
		}
	}
}
