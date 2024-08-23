package ca.sunlife.web.apps.cmsservice.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceRequestTest {

    @InjectMocks
    private ServiceRequest serviceRequest = new ServiceRequest() {
    };

    @Mock
    private OktaTokenGenerator oktaTokenGenerator;

    @Mock
    private ApiGatewayService apiGatewayService;

    @Mock
    private EmailConfig emailConfig;

    @Mock
    private EmailService emailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonNode jsonMap;

    @Mock
    private InputStream inputStream;

    @Mock
    private JsonNode paramsNode;

    @BeforeEach
    void setUp() throws Exception {
        serviceRequest.setServiceFileName("ServiceRequestProspr.json");
    }

    @Test
    void testInit() throws Exception {
    	ServiceUtil.readServiceFile(serviceRequest, serviceRequest.getServiceFileName());
        when(objectMapper.readTree(any(InputStream.class))).thenReturn(jsonMap);
        when(jsonMap.get("serviceName")).thenReturn(mock(JsonNode.class));
        when(jsonMap.get("serviceName").textValue()).thenReturn("TestService");
        when(jsonMap.get("params")).thenReturn(paramsNode);
        JsonNode paramNode = mock(JsonNode.class);
        Set<JsonNode> paramNodeSet = new HashSet<>();
        paramNodeSet.add(paramNode);
        when(paramsNode.iterator()).thenReturn(paramNodeSet.iterator());
        when(paramNode.get("paramIn")).thenReturn(mock(JsonNode.class));
        when(paramNode.get("paramIn").textValue()).thenReturn("inputParam");
        when(paramNode.get("paramRequired")).thenReturn(mock(JsonNode.class));
        when(paramNode.get("paramRequired").booleanValue()).thenReturn(true);
        when(paramNode.get("paramRegex")).thenReturn(mock(JsonNode.class));
        when(paramNode.get("paramRegex").textValue()).thenReturn(".*");
        when(paramNode.get("paramOut")).thenReturn(mock(JsonNode.class));
        when(paramNode.get("paramOut").textValue()).thenReturn("outputParam");
        serviceRequest.init();
        assertEquals("ServiceRequestProspr", serviceRequest.getServiceName());

        Set<ServiceParam> serviceParams = serviceRequest.getServiceParams();
        assertNotNull(serviceParams);
    }

    @Test
    void testServiceValidation() {
        Map<String, Object> inputValues = new HashMap<>();
        inputValues.put("inputParam", "testValue");

        ServiceParam param = new ServiceParam();
        param.setParamName("inputParam");
        param.setParamRequired(true);
        param.setRegex(".*");
        param.setOutputName("outputParam");

        Set<ServiceParam> serviceParams = new HashSet<>();
        serviceParams.add(param);
        serviceRequest.setServiceParams(serviceParams);
        Map<String, Object> validInput = serviceRequest.serviceValidation(inputValues);
        assertNotNull(validInput);
        assertEquals("testValue", validInput.get("outputParam"));
    }

    @Test
    void testGetToken() {
        String tokenEndpoint = "http://test.token.endpoint";
        String scope = "testScope";
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";

        when(oktaTokenGenerator.generateToken(tokenEndpoint, scope, clientId, clientSecret)).thenReturn("dummyToken");

        String token = serviceRequest.getToken(tokenEndpoint, scope, clientId, clientSecret);

        assertEquals("dummyValue", token);
    }
}
