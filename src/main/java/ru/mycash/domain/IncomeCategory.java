package ru.mycash.domain;

import javax.persistence.*;

@Entity
@Table(name = "income_categories")

public class IncomeCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
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
	
	public IncomeCategory() {
		
	}
}
