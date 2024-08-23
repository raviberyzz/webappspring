package ca.sunlife.web.apps.cmsservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.restclient.KafkaClient;

@SpringBootTest

public class ApiGatewayServiceImplTest {

	@Mock
	private KafkaClient kafkaClient = mock(KafkaClient.class);

	@Mock
	private OktaTokenGenerator oktaTokenGenerator = mock(OktaTokenGenerator.class);

	@Mock
	private EmailService emailService = mock(EmailService.class);

	@InjectMocks
	private ApiGatewayService apiGatewayService = new ApiGatewayServiceImpl();

	@BeforeEach
	public void setup() throws ParseException {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSendData() throws JsonProcessingException, ParseException, MessagingException {
		Map<String, Object> testData = new HashMap<>();
		testData.put("firstName", "localTestFirstname");
		testData.put("latName", "localTestLastName");
		testData.put("email", "abc@sunlife.com");
		testData.put("language", "English");
		CmsResponse expectedResponse = new CmsResponse();
		expectedResponse.setStatusCode(200);
		HttpHeaders headers = apiGatewayService.buildHttpHeader("bearerToken", "application/json", "xauth",
				"xtraceability", "xcorelation");
		when(kafkaClient.postData(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
		CmsResponse response = apiGatewayService.sendData(testData, headers, "endpointUrl");
		Assertions.assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
	}

}
