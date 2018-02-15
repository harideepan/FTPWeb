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
			boolean admin=false;

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
            
            
            out.println("<button class=\"tablink\" onclick=\"openPage('Home', this, '#2196F3')\" id=\"defaultOpen\">Home</button>");
            if(admin)
            {
            	out.println("<button class=\"tablink\" onclick=\"openPage('AdminPanel', this, '#ff5722')\">Admin Panel</button>");
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
				out.println("<br><h2>Files in your workspace</h2>");	
				if(l==0)
				{
					out.println("<p>Workspace empty</p>");
				}
				else
				{
					out.println("<table border=\"1\"><tr><th>Name</th><th>Last modified</th><tr>");
				    for (FTPFile file : files) 
					{
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
					out.println("<table border=\"1\"><tr><th>User name</th><th>Role</th><th>Action</th><tr>");
					while(it.hasNext())
					{
						Row r=(Row)it.next();
						if(!r.get(2).toString().equals(name))
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
					} 
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