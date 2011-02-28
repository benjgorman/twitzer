<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta charset=UTF-8>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<title>Twitzer / Home</title>
<link rel="stylesheet" type="text/css" media="all" href="style.css" /> 

<jsp:useBean id="User"
class="uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore"
scope="session"
></jsp:useBean>
<jsp:useBean id="ReturnPoint"
class="uk.ac.dundee.computing.benjgorman.twitzer.stores.ReturnStore"
scope="session"
></jsp:useBean>
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
		if (User.isloggedIn()==false){
		System.out.println("Log check redirect to login");
		response.sendRedirect("/Twitzer/Login.jsp");
		}
		%>

<div class="section" id="articles">
	<h3>Welcome back <%if (User.isloggedIn()==true)%><%=User.getUsername() %>!</h3>
			<%if (User.isloggedIn()==true) 
			{%> <img src=<%=User.getAvatar() %> alt="<%=User.getUsername() %>" height=80pixels width=80pixels/><%} %>
			<form id="tweet"> 
			<fieldset class="login"> 
				<legend>Whats happening where you are?</legend>
					<div>
						<textarea rosw="7" cols="60" id="tweetArea" name="Content" placeholder="Whats happening where you are?">
						</textarea>
					</div>
				<input type="submit" value="Tweet"/>
	</fieldset>
</form>
</div>

<div id="timeline"></div>
<br>
<br>
</div>

		<div class="footer">

			<p>Copyright 2011 - Twitzer</p>

		</div>

<script>
$(function() {timelineLoad();});
window.setInterval(timelineLoad, 20000);
function timelineLoad()
{
	$.getJSON("/Twitzer/Timeline/json", function(json) {
		$("#timeline").empty();
		$.each(json.Data, function(i, Data) {
			$("#timeline").prepend('<div class="tweet" float: left><img src= '+this.AvatarUrl +' height=40pixels width=40pixels/><a href="/Twitzer/Author/'+this.Twit+'">'+this.Twit+' </a>'+this.Content+'</div>');

	  });
	});
}
$("#tweet").submit(function() 
{
	  var data = $(this).serialize();
	  $.ajax({
			aysnc: true,
				type: "POST",
				url: "/Twitzer/Tweet?" + data,
				dataType: "text",
				success: function(msg)
				{
					 $("#tweetArea").val('');
					 timelineLoad();
				}
		});
	  return false;
});

</script>
</body>

</html>