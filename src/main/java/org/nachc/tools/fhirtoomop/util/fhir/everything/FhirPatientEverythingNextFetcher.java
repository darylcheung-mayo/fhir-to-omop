package org.nachc.tools.fhirtoomop.util.fhir.everything;

import org.nachc.tools.fhirtoomop.util.synthea.oauth.SyntheaOauth;

import com.nach.core.util.http.HttpRequestClient;
import com.nach.core.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FhirPatientEverythingNextFetcher {

	private HttpRequestClient client;

	public HttpRequestClient getClient() {
		return this.client;
	}

	public int getStatusCode() {
		return this.client.getStatusCode();
	}

	public String fetchNext(String nextUrl, String token) {
		log.info("URL: " + nextUrl);
		this.client = new HttpRequestClient(nextUrl);
		SyntheaOauth.addHeaders(client, token);
		client.doGet();
		int status = client.getStatusCode();
		log.info("Got status: " + status);
		String response = client.getResponse();
		log.info("Response length: " + response.length());
		if (status != 200) {
			log.info("DID NOT GET 200 STATUS: ");
			log.info(JsonUtil.prettyPrint(response));
		}
		return response;
	}

}
