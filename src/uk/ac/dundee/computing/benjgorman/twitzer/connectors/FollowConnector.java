package uk.ac.dundee.computing.benjgorman.twitzer.connectors;

import java.util.ArrayList;
import java.util.List;

import uk.ac.dundee.computing.benjgorman.twitzer.stores.AuthorStore;
import uk.ac.dundee.computing.benjgorman.twitzer.stores.FolloweeStore;
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


public class FollowConnector
{
	public FollowConnector()
	{
		
	}
	
	public List<FolloweeStore> getFollowing(String username)
	{
		Cluster c; //V2
		try
		{
			c=CassandraHosts.getCluster();
		}
		catch (Exception et)
		{
			System.out.println("Sorry problem!"+et);
			return null;
		}
		try 
		{
			List<FolloweeStore> results = new ArrayList<FolloweeStore>();

			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("Twitzer", c);  //V2
			
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			
			SliceQuery<String, String, String> q = HFactory.createSliceQuery(ks, se, se, se);
			
			q.setColumnFamily("Following")
			.setKey(username)
			.setRange("", "", false, 100);
			
			QueryResult<ColumnSlice<String, String>> r = q.execute();
			ColumnSlice<String, String> slice = r.get();
			List<HColumn<String, String>> slices = slice.getColumns();
			for (HColumn<String, String> column: slices)
			{
				FolloweeStore result = new FolloweeStore();
				result.setUsername(column.getName());
				
				if (column.getValue() != null && !column.getValue().equals(""))
				{
					result.setDate(Long.parseLong(column.getValue()));
				}
				results.add(result);
			}
			return results;
		}
		catch (Exception e) 
		{
			System.out.println("That should have worked"+ e);
			return null;
		}
	}
	
	public List<FolloweeStore> getFollowees(String username)
	{
		Cluster c; //V2
		try
		{
			c=CassandraHosts.getCluster();
		}
		catch (Exception et)
		{
			System.out.println(""+et);
			return null;
		}
		try 
		{
			List<FolloweeStore> results = new ArrayList<FolloweeStore>();

			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("Twitzer", c);  //V2
			
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			SliceQuery<String, String, String> q = HFactory.createSliceQuery(ks, se, se, se);
			
			q.setColumnFamily("FollowedBy")
			.setKey(username)
			.setRange("", "", false, 100);
			
			QueryResult<ColumnSlice<String, String>> r = q.execute();
			ColumnSlice<String, String> slice = r.get();
			
			List<HColumn<String, String>> slices = slice.getColumns();
			
			for (HColumn<String, String> column: slices)
			{
				FolloweeStore result = new FolloweeStore();
				
				result.setUsername(column.getName());
				
				if (column.getValue() != null && !column.getValue().equals(""))
				{
					result.setDate(Long.parseLong(column.getValue()));
				}
				results.add(result);
			}
			return results;
			
		}
		catch (Exception e) {
			System.out.println("Problems!"+ e);
			return null;
		}
	}
	
	public Boolean Follow(String userName, String toFollow)
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
			
			Long timeNow = System.currentTimeMillis();
			
			mutator.insert(userName, "Following", HFactory.createStringColumn(toFollow, timeNow.toString()));
			
			mutator = HFactory.createMutator(ks, ss);
					
			mutator.insert(toFollow, "FollowedBy", HFactory.createStringColumn(userName, timeNow.toString()));
		
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
			AuthorStore Au=new AuthorStore();
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

	public boolean removeFollower(String toFollow, String toBeFollowed)
	{
		Cluster c; //V2
		try
		{
			c=CassandraHosts.getCluster();
		}
		catch (Exception et)
		{
			System.out.println("" + et);
			return false;
		}
		try
		{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("Twitzer", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			Mutator<String> mutator = HFactory.createMutator(ks,se);
			mutator.delete(toBeFollowed, "FollowedBy", toFollow, se);
			mutator.execute();
			return true;
		}
		catch (Exception et)
		{
			System.out.println(""+et);
			return false;
		}
	}

	public boolean removeFollowee(String toFollow, String toBeFollowed)
	{
		Cluster c; //V2
		try
		{
			c=CassandraHosts.getCluster();
		}
		catch (Exception et){
			System.out.println("Whoops! "+et);
			return false;
		}
		try{
			ConsistencyLevelPolicy mcl = new MyConsistancyLevel();
			Keyspace ks = HFactory.createKeyspace("Twitzer", c);  //V2
			ks.setConsistencyLevelPolicy(mcl);
			StringSerializer se = StringSerializer.get();
			Mutator<String> mutator = HFactory.createMutator(ks,se);
			mutator.delete(toFollow, "Following", toBeFollowed, se);
			mutator.execute();
			return true;
		}catch (Exception et){
			System.out.println(""+et);
			return false;
		}
	}
	
}



