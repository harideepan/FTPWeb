import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Query;
import com.adventnet.ds.query.SelectQuery;
import com.adventnet.ds.query.SelectQueryImpl;
import com.adventnet.ds.query.Table;
import com.adventnet.persistence.Row;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.WritableDataObject;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;

import java.lang.Math;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataAccessException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
		{
			String name=request.getUserPrincipal().getName();
			String pageNumberFiles=request.getParameter("pageNoFiles");
			int pageNoFiles;
			String pageNumberUsers=request.getParameter("pageNoUsers");
			int pageNoUsers;
			String defaultOpen=request.getParameter("defaultOpen");

			int recordsPerPage=5;
			boolean admin=false;

			if(pageNumberFiles==null)
			{
				pageNoFiles=1;
			}
			else
			{
				pageNoFiles=Integer.parseInt(pageNumberFiles);
			}

			if(pageNumberUsers==null)
			{
				pageNoUsers=1;
			}
			else
			{
				pageNoUsers=Integer.parseInt(pageNumberUsers);
			}

			if(defaultOpen==null)
			{
				defaultOpen="Home";
			}

			Criteria c = new Criteria(new Column("Users", "NAME"),name, QueryConstants.EQUAL);
			DataObject d = DataAccess.get("Users",c);
			Iterator it=d.getRows("Users");
			if(it.hasNext())
			{
				Row r=(Row)it.next();
				if(r.get(3).toString().equalsIgnoreCase("admin"))
				{
					admin=true;
				}
			} 


            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Welcome</title>"); 
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");        
            out.println("</head>");
            out.println("<body><center>");
            
            if(!defaultOpen.equalsIgnoreCase("AdminPanel"))
            	out.println("<button class=\"tablink\" onclick=\"openPage('Home', this, '#2196F3')\" id=\"defaultOpen\">Home</button>");
            else
            	out.println("<button class=\"tablink\" onclick=\"openPage('Home', this, '#2196F3')\">Home</button>");
            if(admin)
            {
            	if(!defaultOpen.equalsIgnoreCase("AdminPanel"))
            		out.println("<button class=\"tablink\" onclick=\"openPage('AdminPanel', this, '#ff5722')\">Admin Panel</button>");
            	else
            		out.println("<button class=\"tablink\" onclick=\"openPage('AdminPanel', this, '#ff5722')\" id=\"defaultOpen\">Admin Panel</button>");
            }

            out.println("<div id=\"Home\" class=\"tabcontent\">");
			out.println("<h1>Welcome " + name + "</h1>");
			out.println("<a href='logout.jsp'>Logout</a>");
			FTPClient ftp=new FTPClient();
			try 
			{
		        ftp.connect("localhost",4444);
			    int reply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					out.println("<p>Error connecting to FTP server</p>");
				    ftp.disconnect();
				}
				ftp.enterLocalPassiveMode();
				ftp.login("Hariharan", "");
				FTPFile[] files = ftp.listFiles("/Users/" + name);
				int l=files.length;
				int totalPages=(l/recordsPerPage)+(((l%recordsPerPage)==0)?0:1);
				
				int start=(pageNoFiles-1)*recordsPerPage;
				int end=start+recordsPerPage-1;
				if(end+1>l)
				{
					end=l-1;
				}
				out.println("<br><h2>Files in your workspace</h2>");	
				if(l==0)
				{
					out.println("<p>Workspace empty</p>");
				}
				else
				{
					out.println("<table border=\"1\"><tr><th>Name</th><th>Last modified</th><tr>");
				    for (int i=start;i<=end;i++) 
					{
						FTPFile file=files[i];
						if (file.getType() == FTPFile.FILE_TYPE)
						{
							out.println("<tr>");
							out.println("<td><form action='FileViewer' method='post'>");
							out.println("<input type='hidden' name='filename' value='" + file.getName() + "' />");
							out.println("<input type='hidden' name='action' value='existing'/>");
							out.println("<input type='submit' style='width: 15em; background:none!important; border:none;color: white;text-decoration:underline;cursor:pointer;' value='" + file.getName() + "'/>");
							out.println("</form></td>");
							out.println("<td>" + file.getTimestamp().getTime() + "</td>");
							out.println("</tr>");
						}
				    }
					out.println("</table>");
					out.println("<div class='pagination'>");
				    if(pageNoFiles!=1)
				    {
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoFiles="+ 1 +"'><<</a>");
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoFiles="+ (pageNoFiles-1) +"'><</a>");
				    }
				    for(int i=1;i<=totalPages;i++)
				    {
				    	if(i==pageNoFiles)
				    	{
				    		out.print("<a class='pageNumber current' href='LoginServlet?pageNoFiles="+ i +"'>" + i + "</a>");
				    	}
				    	else
				    	{	
				    		out.print("<a class='pageNumber' href='LoginServlet?pageNoFiles="+ i +"'>" + i + "</a>");
				    	}
				    }
				    if(pageNoFiles!=totalPages)
				    {
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoFiles="+ (pageNoFiles+1) +"'>></a>");
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoFiles="+ totalPages +"'>>></a>");
				    }
				    out.println("</div>");
				    out.println("<div>");
				    out.println("Showing " + (start+1) + "-" + (end+1) + " of " + l);
				    out.println("</div>");
				}
				out.println("<form action='FileViewer' method='post'>");
				out.println("<input type='hidden' name='action' value='new'/>");
				out.println("<input type='submit' value='New file'/>");
				out.println("</form>");
				out.println("</div>");

				if(admin)
				{
					out.println("<div id=\"AdminPanel\" class=\"tabcontent\">");
					d = DataAccess.get("Users",(Criteria)null);
					it=d.getRows("Users");
					l=0;
					while(it.hasNext())
					{
						it.next();
						l++;
					}
					l--;//excluding the current user
					totalPages=(l/recordsPerPage)+(((l%recordsPerPage)==0)?0:1);
					start=(pageNoUsers-1)*recordsPerPage;
					end=start+recordsPerPage-1;
					if(end+1>l)
					{
						end=l-1;
					}
					it=d.getRows("Users");
					int index=0;
					out.println("<table border=\"1\"><tr><th>User name</th><th>Role</th><th>Action</th><tr>");
					while(it.hasNext())
					{
						Row r=(Row)it.next();
						if(!r.get(2).toString().equals(name))
						{	
							if(index>=start && index<=end)
							{
								out.println("<tr>");
								out.println("<td>" + r.get(2) + "</td>");
								out.println("<td>" + r.get(3) + "</td>");
								out.println("<td><form action='ChangeRole' method='post'>");
								out.println("<input type='hidden' name='name' value='" + r.get(2) + "' />");
								if(r.get(3).toString().equalsIgnoreCase("admin"))
								{
									out.println("<input type='hidden' name='toRole' value='user'/>");
									out.println("<input type='submit' value='Remove admin rights'/>");
								}
								else
								{
									out.println("<input type='hidden' name='toRole' value='admin'/>");
									out.println("<input type='submit' value='Promote to admin'/>");
								}
								out.println("</form></td>");
								out.println("</tr>");
							}
							index++;
						}
					} 
					out.println("</table>");
					out.println("<div class='pagination'>");
					if(pageNoUsers!=1)
				    {
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoUsers="+ 1 +"&defaultOpen=AdminPanel'><<</a>");
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoUsers="+ (pageNoUsers-1) +"&defaultOpen=AdminPanel'><</a>");
				    }
				    for(int i=1;i<=totalPages;i++)
				    {
				    	if(i==pageNoUsers)
				    	{
				    		out.print("<a class='pageNumber current' href='LoginServlet?pageNoUsers="+ i +"&defaultOpen=AdminPanel'>" + i + "</a>");
				    	}
				    	else
				    	{	
				    		out.print("<a class='pageNumber' href='LoginServlet?pageNoUsers="+ i +"&defaultOpen=AdminPanel'>" + i + "</a>");
				    	}
				    }
				    if(pageNoUsers!=totalPages)
				    {
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoUsers="+ (pageNoUsers+1) +"&defaultOpen=AdminPanel'>></a>");
				    	out.print("<a class='pageNumber' href='LoginServlet?pageNoUsers="+ totalPages +"&defaultOpen=AdminPanel'>>></a>");
				    }
				    out.println("</div>");
				    out.println("<div>");
				    out.println("Showing " + (start+1) + "-" + (end+1) + " of " + l);
				    out.println("</div>");

					out.println("</div>");
				}
				out.println("<script src=\"opentab.js\"></script><script>document.getElementById(\"defaultOpen\").click();</script>");
				if (ftp.isConnected()) {
		            ftp.logout();
		            ftp.disconnect();
				}
			}
			catch (Exception ex) {
				out.println("Exception");
				Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
			}
			out.println("</center>");
            out.println("</body>");
            out.println("</html>");
			out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}