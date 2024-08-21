package ca.sunlife.web.apps.cmsservice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.sunlife.web.apps.cmsservice.EmailConfig;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestProspr;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequestFaa;

@Service
public class EmailServiceImpl implements EmailService{

	private static Logger logger = LogManager.getLogger(EmailServiceImpl.class);
    private static String emailRegExForParam = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static String incorrectFormatMsg = "Incorrect format {}";
    private static String utf8 = "utf-8";

    @Autowired
    EmailConfig emailConfig;
    
    @Autowired
    TransportDelegator transport;
    
    @Override
    public String[] sendEmail(String body, Map<String,Object> serviceRequest) throws MessagingException {
    	emailConfig.setBody(body);
    	
        String[] success = {
                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}" };
        String[] failure = {
                "{ \"Status\" : \"Failure\", \"StatusCode\" : \"500\" ,\"StatusMsg\":\"Email not sent\"}" };
        logger.info("Smtp Host for email service is {}", emailConfig.getEmailProperties().get("mail.smtp.host"));
        if (sendEMailWithStatusAndCC(serviceRequest)) {
            return success;
        } else {
            return failure;
        }
    }

    @Override
    public String[] sendEmail(EmailConfig econfig, Map<String,Object> serviceRequest) throws MessagingException {
    	emailConfig = econfig;
    	
        String[] success = {
                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}" };
        String[] failure = {
                "{ \"Status\" : \"Failure\", \"StatusCode\" : \"500\" ,\"StatusMsg\":\"Email not sent\"}" };
        logger.info("Smtp Host for email service is {}", emailConfig.getEmailProperties().get("mail.smtp.host"));
        if (sendEMailWithStatusAndCC(serviceRequest)) {
            return success;
        } else {
            return failure;
        }
    }
    
    @Override
    //public String[] sendEmail(ServiceRequest serviceRequest) throws MessagingException {
    public String[] sendEmailProspr(Map<String,Object> serviceRequest) throws MessagingException {
        String[] success = {
                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}" };
        String[] failure = {
                "{ \"Status\" : \"Failure\", \"StatusCode\" : \"500\" ,\"StatusMsg\":\"Email not sent\"}" };
        logger.info("Smtp Host for email service is {}", emailConfig.getEmailProperties().get("mail.smtp.host"));
        if (sendEMailWithStatusAndCC(serviceRequest)) {
            return success;
        } else {
            return failure;
        }
    }
    
    //public boolean sendEMailWithStatusAndCC(ServiceRequest serviceRequest) throws MessagingException {
    public boolean sendEMailWithStatusAndCC(Map<String,Object> serviceRequest) throws MessagingException {
        boolean mailStatus = false;
        if (emailConfig.getFromAddress() != null && emailConfig.getToAddress() != null && serviceRequest != null) {
            try {
                 Session session =
                 Session.getInstance(emailConfig.getEmailProperties(), null);

                 Message msg = buildEmailData(new MimeMessage(session), serviceRequest);

                transport.sendEmail(msg);

                mailStatus = true;
                logger.info("Email has been sent");
            } catch (AddressException ae) {
                mailStatus = false;
                logger.error(ae);
                ae.printStackTrace();
            } catch (MessagingException me) {
                mailStatus = false;
                logger.error(me);
                me.printStackTrace();
            } catch (Exception e) {
                mailStatus = false;
                logger.error(e);
                e.printStackTrace();
            }
            return mailStatus;
        } else {
            logger.error("From/To field or the email body is null");
            return false;
        }
    }
    
