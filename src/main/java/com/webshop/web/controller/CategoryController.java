package com.webshop.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoCollection;
import com.webshop.model.Category;
import com.webshop.service.CategoryService;
import com.webshop.support.CategoryToCategoryDTO;
import com.webshop.support.CreateConnection;
import com.webshop.web.dto.CategoryDTO;

@RestController
@RequestMapping(value="/categories")
public class CategoryController {

	private MongoCollection<Document> log=CreateConnection.log();
	
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryToCategoryDTO toCategoryDto;
	
	@RequestMapping(method=RequestMethod.GET)
	ResponseEntity<List<CategoryDTO>> getAll(){
		List<Category> categories=null;
		try {
			categories = categoryService.getAll();
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(toCategoryDto.convert(categories), HttpStatus.OK);
	}
}
