package ca.sunlife.web.apps.cmsservice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sunlife.web.apps.cmsservice.exception.FieldNotFoundException;
import ca.sunlife.web.apps.cmsservice.model.FaaServiceRequest;
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
		reqMap.put("CA_QuickStart_Lead__c", serviceRequest.getLeadSource() == null ? Boolean.FALSE
				: serviceRequest.getLeadSource().indexOf("QuickStart") > -1);
		reqMap.put("Language__c", serviceRequest.getLanguage());
		reqMap.put("PostalCode", serviceRequest.getPostalCode());
		reqMap.put("id", serviceRequest.getId());
		return getJsonString(reqMap);
	}

	public static String getLeadJsonString(ServiceRequest serviceRequest) throws JsonProcessingException {
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
		return getJsonString(reqMap);

	}
	
	public static String getFaaLeadJsonString(FaaServiceRequest serviceRequest) throws JsonProcessingException {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("firstName", serviceRequest.getFirstName()); //done
		reqMap.put("lastName", serviceRequest.getLastName()); //done
		reqMap.put("preferredPhone", serviceRequest.getPreferredPhone()); //done
		reqMap.put("homeAddressPostalCode", serviceRequest.getHomeAddressPostalCode()); //done
		reqMap.put("email", serviceRequest.getEmail()); //done
		reqMap.put("headOfficeLeadSource", serviceRequest.getHeadOfficeLeadSource()); //done
		reqMap.put("leadReceivedDateTime", serviceRequest.getLeadReceivedDateTime()); //done
		reqMap.put("preferredLanguage", serviceRequest.getPreferredLanguage()); //done
		reqMap.put("leadInstructions", serviceRequest.getLeadInstructions()); //done
		reqMap.put("homeAddressCity", serviceRequest.getHomeAddressCity()); //done
		reqMap.put("marketingTactic", serviceRequest.getMarketingTactic()); //done
		reqMap.put("primaryExtension", serviceRequest.getPrimaryExtension()); //done
		reqMap.put("webtrendsAnalyticsId", serviceRequest.getWebtrendsAnalyticsId()); //done
		reqMap.put("submissionId", serviceRequest.getSubmissionId()); //done
		reqMap.put("trafficSource", serviceRequest.getTrafficSource()); //done
		reqMap.put("webDomain", serviceRequest.getWebDomain()); //done
		reqMap.put("homeAddressProvince", serviceRequest.getHomeAddressProvince()); //done
		reqMap.put("requestedAdvisor", serviceRequest.getRequestedAdvisor()); //done
		reqMap.put("alternateLanguage", serviceRequest.getAlternateLanguage()); //done
		reqMap.put("cifPartyId", serviceRequest.getCifPartyId()); //done
		reqMap.put("bestTimeToCall", serviceRequest.getBestTimeToCall()); //done
		return getJsonString(reqMap);

	}
	
	public static String validateFaaServiceRequest(FaaServiceRequest serviceRequest) {
		logger.info("in ServiceUtil.validateFaaServiceRequst");
		
		validateFaaField("firstName", serviceRequest.getFirstName(), true, ServiceConstants.FAA_NAME_REGEXP);
		validateFaaField("lastName", serviceRequest.getLastName(), true, ServiceConstants.FAA_NAME_REGEXP);
		validateFaaField("email", serviceRequest.getEmail(), true, ServiceConstants.FAA_EMAIL_REGEXP);
		validateFaaField("preferredPhone", serviceRequest.getPreferredPhone(), true, ServiceConstants.FAA_PHONE_REGEXP);
		validateFaaField("primaryExtension", serviceRequest.getPrimaryExtension(), false, ServiceConstants.FAA_EXTENSION_REGEXP);
		validateFaaField("homeAddressPostalCode", serviceRequest.getHomeAddressPostalCode(), true, ServiceConstants.FAA_POSTAL_REGEXP);
		validateFaaField("preferredLanguage", serviceRequest.getPreferredLanguage(), true, ServiceConstants.LANGUAGE_REGEXP);
		validateFaaField("alternateLanguage", serviceRequest.getAlternateLanguage(), false, ServiceConstants.FAA_ALT_LANG_REGEXP);
		validateFaaField("headOfficeLeadSource", serviceRequest.getHeadOfficeLeadSource(), true, ServiceConstants.FAA_LEAD_SOURCE_REGEXP);
		validateFaaField("leadReceivedDateTime", serviceRequest.getLeadReceivedDateTime(), true, ServiceConstants.FAA_DATE_TIME_REGEXP);
		validateFaaField("leadInstructions", serviceRequest.getLeadInstructions(), false, ServiceConstants.FAA_INSTRUCTIONS_REGEXP);
		validateFaaField("homeAddressCity", serviceRequest.getHomeAddressCity(), false, ServiceConstants.FAA_CITY_REGEXP);
		validateFaaField("marketingTactic", serviceRequest.getMarketingTactic(), false, ServiceConstants.FAA_MARKETING_REGEXP);
		validateFaaField("webtrendsAnalyticsId", serviceRequest.getWebtrendsAnalyticsId(), false, ServiceConstants.FAA_ANALYTICS_ID_REGEXP);
		validateFaaField("submissionId", serviceRequest.getSubmissionId(), false, ServiceConstants.FAA_SUBMISSION_ID_REGEXP);
		validateFaaField("trafficSource", serviceRequest.getTrafficSource(), false, ServiceConstants.FAA_TRAFFIC_REGEXP);
		validateFaaField("homeAddressProvince", serviceRequest.getHomeAddressProvince(), false, ServiceConstants.FAA_PROVINCE_REGEXP);
		validateFaaField("cifPartyId", serviceRequest.getCifPartyId(), false, ServiceConstants.FAA_PARTY_ID_REGEXP);

		return "Success";
	}
	
	//Create validateFaaServiceRequest function and reuse the validate field function.

	public static String validateServiceRequest(ServiceRequest serviceRequest) {
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
	
	public static boolean validateFaaField(String fieldName, String fieldVal, boolean isRequired, String regExStr) {
		boolean isValid = true;
		if (fieldVal != null) {
			isValid = Pattern.compile(regExStr).matcher(fieldVal).matches();
		}
		
		// if field is required then check whether it is invalid or null, if it is throw the error
		if (isRequired && (fieldVal == null || !isValid)) {
			throw new FieldNotFoundException(String.format("%S is required and not found / invalid : %S", fieldName, fieldVal));
		} else if (!isRequired && fieldVal != null && fieldVal != "" && !isValid) {
			throw new FieldNotFoundException(String.format("%S is not required and is invalid : %S", fieldName, fieldVal));
		}
		
		// if the field is not required then check if its invalid and not blank, if it is both of those then throw another error
		
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

	public static ServiceRequest getServiceRequest(Map<String, String> map) {
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setFirstName(map.get("first_name"));
		serviceRequest.setLastName(map.get("last_name"));
		serviceRequest.setEmail(map.get("email"));
		serviceRequest.setLeadSource(getEmptyStringForNull(map.get("lead_source")));
		serviceRequest.setIncome(0);
		serviceRequest.setMonthlyExpenses(0);
		serviceRequest.setMonthlySavings(0);
		serviceRequest.setSavings(0);
		serviceRequest.setAssets(0);
		serviceRequest.setDebts(0);
		serviceRequest.setLanguage(map.get("language"));

		serviceRequest.setQuickStart(serviceRequest.getLeadSource() == null ? Boolean.FALSE
				: serviceRequest.getLeadSource().indexOf("QuickStart") > -1);

		return serviceRequest;
	}

	public static String getEmptyStringForNull(String val) {
		return val == null ? "" : val;
	}

	public static boolean validateFormField(Map<String, String> paramMap) {
		ServiceUtil.validateField("firstName", paramMap.get("first_name"), true, ServiceConstants.NAME_REGEXP);
		ServiceUtil.validateField("lastName", paramMap.get("last_name"), true, ServiceConstants.NAME_REGEXP);
		ServiceUtil.validateField("email", paramMap.get("email"), true, ServiceConstants.EMAIL_REGEXP);

		return true;

	}

	private static String generateUid(ServiceRequest data) {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("ddMMyyhhmmssMs");
		String randomId = data.getLeadSource() + ft.format(date);
		logger.info("Uid:{}", randomId);
		return randomId;

	}

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

	private ServiceUtil() {
	}

}
