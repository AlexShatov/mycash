package ru.mycash.domain;

import java.util.Date;

public class Expense {
	
	private Integer id;
	private Double amount;
	private String annotation;
	private Date expenseDate;
	private Boolean isActive;
	private Integer expenseCatId;
	private Integer countId;
	private Integer userId;
	
	public Expense() {
	
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
	
	public Integer getExpenseCatId() {
		return expenseCatId;
	}
	
	public void setExpenseCatId(Integer expenseCatId) {
		this.expenseCatId = expenseCatId;
	}
	
	public Integer getCountId() {
		return countId;
	}
	
	public void setCountId(Integer countId) {
		this.countId = countId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer countId) {
		this.userId = countId;
	}

}
