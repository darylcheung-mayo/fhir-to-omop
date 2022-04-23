package org.nachc.tools.fhirtoomop.omop.write.threaded;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.nachc.tools.fhirtoomop.fhir.patient.factory.FhirPatientResources;
import org.nachc.tools.fhirtoomop.fhir.patient.factory.impl.file.FhirPatientResourcesAsFilesFactory;
import org.nachc.tools.fhirtoomop.util.db.connection.OmopDatabaseConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteOmopPeopleToDatabaseWorkerIntegrationTest {

//	private static final String DIR = "D:\\NACHC\\SYNTHEA\\TEST\\SYNTHEA_MICRO\\synthea-micro-patients";
	
	private static final String DIR = "/test/fhir/test-sets/test-set-10";
	
	private int NUM_OF_CONNS = 1;

	private int MAX_PER_BATCH = 1;
	
	@Test
	public void shouldGetPatients() {
		log.info("Starting tests...");
		List<FhirPatientResources> resources = FhirPatientResourcesAsFilesFactory.getForDir(DIR);
		List<Connection> conns = getConnections();
		WriteOmopPeopleToDatabase writer = new WriteOmopPeopleToDatabase(resources, conns, NUM_OF_CONNS, MAX_PER_BATCH);
		writer.exec();
		log.info("Done.");
	}

	private List<Connection> getConnections() {
		List<Connection> rtn = new ArrayList<Connection>();
		for (int i = 0; i < NUM_OF_CONNS; i++) {
			Connection conn = OmopDatabaseConnectionFactory.getOmopConnection();
			rtn.add(conn);
		}	
		return rtn;
	}
	
	private void closeConnections(List<Connection> connList) {
		for(Connection conn : connList) {
			OmopDatabaseConnectionFactory.close(conn);
		}
	}


	
}
