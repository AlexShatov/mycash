package ru.mycash.domain;

import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "incomes")

public class Income {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "annotation")
	private String annotation;
	
	@Column(name = "inc_date")
	private Date incDate;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "income_category_id", referencedColumnName = "id")
	private IncomeCategory incomeCategory;
	
	@ManyToOne
	@JoinColumn(name = "count_id", referencedColumnName = "id")
	private Count count;

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public IncomeCategory getIncomeCategory() {
		return incomeCategory;
	}
	
	public void setIncomeCategory(IncomeCategory incomeCategory) {
		this.incomeCategory = incomeCategory;
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
	
	public Income() {
	
	}
}
