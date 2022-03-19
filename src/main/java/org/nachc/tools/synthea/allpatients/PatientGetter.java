package org.nachc.tools.synthea.allpatients;

import java.io.File;
import java.util.List;

import org.nachc.tools.fhirtoomop.util.synthea.fetcher.patienteverything.SyntheaPatientEverythingFetcher;
import org.nachc.tools.fhirtoomop.util.synthea.oauth.SyntheaOauth;
import org.yaorma.util.time.Timer;

import com.nach.core.util.file.FileUtil;
import com.nach.core.util.guid.GuidFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatientGetter implements Runnable {

	private static double TOKEN_TIMEOUT = (10 * 60);

	private static String TOKEN;

	private static Timer TIMER;

	private static synchronized String getToken() {
		if (TIMER != null) {
			log.info("Elapsed: " + TIMER.getTimeSinceStart());
		}
		if (TOKEN == null || TIMER == null || TIMER.getElapsed() > TOKEN_TIMEOUT) {
			log.info("REFRESING TIMER");
			TIMER = new Timer();
			log.info("REFRESHING TOKEN...");
			TOKEN = SyntheaOauth.fetchToken();
			log.info("Got new token.");
		}
		return TOKEN;
	}

	private List<String> patientIds;

	private int threadId;

	private File outputDir;

	public PatientGetter(List<String> patientIds, File outputDir, int threadId) {
		this.patientIds = patientIds;
		this.threadId = threadId;
		this.outputDir = outputDir;
	}

	@Override
	public void run() {
		int cnt = 0;
		log.info("Getting token...");
		for (String patientId : patientIds) {
			log.info("Thread " + this.threadId + " getting patient...");
			cnt++;
			SyntheaPatientEverythingFetcher synthea = new SyntheaPatientEverythingFetcher();
			String token = getToken();
			String json = synthea.fetchEverything(patientId, token);
			String guid = GuidFactory.getGuid();
			String fileName = patientId + "_" + guid + ".json";
			File file = new File(outputDir, fileName);
			FileUtil.write(json, file);
			log.info("THREAD " + threadId + ": wrote " + cnt + " of " + patientIds.size() + " patients to file (" + patientId + "). " + FileUtil.getCanonicalPath(file));
		}
	}

}
