package ru.mycash;

import ru.mycash.dao.*;

public class MySqlDaoFactory {

    private static final String driver = "com.mysql.cj.jdbc.Driver";

    public MySqlDaoFactory() throws DaoException{
        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
            System.out.println("Driver loading succesful!");
        }
        catch(Exception e){
            throw new DaoException("Failed to create DAOfactory",e);
        }
    }

    public MySqlUserDao getMySqlUserDao() {
            return new MySqlUserDao();
    }

    public MySqlIncomeDao getMySqlIncomeDao() throws  DaoException{
        try{
            return  new MySqlIncomeDao();
        }
        catch (DaoException e){
            throw e;
        }
    }

    public MySqlCountDao getMySqlCountDao() throws DaoException{
        try{
            return  new MySqlCountDao();
        }
        catch (DaoException e){
            throw e;
        }
    }
    
    public MySqlExpenseDao getMySqlExpenseDao() throws  DaoException{
        try{
            return  new MySqlExpenseDao();
        }
        catch (DaoException e){
            throw e;
        }
    }
    
    public MySqlIncomeCategoryDao getMySqlIncomeCategoryDao() throws  DaoException{
        try{
            return  new MySqlIncomeCategoryDao();
        }
        catch (DaoException e){
            throw e;
        }
    }
    
    public MySqlExpenseCategoryDao getMySqlExpenseCategoryDao() throws  DaoException{
        try{
            return  new MySqlExpenseCategoryDao();
        }
        catch (DaoException e){
            throw e;
        }
    }
    
    public MySqlBudgetDao getMySqlBudgetDao() throws  DaoException{
        try{
            return  new MySqlBudgetDao();
        }
        catch (DaoException e){
            throw e;
        }
    }
}