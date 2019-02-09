package com.webshop.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoCollection;
import com.webshop.model.Product;
import com.webshop.model.User;
import com.webshop.service.ProductService;
import com.webshop.support.CreateConnection;
import com.webshop.support.ProductDTOToProduct;
import com.webshop.support.ProductToProductDTO;
import com.webshop.support.Validation;
import com.webshop.web.dto.ProductDTO;

@RestController
@RequestMapping(value="/products")
public class ProductController {

	private MongoCollection<Document> log=CreateConnection.log();
	
	@Autowired
	private ProductToProductDTO toProductDto;
	@Autowired
	private ProductService ps;
	@Autowired
	private Validation validation;
	@Autowired
	private ProductDTOToProduct toProduct;
	/*@RequestMapping(method=RequestMethod.GET)
	ResponseEntity<List<ProductDTO>> allProducts(@RequestParam Integer pageNumber){
		List<Product> products=ps.getAllProducts(pageNumber);
		return new ResponseEntity<>(toProductDto.convert(products), HttpStatus.OK);
	}*/
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	ResponseEntity<?> getOne(@PathVariable String id){
		Product product=null;
		try {
			product = ps.getById(new ObjectId(id));
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		if(product==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(toProductDto.convert(product), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	ResponseEntity<?> findByPriceAndCategory(@RequestParam(required=false) Double min, @RequestParam(required=false) Double max, @RequestParam(required=false) String category_id, @RequestParam(required=false) Integer sortOrder, @RequestParam(required=false) Integer page){
		if(page==null || page<=0) {
			page=1;
		}
		List<Product> products=null;
		
		try {
			if(category_id==null) {
				products=ps.getAllProducts(page);
				return new ResponseEntity<>(toProductDto.convert(products), HttpStatus.OK);
			}
			products=ps.getByPriceAndCategory(min, max, new ObjectId(category_id), sortOrder, page);
			return new ResponseEntity<>(toProductDto.convert(products), HttpStatus.OK);
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/{id}")
	ResponseEntity<?> updateProduct(@RequestBody ProductDTO updatedProduct, @PathVariable String id, HttpSession session){
		User moderator=(User) session.getAttribute("user");
		if(moderator==null || !moderator.getUsername().equals("moderator")){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if(!updatedProduct.getId().equals(id)) {
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Ids do not match.", hrs, HttpStatus.BAD_REQUEST);
		}
/*		if(!ObjectId.isValid(id)){
			return new ResponseEntity<String>("Invalid hexadecimal representation of an ObjectId!", HttpStatus.BAD_REQUEST);
		}*/
		boolean validated=validation.validateUpdatedProduct(updatedProduct);
		if(!validated) {
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Parameters: id, price, quantity and category_id can not miss.\nQuantity can not be less then zero.\nPrice can not be zero or less.", hrs, HttpStatus.BAD_REQUEST);
		}
		
		try {
			Product product=toProduct.convert(updatedProduct);
			product=ps.updateProduct(product);
			return new ResponseEntity<>(toProductDto.convert(product), HttpStatus.OK);
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST)
	ResponseEntity<?> addProduct(@RequestBody ProductDTO newProduct, HttpSession session){
		User moderator=(User) session.getAttribute("user");
		if(moderator==null || !moderator.getUsername().equals("moderator")){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		boolean validated=validation.validateNewProduct(newProduct);
		if(!validated) {
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Parameters: title, price, quantity and category_id can not miss.\nQuantity can not be less then zero.\nPrice can not be zero or less.", hrs, HttpStatus.BAD_REQUEST);
		}
		try {
			Product product=toProduct.convert(newProduct);
			product=ps.addProduct(product);
			return new ResponseEntity<>(toProductDto.convert(product), HttpStatus.CREATED);
			
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
/*		if(!added) {
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Sorry. :( \nCould not add product.", hrs, HttpStatus.BAD_REQUEST);
		}*/
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	ResponseEntity<?> deleteProduct(@PathVariable String id, HttpSession session){
		User moderator=(User) session.getAttribute("user");
		if(moderator==null || !moderator.getUsername().equals("moderator")){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	/*	Product product=ps.getById(new ObjectId(id));
		if(product==null) {
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Can not delete non-existent product.", hrs, HttpStatus.NOT_FOUND);
		}*/
		boolean deleted=false;
		try {
			deleted = ps.deleteProduct(new ObjectId(id));
			if(!deleted) {
				HttpHeaders hrs=new HttpHeaders();
				hrs.add("Content-Type", "text/plain");
				return new ResponseEntity<>("Could not delete product.", hrs, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
}
