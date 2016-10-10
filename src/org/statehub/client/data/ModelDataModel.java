package org.statehub.client.data;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ModelDataModel  extends PropertyAccess<Model>
{
	 ModelKeyProvider<Model> key();
	 ValueProvider<Model, String> id();
	 ValueProvider<Model, String> name();
	 ValueProvider<Model, String> category();
	 ValueProvider<Model, String> author();
	 ValueProvider<Model, String> description();
	 ValueProvider<Model, String> revisionTxt();
}
