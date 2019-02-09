package com.webshop.service;

import java.util.List;

import org.bson.types.ObjectId;

import com.webshop.model.CartItem;
import com.webshop.model.ShoppingCart;

public interface ShoppingCartService {

	ShoppingCart addShoppingCart(ShoppingCart shoppingCart);
	List<ShoppingCart> getUsersShoppingCarts(String user_id);
	ShoppingCart findById(ObjectId id);
}
