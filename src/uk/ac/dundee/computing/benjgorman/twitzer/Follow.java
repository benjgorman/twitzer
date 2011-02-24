package uk.ac.dundee.computing.benjgorman.twitzer;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ac.dundee.computing.benjgorman.twitzer.connectors.FollowConnector;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.FolloweeStore;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore;

/**
 * Servlet implementation class Follow
 */
@WebServlet("/Follow")
public class Follow extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> FormatsMap = new HashMap<String, Integer>();
   /**
    * Default constructor. 
    */
   public Follow() 
   {
   	 FormatsMap.put("Jsp", 0);
   	 FormatsMap.put("xml", 1);
   	 FormatsMap.put("rss", 2);
   	 FormatsMap.put("json",3);
   }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    // /json return followers formatted as Json for logged in user 
    // /[username]/json - return followers formatted as Json for specified user 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String args[]= SplitRequestPath(request);

		switch (args.length)
		{
			case 3:
				if (FormatsMap.containsKey(args[2])) 
				{
					Integer IFormat= (Integer)FormatsMap.get(args[2]);
					HttpSession session=request.getSession();
					UserStore sessionUser = (UserStore)session.getAttribute("User");
					
					if (sessionUser != null && sessionUser.isloggedIn() == true)
					{
						switch((int)IFormat.intValue())
						{
							case 3:GetFollowers(request, response,3,sessionUser.getUsername()); //Only JSON implemented for now
							break;
						}
	
					}
				}
				break;
	
			case 4: 
				if (FormatsMap.containsKey(args[3]))
				{ //all users
							Integer IFormat= (Integer)FormatsMap.get(args[3]);
							
							switch((int)IFormat.intValue())
							{
								case 3:GetFollowers(request, response,3,args[2]); //Only JSON
							 		break;
								default:
									break;
							}
						}
				break;
			default: 
				System.out.println("Wrong number of arguements in doGet Author "+request.getRequestURI()+" : "+args.length);
				break;
		}

	}
	
	public void GetFollowers(HttpServletRequest request, HttpServletResponse response,int Format, String username) throws ServletException, IOException
	{
		HttpSession session=request.getSession();
		session.setAttribute("followers", null);
		
		FollowConnector fc = new FollowConnector();
		
		List<FolloweeStore> followers = fc.getFollowers(username);
		List<FolloweeStore> followerList = new LinkedList<FolloweeStore>();
		
		if (followers != null && followers.size() > 0)
		{
			for (FolloweeStore follow : followers)
			{
				try 
				{
					follow.setAvatarUrl(fc.getAuthorFromEmail(fc.getEmailFromUsername(follow.getUsername())).getAvatar());

				}
				catch(Exception e)
				{
					System.out.println("Problem getting that users avatar, camera shy?" + e);
				}
				followerList.add(follow);
				System.out.println("Followed by " + follow.getUsername());
			}
		}
		
		switch(Format)
		{
			case 3: request.setAttribute("Data", followerList);
					RequestDispatcher rdjson=request.getRequestDispatcher("/RenderJson");
					rdjson.forward(request,response);
					break;
			default: System.out.println("Invalid Format");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher rd;
		HttpSession session=request.getSession();
		
		UserStore us =(UserStore)session.getAttribute("User");
		
		String toFollow = null;
		String args[]=SplitRequestPath(request);
		String op=null;
		
		switch (args.length)
		{
			case 3: op=args[2];
					break;
		}
	        if (op==null) 
	        {
	           
	        	try
	        	{
	        		System.out.println("Can't follow this user.");
	        	}
	        	catch (Exception et)
	        	{
	        		System.out.println("Heres why: "+et);
	        		return;
	        	}
	                     
	            
	            return;
	        } 
	        else
	        {
	            toFollow = op.toString();
	        }
		
		
		String userName = us.getUsername();
		
		System.out.println(userName+ "is to FOLLOW:" + toFollow);
		System.out.println(toFollow+ "is being FOLLOWED by:" +userName);
		
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
    
    private String[] SplitRequestPath(HttpServletRequest request)
    {
		String args[] = null;

		StringTokenizer st = SplitString(request.getRequestURI());
		args = new String[st.countTokens()];
		//Lets assume the number is the last argument

		int argv=0;
		while (st.hasMoreTokens ()) {;
			args[argv]=new String();

			args[argv]=st.nextToken();
			try{
				System.out.println("String was "+URLDecoder.decode(args[argv],"UTF-8"));
				args[argv]=URLDecoder.decode(args[argv],"UTF-8");

			}catch(Exception et){
				System.out.println("Bad URL Encoding"+args[argv]);
			}
			argv++;
			} 

	//so now they'll be in the args array.  
	// argv[0] should be the user directory

		return args;
		}
    
 // Logged in user unfollow user specified in URL
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("Test Success");
		HttpSession session=request.getSession();
		
		UserStore us =(UserStore)session.getAttribute("User");
		
		String toFollow = null;
		String args[]=SplitRequestPath(request);
		String op=null;
		
		switch (args.length)
		{
			case 3: op=args[2];
					break;
		}
	        if (op==null) 
	        {
	           
	        	try
	        	{
	        		System.out.println("Can't unfollow this user.");
	        	}
	        	catch (Exception et)
	        	{
	        		System.out.println("Heres why: "+et);
	        		return;
	        	}
	                     
	            
	            return;
	        } 
	        else
	        {
	            toFollow = op.toString();
	        }
		
		
		String userName = us.getUsername();
		
		FollowConnector fc = new FollowConnector();
		try
		{
			System.out.println(userName + " wishes to unfollow" + toFollow);
			
			if (fc.removeFollower(userName, toFollow)==true && fc.removeFollowee(userName, toFollow)==true)
			{
				System.out.println("Successfulness");
			}
		}
		catch(Exception e)
		{
			System.out.println("Unfollow didn't work: " + e);
		}

	}
	  private StringTokenizer SplitString(String str)
	  {
	  		return new StringTokenizer (str,"/");

	  }


}
