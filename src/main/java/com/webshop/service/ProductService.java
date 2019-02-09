package com.webshop.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.webshop.model.Product;

public interface ProductService {

	boolean logTrial();
	List<Product> getAllProducts(Integer pageNumber);
	Product getById(ObjectId id);
	List<Product> getByPriceAndCategory(Double min, Double max, ObjectId category_id, Integer sortOrder, Integer pageNumber);
	//List<Product> getByCategory(String category);
	Product updateProduct(Product product);
	Product addProduct(Product product);
	boolean deleteProduct(ObjectId id);
	boolean changeQuantityOfProduct(ObjectId product_id, Integer bougthQuantity);
	Integer countProducts();
}