    //public Message buildEmailData(MimeMessage msg, ServiceRequest serviceRequest)
    public Message buildEmailData(MimeMessage msg, Map<String,Object> serviceRequest)
    throws MessagingException, IOException {
        String headerType = null;
        
        msg.setFrom(getFromEmailAddress());

        if (emailConfig.getToAddress() instanceof String) {
            
            List<InternetAddress> addressTo = getEmailAddressList(emailConfig.getToAddress());
            if (!addressTo.isEmpty()) {
                msg.setRecipients(Message.RecipientType.TO, addressTo.toArray(new InternetAddress[addressTo.size()]));
            } else {
                logger.error("Email To field is invalid ");
            }

        }
        if (emailConfig.getCcAddress() instanceof String) {
            List<InternetAddress> addressCc = getEmailAddressList(emailConfig.getCcAddress());
            if (!addressCc.isEmpty()) {
                msg.setRecipients(Message.RecipientType.CC, addressCc.toArray(new InternetAddress[addressCc.size()]));
            }

        }
        if (emailConfig.getBccAddress() instanceof String) {
            List<InternetAddress> addressBcc = getEmailAddressList(emailConfig.getBccAddress());
            if (!addressBcc.isEmpty()) {
                msg.setRecipients(Message.RecipientType.BCC,
                        addressBcc.toArray(new InternetAddress[addressBcc.size()]));
            }
        }
        if (emailConfig.getSubject() instanceof String) {
            // Below code is to prevent Cross site scripting
            String sanitizedEmailSubject = preventXSS(emailConfig.getSubject());
            msg.setSubject(sanitizedEmailSubject, utf8);
        } else {
            logger.info("Email subject is empty ");
        }

        headerType = getContentType();
        msg.setHeader("Content-Type", headerType);
        
        MimeBodyPart mimeContent = new MimeBodyPart();
        mimeContent.setContent(preventXSS(emailConfig.getBody()), headerType);
        
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeContent);
           
        String emailBody = getAttachmentAsString(serviceRequest);
        if (emailBody != null && !emailBody.isEmpty()) {
            logger.debug("Decoding");
            multipart.addBodyPart(createAttachment(preventXSS(emailBody)));
        }
        msg.setSentDate(new Date());
        msg.setContent(multipart);
        
       return msg;
    }
