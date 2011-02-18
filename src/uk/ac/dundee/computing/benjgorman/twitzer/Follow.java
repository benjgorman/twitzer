package uk.ac.dundee.computing.benjgorman.twitzer;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.benjgorman.twitzer.connectors.FollowConnector;
import uk.ac.dundee.computing.benjgorman.twitzer.connectors.UserConnector;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.AuthorStore;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.FollowStore;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore;

/**
 * Servlet implementation class Follow
 */
@WebServlet("/Follow")
public class Follow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Follow() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		RequestDispatcher rd;
		HttpSession session=request.getSession();
		
		UserStore us =(UserStore)session.getAttribute("User");
		String test = (String)session.getAttribute("Username");
		
		
		String userName = us.getUsername();
		String toFollow = test;
		
		System.out.println(userName+ "is to FOLLOW:" + test);
		System.out.println(test+ "is being FOLLOWED by:" +userName);
		
		FollowConnector fc = new FollowConnector();

		
		if (fc.Follow(userName, toFollow)==true)
		{
			System.out.println("Successfulness");
			rd=request.getRequestDispatcher("index.jsp");
		}
		else
		{
			rd=request.getRequestDispatcher("index.jsp");
			rd.forward(request,response);
		}

	}

}
