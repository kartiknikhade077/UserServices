package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.ModuleAccess;

@Repository
public interface ModuleAccessRepository extends JpaRepository<ModuleAccess, Integer> {
	
	ModuleAccess findByEmployeeId(int employeeId);
	

}
