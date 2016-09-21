package org.statehub.client.data;
import com.sencha.gxt.core.client.ValueProvider;

public class StateModelFactory
{
	static public ValueProvider<State,String> getValueProvider(final int index)
	{
		 ValueProvider<State,String> c1 =new ValueProvider<State,String>()
		 {

			@Override
			public String getValue(State object)
			{
				if(index < object.getFeatures().size())
						return object.getFeatures().get(index).getScore().toString();
				return "";
			}

			@Override
			public void setValue(State object, String value)
			{
				
						
			}

			@Override
			public String getPath()
			{
				return String.valueOf(index);
			} 
		 };
		 return c1;
}
}