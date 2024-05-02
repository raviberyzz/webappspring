package ca.sunlife.web.apps.cmsservice.model;

public class CommunicationServiceRequest {

	private String emailAddress;
	
	private String templateId;

	private String language;
	
	private String firstName;

	private String coverageAmount;
	
	private String costRange;

	private String incomeReplacement;
	
	private String debtPayment;
	
	private String additionalExpense;

	private String totalEstimate;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(String coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public String getCostRange() {
		return costRange;
	}

	public void setCostRange(String costRange) {
		this.costRange = costRange;
	}

	public String getIncomeReplacement() {
		return incomeReplacement;
	}

	public void setIncomeReplacement(String incomeReplacement) {
		this.incomeReplacement = incomeReplacement;
	}

	public String getDebtPayment() {
		return debtPayment;
	}

	public void setDebtPayment(String debtPayment) {
		this.debtPayment = debtPayment;
	}

	public String getAdditionalExpense() {
		return additionalExpense;
	}

	public void setAdditionalExpense(String additionalExpense) {
		this.additionalExpense = additionalExpense;
	}

	public String getTotalEstimate() {
		return totalEstimate;
	}

	public void setTotalEstimate(String totalEstimate) {
		this.totalEstimate = totalEstimate;
	}

	
}
