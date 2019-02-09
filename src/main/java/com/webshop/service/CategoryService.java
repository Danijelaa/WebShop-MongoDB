package com.webshop.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.webshop.model.Category;

public interface CategoryService {

	Category findById(ObjectId id);
	List<Category> getAll();
}
