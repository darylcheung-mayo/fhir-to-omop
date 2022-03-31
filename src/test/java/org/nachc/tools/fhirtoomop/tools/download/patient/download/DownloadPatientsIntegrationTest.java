package org.nachc.tools.fhirtoomop.tools.download.patient.download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DownloadPatientsIntegrationTest {

	private static final String DIR_PATH = "/test/fhir/patient-ids/patient-ids-2500";

	@Test
	public void shouldGetPatients() {
		log.info("Getting patients...");
		List<String> patientList = getPatientList();
		log.info("Got " + patientList.size() + " patients.");
		DownloadPatients.getPatients(patientList);
		log.info("Done.");
	}

	private List<String> getPatientList() {
		List<String> rtn = new ArrayList<String>();
		File dir = FileUtil.getFile(DIR_PATH);
		List<File> fileList = FileUtil.listFiles(dir);
		for (File file : fileList) {
			rtn.addAll(FileUtil.getAsList(file));
		}
		return rtn;
	}
}
