package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;
import java.util.ArrayList;
import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.ExpenseCategory;
import ru.mycash.test.util.TestDbCleaner;

public class MySqlExpenseCategoryDaoTest {

	private MySqlDaoFactory factory = null;
	private MySqlExpenseCategoryDao expenseCatDao = null;
	private TestDbCleaner cleaner = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		expenseCatDao = factory.getMySqlExpenseCategoryDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanExpenseCategories();
	}
	
	@Test 
	public void testInit() throws DaoException{
		MySqlExpenseCategoryDao sqlExpenseCatDao = factory.getMySqlExpenseCategoryDao();
		assertNotNull(sqlExpenseCatDao);
		sqlExpenseCatDao.close();
	}
	
	@Test
	public void testRead() throws DaoException{
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		assertEquals("testCategory", expenseCat.getCategoryName());
		assertEquals((Boolean)true, expenseCat.getIsActive());
		assertEquals((Integer)1, expenseCat.getUserId());
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
		assertEquals((Integer)1, expenseCat.getUserId());
	}
	
	@Test
	public void testGetByNameNull() throws DaoException{
		ExpenseCategory expenseCat = expenseCatDao.getByName(2, "testCategory");
		assertNull(expenseCat);
	}
	
	@Test
	public void testInsert() throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		expenseCat.setCategoryName("new");
		expenseCat.setUserId(1);
		expenseCatDao.insert(expenseCat);
		ExpenseCategory insertedExpenseCat = expenseCatDao.getByName(1, "new");
		assertEquals(expenseCat.getId(), insertedExpenseCat.getId());
		assertEquals("new", insertedExpenseCat.getCategoryName());
		assertEquals((Integer)1, insertedExpenseCat.getUserId());
		assertEquals((Boolean)true, insertedExpenseCat.getIsActive());
		expenseCatDao.delete(insertedExpenseCat.getId());
	}
	
	@Test
	public void testDelete() throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		expenseCat.setCategoryName("new");
		expenseCat.setUserId(1);
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
	public void testGetAllActive(int userId, int categoryArraySize) throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		expenseCat.setCategoryName("new");
		expenseCat.setUserId(1);
		expenseCatDao.insert(expenseCat);
		expenseCatDao.deactivate(expenseCat.getId());
		ArrayList<ExpenseCategory> allActive = expenseCatDao.getAllActive(userId);
		assertEquals((Integer)categoryArraySize, (Integer)allActive.size());
		expenseCatDao.delete(expenseCat.getId());
	}
	
	@Test
	public void testDeactivate() throws DaoException {
		ExpenseCategory expenseCat = new ExpenseCategory();
		expenseCat.setCategoryName("new");
		expenseCat.setUserId(1);
		expenseCatDao.insert(expenseCat);
		expenseCatDao.deactivate(expenseCat.getId());
		expenseCat = expenseCatDao.read(expenseCat.getId());
		assertEquals((Boolean)false, expenseCat.getIsActive());
		expenseCatDao.delete(expenseCat.getId());
	}
	
	@Test
	public void testUpdate()throws DaoException{
		ExpenseCategory expenseCat = new ExpenseCategory();
		expenseCat.setCategoryName("new");
		expenseCat.setUserId(1);
		expenseCatDao.insert(expenseCat);
		expenseCat = expenseCatDao.read(expenseCat.getId());
		expenseCat.setCategoryName("updated");
		expenseCatDao.update(expenseCat);
		ExpenseCategory updated = expenseCatDao.read(expenseCat.getId());
		assertEquals("updated", updated.getCategoryName());
		expenseCatDao.delete(updated.getId());
	}
	
	@Test
	public void testDeactivateNull() throws DaoException{
		expenseCatDao.deactivate(2);
		assertNull(expenseCatDao.read(2));
	}
	
	@Test
	public void testClose(){
		try {
			MySqlExpenseCategoryDao sqlExpenseCatDao = factory.getMySqlExpenseCategoryDao();
			assertNotNull(sqlExpenseCatDao);
			sqlExpenseCatDao.close();
		}catch(DaoException e) {
			fail("testClose() failed with exception");
		}
	}
	
	@AfterClass
	public void closeRes() throws DaoException{
		cleaner.close();
		expenseCatDao.close();
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
