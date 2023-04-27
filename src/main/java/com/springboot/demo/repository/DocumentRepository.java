package com.springboot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.demo.model.Document;



public interface DocumentRepository  extends JpaRepository<Document,Integer>{

}
