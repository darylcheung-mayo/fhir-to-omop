package org.nachc.tools.fhirtoomop.fhir.patient;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.nachc.tools.fhirtoomop.fhir.parser.condition.ConditionParser;
import org.nachc.tools.fhirtoomop.fhir.parser.encounter.EncounterParser;
import org.nachc.tools.fhirtoomop.fhir.parser.medicationrequest.MedicationRequestParser;
import org.nachc.tools.fhirtoomop.fhir.parser.patient.PatientParser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FhirPatient {

	private PatientParser patient;

	private List<EncounterParser> encounterList = new ArrayList<EncounterParser>();

	private List<ConditionParser> conditionList = new ArrayList<ConditionParser>();

	private List<MedicationRequestParser> medicationRequestList = new ArrayList<MedicationRequestParser>();

	public EncounterParser getEncounter(String encounterId) {
		if (encounterId == null) {
			return null;
		}
		for (EncounterParser enc : encounterList) {
			if (encounterId.equals(enc.getEncounterId())) {
				return enc;
			}
		}
		return null;
	}

}
