package org.statehub.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import org.bson.Document;
import org.statehub.client.StateHubService;
import org.statehub.client.data.Model;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StateHubServiceImpl extends RemoteServiceServlet implements StateHubService
{
	@Override
	public ArrayList<Model> getModel(String search)
	{
		System.err.println("searching mongo for "+ search);
		ArrayList<Model> models = new ArrayList<Model>();
		Properties prop = new Properties();
		try
		{
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			String username = prop.getProperty("dbUser");
			String password = prop.getProperty("dbPassword");
			String database = prop.getProperty("dbName");
			String collectionName = prop.getProperty("dbCollection");
			MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(prop.getProperty("dbServer")), Arrays.asList(credential));
			MongoDatabase db = mongoClient.getDatabase(database);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			//.collection.collection.cre
			if(search != null && search.length() > 1)
			{
				System.err.println("doing fulltext for "+ search);
				for (Document cur : collection.find(Filters.text(search)))
				{
					Model m = fromJson(cur.toJson());
					m.setId(cur.getObjectId("_id").toString());
					models.add(m);					
				}
					
			}
			else
			{
				System.err.println("getting all models");
				for (Document cur : collection.find()) 
				{
					Model m = fromJson(cur.toJson());
					m.setId(cur.getObjectId("_id").toString());
					models.add(m);					
				}
			}
				
			mongoClient.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return models;
	}
	
	@Override
	public Integer storeModel(Model m)
	{
		Properties prop = new Properties();
		try
		{
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			String username = prop.getProperty("dbUser");
			String password = prop.getProperty("dbPassword");
			String database = prop.getProperty("dbName");
			String collectionName = prop.getProperty("dbCollection");
			if(m.getRevision() == null)
				m.setRevision(new Timestamp(new Date().getTime()));
			MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(prop.getProperty("dbServer")), Arrays.asList(credential));
			MongoDatabase db = mongoClient.getDatabase(database);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.insertOne(Document.parse(toJson(m)));
			mongoClient.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String toJson(Model m)
	{
		Gson gson = new Gson();
		return gson.toJson(m);
	}

	@Override
	public Model fromJson(String s)
	{
		Gson gson = new Gson();
		return gson.fromJson(s,Model.class);
	}
}
