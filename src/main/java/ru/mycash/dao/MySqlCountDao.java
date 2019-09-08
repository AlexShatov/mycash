package ru.mycash.dao;

import java.sql.*;
import java.io.*;
import java.util.*;

import ru.mycash.domain.Count;

public class MySqlCountDao implements AutoCloseable{
	private Connection connection;
	private static final String queryForInsert = "insert into mycash_db.counts (count_name, balance, currency, is_active, user_id) values (?, ?, ?, true, ?)";	
	private static final String queryForUpdate = "update mycash_db.counts set count_name = ?, balance = ?, currency = ?, is_active = ?, user_id = ? where id = ?";
	private static final String queryForGetAll = "select id, count_name, balance, currency, is_active, user_id from mycash_db.counts";
	private static final String queryForDeactivate  = "update mycash_db.counts set is_active = false where id = ?";

	private PreparedStatement statementForInsert;
	private PreparedStatement statementForRead;
	private PreparedStatement statementForUpdate;
	private PreparedStatement statementForGetAll;
	private PreparedStatement statementForGetAllActive;
	private PreparedStatement statementForDeactivate;
	private PreparedStatement statementForGetByName;
	
	public MySqlCountDao() throws DaoException{
		try(InputStream input = MySqlUserDao.class.getClassLoader().getResourceAsStream("/mycash_db.properties")){
			Properties properties = new Properties();
			properties.load(input);
			String url = properties.getProperty("url");
			String username = properties.getProperty("user");
			String password = properties.getProperty("password");
			connection = DriverManager.getConnection(url, username, password);
			statementForInsert = connection.prepareStatement(queryForInsert, Statement.RETURN_GENERATED_KEYS);
			statementForRead = connection.prepareStatement(queryForGetAll + " where id = ?");
			statementForUpdate = connection.prepareStatement(queryForUpdate);
			statementForGetAll = connection.prepareStatement(queryForGetAll + " where user_id = ?");
			statementForGetAllActive = connection.prepareStatement(queryForGetAll + " where is_active = true and user_id = ?");
			statementForDeactivate = connection.prepareStatement(queryForDeactivate);
			statementForGetByName = connection.prepareStatement(queryForGetAll + " where count_name = ? and user_id = ?");
		}
		catch(SQLException | IOException | NullPointerException e) {
			throw new DaoException("Failed to create MySqlCountDao object", e);
		}
	}
	
	public void insert (Count count) throws DaoException{
		ResultSet rSet = null;
		try {
			statementForInsert.setString(1, count.getCountName());
			statementForInsert.setDouble(2, count.getBalance());
			statementForInsert.setString(3, count.getCurrency());
			statementForInsert.setInt(4, count.getUserId());
			statementForInsert.executeUpdate();
			rSet = statementForInsert.getGeneratedKeys();
			Integer recordId;
			if (rSet.next()) {
				recordId = rSet.getInt(1);
				count.setId(recordId);
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to insert count", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public Count read(int id) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForRead.setInt(1, id);
			rSet = statementForRead.executeQuery();
			if(rSet.next()) {
				Count count = new Count();
				count.setId(rSet.getInt("id"));
				count.setCountName(rSet.getString("count_name"));
				count.setBalance(rSet.getDouble("balance"));
				count.setCurrency(rSet.getString("currency"));
				count.setIsActive(rSet.getBoolean("is_active"));
				count.setUserId(rSet.getInt("user_id"));
				return count;
			} else {
				return null;
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to read count", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void update(Count count) throws DaoException{
		try{
			statementForUpdate.setString(1, count.getCountName());
			statementForUpdate.setDouble(2, count.getBalance());
			statementForUpdate.setString(3, count.getCurrency());
			statementForUpdate.setBoolean(4, count.getIsActive());
			statementForUpdate.setInt(5, count.getUserId());
			statementForUpdate.setInt(6, count.getId());
			statementForUpdate.executeUpdate();
		}
		catch (SQLException | NullPointerException e){
			throw new DaoException("Failed to update count", e);
		}
	}
	
	
	public ArrayList<Count> getAll(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Count> result = new ArrayList<>();
			statementForGetAll.setInt(1, userId);
			rSet = statementForGetAll.executeQuery();
			while(rSet.next()){
				Count count = new Count();
				count.setId(rSet.getInt("id"));
				count.setCountName(rSet.getString("count_name"));
				count.setBalance(rSet.getDouble("balance"));
				count.setCurrency(rSet.getString("currency"));
				count.setIsActive(rSet.getBoolean("is_active"));
				count.setUserId(rSet.getInt("user_id"));
				result.add(count);
			}
			return result;
		}
		catch(SQLException e){
			throw new DaoException("Failed to get all counts", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public ArrayList<Count> getAllActive(int userId) throws DaoException{
		ResultSet rSet = null;
		try{
			ArrayList<Count> result = new ArrayList<>();
			statementForGetAllActive.setInt(1, userId);
			rSet = statementForGetAllActive.executeQuery();
			while(rSet.next()){
				Count count = new Count();
				count.setId(rSet.getInt("id"));
				count.setCountName(rSet.getString("count_name"));
				count.setBalance(rSet.getDouble("balance"));
				count.setCurrency(rSet.getString("currency"));
				count.setIsActive(rSet.getBoolean("is_active"));
				count.setUserId(rSet.getInt("user_id"));
				result.add(count);
			}
			return result;
		}
		catch(SQLException e){
			throw new DaoException("Failed to get all active counts", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public Count getByName(String countName, int userId) throws DaoException{
		ResultSet rSet = null;
		try {
			statementForGetByName.setString(1, countName);
			statementForGetByName.setInt(2, userId);
			rSet = statementForGetByName.executeQuery();
			if(rSet.next()) {
				Count count = new Count();
				count.setId(rSet.getInt("id"));
				count.setCountName(rSet.getString("count_name"));
				count.setBalance(rSet.getDouble("balance"));
				count.setCurrency(rSet.getString("currency"));
				count.setIsActive(rSet.getBoolean("is_active"));
				count.setUserId(rSet.getInt("user_id"));
				return count;
			} else {
				return null;
			}
		}
		catch(SQLException e) {
			throw new DaoException("Failed to read count", e);
		}
		finally{
			closeResultSet(rSet);
		}
	}
	
	public void deactivate(int countId) throws DaoException{
		try {
			statementForDeactivate.setInt(1, countId);
			statementForDeactivate.executeUpdate();
		} 
		catch(SQLException e) {
			throw new DaoException("Failed to deactivate count", e);
		}
		
	}
	
	public void close() throws DaoException{
		DaoException exception = new DaoException("MySqlCountDao has been closed with exception(s)");
		if(statementForInsert != null){
			try{
				statementForInsert.close();
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
		
		if(statementForUpdate != null){
			try{
				statementForUpdate.close();
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
		
		if(statementForDeactivate != null){
			try{
				statementForDeactivate.close();
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