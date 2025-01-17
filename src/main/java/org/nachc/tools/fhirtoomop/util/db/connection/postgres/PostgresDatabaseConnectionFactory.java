package org.nachc.tools.fhirtoomop.util.db.connection.postgres;

import java.sql.Connection;
import java.sql.DriverManager;

import org.nachc.tools.fhirtoomop.util.params.AppParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgresDatabaseConnectionFactory {

	public static Connection getBootstrapConnection() {
		try {
			String url = AppParams.getPostgresBootstrapUrl();
			String uid = AppParams.getPostgresBootstrapUid();
			String pwd = AppParams.getPostgresBootstrapPwd();
			url += "/postgres";
			url += "?" + "user=" + uid;
			url += "&" + "password=" + pwd;
			log.info("Getting connection for url: \n" + url);
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (Exception exp) {
			throw (new RuntimeException(exp));
		}

	}

	public static Connection getCdmConnection() {
		return getSchemaConnection();
	}

	public static Connection getOhdsiConnection() {
//		try {
//			String url = AppParams.getPostgresBootstrapUrl();
//			String uid = AppParams.getPostgresBootstrapUid();
//			String pwd = AppParams.getPostgresBootstrapPwd();
//			url += "/OHDSI";
//			url += "?" + "user=" + uid;
//			url += "&" + "password=" + pwd;
//			log.info("Getting connection for url: \n" + url);
//			Connection conn = DriverManager.getConnection(url);
//			return conn;
//		} catch (Exception exp) {
//			throw (new RuntimeException(exp));
//		}
		return getSchemaConnection();
	}

	private static Connection getSchemaConnection() {
		try {
			String url = AppParams.getPostgresBootstrapUrl();
			String uid = AppParams.getPostgresBootstrapUid();
			String pwd = AppParams.getPostgresBootstrapPwd();
			String schema = AppParams.get("atlasCdm");
			url += "/OHDSI";
			url += "?" + "user=" + uid;
			url += "&" + "password=" + pwd;
			url += "&" + "currentSchema=" + schema;
			log.info("Getting connection for url: \n" + url);
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (Exception exp) {
			throw (new RuntimeException(exp));
		}
	}
	
}
