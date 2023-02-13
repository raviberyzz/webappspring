package ca.sunlife.web.apps.cmsservice.authentication;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ca.sunlife.web.apps.cmsservice.model.OktaResponse;

@Service
public class OktaTokenGenerator {

	@Value("${okta.oauth2.endpoint}")
	private String tokenEndpoint;

	@Value("${okta.oauth2.client.id}")
	private String clientId;

	@Value("${okta.oauth2.client.secret}")
	private String clientSecret;

	@Value("${okta.oauth2.grant-type}")
	private String grantType;
	
	@Value("${okta.oauth2.scope}")
	private String scope;
	
	@Autowired
	RestTemplate restTemplate;


	public String generateToken() {
		OktaResponse oktaResponse = null;
		try {
			String clientToken = "Basic "
					+ Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", clientToken);
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			header.add("Accept", "application/json");
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", grantType);
			HttpEntity<MultiValueMap<String, String>> requestHttp = new HttpEntity<>(body, header);
					ResponseEntity<OktaResponse> response = restTemplate.postForEntity(tokenEndpoint, requestHttp,
					OktaResponse.class);
			oktaResponse = response !=null ? response.getBody() : null;

		} catch (RestClientException ex) {
			ex.printStackTrace();
		}
		return oktaResponse != null ? oktaResponse.getAccessToken() : null;

	}

}
