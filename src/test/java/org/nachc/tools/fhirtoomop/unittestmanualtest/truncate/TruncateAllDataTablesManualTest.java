package org.nachc.tools.fhirtoomop.unittestmanualtest.truncate;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.nachc.tools.fhirtoomop.util.db.mysql.MySqlDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.db.truncatedatatables.TruncateDataTables;
import org.yaorma.database.Database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TruncateAllDataTablesManualTest {

	private static final String[] TABLE_NAMES = {
			"condition_occurrence",
			"drug_exposure",
			"procedure_occurrence",
			"device_exposure",
			"measurement",
			"observation",
			"note",
			"visit_detail",
			"visit_occurrence",
			"observation",
			"person"
	};

	
	public static void main(String[] args) {
		Connection conn = MySqlDatabaseConnectionFactory.getSyntheaConnection();
		try {
			List<String> tableNames = Arrays.asList(TABLE_NAMES);
			TruncateDataTables.truncateTables(tableNames, conn);
			Database.commit(conn);
		} finally {
			Database.close(conn);
		}
		log.info("Done.");
	}

}
