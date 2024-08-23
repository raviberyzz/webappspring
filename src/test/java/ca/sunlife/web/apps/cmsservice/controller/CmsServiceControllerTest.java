package ca.sunlife.web.apps.cmsservice.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFactory;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;

class CmsServiceControllerTest {
	

    @Mock
    private ApiGatewayService apiGatewayService;

    @Mock
    private EmailService emailService;

    @Mock
    private ServiceRequestFactory serviceRequestFactory;
    
    @Mock
    private EmailConfig config;

    @InjectMocks
    private CmsServiceController cmsServiceController;
    @Mock
    ServiceRequestProspr serviceRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmit() throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("svcid", "prospr");
        CmsResponse expectedResponse = new CmsResponse();
        expectedResponse.setMessage("Service call failure");
        expectedResponse.setStatusCode(200);
        CmsResponse response = cmsServiceController.submit(data);     
       Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
    }
    
    @Test
    void testSubmitWithServiceRequest() throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>();
        data.put("svcid", "prospr");
        CmsResponse expectedResponse = new CmsResponse();
        expectedResponse.setMessage("data successfully submitted");
        expectedResponse.setStatusCode(201);
        CmsResponse response = new CmsResponse();
        response.setStatusCode(201);
		response.setMessage("data successfully submitted");
        when(serviceRequestFactory.getServiceRequest(anyString())).thenReturn(serviceRequest);
        when(serviceRequest.serviceValidation(data)).thenReturn(data);
        when(serviceRequest.sendData(data)).thenReturn(response);
        CmsResponse cmsResponse = cmsServiceController.submit(data,null);
       Assertions.assertEquals(expectedResponse.getStatusCode(), cmsResponse.getStatusCode());
    }
    
    @Test
    void testSubmitWithSerivceRequestWithoutSVCIDPayoload() throws JsonProcessingException {
    	Map<String, Object> data = new HashMap<>();
    	 when(serviceRequestFactory.getServiceRequest(anyString())).thenReturn(serviceRequest);
         when(serviceRequest.serviceValidation(data)).thenReturn(data);
         CmsResponse expectedResponse = new CmsResponse();
         expectedResponse.setMessage("data successfully submitted");
         expectedResponse.setStatusCode(201);
         CmsResponse response = new CmsResponse();
         response.setStatusCode(201);
         when(serviceRequest.sendData(data)).thenReturn(response);
         CmsResponse cmsResponse = cmsServiceController.submit(data,null);
         Assertions.assertEquals(expectedResponse.getStatusCode(), cmsResponse.getStatusCode());
    }
    
    @Test
    void testSubmitWithServiveRequestandServiceCall() throws JsonProcessingException {
    	Map<String, Object> data = new HashMap<>();
    	 data.put("svcid", "faa");
    	 data.put("templateid", "faa");
    	 CmsResponse expectedResponse = new CmsResponse();
         expectedResponse.setMessage("data successfully submitted");
         expectedResponse.setStatusCode(200);
         CmsResponse response = new CmsResponse();
         response.setStatusCode(200);
         when(serviceRequest.sendData(data)).thenReturn(response);
         CmsResponse cmsResponse = cmsServiceController.submit(data,"faa");
         Assertions.assertEquals(expectedResponse.getStatusCode(), cmsResponse.getStatusCode());
    	 
    }
	
	@Test
	void testFormSubmit() throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("first_name", "cLocalFirst");
		paramMap.put("last_name", "cLocalFirst");
		paramMap.put("email", "local@sunlife.com");
		paramMap.put("language", "English");
		paramMap.put("LeadSource", "Quickstart");
		CmsResponse expectedResponse = new CmsResponse();
		expectedResponse.setMessage("Success");
		expectedResponse.setStatusCode(200);
		CmsResponse response =cmsServiceController.formSubmit(paramMap);
		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
	}
	 
}