package org.statehub.client.data;

import java.io.Serializable;

public class Feature implements Serializable
{
	private static final long serialVersionUID = 1L;
	String name;
	Float score;
	Integer order;
	Tags tags;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Float getScore()
	{
		return score;
	}
	public void setScore(Float score)
	{
		this.score = score;
	}
	public Tags getTags()
	{
		return tags==null? new Tags() : tags;
	}
	public void setTags(Tags tags)
	{
		this.tags = tags;
	}
	public Integer getOrder()
	{
		return order;
	}
	public void setOrder(Integer order)
	{
		this.order = order;
	}

}
