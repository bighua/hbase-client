package com.jcm.hbase.client;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 目前使用的版本是：hbase-0.96.2,hadoop2
 * @author zhuxh
 *
 */
public class RestClient {
	
	private Client client = null;
	
	public RestClient() {
		Cluster cluster = new Cluster();
		cluster.add("master", 9000); 
		client = new Client(cluster); 
	}
	
	public void write() throws IOException {

		RemoteHTable table = new RemoteHTable(client, "testtable"); 
        Put p = new Put(Bytes.toBytes("myRow"));
        p.add(Bytes.toBytes("col1"), Bytes.toBytes("someQualifier"),
                Bytes.toBytes("Some Value"));
        table.put(p);
	}
	
	public void read() throws IOException {
		RemoteHTable table = new RemoteHTable(client, "testtable"); 
        Get g = new Get(Bytes.toBytes("myRow"));
        Result r = table.get(g);
        byte[] value = r.getValue(Bytes.toBytes("col1"),
                Bytes.toBytes("someQualifier"));
        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
	}
	
	public void scan() throws IOException {

		RemoteHTable table = new RemoteHTable(client, "testtable"); 
        Scan s = new Scan();
        s.addColumn(Bytes.toBytes("col1"), Bytes.toBytes("someQualifier"));
        ResultScanner scanner = table.getScanner(s);
        try {
            for (Result rr : scanner) {
                System.out.println("Found row: " + rr);
            }
        } finally {
            scanner.close();
        }
	}
	
	public static void main(String[] args) throws IOException {
		RestClient client = new RestClient();
		client.write();
		client.read();
		client.scan();
	}
}
