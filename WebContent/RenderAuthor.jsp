<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.dundee.computing.benjgorman.twitzer.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Display Single Author</title>
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
else
{
%>
<h1><%=Author.getuserName()%></h1>
<img src=<%=Author.getAvatar() %> alt="<%=Author.getuserName() %>" /> <br/>
<p><a href="/Twitzer/Post/<%=Author.getuserName() %>">All Posts by <%=Author.getname() %> (<%=Author.getuserName() %>)</a><br/>
Name:	<%=Author.getname() %><br/>
Email:	<%=Author.getEmail() %>  <br/>
Telephone:  <%=Author.getTel() %>  <br/>
Bio: <%=Author.getBio() %>  <br/>
NumPosts: <%=Author.getnumPosts() %>
	<% 
}
%>
<%-- 
<p>List all <a href="/jBloggyAppy/"+<%=Author.getname()%>>Subscriptions</a></p>
--%>
<p><a href="/Twitzer/Author">Return to Authors list</a></p>
</body>
</html>