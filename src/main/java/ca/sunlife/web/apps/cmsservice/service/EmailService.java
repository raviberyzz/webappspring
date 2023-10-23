package ca.sunlife.web.apps.cmsservice.service;

import javax.mail.MessagingException;

import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;
import ca.sunlife.web.apps.cmsservice.model.FaaServiceRequest;


public interface EmailService {
    public String[] sendEmail(ServiceRequest serviceRequest) throws MessagingException;

    public String[] sendEmailFaa(FaaServiceRequest serviceRequest) throws MessagingException;

}
