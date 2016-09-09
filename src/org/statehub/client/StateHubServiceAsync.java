package org.statehub.client;

import java.util.ArrayList;

import org.statehub.client.data.Model;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface StateHubServiceAsync
{
		void getAll(AsyncCallback<ArrayList<Model>> callback);
}
