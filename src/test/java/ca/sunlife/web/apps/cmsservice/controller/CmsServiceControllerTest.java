package ca.sunlife.web.apps.cmsservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayServiceImpl;
import ca.sunlife.web.apps.cmsservice.utils.TestServiceUtil;

@SpringBootTest
class CmsServiceControllerTest {

	@Mock
    ApiGatewayService apiGatewayService; //= mock(ApiGatewayServiceImpl.class);

    @InjectMocks
    CmsServiceController cmsServiceController ; // = new CmsServiceController();
    
    
    @BeforeEach
    public void setup() throws ParseException {
        MockitoAnnotations.openMocks(this);    

    }
    
    @Test
    void testSubmit() throws JsonProcessingException, ParseException {
        ServiceRequest request = TestServiceUtil.buildServiceRequest();
        CmsResponse cmsResponse = new CmsResponse();
        when(apiGatewayService.sendData(any(ServiceRequest.class))).thenReturn(cmsResponse);
        CmsResponse response = cmsServiceController.submit(request);
        Assertions.assertNotNull(response);
    }

	
}
