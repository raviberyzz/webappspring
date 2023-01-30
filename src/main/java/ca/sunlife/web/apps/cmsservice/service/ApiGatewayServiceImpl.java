package ca.sunlife.web.apps.cmsservice.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import ca.sunlife.web.apps.cmsservice.authentication.OktaTokenGenerator;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.restclient.SalesforceClient;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;
import ca.sunlife.web.apps.cmsservice.model.CmsResponse;


@Service
public class ApiGatewayServiceImpl implements ApiGatewayService {


	@Autowired
    OktaTokenGenerator oktaTokenGenerator;
    
    @Autowired
    EmailService emailService;
    
    @Autowired
    SalesforceClient salesforceClient;
	
    private static final Logger logger = LogManager.getLogger(ApiGatewayServiceImpl.class);

    @Override
    public CmsResponse sendData(ServiceRequest data) throws JsonProcessingException {
        CmsResponse cmsResponse = null;
        boolean isValidToken = authenticateToken();
        if (isValidToken) {
            data.setId(generateUid(data));

            HttpHeaders header = new HttpHeaders();
            header.add("Accept", MediaType.APPLICATION_JSON_VALUE);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("data", data);
            HttpEntity<String> request = new HttpEntity<>(ServiceUtil.getSalesForceJsonString(data), header);
            cmsResponse = salesforceClient.postData(request);        
        }
        
        if(cmsResponse != null && cmsResponse.getStatusCode() == HttpStatus.OK.value()) {
            cmsResponse.setMessage("data successfully submitted");
            logger.info(cmsResponse.getMessage());
        }else {
            cmsResponse = new CmsResponse();
            cmsResponse.setMessage(isValidToken ? "Something went wrong!" : "Something went wrong!  URL not valid");
            cmsResponse.setStatusCode(500);
            logger.error(cmsResponse.getMessage());
            sendEmail(data);
        }        
        return cmsResponse;
    }

    private boolean authenticateToken() {
        CmsResponse cmsResponse = null;
        String token = oktaTokenGenerator.generateToken();

        if (token != null) {
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);
            header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            header.add("Accept", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<String> request = new HttpEntity<>(header);
            
            cmsResponse = salesforceClient.validateToken(request);    
        }
        return cmsResponse != null && cmsResponse.getStatusCode() == 200;
    }

	private String generateUid(ServiceRequest data) {
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("ddMMyyhhmmssMs");
		String randomId = data.getLeadSource() + ft.format(date);
		logger.info("Uid:{}", randomId);
		return randomId;

	}

	private void sendEmail(ServiceRequest data) {
        try {
            emailService.sendEmail(data);
            logger.info("Email sent successfully");
        }catch(MessagingException ex) {
        	logger.error("Email failed: {}", ex.getMessage());
        }        
    }


}
