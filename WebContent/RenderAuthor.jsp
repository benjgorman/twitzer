<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.dundee.computing.benjgorman.twitzer.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<title>Display Single Author</title>
<jsp:useBean id="User"
class="uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore"
scope="session"
></jsp:useBean>
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
<div id="followButton"></div>
<div id="test"></div>
<div id="tweets"></div>
<script>
$(function() {followLoad();});
function followLoad ()
{
	$.getJSON("/Twitzer/Author/<%=Author.getuserName()%>/json", function(json) 
	{
			$("#followButton").empty();
			var test="true";
			var test2=json.Following;
			
			if (test2==test)
			{
				$("#followButton").append('<button id="unfollow">Unfollow</button>');
			}
			else
			{
				$("#followButton").append('<button id="follow">Follow</button>');
			}

		  });
}
$.getJSON("/Twitzer/Tweet/<%=Author.getuserName()%>/json", function(json) {

	$.each(json.Data, function(i, Data) {
		
		$("#tweets").append('<h3>' + this.Twit + this.Content + '</h3>');

	  });
	});
$("#follow").live('click', function () {
	$.ajax({
		aysnc: true,
			type: "POST",
			url: "/Twitzer/Follow/<%= Author.getuserName() %>",
			dataType: "text",
			success: function(msg)
			{
				 followLoad();
			}
	});
});
$("#unfollow").live('click', function () {
	$.ajax({
		aysnc: true,
			type: "DELETE",
			url: "/Twitzer/Follow/<%= Author.getuserName() %>",
			dataType: "text",
			success: function(msg)
			{
				followLoad();
			}
	});
	});
</script>
</body>
</html>