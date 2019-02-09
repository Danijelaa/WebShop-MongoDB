package com.webshop.model;

import org.bson.types.ObjectId;

public class Product {

	private ObjectId id;
	private String title;
	private Double price;
	private Integer quantity;
	//private String category;
	private ObjectId category_id;
	
	
	
	public ObjectId getCategory_id() {
		return category_id;
	}
	public void setCategory_id(ObjectId category_id) {
		this.category_id = category_id;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	/*public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}*/
	
	
}
