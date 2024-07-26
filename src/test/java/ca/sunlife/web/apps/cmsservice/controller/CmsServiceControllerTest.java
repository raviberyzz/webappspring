package ca.sunlife.web.apps.cmsservice.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFactory;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;
import ca.sunlife.web.apps.cmsservice.util.ServiceConstants;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

class CmsServiceControllerTest {

    @Mock
    private ApiGatewayService apiGatewayService;

    @Mock
    private EmailService emailService;

    @Mock
    private ServiceRequestFactory serviceRequestFactory;

    @InjectMocks
    private CmsServiceController cmsServiceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmit() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("svcid", "prospr");

        CmsResponse expectedResponse = new CmsResponse();
        expectedResponse.setMessage("Success");
        expectedResponse.setStatusCode(200);

        when(serviceRequestFactory.getServiceRequest(anyString())).thenReturn(null);
        when(apiGatewayService.sendData(any())).thenReturn(expectedResponse);

        cmsServiceController.submit(data);

        verify(serviceRequestFactory).getServiceRequest(eq(ServiceConstants.SERVICES_MAP.get("prospr")));
        verify(apiGatewayService).sendData(eq(data));
    }

    @Test
    void testSubmitWithServiceCall() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("svcid", "faa");

        CmsResponse expectedResponse = new CmsResponse();
        expectedResponse.setMessage("Success");
        expectedResponse.setStatusCode(200);

        when(serviceRequestFactory.getServiceRequest(anyString())).thenReturn(null);
        when(apiGatewayService.sendData(any())).thenReturn(expectedResponse);

        cmsServiceController.submit(data, "faa");

        verify(serviceRequestFactory).getServiceRequest(eq(ServiceConstants.SERVICES_MAP.get("faa")));
        verify(apiGatewayService).sendData(eq(data));
    }

    @Test
    void testHandleServiceCall() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("svcid", "prospr");

        CmsResponse expectedResponse = new CmsResponse();
        expectedResponse.setMessage("Success");
        expectedResponse.setStatusCode(200);

        when(serviceRequestFactory.getServiceRequest(anyString())).thenReturn(null);
        when(apiGatewayService.sendData(any())).thenReturn(expectedResponse);

        cmsServiceController.handleServiceCall(data, "prospr");

        verify(serviceRequestFactory).getServiceRequest(eq(ServiceConstants.SERVICES_MAP.get("prospr")));
        verify(apiGatewayService).sendData(eq(data));
    }

    @Test
    void testFormSubmit() throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("param1", "value1");
        paramMap.put("param2", "value2");

        CmsResponse expectedResponse = new CmsResponse();
        expectedResponse.setMessage("Success");
        expectedResponse.setStatusCode(200);

        when(apiGatewayService.sendData(any())).thenReturn(expectedResponse);

        cmsServiceController.formSubmit(paramMap);

        verify(apiGatewayService).sendData(eq(paramMap));
    }
}