package com.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entity.Role;

public interface RoleRepository extends JpaRepository<Role , Long>{

	List<Role> findByCompanyId(int companyId);
	List<Role> findByDepartmentId(int companyId);
	Role findByRoleId(int roleId);
}
