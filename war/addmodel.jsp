<%@ page language="java" contentType="text/plain; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.*,java.io.*,com.google.gson.Gson,org.statehub.client.data.*,com.google.gson.Gson,org.statehub.server.StateHubServiceImpl"%>
<%
//curl -H "Content-Type: application/json" -X POST -d @upload.json http://50.112.80.113/statehub/addmodel.jsp
try
{
	StateHubServiceImpl backend = new StateHubServiceImpl();
	 StringBuffer jb = new StringBuffer();
	  String line = null;
	  
	    BufferedReader reader = request.getReader();
	   while ((line = reader.readLine()) != null)
	         jb.append(line);
	   
	   backend.storeModel(backend.fromJson(jb.toString()));	   

	   
	   out.println("added model");
}
catch(Exception e)
{
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	out.println(sw.toString());
}
%>
