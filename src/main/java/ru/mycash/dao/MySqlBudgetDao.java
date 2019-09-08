package ru.mycash.dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;

import ru.mycash.domain.BudgetEntry;

public class MySqlBudgetDao {

	private Connection connection;
	private static final String queryForInsert = "insert into mycash_db.budget "
			+ "(amount, start_date, end_date, expense_category_id, user_id) "
			+ "values (?, ?, ?, ?, ?)";
	private static final String queryForRead = "select id, amount, start_date, end_date, expense_category_id, user_id "
			+ "from mycash_db.budget where id = ? ";
	private static final String queryForUpdate = "update mycash_db.budget set "
			+ "amount = ?, start_date = ?, end_date = ?, expense_category_id = ?, user_id = ? where id = ?";
	private static final String queryForGetAll = "select id, amount, start_date, end_date, expense_category_id, user_id "
			+ "from mycash_db.budget where user_id = ? ";
	
	
	private PreparedStatement statementForInsert;
	private PreparedStatement statementForRead;
	private PreparedStatement statementForUpdate;
	private PreparedStatement statementForGetAll;
	private PreparedStatement stmtForGetAllForPeriod;

	public MySqlBudgetDao() throws DaoException{
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
			stmtForGetAllForPeriod = connection.prepareStatement(queryForGetAll + " and start_date > ? and end_date < ?");
		}
		catch(SQLException | IOException | NullPointerException e){
			throw new DaoException("Failed to create MySqlBudgetDao object", e);
		}
	}
	
	public void insert (BudgetEntry budget) throws DaoException{
		ResultSet rSet = null;
		try {
			java.sql.Date sqlStartDate = new java.sql.Date(budget.getStartDate().getTime());
			java.sql.Date sqlEndDate = new java.sql.Date(budget.getEndDate().getTime());
			statementForInsert.setDouble(1, budget.getAmount());
			statementForInsert.setDate(2, sqlStartDate);
			statementForInsert.setDate(3, sqlEndDate);
			statementForInsert.setInt(4, budget.getExpenseCatId());
			statementForInsert.setInt(5, budget.getUserId());
			statementForInsert.executeUpdate();
			rSet = statementForInsert.getGeneratedKeys();
			Integer recordId = null;
			if (rSet.next()) {
				recordId = rSet.getInt(1);
				budget.setId(recordId);
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to insert budget entry", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public BudgetEntry read(int id) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForRead.setInt(1, id);
			rSet = statementForRead.executeQuery();
			if(rSet.next()) {
				BudgetEntry budget = new BudgetEntry();
				budget.setId(rSet.getInt("id"));
				budget.setAmount(rSet.getDouble("amount"));
				budget.setStartDate(rSet.getDate("start_date"));
				budget.setEndDate(rSet.getDate("end_date"));
				budget.setExpenseCatId(rSet.getInt("expense_category_id"));
				budget.setUserId(rSet.getInt("user_id"));
				return budget;
			} else {
				return null;
			}

		}
		catch (SQLException e){
			throw new DaoException("Failed to read budget entry", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void update(BudgetEntry budget) throws DaoException{
		try{
			java.sql.Date sqlStartDate = new java.sql.Date(budget.getStartDate().getTime());
			java.sql.Date sqlEndDate = new java.sql.Date(budget.getEndDate().getTime());
			statementForUpdate.setDouble(1, budget.getAmount());
			statementForUpdate.setDate(2, sqlStartDate);
			statementForUpdate.setDate(3, sqlEndDate);
			statementForUpdate.setInt(4, budget.getExpenseCatId());
			statementForUpdate.setInt(5, budget.getUserId());
			statementForUpdate.setInt(6, budget.getId());
			statementForUpdate.executeUpdate();
		}
		catch (SQLException | NullPointerException e){
			throw new DaoException("Failed to update budget entry ", e);
		}
	}
	
	public ArrayList<BudgetEntry> getAll(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<BudgetEntry> result = new ArrayList<>();
			statementForGetAll.setInt(1, userId);
			rSet = statementForGetAll.executeQuery();
			while (rSet.next()){
				BudgetEntry budget = new BudgetEntry();
				budget.setId(rSet.getInt("id"));
				budget.setAmount(rSet.getDouble("amount"));
				budget.setStartDate(rSet.getDate("start_date"));
				budget.setEndDate(rSet.getDate("end_date"));
				budget.setExpenseCatId(rSet.getInt("expense_category_id"));
				budget.setUserId(rSet.getInt("user_id"));
				result.add(budget);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get all budget entries", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ArrayList<BudgetEntry> getAllForPeriod(java.util.Date startDate,
			java.util.Date endDate,int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<BudgetEntry> result = new ArrayList<>();
			String formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
			String formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
			stmtForGetAllForPeriod.setInt(1, userId);
			stmtForGetAllForPeriod.setString(2, formattedStartDate);
			stmtForGetAllForPeriod.setString(3, formattedEndDate);
			rSet = stmtForGetAllForPeriod.executeQuery();
			while (rSet.next()){
				BudgetEntry budget = new BudgetEntry();
				budget.setId(rSet.getInt("id"));
				budget.setAmount(rSet.getDouble("amount"));
				budget.setStartDate(rSet.getDate("start_date"));
				budget.setEndDate(rSet.getDate("end_date"));
				budget.setExpenseCatId(rSet.getInt("expense_category_id"));
				budget.setUserId(rSet.getInt("user_id"));
				result.add(budget);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get all budget entries for period", e);
		}
		finally{
			closeResultSet(rSet);
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
