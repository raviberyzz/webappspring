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
		reqMap.put("Birth_Date", serviceRequest.getDateOfBirth());
		reqMap.put("Income", serviceRequest.getIncome());
		reqMap.put("Monthly_Expenses", serviceRequest.getMonthlyExpenses());
		reqMap.put("Monthly_Savings", serviceRequest.getMonthlySavings());
		reqMap.put("Savings", serviceRequest.getSavings());
		reqMap.put("Assets", serviceRequest.getAssets());
		reqMap.put("Debts", serviceRequest.getDebts());
		reqMap.put("Email", serviceRequest.getEmail());
		reqMap.put("LeadSource", serviceRequest.getLeadSource());
		reqMap.put("Language", serviceRequest.getLanguage());
		reqMap.put("PostalCode", serviceRequest.getPostalCode());
		reqMap.put("LeadID", generateUid(serviceRequest));
		return getJsonString(reqMap);
	}

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

		validateField("language", serviceRequest.getLanguage(), true, ServiceConstants.LANG_REGEXP);

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
		serviceRequest.setDateOfBirth("");
		serviceRequest.setIncome(0);
		serviceRequest.setMonthlyExpenses(0);
		serviceRequest.setMonthlySavings(0);
		serviceRequest.setSavings(0);
		serviceRequest.setAssets(0);
		serviceRequest.setDebts(0);
		serviceRequest.setLanguage("");
		serviceRequest.setPostalCode("");
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

}
