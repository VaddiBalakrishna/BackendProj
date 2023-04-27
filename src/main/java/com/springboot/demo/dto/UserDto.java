package com.springboot.demo.dto;

import org.springframework.stereotype.Component;

@Component
public class UserDto {
	private String email;
	private String role;

	public UserDto() {
	}

	public UserDto(String email, String role) {
		this.email = email;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDto [email=" + email + ", role=" + role + "]";
	}

}
