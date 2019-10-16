package ru.mycash.domain;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name = "users")

public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "login")
	private String login;
	
	@Column(name = "pass")
	private String password;
	
	@Column(name = "mail")
	private String mail;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Count> counts = new ArrayList<Count>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<IncomeCategory> incomeCategories = new ArrayList<IncomeCategory>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<ExpenseCategory> expenseCategories = new ArrayList<ExpenseCategory>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Income> incomes = new ArrayList<Income>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Expense> expenses = new ArrayList<Expense>();
	
	public List<Count> getCounts(){
		return counts;
	}
	
	public void setCounts(List<Count> counts) {
		this.counts = counts;
	}
	
	public void addCounts (Count count) {
		count.setUser(this);
		counts.add(count);
	}
	
	public List<IncomeCategory> getIncomeCategories(){
		return incomeCategories;
	}
	
	public void setIncomeCategories(List<IncomeCategory> categories) {
		this.incomeCategories = categories;
	}
	
	public void addIncomeCategory (IncomeCategory category) {
		category.setUser(this);
		incomeCategories.add(category);
	}
	
	public List<ExpenseCategory> getExpenseCategories(){
		return expenseCategories;
	}
	
	public void setExpenseCategories(List<ExpenseCategory> categories) {
		this.expenseCategories = categories;
	}
	
	public void addExpenseCategory (ExpenseCategory category) {
		category.setUser(this);
		expenseCategories.add(category);
	}
	
	public List<Income> getIncomes(){
		return incomes;
	}
	
	public void setIncomes(List<Income> incomes) {
		this.incomes = incomes;
	}
	
	public void addIncome (Income income) {
		income.setUser(this);
		incomes.add(income);
	}
	
	public List<Expense> getExpenses(){
		return expenses;
	}
	
	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	
	public void addExpense (Expense expense) {
		expense.setUser(this);
		expenses.add(expense);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public Boolean getIsActive () {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public User(){
		
	}
}
