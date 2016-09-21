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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

public class ModelView extends Composite 
{

	private static ModelViewUiBinder uiBinder = GWT.create(ModelViewUiBinder.class);

	interface ModelViewUiBinder extends UiBinder<Widget, ModelView>	{}
	@UiField FramedPanel panel;
    @UiField VerticalLayoutContainer vlc;
	private static final StateModel properties = GWT.create(StateModel.class);
	ListStore<State> store=new ListStore<State>(properties.key());
	public ModelView(Model model)
	
	{
		
		initWidget(uiBinder.createAndBindUi(this));
		panel.setWidth(500);
		
		List<ColumnConfig<State, ?>> columnDefs = new ArrayList<ColumnConfig<State, ?>>();
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
						int g = 40 * (Integer.parseInt(value) + 3);
						int b = 80 * (Integer.parseInt(value) + 4);
						String color = "rgb(" + r + "," + g + "," + b + ")";
						sb.appendHtmlConstant("<span style=\"overflow:auto;height:100%;width:100px;display:block;background-color:" + color + "\" title=\"score is "+  value + "\">" + value + "</span>");
						
					}	 
				 });
		}
		ColumnModel<State> colModel = new ColumnModel<State>(columnDefs);
		
		
		
		store.addAll(model.getStates());
		final Grid<State> stateGrid = new Grid<State>(store,colModel);
		
		vlc.add(stateGrid);
		
		
		
	}


}
