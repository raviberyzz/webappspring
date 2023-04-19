package ca.sunlife.web.apps.cmsservice.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.sunlife.web.apps.cmsservice.model.CmsResponse;
import ca.sunlife.web.apps.cmsservice.model.FaaServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.service.ApiGatewayService;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

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

	@Value("redirect.url")
	private String retURL;

	@PostMapping(value = "/submit")
	public CmsResponse submit(@RequestBody FaaServiceRequest data) throws JsonProcessingException {
		logger.info("in CmsServiceController");
		CmsResponse cmsresponse = null;

//		String validResponse = ServiceUtil.validateServiceRequest(data);
//		setQuickstartFlag(data);
//		logger.info("Validresponse: {}", validResponse);
		
		cmsresponse = apiGatewayService.sendDataFaa(data);

//		if (validResponse.equals("Success")) {
//			cmsresponse = apiGatewayService.sendData(data);
//		} else {
//			cmsresponse = new CmsResponse();
//			cmsresponse.setMessage(validResponse);
//			cmsresponse.setStatusCode(500);
//			logger.error(validResponse);
//			logger.error("Invalid Request:{}", ServiceUtil.getJsonString(data));
//		}
		return cmsresponse;
	}

	@PostMapping(value = "form-submit", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public CmsResponse formSubmit(@RequestParam Map<String, String> paramMap) throws JsonProcessingException {

		logger.info("In form-Submit");

		ServiceUtil.validateFormField(paramMap);
		return apiGatewayService.sendData(ServiceUtil.getServiceRequest(paramMap));
	}

	private void setQuickstartFlag(ServiceRequest data) {
		data.setQuickStart(
				data.getLeadSource() == null ? Boolean.FALSE : data.getLeadSource().indexOf("QuickStart") > -1);
	}
}
