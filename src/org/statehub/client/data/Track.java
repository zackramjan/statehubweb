package org.statehub.client.data;
import java.io.Serializable;
import java.util.ArrayList;


public class Track implements Serializable
{
	private static final long serialVersionUID = 1L;
	String name;
	String description;
	String project;
	String genome;
	ArrayList<String> marks;
	String bedFileName;
	String bigBedFileName;
	String statePaintRVersion;
	String modelID;
	String baseURL;
	Integer order;
	
	public String toString()
	{
		return name + "\t" + description + "\t" + project + "\t"  + genome + "\t[" + getMarksString() + "]\t" + bedFileName + "\t" + bigBedFileName + "\t" + statePaintRVersion + "\t" + modelID; 
	}
	
	public void dummy()
	{
		name="some track name";
		description="some track description";
		project="ENCODE";
		genome="hg19";
		marks = new ArrayList<String>();
		marks.add("mark1");
		marks.add("mark2");
		marks.add("mark3");
		bedFileName="foo.bed";
		bigBedFileName="foo.bb";
		statePaintRVersion="0.2.5";
		modelID="5813b67f46e0fb06b493ceb0";
		baseURL="http://s3-us-west-2.amazonaws.com/statehub-trackhub/tracks";
		order=1;
	}
	
	public String getKey() {
		return bedFileName;
	}
	
	public void setKey(String key) {
		this.bedFileName = key;
	}
	
	public String getMarksString()
	{
		String marksString ="";
		for (String s : marks)
			marksString += s + ",";
		return marksString.substring(0, marksString.length()-1);
	}
	
	public String getBedURL()
	{
		return baseURL + "/" + modelID + "/" + genome + "/" + project + "/" + bedFileName;
	}
	
	public void setBedURL(String s)
	{
		//NOP
	}
	
	public String getBigBedURL()
	{
		return baseURL + "/" + modelID + "/" + genome + "/" + project + "/" + bigBedFileName;
	}
	
	public void setBigBedURL(String s)
	{
		//NOP
	}
	
	public void setMarksString(String s)
	{
		//NOP
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

	public String getProject()
	{
		return project;
	}

	public void setProject(String project)
	{
		this.project = project;
	}

	public String getGenome()
	{
		return genome;
	}

	public void setGenome(String genome)
	{
		this.genome = genome;
	}

	public ArrayList<String> getMarks()
	{
		return marks;
	}

	public void setMarks(ArrayList<String> marks)
	{
		this.marks = marks;
	}

	public String getBedFileName()
	{
		return bedFileName;
	}

	public void setBedFileName(String bedFileName)
	{
		this.bedFileName = bedFileName;
	}

	public String getBigBedFileName()
	{
		return bigBedFileName;
	}

	public void setBigBedFileName(String bigBedFileName)
	{
		this.bigBedFileName = bigBedFileName;
	}

	public String getStatePaintRVersion()
	{
		return statePaintRVersion;
	}

	public void setStatePaintRVersion(String statePaintRVersion)
	{
		this.statePaintRVersion = statePaintRVersion;
	}

	public String getModelID()
	{
		return modelID;
	}

	public void setModelID(String modelID)
	{
		this.modelID = modelID;
	}

	public String getBaseURL()
	{
		return baseURL;
	}

	public void setBaseURL(String baseURL)
	{
		this.baseURL = baseURL;
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
