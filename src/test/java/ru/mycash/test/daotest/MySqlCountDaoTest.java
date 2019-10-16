package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;
import java.util.ArrayList;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.dao.DaoException;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.Count;
import ru.mycash.domain.User;
import ru.mycash.test.util.TestDbCleaner;


public class MySqlCountDaoTest {
	
	private MySqlDaoFactory factory = null;
	private MySqlCountDao countDao = null;
	private TestDbCleaner cleaner = null;
	private MySqlUserDao userDao = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		countDao = factory.getMySqlCountDao();
		userDao = factory.getMySqlUserDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanCounts();
	}
	
	@Test
	public void testRead() throws DaoException{
		Count count = countDao.read(1);
		assertEquals("testCount", count.getCountName());
		assertEquals(1213.11, count.getBalance());
		assertEquals("BYN", count.getCurrency());
		assertEquals((Integer)1, count.getId());
		assertEquals((Boolean)true, count.getIsActive());
		assertEquals((Integer)1, count.getUser().getId());
	}
	
	@Test
	public void testReadNull() throws DaoException{
		Count count = countDao.read(2);
		assertNull(count);
	}	
	
	@Test
	public void testUpdate() throws DaoException{
		User user = userDao.read(1);
		Count count = new Count();
		count.setCountName("test");
		count.setBalance(45.52);
		count.setIsActive(true);
		count.setCurrency("USD");
		count.setUser(user);
		countDao.insert(count);
		count.setCurrency("BYN");
		countDao.update(count);
		assertEquals("BYN", count.getCurrency());
		countDao.delete(count.getId());
	}
	
	@Test
	public void testGetByName() throws DaoException{
		Count count = countDao.getByName("testCount", 1);
		assertEquals("testCount", count.getCountName());
		assertEquals(1213.11, count.getBalance());
		assertEquals("BYN", count.getCurrency());
		assertEquals((Integer)1, count.getId());
		assertEquals((Boolean)true, count.getIsActive());
		assertEquals((Integer)1, count.getUser().getId());
	}
	
	@Test
	public void testGetByNameNull() throws DaoException{
		Count count = countDao.getByName("testNull", 1);
		assertNull(count);
	}
	
	@Test
	public void testInsert() throws DaoException{
		Count count = new Count();
		User user = userDao.read(1);
		count.setCountName("test");
		count.setBalance(45.52);
		count.setIsActive(true);
		count.setCurrency("USD");
		count.setUser(user);
		countDao.insert(count);
		Count insertedCount = countDao.getByName("test", 1);
		assertEquals(count.getId(), insertedCount.getId());
		assertEquals("test", insertedCount.getCountName());
		assertEquals(45.52, insertedCount.getBalance());
		assertEquals((Boolean)true, insertedCount.getIsActive());
		assertEquals((Integer)1, insertedCount.getUser().getId());
		countDao.delete(insertedCount.getId());
	}
	
	@Test
	public void testDeactivate() throws DaoException {
		Count count = new Count();
		User user = userDao.read(1);
		count.setCountName("test");
		count.setBalance(45.52);
		count.setIsActive(true);
		count.setCurrency("USD");
		count.setUser(user);
		countDao.insert(count);
		countDao.deactivate(count.getId());
		count = countDao.read(count.getId());
		assertEquals((Boolean)false, count.getIsActive());
		countDao.delete(count.getId());
	}
	
	@Test (expectedExceptions = DaoException.class)
	public void testDeactivateNull() throws DaoException{
		countDao.deactivate(2);
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int countArraySize) throws DaoException{
		ArrayList<Count> all = countDao.getAll(userId);
		assertEquals((Integer)countArraySize, (Integer)all.size());
	}
	
	@Test(dataProvider = "getGetAllActiveData")
	public void testGetAllActive(int userId, int countArraySize) throws DaoException{
		Count count = new Count();
		User user = userDao.read(1);
		count.setCountName("test");
		count.setBalance(45.52);
		count.setIsActive(true);
		count.setCurrency("USD");
		count.setUser(user);
		countDao.insert(count);
		countDao.deactivate(count.getId());
		ArrayList<Count> allActive = countDao.getAllActive(userId);
		assertEquals((Integer)countArraySize, (Integer)allActive.size());
		countDao.delete(count.getId());
	}
	
	@Test
	public void testDelete() throws DaoException{
		int countId;
		Count count = new Count();
		User user = userDao.read(1);
		count.setCountName("test");
		count.setBalance(45.52);
		count.setIsActive(true);
		count.setCurrency("USD");
		count.setUser(user);
		countDao.insert(count);
		countId = count.getId();
		countDao.delete(countId);
		assertNull(countDao.read(countId));
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


