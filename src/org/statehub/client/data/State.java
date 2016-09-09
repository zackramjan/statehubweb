package org.statehub.client.data;

import java.io.Serializable;
import java.util.ArrayList;

public class State implements Serializable
{
	private static final long serialVersionUID = 1L;
	Integer order;
	String name;
	String Description;
	Tags tags;
	ArrayList<Feature> features;

}
