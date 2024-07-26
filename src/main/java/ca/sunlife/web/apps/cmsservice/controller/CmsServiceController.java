package ca.sunlife.web.apps.cmsservice.controller;

import java.util.Map;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Collection;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFaa;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.model.ServiceParam;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFactory;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestCommunication;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.service.EmailService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.util.ServiceConstants;

/**
 * @author Uma Maheshwaran
 *
 */
@RestController

@RequestMapping(value = "/cms-service/")
@CrossOrigin
public class CmsServiceController {

	private static final Logger logger = LogManager.getLogger(CmsServiceController.class);

	@Autowired
	ApiGatewayService apiGatewayService;
	
	@Autowired
    EmailConfig emailConfig;
	
	@Autowired
    EmailService emailService;
	
	@Autowired
	ServiceRequestFactory serviceRequestFactory;
	
	@Autowired
	Collection<ServiceRequest> services;

	@Value("redirect.url")
	private String retURL;

	//@PostMapping(value = "/submit")
	//public CmsResponse submit(@RequestBody ServiceRequest data) throws JsonProcessingException {
	/*
	public CmsResponse submitProspr(ServiceRequestProspr data) throws JsonProcessingException {
		logger.info("in CmsServiceController");
		CmsResponse cmsresponse = null;

		String validResponse = ServiceUtil.validateServiceRequest(data);
		setQuickstartFlag(data);
		logger.info("Validresponse: {}", validResponse);

		if (validResponse.equals("Success")) {
			cmsresponse = apiGatewayService.sendData(data);
		} else {
			cmsresponse = new CmsResponse();
			cmsresponse.setMessage(validResponse);
			cmsresponse.setStatusCode(500);
			logger.error(validResponse);
			logger.error("Invalid Request:{}", ServiceUtil.getJsonString(data));
		}
		return cmsresponse;
	}
	*/
	
	//@PostMapping(value = "/submit/faa")
	//public CmsResponse submit(@RequestBody FaaServiceRequest data) throws JsonProcessingException {
	/*
	public CmsResponse submitFaa(ServiceRequestFaa data) throws JsonProcessingException {
		logger.info("in CmsServiceController faa response");
		CmsResponse cmsresponse = null;
		CmsResponse cmsCommunicationsResponse = null;
		String validResponse = ServiceUtil.validateFaaServiceRequest(data);
		logger.info("Validresponse: {}", validResponse);
		//add call to serviceUtil.validateFaaServiceReequest
		//based on response proceed with apiGateWayCall
		if (validResponse.equals("Success")) {
			cmsresponse = apiGatewayService.sendDataFaa(data);
			if (data.getTemplateId() != null) {
				cmsCommunicationsResponse = apiGatewayService.sendDataCommunication(data);
				cmsresponse.setMessage(cmsresponse.getMessage() + String.format(" Communications API Response code: %d and response message: %s", cmsCommunicationsResponse.getStatusCode(), cmsCommunicationsResponse.getMessage()));
			}
		} else {
			emailConfig.setBodyFaa("The following FAA Lead was not submitted due to missing required fields: " + validResponse);
			cmsresponse = new CmsResponse();
			cmsresponse.setMessage(validResponse);
			cmsresponse.setStatusCode(400);
			try {
				Map<String,Object> dataMap = ServiceUtil.getFaaLeadJsonMap(data);
                //emailService.sendEmailFaa(data);
				emailService.sendEmailFaa(dataMap);
                logger.info("Email sent successfully");
            }catch(MessagingException ex) {
            	logger.error("Email failed: {}", ex.getMessage());
            }        
			logger.error(validResponse);
			logger.error("Invalid Request:{}", ServiceUtil.getJsonString(data));
		}
		return cmsresponse;
	}
	*/

	@PostMapping(value = "/submit")
	public CmsResponse submit(@RequestBody Map<String,Object> data) throws JsonProcessingException {
		logger.info("ENTERED /submit: pathvariable");
		return handleServiceCall(data, null);
	}
	
	@PostMapping(value = "/submit/{serviceCall}")
	public CmsResponse submit(@RequestBody Map<String,Object> data, @PathVariable("serviceCall") String serviceCall) throws JsonProcessingException {
		logger.info("ENTERED /submit/serviceCall: {}", serviceCall);
		return handleServiceCall(data, serviceCall);
	}
	
