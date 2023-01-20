package ca.sunlife.web.apps.cmsservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.text.ParseException;
import javax.mail.MessagingException;
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
import org.springframework.test.util.ReflectionTestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.restclient.SalesforceClient;
import ca.sunlife.web.apps.cmsservice.utils.TestServiceUtil;

@SpringBootTest
public class ApiGatewayServiceImplTest {
	
	@Mock
    private SalesforceClient salesforceClient = mock(SalesforceClient.class);
    
    @Mock
    private OktaTokenGenerator oktaTokenGenerator = mock(OktaTokenGenerator.class);
    
    @Mock
    private EmailService emailService = mock(EmailService.class);

    @InjectMocks
    private ApiGatewayService apiGatewayService = new ApiGatewayServiceImpl();
    
    private ServiceRequest serviceRequest;
    
    private CmsResponse cmsResponse;

   
    @BeforeEach
    public void setup() throws ParseException {
        MockitoAnnotations.openMocks(this);    
        
        serviceRequest = TestServiceUtil.buildServiceRequest();
        cmsResponse = new CmsResponse();
        cmsResponse.setStatusCode(200);
        
        ReflectionTestUtils.setField(oktaTokenGenerator, "tokenEndpoint", "url");
        ReflectionTestUtils.setField(oktaTokenGenerator, "grantType", "grant_type");
        ReflectionTestUtils.setField(oktaTokenGenerator, "scope", "scope");
    }

    @Test
    void testSendData() throws JsonProcessingException, ParseException, MessagingException {
        Mockito.when(oktaTokenGenerator.generateToken()).thenReturn("21314234");

        Mockito.when(salesforceClient.postData(Mockito.any(HttpEntity.class))).thenReturn(cmsResponse);

        Mockito.when(salesforceClient.validateToken(Mockito.any(HttpEntity.class))).thenReturn(cmsResponse);

        CmsResponse response = apiGatewayService.sendData(serviceRequest);

        verify(oktaTokenGenerator, times(1)).generateToken();
        verify(salesforceClient, times(1)).validateToken(Mockito.any(HttpEntity.class));
        verify(salesforceClient, times(1)).postData(Mockito.any(HttpEntity.class));

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK.value());

    }

    @Test
    void testSendDataFailed() throws JsonProcessingException, ParseException, MessagingException {
        Mockito.when(emailService.sendEmail(Mockito.any(ServiceRequest.class))).thenReturn(null);

        Mockito.when(oktaTokenGenerator.generateToken()).thenReturn("21314234");

        Mockito.when(salesforceClient.postData(Mockito.any(HttpEntity.class))).thenReturn(null);

        Mockito.when(salesforceClient.validateToken(Mockito.any(HttpEntity.class))).thenReturn(cmsResponse);

        CmsResponse response = apiGatewayService.sendData(serviceRequest);

        verify(oktaTokenGenerator, times(1)).generateToken();
        verify(salesforceClient, times(1)).validateToken(Mockito.any(HttpEntity.class));
        verify(salesforceClient, times(1)).postData(Mockito.any(HttpEntity.class));
        verify(emailService, times(1)).sendEmail(Mockito.any(ServiceRequest.class));

        Assertions.assertEquals(500, response.getStatusCode());

    }
    
    @SuppressWarnings("unchecked")
    @Test
    void testSendDataWithInvalidToken() throws JsonProcessingException, ParseException, MessagingException {
        Mockito.when(emailService.sendEmail(Mockito.any(ServiceRequest.class))).thenReturn(null);

        Mockito.when(oktaTokenGenerator.generateToken()).thenReturn(null);

        CmsResponse response = apiGatewayService.sendData(serviceRequest);

        verify(oktaTokenGenerator, times(1)).generateToken();
        verify(emailService, times(1)).sendEmail(Mockito.any(ServiceRequest.class));

        Assertions.assertEquals(500, response.getStatusCode());

    }

}
