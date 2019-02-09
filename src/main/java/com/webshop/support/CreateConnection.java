package com.webshop.support;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class CreateConnection {
	
	private static MongoClient mongo=null;
	private static MongoDatabase database=null;
	
	public static MongoDatabase getConnection() {
		mongo=new MongoClient("localhost", 27017);
		CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));		
		//mongo=new MongoClient("localhost", 27017);
		database=mongo.getDatabase("shopping").withCodecRegistry(pojoCodecRegistry);
		return database;
	}
	
	
	public static MongoCollection<Document> log(){
		MongoCollection<Document> log=getConnection().getCollection("log");
		return log;
	}
	
	
}
