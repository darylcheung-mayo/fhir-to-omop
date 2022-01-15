package org.nachc.tools.fhirtoomop.util.fhir.parser.patienteverything;

import java.util.List;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.nachc.tools.fhirtoomop.util.fhir.parser.bundle.BundleParser;
import org.nachc.tools.fhirtoomop.util.fhir.parser.patient.PatientParser;

import com.nach.core.util.fhir.parser.FhirJsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatientEverythingParser {

	//
	// instance variables
	//

	private String jsonString;
	
	private Bundle fhirBundle;
	
	private BundleParser bundle;
	
	private PatientParser patient;
	
	//
	// constructor
	//
	
	public PatientEverythingParser(String bundleJson) {
		// TODO: (JEG) Should probably do some validation here
		this.jsonString = bundleJson;
		this.fhirBundle = FhirJsonParser.parse(bundleJson, Bundle.class);
		this.bundle = new BundleParser(bundleJson);
		this.patient = new PatientParser(bundle.getPatient());
	}

	// ---
	//
	// implementation
	//
	// ---
	
	public List<String> getResourceTypes() {
		return bundle.getResourceTypes();
	}

	public PatientParser getPatient() {
		return this.patient;
	}

}
