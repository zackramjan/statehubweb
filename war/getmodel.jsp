<%@ page language="java" contentType="text/plain; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.*,java.io.*,com.google.gson.Gson,org.statehub.client.data.*,com.google.gson.Gson,org.statehub.server.StateHubServiceImpl"%>
<%
try
{
	StateHubServiceImpl backend = new StateHubServiceImpl();
	String search = request.getParameter("search");
	 
	   
	 
	   
	   out.println("search: " + search);
}
catch(Exception e)
{
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	out.println(sw.toString());
}
%>