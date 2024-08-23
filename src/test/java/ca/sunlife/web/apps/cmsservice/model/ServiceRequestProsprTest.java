package ca.sunlife.web.apps.cmsservice.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
class ServiceRequestProsprTest {

    @InjectMocks
    private ServiceRequestProspr serviceRequestProspr;

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
        serviceRequestProspr.setServiceFileName("ServiceRequestProspr.json");
        serviceRequestProspr.init();
    }

@Test
void testInit() throws Exception {
    inputStream= ServiceUtil.readServiceFile(serviceRequestProspr,serviceRequestProspr.getServiceFileName());
        when(objectMapper.readTree(any(InputStream.class))).thenReturn(jsonMap);
        when(jsonMap.get("serviceName")).thenReturn(mock(JsonNode.class));
        when(jsonMap.get("serviceName").textValue()).thenReturn("TestService");
        when(jsonMap.get("params")).thenReturn(paramsNode);
        serviceRequestProspr.init();
        assertEquals("ServiceRequestProspr", serviceRequestProspr.getServiceName());
}
@Test
void testServiceValidation() {
	Map<String, Object> inputValues =  new HashMap<>();
    inputValues.put("LeadSource", "QuickStartLead");
    ServiceParam param = new ServiceParam();
    param.setParamName("LeadSource");
    param.setParamRequired(true);
    param.setRegex(".*");
    param.setOutputName("leadSource");
    Set<ServiceParam> serviceParams = new HashSet<>();
    serviceParams.add(param);
    serviceRequestProspr.setServiceParams(serviceParams);
    Map<String, Object> validInput = serviceRequestProspr.serviceValidation(inputValues);
    assertNotNull(validInput);
}
@Test
void testSendData_Success() throws JsonProcessingException {
	Map<String, Object> inputValues =  new HashMap<>();
    inputValues.put("someKey", "someValue");

    String token = "dummyToken";
    when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
    HttpHeaders headers = new HttpHeaders();
    when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(headers);

    CmsResponse mockResponse = new CmsResponse();
    mockResponse.setStatusCode(201);
    when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

    CmsResponse response = serviceRequestProspr.sendData(inputValues);

    assertNotNull(response);
    assertEquals(201, response.getStatusCode());
    assertEquals("data successfully submitted", response.getMessage());
}

@Test
void testSendData_Unauthorized() throws JsonProcessingException {
	Map<String, Object> inputValues =  new HashMap<>();
    inputValues.put("someKey", "someValue");

    String token = "dummyToken";
    when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
    HttpHeaders headers = new HttpHeaders();
    when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(headers);

    CmsResponse mockResponse = new CmsResponse();
    mockResponse.setStatusCode(401);
    when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

    CmsResponse response = serviceRequestProspr.sendData(inputValues);

    assertNotNull(response);
    assertEquals(401, response.getStatusCode());
    assertEquals("Unauthorized", response.getMessage());
}

@Test
void testSendData_Failure() throws JsonProcessingException, MessagingException {
    Map<String, Object> inputValues =  new HashMap<>();
    inputValues.put("someKey", "someValue");

    String token = "dummyToken";
    when(oktaTokenGenerator.generateToken(anyString(), anyString(), anyString(), anyString())).thenReturn(token);
    HttpHeaders headers = new HttpHeaders();
    when(apiGatewayService.buildHttpHeader(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(headers);

    CmsResponse mockResponse = null;
    when(apiGatewayService.sendData(any(), any(), any())).thenReturn(mockResponse);

    CmsResponse response = serviceRequestProspr.sendData(inputValues);

    assertNotNull(response);
    assertEquals(500, response.getStatusCode());
    assertEquals("Unable to connect host!", response.getMessage());
    verify(emailService).sendEmailProspr(inputValues);
}
}
