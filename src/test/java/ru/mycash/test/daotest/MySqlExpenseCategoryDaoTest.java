package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.*;

import java.util.ArrayList;

import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.AppCtxConfig;
import ru.mycash.dao.DaoException;
import ru.mycash.domain.ExpenseCategory;
import ru.mycash.domain.User;

@WebAppConfiguration
@ContextConfiguration(classes=AppCtxConfig.class)
public class MySqlExpenseCategoryDaoTest extends AbstractTransactionalTestNGSpringContextTests{

	@Autowired
	private MySqlExpenseCategoryDao expenseCatDao;
	@Autowired
	private MySqlUserDao userDao;
	
	@Test
	public void testRead() throws DaoException{
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		assertEquals("testCategory", expenseCat.getCategoryName());
		assertEquals((Boolean)true, expenseCat.getIsActive());
		assertEquals((Integer)1, expenseCat.getUser().getId());
	}
	
	@Test
	public void testReadNull() throws DaoException{
		ExpenseCategory expenseCat = expenseCatDao.read(2);
		assertNull(expenseCat);
	}	
	
	@Test
	public void testGetByName() throws DaoException{
		ExpenseCategory expenseCat = expenseCatDao.getByName(1, "testCategory");
		assertEquals("testCategory", expenseCat.getCategoryName());
		assertEquals((Boolean)true, expenseCat.getIsActive());
		assertEquals((Integer)1, expenseCat.getUser().getId());
	}
	
	@Test
	public void testGetByNameNull() throws DaoException{
		ExpenseCategory expenseCat = expenseCatDao.getByName(2, "testCategory");
		assertNull(expenseCat);
	}
	
	@Test
	@Transactional
	public void testInsert() throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		User user = userDao.read(1);
		expenseCat.setCategoryName("new");
		expenseCat.setIsActive(true);
		expenseCat.setUser(user);
		expenseCatDao.insert(expenseCat);
		ExpenseCategory insertedExpenseCat = expenseCatDao.getByName(1, "new");
		assertEquals(expenseCat.getId(), insertedExpenseCat.getId());
		assertEquals("new", insertedExpenseCat.getCategoryName());
		assertEquals((Integer)1, insertedExpenseCat.getUser().getId());
		assertEquals((Boolean)true, insertedExpenseCat.getIsActive());
		expenseCatDao.delete(insertedExpenseCat.getId());
	}
	
	@Test
	@Transactional
	public void testDelete() throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		User user = userDao.read(1);
		expenseCat.setCategoryName("new");
		expenseCat.setIsActive(true);
		expenseCat.setUser(user);
		expenseCatDao.insert(expenseCat);
		expenseCatDao.delete(expenseCat.getId());
		assertNull(expenseCatDao.read(expenseCat.getId()));
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int categoryArraySize) throws DaoException{
		ArrayList<ExpenseCategory> all = expenseCatDao.getAll(userId);
		assertEquals((Integer)categoryArraySize, (Integer)all.size());
	}
	
	@Test(dataProvider = "getGetAllActiveData")
	@Transactional
	public void testGetAllActive(int userId, int categoryArraySize) throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		User user = userDao.read(1);
		expenseCat.setCategoryName("new");
		expenseCat.setIsActive(true);
		expenseCat.setUser(user);
		expenseCatDao.insert(expenseCat);
		expenseCatDao.deactivate(expenseCat.getId());
		ArrayList<ExpenseCategory> allActive = expenseCatDao.getAllActive(userId);
		assertEquals((Integer)categoryArraySize, (Integer)allActive.size());
		expenseCatDao.delete(expenseCat.getId());
	}
	
	@Test
	@Transactional
	public void testDeactivate() throws DaoException {
		ExpenseCategory expenseCat = new ExpenseCategory();
		User user = userDao.read(1);
		expenseCat.setCategoryName("new");
		expenseCat.setIsActive(true);
		expenseCat.setUser(user);
		expenseCatDao.insert(expenseCat);
		expenseCatDao.deactivate(expenseCat.getId());
		expenseCat = expenseCatDao.read(expenseCat.getId());
		assertEquals((Boolean)false, expenseCat.getIsActive());
		expenseCatDao.delete(expenseCat.getId());
	}
	
	@Test (expectedExceptions = DaoException.class)
	@Transactional
	public void testDeactivateNull() throws DaoException{
		expenseCatDao.deactivate(2);
	}
	
	@Test
	@Transactional
	public void testUpdate()throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		User user = userDao.read(1);
		expenseCat.setCategoryName("new");
		expenseCat.setIsActive(true);
		expenseCat.setUser(user);
		expenseCatDao.insert(expenseCat);
		expenseCat = expenseCatDao.read(expenseCat.getId());
		expenseCat.setCategoryName("updated");
		expenseCatDao.update(expenseCat);
		ExpenseCategory updated = expenseCatDao.read(expenseCat.getId());
		assertEquals("updated", updated.getCategoryName());
		expenseCatDao.delete(updated.getId());
	}
	
	@DataProvider
	public Object[][] getGetAllData(){
		return new Object[][] {{1, 1}, {2, 0}};
	}
	
	@DataProvider
	public Object[][] getGetAllActiveData(){
		return new Object[][] {{1, 1}, {2, 0}};
	}
}
