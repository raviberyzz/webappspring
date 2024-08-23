package ca.sunlife.web.apps.cmsservice.model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;

public class ServiceRequestCommunicationTest {

	@InjectMocks
	private ServiceRequestCommunication serviceRequestCommunication;

	@Mock
	private ApiGatewayService apiGatewayService;

	@Mock
	private OktaTokenGenerator oktaTokenGenerator;

	@Mock
	private EmailService emailService;

	@Mock
	private EmailConfig emailConfig;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		serviceRequestCommunication.init();
	}

	@Test
	public void testInit() {
		assertNotNull(serviceRequestCommunication);
		assertEquals("ServiceRequestCommunication.json", serviceRequestCommunication.getServiceFileName());
	}

	@Test
	public void testServiceValidation() {
		Map<String, Object> input = new HashMap<>();
		input.put("firstName", "John");
		input.put("coverageAmount", "500000");
		input.put("costRange", "Hello,World 123");
		input.put("email", "test.email+regex@example-domain.com");
		input.put("templateId", "template123");
		input.put("Language", "English");
		input.put("debtPayment", "500000");
		input.put("additionalExpense", "500000");
		input.put("incomeReplacement", "500000");
		input.put("totalEstimate", "500000");
		Map<String, Object> validatedInputs = serviceRequestCommunication.serviceValidation(input);
		assertNotNull(validatedInputs);
	}

	@Test
	public void testSendData_Success() throws JsonProcessingException {
		Map<String, Object> input = new HashMap<>();
		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("data successfully submitted");

		String token = "mockToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		when(apiGatewayService.buildHttpHeader(anyString(), eq(MediaType.APPLICATION_JSON_VALUE), isNull(), eq("2"),
				isNull())).thenReturn(new HttpHeaders());
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestCommunication.sendData(input);

		assertEquals(200, response.getStatusCode());
		assertEquals("data successfully submitted", response.getMessage());
	}

	@Test
	public void testSendData_Unauthorized() throws JsonProcessingException {
		Map<String, Object> input = new HashMap<>();
		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(401);
		mockResponse.setMessage("Unauthorized Access");

		String token = "mockToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		when(apiGatewayService.buildHttpHeader(anyString(), eq(MediaType.APPLICATION_JSON_VALUE), isNull(), eq("2"),
				isNull())).thenReturn(new HttpHeaders());
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestCommunication.sendData(input);

		assertEquals(401, response.getStatusCode());
		assertEquals("Unauthorized Access to API, API Response: Unauthorized Access", response.getMessage());
	}

	@Test
	public void testSendData_BadRequest() throws JsonProcessingException {
		Map<String, Object> input = new HashMap<>();
		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(400);
		mockResponse.setMessage("Bad Request");

		String token = "mockToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		when(apiGatewayService.buildHttpHeader(anyString(), eq(MediaType.APPLICATION_JSON_VALUE), isNull(), eq("2"),
				isNull())).thenReturn(new HttpHeaders());
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestCommunication.sendData(input);

		assertEquals(400, response.getStatusCode());
		assertEquals(
				"Api responded with status 400! API rejected lead details due to failed validation, API Response: Bad Request",
				response.getMessage());
	}

	@Test
	public void testSendData_ServerError() throws JsonProcessingException {
		Map<String, Object> input = new HashMap<>();
		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(500);
		mockResponse.setMessage("Internal Server Error");

		String token = "mockToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		when(apiGatewayService.buildHttpHeader(anyString(), eq(MediaType.APPLICATION_JSON_VALUE), isNull(), eq("2"),
				isNull())).thenReturn(new HttpHeaders());
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestCommunication.sendData(input);

		assertEquals(500, response.getStatusCode());
		assertEquals("Api responded with status 500! Api service unavailable, API response: Internal Server Error",
				response.getMessage());
	}
}
