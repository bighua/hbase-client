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
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 目前使用的版本是：hbase-0.96.2,hadoop2
 * 
 * ERROR: Could not locate executable null\bin\winutils.exe in the Hadoop binaries.
 * 在windows下执行需要hadoop用的windows本地组件，shit，该死的windows。
 * 先不用这个玩了，用rest客户端做吧！！！！
 * 
 * @author zhuxh
 *
 */
public class HBaseClient {
	
	private Configuration conf = null;
	
	public HBaseClient() {
		// 通过hbase-site.xml中配置的hdfs才能连接上服务器端的hbase服务
		conf = HBaseConfiguration.create();
	}
	
	public void write() throws IOException {

        HTable table = new HTable(conf, "testtable");
        Put p = new Put(Bytes.toBytes("myRow"));
        p.add(Bytes.toBytes("col1"), Bytes.toBytes("someQualifier"),
                Bytes.toBytes("Some Value"));
        table.put(p);
	}
	
	public void read() throws IOException {

        HTable table = new HTable(conf, "testtable");
        Get g = new Get(Bytes.toBytes("myRow"));
        Result r = table.get(g);
        byte[] value = r.getValue(Bytes.toBytes("col1"),
                Bytes.toBytes("someQualifier"));
        String valueStr = Bytes.toString(value);
        System.out.println("GET: " + valueStr);
 
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
		HBaseClient client = new HBaseClient();
		client.write();
		client.read();
		
	}
}
