package com.webshop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.webshop.model.Product;
import com.webshop.service.ProductService;
import com.webshop.support.CreateConnection;

@Service
public class ProductServiceImpl implements ProductService {

	private MongoCollection<Document> log=CreateConnection.log();
	@Override
	public List<Product> getAllProducts(Integer pageNumber) {
		List<Product> products=new ArrayList<>();
		Integer start=(pageNumber-1)*4;
		//Integer end=start+4;
		MongoDatabase database=CreateConnection.getConnection();
		//MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		//MongoCursor<Product> cursor=productCollection.find().iterator();
		MongoCursor<Product> cursor=database.getCollection("product", Product.class).find().limit(4).skip(start).iterator();
		while(cursor.hasNext()) {
			Product product=(Product)cursor.next();
			products.add(product);	
		}
		
		try {
			cursor.close();
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
		}
		
		return products;
	}

	@Override
	public Product getById(ObjectId id) {
		Product product=null;
		MongoDatabase database=CreateConnection.getConnection();
		product=database.getCollection("product", Product.class).find(Filters.eq("_id", id)).first();
		return product;
	}

	@Override
	public Product updateProduct(Product product) {
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		if(getById(product.getId())==null){
			throw new IllegalArgumentException("Can not update non-existent product.");
		}
		/*Document findOne=new Document();
		findOne.append("_id", product.getId());*/
		//Document update=new Document();
		//update.append("$set", new Document().append("price", product.getPrice()));
		long result=productCollection.replaceOne(Filters.eq("_id", product.getId()), product).getModifiedCount();
		if(result!=1){
			throw new MongoException("Error happened. Could not update product.");		}
		return getById(product.getId());
	}

	@Override
	public Product addProduct(Product product) {
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		productCollection.insertOne(product);
		return getById(product.getId());
	}

	@Override
	public boolean deleteProduct(ObjectId id) {
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		if(getById(id)==null){
			throw new IllegalArgumentException("Can not delete non-existent product.");
		}
		long result=productCollection.deleteOne(Filters.eq("_id", id)).getDeletedCount();
		return result==1;
	}

	@Override
	public Integer countProducts() {
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		//Integer 
		return null;
	}

	@Override
	public List<Product> getByPriceAndCategory(Double min, Double max, ObjectId category_id, Integer sortOrder,Integer pageNumber) {
		List<Product> products=new ArrayList<>();
		MongoDatabase database=CreateConnection.getConnection();
		Document query=new Document("category_id", category_id);
		if(min==null) {
			min=0.0;
		}
		Document queryPrice=new Document("$gte", min);
		if(max!=null) {
			queryPrice.append("$lte", max);
		}
		query.append("price", queryPrice);
		if(sortOrder==null || !(sortOrder==1 || sortOrder==-1)) {
			sortOrder=1;
		}
		Integer start=(pageNumber-1)*4;
		MongoCursor<Product> cursor=database.getCollection("product", Product.class).find(query).sort(new Document("price", sortOrder)).limit(4).skip(start).iterator();
		
		while(cursor.hasNext()){
			Product product=cursor.next();
			//System.out.println(product.getCategory()+"|"+product.getTitle()+"|"+product.getPrice()+"|"+product.getQuantity()+"|"+product.getId());
			products.add(product);
		}
		return products;
	}

	@Override
	public boolean changeQuantityOfProduct(ObjectId product_id, Integer bougthQuantity) {
		MongoDatabase database=CreateConnection.getConnection();
		MongoCollection<Product> productCollection=database.getCollection("product", Product.class);
		Product product=getById(product_id);
		if(product.getQuantity()<bougthQuantity) {
			return false;
		}
		long result=productCollection.updateOne(Filters.eq("_id", product.getId()), new Document().append("$inc", new Document("quantity", bougthQuantity*(-1)))).getModifiedCount();
		return result==1;
	}

	@Override
	public boolean logTrial() {
		try {
			MongoCollection<Product> productCollection=CreateConnection.getConnection().getCollection("product", Product.class);
			productCollection.updateOne(Filters.eq("x"), new Document());
		} catch (Exception e) {
			Document doc=new Document();
			doc.append("date", new Date());
			doc.append("message", e.getMessage());
			log.insertOne(doc);
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
