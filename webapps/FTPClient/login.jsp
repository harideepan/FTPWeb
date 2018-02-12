<html>
    <head>
        <title>Welcome</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
	<center>
		<h1>Login</h1>
        <form method="post" action='<%=response.encodeURL("j_security_check")%>'>
  			User name:
  			<input type="text" name="j_username" >
  			<br><br>
  			Password:
  			<input type="password" name="j_password" >
  			<br><br>
  			<input type="submit" value="Login"><br>
			<a href="signup.html">New user?</a>
		</form>  
	</center>
    </body>
</html>