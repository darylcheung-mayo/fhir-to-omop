package org.nachc.tools.fhirtoomop.util.params;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import com.nach.core.util.props.PropertiesUtil;

public class AppParams {

	private static final String DEFAULT = "auth/app.properties";
	
	private static Properties PROPS = null;

	static {
		try {
			PROPS = PropertiesUtil.getAsProperties(DEFAULT);			
		} catch(Exception exp) {
			System.out.println("Could not load default properties.");
			System.out.println("A properties file will need to be provided by the user.");
		}
	}
	
	/**
	 * 
	 * This method should only be used by the main class to allow the user to set
	 * properties locally
	 * 
	 */
	public static void setProps(InputStream is) {
		PROPS = PropertiesUtil.getAsProperties(is, "User's app.properties");
	}

	public static void resetToDefault() {
		PROPS = PropertiesUtil.getAsProperties(DEFAULT);
	}
	
	public static String OUT_DIR = "/test/synthea-tools";

	// passthrough method
	public static String get(String key) {
		return PROPS.getProperty(key);
	}
	
	// local files stuff

	public static String getTestOutputDirName() {
		return PROPS.getProperty("testOutputDir");
	}

	public static File getTestOutputDir() {
		return new File(OUT_DIR);
	}

	public static File getTestOutFile(String fileName) {
		return new File(getTestOutputDir(), fileName);
	}

	public static File getFullSetOfSyntheaPatientsDir() {
		String fileName = PROPS.getProperty("fullSetOfSyntheaPatientsDir");
		File file = new File(fileName);
		return file;
	}

	// synthea stuff

	public static String getSyntheaOauthUrl() {
		return PROPS.getProperty("synthea-oauth-url");
	}

	public static String getSyntheaUrl() {
		return PROPS.getProperty("synthea-url");
	}

	public static String getSyntheaAppId() {
		return PROPS.getProperty("synthea-app-id");
	}

	public static String getSyntheaKeyForToken() {
		return PROPS.getProperty("synthea-key");
	}

	public static String getSyntheaSecret() {
		return PROPS.getProperty("synthea-secret");
	}

	// connection stuff

	public static String getBootstrapUrl() {
		return PROPS.getProperty("bootstrapUrl");
	}

	public static String getUrl() {
		return PROPS.getProperty("url");
	}

	public static String getUid() {
		return PROPS.getProperty("uid");
	}

	public static String getPwd() {
		return PROPS.getProperty("pwd");
	}

	public static String getFullyQualifiedDbName() {
		return PROPS.getProperty("syntheaDb");
	}

	public static String getDbName() {
		String rtn = PROPS.getProperty("syntheaDb");
		return getCatalogPart(rtn);
	}

	public static String getCatalogPart(String schemaName) {
		String rtn = schemaName;
		if (rtn.indexOf(".") > 0) {
			rtn = rtn.trim().substring(0, rtn.trim().indexOf("."));
		}
		return rtn;
	}

	public static String getSchemaPart(String schemaName) {
		String rtn = schemaName;
		if (rtn.indexOf(".") > 0) {
			rtn = rtn.trim().substring(rtn.indexOf(".") + 1, rtn.length());
		}
		return rtn;
	}

	// umls stuff
	
	public static String getUmlsApiKey() {
		return PROPS.getProperty("umls-api-key");
	}
	
	// terminology stuff
	
	public static String getTerminologyRootDir() {
		return PROPS.getProperty("terminologyRootDir");
	}
	
	
	
}
