package com.smart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Company {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int companyId;
	private int userId;
	private String companyName;
	private String companyEmail;
	private String companyDescription;
	public Company(int companyId, int userId, String companyName, String companyEmail, String companyDescription) {
		super();
		this.companyId = companyId;
		this.userId = userId;
		this.companyName = companyName;
		this.companyEmail = companyEmail;
		this.companyDescription = companyDescription;
	}
	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	public String getCompanyDescription() {
		return companyDescription;
	}
	public void setCompanyDescription(String companyDescription) {
		this.companyDescription = companyDescription;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	

}
