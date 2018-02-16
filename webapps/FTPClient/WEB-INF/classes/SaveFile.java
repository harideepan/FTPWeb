import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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

public class SaveFile extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
		{
			if(request.getUserPrincipal()==null)
			{
				response.sendRedirect("LoginServlet");
			}
            else
            {	

            	String fileContent=request.getParameter("filecontent");
				String name=request.getUserPrincipal().getName();
				String fileName=request.getParameter("filename");
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
						InputStream stream = new ByteArrayInputStream(fileContent.getBytes());
						ftp.storeFile("/Users/"+ name +"/"+fileName,stream);
					}
						
					if (ftp.isConnected()) 
					{
				        ftp.logout();
				        ftp.disconnect();
					}
					response.sendRedirect("LoginServlet");
				}
				catch (Exception ex) {
					   Logger.getLogger(SaveFile.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SaveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(SaveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}