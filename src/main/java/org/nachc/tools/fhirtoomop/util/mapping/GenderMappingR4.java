package org.nachc.tools.fhirtoomop.util.mapping;

import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;

public class GenderMappingR4 {

	public static Integer getOmopConceptForFhirCode(AdministrativeGender ag) {
		if(ag == AdministrativeGender.MALE) {
			return 8507;
		} else if (ag == AdministrativeGender.FEMALE) {
			return 8532;
		} else {
			return null;
		}
	}
	
}
