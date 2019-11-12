package ru.mycash.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.mycash.dao.DaoException;
import ru.mycash.dao.MySqlBudgetDao;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.dao.MySqlExpenseDao;
import ru.mycash.dao.MySqlIncomeCategoryDao;
import ru.mycash.dao.MySqlIncomeDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.domain.Count;
import ru.mycash.domain.Expense;
import ru.mycash.domain.ExpenseCategory;
import ru.mycash.domain.Income;
import ru.mycash.domain.IncomeCategory;
import ru.mycash.domain.User;



public class MyCashMainService {

	@Autowired
	private MySqlIncomeDao incomeDao;
	@Autowired
	private MySqlUserDao userDao;
	@Autowired
	private MySqlExpenseDao expenseDao;
	@Autowired
	private MySqlExpenseCategoryDao expenseCategoryDao;
	@Autowired
	private MySqlIncomeCategoryDao incomeCategoryDao;
	@Autowired
	private MySqlCountDao countDao;
	@Autowired
	private MySqlBudgetDao budgetDao;
	
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED,
			readOnly = true)
	public User getUserByLogin(String login) throws DaoException{
		return userDao.getByLogin(login);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insertUser(User user) throws DaoException {
		userDao.insert(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void updateUser(User user) throws DaoException {
		userDao.update(user);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, 
			readOnly = true)
	public HashMap<String, ArrayList<?>> getDataForIncomesPage(Integer userId, 
			String startDate, String endDate) throws DaoException{
		HashMap<String, ArrayList<?>> result = new HashMap<String, ArrayList<?>>();
		result.put("categories", incomeCategoryDao.getAllActive(userId));
		result.put("counts", countDao.getAllActive(userId));
		result.put("incomes", incomeDao.getAllForPeriod(startDate, endDate, userId));
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void addIncome(int userId, String incomeDate, String categoryName, String annotation, 
			String amountStr, String countName, User loginedUser) throws DaoException, ParseException{
		Income income = new Income();
		IncomeCategory incomeCategory = incomeCategoryDao.getByName(userId, categoryName);
		Count count = countDao.getByName(countName, userId);
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern("yyyy-MM-dd"); 
		Date date = format.parse(incomeDate);
		Double amount = Double.parseDouble(amountStr);
		income.setAnnotation(annotation);
		income.setIncomeCategory(incomeCategory);
		income.setAmount(amount);
		income.setCount(count);
		income.setIncDate(date);
		income.setUser(loginedUser);
		incomeDao.insert(income);
		Double oldAmount = count.getBalance();
		count.setBalance(oldAmount + amount);
		countDao.update(count);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void deleteIncome(int incomeId) throws DaoException {
		incomeDao.deactivate(incomeId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void addExpense(int userId, String expenseDate, String categoryName, String annotation, 
			String amountStr, String countName, User loginedUser) throws DaoException, ParseException{
		Expense expense = new Expense();
		ExpenseCategory expenseCategory = expenseCategoryDao.getByName(userId, categoryName);
		Count count = countDao.getByName(countName, userId);
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern("yyyy-MM-dd"); 
		Date date = format.parse(expenseDate);
		Double amount = Double.parseDouble(amountStr);
		expense.setAnnotation(annotation);
		expense.setExpenseCategory(expenseCategory);
		expense.setAmount(amount);
		expense.setCount(count);
		expense.setExpenseDate(date);
		expense.setUser(loginedUser);
		expenseDao.insert(expense);
		Double oldAmount = count.getBalance();
		count.setBalance(oldAmount - amount);
		countDao.update(count);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void deleteExpense(int expenseId) throws DaoException {
		expenseDao.deactivate(expenseId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, 
			readOnly = true)
	public HashMap<String, ArrayList<?>> getDataForExpensePage(Integer userId, String startDate, 
			String endDate) throws DaoException{
		HashMap<String, ArrayList<?>> result = new HashMap<String, ArrayList<?>>();
		result.put("categories", expenseCategoryDao.getAllActive(userId));
		result.put("counts", countDao.getAllActive(userId));
		result.put("expenses", expenseDao.getAllForPeriod(startDate, endDate, userId));
		return result;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED,
			readOnly = true)
	public ArrayList<Count> getDataForCountPage(int userId) throws DaoException{
		return countDao.getAllActive(userId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void deleteCount(int countId) throws DaoException {
		countDao.deactivate(countId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insertCount(String currency, String balance, String countName, 
			User loginedUser) throws DaoException{
		Count count = new Count();
		count.setCountName(countName);
		count.setBalance(Double.parseDouble(balance));
		count.setCurrency(currency);
		count.setUser(loginedUser);
		countDao.insert(count);
	}
}
