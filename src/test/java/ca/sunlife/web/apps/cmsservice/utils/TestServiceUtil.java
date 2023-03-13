package ca.sunlife.web.apps.cmsservice.utils;

import java.text.ParseException;

import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;

public class TestServiceUtil {

	public static ServiceRequest buildServiceRequest() throws ParseException {
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setFirstName("fName");
		serviceRequest.setLastName("lName");
		serviceRequest.setDateOfBirth("2022-09-26");
		serviceRequest.setIncome(100000);
		serviceRequest.setMonthlyExpenses(10000);
		serviceRequest.setMonthlySavings(50000);
		serviceRequest.setSavings(500000);
		serviceRequest.setAssets(1000000);
		serviceRequest.setDebts(20000);
		serviceRequest.setEmail("fName.lName@test.com");
		serviceRequest.setLeadSource("QuickStart");
		serviceRequest.setQuickStart(false);
		serviceRequest.setLanguage("English");
		serviceRequest.setPostalCode("N2H0B6");
		return serviceRequest;

	}

}
