package com.webshop.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoCollection;
import com.webshop.model.CartItem;
import com.webshop.model.ShoppingCart;
import com.webshop.model.User;
import com.webshop.service.ProductService;
import com.webshop.service.ShoppingCartService;
import com.webshop.support.CartItemDTOToCartItem;
import com.webshop.support.CreateConnection;
import com.webshop.support.ShoppingCartDTOToShoppingCart;
import com.webshop.support.ShoppingCartToShoppingCartDTOOut;
import com.webshop.support.Validation;
import com.webshop.web.dto.CartItemDTO;
import com.webshop.web.dto.ShoppingCartDTO;

@RestController
@RequestMapping(value="/shopping-carts")
public class ShoppingCartController {
	
	private MongoCollection<Document> log=CreateConnection.log();
	@Autowired
	ProductService ps;
	@Autowired
	Validation validation;
	@Autowired
	ShoppingCartService scs;
	@Autowired
	CartItemDTOToCartItem toCartItem;
	@Autowired
	ShoppingCartToShoppingCartDTOOut toShoppingCartDtoOut;
	@Autowired
	ShoppingCartDTOToShoppingCart toShoppingCart;
	
	@RequestMapping(method=RequestMethod.GET)
	ResponseEntity<?> getShoppingCarts(HttpSession session){
		User user=(User) session.getAttribute("user");
		if(user==null){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		List<ShoppingCart> shoppingCarts=null;
		try {
			shoppingCarts=scs.getUsersShoppingCarts(user.getUsername());
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(toShoppingCartDtoOut.convert(shoppingCarts), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	ResponseEntity<?> createShoppingCart(@RequestBody List<CartItemDTO> cartItemDtos, HttpSession session){
		User user=(User) session.getAttribute("user");
		if(user==null){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if(cartItemDtos.size()==0){
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Can not create shopping cart out of none cart items.", hrs, HttpStatus.BAD_REQUEST);
		}
		boolean validated=validation.validateCartItems(cartItemDtos);
		if(!validated){
			HttpHeaders hrs=new HttpHeaders();
			hrs.add("Content-Type", "text/plain");
			return new ResponseEntity<>("Parameters 'product_id' or 'quantity' ivalid or missing for one or more cart items.\nQuantity can not be less then zero.", hrs, HttpStatus.BAD_REQUEST);
		}
		cartItemDtos=validation.removeDuplicateItems(cartItemDtos);
		
		ShoppingCart shoppingCart=null;
		try {
			shoppingCart = toShoppingCart.convert(cartItemDtos);
			shoppingCart.setUser_id(user.getUsername());
			shoppingCart=scs.addShoppingCart(shoppingCart);
			if(shoppingCart==null){
				HttpHeaders hrs=new HttpHeaders();
				hrs.add("Content-Type", "text/plain");
				return new ResponseEntity<>("Could not create shopping cart.\nSome of the products with given id not found or there are not enough products.", hrs, HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(toShoppingCartDtoOut.convert(shoppingCart),HttpStatus.CREATED);
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
