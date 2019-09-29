package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.*;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.User;
import ru.mycash.test.util.TestDbCleaner;


public class MySqlUserDaoTest {
	
	private MySqlDaoFactory factory = null;
	private MySqlUserDao userDao = null;
	private TestDbCleaner cleaner = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		userDao = factory.getMySqlUserDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanUsers();
	}
	
	
	@Test 
	public void testInit() throws DaoException{
		MySqlUserDao sqlUserDao = factory.getMySqlUserDao();
		assertNotNull(sqlUserDao);
		sqlUserDao.close();
	}
	
	@Test 
	public void testRead() throws DaoException{
		User firstUser = userDao.read(1);
		assertEquals("first", firstUser.getLogin());
		assertEquals("1Qqqqq", firstUser.getPassword());
		assertEquals("user1@mail.ru", firstUser.getMail());
		assertEquals((Integer)1, firstUser.getId());
		assertEquals((Boolean)true, firstUser.getIsActive());
	}
	
	@Test 
	public void testReadNull() throws DaoException{
		User secondUser = userDao.read(2);
		assertNull(secondUser);
	}	
	
	@Test 
	public void testGetByLogin() throws DaoException{
		User firstUser = userDao.getByLogin("first");
		assertEquals("first", firstUser.getLogin());
		assertEquals("1Qqqqq", firstUser.getPassword());
		assertEquals("user1@mail.ru", firstUser.getMail());
		assertEquals((Integer)1, firstUser.getId());
		assertEquals((Boolean)true, firstUser.getIsActive());
	}
	
	@Test 
	public void testGetByLoginNull() throws DaoException{
		User secondUser = userDao.getByLogin("testNull");
		assertNull(secondUser);
	}
	
	@Test 
	public void testInsert() throws DaoException{
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setMail("test@gmail.com");
		userDao.insert(user);
		User insertedUser = userDao.getByLogin("test");
		assertEquals(user.getId(), insertedUser.getId());
		assertEquals("test", insertedUser.getLogin());
		assertEquals("2Qqqqq", insertedUser.getPassword());
		assertEquals((Boolean)true, insertedUser.getIsActive());
		assertEquals("test@gmail.com", insertedUser.getMail());
		userDao.delete(insertedUser.getId());
	}
	
	@Test(expectedExceptions = DaoException.class)
	public void testInsertException() throws DaoException{
		User user = new User();
		user.setPassword("2Qqqqq");
		user.setMail("test@gmail.com");
		userDao.insert(user);
	}
	
	@Test 
	public void testDeactivate() throws DaoException {
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setMail("test@gmail.com");
		userDao.insert(user);
		userDao.deactivate(user.getId());
		User insertedUser = userDao.read(user.getId());
		assertEquals((Boolean)false, insertedUser.getIsActive());
		userDao.delete(user.getId());
	}
	
	@Test
	public void TestDeactivateNull() throws DaoException{
		userDao.deactivate(2);
		assertNull(userDao.read(2));
	}
	
	@Test 
	public void testDelete() throws DaoException{
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setMail("test@gmail.com");
		userDao.insert(user);
		userDao.delete(user.getId());
		assertNull(userDao.read(user.getId()));
	}
	

	@Test 
	public void testClose(){
		try {
			MySqlUserDao sqlUserDao = factory.getMySqlUserDao();
			assertNotNull(sqlUserDao);
			sqlUserDao.close();
		}catch(DaoException e) {
			fail("testClose() failed with exception");
		}
	}
	
	@AfterClass
	public void closeRes() throws DaoException{
		cleaner.close();
		userDao.close();
	}
}
