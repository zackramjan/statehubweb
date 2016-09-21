package org.statehub.client.data;

import java.io.Serializable;
import java.util.ArrayList;

public class State implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer id;
	Integer order;
	String name;
	String description;
	Tags tags;
	String format;
	ArrayList<Feature> features;
	public String getKey()
	{
		return name+order;
	}
	public void setKey(Integer id)
	{
		this.id = id;
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getOrder()
	{
		return order;
	}
	public void setOrder(Integer order)
	{
		this.order = order;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Tags getTags()
	{
		return tags==null? new Tags() : tags;
	}
	public void setTags(Tags tags)
	{
		this.tags = tags;
	}
	public ArrayList<Feature> getFeatures()
	{
		return features;
	}
	public void setFeatures(ArrayList<Feature> features)
	{
		this.features = features;
	}
	public String getFormat()
	{
		return format;
	}
	public void setFormat(String format)
	{
		this.format = format;
	}
}
