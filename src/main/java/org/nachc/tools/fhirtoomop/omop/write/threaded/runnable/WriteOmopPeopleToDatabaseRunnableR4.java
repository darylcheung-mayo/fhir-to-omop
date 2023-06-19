package org.nachc.tools.fhirtoomop.omop.write.threaded.runnable;

import java.sql.Connection;

import org.nachc.tools.fhirtoomop.fhir.patient.r4.FhirPatient;
import org.nachc.tools.fhirtoomop.fhir.patient.r4.factory.FhirPatientFactory;
import org.nachc.tools.fhirtoomop.fhir.patient.factory.FhirPatientResources;
import org.nachc.tools.fhirtoomop.omop.person.OmopPerson;
import org.nachc.tools.fhirtoomop.omop.person.factory.OmopPersonFactoryR4;
import org.nachc.tools.fhirtoomop.omop.write.singlepatient.WriteOmopPersonToDatabase;
import org.yaorma.database.Database;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteOmopPeopleToDatabaseRunnableR4 implements Runnable {

	private FhirPatientResources resources;

	private Connection conn;

	public WriteOmopPeopleToDatabaseRunnableR4(FhirPatientResources resources, Connection conn) {
		this.resources = resources;
		this.conn = conn;
	}

	@Override
	public void run() {
		try {
			if(resources.getResources().size() == 1) {
				System.out.println("Inside the actual file");
				FhirPatient fhirPatient = new FhirPatientFactory(resources).build();
				OmopPerson omopPerson = new OmopPersonFactoryR4().build(fhirPatient, conn);
				log.info("Done parsing file");
				WriteOmopPersonToDatabase.exec(omopPerson, conn);
				Database.commit(conn);
			}
			// FhirPatient fhirPatient = new FhirPatientFactory(resources).build();
			
			// OmopPerson omopPerson = new OmopPersonFactory().build(fhirPatient, conn);
			// log.info("line33 Create the OMOP Person");
			// WriteOmopPersonToDatabase.exec(omopPerson, conn);
			// Database.commit(conn);
			log.info("DONE WRITING PATIENT TO DATABASE");
		} catch(Exception exp) {
			exp.printStackTrace();
		}
	}

}
