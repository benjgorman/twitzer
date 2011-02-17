<jsp:useBean id="User"
class="uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore"
scope="session"
></jsp:useBean>

<jsp:useBean id="ReturnPoint"
class="uk.ac.dundee.computing.benjgorman.twitzer.stores.ReturnStore"
scope="session"
></jsp:useBean>
<%
System.out.println("Called From "+request.getRequestURI());
System.out.println("logcheck "+User.isloggedIn());
ReturnPoint.setReturnTo(request.getRequestURI());
%>

<h1><% User.getname();%></h1>

<%
if (User.isloggedIn()==false){
System.out.println("Log check redirect to login");
response.sendRedirect("/Twitzer/Login.jsp");
}
%>