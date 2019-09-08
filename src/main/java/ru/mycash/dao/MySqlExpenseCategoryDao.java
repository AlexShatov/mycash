package ru.mycash.dao;

import java.sql.*;
import java.io.*;
import java.util.*;

import ru.mycash.domain.ExpenseCategory;
import ru.mycash.domain.IncomeCategory;

public class MySqlExpenseCategoryDao{
	private Connection connection;
	private static final String queryForInsert = "insert into mycash_db.expense_categories (category_name, is_active, user_id)"
			+ " values (?, true, ?)";
	private static final String queryForUpdate = "update mycash_db.expense_categories set" +
			" category_name = ?, is_active = ?, user_id = ? where id = ?";
	private static final String queryForGetAll = "select id, category_name, is_active, user_id from mycash_db.expense_categories ";
	private static final String queryForGetAllActive = "select id, category_name, is_active, user_id" + 
			" from mycash_db.expense_categories where user_id = ? and is_active = true";

	private PreparedStatement statementForInsert;
	private PreparedStatement statementForRead;
	private PreparedStatement statementForUpdate;
	private PreparedStatement statementForGetAll;
	private PreparedStatement statementForGetAllActive;
	private PreparedStatement statementForGetByName;
	
	public MySqlExpenseCategoryDao() throws DaoException{
		try(InputStream input = MySqlExpenseCategoryDao.class.getClassLoader().getResourceAsStream("/mycash_db.properties")){
			Properties properties = new Properties();
			properties.load(input);
			String url = properties.getProperty("url");
			String username = properties.getProperty("user");
			String password = properties.getProperty("password");
			connection = DriverManager.getConnection(url, username, password);
			statementForInsert = connection.prepareStatement(queryForInsert, Statement.RETURN_GENERATED_KEYS);
			statementForRead = connection.prepareStatement(queryForGetAll + "where id = ?");
			statementForUpdate = connection.prepareStatement(queryForUpdate);
			statementForGetAll = connection.prepareStatement(queryForGetAll);
			statementForGetAllActive = connection.prepareStatement(queryForGetAllActive);
			statementForGetByName = connection.prepareStatement(queryForGetAllActive + " and category_name = ?");
		}
		catch(SQLException | IOException | NullPointerException e){
			throw new DaoException("Failed to create MySqlExpenseCategoryDao object", e);
		}
	}
	
	public void insert (ExpenseCategory expenseCat) throws DaoException{
		ResultSet rSet = null;
		try {
			statementForInsert.setString(1, expenseCat.getCategoryName());
			statementForInsert.setInt(2, expenseCat.getUserId());
			statementForInsert.executeUpdate();
			rSet = statementForInsert.getGeneratedKeys();
			Integer recordId = null;
			if (rSet.next()) {
				recordId = rSet.getInt(1);
				expenseCat.setId(recordId);
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to insert expense category", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ExpenseCategory read(int id) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForRead.setInt(1, id);
			rSet = statementForRead.executeQuery();
			if(rSet.next()) {
				ExpenseCategory expenseCat = new ExpenseCategory();
				expenseCat.setId(rSet.getInt("id"));
				expenseCat.setCategoryName(rSet.getString("category_name"));
				expenseCat.setIsActive(rSet.getBoolean("is_active"));
				expenseCat.setUserId(rSet.getInt("user_id"));
				return expenseCat;
			} else {
				return null;
			}

		}
		catch (SQLException e){
			throw new DaoException("Failed to read expense category", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void update(ExpenseCategory expenseCat) throws DaoException{
		try{
			statementForUpdate.setString(1, expenseCat.getCategoryName());
			statementForUpdate.setBoolean(2, expenseCat.getIsActive());
			statementForUpdate.setInt(3, expenseCat.getUserId());
			statementForUpdate.setInt(4, expenseCat.getId());
			statementForUpdate.executeUpdate();
		}
		catch (SQLException | NullPointerException e){
			throw new DaoException("Failed to update expense category", e);
		}
	}
	
	public ArrayList<ExpenseCategory> getAll() throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<ExpenseCategory> result = new ArrayList<>();
			rSet = statementForGetAll.executeQuery();
			while (rSet.next()){
				ExpenseCategory expenseCat = new ExpenseCategory();
				expenseCat.setId(rSet.getInt("id"));
				expenseCat.setCategoryName(rSet.getString("category_name"));
				expenseCat.setIsActive(rSet.getBoolean("is_active"));
				expenseCat.setUserId(rSet.getInt("user_id"));
				result.add(expenseCat);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get all expense categories", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ArrayList<ExpenseCategory> getAllActive(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<ExpenseCategory> result = new ArrayList<>();
			statementForGetAllActive.setInt(1, userId);
			rSet = statementForGetAllActive.executeQuery();
			while (rSet.next()){
				ExpenseCategory expenseCat = new ExpenseCategory();
				expenseCat.setId(rSet.getInt("id"));
				expenseCat.setCategoryName(rSet.getString("category_name"));
				expenseCat.setIsActive(rSet.getBoolean("is_active"));
				expenseCat.setUserId(rSet.getInt("user_id"));
				result.add(expenseCat);
			}
			return result;
		}
		catch (SQLException e){
			throw new DaoException("Failed to get all active expense categories", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public IncomeCategory getByName(int userId, String categoryName) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForGetByName.setInt(1, userId);
			statementForGetByName.setString(2, categoryName);
			rSet = statementForGetByName.executeQuery();
			if(rSet.next()) {
				IncomeCategory incomeCat = new IncomeCategory();
				incomeCat.setId(rSet.getInt("id"));
				incomeCat.setCategoryName(rSet.getString("category_name"));
				incomeCat.setIsActive(rSet.getBoolean("is_active"));
				incomeCat.setUserId(rSet.getInt("user_id"));
				return incomeCat;
			} else {
				return null;
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to read expense category", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void close() throws DaoException{
		DaoException exception = new DaoException("MySqlExpenseCategoryDao has been closed with exception(s)");
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
		
		if(statementForGetByName != null){
			try{
				statementForGetByName.close();
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