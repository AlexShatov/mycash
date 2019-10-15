package ru.mycash;

import ru.mycash.dao.*;

public class MySqlDaoFactory {

    private static final String driver = "com.mysql.cj.jdbc.Driver";

    public MySqlDaoFactory() throws DaoException{
        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        }
        catch(Exception e){
            throw new DaoException("Failed to create DAOfactory",e);
        }
    }

    public MySqlUserDao getMySqlUserDao() {
            return new MySqlUserDao();
    }

    public MySqlIncomeDao getMySqlIncomeDao(){
            return new MySqlIncomeDao();
    }

    public MySqlCountDao getMySqlCountDao(){
            return  new MySqlCountDao();
    }
    
    public MySqlExpenseDao getMySqlExpenseDao() throws  DaoException{
            return  new MySqlExpenseDao();
    }
    
    public MySqlIncomeCategoryDao getMySqlIncomeCategoryDao(){
        return  new MySqlIncomeCategoryDao();
    }
    
    public MySqlExpenseCategoryDao getMySqlExpenseCategoryDao(){
    	return  new MySqlExpenseCategoryDao();
    }
    
    public MySqlBudgetDao getMySqlBudgetDao() throws  DaoException{
            return  new MySqlBudgetDao();
    }
}