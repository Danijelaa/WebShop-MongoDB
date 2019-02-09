package com.webshop.service;

import org.bson.types.ObjectId;

import com.webshop.model.User;

public interface UserService {

	User findByUserNameAndPassword(String username, String password);
}
