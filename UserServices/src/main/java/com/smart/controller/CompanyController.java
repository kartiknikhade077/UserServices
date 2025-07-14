package com.smart.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dto.EmployeeDto;
import com.smart.entity.Company;
import com.smart.entity.Employee;
import com.smart.entity.ModuleAccess;
import com.smart.entity.User;
import com.smart.repository.CompanyRepository;
import com.smart.repository.EmployeeRepository;
import com.smart.repository.ModuleAccessRepository;
import com.smart.repository.UserRepository;

@RestController
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmployeeRepository employRepository;
	@Autowired
	private ModuleAccessRepository moduleAccessRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Company company;

	@ModelAttribute
	public void companyDetails(Principal principal) {

		company = companyRepository.findByCompanyEmail(principal.getName());
	}

	@PostMapping("/createEmployee")
	public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
		try {
			Employee employee = new Employee();
			employee.setName(employeeDto.getName());
			employee.setEmail(employeeDto.getEmail());
			employee.setPhone(employeeDto.getPhone());
			employee.setDepartment(employeeDto.getDepartment());
			employee.setGender(employeeDto.getGender());
			employee.setDescription(employeeDto.getDescription());
			employee.setUserId(company.getUserId());
			employee.setCompanyId(company.getCompanyId());

			employRepository.save(employee);

			User user = new User();
			user.setName(employeeDto.getName());
			user.setEmail(employeeDto.getEmail());
			user.setPassword(bCryptPasswordEncoder.encode(employeeDto.getPassword()));
			user.setEnabled(false);
			user.setRole("ROLE_EMP");

			userRepository.save(user);

			ModuleAccess module = new ModuleAccess();
			module.setCompanyId(company.getCompanyId());
			module.setEmployeeId(employee.getEmployeeId());
			module.setEmail(employeeDto.isEmailAccess());
			module.setLeadAccess(employeeDto.isLeadAccess());
			module.setTemplate(employeeDto.isTemplateAccess());

			moduleAccessRepository.save(module);

			return ResponseEntity.ok(employee);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating employee: " + ex.getMessage());
		}
	}

	@GetMapping("/getEmployeeList")
	public ResponseEntity<?> getEmployeeList() {
		try {

			List<Employee> employeeList = employRepository.findByCompanyId(company.getCompanyId());

			return ResponseEntity.ok(employeeList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating employee: " + e.getMessage());
		}
	}
	
	@GetMapping("/getEmployee/{employeeId}")
	public ResponseEntity<?> getEmployee(@PathVariable int employeeId) {
		try {
           Map<String,Object> employeeDetails=new HashMap<String, Object>();
			
			Employee employee = employRepository.findByEmployeeId(employeeId);
			ModuleAccess module=moduleAccessRepository.findByEmployeeId(employeeId);
            
			employeeDetails.put("employeeInfo", employee);
			employeeDetails.put("moduleAccess", module);
			return ResponseEntity.ok(employeeDetails);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating employee: " + e.getMessage());
		}
	}

}
