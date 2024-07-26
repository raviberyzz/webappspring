package ca.sunlife.web.apps.cmsservice.service;

import javax.mail.MessagingException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestCommunication;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFaa;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.restclient.KafkaClient;
import ca.sunlife.web.apps.cmsservice.restclient.SalesforceClient;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.EmailConfig;


@Service
public class ApiGatewayServiceImpl implements ApiGatewayService {

	@Autowired
    OktaTokenGenerator oktaTokenGenerator;

    @Value("${okta.oauth2.endpoint}")
	private String tokenEndpointProspr;

	@Value("${okta.oauth2.client.id}")
	private String clientIdProspr;

	@Value("${okta.oauth2.client.secret}")
	private String clientSecretProspr;

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
	
	@Value("${kafka.producer.endpoint}")
    private String kafkaProducerEndpoint;
    
	@Value("${kafka.producer.endpoint.faa}")
    private String kafkaProducerEndpointFaa;
	
	@Value("${kafka.endpoint.communication}")
    private String kafkaEndpointCommunication;
	
	@Autowired
    EmailConfig emailConfig;
	
    @Autowired
    EmailService emailService;
    
    @Autowired
    SalesforceClient salesforceClient;
    
    @Autowired
    KafkaClient kafkaClient;
	
    private static final Logger logger = LogManager.getLogger(ApiGatewayServiceImpl.class);
    
    @Override
    public CmsResponse sendData(Map<String,Object> data, HttpHeaders header, String endpoint) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        String dataString = ServiceUtil.getJsonString(data);
        HttpEntity<String> request = new HttpEntity<>(dataString, header);
        cmsResponse = kafkaClient.postData(request, endpoint);

