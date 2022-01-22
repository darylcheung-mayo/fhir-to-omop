package org.nachc.tools.fhirtoomop.util.fhir.parser.encounter;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.Coding;
import org.junit.Test;
import org.nachc.tools.fhirtoomop.unittesttool.params.TestParams;
import org.nachc.tools.fhirtoomop.util.fhir.parser.patienteverything.PatientEverythingParser;
import org.yaorma.util.time.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncounterParserIntegrationTest {

	@Test
	public void shouldGetEncounter() {
		log.info("Starting test...");
		PatientEverythingParser parser = TestParams.getPatientEverything();
		List<EncounterParser> encounterList = parser.getEncounterList();
		// get and test a single encounter
		EncounterParser enc = encounterList.get(0);
		// get encounter ids
		String encounterId = enc.getEncounterId();
		log.info("encounterIdQual: " + encounterId);
		assertTrue(encounterId.equals("Encounter/051b0d30-03d3-4d6d-a070-f8d363ef277f/_history/MTU1NDgxMjczNjQ3Nzk3NjAwMA"));
		String encounterIdUnc = enc.getEncounterIdUnqualified();
		log.info("encounterIdUnc:  " + encounterIdUnc);
		assertTrue(encounterIdUnc.equals("051b0d30-03d3-4d6d-a070-f8d363ef277f"));
		String encounterIdUncAndQual = enc.getEncounterIdUncAndQual();
		log.info("encounterIdUncAndQual: " + encounterIdUncAndQual);
		assertTrue(encounterIdUncAndQual.equals("051b0d30-03d3-4d6d-a070-f8d363ef277f|Encounter/051b0d30-03d3-4d6d-a070-f8d363ef277f/_history/MTU1NDgxMjczNjQ3Nzk3NjAwMA"));
		// get encounter date
		Date startDate = enc.getStartDate();
		String startDateString = TimeUtil.format(startDate, "yyyy-MM-dd");
		log.info("startDate: " + startDateString);
		assertTrue("2009-01-10".equals(startDateString));
		Date endDate = enc.getEndDate();
		String endDateString = TimeUtil.format(endDate, "yyyy-MM-dd");
		log.info("endDate: " + endDateString);
		assertTrue("2009-01-10".equals(endDateString));
		// encounter type
		Coding typeCoding = enc.getEncounterType();
		log.info("coding: " + typeCoding);
		log.info("typeCode: " + typeCoding.getCode());
		log.info("typeSys:  " + typeCoding.getSystem());
		log.info("typeDis:  " + typeCoding.getDisplay());
		// done
		log.info("Done.");
	}
	
}