package org.nachc.cosmos.tools.fhirtoomop.unittestintegrationtest.synthea.basicqueries;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;
import org.nachc.cosmos.tools.fhirtoomop.unittesttool.synthea.patient.PatientIdFetcher;
import org.nachc.tools.fhirtoomop.util.fhir.parser.patientsummary.PatientSummaryParser;
import org.nachc.tools.fhirtoomop.util.fhir.parser.patientsummarylistbundle.PatientSummaryListBundleParser;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.nachc.tools.fhirtoomop.util.synthea.fetcher.patienteverything.SyntheaPatientEverythingFetcher;
import org.nachc.tools.fhirtoomop.util.synthea.fetcher.patientsummarylist.SyntheaPatientSummaryListFetcher;

import com.nach.core.util.file.FileUtil;
import com.nach.core.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class C_GetEverythingForSinglePatientIntegrationTest {

	@Test
	public void shouldGetPatient() {
		log.info("Starting test...");
		// get a patient id
		String patientId = PatientIdFetcher.getASinglePatientId();
		log.info("Got patient: " + patientId);
		log.info("Getting everything...");
		// get everything for that patient from synthea
		SyntheaPatientEverythingFetcher synthea = new SyntheaPatientEverythingFetcher();
		String everythingJson = synthea.fetchEverything(patientId);
		log.info("Status: " + synthea.getStatusCode());
		log.info("Got response: \n" + JsonUtil.prettyPrint(everythingJson) + "\n\n");
		log.info("Status: " + synthea.getStatusCode());
		// write the patient to a file in case someone wants to take a closer look
		File file = AppParams.getOutFile("everything-patient.json");
		log.info("Writing file to: " + FileUtil.getCanonicalPath(file));
		FileUtil.write(JsonUtil.prettyPrint(everythingJson), file);
		// assert that we got a patient
		log.info("Status code: " + synthea.getStatusCode());
		assertTrue(synthea.getStatusCode() == 200);
		log.info("Done.");
	}

}