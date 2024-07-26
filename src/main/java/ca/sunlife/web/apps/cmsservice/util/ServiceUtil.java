package ca.sunlife.web.apps.cmsservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sunlife.web.apps.cmsservice.exception.FieldNotFoundException;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestCommunication;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFaa;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;

public class ServiceUtil {

	private static final Logger logger = LogManager.getLogger(ServiceUtil.class);

	public static String getJsonString(Object obj) throws JsonProcessingException {
		logger.info("in ServiceUtil.getJsonString");
		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(obj);
		logger.info("obj as str:{}", result);
		return result;
	}
/*
	public static String getSalesForceJsonString(ServiceRequestProspr serviceRequest) throws JsonProcessingException {
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
		reqMap.put("CA_QuickStart_Lead__c", serviceRequest.getLeadSource() == null ? Boolean.FALSE
				: serviceRequest.getLeadSource().indexOf("QuickStart") > -1);
		reqMap.put("Language__c", serviceRequest.getLanguage());
		reqMap.put("PostalCode", serviceRequest.getPostalCode());
		reqMap.put("id", serviceRequest.getId());
		return getJsonString(reqMap);
	}
*/
/*
	public static Map<String,Object> getLeadJsonMap(ServiceRequestProspr serviceRequest) throws JsonProcessingException {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("FirstName", serviceRequest.getFirstName());
		reqMap.put("LastName", serviceRequest.getLastName());
		if (serviceRequest.getDateOfBirth() != null && !serviceRequest.getDateOfBirth().trim().isEmpty()) {
			reqMap.put("Birth_Date", serviceRequest.getDateOfBirth());
		}
		reqMap.put("Income", serviceRequest.getIncome());
		reqMap.put("Monthly_Expenses", serviceRequest.getMonthlyExpenses());
		reqMap.put("Monthly_Savings", serviceRequest.getMonthlySavings());
		reqMap.put("Savings", serviceRequest.getSavings());
		reqMap.put("Assets", serviceRequest.getAssets());
		reqMap.put("Debts", serviceRequest.getDebts());
		reqMap.put("Email", serviceRequest.getEmail());
		reqMap.put("LeadSource", serviceRequest.getLeadSource());
		if (serviceRequest.getLanguage() != null && !serviceRequest.getLanguage().trim().isEmpty()) {
			reqMap.put("Language", serviceRequest.getLanguage());

		}

		if (serviceRequest.getPostalCode() != null && !serviceRequest.getPostalCode().trim().isEmpty()) {
			reqMap.put("PostalCode", serviceRequest.getPostalCode());
		}

		reqMap.put("LeadID", generateUid(serviceRequest));
		
		return reqMap;
		//return getJsonString(reqMap);

	}
*/
/*	
	public static Map<String,Object> getFaaLeadJsonMap(ServiceRequestFaa serviceRequest) throws JsonProcessingException {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("firstName", serviceRequest.getFirstName());
		reqMap.put("lastName", serviceRequest.getLastName());
		reqMap.put("preferredPhone", serviceRequest.getPreferredPhone());
		reqMap.put("homeAddressPostalCode", serviceRequest.getHomeAddressPostalCode());
		reqMap.put("email", serviceRequest.getEmail());
		reqMap.put("headOfficeLeadSource", serviceRequest.getHeadOfficeLeadSource());
		reqMap.put("leadReceivedDateTime", serviceRequest.getLeadReceivedDateTime());
		reqMap.put("preferredLanguage", serviceRequest.getPreferredLanguage());
		reqMap.put("leadInstructions", serviceRequest.getLeadInstructions());
		reqMap.put("homeAddressCity", serviceRequest.getHomeAddressCity());
		reqMap.put("marketingTactic", serviceRequest.getMarketingTactic());
		reqMap.put("primaryExtension", serviceRequest.getPrimaryExtension());
		reqMap.put("webtrendsAnalyticsId", serviceRequest.getWebtrendsAnalyticsId());
		reqMap.put("submissionId", serviceRequest.getSubmissionId());
		reqMap.put("trafficSource", serviceRequest.getTrafficSource());
		reqMap.put("webDomain", serviceRequest.getWebDomain());
		reqMap.put("homeAddressProvince", serviceRequest.getHomeAddressProvince());
		reqMap.put("requestedAdvisor", serviceRequest.getRequestedAdvisor());
		reqMap.put("alternateLanguage", serviceRequest.getAlternateLanguage());
		reqMap.put("cifPartyId", serviceRequest.getCifPartyId());
		reqMap.put("bestTimeToCall", serviceRequest.getBestTimeToCall());
		
		return reqMap;
		//return getJsonString(reqMap);

	}
*/
/*
	public static Map<String,Object> getCommunicationJsonMap(ServiceRequestFaa serviceRequest) throws JsonProcessingException {
		Map<String, Object> reqMap = new HashMap<>();
		Map<String, Object> emailMap = new HashMap<>();
		emailMap.put("emailAddress", serviceRequest.getEmail());
		emailMap.put("templateId", serviceRequest.getTemplateId());
		emailMap.put("language", serviceRequest.getPreferredLanguage() == "French" ? "F" : "E");
		reqMap.put("Email", emailMap);
		Map<String, Object> additionalAttributes = new HashMap<>();
		additionalAttributes.put("firstName", serviceRequest.getFirstName());
		additionalAttributes.put("coverageAmount", serviceRequest.getCoverageAmount());
		additionalAttributes.put("costRange", serviceRequest.getCostRange());
		additionalAttributes.put("incomeReplacement", serviceRequest.getIncomeReplacement());
		additionalAttributes.put("debtPayment", serviceRequest.getDebtPayment());
		additionalAttributes.put("additionalExpense", serviceRequest.getAdditionalExpense());
		additionalAttributes.put("totalEstimate", serviceRequest.getTotalEstimate());
		emailMap.put("additionalAttributes", additionalAttributes);
		
		return reqMap;
		//return getJsonString(reqMap);

	}
*/
/*
	public static String validateFaaServiceRequest(ServiceRequestFaa serviceRequest) {
		logger.info("in ServiceUtil.validateFaaServiceRequst");
		try {
			validateFaaField("firstName", serviceRequest.getFirstName());
			validateFaaField("lastName", serviceRequest.getLastName());
//			validateFaaField("preferredPhone", serviceRequest.getPreferredPhone());
			validateFaaField("homeAddressPostalCode", serviceRequest.getHomeAddressPostalCode());
			validateFaaField("preferredLanguage", serviceRequest.getPreferredLanguage());
			validateFaaField("headOfficeLeadSource", serviceRequest.getHeadOfficeLeadSource());
			validateFaaField("leadReceivedDateTime", serviceRequest.getLeadReceivedDateTime());
			String phoneNumber = serviceRequest.getPreferredPhone();
			String email = serviceRequest.getEmail();
			if (phoneNumber == null || phoneNumber.equals("")) {
				validateFaaField("email", email);
			} else {
				validateFaaField("preferredPhone", phoneNumber);
			}
		}
			catch(Exception e) {
			  return "Error details: " + e.toString();
			}
		
		return "Success";
	}
*/
/*
	public static String validateCommunicationServiceRequest(ServiceRequestCommunication serviceRequest) {
		logger.info("in ServiceUtil.validateFaaServiceRequst");
//		try {
//			validateCommunicationField("firstName", serviceRequest.getFirstName());
//			validateFaaField("lastName", serviceRequest.getLastName());
////			validateFaaField("preferredPhone", serviceRequest.getPreferredPhone());
//			validateFaaField("homeAddressPostalCode", serviceRequest.getHomeAddressPostalCode());
//			validateFaaField("preferredLanguage", serviceRequest.getPreferredLanguage());
//			validateFaaField("headOfficeLeadSource", serviceRequest.getHeadOfficeLeadSource());
//			validateFaaField("leadReceivedDateTime", serviceRequest.getLeadReceivedDateTime());
//			String phoneNumber = serviceRequest.getPreferredPhone();
//			String email = serviceRequest.getEmail();
//			if (phoneNumber == null || phoneNumber.equals("")) {
//				validateFaaField("email", email);
//			} else {
//				validateFaaField("preferredPhone", phoneNumber);
//			}
//		}
//			catch(Exception e) {
//			  return "Error details: " + e.toString();
//			}
		
		return "Success";
	}
*/
	//Create validateFaaServiceRequest function and reuse the validate field function.
/*
	public static String validateServiceRequest(ServiceRequestProspr serviceRequest) {
		logger.info("in ServiceUtil.validateServiceRequst");

		validateField("firstName", serviceRequest.getFirstName(), true, ServiceConstants.NAME_REGEXP);

		validateField("lastName", serviceRequest.getLastName(), true, ServiceConstants.NAME_REGEXP);

		validateField("dateOfBirth", serviceRequest.getDateOfBirth(), true, ServiceConstants.DOB_REGEXP);

		validateField("email", serviceRequest.getEmail(), true, ServiceConstants.EMAIL_REGEXP);

		validateCurrencyField("income", serviceRequest.getIncome(), true);

		validateCurrencyField("monthlyExpenses", serviceRequest.getMonthlyExpenses(), true);

		validateCurrencyField("monthlySavings", serviceRequest.getMonthlySavings(), true);

		validateCurrencyField("savings", serviceRequest.getSavings(), true);

		validateCurrencyField("assets", serviceRequest.getAssets(), true);

		validateCurrencyField("debts", serviceRequest.getDebts(), true);

		validateField("leadSource", serviceRequest.getLeadSource(), true, ServiceConstants.LANG_REGEXP);

		validateField("language", serviceRequest.getLanguage(), true, ServiceConstants.LANGUAGE_REGEXP);

		validateField("postalCode", serviceRequest.getPostalCode(), true, ServiceConstants.POSTAL_REGEXP);

		return "Success";

	}
*/
	public static boolean validateField(String fieldName, String fieldVal, boolean isRequired, String regExStr) {
		boolean isValid = true;
		if (fieldVal != null) {
			isValid = Pattern.compile(regExStr).matcher(fieldVal).matches();
		}

		if (isRequired && (fieldVal == null || !isValid)) {
			throw new FieldNotFoundException(String.format("%S is not found/ invalid : %S", fieldName, fieldVal));
		}
		return isValid;
	}
	
