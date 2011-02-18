package uk.ac.dundee.computing.benjgorman.twitzer.connectors;

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
		StringSerializer ss = StringSerializer.get();
		
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
	
}



