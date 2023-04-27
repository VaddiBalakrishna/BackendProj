package com.springboot.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.demo.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	@Query("select c from Customer c where c.user.username=?1")
	Customer getCustomerDetails(String username);

	@Query("select c from Customer c where c.id=?1")
	Optional<Customer> findById(String customerId);
	
	@Query("select c from Customer c where c.user.username=?1")
	Customer getByUsername(String username);

}
