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
	String urlBed;
	String urlBigBed;
	
	public String toString()
	{
		return genome + "\t" + project + "\t" + filename + "\t" + "\t" + mark + "\t" + segfile + "\t" + urlBed + "\t" + urlBigBed; 
	}
	
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
	public String getUrlBed() {
		return urlBed;
	}
	public void setUrlBed(String url) {
		this.urlBed = url;
	}
	
	public String getUrlBigBed()
	{
		return urlBigBed;
	}

	public void setUrlBigBed(String urlBigBed)
	{
		this.urlBigBed = urlBigBed;
	}
}
