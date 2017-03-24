package org.statehub.client.data;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Model implements Serializable
{
	private static final long serialVersionUID = 1L;
	String id;
	String name;
	String category;
	String author;
	Timestamp revision;
	String description;
	ArrayList<State> states;
	HashMap<String,ArrayList<String>> translation = new HashMap<String,ArrayList<String>>();
	Tags tags;
	ArrayList<Track> tracks;
	
	public String getKey()
	{
		return id;
	}
	public void setKey(String _id)
	{
		this.id = _id;
	}
	
	public String getId()
	{
		return id;
	}
	public void setId(String _id)
	{
		this.id = _id;
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
	public String getRevisionTxt()
	{
		return revision.toString();
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
		
		Boolean isStateDef = true;
		
		for(int i=6;i<lines.length;i++)
		{
			if(lines[i].startsWith("---"))
				isStateDef=false; //we are now defining translations
			else if(isStateDef)
			{	
				//parse the state def row
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
						stateTags.add(stateTokens[j].replace("tag:", ""));
						stateDescription += " " +  stateTokens[j].replace("tag:", "");
					}
				}
				
				myState.setFeatures(stateFeatures);
				myState.setTags(stateTags);
				myState.setDescription(stateDescription);
				this.states.add(myState);
			}
			else 
			{
				//parse the translation row
				String[] transTokens = lines[i].split("\\s");
				if(transTokens.length < 3)
					continue;
				ArrayList<String> transList = new ArrayList<String>();
				for (int j = 1;j<transTokens.length;j++)				
					transList.add(transTokens[j]);
				translation.put(transTokens[0], transList);
			}
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
		if(translation.keySet().size() > 0)
		{
			ret += "---\n";
			for (String s : translation.keySet())
			{
				ret += s;
				for(String m : translation.get(s))
					ret += "\t" + m;
				ret += "\n";
			}		
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
	public HashMap<String, ArrayList<String>> getTranslation() {
		return translation;
	}
	public void setTranslation(HashMap<String, ArrayList<String>> translation) {
		this.translation = translation;
	}
	public ArrayList<Track> getTracks() {
		return tracks;
	}
	public void setTracks(ArrayList<Track> tracks) {
		this.tracks = tracks;
	}
}
