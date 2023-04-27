package com.springboot.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
import org.springframework.web.bind.annotation.RestController;

import com.springboot.demo.dto.AdminDto;
import com.springboot.demo.dto.ChangePasswordRequest;
import com.springboot.demo.dto.CustomerDto;
import com.springboot.demo.dto.EmployeeResponseDto;
import com.springboot.demo.model.Admin;
import com.springboot.demo.model.Customer;
import com.springboot.demo.model.Employee;
import com.springboot.demo.model.Product;
import com.springboot.demo.model.User;
import com.springboot.demo.repository.AdminRepository;
import com.springboot.demo.repository.CustomerRepository;
import com.springboot.demo.repository.EmployeeRepository;
import com.springboot.demo.repository.ProductRepository;
import com.springboot.demo.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("http://localhost:8200/")
public class AdminController {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CustomerRepository customerRepository; 
	
	@Autowired
	private ProductRepository productRepository;

	@PostMapping("/add")
	public ResponseEntity<Object> addAdmin(@RequestBody AdminDto adminDto) {
		/* Fetch User info from admin */
		User user = new User();
		user.setUsername(adminDto.getUsername());
		user.setPassword(adminDto.getPassword());
		user.setRole("ADMIN");

		/* Encrypt the password */
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepository.save(user); // had an ID

		Admin admin = new Admin();
		admin.setCreatedAt(adminDto.getCreatedAt());
		admin.setEmail(adminDto.getEmail());
		admin.setJobTitle("ADMIN");
		admin.setUser(user);

		/* Save the Admin */
		adminRepository.save(admin);

		// response.setMsg("Sign Up Success");
		return ResponseEntity.status(HttpStatus.OK).body("Admin added Successfully");
	}

	@PutMapping("/change-password/{username}")
	public ResponseEntity<Object> changePassword(@PathVariable String username,
			@RequestBody ChangePasswordRequest changePasswordRequest) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
		}

		if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect old password");
		}

		user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
	}

	@GetMapping("/allemployees")
	public List<EmployeeResponseDto> getAllEmployees() {
		List<Employee> list = employeeRepository.findAll();
		List<EmployeeResponseDto> listDto = new ArrayList<>();
		for (Employee e : list) {
			EmployeeResponseDto dto = new EmployeeResponseDto(); // id,name -- //200X
			dto.setId(e.getId()); // 200X (id)
			dto.setName(e.getName());// 200X (id,name)
			dto.setJobTitle(e.getJobTitle());
			dto.setCreatedAt(e.getCreatedAt());
			dto.setUsername(e.getUser().getUsername());
			listDto.add(dto); // [100X,200X]
		}
		return listDto;
	}

	@GetMapping("/allcustomers")
	public List<CustomerDto> getAllCustomers() {
		List<Customer> list = customerRepository.findAll();
		List<CustomerDto> listDto = new ArrayList<>();
		for (Customer c : list) {
			CustomerDto dto = new CustomerDto(); // id,name -- //200X
			dto.setId(c.getId()); // 200X (id)
			dto.setName(c.getName());// 200X (id,name)
			dto.setCompanyName(c.getCompanyName());
			dto.setMobileNo(c.getMobileNo());
			dto.setPanCard(c.getPanCard());
			dto.setEmail(c.getEmail());
			dto.setUsername(c.getUser().getUsername());
			listDto.add(dto); // [100X,200X]
		}
		return listDto;
	}
	
	@PostMapping("/product/{id}")
	public ResponseEntity addProduct(@PathVariable("id") Long admin_id,

			@RequestBody Product product) {

		Admin admin = adminRepository.findById(admin_id).get();
		product.setAdmin(admin);
		product.getName();
		product.getDescription();
		product.getAdmin();
		product.getRate();
		product.getSpecification();
		productRepository.save(product);
		return ResponseEntity.status(HttpStatus.OK).body("Product Added Successfully");

	}

	/*
	 * @PostMapping("/addProduct/{adminId}") public ResponseEntity<Product>
	 * addProduct(@PathVariable Long adminId, @RequestBody Product product) {
	 * Product savedProduct = productRepository.addProduct(adminId, product); return
	 * new ResponseEntity<>(savedProduct, HttpStatus.CREATED); }
	 */
}

