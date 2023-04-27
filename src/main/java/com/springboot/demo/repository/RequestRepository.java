package com.springboot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.demo.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long>{
	
	

}
