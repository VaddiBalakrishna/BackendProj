package com.springboot.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.demo.enums.RequsetStatusEnum;
import com.springboot.demo.exception.ResourceNotFoundException;
import com.springboot.demo.model.Customer;
import com.springboot.demo.model.Employee;
import com.springboot.demo.model.Request;
import com.springboot.demo.repository.CustomerRepository;
import com.springboot.demo.repository.EmployeeRepository;
import com.springboot.demo.repository.RequestRepository;

@RestController
@RequestMapping("/requests")
@CrossOrigin("http://localhost:8200/")
public class RequestController {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping("/add")
	public ResponseEntity<String> addRequest(@RequestBody Request request) {
		/*
		 * Employee employee =
		 * employeeRepository.findById(request.getEmployee().getId()) .orElseThrow(() ->
		 * new ResourceNotFoundException("Employee not found"));
		 */
		Customer customer = customerRepository.findById(request.getCustomer().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		// request.setEmployee(employee);
		request.setCustomer(customer);
		request.setRequestStatus(RequsetStatusEnum.PENDING);
		requestRepository.save(request);
		return ResponseEntity.ok("Request added successfully");
	}

	@GetMapping("/all")
	public ResponseEntity<List<Request>> getRequests() {
		List<Request> requests = requestRepository.findAll();
		return ResponseEntity.ok(requests);
	}
}
