package ru.mycash.domain;

public class Count {
	
	private Integer id;
	private String countName;
	private Double balance;
	private String currency;
	private Boolean isActive;
	private Integer userId;
	
	public Count () {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCountName () {
		return countName;
	}
	
	public void setCountName (String countName) {
		this.countName = countName;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public String getCurrency () {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Boolean getIsActive () {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Integer getUserId () {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
