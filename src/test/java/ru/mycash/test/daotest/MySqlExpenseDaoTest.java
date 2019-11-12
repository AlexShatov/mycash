package ru.mycash.test.daotest;

import static org.testng.AssertJUnit.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import ru.mycash.dao.MySqlExpenseDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.AppCtxConfig;
import ru.mycash.dao.DaoException;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.domain.Count;
import ru.mycash.domain.Expense;
import ru.mycash.domain.ExpenseCategory;
import ru.mycash.domain.User;

@WebAppConfiguration
@ContextConfiguration(classes=AppCtxConfig.class)
public class MySqlExpenseDaoTest extends AbstractTransactionalTestNGSpringContextTests{
	
	@Autowired
	private MySqlExpenseDao expenseDao;
	@Autowired
	private MySqlUserDao userDao;
	@Autowired
	private MySqlCountDao countDao;
	@Autowired
	private MySqlExpenseCategoryDao expenseCatDao;
	
	@Test
	public void testRead() throws DaoException, ParseException{
		Expense expense = expenseDao.read(1);
		assertEquals("testExpense", expense.getAnnotation());
		assertEquals((Boolean)true, expense.getIsActive());
		assertEquals(156.21, expense.getAmount());
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 02, 23");
		assertEquals(date, expense.getExpenseDate());
		assertEquals((Integer)1, expense.getUser().getId());
		assertEquals((Integer)1, expense.getCount().getId());
		assertEquals((Integer)1, expense.getExpenseCategory().getId()); 		
	}
	
	@Test
	@Transactional
	public void testInsert() throws DaoException, ParseException{
		Expense expense = new Expense();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		expense.setAnnotation("new");
		expense.setIsActive(true);
		expense.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		expense.setExpenseDate(date);
		expense.setUser(user);
		expense.setCount(count);
		expense.setExpenseCategory(expenseCat);
		expenseDao.insert(expense);
		Expense insertedExpense = expenseDao.read(expense.getId());
		assertEquals("new", insertedExpense.getAnnotation());
		assertEquals((Integer)1, insertedExpense.getUser().getId());
		assertEquals((Integer)1, insertedExpense.getCount().getId());
		assertEquals((Integer)1, insertedExpense.getExpenseCategory().getId());
		assertEquals((Boolean)true, insertedExpense.getIsActive());
		assertEquals(date, insertedExpense.getExpenseDate());
		expenseDao.delete(expense.getId());
	}
	
	@Test
	@Transactional
	public void testUpdate()throws DaoException, ParseException{
		Expense expense = new Expense();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		expense.setAnnotation("new");
		expense.setIsActive(true);
		expense.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		expense.setExpenseDate(date);
		expense.setUser(user);
		expense.setCount(count);
		expense.setExpenseCategory(expenseCat);
		expenseDao.insert(expense);
		expense.setAnnotation("updated");
		expenseDao.update(expense);
		Expense updated = expenseDao.read(expense.getId());
		assertEquals("updated", updated.getAnnotation());
		expenseDao.delete(updated.getId());
	}
	@Test
	@Transactional
	public void testDelete() throws DaoException, ParseException{
		Expense expense = new Expense();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		expense.setAnnotation("new");
		expense.setIsActive(true);
		expense.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		expense.setExpenseDate(date);
		expense.setUser(user);
		expense.setCount(count);
		expense.setExpenseCategory(expenseCat);
		expenseDao.insert(expense);
		int expenseId = expense.getId(); 
		expenseDao.delete(expenseId);
		assertNull(expenseDao.read(expenseId));
	}
	
	@Test(dataProvider = "getGetAllData")
	public void testGetAll(int userId, int ArraySize) throws DaoException{
		ArrayList<Expense> all = expenseDao.getAll(userId);
		assertEquals((Integer)ArraySize, (Integer)all.size());
	}
	
	@Test(dataProvider = "getGetAllActiveData")
	@Transactional
	public void testGetAllActive(int userId, int ArraySize) throws DaoException, ParseException{
		Expense expense = new Expense();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		expense.setAnnotation("new");
		expense.setIsActive(true);
		expense.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		expense.setExpenseDate(date);
		expense.setUser(user);
		expense.setCount(count);
		expense.setExpenseCategory(expenseCat);
		expenseDao.insert(expense);
		expenseDao.deactivate(expense.getId());
		ArrayList<Expense> allActive = expenseDao.getAllActive(userId);
		assertEquals((Integer)ArraySize, (Integer)allActive.size());
		expenseDao.delete(expense.getId());
	}
	
	@Test
	@Transactional
	public void testDeactivate() throws DaoException, ParseException {
		Expense expense = new Expense();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		expense.setAnnotation("new");
		expense.setIsActive(true);
		expense.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		expense.setExpenseDate(date);
		expense.setUser(user);
		expense.setCount(count);
		expense.setExpenseCategory(expenseCat);
		expenseDao.insert(expense);
		expenseDao.deactivate(expense.getId());
		expense = expenseDao.read(expense.getId());
		assertEquals((Boolean)false, expense.getIsActive());
		expenseDao.delete(expense.getId());
	}
	
	@Test
	@Transactional
	public void testGetAllForPeriod() throws DaoException, ParseException{
		Expense expense = new Expense();
		User user = userDao.read(1);
		Count count = countDao.read(1);
		ExpenseCategory expenseCat = expenseCatDao.read(1);
		expense.setAnnotation("new");
		expense.setIsActive(true);
		expense.setAmount(122.02);
		SimpleDateFormat format = new SimpleDateFormat("yyyy, MM, dd");
		Date date = format.parse("2019, 04, 23");
		expense.setExpenseDate(date);
		expense.setUser(user);
		expense.setCount(count);
		expense.setExpenseCategory(expenseCat);
		expenseDao.insert(expense);
		ArrayList<Expense> incomeForPeriod = expenseDao.getAllForPeriod("20190420", "20190425", 1);
		assertEquals(1, incomeForPeriod.size());
		expenseDao.delete(expense.getId());
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
