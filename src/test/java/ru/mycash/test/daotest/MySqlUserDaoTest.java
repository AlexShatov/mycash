package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.*;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.AppCtxConfig;
import ru.mycash.dao.DaoException;
import ru.mycash.domain.User;

@WebAppConfiguration
@ContextConfiguration(classes=AppCtxConfig.class)
public class MySqlUserDaoTest extends AbstractTransactionalTestNGSpringContextTests{
	
	@Autowired
	private MySqlUserDao userDao;
	
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
	@Transactional
	public void testInsert() throws DaoException{
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setIsActive(true);
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
	@Transactional
	public void testInsertException() throws DaoException{
		User user = new User();
		user.setPassword("2Qqqqq");
		user.setMail("test@gmail.com");
		userDao.insert(user);
	}
	
	@Test 
	@Transactional
	public void testDeactivate() throws DaoException {
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setIsActive(true);
		user.setMail("test@gmail.com");
		userDao.insert(user);
		userDao.deactivate(user.getId());
		User insertedUser = userDao.read(user.getId());
		assertEquals((Boolean)false, insertedUser.getIsActive());
		userDao.delete(user.getId());
	}
	
	@Test (expectedExceptions = DaoException.class)
	@Transactional
	public void TestDeactivateNull() throws DaoException{
		userDao.deactivate(2);
	}
	
	@Test 
	@Transactional
	public void testDelete() throws DaoException{
		User user = new User();
		user.setLogin("test");
		user.setPassword("2Qqqqq");
		user.setIsActive(true);
		user.setMail("test@gmail.com");
		userDao.insert(user);
		userDao.delete(user.getId());
		assertNull(userDao.read(user.getId()));
	}
}
