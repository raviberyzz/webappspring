package ca.sunlife.web.apps.cmsservice.service;

import javax.mail.MessagingException;
import java.util.Map;

import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFaa;
import ca.sunlife.web.apps.cmsservice.EmailConfig;


public interface EmailService {
	public String[] sendEmail(String body, Map<String,Object> serviceRequest) throws MessagingException;
	public String[] sendEmail(EmailConfig econfig, Map<String,Object> serviceRequest) throws MessagingException;
	
	//public String[] sendEmail(ServiceRequest serviceRequest) throws MessagingException;
    public String[] sendEmailProspr(Map<String,Object> serviceRequest) throws MessagingException;

    //public String[] sendEmailFaa(Map<String,Object> serviceRequest) throws MessagingException;

}
