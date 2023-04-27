package com.springboot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.demo.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	@Query("select e from Employee e where e.user.username=?1")
	Employee getEmployeeByUsername(String username);

}
