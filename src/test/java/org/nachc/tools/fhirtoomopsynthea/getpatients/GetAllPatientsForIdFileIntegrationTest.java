package org.nachc.tools.fhirtoomopsynthea.getpatients;

import java.io.File;

import org.junit.Test;
import org.nachc.tools.fhirtoomop.util.params.AppParams;
import org.nachc.tools.synthea.allpatients.GetAllPatientsForIdFile;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAllPatientsForIdFileIntegrationTest {

	private static final String TEST_FILE_NAME = "/synthea/patientids/synthea-patient-ids-1107.txt";

	private static final String OUT_FILE_DIR = "/test-patients/out";

	private static final int PATIENTS_PER_THREAD = 1;

	@Test
	public void shouldGetPatients() {
		log.info("Staring test...");
		// delete the existing output file
		File outDir = AppParams.getTestOutFile(OUT_FILE_DIR);
		// delete the existing output file
		log.info("Deleting existing out file: " + FileUtil.getCanonicalPath(outDir));
		FileUtil.rmdir(outDir);
		FileUtil.mkdirs(outDir);
		// get the test file
		File file = FileUtil.getFile(TEST_FILE_NAME);
		// get the patients
		GetAllPatientsForIdFile getter = new GetAllPatientsForIdFile(file, outDir, PATIENTS_PER_THREAD);
		log.info("Getting patients...");
		getter.getPatients();
		log.info("Done.");
	}

}
