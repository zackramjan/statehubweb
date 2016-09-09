package org.statehub.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

import org.statehub.client.data.*;
/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface StateHubService extends RemoteService
{
	ArrayList<Model> getAll();
}
