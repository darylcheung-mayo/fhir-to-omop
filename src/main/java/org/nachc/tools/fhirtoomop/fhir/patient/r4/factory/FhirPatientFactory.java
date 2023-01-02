package org.nachc.tools.fhirtoomop.fhir.patient.r4.factory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.nachc.tools.fhirtoomop.fhir.parser.bundle.IBundleParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.bundle.BundleParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.condition.ConditionParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.encounter.EncounterParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.medicationrequest.MedicationRequestParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.observation.ObservationParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.patient.PatientParser;
import org.nachc.tools.fhirtoomop.fhir.parser.r4.procedure.ProcedureParser;
import org.nachc.tools.fhirtoomop.fhir.patient.r4.FhirPatient;
import org.nachc.tools.fhirtoomop.fhir.patient.r4.factory.FhirPatientFactory;
import org.nachc.tools.fhirtoomop.fhir.patient.factory.FhirPatientResources;
import org.nachc.tools.fhirtoomop.fhir.patient.factory.impl.file.FhirPatientResourcesAsFiles;
import org.nachc.tools.fhirtoomop.fhir.patient.factory.impl.file.FhirPatientResourcesAsFilesFactory;

import com.nach.core.util.file.FileUtil;

public class FhirPatientFactory {

	//
	// static methods
	//
	
	public static FhirPatient build(File dir) {
		FhirPatientResources resources =  FhirPatientResourcesAsFilesFactory.getForPatient(dir);
		return build(resources);
	}

	
	public static FhirPatient build(FhirPatientResources resources) {
		FhirPatient fhirPatient = new FhirPatientFactory(resources).build();
		return fhirPatient;
	}
	
	public static FhirPatient buildFromSingleFile(File file) {
		FhirPatientResources resources = new FhirPatientResourcesAsFiles(file);
		FhirPatient fhirPatient = new FhirPatientFactory(resources).build();
		return fhirPatient;
	}
	
	//
	// instance variables
	//
	
	private FhirPatientResources resources;

	private List<BundleParser> bundleParserList = new ArrayList<BundleParser>();

	//
	// constructor(s)
	//
	
	public FhirPatientFactory(FhirPatientResources resources) {
		this.resources = resources;
	}

	//
	// build method
	//
	
	public FhirPatient build() {
		for (InputStream is : resources.getResources()) {
			String str = FileUtil.getAsString(is);
			BundleParser bundleParser = new BundleParser(str);
			this.bundleParserList.add(bundleParser);
		}
		return doBuild();
	}

	// ---
	//
	// all private past here
	//
	// ---

	private FhirPatient doBuild() {
		FhirPatient rtn = new FhirPatient();
		rtn.setBundleParserList(this.bundleParserList);
		buildResourceTypes(rtn);
		buildPatient(rtn);
		buildEncounterList(rtn);
		buildConditionList(rtn);
		buildMedicationList(rtn);
		buildObservationList(rtn);
		buildProcedureList(rtn);
		return rtn;
	}

	private void buildResourceTypes(FhirPatient rtn) {
		for (IBundleParser parser : this.bundleParserList) {
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