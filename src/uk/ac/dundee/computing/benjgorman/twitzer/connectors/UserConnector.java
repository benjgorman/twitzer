package uk.ac.dundee.computing.benjgorman.twitzer.connectors;

import uk.ac.dundee.computing.benjgorman.twitzer.stores.*;
import uk.ac.dundee.computing.benjgorman.twitzer.utils.*;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
 
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.mutation.*;

public class UserConnector
{
	public UserConnector()
	{
		
	}
	
	public Boolean addAuthor(AuthorStore authorStore)
	{
		Cluster c;
		try
		{
			c=CassandraHosts.getCluster();
		}
		catch (Exception et)
		{
			System.out.println("Uh oh something went wrong."+et);
			return false;
		}
		
		try
		{

			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("Twitzer", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			
			StringSerializer ss = StringSerializer.get();
			
			Mutator<String> mutator = HFactory.createMutator(ks, ss);
			mutator.insert(authorStore.getuserName(), "TwitIndex", HFactory.createStringColumn("email", authorStore.getEmail()));
			
			mutator = HFactory.createMutator(ks, ss);
			
			
			mutator.addInsertion(authorStore.getEmail(), "Twit",
																HFactory.createStringColumn("name", authorStore.getname()))
					.addInsertion(authorStore.getEmail(), "Twit", HFactory.createStringColumn("username", authorStore.getuserName()))
					.addInsertion(authorStore.getEmail(), "Twit", HFactory.createStringColumn("avatar", authorStore.getAvatar()))
					.addInsertion(authorStore.getEmail(), "Twit", HFactory.createStringColumn("address", authorStore.getAddress()))
					.addInsertion(authorStore.getEmail(), "Twit", HFactory.createStringColumn("bio", authorStore.getBio()))
					.addInsertion(authorStore.getEmail(), "Twit", HFactory.createStringColumn("tel", authorStore.getTel()));
			
			mutator.execute();
			
			System.out.println(authorStore.getuserName());
			
			return true;
			
		
		}
		catch(Exception e)
		{
			
			System.out.println(e);
			return false;
		}
		
	}
	
	public String getEmailFromUsername(String username)
	{
		String email = null;
		Cluster c; //V2
		try
		{
			c=CassandraHosts.getCluster();
		}
		catch (Exception et)
		{
			System.out.println("get Articles Posts Can't Connect "+et);
			return null;
		}
		
		ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
		try
		{
			Keyspace ks = HFactory.createKeyspace("Twitzer", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			
			try
			{
				ColumnQuery<String, String, String> columnQuery =
					HFactory.createStringColumnQuery(ks);
		
				columnQuery.setColumnFamily("TwitIndex").setKey(username).setName("email");
				QueryResult<HColumn<String, String>> result = columnQuery.execute();
				
				email = result.get().getValue();
			}
			catch(Exception et)
			{
				System.out.println("Cant make this Query");

				System.out.println(""+et);
				System.out.flush();
				return null;
			}
		}
		catch (Exception et)
		{
			System.out.println("Can't get TwitIndex "+et);
			return null;
		}
		
		return email;
	}
	
	public AuthorStore getAuthorFromEmail(String Email)
	{
		if (Email == null)
		{
			return null;
		}
		else
		{
			AuthorStore Au = new AuthorStore();
			Cluster c; //V2
			
			try
			{
	
				c=CassandraHosts.getCluster();
	
			}
			catch (Exception et)
			{
				System.out.println("get Articles Posts Can't Connect "+et);
				return null;
			}
			
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			StringSerializer se = StringSerializer.get();
	
			try
			{
				Keyspace ko = HFactory.createKeyspace("Twitzer", c);  //V2
				ko.setConsistencyLevelPolicy(mcl);
				//retrieve  data
				
				ColumnSlice<String, String> slice=null;
				
				try
				{
	
					//retrieve  data
					SliceQuery<String,String, String> s= HFactory.createSliceQuery(ko,se, se, se);
	
					s.setColumnFamily("Twit");
	
					s.setKey(Email); //Set the Key
					s.setRange("", "", false, 100); //Set the range of columns (we want them all) 
					QueryResult<ColumnSlice<String, String>> r2 = s.execute();
					slice = r2.get();
					
					
				}
				catch(Exception et)
				{
					System.out.println("Cant make Query on Registered openid emails");
	
					System.out.println(""+et);
					System.out.flush();
					return null;
				}
				try
				{
				     for (HColumn<String, String> column : slice.getColumns()) 
				     {
	
			    	  String Name=column.getName();
	         		 String Value=column.getValue();
	
	         		Au.setemailName(Email);
	         		if (Name.compareTo("name")==0)
	         			 Au.setname(Value);
	         		else if (Name.compareTo("username")==0)
	        			 Au.setUserName(Value);
	         		else if (Name.compareTo("avatar")==0)
	         			 Au.setAvatar(Value);
	         		else if (Name.compareTo("website")==0)
	         			 Au.setAddress(Value);
	         		else if (Name.compareTo("tel")==0)
	         			Au.setTel(Value);
	         		else if (Name.compareTo("bio")==0)
	         			Au.setBio(Value);
	         		 
	
			      }
				}
				catch (Exception et)
				{
				System.out.println("Can't get registered author "+et);
				return null;
				}
			}
				catch (Exception et)
				{
				System.out.println("Can't get registered author "+et);
				return null;
				}
			return Au;
		}
	}
	
	
}



