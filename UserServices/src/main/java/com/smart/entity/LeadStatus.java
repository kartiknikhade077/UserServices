package com.smart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LeadStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leadStatusId;
	private int companyId;
	private String leadStatus;
    private int sequence;
	public LeadStatus(Long leadStatusId, int companyId, String leadStatus, int sequence) {
		super();
		this.leadStatusId = leadStatusId;
		this.companyId = companyId;
		this.leadStatus = leadStatus;
		this.sequence = sequence;
	}
	public LeadStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getLeadStatusId() {
		return leadStatusId;
	}
	public void setLeadStatusId(Long leadStatusId) {
		this.leadStatusId = leadStatusId;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getLeadStatus() {
		return leadStatus;
	}
	public void setLeadStatus(String leadStatus) {
		this.leadStatus = leadStatus;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
    
    
}