        return cmsResponse;
    }

    @Override
    public CmsResponse sendData(ServiceRequestProspr data) throws JsonProcessingException {
    	CmsResponse cmsResponse = null;
    	
    	//Map<String,Object> dataMap = ServiceUtil.getLeadJsonMap(data);
    	Map<String,Object> dataMap = null;
    	HttpHeaders header = new HttpHeaders();
    	cmsResponse = sendDataProspr(dataMap, header);
    	
    	return cmsResponse;
    }
    
    @Override
    public CmsResponse sendDataProspr(ServiceRequestProspr data) throws JsonProcessingException {
    	CmsResponse cmsResponse = null;
    	String token = oktaTokenGenerator.generateToken(tokenEndpointProspr, scope, clientIdProspr, clientSecretProspr);

    	//Map<String,Object> dataMap = ServiceUtil.getLeadJsonMap(data);
    	Map<String,Object> dataMap = null;
    	HttpHeaders header = new HttpHeaders();
    	cmsResponse = sendDataProspr(dataMap, header);
    	
    	return cmsResponse;
    }

    @Override
    //public CmsResponse sendData(ServiceRequest data) throws JsonProcessingException {
    public CmsResponse sendDataProspr(Map<String,Object> data, HttpHeaders header) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        
        String dataString = ServiceUtil.getJsonString(data);
        
        //String token = oktaTokenGenerator.generateTokenProspr();
        
        //logger.info("token::{}",token);
        //if (token != null) {
        	//HttpHeaders header = buildHttpHeader(token);
            /*HttpHeaders header = new HttpHeaders();
            header.add("Authorization", "Bearer "+token);
            header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            header.add("x-auth-token", "1");
            header.add("x-traceability-id", "2");
            header.add("x-correlation-id", "3");
            */
            //HttpEntity<String> request = new HttpEntity<>(ServiceUtil.getLeadJsonString(data), header);
            HttpEntity<String> request = new HttpEntity<>(dataString, header);
            //cmsResponse = kafkaClient.postData(request);
            cmsResponse = kafkaClient.postData(request, kafkaProducerEndpoint );
        //}

            /*
		if (cmsResponse != null) {
        	if(cmsResponse.getStatusCode() == 201) {
        		 cmsResponse.setStatusCode(201);
                 cmsResponse.setMessage("data successfully submitted");
        	}else if(cmsResponse.getStatusCode() == 401) {
       		 cmsResponse.setStatusCode(401);
             cmsResponse.setMessage("Unauthorized");        	
        	}else if(cmsResponse.getStatusCode() == 400) {
          		 cmsResponse.setStatusCode(400);
                 cmsResponse.setMessage("Api responded with 400!");            		
            }else if(cmsResponse.getStatusCode() == 500) {
          		 cmsResponse.setStatusCode(500);
                 cmsResponse.setMessage("Api responded with 500!");	                 
            }else if(cmsResponse.getStatusCode() == 503) {
          		 cmsResponse.setStatusCode(503);
                 cmsResponse.setMessage("Service Unavailable");	                 
            }else {
                 cmsResponse.setMessage("Something went wrong!");	
            }
        } else {
            cmsResponse = new CmsResponse();
            cmsResponse.setMessage(token != null ? "Unable to connect host!" : "Something went wrong!  URL not valid");
            cmsResponse.setStatusCode(500);
            logger.error(cmsResponse.getMessage());
            sendEmail(data);
        }
*/
        return cmsResponse;
    }
    
    @Override
    public CmsResponse sendDataFaa(ServiceRequestFaa data) throws JsonProcessingException {
    	CmsResponse cmsResponse = null;
    	
    	//Map<String,Object> dataMap = ServiceUtil.getFaaLeadJsonMap(data);
    	Map<String,Object> dataMap = null;
    	HttpHeaders header = new HttpHeaders();
    	cmsResponse = sendDataFaa(dataMap, header);
    	
    	return cmsResponse;
    }
    
    @Override
    public CmsResponse sendDataFaa(Map<String,Object> data, HttpHeaders header) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        
        String dataString = ServiceUtil.getJsonString(data);
        
        //String token = oktaTokenGenerator.generateTokenFaa();
        //String token = oktaTokenGenerator.generateToken(tokenEndpointfaa, scopefaa, clientIdfaa, clientSecretfaa);
        //logger.info("token::{}",token);
        //if (token != null) {
        	//HttpHeaders header = buildHttpHeader(token);
        	/*
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", "Bearer "+token);
            header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            header.add("x-auth-token", "1");
            header.add("x-traceability-id", "2");
            header.add("x-correlation-id", "3");
            */
            HttpEntity<String> request = new HttpEntity<>(dataString, header);
            //cmsResponse = kafkaClient.postDataFaa(request);
            cmsResponse = kafkaClient.postData(request, kafkaProducerEndpointFaa);        
        //}
        /*
        if(cmsResponse != null) {
        	
        	String currentMessage = cmsResponse.getMessage();
        	if(cmsResponse.getStatusCode() == 200) {
                 cmsResponse.setMessage("data successfully submitted");
        	}else if(cmsResponse.getStatusCode() == 401) {
	             cmsResponse.setMessage("Unauthorized Access to API, API Response: " + currentMessage);
	             sendFaaEmail("The following FAA Lead was not submitted due to a status 401 authentication issue between the middleware and Leads API.<br/><br/>API Response: " + currentMessage, data);
        	}else if(cmsResponse.getStatusCode() == 400) {
                 cmsResponse.setMessage("Api responded with status 400! API rejected lead details due to failed validation, API Response: " + currentMessage);            		
                 sendFaaEmail("The following FAA Lead was not submitted due to a status 400 rejection from Leads API, the lead failed API validation.<br/><br/>API Response: " + currentMessage, data);
        	}else if(cmsResponse.getStatusCode() == 500) {
                 cmsResponse.setMessage("Api responded with status 500! Api service unavailable, API response: " + currentMessage);	                 
                 sendFaaEmail("The following FAA Lead was not submitted due to a status 500 response from the API, this means that Leads API is down.<br/><br/>API Response: " + currentMessage, data);
            }else {
            	cmsResponse.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
            	cmsResponse.setStatusCode(500);
            	sendFaaEmail(token != null ? "The following FAA Lead was not submitted due to a connection issue between the middleware and Leads API, Okta authentication was successful but connection from middleware to Leads API was not."
        				: "The following FAA Lead was not submitted due to an Okta authentication issue in the middleware, no Okta token was generated.", data);
            }
        }else {
        	cmsResponse = new CmsResponse();
            cmsResponse.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
            cmsResponse.setStatusCode(500);
            logger.error(cmsResponse.getMessage());
            sendFaaEmail(token != null ? "The following FAA Lead was not submitted due to a connection issue between the middleware and Leads API, Okta authentication was successful but connection from middleware to Leads API was not."
            				: "The following FAA Lead was not submitted due to an Okta authentication issue in the middleware, no Okta token was generated.", data);
        }
	*/
        return cmsResponse;
    }
    
    @Override
    public CmsResponse sendDataCommunication(ServiceRequestFaa data) throws JsonProcessingException {
    	CmsResponse cmsResponse = null;
    	
    	//Map<String,Object> dataMap = ServiceUtil.getCommunicationJsonMap(data);
    	Map<String,Object> dataMap = null;
        HttpHeaders header = new HttpHeaders();
    	cmsResponse = sendDataCommunication(dataMap, header);
    	
    	return cmsResponse;    	
    }
    
    @Override
    public CmsResponse sendDataCommunication(Map<String,Object> data, HttpHeaders header) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        
        String dataString = ServiceUtil.getJsonString(data);
        
        //String token = oktaTokenGenerator.generateTokenCommunication();
        //String token = oktaTokenGenerator.generateToken(tokenEndpointCommunication, scopefaa, clientIdCommunication, clientSecretCommunication);
        //logger.info("token::{}",token);
        //if (token != null) {
        /*
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", "Bearer "+token);
            header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            header.add("x-traceability-id", "2");
//            header.add("x-correlation-id", "3");
         */
            HttpEntity<String> request = new HttpEntity<>(dataString, header);
            //cmsResponse = kafkaClient.postDataCommunication(request);        
            cmsResponse = kafkaClient.postData(request,kafkaEndpointCommunication);
        //}
        
            /*
        if(cmsResponse != null) {
        	
        	String currentMessage = cmsResponse.getMessage();
        	if(cmsResponse.getStatusCode() == 200) {
                 cmsResponse.setMessage("data successfully submitted");
        	}else if(cmsResponse.getStatusCode() == 401) {
	             cmsResponse.setMessage("Unauthorized Access to API, API Response: " + currentMessage);
        	}else if(cmsResponse.getStatusCode() == 400) {
                 cmsResponse.setMessage("Api responded with status 400! API rejected lead details due to failed validation, API Response: " + currentMessage);
        	}else if(cmsResponse.getStatusCode() == 500) {
                 cmsResponse.setMessage("Api responded with status 500! Api service unavailable, API response: " + currentMessage);	                 
            }else {
            	cmsResponse.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
            	cmsResponse.setStatusCode(500);
            }
        }else {
        	cmsResponse = new CmsResponse();
            cmsResponse.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
            cmsResponse.setStatusCode(500);
            logger.error(cmsResponse.getMessage());
        }
        */
        return cmsResponse;
    }
    
    //private void sendFaaEmail(String body, FaaServiceRequest data) {
    private void sendFaaEmail(String body, Map<String,Object> data) {
    	try {
        	emailConfig.setBodyFaa(body);
            emailService.sendEmailFaa(data);
            logger.info("Email sent successfully");
        }catch(MessagingException ex) {
        	logger.error("Email failed: {}", ex.getMessage());
        }        
    }
   /*
    private boolean authenticateToken() {
        CmsResponse cmsResponse = null;
        String token = oktaTokenGenerator.generateTokenProspr();

        if (token != null) {
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);
            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            header.add("Accept", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<String> request = new HttpEntity<>(header);
            
            cmsResponse = salesforceClient.validateToken(request);    
        }
        return cmsResponse != null && cmsResponse.getStatusCode() == 200;
    }
    */
    
    public HttpHeaders buildHttpHeader(String token, String mediaType, String xauth, String xtraceability, String xcorrelation) {
    	HttpHeaders header = new HttpHeaders();
    	
		if ((token != null) && (!token.isEmpty())) {	
        	header.add("Authorization", "Bearer " + token);
		}
		if ((mediaType != null) && (!mediaType.isEmpty())) {
			header.add("Content-Type", mediaType);
		}
        if ((xauth != null) && (!xauth.isEmpty())) {
			header.add("x-auth-token", xauth);
		}
		if ((xtraceability != null) && (!xtraceability.isEmpty())) {
			header.add("x-traceability-id", xtraceability);
		}
		if ((xcorrelation != null) && (!xcorrelation.isEmpty())) {
			header.add("x-correlation-id", xcorrelation);
		}
		
		return header;    	
	}  
    
	//private void sendEmail(ServiceRequest data) {
    public void sendEmail(Map<String,Object> data) {
        try {
            emailService.sendEmail("",data);
            logger.info("Email sent successfully");
        }catch(MessagingException ex) {
        	logger.error("Email failed: {}", ex.getMessage());
        }        
    }
}
