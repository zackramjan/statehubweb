package org.statehub.client.data;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface TrackModel extends PropertyAccess<Track>
{
	ModelKeyProvider<Track> key();
	ValueProvider<Track, String> segfile();
	ValueProvider<Track, String> mark();
	ValueProvider<Track, String> project();
	ValueProvider<Track, String> genome();
	ValueProvider<Track, String> filename();
	ValueProvider<Track, String> urlBed();
	ValueProvider<Track, String> urlBigBed();
}
