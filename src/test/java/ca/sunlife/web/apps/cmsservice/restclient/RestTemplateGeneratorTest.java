package ca.sunlife.web.apps.cmsservice.restclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class RestTemplateGeneratorTest {
	
	@InjectMocks
	private RestTemplateGenerator restTemplateGenerator;
	
	@BeforeEach
	public void setup() {
		 MockitoAnnotations.openMocks(this);
		 ReflectionTestUtils.setField(restTemplateGenerator, "connectionTimeout", 10000);
		 ReflectionTestUtils.setField(restTemplateGenerator, "readTimeout", 10000);
		
	}

	@Test
	void testGenrateRestTemplate() {
	RestTemplate template = restTemplateGenerator.initializeRestTemplate();
	Assertions.assertNotNull(template);
	}

}
