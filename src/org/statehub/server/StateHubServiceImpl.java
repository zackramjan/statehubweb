package org.statehub.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import org.bson.Document;
import org.statehub.client.StateHubService;
import org.statehub.client.data.Model;
import org.statehub.client.data.Track;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;



/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StateHubServiceImpl extends RemoteServiceServlet implements StateHubService
{
	@Override
	public ArrayList<Model> getModel(String search)
	{
		
		try{getTrack("5813b67f46e0fb06b493ceb0");} catch(Exception e){e.printStackTrace();}
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

	@Override
	public ArrayList<Track> getTrack(String id)
	{
		Properties prop = new Properties();
		try
		{
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String bucketName = prop.getProperty("s3bucket");
		String s3uri = prop.getProperty("s3uri");
		ArrayList<Track> tracks = new ArrayList<Track>();
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials())).build();
		int i =1;
		
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix("tracks/"+id);
		ObjectListing objectListing;

			do {
			        objectListing = s3.listObjects(listObjectsRequest);
			        for (S3ObjectSummary objectSummary : 
			            objectListing.getObjectSummaries()) {
			        	//objectSummary.
			            Path p = new File("/"+objectSummary.getKey()).toPath();
			            Track t = new Track();
			            if( objectSummary.getSize() > 0 && p.getNameCount()>4 && objectSummary.getKey().toLowerCase().endsWith("bed"))
			            {
			            	t.setGenome(p.subpath(2, 3).toString());
			            	t.setProject(p.subpath(3, 4).toString());
			            	t.setFilename(p.subpath(4, 5).toString());
			            	t.setMark("unknown mark");
			            	t.setSegfile("someSegFile");
			            	t.setUrlBed(s3uri + objectSummary.getKey());
			            	t.setUrlBigBed(s3uri + objectSummary.getKey().substring(0,objectSummary.getKey().toLowerCase().lastIndexOf("bed")) + "bb");
			            	tracks.add(t);
			            	System.out.println(i++ + " - " + t.toString());
			            }
			        	
			            
			        }
			        listObjectsRequest.setMarker(objectListing.getNextMarker());
			} while (objectListing.isTruncated());
		
       return tracks;
	}
	
	
}
