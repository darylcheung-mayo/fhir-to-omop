package org.nachc.tools.fhirtoomop.tools.build.postgres;

import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.db.connection.postgres.PostgresDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.yaorma.database.Database;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateCdmSourceRecordPostgres {

	private static final String PATH = "/postgres/omop/atlas/create-cdm-source-record.sql";

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

	private static void exec(Connection conn) {
		String sqlString = FileUtil.getAsString(PATH);
		sqlString = replace(sqlString, "cdm_source_name");
		sqlString = replace(sqlString, "cdm_source_abbreviation");
		sqlString = replace(sqlString, "cdm_holder");
		sqlString = replace(sqlString, "source_description");
		sqlString = replace(sqlString, "source_documentation_reference");
		sqlString = replace(sqlString, "cdm_etl_reference");
		sqlString = replace(sqlString, "source_release_date");
		sqlString = replace(sqlString, "cdm_release_date");
		sqlString = replace(sqlString, "cdm_version_concept_id");
		sqlString = replace(sqlString, "cdm_version");
		sqlString = replace(sqlString, "vocabulary_version");
		sqlString = sqlString.replace("@cdmDatabaseSchema", "public");
		log.info("SQLSTRING: \n\n" + sqlString);
		Database.executeSqlScript(sqlString, conn);
	}

	private static String replace(String sqlString, String name) {
		return sqlString.replace("@" + name, AppParams.get(name));
	}

}
