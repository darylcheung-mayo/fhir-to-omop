package org.nachc.tools.fhirtoomop.util.fhirtoomop.person;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.nachc.tools.fhirtoomop.util.fhir.parser.patienteverything.PatientEverythingParser;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.OmopConditionFactory;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.OmopDrugExposureFactory;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.OmopMeasurementFactory;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.OmopObservationFactory;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.OmopPersonFactory;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.OmopVisitOccurrenceFactory;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.obs.ObservationDvoProxy;
import org.nachc.tools.fhirtoomop.util.fhirtoomop.person.impl.obs.ObservationOrMeasurement;
import org.nachc.tools.omop.yaorma.dvo.ConditionOccurrenceDvo;
import org.nachc.tools.omop.yaorma.dvo.DrugExposureDvo;
import org.nachc.tools.omop.yaorma.dvo.MeasurementDvo;
import org.nachc.tools.omop.yaorma.dvo.ObservationDvo;
import org.nachc.tools.omop.yaorma.dvo.PersonDvo;
import org.nachc.tools.omop.yaorma.dvo.VisitOccurrenceDvo;

/**
 * 
 * This is the class that turns a FHIR Patient/$everything resource into an OMOP
 * Person data value object (dvo).
 *
 */

public class OmopPersonEverythingFactory {

	//
	// instance variables
	//

	private String json;

	private Connection conn;

	private PatientEverythingParser patientEverything;

	private PersonDvo person;

	private List<VisitOccurrenceDvo> visitOccurrenceList;

	private List<ConditionOccurrenceDvo> conditionOccurrenceList;

	private List<ObservationDvoProxy> observationList;

	private List<DrugExposureDvo> drugExposureList;

	//
	// constructors
	//

	public OmopPersonEverythingFactory(PatientEverythingParser patientEverything, Connection conn) {
		this.conn = conn;
		this.patientEverything = patientEverything;
	}

	public OmopPersonEverythingFactory(String json, Connection conn) {
		this.json = json;
		this.conn = conn;
		this.patientEverything = new PatientEverythingParser(json);
	}

	//
	// trivial getters and setters
	//

	public PatientEverythingParser getFhirPatientEverything() {
		return this.patientEverything;
	}

	//
	// getters and setters
	//

	public PersonDvo getPerson() {
		if (this.person == null) {
			this.person = new OmopPersonFactory(patientEverything, conn).getPerson();
		}
		return this.person;
	}

	public List<VisitOccurrenceDvo> getVisitOccurrenceList() {
		if (this.visitOccurrenceList == null) {
			OmopVisitOccurrenceFactory visitParser = new OmopVisitOccurrenceFactory(this, conn);
			this.visitOccurrenceList = visitParser.getVisitOccurencesList();
		}
		return this.visitOccurrenceList;
	}

	public List<ConditionOccurrenceDvo> getConditionOccurrenceList() {
		if (this.conditionOccurrenceList == null) {
			OmopConditionFactory factory = new OmopConditionFactory(this, conn);
			this.conditionOccurrenceList = factory.getConditionList();
		}
		return this.conditionOccurrenceList;
	}

	public List<DrugExposureDvo> getDrugExposureList() {
		if (this.drugExposureList == null) {
			OmopDrugExposureFactory factory = new OmopDrugExposureFactory(this, conn);
			this.drugExposureList = factory.getDrugExposureList();
		}
		return this.drugExposureList;
	}

	//
	// observation stuff
	//

	public List<ObservationDvoProxy> getFhirObservationList() {
		if (this.observationList == null) {
			OmopObservationFactory factory = new OmopObservationFactory(this, conn);
			this.observationList = factory.getObservationList();
		}
		return this.observationList;
	}

	public List<MeasurementDvo> getMeasurementList() {
		List<ObservationDvoProxy> allObs = this.getFhirObservationList();
		List<MeasurementDvo> rtn = new ArrayList<MeasurementDvo>();
		for (ObservationDvoProxy proxy : allObs) {
			if (proxy.getObservationOrMeasurement() == ObservationOrMeasurement.MEASUREMENT) {
				MeasurementDvo meas = OmopMeasurementFactory.getMeasurement(proxy);
				rtn.add(meas);
			}
		}
		return rtn;
	}

	public List<ObservationDvo> getObservationList() {
		List<ObservationDvoProxy> allObs = this.getFhirObservationList();
		List<ObservationDvo> rtn = new ArrayList<ObservationDvo>();
		for (ObservationDvoProxy proxy : allObs) {
			if (proxy.getObservationOrMeasurement() == ObservationOrMeasurement.OBSERVATION) {
				rtn.add(proxy.getDvo());
			}
		}
		return rtn;
	}

	//
	// convenience methods to get ids
	//

	public Integer getOmopPatientId() {
		return this.getPerson().getPersonId();
	}

	public Integer getOmopEncounterId(String fhirEncounterId) {
		List<VisitOccurrenceDvo> visitList = this.getVisitOccurrenceList();
		for (VisitOccurrenceDvo dvo : visitList) {
			String sourceId = dvo.getVisitSourceValue();
			if (sourceId != null && sourceId.equals(fhirEncounterId)) {
				return dvo.getVisitOccurrenceId();
			}
		}
		return null;
	}

}
