package com.springboot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.demo.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	/*
	 * @Query("select a from Admin a where a.user.email=?1") Admin
	 * findAdminDetails(String username);
	 */

}
