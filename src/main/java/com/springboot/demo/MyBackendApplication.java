package com.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBackendApplication.class, args);
		System.out.println("working");
	}

}
