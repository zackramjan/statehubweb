package org.statehub.server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import org.statehub.client.StateHubService;
import org.statehub.client.data.Feature;
import org.statehub.client.data.Model;
import org.statehub.client.data.State;
import org.statehub.client.data.Tags;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.Statement;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class StateHubServiceImpl extends RemoteServiceServlet implements StateHubService
{
	@Override
	public ArrayList<Model> getModel(String search)
	{
		
		//TODO we dont do any searching with the query now, we just return EVERYTHING
		//THIS SHOULD BE ADDED LATER
		java.sql.Connection myConnection = getConnection();
		ArrayList<Model> models = new ArrayList<Model>();
		try
		{
			if (myConnection != null)
			{
				String queryString = "select * from model";
				PreparedStatement query = myConnection.prepareStatement(queryString);
				ResultSet results = query.executeQuery();
				while(results.next())
				{
					Model m = new Model();
					m.setId(results.getInt(1));
					m.setName(results.getString(2));
					m.setAuthor(results.getString(3));
					m.setRevision(results.getTimestamp(4));
					m.setDescription(results.getString(5));
					m.setStates(getStatesForModel(m.getId()));
					m.setTags(getTagsforID(m.getId(),0,0));
					models.add(m);
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return models;
	}
	
	ArrayList<State> getStatesForModel(int model_id)
	{
		java.sql.Connection myConnection = getConnection();
		ArrayList<State> states = new ArrayList<State>();
		try
		{
			if (myConnection != null)
			{
				String queryString = "select * from state where model_id=? order by order ASC";
				PreparedStatement query = myConnection.prepareStatement(queryString);
				query.setInt(1, model_id);
				
				ResultSet results = query.executeQuery();
				while(results.next())
				{
					State s = new State();
					s.setId(results.getInt(1));
					s.setOrder(results.getInt(3));
					s.setName(results.getString(4));
					s.setDescription(results.getString(5));
					s.setFeatures(getFeaturesForState(s.getId()));
					s.setTags(getTagsforID(0,s.getId(),0));
					states.add(s);
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return states;
	}
	
	ArrayList<Feature> getFeaturesForState(int state_id)
	{
		java.sql.Connection myConnection = getConnection();
		ArrayList<Feature> features = new ArrayList<Feature>();
		try
		{
			if (myConnection != null)
			{
				String queryString = "select f.name,s.score, s.id from state_features s inner join features f on s.feature_id = f.id where s.state_id=? order by order ASC";
				PreparedStatement query = myConnection.prepareStatement(queryString);
				query.setInt(1, state_id);
				
				ResultSet results = query.executeQuery();
				while(results.next())
				{
					Feature f = new Feature();
					f.setName(results.getString(1));
					f.setScore(results.getFloat(2));
					f.setTags(getTagsforID(0,0,results.getInt(3)));
					features.add(f);
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return features;
	}
	
	Tags getTagsforID(Integer model_id,Integer state_id,Integer feature_id)
	{
		java.sql.Connection myConnection = getConnection();
		Tags tags = new Tags();
		try
		{
			if (myConnection != null)
			{
				String queryString = "select tag from tags where model_id=? OR state_id=? OR feature_id=?";
				PreparedStatement query = myConnection.prepareStatement(queryString);
				query.setInt(1, model_id);
				query.setInt(2, state_id);
				query.setInt(3, feature_id);
				
				ResultSet results = query.executeQuery();
				while(results.next())
				{
					tags.add(results.getString(1));
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return tags;
	}
	
	Integer insertModelRow(String name,String author,Timestamp revision,String description)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert into model (name,author,revision) values(?,?,?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				query.setString(1, name);
				query.setString(2, author);
				query.setTimestamp(3,revision);
				query.setString(4, description);
				ResultSet results = query.executeQuery();
				
				if(results.next())
					ret = results.getInt(1);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return ret;
	}
	
	Integer insertStateRow(int model_id,int order,String name,String description)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert into state (model_id,order,name,description) values(?,?,?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				query.setInt(1, model_id);
				query.setInt(2, order);
				query.setString(3,name);
				query.setString(4,description);
				ResultSet results = query.executeQuery();
				
				if(results.next())
					ret = results.getInt(1);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return ret;
	}
	
	Integer insertFeatureNameRow(String name,String description)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert ignore into features (name,description) values(?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString);
				query.setString(1,name);
				query.setString(2,description);
				query.executeQuery();
				
				String queryString2 = "select id from features where name=?";
				PreparedStatement query2 = myConnection.prepareStatement(queryString2);
				query2.setString(1,name);
				ResultSet results2 = query.executeQuery();
				
				while(results2.next())
					ret = results2.getInt(1);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return ret;
	}
	
	Integer insertStateFeatureScoreRow(int state_id,int feature_id,int order,float score)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert into state_features (state_id,state_feature_id,order,score) values(?,?,?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				query.setInt(1, state_id);
				query.setInt(2, feature_id);
				query.setInt(3, order);
				query.setFloat(4,score);

				ResultSet results = query.executeQuery();
				
				if(results.next())
					ret = results.getInt(1);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return ret;
	}
	
	Integer insertTagRow(Integer model_id,Integer state_id,Integer feature_id, String tag)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert into tags (model_id,state_id,feature_id,tag) values(?,?,?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				query.setInt(1, model_id);
				query.setInt(2, state_id);
				query.setInt(3,feature_id);
				query.setString(4,tag);

				ResultSet results = query.executeQuery();
				
				if(results.next())
					ret = results.getInt(1);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		finally 	{
			if(myConnection != null)
				try{myConnection.close();}catch(Exception f){f.printStackTrace();}
		}
		return ret;
	}
	
	
	java.sql.Connection getConnection()
	{
		java.sql.Connection myConnection = null;
		try
		{
			//load a properties file with db info
			Properties prop = new Properties();
			
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
	
			Class.forName(prop.getProperty("dbDriver")).newInstance();
			// get database details from param file
			String username = prop.getProperty("dbUserName");
			String password = prop.getProperty("dbPassword");
	
			// URL to connect to the database
			String dbURL = prop.getProperty("dbConnection") + username + "&password=" + password;
			// create the connection
			myConnection = DriverManager.getConnection(dbURL);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return myConnection;
	}

	@Override
	public Integer StoreModel(Model m)
	{
		int model_id = insertModelRow(m.getName(),m.getAuthor(),m.getRevision(),m.getDescription());
		for(String t1 : m.getTags())
			insertTagRow(model_id,0,0,t1);
		for(State s : m.getStates())
		{
			int state_id = insertStateRow(model_id,s.getOrder(),s.getName(),s.getDescription());
			for(String t2 : s.getTags())
				insertTagRow(0,state_id,0,t2);
			for(Feature f : s.getFeatures())
			{
				int feature_id = insertFeatureNameRow(f.getName(),"a feature desc");
				int state_feature_id = insertStateFeatureScoreRow(state_id,feature_id,f.getOrder(),f.getScore());
				for(String t3 : f.getTags())
					insertTagRow(0,0,state_feature_id,t3);
			}
		}
		return model_id;
	}
}
