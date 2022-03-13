package org.nachc.tools.fhirtoomop.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.nachc.tools.fhirtoomop.tools.build.CreateOmopInstanceTool;
import org.nachc.tools.fhirtoomop.tools.test.ListTestPatients;
import org.nachc.tools.fhirtoomop.tools.test.WriteTestPatientsToDatabase;
import org.nachc.tools.fhirtoomop.util.params.AppParams;

import com.nach.core.util.file.FileUtil;

public class FhirToOmopMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to fhir-to-omop");
		if (args == null || args.length < 2) {
			zeroParam();
		} else {
			// get the config file
			String fileName = args[1];
			System.out.println("Using config file from here:");
			System.out.println(fileName);
			File file = new File(fileName, "app.properties");
			InputStream is = new FileInputStream(file);
			AppParams.setProps(is);
			// run the requested task
			String name = args[0];
			switch (name.toLowerCase()) {
			case "say-hello":
			case "h":
				sayHello();
				break;
			case "params-example":
			case "p":
				paramsExample();
				break;
			case "my-params":
			case "m":
				myParams(fileName);
				break;
			case "instant-omop":
			case "i":
				instantOmop();
				break;
			case "list-test-patients":
			case "l":
				listTestPatients();
				break;
			case "add-test-patients":
			case "a":
				addTestPatients();
				break;
			case "add-from-dir":
				break;
			default:
				wrongParam();
				break;
			}
		}
	}

	private static void sayHello() {
		System.out.println("Hello World!");
	}

	private static void paramsExample() {
		System.out.println("\n\nThe following is an example of a params file");
		System.out.println("Cut and paste the section between the dotted lines and put it in a file called app.params");
		System.out.println("Put the file in the directory you are in now.");
		System.out.println("Replace <my_api_key> with your api key.\n\n");
		System.out.println("# -----------------8<-----------------8<-----------------8<--------------------\n\n");
		String msg = FileUtil.getAsString("/main/example-params/app.properties");
		System.out.println(msg);
		System.out.println("# -----------------8<-----------------8<-----------------8<--------------------");
	}

	private static void myParams(String fileName) {
		File file = new File(fileName, "app.properties");
		String msg = FileUtil.getAsString(file);
		System.out.println("The contents of you app.properties file is shown between the dotted lines below.\n\n");
		System.out.println("-----------------8<-----------------8<-----------------8<--------------------\n\n");
		System.out.println(msg);
		System.out.println("-----------------8<-----------------8<-----------------8<--------------------");
	}

	private static void instantOmop() {
		CreateOmopInstanceTool.createOmopInstance();
	}

	private static void listTestPatients() {
		ListTestPatients.exec();
	}
	
	private static void addTestPatients() {
		WriteTestPatientsToDatabase.exec();
	}
	
	// ---
	//
	// error cases
	//
	// ---

	private static void zeroParam() {
		String msg = FileUtil.getAsString("/main/msg/zero-args-error.txt");
		System.out.println(msg);
	}

	private static void wrongParam() {
		String msg = FileUtil.getAsString("/main/msg/wrong-param-error.txt");
		System.out.println(msg);
	}

}