	private CmsResponse handleServiceCall(Map<String,Object> data, String serviceCall) throws JsonProcessingException {
		logger.info("====> service called: {}", serviceCall);
		logger.info("====> service with svc called: {}", data.get("svcid"));

		CmsResponse parentCmsResponse = null;
		Map<String,Object> validatedInput = null;
		String[] serviceClasses = getServiceClass(data, serviceCall);;
		
		if (serviceClasses != null) {
			for (String serviceClass : serviceClasses) {
				if (serviceClass != null) {
					CmsResponse cmsresponse = null;
					ServiceRequest sr = serviceRequestFactory.getServiceRequest(serviceClass);
					logger.info("got class");
					
					if (sr != null) {
						validatedInput = sr.serviceValidation(data);
		
						if (validatedInput != null) {
							cmsresponse = sr.sendData(validatedInput);

							if (parentCmsResponse != null) {
								parentCmsResponse.setMessage(parentCmsResponse.getMessage() + 
									String.format(" %s API Response code: %d and response message: %s", serviceClass, cmsresponse.getStatusCode(), cmsresponse.getMessage()));
							} else {
								parentCmsResponse = cmsresponse;
							}
						}
					}
				}
			}
		}
		
		if (parentCmsResponse == null) {
			parentCmsResponse = new CmsResponse();
			parentCmsResponse.setMessage("Service call failure");
			parentCmsResponse.setStatusCode(200);
			logger.error("Service call failure: {}", serviceCall);
		}
		
		logger.info(data);
		
		logger.info("==> EXIT: ending service call");
		return parentCmsResponse;
	}
	
//	@PostMapping(value = "/submit/communication")
//	public CmsResponse submit(@RequestBody CommunicationServiceRequest data) throws JsonProcessingException {
//		logger.info("in CmsServiceController faa response");
//		CmsResponse cmsresponse = null;
//
//		String validResponse = ServiceUtil.validateCommunicationServiceRequest(data);
//		logger.info("Validresponse: {}", validResponse);
//		if (validResponse.equals("Success")) {
//			cmsresponse = apiGatewayService.sendDataCommunication(data);
//		} else {
//			cmsresponse = new CmsResponse();
//			cmsresponse.setMessage(validResponse);
//			cmsresponse.setStatusCode(400);
//			logger.error(validResponse);
//			logger.error("Invalid Request:{}", ServiceUtil.getJsonString(data));
//		}
//		return cmsresponse;
//	}

	/* i don't think this is used ?? this is basically a prospr call */
	@PostMapping(value = "form-submit", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public CmsResponse formSubmit(@RequestParam Map<String, Object> paramMap) throws JsonProcessingException {

		logger.info("====> form-submit entered");
		
		ServiceUtil.validateFormField(paramMap);
		Map<String,Object> preppedData = ServiceUtil.prepSubmitProspr(paramMap);
		//CmsResponse cr = apiGatewayService.sendData(ServiceUtil.getServiceRequest(paramMap));
		CmsResponse cr = handleServiceCall(preppedData, "prospr");

		logger.info("==> form-submit exit");
		return cr;
		//return apiGatewayService.sendData(ServiceUtil.getServiceRequest(paramMap));
		//return apiGatewayService.sendDataProspr(preppedData);
	}

	/*
	 * Get the service call to handle the request.  
	 * Will handle legacy calls, where 'svcid' is not provided or provided in pathvariable.
	 */
	private String[] getServiceClass(Map<String,Object> data, String serviceCall) {
		String serviceClass = null;
		String[] serviceClasses = null;
		
		if (!data.containsKey("svcid")) {
			/* this should handle older, legacy calls */
			if (serviceCall == null)
				serviceCall = "prospr";
			if (serviceCall.equals("faa")) {
				serviceCall = data.containsKey("templateid") ? "lic" : "faa";
			}
		} else {
			serviceCall = (String)data.get("svcid");
		}
		
		if (ServiceUtil.passRegex("ALPHANUMERIC", serviceCall)) {
			if (ServiceConstants.SERVICES_MAP.containsKey(serviceCall))
				serviceClass = ServiceConstants.SERVICES_MAP.get(serviceCall);
			
			if (ServiceConstants.SERVICES_GROUP_MAP.containsKey(serviceCall))
				serviceClasses = ServiceConstants.SERVICES_GROUP_MAP.get(serviceCall);
		}

		return serviceClasses;
	}
	/*
	private void setQuickstartFlag(ServiceRequestProspr data) {
		data.setQuickStart(
				data.getLeadSource() == null ? Boolean.FALSE : data.getLeadSource().indexOf("QuickStart") > -1);
	}
	*/
}
