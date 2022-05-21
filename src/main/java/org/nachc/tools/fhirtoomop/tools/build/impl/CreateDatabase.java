package org.nachc.tools.fhirtoomop.tools.build.impl;

import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.db.connection.OmopDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.yaorma.database.Database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateDatabase {

	public static void main(String[] args) {
		log.info("Starting main...");
		Connection conn = OmopDatabaseConnectionFactory.getBootstrapConnection();
		try {
			exec(conn);
		} finally {
			OmopDatabaseConnectionFactory.close(conn);
		}
		log.info("Done.");
	}
	
	public static void exec(Connection conn) {
		log.info("Creating databases...");
		String databaseName = AppParams.getDbName();
		createDatabase(databaseName, conn);
		createDatabase(databaseName + "_dqd_results", conn);
		createDatabase(databaseName + "_achilles_results", conn);
		createDatabase(databaseName + "_achilles_temp", conn);
		Database.commit(conn);
		log.info("Done creating databases.");
	}
	
	private static void createDatabase(String databaseName, Connection conn) {
		log.info("Creating database: " + databaseName);
		Database.update("drop database if exists " + databaseName, conn);
		Database.update("create database " + databaseName, conn);
	}
	
}
