<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.dundee.computing.benjgorman.twitzer.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>That user doesn't exist</title>
<link rel="stylesheet" href="style.css" /> 

</head>
<body>
<% 

System.out.println("In RenderAuthor.jsp");
AuthorStore Author = (AuthorStore)request.getAttribute("Author");

if (Author==null){
%>
	<h1>Error no Author returned</h1>
<% 
}
%>
<h1>Sorry, that user does not exist.</h1>
</body>
</html>