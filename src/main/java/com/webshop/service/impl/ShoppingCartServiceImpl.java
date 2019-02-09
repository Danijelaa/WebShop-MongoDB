package com.webshop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.webshop.model.CartItem;
import com.webshop.model.Product;
import com.webshop.model.ShoppingCart;
import com.webshop.service.ProductService;
import com.webshop.service.ShoppingCartService;
import com.webshop.support.CreateConnection;
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	ProductService ps;

	@Override
	public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		List<CartItem> updatedProducts=new ArrayList<>();
		boolean enoughQuantity=true;
		double amount=0.0;
		for(CartItem ci:shoppingCart.getItems()){
			Product product=ps.getById(ci.getProduct_id());
			if(product==null || product.getQuantity()<ci.getQuantity()){
				enoughQuantity=false;
				break;
			}
			productCollection.updateOne(Filters.eq("_id", product.getId()), new Document().append("$inc", new Document("quantity", ci.getQuantity()*(-1))));
			updatedProducts.add(ci);
			amount+=product.getPrice()*ci.getQuantity();
		}
		//rollback if there's no enough product
		if(!enoughQuantity){
			for(CartItem ci:updatedProducts){
				productCollection.updateOne(Filters.eq("_id", ci.getProduct_id()), new Document().append("$inc", new Document("quantity", ci.getQuantity())));
			}
			return null;
		}
		
		//creating shopping cart
		MongoCollection<ShoppingCart> shoppingCartCollection=database.getCollection("shopping_cart", ShoppingCart.class);
		ObjectId id=new ObjectId();
		shoppingCart.setId(id);
		shoppingCart.setDate(new Date());
		shoppingCart.setAmount(amount);
		try {
			shoppingCartCollection.insertOne(shoppingCart);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return findById(id);
	}

	@Override
	public List<ShoppingCart> getUsersShoppingCarts(String user_id) {
		List<ShoppingCart> shoppingCarts=new ArrayList<ShoppingCart>();
		MongoDatabase database=CreateConnection.getConnection();
		MongoCursor<ShoppingCart> cursor=database.getCollection("shopping_cart", ShoppingCart.class).find(Filters.eq("user_id", user_id)).sort(new Document("date", -1)).iterator();
		while(cursor.hasNext()){
			ShoppingCart shoppingCart=cursor.next();
			shoppingCarts.add(shoppingCart);
		}
		return shoppingCarts;
	}

	@Override
	public ShoppingCart findById(ObjectId id) {
		ShoppingCart shoppingCart=null;
		MongoDatabase database=CreateConnection.getConnection();
		shoppingCart=database.getCollection("shopping_cart", ShoppingCart.class).find(Filters.eq("_id", id)).first();
		return shoppingCart;
	}
	
	

}
