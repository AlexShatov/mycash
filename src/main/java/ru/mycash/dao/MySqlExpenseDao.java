package ru.mycash.dao;

import java.sql.*;
import java.io.*;
import java.util.*;

import ru.mycash.domain.Expense;

import java.text.*;

public class MySqlExpenseDao{
	
	private Connection connection;
	private static final String queryForInsert = "insert into mycash_db.expenses "
			+ "(amount, annotation, exp_date, is_active, expense_category_id, count_id, user_id) "
			+ "values (?, ?, ?, true, ?, ?, ?)";
	private static final String queryForRead = "select id, amount, annotation, exp_date, is_active,"  
			+ " expense_category_id, count_id, user_id from mycash_db.expenses where id = ?";
	private static final String queryForUpdate = "update mycash_db.expenses set"
			+ " amount = ?, annotation = ?, exp_date = ?, is_active = ?, expense_category_id = ?,"
			+ " count_id = ?, user_id = ? where id = ?";
	private static final String queryForGetAll = "select id, amount, annotation, exp_date, is_active,"
			+ " expense_category_id, count_id, user_id from mycash_db.expenses where user_id = ? ";
	private final String queryForDeactivate  = "update mycash_db.expenses set is_active = false where id = ?";
	private PreparedStatement statementForInsert;
	private PreparedStatement statementForRead;
	private PreparedStatement statementForUpdate;
	private PreparedStatement statementForGetAll;
	private PreparedStatement statementForGetAllActive;
	private PreparedStatement stmtForGetAllForPeriod;
	private PreparedStatement stmtForDeactivate;
	
	public MySqlExpenseDao() throws DaoException{
		try(InputStream input = MySqlExpenseDao.class.getClassLoader().getResourceAsStream("/mycash_db.properties")){
			Properties properties = new Properties();
			properties.load(input);
			String url = properties.getProperty("url");
			String username = properties.getProperty("user");
			String password = properties.getProperty("password");
			connection = DriverManager.getConnection(url, username, password);
			statementForInsert = connection.prepareStatement(queryForInsert, Statement.RETURN_GENERATED_KEYS);
			statementForRead = connection.prepareStatement(queryForRead);
			statementForUpdate = connection.prepareStatement(queryForUpdate);
			statementForGetAll = connection.prepareStatement(queryForGetAll);
			statementForGetAllActive = connection.prepareStatement(queryForGetAll + "and is_active = true");
			stmtForGetAllForPeriod = connection.prepareStatement(queryForGetAll + "and exp_date between ? and ?");
			stmtForDeactivate = connection.prepareStatement(queryForDeactivate);
		}
		catch(SQLException | IOException | NullPointerException e){
			throw new DaoException("Failed to create MySqlExpenseDao object", e);
		}
	}
	
