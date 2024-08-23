package ca.sunlife.web.apps.cmsservice.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceRequestFaaTest {

	@InjectMocks
	private ServiceRequestFaa serviceRequestFaa;

	@Mock
	private OktaTokenGenerator oktaTokenGenerator;

	@Mock
	private ApiGatewayService apiGatewayService;

	@Mock
	private EmailService emailService;

	@Mock
	private EmailConfig emailConfig;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private JsonNode jsonMap;

	@Mock
	private InputStream inputStream;

	@Mock
	private JsonNode paramsNode;

	@Mock
	private CmsResponse cmsResponse;

	@BeforeEach
	void setUp() throws Exception {
		serviceRequestFaa.setServiceFileName("ServiceRequestFaa.json");
		serviceRequestFaa.init();
	}

	@Test
	void testInit() throws Exception {
		    inputStream= ServiceUtil.readServiceFile(serviceRequestFaa,serviceRequestFaa.getServiceFileName());
			when(objectMapper.readTree(any(InputStream.class))).thenReturn(jsonMap);
			when(jsonMap.get("serviceName")).thenReturn(mock(JsonNode.class));
			when(jsonMap.get("serviceName").textValue()).thenReturn("ServiceRequestFaa");
			when(jsonMap.get("params")).thenReturn(paramsNode);

			serviceRequestFaa.init();

			assertEquals("ServiceRequestFaa", serviceRequestFaa.getServiceName());

	}

	@Test
	void testServiceValidation() {
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("key1", "value1");

		Map<String, Object> result = serviceRequestFaa.serviceValidation(inputValues);

		assertEquals(inputValues, result);
	}

	@Test
	void testSendData_Success() throws JsonProcessingException {
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("someKey", "someValue");

		String token = "dummyToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		HttpHeaders headers = new HttpHeaders();
		when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(headers);

		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("Initial message");
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestFaa.sendData(inputValues);

		assertNotNull(response);
		assertEquals(200, response.getStatusCode());
		assertEquals("data successfully submitted", response.getMessage());
	}

	@Test
	void testSendData_Unauthorized() throws JsonProcessingException, MessagingException {
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("someKey", "someValue");

		String token = "dummyToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		HttpHeaders headers = new HttpHeaders();
		when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(headers);

		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(401);
		mockResponse.setMessage("Unauthorized");
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestFaa.sendData(inputValues);

		assertNotNull(response);
		assertEquals(401, response.getStatusCode());
		assertEquals("Unauthorized Access to API, API Response: Unauthorized", response.getMessage());
		verify(emailService).sendEmail(any(EmailConfig.class), anyMap());
	}

	@Test
	void testSendData_ApiFailure() throws JsonProcessingException, MessagingException {
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("someKey", "someValue");

		String token = "dummyToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		HttpHeaders headers = new HttpHeaders();
		when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(headers);

		CmsResponse mockResponse = new CmsResponse();
		mockResponse.setStatusCode(500);
		mockResponse.setMessage("Internal Server Error");
		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

		CmsResponse response = serviceRequestFaa.sendData(inputValues);

		assertNotNull(response);
		assertEquals(500, response.getStatusCode());
		assertEquals("Api responded with status 500! Api service unavailable, API response: Internal Server Error",
				response.getMessage());
		verify(emailService).sendEmail(any(EmailConfig.class), anyMap());
	}

	@Test
	void testSendData_ConnectionFailure() throws JsonProcessingException, MessagingException {
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("someKey", "someValue");

		String token = "dummyToken";
		when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
		HttpHeaders headers = new HttpHeaders();
		when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(headers);

		when(apiGatewayService.sendData(any(), any(), any())).thenReturn(null);

		CmsResponse response = serviceRequestFaa.sendData(inputValues);

		assertNotNull(response);
		assertEquals(500, response.getStatusCode());
		verify(emailService).sendEmail(any(EmailConfig.class), anyMap());
	}

}
