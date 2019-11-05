package ru.mycash;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import ru.mycash.dao.MySqlBudgetDao;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.dao.MySqlExpenseDao;
import ru.mycash.dao.MySqlIncomeCategoryDao;
import ru.mycash.dao.MySqlIncomeDao;
import ru.mycash.dao.MySqlUserDao;

@Configuration
@ComponentScan(basePackages = {"ru.mycash.servlet"})
public class AppCtxConfig {
	
	
	@Bean(name="incomedao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlIncomeDao getMySqlIncomeDao() {
		return new MySqlIncomeDao();
	}
	
	@Bean(name="userdao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlUserDao getMySqlUserDao() {
		return new MySqlUserDao();
	}
	
	@Bean(name="countdao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlCountDao getMySqlCountDao() {
		return new MySqlCountDao();
	}
	
	@Bean(name="expensedao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlExpenseDao getMySqlExpenseDao() {
		return new MySqlExpenseDao();
	}
	
	@Bean(name="expensecatdao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlExpenseCategoryDao getMySqlExpenseCategoryDao() {
		return new MySqlExpenseCategoryDao();
	}
	
	@Bean(name="incomecatdao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlIncomeCategoryDao getMySqlIncomeCategoryDao() {
		return new MySqlIncomeCategoryDao();
	}
	
	@Bean(name="budgetdao")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS )
	public MySqlBudgetDao getMySqlBudgetDao() {
		return new MySqlBudgetDao();
	}
}
