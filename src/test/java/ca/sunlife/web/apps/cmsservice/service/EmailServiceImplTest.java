package ca.sunlife.web.apps.cmsservice.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import ca.sunlife.web.apps.cmsservice.EmailConfig;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private EmailConfig emailConfig;
    
    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private TransportDelegator transport;

    @BeforeEach
    void setUp() {
    	Properties props = new Properties();
        props.put("mail.host", "emailhost");
        props.put("mail.smtp.host", "smtpHost");
    
        when(emailConfig.getFromAddress()).thenReturn("from@example.com");
        when(emailConfig.getToAddress()).thenReturn("to@example.com");
        when(emailConfig.getSubject()).thenReturn("Test Subject");
        when(emailConfig.getBody()).thenReturn("Test Body");
        when(emailConfig.getEmailProperties()).thenReturn(props);
    }

    @Test
    void testSendEmail() throws MessagingException {
        Map<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("key1", "value1");
        serviceRequest.put("key2", "value2");

        String[] result = emailService.sendEmail("Test Body", serviceRequest);
        assertArrayEquals(new String[]{
                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}"
        }, result);

        verify(transport, times(1)).sendEmail(any(Message.class));
    }

    @Test
    void testSendEmailProspr() throws MessagingException {
        Map<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("key1", "value1");
        serviceRequest.put("key2", "value2");

        String[] result = emailService.sendEmailProspr(serviceRequest);
        assertArrayEquals(new String[]{
                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}"
        }, result);

        verify(transport, times(1)).sendEmail(any(Message.class));
    }
	  @Test
	  void testBuildEmailData() throws MessagingException, IOException {
	  Map<String, Object> serviceRequest = new HashMap<>();
	  serviceRequest.put("key1", "value1"); serviceRequest.put("key2", "value2");
	  Message message = emailService.buildEmailData(mimeMessage,
			  serviceRequest); 
	  
	  }
	  
	  @Test
	  void testSendEmailWithEmailConfig() throws MessagingException{
		  Map<String, Object> serviceRequest = new HashMap<>();
	        serviceRequest.put("key1", "value1");
	        serviceRequest.put("key2", "value2");
	        String[] result =  emailService.sendEmail(emailConfig, serviceRequest);
	        assertArrayEquals(new String[]{
	                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}"
	        }, result);

	        verify(transport, times(1)).sendEmail(any(Message.class));

	  }
}