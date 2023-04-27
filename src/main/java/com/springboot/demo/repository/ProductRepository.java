package com.springboot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	//@Query("select p from Product p where p.admin.adminId=?1")
	//Product addProduct(Long adminId, Product product);

}
