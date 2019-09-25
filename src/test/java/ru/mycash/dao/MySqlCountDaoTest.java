package ru.mycash.dao;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;
import java.util.ArrayList;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.Count;


public class MySqlCountDaoTest {
	
	private MySqlDaoFactory factory = null;
	private MySqlCountDao countDao = null;
	private Count insertedCount = null;
	
	public MySqlCountDaoTest() throws DaoException{
		factory = new MySqlDaoFactory();
	}

	@Test (priority = 1)
	public void testInit() throws DaoException{
		countDao = factory.getMySqlCountDao();
		assertNotNull(countDao);
	}
	
	@Test(priority = 2, dependsOnMethods = {"testInit"})
	public void testRead() throws DaoException{
		Count count = countDao.read(1);
		assertEquals("testCount", count.getCountName());
		assertEquals(1213.11, count.getBalance());
		assertEquals("BYN", count.getCurrency());
		assertEquals((Integer)1, count.getId());
		assertEquals((Boolean)true, count.getIsActive());
		assertEquals((Integer)1, count.getUserId());
	}
	
	@Test(priority = 3, dependsOnMethods = {"testInit"})
	public void testReadNull() throws DaoException{
		Count count = countDao.read(2);
		assertNull(count);
	}	
	
	@Test(priority = 4, dependsOnMethods = {"testInit"})
	public void testGetByName() throws DaoException{
		Count count = countDao.getByName("testCount", 1);
		assertEquals("testCount", count.getCountName());
		assertEquals(1213.11, count.getBalance());
		assertEquals("BYN", count.getCurrency());
		assertEquals((Integer)1, count.getId());
		assertEquals((Boolean)true, count.getIsActive());
		assertEquals((Integer)1, count.getUserId());
	}
	
	@Test(priority = 5, dependsOnMethods = {"testInit"})
	public void testGetByNameNull() throws DaoException{
		Count count = countDao.getByName("testNull", 1);
		assertNull(count);
	}
	
	@Test(priority = 6, dependsOnMethods = {"testGetByName"})
	public void testInsert() throws DaoException{
		Count count = new Count();
		count.setCountName("test");
		count.setBalance(45.52);
		count.setCurrency("USD");
		count.setUserId(1);
		countDao.insert(count);
		insertedCount = countDao.getByName("test", 1);
		assertEquals(count.getId(), insertedCount.getId());
		assertEquals("test", insertedCount.getCountName());
		assertEquals(45.52, insertedCount.getBalance());
		assertEquals((Boolean)true, insertedCount.getIsActive());
		assertEquals((Integer)1, insertedCount.getUserId());
	}
	
	@Test(priority = 7, dependsOnMethods = {"testRead", "testInsert"})
	public void testDeactivate() throws DaoException {
		int insertedId = insertedCount.getId();
		countDao.deactivate(insertedId);
		Count count = countDao.read(insertedId);
		assertEquals((Boolean)false, count.getIsActive());
	}
	
	@Test(priority = 8, dependsOnMethods = {"testInsert"},
			dataProvider = "getGetAllData")
	public void testGetAll(int userId, int countArraySize) throws DaoException{
		ArrayList<Count> all = countDao.getAll(userId);
		assertEquals((Integer)countArraySize, (Integer)all.size());
	}
	
	@Test(priority = 9, dependsOnMethods = {"testDeactivate"},
			dataProvider = "getGetAllActiveData")
	public void testGetAllActive(int userId, int countArraySize) throws DaoException{
		ArrayList<Count> allActive = countDao.getAllActive(userId);
		assertEquals((Integer)countArraySize, (Integer)allActive.size());
	}
	
	@Test(priority = 10, dependsOnMethods = {"testInsert"})
	public void testDelete() throws DaoException{
		int insertedId = insertedCount.getId();
		countDao.delete(insertedId);
		assertNull(countDao.read(insertedId));
	}
	

	@Test(priority = 11, dependsOnMethods = {"testInit"})
	public void testClose(){
		try {
			countDao.close();
		}catch(DaoException e) {
			fail("testClose() failed with exception");
		}
	}
	
	@DataProvider
	public Object[][] getGetAllData(){
		return new Object[][] {{1, 2}, {2, 0}};
	}
	
	@DataProvider
	public Object[][] getGetAllActiveData(){
		return new Object[][] {{1, 1}, {2, 0}};
	}
}


