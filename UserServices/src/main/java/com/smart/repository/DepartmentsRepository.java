package com.smart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smart.entity.Departments;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

	List<Departments> findByCompanyId(int companyId);
}
