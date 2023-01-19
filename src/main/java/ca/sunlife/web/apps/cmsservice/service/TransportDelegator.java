package ca.sunlife.web.apps.cmsservice.service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import org.springframework.stereotype.Service;

@Service
public class TransportDelegator {
    public void sendEmail(Message msg)
            throws MessagingException {
        Transport.send(msg);
    }
}

