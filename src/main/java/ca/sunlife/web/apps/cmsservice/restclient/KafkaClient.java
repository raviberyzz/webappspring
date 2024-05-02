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
	
	@Value("${kafka.endpoint.communication}")
    private String kafkaEndpointCommunication;

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
    	CmsResponse cmsResponse = null;
    	String errorMessage = "";
    	try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
			}
               response = restTemplate.postForEntity(kafkaProducerEndpointFaa, request, CmsResponse.class);
               logger.info("Response Body::{}",response.getBody());
               cmsResponse = response.getBody();
               cmsResponse.setStatusCode(response.getStatusCodeValue());
    	} catch (RestClientException ex) {
    		errorMessage = ex.getMessage();
    		cmsResponse = new CmsResponse();
            cmsResponse.setMessage(errorMessage);
            if (errorMessage.contains("400")) {
            	cmsResponse.setStatusCode(400);
            } else if (errorMessage.contains("401")) {
            	cmsResponse.setStatusCode(401);
            } else {
            	cmsResponse.setStatusCode(500);
            }
		}
    	return cmsResponse != null ? cmsResponse : null;           
    }
    
    public CmsResponse postDataCommunication(HttpEntity<String> request) {
    	ResponseEntity<CmsResponse> response = null;
    	CmsResponse cmsResponse = null;
    	String errorMessage = "";
    	try {
			if(restTemplate == null){
				restTemplate = restTemplateGenerator.initializeRestTemplate();
			}
               response = restTemplate.postForEntity(kafkaEndpointCommunication, request, CmsResponse.class);
               logger.info("Response Body::{}",response.getBody());
               cmsResponse = response.getBody();
               cmsResponse.setStatusCode(response.getStatusCodeValue());
    	} catch (RestClientException ex) {
    		errorMessage = ex.getMessage();
    		cmsResponse = new CmsResponse();
            cmsResponse.setMessage(errorMessage);
            if (errorMessage.contains("400")) {
            	cmsResponse.setStatusCode(400);
            } else if (errorMessage.contains("401")) {
            	cmsResponse.setStatusCode(401);
            } else {
            	cmsResponse.setStatusCode(500);
            }
		}
    	return cmsResponse != null ? cmsResponse : null;           
    }
}
