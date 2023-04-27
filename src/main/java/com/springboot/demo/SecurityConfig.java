package com.springboot.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.demo.service.MyUserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> {
			try {
				authorize.requestMatchers("/login").permitAll()
				         .requestMatchers(HttpMethod.POST,"/api/customer/add").permitAll()
				         .requestMatchers("/add/product").hasAuthority("ADMIN")
				         .requestMatchers("/add/employee").hasAuthority("ADMIN")
				         .requestMatchers("/api/admin/allcustomers").hasAuthority("ADMIN")
				         .requestMatchers("updateProfile/{customerId}").hasAuthority("CUSTOMER")
				         .requestMatchers("/add/request").hasAuthority("CUSTOMER")
						 .requestMatchers("/get/user/details").permitAll()
						 .requestMatchers("/get/customer/{id}").permitAll()
						 .anyRequest()
						 .permitAll()
						 .and()
						 .csrf().disable();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		})
			.httpBasic(Customizer.withDefaults());
		
		http.authenticationProvider(getDBAuth());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider getDBAuth() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setPasswordEncoder(getPasswordEncoder());
		dao.setUserDetailsService(myUserDetailsService);
		return dao;
	}

}
