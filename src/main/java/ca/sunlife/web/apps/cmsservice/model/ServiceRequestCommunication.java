package ca.sunlife.web.apps.cmsservice.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;

@Component
public class ServiceRequestCommunication extends ServiceRequest {
	private static final Logger logger = LogManager.getLogger(ServiceRequestCommunication.class);
	
/*	
	@Value("${okta.oauth2.scope.faa}")
	private String scope;
	@Value("${okta.oauth2.endpoint.communication}")
	private String tokenEndpoint;
	@Value("${okta.oauth2.client.id.communication}")
	private String clientId;
	@Value("${okta.oauth2.client.secret.communication}")
	private String clientSecret;

	@Value("${kafka.endpoint.communication}")
    private String kafkaEndpointCommunication;

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
   
*/
	private String emailAddress;
	private String templateId;
	private String language;
	private String firstName;
	private String coverageAmount;
	private String costRange;
	private String incomeReplacement;
	private String debtPayment;
	private String additionalExpense;
	private String totalEstimate;
	
	@Override
	public void init() {
		setServiceFileName("ServiceRequestCommunication.json");
		super.init();

/*
		emailConfig.setFromAddress(fromAddress);
		emailConfig.setFromText(fromText);
		emailConfig.setToAddress(toAddress);
		emailConfig.setCcAddress(ccAddress);
		emailConfig.setBccAddress(bccAddress);
		emailConfig.setSubject(subject);
		emailConfig.setBody(body);
*/
	}
	
	@Override
	public Map<String,Object> serviceValidation(Map<String,Object> inputs) {
		//Map<String,Object> map = restructureData(super.serviceValidation(inputs));
		Map<String,Object> map = super.serviceValidation(inputs);
		
		return map;
	}
	
	@Override
	public CmsResponse sendData(Map<String,Object> inputValues) {
		CmsResponse response = null;

		String token = oktaTokenGenerator.generateToken(tokenEndpoint, scope, clientId, clientSecret);
		try {
			HttpHeaders header = apiGatewayService.buildHttpHeader(token, MediaType.APPLICATION_JSON_VALUE, null, "2", null);
			response = apiGatewayService.sendData(inputValues, header, serviceEndpoint);
		} catch (Exception e) {
			logger.info(e);
		}

        if(response != null) {
        	
        	String currentMessage = response.getMessage();
        	if(response.getStatusCode() == 200) {
                 response.setMessage("data successfully submitted");
        	}else if(response.getStatusCode() == 401) {
	             response.setMessage("Unauthorized Access to API, API Response: " + currentMessage);
        	}else if(response.getStatusCode() == 400) {
                 response.setMessage("Api responded with status 400! API rejected lead details due to failed validation, API Response: " + currentMessage);
        	}else if(response.getStatusCode() == 500) {
                 response.setMessage("Api responded with status 500! Api service unavailable, API response: " + currentMessage);	                 
            }else {
            	response.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
            	response.setStatusCode(500);
            }
        }else {
        	response = new CmsResponse();
            response.setMessage(token != null ? "Something went wrong after Okta Authentication!" : "Something went wrong with Okta Authentication!  URL not valid");
            response.setStatusCode(500);
            logger.error(response.getMessage());
        }
		return response;
	}

	/* communications api requires a specific input structure */
	private Map<String,Object> restructureData(Map<String,Object> data) {
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, Object> emailMap = new HashMap<>();
		Map<String, Object> additionalAttributes = new HashMap<>();

		additionalAttributes.put("firstName", data.get("firstName"));
		additionalAttributes.put("coverageAmount", data.get("coverageAmount"));
		additionalAttributes.put("costRange", data.get("costRange"));
		additionalAttributes.put("incomeReplacement", data.get("incomeReplacement"));
		additionalAttributes.put("debtPayment", data.get("debtPayment"));
		additionalAttributes.put("additionalExpense", data.get("additionalExpense"));
		additionalAttributes.put("totalEstimate", data.get("totalEstimate"));

		emailMap.put("additionalAttributes", additionalAttributes);

		emailMap.put("emailAddress", data.get("emailAddress"));
		emailMap.put("templateId", data.get("templateId"));
		emailMap.put("language", data.get("language") == "French" ? "F" : "E");
		
		map.put("Email", emailMap);

		return map;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(String coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public String getCostRange() {
		return costRange;
	}

	public void setCostRange(String costRange) {
		this.costRange = costRange;
	}

	public String getIncomeReplacement() {
		return incomeReplacement;
	}

	public void setIncomeReplacement(String incomeReplacement) {
		this.incomeReplacement = incomeReplacement;
	}

	public String getDebtPayment() {
		return debtPayment;
	}

	public void setDebtPayment(String debtPayment) {
		this.debtPayment = debtPayment;
	}

	public String getAdditionalExpense() {
		return additionalExpense;
	}

	public void setAdditionalExpense(String additionalExpense) {
		this.additionalExpense = additionalExpense;
	}

	public String getTotalEstimate() {
		return totalEstimate;
	}

	public void setTotalEstimate(String totalEstimate) {
		this.totalEstimate = totalEstimate;
	}

	
}
