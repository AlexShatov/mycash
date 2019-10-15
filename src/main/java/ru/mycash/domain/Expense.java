package ru.mycash.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "expenses")

public class Expense {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "annotation")
	private String annotation;
	
	@Column(name = "exp_date")
	private Date expenseDate;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "expense_category_id", referencedColumnName = "id")
	private ExpenseCategory expenseCategory;
	
	@ManyToOne
	@JoinColumn(name = "count_id", referencedColumnName = "id")
	private Count count;

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public ExpenseCategory getExpenseCategory() {
		return expenseCategory;
	}
	
	public void setExpenseCategory(ExpenseCategory expenseCategory) {
		this.expenseCategory = expenseCategory;
	}
	
	public Count getCount() {
		return count;
	}
	
	public void setCount(Count count) {
		this.count = count;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getAnnotation() {
		return annotation;
	}
	
	public void setAnnotation (String annotation) {
		this.annotation = annotation;
	}
	
	public Date getExpenseDate() {
		return expenseDate;
	}
	
	public void setExpenseDate(Date expenseDate) {
		this.expenseDate = expenseDate;
	}
	
	public Boolean getIsActive () {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Expense() {
		
	}
}
