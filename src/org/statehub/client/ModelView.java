package org.statehub.client;

import java.util.ArrayList;
import java.util.List;

import org.statehub.client.data.Model;
import org.statehub.client.data.State;
import org.statehub.client.data.StateModel;
import org.statehub.client.data.StateModelFactory;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent.ViewReadyHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.info.Info;

public class ModelView extends Composite 
{

	private static ModelViewUiBinder uiBinder = GWT.create(ModelViewUiBinder.class);

	interface ModelViewUiBinder extends UiBinder<Widget, ModelView>	{}
	@UiField FramedPanel panel;
    @UiField VerticalLayoutContainer vlc;
    @UiField VerticalLayoutContainer modelDetailsPanel;
    @UiField FieldLabel nameLabel;
    @UiField FieldLabel descLabel;
    @UiField FieldLabel idLabel;
	private static final StateModel properties = GWT.create(StateModel.class);
	private final StateHubServiceAsync statehubsvc = GWT.create(StateHubService.class);
	ListStore<State> store=new ListStore<State>(properties.key());
	StoreFilter<State> filter;
	Grid<State> stateGrid; 
	Model model;
	Boolean isExpanded = false;
	  RowExpander<State> rowExpander = new RowExpander<State>(new AbstractCell<State>() {
	        @Override
	        public void render(Context context, State value, SafeHtmlBuilder sb) {
	          sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Name:</b> " + value.getName() + "</p>");
	          sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Description:</b> " + value.getDescription());
	          sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>TAGS:</b> " + value.getTags().toReadable().replaceAll("img src=\"", "img src=\"images/"+model.getId()+"-"));
	        }
	      });
	
	
	public ModelView(final Model model)
	
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.model = model;
		panel.setCollapsible(true);
		panel.setBorders(false);
		panel.setHeading(model.getName() + "  (" + model.getCategory() + ") - revision: " + model.getRevision());
		nameLabel.setText("creator: " + model.getAuthor());
		descLabel.setText("description: " + model.getDescription());
		idLabel.setText("Unique ID: "  + model.getId());
		
		Image filterIcon = new Image("filter.png");
		filterIcon.setPixelSize(30, 30);
		panel.getHeader().addTool(filterIcon);
		filterIcon.setTitle("Show all states in this model");
		
		Image expandIcon = new Image("expand.png");
		expandIcon.setPixelSize(30, 30);
		panel.getHeader().addTool(expandIcon);
		expandIcon.setTitle("Show/Hide State details");
		
		Image txtIcon = new Image("txt-icon.png");
		txtIcon.setPixelSize(30, 30);
		panel.getHeader().addTool(txtIcon);
		txtIcon.setTitle("Export Model to TXT (JSON is preffered)");
		
		Image jsonIcon = new Image("json-icon.png");
		jsonIcon.setPixelSize(30, 30);
		panel.getHeader().addTool(jsonIcon);
		jsonIcon.setTitle("Export Model to JSON");
		
		
		txtIcon.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				   final Dialog simple = new Dialog();
				   simple.setHeading(model.getName() + " — TXT");
				      simple.setWidth(700);
				      simple.setHeight(700);
				      simple.setResizable(true);
				      simple.setHideOnButtonClick(true);
				      simple.setPredefinedButtons(PredefinedButton.OK);
				      simple.setBodyStyleName("pad-text");
				      simple.getBody().addClassName("pad-text");
				      TextArea t = new TextArea();
				      t.setText(model.toString());
				      simple.add(t);
				      simple.show();
				
			}});
		
		jsonIcon.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				   final Dialog simple = new Dialog();
				      simple.setHeading(model.getName() + " — JSON");
				      simple.setWidth(700);
				      simple.setHeight(700);
				      simple.setResizable(true);
				      simple.setHideOnButtonClick(true);
				      simple.setPredefinedButtons(PredefinedButton.OK);
				      simple.setBodyStyleName("pad-text");
				      simple.getBody().addClassName("pad-text");
				      final TextArea t = new TextArea();
				      statehubsvc.toJson(model, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							Info.display("Error",caught.getMessage());
							
						}

						@Override
						public void onSuccess(String result) {
							t.setText(result);
							
						}});
				      simple.add(t);
				      simple.show();
				
			}});
		
		expandIcon.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(isExpanded == false)
				{
				stateGrid.addViewReadyHandler(new ViewReadyHandler(){

					@Override
					public void onViewReady(ViewReadyEvent event) {
						for(int i =0; i<store.size();i++)
							rowExpander.expandRow(i);
						isExpanded = true;
					}});
					for(int i =0; i<store.size();i++)
						rowExpander.expandRow(i);
					isExpanded = true;

				}
				else
				{
					stateGrid.addViewReadyHandler(new ViewReadyHandler(){

						@Override
						public void onViewReady(ViewReadyEvent event) {
							for(int i =0; i<store.size();i++)
								rowExpander.collapseRow(i);
							isExpanded = false;
						}});
					for(int i =0; i<store.size();i++)
						rowExpander.collapseRow(i);
					isExpanded = false;
				}  
				
			}});
		
		
		filterIcon.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				store.setEnableFilters(!store.isEnableFilters());
				if(isExpanded=false);
				if(!store.isEnableFilters())
					panel.expand();
				else
					panel.collapse();
				
			}});
		
		
		List<ColumnConfig<State, ?>> columnDefs = new ArrayList<ColumnConfig<State, ?>>();
		columnDefs.add(rowExpander);
		ColumnConfig<State, String> cc1 = new ColumnConfig<State, String>(properties.name(), 100, "Name");
		cc1.setCellPadding(false);
		cc1.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context,String value, SafeHtmlBuilder sb) 
			{
				State m = store.get(context.getIndex()); 
				String format = m.getFormat().replaceAll("{", "").replace("}", "");
				sb.appendHtmlConstant("<span style=\"font-weight:bold;overflow:auto;height:100%;width:100px;display:block;" + format + " \" title=\""+ m.getDescription() + "\">" + m.getName() + "</span>");
			}	 
		 });
		
		columnDefs.add(cc1);
		
		for(int i = 0 ; i < model.getStates().get(0).getFeatures().size(); i++)
		{
			 ColumnConfig<State, String> cc = new ColumnConfig<State, String>(StateModelFactory.getValueProvider(i), 100,  model.getStates().get(0).getFeatures().get(i).getName());
			 cc.setCellPadding(false);
			 columnDefs.add(cc);
			 cc.setCell(new AbstractCell<String>() {
					@Override
					public void render(Context context,String value, SafeHtmlBuilder sb) 
					{
						int r = 40 * (Integer.parseInt(value) + 2);
						int g = 20 * (Integer.parseInt(value) + 3);
						int b = 80 * (Integer.parseInt(value) + 4);
						String color = "rgb(" + r + "," + g + "," + b + ")";
						sb.appendHtmlConstant("<span style=\"overflow:auto;height:100%;width:100px;display:block;background-color:" + color + "\" title=\"score is "+  value + "\">" + value + "</span>");
						
					}	 
				 });
		}
		ColumnModel<State> colModel = new ColumnModel<State>(columnDefs);
		
		
		
		store.addAll(model.getStates());
		stateGrid = new Grid<State>(store,colModel);
		rowExpander.initPlugin(stateGrid);
		vlc.add(stateGrid);
		
	}
	public ModelView(Model model, final String search)
	{
		this(model);
		filter = new StoreFilter<State>(){

		@Override
		public boolean select(Store<State> store, State parent, State item)
		{
			String searchLower = search.toLowerCase();
			Boolean found = 
			item.getDescription().toLowerCase().contains(searchLower) ||
			item.getName().toLowerCase().contains(searchLower) ||
			item.getTags().toReadable().toLowerCase().contains(searchLower) ||
			item.getFeaturesReadable().toLowerCase().contains(searchLower);
			
			//found = item.getName().toLowerCase().contains(searchLower);
			return found;
		}};
		store.setEnableFilters(true);
		store.addFilter(filter);
		panel.collapse();
		stateGrid.addViewReadyHandler(new ViewReadyHandler(){

			@Override
			public void onViewReady(ViewReadyEvent event) {
				for(int i =0; i<store.size();i++)
					rowExpander.expandRow(i);
				isExpanded = true;
			}});
		
		
		
	}


}
