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
import uk.ac.dundee.computing.benjgorman.twitzer.connectors.UserConnector;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.AuthorStore;
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
				else
				{ 
						System.out.println("Call return Author");
						ReturnAuthor(request, response,0,args[2]);
						break;
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
	
	/*
	 * Returns a list of those a user is following in jsp/json
	 * 
	 */
	public void ReturnAuthor(HttpServletRequest request, HttpServletResponse response,int Format, String AuthorName) throws ServletException, IOException
	{
		UserConnector au = new UserConnector();
		FollowConnector fc = new FollowConnector();
		
        String email = au.getEmailFromUsername(AuthorName);
        
		AuthorStore Author = au.getAuthorFromEmail(email);
		
		HttpSession session=request.getSession();
		UserStore lc =(UserStore)session.getAttribute("User");
		
		if (Author==null)
		{
			Author=new AuthorStore();
			Format = 4;
		}
		
		if (lc!=null && lc.getUsername()!= null)
		{
			if (lc.getUsername() == null)
			{
				lc.setUsername("");
			}
			
			
			List<FolloweeStore> fsl = fc.getFollowing(lc.getUsername());
			
			if (fsl!=null)
			{	
				
				for (FolloweeStore follow: fsl)
				{				
					if (follow.getUsername().equalsIgnoreCase(Author.getuserName()))
					{	
						Author.setFollowing("true");
					}
					else
					{
						Author.setFollowing("false");
					}	
				}
			}
			else
			{
				Author.setFollowing("false");
			}
			
		}
		else
		{
			Author.setFollowing("false");
		}	
		
		System.out.println("Got Author "+Author.getname()+" : "+Format);
		System.out.flush();
		
		switch(Format)
		{
			case 0: request.setAttribute("Author", Author);
					RequestDispatcher rd=request.getRequestDispatcher("/Following.jsp");
					rd.forward(request,response);
					break;
					
			case 3: request.setAttribute("Data", Author);
					RequestDispatcher rdjson=request.getRequestDispatcher("/RenderJson");
					rdjson.forward(request,response);
					
					break;
					
			case 4: request.setAttribute("Null", Author);
					RequestDispatcher rdnull=request.getRequestDispatcher("/NullAuthor.jsp");
					rdnull.forward(request,response);
					break;
					
			default: System.out.println("Invalid Format in ReturnAllAuthors ");
		}
	}
	
	/*
	 * Actually gets the followers
	 * 
	 */
	public void GetFollowers(HttpServletRequest request, HttpServletResponse response,int Format, String username) throws ServletException, IOException
	{
		HttpSession session=request.getSession();
		session.setAttribute("followers", null);
		
		FollowConnector fc = new FollowConnector();
		
		List<FolloweeStore> followers = fc.getFollowing(username);
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

	/*
	 * Follows the user specified for the logged in user
	 * 
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
		
		System.out.println(userName+ " is to FOLLOW: " + toFollow);
		System.out.println(toFollow+ " is being FOLLOWED by: " +userName);
		
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
    
    /*
	 * Unfollows the user specified for the logged in user.
	 * 
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session=request.getSession();
		UserStore us =(UserStore)session.getAttribute("User");
		
		String toFollow = null;
		String args[]=SplitRequestPath(request);
		String ex=null;
		
		switch (args.length)
		{
			case 3: ex=args[2];
					break;
		}
	        if (ex==null) 
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
	            toFollow = ex.toString();
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
