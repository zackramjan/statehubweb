package org.statehub.client.data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class model implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer id;
	String name;
	Date revision;
	ArrayList<State> states;
	Tags tags;
	

}
