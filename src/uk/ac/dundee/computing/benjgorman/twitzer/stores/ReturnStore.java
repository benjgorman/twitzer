package uk.ac.dundee.computing.benjgorman.twitzer.stores;

public class ReturnStore {
	// This class has one purpose, it allows a logon through Open ID to return to the original calling page
	// Store it in a session on a jsp page
	String ReturnTo;
	public ReturnStore(){
	}
	
	public String getreturnTo(){
		return ReturnTo;
	}
	
	//This will remove the jBloggyAppy bit
	public void setReturnTo(String ReturnTo){
		String ret = ReturnTo.replaceFirst("/Twitzer", "");
		this.ReturnTo=ret;
	}
	

}
