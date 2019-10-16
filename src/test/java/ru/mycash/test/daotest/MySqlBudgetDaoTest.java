package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;
import org.testng.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ru.mycash.dao.MySqlBudgetDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.dao.DaoException;
import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.MySqlDaoFactory;
import ru.mycash.domain.BudgetEntry;
import ru.mycash.domain.ExpenseCategory;
import ru.mycash.domain.User;
import ru.mycash.test.util.TestDbCleaner;

public class MySqlBudgetDaoTest {
	
	private MySqlDaoFactory factory = null;
	private MySqlBudgetDao budgetDao = null;
	private TestDbCleaner cleaner = null;
	private MySqlUserDao userDao = null;
	private MySqlExpenseCategoryDao expenseCatDao = null;
	
	@BeforeClass
	public void initialize() throws DaoException{
		factory = new MySqlDaoFactory();
		budgetDao = factory.getMySqlBudgetDao();
		userDao = factory.getMySqlUserDao();
		expenseCatDao = factory.getMySqlExpenseCategoryDao();
		cleaner = new TestDbCleaner();
	}
	
	@BeforeMethod
	public void cleanDb() throws DaoException{
		cleaner.cleanBudget();
	}
	
	@Test
	public void testRead() throws DaoException, ParseException{
		BudgetEntry entry = budgetDao.read(1);
		assertEquals(156.21, entry.getAmount());
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date startDate = format.parse("2019, 02, 01");
		Date endDate = format.parse("2019, 03, 01");
		assertEquals(startDate, entry.getStartDate());
		assertEquals(endDate, entry.getEndDate());
		assertEquals((Integer)1, entry.getUser().getId());
		assertEquals((Integer)1, entry.getExpenseCategory().getId()); 		
	}
	
	@Test
	public void testInsert() throws DaoException, ParseException{
		BudgetEntry entry = new BudgetEntry();
		User user = userDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		entry.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date startDate = format.parse("2019, 04, 23");
		Date endDate = format.parse("2019, 05, 23");
		entry.setStartDate(startDate);
		entry.setEndDate(endDate);
		entry.setUser(user);
		entry.setExpenseCategory(expenseCat);
		budgetDao.insert(entry);
		BudgetEntry insertedEntry = budgetDao.read(entry.getId());
		assertEquals((Integer)1, insertedEntry.getUser().getId());
		assertEquals((Integer)1, insertedEntry.getExpenseCategory().getId());
		assertEquals(startDate, insertedEntry.getStartDate());
		assertEquals(endDate, insertedEntry.getEndDate());
		budgetDao.delete(insertedEntry.getId());
	}
	
	@Test
	public void testUpdate()throws DaoException, ParseException{
		BudgetEntry entry = new BudgetEntry();
		User user = userDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		entry.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date startDate = format.parse("2019, 04, 23");
		Date endDate = format.parse("2019, 05, 23");
		entry.setStartDate(startDate);
		entry.setEndDate(endDate);
		entry.setUser(user);
		entry.setExpenseCategory(expenseCat);
		budgetDao.insert(entry);
		entry.setAmount(100.00);
		budgetDao.update(entry);
		BudgetEntry updated = budgetDao.read(entry.getId());
		assertEquals(100.00, updated.getAmount());
		budgetDao.delete(updated.getId());
	}
	
	@Test
	public void testDelete() throws DaoException, ParseException{
		BudgetEntry entry = new BudgetEntry();
		User user = userDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		entry.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date startDate = format.parse("2019, 04, 23");
		Date endDate = format.parse("2019, 05, 23");
		entry.setStartDate(startDate);
		entry.setEndDate(endDate);
		entry.setUser(user);
		entry.setExpenseCategory(expenseCat);
		budgetDao.insert(entry);
		int entryId = entry.getId(); 
		budgetDao.delete(entryId);
		assertNull(budgetDao.read(entryId));
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int ArraySize) throws DaoException{
		ArrayList<BudgetEntry> all = budgetDao.getAll(userId);
		assertEquals((Integer)ArraySize, (Integer)all.size());
	}
	
	@DataProvider
	public Object[][] getGetAllData(){
		return new Object[][] {{1, 1}, {2, 0}};
	}
}

