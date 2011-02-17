package uk.ac.dundee.computing.benjgorman.twitzer.stores;

public class UserStore {
	private boolean LoggedIn=false;
	private long numPosts;
	private String Name="";
	private String email="";
	private String username = "";
	private String avatar = "";
	

	public UserStore()
	{


	}

	public void setloggedIn(String Name)
	{
		this.Name=Name;
		LoggedIn=true;
		
	}

	public void setloggedIn(String Name,String Email,String Avatar,String username)
	{
		this.Name=Name;
		this.email=Email;
		this.avatar=Avatar;
		this.username=username;
		LoggedIn=true;
	}
	
	public boolean isloggedIn()
	{
		System.out.println("Logged "+LoggedIn);
		return LoggedIn;
	}

	public String getname()
	{
		return Name;
	}

	public String getemail()
	{
		return email;
	}
	
	public void logout()
	{
		LoggedIn=false;
		Name="";
	}

	public void setNumPosts(long numPosts) {
		this.numPosts = numPosts;
	}

	public long getNumPosts() {
		return numPosts;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return avatar;
	}
}