package com.smart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Departments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long departmentId;
	private int companyId;
	private String departmentName;
	private String departmentEmail;
	
	
	public Departments(long departmentId, int companyId, String departmentName,String departmentEmail) {
		super();
		this.departmentId = departmentId;
		this.companyId = companyId;
		this.departmentName = departmentName;
		this.departmentEmail=departmentEmail;
	}
	
	
	public Departments() {
		super();
		// TODO Auto-generated constructor stub
	}


	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public String getDepartmentEmail() {
		return departmentEmail;
	}


	public void setDepartmentEmail(String departmentEmail) {
		this.departmentEmail = departmentEmail;
	}

	
	
	
}
