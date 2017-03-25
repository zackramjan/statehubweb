<%@ page language="java" contentType="text/plain; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*,java.io.*,org.statehub.client.data.*,org.statehub.server.StateHubServiceImpl"%><%
try
{
	StateHubServiceImpl backend = new StateHubServiceImpl();
	String id = request.getParameter("id");
	String genomes = request.getParameter("genomes");
	String trackdb = request.getParameter("trackdb");
	
	ArrayList<Track> tracks = backend.getTrack(id);
	if(id != null && id.length() > 0 && tracks.size() > 0 && genomes == null)
	{
		out.println("hub StateHub");
		out.println("shortLabel StateHub Tracks for model");
		out.println("longLabel StateHub Tracks for model, Berman Lab at Cedars Sinai Hospital in Los angeles");
		out.println("genomesFile trackhub.jsp?id=" + id + "&genomes");
		out.println("email help@statehub.org");
	}
	else if(id != null && id.length() > 0 && tracks.size() > 0 && genomes != null && trackdb == null)
	{
		HashSet<String> set = new HashSet<String>();
		for(Track t : tracks)
		{
			if(!set.contains(t.getGenome()))
			{
				set.add(t.getGenome());
				out.println("genome " + t.getGenome());
				out.println("trackDb trackhub.jsp?id=" + id + "&genomes="+t.getGenome()+"&trackdb");
				out.println();
			}		
		}
	}
	else if(id != null && id.length() > 0 && tracks.size() > 0 && genomes != null && trackdb != null)
	{
		for(Track t : tracks)
		{
			if(t.getGenome().equals(genomes))
			{
				out.println("track " + t.getFilename());
				out.println("bigDataUrl " + t.getUrlBigBed());
				out.println("shortLabel " + t.getFilename() + " " + t.getMark() + " " + t.getSegfile());
				out.println("LongLabel " + t.getFilename() + " " + t.getMark() + " " + t.getSegfile());
				out.println("type bigBed ");
				out.println("");
			}
		}
	}
	
	
	
}
catch(Exception e)
{
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	out.println(sw.toString());
}
%>