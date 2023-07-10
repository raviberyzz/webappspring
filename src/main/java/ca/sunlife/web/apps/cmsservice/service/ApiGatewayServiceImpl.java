package ca.sunlife.web.apps.cmsservice.service;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.FaaServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.restclient.KafkaClient;
import ca.sunlife.web.apps.cmsservice.restclient.SalesforceClient;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;


@Service
public class ApiGatewayServiceImpl implements ApiGatewayService {


	@Autowired
    OktaTokenGenerator oktaTokenGenerator;
    
    @Autowired
    EmailService emailService;
    
    @Autowired
    SalesforceClient salesforceClient;
    
    @Autowired
    KafkaClient kafkaClient;
	
    private static final Logger logger = LogManager.getLogger(ApiGatewayServiceImpl.class);

    @Override
    public CmsResponse sendData(ServiceRequest data) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        String token = oktaTokenGenerator.generateToken();
        logger.info("token::{}",token);
        if (token != null) {
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", "Bearer "+token);
            header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            header.add("x-auth-token", "1");
            header.add("x-traceability-id", "2");
            header.add("x-correlation-id", "3");
            HttpEntity<String> request = new HttpEntity<>(ServiceUtil.getLeadJsonString(data), header);
            cmsResponse = kafkaClient.postData(request);
        }
        
        if(cmsResponse != null) {
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
        }else {
            cmsResponse = new CmsResponse();
            cmsResponse.setMessage(token != null ? "Unable to connect host!" : "Something went wrong!  URL not valid");
            cmsResponse.setStatusCode(500);
            logger.error(cmsResponse.getMessage());
            sendEmail(data);
        }        
        return cmsResponse;
    }
    
    @Override
    public CmsResponse sendDataFaa(FaaServiceRequest data) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        String token = oktaTokenGenerator.generateTokenFaa();
        logger.info("token::{}",token);
        if (token != null) {
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", "Bearer "+token);
            header.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            header.add("x-auth-token", "1");
            header.add("x-traceability-id", "2");
            header.add("x-correlation-id", "3");
            HttpEntity<String> request = new HttpEntity<>(ServiceUtil.getFaaLeadJsonString(data), header);
            cmsResponse = kafkaClient.postDataFaa(request);        
        }
        
        if(cmsResponse != null) {
            cmsResponse.setMessage("data successfully submitted");
            logger.info(cmsResponse.getMessage());
        }else {
            cmsResponse = new CmsResponse();
            cmsResponse.setMessage(token != null ? "Something went wrong!" : "Something went wrong!  URL not valid");
            cmsResponse.setStatusCode(500);
            logger.error(cmsResponse.getMessage());
        }        
        return cmsResponse;
    }
   
    private boolean authenticateToken() {
        CmsResponse cmsResponse = null;
        String token = oktaTokenGenerator.generateToken();

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

	private void sendEmail(ServiceRequest data) {
        try {
            emailService.sendEmail(data);
            logger.info("Email sent successfully");
        }catch(MessagingException ex) {
        	logger.error("Email failed: {}", ex.getMessage());
        }        
    }
	


}
