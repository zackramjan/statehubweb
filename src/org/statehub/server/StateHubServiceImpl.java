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
import org.statehub.client.data.TrackList;

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
		
		System.err.println("searching mongo for "+ search);
		ArrayList<Model> models = new ArrayList<Model>();
		Properties prop = new Properties();
		try
		{
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			String username = prop.getProperty("dbUser");
			String password = prop.getProperty("dbPassword");
			String database = prop.getProperty("dbName");
			String collectionName = prop.getProperty("modelsCollection");
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
			String collectionName = prop.getProperty("modelsCollection");
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
		
		if(prop.getProperty("useS3") != null && prop.getProperty("useS3").toLowerCase().equals("yes"))
			return getTracksFromS3(id);
		else
			return getTracksFromMongo(id);
		
	}
	
	
	public ArrayList<Track> getTracksFromS3(String id)
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
			            t.dummy();
			            if( objectSummary.getSize() > 0 && p.getNameCount()>4 && objectSummary.getKey().toLowerCase().endsWith("bed"))
			            {
			            	t.setOrder(i);
			            	t.setGenome(p.subpath(2, 3).toString());
			            	t.setProject(p.subpath(3, 4).toString());
			            	t.setBedFileName(p.subpath(4, 5).toString());
			            	t.setName(t.getBedFileName());
			            	t.setDescription(t.getBedFileName());
			            	t.setModelID(id);
			            	t.setStatePaintRVersion("0.0.0");
			            	t.setBaseURL(s3uri + "/tracks");
			            	ArrayList<String> marks = new ArrayList<String>();
			            	marks.add("unspecified");
			            	t.setMarks(marks);
			            	tracks.add(t);
			            	System.out.println(i++ + " - " + t.toString());
			            }
			        }
			        listObjectsRequest.setMarker(objectListing.getNextMarker());
			} while (objectListing.isTruncated());
       return tracks;
	}
	
	public Integer storeEntireTrackDBToMongo(ArrayList<ArrayList<Track>> store)
	{
		Properties prop = new Properties();
		try
		{
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			String username = prop.getProperty("dbUser");
			String password = prop.getProperty("dbPassword");
			String database = prop.getProperty("dbName");
			String collectionName = prop.getProperty("tracksCollection");
			Gson gson = new Gson();

			MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(prop.getProperty("dbServer")), Arrays.asList(credential));
			MongoDatabase db = mongoClient.getDatabase(database);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			for(ArrayList<Track> t : store)
			{
				TrackList trackList = new TrackList();
				trackList.setModelID(t.get(0).getModelID());
				trackList.setTracks(t);
				collection.insertOne(Document.parse(gson.toJson(trackList)));
			}
				
			mongoClient.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	

	public ArrayList<Track> getTracksFromMongo(String id) {
		
		Properties prop = new Properties();
		try
		{
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			String username = prop.getProperty("dbUser");
			String password = prop.getProperty("dbPassword");
			String database = prop.getProperty("dbName");
			String collectionName = prop.getProperty("tracksCollection");
			MongoCredential credential = MongoCredential.createCredential(username, "admin", password.toCharArray());
			MongoClient mongoClient = new MongoClient(new ServerAddress(prop.getProperty("dbServer")), Arrays.asList(credential));
			MongoDatabase db = mongoClient.getDatabase(database);
			MongoCollection<Document> collection = db.getCollection(collectionName);
			System.err.println("getting all tracks for " + id  + " from mongo");
			Gson gson = new Gson();
			
			
			for (Document cur : collection.find()) 
			{
				TrackList t = gson.fromJson(cur.toJson(),TrackList.class);
				if(t.getModelID().equals(id))
				{
					ArrayList<Track> m = t.getTracks();
					mongoClient.close();
					for(int i=0;i<m.size();i++)
						m.get(i).setOrder(i+1);
					return m;
				}
			}
			mongoClient.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Track>();
	}
}
