package org.statehub.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class StateHubBrowser extends Composite
{

	private static StateHubBrowserUiBinder uiBinder = GWT.create(StateHubBrowserUiBinder.class);
	private final StateHubServiceAsync statehubsvc = GWT.create(StateHubService.class);
	interface StateHubBrowserUiBinder extends UiBinder<Widget, StateHubBrowser> 	{}

	@UiField FramedPanel panel;
//	@UiField VerticalLayoutContainer vlc;
	
	
	public StateHubBrowser()
	{
		initWidget(uiBinder.createAndBindUi(this));
		panel.setWidth(com.google.gwt.user.client.Window.getClientWidth()-100);
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				panel.setWidth(event.getWidth()-100);
				
			}});
	}
}
