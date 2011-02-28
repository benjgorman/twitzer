package uk.ac.dundee.computing.benjgorman.twitzer;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;


import uk.ac.dundee.computing.benjgorman.twitzer.connectors.*;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.AuthorStore;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.FolloweeStore;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.UserStore;

/**
 * Servlet implementation class Author
 */
public class Author extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	 private HashMap<String, Integer> FormatsMap = new HashMap<String, Integer>();
    /**
     * Default constructor. 
     */
	 ////hello
    public Author() 
    {
        // TODO Auto-generated constructor stub
    	 FormatsMap.put("Jsp", 0); 
    	 FormatsMap.put("xml", 1);
    	 FormatsMap.put("rss", 2);
    	 FormatsMap.put("json",3); //only once which works
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.setAttribute("ViewUser", null);

		String args[]=SplitRequestPath(request);

		switch (args.length){

			case 2: System.out.println("All Users");
					break;
			case 3: 
			if (FormatsMap.containsKey(args[2]))
			{
					Integer IFormat= (Integer)FormatsMap.get(args[2]);

						switch((int)IFormat.intValue())
						{
							 case 3:System.out.println("Testing");
							 		break;
							 default:break;
						}
					}
			else
			{ 
						System.out.println("Call return Author");
						ReturnAuthor(request, response,0,args[2]);
						break;
			}
			break;

			case 4: if (FormatsMap.containsKey(args[3]))
			{
						Integer IFormat= (Integer)FormatsMap.get(args[3]);
						switch((int)IFormat.intValue())
						{
							case 3:ReturnAuthor(request, response,3,args[2]); //Only JSON implemented for now
						 		break;
							default:break;
						}
					}
					break;
			default: System.out.println("Wrong number of arguements, error: "+request.getRequestURI()+" : "+args.length);
					break;
		}


	}

	/*
	 * Returns an Author based on a username supplied.
	 * 
	 */
	public void ReturnAuthor(HttpServletRequest request, HttpServletResponse response,int Format, String AuthorName) throws ServletException, IOException
	{
		UserConnector au = new UserConnector();
		FollowConnector fc = new FollowConnector(); //needed to we can check if the user who is viewing the author page is following them
		
        String email = au.getEmailFromUsername(AuthorName);
        
		AuthorStore Author = au.getAuthorFromEmail(email);
		
		HttpSession session=request.getSession();
		UserStore lc =(UserStore)session.getAttribute("User");
		
		if (Author==null)
		{
			Author=new AuthorStore();
			Format = 4;
		}
		
		//cycle through the logged in users following list, to see if we can get the user they are looking at
		if (lc!=null && lc.getUsername()!= null)
		{
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
					RequestDispatcher rd=request.getRequestDispatcher("/RenderAuthor.jsp");
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
	 * Sets up a users account. Setting their data taken from the RegisterUser page.
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		AuthorStore Author =new AuthorStore();
		RequestDispatcher rd;
		HttpSession session=request.getSession();

		UserStore lc =(UserStore)session.getAttribute("User");

		if (lc==null)
		{
			//if not logged in then send them to the register page
			rd=request.getRequestDispatcher("RegisterUser.jsp");
			rd.forward(request,response);
		}

		//escape HTML to make sure we have no nasty stuff in our text fields.
		//we are setting up a users account here in an author store
		Author.setemailName(lc.getemail());
		Author.setname(org.apache.commons.lang.StringEscapeUtils.escapeHtml(request.getParameter("Name")));
		Author.setAddress(org.apache.commons.lang.StringEscapeUtils.escapeHtml(request.getParameter("Website")));
		
		if (request.getParameter("Avatar")=="null" || request.getParameter("Avatar")=="")
		{
			Author.setAvatar("/Twitzer/images/Blank_Avatar_1.jpg");
		}
		else
		{
			Author.setAvatar(org.apache.commons.lang.StringEscapeUtils.escapeHtml(request.getParameter("Avatar")));
		}
		
		Author.setUserName(org.apache.commons.lang.StringEscapeUtils.escapeHtml(request.getParameter("Username")));
		Author.setBio(org.apache.commons.lang.StringEscapeUtils.escapeHtml(request.getParameter("Bio")));
		Author.setTel(org.apache.commons.lang.StringEscapeUtils.escapeHtml(request.getParameter("Tel")));

		UserConnector au = new UserConnector();

		//if we have added them, lets sign them in
		if (au.addAuthor(Author)== true)
		{
			lc.setloggedIn(Author.getname(), Author.getEmail(), Author.getAvatar(), Author.getuserName());
			response.sendRedirect("/Twitzer/");
		}
		else // if its not worked then send them back to register, something must have borked
		{
			rd=request.getRequestDispatcher("/Twitzer/RegisterUser.jsp");
			rd.forward(request,response);
		}


	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
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
			try
			{
				System.out.println("String was "+URLDecoder.decode(args[argv],"UTF-8"));
				args[argv]=URLDecoder.decode(args[argv],"UTF-8");

			}
			catch(Exception et){
				System.out.println("Bad URL Encoding"+args[argv]);
			}
			argv++;
			} 

	//so now they'll be in the args array.  
	// argv[0] should be the user directory

		return args;
		}

	  private StringTokenizer SplitString(String str){
	  		return new StringTokenizer (str,"/");

	  }

}
