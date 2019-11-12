package ru.mycash;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ru.mycash.dao.MySqlBudgetDao;
import ru.mycash.dao.MySqlCountDao;
import ru.mycash.dao.MySqlExpenseCategoryDao;
import ru.mycash.dao.MySqlExpenseDao;
import ru.mycash.dao.MySqlIncomeCategoryDao;
import ru.mycash.dao.MySqlIncomeDao;
import ru.mycash.dao.MySqlUserDao;
import ru.mycash.service.MyCashMainService;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
public class AppCtxConfig {
	
	private Properties appProperties = null;
	
	public AppCtxConfig() throws IOException{
		appProperties = getAppProperties();
	}
	
 	@Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ru.mycash.domain");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
 
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(appProperties.getProperty("driver"));
        dataSource.setUrl(appProperties.getProperty("url"));
        dataSource.setUsername(appProperties.getProperty("user"));
        dataSource.setPassword(appProperties.getProperty("password"));
        return dataSource;
    }
 
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
          = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
	 
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", appProperties.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", appProperties.getProperty("hibernate.dialect"));
        return hibernateProperties;
    }
	    
	private Properties getAppProperties() throws IOException{
		Properties properties = null;
		try(InputStream input = AppCtxConfig.class.getResourceAsStream("/mycash_db.properties")){
			properties = new Properties();
			properties.load(input);
		} catch(IOException e) {
			throw new IOException("Failed to read properties file", e) ;
		}
		return properties;
	}
	
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
	
	@Bean(name="mainservice")
	public MyCashMainService getMyCashMainService(){
		return new MyCashMainService();
	} 
}
