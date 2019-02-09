package com.webshop.model;

import org.bson.types.ObjectId;

public class Category {

	private ObjectId id;
	private String title;
	
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
	
	
}
