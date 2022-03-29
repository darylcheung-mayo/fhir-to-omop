package org.nachc.tools.fhirtoomop.fhir.validate;

import java.util.ArrayList;
import java.util.List;

import org.nachc.tools.fhirtoomop.fhir.parser.encounter.EncounterParser;
import org.nachc.tools.fhirtoomop.fhir.patient.FhirPatient;
import org.nachc.tools.fhirtoomop.fhir.validate.encounter.ValidateEncounter;
import org.nachc.tools.fhirtoomop.fhir.validate.patient.ValidatePatient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidateFhirPatient {

	private FhirPatient pat;
	
	private List<String> err = new ArrayList<String>();;
	
	public ValidateFhirPatient(FhirPatient fhirPatient) {
		this.pat = fhirPatient;
	}
	
	public boolean isValid() {
		return this.err.size() == 0;
	}
	
	public ValidateFhirPatient validate() {
		if(pat.getPatient() == null) {
			err.add("null patient");
		}
		log.info("Got " + pat.getEncounterList().size() + " encounters.");
		log.info("Got " + pat.getConditionList().size() + " conditions.");
		err.addAll(new ValidatePatient(pat.getPatient()).validate().getErr());
		log.info("--- ENCOUNTERS ---");
		for(EncounterParser enc : pat.getEncounterList()) {
			err.addAll(new ValidateEncounter(enc).validate().getErr());
		}
		log.info("--- END ENCOUNTERS ---");
		logErr();
		return this;
	}
	
	public void logErr() {
		if(err.size() > 0) {
			log.info("--- GOT ERRORS");
			for(String msg : err) {
				log.info(msg);
			}
			log.info("--- END ERRORS");
		} else {
			log.info("* * * ALL CLEAR * * *");
		}
	}

}
