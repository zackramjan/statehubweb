package org.statehub.client.data;

import java.io.Serializable;
import java.util.ArrayList;

public class State implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer id;
	Integer order;
	String name;
	String Description;
	Tags tags;
	ArrayList<Feature> features;
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
		return Description;
	}
	public void setDescription(String description)
	{
		Description = description;
	}
	public Tags getTags()
	{
		return tags;
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
}
