package org.statehub.client.data;

import java.util.ArrayList;

public class Tags extends ArrayList<String>
{
	private static final long serialVersionUID = 1L;
	public String toReadable()
	{
		String ret = "";
		for(String r : this)
			ret += r + " ";
		return ret;
	}

}
