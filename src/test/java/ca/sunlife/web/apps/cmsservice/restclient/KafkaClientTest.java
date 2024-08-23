package ca.sunlife.web.apps.cmsservice.restclient;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;

public class KafkaClientTest {

    @InjectMocks
    private KafkaClient kafkaClient;

    @Mock
    private RestTemplateGenerator restTemplateGenerator;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateGenerator.initializeRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    public void testPostData_Success() {
        HttpEntity<String> request = new HttpEntity<>("test data");
        String apiEndpoint = "http://mock-api.com/kafka";

        CmsResponse mockResponse = new CmsResponse();
        mockResponse.setMessage("Success");
        ResponseEntity<CmsResponse> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(apiEndpoint, request, CmsResponse.class)).thenReturn(responseEntity);

        CmsResponse response = kafkaClient.postData(request, apiEndpoint);

        assertNotNull(response);
        assertEquals("Success", response.getMessage());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testPostData_BadRequest() {
        HttpEntity<String> request = new HttpEntity<>("test data");
        String apiEndpoint = "http://mock-api.com/kafka";

        RestClientException exception = new RestClientException("400 Bad Request");

        when(restTemplate.postForEntity(apiEndpoint, request, CmsResponse.class)).thenThrow(exception);

        CmsResponse response = kafkaClient.postData(request, apiEndpoint);

        assertNotNull(response);
        assertEquals("400 Bad Request", response.getMessage());
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void testPostData_Unauthorized() {
        HttpEntity<String> request = new HttpEntity<>("test data");
        String apiEndpoint = "http://mock-api.com/kafka";

        RestClientException exception = new RestClientException("401 Unauthorized");

        when(restTemplate.postForEntity(apiEndpoint, request, CmsResponse.class)).thenThrow(exception);

        CmsResponse response = kafkaClient.postData(request, apiEndpoint);

        assertNotNull(response);
        assertEquals("401 Unauthorized", response.getMessage());
        assertEquals(401, response.getStatusCode());
    }

    @Test
    public void testPostData_InternalServerError() {
        HttpEntity<String> request = new HttpEntity<>("test data");
        String apiEndpoint = "http://mock-api.com/kafka";

        RestClientException exception = new RestClientException("500 Internal Server Error");

        when(restTemplate.postForEntity(apiEndpoint, request, CmsResponse.class)).thenThrow(exception);

        CmsResponse response = kafkaClient.postData(request, apiEndpoint);

        assertNotNull(response);
        assertEquals("500 Internal Server Error", response.getMessage());
        assertEquals(500, response.getStatusCode());
    }
}

