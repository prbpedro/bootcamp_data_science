package hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClientExample {
	
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default;auth=noSasl");
		Statement stmt = con.createStatement();

		ResultSet res = stmt.executeQuery("show databases");
		if (res.next()) {
			System.out.println(res.getString(1));
		}
		res.close();
		stmt.close();
		
		stmt = con.createStatement();
		if(stmt.execute("use dbdesafio")) {
			throw new SQLException("");
		}
		stmt.close();
		
		stmt = con.createStatement();
		res = stmt.executeQuery("show tables");
		if (res.next()) {
			System.out.println(res.getString(1));
		}
		else {
			System.out.println("No tables");
		}
		res.close();
		stmt.close();
		
		
		stmt = con.createStatement();
		res = stmt.executeQuery("select descpais, count(*) from dadoscovid where descpais in ('Brazil', 'Uruguay') group by descpais");
		while (res.next()) {
			System.out.println(res.getString(1) + " - " + res.getString(2));
		}
		res.close();
		stmt.close();
		
		con.close();
	}
}