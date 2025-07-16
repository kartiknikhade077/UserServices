package com.smart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entity.Role;

public interface RoleRepository extends JpaRepository<Role , Long>{

	Page<Role> findByCompanyId(int companyId,Pageable pageable);
	List<Role> findByDepartmentId(int companyId);
	Role findByRoleId(int roleId);
}
