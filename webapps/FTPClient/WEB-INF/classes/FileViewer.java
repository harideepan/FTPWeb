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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

public class FileViewer extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataAccessException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
		{
			String fileName=request.getParameter("filename");
			String action=request.getParameter("action");
			String name=request.getUserPrincipal().getName();
			
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>File Viewer</title>");            
            out.println("</head>");
            out.println("<body><center>");
            
			if(!action.equals("new"))
			{
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
					
					if(!fileName.endsWith(".csv"))
					{
						InputStream is=ftp.retrieveFileStream("/Users/"+ name +"/"+fileName);
		                BufferedReader br = new BufferedReader(new InputStreamReader(is));
		                String line;
		                String fileContent="";
		                while((line=br.readLine())!=null)
		                {
		                    fileContent=fileContent + "\n" + line;
		                }
		                out.println("<form action=\"SaveFile\" method=\"post\">");
						out.println("File name <input type=\"text\" name=\"filename\" value=\"" + fileName + "\">");
						out.println("<br><textarea style='width: 50em; height:50em' name=\"filecontent\">"+ fileContent +"</textarea>");
						out.println("<br><input type=\"submit\" value=\"Save file\">");
						out.println("</form>");
					}
					else
					{
						out.println("<form action=\"SaveFile\" method=\"post\">");
						out.println("File name <input type=\"text\" name=\"filename\" value=\"" + fileName + "\">");
						out.println("<table border=\"1\">");
						InputStream is=ftp.retrieveFileStream("/Users/"+ name +"/"+fileName);
		                List<String >fileContent=new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
                		for (String line:fileContent)
                		{
							out.println("<tr>");
                    		String[] array = line.split(",");
							for(int j=0;j<array.length;j++)
							{
								out.println("<td>" + array[j] + "</td>");
							}
							out.println("</tr>");
                		}
						//out.println("<br><input type=\"submit\" value=\"Save file\">");
						out.println("</form>");
					}
					
					if (ftp.isConnected()) {
			        	ftp.logout();
			            ftp.disconnect();
					}
				}
				catch (Exception ex) {
					out.println("Exception");
				    Logger.getLogger(FileViewer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			else
			{
				out.println("<form action=\"SaveFile\" method=\"post\">");
				out.println("File name <input type=\"text\" name=\"filename\" value=\"\">");
				out.println("<br><textarea style='width: 50em; height:50em' name=\"filecontent\"></textarea>");
				out.println("<br><input type=\"submit\" value=\"Save file\">");
				out.println("</form>");
			}
            out.println("</center></body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(FileViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(FileViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}