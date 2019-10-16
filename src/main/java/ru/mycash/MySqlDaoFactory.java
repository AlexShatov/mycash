package ru.mycash;

import ru.mycash.dao.*;

public class MySqlDaoFactory {

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