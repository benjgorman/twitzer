<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>Add a new User</h2>
<div>
<%@include file="LogCheck.jsp" %>
<P>Fill in the form to register <%= User.getemail()%></P>
<form action="/Twitzer/Author" method="POST">

Name:<input name="Name" value="<%= User.getname() %>"></input><br/>
Username:<input name="Username"></input><br/>
Avatar:<input name="Avatar"></input><br/>
Mobile:<input name="Tel"></input><br/>
Website:<input name="Website"></input><br/>
Bio:<input name="Bio"></input><br/>
<input type="submit"  value="Submit">
</form>

</div>
</body>
</html>