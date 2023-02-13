package ca.sunlife.web.apps.cmsservice.authentication;

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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ca.sunlife.web.apps.cmsservice.model.OktaResponse;

@SpringBootTest
class OktaTokenGeneratorTest {

	@Mock
    private RestTemplate restTemplate = mock(RestTemplate.class);
    
    @InjectMocks
    OktaTokenGenerator oktaTokenGenerator = new OktaTokenGenerator();
    
    @SuppressWarnings("deprecation")
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);        
    }
    
    @Test
    void testGenerateToken() throws RestClientException, URISyntaxException {
        ReflectionTestUtils.setField(oktaTokenGenerator, "tokenEndpoint", "url");
        ReflectionTestUtils.setField(oktaTokenGenerator, "grantType", "grant_type");
        ReflectionTestUtils.setField(oktaTokenGenerator, "scope", "scope");
        
        OktaResponse oktaResponse = new OktaResponse();
        oktaResponse.setAccessToken("11111");
        oktaResponse.setScope("scope");
        oktaResponse.setTokenType("credential");
        
        when(restTemplate.postForEntity(Mockito.any(String.class), 
                Mockito.any(HttpEntity.class),
                Mockito.eq(OktaResponse.class)))
               .thenReturn(new ResponseEntity<OktaResponse>(oktaResponse, HttpStatus.OK));
        
        String token = oktaTokenGenerator.generateToken();
        
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
        
        String token = oktaTokenGenerator.generateToken();
        
        Assertions.assertNull(token);
    }

}