	public static boolean validateFaaField(String fieldName, String fieldVal) {
		boolean isValid = true;
		// if field is required then check whether it is invalid or null, if it is throw the error
		if (fieldVal == null || fieldVal.equals("")){
			throw new FieldNotFoundException(String.format("%S is required and not found. The Value in the request was : %S", fieldName, fieldVal));
		}		
		return isValid;
	}

	public static boolean validateCurrencyField(String fieldName, int fieldVal, boolean isRequired) {
		boolean isValid = true;

		if (isRequired && (fieldVal < 0 || countDigits(fieldVal) > 8)) {
			throw new FieldNotFoundException(String.format(
					"%S input should be less than or equal to 7 numeric digit and shouldn't be negative or empty values: %S",
					fieldName, fieldVal));
		}
		return isValid;
	}

	public static Map<String,Object> prepSubmitProspr(Map<String,Object> data) {
		data.put("FirstName", (String)data.get("first_name"));
		data.put("LastName", (String)data.get("last_name"));
		data.put("Email", (String)data.get("email"));
		data.put("Language", data.get("language"));
		data.put("LeadID", "");
		
		data.put("Income", 0);
		data.put("Monthly_Expenses", 0);
		data.put("Monthly_Savings", 0);
		data.put("Savings", 0);
		data.put("Assets", 0);
		data.put("Debts", 0);
		data.put("Monthly_Savings", 0);
		
		data.put("QuickStart", data.get("LeadSource") == null ? false : data.get("LeadSource").toString().indexOf("QuickStart") > -1);
		data.put("LeadSource", getEmptyStringForNull((String)data.get("lead_source")));
		
		// need quickStart

		return data;		
	}
/*
	public static ServiceRequestProspr getServiceRequest(Map<String, Object> map) {
		ServiceRequestProspr serviceRequest = new ServiceRequestProspr();
		serviceRequest.setFirstName((String)map.get("first_name"));
		serviceRequest.setLastName((String)map.get("last_name"));
		serviceRequest.setEmail((String)map.get("email"));
		serviceRequest.setLeadSource(getEmptyStringForNull((String)map.get("lead_source")));
		serviceRequest.setIncome(0);
		serviceRequest.setMonthlyExpenses(0);
		serviceRequest.setMonthlySavings(0);
		serviceRequest.setSavings(0);
		serviceRequest.setAssets(0);
		serviceRequest.setDebts(0);
		serviceRequest.setLanguage((String)map.get("language"));

		serviceRequest.setQuickStart(serviceRequest.getLeadSource() == null ? Boolean.FALSE
				: serviceRequest.getLeadSource().indexOf("QuickStart") > -1);

		return serviceRequest;
	}
*/
	public static String getEmptyStringForNull(String val) {
		return val == null ? "" : val;
	}

