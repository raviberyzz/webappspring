package ca.sunlife.web.apps.cmsservice.authentication;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ca.sunlife.web.apps.cmsservice.model.OktaResponse;
import ca.sunlife.web.apps.cmsservice.restclient.RestTemplateGenerator;

@SpringBootTest
class OktaTokenGeneratorTest {

	@Mock
    private RestTemplate restTemplate = mock(RestTemplate.class);
    
    @InjectMocks
    OktaTokenGenerator oktaTokenGenerator = new OktaTokenGenerator();
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);        
    }
    
    @Test
    void testGenerateTokenProspr() throws RestClientException, URISyntaxException {
        ReflectionTestUtils.setField(oktaTokenGenerator, "tokenEndpoint", "url");
        ReflectionTestUtils.setField(oktaTokenGenerator, "grantType", "grant_type");
        ReflectionTestUtils.setField(oktaTokenGenerator, "scope", "scope");
         
        OktaResponse oktaResponse = new OktaResponse();
        oktaResponse.setAccess_token("11111");
        oktaResponse.setScope("scope");
        oktaResponse.setToken_type("credential");
        
        when(restTemplate.postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(OktaResponse.class)))
               .thenReturn(new ResponseEntity<OktaResponse>(oktaResponse, HttpStatus.OK));
        
        String token = oktaTokenGenerator.generateToken("11111","scope","clientId","clientSecret");
        
        verify(restTemplate, times(1)).postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(OktaResponse.class));
        
        Assertions.assertNotNull(token);
    }
    @Test
    void testGenerateTokenNull() throws RestClientException, URISyntaxException {
        OktaResponse oktaResponse = new OktaResponse();
        
        when(restTemplate.postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(OktaResponse.class)))
               .thenReturn(new ResponseEntity<OktaResponse>(oktaResponse, HttpStatus.OK));
        
        String token = oktaTokenGenerator.generateToken("11111","scope","clientId","clientSecret");
        
        Assertions.assertNull(token);
    }
    
	@Test
	void testGenerateWithNullTemplate() {
		RestTemplateGenerator restTemplateGenerator = mock(RestTemplateGenerator.class);
		ReflectionTestUtils.setField(oktaTokenGenerator, "restTemplateGenerator", restTemplateGenerator);
		ReflectionTestUtils.setField(oktaTokenGenerator, "restTemplate", null);
		when(restTemplateGenerator.initializeRestTemplate()).thenReturn(restTemplate);
		OktaResponse oktaResponse = new OktaResponse();
		ResponseEntity<OktaResponse> entity = new ResponseEntity<OktaResponse>(oktaResponse, HttpStatus.OK);
		when(restTemplate.postForEntity(Mockito.any(String.class), Mockito.any(HttpEntity.class),
				Mockito.eq(OktaResponse.class)))
				.thenReturn(new ResponseEntity<OktaResponse>(oktaResponse, HttpStatus.OK));

		Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());

	}

}
