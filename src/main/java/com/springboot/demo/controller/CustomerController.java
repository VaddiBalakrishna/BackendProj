package com.springboot.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.demo.dto.ChangePasswordRequest;
import com.springboot.demo.dto.CustomerDto;
import com.springboot.demo.dto.EmployeeResponseDto;
import com.springboot.demo.model.Customer;
import com.springboot.demo.model.Employee;
import com.springboot.demo.model.User;
import com.springboot.demo.repository.CustomerRepository;
import com.springboot.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("http://localhost:8200/")
public class CustomerController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/add")
	public ResponseEntity<Object> addCustomer(@RequestBody CustomerDto customerDto) {
		/* Fetch User info from Customer */
		User user = new User();
		user.setUsername(customerDto.getUsername());
		user.setPassword(customerDto.getPassword());
		user.setEmail(customerDto.getEmail());
		user.setRole("CUSTOMER");

		/* Encrypt the password */
		String encryptedPassword = encoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);

		/*
		 * Save this User in DB(you get the ID assigned) and attach the User again to
		 * customer
		 */
		user = userRepository.save(user); // had an ID
		Customer customer = new Customer();
		customer.setName(customerDto.getName());
		customer.setCompanyName(customerDto.getCompanyName());
		customer.setEmail(customerDto.getEmail());
		customer.setMobileNo(customerDto.getMobileNo());
		customer.setUser(user);

		/* Save the Customer */
		customerRepository.save(customer);

		// response.setMsg("Sign Up Success");
		return ResponseEntity.status(HttpStatus.OK).body("Sign Up Success");
	}

	@PutMapping("/change-password/{username}")
	public ResponseEntity<Object> changePassword(@PathVariable String username,
			@RequestBody ChangePasswordRequest changePasswordRequest) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		if (!encoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect old password");
		}

		user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
	}

	// Path: /api/customer/details
	@GetMapping("/details/{customerId}")
	public CustomerDto getCustomerDetails(@PathVariable("customerId") Long customerId) {
		/* Validate Customer ID and fetch Customer Details */
		Optional<Customer> optional = customerRepository.findById(customerId);

		Customer customer = optional.get();

		CustomerDto dto = new CustomerDto();
		dto.setId(customer.getId());
		dto.setName(customer.getName());
		dto.setEmail(customer.getEmail());
		dto.setCompanyName(customer.getCompanyName());
		dto.setPanCard(customer.getPanCard());
		dto.setMobileNo(customer.getMobileNo());
		dto.setUsername(customer.getUser().getUsername());
		return dto;
	}
	
	@GetMapping("/custdetails/{username}")
	public CustomerDto getCustomerDetails(@PathVariable("username") String username ) {
		Customer customer = customerRepository.getByUsername(username);
		CustomerDto dto = new CustomerDto();
		
		dto.setId(customer.getId());
		dto.setName(customer.getName());
		dto.setEmail(customer.getEmail());
		dto.setCompanyName(customer.getCompanyName());
		dto.setPanCard(customer.getPanCard());
		dto.setMobileNo(customer.getMobileNo());
		dto.setUsername(customer.getUser().getUsername());
		return dto;
		
	}

	@PutMapping("updateProfile/{customerId}")
	public ResponseEntity<String> updateCustomerProfile(@PathVariable Long customerId,
			@RequestBody Customer updatedCustomer) {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			customer.setCompanyName(updatedCustomer.getCompanyName());
			customer.setMobileNo(updatedCustomer.getMobileNo());
			customer.setEmail(updatedCustomer.getEmail());
			customer.setPanCard(updatedCustomer.getPanCard());
			customerRepository.save(customer);
			return ResponseEntity.status(HttpStatus.OK).body("Profile Updated Successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/*
	 * @PostMapping("/forgot-password") public ResponseEntity<Object>
	 * forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
	 * User user = userRepository.findByEmail(forgotPasswordRequest.getEmail()); if
	 * (user == null) { return
	 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found"); }
	 * 
	 * String token = UUID.randomUUID().toString();
	 * user.setResetPasswordToken(token);
	 * user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
	 * userRepository.save(user);
	 * 
	 * // Send email with password reset link String resetPasswordLink =
	 * "http://localhost:8080/reset-password?token=" + token;
	 * emailService.sendResetPasswordEmail(forgotPasswordRequest.getEmail(),
	 * resetPasswordLink);
	 * 
	 * return
	 * ResponseEntity.status(HttpStatus.OK).body("Password reset link sent to email"
	 * ); }
	 */

}

