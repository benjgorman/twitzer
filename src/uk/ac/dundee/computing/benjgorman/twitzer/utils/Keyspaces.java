package uk.ac.dundee.computing.benjgorman.twitzer.utils;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.*;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.service.*;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.*;

public final class Keyspaces {

	
	
	public Keyspaces(){
		
	}
	public static void AddColumnFamilies(){
		try{
			ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition("BloggyAppy", "DynCf");
		}catch(Exception et){
			
			System.out.println("I can't actually create that ColumnFamily right now, heres why: "+et);
		}
	}
	public static void SetUpKeySpaces(Cluster c)
	{
		try
		{
			try
			{
				KeyspaceDefinition kd =c.describeKeyspace("Twitzer");
				System.out.println("Keyspace: "+kd.getName());
				System.out.println("Replication: "+kd.getReplicationFactor());
				System.out.println("Strategy: "+kd.getStrategyClass());
			}
			catch(Exception et)
			{
				System.out.println("You have probably been a bit of a spoon and not created your keyspace. Don't worry I'll do it for you."+et);
				List<ColumnFamilyDefinition> cfs = new ArrayList<ColumnFamilyDefinition>(); 
				
				BasicColumnFamilyDefinition cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("Twit");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				ColumnFamilyDefinition cfDef = new ThriftCfDef(cf);
				cfs.add(cfDef);
				
				cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("Zer");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				cfDef = new ThriftCfDef(cf); 
				cfs.add(cfDef);
				
				cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("TwitIndex");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				cfDef = new ThriftCfDef(cf); 
				cfs.add(cfDef);
				
				cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("Following");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				cfDef = new ThriftCfDef(cf); 
				cfs.add(cfDef);
				
				cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("FollowedBy");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				cfDef = new ThriftCfDef(cf); 
				cfs.add(cfDef);
				
				cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("AllTweets");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				cfDef = new ThriftCfDef(cf); 
				cfs.add(cfDef);
				
				cf = new BasicColumnFamilyDefinition(); 
				
				cf.setName("UserTweets");
				cf.setKeyspaceName("Twitzer");
				cf.setComparatorType(ComparatorType.BYTESTYPE);
				cfDef = new ThriftCfDef(cf); 
				cfs.add(cfDef);
				
				KeyspaceDefinition ks=HFactory.createKeyspaceDefinition("Twitzer","org.apache.cassandra.locator.SimpleStrategy", 1, cfs);
				c.addKeyspace(ks);
				System.out.println("I reckon that Keyspace was created.");
				try
				{
					KeyspaceDefinition kd =c.describeKeyspace("Twitzer");
					System.out.println("Keyspace: "+kd.getName());
					System.out.println("Replication: "+kd.getReplicationFactor());
					System.out.println("Strategy: "+kd.getStrategyClass());
				}
				catch(Exception et2)
				{
					System.out.println("Scrap my last, keyspace wasn't created!" +et2);
				}
				
			}
			
			
		}
		catch(Exception et)
		{
			System.out.println("Really have no idea what happened, keyspace or column error." +et);
		}
		
	}
}
