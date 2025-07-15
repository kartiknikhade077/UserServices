package com.smart.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smart.entity.User;
import com.smart.helper.SoftwareValidityExpiredException;
import com.smart.repository.EmployeeRepository;
import com.smart.repository.UserRepository;

@Service
public class UserDetailServiceImp implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetchin user from data base
		User user = userRepository.getUserByUserName(username);

		LocalDate currentDate = LocalDate.now();

		if (user == null) {
			throw new UsernameNotFoundException("user not found of email");
		}else if (user != null && "ROLE_COMPANY".equals(user.getRole()) && currentDate.isAfter(user.getExpirayDate())) {
			throw new SoftwareValidityExpiredException("Your Subcription is Expired, Please Get Subcription");
		}else if (user != null && "ROLE_EMP".equals(user.getRole()) && currentDate.isAfter(user.getExpirayDate())) {
			throw new SoftwareValidityExpiredException("Your Company Subcription is Expired");
		}else if (user != null && "ROLE_EMP".equals(user.getRole()) && user.isEnabled()== true) {
			throw new SoftwareValidityExpiredException("Access Denied, Please Contact To Your Company");
		}
		
		CustomUserDetail customUserDetail = new CustomUserDetail(user);

		return customUserDetail;
	}
}
