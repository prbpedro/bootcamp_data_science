package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseAcessExemple {

	public static void main(String[] args) throws Exception {

		Configuration config = HBaseConfiguration.create();

		String path = HBaseAcessExemple.class.getClassLoader().getResource("hbase-site.xml").getPath();
		config.addResource(new Path(path));

		HBaseAdmin.available(config);
		System.out.println("Avaible");

		TableName table1 = TableName.valueOf("Table1");
		String family1 = "Family1";
		String family2 = "Family2";

		Connection connection = ConnectionFactory.createConnection(config);
		Admin admin = connection.getAdmin();

		TableDescriptor tbb = TableDescriptorBuilder.newBuilder(table1)
				.setColumnFamily(ColumnFamilyDescriptorBuilder.of(family1))
				.setColumnFamily(ColumnFamilyDescriptorBuilder.of(family2)).build();

		if (!admin.tableExists(table1)) {
			admin.createTable(tbb);
		}

		Table table = connection.getTable(TableName.valueOf("Table1"));

		Put put1 = new Put(Bytes.toBytes("row1"));
		Put put2 = new Put(Bytes.toBytes("row2"));
		Put put3 = new Put(Bytes.toBytes("row3"));

		put1.addColumn(Bytes.toBytes(family1), Bytes.toBytes("qual1"), Bytes.toBytes("ValueOneForPut1Qual1"));

		put2.addColumn(Bytes.toBytes(family1), Bytes.toBytes("qual1"), Bytes.toBytes("ValueOneForPut2Qual1"));

		put3.addColumn(Bytes.toBytes(family1), Bytes.toBytes("qual1"), Bytes.toBytes("ValueOneForPut3Qual1"));

		put1.addColumn(Bytes.toBytes(family2), Bytes.toBytes("qual2"), Bytes.toBytes("ValueOneForPut1Qual2"));

		put2.addColumn(Bytes.toBytes(family2), Bytes.toBytes("qual2"), Bytes.toBytes("ValueOneForPut2Qual2"));

		put3.addColumn(Bytes.toBytes(family2), Bytes.toBytes("qual2"), Bytes.toBytes("ValueOneForPut3Qual3"));

		table.put(put1);
		table.put(put2);
		table.put(put3);

		Get get = new Get(Bytes.toBytes("row1"));
		Result res = table.get(get);

		byte[] value = res.getValue(Bytes.toBytes(family1), Bytes.toBytes("qual1"));
		byte[] value1 = res.getValue(Bytes.toBytes(family2), Bytes.toBytes("qual2"));
		String valueStr = Bytes.toString(value);

		String valueStr1 = Bytes.toString(value1);
		System.out.println("GET row1: " + family1 + ": " + valueStr + ", " + family2 + ": " + valueStr1);

		Scan scan = new Scan();

		scan.addColumn(Bytes.toBytes(family1), Bytes.toBytes("qual1"));
		scan.addColumn(Bytes.toBytes(family2), Bytes.toBytes("qual2"));

		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(family1), Bytes.toBytes("qual1"),
				CompareOperator.EQUAL, Bytes.toBytes("ValueOneForPut2Qual1"));
		
		scan.setFilter(filter);

		ResultScanner scanner = table.getScanner(scan);
		for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
			System.out.println("Found row : " + rr);
		}
		scanner.close();

		table.close();
		connection.close();
	}
}
