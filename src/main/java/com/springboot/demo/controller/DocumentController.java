package com.springboot.demo.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.demo.model.Document;
import com.springboot.demo.repository.CustomerRepository;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.service.DocumentService;


@RestController
@RequestMapping("/api/doc")
@CrossOrigin("http://localhost:8200/")
public class DocumentController {

	@Autowired 
	private DocumentService docStorageService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@GetMapping("/")
	public String get(Model model) {
		List<Document> docs = docStorageService.getFiles();
		model.addAttribute("docs", docs);
		return "doc";
	}
	
	@PostMapping("/uploadFiles/{customer_id}")
	public String uploadMultipleFiles(@PathVariable("customer_id")  Long customer_id, @RequestParam("files") MultipartFile[] files) throws Exception {
	
		for (MultipartFile file: files) {
			docStorageService.saveFile(file, customer_id);
			
		}
		return "File Has uploaded successfully";
	}
	@GetMapping("/downloadFile/{fileId}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
		Document doc = docStorageService.getFile(fileId).get();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(doc.getDocType()))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment:filename=\""+doc.getDocName()+"\"")
				.body(new ByteArrayResource(doc.getData()));
	}
	
}
