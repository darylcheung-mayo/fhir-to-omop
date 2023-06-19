package org.nachc.tools.fhirtoomop.omop.person.factory;

import java.sql.Connection;

import org.hl7.fhir.dstu3.model.codesystems.ObservationParamcodeEnumFactory;
import org.nachc.tools.fhirtoomop.fhir.patient.r4.FhirPatient;
import org.nachc.tools.fhirtoomop.omop.person.OmopPerson;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.condition.OmopConditionOccurrenceBuilderR4;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.drugexposure.OmopDrugExposureBuilderR4;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.observation.MeasurementPostProcessor;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.observation.ObservationPostProcessor;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.observation.OmopObservationBuilderR4;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.person.OmopPersonBuilderR4;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.procedure.OmopProcedureBuilderR4;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.resource.OmopFhirResourceBuilderR4;
import org.nachc.tools.fhirtoomop.omop.person.factory.builder.visitoccurrence.OmopVisitOccurrenceBuilderR4;

public class OmopPersonFactoryR4 {

	public OmopPerson build(FhirPatient fhirPatient, Connection conn) {
		OmopPerson rtn = new OmopPerson();
		new OmopPersonBuilderR4(fhirPatient, rtn, conn).build();
		new OmopVisitOccurrenceBuilderR4(fhirPatient, rtn, conn).build();
		new OmopConditionOccurrenceBuilderR4(fhirPatient, rtn, conn).build();
		new OmopDrugExposureBuilderR4(fhirPatient, rtn, conn).build();
		new OmopObservationBuilderR4(fhirPatient, rtn, conn).build();
		new OmopProcedureBuilderR4(fhirPatient, rtn, conn).build();
		new OmopFhirResourceBuilderR4(fhirPatient, rtn, conn).build();
		// post processing
		new MeasurementPostProcessor(rtn, conn).build();
		new ObservationPostProcessor(rtn, conn).build();
		return rtn;
	}

}
