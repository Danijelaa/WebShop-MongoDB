package com.webshop.service.impl;

import org.springframework.stereotype.Service;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.webshop.model.User;
import com.webshop.service.UserService;
import com.webshop.support.CreateConnection;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User findByUserNameAndPassword(String username, String password) {
		User user=null;
		MongoDatabase database=CreateConnection.getConnection();
		user=database.getCollection("user", User.class).find(Filters.and(Filters.eq("username", username), Filters.eq("password", password))).first();
		return user;
	}

}
