package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smart.entity.ModuleAccess;

@Repository
public interface ModuleAccessRepository extends JpaRepository<ModuleAccess, Integer> {
	
	ModuleAccess findByEmployeeId(int employeeId);
	
	ModuleAccess findByEmployeeIdAndCompanyId(int employeeId,int companyId);
	
	@Modifying
	@Transactional
	@Query("UPDATE ModuleAccess m SET m.leadAccess = :leadAccess, m.template = :template, m.email = :email WHERE m.companyId = :companyId")
	int updateModuleAccessByCompanyId(
	    @Param("leadAccess") Boolean leadAccess,
	    @Param("template") Boolean template,
	    @Param("email") Boolean email,
	    @Param("companyId") int companyId
	);
	
	@Modifying
	@Transactional
	@Query("UPDATE ModuleAccess m SET m.leadAccess = :leadAccess, m.template = :template, m.email = :email WHERE m.employeeId = :employeeId")
	int updateModuleAccessByEmployeeId(
	    @Param("leadAccess") Boolean leadAccess,
	    @Param("template") Boolean template,
	    @Param("email") Boolean email,
	    @Param("employeeId") int employeeId
	);
}
