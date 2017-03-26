package org.statehub.client;

import java.util.ArrayList;
import java.util.List;

import org.statehub.client.data.Model;
import org.statehub.client.data.ModelDataModel;


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.info.Info;

public class StateHubBrowser extends Composite
{

	private static StateHubBrowserUiBinder uiBinder = GWT.create(StateHubBrowserUiBinder.class);
	private final StateHubServiceAsync statehubsvc = GWT.create(StateHubService.class);
	interface StateHubBrowserUiBinder extends UiBinder<Widget, StateHubBrowser> 	{}

	@UiField BorderLayoutContainer borderLayout;
	@UiField ContentPanel panel;
	@UiField ContentPanel navPanel;
	//@UiField HTMLPanel frontPageIntro;
    @UiField VerticalLayoutContainer vlc;
    @UiField VerticalLayoutContainer navPanelVC;
    @UiField VerticalLayoutContainer resultsVlc;
	@UiField TextButton searchButton;
	@UiField TextField searchText;
	ArrayList<Model> models;
	private static final ModelDataModel properties = GWT.create(ModelDataModel.class);
	Grid<Model> grid;
	ListStore<Model> store=new ListStore<Model>(properties.key());
	Boolean isShowList = true;
	ArrayList<Widget> listviewWidgets = new ArrayList<Widget>();
	ArrayList<Widget> historyWidgets = new ArrayList<Widget>();
	 RowExpander<Model> rowExpander = new RowExpander<Model>(new AbstractCell<Model>() {
	        @Override
	        public void render(Context context, Model value, SafeHtmlBuilder sb) {
	          sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Name:</b> " + value.getName() + "</p>");
	          sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Description:</b> " + value.getDescription());
	          sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>TAGS:</b> " + value.getTags().toReadable().replaceAll("img src=\"", "img src=\"images/"+value.getId()+"-"));
	        }
	      });
	
	public StateHubBrowser()
	{
		initWidget(uiBinder.createAndBindUi(this));
		panel.setWidth(com.google.gwt.user.client.Window.getClientWidth() - (navPanel.getOffsetWidth()));
		panel.setHeight(com.google.gwt.user.client.Window.getClientHeight()-20);
		//insertNavFrame();
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<img class=\"headerImage\" src=\"logoblue.png\"/>");
		sb.appendHtmlConstant("<span class=\"headerTxt\">ALPHA PREVIEW RELEASE (features may be added/changed)</span>");
		panel.setHeading(sb.toSafeHtml());
		vlc.setScrollMode(ScrollMode.AUTO);
		resultsVlc.setScrollMode(ScrollMode.AUTO);
		
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				panel.setWidth(event.getWidth()-200);
				panel.setHeight(event.getHeight()-20);
				insertNavFrame();
			}});
		
		searchText.addKeyDownHandler(new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					find(null);
			}
		});
		
		Image tableview = new Image("table.png");
		tableview.setTitle("Toggle between list and table views");
		tableview.setPixelSize(20, 20);
		panel.getHeader().addTool(tableview);
		tableview.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				History.newItem("", false);
				if(isShowList)
				{
					listviewWidgets.clear();
					for(int i = 0; i < resultsVlc.getWidgetCount(); i++)
						listviewWidgets.add(resultsVlc.getWidget(i));
					resultsVlc.clear();
					resultsVlc.add(grid);
					isShowList=false;
				}
				else
				{
					resultsVlc.clear();
					for(Widget w : listviewWidgets)
						resultsVlc.add(w);
					isShowList=true;
						
				}
			}});
		
		
		List<ColumnConfig<Model, ?>> columnDefs = new ArrayList<ColumnConfig<Model, ?>>();
		columnDefs.add(rowExpander);
		ColumnConfig<Model, String> cc1 = new ColumnConfig<Model, String>(properties.name(), 200, "Name");
		cc1.setCellPadding(false);
		ColumnConfig<Model, String> cc2 = new ColumnConfig<Model, String>(properties.category(), 150, "Type");
		ColumnConfig<Model, String> cc3 = new ColumnConfig<Model, String>(properties.description(), 400, "Description");
		ColumnConfig<Model, String> cc4 = new ColumnConfig<Model, String>(properties.id(), 100, "Unique id");
		ColumnConfig<Model, String> cc5 = new ColumnConfig<Model, String>(properties.revisionTxt(), 200, "revision");
		columnDefs.add(cc1);columnDefs.add(cc2);columnDefs.add(cc3);columnDefs.add(cc4);columnDefs.add(cc5);
		ColumnModel<Model> colModel = new ColumnModel<Model>(columnDefs);
		grid = new Grid<Model>(store,colModel);
		grid.getView().setAutoExpandColumn(cc3);
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event)
			{
				resultsVlc.clear();
				for(Widget w : historyWidgets)
					resultsVlc.add(w);
			}
	    });
		
		grid.addCellClickHandler(new CellClickHandler(){

			@Override
			public void onCellClick(CellClickEvent event)
			{
				if(event.getCellIndex()<1 || event.getCellIndex()==3 )
					return;
				Model m = store.get(event.getRowIndex());
				historyWidgets.clear();
				for(int i = 0; i < resultsVlc.getWidgetCount(); i++)
					historyWidgets.add(resultsVlc.getWidget(i));
				History.newItem("md");
				resultsVlc.clear();
				Label back = new Label("< Back to results");
				back.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event)
					{
						History.back();
					}});
				back.setStylePrimaryName("backLabel");
				resultsVlc.add(back);
				resultsVlc.add(new ModelView(m));
				
			}});
		rowExpander.initPlugin(grid);
		insertNavFrame();
		navPanel.addResizeHandler(new ResizeHandler(){

			@Override
			public void onResize(ResizeEvent event)
			{
				insertNavFrame();				
			}});
		
		//read GET string in URL and do search if text was passed. this is to handle links
		String searchGET = com.google.gwt.user.client.Window.Location.getParameter("search");
		if(searchGET != null )
		{
			searchText.setText(searchGET);
			find(null);
		}
		
		
		
	}
	@UiHandler("searchButton")
	public void find(SelectEvent event)
	{
		
		resultsVlc.clear();
		Image progress = new Image("progress.gif");
		progress.setHeight("200px");
		progress.setWidth("200px");
		resultsVlc.add(progress);
		statehubsvc.getModel(searchText.getText(), new AsyncCallback<ArrayList<Model>>(){

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Error",caught.getMessage());
			}

			@Override
			public void onSuccess(ArrayList<Model> result) {
				resultsVlc.clear();
				store.clear();
				store.addAll(result);
				listviewWidgets.clear();
				isShowList = true;
				Label hits = new Label(result.size() + " models found");
				hits.setStylePrimaryName("hitsLabel");
				resultsVlc.add(hits);
				for(final Model m : result)
					if(searchText.getText().length()>0)
						resultsVlc.add(new ModelView(m,searchText.getText()));
					else
						resultsVlc.add(new ModelView(m));
					//showModel(m);
			}});

	}
//	@UiHandler("importButton")
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
		resultsVlc.clear();
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
	
	private void insertNavFrame()
	{
		
		Frame f = new Frame("nav.html");
		f.setStylePrimaryName("navIframe");
		navPanel.setHeight(com.google.gwt.user.client.Window.getClientHeight()-50);
		navPanelVC.setHeight(com.google.gwt.user.client.Window.getClientHeight()-50);
		navPanelVC.clear();
		navPanelVC.add(f);
		
		
	}
}
