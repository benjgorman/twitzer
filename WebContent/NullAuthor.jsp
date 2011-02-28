<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.dundee.computing.benjgorman.twitzer.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>That user doesn't exist</title>
<link rel="stylesheet" type="text/css" media="all" href="/Twitzer/style.css" /> 

</head>
<body>

<div class id="page">
	<div class ="header">
	
		<h1>Twitzer</h1>			
			<div class="nav clear">
				<ul>
					<li><a href="/Twitzer/">Home</a>
					<li><a href="Logout">Logout</a>
				</ul>
			</div>
			
	</div>
<% 

System.out.println("In RenderAuthor.jsp");
AuthorStore Author = (AuthorStore)request.getAttribute("Author");

if (Author==null){
%>
	<h1>Whoa dude!</h1>
<% 
}
%>
<h2>Sorry, that user does not exist.</h2>
</div>
</body>
</html>