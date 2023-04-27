package com.springboot.demo.controller;

import java.security.Principal;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;

import com.springboot.demo.dto.EmployeeResponseDto;
import com.springboot.demo.dto.MessageDto;
import com.springboot.demo.enums.EmployeeAccessStatusEnum;
import com.springboot.demo.enums.RequsetStatusEnum;
import com.springboot.demo.model.Admin;
import com.springboot.demo.model.Employee;
import com.springboot.demo.model.Request;
import com.springboot.demo.model.User;
import com.springboot.demo.repository.AdminRepository;
import com.springboot.demo.repository.EmployeeRepository;
import com.springboot.demo.repository.RequestRepository;
import com.springboot.demo.repository.UserRepository;


@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = {"http://localhost:8200"})
public class EmployeeController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RequestRepository requestRepository;
	
	/*
	 Path: /api/employee/add
	 */
	@PostMapping("/add/{id}")
	public ResponseEntity<MessageDto> addEmployee(@PathVariable("id") Long adminId, 
											  @RequestBody Employee employee
											  ) { 
		
		/* Validate ManagerID */
		Optional<Admin> optional = adminRepository.findById(adminId);  
		if(!optional.isPresent())
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto("Admin ID is Invalid"));
		
		Admin admin = optional.get();
		
		/* Prepare Employee Object */
		employee.setAdmin(admin);
		employee.setCreatedAt(LocalDate.now());
		employee.setJobTitle("EMPLOYEE");
		
		/* Save User info in DB */
		User user = employee.getUser(); 
		user.setRole("EMPLOYEE");
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setUsername(user.getUsername().toLowerCase());
		user  = userRepository.save(user); //id,username,password,role
		
		/* Attach User to Employee and Save Employee */
		employee.setUser(user);
		
		/* Set the access type */
		employee.setAccessType(EmployeeAccessStatusEnum.PENDING);
		employeeRepository.save(employee);
		
		return  ResponseEntity.status(HttpStatus.OK).body(new MessageDto("Employee Sign up Success"));
		
	}
	
	@PutMapping("/status/{status}/{id}") //PENDING --> GRANTED, DENIED
	public ResponseEntity<String> EmployeeAccessStatusUpdate(@PathVariable("status") String status, 
															 @PathVariable("id") Long empId) {
		/* Convert status to Enum*/
		EmployeeAccessStatusEnum accessStatus= null;
		try {
			accessStatus = EmployeeAccessStatusEnum.valueOf(status);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown Status");
		}
		/* Validate Employee ID and fetch EMployee Details */
		Optional<Employee> optional = employeeRepository.findById(empId);
		
		if(!optional.isPresent())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee ID is Invalid");
		
		Employee employee = optional.get();
		
		/* Update the status of this employee */
		employee.setAccessType(accessStatus);
		
		/*Save the employee */
		employeeRepository.save(employee);
		
		return ResponseEntity.status(HttpStatus.OK).body("Employee Status updated");
	}
	
	//Path: /api/employee/details
	@GetMapping("/details/{id}")
	public EmployeeResponseDto  getEmployeeDetails(@PathVariable("id") Long empId) {
		/* Validate Employee ID and fetch EMployee Details */
		Optional<Employee> optional = employeeRepository.findById(empId);
		
		Employee employee = optional.get();
		
		EmployeeResponseDto dto = new EmployeeResponseDto();
		dto.setId(employee.getId());
		dto.setName(employee.getName());
		dto.setJobTitle(employee.getJobTitle());
		dto.setUsername(employee.getUser().getUsername());
		dto.setCreatedAt(employee.getCreatedAt());
		return dto; 
	}

	@PutMapping("/requestStatus/{status}/{id}") //PENDING --> GRANTED, DENIED
	public ResponseEntity<String> RequestStatusUpdate(@PathVariable("requestStatus") String status, 
															 @PathVariable("id") Long requestId) {
		/* Convert status to Enum*/
		RequsetStatusEnum requestStatus= null;
		try {
			requestStatus = RequsetStatusEnum.valueOf(status);
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown Status");
		}
		/* Validate Request ID and fetch request Details */
		Optional<Request> optional = requestRepository.findById(requestId);
		
		if(!optional.isPresent())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request ID is Invalid");
		
		Request request = optional.get();
		
		/* Update the status of this employee */
		request.setRequestStatus(requestStatus);
		
		/*Save the employee */
		requestRepository.save(request);
		
		return ResponseEntity.status(HttpStatus.OK).body("Request Status updated");
	}
}


