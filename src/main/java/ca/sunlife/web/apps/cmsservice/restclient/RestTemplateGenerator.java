package ca.sunlife.web.apps.cmsservice.restclient;


import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateGenerator {
	
	@Value("${rest.temp.conectionTimeout}")
	private int connectionTimeout;
	
	@Value("${rest.temp.readTimeout}")
	private int readTimeout;
	
    RestTemplate restTemplate;

    
	public RestTemplate initializeRestTemplate() {
	try {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(connectionTimeout);
		httpRequestFactory.setReadTimeout(readTimeout);
		httpRequestFactory.setConnectionRequestTimeout(connectionTimeout);
		restTemplate = new RestTemplate(httpRequestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		} catch (RestClientException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restTemplate;
	}


}
