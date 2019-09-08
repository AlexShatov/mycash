package ru.mycash.domain;

public class IncomeCategory {

	private Integer id;
	private String categoryName;
	private Boolean isActive;
	private Integer userId;
	
	public IncomeCategory() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCategoryName () {
		return categoryName;
	}
	
	public void setCategoryName (String categoryName) {
		this.categoryName = categoryName;
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
