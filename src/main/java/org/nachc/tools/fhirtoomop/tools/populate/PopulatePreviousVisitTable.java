package org.nachc.tools.fhirtoomop.tools.populate;

import java.io.InputStream;
import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.db.connection.OmopDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.db.connection.postgres.PostgresDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.yaorma.database.Database;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PopulatePreviousVisitTable {

	public static void main(String[] args) {
		exec();
	}
	
	public static void exec() {
		log.info("* * *");
		log.info("* * *");
		log.info("* * *");
		log.info("UPDATING PREVIOUS VISIT RECORDS");
		log.info("* * *");
		log.info("* * *");
		log.info("* * *");
		log.info("Updating prev visit records...");
		String filePath = "/sqlserver/omop/update-prev-visit.sql";
		InputStream is = FileUtil.getInputStream(filePath);
		Connection conn = null;
		if("postgres".equals(AppParams.get("cdmDbType"))) {
			conn = PostgresDatabaseConnectionFactory.getCdmConnection();
		} else {
			conn = OmopDatabaseConnectionFactory.getBootstrapConnection();
			String useString = "use " + AppParams.getDbName();
			log.info("Setting schema: \n" + useString);
			Database.update(useString, conn);
		}
		try {
			if("mssql".equals(AppParams.get("cdmDbType"))) {
				String dbName = AppParams.getDbName();
				Database.update("use " + dbName, conn);
			}
			Database.executeSqlScript(is, conn);
		} finally {
			Database.close(conn);
		}
		log.info("Done updating prev visit.");
	}

}
