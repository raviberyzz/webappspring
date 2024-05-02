package ca.sunlife.web.apps.cmsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.CommunicationServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.FaaServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;


public interface ApiGatewayService {

	CmsResponse sendData(ServiceRequest data) throws JsonProcessingException;
	
	CmsResponse sendDataFaa(FaaServiceRequest data) throws JsonProcessingException;

	CmsResponse sendDataCommunication(CommunicationServiceRequest data) throws JsonProcessingException;

}
