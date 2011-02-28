<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.dundee.computing.benjgorman.twitzer.stores.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html >
<html>
<head>
<link rel="stylesheet" type="text/css" media="all" href="/Twitzer/style.css" /> 
<meta charset=UTF-8>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<% 
System.out.println("In RenderAuthor.jsp");
AuthorStore Author = (AuthorStore)request.getAttribute("Author");
%>
<title>Twitzer / <%=Author.getuserName() %></title>
<jsp:useBean id="User"
class="uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore"
scope="session"
></jsp:useBean>

</head>
<body>

	<section id="page">
	<div class ="header">
	
		<h1>Twitzer</h1>			
			<div class="nav clear">
				<ul>
					<li><a href="/Twitzer/">Home</a>
					<li><a href="/Twitzer/Logout">Logout</a>
				</ul>
			</div>
			
	</div>
	<div class="section" id="articles">
		<%
		if (Author==null)
		{
		%>
			<h1>Error no Author returned</h1>
		<% 
		}
		else
		{
		%>
		<h2><%=Author.getname() %> (<%=Author.getuserName()%>)</h2>
		<img src=<%=Author.getAvatar() %> alt="<%=Author.getuserName() %>" height=80pixels width=80pixels/> <br/>
		About: <p> <%=Author.getBio() %> </p>
		Mail:  <%=Author.getEmail() %>  <br/>
		<% 
		}
		%>
		<a href="/Twitzer/Follow/<%=Author.getuserName()%>">Following</a>
		<a href="/Twitzer/Follows/<%=Author.getuserName()%>">Followers</a>
		
		<div id="followButton"></div>
	<div id="tweets"></div>
	</div>
</section>

<div class="footer">

			<p>Copyright 2011 - Twitzer</p>

		</div>
<script>
$(function() {followLoad();});
function followLoad ()
{
	$.getJSON("/Twitzer/Author/<%=Author.getuserName()%>/json", function(json) 
	{
			$("#followButton").empty();
			var test="true";
			var test2=json.Following;
			
			var userName = "<%=Author.getuserName()%>";
			var loggedIn = "<%=User.getUsername()%>";
			
			if (userName==loggedIn)
			{
				$("#followButton").append('<h3>Firstly, why are you stalking your own profile?</h3>');
			}
			else if (test2==test)
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
		
		$("#tweets").prepend('<div class="tweet" float: left><img src= '+this.AvatarUrl +' height=40pixels width=40pixels/><a href="/Twitzer/Author/'+this.Twit+'">'+this.Twit+' </a>'+this.Content+'</div>');

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