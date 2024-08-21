package ca.sunlife.web.apps.cmsservice;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {
	
	
	@Value("${mail.smtp.host}")
    private String emailHost;
    @Value("${mail.smtp.port}")
    private String emailPort;
    @Value("${mail.smtp.auth}")
    private boolean isAuth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean isStarttlsEnabled;
    @Value("${mail.smtp.username}")
    private String username;
    @Value("${mail.smtp.password}")
    private String password;
    
    @Value("${mail.slf.fromtext}")
    private String fromText;
    @Value("${mail.slf.subject}")
    private String subject;
    @Value("${mail.slf.body}")
    private String body;
    @Value("${mail.slf.fromaddress}")
    private String fromAddress;
    @Value("${mail.slf.toaddress}")
    private String toAddress;
    @Value("${mail.slf.ccaddress}")
    private String ccAddress;
    @Value("${mail.slf.bccaddress}")
    private String bccAddress;
    @Value("${mail.slf.mailtype}")    
    private String mailType;
    @Value("${mail.slf.charset}")
    private String charSet;
    
//  FAA Email values
/*
    @Value("${mail.slf.fromtext.faa}")
    private String fromTextFaa;
	@Value("${mail.slf.subject.faa}")
    private String subjectFaa;
    @Value("${mail.slf.body.faa}")
    private String bodyFaa;
    @Value("${mail.slf.toaddress.faa}")
    private String toAddressFaa;
    @Value("${mail.slf.ccaddress.faa}")
    private String ccAddressFaa;
    @Value("${mail.slf.bccaddress.faa}")
    private String bccAddressFaa;
*/

    @Bean("emailProperties")
    public Properties getEmailProperties() {
        Properties props = new Properties();
        props.put("mail.host", emailHost);

        return props;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getFromText() {
        return fromText;
    }

    public void setFromText(String fromText) {
        this.fromText = fromText;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }
    
/*
    public String getFromTextFaa() {
		return fromTextFaa;
	}

	public void setFromTextFaa(String fromTextFaa) {
		this.fromTextFaa = fromTextFaa;
	}

	public String getSubjectFaa() {
		return subjectFaa;
	}

	public void setSubjectFaa(String subjectFaa) {
		this.subjectFaa = subjectFaa;
	}

	public String getBodyFaa() {
		return bodyFaa;
	}

	public void setBodyFaa(String bodyFaa) {
		this.bodyFaa = bodyFaa;
	}

	public String getToAddressFaa() {
		return toAddressFaa;
	}

	public void setToAddressFaa(String toAddressFaa) {
		this.toAddressFaa = toAddressFaa;
	}

	public String getCcAddressFaa() {
		return ccAddressFaa;
	}

	public void setCcAddressFaa(String ccAddressFaa) {
		this.ccAddressFaa = ccAddressFaa;
	}
	
	public String getBccAddressFaa() {
		return bccAddressFaa;
	}

	public void setBccAddressFaa(String bccAddressFaa) {
		this.bccAddressFaa = bccAddressFaa;
	}
*/

}
