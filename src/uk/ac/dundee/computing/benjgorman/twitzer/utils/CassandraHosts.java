package uk.ac.dundee.computing.benjgorman.twitzer.utils;

import java.util.Iterator;
import java.util.Set;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.*;
//import me.prettyprint.cassandra.service.Cluster;
import me.prettyprint.cassandra.service.*;
/**********************************************************
 * 
 * 
 * @author administrator
 *
 *Hosts are 
 * 134.36.36.83  Seed Windows 
 * 
 * 134.36.36.84 
 * 
 * 134.36.36.85  Windows 
 * 
 * 134.36.36.205 Seed Windows, no network
 *
 *
 */

public  final class CassandraHosts {
	static Cluster c=null;
	static String Host ="127.0.0.1";
	public CassandraHosts(){
		
	}
	
	public static String getHost(){
		return (Host);
	}
	
	public static String[] getHosts(){
		  if (c==null){
			  System.out.println("Creating cluster connection");
			c = HFactory.getOrCreateCluster("Test Cluster", Host+":9160");
		  }
			System.out.println(c.describeClusterName());
		
		   //Set <String>hosts= c.getClusterHosts(true);
			Set <CassandraHost>hosts= c.getKnownPoolHosts(false);
			
		   String sHosts[] = new String[hosts.size()];
		   Iterator<CassandraHost> it =hosts.iterator();
		   int i=0;
		   while (it.hasNext()) {
			   CassandraHost ch=it.next();
			   
		       sHosts[i]=(String)ch.getHost();
		       System.out.println("Hosts"+sHosts[i]);
		       i++;
		   }
		  
		   return sHosts;
	}
	public static Cluster getCluster(){
		System.out.println("getCluster");
		
			c = HFactory.getOrCreateCluster("Test Cluster", Host+":9160");
			getHosts();
			Keyspaces.SetUpKeySpaces(c);
		
		
		
		return c;
		
	}	
	
	
}
