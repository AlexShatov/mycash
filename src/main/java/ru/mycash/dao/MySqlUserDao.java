package ru.mycash.dao;

import java.sql.*;
import java.io.*;
import java.util.Properties;

import ru.mycash.domain.User;

public class MySqlUserDao implements AutoCloseable {
	
	private Connection connection;
	private static final String queryForInsert = "insert into mycash_db.users (login, pass, mail, is_active) values (?, ?, ?, true)";
	private static final String queryForRead = "select id, login, pass, mail, is_active from mycash_db.users where id = ?";	
	private static final String queryForUpdate = "update mycash_db.users set login = ?, pass = ?, mail = ?, is_active = ? where id = ?";
	private static final String queryGetByLogin = "select id, login, pass, mail, is_active from mycash_db.users where login = ?";
	
	private PreparedStatement statementForInsert;
	private PreparedStatement statementForRead;
	private PreparedStatement statementForUpdate;
	private PreparedStatement stmtForGetByLogin;
	
	public MySqlUserDao() throws DaoException{
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
			stmtForGetByLogin = connection.prepareStatement(queryGetByLogin);
		}
		catch(SQLException | IOException | NullPointerException e) {
			throw new DaoException("Failed to create MySqlUserDao object", e);
		}
	}
	
	public void insert (User user) throws DaoException{
		ResultSet rSet = null;
		try {
			statementForInsert.setString(1, user.getLogin());
			statementForInsert.setString(2, user.getPassword());
			statementForInsert.setString(3, user.getMail());
			statementForInsert.executeUpdate();
			rSet = statementForInsert.getGeneratedKeys();
			Integer recordId;
			if (rSet.next()) {
				recordId = rSet.getInt(1);
				user.setId(recordId);
			}
		}
		catch (SQLException e){
			throw new DaoException("Failed to insert user", e);
		}
		finally{
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
	
	public User read(int id) throws DaoException{
		ResultSet rSet = null;
		try{
			statementForRead.setInt(1, id);
			rSet = statementForRead.executeQuery();
			if(rSet.next()) {
				User user = new User();
				user.setId(rSet.getInt("id"));
				user.setLogin(rSet.getString("login"));
				user.setPassword(rSet.getString("pass"));
				user.setMail(rSet.getString("mail"));
				user.setIsActive(rSet.getBoolean("is_active"));
				return user;
			} else {
				return null;
			}

		}
		catch (SQLException e){
			throw new DaoException("Failed to read user", e);
		}
		finally{
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
	
	public void update(User user) throws DaoException{
		try{
			statementForUpdate.setString(1, user.getLogin());
			statementForUpdate.setString(2, user.getPassword());
			statementForUpdate.setString(3, user.getMail());
			statementForUpdate.setBoolean(4, user.getIsActive());
			statementForUpdate.setInt(5, user.getId());
			statementForUpdate.executeUpdate();
		}
		catch (SQLException | NullPointerException e){
			throw new DaoException("Failed to update user", e);
		}
	}
	
	public User getByLogin(String login) throws DaoException{
		ResultSet rSet = null;
		User user = null;
		try{
			stmtForGetByLogin.setString(1, login);
			rSet = stmtForGetByLogin.executeQuery();
			if(rSet.next()) {
				user = new User();
				user.setId(rSet.getInt("id"));
				user.setLogin(rSet.getString("login"));
				user.setPassword(rSet.getString("pass"));
				user.setMail(rSet.getString("mail"));
				user.setIsActive(rSet.getBoolean("is_active"));
			}
		return user;
		}
		catch (SQLException e){
			throw new DaoException("Failed to read user", e);
		}
		finally{
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
	
	public void close() throws DaoException{
		DaoException exception = new DaoException("MySqlUserDao has been closed with exception(s)");
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
}
