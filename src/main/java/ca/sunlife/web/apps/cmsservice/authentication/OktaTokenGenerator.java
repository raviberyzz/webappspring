package ca.sunlife.web.apps.cmsservice.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
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
	
	@Value("${okta.oauth2.conectionTimeout}")
	private int oktaConnectionTimeout;
	
	@Value("${okta.oauth2.oktaReadTimeout}")
	private int oktaReadTimeout;
	
	RestTemplate restTemplate;
	
	private static final Logger logger = LogManager.getLogger(OktaTokenGenerator.class);

	public String generateToken() {
		OktaResponse oktaResponse = null;
		try {
			if(restTemplate == null){
				restTemplate = initializeRestTemplate();
				logger.info("getOktaAuthToken: Initiating rest template");
			}
			String clientToken = "Basic "
					+ Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
            logger.info("client token ::{}", clientToken);
			HttpHeaders header = new HttpHeaders();
			header.add("Authorization", clientToken);
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
			ex.printStackTrace();
		}
		return oktaResponse != null ? oktaResponse.getAccess_token() : null;

	}
	public RestTemplate initializeRestTemplate() {
	try {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(oktaConnectionTimeout);
		httpRequestFactory.setReadTimeout(oktaReadTimeout);
		httpRequestFactory.setConnectionRequestTimeout(oktaConnectionTimeout);
		restTemplate = new RestTemplate(httpRequestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		} catch (RestClientException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restTemplate;
	}
}
