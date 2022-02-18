package org.nachc.tools.fhirtoomop.util.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import org.nachc.tools.fhirtoomop.util.params.MySqlAuthParams;
import org.yaorma.database.Database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OmopDatabaseConnectionFactory {

	private static ArrayList<Connection> conns = new ArrayList<Connection>();
	
	public static Connection getOmopConnection() {
		return connect(MySqlAuthParams.syntheaDb());
	}

	private static Connection connect(String schema) {
		try {
			String url = MySqlAuthParams.getUrl();
			String uid = MySqlAuthParams.getUid();
			String pwd = MySqlAuthParams.getPwd();
			url = url + ";databaseName=" + schema;
			url = url +";encrypt=false";
			log.info("URL: " + url);
			Connection conn = DriverManager.getConnection(url, uid, pwd);
			conn.setAutoCommit(true);
			conns.add(conn);
			log.info("OPEN CONNECTIONS: " + conns.size());
			return conn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	private static Connection connectToMySql(String schema) {
		try {
			String url = MySqlAuthParams.getUrl();
			String uid = MySqlAuthParams.getUid();
			String pwd = MySqlAuthParams.getPwd();
			url = url + "/" + schema;
			url = url + "?rewriteBatchedStatements=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			log.info("URL: " + url);
			Connection conn = DriverManager.getConnection(url, uid, pwd);
			conn.setAutoCommit(true);
			conns.add(conn);
			log.info("OPEN CONNECTIONS: " + conns.size());
			return conn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static void close(Connection conn) {
		Database.close(conn);
		conns.remove(conn);
		log.info("OPEN CONNECTIONS: " + conns.size());
	}
	
	public static int getConnectionCount() {
		return conns.size();
	}
	
}
