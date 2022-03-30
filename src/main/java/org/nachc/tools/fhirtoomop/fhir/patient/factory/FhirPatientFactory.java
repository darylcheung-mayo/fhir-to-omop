package org.nachc.tools.fhirtoomop.fhir.patient.factory;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Procedure;
import org.nachc.tools.fhirtoomop.fhir.parser.bundle.BundleParser;
import org.nachc.tools.fhirtoomop.fhir.parser.condition.ConditionParser;
import org.nachc.tools.fhirtoomop.fhir.parser.encounter.EncounterParser;
import org.nachc.tools.fhirtoomop.fhir.parser.medicationrequest.MedicationRequestParser;
import org.nachc.tools.fhirtoomop.fhir.parser.observation.ObservationParser;
import org.nachc.tools.fhirtoomop.fhir.parser.patient.PatientParser;
import org.nachc.tools.fhirtoomop.fhir.parser.procedure.ProcedureParser;
import org.nachc.tools.fhirtoomop.fhir.patient.FhirPatient;

import com.nach.core.util.file.FileUtil;

public class FhirPatientFactory {

	private List<BundleParser> bundleParserList = new ArrayList<BundleParser>();

	public FhirPatientFactory(List<String> resourceList) {
		for (String path : resourceList) {
			String json = FileUtil.getAsString(path);
			BundleParser bundleParser = new BundleParser(json);
			this.bundleParserList.add(bundleParser);
		}
	}

	public FhirPatient buildFhirPatient() {
		FhirPatient rtn = new FhirPatient();
		buildResourceTypes(rtn);
		buildPatient(rtn);
		buildEncounterList(rtn);
		buildConditionList(rtn);
		buildMedicationList(rtn);
		buildObservationList(rtn);
		buildProcedureList(rtn);
		return rtn;
	}

	// ---
	//
	// all private past here
	//
	// ---

	private void buildResourceTypes(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			rtn.getResourceTypes().addAll(parser.getResourceTypes());
		}
	}

	private void buildPatient(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			Patient patient = parser.getResourceForType(Patient.class);
			if (patient != null) {
				rtn.setPatient(new PatientParser(patient));
				return;
			}
		}
	}

	private void buildEncounterList(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			List<Encounter> list = parser.getResourceListForType(Encounter.class);
			for (Encounter enc : list) {
				rtn.getEncounterList().add(new EncounterParser(enc));
			}
		}
	}

	private void buildConditionList(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			List<Condition> list = parser.getResourceListForType(Condition.class);
			for (Condition con : list) {
				rtn.getConditionList().add(new ConditionParser(con));
			}
		}
	}

	private void buildMedicationList(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			List<MedicationRequest> list = parser.getResourceListForType(MedicationRequest.class);
			for (MedicationRequest med : list) {
				rtn.getMedicationRequestList().add(new MedicationRequestParser(med, rtn));
			}
		}
	}

	private void buildObservationList(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			List<Observation> list = parser.getResourceListForType(Observation.class);
			for (Observation obs : list) {
				rtn.getObservationList().add(new ObservationParser(obs, rtn));
			}
		}
	}

	private void buildProcedureList(FhirPatient rtn) {
		for (BundleParser parser : this.bundleParserList) {
			List<Procedure> list = parser.getResourceListForType(Procedure.class);
			for (Procedure proc : list) {
				rtn.getProcedureList().add(new ProcedureParser(proc, rtn));
			}
		}
	}

}
