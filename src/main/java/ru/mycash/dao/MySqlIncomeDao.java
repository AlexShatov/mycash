package ru.mycash.dao;

import java.sql.*;
import java.io.*;
import java.util.*;

import ru.mycash.domain.Income;

import java.text.*;

public class MySqlIncomeDao {

	private Connection connection;
	private static final String queryForInsert = "insert into mycash_db.incomes "
			+ "(amount, annotation, inc_date, is_active, income_category_id, count_id, user_id)" 
			+ " values (?, ?, ?, true, ?, ?, ?)";
	private static final String queryForRead = "select id, amount, annotation, inc_date, is_active," 
			+ " income_category_id, count_id, user_id from mycash_db.incomes where id = ?";
	private static final String queryForUpdate = "update mycash_db.incomes set amount = ?, annotation = ?," 
			+ " inc_date = ?, is_active = ?, income_category_id = ?, count_id = ?, user_id = ? where id = ?";
	private static final String queryForGetAll = "select id, amount, annotation, inc_date, is_active," 
			+ " income_category_id, count_id, user_id from mycash_db.incomes where user_id = ? ";	
	private static final String queryForDeactivate  = "update mycash_db.incomes set is_active = false where id = ?";
	private PreparedStatement statementForInsert;
	private PreparedStatement statementForRead;
	private PreparedStatement statementForUpdate;
	private PreparedStatement stmtForGetAll;
	private PreparedStatement stmtForGetAllActive;
	private PreparedStatement stmtForGetAllForPeriod;
	private PreparedStatement stmtForDeactivate;
	
	public MySqlIncomeDao() throws DaoException{
		try(InputStream input = MySqlUserDao.class.getClassLoader().getResourceAsStream("/mycash_db.properties")){
			Properties properties = new Properties();
			properties.load(input);
			String url = properties.getProperty("url");
			String username = properties.getProperty("user");
			String password = properties.getProperty("password");
			connection = DriverManager.getConnection(url, username, password);
			statementForInsert = connection.prepareStatement(queryForInsert, Statement.RETURN_GENERATED_KEYS);
			statementForRead = connection.prepareStatement(queryForRead);
			statementForUpdate = connection.prepareStatement(queryForUpdate);
			stmtForGetAll = connection.prepareStatement(queryForGetAll);
			stmtForGetAllActive = connection.prepareStatement(queryForGetAll + "and is_active = true");
			stmtForGetAllForPeriod = connection.prepareStatement(queryForGetAll 
					+ "and is_active = true and inc_date between ? and ?");
			stmtForDeactivate = connection.prepareStatement(queryForDeactivate);
		}
		catch(SQLException | IOException | NullPointerException e) {
			throw new DaoException("Failed to create MySqlIncomeDao object", e);
		}
	}
	
