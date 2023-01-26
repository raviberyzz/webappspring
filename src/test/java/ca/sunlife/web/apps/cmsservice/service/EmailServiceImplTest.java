package ca.sunlife.web.apps.cmsservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.IOException;
import java.text.ParseException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.utils.TestServiceUtil;

@SpringBootTest
class EmailServiceImplTest {

	@InjectMocks
    EmailConfig emailConfig;

    @InjectMocks
    private EmailServiceImpl emailService = new EmailServiceImpl();

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(emailConfig, "emailHost", "emailHost");
        ReflectionTestUtils.setField(emailConfig, "emailPort", "emailPort");
        ReflectionTestUtils.setField(emailConfig, "isAuth", Boolean.TRUE);
        ReflectionTestUtils.setField(emailConfig, "isStarttlsEnabled", Boolean.TRUE);
        ReflectionTestUtils.setField(emailConfig, "username", "username");
        ReflectionTestUtils.setField(emailConfig, "password", "password");
        ReflectionTestUtils.setField(emailConfig, "fromText", "fromText");
        ReflectionTestUtils.setField(emailConfig, "subject", "subject");
        ReflectionTestUtils.setField(emailConfig, "body", "body");
        ReflectionTestUtils.setField(emailConfig, "fromAddress", "test@test.com");
        ReflectionTestUtils.setField(emailConfig, "toAddress", "test2@test.com");
        ReflectionTestUtils.setField(emailConfig, "ccAddress", "test3@test.com");
        ReflectionTestUtils.setField(emailConfig, "bccAddress", "test4@test.com");
        ReflectionTestUtils.setField(emailConfig, "mailType", "mailType");
        ReflectionTestUtils.setField(emailConfig, "charSet", "charSet");

        ReflectionTestUtils.setField(emailService, "emailConfig", emailConfig);

        // ReflectionTestUtils.setField(emailConfig, "emailProperties",
        // emailConfig.getEmailProperties());
    }
    @Test
    void testSendEmail() throws MessagingException, ParseException, IOException {
        ReflectionTestUtils.setField(emailConfig, "fromAddress", "test@test.com");
        ReflectionTestUtils.setField(emailConfig, "toAddress", "test2@test.com");
        TransportDelegator transport = mock(TransportDelegator.class);
        ReflectionTestUtils.setField(emailService, "transport", transport);
        doNothing().when(transport).sendEmail(any(Message.class));
        ServiceRequest serviceRequest = TestServiceUtil.buildServiceRequest();
        String[] result = emailService.sendEmail(serviceRequest);
        verify(transport, times(1)).sendEmail(Mockito.any(MimeMessage.class));
        Assertions.assertNotNull(result);
    }
    
    @Test
    void testEmailNotSend() throws MessagingException, ParseException, IOException {

        ServiceRequest serviceRequest = TestServiceUtil.buildServiceRequest();
        String[] result = emailService.sendEmail(serviceRequest);
        Assertions.assertNotNull(result);
    }

    @Test
    void testSendEmailNullFromAddress() throws MessagingException, ParseException {
        ReflectionTestUtils.setField(emailConfig, "fromAddress", null);
        ServiceRequest serviceRequest = TestServiceUtil.buildServiceRequest();
        String[] result = emailService.sendEmail(serviceRequest);
        Assertions.assertNotNull(result);

    }
    
    @Test
    void testSendEmailNullToAddress() throws MessagingException, ParseException {
        ReflectionTestUtils.setField(emailConfig, "fromAddress", "test@test.com");
        ReflectionTestUtils.setField(emailConfig, "toAddress", null);
        ServiceRequest serviceRequest = TestServiceUtil.buildServiceRequest();
        String[] result = emailService.sendEmail(serviceRequest);
        Assertions.assertNotNull(result);

    }

    @Test
    void testSendEmailEmptyToAddress() throws MessagingException, ParseException {
        ReflectionTestUtils.setField(emailConfig, "toAddress", "");
        ServiceRequest serviceRequest = TestServiceUtil.buildServiceRequest();
        String[] result = emailService.sendEmail(serviceRequest);
        Assertions.assertNotNull(result);

    }

    @Test
    void testSendEmailNullServiceRequest() throws MessagingException, ParseException {
        ReflectionTestUtils.setField(emailConfig, "toAddress", "test@test.com");
        ServiceRequest serviceRequest = null;
        String[] result = emailService.sendEmail(serviceRequest);
        Assertions.assertNotNull(result);

    }


}
