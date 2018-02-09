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

public class TestServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DataAccessException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String userID=request.getParameter("userid");
            String password=request.getParameter("password");
            Row row=new Row("Users");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            row.set("NAME", userID);
            row.set("PASSWORD",password);
            DataObject dataObject = new WritableDataObject();
            dataObject.addRow(row);
	    DataAccess.add(dataObject);
            
            Table table=new Table("Users");
            out.println("<p>Table Name: " + table.getTableName() +  "</p>");
            //Criteria c = new Criteria(new Column("Users", "FIRSTNAME"),"uma", QueryConstants.EQUAL);
            DataObject d = DataAccess.get("Users",(Criteria)null);
            Iterator it=d.getRows("Users");
            out.println("Hello " + userID + "</h1>");
            while(it.hasNext())
            {
                Row r=(Row)it.next();
                out.println(r.get(1) + " " + r.get(2) + " " + r.get(3));
            } 
            out.println("<p>END</p>");
            out.println("<p>Name: " + row.get("USER_ID") +  "</p>");
	    out.println("<p>Name: " + row.get("NAME") +  "</p>");
            out.println("<p>Password: " + row.get("PASSWORD") +  "</p>");
	    out.println("<p>END2</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (DataAccessException ex) {
            Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}