	public void insert (Expense expense) throws DaoException{
		ResultSet rSet = null;
		try {
			java.sql.Date sqlDate = new java.sql.Date(expense.getExpenseDate().getTime());
			statementForInsert.setDouble(1, expense.getAmount());
			statementForInsert.setString(2, expense.getAnnotation());
			statementForInsert.setDate(3, sqlDate);
			statementForInsert.setInt(4, expense.getExpenseCatId());
			statementForInsert.setInt(5, expense.getCountId());
			statementForInsert.setInt(6, expense.getUserId());
			statementForInsert.executeUpdate();
			rSet = statementForInsert.getGeneratedKeys();
			Integer recordId = null;
			if (rSet.next()) {
				recordId = rSet.getInt(1);
				expense.setId(recordId);
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to insert expense", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public Expense read(int id) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForRead.setInt(1, id);
			rSet = statementForRead.executeQuery();
			if(rSet.next()) {
				Expense expense = new Expense();
				expense.setId(rSet.getInt("id"));
				expense.setAmount(rSet.getDouble("amount"));
				expense.setAnnotation(rSet.getString("annotation"));
				expense.setExpenseDate(rSet.getDate("exp_date"));
				expense.setIsActive(rSet.getBoolean("is_active"));
				expense.setExpenseCatId(rSet.getInt("expense_category_id"));
				expense.setCountId(rSet.getInt("count_id"));
				expense.setUserId(rSet.getInt("user_id"));
				return expense;
			} else {
				return null;
			}

		}
		catch (SQLException e){
			throw new DaoException("Failed to read expense", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void update(Expense expense) throws DaoException{
		try{
			java.sql.Date sqlDate = new java.sql.Date(expense.getExpenseDate().getTime());
			statementForUpdate.setDouble(1, expense.getAmount());
			statementForUpdate.setString(2, expense.getAnnotation());
			statementForUpdate.setDate(3, sqlDate);
			statementForUpdate.setBoolean(4, expense.getIsActive());
			statementForUpdate.setInt(5, expense.getExpenseCatId());
			statementForUpdate.setInt(6, expense.getCountId());
			statementForUpdate.setInt(7, expense.getUserId());
			statementForUpdate.setInt(8, expense.getId());
			statementForUpdate.executeUpdate();
		}
		catch (SQLException | NullPointerException e){
			throw new DaoException("Failed to update expense ", e);
		}
	}
	
	public ArrayList<Expense> getAll(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Expense> result = new ArrayList<>();
			statementForGetAll.setInt(1, userId);
			rSet = statementForGetAll.executeQuery();
			while (rSet.next()){
				Expense expense = new Expense();
				expense.setId(rSet.getInt("id"));
				expense.setAmount(rSet.getDouble("amount"));
				expense.setAnnotation(rSet.getString("annotation"));
				expense.setExpenseDate(rSet.getDate("exp_date"));
				expense.setIsActive(rSet.getBoolean("is_active"));
				expense.setExpenseCatId(rSet.getInt("expense_category_id"));
				expense.setCountId(rSet.getInt("count_id"));
				expense.setUserId(rSet.getInt("user_id"));
				result.add(expense);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get all expenses", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ArrayList<Expense> getAllActive(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Expense> result = new ArrayList<>();
			statementForGetAllActive.setInt(1, userId);
			rSet = statementForGetAllActive.executeQuery();
			while (rSet.next()){
				Expense expense = new Expense();
				expense.setId(rSet.getInt("id"));
				expense.setAmount(rSet.getDouble("amount"));
				expense.setAnnotation(rSet.getString("annotation"));
				expense.setExpenseDate(rSet.getDate("exp_date"));
				expense.setIsActive(rSet.getBoolean("is_active"));
				expense.setExpenseCatId(rSet.getInt("expense_category_id"));
				expense.setCountId(rSet.getInt("count_id"));
				expense.setUserId(rSet.getInt("user_id"));
				result.add(expense);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get all active expenses", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ArrayList<Expense> getAllForPeriod(String startDate, 
			String endDate, int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Expense> result = new ArrayList<>();
			stmtForGetAllForPeriod.setInt(1, userId);
			stmtForGetAllForPeriod.setString(2, startDate);
			stmtForGetAllForPeriod.setString(3, endDate);
			rSet = stmtForGetAllForPeriod.executeQuery();
			while (rSet.next()){
				Expense expense = new Expense();
				expense.setId(rSet.getInt("id"));
				expense.setAmount(rSet.getDouble("amount"));
				expense.setAnnotation(rSet.getString("annotation"));
				expense.setExpenseDate(rSet.getDate("exp_date"));
				expense.setIsActive(rSet.getBoolean("is_active"));
				expense.setExpenseCatId(rSet.getInt("expense_category_id"));
				expense.setCountId(rSet.getInt("count_id"));
				expense.setUserId(rSet.getInt("user_id"));
				result.add(expense);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get expenses for period", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void deactivate(int expenseId) throws DaoException{
		try {
			stmtForDeactivate.setInt(1, expenseId);
			stmtForDeactivate.executeUpdate();
		} 
		catch(SQLException e) {
			throw new DaoException("Failed to deactivate income", e);
		}
		
	}
	
	public void close() throws DaoException{
		DaoException exception = new DaoException("MySqlExpenseDao has been closed with exception(s)");
		if(statementForInsert != null){
			try{
				statementForInsert.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForUpdate != null){
			try{
				statementForUpdate.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForRead != null){
			try{
				statementForRead.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForGetAll != null){
			try{
				statementForGetAll.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForGetAllActive != null){
			try{
				statementForGetAllActive.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(stmtForGetAllForPeriod != null){
			try{
				stmtForGetAllForPeriod.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(connection != null){
			try{
				connection.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		Throwable[] suppressed = exception.getSuppressed();
		if(suppressed.length > 0){
			throw exception;
		} 
	}
	
	private void closeResultSet(ResultSet rSet) throws DaoException{
		if (rSet != null){
			try{
				rSet.close();
			}
			catch (SQLException e){
				throw new DaoException("Unable to close ResultSet", e);
			}
		}
	}
}