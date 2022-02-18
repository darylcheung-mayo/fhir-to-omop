package org.nachc.tools.fhirtoomop.util.db.connection;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.Test;
import org.nachc.tools.fhirtoomop.util.db.connection.OmopDatabaseConnectionFactory;
import org.nachc.tools.fhirtoomop.util.params.MySqlAuthParams;
import org.yaorma.database.Data;
import org.yaorma.database.Database;
import org.yaorma.database.Row;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OmopDatabaseConnectionFactoryIntegrationTest {

	@Test
	public void shouldGetConnection() {
		log.info("Starting test...");
		Connection conn = OmopDatabaseConnectionFactory.getOmopConnection();
		try {
			String sqlString = "select * from master.information_schema.tables order by table_name";
			Data data = Database.query(sqlString, conn);
			log.info("Got " + data.size() + " records.");
			for(Row row : data) {
				log.info("\t" + row.get("tableName"));
			}
			log.info("Got " + data.size() + " records.");
			assertTrue(data.size() > 0);
			String schemaName = MySqlAuthParams.syntheaDb();
			Database.update("use " + schemaName, conn);
		} finally {
			OmopDatabaseConnectionFactory.close(conn);
		}
		log.info("Done.");
	}
	
}
