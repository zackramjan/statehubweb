package org.statehub.server;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public ArrayList<Model> getAll() 
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
		} finally
		{
			try
			{
				myConnection.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		
		return null;
	}
	
	
	ArrayList<State> getStatesForModel(int id)
	{
		return null;
	}
	
	Tags getTagsforID(int id)
	{
		return null;
	}
	
	ArrayList<Feature> getFeaturesForState(int id)
	{
		return null;
	}
	
	Integer insertModel(String name,String author,Timestamp revision)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert into model (name,author,revision) values(?,?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				query.setString(1, name);
				query.setString(2, author);
				query.setTimestamp(3,revision);
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
	
	Integer insertState(int model_id,int order,String name,String description)
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
	
	Integer insertFeatureName(String name,String description)
	{
		java.sql.Connection myConnection = getConnection();
		Integer ret = 0;
		try
		{
			if (myConnection != null)
			{
				String queryString = "insert ignore into features (name,description) values(?,?)";
				PreparedStatement query = myConnection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
				query.setString(1,name);
				query.setString(2,description);
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
	
	Integer insertFeatureScore(int state_id,int state_feature_id,float score)
	{
		return 0;
	}
	
	Integer insertTag(Integer model_id,Integer state_id,Integer feature_id, String tag)
	{
		return 0;
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
	
	
}