/*    
    @Override
    //public String[] sendEmailFaa(FaaServiceRequest serviceRequest) throws MessagingException {
    public String[] sendEmailFaa(Map<String,Object> serviceRequest) throws MessagingException {
        String[] success = {
                "{ \"Status\" : \"Success\", \"StatusCode\" : \"200\" ,\"StatusMsg\":\"Email sent successfully\"}" };
        String[] failure = {
                "{ \"Status\" : \"Failure\", \"StatusCode\" : \"500\" ,\"StatusMsg\":\"Email not sent\"}" };
        logger.info("Smtp Host for email service is {}", emailConfig.getEmailProperties().get("mail.smtp.host"));
        if (sendEMailWithStatusAndCCFaa(serviceRequest)) {
            return success;
        } else {
            return failure;
        }
    }
    
    //public boolean sendEMailWithStatusAndCCFaa(FaaServiceRequest serviceRequest) throws MessagingException {
    public boolean sendEMailWithStatusAndCCFaa(Map<String,Object> serviceRequest) throws MessagingException {
        boolean mailStatus = false;
        if (emailConfig.getFromAddress() != null && emailConfig.getToAddressFaa() != null && serviceRequest != null) {
            try {
                 Session session =
                 Session.getInstance(emailConfig.getEmailProperties(), null);

                 Message msg = buildEmailDataFaa(new MimeMessage(session), serviceRequest);

                transport.sendEmail(msg);

                mailStatus = true;
                logger.info("Email has been sent");
            } catch (AddressException ae) {
                mailStatus = false;
                logger.error(ae);
                ae.printStackTrace();
            } catch (MessagingException me) {
                mailStatus = false;
                logger.error(me);
                me.printStackTrace();
            } catch (Exception e) {
                mailStatus = false;
                logger.error(e);
                e.printStackTrace();
            }
            return mailStatus;
        } else {
            logger.error("From/To field or the email body is null");
            return false;
        }
    }
    
    //public Message buildEmailDataFaa(MimeMessage msg, FaaServiceRequest serviceRequest)
    public Message buildEmailDataFaa(MimeMessage msg, Map<String,Object> serviceRequest)
    	    throws MessagingException, IOException {
    	        String headerType = null;
    	        
    	        msg.setFrom(getFromEmailAddressFaa());

    	        if (emailConfig.getToAddressFaa() instanceof String) {
    	            
    	            List<InternetAddress> addressTo = getEmailAddressList(emailConfig.getToAddressFaa());
    	            if (!addressTo.isEmpty()) {
    	            	logger.info("getToAddressFaa: " + addressTo.toString());
    	                msg.setRecipients(Message.RecipientType.TO, addressTo.toArray(new InternetAddress[addressTo.size()]));
    	            } else {
    	                logger.error("Email To field is invalid ");
    	            }

    	        }
    	        if (emailConfig.getCcAddressFaa() instanceof String) {
    	            List<InternetAddress> addressCc = getEmailAddressList(emailConfig.getCcAddressFaa());
    	            if (!addressCc.isEmpty()) {
    	            	logger.info("getCCAddressFaa: " + addressCc.toString());
    	                msg.setRecipients(Message.RecipientType.CC, addressCc.toArray(new InternetAddress[addressCc.size()]));
    	            }

    	        }
    	        if (emailConfig.getBccAddressFaa() instanceof String) {
    	            List<InternetAddress> addressBcc = getEmailAddressList(emailConfig.getBccAddressFaa());
    	            if (!addressBcc.isEmpty()) {
    	                msg.setRecipients(Message.RecipientType.BCC,
    	                        addressBcc.toArray(new InternetAddress[addressBcc.size()]));
    	            }
    	        }
    	        if (emailConfig.getSubjectFaa() instanceof String) {
    	            // Below code is to prevent Cross site scripting
    	            String sanitizedEmailSubject = preventXSS(emailConfig.getSubjectFaa());
    	            logger.info("sanitizedEmailSubject: " + sanitizedEmailSubject);
    	            msg.setSubject(sanitizedEmailSubject, utf8);
    	        } else {
    	            logger.info("Email subject is empty ");
    	        }

    	        headerType = getContentType();
    	        msg.setHeader("Content-Type", headerType);
    	        
    	        String emailClientDetails = getAttachmentAsStringFaa(serviceRequest);
    	        
    	        String emailBody = emailConfig.getBodyFaa() + "<br/><br/>Lead details below:<br/><br/>" + emailClientDetails.replaceAll("\n","<br/>");
    	        
    	        MimeBodyPart mimeContent = new MimeBodyPart();
    	        mimeContent.setContent(preventXSS(emailBody), headerType);
	            logger.info("getBodyFaa: " + emailConfig.getBodyFaa());    	        
    	        
    	        Multipart multipart = new MimeMultipart();
    	        multipart.addBodyPart(mimeContent);
    	           
    	        logger.info("emailBody: " + emailBody);
    	        if (emailBody != null && !emailBody.isEmpty()) {
    	            logger.debug("Decoding");
    	            multipart.addBodyPart(createAttachment(preventXSS(emailClientDetails)));
    	        }
    	        msg.setSentDate(new Date());
    	        msg.setContent(multipart);
    	        
    	       return msg;
    	    }
*/
    private static boolean isValidStringWithRegex(String param, String regex) {
        if (regex == null || regex.length() == 0) {
            return true;
        }
        if (param == null || param.length() == 0) {
            return false;
        }
        return param.matches(regex);
    }

    private static String preventXSS(String content) {
        logger.info("Content is {}", content);
        // encodedContent is <\/script> which shows as <\\/script> in string form.
        // Therefore four backward slashes are used in regex pattern.
        content = content.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("(?i)%3Cscript.*?%3E.*?%3C/script.*?%3E", "");
        logger.info("Content after replacing is {}", content);
        return content;
    }

    public String getAttachmentAsString(Map<String,Object> serviceRequest) {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String,Object> entry : serviceRequest.entrySet()) {
            strBuilder.append(entry.getKey());
            strBuilder.append("=");
            strBuilder.append(entry.getValue());
            strBuilder.append(",\n");
        }
        int lIndex = strBuilder.lastIndexOf(",");
        if (lIndex > -1) {
            strBuilder.delete(lIndex, lIndex + 1);
        }
        return strBuilder.toString();
    }
    
    public String getAttachmentAsString(ServiceRequestProspr serviceRequest) {
        ObjectMapper oMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = oMapper.convertValue(serviceRequest, Map.class);
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            strBuilder.append(entry.getKey());
            strBuilder.append("=");
            strBuilder.append(entry.getValue());
            strBuilder.append(",\n");
        }
        int lIndex = strBuilder.lastIndexOf(",");
        if (lIndex > -1) {
            strBuilder.delete(lIndex, lIndex + 1);
        }
        return strBuilder.toString();
    }
   
    private String getAttachmentAsStringFaa(Map<String,Object> serviceRequest) {
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String,Object> entry : serviceRequest.entrySet()) {
            strBuilder.append(entry.getKey());
            strBuilder.append("=");
            strBuilder.append(entry.getValue());
            strBuilder.append(",\n");
        }
        int lIndex = strBuilder.lastIndexOf(",");
        if (lIndex > -1) {
            strBuilder.delete(lIndex, lIndex + 1);
        }
        return strBuilder.toString();
    }
    
    private String getAttachmentAsStringFaa(ServiceRequestFaa serviceRequest) {
        ObjectMapper oMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = oMapper.convertValue(serviceRequest, Map.class);
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            strBuilder.append(entry.getKey());
            strBuilder.append("=");
            strBuilder.append(entry.getValue());
            strBuilder.append(",\n");
        }
        int lIndex = strBuilder.lastIndexOf(",");
        if (lIndex > -1) {
            strBuilder.delete(lIndex, lIndex + 1);
        }
        return strBuilder.toString();
    }
	
    private MimeBodyPart createAttachment(String attachStr) throws IOException, MessagingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(attachStr.getBytes());
        byte[] byteData = out.toByteArray();
        byte[] base64ByteArray = java.util.Base64.getEncoder().encode(byteData);

        InternetHeaders fileHeaders = new InternetHeaders();
        fileHeaders.setHeader("Content-Transfer-Encoding", "base64");

        // Body content
        MimeBodyPart mimeContent = new MimeBodyPart();
        mimeContent.setContent("Email content here", "text/html;charset=\"utf-8\"");

        // Attachment
        MimeBodyPart mimeAttachment = new MimeBodyPart(fileHeaders, base64ByteArray);
        mimeAttachment.setFileName("faalead.txt");
        return mimeAttachment;
    }
    
    private List<InternetAddress> getEmailAddressList(String emailAddressStr) throws AddressException{
        List<InternetAddress> emailAddressList = new ArrayList<>();
        if (emailConfig.getToAddress() instanceof String) {
            String[] recipientsTo = (emailAddressStr).split(";");
            for (int i = 0; i < recipientsTo.length; i++) {
                if (isValidStringWithRegex(recipientsTo[i], emailRegExForParam)) {
                    emailAddressList.add(new InternetAddress(recipientsTo[i]));
                } else {
                    logger.error(incorrectFormatMsg, recipientsTo[i]);
                }
            }
        }
        return emailAddressList;
    }
    
    private String getContentType() {
        String mailType = emailConfig.getMailType();
        if (mailType == null || mailType.isEmpty())
            mailType = "html";
        String charset = emailConfig.getCharSet();
        if (charset == null || charset.isEmpty())
            charset = "iso-8859-1";
        return "text/" + mailType + ";charset=\"" + charset + "\"";
    }
    
    private InternetAddress getFromEmailAddress() throws UnsupportedEncodingException, AddressException {
        InternetAddress internetAddress=null;
        if (emailConfig.getFromText() instanceof String) {
            String sanitizedFromText = preventXSS(emailConfig.getFromText());
            internetAddress = new InternetAddress(emailConfig.getFromAddress(), sanitizedFromText, utf8);
        } else {
            internetAddress = new InternetAddress(emailConfig.getFromAddress());
        }

        logger.info("fromText: " + emailConfig.getFromText());
        logger.info("fromAddress: " + emailConfig.getFromAddress());
        return internetAddress;

    }
/*    
    private InternetAddress getFromEmailAddressFaa() throws UnsupportedEncodingException, AddressException {
        InternetAddress internetAddress=null;
        if (emailConfig.getFromTextFaa() instanceof String) {
            String sanitizedFromText = preventXSS(emailConfig.getFromTextFaa());
            internetAddress = new InternetAddress(emailConfig.getFromAddress(), sanitizedFromText, utf8);
        } else {
            internetAddress = new InternetAddress(emailConfig.getFromAddress());
        }

        logger.info("fromTextFaa: " + emailConfig.getFromTextFaa());
        logger.info("fromAddress: " + emailConfig.getFromAddress());
        return internetAddress;

    }
*/
}
