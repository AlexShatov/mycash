package ru.mycash.domain;

import java.util.Date;

public class BudgetEntry {

	private Integer id;
	private Double amount;
	private Date startDate;
	private Date endDate;
	private Integer expenseCatId;
	private Integer userId;
	
	public BudgetEntry() {
		
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
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Integer getExpenseCatId() {
		return expenseCatId;
	}
	
	public void setExpenseCatId(Integer expenseCatId) {
		this.expenseCatId = expenseCatId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
