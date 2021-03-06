package org.statehub.client;

import java.util.ArrayList;

import org.statehub.client.data.Model;
import org.statehub.client.data.Track;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface StateHubServiceAsync
{
		void getModel(String search, AsyncCallback<ArrayList<Model>> callback);
		void storeModel(Model m, AsyncCallback<Integer> callback);
		void toJson(Model m, AsyncCallback<String> callback);
		void fromJson(String s, AsyncCallback<Model> callback);
		void getTrack(String id, AsyncCallback<ArrayList<Track>> callback);
}
