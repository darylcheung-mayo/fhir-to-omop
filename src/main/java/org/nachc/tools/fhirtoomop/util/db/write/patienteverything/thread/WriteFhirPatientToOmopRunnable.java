package org.nachc.tools.fhirtoomop.util.db.write.patienteverything.thread;

import java.io.File;
import java.sql.Connection;

import org.nachc.tools.fhirtoomop.util.db.write.patienteverything.WriteFhirPatientToOmop;
import org.nachc.tools.fhirtoomop.util.fhir.parser.patienteverything.PatientEverythingParser;

import com.nach.core.util.file.FileUtil;

import ca.uhn.fhir.parser.DataFormatException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteFhirPatientToOmopRunnable implements Runnable {

	private Integer id;

	private Connection conn;

	private File file;

	private String json;

	private PatientEverythingParser parser;

	public WriteFhirPatientToOmopRunnable(File file, Connection conn, Integer id) {
		this.file = file;
		this.conn = conn;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			this.json = FileUtil.getAsString(file);
			this.parser = new PatientEverythingParser(json);
			WriteFhirPatientToOmop.exec(this.parser, this.conn);
			log.info("DONE:  Writing to database (thread " + this.id + ")");
		} catch (RuntimeException exp) {
			Throwable cause = exp.getCause();
			if (cause instanceof DataFormatException) {
				log.warn("! ! ! EXCEPTION THROWN TRING TO WRITE PATIENT ! ! !");
				log.warn("File: " + FileUtil.getCanonicalPath(file));
				log.warn("This is generally expected for the data files we are using.");
			} else {
				throw new RuntimeException(exp);
			}
		}
	}

}
