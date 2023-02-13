package ca.sunlife.web.apps.cmsservice.restclient;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.utils.TestServiceUtil;

@SpringBootTest
class SalesforceClientTest {
	
	@Mock
    private RestTemplate restTemplate = mock(RestTemplate.class);
    
    @InjectMocks
    private SalesforceClient salesforceClient = new SalesforceClient();
    
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);        
    }
    @Test
    void testPostData() throws ParseException, JsonProcessingException {
        
        ServiceRequest data = TestServiceUtil.buildServiceRequest();
        CmsResponse cmsResponse = new CmsResponse();
        cmsResponse.setStatusCode(200);
        
        ReflectionTestUtils.setField(salesforceClient, "salesforceEndpoint", "url");
        
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(ServiceUtil.getJsonString(data), header);
        
        
        when(restTemplate.postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(CmsResponse.class)))
               .thenReturn(new ResponseEntity<>(cmsResponse, HttpStatus.OK));
        
        
        CmsResponse response = salesforceClient.postData(request);
        
        verify(restTemplate, times(1)).postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(CmsResponse.class));
        
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK.value());
    }

    @Test
    void testValidateToken() throws ParseException, JsonProcessingException {
        
        ServiceRequest data = TestServiceUtil.buildServiceRequest();
        CmsResponse cmsResponse = new CmsResponse();
        cmsResponse.setStatusCode(200);
        
        ReflectionTestUtils.setField(salesforceClient, "salesforceEndpoint", "url");
        
        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(ServiceUtil.getJsonString(data), header);        
        
        when(restTemplate.postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(CmsResponse.class)))
               .thenReturn(new ResponseEntity<>(cmsResponse, HttpStatus.OK));
        
        CmsResponse response = salesforceClient.postData(request);
        
        verify(restTemplate, times(1)).postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(CmsResponse.class));
        
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK.value());
    }

}
