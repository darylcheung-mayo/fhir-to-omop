package org.nachc.tools.fhirtoomop.omop.write.threaded;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.nachc.tools.fhirtoomop.fhir.patient.factory.FhirPatientResources;
import org.nachc.tools.fhirtoomop.omop.write.threaded.runnable.WriteOmopPeopleToDatabaseWorkerRunnable;
import org.yaorma.util.time.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WriteOmopPeopleToDatabase {

	private final Object LOCK = new Object();
	
	private List<FhirPatientResources> resourceList;

	private List<Connection> connList;
	
	private int numberOfWorkers;

	private int numberOfPatientsPerWorker;
	
	private List<WriteOmopPeopleToDatabaseWorker> active = new ArrayList<WriteOmopPeopleToDatabaseWorker>();
	
	private HashMap<WriteOmopPeopleToDatabaseWorker, Thread> threads = new HashMap<WriteOmopPeopleToDatabaseWorker, Thread>();

	public WriteOmopPeopleToDatabase(List<FhirPatientResources> resourceList, List<Connection> connList, int numberOfWorkers, int numberOfPatientsPerWorker) {
		this.resourceList = resourceList;
		this.connList = connList;
		this.numberOfWorkers = numberOfWorkers;
		this.numberOfPatientsPerWorker = numberOfPatientsPerWorker;
	}
	
	private WriteOmopPeopleToDatabaseWorker getNextWorker() {
		if(resourceList.size() == 0) {
			return null;
		}
		List<FhirPatientResources> resourcesForNextWorker = new ArrayList<FhirPatientResources>();
		for(int i=0;i<numberOfPatientsPerWorker;i++) {
			if(resourceList.size() > 0) {
				resourcesForNextWorker.add(resourceList.remove(0));
			} else {
				break;
			}
		}
		WriteOmopPeopleToDatabaseWorker worker = new WriteOmopPeopleToDatabaseWorker(resourcesForNextWorker, this.connList, this);
		return worker;
	}

	public void exec() {
		while(true) {
			synchronized (LOCK) {
				if(active.size() < numberOfWorkers) {
					WriteOmopPeopleToDatabaseWorker worker = getNextWorker();
					if(worker == null) {
						break;
					}
					active.add(worker);
					WriteOmopPeopleToDatabaseWorkerRunnable runnable = new WriteOmopPeopleToDatabaseWorkerRunnable(worker);
					Thread thread = new Thread(runnable);
					threads.put(worker, thread);
					thread.start();
				}
			}
		}
		while(active.size() > 0) {
			TimeUtil.sleep(1);
			log.info("Almost done: " + active.size() + " active threads still running...");
		}
		Set<WriteOmopPeopleToDatabaseWorker> keys = threads.keySet();
		for (WriteOmopPeopleToDatabaseWorker key : keys) {
			try {
				Thread thread = threads.get(key);
				thread.join();
			} catch (Exception exp) {
				log.info("Could not join thread");
			}
		}
	}

	public void done(WriteOmopPeopleToDatabaseWorker worker) {
		synchronized (LOCK) {
			log.info("-----");
			log.info("Active:  " + active.size());
			log.info("Waiting: " + resourceList.size());
			log.info("-----");
			active.remove(worker);
			threads.remove(worker);
		}
	}

}
