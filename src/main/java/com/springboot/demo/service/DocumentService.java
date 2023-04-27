package com.springboot.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.demo.model.Customer;
import com.springboot.demo.model.Document;
import com.springboot.demo.repository.CustomerRepository;
import com.springboot.demo.repository.DocumentRepository;



@Service
public class DocumentService {
	@Autowired
	private DocumentRepository docRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private DocumentRepository documentRepository;

	public Document saveFile(MultipartFile file,Long customer_id) {
		String docname = file.getOriginalFilename();
		try {
			Document doc = new Document(docname, file.getContentType(), file.getBytes());

			Customer customer = customerRepository.findById(customer_id).get();
			//Document doc = new Document();
			doc.setCustomer(customer);
			documentRepository.save(doc);

			return docRepository.save(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Optional<Document> getFile(Integer fileId) {
		return docRepository.findById(fileId);
	}

	public List<Document> getFiles() {
		return docRepository.findAll();
	}
}
