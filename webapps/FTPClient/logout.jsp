 <%
        request.getSession().invalidate();
	response.sendRedirect("LoginServlet");
  %>