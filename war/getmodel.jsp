<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*,java.io.*,com.google.gson.Gson,org.statehub.client.data.*,com.google.gson.Gson,org.statehub.server.StateHubServiceImpl"%>
<%
try
{
	StateHubServiceImpl backend = new StateHubServiceImpl();
	String search = request.getParameter("search");
	String id = request.getParameter("id");
	ArrayList<Model> full = backend.getModel(search);
	ArrayList<Model> filtered = new ArrayList<Model>();
	if(id != null && id.length() > 0)
	{
	 	for(Model m : full)
	 		if(m.getId().contains(id))
	 			filtered.add(m);
	}
	else
		filtered = full;
	 backend.getModel(search);
	 Gson gson = new Gson();
     out.print( gson.toJson(filtered));
}
catch(Exception e)
{
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	out.println(sw.toString());
}
%>