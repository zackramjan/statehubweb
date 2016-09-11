package org.statehub.client.data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Model implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer id;
	String name;
	String author;
	Timestamp revision;
	String description;
	ArrayList<State> states;
	Tags tags;
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Timestamp getRevision()
	{
		return revision;
	}
	public void setRevision(Timestamp revision)
	{
		this.revision = revision;
	}
	public ArrayList<State> getStates()
	{
		return states;
	}
	public void setStates(ArrayList<State> states)
	{
		this.states = states;
	}
	public Tags getTags()
	{
		return tags;
	}
	public void setTags(Tags tags)
	{
		this.tags = tags;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	

}
