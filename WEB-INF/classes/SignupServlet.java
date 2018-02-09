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

public class SignupServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataAccessException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String name=request.getParameter("name");
            String password=request.getParameter("password");
            Row row=new Row("Users");
			row.set("NAME", name);
            row.set("PASSWORD",password);
            DataObject dataObject = new WritableDataObject();
            dataObject.addRow(row);
			DataAccess.add(dataObject);
			
			out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Signup success</title>");            
            out.println("</head>");
            out.println("<body><center><br><br>");
			
			FTPClient ftp=new FTPClient();
			try {
            	ftp.connect("localhost",4444);
	        	int reply = ftp.getReplyCode();
		        if (!FTPReply.isPositiveCompletion(reply)) {
						out.println("<p>Error connecting to FTP server</p>");
		                ftp.disconnect();
		        }
				ftp.enterLocalPassiveMode();
		        ftp.login("Hariharan", "");
				ftp.makeDirectory("/Users/"+row.get("USER_ID"));
				if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
				}
				
			catch (Exception ex) {
				out.println("Exception");
	            Logger.getLogger(FTPClassA.class.getName()).log(Level.SEVERE, null, ex);
	        }
			
            
	    	out.println("<p>Signup success. your User ID is : " + row.get("USER_ID") +  "</p>");
			out.println("<a href=\"index.html\">Click here to login</a>");
            out.println("</center></body>");
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
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}