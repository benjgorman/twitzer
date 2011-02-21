<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta charset=UTF-8>
<title>Twitzer</title>
   
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

<h3>Twitzer</h3>

<h3> <%if (User.isloggedIn()==true)%><%=User.getUsername() %> </h3>
<h3> <%if (User.isloggedIn()==true)%><%=User.getNumPosts() %> </h3>
<%if (User.isloggedIn()==true) {%> <img src=<%=User.getAvatar() %> alt="<%=User.getUsername() %>" height=40pixels width=40pixels/><%} %>

<form id="tweet" action="/Twitzer/Tweet"> 
<textarea placeholder="whats happening?"></textarea>
<input type="submit" value="Tweet"/>
</form>
	<hr></hr>

</body>
</html>