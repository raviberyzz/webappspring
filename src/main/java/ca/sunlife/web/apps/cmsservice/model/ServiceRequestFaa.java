package ca.sunlife.web.apps.cmsservice.model;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

@Component
public class ServiceRequestFaa extends ServiceRequest{
	private static final Logger logger = LogManager.getLogger(ServiceRequestFaa.class);
	
	@Value("${okta.oauth2.endpoint.faa}")
	private String tokenEndpoint;

	@Value("${okta.oauth2.client.id.faa}")
	private String clientId;

	@Value("${okta.oauth2.client.secret.faa}")
	private String clientSecret;
	
	@Value("${okta.oauth2.scope.faa}")
	private String scope;

	@Value("${kafka.producer.endpoint.faa}")
    private String kafkaProducerEndpointFaa;
	
    @Value("${mail.slf.fromaddress}")
    private String fromAddress;
	@Value("${mail.slf.fromtext.faa}")
    private String fromText;
	@Value("${mail.slf.subject.faa}")
    private String subject;
    @Value("${mail.slf.body.faa}")
    private String body;
    @Value("${mail.slf.toaddress.faa}")
    private String toAddress;
    @Value("${mail.slf.ccaddress.faa}")
    private String ccAddress;
    @Value("${mail.slf.bccaddress.faa}")
    private String bccAddress;
	
	private String serviceName;
	private String serviceURI;
	private String authKey;
	

	@Override
	public void init() {
		setServiceFileName("ServiceRequestFaa.json");
		super.init();

		emailConfig.setFromAddress(fromAddress);
		emailConfig.setFromText(fromText);
		emailConfig.setToAddress(toAddress);
		emailConfig.setCcAddress(ccAddress);
		emailConfig.setBccAddress(bccAddress);
		emailConfig.setSubject(subject);
		emailConfig.setBody(body);
	}
		
/*
	public Set<ServiceParam> getServiceParams() {
		return serviceParams;
	}
	*/

	@Override
	public Map<String,Object> serviceValidation(Map<String,Object> inputs) {
		Map<String,Object> hm = super.serviceValidation(inputs);
		
		return inputs;
	}
	
	@Override
	public CmsResponse sendData(Map<String,Object> inputValues) {
		CmsResponse response = null;
		
		String token = oktaTokenGenerator.generateToken(tokenEndpoint, scope, clientId, clientSecret);
		try {
			HttpHeaders header = apiGatewayService.buildHttpHeader(token, MediaType.APPLICATION_JSON_VALUE, "1", "2", "3");
			response = apiGatewayService.sendData(inputValues, header, kafkaProducerEndpointFaa);
		} catch (Exception e) {
			logger.info(e);
		}

		try {
	        if(response != null) {
	        	String currentMessage = response.getMessage();
	        	if(response.getStatusCode() == 200) {
	                 response.setMessage("data successfully submitted");
	        	}else if(response.getStatusCode() == 401) {
		             response.setMessage("Unauthorized Access to API, API Response: " + currentMessage);
		             //emailService.sendFaaEmail("The following FAA Lead was not submitted due to a status 401 authentication issue between the middleware and Leads API.<br/><br/>API Response: " + currentMessage, inputValues);
		             emailConfig.setBody("The following FAA Lead was not submitted due to a status 401 authentication issue between the middleware and Leads API.<br/><br/>API Response: " + currentMessage);
		             emailService.sendEmail(emailConfig, inputValues);
	        	}else if(response.getStatusCode() == 400) {
	                 response.setMessage("Api responded with status 400! API rejected lead details due to failed validation, API Response: " + currentMessage);            		
	                 //emailService.sendFaaEmail("The following FAA Lead was not submitted due to a status 400 rejection from Leads API, the lead failed API validation.<br/><br/>API Response: " + currentMessage, inputValues);
	                 emailConfig.setBody("The following FAA Lead was not submitted due to a status 400 rejection from Leads API, the lead failed API validation.<br/><br/>API Response: " + currentMessage);
	                 emailService.sendEmail(emailConfig, inputValues);
	        	}else if(response.getStatusCode() == 500) {
	                 response.setMessage("Api responded with status 500! Api service unavailable, API response: " + currentMessage);	                 
	                 //emailService.sendFaaEmail("The following FAA Lead was not submitted due to a status 500 response from the API, this means that Leads API is down.<br/><br/>API Response: " + currentMessage, inputValues);
	                 emailConfig.setBody("The following FAA Lead was not submitted due to a status 500 response from the API, this means that Leads API is down.<br/><br/>API Response: " + currentMessage);
	                 emailService.sendEmail(emailConfig, inputValues);
	            }else {
	            	response.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
	            	response.setStatusCode(500);
	            	/* emailService.sendFaaEmail(token != null ? "The following FAA Lead was not submitted due to a connection issue between the middleware and Leads API, Okta authentication was successful but connection from middleware to Leads API was not."
	        				: "The following FAA Lead was not submitted due to an Okta authentication issue in the middleware, no Okta token was generated.", inputValues);
	        				*/
	            	emailConfig.setBody(token != null ? "The following FAA Lead was not submitted due to a connection issue between the middleware and Leads API, Okta authentication was successful but connection from middleware to Leads API was not."
	            			: "The following FAA Lead was not submitted due to an Okta authentication issue in the middleware, no Okta token was generated.");
	            	emailService.sendEmail(emailConfig, inputValues);
	            }
	        }else {
	        	response = new CmsResponse();
	            response.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
	            response.setStatusCode(500);
	            logger.error(response.getMessage());
	            /*
	            emailService.sendFaaEmail(token != null ? "The following FAA Lead was not submitted due to a connection issue between the middleware and Leads API, Okta authentication was successful but connection from middleware to Leads API was not."
	            				: "The following FAA Lead was not submitted due to an Okta authentication issue in the middleware, no Okta token was generated.", inputValues);
	            */
	            emailConfig.setBody(token != null ? "The following FAA Lead was not submitted due to a connection issue between the middleware and Leads API, Okta authentication was successful but connection from middleware to Leads API was not."
	            				: "The following FAA Lead was not submitted due to an Okta authentication issue in the middleware, no Okta token was generated.");
	            emailService.sendEmail(emailConfig, inputValues);
	        }
		} catch (Exception e) {
			logger.error(e);
		}
			
	
		return response;
	}

}
