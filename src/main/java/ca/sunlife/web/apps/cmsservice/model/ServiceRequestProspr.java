package ca.sunlife.web.apps.cmsservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.regex.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import ca.sunlife.web.apps.cmsservice.controller.CmsServiceController;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.ServiceParam;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.EmailConfig;

@Component
public class ServiceRequestProspr extends ServiceRequest{
	private static final Logger logger = LogManager.getLogger(ServiceRequestProspr.class);
	
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
	
	@Value("${kafka.producer.endpoint}")
    private String kafkaProducerEndpoint;
	
    @Value("${mail.slf.fromaddress}")
    private String fromAddress;
	@Value("${mail.slf.fromtext}")
    private String fromText;
    @Value("${mail.slf.subject}")
    private String subject;
    @Value("${mail.slf.body}")
    private String body;
    @Value("${mail.slf.toaddress}")
    private String toAddress;
    @Value("${mail.slf.ccaddress}")
    private String ccAddress;
    @Value("${mail.slf.bccaddress}")
    private String bccAddress;
	
//	private String serviceName;
	private String serviceURI;
	private String authKey;

	@Override
	public void init() {
		setServiceFileName("ServiceRequestProspr.json");
		super.init();

		emailConfig.setFromAddress(fromAddress);
		emailConfig.setFromText(fromText);
		emailConfig.setToAddress(toAddress);
		emailConfig.setCcAddress(ccAddress);
		emailConfig.setBccAddress(bccAddress);
		emailConfig.setSubject(subject);
		emailConfig.setBody(body);
	}

	@Override
	public Map<String,Object> serviceValidation(Map<String,Object> inputs) {
		Map<String,Object> hm = super.serviceValidation(inputs);
		
		if (hm != null) {
			String l = (String)hm.get("LeadSource");
			
			System.out.println("Leadsource: " + hm.get("leadSource"));
			System.out.println("Leadsource: " + l);
			
			hm.put("QuickStart", hm.get("LeadSource") == null ? false : hm.get("LeadSource").toString().indexOf("QuickStart") > -1);
			hm.put("Leadid", hm.get("LeadSource") + ServiceUtil.generateUniqueId());
		}
		
		return hm;
	}
	
	@Override
	public CmsResponse sendData(Map<String,Object> inputValues) {
		CmsResponse response = null;
		
		String token = super.getToken(tokenEndpoint, scope, clientId, clientSecret);
		if (token != null) {
			HttpHeaders header = apiGatewayService.buildHttpHeader(token, MediaType.APPLICATION_JSON_VALUE, "1", "2", "3");
			try {
				response = apiGatewayService.sendData(inputValues, header, kafkaProducerEndpoint);
			} catch (Exception e) {
				logger.info(e);
			}
		}
		
		if (response != null) {
        	if(response.getStatusCode() == 201) {
        		response.setStatusCode(201);
        		response.setMessage("data successfully submitted");
        	}else if(response.getStatusCode() == 401) {
        		response.setStatusCode(401);
        		response.setMessage("Unauthorized");        	
        	}else if(response.getStatusCode() == 400) {
        		response.setStatusCode(400);
        		response.setMessage("Api responded with 400!");            		
            }else if(response.getStatusCode() == 500) {
            	response.setStatusCode(500);
            	response.setMessage("Api responded with 500!");	                 
            }else if(response.getStatusCode() == 503) {
            	response.setStatusCode(503);
            	response.setMessage("Service Unavailable");	                 
            }else {
            	response.setMessage("Something went wrong!");	
            }
        } else {
        	response = new CmsResponse();
        	response.setMessage(token != null ? "Unable to connect host!" : "Something went wrong!  URL not valid");
        	response.setStatusCode(500);
            logger.error(response.getMessage());
            try {
            	emailService.sendEmailProspr(inputValues);
            } catch (Exception e) {
            	logger.error("Error sending email: " + e.getMessage());
            }
        }
		return response;
	}

	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	
	public String getTokenClientId() {
		return clientId;
	}
	
	public String getTokenClientSecret() {
		return clientSecret;
	}
	
	public String getTokenScope() {
		return scope;
	}

	@Override
	public void setServiceFileName(String filename) {
		serviceFileName = filename;
	}

}
