import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.WritableDataObject;
import com.adventnet.authentication.AAAUSER;
import com.adventnet.authentication.AAALOGIN;
import com.adventnet.authentication.AAAACCOUNT;
import com.adventnet.authentication.AAAPASSWORD;
import com.adventnet.authentication.AAAACCPASSWORD;
import com.adventnet.authentication.util.AuthUtil;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.Persistence;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignupServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataAccessException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String name=request.getParameter("name");
            String password=request.getParameter("password");
            
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence"); 
            DataObject dobj = persistence.constructDataObject();
            Row userRow = new Row(AAAUSER.TABLE);
            
            userRow.set(AAAUSER.FIRST_NAME,name);
            dobj.addRow(userRow);

            Row loginRow = new Row(AAALOGIN.TABLE);
            loginRow.set(AAALOGIN.NAME,name);
            dobj.addRow(loginRow);

            Row accRow = new Row(AAAACCOUNT.TABLE);
            accRow.set(AAAACCOUNT.SERVICE_ID, new Long(1));
            accRow.set(AAAACCOUNT.ACCOUNTPROFILE_ID,new Long(1));
            dobj.addRow(accRow);

            Row passwordRow = new Row(AAAPASSWORD.TABLE);
            passwordRow.set(AAAPASSWORD.PASSWORD, password);
            passwordRow.set(AAAPASSWORD.PASSWDPROFILE_ID,new Long(1));
            dobj.addRow(passwordRow);

            Row accPassRow = new Row(AAAACCPASSWORD.TABLE);
            accPassRow.set(AAAACCPASSWORD.ACCOUNT_ID, accRow.get(AAAACCOUNT.ACCOUNT_ID));
            accPassRow.set(AAAACCPASSWORD.PASSWORD_ID, passwordRow.get(AAAPASSWORD.PASSWORD_ID));
            dobj.addRow(accPassRow);
            
            AuthUtil.createUserAccount(dobj);
            
            /*Row row=new Row("Users");
            row.set("NAME", name);
            row.set("PASSWORD",password);
            DataObject dataObject = new WritableDataObject();
            dataObject.addRow(row);
            DataAccess.add(dataObject);*/
			
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
				ftp.makeDirectory("/Users/"+ name);
				if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
				}
			}	
			catch (Exception ex) {
				out.println("Exception");
	            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
	        }
			
            
	    	out.println("<p>Signup success. your User ID is : " + userRow.get("USER_ID") +  "</p>");
            //out.println("<p>Signup success.</p>");
            out.println("<a href=\"LoginServlet\">Click here to login</a>");
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
        } 
		catch (DataAccessException ex) {
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
		catch (Exception ex) {
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } 
		catch (DataAccessException ex) {
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
		catch (Exception ex) {
            Logger.getLogger(SignupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}