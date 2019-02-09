package com.webshop.model;

import org.bson.types.ObjectId;

public class CartItem {

	private ObjectId product_id;
	private Integer quantity;
	
	public ObjectId getProduct_id() {
		return product_id;
	}
	public void setProduct_id(ObjectId product_id) {
		this.product_id = product_id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	
}
