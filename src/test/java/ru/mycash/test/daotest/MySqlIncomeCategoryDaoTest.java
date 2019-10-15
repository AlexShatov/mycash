package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;
import java.util.ArrayList;
import ru.mycash.dao.MySqlIncomeCategoryDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.IncomeCategory;
import ru.mycash.domain.User;
import ru.mycash.test.util.TestDbCleaner;

public class MySqlIncomeCategoryDaoTest {

	private MySqlDaoFactory factory = null;
	private MySqlIncomeCategoryDao incomeCatDao = null;
	private TestDbCleaner cleaner = null;
	private MySqlUserDao userDao = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		incomeCatDao = factory.getMySqlIncomeCategoryDao();
		userDao = factory.getMySqlUserDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanIncomeCategories();
	}
	
	@Test
	public void testRead() throws DaoException{
		IncomeCategory incomeCat = incomeCatDao.read(1);
		assertEquals("testCategory", incomeCat.getCategoryName());
		assertEquals((Boolean)true, incomeCat.getIsActive());
		assertEquals((Integer)1, incomeCat.getUser().getId());
	}
	
	@Test
	public void testReadNull() throws DaoException{
		IncomeCategory incomeCat = incomeCatDao.read(2);
		assertNull(incomeCat);
	}	
	
	@Test
	public void testGetByName() throws DaoException{
		IncomeCategory incomeCat = incomeCatDao.getByName(1, "testCategory");
		assertEquals("testCategory", incomeCat.getCategoryName());
		assertEquals((Boolean)true, incomeCat.getIsActive());
		assertEquals((Integer)1, incomeCat.getUser().getId());
	}
	
	@Test
	public void testGetByNameNull() throws DaoException{
		IncomeCategory incomeCat = incomeCatDao.getByName(2, "testCategory");
		assertNull(incomeCat);
	}
	
	@Test
	public void testInsert() throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		User user = userDao.read(1);
		incomeCat.setCategoryName("new");
		incomeCat.setIsActive(true);
		incomeCat.setUser(user);
		incomeCatDao.insert(incomeCat);
		IncomeCategory insertedIncomeCat = incomeCatDao.getByName(1, "new");
		assertEquals(incomeCat.getId(), insertedIncomeCat.getId());
		assertEquals("new", insertedIncomeCat.getCategoryName());
		assertEquals((Integer)1, incomeCat.getUser().getId());
		assertEquals((Boolean)true, insertedIncomeCat.getIsActive());
		incomeCatDao.delete(insertedIncomeCat.getId());
	}
	
	@Test
	public void testDelete() throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		User user = userDao.read(1);
		incomeCat.setCategoryName("new");
		incomeCat.setIsActive(true);
		incomeCat.setUser(user);
		incomeCatDao.insert(incomeCat);
		int incomeCatId = incomeCat.getId(); 
		incomeCatDao.delete(incomeCatId);
		assertNull(incomeCatDao.read(incomeCat.getId()));
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int categoryArraySize) throws DaoException{
		ArrayList<IncomeCategory> all = incomeCatDao.getAll(userId);
		assertEquals((Integer)categoryArraySize, (Integer)all.size());
	}
	
	@Test(dataProvider = "getGetAllActiveData")
	public void testGetAllActive(int userId, int categoryArraySize) throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		User user = userDao.read(1);
		incomeCat.setCategoryName("new");
		incomeCat.setIsActive(true);
		incomeCat.setUser(user);
		incomeCatDao.insert(incomeCat);
		incomeCatDao.deactivate(incomeCat.getId());
		ArrayList<IncomeCategory> allActive = incomeCatDao.getAllActive(userId);
		assertEquals((Integer)categoryArraySize, (Integer)allActive.size());
		incomeCatDao.delete(incomeCat.getId());
	}
	
	@Test
	public void testDeactivate() throws DaoException {
		IncomeCategory incomeCat = new IncomeCategory();
		User user = userDao.read(1);
		incomeCat.setCategoryName("new");
		incomeCat.setIsActive(true);
		incomeCat.setUser(user);
		incomeCatDao.insert(incomeCat);
		incomeCatDao.deactivate(incomeCat.getId());
		incomeCat = incomeCatDao.read(incomeCat.getId());
		assertEquals((Boolean)false, incomeCat.getIsActive());
		incomeCatDao.delete(incomeCat.getId());
	}
	
	@Test (expectedExceptions = DaoException.class)
	public void testDeactivateNull() throws DaoException{
		incomeCatDao.deactivate(2);
	}
	
	@Test
	public void testUpdate()throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		User user = userDao.read(1);
		incomeCat.setCategoryName("new");
		incomeCat.setIsActive(true);
		incomeCat.setUser(user);
		incomeCatDao.insert(incomeCat);
		incomeCat = incomeCatDao.read(incomeCat.getId());
		incomeCat.setCategoryName("updated");
		incomeCatDao.update(incomeCat);
		IncomeCategory updated = incomeCatDao.read(incomeCat.getId());
		assertEquals("updated", updated.getCategoryName());
		incomeCatDao.delete(updated.getId());
	}
	
	@AfterClass
	public void closeRes() throws DaoException{
		cleaner.close();
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
