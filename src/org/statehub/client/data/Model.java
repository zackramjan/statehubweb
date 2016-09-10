package org.statehub.client.data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Model implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer id;
	String name;
	Timestamp revision;
	ArrayList<State> states;
	Tags tags;
	

}
