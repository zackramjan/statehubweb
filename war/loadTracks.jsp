<%@ page language="java" contentType="text/plain; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*,java.lang.*,java.io.*,org.statehub.client.data.*,org.statehub.server.StateHubServiceImpl,com.google.gwt.util.tools.shared.Md5Utils"%><%
try
{
	StateHubServiceImpl backend = new StateHubServiceImpl();
	String idsString = request.getParameter("id");
	String[] ids = idsString.split("-");
	String id = ids[0];
	String genomes = request.getParameter("genomes");
	String trackdb = request.getParameter("trackdb");
	
	ArrayList<ArrayList<Track>> store = new ArrayList<ArrayList<Track>>();
	store.add(backend.getTrack("5813b67f46e0fb06b493ceb0"));
	store.add(backend.getTrack("581ff9f246e0fb06b4b6b178"));
	
	
	
	
	
	
	
	
}
catch(Exception e)
{
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	out.println(sw.toString());
}
%>