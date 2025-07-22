package com.smart.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.RoleResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dto.EmployeeDto;
import com.smart.entity.Company;
import com.smart.entity.Departments;
import com.smart.entity.Employee;
import com.smart.entity.LeadStatus;
import com.smart.entity.Leads;
import com.smart.entity.ModuleAccess;
import com.smart.entity.Role;
import com.smart.entity.User;
import com.smart.repository.CompanyRepository;
import com.smart.repository.DepartmentsRepository;
import com.smart.repository.EmployeeRepository;
import com.smart.repository.LeadRepositroy;
import com.smart.repository.LeadStatusRepository;
import com.smart.repository.ModuleAccessRepository;
import com.smart.repository.RoleRepository;
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
	
	@Autowired
	private DepartmentsRepository departmentsRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private LeadRepositroy leadRepositroy;
	
	@Autowired
	private LeadStatusRepository leadStatusRepository;
	
	Company company;
	User user;

	@ModelAttribute
	public void companyDetails(Principal principal) {

		company = companyRepository.findByCompanyEmail(principal.getName());
		user=userRepository.getUserByUserName(principal.getName());
		
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
			employee.setDepartmentId(employeeDto.getDepartmentId());
			employee.setRoleId(employeeDto.getRoleId());
			employee.setRoleName(employeeDto.getRoleName());

			employRepository.save(employee);

			User employeeUser = new User();
			employeeUser.setName(employeeDto.getName());
			employeeUser.setEmail(employeeDto.getEmail());
			employeeUser.setPassword(bCryptPasswordEncoder.encode(employeeDto.getPassword()));
			employeeUser.setEnabled(false);
			employeeUser.setRole("ROLE_EMP");
			employeeUser.setExpirayDate(user.getExpirayDate());

			userRepository.save(employeeUser);

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
	
	
	@GetMapping("/checkDuplicateEmail/{email}")
	public boolean checkDuplicate(@PathVariable String email) {
		boolean isUserExist = true;

		User user = userRepository.getUserByUserName(email);

		if (user != null) {
			isUserExist = false;
		}

		return isUserExist;
	}

	@GetMapping("/getEmployeeList/{page}/{size}")
	public ResponseEntity<?> getEmployeeList(@PathVariable int page ,@PathVariable int size, @RequestParam(defaultValue = "") String name) {
		try {
			Map<String ,Object> data=new HashMap<String , Object>();
			 Pageable pageable = PageRequest.of(page, size, Sort.by("employeeId").descending());
		        Page<Employee> employeePage = employRepository.findByCompanyIdAndNameContainingIgnoreCase(company.getCompanyId(), name, pageable);
		        List<Employee> employeeList = employeePage.getContent();
		        data.put("employeeList", employeeList);
		        data.put("totalPages", employeePage.getTotalPages());
		        data.put("currentPage", employeePage.getNumber());
			return ResponseEntity.ok(data);
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
	
	
	@PutMapping("/updateEmployeeInfo")
	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee) {

		try {
			employRepository.save(employee);
			return ResponseEntity.ok(employee);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating employee: " + e.getMessage());
		}
	   
   }
	
	
	@PutMapping("/updateEmployeeModules")
	public ResponseEntity<?> updateCompanyModules(@RequestBody ModuleAccess moduleAccess) {

		try {
			
			moduleAccessRepository.updateModuleAccessByEmployeeId(moduleAccess.isLeadAccess(),moduleAccess.isTemplate(),moduleAccess.isEmail(),moduleAccess.getEmployeeId());
			return ResponseEntity.ok("Modules Updated Succesfully");

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	@PostMapping("/createDepartment")
	public ResponseEntity<?> createDepartment(@RequestBody Departments departments) {

		try {
			departments.setCompanyId(company.getCompanyId());
			departmentsRepository.save(departments);
			return ResponseEntity.ok(departments);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	
	@PutMapping("/updateDepartment")
	public ResponseEntity<?> updateDepartment(@RequestBody Departments departments) {

		try {
			departments.setCompanyId(company.getCompanyId());
			departmentsRepository.save(departments);
			return ResponseEntity.ok(departments);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	@GetMapping("/getDepartments/{page}/{size}")
	public ResponseEntity<?> getDepartments(@PathVariable int page ,@PathVariable int size){

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("departmentId").descending());
			Page<Departments> deparmentList=departmentsRepository.findByCompanyId(company.getCompanyId(),pageable);
			Map<String ,Object> data=new HashMap<String,Object>();
			data.put("deparmentList", deparmentList.getContent());
			data.put("currentPage", deparmentList.getNumber());
			data.put("totalPage", deparmentList.getTotalPages());
			return ResponseEntity.ok(deparmentList);

		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	@PostMapping("/createRole")
	public ResponseEntity<?> createRole(@RequestBody Role role) {

		try {
			role.setCompanyId(company.getCompanyId());
			roleRepository.save(role);
			return ResponseEntity.ok(role);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	@PutMapping("/updateRole")
	public ResponseEntity<?> updateRole(@RequestBody Role role) {

		try {
			role.setCompanyId(company.getCompanyId());
			roleRepository.save(role);
			return ResponseEntity.ok(role);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	@GetMapping("/getRoleByCompanyId/{page}/{size}")
	public ResponseEntity<?> getRolesByCompanyId(@PathVariable int page , @PathVariable int size) {

		try {
			Pageable pageable = PageRequest.of(page, size,Sort.by("roleId").descending());
			Page<Role> rolePage = roleRepository.findByCompanyId(company.getCompanyId(), pageable);
			Map<String, Object> response = new HashMap<>();
			response.put("roles", rolePage.getContent());
			response.put("currentPage", rolePage.getNumber());
			response.put("totalItems", rolePage.getTotalElements());
			response.put("totalPages", rolePage.getTotalPages());

			return ResponseEntity.ok(response);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	@GetMapping("/getRolesByDepartmentId/{departmentId}")
	public ResponseEntity<?> getRolesByDepartmentId(@PathVariable int departmentId) {

		try {

			List<Role> roleList = roleRepository.findByDepartmentId(departmentId);

			return ResponseEntity.ok(roleList);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	@GetMapping("/getRolesByRoleId/{roleId}")
	public ResponseEntity<?> getRolesByRoleId(@PathVariable int roleId) {

		try {

			Role roleList = roleRepository.findByRoleId(roleId);

			return ResponseEntity.ok(roleList);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	
	//Lead APIs
	@PostMapping("/createLead")
	public ResponseEntity< ?> createCompany(@RequestBody Leads lead){
		
		try {
			lead.setEmployeeId(0);
			lead.setCompanyId(company.getCompanyId());
			leadRepositroy.save(lead);
			
			return ResponseEntity.ok(lead);
			
		}catch(Exception e) {
			
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	
	@GetMapping("/getLeads/{page}/{size}")
	public ResponseEntity<?> getLeads(@PathVariable int page , @PathVariable int size,@RequestParam(defaultValue = "") String customerName) {

		try {
			Pageable pageable = PageRequest.of(page, size,Sort.by("leadId").descending());
			Page<Leads> LeadsPage = leadRepositroy.findByCompanyIdAndCustomerNameContainingIgnoreCase(company.getCompanyId(), pageable,customerName);
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("Leads", LeadsPage.getContent());
			response.put("currentPage", LeadsPage.getNumber());
			response.put("totalItems", LeadsPage.getTotalElements());
			response.put("totalPages", LeadsPage.getTotalPages());

			return ResponseEntity.ok(response);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	@GetMapping("/getLead/{leadId}")
	public ResponseEntity< ?> updateLead(@PathVariable long leadId){
		
		try {
			Leads  lead= leadRepositroy.findLeadByLeadId(leadId);
			
			return ResponseEntity.ok(lead);
			
		}catch(Exception e) {
			
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	@PutMapping("/updateLead")
	public ResponseEntity< ?> updateLead(@RequestBody Leads lead){
		
		try {
			lead.setEmployeeId(0);
			lead.setCompanyId(company.getCompanyId());
			leadRepositroy.save(lead);
			
			return ResponseEntity.ok(lead);
			
		}catch(Exception e) {
			
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	@PostMapping("/createLeadStatus")
	public ResponseEntity< ?> createLeadStatus(@RequestBody LeadStatus leadStatus){
		
		try {
			
			leadStatus.setCompanyId(company.getCompanyId());
			leadStatusRepository.save(leadStatus);
			
			return ResponseEntity.ok(leadStatus);
			
		}catch(Exception e) {
			
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	@GetMapping("/getLeadStatus")
	public ResponseEntity< ?> getLeadStatus(){
		
		try {
			List<LeadStatus> leadStatus=leadStatusRepository.findByCompanyId(company.getCompanyId());
			return ResponseEntity.ok(leadStatus);
			
		}catch(Exception e) {
			
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	
	@PutMapping("/updateLeadStatus")
	public ResponseEntity<?> updateLeadStatus(@RequestBody Map<String, LeadStatus> leadStatusMap) {

		try {
			LeadStatus leadStatusFirst = leadStatusMap.get("leadStatusFirst");
			LeadStatus leadStatusSecond = leadStatusMap.get("leadStatusSecond");

			leadStatusRepository.save(leadStatusFirst);
			leadStatusRepository.save(leadStatusSecond);
			return ResponseEntity.ok(leadStatusMap);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	
	
	@PostMapping("/deleteLeadStatus/{leadStatusId}")
	public ResponseEntity< ?> deleteLeadStatus(@PathVariable long leadStatusId){
		
		try {
			
			leadStatusRepository.deleteById(leadStatusId);
			
			return ResponseEntity.ok("Deleted Succefullys");
			
		}catch(Exception e) {
			
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

}
