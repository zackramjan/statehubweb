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
	
	ArrayList<Track> tracksAll = backend.getTrack(id);
	ArrayList<Track> tracks = new ArrayList<Track>();
	if(ids.length< 2)
		tracks = tracksAll;
	else
	{
		for(int i = 1; i<ids.length;i++)
			tracks.add(tracksAll.get(Integer.parseInt(ids[i])-1));
	}
	 	
	if(id != null && id.length() > 0 && tracks.size() > 0 && genomes == null)
	{
		final StringBuilder builder = new StringBuilder();
	    for(byte b : Md5Utils.getMd5Digest(idsString)) {
	        builder.append(String.format("%02x", b));
	    }
	    String md5sum = builder.toString();
		out.println("hub StateHub-" + md5sum);
		out.println("shortLabel StateHub.org session " + md5sum);
		out.println("longLabel StateHub session + " +  md5sum + " , Berman Lab at Cedars Sinai Hospital in Los Angeles");
		out.println("genomesFile trackhub.jsp?id=" + idsString + "&genomes");
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
				out.println("trackDb trackhub.jsp?id=" + idsString + "&genomes="+t.getGenome()+"&trackdb");
				out.println();
			}		
		}
	}
	else if(id != null && id.length() > 0 && tracks.size() > 0 && genomes != null && trackdb != null)
	{
		String s = "";
		for(Track t : tracks)
		{
			if(t.getGenome().equals(genomes))
			{
				s+="track " + t.getBedFileName().replace(" ","_").substring(0, t.getBedFileName().lastIndexOf(".",t.getBedFileName().lastIndexOf(".")-1))  + "\n";
				s+="bigDataUrl " + t.getBigBedURL()+ "\n";
				s+="shortLabel " + t.getName()+"\n";
				s+="longLabel " + t.getDescription()+"\n";
				s+="visibility dense\n";
				s+="type bigBed 9\n";
				s+="itemRgb on\n";
				s+="labelOnFeature on\n";
				s+="spectrum off\n";
				s+="\n";
				
				
			}
		}
		response.setContentLength(s.getBytes().length);
		out.print(s);
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
