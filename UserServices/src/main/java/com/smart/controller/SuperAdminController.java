package com.smart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dto.CompanyDto;
import com.smart.entity.Company;
import com.smart.entity.ModuleAccess;
import com.smart.entity.User;
import com.smart.repository.CompanyRepository;
import com.smart.repository.ModuleAccessRepository;
import com.smart.repository.UserRepository;

@RestController
@RequestMapping("/super")
public class SuperAdminController {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ModuleAccessRepository moduleAccessRepository;
	

	
	@PostMapping("/createCompany")
	public CompanyDto createCompanyList(@RequestBody CompanyDto companyDto) {
		
		User user=new User();
		
		user.setName(companyDto.getCompanyName());
		user.setEmail(companyDto.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(companyDto.getPassword()));
		user.setEnabled(true);
		user.setAbout(companyDto.getDesciption());
		user.setRole("ROLE_COMPANY");
		userRepository.save(user);
		
	    Company company=new Company();
	    company.setCompnayName(companyDto.getCompanyName());
	    company.setCompanyEmail(companyDto.getEmail());
	    company.setCompanyDescription(companyDto.getDesciption());
	    company.setUserId(user.getId());
		
		companyRepository.save(company);
		
	
		ModuleAccess module=new ModuleAccess();
		module.setCompanyId(company.getCompanyId());
		module.setEmployeeId(0);
		module.setEmail(companyDto.isEmailAccess());
		module.setLeadAccess(companyDto.isLeadAccess());
		module.setTemplate(companyDto.isTempalteAccess());
		
		moduleAccessRepository.save(module);
		
		return companyDto;
	
		
		
	}
			
	
	@GetMapping("/getCompanyList")
	 List<Company> getCompanyList(){
		
		List<Company> companyList=companyRepository.findAll();
		
		return companyList;
	}

}
