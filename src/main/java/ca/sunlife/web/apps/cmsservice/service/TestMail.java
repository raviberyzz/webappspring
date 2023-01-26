package ca.sunlife.web.apps.cmsservice.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class TestMail {

   public static void main(String [] args) {    
      // Recipient's email ID needs to be mentioned.
      String to = "uma.maheshvaran@sunlife.comn";

      // Sender's email ID needs to be mentioned
      String from = "uma.maheshvaran@sunlife.com";

      // Assuming you are sending email from localhost
      String host = "smtp.ca.sunlife";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.host", host);

      // Get the default Session object.
      Session session = Session.getInstance(properties,null);

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("This is the Subject Line!");

         // Now set the actual message
         message.setText("This is actual message");

         // Send message
         Transport.send(message);
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
}