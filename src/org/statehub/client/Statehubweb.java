package org.statehub.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Statehubweb implements EntryPoint
{
	
	public void onModuleLoad() 
	{
		Viewport viewport = new Viewport();
		StateHubBrowser s = new StateHubBrowser();
		viewport.setWidget(s);
		RootLayoutPanel.get().add(viewport);
		
	}
}
