package ca.sunlife.web.apps.cmsservice.service;

import javax.mail.MessagingException;

import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;

public interface EmailService {
    public String[] sendEmail(ServiceRequest serviceRequest) throws MessagingException;
}
