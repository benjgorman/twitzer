package uk.ac.dundee.computing.benjgorman.twitzer.stores;

public class AuthorStore
{
	private long numPosts;
	private String name ="";
	private String userName ="";
	private String email ="";
	private String bio ="";
	private String avatar ="";
	private String webUrl = "";
	private String tel ="";
	private Boolean following = false;
	
	public AuthorStore()
	{
	
	}
	
	public void setFollowing(Boolean following)
	{
		this.following=following;
	}
	
	public Boolean getFollowing()
	{
		return following;
	}
	
	public long getnumPosts()
	{
		return numPosts;
	}
	
	public String getname()
	{
		return name;
	}
	
	public String getuserName()
	{
		return userName;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getBio()
	{
		return bio;
	}
	public String getAddress()
	{
		return webUrl;
	}
	
	public String getTel()
	{
		return tel;
	}
	
	public String getAvatar()
	{
		return avatar;
	}

	//A convenient method to set all variables at once.
	public void setAll(long numPosts, String name, String userName,String emailName, String bio, String address, String tel, String avatar)
	{
		this.name=name;
		this.numPosts=numPosts;
		this.userName=userName;
		this.email=emailName;
		this.bio=bio;
		this.webUrl=address;
		this.avatar= avatar;
		this.tel=tel;
	}
	
	public void setnumPosts(long posts)
	{
		this.numPosts=posts;
	}
	
	public void setname(String name)
	{
		this.name=name;
	}
	
	public void setUserName(String userName)
	{
		this.userName=userName;
	}
	
	public void setemailName(String email)
	{
		this.email=email;
	}
	
	public void setBio(String bio)
	{
		this.bio=bio;
	}
	public void setAddress(String address)
	{
		this.webUrl=address;
	}
	public void setTel(String tel)
	{
		this.tel=tel;
	}

	public void setAvatar(String avatar)
	{
		this.avatar=avatar;
	}
	

}
