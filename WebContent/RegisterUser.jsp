<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Twitzer / Home</title>
<link rel="stylesheet" type="text/css" media="all" href="/Twitzer/style.css" /> 
</head>
<body>
	<div class id="page">
		<div class ="header">
			<h1>Twitzer</h1>			
				<div class="nav clear">
					<ul>
						<li><a href="/Twitzer/About">About</a>
					</ul>
				</div>
		</div>
		<div>
			<h2>Come join in</h2>
				<div>
				<%@include file="LogCheck.jsp" %>
					<P>Fill in the form to register <%= User.getemail()%></P>
						<form action="/Twitzer/Author" method="POST">
							<fieldset class="login"> 
							<legend>Login Details</legend> 
								<div> 
									<label for="Name">Name</label> <input type="text" id="Name" name="Name" value="<%= User.getname() %>"> 
								</div> 
								<div> 
									<label for="Username">Username</label> <input type="text" id="Username" name="Username"> 
								</div> 
								<div> 
									<label for="Avatar">Avatar</label> <input type="text" id="Avatar" name="Avatar"> 
								</div> 
								<div> 
									<label for="Tel">Tel</label> <input type="text" id="Tel" name="Tel"> 
								</div>
								<div> 
									<label for="Website">Website</label> <input type="text" id="Website" name="Website"> 
								</div>
							<div> 
								<label for="Bio">Bio</label> <input type="text" id="Bio" name="Bio"> 
							</div>		
							</fieldset> 
						<br>
						<input type="submit"  value="Submit">
					</form>
				</div>
			</div>
	</div>
</body>
</html>