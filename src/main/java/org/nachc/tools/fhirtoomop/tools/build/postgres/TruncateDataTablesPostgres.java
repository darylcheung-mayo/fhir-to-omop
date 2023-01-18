package org.nachc.tools.fhirtoomop.tools.build.postgres;

import org.nachc.tools.fhirtoomop.util.db.truncatedatatables.TruncateAllDataTables;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TruncateDataTablesPostgres {

	public static void main(String[] args) {
		log.info("Truncating all data tables...");
		TruncateAllDataTables.exec();
		log.info("Done.");
	}
	
}
