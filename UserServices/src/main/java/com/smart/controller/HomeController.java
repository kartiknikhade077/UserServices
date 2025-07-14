package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smart.config.UserDetailServiceImp;
import com.smart.dto.CompanyDto;
import com.smart.dto.EmployeeDto;
import com.smart.dto.ErrorMessage;
import com.smart.dto.UserDto;
import com.smart.entity.AuthRequest;
import com.smart.entity.Employee;
import com.smart.entity.ModuleAccess;
import com.smart.entity.User;
import com.smart.helper.SoftwareValidityExpiredException;
import com.smart.repository.CompanyRepository;
import com.smart.repository.EmployeeRepository;
import com.smart.repository.ModuleAccessRepository;
import com.smart.repository.UserRepository;
import com.smart.service.JwtUtil;
import com.smart.service.UserService;

@Controller
@RestController
public class HomeController {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
	private UserDetailServiceImp userDetailServiceImp;
    
    @Autowired
	private UserRepository userRepository;
    
    @Autowired
	private EmployeeRepository employeeRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ModuleAccessRepository moduleAccessRepository;
    

    @GetMapping("/")
    public String welcome() {
        return "Welcome to Home !!";
    }
    
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }
    

    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRequest authRequest) {
        userService.saveUser(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            
            
            final UserDetails userDetails = userDetailServiceImp.loadUserByUsername(authRequest.getUsername());
           
            User user = userRepository.getUserByUserName(authRequest.getUsername());
              
          
    		
    		if (user == null) {
    			//session.setAttribute("message", new com.smart.helper.Message("user not found of email", "danger"));
    			return ResponseEntity.ok(new ErrorMessage("user not found of email or invalid credential"));
    		}
            

            final String token = jwtUtil.generateToken(userDetails);
            UserDto uDto = null;
            
            if(user.getRole().equals("ROLE_COMPANY")) {
            	CompanyDto company=companyRepository.findCompanyDtoByEmail(user.getEmail());
            	uDto = new UserDto(user.getId(),company.getCompanyId(), user.getName(), user.getEmail(), user.getRole(),user.isEnabled(), token,false,false,false);
            }else if(user.getRole().equals("ROLE_EMP")){
            	EmployeeDto emp = employeeRepository.findByEmailWithAccess(user.getEmail());
            	uDto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole(),user.isEnabled(),emp.getCompanyId(), token,emp.isLeadAccess(),emp.isTemplateAccess(),emp.isEmailAccess());
            }else {
            	uDto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole(),user.isEnabled(),user.getId(), token,false,false,false);
            	
            }
            return ResponseEntity.ok(uDto);
        }catch (SoftwareValidityExpiredException s) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your subscription is expired. Please renew your subscription.");
        } catch (UsernameNotFoundException u) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (AuthenticationException ex) {
        	//ex.printStackTrace();
        	//System.err.println("Error :" + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }  catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during authentication: " + e.getMessage());
        }
    }
    


}
