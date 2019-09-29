package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;
import java.util.ArrayList;
import ru.mycash.dao.MySqlIncomeCategoryDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.IncomeCategory;
import ru.mycash.test.util.TestDbCleaner;

public class MySqlIncomeCategoryDaoTest {

	private MySqlDaoFactory factory = null;
	private MySqlIncomeCategoryDao incomeCatDao = null;
	private TestDbCleaner cleaner = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		incomeCatDao = factory.getMySqlIncomeCategoryDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanIncomeCategories();
	}
	
	@Test 
	public void testInit() throws DaoException{
		MySqlIncomeCategoryDao sqlIncomeCatDao = factory.getMySqlIncomeCategoryDao();
		assertNotNull(sqlIncomeCatDao);
		sqlIncomeCatDao.close();
	}
	
	@Test
	public void testRead() throws DaoException{
		IncomeCategory incomeCat = incomeCatDao.read(1);
		assertEquals("testCategory", incomeCat.getCategoryName());
		assertEquals((Boolean)true, incomeCat.getIsActive());
		assertEquals((Integer)1, incomeCat.getUserId());
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
		assertEquals((Integer)1, incomeCat.getUserId());
	}
	
	@Test
	public void testGetByNameNull() throws DaoException{
		IncomeCategory incomeCat = incomeCatDao.getByName(2, "testCategory");
		assertNull(incomeCat);
	}
	
	@Test
	public void testInsert() throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		incomeCat.setCategoryName("new");
		incomeCat.setUserId(1);
		incomeCatDao.insert(incomeCat);
		IncomeCategory insertedIncomeCat = incomeCatDao.getByName(1, "new");
		assertEquals(incomeCat.getId(), insertedIncomeCat.getId());
		assertEquals("new", insertedIncomeCat.getCategoryName());
		assertEquals((Integer)1, insertedIncomeCat.getUserId());
		assertEquals((Boolean)true, insertedIncomeCat.getIsActive());
		incomeCatDao.delete(insertedIncomeCat.getId());
	}
	
	@Test
	public void testDelete() throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		incomeCat.setCategoryName("new");
		incomeCat.setUserId(1);
		incomeCatDao.insert(incomeCat);
		incomeCatDao.delete(incomeCat.getId());
		assertNull(incomeCatDao.read(incomeCat.getId()));
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int countArraySize) throws DaoException{
		ArrayList<IncomeCategory> all = incomeCatDao.getAll(userId);
		assertEquals((Integer)countArraySize, (Integer)all.size());
	}
	
	@Test(dataProvider = "getGetAllActiveData")
	public void testGetAllActive(int userId, int countArraySize) throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		incomeCat.setCategoryName("new");
		incomeCat.setUserId(1);
		incomeCatDao.insert(incomeCat);
		incomeCatDao.deactivate(incomeCat.getId());
		ArrayList<IncomeCategory> allActive = incomeCatDao.getAllActive(userId);
		assertEquals((Integer)countArraySize, (Integer)allActive.size());
		incomeCatDao.delete(incomeCat.getId());
	}
	
	@Test
	public void testDeactivate() throws DaoException {
		IncomeCategory incomeCat = new IncomeCategory();
		incomeCat.setCategoryName("new");
		incomeCat.setUserId(1);
		incomeCatDao.insert(incomeCat);
		incomeCatDao.deactivate(incomeCat.getId());
		incomeCat = incomeCatDao.read(incomeCat.getId());
		assertEquals((Boolean)false, incomeCat.getIsActive());
		incomeCatDao.delete(incomeCat.getId());
	}
	
	@Test
	public void testUpdate()throws DaoException{
		IncomeCategory incomeCat = new IncomeCategory();
		incomeCat.setCategoryName("new");
		incomeCat.setUserId(1);
		incomeCatDao.insert(incomeCat);
		incomeCat = incomeCatDao.read(incomeCat.getId());
		incomeCat.setCategoryName("updated");
		incomeCatDao.update(incomeCat);
		IncomeCategory updated = incomeCatDao.read(incomeCat.getId());
		assertEquals("updated", updated.getCategoryName());
		incomeCatDao.delete(updated.getId());
	}
	
	@Test
	public void testDeactivateNull() throws DaoException{
		incomeCatDao.deactivate(2);
		assertNull(incomeCatDao.read(2));
	}
	
	@Test
	public void testClose(){
		try {
			MySqlIncomeCategoryDao sqlIncomeCatDao = factory.getMySqlIncomeCategoryDao();
			assertNotNull(sqlIncomeCatDao);
			sqlIncomeCatDao.close();
		}catch(DaoException e) {
			fail("testClose() failed with exception");
		}
	}
	
	@AfterClass
	public void closeRes() throws DaoException{
		cleaner.close();
		incomeCatDao.close();
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
