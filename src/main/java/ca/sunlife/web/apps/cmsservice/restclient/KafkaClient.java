package ca.sunlife.web.apps.cmsservice.restclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	
	private static final Logger logger = LogManager.getLogger(KafkaClient.class);
	
	@Value("${kafka.producer.endpoint}")
    private String kafkaProducerEndpoint;
    
	@Value("${kafka.producer.endpoint.faa}")
    private String kafkaProducerEndpointFaa;

    @Autowired
    RestTemplateGenerator restTemplateGenerator;
    
    RestTemplate restTemplate;
    
    public CmsResponse postData(HttpEntity<String> request) {
    	ResponseEntity<CmsResponse> response = null;
    	 CmsResponse cmsResponse = null;
    	try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
			}
               response = restTemplate.postForEntity(kafkaProducerEndpoint, request, CmsResponse.class);
               logger.info("Response Body::{}",response.getBody());
               cmsResponse = response.getBody();
               cmsResponse.setStatusCode(response.getStatusCodeValue());
		} catch (RestClientException ex) {
			ex.printStackTrace();
		}
        return cmsResponse != null ? cmsResponse : null;            
    }
    
    public CmsResponse postDataFaa(HttpEntity<String> request) {
    	ResponseEntity<CmsResponse> response = null;
    	try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
			}
               response = restTemplate.postForEntity(kafkaProducerEndpointFaa, request, CmsResponse.class);
		} catch (RestClientException ex) {
			ex.printStackTrace();
		}
        return response != null ? response.getBody() : null;            
    }
}
