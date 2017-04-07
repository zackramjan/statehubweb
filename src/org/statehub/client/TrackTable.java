package org.statehub.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.statehub.client.data.Track;
import org.statehub.client.data.TrackModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
//import com.sencha.gxt.widget.core.client.info.Info;

public class TrackTable extends Composite
{

	private static TrackTableUiBinder uiBinder = GWT.create(TrackTableUiBinder.class);
	private final StateHubServiceAsync statehubsvc = GWT.create(StateHubService.class);
	interface TrackTableUiBinder extends UiBinder<Widget, TrackTable> 	{ }
	
	@UiField VerticalLayoutContainer vlc;
	ArrayList<Track> tracks;
	private static final TrackModel properties = GWT.create(TrackModel.class);
	Grid<Track> grid;
	ListStore<Track> store=new ListStore<Track>(properties.key());
	String id;
	
	//handle live filtering of metrics, match multiple properties against filter txt
		StoreFilterField<Track> filter = new StoreFilterField<Track>() 
		{
			@Override
			protected boolean doSelect(Store<Track> store, Track parent,Track item, String filter) 
			{
				boolean match = true;
				for(String token : filter.split("\\s"))
					match = match && item.toString().toLowerCase().contains(token.toLowerCase());
				return match;				
			}
		 };

	 
	public TrackTable(String stateID)
	{
		initWidget(uiBinder.createAndBindUi(this));
		Image progress = new Image("progress.gif");
		progress.setHeight("200px");
		progress.setWidth("200px");
		
		vlc.add(progress);
		id=stateID;
		statehubsvc.getTrack(stateID, new AsyncCallback<ArrayList<Track>>(){

			@Override
			public void onFailure(Throwable caught)
			{
				vlc.clear();
				vlc.add(new Label(caught.toString()));
			}

			@Override
			public void onSuccess(ArrayList<Track> result)
			{
				tracks = result;
				renderTable();
			}});
		
	}
	public TrackTable(ArrayList<Track> tracks)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.tracks = tracks;
		renderTable();
	}

	private void renderTable()
	{
		vlc.clear();
		
		List<ColumnConfig<Track, ?>> columnDefs = new ArrayList<ColumnConfig<Track, ?>>();
		IdentityValueProvider<Track> identity = new IdentityValueProvider<Track>();
	      final CheckBoxSelectionModel<Track> selectionModel = new CheckBoxSelectionModel<Track>(identity);
		ColumnConfig<Track, String> cc1 = new ColumnConfig<Track, String>(properties.name(), 250, "Track Name");
		ColumnConfig<Track, String> cc15 = new ColumnConfig<Track, String>(properties.description(), 250, "Description");
		ColumnConfig<Track, String> cc2 = new ColumnConfig<Track, String>(properties.project(), 90, "Project");
		ColumnConfig<Track, String> cc3 = new ColumnConfig<Track, String>(properties.genome(), 90, "Genome");
		ColumnConfig<Track, String> cc4 = new ColumnConfig<Track, String>(properties.marksString(), 150, "Marks");
		ColumnConfig<Track, String> cc5 = new ColumnConfig<Track, String>(properties.statePaintRVersion(), 60, "StatePaintR Version");
		ColumnConfig<Track, String> cc6 = new ColumnConfig<Track, String>(properties.bedURL(), 90, "Get");
		cc6.setCell(new SimpleSafeHtmlCell<String>(new AbstractSafeHtmlRenderer<String>() 
			{
			      public SafeHtml render(String url) 
			      {  
			    	return SafeHtmlUtils.fromTrustedString("<a target=\"new\" href=\"" + url + " \">download</a>");
			      }
			}));
		
		columnDefs.add(selectionModel.getColumn());columnDefs.add(cc1);columnDefs.add(cc15);columnDefs.add(cc2);columnDefs.add(cc3);columnDefs.add(cc4);columnDefs.add(cc5);columnDefs.add(cc6);
		ColumnModel<Track> colModel = new ColumnModel<Track>(columnDefs);
		grid = new Grid<Track>(store,colModel);
		grid.setSelectionModel(selectionModel);
		grid.getView().setAutoExpandColumn(cc1);
	    grid.getView().setStripeRows(true);
	    grid.getView().setColumnLines(true);
	    store.replaceAll(tracks);
	    filter.bind(store);
	    filter.setEmptyText("search tracks...");
	    
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.add(filter);
	    filter.setWidth(200);
	    TextButton launchBrowser = new TextButton("View Selected Tracks on Genome Browser");
	    launchBrowser.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event)
			{
				String selected = "";
				HashMap<String,Integer> genomeCount = new HashMap<String,Integer>();
				for(Track t : selectionModel.getSelectedItems())
				{
					selected +=t.getOrder() + "-";
					if(!genomeCount.containsKey(t.getGenome()))
						genomeCount.put(t.getGenome(), new Integer(1));
					else
						genomeCount.put(t.getGenome(),genomeCount.get(t.getGenome())+1);
				}
				String genome = "hg19";
				Integer count = new Integer(0);
				for (String key : genomeCount.keySet())
				{
					if(genomeCount.get(key) > count)
					{
						genome = key;
						count = genomeCount.get(key);
					}
				}
				selected = selected.substring(0, selected.length()-1);
				String url = "http://genome.ucsc.edu/cgi-bin/hgTracks?db="+ genome + "&hubUrl=http://statehub.org/statehub/trackhub.jsp?id=" + id + "-" + selected;
				//Info.display("launching Browser",url);
				Window.open(url, "_blank", "");
			}});
	    
	    TextButton launchDir = new TextButton("Browse the Folder");
	    launchDir.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event)
			{
				String selected = "";
				for(Track t : selectionModel.getSelectedItems())
					selected +=t.getOrder() + "-";
				selected = selected.substring(0, selected.length()-1);
				String url = "http://statehub-trackhub.s3-website-us-west-2.amazonaws.com/?prefix=tracks/" + id;
				//Info.display("launching Browser",url);
				Window.open(url, "_blank", "");
			}});
	    
	    hp.add(launchBrowser);
	    hp.add(launchDir);
	    vlc.add(hp);
	    vlc.add(grid);	    
	}
}
