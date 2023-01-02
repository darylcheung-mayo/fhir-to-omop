package org.nachc.tools.fhirtoomop.tools.build.postgres;

import java.io.InputStream;
import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.db.connection.OmopDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.db.connection.postgres.PostgresDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.yaorma.database.Database;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateSequencesForPrimaryKeysPostgres {

	private static final InputStream IS = FileUtil.getInputStream("/postgres/seq/sequences.sql");

	public static void main(String[] args) {
		Connection conn = PostgresDatabaseConnectionFactory.getDbConnection();
		log.info("Got connection...");
		try {
			exec(conn);
		} finally {
			Database.close(conn);
		}
		log.info("Done.");
	}

	public static void exec(Connection conn) {
		String dbName = AppParams.getDbName();
		log.info("Running script...");
		Database.executeSqlScript(IS, conn);
		log.info("Done running script.");
		log.info("Done creating sequences for primary keys.");
	}

}
