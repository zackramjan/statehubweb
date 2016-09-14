package org.statehub.client.data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Model implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer id;
	String name;
	String category;
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
		
		return tags==null? new Tags() : tags;
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
	
	public void parseFromString(String s)
	{
		
		String[] lines = s.split("[\r\n]+");
		this.setName(lines[0]);
		this.setCategory(lines[2]);
		this.setAuthor(lines[3]);
		this.setDescription(lines[4]);
		this.setRevision(new Timestamp(new Date().getTime()));
		String[] features = lines[5].trim().split("\\s+");
		
		this.states = new ArrayList<State>();
		for(int i=6;i<lines.length;i++)
		{
			String stateDescription = "";
			Tags stateTags = new Tags();
			String stateFormat = "";
			if(lines[i].contains("{") && lines[i].contains("}"))
			{
				stateFormat = lines[i].substring(lines[i].lastIndexOf("{"),lines[i].lastIndexOf("}")+1);
				lines[i] = lines[i].substring(0,lines[i].lastIndexOf("{"));
			}
			String[] stateTokens = lines[i].split("\\s");
			
			State myState = new State();
			myState.setName(stateTokens[0]);
			myState.setOrder(i-5);
			myState.setFormat(stateFormat);
			ArrayList<Feature> stateFeatures = new ArrayList<Feature>();
			
			for (int j = 1;j<stateTokens.length;j++)				
			{
				if(j<=features.length)
				{
					Feature f = new Feature();
					f.setName(features[j-1]);
					f.setOrder(j);
					f.setScore(stateTokens[j].equals("NA") ? -1 : Float.valueOf(stateTokens[j]));
					stateFeatures.add(f);
				}
				else
				{
					stateTags.add(stateTokens[j]);
					stateDescription += " " +  stateTokens[j];
				}
			}
			
			myState.setFeatures(stateFeatures);
			myState.setTags(stateTags);
			myState.setDescription(stateDescription);
			this.states.add(myState);
		}
	}
	
	public String toString()
	{
		String ret = this.getName() + "\n";
		ret += this.getRevision().toString() + "\n";
		ret += this.getCategory() + "\n";
		ret += this.getAuthor() + "\n";
		ret += this.getDescription() + "\n";
		String featureHeader = "";
		for (Feature f : this.getStates().get(0).getFeatures() )
			featureHeader += f.getName() + "\t";
		ret+= featureHeader + "\n";
		for (State s : this.getStates())
		{
			String stateRow = "";
			stateRow += s.getName() + "\t";
			for (Feature f : s.getFeatures())
				stateRow += f.getScore() + "\t";
			ret += stateRow + s.getDescription() + "\t" + s.getFormat() + "\n";
			
		}
		
		return ret;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
}
