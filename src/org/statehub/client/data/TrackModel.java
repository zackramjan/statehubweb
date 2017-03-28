package org.statehub.client.data;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface TrackModel extends PropertyAccess<Track>
{
	ModelKeyProvider<Track> key();
	ValueProvider<Track, String> name();
	ValueProvider<Track, String> description();
	ValueProvider<Track, String> project();
	ValueProvider<Track, String> genome();
	ValueProvider<Track, String> marksString();
	ValueProvider<Track, String> bedFileName();
	ValueProvider<Track, String> bigBedFileName();
	ValueProvider<Track, String> statePaintRVersion();
	ValueProvider<Track, String> modelID();
	ValueProvider<Track, String> bedURL();
	ValueProvider<Track, String> bigBedURL();
	ValueProvider<Track, Integer> order();	
}