	public void insert (Income income) throws DaoException{
		ResultSet rSet = null;
		try {
			java.sql.Date sqlDate = new java.sql.Date(income.getIncDate().getTime());
			statementForInsert.setDouble(1, income.getAmount());
			statementForInsert.setString(2, income.getAnnotation());
			statementForInsert.setDate(3, sqlDate);
			statementForInsert.setInt(4, income.getIncomeCatId());
			statementForInsert.setInt(5, income.getCountId());
			statementForInsert.setInt(6, income.getUserId());
			statementForInsert.executeUpdate();
			rSet = statementForInsert.getGeneratedKeys();
			Integer recordId;
			if (rSet.next()) {
				recordId = rSet.getInt(1);
				income.setId(recordId);
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to insert income", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public Income read(int id) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForRead.setInt(1, id);
			rSet = statementForRead.executeQuery();
			if(rSet.next()) {
				Income income = new Income();
				income.setId(rSet.getInt("id"));
				income.setAmount(rSet.getDouble("amount"));
				income.setAnnotation(rSet.getString("annotation"));
				income.setIncDate(rSet.getDate("inc_date"));
				income.setIsActive(rSet.getBoolean("is_active"));
				income.setIncomeCatId(rSet.getInt("income_category_id"));
				income.setCountId(rSet.getInt("count_id"));
				income.setUserId(rSet.getInt("user_id"));
				return income;
			} else {
				return null;
			}

		}
		catch (SQLException e){
			throw new DaoException("Failed to read income", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void update(Income income) throws DaoException{
		try{
			java.sql.Date sqlDate = new java.sql.Date(income.getIncDate().getTime());
			statementForUpdate.setDouble(1, income.getAmount());
			statementForUpdate.setString(2, income.getAnnotation());
			statementForUpdate.setDate(3, sqlDate);
			statementForUpdate.setBoolean(4, income.getIsActive());
			statementForUpdate.setInt(5, income.getIncomeCatId());
			statementForUpdate.setInt(6, income.getCountId());
			statementForUpdate.setInt(7, income.getUserId());
			statementForUpdate.setInt(8, income.getId());
			statementForUpdate.executeUpdate();
		}
		catch (SQLException | NullPointerException e){
			throw new DaoException("Failed to update income", e);
		}
	}
	
	public ArrayList<Income> getAll(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Income> result = new ArrayList<>();
			stmtForGetAll.setInt(1, userId);
			rSet = stmtForGetAll.executeQuery();
			while(rSet.next()){
				Income income = new Income();
				income.setId(rSet.getInt("id"));
				income.setAmount(rSet.getDouble("amount"));
				income.setAnnotation(rSet.getString("annotation"));
				income.setIncDate(rSet.getDate("inc_date"));
				income.setIsActive(rSet.getBoolean("is_active"));
				income.setIncomeCatId(rSet.getInt("income_category_id"));
				income.setCountId(rSet.getInt("count_id"));
				income.setUserId(rSet.getInt("user_id"));
				result.add(income);
			}
			return result;
		}
		catch(SQLException e){
			throw new DaoException("Failed to get all incomes", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ArrayList<Income> getAllActive(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Income> result = new ArrayList<>();
			stmtForGetAllActive.setInt(1, userId);
			rSet = stmtForGetAllActive.executeQuery();
			while(rSet.next()){
				Income income = new Income();
				income.setId(rSet.getInt("id"));
				income.setAmount(rSet.getDouble("amount"));
				income.setAnnotation(rSet.getString("annotation"));
				income.setIncDate(rSet.getDate("inc_date"));
				income.setIsActive(rSet.getBoolean("is_active"));
				income.setIncomeCatId(rSet.getInt("income_category_id"));
				income.setCountId(rSet.getInt("count_id"));
				income.setUserId(rSet.getInt("user_id"));
				result.add(income);
			}
			return result;
		}
		catch(SQLException e){
			throw new DaoException("Failed to get all incomes", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}	
	
	public ArrayList<Income> getAllForPeriod(String startDate, 
			String endDate, int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Income> result = new ArrayList<>();
			stmtForGetAllForPeriod.setInt(1, userId);
			stmtForGetAllForPeriod.setString(2, startDate);
			stmtForGetAllForPeriod.setString(3, endDate);
			rSet = stmtForGetAllForPeriod.executeQuery();
			while(rSet.next()){
				Income income = new Income();
				income.setId(rSet.getInt("id"));
				income.setAmount(rSet.getDouble("amount"));
				income.setAnnotation(rSet.getString("annotation"));
				income.setIncDate(rSet.getDate("inc_date"));
				income.setIsActive(rSet.getBoolean("is_active"));
				income.setIncomeCatId(rSet.getInt("income_category_id"));
				income.setCountId(rSet.getInt("count_id"));
				income.setUserId(rSet.getInt("user_id"));
				result.add(income);
			}
			return result;
		}
		catch(SQLException e){
			throw new DaoException("Failed to get incomes for period", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void deactivate(int incomeId) throws DaoException{
		try {
			stmtForDeactivate.setInt(1, incomeId);
			stmtForDeactivate.executeUpdate();
		} 
		catch(SQLException e) {
			throw new DaoException("Failed to deactivate income", e);
		}
		
	}
	
	public void close() throws DaoException{
		DaoException exception = new DaoException("MySqlIncomeCategoryDao closed with exception(s)");
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
		
		if(stmtForGetAll != null){
			try{
				stmtForGetAll.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(stmtForGetAllActive != null){
			try{
				stmtForGetAllActive.close();
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

