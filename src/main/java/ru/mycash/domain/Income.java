package ru.mycash.domain;

import java.util.Date;

public class Income {

	private Integer id;
	private Double amount;
	private String annotation;
	private Date incDate;
	private Boolean isActive;
	private Integer incomeCatId;
	private Integer countId;
	private Integer userId;
	
	public Income() {
	
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
	
	public Date getIncDate() {
		return incDate;
	}
	
	public void setIncDate(Date incDate) {
		this.incDate = incDate;
	}
	
	public Boolean getIsActive () {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Integer getIncomeCatId() {
		return incomeCatId;
	}
	
	public void setIncomeCatId(Integer incomeCatId) {
		this.incomeCatId = incomeCatId;
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
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
