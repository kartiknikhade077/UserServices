package com.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.LeadStatus;

@Repository
public interface LeadStatusRepository extends JpaRepository<LeadStatus, Long>{
	
	List<LeadStatus> findByCompanyId(int companyId);

}
