package com.webshop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.webshop.model.Category;
import com.webshop.service.CategoryService;
import com.webshop.support.CreateConnection;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Override
	public Category findById(ObjectId id){
		Category category=null;
		MongoDatabase database=CreateConnection.getConnection();
		category=database.getCollection("category", Category.class).find(Filters.eq("_id", id)).first();
		return category;
	}

	@Override
	public List<Category> getAll() {
		List<Category> categories=new ArrayList<>();
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Category> categoryCollection=database.getCollection("category", Category.class);
		MongoCursor<Category> cursor=categoryCollection.find().iterator();
		while(cursor.hasNext()){
			Category category=cursor.next();
			categories.add(category);
		}
		return categories;
	}

}
