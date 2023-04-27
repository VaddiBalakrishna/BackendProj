package com.springboot.demo.controller;


import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.demo.model.User;
import com.springboot.demo.repository.UserRepository;
import com.springboot.demo.service.UserService;


@RestController
@RequestMapping("/api/user")
@CrossOrigin("http://localhost:8200/")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public User login(Principal principal) { 
		String username = principal.getName();
		User user = userRepository.findByUsername(username);
		user.setPassword("----------");
		return user;
	}
	
	@PutMapping("updateProfile/{userId}")
	public ResponseEntity<String> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedUser) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setEmail(updatedUser.getEmail());
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body("Profile Updated Successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email) {

		String response = userService.forgotPassword(email);

		if (!response.startsWith("Invalid")) {
			response = "http://localhost:8080/reset-password?token=" + response;
		}
		return response;
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password) {

		return userService.resetPassword(token, password);
	}
	
}
