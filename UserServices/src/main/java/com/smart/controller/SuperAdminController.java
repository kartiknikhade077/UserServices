package com.smart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	@GetMapping("/checkDuplicateEmail/{email}")
	public boolean checkDuplicate(@PathVariable String email) {
		boolean isUserExist = true;

		User user = userRepository.getUserByUserName(email);

		if (user != null) {
			isUserExist = false;
		}

		return isUserExist;
	}

	@PostMapping("/createCompany")
	public ResponseEntity<?> createCompanyList(@RequestBody CompanyDto companyDto) {
       try {
		User user = new User();

		user.setName(companyDto.getCompanyName());
		user.setEmail(companyDto.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(companyDto.getPassword()));
		user.setEnabled(true);
		user.setAbout(companyDto.getDesciption());
		user.setRole("ROLE_COMPANY");
		user.setExpirayDate(companyDto.getExpirayDate());
		userRepository.save(user);

		Company company = new Company();
		company.setCompnayName(companyDto.getCompanyName());
		company.setCompanyEmail(companyDto.getEmail());
		company.setCompanyDescription(companyDto.getDesciption());
		company.setUserId(user.getId());

		companyRepository.save(company);

		ModuleAccess module = new ModuleAccess();
		module.setCompanyId(company.getCompanyId());
		module.setEmployeeId(0);
		module.setEmail(companyDto.isEmailAccess());
		module.setLeadAccess(companyDto.isLeadAccess());
		module.setTemplate(companyDto.isTempalteAccess());

		moduleAccessRepository.save(module);

		return ResponseEntity.ok(companyDto);
       }catch(Exception e) {
    	   
    	   e.printStackTrace();
    	   
    	   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }
	}

		@GetMapping("/getCompanyList/{page}/{size}")
		public ResponseEntity<?> getCompanyList(@PathVariable int page, @PathVariable int size,
				@RequestParam(defaultValue = "") String companyName) {
	
			try {
			
			Pageable pageable = PageRequest.of(page, size, Sort.by("companyId").descending());
	
			Page<Company> companyPage = companyRepository.findByCompnayNameContainingIgnoreCase(companyName, pageable);
	
			List<Company> companyList = companyPage.getContent();
	
			return ResponseEntity.ok(companyList);
			}catch(Exception e){
				e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			}
		}

}
