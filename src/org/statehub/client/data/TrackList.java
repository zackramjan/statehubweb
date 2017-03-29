package org.statehub.client.data;

import java.io.Serializable;
import java.util.ArrayList;

public class TrackList implements Serializable
{

	private static final long serialVersionUID = 1L;
	public String getModelID()
	{
		return modelID;
	}
	public void setModelID(String modelID)
	{
		this.modelID = modelID;
	}
	public ArrayList<Track> getTracks()
	{
		return tracks;
	}
	public void setTracks(ArrayList<Track> tracks)
	{
		this.tracks = tracks;
	}
	String modelID;
	ArrayList<Track> tracks;
}
