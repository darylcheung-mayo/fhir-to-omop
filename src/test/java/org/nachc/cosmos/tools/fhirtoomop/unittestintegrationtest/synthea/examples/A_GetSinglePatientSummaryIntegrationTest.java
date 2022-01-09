package org.nachc.cosmos.tools.fhirtoomop.unittestintegrationtest.synthea.examples;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nachc.tools.fhirtoomop.util.synthea.SyntheaPatientSummaryListFetcher;

import com.nach.core.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class A_GetSinglePatientSummaryIntegrationTest {

	@Test
	public void shouldGetPatients() {
		log.info("Starting test..");
		log.info("Getting patient from synthea...");
		SyntheaPatientSummaryListFetcher synthea = new SyntheaPatientSummaryListFetcher(); 
		String response = synthea.fetchPatients(1);
		log.info("Got response: \n" + JsonUtil.prettyPrint(response));
		log.info("Status code: " + synthea.getStatusCode());
		assertTrue(synthea.getStatusCode() == 200);
		log.info("Done.");
	}

}
