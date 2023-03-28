package ca.sunlife.web.apps.cmsservice.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;

@Component
public class KafkaClient {

	@Value("${kafka.producer.endpoint}")
    private String kafkaProducerEndpoint;
	
    @Autowired
    RestTemplate restTemplate;
    
    public CmsResponse postData(HttpEntity<String> request) {
    	ResponseEntity<CmsResponse> response = null;
         response = restTemplate.postForEntity(kafkaProducerEndpoint, request, CmsResponse.class);
       return response != null ? response.getBody() : null;            
    }
    
   
}
