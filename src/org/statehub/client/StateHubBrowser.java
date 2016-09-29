package org.statehub.client;

import java.util.ArrayList;
import org.statehub.client.data.Model;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

public class StateHubBrowser extends Composite
{

	private static StateHubBrowserUiBinder uiBinder = GWT.create(StateHubBrowserUiBinder.class);
	private final StateHubServiceAsync statehubsvc = GWT.create(StateHubService.class);
	interface StateHubBrowserUiBinder extends UiBinder<Widget, StateHubBrowser> 	{}

	@UiField FramedPanel panel;
    @UiField VerticalLayoutContainer vlc;
    @UiField VerticalLayoutContainer resultsVlc;
	@UiField TextButton searchButton;
	@UiField TextField searchText;
	
	public StateHubBrowser()
	{
		initWidget(uiBinder.createAndBindUi(this));
		panel.setWidth(com.google.gwt.user.client.Window.getClientWidth()-100);
		panel.setHeight(com.google.gwt.user.client.Window.getClientHeight()-80);
		vlc.setScrollMode(ScrollMode.AUTO);
		resultsVlc.setScrollMode(ScrollMode.AUTO);
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				panel.setWidth(event.getWidth()-100);
				panel.setHeight(event.getHeight()-200);
			}});
		
		searchText.addKeyDownHandler(new KeyDownHandler(){

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					find(null);
			}

			
				
			});
		
		
	}
	@UiHandler("searchButton")
	public void find(SelectEvent event)
	{
		statehubsvc.getModel(searchText.getText(), new AsyncCallback<ArrayList<Model>>(){

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Error",caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<Model> result) {
				resultsVlc.clear();
				for(final Model m : result)
					if(searchText.getText().length()>0)
						resultsVlc.add(new ModelView(m,searchText.getText()));
					else
						resultsVlc.add(new ModelView(m));
					//showModel(m);
			}});

	}
	@UiHandler("importButton")
	public void importModel(SelectEvent event)
	{
		final FramedPanel f = new FramedPanel();
		f.setHeading("Import a model from text");
		VerticalLayoutContainer importVLC = new  VerticalLayoutContainer();
		
		final TextArea t = new TextArea();
		t.setWidth(800);
		t.setHeight(500);
		f.setWidth(800);
		t.setText("paste your text here");
		importVLC.add(t);
		TextButton doImportButton = new TextButton("import");
		importVLC.add(doImportButton);
		resultsVlc.add(f);
		f.add(importVLC);
		doImportButton.addSelectHandler(new SelectHandler(){

			@Override
			public void onSelect(SelectEvent event) {
				Model m = new Model();
				m.parseFromString(t.getText());
				showModel(m);
				resultsVlc.remove(f);
				statehubsvc.storeModel(m, new AsyncCallback<Integer>(){

					@Override
					public void onFailure(Throwable caught) {
						Info.display("Error importing",caught.getMessage());
						
					}

					@Override
					public void onSuccess(Integer result) {
						Info.display("Success","Saved Model to DB");
						
					}});
			}});
	}
	
	private void showModel(final Model m)
	{
		resultsVlc.add(new ModelView(m));
		
	}
}
