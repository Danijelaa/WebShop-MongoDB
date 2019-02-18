package com.webshop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Indexes;
import com.webshop.model.CartItem;
import com.webshop.model.Category;
import com.webshop.model.Product;
import com.webshop.model.ShoppingCart;
import com.webshop.model.User;
import com.webshop.service.ProductService;
import com.webshop.support.CreateConnection;

@Component
public class TestData {
	@Autowired
	private ProductService ps;

	@PostConstruct
	public void init() {
        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));		
		MongoClient mongo=new MongoClient("localhost", 27017);
		  mongo.dropDatabase("shopping");
		MongoDatabase database=mongo.getDatabase("shopping").withCodecRegistry(pojoCodecRegistry);

		  MongoCollection<User> userCollection=database.getCollection("user", User.class);
		  User moderator=new User();
		  moderator.setUsername("moderator");
		  moderator.setPassword("moderator");
		  User userData1=new User();
		  userData1.setUsername("username1");
		  userData1.setPassword("password1");
		  User userData2=new User();
		  userData2.setUsername("username2");
		  userData2.setPassword("password2");
		  List<User> users=new ArrayList<>();
		  users.add(userData1);
		  users.add(userData2);
		  users.add(moderator);
		  userCollection.insertMany(users);
		  userCollection.createIndex(Indexes.compoundIndex(Indexes.text("username"), Indexes.text("password")));
		  
		  MongoCollection<Category> categoryCollection=database.getCollection("category", Category.class);
		  List<Category> categories=new ArrayList<>();
		  for(int i=1; i<=3; i++){
			  Category category=new Category();
			  category.setId(new ObjectId());
			  category.setTitle("category"+i);
			  categories.add(category);
		  }
		  categoryCollection.insertMany(categories);
		  
		  
		  MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		  
		  List<Product> products=new ArrayList<Product>();
		  for(int i=1; i<12; i++) {
			  Product product=new Product();
			  product.setId(new ObjectId());
			  product.setTitle("title"+i);
			  product.setPrice((double) (i*100+i*10));
			  product.setQuantity(i*10+10);
			  int n=(int) Math.floor(Math.random()*3);
			  product.setCategory_id(categories.get(n).getId());
			  products.add(product);
		  }
		  productCollection.insertMany(products);
		
		  MongoCollection<ShoppingCart> shoppingCartCollection=database.getCollection("shopping_cart", ShoppingCart.class);
		  
		  CartItem cartItemData1=new CartItem();
		  cartItemData1.setProduct_id(products.get(0).getId());
		  cartItemData1.setQuantity(2);
		  
		  CartItem cartItemData2=new CartItem();
		  cartItemData2.setProduct_id(products.get(1).getId());
		  cartItemData2.setQuantity(5);
		  
		  ShoppingCart shoppingCartData1=new ShoppingCart();
		  shoppingCartData1.setId(new ObjectId());
		  shoppingCartData1.setDate(new Date());
		  shoppingCartData1.setAmount(1320.00);
		  shoppingCartData1.setUser_id(userData1.getUsername());
		  shoppingCartData1.getItems().add(cartItemData1);
		  shoppingCartData1.getItems().add(cartItemData2);
		  shoppingCartCollection.insertOne(shoppingCartData1);
		  shoppingCartCollection.createIndex(Indexes.text("user_id"));
		  
		  database.createCollection("log", new CreateCollectionOptions()
          .capped(true)
          .sizeInBytes(256));
		  
		  
	}
}
