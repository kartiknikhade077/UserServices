package com.smart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long roleId;
	private long departmentId;
	private int companyId;
	private String roleName;
	
	// access module Name
	private boolean leadAccess;
	private boolean templateAccess;
	private boolean emailAccess;
	public Role(long roleId, long departmentId, int companyId, String roleName, boolean leadAccess,
			boolean templateAccess, boolean emailAccess) {
		super();
		this.roleId = roleId;
		this.departmentId = departmentId;
		this.companyId = companyId;
		this.roleName = roleName;
		this.leadAccess = leadAccess;
		this.templateAccess = templateAccess;
		this.emailAccess = emailAccess;
	}
	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
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
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isLeadAccess() {
		return leadAccess;
	}
	public void setLeadAccess(boolean leadAccess) {
		this.leadAccess = leadAccess;
	}
	public boolean isTemplateAccess() {
		return templateAccess;
	}
	public void setTemplateAccess(boolean templateAccess) {
		this.templateAccess = templateAccess;
	}
	public boolean isEmailAccess() {
		return emailAccess;
	}
	public void setEmailAccess(boolean emailAccess) {
		this.emailAccess = emailAccess;
	}
	

}
