package ca.sunlife.web.apps.cmsservice.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

/**
 * @author as42
 *
 */
@RestController

@RequestMapping(value = "/cms-service/")
@CrossOrigin
public class CmsServiceController {
	
	private static final Logger logger = LogManager.getLogger(CmsServiceController.class);
	
	@Autowired
    ApiGatewayService apiGatewayService;
	
    @PostMapping(value = "/submit")
    public CmsResponse submit(@RequestBody ServiceRequest data) throws JsonProcessingException {
    	logger.info("in CmsServiceController");
    	CmsResponse cmsresponse =null;    
    	String Validresponse = ServiceUtil.validateServiceRequest(data);
    	logger.info("Validresponse: " + Validresponse);
    	
    	if(Validresponse.equals("Success")) {
    		cmsresponse= apiGatewayService.sendData(data);
    	}else {
    		cmsresponse  = new CmsResponse();
    		cmsresponse.setMessage(Validresponse);
    		cmsresponse.setStatusCode(500);
    		logger.error(Validresponse);
    		logger.error("Invalid Request:{}",ServiceUtil.getJsonString(data) );
    		
    	}
    	return cmsresponse;
    	
    }
    
    @PostMapping(value = "form-submit", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public CmsResponse formSubmit(@RequestParam Map<String,String> paramMap) throws JsonProcessingException{
    	logger.info("In form-Submit");
    	ServiceUtil.validateFormField(paramMap);
    	return apiGatewayService.sendData(ServiceUtil.getServiceRequest(paramMap));
        
    }
}
