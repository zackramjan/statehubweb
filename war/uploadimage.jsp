<%@ page import="java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.output.*" %>

<%
// curl -i -F file=test.png -F filedata=@graph.png http://127.0.0.1:8888/uploadimage.jsp?id=ABC123TEST
   File file ;
   int maxFileSize = 5000 * 1024;
   int maxMemSize = 5000 * 1024;
   String filePath = getServletContext().getRealPath("/").replace('\\', '/') + "/images";
  
   String id = request.getParameter("id");
   String contentType = request.getContentType();
   if ((contentType != null && contentType.indexOf("multipart/form-data") >= 0)) {
	  DiskFileItemFactory factory = new DiskFileItemFactory();
      factory.setSizeThreshold(maxMemSize);
      factory.setRepository(new File("\tmp"));
      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setSizeMax( maxFileSize );
      try{ 
         List fileItems = upload.parseRequest(request);
         Iterator i = fileItems.iterator();
         out.println("<html>");
         out.println("<body>");
         while ( i.hasNext () ) 
         {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () )  {
                String fieldName = fi.getFieldName();
                String fileName = fi.getName();
                boolean isInMemory = fi.isInMemory();
                long sizeInBytes = fi.getSize();
                file = new File( filePath + "/" + id + "-" + fileName) ;
                fi.write( file ) ;
                out.println("Uploaded Filename: " + filePath + "/" + id + "-" + fileName);
            }
         }
         out.println("</body>");
         out.println("</html>");
      }catch(Exception ex) {
         System.out.println(ex);
      }
  }else{
      out.println("<html>");
      out.println("<body>");
      out.println("<p>No file uploaded</p>"); 
      out.println(getServletContext().getRealPath("/").replace('\\', '/'));
      out.println("</body>");
      out.println("</html>");
   }
%>