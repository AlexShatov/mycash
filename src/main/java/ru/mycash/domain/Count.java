package ru.mycash.domain;

import javax.persistence.*;

@Entity
@Table(name = "counts")

public class Count {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "count_name")
	private String countName;
	
	@Column(name = "balance")
	private Double balance;
	
	@Column(name = "currency")
	private String currency;
	
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
	
	public Count () {
		
	}
}
