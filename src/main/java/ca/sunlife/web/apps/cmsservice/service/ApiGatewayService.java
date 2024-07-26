package ca.sunlife.web.apps.cmsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestCommunication;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFaa;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;



public interface ApiGatewayService {

	CmsResponse sendData(Map<String,Object> data, HttpHeaders header, String endpoint) throws JsonProcessingException;
	CmsResponse sendData(ServiceRequestProspr data) throws JsonProcessingException;
	
	CmsResponse sendDataProspr(ServiceRequestProspr data) throws JsonProcessingException;
	CmsResponse sendDataProspr(Map<String,Object> data, HttpHeaders header) throws JsonProcessingException;
	
	CmsResponse sendDataFaa(ServiceRequestFaa data) throws JsonProcessingException;
	CmsResponse sendDataFaa(Map<String,Object> data, HttpHeaders header) throws JsonProcessingException;

	CmsResponse sendDataCommunication(ServiceRequestFaa data) throws JsonProcessingException;
	CmsResponse sendDataCommunication(Map<String,Object> data, HttpHeaders header) throws JsonProcessingException;

	HttpHeaders buildHttpHeader(String token, String mediaType, String xauth, String xtraceability, String xcorrelation);
}
