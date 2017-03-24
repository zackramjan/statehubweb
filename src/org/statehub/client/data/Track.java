package org.statehub.client.data;
import java.io.Serializable;


public class Track implements Serializable
{
	private static final long serialVersionUID = 1L;
	String segfile;
	String mark;
	String project;
	String genome;
	String filename;
	String url;
	
	public String getKey() {
		return filename;
	}
	
	public void setKey(String key) {
		this.filename = key;
	}
	
	public String getSegfile() {
		return segfile;
	}
	
	public void setSegfile(String segfile) {
		this.segfile = segfile;
	}
	
	public String getMark() {
		return mark;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getGenome() {
		return genome;
	}
	public void setGenome(String genome) {
		this.genome = genome;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
