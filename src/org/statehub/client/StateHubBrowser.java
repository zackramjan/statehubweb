package org.statehub.client;

import java.util.ArrayList;

import org.statehub.client.data.Model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.icu.util.BytesTrie.Result;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.info.Info;

public class StateHubBrowser extends Composite
{

	private static StateHubBrowserUiBinder uiBinder = GWT.create(StateHubBrowserUiBinder.class);
	private final StateHubServiceAsync statehubsvc = GWT.create(StateHubService.class);
	interface StateHubBrowserUiBinder extends UiBinder<Widget, StateHubBrowser> 	{}

	@UiField FramedPanel panel;
    @UiField VerticalLayoutContainer vlc;
	@UiField TextButton searchButton;
	
	public StateHubBrowser()
	{
		initWidget(uiBinder.createAndBindUi(this));
		panel.setWidth(com.google.gwt.user.client.Window.getClientWidth()-100);
		panel.setHeight(com.google.gwt.user.client.Window.getClientHeight()-80);
		vlc.setScrollMode(ScrollMode.AUTO);
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				panel.setWidth(event.getWidth()-100);
				panel.setHeight(event.getHeight()-200);
			}});
	}
	@UiHandler("searchButton")
	public void find(SelectEvent event)
	{
		statehubsvc.getModel("", new AsyncCallback<ArrayList<Model>>(){

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Error",caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<Model> result) {
				for(final Model m : result)
					showModel(m);
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
		vlc.add(f);
		f.add(importVLC);
		doImportButton.addSelectHandler(new SelectHandler(){

			@Override
			public void onSelect(SelectEvent event) {
				Model m = new Model();
				m.parseFromString(t.getText());
				showModel(m);
				vlc.remove(f);
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
		statehubsvc.toJson(m, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				 Info.display("Error",caught.getMessage());
				
			}

			@Override
			public void onSuccess(String result) {
				final FramedPanel f = new FramedPanel();
				f.setWidth(1000);
				f.setHeading(m.getName());
				HorizontalPanel h = new HorizontalPanel();
				 TextArea j = new TextArea();
				 TextArea t = new TextArea();
				 j.setText(result);
				 j.setWidth(500);
				 j.setHeight(500);
				 t.setText(m.toString());
				 t.setWidth(500);
				 t.setHeight(500);
				
				 h.add(j);
				 h.add(t);
				 f.add(h);
				 vlc.add(f);
			}});
	}
}
