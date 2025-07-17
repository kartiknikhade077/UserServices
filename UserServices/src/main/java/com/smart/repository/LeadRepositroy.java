package com.smart.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.Leads;

@Repository
public interface LeadRepositroy extends JpaRepository<Leads, Long> {
	
	Page<Leads> findByCompanyIdAndCustomerNameContainingIgnoreCase(int companyId ,Pageable pageable,String customerName);

}
