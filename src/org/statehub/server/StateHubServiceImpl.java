package org.statehub.server;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.statehub.client.StateHubService;
import org.statehub.client.data.Feature;
import org.statehub.client.data.Model;
import org.statehub.client.data.State;
import org.statehub.client.data.Tags;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

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
	
	
}
