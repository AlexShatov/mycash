package ru.mycash.test.util;

import java.sql.*;
import java.io.*;
import java.util.Properties;

import ru.mycash.dao.DaoException;

public class TestDbCleaner {

	private Connection connection;
	
	private static final String queryForUsers = "delete from users where id > 1";
	private static final String queryForCounts = "delete from counts where id > 1";
	private static final String queryForIncomeCategories = "delete from income_categories where id > 1";
	private static final String queryForExpenseCategories = "delete from expense_categories where id > 1";
	private static final String queryForIncomes = "delete from incomes where id > 1";
	private static final String queryForExpenses = "delete from expenses where id > 1";
	private static final String queryForBudget = "delete from budget where id > 1";
	
	private PreparedStatement statementForUsers;
	private PreparedStatement statementForCounts;
	private PreparedStatement statementForIncomeCategories;
	private PreparedStatement statementForExpenseCategories;
	private PreparedStatement statementForIncomes;
	private PreparedStatement statementForExpenses;
	private PreparedStatement statementForBudget;
	
	public TestDbCleaner() throws DaoException{
		try(InputStream input = TestDbCleaner.class.getClassLoader().getResourceAsStream("mycash_db.properties")){
			Properties properties = new Properties();
			properties.load(input);
			String url = properties.getProperty("url");
			String username = properties.getProperty("user");
			String password = properties.getProperty("password");
			connection = DriverManager.getConnection(url, username, password);
			statementForUsers = connection.prepareStatement(queryForUsers);
			statementForCounts = connection.prepareStatement(queryForCounts);
			statementForIncomeCategories = connection.prepareStatement(queryForIncomeCategories);
			statementForExpenseCategories = connection.prepareStatement(queryForExpenseCategories);
			statementForIncomes = connection.prepareStatement(queryForIncomes);
			statementForExpenses = connection.prepareStatement(queryForExpenses);
			statementForBudget = connection.prepareStatement(queryForBudget);
		}
		catch(SQLException | IOException | NullPointerException e) {
			throw new DaoException("Failed to create TestDbCleaner object", e);
		}
	}
	
	public void cleanUsers() throws DaoException{
		try {
			statementForUsers.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"users\" table in DB", e);
		}
	}
	
	public void cleanCounts() throws DaoException{
		try {
			statementForCounts.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"counts\" table in DB", e);
		}
	}
	
	public void cleanIncomeCategories() throws DaoException{
		try {
			statementForIncomeCategories.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"income_categories\" table in DB", e);
		}
	}
	
	public void cleanExpenseCategories() throws DaoException{
		try {
			statementForExpenseCategories.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"expense_categories\" table in DB", e);
		}
	}
	
	public void cleanIncomes() throws DaoException{
		try {
			statementForIncomes.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"incomes\" table in DB", e);
		}
	}
	
	public void cleanExpenses() throws DaoException{
		try {
			statementForExpenses.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"expenses\" table in DB", e);
		}
	}
	
	public void cleanBudget() throws DaoException{
		try {
			statementForBudget.executeUpdate();
		}
		catch (SQLException e){
			throw new DaoException("Failed to clean \"budget\" table in DB", e);
		}
	}
	
	public void close() throws DaoException{
		DaoException exception = new DaoException("TestDbCleaner has been closed with exception(s)");
		
		if(statementForUsers != null){
			try{
				statementForUsers.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForCounts != null){
			try{
				statementForCounts.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForIncomeCategories != null){
			try{
				statementForIncomeCategories.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForExpenseCategories != null){
			try{
				statementForExpenseCategories.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForIncomes != null){
			try{
				statementForIncomes.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForExpenses != null){
			try{
				statementForExpenses.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(statementForBudget != null){
			try{
				statementForBudget.close();
			}
			catch(SQLException e){
				exception.addSuppressed(e);
			}
		}
		
		if(connection != null) {
			try {
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
}
