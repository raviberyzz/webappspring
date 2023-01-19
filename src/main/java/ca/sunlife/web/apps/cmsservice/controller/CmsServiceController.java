package ca.sunlife.web.apps.cmsservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class CmsServiceController {
	
	private static final Logger logger = LogManager.getLogger(CmsServiceController.class);
	
	@Autowired
    ApiGatewayService apiGatewayService;
	
    @PostMapping(value = "/submit")
    public CmsResponse submit(@RequestBody ServiceRequest data) throws JsonProcessingException {
    	CmsResponse cmsresponse =null;    
    	boolean isValid = ServiceUtil.validateServiceRequest(data);
    	if(isValid) {
    		cmsresponse= apiGatewayService.sendData(data);
    	}else {
    		cmsresponse  = new CmsResponse();
    		cmsresponse.setMessage("Invalid Request");
    		cmsresponse.setStatusCode(500);
    		logger.error("Invalid Request:{}",ServiceUtil.getJsonString(data) );
    	}
    	return cmsresponse;
    	
    }
}