	public static boolean validateFormField(Map<String, Object> paramMap) {
		ServiceUtil.validateField("firstName", (String)paramMap.get("first_name"), true, ServiceConstants.NAME_REGEXP);
		ServiceUtil.validateField("lastName", (String)paramMap.get("last_name"), true, ServiceConstants.NAME_REGEXP);
		ServiceUtil.validateField("email", (String)paramMap.get("email"), true, ServiceConstants.EMAIL_REGEXP);

		return true;

	}

	public static String generateUniqueId() {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("ddMMyyhhmmssMs");
		String randomId = ft.format(date);
		logger.info("UniqueId: {}", randomId);
		return randomId;
	}
/*
	private static String generateUid(ServiceRequestProspr data) {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("ddMMyyhhmmssMs");
		String randomId = data.getLeadSource() + ft.format(date);
		logger.info("Uid:{}", randomId);
		return randomId;

	}
*/
	public static int countDigits(int n) {
		int count = 0;
		while (n != 0) {
			// removing the last digit of the number n
			n = n / 10;
			// increasing count by 1
			count = count + 1;
		}
		return count;
	}
	
	public static InputStream readServiceFile(ServiceRequest sr, String filename) {
		try {
			ClassLoader classLoader = sr.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("json/"+filename);
			
			if (inputStream != null) {
				logger.info("got file");
				return inputStream;
				
			} else {
				logger.info("no file");
			}
		} catch (Exception e) {
			logger.info("fail");
		}
		return null;

	}

	public static boolean passRegex(String regexValue, String value) {
		String regexKey = regexValue.toUpperCase();
		String regex = ServiceConstants.REGEX_MAP.containsKey(regexKey) ? ServiceConstants.REGEX_MAP.get(regexKey) : regexValue;
		
		return Pattern.matches(regex, value);		
	}

}
