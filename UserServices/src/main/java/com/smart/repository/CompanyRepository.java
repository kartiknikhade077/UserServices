package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.dto.CompanyDto;
import com.smart.entity.Company;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	public List<Company> findById(int id);

	public Company findByCompanyEmail(String companyEmail);

	@Query(value = """
		    SELECT
		        c.compnay_name AS companyName,
		        c.company_id AS companyId,
		        c.company_email AS email,
		        c.company_description AS desciption,
		        m.lead_access AS leadAccess,
		        m.template AS tempalteAccess,
		        m.email AS emailAccess
		    FROM company c
		    JOIN module_access m ON c.company_id = m.company_id
		    WHERE c.company_email = :email
		""", nativeQuery = true)
		CompanyDto findCompanyDtoByEmail(@Param("email") String email);

}
