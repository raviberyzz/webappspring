package ca.sunlife.web.apps.cmsservice.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;


public class ServiceUtil {
	
	private static final Logger logger = LogManager.getLogger(ServiceUtil.class);
	
	public static String getJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
	
	public static String getSalesForceJsonString(ServiceRequest serviceRequest) throws JsonProcessingException {
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("FirstName", serviceRequest.getFirstName());
        reqMap.put("LastName", serviceRequest.getLastName());
        reqMap.put("CA_Birth_Date__c", serviceRequest.getDateOfBirth());
        reqMap.put("CA_Income__c", serviceRequest.getIncome());
        reqMap.put("CA_Monthly_Expenses__c", serviceRequest.getMonthlyExpenses());
        reqMap.put("CA_Monthly_Savings__c", serviceRequest.getMonthlySavings());
        reqMap.put("CA_Savings__c", serviceRequest.getSavings());
        reqMap.put("CA_Assets__c", serviceRequest.getAssets());
        reqMap.put("CA_Debts__c", serviceRequest.getDebts());
        reqMap.put("Email", serviceRequest.getEmail());
        reqMap.put("LeadSource", serviceRequest.getLeadSource());
        reqMap.put("CA_QuickStart_Lead__c", serviceRequest.getLeadSource()== null ? Boolean.FALSE: serviceRequest.getLeadSource().indexOf("QuickStart")>-1);
        reqMap.put("Language__c", serviceRequest.getLanguage());
        reqMap.put("PostalCode", serviceRequest.getPostalCode());
        reqMap.put("id", serviceRequest.getId());
        return getJsonString(reqMap);
    }
	

public static String validateServiceRequest(ServiceRequest serviceRequest) {
    
	   
    boolean isValid = Pattern.compile(ServiceConstants.NAME_REGEXP).matcher(serviceRequest.getFirstName()).matches();
    if(!isValid) {
        
        return String.format("FirstName is not valid: %S", serviceRequest.getFirstName());
        
    }
    isValid = Pattern.compile(ServiceConstants.NAME_REGEXP).matcher(serviceRequest.getLastName()).matches();
    if(!isValid) {
    	 return String.format("LasttName is not valid: %S", serviceRequest.getLastName());
       
    }
    isValid = Pattern.compile(ServiceConstants.DOB_REGEXP).matcher(serviceRequest.getDateOfBirth()).matches();
    if(!isValid) {
    	 return String.format("Date of Birth is not valid: %S", serviceRequest.getDateOfBirth());
        
    }
    isValid = Pattern.compile(ServiceConstants.EMAIL_REGEXP).matcher(serviceRequest.getEmail()).matches();
    if(!isValid) {
    	 return String.format("Email is not valid: %S", serviceRequest.getEmail());
        
    }
    isValid = Pattern.compile(ServiceConstants.LANG_REGEXP).matcher(serviceRequest.getLeadSource()).matches();
    if(!isValid) {
    	 return String.format("LeadSource is not valid: %S", serviceRequest.getLeadSource());
        
    }
    isValid = Pattern.compile(ServiceConstants.LANG_REGEXP).matcher(serviceRequest.getLanguage()).matches();
    if(!isValid) {
    	 return String.format("Language is not valid: %S", serviceRequest.getLanguage());
        
    }
    isValid = Pattern.compile(ServiceConstants.POSTAL_REGEXP).matcher(serviceRequest.getPostalCode()).matches();
    if(!isValid) {
    	 return String.format("Postal Code is not valid: %S", serviceRequest.getPostalCode());
       
    }
    
    
    return "Success";
    
}

}
