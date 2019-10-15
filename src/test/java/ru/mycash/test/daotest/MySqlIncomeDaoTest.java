package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ru.mycash.dao.MySqlIncomeDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.dao.DaoException;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.MySqlIncomeCategoryDao;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.Count;
import ru.mycash.domain.Income;
import ru.mycash.domain.IncomeCategory;
import ru.mycash.domain.User;
import ru.mycash.test.util.TestDbCleaner;

public class MySqlIncomeDaoTest {
	
	private MySqlDaoFactory factory = null;
	private MySqlIncomeDao incomeDao = null;
	private TestDbCleaner cleaner = null;
	private MySqlUserDao userDao = null;
	private MySqlCountDao countDao = null;
	private MySqlIncomeCategoryDao incomeCatDao = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		incomeDao = factory.getMySqlIncomeDao();
		userDao = factory.getMySqlUserDao();
		countDao = factory.getMySqlCountDao();
		incomeCatDao = factory.getMySqlIncomeCategoryDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanIncomes();
	}
	
	@Test
	public void testRead() throws DaoException, ParseException{
		Income income = incomeDao.read(1);
		assertEquals("testIncome", income.getAnnotation());
		assertEquals((Boolean)true, income.getIsActive());
		assertEquals(156.21, income.getAmount());
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 05, 23");
		assertEquals(date, income.getIncDate());
		assertEquals((Integer)1, income.getUser().getId());
		assertEquals((Integer)1, income.getCount().getId());
		assertEquals((Integer)1, income.getIncomeCategory().getId()); 		
	}
	
	@Test
	public void testInsert() throws DaoException, ParseException{
		Income income = new Income();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		IncomeCategory incomeCat = incomeCatDao.read(1);
		income.setAnnotation("new");
		income.setIsActive(true);
		income.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		income.setIncDate(date);
		income.setUser(user);
		income.setCount(count);
		income.setIncomeCategory(incomeCat);
		incomeDao.insert(income);
		Income insertedIncome = incomeDao.read(income.getId());
		assertEquals("new", insertedIncome.getAnnotation());
		assertEquals((Integer)1, insertedIncome.getUser().getId());
		assertEquals((Integer)1, insertedIncome.getCount().getId());
		assertEquals((Integer)1, insertedIncome.getIncomeCategory().getId());
		assertEquals((Boolean)true, insertedIncome.getIsActive());
		assertEquals(date, insertedIncome.getIncDate());
		incomeDao.delete(income.getId());
	}
	
	@Test
	public void testUpdate()throws DaoException, ParseException{
		Income income = new Income();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		IncomeCategory incomeCat = incomeCatDao.read(1);
		income.setAnnotation("new");
		income.setIsActive(true);
		income.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		income.setIncDate(date);
		income.setUser(user);
		income.setCount(count);
		income.setIncomeCategory(incomeCat);
		incomeDao.insert(income);
		income.setAnnotation("updated");
		incomeDao.update(income);
		Income updated = incomeDao.read(income.getId());
		assertEquals("updated", updated.getAnnotation());
		incomeDao.delete(updated.getId());
	}
	
	public void testDelete() throws DaoException, ParseException{
		Income income = new Income();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		IncomeCategory incomeCat = incomeCatDao.read(1);
		income.setAnnotation("new");
		income.setIsActive(true);
		income.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		income.setIncDate(date);
		income.setUser(user);
		income.setCount(count);
		income.setIncomeCategory(incomeCat);
		incomeDao.insert(income);
		int incomeId = income.getId(); 
		incomeDao.delete(incomeId);
		assertNull(incomeDao.read(incomeId));
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int ArraySize) throws DaoException{
		ArrayList<Income> all = incomeDao.getAll(userId);
		assertEquals((Integer)ArraySize, (Integer)all.size());
	}
	
	@Test(dataProvider = "getGetAllActiveData")
	public void testGetAllActive(int userId, int ArraySize) throws DaoException, ParseException{
		Income income = new Income();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		IncomeCategory incomeCat = incomeCatDao.read(1);
		income.setAnnotation("new");
		income.setIsActive(true);
		income.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		income.setIncDate(date);
		income.setUser(user);
		income.setCount(count);
		income.setIncomeCategory(incomeCat);
		incomeDao.insert(income);
		incomeDao.deactivate(income.getId());
		ArrayList<Income> allActive = incomeDao.getAllActive(userId);
		assertEquals((Integer)ArraySize, (Integer)allActive.size());
		incomeDao.delete(income.getId());
	}
	
	@Test
	public void testDeactivate() throws DaoException, ParseException {
		Income income = new Income();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		IncomeCategory incomeCat = incomeCatDao.read(1);
		income.setAnnotation("new");
		income.setIsActive(true);
		income.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		income.setIncDate(date);
		income.setUser(user);
		income.setCount(count);
		income.setIncomeCategory(incomeCat);
		incomeDao.insert(income);
		incomeDao.deactivate(income.getId());
		income = incomeDao.read(income.getId());
		assertEquals((Boolean)false, income.getIsActive());
		incomeDao.delete(income.getId());
	}
	
	@Test
	public void testGetAllForPeriod() throws DaoException, ParseException{
		Income income = new Income();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		IncomeCategory incomeCat = incomeCatDao.read(1);
		income.setAnnotation("new");
		income.setIsActive(true);
		income.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		income.setIncDate(date);
		income.setUser(user);
		income.setCount(count);
		income.setIncomeCategory(incomeCat);
		incomeDao.insert(income);
		ArrayList<Income> incomeForPeriod = incomeDao.getAllForPeriod("20190420", "20190425", 1);
		assertEquals(1, incomeForPeriod.size());
		incomeDao.delete(income.getId());
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
