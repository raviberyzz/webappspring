package ca.sunlife.web.apps.cmsservice.model;

public class FaaServiceRequest {

	private String firstName;

	private String lastName;
	
	private String preferredPhone;

	private String homeAddressPostalCode;
	
	private String email;
	
	private String headOfficeLeadSource;

	private String leadReceivedDateTime;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPreferredPhone() {
		return preferredPhone;
	}

	public void setPreferredPhone(String preferredPhone) {
		this.preferredPhone = preferredPhone;
	}

	public String getHomeAddressPostalCode() {
		return homeAddressPostalCode;
	}

	public void setHomeAddressPostalCode(String homeAddressPostalCode) {
		this.homeAddressPostalCode = homeAddressPostalCode;
	}

	public String getHeadOfficeLeadSource() {
		return headOfficeLeadSource;
	}

	public void setHeadOfficeLeadSource(String headOfficeLeadSource) {
		this.headOfficeLeadSource = headOfficeLeadSource;
	}

	public String getLeadReceivedDateTime() {
		return leadReceivedDateTime;
	}

	public void setLeadReceivedDateTime(String leadReceivedDateTime) {
		this.leadReceivedDateTime = leadReceivedDateTime;
	}
}
