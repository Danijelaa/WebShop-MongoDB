package com.webshop.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webshop.model.User;
import com.webshop.service.UserService;

@RestController
@RequestMapping(value="/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	ResponseEntity<String> login(@RequestBody User userLogin, HttpSession session){
		User user=userService.findByUserNameAndPassword(userLogin.getUsername(), userLogin.getPassword());
		if(user==null){
			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders hrs=new HttpHeaders();
		hrs.add("Content-Type", "text/plain");
		session.setAttribute("user", user);
		return new ResponseEntity<>(user.getUsername(),hrs, HttpStatus.OK);
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	ResponseEntity<String> logout(HttpSession session){
		session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
