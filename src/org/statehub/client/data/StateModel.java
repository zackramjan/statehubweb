package org.statehub.client.data;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface StateModel  extends PropertyAccess<StateModel>
{
	 ModelKeyProvider<State> key();
	 ValueProvider<State, String> name();
	 ValueProvider<State, Integer> order();
	 ValueProvider<State, String> description();
	 ValueProvider<State, String> format();
	 }
