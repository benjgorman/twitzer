<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta charset=UTF-8>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
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

<form id="tweet" action="/Twitzer/Tweet" method="POST"> 
<textarea name="Content" placeholder="whats happening?"></textarea>
<input type="submit" value="Tweet"/>
</form>
<hr></hr>
<div id="images"></div>
<script>
$.getJSON("http://api.flickr.com/services/feeds/photos_public.gne?tags=cat&tagmode=any&format=json&jsoncallback=?",
        function(data){
          $.each(data.items, function(i,item){
            $("<img/>").attr("src", item.media.m).appendTo("#images");
            if ( i == 3 ) return false;
          });
        });
</script>
</body>

</html>