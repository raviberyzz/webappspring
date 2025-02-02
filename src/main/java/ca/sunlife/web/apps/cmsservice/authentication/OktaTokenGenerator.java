package ca.sunlife.web.apps.cmsservice.authentication;

import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import ca.sunlife.web.apps.cmsservice.restclient.RestTemplateGenerator;

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
	
	@Value("${okta.oauth2.endpoint.faa}")
	private String tokenEndpointfaa;

	@Value("${okta.oauth2.client.id.faa}")
	private String clientIdfaa;

	@Value("${okta.oauth2.client.secret.faa}")
	private String clientSecretfaa;
	
	@Value("${okta.oauth2.scope.faa}")
	private String scopefaa;
	
	@Value("${okta.oauth2.endpoint.communication}")
	private String tokenEndpointCommunication;

	@Value("${okta.oauth2.client.id.communication}")
	private String clientIdCommunication;

	@Value("${okta.oauth2.client.secret.communication}")
	private String clientSecretCommunication;
	
    @Autowired
    RestTemplateGenerator restTemplateGenerator;
    
    RestTemplate restTemplate;
	
	private static final Logger logger = LogManager.getLogger(OktaTokenGenerator.class);
	
	public String generateToken(String endpoint, String clientScope, String id, String secret) {
		OktaResponse oktaResponse = null;
		
		try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
				logger.info("getOktaAuthTokencommunication: Initiating rest template");
			}
			String clientAuthKey = "Basic " + Base64.getEncoder().encodeToString((id + ":" + secret).getBytes());
            logger.info("client authKey Communications API ::{}", clientAuthKey);
            
			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", clientAuthKey);
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			header.add("Accept", "application/json");
			
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", grantType);
			body.add("scope", clientScope);
			HttpEntity<MultiValueMap<String, String>> requestHttp = new HttpEntity<>(body, header);
            logger.info("request http ::{}",requestHttp);
			logger.info("tokenEndpoint ::{}",tokenEndpointCommunication);
			
			ResponseEntity<OktaResponse> response = restTemplate.postForEntity(endpoint, requestHttp, OktaResponse.class);
			oktaResponse = response != null ? response.getBody() : null;
			logger.info("okta response communications API ::{}",oktaResponse);
		} catch (RestClientException e) {
			logger.error("Failed to get okta token.  Endpoint: " + endpoint + ":::ClientId: " + id);
			e.printStackTrace();
		}
		return oktaResponse != null ? oktaResponse.getAccess_token() : null;
	}
	
	public String generateTokenFaa() {
		
		OktaResponse oktaResponse = null;
		try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
				logger.info("getOktaAuthTokenfaa: Initiating rest template");
			}
			String clientAuthKey = "Basic "
					+ Base64.getEncoder().encodeToString((clientIdfaa + ":" + clientSecretfaa).getBytes());
            logger.info("client authKey faa ::{}", clientAuthKey);
			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", clientAuthKey);
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			header.add("Accept", "application/json");
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", grantType);
			body.add("scope", scopefaa);
			HttpEntity<MultiValueMap<String, String>> requestHttp = new HttpEntity<>(body, header);
            logger.info("request http ::{}",requestHttp);
			logger.info("tokenEndpoint ::{}",tokenEndpointfaa);
			ResponseEntity<OktaResponse> response = restTemplate.postForEntity(tokenEndpointfaa, requestHttp,
			OktaResponse.class);
			oktaResponse = response != null ? response.getBody() : null;
			logger.info("okta response faa ::{}",oktaResponse);
		} catch (RestClientException ex) {
			logger.error("failed to get faa okta token");
			ex.printStackTrace();
		}
		return oktaResponse != null ? oktaResponse.getAccess_token() : null;

	}
	
public String generateTokenCommunication() {
		
		OktaResponse oktaResponse = null;
		try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
				logger.info("getOktaAuthTokencommunication: Initiating rest template");
			}
			String clientAuthKey = "Basic "
					+ Base64.getEncoder().encodeToString((clientIdCommunication + ":" + clientSecretCommunication).getBytes());
            logger.info("client authKey Communications API ::{}", clientAuthKey);
			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", clientAuthKey);
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			header.add("Accept", "application/json");
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", grantType);
			HttpEntity<MultiValueMap<String, String>> requestHttp = new HttpEntity<>(body, header);
            logger.info("request http ::{}",requestHttp);
			logger.info("tokenEndpoint ::{}",tokenEndpointCommunication);
			ResponseEntity<OktaResponse> response = restTemplate.postForEntity(tokenEndpointCommunication, requestHttp,
			OktaResponse.class);
			oktaResponse = response != null ? response.getBody() : null;
			logger.info("okta response communications API ::{}",oktaResponse);
		} catch (RestClientException ex) {
			logger.error("failed to get communications API okta token");
			ex.printStackTrace();
		}
		return oktaResponse != null ? oktaResponse.getAccess_token() : null;

	}

	public String generateTokenProspr() {
		
		OktaResponse oktaResponse = null;
		try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
				logger.info("getOktaAuthToken: Initiating rest template");
			}
			String clientAuthKey = "Basic "
					+ Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
            logger.info("client authKey ::{}", clientAuthKey);
			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", clientAuthKey);
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			header.add("Accept", "application/json");
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", grantType);
			body.add("scope", scope);
			HttpEntity<MultiValueMap<String, String>> requestHttp = new HttpEntity<>(body, header);
            logger.info("request http ::{}",requestHttp);
			logger.info("tokenEndpoint ::{}",tokenEndpoint);
			ResponseEntity<OktaResponse> response = restTemplate.postForEntity(tokenEndpoint, requestHttp,
			OktaResponse.class);
			oktaResponse = response !=null ? response.getBody() : null;
			logger.info("okta response ::{}",oktaResponse);
		} catch (RestClientException ex) {
			logger.error("failed to get okta token");
			ex.printStackTrace();
		}
		return oktaResponse != null ? oktaResponse.getAccess_token() : null;

	}
}
