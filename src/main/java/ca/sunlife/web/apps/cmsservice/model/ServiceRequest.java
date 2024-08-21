package ca.sunlife.web.apps.cmsservice.model;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;
import java.util.Iterator;
import java.util.Map;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.util.ServiceConstants;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.service.EmailService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract public class ServiceRequest {
	public static final Logger logger = LogManager.getLogger(ServiceRequest.class);
	
	protected String serviceName;
	protected String serviceFileName;
	protected Set<ServiceParam> serviceParams;
	
	protected String tokenEndpoint;
	protected String clientId;
	protected String clientSecret;
	protected String scope;
    protected String serviceEndpoint;

	@Value("${spring.profiles.active:dev}")
	private String activeProfile;

	@Autowired
    OktaTokenGenerator oktaTokenGenerator;

	@Autowired
	ApiGatewayService apiGatewayService;

	@Autowired
    EmailConfig emailConfig;
	
    @Autowired
    EmailService emailService;
	
	public void ServiceRequest() {}
		
	public void init() {

		System.out.println("activeProfile: " + activeProfile);
		logger.info("activeProfile: {}", activeProfile);

		try {
			InputStream is = ServiceUtil.readServiceFile(this, this.getServiceFileName());
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonMap = mapper.readTree(is);
			this.serviceName = jsonMap.get("serviceName").textValue();

			JsonNode params = jsonMap.get("params");
			serviceParams = processParams(params);
			
			tokenEndpoint = getJsonNodeValue(jsonMap, "okta.oauth2.endpoint");
			clientId = getJsonNodeValue(jsonMap, "okta.oauth2.client.id");
			clientSecret = getJsonNodeValue(jsonMap, "okta.oauth2.client.secret");
			scope = getJsonNodeValue(jsonMap, "okta.oauth2.scope");
			serviceEndpoint = getJsonNodeValue(jsonMap, "serviceURI");

			emailConfig.setFromAddress(getJsonNodeValue(jsonMap, "mail.slf.fromaddress"));
			emailConfig.setFromText(getJsonNodeValue(jsonMap, "mail.slf.fromtext"));
			emailConfig.setToAddress(getJsonNodeValue(jsonMap, "mail.slf.toaddress"));
			emailConfig.setCcAddress(getJsonNodeValue(jsonMap, "mail.slf.ccaddress"));
			emailConfig.setBccAddress(getJsonNodeValue(jsonMap, "mail.slf.bccaddress"));
			emailConfig.setSubject(getJsonNodeValue(jsonMap, "mail.slf.subject"));
			emailConfig.setBody(getJsonNodeValue(jsonMap, "mail.slf.body"));

			logger.info(jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}	
	
	private HashSet<ServiceParam> processParams(JsonNode definedParams) {
		HashSet<ServiceParam> hs = new HashSet();
		
		for (JsonNode param : definedParams) {
			ServiceParam sp = new ServiceParam();
			
			try {
				sp.setParamName(param.get("paramIn").textValue());
				sp.setParamRequired(param.get("paramRequired").booleanValue());
				sp.setRegex(param.get("paramRegex").textValue());
				sp.setOutputName(param.get("paramOut").textValue());
				
				hs.add(sp);
				//System.out.print("paramIn: " + param.get("paramIn").textValue() + "; ");
			} catch (Exception e) {
				//logger.info(e + ":" + param);
				System.out.println(e + ": " + e.getMessage());
			}
		}
		System.out.println("defined params: " + hs.toArray());
		return hs;
	}

	private String getJsonNodeValue(JsonNode jsonMap, String nodeName) {
		logger.info("envProfile: {}", activeProfile);

		JsonNode env = jsonMap.get(activeProfile);
		if (env != null) {
			JsonNode node = env.get(nodeName);
			if (node != null) {
				return node.textValue();
			}
		}
		logger.info("Unable to find -{}- or -{}- node for: {} service", activeProfile, nodeName, serviceName);
		return null;
	}
	
	public String getServiceFileName() {
		return serviceFileName;
	}
	
	public void setServiceFileName(String filename) {
		this.serviceFileName = filename ;
	}
	
    public String getServiceName() {
    	return serviceName;
    }

    public void setServiceName(String serviceName) {
    	this.serviceName = serviceName;
    }
    
    public Set<ServiceParam> getServiceParams() {
    	return serviceParams;
    }

	public void setServiceParams(Set<ServiceParam> serviceParams) {
		this.serviceParams = serviceParams;
	}
    
    public CmsResponse sendData(Map<String,Object> inputValues) {
    	CmsResponse cmsResponse = null;
    	
    	return cmsResponse;
    	
    }
   	
    public Map<String,Object> serviceValidation(Map<String,Object> inputValues) {
		Map<String,Object> validInput = new HashMap<String,Object>();
		System.out.println("serviceName: " + serviceName);
		System.out.println("input: " + inputValues);
		System.out.println("serviceParams: " + getServiceParams());

		for (ServiceParam param : serviceParams) {
			String paramKey = param.getParamName();
			String inputValue = null;
			
			inputValue = (String)inputValues.get(paramKey);
		
			if (inputValue == null) {
				// if required and null, reject submission 
				if (param.getParamRequired()) {
					validInput = null;
					logger.info("missing required input param: {}", paramKey);
					System.out.println("service: " + serviceName + "::required param is null: " + paramKey);
					break;
				}
			} else {
				if (param.getParamRegex().equals("")) {
					logger.info("service: {}. Input validation bypassed for param: {} :: {}", serviceName, paramKey, inputValue);
				} else {
					if (ServiceUtil.passRegex(param.getParamRegex(), inputValue)) {
						validInput.put(param.getOutputName(), inputValue);
					} else {
						validInput = null;
						logger.info("Service: {}::param FAILED VALIDATION: {} :: {} --- exiting", serviceName, paramKey, inputValue);
						System.out.println("Service: " + serviceName + " >> param FAILED VALIDATION: " + paramKey + "::" + inputValue + " --- exiting");
						break;
					}
				}
			}
		}
		System.out.println("Validated inputs: " + validInput);
		return validInput;
    }

	protected String getToken(String tokenEndpoint, String scope, String clientId, String clientSecret) {
		String token = oktaTokenGenerator.generateToken(tokenEndpoint, scope, clientId, clientSecret);

		token = "dummyValue";
		
		if (token == null) {
			logger.error("{} service failed to generate token", serviceName);
		}

		return token;
	}
    
}