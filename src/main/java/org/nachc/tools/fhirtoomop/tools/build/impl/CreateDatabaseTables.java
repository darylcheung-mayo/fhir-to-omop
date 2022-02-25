package org.nachc.tools.fhirtoomop.tools.build.impl;

import java.io.File;
import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.params.AppConnectionParams;
import org.yaorma.database.Database;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateDatabaseTables {

	private static final File DIR = FileUtil.getFile("/sqlserver/omop");
	
	private static final File FILE = new File(DIR, "OMOPCDM_sql_server_5.4_ddl.sql");
	
	public static void exec(Connection conn) {
		String dbName = AppConnectionParams.getDbName();
		log.info("Using: " + dbName);
		Database.update("use " + dbName, conn);
		log.info("Running script...");
		Database.executeSqlScript(FILE, conn);
		log.info("Done running script.");
		log.info("Done creating database tables.");
	}
	
}
